package com.wserrano.wifi_health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.wserrano.wifi_health.receivers.WifiEventBroadcastReceiver;
import com.wserrano.wifi_health.services.WifiServices;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private void updateConnectionStatus(){
        TextView tv = findViewById(R.id.id_connection_test);

        tv.setText(WifiServices.getInstance().isNetworkConnected() ? "Connnected" : "Not connected");
    }

    private void registerIntents(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);

        Handler handler = new Handler();

        WifiEventBroadcastReceiver receiver = new WifiEventBroadcastReceiver(handler);

        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateConnectionStatus();
        registerIntents();
    }
}