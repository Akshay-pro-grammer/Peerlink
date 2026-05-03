package com.peerlink.transport;

import java.net.*;

public class UDPReceiver {

    private DatagramSocket socket;
    static int lastseq = -1;

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

            String msgString = new String(packet.getData(), 0, packet.getLength());
            String[] parts = msgString.split("\\|"); // escape the pipe for regex
            int seq = Integer.parseInt(parts[0]);
            int packetlost = 0;
            // Fix: always update lastseq
            if (lastseq + 1 == seq) {
                lastseq = seq;
            } else {
                packetlost = Math.abs(seq - lastseq) - 1; // -1 because gap is seq - lastseq - 1
                lastseq = seq; // ← add this
            }
            return msgString + "|packetLost:" + packetlost;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        socket.close();
    }
}