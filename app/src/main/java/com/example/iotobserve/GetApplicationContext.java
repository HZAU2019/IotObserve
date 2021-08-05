package com.example.iotobserve;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

public class GetApplicationContext extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        context = getApplicationContext();
    }
    public static Context getContext() {
        return context;
    }
}
