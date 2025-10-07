package com.auction.auction;

import com.auction.dao.PlayerDAO;
import com.auction.dao.AuctionLogDAO;
import com.auction.dao.TeamDAO;
import com.auction.models.Player;

import java.util.List;
import java.util.Scanner;

public class AuctionManager {

    private final PlayerDAO playerDAO = new PlayerDAO();
    private final AuctionLogDAO auctionLogDAO = new AuctionLogDAO();
    private final TeamDAO teamDAO = new TeamDAO(); // ✅ added this
    private final Scanner scanner = new Scanner(System.in);

    public void startAuction() {
        System.out.println("=== Player Auction ===");
        List<Player> players = playerDAO.getAllPlayers();

        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }

        // Display all players
        System.out.println("\nAvailable Players:");
        for (Player p : players) {
            System.out.println(p.getPlayerId() + ". " + p.getName() + " - " + p.getPosition());
        }

        System.out.print("\nEnter Player ID to start auction: ");
        int playerId = scanner.nextInt();

        Player player = playerDAO.getPlayerById(playerId);
        if (player == null) {
            System.out.println("Invalid player ID!");
            return;
        }

        // 🔹 Step 2 integrated here — check if player already sold
        if (auctionLogDAO.isPlayerSold(player.getPlayerId())) {
            System.out.println("⚠️ Player " + player.getName() + " has already been sold. Skipping auction.");
            return;
        }

        // If not sold, start the auction normally
        conductAuction(player);
    }

    private void conductAuction(Player player) {
        System.out.println("\n🎯 Starting auction for: " + player.getName());
        System.out.println("(Type 'end' to finish auction)\n");

        double highestBid = 0;
        String highestBidder = "";
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter Team Name (or 'end' to stop): ");
            String teamName = scanner.nextLine();

            if (teamName.equalsIgnoreCase("end")) {
                break;
            }

            System.out.print("Enter Bid Amount: ");
            double bid = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            if (bid > highestBid) {
                highestBid = bid;
                highestBidder = teamName;
                System.out.println("✅ New Highest Bid: " + bid + " by " + teamName);
            } else {
                System.out.println("❌ Bid must be higher than current highest bid!");
            }
        }

        if (!highestBidder.isEmpty()) {
            System.out.println("\n🏆 " + highestBidder + " won " + player.getName() + " for " + highestBid);

            // ✅ Convert team name to teamId before saving
            int teamId = teamDAO.getTeamIdByName(highestBidder);
            if (teamId != -1) {
                auctionLogDAO.saveWinner(player.getPlayerId(), teamId, highestBid);
                System.out.println("✅ Winner saved successfully in auction log!");
            } else {
                System.out.println("⚠️ Could not find team: " + highestBidder + ". Winner not saved.");
            }

        } else {
            System.out.println("No valid bids placed for " + player.getName());
        }
    }
}
