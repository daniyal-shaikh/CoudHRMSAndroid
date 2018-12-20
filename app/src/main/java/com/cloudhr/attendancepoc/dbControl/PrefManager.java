package com.cloudhr.attendancepoc.dbControl;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IN-RB on 09-06-2018.
 */

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;


    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BBA_USER";

    private static final String IS_PRODUCT_MASTER_UPDATE = "isProductMasterUpdate";


    private static final String PASSWORD = "BBA__PASSWORD";
    private static final String USER_ID = "BBA_USER_ID";
    private static final String BBA_TYPE = "BBA_TYPE";
    public static String CALL_STATUS = "callstatus";
    private static final String USER = "BBA_USER";
    private static final String EMPLOYEEID = "BBA_EMPLOYEEID";
    private static final String EMPLOYEENAME = "BBA_EMPLOYEENAME";
    private static final String EMPLOYEECODE = "BBA_EMPLOYEECODE";
    private static final String PHONENO1 = "BBA_PHONENO1";
    private static final String PHONENO2 = "BBA_PHONENO2";
    private static final String EMAILID = "BBA_EMAILID";
    private static final String BRANCHNAME = "BBA_BRANCHNAME";
    private static final String COMPANYNAME = "BBA_COMPANYNAME";
    private static final String LATTUTUDE = "BBA_LATTUTUDE";
    private static final String LONITUDE = "BBA_LONITUDE";
    private static final String ATTENDANCEDATE = "BBA_ATTENDANCEDATE";


    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }





    public void setIsProductMasterUpdate(boolean isFirstTime) {
        editor.putBoolean(IS_PRODUCT_MASTER_UPDATE, isFirstTime);
        editor.commit();
    }

    public boolean IsProductMasterUpdate() {
        return pref.getBoolean(IS_PRODUCT_MASTER_UPDATE, true);
    }


    public  void setUser(String user)
    {
        editor.putString(USER, user);

        editor.commit();
    }

    public String getUser() {
        return pref.getString(USER, "");
    }




    public  void setUserID(String userId)
    {
        editor.putString(USER_ID, userId);

        editor.commit();
    }

    public String getUserID() {
        return pref.getString(USER_ID, "0");
    }

    public  void setType(String type)
    {
        editor.putString(BBA_TYPE, type);

        editor.commit();
    }

    public String getType() {
        return pref.getString(BBA_TYPE, "");
    }


    public  void setPassword(String pwd)
    {

        editor.putString(PASSWORD, pwd);
        editor.commit();
    }

    public String getPassword() {
        return pref.getString(PASSWORD, "");
    }

    public  void setCallStatus(String callStatus)
    {

        editor.putString(CALL_STATUS, callStatus);
        editor.commit();
    }

    public  void setemployeename(String employeename)
    {
        editor.putString(EMPLOYEENAME, employeename);

        editor.commit();
    }

    public String getemployeename() {
        return pref.getString(EMPLOYEENAME, "");
    }


    public  void setemployeecode(String employeecode)
    {
        editor.putString(EMPLOYEECODE, employeecode);

        editor.commit();
    }

    public String getemployeecode() {
        return pref.getString(EMPLOYEECODE, "");
    }



    public  void setEmployeeid(String employeeId)
    {
        editor.putString(EMPLOYEEID, employeeId);

        editor.commit();
    }

    public String getEmployeeid() {
        return pref.getString(EMPLOYEEID, "");
    }



    public  void setphoneno1(String phoneno1)
    {
        editor.putString(PHONENO1, phoneno1);

        editor.commit();
    }

    public String getphoneno1() {
        return pref.getString(PHONENO1, "");
    }



    public  void setphoneno2(String phoneno2)
    {
        editor.putString(PHONENO2, phoneno2);

        editor.commit();
    }

    public String getphoneno2() {
        return pref.getString(PHONENO2, "");
    }


    public  void setemailid(String emailid)
    {
        editor.putString(EMAILID, emailid);

        editor.commit();
    }

    public String getemailid() {
        return pref.getString(EMAILID, "");
    }


    public  void setbranchname(String branchname)
    {
        editor.putString(BRANCHNAME, branchname);

        editor.commit();
    }

    public String getbranchname() {
        return pref.getString(BRANCHNAME, "");
    }


    public  void setcompanyname(String companyname)
    {
        editor.putString(COMPANYNAME, companyname);

        editor.commit();
    }

    public String getcompanyname() {
        return pref.getString(COMPANYNAME, "");
    }

    public String getCallStatus() {
        return pref.getString(CALL_STATUS, "");
    }



    public  void setLattitude(String lattitude)
    {
        editor.putString(LATTUTUDE, lattitude);

        editor.commit();
    }

    public String getLattitude() {
        return pref.getString(LATTUTUDE, "");
    }



    public  void setLongutide(String lattitude)
    {
        editor.putString(LONITUDE, lattitude);

        editor.commit();
    }

    public String getLongutide() {
        return pref.getString(LONITUDE, "");
    }


    public  void setAttendancedate(String date)
    {
        editor.putString(ATTENDANCEDATE, date);

        editor.commit();
    }

    public String getAttendancedate() {
        return pref.getString(ATTENDANCEDATE, "");
    }

    public void clearAll() {
        pref.edit().remove(USER)
                .remove(PASSWORD)
                .remove(USER_ID)
                .remove(CALL_STATUS)
                .remove(BBA_TYPE).commit();

    }


}
