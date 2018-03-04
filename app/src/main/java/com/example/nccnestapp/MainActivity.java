package com.example.nccnestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mainButtonClick(View v){
        Intent i = new Intent (Intent.ACTION_VIEW);
        i.setType("vnd.android.cursor.dir/vnd.odk.form");
        startActivity(i);
    }
}
