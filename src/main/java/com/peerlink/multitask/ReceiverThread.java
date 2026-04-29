package com.peerlink.multitask;

import com.peerlink.session.SessionPayload;
import com.peerlink.transport.UDPReceiver;

public class ReceiverThread extends Thread {

    private SessionPayload payload;

    public ReceiverThread(SessionPayload payload) {
        this.payload = payload;
    }

    @Override
    public void run() {

        UDPReceiver receiver = new UDPReceiver(payload.getVideoPort());

        try {
            while (true) {
                String msg = receiver.receive();

                if (msg == null) continue;

                System.out.println("Received: " + msg);

                if ("bye".equals(msg)) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            receiver.close();
        }
    }
}