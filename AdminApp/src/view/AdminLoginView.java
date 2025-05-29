/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AdminController;
import model.Admin;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminLoginView extends JFrame {

    public AdminLoginView() {
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Login Admin - Sinar Jaya Travel");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(new Color(45, 45, 45));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo1.png"));
            Image image = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            mainPanel.add(new JLabel(new ImageIcon(image)), gbc);
        } catch (Exception e) {
            mainPanel.add(new JLabel("Logo tidak ditemukan"), gbc);
        }

        JLabel titleLabel = new JLabel("Login Administrator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        JLabel emailLabel = new JLabel("Alamat Email");
        emailLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(emailLabel, gbc);
        JTextField emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Kata Sandi");
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(passwordLabel, gbc);
        JPasswordField passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(2, 117, 216));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            AdminController adminController = new AdminController();
            Admin admin = adminController.login(emailField.getText(), new String(passwordField.getPassword()));

            if (admin != null) {
                dispose();
                new AdminDashboardView(admin).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Email atau kata sandi salah.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}