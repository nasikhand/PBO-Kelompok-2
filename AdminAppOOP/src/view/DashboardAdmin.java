package view;

import view.perjalanan.KelolaPerjalananPanel;
import view.pengguna.KelolaPenggunaPanel; // Pastikan Anda memiliki kelas ini

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DashboardAdmin extends JFrame {

    private JPanel currentContentPanel;
    private JPanel dashboardHomePanel;
    // private String subContent2; // Ini dihapus karena tidak diperlukan di sini

    public DashboardAdmin() {
        setTitle("Dashboard Admin - Sinar Jaya Travel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ========== Sidebar ==========
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(33, 45, 62));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Logo
        JLabel logoLabel = new JLabel();
        ImageIcon logo = new ImageIcon(getClass().getResource("/images/logo1.png"));
        if (logo.getImageLoadStatus() == MediaTracker.ERRORED || logo.getIconWidth() == -1) {
            // Fallback jika gambar tidak ditemukan atau rusak
            System.err.println("Warning: Logo image not found or corrupted. Using placeholder.");
            BufferedImage buffer = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = buffer.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 120, 120);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String text = "Logo";
            FontMetrics fm = g.getFontMetrics();
            int x = (buffer.getWidth() - fm.stringWidth(text)) / 2;
            int y = (buffer.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(text, x, y);
            g.dispose();
            logo = new ImageIcon(buffer);
        }
        Image scaledLogo = logo.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(scaledLogo));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Label Panel Admin
        JLabel adminPanelLabel = createSidebarSectionLabel("ADMIN PANEL");
        sidebar.add(adminPanelLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tombol Kelola Perjalanan
        JButton btnKelolaPerjalanan = createSidebarButton("Kelola Perjalanan");
        btnKelolaPerjalanan.addActionListener(e -> switchContent(new KelolaPerjalananPanel(this)));
        sidebar.add(btnKelolaPerjalanan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tombol Kelola Pengguna
        JButton btnKelolaPengguna = createSidebarButton("Kelola Pengguna");
        btnKelolaPengguna.addActionListener(e -> switchContent(new KelolaPenggunaPanel(this)));
        sidebar.add(btnKelolaPengguna);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Label Quick Action
        JLabel quickActionLabel = createSidebarSectionLabel("QUICK ACTION");
        sidebar.add(quickActionLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        // Contoh tombol lain
        JButton btnLaporan = createSidebarButton("Laporan");
        btnLaporan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Halaman Laporan belum dibuat."));
        sidebar.add(btnLaporan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnPengaturan = createSidebarButton("Pengaturan");
        btnPengaturan.addActionListener(e -> JOptionPane.showMessageDialog(this, "Halaman Pengaturan belum dibuat."));
        sidebar.add(btnPengaturan);
        sidebar.add(Box.createVerticalGlue());

        // ========== Header ==========
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(240, 240, 240));
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel welcome = new JLabel("Selamat Datang, Admin!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcome.setForeground(new Color(50, 50, 50));

        JLabel userInfo = new JLabel("User: Administrator");
        userInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userInfo.setForeground(new Color(80, 80, 80));

        header.add(welcome, BorderLayout.WEST);
        header.add(userInfo, BorderLayout.EAST);

        // ========== Konten Dashboard Utama ==========
        dashboardHomePanel = new JPanel();
        dashboardHomePanel.setLayout(new BoxLayout(dashboardHomePanel, BoxLayout.Y_AXIS));
        dashboardHomePanel.setBackground(new Color(248, 248, 248));
        dashboardHomePanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel dashboardTitle = new JLabel("Ikhtisar Dashboard");
        dashboardTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        dashboardTitle.setForeground(new Color(40, 40, 40));
        dashboardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboardHomePanel.add(dashboardTitle);
        dashboardHomePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 25, 0));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // === PERBAIKAN DI SINI: Tambahkan "" untuk argumen subContent2 ===
        statsPanel.add(createInfoCard("Total Booking", "20", "Hari Ini", "300", "Minggu Ini", "", new Color(100, 181, 246)));
        statsPanel.add(createInfoCard("Total Pendapatan", "Rp 12,000,000", "Bulan Ini", "Rp 3,000,000", "Minggu Ini", "", new Color(129, 199, 132)));
        statsPanel.add(createInfoCard("Pengguna Baru", "9", "Hari Ini", "100", "Total", "", new Color(255, 183, 77)));
        statsPanel.add(createInfoCard("Perjalanan Aktif", "123", "Saat Ini", "10", "Akan Datang", "", new Color(229, 115, 115)));
        dashboardHomePanel.add(statsPanel);
        dashboardHomePanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        chartsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        // === PERBAIKAN DI SINI: Tambahkan "" untuk argumen subContent2 ===
        chartsPanel.add(createInfoCard("Grafik Penjualan", "Data akan ditampilkan di sini", "", "", "", "", new Color(179, 157, 219)));
        chartsPanel.add(createInfoCard("Destinasi Terpopuler", "Data akan ditampilkan di sini", "", "", "", "", new Color(144, 202, 249)));
        dashboardHomePanel.add(chartsPanel);
        dashboardHomePanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // === PERBAIKAN DI SINI: Tambahkan "" untuk argumen subContent2 ===
        JPanel recentBooking = createInfoCard("Booking Terbaru", "Daftar booking terbaru akan muncul di sini.", "", "", "", "", new Color(255, 224, 130));
        recentBooking.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        recentBooking.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboardHomePanel.add(recentBooking);

        // Atur panel konten awal
        currentContentPanel = dashboardHomePanel;

        // Gabungkan semua ke JFrame
        add(sidebar, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
        add(currentContentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Metode pembantu untuk membuat label bagian di sidebar
    private JLabel createSidebarSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(new Color(150, 150, 150));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 5, 5, 0));
        return label;
    }

    // Metode pembantu untuk membuat tombol sidebar dengan gaya seragam
    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(58, 130, 247));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 110, 220), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efek hover sederhana
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 140, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(58, 130, 247));
            }
        });
        return button;
    }

    // Metode pembantu untuk membuat kartu informasi yang lebih kaya
    // Parameter subContent2 sekarang digunakan
    private JPanel createInfoCard(String title, String mainContent, String subTitle1, String subContent1, String subTitle2, String subContent2, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setOpaque(true);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel mainContentLabel = new JLabel(mainContent);
        mainContentLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        mainContentLabel.setForeground(Color.WHITE);
        mainContentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(mainContentLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tambahkan detail tambahan jika ada
        if (!subTitle1.isEmpty() && !subContent1.isEmpty()) {
            JLabel sub1 = new JLabel(subTitle1 + ": " + subContent1);
            sub1.setFont(new Font("SansSerif", Font.PLAIN, 12));
            sub1.setForeground(new Color(230, 230, 230));
            sub1.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(sub1);
        }
        // Pastikan subTitle2 dan subContent2 ada sebelum menampilkannya
        if (!subTitle2.isEmpty() && !subContent2.isEmpty()) {
            JLabel sub2 = new JLabel(subTitle2 + ": " + subContent2);
            sub2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            sub2.setForeground(new Color(230, 230, 230));
            sub2.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(sub2);
        }
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // Metode untuk mengganti panel konten utama
    public void switchContent(JPanel newPanel) {
        if (currentContentPanel != null) {
            remove(currentContentPanel);
        }
        currentContentPanel = newPanel;
        add(currentContentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // Metode untuk kembali ke dashboard utama
    public void backToDashboard() {
        switchContent(dashboardHomePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardAdmin::new);
    }
}