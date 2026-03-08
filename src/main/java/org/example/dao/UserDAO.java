package org.example.dao;

import org.example.DatabaseManager;
import org.example.model.User;

import java.sql.*;

public class UserDAO {

    public User findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, role FROM users WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerUser(String username, String plainPassword, String role) {
        String sql = "INSERT INTO users(username, password_hash, role) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, DatabaseManager.hashPassword(plainPassword));
            ps.setString(3, role);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String plainPassword) {
        User user = findByUsername(username);
        if (user == null) return false;
        return user.getPasswordHash().equals(DatabaseManager.hashPassword(plainPassword));
    }
}

