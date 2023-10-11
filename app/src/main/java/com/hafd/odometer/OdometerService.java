package com.hafd.odometer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class OdometerService extends Service {

    private final IBinder binder = new OdometerBinder();
    private final Random random = new Random();

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
        return random.nextDouble();
    }
}