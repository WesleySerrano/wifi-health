package com.wserrano.wifi_health.interfaces;

import com.wserrano.wifi_health.model.WifiData;

import java.util.Map;

public interface IWifiScanResultReceiver {
    void receiveWifiScanResult(Map<String, WifiData> networksDatas);
}
