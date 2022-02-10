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

import com.wserrano.wifi_health.monitoring.interfaces.IWifiScanResultReceiver;
import com.wserrano.wifi_health.monitoring.model.WifiData;
import com.wserrano.wifi_health.monitoring.receivers.WifiEventBroadcastReceiver;
import com.wserrano.wifi_health.monitoring.services.WifiServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        WifiEventBroadcastReceiver receiver = new WifiEventBroadcastReceiver(handler, this);

        registerReceiver(receiver, intentFilter);
    }

    private void updateNetworksList(Map<String, WifiData> networksDatas)
    {
        this.clearTexts();
        this.networksDatas.clear();
        if(networksDatas != null && networksDatas.size() > 0) this.networksDatas.putAll(networksDatas);

        List<String> adapterEntries = (networksDatas != null) ? new ArrayList<String>(networksDatas.keySet()) : new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adapterEntries);
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

    private void clearTexts()
    {
        TextView connectionNameTextView = (TextView)findViewById(R.id.textViewConnectionName);
        TextView frequencyTextView = (TextView)findViewById(R.id.textViewFrequency);
        TextView rssiTextView = (TextView)findViewById(R.id.textViewRSSI);
        TextView channelWidthTextView = (TextView)findViewById(R.id.textViewChannelWidth);
        TextView is80211mcResponderTextView = (TextView)findViewById(R.id.textViewIs80211mcResponder);

        StringBuffer connectionNamePrefix = new StringBuffer("SSID: -");
        StringBuffer frequencyPrefix = new StringBuffer("Frequency: -");
        StringBuffer rssiPrefix = new StringBuffer("RSSI: -");
        StringBuffer channelWidth = new StringBuffer("Channel Width: -");
        StringBuffer is80211mc = new StringBuffer("Is 802.11mc responder: -");

        connectionNameTextView.setText(connectionNamePrefix.toString());
        frequencyTextView.setText(frequencyPrefix.toString());
        rssiTextView.setText(rssiPrefix.toString());
        channelWidthTextView.setText(channelWidth.toString());
        is80211mcResponderTextView.setText(is80211mc.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getAdapter().getItem(position).toString();

        TextView connectionNameTextView = (TextView)findViewById(R.id.textViewConnectionName);
        TextView frequencyTextView = (TextView)findViewById(R.id.textViewFrequency);
        TextView rssiTextView = (TextView)findViewById(R.id.textViewRSSI);
        TextView channelWidthTextView = (TextView)findViewById(R.id.textViewChannelWidth);
        TextView is80211mcResponderTextView = (TextView)findViewById(R.id.textViewIs80211mcResponder);

        StringBuffer connectionNamePrefix = new StringBuffer("SSID: ");
        StringBuffer frequencyPrefix = new StringBuffer("Frequency: ");
        StringBuffer rssiPrefix = new StringBuffer("RSSI: ");
        StringBuffer channelWidth = new StringBuffer("Channel Width: ");
        StringBuffer is80211mc = new StringBuffer("Is 802.11mc responder: ");

        if(this.networksDatas.get(item) != null)
        {
            connectionNamePrefix.append(this.networksDatas.get(item).getWifiName());
            frequencyPrefix.append(this.networksDatas.get(item).getFrequency());
            rssiPrefix.append(this.networksDatas.get(item).getSignalStrength());
            channelWidth.append(this.networksDatas.get(item).getChannelWidth());
            is80211mc.append(this.networksDatas.get(item).getIs80211mc() ? "Yes" : "No");
        }

        connectionNameTextView.setText(connectionNamePrefix.toString());
        frequencyTextView.setText(frequencyPrefix.toString());
        rssiTextView.setText(rssiPrefix.toString());
        channelWidthTextView.setText(channelWidth.toString());
        is80211mcResponderTextView.setText(is80211mc.toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void receiveWifiScanResult(Map<String, WifiData> networksDatas) {
        this.updateNetworksList(networksDatas);
    }

    @Override
    public void clearResults() {
        this.updateNetworksList(null);
    }


}