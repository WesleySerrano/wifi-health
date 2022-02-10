package com.wserrano.wifi_health.monitoring.model;

import android.net.wifi.ScanResult;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class WifiData {

    private String wifiName;
    private Double signalStrength;
    private Double distance;
    private Double frequency;
    private Boolean is80211mc;
    private Double channelWidth;

    private Double centimetersDistance;


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
        this.channelWidth = Double.valueOf(scanResult.channelWidth);
        this.is80211mc = scanResult.is80211mcResponder();

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

    public Double getCentimetersDistance() {
        return centimetersDistance;
    }

    public void setCentimetersDistance(Double centimetersDistance) {
        this.centimetersDistance = centimetersDistance;
    }

    public Boolean getIs80211mc() {
        return is80211mc;
    }

    public void setIs80211mc(Boolean is80211mc) {
        this.is80211mc = is80211mc;
    }

    public Double getChannelWidth() {
        return channelWidth;
    }

    public void setChannelWidth(Double channelWidth) {
        this.channelWidth = channelWidth;
    }
}
