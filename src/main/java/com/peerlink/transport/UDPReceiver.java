package com.peerlink.transport;

import java.net.*;

public class UDPReceiver {

    private DatagramSocket socket;

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
            DatagramPacket packet =
                    new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            return new String(packet.getData(), 0, packet.getLength());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        socket.close();
    }
}