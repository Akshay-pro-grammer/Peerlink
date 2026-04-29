package com.peerlink.networking;

import java.io.*;
import java.net.*;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server: " + address);

            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    public String readMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}