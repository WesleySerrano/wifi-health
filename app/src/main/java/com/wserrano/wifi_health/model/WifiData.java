package com.wserrano.wifi_health.model;

import android.net.wifi.ScanResult;

public class WifiData {

    private String wifiName;
    private Double signalStrength;
    private Double distance;
    private Double frequency;


    public WifiData()
    {

    }

    public WifiData(String wifiName, Double signalStrength, Double distance, Double frequency) {
        this.wifiName = wifiName;
        this.signalStrength = signalStrength;
        this.distance = distance;
        this.frequency = frequency;
    }

    public WifiData(ScanResult scanResult)
    {
        this.wifiName = scanResult.SSID;
        this.signalStrength = Double.valueOf(scanResult.level);
        this.frequency = Double.valueOf(scanResult.frequency);
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public Double getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }
}
