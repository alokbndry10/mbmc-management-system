package org.example.ui;

import org.example.dao.MenuDAO;
import org.example.dao.OrderDAO;
import org.example.model.MenuItem;
import org.example.model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderPanel extends JPanel {

    private MenuDAO menuDAO;
    private OrderDAO orderDAO;
    private JTextField txtCustomerName;
    private JComboBox<String> cmbMenuItems;
    private JSpinner spnQuantity;
    private DefaultTableModel cartModel;
    private JTable cartTable;
    private JLabel lblTotal;
    private List<MenuItem> availableItems;
    private List<OrderItem> cart;

    public PlaceOrderPanel() {
        menuDAO = new MenuDAO();
        orderDAO = new OrderDAO();
        cart = new ArrayList<>();
        initComponents();
        loadMenuItems();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel lblTitle = new JLabel("  🛒 PLACE ORDER");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(lblTitle, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Order Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Order Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        txtCustomerName = new JTextField(20);
        formPanel.add(txtCustomerName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Select Item:"), gbc);
        gbc.gridx = 1;
        cmbMenuItems = new JComboBox<>();
        formPanel.add(cmbMenuItems, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        formPanel.add(spnQuantity, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JButton btnAddToCart = new JButton("➕ Add to Cart");
        btnAddToCart.setBackground(new Color(40, 167, 69));
        btnAddToCart.setForeground(Color.BLACK);
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(e -> addToCart());
        formPanel.add(btnAddToCart, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Cart Table
        JPanel cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout());
        cartPanel.setBorder(BorderFactory.createTitledBorder("Cart"));

        String[] columns = {"Item", "Price", "Qty", "Subtotal"};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartModel);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 13));
        cartTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        cartPanel.add(scrollPane, BorderLayout.CENTER);

        // Total and buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: Rs. 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(lblTotal);

        JButton btnClearCart = new JButton("🗑️ Clear Cart");
        btnClearCart.setBackground(new Color(220, 53, 69));
        btnClearCart.setForeground(Color.BLACK);
        btnClearCart.setFocusPainted(false);
        btnClearCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClearCart.addActionListener(e -> clearCart());
        bottomPanel.add(btnClearCart);

        JButton btnPlaceOrder = new JButton("✔️ Place Order");
        btnPlaceOrder.setBackground(new Color(40, 167, 69));
        btnPlaceOrder.setForeground(Color.BLACK);
        btnPlaceOrder.setFocusPainted(false);
        btnPlaceOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPlaceOrder.addActionListener(e -> placeOrder());
        bottomPanel.add(btnPlaceOrder);

        cartPanel.add(bottomPanel, BorderLayout.SOUTH);
        mainPanel.add(cartPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadMenuItems() {
        availableItems = menuDAO.getAvailableItems();
        cmbMenuItems.removeAllItems();
        for (MenuItem item : availableItems) {
            cmbMenuItems.addItem(item.getName() + " - Rs." + item.getPrice());
        }
    }

    private void addToCart() {
        if (cmbMenuItems.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Please select an item.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MenuItem selected = availableItems.get(cmbMenuItems.getSelectedIndex());
        int qty = (int) spnQuantity.getValue();

        // Check if already in cart
        boolean found = false;
        for (OrderItem oi : cart) {
            if (oi.getMenuItemId() == selected.getId()) {
                oi.setQuantity(oi.getQuantity() + qty);
                found = true;
                break;
            }
        }

        if (!found) {
            cart.add(new OrderItem(selected.getId(), selected.getName(), qty, selected.getPrice()));
        }

        updateCartTable();
        JOptionPane.showMessageDialog(this, "Added to cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCartTable() {
        cartModel.setRowCount(0);
        double total = 0;
        for (OrderItem oi : cart) {
            cartModel.addRow(new Object[]{
                oi.getMenuItemName(),
                "Rs." + oi.getUnitPrice(),
                oi.getQuantity(),
                "Rs." + String.format("%.2f", oi.getSubtotal())
            });
            total += oi.getSubtotal();
        }
        lblTotal.setText("Total: Rs. " + String.format("%.2f", total));
    }

    private void clearCart() {
        cart.clear();
        updateCartTable();
    }

    private void placeOrder() {
        String customerName = txtCustomerName.getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = orderDAO.placeOrder(customerName, cart);
        if (orderId > 0) {
            JOptionPane.showMessageDialog(this,
                "Order #" + orderId + " placed successfully for " + customerName + "!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            txtCustomerName.setText("");
            clearCart();
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to place order.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

