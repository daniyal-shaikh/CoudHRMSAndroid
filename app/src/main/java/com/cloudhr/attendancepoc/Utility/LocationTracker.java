package com.cloudhr.attendancepoc.Utility;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.cloudhr.attendancepoc.BaseActivity;
import com.cloudhr.attendancepoc.HomeActivity;
import com.cloudhr.attendancepoc.attandance.EulaActivity;
import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.controller.ProductController;
import com.cloudhr.attendancepoc.dbControl.PrefManager;
import com.cloudhr.attendancepoc.service.AttendanceNotificationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class LocationTracker extends BroadcastReceiver implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,IResponseSubcriber {

    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    Context mContext;
    String branchlat = "0.0";
    String brnachlon = "0.0";

    public LocationTracker(Context activity) {
        mContext = activity;
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Toast.makeText(mContext, lat+","+lon, Toast.LENGTH_SHORT).show();
        PrefManager prefManager =new PrefManager(mContext);
        if(prefManager.getAttendancedate().equals(getCurrentDate())){
            return;
        }
        Log.d("LOCATION",lat+","+lon);
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());

        Toast.makeText(mContext, lat+","+lon, Toast.LENGTH_SHORT).show();

//        lat = String.valueOf(location.getLatitude());
//        lon = String.valueOf(location.getLongitude());
        branchlat= prefManager.getLattitude();
        brnachlon= prefManager.getLongutide();

        if(lat!=null && lon!=null){
            double distanceinkm =distance(location.getLatitude(),location.getLongitude(),Double.valueOf(branchlat),Double.valueOf(brnachlon));
            double distinmeter = distanceinkm * 1000;
            if(distinmeter<=500000){
                new ProductController(mContext).getSwipe(Integer.parseInt(prefManager.getEmployeeid()),lat,lon,"IN",this);
                Toast.makeText(mContext, "Attendance Marked", Toast.LENGTH_LONG).show();

                Intent newIntent = new Intent(mContext, AttendanceNotificationService.class);
                mContext.startService(newIntent);

                Log.d("LOCATION","Attendance Marked");
                prefManager.setAttendancedate(getCurrentDate());
            }
            Log.d("LOCATION","Distance : "+String.valueOf(distanceinkm));
        }
        //Toast.makeText(mContext, lat+","+lon, Toast.LENGTH_SHORT).show();
        Log.d("LOCATION",lat+","+lon);
    }





    private String getCurrentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return  formattedDate;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public boolean canGetLocation() {
        if (mLastLocation != null) {
            return true;
        } else {
            return false;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {


        //LocationManager  mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);




        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Utility.TIME_INTERVAL);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());
        }

    }

    public Location getLocation() {
        if (mLastLocation == null) {
            return null;
        } else {
            return mLastLocation;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void init() {
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LOCATION","OnReceivd");
        Toast.makeText(context, "Called", Toast.LENGTH_SHORT).show();
        mContext = context;
        buildGoogleApiClient();
    }

    @Override
    public void OnSuccess(APIResponse response, String message) {

    }

    @Override
    public void OnFailure(Throwable t) {

    }
}
