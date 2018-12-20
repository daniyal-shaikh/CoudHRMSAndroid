package com.cloudhr.attendancepoc.attandance;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.cloudhr.attendancepoc.BaseActivity;
import com.cloudhr.attendancepoc.HomeActivity;
import com.cloudhr.attendancepoc.LoginActivity;
import com.cloudhr.attendancepoc.R;
import com.cloudhr.attendancepoc.Utility.LocationTracker;
import com.cloudhr.attendancepoc.Utility.Utility;
import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.controller.LoginController;
import com.cloudhr.attendancepoc.core.response.LoginResponse;
import com.cloudhr.attendancepoc.dbControl.PrefManager;
import com.cloudhr.attendancepoc.service.MyAlarmService;
import com.cloudhr.attendancepoc.service.TrackLocationAlarm;
import com.cloudhr.attendancepoc.webview.MyWebViewClient;

import java.util.Calendar;
import java.util.List;

import static com.cloudhr.attendancepoc.Utility.Utility.REQUEST_CODE_ASK_PERMISSIONS;

public class EulaActivity extends BaseActivity implements View.OnClickListener, IResponseSubcriber {

    Button btnAgree, btnDisAgree;
    PrefManager prefManager;
    WebView webView;

    private PendingIntent pendingIntent;
    String[] perms = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.READ_PHONE_STATE"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(this);
        initWidgets();
        setListener();
        settingWebview();
        if (!checkPermission()) {
            requestPermission();
        }
        else{
            callAutoLogin();
        }
    }


    private boolean checkPermission() {

        int writeLogResult = ContextCompat.checkSelfPermission(getApplicationContext(), Utility.perms[0]);
        int fineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), perms[1]);
        int access_coarse_location = ContextCompat.checkSelfPermission(getApplicationContext(), perms[2]);
        int access_phone_state = ContextCompat.checkSelfPermission(getApplicationContext(), perms[3]);
        return writeLogResult == PackageManager.PERMISSION_GRANTED
                && fineLocation == PackageManager.PERMISSION_GRANTED
                && access_coarse_location == PackageManager.PERMISSION_GRANTED
                && access_phone_state == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, perms, REQUEST_CODE_ASK_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults.length > 0) {

                    boolean writeExternal = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean fineLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean accessCoraseLoc = grantResults[2] == PackageManager.PERMISSION_GRANTED;



                    if (writeExternal && fineLocation && accessCoraseLoc) {
                        // you can do all necessary steps
                        // new Dialer().getObject().getLeadData(String.valueOf(Utility.EmpCode), this, this);
                        // Toast.makeText(this, "All permission granted", Toast.LENGTH_SHORT).show();
                    } else {

                        //Permission Denied, You cannot access location data and camera
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            showMessageOKCancel("Required permissions to proceed BBA..!",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // finish();
                                            requestPermission();
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private static final int ALARM_REQUEST_CODE = 133;
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EulaActivity.this)
                .setTitle("Retry")
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                //.setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void initWidgets() {
        webView = (WebView) findViewById(R.id.webView);
        btnAgree = (Button) findViewById(R.id.btnAgree);
        btnDisAgree = (Button) findViewById(R.id.btnDisAgree);
    }

    private void setListener() {
        btnAgree.setOnClickListener(this);
        btnDisAgree.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAgree:

                prefManager.setFirstTimeLaunch(false);

                callAutoLogin();
                //startActivity(new Intent(this,  LoginActivity.class));


                break;
            case R.id.btnDisAgree:
                finish();
                break;
        }
    }


    private void callAutoLogin(){


        if (!checkPermission()) {
            requestPermission();
        }
        else {
            if(prefManager.getEmployeeid().equals("")){
                String mPhoneNumber="";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
                    for (int i = 0; i < subscription.size(); i++) {
                        SubscriptionInfo info = subscription.get(i);
                        if(info.getNumber()!=null   && mPhoneNumber.equals("") ) {
                            mPhoneNumber = info.getNumber();
                        }
                    }
                }

            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber1 = tMgr.getLine1Number();
                showDialog("Please Wait........");
                new LoginController(EulaActivity.this).login(mPhoneNumber,this);
                //new RegisterController(LoginActivity.this).getLogin(etMobile.getText().toString(), etPassword.getText().toString(),strToken, deviceId, this);
                Toast.makeText(this, mPhoneNumber, Toast.LENGTH_LONG).show();
            }
            else{
                startActivity(new Intent(this,  HomeActivity.class));
               //startActivity(new Intent(this,  LoginActivity.class));
                finish();
            }

        }
    }

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
                prefManager.setLattitude(String.valueOf(((LoginResponse) response).getData().get(0).getLattitude()));
                prefManager.setLongutide(String.valueOf(((LoginResponse) response).getData().get(0).getLongitude()));

          /*      Intent myIntent = new Intent(EulaActivity.this, MyAlarmService.class);
                pendingIntent = PendingIntent.getBroadcast(EulaActivity.this, 0, myIntent, 0);



                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.SECOND, 1);
//                alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                        SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
//                        Utility.TIME_INTERVAL, pendingIntent);

                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000  * 1, pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                //                startActivity(new Intent(this, OutDoorAttendanceActivity.class));*/


                Intent alarmIntent = new Intent(EulaActivity.this, TrackLocationAlarm.class);
                pendingIntent = PendingIntent.getBroadcast(EulaActivity.this, ALARM_REQUEST_CODE, alarmIntent, 0);

                Calendar cal = Calendar.getInstance();
                // add alarmTriggerTime seconds to the calendar object
                cal.add(Calendar.SECOND, 1);

                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
                //manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds
                manager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),1000  * 5, pendingIntent);
                Toast.makeText(this, "Alarm Set for " + 1 + " seconds.", Toast.LENGTH_SHORT).show();


                startActivity(new Intent(this, HomeActivity.class));
                finish();
                //Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
            }
            else{
                startActivity(new Intent(this,  LoginActivity.class));
            }
        }

    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();
        startActivity(new Intent(this,  LoginActivity.class));
        //Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();
        //getCustomToast(t.getMessage());
    }


    private void settingWebview() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(false);

        settings.setLoadsImagesAutomatically(true);
        settings.setLightTouchEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);


        MyWebViewClient webViewClient = new MyWebViewClient(this);
        webView.setWebViewClient(webViewClient);

        webView.getSettings().setBuiltInZoomControls(true);
       /* Log.d("URL", url);
        //webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        webView.loadUrl(url);*/
        webView.loadUrl("file:///assets/eula.html");
    }
}
