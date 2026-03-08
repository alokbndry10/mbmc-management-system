package org.example.dao;

import org.example.DatabaseManager;
import org.example.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public List<MenuItem> getAllItems() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT id, name, price, available FROM menu_items ORDER BY id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("available") == 1
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu: " + e.getMessage());
        }
        return list;
    }

    public List<MenuItem> getAvailableItems() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT id, name, price, available FROM menu_items WHERE available=1 ORDER BY id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        true
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching menu: " + e.getMessage());
        }
        return list;
    }

    public MenuItem findById(int id) {
        String sql = "SELECT id, name, price, available FROM menu_items WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("available") == 1
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding item: " + e.getMessage());
        }
        return null;
    }

    public boolean addItem(String name, double price) {
        String sql = "INSERT INTO menu_items(name, price, available) VALUES(?,?,1)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
            return false;
        }
    }

    public boolean removeItem(int id) {
        String sql = "DELETE FROM menu_items WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error removing item: " + e.getMessage());
            return false;
        }
    }

    public boolean toggleAvailability(int id, boolean available) {
        String sql = "UPDATE menu_items SET available=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, available ? 1 : 0);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating availability: " + e.getMessage());
            return false;
        }
    }
}

