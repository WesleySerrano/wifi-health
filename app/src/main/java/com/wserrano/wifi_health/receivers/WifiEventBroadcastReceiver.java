package com.wserrano.wifi_health.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.widget.TextView;

import com.wserrano.wifi_health.R;

public class WifiEventBroadcastReceiver extends BroadcastReceiver {
    private Handler handler;

    public WifiEventBroadcastReceiver(Handler handler){
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String ACTION = intent.getAction();
        if(ACTION.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
            if(intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)){

                System.out.println("CONNECTED");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = ((Activity) context).findViewById(R.id.id_connection_test);
                        tv.setText("AAAA");
                    }
                });
            }
            else {
                System.out.println("NOT CONNECTED");
            }
        }
    }
}
