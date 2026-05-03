package com.peerlink.transport;

import java.net.*;

public class UDPReceiver {

    private DatagramSocket socket;
    private int lastSeq = -1;

    public UDPReceiver(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String receive() {
        try {
            byte[] buffer = new byte[65535];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            String msg = new String(packet.getData(), 0, packet.getLength());

            String[] parts = msg.split("\\|");

            int seq = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);

            int packetLost = 0;
            String status = "OK";

            if (lastSeq == -1) {
                // first packet
                lastSeq = seq;
            } 
            else if (seq == lastSeq + 1) {
                // perfect order
                lastSeq = seq;
            } 
            else if (seq > lastSeq + 1) {
                // packet loss
                packetLost = seq - lastSeq - 1;
                lastSeq = seq;
                status = "LOSS";
            } 
            else {
                // old or duplicate packet
                status = "IGNORED";
                return "SEQ:" + seq + " | " + status;
            }

            long latency = System.currentTimeMillis() - timestamp;

            return "SEQ:" + seq +
                    " | latency:" + latency + " ms" +
                    " | lost:" + packetLost +
                    " | " + status;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        socket.close();
    }
}