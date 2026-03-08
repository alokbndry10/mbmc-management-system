package org.example.ui;

import org.example.dao.UserDAO;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Bhandari Khaja Ghar - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(220, 53, 69));
        headerPanel.setPreferredSize(new Dimension(450, 80));
        JLabel lblTitle = new JLabel("🍔 Bhandari Khaja Ghar 🍕");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        // Login Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblUsername, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(txtPassword, gbc);

        // Login Button
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(40, 167, 69));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> login());
        formPanel.add(btnLogin, gbc);

        // Default credentials hint
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JLabel lblHint = new JLabel("<html><center>Default: admin / admin123</center></html>");
        lblHint.setFont(new Font("Arial", Font.ITALIC, 11));
        lblHint.setForeground(Color.GRAY);
        lblHint.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(lblHint, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Enter key to login
        txtPassword.addActionListener(e -> login());
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter both username and password.",
                "Login Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userDAO.findByUsername(username);
        if (user != null && userDAO.login(username, password)) {
            JOptionPane.showMessageDialog(this,
                "Welcome, " + user.getUsername() + "!",
                "Login Successful", JOptionPane.INFORMATION_MESSAGE);

            // Open main dashboard
            new MainDashboard(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }
}

