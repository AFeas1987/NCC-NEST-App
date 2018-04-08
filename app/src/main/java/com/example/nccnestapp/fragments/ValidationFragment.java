package com.example.nccnestapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.nccnestapp.R;
import com.example.nccnestapp.activities.AbstractActivity;
import com.example.nccnestapp.activities.QuestionActivity;
import com.example.nccnestapp.utilities.PantryGuest;

import io.realm.Case;
import io.realm.RealmResults;

public class ValidationFragment extends Fragment {

    EditText emailView;
    AbstractActivity myActivity;
    RealmResults<PantryGuest> mResults;
    ProgressBar mProgress;
    ImageView mImage;
    Button launchButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_validation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myActivity = (AbstractActivity) getActivity();
        emailView = myActivity.findViewById(R.id.edit_question_response);
        mProgress = myActivity.findViewById(R.id.progress_valid_email);
        mImage = myActivity.findViewById(R.id.img_valid_email);
        launchButton = myActivity.findViewById(R.id.btn_valid_launch);
        launchButton.setOnClickListener(launchView -> {
            ((QuestionActivity)myActivity).showPinDialog(emailView.getText().toString());
//            onDestroyView();
        });

        emailView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mImage.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mResults = myActivity.realm.where(PantryGuest.class).equalTo("email", emailView.getText().toString(), Case.INSENSITIVE).findAll();
                mProgress.setVisibility(View.GONE);
                if (!isValidEmail(emailView.getText()) || mResults.size() != 0) {
                    mImage.setImageResource(android.R.drawable.ic_delete);
                    mImage.setVisibility(View.VISIBLE);
                    launchButton.setEnabled(false);
                }
                else {
                    mImage.setImageResource(android.R.drawable.presence_online);
                    mImage.setVisibility(View.VISIBLE);
                    launchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
