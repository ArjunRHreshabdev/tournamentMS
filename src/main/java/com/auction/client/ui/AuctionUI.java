package com.auction.client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class AuctionUI extends JFrame {
    private JLabel playerNameLabel;
    private JLabel playerPositionLabel;
    private JLabel basePriceLabel;
    private JLabel highestBidLabel;
    private JLabel highestBidderLabel;
    private JTextField bidAmountField;
    private JButton placeBidButton;
    private JLabel statusLabel;

    private final Socket socket;
    private final String teamName;

    public AuctionUI(Socket socket, String teamName) {
        this.socket = socket;
        this.teamName = teamName;
        // Start listening for server updates
        AuctionListener listener = new AuctionListener(socket, this);
        listener.start();

        setTitle("Auction - " + teamName);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        playerNameLabel = new JLabel("Player: -");
        playerPositionLabel = new JLabel("Position: -");
        basePriceLabel = new JLabel("Base Price: -");
        highestBidLabel = new JLabel("Highest Bid: -");
        highestBidderLabel = new JLabel("Highest Bidder: -");

        bidAmountField = new JTextField(10);
        placeBidButton = new JButton("Place Bid");
        statusLabel = new JLabel(" ");

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(new JLabel("🧑‍🤝‍🧑 Current Player"), gbc);

        // Row 1
        gbc.gridy = 1; gbc.gridwidth = 1;
        add(playerNameLabel, gbc);
        gbc.gridx = 1;
        add(playerPositionLabel, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        add(basePriceLabel, gbc);
        gbc.gridx = 1;
        add(highestBidLabel, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3;
        add(highestBidderLabel, gbc);

        // Row 4 - Bid input
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Your Bid (₹): "), gbc);
        gbc.gridx = 1;
        add(bidAmountField, gbc);

        // Row 5 - Button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(placeBidButton, gbc);

        // Row 6 - Status
        gbc.gridy = 6;
        add(statusLabel, gbc);

        placeBidButton.addActionListener(this::sendBid);

        setVisible(true);
    }

    private void sendBid(ActionEvent e) {
        try {
            double bidAmount = Double.parseDouble(bidAmountField.getText().trim());

            Map<String, Object> bidData = Map.of(
                    "type", "BID",
                    "team", teamName,
                    "amount", bidAmount
            );

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(bidData);
            out.flush();

            statusLabel.setText("✅ Bid placed: ₹" + bidAmount);
        } catch (NumberFormatException ex) {
            statusLabel.setText("❌ Invalid bid amount");
        } catch (IOException ex) {
            statusLabel.setText("❌ Failed to send bid: " + ex.getMessage());
        }
    }

    // Called when server sends player update
    public void updatePlayer(String name, String position, double basePrice, double highestBid, String highestBidder) {
        playerNameLabel.setText("Player: " + name);
        playerPositionLabel.setText("Position: " + position);
        basePriceLabel.setText("Base Price: ₹" + basePrice);
        highestBidLabel.setText("Highest Bid: ₹" + highestBid);
        highestBidderLabel.setText("Highest Bidder: " + highestBidder);
    }

    public void updatePlayerFields(double highestBid, String highestBidder) {
        highestBidLabel.setText("Highest Bid: ₹" + highestBid);
        highestBidderLabel.setText("Highest Bidder: " + highestBidder);
    }

    public void showSoldMessage(String playerName, String winner, double amount) {
        JOptionPane.showMessageDialog(this,
                "🏆 " + playerName + " sold to " + winner + " for ₹" + amount,
                "Player Sold",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
