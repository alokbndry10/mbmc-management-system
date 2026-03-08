package org.example.ui;

import org.example.dao.OrderDAO;
import org.example.model.Order;
import org.example.model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewOrdersPanel extends JPanel {

    private OrderDAO orderDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewOrdersPanel() {
        orderDAO = new OrderDAO();
        initComponents();
        loadOrders();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("  📦 ALL ORDERS");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"Order ID", "Customer", "Total (Rs.)", "Status", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(52, 58, 64));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 13));
        btnRefresh.setBackground(new Color(0, 123, 255));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadOrders());
        bottomPanel.add(btnRefresh);

        JButton btnViewDetails = new JButton("📋 View Details");
        btnViewDetails.setFont(new Font("Arial", Font.BOLD, 13));
        btnViewDetails.setBackground(new Color(23, 162, 184));
        btnViewDetails.setForeground(Color.BLACK);
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewDetails.addActionListener(e -> viewOrderDetails());
        bottomPanel.add(btnViewDetails);

        JButton btnUpdateStatus = new JButton("✏️ Update Status");
        btnUpdateStatus.setFont(new Font("Arial", Font.BOLD, 13));
        btnUpdateStatus.setBackground(new Color(255, 193, 7));
        btnUpdateStatus.setForeground(Color.BLACK);
        btnUpdateStatus.setFocusPainted(false);
        btnUpdateStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateStatus.addActionListener(e -> updateOrderStatus());
        bottomPanel.add(btnUpdateStatus);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        for (Order order : orders) {
            tableModel.addRow(new Object[]{
                order.getId(),
                order.getCustomerName(),
                String.format("%.2f", order.getTotalPrice()),
                order.getStatus(),
                order.getCreatedAt()
            });
        }
    }

    private void viewOrderDetails() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an order first.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        List<Order> orders = orderDAO.getAllOrders();
        Order order = orders.stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);

        if (order == null) return;

        StringBuilder details = new StringBuilder();
        details.append("Order ID: ").append(order.getId()).append("\n");
        details.append("Customer: ").append(order.getCustomerName()).append("\n");
        details.append("Status: ").append(order.getStatus()).append("\n");
        details.append("Date: ").append(order.getCreatedAt()).append("\n\n");
        details.append("Items:\n");
        for (OrderItem item : order.getItems()) {
            details.append(String.format("  %s x%d @ Rs.%.2f = Rs.%.2f\n",
                item.getMenuItemName(), item.getQuantity(),
                item.getUnitPrice(), item.getSubtotal()));
        }
        details.append("\nTotal: Rs.").append(String.format("%.2f", order.getTotalPrice()));

        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
            "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateOrderStatus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an order first.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);
        String[] statuses = {"Pending", "Preparing", "Ready", "Delivered", "Cancelled"};
        String status = (String) JOptionPane.showInputDialog(this,
            "Select new status:", "Update Order Status",
            JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

        if (status != null) {
            if (orderDAO.updateStatus(orderId, status)) {
                JOptionPane.showMessageDialog(this,
                    "Order status updated successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadOrders();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update order status.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

