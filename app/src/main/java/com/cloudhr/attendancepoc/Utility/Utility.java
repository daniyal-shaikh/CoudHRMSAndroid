package com.cloudhr.attendancepoc.Utility;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Nilesh Birhade on 18-10-2016.
 */

public class Utility {


    public static long TIME_INTERVAL = 5000;//30 Min
    public static final int REQUEST_CODE_ASK_PERMISSIONS = 1115;

    public static String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
    };

    public boolean checkFileExist(String path) {
        return new File(path).isFile();
    }

    public static String getListOfFilesPath(String folderPath) {
        List<String> results = new ArrayList<String>();
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Log.d("File ", listOfFiles[i].getName());
                results.add(folderPath + "/" + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                Log.d("Directory ", listOfFiles[i].getName());
            }
        }

        return results.get(listOfFiles.length - 1);
    }

    public static void hideKeyBoard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException ex) {
        }
        return "0";
    }



    public static boolean checkInternetStatus(Context context) {
      /*  ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                conMgr.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                conMgr.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                conMgr.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        }*/
        final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // notify user you are online
            return true;
        }
        return false;
    }

    public static boolean checkGpsStatus(Context context) {
        PackageManager pm = context.getPackageManager();
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        boolean gps_enabled = false;
        if (hasGps) {
            gps_enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return gps_enabled;
    }

    public static String getMobileDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getCurrentMobileDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }





    public static boolean deleteAudioFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, os);
                byte[] byteArray = os.toByteArray();
                return Base64.encodeToString(byteArray, Base64.NO_WRAP);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap convertBase64ToBitmap(String b64) {
        try {
            byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File createDirIfNotExists() {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), "/RupeeBoss");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ret = false;
            }
        }
        return file;
    }

    public static void loadWebViewUrlInBrowser(Context context, String url) {
        Log.d("URL", url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        if (Uri.parse(url) != null) {
            browserIntent.setData(Uri.parse(url));
        }
        context.startActivity(browserIntent);
    }
}
