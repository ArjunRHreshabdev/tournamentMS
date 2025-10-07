package com.auction.dao;
import com.auction.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuctionLogDAO {
    public boolean saveWinner(int playerId, int teamId, double price) {
        String sql = "INSERT INTO auction_logs (player_id, winning_team_id, price) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, playerId);
            stmt.setInt(2, teamId);
            stmt.setDouble(3, price);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error saving auction winner: " + e.getMessage());
            return false;
        }

    }
    public boolean isPlayerSold(int playerId) {
        String sql = "SELECT COUNT(*) FROM auction_logs WHERE player_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking player status: " + e.getMessage());
        }
        return false;
    }


}
