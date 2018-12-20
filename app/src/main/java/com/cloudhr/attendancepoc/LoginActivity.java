package com.cloudhr.attendancepoc;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhr.attendancepoc.Utility.Constants;
import com.cloudhr.attendancepoc.Utility.LocationTracker;
import com.cloudhr.attendancepoc.Utility.Utility;
import com.cloudhr.attendancepoc.attandance.EulaActivity;
import com.cloudhr.attendancepoc.attandance.OutDoorAttendanceActivity;
import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.controller.LoginController;
import com.cloudhr.attendancepoc.core.response.LoginResponse;
import com.cloudhr.attendancepoc.dbControl.PrefManager;
import com.cloudhr.attendancepoc.service.TrackLocationAlarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener, IResponseSubcriber {


    final private int REQUEST_CODE_ASK_PERMISSIONS = 1111;
    EditText etPassword, etMobile;
    PrefManager prefManager;
    PendingIntent pendingIntent;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);


     /*   TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String mPhoneNumber = tMgr.getLine1Number();
            List<CellInfo> mPhoneNumber1 = tMgr.getAllCellInfo();

            Toast.makeText(this, mPhoneNumber, Toast.LENGTH_LONG).show();

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
//        String mPhoneNumber = tMgr.getLine1Number();

        initialize();
        prefManager = new PrefManager(this);


    }

    private void initialize() {

        etPassword = (EditText) findViewById(R.id.etPassword);
        etMobile = (EditText) findViewById(R.id.etMobile);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        Constants.hideKeyBoard(view,this);
        switch (view.getId()) {

            case R.id.btnSignIn:
                if (!isEmpty(etMobile)) {
                    etMobile.requestFocus();
                    Snackbar.make(etMobile, "Enter LoginID", Snackbar.LENGTH_LONG).show();
                    // etMobile.setError("Enter Mobile");
                    return;
                }
//                if (!isEmpty(etPassword)) {
//                    etPassword.requestFocus();
//                    Snackbar.make(etMobile, "Enter Password", Snackbar.LENGTH_LONG).show();
//                    //  etPassword.setError("Enter Password");
//                    return;
//                }
                new LoginController(LoginActivity.this).login(String.valueOf(etMobile.getText()),this);

//                if(etMobile.getText().toString().equalsIgnoreCase("123") && etPassword.getText().toString().equalsIgnoreCase("123") ) {
//                    Intent intent = new Intent(this, OutDoorAttendanceActivity.class);
//                    startActivity(intent);
//                }else{
//                    Snackbar.make(etMobile, "Invalid LoginId or Password", Snackbar.LENGTH_LONG).show();
//                }
                break;
        }
    }



    private static final int ALARM_REQUEST_CODE = 133;
    @Override
    public void OnSuccess(APIResponse response, String message) {
        if(response instanceof LoginResponse){
            cancelDialog();
            if (response.getStatus_code() == 0) {
                prefManager.setEmployeeid(String.valueOf(((LoginResponse) response).getData().get(0).getEmployeeid()));
                prefManager.setemployeecode(String.valueOf(((LoginResponse) response).getData().get(0).getEmployeecode()));
                prefManager.setemployeename(String.valueOf(((LoginResponse) response).getData().get(0).getEmployeename()));
                prefManager.setphoneno1(String.valueOf(((LoginResponse) response).getData().get(0).getPhoneno1()));
                prefManager.setphoneno2(String.valueOf(((LoginResponse) response).getData().get(0).getPhoneno2()));
                prefManager.setemailid(String.valueOf(((LoginResponse) response).getData().get(0).getEmailid()));
                prefManager.setbranchname(String.valueOf(((LoginResponse) response).getData().get(0).getBranchname()));
                prefManager.setcompanyname(String.valueOf(((LoginResponse) response).getData().get(0).getCompanyname()));
                prefManager.setcompanyname(String.valueOf(((LoginResponse) response).getData().get(0).getCompanyname()));
                prefManager.setLattitude(String.valueOf(((LoginResponse) response).getData().get(0).getLattitude()));
                prefManager.setLongutide(String.valueOf(((LoginResponse) response).getData().get(0).getLongitude()));


                Intent alarmIntent = new Intent(LoginActivity.this, TrackLocationAlarm.class);
                pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);

                Calendar cal = Calendar.getInstance();
                // add alarmTriggerTime seconds to the calendar object
                cal.add(Calendar.SECOND, 1);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
                //manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds
                manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),1000  * 5, pendingIntent);
                Toast.makeText(this, "Alarm Set for " + 5 + " seconds.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, OutDoorAttendanceActivity.class));
                startActivity(new Intent(this, HomeActivity.class));

                //Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
                finish();
            }
            else{
                startActivity(new Intent(this,  LoginActivity.class));
            }
        }

    }
    @Override
    public void OnFailure(Throwable t) {
        Toast.makeText(this, "Invalid Phoneno", Toast.LENGTH_LONG).show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("Retry")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (checkPermission()) {
//            Loc = new LocationTracker(LoginActivity.this);
//            Loc.init();
//
//            super.onResume();
//        }
    }


}
