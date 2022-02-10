package com.wserrano.wifi_health.monitoring.model;

import net.sourceforge.jpcap.capture.PacketListener;
import net.sourceforge.jpcap.net.Packet;
import net.sourceforge.jpcap.net.TCPPacket;

public class PacketArrivalHandler implements PacketListener
{
    @Override
    public void packetArrived(Packet packet) {
        try {
            // only handle TCP packets

            if(packet instanceof TCPPacket) {
                TCPPacket tcpPacket = (TCPPacket)packet;
                byte[] data = tcpPacket.getTCPData();

                String srcHost = tcpPacket.getSourceAddress();
                String dstHost = tcpPacket.getDestinationAddress();
                String isoData = new String(data, "ISO-8859-1");

                System.out.println(srcHost+" -> " + dstHost + ": " + isoData);
            }
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
}
