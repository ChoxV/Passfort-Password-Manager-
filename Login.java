package com.PasswordManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.SecureRandom;
import java.sql.*;
import javax.swing.*;

public class Login extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField otpField;
    private JButton otpButton;
    private JButton loginButton;

    // Class-level variable to store the sent OTP
    private String sentOTP;

    public Login() {
        initializeComponents();
        setupLayout();
    }

    private void initializeComponents() {
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        otpField = new JTextField();
        otpButton = new JButton("Send OTP");
        loginButton = new JButton("Login");

        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        usernameField.setPreferredSize(new Dimension(300, 45));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        passwordField.setPreferredSize(new Dimension(300, 45));
        otpField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        otpField.setPreferredSize(new Dimension(170, 45)); // Adjusted width

        otpButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        otpButton.setForeground(Color.WHITE);
        otpButton.setPreferredSize(new Dimension(120, 45)); // Adjusted width
        otpButton.setBackground(new Color(27, 161, 226)); // Set blue color
        otpButton.addActionListener(e -> {
            sentOTP = generateOTP(6); // Generate and store the OTP
            System.out.println(sentOTP); // Log the OTP for debugging
            JOptionPane.showMessageDialog(this, "OTP sent to your registered medium!", "OTP Sent", JOptionPane.INFORMATION_MESSAGE);
        });

        loginButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.setBackground(new Color(204, 102, 0));
        loginButton.addActionListener(this::handleLoginAction);

        setBackground(new Color(30, 30, 30));

        enableEnterToNavigate(usernameField, passwordField);
        enableEnterToNavigate(passwordField, otpField);
        bindEnterKeyToButton(otpField, loginButton);
    }

    private void setupLayout() {
        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));

        JLabel otpLabel = new JLabel("OTP");
        otpLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(200, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(titleLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(usernameLabel)
                                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(otpLabel)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(otpField, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE) // Reduced width
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(otpButton, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)) // Button beside field
                                        .addComponent(loginButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(157, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(170)
                                .addComponent(titleLabel)
                                .addGap(28)
                                .addComponent(usernameLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(passwordLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(10)
                                .addComponent(otpLabel)
                                .addGap(0)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(otpField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE) // Aligned field
                                        .addComponent(otpButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)) // Aligned button
                                .addGap(33)
                                .addComponent(loginButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(65))
        );
    }

    private void handleLoginAction(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String submittedOTP = otpField.getText();

        if (!validateLogin(username, password, submittedOTP)) {
            JOptionPane.showMessageDialog(this, "Invalid Credentials or OTP!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateLogin(String username, String password, String submittedOTP) {
        String url = "jdbc:mysql://localhost:3306/passfort";
        String dbUser = "root";
        String dbPassword = "password";

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && sentOTP != null && sentOTP.equals(submittedOTP)) {
                navigateToHome(username);
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void navigateToHome(String username) {
        SwingUtilities.invokeLater(() -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.setContentPane(new Home(username));
                topFrame.revalidate();
                topFrame.repaint();
            }
        });
    }

    private void enableEnterToNavigate(JTextField currentField, JTextField nextField) {
        currentField.addActionListener(e -> nextField.requestFocus());
    }

    private void bindEnterKeyToButton(JTextField textField, JButton button) {
        textField.addActionListener(e -> button.doClick());
    }

    public static String generateOTP(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // Add digits from 0-9
        }
        return otp.toString();
    }
}
