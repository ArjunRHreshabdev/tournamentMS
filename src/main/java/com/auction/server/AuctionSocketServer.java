package com.auction.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AuctionSocketServer
 * ---------------------
 * Handles live connections from Team Managers.
 * Broadcasts current player and bids in real time.
 */
public class AuctionSocketServer {

    // List of all connected managers
    private static final List<Socket> clients = new CopyOnWriteArrayList<>();

    // Track current auction state
    private static String currentPlayer = "";
    private static double currentBid = 0.0;
    private static String highestBidder = "None";

    public static void main(String[] args) {
        int port = 9090; // separate from REST API port
        System.out.println("⚡ Live Auction Server started on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                clients.add(client);
                System.out.println("✅ Team Manager connected: " + client.getInetAddress());

                // Start a listener thread for this client
                new Thread(() -> handleClient(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("📩 Received: " + message);

                if (message.startsWith("BID:")) {
                    // e.g., BID:600000:Team Alpha
                    String[] parts = message.split(":");
                    if (parts.length >= 3) {
                        double bidAmount = Double.parseDouble(parts[1]);
                        String teamName = parts[2];

                        if (bidAmount > currentBid) {
                            currentBid = bidAmount;
                            highestBidder = teamName;
                            broadcast("NEW_BID:" + teamName + ":" + bidAmount);
                        } else {
                            sendToClient(client, "ERROR:Bid too low");
                        }
                    }
                }
                else if (message.startsWith("START:")) {
                    // e.g., START:Virat Kohli:Batsman:500000
                    String[] parts = message.split(":");
                    if (parts.length >= 4) {
                        currentPlayer = parts[1];
                        currentBid = Double.parseDouble(parts[3]);
                        highestBidder = "None";
                        broadcast("CURRENT_PLAYER:" + parts[1] + ":" + parts[2] + ":" + parts[3]);
                    }
                }
                else if (message.equals("SOLD")) {
                    broadcast("SOLD:" + currentPlayer + ":" + highestBidder + ":" + currentBid);
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Client disconnected.");
        } finally {
            clients.remove(client);
        }
    }

    private static void broadcast(String msg) {
        for (Socket s : clients) {
            try {
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("📢 Broadcast: " + msg);
    }

    private static void sendToClient(Socket client, String msg) {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            out.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
