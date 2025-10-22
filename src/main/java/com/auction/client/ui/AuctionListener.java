package com.auction.client.ui;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Map;

public class AuctionListener extends Thread {

    private final Socket socket;
    private final AuctionUI auctionUI;
    private boolean running = true;

    public AuctionListener(Socket socket, AuctionUI auctionUI) {
        this.socket = socket;
        this.auctionUI = auctionUI;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            while (running) {
                Object obj = in.readObject();
                if (!(obj instanceof Map)) continue;

                @SuppressWarnings("unchecked")
                Map<String, Object> message = (Map<String, Object>) obj;

                String type = (String) message.get("type");

                switch (type) {
                    case "PLAYER_UPDATE" -> handlePlayerUpdate(message);
                    case "BID_UPDATE" -> handleBidUpdate(message);
                    case "PLAYER_SOLD" -> handlePlayerSold(message);
                    default -> System.out.println("Unknown message: " + message);
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Listener stopped: " + e.getMessage());
        }
    }

    private void handlePlayerUpdate(Map<String, Object> msg) {
        String name = (String) msg.get("playerName");
        String position = (String) msg.get("position");
        double basePrice = ((Number) msg.get("basePrice")).doubleValue();
        double highestBid = ((Number) msg.getOrDefault("highestBid", 0)).doubleValue();
        String highestBidder = (String) msg.getOrDefault("highestBidder", "-");

        auctionUI.updatePlayer(name, position, basePrice, highestBid, highestBidder);
    }

    private void handleBidUpdate(Map<String, Object> msg) {
        double highestBid = ((Number) msg.get("highestBid")).doubleValue();
        String highestBidder = (String) msg.get("highestBidder");

        auctionUI.updatePlayerFields(highestBid, highestBidder);
    }

    private void handlePlayerSold(Map<String, Object> msg) {
        String playerName = (String) msg.get("playerName");
        String winner = (String) msg.get("winner");
        double amount = ((Number) msg.get("amount")).doubleValue();

        auctionUI.showSoldMessage(playerName, winner, amount);
    }

    public void stopListener() {
        running = false;
        try {
            socket.close();
        } catch (Exception ignored) {}
    }
}
