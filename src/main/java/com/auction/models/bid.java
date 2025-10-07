package com.auction.models;
import java.time.LocalDateTime;

public class bid {

        // Fields
        private int bidId;
        private int playerId;
        private int teamId;
        private double bidAmount;
        private LocalDateTime timestamp;

        // Default constructor
        public  bid() {
        }

        // Parameterized constructor
        public bid(int bidId, int playerId, int teamId, double bidAmount, LocalDateTime timestamp) {
            this.bidId = bidId;
            this.playerId = playerId;
            this.teamId = teamId;
            this.bidAmount = bidAmount;
            this.timestamp = timestamp;
        }

        // Getters and Setters
        public int getBidId() { return bidId; }
        public void setBidId(int bidId) { this.bidId = bidId; }

        public int getPlayerId() { return playerId; }
        public void setPlayerId(int playerId) { this.playerId = playerId; }

        public int getTeamId() { return teamId; }
        public void setTeamId(int teamId) { this.teamId = teamId; }

        public double getBidAmount() { return bidAmount; }
        public void setBidAmount(double bidAmount) { this.bidAmount = bidAmount; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        // Optional: toString
        @Override
        public String toString() {
            return "Bid{" +
                    "bidId=" + bidId +
                    ", playerId=" + playerId +
                    ", teamId=" + teamId +
                    ", bidAmount=" + bidAmount +
                    ", timestamp=" + timestamp +
                    '}';
        }
}
