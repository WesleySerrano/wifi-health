package com.wserrano.wifi_health;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wserrano.wifi_health.interfaces.IWifiScanResultReceiver;
import com.wserrano.wifi_health.model.WifiData;
import com.wserrano.wifi_health.receivers.WifiEventBroadcastReceiver;
import com.wserrano.wifi_health.services.WifiServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, IWifiScanResultReceiver {
    WifiManager wifiManager;
    Map<String, WifiData> networksDatas = new HashMap<>();

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

    private void updateNetworksList(Map<String, WifiData> networksDatas)
    {
        this.networksDatas.putAll(networksDatas);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>(networksDatas.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.id_spinner_network_selection);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        System.out.println("Network list");
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
        WifiServices.scanNetworks(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getAdapter().getItem(position).toString();

        System.out.println("/***********************************/");
        System.out.println(item);
        System.out.println("/***********************************/");

        TextView connectionNameTextView = (TextView)findViewById(R.id.textViewConnectionName);
        TextView frequencyTextView = (TextView)findViewById(R.id.textViewFrequency);
        TextView rssiTextView = (TextView)findViewById(R.id.textViewRSSI);

        StringBuffer connectionNamePrefix = new StringBuffer("SSID: ");
        StringBuffer frequencyPrefix = new StringBuffer("Frequency: ");
        StringBuffer rssiPrefix = new StringBuffer("RSSI: ");
        if(this.networksDatas.get(item) == null) return;
        connectionNamePrefix.append(this.networksDatas.get(item).getWifiName());
        frequencyPrefix.append(this.networksDatas.get(item).getFrequency());
        rssiPrefix.append(this.networksDatas.get(item).getSignalStrength());

        connectionNameTextView.setText(connectionNamePrefix.toString());
        frequencyTextView.setText(frequencyPrefix.toString());
        rssiTextView.setText(rssiPrefix.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void receiveWifiScanResult(Map<String, WifiData> networksDatas) {
        this.updateNetworksList(networksDatas);
    }
}