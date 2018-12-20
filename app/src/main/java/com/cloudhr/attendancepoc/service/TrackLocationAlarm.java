package com.cloudhr.attendancepoc.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.cloudhr.attendancepoc.HomeActivity;
import com.cloudhr.attendancepoc.Utility.LocationTracker;
import com.cloudhr.attendancepoc.Utility.Utility;
import com.cloudhr.attendancepoc.dbControl.PrefManager;

/**
 * Created by daniyalshaikh on 19/12/18.
 */

public class TrackLocationAlarm extends BroadcastReceiver {

    LocationTracker LocTracker;
    LocationTracker Loc;
    PrefManager prefManager;
    String internetStatus;
    String employeeDateTime;
    String lat = "0.0";
    String lon = "0.0";


    @Override
    public void onReceive(Context context, Intent intent) {

       // Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();
        Log.d("ALARM","ALARM");
        Loc = new LocationTracker(context);
        Loc.init();


      /*  Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();
        Log.d("ALARM","ALARM");
        LocTracker = new LocationTracker(context);
        LocTracker.init();
        prefManager = new PrefManager(context);

        if (Utility.checkGpsStatus(context)) {
            //Gps On
            if (Utility.checkInternetStatus(context)) {
                //Internet And Gps both on
                internetStatus = "Internet And Gps BOTH ON";
                postDataToServer(context);
            } else {
                //Gps on  And Internet down
                internetStatus="Gps On Internet Down";
                //saveLocationToDatabase();
            }
        } else {
            //Gps Off
            if (Utility.checkInternetStatus(context)) {
                //Gps Off And Internet on
                internetStatus="GPS OFF Internet On";
                Log.d("ATTENDANCE","");
                // new LocationController().insertLocation("0",sharedPreferences.getString(prefManager.getEmployeeid(), ""), sharedPreferences.getString(Utility.DEVICE_ID, ""), "0.0" , "0.0", "false", internetStatus, employeeDateTime, null);
            } else {
                //Gps Off And Internet Off
                internetStatus="Internet and gps Both OFF";
                //saveLocationToDatabase();
            }
        }*/
    }




    private void postDataToServer(Context context) {
        try {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

            if(LocTracker.canGetLocation()){
                Location location = LocTracker.getLocation();
                if (location != null) {
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());
                    Toast.makeText(context, "Location:"+lat+","+lon, Toast.LENGTH_SHORT).show();
                    //new LocationController().insertLocation("0",sharedPreferences.getString(Utility.EMPLOYEE_ID, ""), sharedPreferences.getString(Utility.DEVICE_ID, ""), "" + lat, "" + lon, "true", internetStatus, employeeDateTime, null);
                }
                Toast.makeText(context, lat+","+lon, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
