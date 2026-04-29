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
                Long timestamp=Long.parseLong(msg);

                System.out.println("Received: " + timestamp+" latency:"+(System.currentTimeMillis()-timestamp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            receiver.close();
        }
    }
}