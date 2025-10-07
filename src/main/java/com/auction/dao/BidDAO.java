package com.auction.dao;

import com.auction.models.bid;
import com.auction.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.*;

public class BidDAO {
    // Method to place a bid
    public boolean placeBid(bid bid) {
        String sql = "INSERT INTO bids (player_id, team_id, bid_amount, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bid.getPlayerId());
            ps.setInt(2, bid.getTeamId());
            ps.setDouble(3, bid.getBidAmount());
            ps.setTimestamp(4, Timestamp.valueOf(bid.getTimestamp()));

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public bid getHighestBidForPlayer(int playerId) {
        String sql = "SELECT bid_id, player_id, team_id, bid_amount, timestamp " +
                "FROM bids WHERE player_id = ? ORDER BY bid_amount DESC LIMIT 1";
        bid bid = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bid = new bid();
                    bid.setBidId(rs.getInt("bid_id"));
                    bid.setPlayerId(rs.getInt("player_id"));
                    bid.setTeamId(rs.getInt("team_id"));
                    bid.setBidAmount(rs.getDouble("bid_amount"));
                    bid.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bid;
    }

}
