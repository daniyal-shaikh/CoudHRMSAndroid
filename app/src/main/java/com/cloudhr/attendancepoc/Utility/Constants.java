package com.cloudhr.attendancepoc.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by IN-RB on 02-02-2018.
 */

public class Constants {

    public static int SPLASH_DISPLAY_LENGTH = 2000;
    public static int PAYMENT_SUCCESS = 1;
    public static int PAYMENT_FAILURE = 0;
    public static String PAYMENT_AMOUNT = "10.05";
    public static String SERVICE_RTO = "RTO";
    public static String SERVICE_NONRTO = "NONRTO";
    public static String SUB_PRODUCT_LIST = "SUB_PRODUCT_LIST";

    public static String SERVICE_TYPE = "ELITE_SERVICE_TYPE";

    public static String RTO_PRODUCT_DATA = "ELITE_RTO_PRODUCT_DATA";
    public static String NON_RTO_PRODUCT_DATA = "ELITE_NON_RTO_PRODUCT_DATA";

    public static String SUB_PRODUCT_DATA = "ELITE_SUB_RTO_PRODUCT_DATA";

    public static int SEARCH_CITY_CODE = 5;
    public static int ORDER_CODE = 2;
    public static int UPLOAD_FILE = 4;
    public static int REQUEST_CODE = 22;

    public static String SEARCH_CITY_DATA = "ELITE_SEARCH_CITY_DATA";
    public static String SEARCH_CITY_ID = "ELITE_SEARCH_CITY_ID";
    public static final int PERMISSION_CAMERA_STORACGE_CONSTANT = 103;
    public static final int PERMISSION_CALLBACK_CONSTANT = 100;
    public static final int REQUEST_PERMISSION_SETTING = 101;


    public static void hideKeyBoard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
            return activeNetwork.isConnected();
        }
        else {
            return activeNetwork.isConnected();
        }
    }
}
