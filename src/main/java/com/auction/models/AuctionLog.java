package com.auction.models;

public class AuctionLog {
    private int playerId;
    private int winningTeamId;
    private double price;

    // Default constructor
    public AuctionLog() {
    }

    // Parameterized constructor
    public AuctionLog(int playerId, int winningTeamId, double price) {
        this.playerId = playerId;
        this.winningTeamId = winningTeamId;
        this.price = price;
    }

    // Getters and Setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public int getWinningTeamId() { return winningTeamId; }
    public void setWinningTeamId(int winningTeamId) { this.winningTeamId = winningTeamId; }

    public double getPrice() {
        return price; }
    public void setPrice(double price) {
        this.price = price;
    }

    // Optional: toString
    @Override
    public String toString() {
        return "AuctionLog{" +
                "playerId=" + playerId +
                ", winningTeamId=" + winningTeamId +
                ", price=" + price +
                '}';
    }

}
