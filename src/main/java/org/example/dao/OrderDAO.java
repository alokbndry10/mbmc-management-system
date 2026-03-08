package org.example.dao;

import org.example.DatabaseManager;
import org.example.model.Order;
import org.example.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int placeOrder(String customerName, List<OrderItem> items) {
        String insertOrder = "INSERT INTO orders(customer_name, total_price, status) VALUES(?,?,?)";
        String insertItem  = "INSERT INTO order_items(order_id, menu_item_id, quantity, unit_price) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

            double total = items.stream().mapToDouble(OrderItem::getSubtotal).sum();

            PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customerName);
            ps.setDouble(2, total);
            ps.setString(3, "Pending");
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            int orderId = keys.next() ? keys.getInt(1) : -1;

            PreparedStatement psItem = conn.prepareStatement(insertItem);
            for (OrderItem oi : items) {
                psItem.setInt(1, orderId);
                psItem.setInt(2, oi.getMenuItemId());
                psItem.setInt(3, oi.getQuantity());
                psItem.setDouble(4, oi.getUnitPrice());
                psItem.addBatch();
            }
            psItem.executeBatch();
            conn.commit();
            return orderId;
        } catch (SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
            return -1;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, customer_name, total_price, status, created_at FROM orders ORDER BY id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order o = new Order(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getDouble("total_price"),
                        rs.getString("created_at")
                );
                o.setItems(getOrderItems(conn, o.getId()));
                orders.add(o);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }
        return orders;
    }

    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
            return false;
        }
    }

    private List<OrderItem> getOrderItems(Connection conn, int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = """
            SELECT oi.menu_item_id, m.name, oi.quantity, oi.unit_price
            FROM order_items oi
            JOIN menu_items m ON m.id = oi.menu_item_id
            WHERE oi.order_id = ?
        """;
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            items.add(new OrderItem(
                    rs.getInt("menu_item_id"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price")
            ));
        }
        return items;
    }
}

