package com.cloudhr.attendancepoc.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.cloudhr.attendancepoc.Utility.LocationTracker;
import com.cloudhr.attendancepoc.Utility.Utility;
import com.cloudhr.attendancepoc.dbControl.PrefManager;

/**
 * Created by daniyalshaikh on 19/12/18.
 */

public class MyAlarmService extends Service {
    String lat = "0.0";
    String lon = "0.0";
    LocationTracker LocTracker;
    String dayOfTheWeek;
    String internetStatus;
    String employeeDateTime;
    String deviceId;
    SharedPreferences sharedPreferences;
    PrefManager prefManager;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        LocTracker = new LocationTracker(this);
        LocTracker.init();
        prefManager = new PrefManager(this);
    }


    @Override

    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override

    public void onStart(Intent intent, int startId) {

        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        sharedPreferences = getSharedPreferences("ATTENDANCE", MODE_PRIVATE);
        employeeDateTime = Utility.getMobileDateTime();
        //deviceId = sharedPreferences.getString(Utility.DEVICE_ID, "");

            if (Utility.checkGpsStatus(getApplicationContext())) {
                //Gps On
                if (Utility.checkInternetStatus(getApplicationContext())) {
                    //Internet And Gps both on
                    internetStatus = "Internet And Gps BOTH ON";
                    postDataToServer();
                } else {
                    //Gps on  And Internet down
                    internetStatus="Gps On Internet Down";
                    saveLocationToDatabase();
                }
            } else {
                //Gps Off
                if (Utility.checkInternetStatus(getApplicationContext())) {
                    //Gps Off And Internet on
                    internetStatus="GPS OFF Internet On";
                    Log.d("ATTENDANCE","");
                   // new LocationController().insertLocation("0",sharedPreferences.getString(prefManager.getEmployeeid(), ""), sharedPreferences.getString(Utility.DEVICE_ID, ""), "0.0" , "0.0", "false", internetStatus, employeeDateTime, null);
                } else {
                    //Gps Off And Internet Off
                    internetStatus="Internet and gps Both OFF";
                    saveLocationToDatabase();
                }
            }



        /*if (Utility.validateDayTime()) {
            if (Utility.checkInternetStatus(getApplicationContext())) {
                if (Utility.checkGpsStatus(getApplicationContext())) {
                    internetStatus = "Gps On";
                    postDataToServer();
                } else {
                    internetStatus = "GPS OFF";
                    new LocationController().insertLocation(sharedPreferences.getString(Utility.EMPLOYEE_ID, ""), sharedPreferences.getString(Utility.DEVICE_ID, ""), "0", "0", "false", internetStatus, employeeDateTime, null);
                }
            } else {
                internetStatus = "Internet Down";
                saveLocationToDatabase();
            }
        }*/
    }

    private void postDataToServer() {
        try {
            Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show();
            if(LocTracker.canGetLocation()){
                Location location = LocTracker.getLocation();
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());
                    Toast.makeText(this, "Location:"+lat+","+lon, Toast.LENGTH_SHORT).show();
                    //new LocationController().insertLocation("0",sharedPreferences.getString(Utility.EMPLOYEE_ID, ""), sharedPreferences.getString(Utility.DEVICE_ID, ""), "" + lat, "" + lon, "true", internetStatus, employeeDateTime, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void saveLocationToDatabase() {
        try {
//            GPSTempData tempData = new GPSTempData();
//            tempData.setDeviceID(sharedPreferences.getString(Utility.DEVICE_ID, ""));
//            tempData.setEmployeeDateTime(employeeDateTime);
//            tempData.setLattitude("0.0");
//            tempData.setLongitude("0.0");
//            tempData.setFlag("false");
//            tempData.setStatus(internetStatus);
//            long in = tempData.save();
            //Log.d("DB inserted", "" + in + " " + internetStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}