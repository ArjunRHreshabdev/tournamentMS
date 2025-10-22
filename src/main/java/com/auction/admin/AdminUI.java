package com.auction.admin;

import com.auction.dao.PlayerDAO;
import com.auction.dao.AuctionLogDAO;
import com.auction.models.Player;

import javax.swing.*;
import java.awt.*;

public class AdminUI extends JFrame {

    private PlayerPanel playerPanel;
    private CurrentAuctionPanel currentAuctionPanel;
    private TeamPanel teamPanel;
    private AuctionLogPanel auctionLogPanel;
    private JButton startAuctionBtn;
    private JButton sellPlayerBtn;

    private AuctionLogDAO auctionLogDAO = new AuctionLogDAO();

    public AdminUI() {
        setTitle("Auction Admin Panel");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        playerPanel = new PlayerPanel();
        add(playerPanel, BorderLayout.WEST);

        currentAuctionPanel = new CurrentAuctionPanel();
        add(currentAuctionPanel, BorderLayout.CENTER);

        teamPanel = new TeamPanel();
        add(teamPanel, BorderLayout.EAST);

        auctionLogPanel = new AuctionLogPanel();
        auctionLogPanel.setPreferredSize(new Dimension(1200, 200));
        add(auctionLogPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        startAuctionBtn = new JButton("Start Auction");
        sellPlayerBtn = new JButton("Sell Player");
        buttonPanel.add(startAuctionBtn);
        buttonPanel.add(sellPlayerBtn);
        add(buttonPanel, BorderLayout.NORTH);

        startAuctionBtn.addActionListener(e -> {
            Player selected = playerPanel.getSelectedPlayer();
            if (selected != null) {
                currentAuctionPanel.setPlayer(selected.getName());
            } else {
                JOptionPane.showMessageDialog(this, "Select a player first!");
            }
        });

        sellPlayerBtn.addActionListener(e -> {
            Player selected = playerPanel.getSelectedPlayer();
            if (selected == null) return;

            double bidAmount = currentAuctionPanel.getHighestBidAmount();
            String bidder = currentAuctionPanel.getHighestBidder();
            if (bidAmount <= 0) {
                JOptionPane.showMessageDialog(this, "No bid placed yet!");
                return;
            }

            // Save auction winner
            auctionLogDAO.saveWinner(selected.getPlayerId(), Integer.parseInt(bidder.replace("Team ", "")), bidAmount);

            // Update Team budget
            int teamId = Integer.parseInt(bidder.replace("Team ", "")) - 1;
            double newBudget = teamPanel.getTeamBudget(teamId) - bidAmount;
            teamPanel.updateTeamBudget(teamId, newBudget);

            // Update AuctionLogPanel
            auctionLogPanel.addAuctionLog(selected.getName(), bidder, bidAmount);

            // Reset current auction
            currentAuctionPanel.reset();
        });

        setVisible(true);
    }

    public void updateHighestBid(double amount, String team) {
        currentAuctionPanel.updateHighestBid(amount, team);
    }


    public void addAuctionLog(String player, String team, double price) {
        auctionLogPanel.addAuctionLog(player, team, price);
    }

    public void updateTeamBudget(int teamId, double newBudget) {
        teamPanel.updateTeamBudget(teamId, newBudget);
    }

    public double getTeamBudget(int teamId) {
        return teamPanel.getTeamBudget(teamId);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminUI::new);
    }
}
