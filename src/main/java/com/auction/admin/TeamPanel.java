package com.auction.admin;

import com.auction.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class TeamPanel extends JPanel {

    private Map<Integer, JLabel> teamLabels;
    private Map<Integer, Double> teamBudgets;

    public TeamPanel() {
        setLayout(new GridLayout(0, 1));
        setBorder(BorderFactory.createTitledBorder("Teams"));
        setPreferredSize(new Dimension(250, 0));

        teamLabels = new HashMap<>();
        teamBudgets = new HashMap<>();

        loadTeams();
    }

    private void loadTeams() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT team_id, team_name, budget FROM teams");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("team_id");
                String name = rs.getString("team_name");
                double budget = rs.getDouble("budget");
                teamBudgets.put(id, budget);

                JLabel lbl = new JLabel(name + " : " + budget);
                teamLabels.put(id, lbl);
                add(lbl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getTeamBudget(int teamId) {
        return teamBudgets.getOrDefault(teamId, 0.0);
    }

    public void updateTeamBudget(int teamId, double newBudget) {
        teamBudgets.put(teamId, newBudget);
        JLabel lbl = teamLabels.get(teamId);
        if (lbl != null) {
            lbl.setText(lbl.getText().split(":")[0] + ": " + newBudget);
        }

        // Update DB
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE teams SET budget = ? WHERE team_id = ?")) {
            ps.setDouble(1, newBudget);
            ps.setInt(2, teamId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
