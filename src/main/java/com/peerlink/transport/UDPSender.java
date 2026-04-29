package com.peerlink.transport;

import java.net.*;

public class UDPSender {

    private DatagramSocket socket;

    public UDPSender() {
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message, InetAddress address, int port) {
        try {
            byte[] data = message.getBytes();
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