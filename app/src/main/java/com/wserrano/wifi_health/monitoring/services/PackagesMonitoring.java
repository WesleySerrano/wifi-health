package com.wserrano.wifi_health.monitoring.services;

import com.wserrano.wifi_health.monitoring.model.PacketArrivalHandler;

import net.sourceforge.jpcap.capture.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PackagesMonitoring {
    private static final int INFINITE = -1;
    private static final int PACKET_COUNT = INFINITE;
  /*
    private static final String HOST = "203.239.110.20";
    private static final String FILTER =
      "host " + HOST + " and proto TCP and port 23";
  */

    private static final String FILTER =
            // "port 23";
            "";

    private List<String> networkDevicesNames;

    public PackagesMonitoring(){
        this.networkDevicesNames = new ArrayList<>();

        try {
            for (Enumeration<NetworkInterface> en =
                 NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface networkInterface = en.nextElement();
                networkDevicesNames.add(networkInterface.getName());
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        System.out.println(this.networkDevicesNames);
    }

    public void monitorPackages(){
        PacketCapture pcap = new PacketCapture();
        System.out.println("Using device '" + this.networkDevicesNames + "'");
        try{
        for(String deviceName : this.networkDevicesNames)
        {
            pcap.open(deviceName, true);
            pcap.setFilter(FILTER, true);
            pcap.addPacketListener(new PacketArrivalHandler());

            System.out.println("Capturing packets...");
            pcap.capture(PACKET_COUNT);
        }

        } catch (CaptureDeviceOpenException e) {
            e.printStackTrace();
        } catch (InvalidFilterException e) {
            e.printStackTrace();
        } catch (CapturePacketException e) {
            e.printStackTrace();
        }
    }
}
