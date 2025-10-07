package com.auction.models;

public class Team {
    // Fields matching the 'teams' table
    private int teamId;
    private String name;
    private double budget;

    // Default constructor
    public Team() {
    }

    // Parameterized constructor
    public Team(int teamId, String name, double budget) {
        this.teamId = teamId;
        this.name = name;
        this.budget = budget;
    }

    // Getters and Setters

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    // Optional: Override toString for easy printing
    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                '}';
    }
}
