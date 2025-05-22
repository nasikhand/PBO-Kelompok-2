/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.DashboardController;

import javax.swing.*;
import java.awt.*;

public class DashboardAdmin extends JFrame {

    private CardLayout contentLayout;
    private JPanel contentPanel;

    public DashboardAdmin() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Sidebar ===
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30, 30, 30));
        sidebar.setPreferredSize(new Dimension(180, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel sidebarTitle = new JLabel("Admin Panel");
        sidebarTitle.setForeground(Color.WHITE);
        sidebarTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidebarTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(sidebarTitle);

        sidebar.add(Box.createVerticalStrut(20));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnKelolaPaket = new JButton("Kelola Perjalanan");

        btnDashboard.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnKelolaPaket.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnKelolaPaket);

        // === Content Area (CardLayout) ===
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        contentPanel.add(makeDashboardPanel(), "dashboard");
        contentPanel.add(new PaketPanel(), "paket");

        // === Button Actions ===
        btnDashboard.addActionListener(e -> contentLayout.show(contentPanel, "dashboard"));
        btnKelolaPaket.addActionListener(e -> contentLayout.show(contentPanel, "paket"));

        // === Layout All ===
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // === DASHBOARD PANEL ===
    private JPanel makeDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Top Stats
        JPanel topInfoPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        topInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        topInfoPanel.setBackground(Color.WHITE);

        topInfoPanel.add(createStatPanel("📦 Booking",
                "Today: " + DashboardController.getBookingToday() +
                        "<br>Week: " + DashboardController.getBookingWeek(),
                new Color(70, 130, 180)));

        topInfoPanel.add(createStatPanel("💰 Revenue",
                "Rp " + DashboardController.getRevenueThisWeek() +
                        "<br>This Week", new Color(46, 139, 87)));

        topInfoPanel.add(createStatPanel("👤 User",
                "New: " + DashboardController.getUserNewThisWeek() +
                        "<br>Total: " + DashboardController.getUserTotal(),
                new Color(218, 112, 214)));

        topInfoPanel.add(createStatPanel("🧳 Trips",
                DashboardController.getTripsActive() + "<br>Active",
                new Color(255, 165, 0)));

        // Center Panels
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        centerPanel.add(createBoxPanel("📈 Sales Performance", "..."));
        centerPanel.add(createBoxPanel("🌍 Top Destination", "..."));
        centerPanel.add(createBoxPanel("🕒 Recent Booking", "..."));
        centerPanel.add(new JPanel());

        // User Info Footer
        JLabel userInfoLabel = new JLabel("Login sebagai [Admin]", SwingConstants.RIGHT);
        userInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        userInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(topInfoPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(userInfoLabel, BorderLayout.SOUTH);

        return panel;
    }

    // === Utility Components ===

    private JPanel createStatPanel(String title, String htmlContent, Color bgColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel contentLabel = new JLabel("<html><center>" + htmlContent + "</center></html>", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentLabel.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBoxPanel(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        JLabel contentLabel = new JLabel(content, SwingConstants.CENTER);
        panel.add(contentLabel, BorderLayout.CENTER);
        return panel;
    }
}

