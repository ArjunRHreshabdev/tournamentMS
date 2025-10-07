package com.auction.models;

public class Player {
    private int playerId;
    private String name;
    private String position;
    private int teamId;   // FK to Team

    // Default constructor
    public Player() {
    }

    // Parameterized constructor
    public Player(int playerId, String name, String position, int teamId) {
        this.playerId = playerId;
        this.name = name;
        this.position = position;
        this.teamId = teamId;
    }

    // Getters and Setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    // Optional: toString
    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", teamId=" + teamId +
                '}';
    }
}
