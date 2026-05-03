package com.peerlink.transport;

import java.net.*;

public class UDPSender {

    private DatagramSocket socket;
    private int seq = 0;   // instance-level (better than static)

    public UDPSender() {
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message, InetAddress address, int port) {
        try {
            String payload = seq++ + "|" + message;

            byte[] data = payload.getBytes();
            DatagramPacket packet =
                    new DatagramPacket(data, data.length, address, port);

            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        socket.close();
    }
}