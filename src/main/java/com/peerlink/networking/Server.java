package com.peerlink.networking;

import java.io.*;
import java.net.*;

public class Server {

    private int port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    
    public Socket getClientSocket() {
        return clientSocket;
    }

    private BufferedReader reader;
    private PrintWriter writer;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendMessage(String msg) {
        writer.println(msg);
    }

    public void close() {
        try {
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}