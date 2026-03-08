package org.example.ui;

import org.example.dao.MenuDAO;
import org.example.model.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuPanel extends JPanel {

    private MenuDAO menuDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public MenuPanel() {
        menuDAO = new MenuDAO();
        initComponents();
        loadMenuItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("  📋 OUR MENU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Refresh Button
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
}

