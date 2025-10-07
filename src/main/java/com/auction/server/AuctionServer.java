package com.auction.server;

import static spark.Spark.*;
import com.auction.db.DBConnection;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class AuctionServer {

    public static void main(String[] args) {
        port(8080);

        // Allow access from other devices (like phone)
        ipAddress("0.0.0.0");

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
                    //p.put("base_price", rs.getDouble("base_price"));
                    players.put(p);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return players.toString();
        });

        System.out.println("✅ Server running at http://localhost:8080/api/players");
    }
}
