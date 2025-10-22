package com.auction.main;

import com.auction.models.*;
import com.auction.dao.*;
import com.auction.auction.AuctionManager;

import java.util.Scanner;

public class Main {

    private static final User ADMIN = new User(1, "admin", "admin123", Role.ADMIN, 0);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AuctionManager auctionManager = new AuctionManager();
        PlayerDAO playerDAO = new PlayerDAO();
        TeamDAO teamDAO = new TeamDAO();

        // ✅ Setup sample data
        //setupSampleData();

        System.out.println("=== Welcome to the Auction App ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!login(username, password)) {
            System.out.println("❌ Invalid credentials. Exiting...");
            return;
        }

        System.out.println("✅ Login successful! Welcome, " + username);

        // Main menu
        while (true) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Start Player Auction");
            System.out.println("2. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    auctionManager.startAuction();
                    break;
                case 2:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("❌ Invalid option! Try again.");
            }
        }
    }

    // Hardcoded login check
    private static boolean login(String username, String password) {
        return ADMIN.getUsername().equals(username) && ADMIN.getPassword().equals(password);
    }

    // ✅ Setup sample teams and players
   /* private static void setupSampleData() {
        TeamDAO teamDAO = new TeamDAO();
        PlayerDAO playerDAO = new PlayerDAO();

        // Create teams if not exists
        String[] teamNames = {"Sharks", "Tigers", "Eagles"};

        for (String name : teamNames) {
            if (teamDAO.getTeamIdByName(name) == -1) {
                try {
                    var conn = com.auction.db.DBConnection.getConnection();
                    var ps = conn.prepareStatement("INSERT INTO teams (name, budget) VALUES (?, ?)");
                    ps.setString(1, name);
                    ps.setDouble(2, 100.0); // initial budget
                    ps.executeUpdate();
                    ps.close();
                    conn.close();
                    System.out.println("Created team: " + name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Create sample players
        Player[] players = {
                new Player(0, "Alice", "Forward", teamDAO.getTeamIdByName("Sharks")),
                new Player(0, "Bob", "Defender", teamDAO.getTeamIdByName("Tigers")),
                new Player(0, "Charlie", "Midfielder", teamDAO.getTeamIdByName("Eagles"))
        };

        for (Player p : players) {
            if (playerDAO.getPlayerById(p.getPlayerId()) == null) {
                boolean inserted = playerDAO.addPlayer(p);
                if (inserted) {
                    System.out.println("Created player: " + p.getName());
                }
            }
        }
    }*/
}
