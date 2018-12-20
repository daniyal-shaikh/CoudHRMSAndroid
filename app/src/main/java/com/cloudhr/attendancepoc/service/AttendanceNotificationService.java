package com.cloudhr.attendancepoc.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.cloudhr.attendancepoc.HomeActivity;
import com.cloudhr.attendancepoc.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AttendanceNotificationService extends IntentService {

    private NotificationManager alarmNotificationManager;
    public static final String CHANNEL_ID = "com.cloudhr.attendancepoc.NotifyID";

    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 1;

    public AttendanceNotificationService() {
        super("AlarmNotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        //Send notification
        sendNotification("Attendance Marked Successfully....");
    }



    private NotificationManager getManager() {
        if (alarmNotificationManager == null) {
            alarmNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return alarmNotificationManager;
    }
    //handle notification
    private void sendNotification(String msg) {
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        createChannels();

        //get pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = null;
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);

        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setContentTitle("Attendance")
                .setContentText("Attendance Marked for today")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setTicker("Attendance")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setVisibility(NOTIFICATION_ID)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(contentIntent);


        getManager().notify(NOTIFICATION_ID, notificationBuilder.build());


        //Create notification
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Attendance").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setAutoCancel(true);
        alamNotificationBuilder.setContentIntent(contentIntent);

        //notiy notification manager about new notification
        alarmNotificationManager.notify(NOTIFICATION_ID, alamNotificationBuilder.build());
    }

    public static final String CHANNEL_NAME = "ATTENDANCE CHANNEL";
    public void createChannels() {
        if (Build.VERSION.SDK_INT >= 26) {


            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.BLUE);
            channel.setDescription("Attendance");
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);    // Notification.VISIBILITY_PRIVATE
            getManager().createNotificationChannel(channel);
        }
    }
}
