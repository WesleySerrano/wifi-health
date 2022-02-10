package com.wserrano.wifi_health.monitoring.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.TextView;

import com.wserrano.wifi_health.R;
import com.wserrano.wifi_health.monitoring.interfaces.IWifiScanResultReceiver;
import com.wserrano.wifi_health.monitoring.services.WifiServices;

public class WifiEventBroadcastReceiver extends BroadcastReceiver {
    private Handler handler;
    private IWifiScanResultReceiver wifiScanResultReceiver;

    public WifiEventBroadcastReceiver(Handler handler, IWifiScanResultReceiver wifiScanResultReceiver){
        this.handler = handler;
        this.wifiScanResultReceiver = wifiScanResultReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String ACTION = intent.getAction();
        if(ACTION.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
            WifiServices.scanNetworks(this.wifiScanResultReceiver);
            if(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){

                System.out.println("CONNECTED");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = ((Activity) context).findViewById(R.id.id_connection_test);
                        tv.setText("Connected");
                    }
                });
            }
            else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = ((Activity) context).findViewById(R.id.id_connection_test);
                        tv.setText("Not connected");
                    }
                });
            }
        }
    }
}
