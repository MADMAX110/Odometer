package com.hafd.odometer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class OdometerService extends Service {

    private static double distanceInMeters;
    private static Location lastLocation = null;
    private final IBinder binder = new OdometerBinder();
    private LocationListener listener;
    private LocationManager locManager;
    public static final String PERMISSION_STRIMG =
            Manifest.permission.ACCESS_FINE_LOCATION;

    //创建一个绑定式服务时，需要提供一个Binder实现
    public class OdometerBinder extends Binder {
        //活动将使用这个方法得到OdometerService的一个引用
        OdometerService getOdometer() {
            return OdometerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public double getDistance() {
        return this.distanceInMeters;
    }

    @SuppressLint("ServiceCast")
    @Override
    public void onCreate() {
        super.onCreate();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location == null)lastLocation = location;
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }

        };
        locManager = (LocationManager) getSystemService(Context.LOCALE_SERVICE);

        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRIMG)
                        == PackageManager.PERMISSION_GRANTED){
            String provider = locManager.getBestProvider(new Criteria(), true);
            if (provider != null)
                locManager.requestLocationUpdates(provider, 1000, 1, listener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locManager != null && listener != null) {
            if (ContextCompat.checkSelfPermission(this, PERMISSION_STRIMG)
                    == PackageManager.PERMISSION_GRANTED){
                locManager.removeUpdates(listener);
            }
            locManager = null;
            listener = null;
        }
    }
}