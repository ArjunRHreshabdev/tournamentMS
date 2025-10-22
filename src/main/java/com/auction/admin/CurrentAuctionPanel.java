package com.auction.admin;

import javax.swing.*;
import java.awt.*;

public class CurrentAuctionPanel extends JPanel {

    private JLabel playerLabel;
    private JLabel highestBidLabel;
    private JLabel highestBidderLabel;

    private double highestBid;
    private String highestBidder;
    private String playerName;

    public CurrentAuctionPanel() {
        setLayout(new GridLayout(3, 1));
        setBorder(BorderFactory.createTitledBorder("Current Auction"));

        playerLabel = new JLabel("No player selected");
        highestBidLabel = new JLabel("Highest Bid: 0");
        highestBidderLabel = new JLabel("Highest Bidder: -");

        add(playerLabel);
        add(highestBidLabel);
        add(highestBidderLabel);
    }

    public void setPlayer(String name) {
        this.playerName = name;
        playerLabel.setText("Player: " + name);
        reset();
    }

    public void updateHighestBid(double amount, String team) {
        if (amount > highestBid) {
            highestBid = amount;
            highestBidder = team;
            highestBidLabel.setText("Highest Bid: " + amount);
            highestBidderLabel.setText("Highest Bidder: " + team);
        }
    }

    public void reset() {
        highestBid = 0;
        highestBidder = "-";
        highestBidLabel.setText("Highest Bid: 0");
        highestBidderLabel.setText("Highest Bidder: -");
    }

    public double getHighestBidAmount() {
        return highestBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public String getPlayerName() {
        return playerName;
    }
}
