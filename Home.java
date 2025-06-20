package com.PasswordManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class Home extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/passfort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    private static final Color BACKGROUND_COLOR = new Color(25, 25, 25);
    private static final Color PANEL_COLOR = new Color(45, 45, 45);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 149, 237);
    private static final Color FONT_COLOR = Color.WHITE;
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font LABEL_FONT = new Font("Segoe UI Bold", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);

    private JTextField websiteField, passwordField;
    private DefaultTableModel tableModel;
    private String username;
    private Analyzer analyzer;  // Reference to the pie chart (Analyzer)

    public Home(String username) {
        this.username = username;
        setLayout(null);
        setBackground(BACKGROUND_COLOR);

        // Add left and right panels
        add(createLeftPanel());

        // Create and add the right panel with table and pie chart
        JPanel rightPanel = createRightPanel();
        add(rightPanel);

        loadData();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(PANEL_COLOR);
        leftPanel.setBounds(0, 0, 305, 834);
        leftPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setFont(HEADER_FONT);
        welcomeLabel.setForeground(new Color(255, 215, 0));
        welcomeLabel.setBounds(20, 20, 300, 40);
        leftPanel.add(welcomeLabel);

        websiteField = createTextField(130, 95);
        passwordField = createTextField(130, 145);
        enableEnterToNavigate(websiteField, passwordField);

        addLabelToPanel(leftPanel, "Domain", 20, 95);
        leftPanel.add(websiteField);

        addLabelToPanel(leftPanel, "Password", 20, 145);
        leftPanel.add(passwordField);

        leftPanel.add(createButton("Add", 20, 190, e -> addPassword()));
        leftPanel.add(createButton("Delete", 150, 190, e -> deletePassword()));
        leftPanel.add(createButton("Update", 20, 225, e -> updatePassword()));
        leftPanel.add(createButton("Clear", 150, 225, e -> clearFields()));

        JButton logoutButton = createButton("Logout", 100, 720, e -> logout());
        leftPanel.add(logoutButton);

        // Adding the "fort" image
        ImageIcon fortressIcon = new ImageIcon("fortress.png"); // Adjust path if needed
        JLabel fort = new JLabel(fortressIcon, JLabel.CENTER);
        fort.setBounds(0, 350, 305, 200); // Adjust bounds to fit the panel layout
        leftPanel.add(fort);

        // Adding the "PassFort" text below the image
        JLabel belowFort = new JLabel("PassFort");
        belowFort.setFont(new Font(Font.DIALOG, Font.BOLD, 55));
        belowFort.setForeground(Color.YELLOW);
        belowFort.setBounds(32, 525, 300, 50); // Adjust bounds as needed
        leftPanel.add(belowFort);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(30, 30, 30));
        rightPanel.setBounds(305, 0, 800, 834);  // Increase the width to accommodate both the table and pie chart
        rightPanel.setLayout(null);

        JLabel loginLabel = new JLabel("Credentials");
        loginLabel.setFont(HEADER_FONT);
        loginLabel.setForeground(new Color(255, 215, 0));
        loginLabel.setBounds(180, 20, 250, 30);
        rightPanel.add(loginLabel);

        tableModel = new DefaultTableModel(new String[]{"Domain", "Password", "Strength"}, 0);
        JTable passwordTable = new JTable(tableModel);
        styleTable(passwordTable);

        JScrollPane scrollPane = new JScrollPane(passwordTable);
        scrollPane.setBounds(28, 70, 450, 697);
        rightPanel.add(scrollPane);

        // Create and add the Analyzer (Pie Chart)
        analyzer = new Analyzer(username);
        analyzer.setBounds(480, 70, 300, 700);  // Set the position and size for the pie chart
        rightPanel.add(analyzer);

        return rightPanel;
    }

    private void addPassword() {
        int strength = checkPasswordStrength(); // Get password strength as an integer
        executeUpdate("INSERT INTO " + username + " (website, password, strength) VALUES (?, ?, ?)",
                websiteField.getText(), passwordField.getText(), String.valueOf(strength));
    }

    private void deletePassword() {
        executeUpdate("DELETE FROM " + username + " WHERE website = ?", websiteField.getText());
    }

    private void updatePassword() {
        int strength = checkPasswordStrength(); // Recalculate password strength
        executeUpdate("UPDATE " + username + " SET password = ?, strength = ? WHERE website = ?",
                passwordField.getText(), String.valueOf(strength), websiteField.getText());
    }

    private void loadData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT website, password, strength FROM " + username);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                String website = rs.getString("website");
                String password = rs.getString("password");
                int strength = rs.getInt("strength");

                tableModel.addRow(new Object[]{website, password, strength});
            }
            analyzer.updateChart();  // Refresh the pie chart after loading data
        } catch (SQLException e) {
            showError("Failed to load data: " + e.getMessage());
        }
    }

    private void executeUpdate(String query, String... params) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                clearFields();
                loadData();  // Reload data and refresh chart
            }
        } catch (SQLException e) {
            showError("Operation failed: " + e.getMessage());
        }
    }

    private void clearFields() {
        websiteField.setText("");
        passwordField.setText("");
    }

    private void logout() {
        // Implement logout logic
    }

    private JTextField createTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setBounds(x, y, 150, 30);
        return textField;
    }

    private JButton createButton(String text, int x, int y, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(FONT_COLOR);
        button.setFocusPainted(false);
        button.setBounds(x, y, 130, 35);
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        button.addActionListener(action);
        return button;
    }

    private void addLabelToPanel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(FONT_COLOR);
        label.setBounds(x, y, 100, 30);
        panel.add(label);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setBackground(new Color(40, 40, 40));
        table.setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(50, 50, 50));
        table.getTableHeader().setForeground(FONT_COLOR);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void enableEnterToNavigate(JTextField currentField, JTextField nextField) {
        currentField.addActionListener(e -> nextField.requestFocus());
    }

    private int checkPasswordStrength() {
        String password = passwordField.getText();
        String strength = getPasswordStrength(password);

        return switch (strength) {
            case "Intermediate" -> 1;
            case "Strong" -> 2;
            default -> 0; // Weak
        };
    }

    private String getPasswordStrength(String password) {
        if (password.length() < 8) return "Weak";
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;':,.<>?/".indexOf(ch) >= 0);

        if (hasUpper && hasLower && hasDigit && hasSpecial) return "Strong";
        if (hasUpper || hasLower && (hasDigit || hasSpecial)) return "Intermediate";
        return "Weak";
    }

    // Custom TableCellRenderer for password strength with more saturated pastel colors
    private static class StrengthCellRenderer extends JLabel implements TableCellRenderer {
        public StrengthCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Integer) {
                int strength = (Integer) value;
                switch (strength) {
                    case 2 -> { // Strong - More Saturated Green
                        setText("Strong");
                        setBackground(new Color(102, 255, 102)); // More Saturated Green
                        setForeground(Color.BLACK);
                    }
                    case 1 -> { // Intermediate - More Saturated Yellow
                        setText("Intermediate");
                        setBackground(new Color(255, 255, 102)); // More Saturated Yellow
                        setForeground(Color.BLACK);
                    }
                    default -> { // Weak - More Saturated Red
                        setText("Weak");
                        setBackground(new Color(255, 99, 71)); // More Saturated Red (Tomato)
                        setForeground(Color.BLACK);
                    }
                }
            }
            return this;
        }
    }
}
