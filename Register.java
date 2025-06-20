package com.PasswordManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Register extends JPanel {
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JPasswordField jPasswordField;
    private JLabel passwordStrengthLabel;
    private JButton jButton1;

    private void checkPasswordStrength() {
        String password = new String(jPasswordField.getPassword());
        String strength = getPasswordStrength(password);

        switch (strength) {
            case "Weak" -> passwordStrengthLabel.setForeground(Color.RED);
            case "Intermediate" -> passwordStrengthLabel.setForeground(Color.YELLOW);
            case "Strong" -> passwordStrengthLabel.setForeground(Color.GREEN);
        }
        passwordStrengthLabel.setText(strength + " password");
    }

    private String getPasswordStrength(String password) {
        int length = password.length();
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()-_+=<>?".indexOf(ch) >= 0);

        if (length >= 8 && hasUpper && hasLower && hasDigit && hasSpecial) return "Strong";
        if (length >= 6 && ((hasUpper && hasLower) || (hasLower && hasDigit) || (hasUpper && hasDigit))) return "Intermediate";
        return "Weak";
    }

    public Register() {
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();
        jPasswordField = new JPasswordField();
        jButton1 = new JButton("Register");
        passwordStrengthLabel = new JLabel();
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
                SwingUtilities.invokeLater(() -> {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(Register.this);
                    if (topFrame != null) {
                        topFrame.setContentPane(new Login());
                        topFrame.revalidate();
                        topFrame.repaint();
                    }
                });
            }
        });
        setBackground(new Color(30, 30, 30));
        setupLayout();
    }
    private void setupLayout() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        JLabel jLabel1 = new JLabel("Register", JLabel.CENTER);
        jLabel1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 30));
        JLabel jLabel2 = new JLabel("Name");
        jLabel2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        JLabel jLabel3 = new JLabel("Username");
        jLabel3.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        JLabel jLabel4 = new JLabel("Phone Number");
        jLabel4.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        JLabel jLabel5 = new JLabel("Password");
        jLabel5.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        passwordStrengthLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        jTextField1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        enableEnterToNavigate(jTextField1, jTextField2);
        jTextField2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        enableEnterToNavigate(jTextField2, jTextField3);
        jTextField3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jTextField3.setText("+91 ");
        jTextField3.setForeground(Color.GRAY);
        jTextField3.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (jTextField3.getText().equals("+91 ")) {
                    jTextField3.setText(""); // Clear placeholder
                    jTextField3.setForeground(Color.lightGray); // Set normal text color
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (jTextField3.getText().isEmpty()) {
                    jTextField3.setText("+91 "); // Restore placeholder
                    jTextField3.setForeground(Color.GRAY); // Set placeholder text color
                }
            }
        });
        enableEnterToNavigate(jTextField3, jPasswordField);
        jPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jPasswordField.setPreferredSize(new Dimension(0, 50));
        jPasswordField.getDocument().addDocumentListener((SimpleDocumentListener) e -> checkPasswordStrength());
        passwordStrengthLabel.setFont(new Font("Segoe UI Semibold", 0, 18));
        passwordStrengthLabel.setText("");

        jButton1.setBackground(new Color(204, 102, 0));
        jButton1.setForeground(Color.WHITE);
        jButton1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        bindEnterKeyToButton(jPasswordField, jButton1);

        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap(500, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addComponent(jTextField1)
                                .addComponent(jLabel3)
                                .addComponent(jTextField2)
                                .addComponent(jLabel4)
                                .addComponent(jTextField3)
                                .addComponent(jLabel5)
                                .addComponent(jPasswordField)
                                .addComponent(passwordStrengthLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(230))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(90)
                .addComponent(jLabel1)
                .addGap(20)
                .addComponent(jLabel2)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addComponent(jLabel3)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addComponent(jLabel4)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(20)
                .addComponent(jLabel5)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPasswordField, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(10)
                .addComponent(passwordStrengthLabel)
                .addGap(20)
                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                .addGap(50)
        );
    }
    private void registerUser() {
        String name = jTextField1.getText();
        String username = jTextField2.getText();
        String phone = jTextField3.getText();
        String password = new String(jPasswordField.getPassword());

        String dbUrl = "jdbc:mysql://localhost:3306/passfort";
        String dbUser = "root";
        String dbPassword = "password";
        String query = "INSERT INTO users (name, username, phone, password) VALUES (?, ?, ?, ?)";
        String create_vault1 = "create table " + username + "(website varchar(100), password varchar(100), strength int)"; //Strength in 0, 1, 2
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, phone);
            stmt.setString(4, password);
            int rowsInserted = stmt.executeUpdate();
            stmt.execute(create_vault1);
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enableEnterToNavigate(JTextField currentField, JTextField nextField) {
        currentField.addActionListener(e -> nextField.requestFocus());
    }

    private void bindEnterKeyToButton(JTextField textField, JButton button) {
        textField.addActionListener(e -> button.doClick());
    }
}
// Document listener interface for Java 8 compatibility
@FunctionalInterface
interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
    void update(javax.swing.event.DocumentEvent e);

    @Override
    default void insertUpdate(javax.swing.event.DocumentEvent e) {
        update(e);
    }

    @Override
    default void removeUpdate(javax.swing.event.DocumentEvent e) {
        update(e);
    }

    @Override
    default void changedUpdate(javax.swing.event.DocumentEvent e) {
        update(e);
    }
}