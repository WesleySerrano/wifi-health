package com.wserrano;

import android.app.Application;
import android.content.Context;

public class WifiHealth extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        WifiHealth.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return WifiHealth.context;
    }
}
