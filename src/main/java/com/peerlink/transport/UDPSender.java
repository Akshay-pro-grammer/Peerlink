package com.peerlink.transport;

import java.net.*;

public class UDPSender {

    private DatagramSocket socket;
    static int seq=0;

    public UDPSender() {
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String message, InetAddress address, int port) {
        try {
            byte[] data = (seq+"|"+message).getBytes();
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