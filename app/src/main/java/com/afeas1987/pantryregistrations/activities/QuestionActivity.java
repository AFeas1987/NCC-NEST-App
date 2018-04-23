package com.afeas1987.pantryregistrations.activities;
/*
 * Copyright 2018 AFeas1987
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.afeas1987.pantryregistrations.R;
import com.afeas1987.pantryregistrations.fragments.QuestionFragment;
import com.afeas1987.pantryregistrations.fragments.ResultsFragment;
import com.afeas1987.pantryregistrations.fragments.ValidationFragment;
import com.afeas1987.pantryregistrations.utilities.PantryGuest;
import com.afeas1987.pantryregistrations.utilities.SurveyQuestion;

import java.util.Arrays;
import java.util.BitSet;

import static com.afeas1987.pantryregistrations.utilities.Constants.QUESTIONS;

public class QuestionActivity extends AbstractActivity {

    public int q_number;
    public final Object[] RESULTS = new Object[QUESTIONS.length + 2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        q_number = 1;
        Log.d("DEBUG", String.format("~~~~~~~~~~~~~ Questions.length: %d, RESULTS.length: %d, QNUM: %d", QUESTIONS.length, RESULTS.length, q_number));
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.frame_layout_main, new ValidationFragment()).commit();
    }


    public void onFragmentEnd() {
        if (++q_number < QUESTIONS.length + 2)
            nextQuestionFragment();
        else {
            --q_number;
            Bundle b = new Bundle();
            b.putString("EMAIL", createGuest());
            ResultsFragment fragment = new ResultsFragment();
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.frame_layout_main, fragment).commit();
        }
    }


    private void nextQuestionFragment() {
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(packQuestionData());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.frame_layout_main, fragment).addToBackStack(null).commit();

//        Toast.makeText(getApplicationContext(), "Back stack " + getFragmentManager().getBackStackEntryCount(), Toast.LENGTH_LONG).show();
    }


    private Bundle packQuestionData() {
        Bundle bundle = new Bundle();
        SurveyQuestion question = QUESTIONS[q_number - 2];
        bundle.putIntArray("CONSTRAINTS", question.constraints);
        bundle.putInt("TYPE", question.type);
        Integer min = question.min;
        if (min != null) {
            bundle.putInt("MIN", min);
            bundle.putInt("MAX", question.max);
        }
        bundle.putInt("REQUIRED", question.required ? 1 : 0);
        String[] options = question.options;
        if (options != null)
            bundle.putStringArray("OPTIONS", options);
        String title = question.title;
        if (title != null)
            bundle.putString("TITLE", title);
        String prompt = question.prompt;
        if (prompt != null)
            bundle.putString("PROMPT", prompt);
        String hint = question.hint;
        if (hint != null)
            bundle.putString("HINT", hint);
        String mReq = question.msgRequired;
        if (mReq != null)
            bundle.putString("MSG_REQUIRED", mReq);
        String mCnstr = question.msgConstraint;
        if (mCnstr != null)
            bundle.putString("MSG_CONSTRAINT", mCnstr);
        return bundle;
    }


    public void showPinDialog(String email) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_pin, null);
        EditText pinEdit = dialogView.findViewById(R.id.edit_pin);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pin creation")
                .setMessage("Create a pin number")
                .setView(dialogView)
                .setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(alertView -> {
            if (QuestionActivity.this.isValidPin(pinEdit.getText())) {
                RESULTS[0] = email;
                RESULTS[1] = String.valueOf(pinEdit.getText().toString().hashCode());
                QuestionActivity.this.onFragmentEnd();
                dialog.dismiss();
            } else
                Toast.makeText(dialog.getContext(), "Invalid pin", Toast.LENGTH_LONG).show();
        });
    }


    private boolean isValidPin(CharSequence target) {
        Integer i = null;
        try {
            i = Integer.parseInt(target.toString());
        }
        catch (NumberFormatException ex) {
            Log.d("DEBUG", ex.getMessage());
        }
        return i != null && i >= 0 && target.toString().length() == 4;
    }


    @Override
    public void onBackPressed() {
        getFragmentManager().beginTransaction().addToBackStack(null).commit();
        if (--q_number == 0)
            finish();
        else
            super.onBackPressed();
    }


    public String createGuest() {
        Log.d("DEBUG", "~~~~~~~~~~~~~~~~  RESULTS = \n" + Arrays.deepToString(RESULTS));
        String[] address = (String[]) RESULTS[5];
        PantryGuest guest = new PantryGuest((String)RESULTS[0], (String)RESULTS[1]);
        guest   .setLastName(RESULTS[2])                .setFirstName(RESULTS[3])
                .setPhone(RESULTS[4])                   .setStreet(address[0])
                .setCity(address[1])                    .setState(address[2])
                .setZip(address[3])                     .setSchoolID(RESULTS[6])
                .setGender(RESULTS[7])                  .setAge(RESULTS[8])
                .setHouseholdSize(RESULTS[9])           .setIncome(RESULTS[10])
                .setFoodStamps(RESULTS[11])             .setFoodPrograms(RESULTS[12])
                .setStatusEmploy(multiSelectData(13))   .setStatusHealth(multiSelectData(14))
                .setStatusHousing(multiSelectData(15))  .setStatusChild(multiSelectData(16))
                .setChildUnder1(RESULTS[17])            .setChild1to5(RESULTS[18])
                .setChild6to12(RESULTS[19])             .setChild13to18(RESULTS[20])
                .setDietNeeds(multiSelectData(21))      .setFoundFrom(multiSelectData(22))
                .setComments(RESULTS[23])               .setHelpedBy(RESULTS[24]);
        this.realm.executeTransaction(realm -> realm.insert(guest));
        Log.d("DEBUG", "~~~~~~~~~~~~~~~  " + guest);
        return guest.getEmail();
    }


    private String multiSelectData(int index) {
        String[] opts = QUESTIONS[index - 2].options;
        BitSet set = (BitSet) RESULTS[index];
        if (set.get(0))
            return opts[opts.length - 1];
        StringBuilder str = new StringBuilder();
        for (int i = 1; i <= opts.length; i++)
            if (set.get(i))
                str.append(opts[i - 1]).append(", ");
        return str.length() < 2 ? "" : str.substring(0, str.length() - 2);
    }
}
