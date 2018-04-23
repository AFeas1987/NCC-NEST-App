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
package com.afeas1987.pantryregistrations.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afeas1987.pantryregistrations.R;
import com.afeas1987.pantryregistrations.activities.SurveyActivity;
import com.afeas1987.pantryregistrations.utilities.PantryGuest;
import com.afeas1987.pantryregistrations.utilities.SurveyQuestion;

import java.util.Arrays;
import java.util.BitSet;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.afeas1987.pantryregistrations.utilities.Constants.Constraint.NCC_ID;
import static com.afeas1987.pantryregistrations.utilities.Constants.Constraint.PHONE_NUMBER;
import static com.afeas1987.pantryregistrations.utilities.Constants.QuestionType.ADDRESS;
import static com.afeas1987.pantryregistrations.utilities.Constants.QuestionType.NUMBER_ENTRY;
import static com.afeas1987.pantryregistrations.utilities.Constants.QuestionType.SELECT_MULTI;
import static com.afeas1987.pantryregistrations.utilities.Constants.QuestionType.SELECT_NUMBER;
import static com.afeas1987.pantryregistrations.utilities.Constants.QuestionType.SELECT_ONE;
import static com.afeas1987.pantryregistrations.utilities.Constants.QuestionType.TEXT;

public class QuestionFragment extends Fragment {

    private SurveyQuestion question;
    private SurveyActivity mActivity;
    private EditText mResponse;
    private RadioGroup group;
    private NumberPicker picker;
    private CheckBox[] checks;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (SurveyActivity) getActivity();
        question = mActivity.QUESTIONS[mActivity.q_number];
        return inflater.inflate(getTypeLayout(), container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView mTitle = mActivity.findViewById(R.id.txt_question_title);
        TextView mPrompt = mActivity.findViewById(R.id.txt_question_prompt);
        Button mButton = mActivity.findViewById(R.id.btn_next_question);
        mTitle.setText(question.title);
        mPrompt.setText(question.prompt);
        initializeQuestion();
        mButton.setOnClickListener(v -> processResponse());
    }


    private int getTypeLayout() {
        switch (question.type){
            case SELECT_NUMBER:
            case SELECT_ONE:
            case SELECT_MULTI:
                return R.layout.fragment_question_select;
            case ADDRESS:
                return R.layout.fragment_question_address;
            default:
                return R.layout.fragment_question_default;
        }
    }


    private void initializeQuestion() {
        Object prevResult = mActivity.RESULTS[mActivity.q_number];
        boolean existPrevRes = prevResult != null;
        switch (question.type) {
            case SELECT_ONE:
                group = mActivity.findViewById(R.id.radio_question_select);
                for (String s : question.options) {
                    RadioButton r = new RadioButton(mActivity);
                    r.setText(s);
                    r.setTextColor(Color.WHITE);
                    r.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    r.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                    group.addView(r);
                    if (existPrevRes && s.equalsIgnoreCase((String) prevResult))
                        r.setChecked(true);
                }
                group.setVisibility(View.VISIBLE);
                break;
            case SELECT_MULTI:
                checks = new CheckBox[question.options.length + 1];
                LinearLayout layout = mActivity.findViewById(R.id.linear_question_select);
                layout.setHorizontalGravity(Gravity.START);
                int i = 0;
                for (String s : question.options) {
                    CheckBox c = new CheckBox(mActivity);
                    c.setText(s);
                    c.setTextColor(Color.WHITE);
                    c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    c.setLayoutParams(new LayoutParams(MATCH_PARENT, WRAP_CONTENT));
                    checks[s.equalsIgnoreCase(getString(R.string.str_decline)) ? 0 : ++i] = c;
                    layout.addView(c);
                    if (existPrevRes &&
                            Arrays.asList(((String) prevResult).split(", ")).contains(s))
                        c.setChecked(true);
                }
                break;
            case SELECT_NUMBER:
                layout = mActivity.findViewById(R.id.linear_question_select);
                picker = new NumberPicker(mActivity);
                picker.setMinValue(question.min);
                picker.setMaxValue(question.max);
                picker.setBackgroundColor(Color.WHITE);
                picker.setLayoutParams(new LayoutParams(WRAP_CONTENT, MATCH_PARENT));
                layout.addView(picker);
                if (existPrevRes)
                    picker.setValue(Integer.parseInt((String)prevResult));
                break;
            case NUMBER_ENTRY:
                mResponse = mActivity.findViewById(R.id.edit_question_response);
                mResponse.setHint(question.hint);
                mResponse.setInputType(TYPE_CLASS_NUMBER);
                if (existPrevRes)
                    mResponse.setText((String)prevResult);
                break;
            case ADDRESS:
                Spinner spinner = mActivity.findViewById(R.id.spinner_address_state);
                spinner.setSelection(31, false);
                if (existPrevRes && prevResult.getClass().isArray()) {
                    String[] addressult = (String[]) prevResult;
                    EditText street = mActivity.findViewById(R.id.edit_address_street);
                    EditText city = mActivity.findViewById(R.id.edit_address_city);
                    EditText zip = mActivity.findViewById(R.id.edit_address_zip);
                    street.setText(addressult[0]);
                    city.setText(addressult[1]);
                    zip.setText(addressult[3]);
                    int count = spinner.getAdapter().getCount();
                    for (int j = 0; j < count; j++) {
                        String s = (String)spinner.getAdapter().getItem(j);
                        if (s.equals(addressult[2])) {
                            spinner.setSelection(j);
                            break;
                        }
                    }
                }
                break;
            default:
                mResponse = mActivity.findViewById(R.id.edit_question_response);
                mResponse.setHint(question.hint);
                if (existPrevRes)
                    mResponse.setText((String)prevResult);
                break;
        }
        for (int c : question.constraints) {
            switch (c) {
                case PHONE_NUMBER:
                    mResponse.setInputType(TYPE_CLASS_PHONE);
                    break;
                case NCC_ID:
                    mResponse.setAllCaps(true);
                    break;
            }
        }
    }


