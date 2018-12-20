package com.cloudhr.attendancepoc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyLocationService extends Service {
    public MyLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
