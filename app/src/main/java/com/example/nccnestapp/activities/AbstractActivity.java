package com.example.nccnestapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.nccnestapp.R;
import static com.example.nccnestapp.utilities.Constants.COLLECT_PACKAGE_NAME;


public class AbstractActivity extends AppCompatActivity {

    public void startActivityIfAvailable(Intent i) {
        if (!isCollectAppInstalled()) {
            Toast
                    .makeText(this, getString(R.string.collect_app_not_installed), Toast.LENGTH_LONG)
                    .show();
        } else if (isActivityAvailable(i)) {
            startActivity(i);
        } else {
            Toast
                    .makeText(this, getString(R.string.activity_not_found), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean isActivityAvailable(Intent intent) {
        return getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    public boolean isCollectAppInstalled() {
        try {
            getPackageManager().getPackageInfo(COLLECT_PACKAGE_NAME, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
