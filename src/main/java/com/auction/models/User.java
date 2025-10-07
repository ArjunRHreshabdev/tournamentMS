package com.auction.models;

public class User {

    // Fields matching 'users' table
    private int userId;
    private String username;
    private String password;
    private Role role;     // enum for role
    private int teamId;    // foreign key to Team

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(int userId, String username, String password, Role role, int teamId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.teamId = teamId;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    // Optional: toString
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", teamId=" + teamId +
                '}';
    }
}
