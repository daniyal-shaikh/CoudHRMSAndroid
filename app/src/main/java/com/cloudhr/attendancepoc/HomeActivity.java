package com.cloudhr.attendancepoc;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudhr.attendancepoc.Utility.LocationTracker;
import com.cloudhr.attendancepoc.Utility.Utility;
import com.cloudhr.attendancepoc.attandance.AttendanceAdapter;
import com.cloudhr.attendancepoc.attandance.OutDoorAttendanceActivity;
import com.cloudhr.attendancepoc.core.APIResponse;
import com.cloudhr.attendancepoc.core.IResponseSubcriber;
import com.cloudhr.attendancepoc.core.controller.ProductController;
import com.cloudhr.attendancepoc.core.model.SwipeDetailEntity;
import com.cloudhr.attendancepoc.core.response.SwipeDetailResponse;
import com.cloudhr.attendancepoc.dbControl.PrefManager;
import com.cloudhr.attendancepoc.service.AttendanceNotificationService;
import com.cloudhr.attendancepoc.service.MyLocationService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class HomeActivity extends BaseActivity implements View.OnClickListener ,IResponseSubcriber {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    public static int navItemIndex = 0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String loadcalled ="1";

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    TextView textNotifyItemCount, txtEmail, txtName;

    private static final String TAG_HOME = "Home";
    private static final String TAG_PROFILE = "Profile";
    private static final String TAG_ORDER = "Request Detail";
    private static final String TAG_DOC = "Document Upload";
    private static final String TAG_ABOUT = "About US";
    private static final String TAG_TERMS = "Terms And Condition";
    private static final String TAG_CHANGE_PWD = "ChangePassword";
    public static String CURRENT_TAG = TAG_HOME;

    LocationTracker Loc;
    boolean doubleBackToExitPressedOnce = false;
    String lat, lon;
    Button btnAttendIN, btnAttendOUT;

    RecyclerView rvAttendace;
    List<SwipeDetailEntity> lstAttendance;
    AttendanceAdapter mAdapter;
    String AttendTyp = "";

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        prefManager= new PrefManager(this);
//        startService(new Intent(this, MyLocationService.class));
        setUpNavigationView();
        initialize();
        Toast.makeText(HomeActivity.this, "Welcome "+prefManager.getemployeename(), Toast.LENGTH_SHORT).show();
        Loc = new LocationTracker(HomeActivity.this);
        Loc.init();
        new ProductController(HomeActivity.this).getSwipe(Integer.parseInt(prefManager.getEmployeeid()),lat,lon,"",this);
    }


    private void initialize() {
        btnAttendIN = (Button) findViewById(R.id.btnAttIn);
        btnAttendOUT = (Button) findViewById(R.id.btnAttOut);
        rvAttendace = (RecyclerView) findViewById(R.id.rvAttendace);
        rvAttendace.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
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
            if (Utility.checkInternetStatus(HomeActivity.this)) {
                loadcalled="0";
                GetMyAttendance("IN");

                AttendTyp = "IN";
            } else {
                Snackbar.make(btnAttendOUT, "Check your internet connection", Snackbar.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnAttOut) {
            if (Utility.checkInternetStatus(HomeActivity.this)) {
                loadcalled="0";
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
                        AttendTyp = "OUT";
                        new ProductController(HomeActivity.this).getSwipe(Integer.parseInt(prefManager.getEmployeeid()),lat,lon,"OUT",this);
                    } else {
                        PrefManager prefManager =new PrefManager(HomeActivity.this);
                        if(prefManager.getAttendancedate().equals(getCurrentDate())){
                            Toast.makeText(HomeActivity.this, "Attendance Already Marked", Toast.LENGTH_LONG).show();
                            cancelDialog();
                            return;
                        }
                        AttendTyp = "IN";
                        new ProductController(HomeActivity.this).getSwipe(Integer.parseInt(prefManager.getEmployeeid()),lat,lon,"IN",this);
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
                        new ProductController(HomeActivity.this).getSwipe(Integer.parseInt(prefManager.getEmployeeid()),lat,lon,"OUT",this);
                    } else {
                        new ProductController(HomeActivity.this).getSwipe(Integer.parseInt(prefManager.getEmployeeid()),lat,lon,"IN",this);
                    }

                } else {
                    //Snackbar.make(btnAttendIN, "Location Not Found, Please Try Again", Snackbar.LENGTH_SHORT).show();
                    showGoogleMap();
                }

            }
        }
    }

    private String getCurrentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return  formattedDate;
    }
    private void showGoogleMap() {

        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Oops! Your location not found.");

            builder.setMessage("Kindly go to google map , set your current location and try again.");

            String positiveText = "OK";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 19.0857745, 72.8883218);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            HomeActivity.this.startActivity(intent);

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
                if(loadcalled.equals("0")) {

                    ComponentName comp = new ComponentName(HomeActivity.this.getPackageName(),
                            AttendanceNotificationService.class.getName());
                    startWakefulService(HomeActivity.this, (HomeActivity.this.getIntent().setComponent(comp)));
                }
                lstAttendance = ((SwipeDetailResponse) response).getData();

                mAdapter = new AttendanceAdapter(HomeActivity.this, lstAttendance);
                rvAttendace.setAdapter(mAdapter);
                if(AttendTyp.equals("IN")){
                    prefManager.setAttendancedate(getCurrentDate());
                }

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

       // Loc = new LocationTracker(HomeActivity.this);
//        Loc.init();

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
    private void setUpNavigationView() {
        navigationView.setItemIconTintList(null);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_prod:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

//
                    case R.id.nav_profile:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PROFILE;
                        break;

                  //  case R.id.nav_order:
//                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_ORDER;
                        //startActivity(new Intent(HomeActivity.this,DemActivity.class));
                  //      break;

//                    case R.id.nav_doc:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_DOC;
//                        break;

                    case R.id.nav_change_pwd:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_CHANGE_PWD;
                        break;

                    case R.id.nav_about_us:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ABOUT;

                        break;

                    case R.id.nav_terms:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_TERMS;
                        break;

                    case R.id.nav_logout:

                        //dataBaseController.logout();
                       // prefManager.clearUserCache();
                       // clear();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        break;

                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;


                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

               // loadHomeFragment(CURRENT_TAG);
                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

}
