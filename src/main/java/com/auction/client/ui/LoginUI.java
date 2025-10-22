package com.auction.client.ui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;


public class LoginUI extends JFrame {
    private JTextField teamNameField;
    private JTextField ipField;
    private JTextField portField;
    private JButton connectButton;
    private JLabel statusLabel;

    public LoginUI() {
        setTitle("Team Manager - Connect to Auction");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel teamLabel = new JLabel("Team Name:");
        JLabel ipLabel = new JLabel("Server IP:");
        JLabel portLabel = new JLabel("Port:");

        teamNameField = new JTextField(15);
        ipField = new JTextField("127.0.0.1", 15);
        portField = new JTextField("8080", 15);

        connectButton = new JButton("Connect");
        statusLabel = new JLabel(" ");

        gbc.gridx = 0; gbc.gridy = 0;
        add(teamLabel, gbc);
        gbc.gridx = 1;
        add(teamNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(ipLabel, gbc);
        gbc.gridx = 1;
        add(ipField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(portLabel, gbc);
        gbc.gridx = 1;
        add(portField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(connectButton, gbc);

        gbc.gridy = 4;
        add(statusLabel, gbc);

        connectButton.addActionListener(this::connectToServer);

        setVisible(true);
    }

    private void connectToServer(ActionEvent e) {
        String teamName = teamNameField.getText().trim();
        String ip = ipField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());

        if (teamName.isEmpty()) {
            statusLabel.setText("Please enter a team name!");
            return;
        }

        try {
            Socket socket = new Socket(ip, port);
            statusLabel.setText("✅ Connected to " + ip + ":" + port);
            connectButton.setEnabled(false);

            // You can now pass this socket to next UI (Auction screen)
            System.out.println("Connected as " + teamName);

            new com.auction.client.ui.AuctionUI(socket, teamName);
            dispose(); // close login window


        } catch (IOException ex) {
            statusLabel.setText("❌ Connection failed: " + ex.getMessage());
        }
    }
}
