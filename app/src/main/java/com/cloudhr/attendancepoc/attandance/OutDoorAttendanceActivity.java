package com.cloudhr.attendancepoc.attandance;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;


import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cloudhr.attendancepoc.BaseActivity;
import com.cloudhr.attendancepoc.R;
import com.cloudhr.attendancepoc.Utility.LocationTracker;
import com.cloudhr.attendancepoc.Utility.Utility;
import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.controller.ProductController;
import com.cloudhr.attendancepoc.core.model.SwipeDetailEntity;
import com.cloudhr.attendancepoc.core.response.SwipeDetailResponse;

import java.util.List;
import java.util.Locale;

public class OutDoorAttendanceActivity extends BaseActivity implements View.OnClickListener ,IResponseSubcriber {

    LocationTracker Loc;
    boolean doubleBackToExitPressedOnce = false;
    String lat, lon;
    Button btnAttendIN, btnAttendOUT;

    RecyclerView rvAttendace;
    List<SwipeDetailEntity> lstAttendance;
    AttendanceAdapter mAdapter;
    String AttendTyp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_door_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initialize();
    }

    private void initialize() {
        btnAttendIN = (Button) findViewById(R.id.btnAttIn);
        btnAttendOUT = (Button) findViewById(R.id.btnAttOut);
        rvAttendace = (RecyclerView) findViewById(R.id.rvAttendace);
        rvAttendace.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OutDoorAttendanceActivity.this);
        rvAttendace.setLayoutManager(layoutManager);

        btnAttendIN.setOnClickListener(this);
        btnAttendOUT.setOnClickListener(this);

//        loginFacade = new LoginFacade(OutDoorAttendanceActivity.this);
//        boolean blnInBtn = loginFacade.getLoginResponse().getResult().isEnable_INButton();

//        if(blnInBtn) {
//            btnAttendIN.setVisibility(View.VISIBLE);
//        }else{
//            btnAttendIN.setVisibility(View.GONE);
//
//        }
//
//        showDialog();
//        new Swipe(OutDoorAttendanceActivity.this).swipeDetailsTop(empCode, OutDoorAttendanceActivity.this);


    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.btnAttIn) {
            if (Utility.checkInternetStatus(OutDoorAttendanceActivity.this)) {
                GetMyAttendance("IN");

                AttendTyp = "IN";
            } else {
                Snackbar.make(btnAttendOUT, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnAttOut) {
            if (Utility.checkInternetStatus(OutDoorAttendanceActivity.this)) {

                GetMyAttendance("OUT");
                AttendTyp = "OUT";
//                if(serverTimeFlag) {
//                    GetMyAttendance("OUT");
//                    AttendTyp = "OUT";
//                }else{
//                    Snackbar.make(btnAttendOUT, "Please Use  OUT Punch Between 14.30 And 23.59 Hrs ", Snackbar.LENGTH_LONG).show();
//                }
            } else {
                Snackbar.make(btnAttendOUT, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    public void GetMyAttendance(String entryTyp) {
        // Call Latlong code here
        lat = "";
        lon = "";
        PackageManager pm = getPackageManager();
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        boolean network_enabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (hasGps) {
            if (!gps_enabled) {
                Loc.showSettingsAlert();
                return;
            } else {
                Location location = Loc.getLocation();
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());

                    showDialog();
                  //  Toast.makeText(this, " Attendance lat "+ lat+ "and  Long" + lon+ " " ,Toast.LENGTH_LONG).show();
                    if (entryTyp.equals("OUT")) {
                        new ProductController(OutDoorAttendanceActivity.this).getSwipe(62,lat,lon,"OUT",this);
                    } else {
                        new ProductController(OutDoorAttendanceActivity.this).getSwipe(62,lat,lon,"IN",this);
                    }


                } else {
                    //Snackbar.make(btnAttendIN, "Location Not Found, Please Try Again", Snackbar.LENGTH_SHORT).show();
                    showGoogleMap();
                }
            }
        } else {
            if (!network_enabled) {
                Loc.showSettingsAlert();
                return;
            } else {

                Location location = Loc.getLocation();
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());

                    showDialog();
                    //  Toast.makeText(this, " Attendance lat "+ lat+ "and  Long" + lon+ " " ,Toast.LENGTH_LONG).show();
                    if (entryTyp.equals("OUT")) {
                        new ProductController(OutDoorAttendanceActivity.this).getSwipe(62,lat,lon,"OUT",this);
                    } else {
                        new ProductController(OutDoorAttendanceActivity.this).getSwipe(62,lat,lon,"IN",this);
                    }

                } else {
                    //Snackbar.make(btnAttendIN, "Location Not Found, Please Try Again", Snackbar.LENGTH_SHORT).show();
                    showGoogleMap();
                }

            }
        }
    }


    private void showGoogleMap() {

        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(OutDoorAttendanceActivity.this);
            builder.setTitle("Oops! Your location not found.");

            builder.setMessage("Kindly go to google map , set your current location and try again.");

            String positiveText = "OK";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 19.0857745, 72.8883218);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            OutDoorAttendanceActivity.this.startActivity(intent);

                        }
                    });


            final android.support.v7.app.AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } catch (Exception ex) {
            Toast.makeText(this, "Please try again..", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

        cancelDialog();

        if (response instanceof SwipeDetailResponse) {

            if (response.getStatus_code() == 0) {

                if (!message.equals("OK")) {
                    Snackbar.make(btnAttendIN, message, Snackbar.LENGTH_SHORT).show();
                }
                lstAttendance = ((SwipeDetailResponse) response).getData();
                mAdapter = new AttendanceAdapter(OutDoorAttendanceActivity.this, lstAttendance);
                rvAttendace.setAdapter(mAdapter);


            } else {
                rvAttendace.setAdapter(null);
                Snackbar.make(btnAttendIN, "No data available", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void OnFailure(Throwable t) {
        cancelDialog();

       Toast.makeText(this,t.getMessage(),Toast.LENGTH_SHORT).show();
    }


@Override
protected void onResume() {

    if (!checkPermission()) {
        requestPermission();
    }

    Loc = new LocationTracker(OutDoorAttendanceActivity.this);
    Loc.init();

  //  new Swipe(OutDoorAttendanceActivity.this).getServerTimeVerify(OutDoorAttendanceActivity.this);

    super.onResume();
}

    private boolean checkPermission() {

        int writeLogResult = ContextCompat.checkSelfPermission(getApplicationContext(), Utility.perms[0]);
        int fineLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Utility.perms[1]);
        int coarseLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Utility.perms[2]);

        return writeLogResult == PackageManager.PERMISSION_GRANTED
                && fineLocation == PackageManager.PERMISSION_GRANTED
                && coarseLocation == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, Utility.perms, Utility.REQUEST_CODE_ASK_PERMISSIONS);

    }
}
