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
        setSize(450, 520); // Sedikit lebih tinggi untuk padding
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 40, 40)); // Padding lebih
        mainPanel.setBackground(new Color(45, 45, 45));
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            // Pastikan logo.png ada di src/resources/
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/logo.png"));
            Image image = logoIcon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(image));
            gbc.insets = new Insets(10, 10, 20, 10); // Margin bawah logo
            mainPanel.add(logoLabel, gbc);
        } catch (Exception e) {
            JLabel logoErrorLabel = new JLabel("Logo tidak ditemukan");
            logoErrorLabel.setForeground(Color.GRAY);
            mainPanel.add(logoErrorLabel, gbc);
        }

        gbc.insets = new Insets(10, 10, 10, 10); // Kembalikan insets
        JLabel titleLabel = new JLabel("Administrator Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        gbc.insets = new Insets(0, 10, 20, 10); // Margin bawah judul
        mainPanel.add(titleLabel, gbc);

        gbc.insets = new Insets(8, 5, 8, 5); // Perkecil insets form
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
        gbc.fill = GridBagConstraints.HORIZONTAL; // Tombol mengisi horizontal
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(100, 45)); // Tinggi tombol
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