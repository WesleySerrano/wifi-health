package com.wserrano.wifi_health.services;

import android.content.Context;
import android.net.ConnectivityManager;

import com.wserrano.WifiHealth;
public class WifiServices {

    private static WifiServices instance;

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WifiHealth
                .getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static WifiServices getInstance(){
        if(instance == null) instance = new WifiServices();

        return instance;
    }
}
