package com.auction.gui;

import com.auction.models.User;
import com.auction.models.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JButton loginButton = new JButton("Login");

    // Hardcoded admin
    private final User ADMIN = new User(1, "admin", "admin123", Role.ADMIN, 0);

    public LoginFrame() {
        setTitle("Auction App - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(220, 240, 255)); // soft blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Username label + field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password label + field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        add(panel);

        // Login action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals(ADMIN.getUsername()) && password.equals(ADMIN.getPassword())) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Open Auction Frame
                    new AuctionDashboardFrame().setVisible(true);
                    dispose(); // close login frame
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

}
