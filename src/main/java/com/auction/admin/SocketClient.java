package com.auction.admin;

import java.io.*;
import java.net.Socket;

public class SocketClient {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private AdminUI adminUI;

    public SocketClient(String host, int port, AdminUI adminUI) {
        this.adminUI = adminUI;
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Start listening
            new Thread(this::listen).start();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to connect to auction server.");
        }
    }

    public void sendMessage(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                handleMessage(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(String msg) {
        String[] parts = msg.split(":");
        switch (parts[0]) {
            case "BID":
                int teamId = Integer.parseInt(parts[1]);
                double amount = Double.parseDouble(parts[2]);
                adminUI.updateHighestBid(amount, "Team " + teamId);
                break;
            case "SOLD":
                String playerName = parts[1];
                String teamName = parts[2];
                double bidAmount = Double.parseDouble(parts[3]);
                adminUI.addAuctionLog(playerName, teamName, bidAmount);
                int soldTeamId = Integer.parseInt(teamName.replace("Team ", "")) - 1;
                adminUI.updateTeamBudget(soldTeamId, adminUI.getTeamBudget(soldTeamId) - bidAmount);
                break;
            default:
                System.out.println("Unknown message: " + msg);
        }
    }
}
