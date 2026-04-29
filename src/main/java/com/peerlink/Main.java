package com.peerlink;

import com.google.common.hash.Hashing;
import com.peerlink.multitask.ReceiverThread;
import com.peerlink.multitask.SenderThread;
import com.peerlink.networking.Client;
import com.peerlink.networking.Server;
import com.peerlink.session.SessionPayload;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static String sharedSecret = "peerlinktesting";

    public static void main(String[] args) throws UnknownHostException {

        Scanner sc = new Scanner(System.in);

        System.out.println("1. Start Server");
        System.out.println("2. Connect to Server");
        System.out.print("Enter choice: ");

        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            // ───────── SERVER ─────────
            Server server = new Server(9000);
            server.acceptConnection();

            // AUTH
            String token = UUID.randomUUID().toString();
            server.sendMessage("CHALLENGE:" + token);

            String msg = server.readMessage();
            boolean authOk = false;

            if (msg != null && msg.startsWith("RESPONSE")) {
                String receivedHash = msg.split(":")[1];

                String expectedHash = Hashing.sha256()
                        .hashString(token + sharedSecret, StandardCharsets.UTF_8)
                        .toString();

                if (expectedHash.equals(receivedHash)) {
                    server.sendMessage("AUTH_OK");
                    authOk = true;
                    System.out.println("Client authenticated ✅");
                } else {
                    server.sendMessage("AUTH_FAIL");
                    // just a placeholder if auth is wrong so that program does not move ahead
                    System.out.println("[Server] Auth denied");
                    System.exit(0);
                }
            }

            // SESSION
            if (authOk) {

                SessionPayload myPayload = new SessionPayload(
                        5004,
                        5006,
                        randomSsrc(),
                        randomSsrc());

                server.sendMessage(myPayload.encode());

                SessionPayload clientPayload = SessionPayload.decode(server.readMessage());

                System.out.println("Client session: " + clientPayload);
                InetAddress clientAddr = server.getClientSocket().getInetAddress();
                SenderThread serverSenderThread =new SenderThread(clientPayload, clientAddr);
                serverSenderThread.start();
                ReceiverThread serverReceiverThread = new ReceiverThread(myPayload);
                serverReceiverThread.start();
                try {
                    serverSenderThread.join();
                    serverReceiverThread.join();
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }

            server.close();

        } else {
            // ───────── CLIENT ─────────
            System.out.print("Enter server IP: ");
            String ip = sc.nextLine();

            Client client = new Client(ip, 9000);

            boolean authOk = false;

            // AUTH
            String msg = client.readMessage();

            if (msg != null && msg.startsWith("CHALLENGE")) {
                String token = msg.split(":")[1];

                String hash = Hashing.sha256()
                        .hashString(token + sharedSecret, StandardCharsets.UTF_8)
                        .toString();

                client.sendMessage("RESPONSE:" + hash);

                String authResult = client.readMessage();
                if ("AUTH_OK".equals(authResult)) {
                    authOk = true;
                    System.out.println("Authenticated ✅");
                } else {
                    System.out.println("Auth denied");
                    // just a placeholder if auth is wrong so that program does not move ahead
                    System.exit(0);
                }
            }

            // SESSION
            if (authOk) {

                SessionPayload serverPayload = SessionPayload.decode(client.readMessage());

                SessionPayload myPayload = new SessionPayload(
                        6004,
                        6006,
                        randomSsrc(),
                        randomSsrc());

                client.sendMessage(myPayload.encode());

                System.out.println("Server session: " + serverPayload);


                InetAddress serverAddr = InetAddress.getByName(ip);

                SenderThread clientSenderThread =new SenderThread(serverPayload, serverAddr);
                clientSenderThread.start();
                ReceiverThread clientReceiverThread = new ReceiverThread(myPayload);
                clientReceiverThread.start();
                try {
                    clientSenderThread.join();
                    clientReceiverThread.join();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            client.close();
        }

        sc.close();
    }

    private static long randomSsrc() {
        return ThreadLocalRandom.current().nextLong(1, 0xFFFFFFFFL);
    }
}