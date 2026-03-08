package org.example.ui;

import org.example.dao.MenuDAO;

import javax.swing.*;
import java.awt.*;

public class AddMenuItemPanel extends JPanel {

    private MenuDAO menuDAO;
    private JTextField txtName;
    private JTextField txtPrice;

    public AddMenuItemPanel() {
        menuDAO = new MenuDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("  ➕ ADD MENU ITEM");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Item Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Item Name:");
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(lblName, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        txtName = new JTextField(25);
        txtName.setFont(new Font("Arial", Font.PLAIN, 15));
        formPanel.add(txtName, gbc);

        // Price
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPrice = new JLabel("Price (Rs.):");
        lblPrice.setFont(new Font("Arial", Font.BOLD, 16));
        formPanel.add(lblPrice, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtPrice = new JTextField(25);
        txtPrice.setFont(new Font("Arial", Font.PLAIN, 15));
        formPanel.add(txtPrice, gbc);

        // Buttons
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton btnAdd = new JButton("✔️ Add Item");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 15));
        btnAdd.setPreferredSize(new Dimension(140, 40));
        btnAdd.setBackground(new Color(40, 167, 69));
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addItem());
        buttonPanel.add(btnAdd);

        JButton btnClear = new JButton("🔄 Clear");
        btnClear.setFont(new Font("Arial", Font.BOLD, 15));
        btnClear.setPreferredSize(new Dimension(140, 40));
        btnClear.setBackground(new Color(108, 117, 125));
        btnClear.setForeground(Color.BLACK);
        btnClear.setFocusPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.addActionListener(e -> clearFields());
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void addItem() {
        String name = txtName.getText().trim();
        String priceStr = txtPrice.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter item name.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (priceStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter price.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Price must be greater than 0.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (menuDAO.addItem(name, price)) {
                JOptionPane.showMessageDialog(this,
                    "'" + name + "' added successfully at Rs." + String.format("%.2f", price),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to add item.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid price format. Please enter a valid number.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtPrice.setText("");
        txtName.requestFocus();
    }
}

