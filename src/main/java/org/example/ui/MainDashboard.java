package org.example.ui;

import org.example.model.User;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {

    private User currentUser;
    private JPanel contentPanel;

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Bhandari Khaja Ghar - Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(220, 53, 69));
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("  🍔 Bhandari Khaja Ghar");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblUser = new JLabel("👤 " + currentUser.getUsername() + " [" + currentUser.getRole().toUpperCase() + "]  ");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);
        headerPanel.add(lblUser, BorderLayout.EAST);

        // Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(52, 58, 64));
        sidebarPanel.setPreferredSize(new Dimension(220, 640));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addMenuButton(sidebarPanel, "📋 View Menu", () -> showPanel(new MenuPanel()));
        addMenuButton(sidebarPanel, "🛒 Place Order", () -> showPanel(new PlaceOrderPanel()));

        if (currentUser.isAdmin()) {
            addMenuButton(sidebarPanel, "📦 View All Orders", () -> showPanel(new ViewOrdersPanel()));
            addMenuButton(sidebarPanel, "➕ Add Menu Item", () -> showPanel(new AddMenuItemPanel()));
            addMenuButton(sidebarPanel, "🗑️ Remove Menu Item", () -> showPanel(new RemoveMenuItemPanel()));
        }

        sidebarPanel.add(Box.createVerticalGlue());

        JButton btnLogout = createMenuButton("🚪 Logout", () -> logout());
        btnLogout.setBackground(new Color(108, 117, 125));
        sidebarPanel.add(btnLogout);

        // Content Panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Welcome Screen
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        JLabel lblWelcome = new JLabel("<html><center><h1>Welcome to Restaurant Management System</h1>" +
                "<p>Menu: Balen Burger, Harke Pizza, KP Burger, Mutton Biryani</p></center></html>");
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        welcomePanel.add(lblWelcome, BorderLayout.CENTER);
        contentPanel.add(welcomePanel);

        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addMenuButton(JPanel panel, String text, Runnable action) {
        JButton btn = createMenuButton(text, action);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JButton createMenuButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(new Color(73, 80, 87));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}

