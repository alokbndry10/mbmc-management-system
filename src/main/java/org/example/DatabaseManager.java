package org.example;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DatabaseManager {

    // ── Change these to match your MySQL setup ──────────────────────────────
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DATABASE = "restaurant_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";          // your MySQL root password
    // ────────────────────────────────────────────────────────────────────────

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE
            + "?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** Hash a plain-text password with SHA-256. */
    public static String hashPassword(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id            INT AUTO_INCREMENT PRIMARY KEY,
                    username      VARCHAR(50)  NOT NULL UNIQUE,
                    password_hash VARCHAR(64)  NOT NULL,
                    role          VARCHAR(20)  NOT NULL DEFAULT 'staff'
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS menu_items (
                    id        INT AUTO_INCREMENT PRIMARY KEY,
                    name      VARCHAR(100) NOT NULL,
                    price     DECIMAL(10,2) NOT NULL,
                    available TINYINT(1)   NOT NULL DEFAULT 1
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS orders (
                    id            INT AUTO_INCREMENT PRIMARY KEY,
                    customer_name VARCHAR(100) NOT NULL,
                    total_price   DECIMAL(10,2) NOT NULL DEFAULT 0,
                    status        VARCHAR(30)  NOT NULL DEFAULT 'Pending',
                    created_at    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id           INT AUTO_INCREMENT PRIMARY KEY,
                    order_id     INT NOT NULL,
                    menu_item_id INT NOT NULL,
                    quantity     INT NOT NULL,
                    unit_price   DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (order_id)     REFERENCES orders(id),
                    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
                )
            """);

            // Seed admin user
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username='admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users(username, password_hash, role) VALUES(?,?,?)");
                ps.setString(1, "admin");
                ps.setString(2, hashPassword("admin123"));
                ps.setString(3, "admin");
                ps.executeUpdate();
                ps.close();
            }

            // Seed menu items
            rs = stmt.executeQuery("SELECT COUNT(*) FROM menu_items");
            if (rs.next() && rs.getInt(1) == 0) {
                String[][] items = {
                    {"Balen Burger",   "199.00"},
                    {"Harke Pizza",    "349.00"},
                    {"KP Burger",      "179.00"},
                    {"Mutton Biryani", "299.00"}
                };
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO menu_items(name, price, available) VALUES(?,?,1)");
                for (String[] item : items) {
                    ps.setString(1, item[0]);
                    ps.setDouble(2, Double.parseDouble(item[1]));
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.close();
            }
            rs.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Database connection failed:\n" + e.getMessage()
                + "\n\nMake sure MySQL is running and credentials in DatabaseManager.java are correct.",
                "DB Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}

