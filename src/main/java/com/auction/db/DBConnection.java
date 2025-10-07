package com.auction.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection class:
 * - Provides a single reusable connection to MySQL.
 * - Other classes (DAOs) will use this to query/update the database.
 */

public class DBConnection {

    // Update these with your MySQL credentials
    private static final String URL = "jdbc:mysql://localhost:3306/AUC?serverTimezone=UTC";
    private static final String USER = "javauser";
    private static final String PASS = "mypassword";

    // Singleton connection instance
    private static Connection conn = null;

    // Private constructor to prevent instantiation
    private DBConnection() {}

    /**
     * Returns a live database connection.
     * If none exists or it is closed, creates a new one.
     */

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                // Load MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }

            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASS);
        }
        return conn;
    }

    /**
     * Closes the connection safely.
     */
    public static void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                conn = null;
            }
        }
    }
}
