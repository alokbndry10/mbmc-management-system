package org.example.ui;

import org.example.dao.MenuDAO;
import org.example.model.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RemoveMenuItemPanel extends JPanel {

    private MenuDAO menuDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public RemoveMenuItemPanel() {
        menuDAO = new MenuDAO();
        initComponents();
        loadMenuItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("  🗑️ REMOVE MENU ITEM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Item Name", "Price (Rs.)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(30);
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
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.setBackground(new Color(0, 123, 255));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadMenuItems());
        bottomPanel.add(btnRefresh);

        JButton btnRemove = new JButton("❌ Remove Selected");
        btnRemove.setFont(new Font("Arial", Font.BOLD, 14));
        btnRemove.setBackground(new Color(220, 53, 69));
        btnRemove.setForeground(Color.BLACK);
        btnRemove.setFocusPainted(false);
        btnRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemove.addActionListener(e -> removeItem());
        bottomPanel.add(btnRemove);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadMenuItems() {
        tableModel.setRowCount(0);
        List<MenuItem> items = menuDAO.getAllItems();
        for (MenuItem item : items) {
            tableModel.addRow(new Object[]{
                item.getId(),
                item.getName(),
                String.format("%.2f", item.getPrice()),
                item.isAvailable() ? "✔ Available" : "✖ Unavailable"
            });
        }
    }

    private void removeItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select an item to remove.",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int itemId = (int) tableModel.getValueAt(selectedRow, 0);
        String itemName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to remove '" + itemName + "'?",
            "Confirm Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (menuDAO.removeItem(itemId)) {
                JOptionPane.showMessageDialog(this,
                    "'" + itemName + "' removed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadMenuItems();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to remove item.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

