package com.PasswordManager;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Main extends JFrame {
    private Login login;
    private Register register;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JPanel jPanel2;

    public void setLogin() {
        if (login == null) {
            login = new Login();
        }

        jPanel2.removeAll();
        jPanel2.add(login);
        SwingUtilities.updateComponentTreeUI(jPanel2);
    }

    public void setRegister() {
        if (register == null) {
            register = new Register();
        }

        jPanel2.removeAll();
        jPanel2.add(register);
        SwingUtilities.updateComponentTreeUI(jPanel2);
    }

    public Main() {
        initComponents();
        setLogin();
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jPanel2 = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1243, 834));
        jPanel1.setBackground(new Color(44, 44, 44));
        jLabel1.setBackground(new Color(64, 64, 64));
        jLabel1.setFont(new Font("Segoe UI Semibold", Font.BOLD, 35));
        jLabel1.setForeground(new Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("PassFort");
        jLabel1.setOpaque(true);
        jButton1.setBackground(new Color(77, 120, 204));
        jButton1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        jButton1.setForeground(new Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.setBorderPainted(false);
        jButton1.setPreferredSize(new Dimension(0, 50));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jButton2.setBackground(new Color(44, 44, 44));
        jButton2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        jButton2.setForeground(new Color(255, 255, 255));
        jButton2.setText("Register");
        jButton2.setBorderPainted(false);
        jButton2.setPreferredSize(new Dimension(0, 50));
        jButton2.addActionListener(this::jButton2ActionPerformed);
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                                .addGap(139, 139, 139)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 520, Short.MAX_VALUE))
        );
        jPanel2.setBackground(new Color(30, 30, 30));
        jPanel2.setLayout(new BorderLayout());
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        jButton1.setBackground(new Color(77, 120, 204));
        jButton2.setBackground(new Color(44, 44, 44));
        setLogin();
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        jButton2.setBackground(new Color(77, 120, 204));
        jButton1.setBackground(new Color(44, 44, 44));
        setRegister();
    }

    private void NewAccActionPerformed(ActionEvent e) {
        // Placeholder for new account action
    }

    private void ExistingAccActionPerformed(ActionEvent e) {
        // Placeholder for existing account action
    }

    public static void main(String[] args) {
        FlatDarkLaf.setup();
        EventQueue.invokeLater(() -> new Main().setVisible(true));
    }
}
