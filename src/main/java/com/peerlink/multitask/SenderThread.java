package com.peerlink.multitask;

import java.net.InetAddress;
// import java.util.Scanner;

import com.peerlink.session.SessionPayload;
import com.peerlink.transport.UDPSender;

public class SenderThread extends Thread {

    private SessionPayload payload;
    private InetAddress remoteAddress;
    // private static final Scanner sc = new Scanner(System.in);

    public SenderThread(SessionPayload payload, InetAddress remoteAddress) {
        this.payload = payload;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {

        UDPSender sender = new UDPSender();

        try {
            while (true) {
                // String message = sc.nextLine();
                String message = System.currentTimeMillis() + "";
                sender.send(message, remoteAddress, payload.getVideoPort());
                sleep(30);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sender.close();
        }
    }
}