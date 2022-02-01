package com.wserrano.wifi_health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wserrano.WifiHealth;
import com.wserrano.wifi_health.receivers.WifiEventBroadcastReceiver;
import com.wserrano.wifi_health.services.WifiServices;

import java.net.UnknownHostException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    WifiManager wifiManager;

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

    private void updateNetworksList()
    {
        List<ScanResult> scanResults = wifiManager.getScanResults();

        Spinner spinner = (Spinner) findViewById(R.id.id_spinner_network_selection);
        List<String> networksNames = new ArrayList<>();

        for(ScanResult result : scanResults)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                networksNames.add(result.SSID);
            }
        }

        System.out.println("Network list");
    }

    private void scanSuccess() {
        updateNetworksList();
        System.out.println("Scan success");
    }
    private void scanFailure() {
        updateNetworksList();
        System.out.println("Scan failure");
    }

    private void listNetworks(){
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    // scan failure handling
                    scanFailure();
                }
            }

        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if(!success)
        {
            scanFailure();
        }
    }

    private void checkPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_WIFI_STATE},81);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CHANGE_WIFI_STATE},81);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},81);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},81);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        updateConnectionStatus();
        registerIntents();
        listNetworks();
    }
}