package com.auction.dao;

import com.auction.models.Player;
import com.auction.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class PlayerDAO {

    // Method to add a player
    public boolean addPlayer(Player player) {
        String sql = "INSERT INTO players (Name, position, team_id) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, player.getName());
            ps.setString(2, player.getPosition());
            ps.setInt(3, player.getTeamId());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT player_id, Name, position, team_id FROM players";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Player player = new Player();
                player.setPlayerId(rs.getInt("player_id"));
                player.setName(rs.getString("Name"));
                player.setPosition(rs.getString("position"));
                player.setTeamId(rs.getInt("team_id"));

                players.add(player);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return players;
    }

    public Player getPlayerById(int playerId) {
        String sql = "SELECT player_id, Name, position, team_id FROM players WHERE player_id = ?";
        Player player = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    player = new Player();
                    player.setPlayerId(rs.getInt("player_id"));
                    player.setName(rs.getString("Name"));
                    player.setPosition(rs.getString("position"));
                    player.setTeamId(rs.getInt("team_id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return player;
    }


}


