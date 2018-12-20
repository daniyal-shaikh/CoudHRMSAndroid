package com.cloudhr.attendancepoc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cloudhr.attendancepoc.Utility.Constants;
import com.cloudhr.attendancepoc.attandance.EulaActivity;
import com.cloudhr.attendancepoc.attandance.OutDoorAttendanceActivity;
import com.cloudhr.attendancepoc.attandance.WelcomeActivity;
import com.cloudhr.attendancepoc.dbControl.PrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    PrefManager prefManager;
    TextView txtGroup;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);
        txtGroup = (TextView) findViewById(R.id.txtGroup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefManager = new PrefManager(this);
       // verify();
        {
            if (prefManager.isFirstTimeLaunch()) {
                startActivity(new Intent(this, WelcomeActivity.class));
            } else {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(SplashScreenActivity.this, EulaActivity.class));
                        finish();
                       // startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                       /* UserEntity loginEntity = dataBaseController.getUserData();
                        if (loginEntity != null) {
                            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));

                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                        }*/
                        //fetchMasters();

                    }
                }, SPLASH_DISPLAY_LENGTH);

            }
        }

    }


    public void verify() {
        if (Constants.checkInternetStatus(SplashScreenActivity.this)) {

            Snackbar snackbar = Snackbar.make(txtGroup, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            verify();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.CYAN);

            snackbar.show();
        } else {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                    }
                }, SPLASH_DISPLAY_LENGTH);


        }


    }

}
