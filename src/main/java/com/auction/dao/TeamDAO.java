package com.auction.dao;

import com.auction.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamDAO {
    public int getTeamIdByName(String teamName) {
        String sql = "SELECT team_id FROM teams WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teamName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("team_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }
}