package com.peerlink.multitask;

import java.net.InetAddress;
import java.util.Scanner;

import com.peerlink.session.SessionPayload;
import com.peerlink.transport.UDPSender;

public class SenderThread extends Thread {

    private SessionPayload payload;
    private InetAddress remoteAddress;
    private static final Scanner sc = new Scanner(System.in);

    public SenderThread(SessionPayload payload, InetAddress remoteAddress) {
        this.payload = payload;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {

        UDPSender sender = new UDPSender();

        try {
            while (true) {
                String message = sc.nextLine();

                sender.send(message, remoteAddress, payload.getVideoPort());

                if ("bye".equals(message)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sender.close();
        }
    }
}