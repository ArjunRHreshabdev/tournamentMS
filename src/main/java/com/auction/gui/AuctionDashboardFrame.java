package com.auction.gui;

import com.auction.dao.BidDAO;
import com.auction.dao.PlayerDAO;
import com.auction.dao.TeamDAO;
import com.auction.dao.AuctionLogDAO;
import com.auction.models.Player;
import com.auction.models.bid;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;

public class AuctionDashboardFrame extends JFrame {

    private final PlayerDAO playerDAO = new PlayerDAO();
    private final TeamDAO teamDAO = new TeamDAO();
    private final BidDAO bidDAO = new BidDAO();
    private final AuctionLogDAO auctionLogDAO = new AuctionLogDAO();

    private JList<String> playerList;
    private DefaultListModel<String> playerListModel;

    private JComboBox<String> teamCombo;
    private JTextField bidField;
    private JLabel highestBidLabel;

    private JTable bidTable;
    private DefaultTableModel bidTableModel;

    private double highestBid = 0;
    private String highestBidder = "";
    private Player currentPlayer;

    public AuctionDashboardFrame() {
        setTitle("Auction App - Dashboard");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        loadPlayers();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220, 240, 255));

        // Left Panel: Player List
        playerListModel = new DefaultListModel<>();
        playerList = new JList<>(playerListModel);
        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setPreferredSize(new Dimension(200, 0));
        mainPanel.add(scrollPane, BorderLayout.WEST);

        // Right Panel: Auction Controls
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        highestBidLabel = new JLabel("Highest Bid: 0");
        highestBidLabel.setFont(new Font("Arial", Font.BOLD, 16));
        highestBidLabel.setForeground(new Color(10, 50, 120));
        rightPanel.add(highestBidLabel);
        rightPanel.add(Box.createVerticalStrut(10));

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
        rightPanel.add(Box.createVerticalStrut(20));

        // Bid Table
        bidTableModel = new DefaultTableModel(new String[]{"Team", "Bid Amount", "Time"}, 0);
        bidTable = new JTable(bidTableModel);
        bidTable.setFillsViewportHeight(true);
        bidTable.setBackground(new Color(240, 250, 255));
        bidTable.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane tableScroll = new JScrollPane(bidTable);
        tableScroll.setPreferredSize(new Dimension(550, 250));
        rightPanel.add(tableScroll);

        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);

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

        // Place bid
        placeBidBtn.addActionListener(e -> placeBid());

        // End auction
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
        bidTableModel.setRowCount(0); // clear previous bids
    }

    private void placeBid() {
        if (currentPlayer == null) {
            JOptionPane.showMessageDialog(this, "Select a player first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String team = (String) teamCombo.getSelectedItem();
        double bidAmount;
        try {
            bidAmount = Double.parseDouble(bidField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid bid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (bidAmount > highestBid) {
            highestBid = bidAmount;
            highestBidder = team;
            highestBidLabel.setText("Highest Bid: " + highestBid + " by " + highestBidder);

            // Add to bid table
            bid newBid = new bid(0, currentPlayer.getPlayerId(), teamDAO.getTeamIdByName(team), bidAmount, LocalDateTime.now());
            bidDAO.placeBid(newBid);
            bidTableModel.addRow(new Object[]{team, bidAmount, newBid.getTimestamp().toString()});

        } else {
            JOptionPane.showMessageDialog(this, "Bid must be higher than current highest bid!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        bidField.setText("");
    }

    private void endAuction() {
        if (currentPlayer == null) {
            JOptionPane.showMessageDialog(this, "Select a player first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (highestBidder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid bids placed.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int teamId = teamDAO.getTeamIdByName(highestBidder);
        if (teamId != -1) {
            auctionLogDAO.saveWinner(currentPlayer.getPlayerId(), teamId, highestBid);
            JOptionPane.showMessageDialog(this, highestBidder + " won " + currentPlayer.getName() + " for " + highestBid,
                    "Winner", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Team not found! Winner not saved.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        resetAuction();
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuctionDashboardFrame().setVisible(true));
    }
}