    private void processResponse() {
        boolean empty;
        int q_number = mActivity.q_number;
        switch (question.type) {
            case SELECT_MULTI:
                BitSet bitSet = new BitSet();
                for (int i = 0; i < checks.length; i++)
                        if (checks[i] != null && checks[i].isChecked())
                            bitSet.flip(i);
                if (question.required && bitSet.cardinality() == 0) {
                    displayToastRequired();
                    break;
                }
                if (bitSet.cardinality() == 0 || !bitSet.get(0) ||
                        (bitSet.get(0) && bitSet.cardinality() == 1)) {
                    String[] opts = question.options;
                    String result;
                    if (bitSet.get(0))
                        result = opts[opts.length - 1];
                    else {
                        StringBuilder str = new StringBuilder();
                        for (int i = 1; i <= opts.length; i++)
                            if (bitSet.get(i))
                                str.append(opts[i - 1]).append(", ");
                        result = str.length() < 2 ?
                                "" : str.substring(0, str.length() - 2);
                    }
                    mActivity.RESULTS[q_number] = result;
                    mActivity.onFragmentEnd();
                }
                else
                    displayToastConstraint(getString(R.string.str_uncheck_decline));
                break;
            case SELECT_ONE:
                int i = group.getCheckedRadioButtonId();
                if (question.required && i == -1){
                    displayToastRequired();
                    return;
                }
                mActivity.RESULTS[q_number] = i == -1 ? null :
                        ((RadioButton)mActivity.findViewById(i)).getText().toString();
                mActivity.onFragmentEnd();
                break;
            case ADDRESS:
                Spinner spinner = mActivity.findViewById(R.id.spinner_address_state);
                EditText street = mActivity.findViewById(R.id.edit_address_street);
                EditText city = mActivity.findViewById(R.id.edit_address_city);
                EditText zip = mActivity.findViewById(R.id.edit_address_zip);
                empty = street.getText().toString().equals("") ||
                        city.getText().toString().equals("") ||
                        zip.getText().toString().equals("");
                if (question.required && empty) {
                    displayToastRequired();
                    return;
                }
                else if (!empty && !zip.getText().toString().matches("[0-9]{5}")) {
                    displayToastConstraint(getString(R.string.str_zip_invalid));
                    return;
                }
                mActivity.RESULTS[q_number] = empty ? null : new String[] {
                        street.getText().toString(),
                        city.getText().toString(),
                        (String)spinner.getSelectedItem(),
                        zip.getText().toString()
                };
                mActivity.onFragmentEnd();
                break;
            case SELECT_NUMBER:
                mActivity.RESULTS[q_number] = String.valueOf(picker.getValue());
                mActivity.onFragmentEnd();
                break;
            case TEXT:
            case NUMBER_ENTRY:
                empty = mResponse.getText().toString().isEmpty();
                if (question.required && empty) {
                    displayToastRequired();
                    return;
                } else if (empty || evaluateEntryConstraints()) {
                    mActivity.RESULTS[q_number] = mResponse.getText().toString();
                    mActivity.onFragmentEnd();
                    return;
                }
                break;
        }
    }


    private boolean evaluateEntryConstraints() {
        String r = mResponse.getText().toString();
        for (int c : question.constraints)
            switch (c) {
                case PHONE_NUMBER:
                    if (!r.matches("[2-9]\\d{2}-[2-9]\\d{2}-\\d{4}")) {
                        displayToastConstraint(getResources()
                                .getString(R.string.str_constraint_phone));
                        return false;
                    }
                    break;
                case NCC_ID:
                    if (!r.matches("N00[0-9]{6}")) {
                        displayToastConstraint(getResources()
                                .getString(R.string.str_constraint_ncc_invalid));
                        return false;
                    }
                    if (mActivity.realm.where(PantryGuest.class)
                            .equalTo("nccID", r)
                            .findAll().size() != 0) {
                        displayToastConstraint(getResources()
                                .getString(R.string.str_constraint_ncc_exists));
                        return false;
                    }
                    break;
            }
        if (question.type == NUMBER_ENTRY &&
                (Integer.parseInt(r) < question.min ||
                        Integer.parseInt(r) > question.max)) {
            displayToastConstraint(getResources()
                    .getString(R.string.str_constraint_range));
            return false;
        }
        return true;
    }


    private void displayToastConstraint(String message) {
        Toast.makeText(mActivity.getApplicationContext(),
                message, Toast.LENGTH_LONG).show();
    }


    private void displayToastRequired() {
        Toast.makeText(mActivity.getApplicationContext(),
                R.string.str_response_required, Toast.LENGTH_LONG).show();
    }
}
