/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.AuthController;
import model.Admin;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    public LoginForm() {
        setTitle("Login - Sinar Jaya Travel");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        // Logo
//        JLabel logoLabel = new JLabel();
//        ImageIcon logo = new ImageIcon("images/logo1.png"); 
//        Image scaledLogo = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//        logoLabel.setIcon(new ImageIcon(scaledLogo));
//        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Form login
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Masuk");
        loginButton.setBackground(new Color(58, 130, 247));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        // Tambahkan komponen ke formPanel
        formPanel.add(new JLabel("Email"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password"));
        formPanel.add(passwordField);
        formPanel.add(loginButton);

        // Aksi Login
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            Admin admin = AuthController.login(email, password);
            if (admin != null) {
                JOptionPane.showMessageDialog(this, "Selamat datang, " + admin.getNamaLengkap());
                new DashboardAdmin();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email atau password salah.", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Gabungkan ke panel utama
//        mainPanel.add(logoLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }
}

