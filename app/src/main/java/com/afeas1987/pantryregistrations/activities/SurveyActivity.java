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
package com.afeas1987.pantryregistrations.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.afeas1987.pantryregistrations.utilities.Constants.SurveyType;
import com.afeas1987.pantryregistrations.utilities.PantryGuest;
import com.afeas1987.pantryregistrations.utilities.PantryVolunteer;
import com.afeas1987.pantryregistrations.utilities.SurveyQuestion;

import static com.afeas1987.pantryregistrations.utilities.Constants.GUEST_QUESTIONS;
import static com.afeas1987.pantryregistrations.utilities.Constants.VOLUNTEER_QUESTIONS;


public class SurveyActivity extends AbstractActivity {

    public int q_number, surveyType;
    public SurveyQuestion[] QUESTIONS;
    public Object[] RESULTS;
    Fragment currentFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initializeSurvey();
        if (savedInstanceState == null) {
            q_number = 1;
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    .add(R.id.frame_layout_main,
                            currentFrag = new ValidationFragment(),
                            String.valueOf(q_number))
                    .commit();
        } else {
            unpackResults(savedInstanceState);
            q_number = savedInstanceState.getInt("Q_NUM");
            String tag = (q_number == QUESTIONS.length) ? "RESULTS_FRAG" :
                    (q_number < 2) ? "1" : String.valueOf(q_number);
            currentFrag = getSupportFragmentManager()
                    .getFragment(savedInstanceState, tag);
        }
    }

    private void initializeSurvey() {
        surveyType = getIntent().getIntExtra("SURVEY_TYPE", -1);
        switch (surveyType) {
            case SurveyType.GUEST:
                QUESTIONS = GUEST_QUESTIONS;
                loginToRealmAsync("guests");
                break;
            case SurveyType.VOLUNTEER:
                QUESTIONS = VOLUNTEER_QUESTIONS;
                loginToRealmAsync("personnel");
                break;
        }
        RESULTS = new Object[QUESTIONS.length];
    }


    private void unpackResults(Bundle savedInstanceState) {
        for (int i = 0; i < RESULTS.length; i++) {
            String tag = "RES_" + String.valueOf(i);
            if (!savedInstanceState.containsKey(tag))
                continue;
            Object obj = savedInstanceState.get(tag);
            if (obj != null)
                RESULTS[i] = (obj.getClass().isArray()) ?
                        (String[]) obj : (String) obj;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        String tag = (q_number == QUESTIONS.length) ? "RESULTS_FRAG" :
                (q_number < 2) ? "1" : String.valueOf(q_number);
        getSupportFragmentManager().putFragment(savedState, tag, currentFrag);
        savedState.putInt("Q_NUM", q_number);
        packResults(savedState);
    }


    private void packResults(Bundle savedState) {
        for (int i = 0; i < RESULTS.length; i++)
            if (RESULTS[i] != null) {
                Object obj = RESULTS[i];
                String tag = "RES_" + String.valueOf(i);
                if (obj.getClass() == String.class) {
                    if ("".equals(obj))
                        continue;
                    savedState.putString(tag, (String) obj);
                }
                else
                    savedState.putStringArray(tag, (String[]) obj);
            }
    }


    public void onFragmentEnd() {
        if (++q_number < QUESTIONS.length)
            nextQuestionFragment();
        else {
            Bundle b = new Bundle();
            b.putString("EMAIL", surveyType == 0 ? createGuest() : createVolunteer());
            ResultsFragment fragment = new ResultsFragment();
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right)
                    .replace(R.id.frame_layout_main,
                            currentFrag = fragment, "RESULTS_FRAG")
                    .commit();
        }
    }


    private void nextQuestionFragment() {
        QuestionFragment fragment = new QuestionFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right)
                .replace(R.id.frame_layout_main,
                        currentFrag = fragment,
                        String.valueOf(q_number))
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (--q_number == 0)
            finish();
        else {
            getFragmentManager().beginTransaction().addToBackStack(null)
                    .commit();
            currentFrag = getSupportFragmentManager()
                    .findFragmentByTag(q_number < 2 ?
                            "1" : String.valueOf(q_number));
            super.onBackPressed();
        }
    }


    public void showPinDialog(String email) {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_pin, null);
        EditText pinEdit = dialogView.findViewById(R.id.edit_pin);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pin creation")
                .setMessage("Create a pin number")
                .setView(dialogView)
                .setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(alertView -> {
                    if (SurveyActivity.this.isValidPin(pinEdit.getText())) {
                        RESULTS[0] = email;
                        RESULTS[1] = String.valueOf(
                                pinEdit.getText().toString().hashCode());
                        onFragmentEnd();
                        dialog.dismiss();
                    } else
                        Toast.makeText(dialog.getContext(),
                                "Invalid pin", Toast.LENGTH_LONG).show();
                });
    }


    private boolean isValidPin(CharSequence target) {
        Integer i = null;
        try {
            i = Integer.parseInt(target.toString());
        } catch (NumberFormatException ex) {
            Log.d("DEBUG", ex.getMessage());
        }
        return i != null && i >= 0 && target.toString().length() == 4;
    }


    private String createGuest() {
        String[] address = (String[]) RESULTS[5];
        PantryGuest guest = new PantryGuest((String) RESULTS[0], (String) RESULTS[1]);
        guest.setLastName(RESULTS[2]).setFirstName(RESULTS[3])
                .setPhone(RESULTS[4]).setSchoolID(RESULTS[6])
                .setGender(RESULTS[7]).setAge(RESULTS[8])
                .setHouseholdSize(RESULTS[9]).setIncome(RESULTS[10])
                .setFoodStamps(RESULTS[11]).setFoodPrograms(RESULTS[12])
                .setStatusEmploy(RESULTS[13]).setStatusHealth(RESULTS[14])
                .setStatusHousing(RESULTS[15]).setStatusChild(RESULTS[16])
                .setChildUnder1(RESULTS[17]).setChild1to5(RESULTS[18])
                .setChild6to12(RESULTS[19]).setChild13to18(RESULTS[20])
                .setDietNeeds(RESULTS[21]).setFoundFrom(RESULTS[22])
                .setComments(RESULTS[23]).setHelpedBy(RESULTS[24]);
        if (address != null)
            guest.setStreet(address[0]).setCity(address[1])
                    .setState(address[2]).setZip(address[3]);
        this.realm.executeTransaction(realm -> realm.insert(guest));
        return guest.getEmail();
    }


    private String createVolunteer() {
        String[] address = (String[]) RESULTS[5];
        PantryVolunteer volunteer = new PantryVolunteer(
                (String)RESULTS[0], (String)RESULTS[1], true);
        volunteer   .setLastName(RESULTS[2]).setFirstName(RESULTS[3])
                .setPhone(RESULTS[4]).setSchoolID(RESULTS[6]);
        if (address != null)
            volunteer.setStreet(address[0]).setCity(address[1])
                    .setState(address[2]).setZip(address[3]);
        this.realm.executeTransaction(realm -> realm.insert(volunteer));
        return volunteer.getEmail();
    }
}