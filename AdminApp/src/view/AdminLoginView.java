/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import config.ImageUtil; // <-- Pastikan import ini ada
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
        setSize(450, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 40, 40));
        mainPanel.setBackground(new Color(45, 45, 45));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            ImageIcon resizedIcon = ImageUtil.loadAndResizeFromResource("/resources/logo1.png", 130, 130);
            JLabel logoLabel = new JLabel(resizedIcon != null ? resizedIcon : new JLabel("Logo?").getIcon());
            gbc.insets = new Insets(10, 10, 20, 10);
            mainPanel.add(logoLabel, gbc);
        } catch (Exception e) {
            JLabel logoErrorLabel = new JLabel("Logo tidak ditemukan");
            logoErrorLabel.setForeground(Color.GRAY);
            mainPanel.add(logoErrorLabel, gbc);
        }

        gbc.insets = new Insets(10, 10, 10, 10);
        JLabel titleLabel = new JLabel("Administrator Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 10, 20, 10);
        mainPanel.add(titleLabel, gbc);

        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        JLabel emailLabel = new JLabel("Alamat Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(emailLabel, gbc);
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Kata Sandi");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.LIGHT_GRAY);
        mainPanel.add(passwordLabel, gbc);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(passwordField, gbc);

        gbc.insets = new Insets(25, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(100, 45));
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            AdminController adminController = new AdminController();
            Admin admin = adminController.login(emailField.getText(), new String(passwordField.getPassword()));

            if (admin != null) {
                dispose();
                // Tampilkan dashboard di thread yang benar
                SwingUtilities.invokeLater(() -> new AdminDashboardView(admin).setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Email atau kata sandi salah.", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}