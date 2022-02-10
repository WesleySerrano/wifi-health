package com.wserrano.wifi_health.monitoring.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;

import com.wserrano.WifiHealth;
import com.wserrano.wifi_health.monitoring.interfaces.IWifiScanResultReceiver;
import com.wserrano.wifi_health.monitoring.model.WifiData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void scanNetworks(IWifiScanResultReceiver receiver)
    {
        WifiManager wifiManager = (WifiManager) WifiHealth.getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Map<String, WifiData> networksDatas = new HashMap<>();
        receiver.clearResults();

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    System.out.println("Scan success");
                } else {
                    // scan failure handling
                    System.out.println("Scan failure");
                }

                List<ScanResult> scanResults = wifiManager.getScanResults();

                for(ScanResult result : scanResults)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        networksDatas.put(result.SSID, new WifiData(result));
                    }
                }

                receiver.receiveWifiScanResult(networksDatas);
            }

        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        WifiHealth.getAppContext().registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if(!success)
        {
        }
    }
}
