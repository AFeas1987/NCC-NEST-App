package com.example.nccnestapp.fragments;

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

import com.example.nccnestapp.R;
import com.example.nccnestapp.activities.QuestionActivity;
import com.example.nccnestapp.utilities.PantryGuest;
import com.example.nccnestapp.utilities.SurveyQuestion;

import java.util.BitSet;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_PHONE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.example.nccnestapp.utilities.Constants.Constraint.NCC_ID;
import static com.example.nccnestapp.utilities.Constants.Constraint.PHONE_NUMBER;
import static com.example.nccnestapp.utilities.Constants.QuestionType.ADDRESS;
import static com.example.nccnestapp.utilities.Constants.QuestionType.NUMBER_ENTRY;
import static com.example.nccnestapp.utilities.Constants.QuestionType.SELECT_MULTI;
import static com.example.nccnestapp.utilities.Constants.QuestionType.SELECT_NUMBER;
import static com.example.nccnestapp.utilities.Constants.QuestionType.SELECT_ONE;
import static com.example.nccnestapp.utilities.Constants.QuestionType.TEXT;

public class QuestionFragment extends Fragment {

    private SurveyQuestion question;
    private QuestionActivity mActivity;
    private EditText mResponse;
    private RadioGroup group;
    private NumberPicker picker;
    private CheckBox[] checks;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        question = unpackQuestion(getArguments());
        if (question.options != null)
            checks = new CheckBox[question.options.length + 1];
        return inflater.inflate(getTypeLayout(), container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = (QuestionActivity) getActivity();
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
        switch (question.type) {
            case SELECT_ONE:
                group = mActivity.findViewById(R.id.radio_question_select);
                for (String s : question.options){
                    RadioButton r = new RadioButton(mActivity);
                    r.setText(s);
                    r.setTextColor(Color.WHITE);
                    r.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    r.setLayoutParams(new LayoutParams(
                            WRAP_CONTENT, WRAP_CONTENT));
                    group.addView(r);
                }
                group.setVisibility(View.VISIBLE);
                break;
            case SELECT_MULTI:
                LinearLayout layout = mActivity.findViewById(R.id.linear_question_select);
                layout.setHorizontalGravity(Gravity.START);
                int i = 0;
                for (String s : question.options){
                    CheckBox c = new CheckBox(mActivity);
                    c.setText(s);
                    c.setTextColor(Color.WHITE);
                    c.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    c.setLayoutParams(new LayoutParams(
                            WRAP_CONTENT, WRAP_CONTENT));
                    checks[s.equalsIgnoreCase(getString(R.string.str_decline)) ? 0 : ++i] = c;
                    layout.addView(c);
                }
                break;
            case SELECT_NUMBER:
                layout = mActivity.findViewById(R.id.linear_question_select);
                picker = new NumberPicker(mActivity);
                picker.setMinValue(question.min);
                picker.setMaxValue(question.max);
                picker.setBackgroundColor(Color.WHITE);
                picker.setLayoutParams(new LayoutParams(
                        WRAP_CONTENT, WRAP_CONTENT));
                layout.addView(picker);
                break;
            case NUMBER_ENTRY:
                mResponse = mActivity.findViewById(R.id.edit_question_response);
                mResponse.setHint(question.hint);
                mResponse.setInputType(TYPE_CLASS_NUMBER);
                break;
            case ADDRESS:
                Spinner spinner = mActivity.findViewById(R.id.spinner_address_state);
                spinner.setSelection(31, false);
                break;
            default:
                mResponse = mActivity.findViewById(R.id.edit_question_response);
                mResponse.setHint(question.hint);
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
                    mActivity.RESULTS[q_number] = bitSet;
                    mActivity.onFragmentEnd();
                }
                else displayToastConstraint(getString(R.string.str_uncheck_decline));
                break;
            case SELECT_ONE:
                int i = group.getCheckedRadioButtonId();
                if (question.required && i == -1){
                    displayToastRequired();
                    return;
                }
                mActivity.RESULTS[q_number] =
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
                mActivity.RESULTS[q_number] = new String[] {
                        street.getText().toString(), city.getText().toString(),
                        (String)spinner.getSelectedItem(), zip.getText().toString()
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
                        displayToastConstraint(getResources().getString(R.string.str_constraint_phone));
                        return false;
                    }
                    break;
                case NCC_ID:
                    if (!r.matches("N00[0-9]{6}")) {
                        displayToastConstraint(getResources().getString(R.string.str_constraint_ncc_invalid));
                        return false;
                    }
                    if (mActivity.realm.where(PantryGuest.class)
                            .equalTo("nccID", r)
                            .findAll().size() != 0) {
                        displayToastConstraint(getResources().getString(R.string.str_constraint_ncc_exists));
                        return false;
                    }
                    break;
            }
        if (question.type == NUMBER_ENTRY &&
                (Integer.parseInt(r) < question.min ||
                        Integer.parseInt(r) > question.max)) {
            displayToastConstraint(getResources().getString(R.string.str_constraint_range));
            return false;
        }
        return true;
    }


    private void displayToastConstraint(String message) {
        Toast.makeText(mActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    private void displayToastRequired() {
        Toast.makeText(mActivity.getApplicationContext(), R.string.str_response_required, Toast.LENGTH_LONG).show();
    }


    private SurveyQuestion unpackQuestion(Bundle args){
        SurveyQuestion q = new SurveyQuestion(args.getInt("TYPE"), args.getInt("REQUIRED") == 1,
                args.getString("TITLE"), args.getString("PROMPT"), args.getIntArray("CONSTRAINTS"));
        q.min = args.getInt("MIN");
        q.max = args.getInt("MAX");
        q.hint = args.getString("HINT");
        q.setOptions(args.getStringArray("OPTIONS"));
        q.msgRequired = args.getString("MSG_REQUIRED");
        q.msgConstraint = args.getString("MSG_CONSTRAINT");
        return q;
    }
}
