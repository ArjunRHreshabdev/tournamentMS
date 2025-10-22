package com.auction.server;

import static spark.Spark.*;
import com.auction.db.DBConnection;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class AuctionServer {

    public static void main(String[] args) {
        port(8080);
        ipAddress("0.0.0.0"); // allow LAN access

        // ========== PLAYERS ==========
        get("/api/players", (req, res) -> {
            res.type("application/json");
            JSONArray players = new JSONArray();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM players")) {

                while (rs.next()) {
                    JSONObject p = new JSONObject();
                    p.put("id", rs.getInt("player_id"));
                    p.put("name", rs.getString("name"));
                    p.put("position", rs.getString("position"));
                    p.put("base_price", rs.getDouble("base_price"));
                    p.put("team_id", rs.getInt("team_id"));
                    players.put(p);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return players.toString();
        });

        // ========== TEAMS ==========
        get("/api/teams", (req, res) -> {
            res.type("application/json");
            JSONArray teams = new JSONArray();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM teams")) {

                while (rs.next()) {
                    JSONObject t = new JSONObject();
                    t.put("team_id", rs.getInt("team_id"));
                    t.put("team_name", rs.getString("team_name"));
                    t.put("budget", rs.getDouble("budget"));
                    teams.put(t);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return teams.toString();
        });

        // ========== AUCTION LOGS ==========
        get("/api/auction/logs", (req, res) -> {
            res.type("application/json");
            JSONArray logs = new JSONArray();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM auction_log ORDER BY timestamp DESC")) {

                while (rs.next()) {
                    JSONObject log = new JSONObject();
                    log.put("id", rs.getInt("log_id"));
                    log.put("player_id", rs.getInt("player_id"));
                    log.put("team_id", rs.getInt("team_id"));
                    log.put("bid_amount", rs.getDouble("bid_amount"));
                    log.put("timestamp", rs.getTimestamp("timestamp"));
                    logs.put(log);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return logs.toString();
        });

        System.out.println("✅ Server running at:");
        System.out.println("➡ http://localhost:8080/api/players");
        System.out.println("➡ http://localhost:8080/api/teams");
        System.out.println("➡ http://localhost:8080/api/auction/logs");
    }
}
