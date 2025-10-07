package com.auction.gui;

import com.auction.dao.PlayerDAO;
import com.auction.dao.TeamDAO;
import com.auction.dao.AuctionLogDAO;
import com.auction.models.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AuctionFrame extends JFrame {

    private final PlayerDAO playerDAO = new PlayerDAO();
    private final TeamDAO teamDAO = new TeamDAO();
    private final AuctionLogDAO auctionLogDAO = new AuctionLogDAO();

    private JList<String> playerList;
    private DefaultListModel<String> playerListModel;
    private JTextField bidField;
    private JComboBox<String> teamCombo;
    private JLabel highestBidLabel;

    private double highestBid = 0;
    private String highestBidder = "";

    private Player currentPlayer;

    public AuctionFrame() {
        setTitle("Auction App - Player Auction");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadPlayers();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 240, 255));

        // Left panel: Player list
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        panel.add(scrollPane, BorderLayout.WEST);

        // Right panel: Auction controls
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        highestBidLabel = new JLabel("Highest Bid: 0");
        highestBidLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(highestBidLabel);
        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.add(new JLabel("Select Team:"));
        teamCombo = new JComboBox<>(new String[]{"Sharks", "Tigers", "Eagles"});
        rightPanel.add(teamCombo);
        rightPanel.add(Box.createVerticalStrut(10));

        rightPanel.add(new JLabel("Enter Bid:"));
        bidField = new JTextField();
        rightPanel.add(bidField);
        rightPanel.add(Box.createVerticalStrut(10));

        JButton placeBidBtn = new JButton("Place Bid");
        rightPanel.add(placeBidBtn);
        rightPanel.add(Box.createVerticalStrut(10));

        JButton endAuctionBtn = new JButton("End Auction");
        rightPanel.add(endAuctionBtn);

        panel.add(rightPanel, BorderLayout.CENTER);

        add(panel);

        // List selection listener
        playerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = playerList.getSelectedIndex();
                if (index != -1) {
                    currentPlayer = playerDAO.getAllPlayers().get(index);
                    resetAuction();
                }
            }
        });

        // Place bid action
        placeBidBtn.addActionListener(e -> placeBid());

        // End auction action
        endAuctionBtn.addActionListener(e -> endAuction());
    }

    private void loadPlayers() {
        List<Player> players = playerDAO.getAllPlayers();
        playerListModel.clear();
        for (Player p : players) {
            playerListModel.addElement(p.getName() + " - " + p.getPosition());
        }
    }

    private void resetAuction() {
        highestBid = 0;
        highestBidder = "";
        highestBidLabel.setText("Highest Bid: 0");
    }

    private void placeBid() {
        if (currentPlayer == null) {
            JOptionPane.showMessageDialog(this, "Select a player first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String team = (String) teamCombo.getSelectedItem();
        double bid;
        try {
            bid = Double.parseDouble(bidField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid bid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (bid > highestBid) {
            highestBid = bid;
            highestBidder = team;
            highestBidLabel.setText("Highest Bid: " + highestBid + " by " + highestBidder);
        } else {
            JOptionPane.showMessageDialog(this, "Bid must be higher than current highest bid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void endAuction() {
        if (currentPlayer == null) {
            JOptionPane.showMessageDialog(this, "Select a player first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (highestBidder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid bids placed for this player.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int teamId = teamDAO.getTeamIdByName(highestBidder);
        if (teamId != -1) {
            auctionLogDAO.saveWinner(currentPlayer.getPlayerId(), teamId, highestBid);
            JOptionPane.showMessageDialog(this, highestBidder + " won " + currentPlayer.getName() +
                    " for " + highestBid, "Winner", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Team not found! Winner not saved.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        resetAuction();
    }
}
