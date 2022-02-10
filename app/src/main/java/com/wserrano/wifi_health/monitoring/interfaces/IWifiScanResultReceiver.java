package com.wserrano.wifi_health.monitoring.interfaces;

import com.wserrano.wifi_health.monitoring.model.WifiData;

import java.util.Map;

public interface IWifiScanResultReceiver {
    void receiveWifiScanResult(Map<String, WifiData> networksDatas);
    void clearResults();
}
