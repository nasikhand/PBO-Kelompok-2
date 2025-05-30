package managementtrevel; // Pastikan package sesuai

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import managementtrevel.HomeUser.PanelBeranda; // Impor PanelBeranda
// Impor panel-panel lain yang akan Anda buat
// import managementtrevel.Destinations.PanelDestinasi;
// import managementtrevel.Bookings.PanelPemesanan;
// import managementtrevel.Profile.PanelProfil;
import model.UserModel; // Jika Anda meneruskan UserModel

public class MainAppFrame extends JFrame {

    private JPanel mainPanelContainer; // Mengganti nama dari mainPanel agar lebih deskriptif
    private CardLayout cardLayout;
    private UserModel currentUser; // Opsional, jika Anda meneruskan user

    // Konstanta untuk nama panel
    public static final String PANEL_BERANDA = "PanelBeranda";
    public static final String PANEL_DESTINASI = "PanelDestinasi";
    public static final String PANEL_PEMESANAN = "PanelPemesanan";
    public static final String PANEL_PROFIL = "PanelProfil";
    // Tambahkan nama panel lain jika ada

    // Konstruktor bisa menerima UserModel jika diperlukan
    public MainAppFrame(UserModel user) {
        this(); // Panggil konstruktor default
        this.currentUser = user;
        // Anda bisa meneruskan currentUser ke panel yang membutuhkan, misalnya PanelProfil
        // Atau memanggil metode di PanelBeranda jika perlu data user
        // Contoh: PanelBeranda panelBeranda = (PanelBeranda) getPanelByName(PANEL_BERANDA);
        // if (panelBeranda != null) {
        // panelBeranda.updateUserInfo(user); // Anda perlu buat metode ini di PanelBeranda
        // }
    }


    public MainAppFrame() {
        setTitle("Travel App - Utama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700); // Sesuaikan ukuran agar pas dengan PanelBeranda + Navigasi
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);
        mainPanelContainer.setBackground(Color.decode("#F0F0F0")); // Warna latar belakang netral

        // --- Buat dan Tambahkan Panel-Panel Aplikasi ---

        // 1. Panel Beranda (HomeScreen yang sudah jadi JPanel)
        PanelBeranda panelBeranda = new PanelBeranda();
        mainPanelContainer.add(panelBeranda, PANEL_BERANDA);

        // 2. Panel Destinasi (Contoh, Anda akan buat kelasnya sendiri)
        // PanelDestinasi panelDestinasi = new PanelDestinasi(this); // 'this' jika perlu referensi MainAppFrame
        // mainPanelContainer.add(panelDestinasi, PANEL_DESTINASI);
        JPanel panelDestinasiPlaceholder = createPlaceholderPanel("Halaman Destinasi", Color.ORANGE);
        mainPanelContainer.add(panelDestinasiPlaceholder, PANEL_DESTINASI);


        // 3. Panel Pemesanan (Contoh)
        // PanelPemesanan panelPemesanan = new PanelPemesanan(this.currentUser); // Kirim user jika perlu
        // mainPanelContainer.add(panelPemesanan, PANEL_PEMESANAN);
        JPanel panelPemesananPlaceholder = createPlaceholderPanel("Halaman Pemesanan", Color.GREEN);
        mainPanelContainer.add(panelPemesananPlaceholder, PANEL_PEMESANAN);

        // 4. Panel Profil (Contoh)
        // PanelProfil panelProfil = new PanelProfil(this.currentUser);
        // mainPanelContainer.add(panelProfil, PANEL_PROFIL);
        JPanel panelProfilPlaceholder = createPlaceholderPanel("Halaman Profil Pengguna", Color.MAGENTA);
        mainPanelContainer.add(panelProfilPlaceholder, PANEL_PROFIL);


        // --- Buat Komponen Navigasi ---
        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        navigationPanel.setBackground(Color.decode("#333333")); // Warna gelap untuk navigasi

        JButton homeButton = createNavButton("Beranda", PANEL_BERANDA);
        JButton destinationsButton = createNavButton("Destinasi", PANEL_DESTINASI);
        JButton bookingsButton = createNavButton("Pemesanan", PANEL_PEMESANAN);
        JButton profileButton = createNavButton("Profil", PANEL_PROFIL);
        
        JButton logoutButton = new JButton("Logout");
        styleNavButton(logoutButton); // Style tombol logout
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                // Kembali ke AuthFrame
                // Asumsi AuthFrame ada di package yang sama atau diimpor dengan benar
                new AuthFrame().setVisible(true); 
                System.out.println("Logout berhasil. Kembali ke AuthFrame.");
            }
        });

        navigationPanel.add(homeButton);
        navigationPanel.add(destinationsButton);
        navigationPanel.add(bookingsButton);
        navigationPanel.add(profileButton);
        navigationPanel.add(Box.createHorizontalStrut(30)); // Spasi sebelum logout
        navigationPanel.add(logoutButton);

        add(navigationPanel, BorderLayout.NORTH);
        add(mainPanelContainer, BorderLayout.CENTER);

        showPanel(PANEL_BERANDA); // Tampilkan PanelBeranda sebagai default
    }

    private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        styleNavButton(button);
        button.addActionListener(e -> showPanel(panelName));
        return button;
    }

    private void styleNavButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#555555"));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Efek hover sederhana
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#777777"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#555555"));
            }
        });
    }
    
    // Metode untuk membuat panel placeholder (ganti dengan panel kustom Anda)
    private JPanel createPlaceholderPanel(String text, Color color) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(color);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label);
        return panel;
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanelContainer, panelName);
        System.out.println("Menampilkan panel: " + panelName);
    }

    // Opsional: Metode untuk mendapatkan panel berdasarkan nama jika perlu interaksi
    public JPanel getPanelByName(String panelName) {
        for (Component comp : mainPanelContainer.getComponents()) {
            if (mainPanelContainer.getComponentZOrder(comp) != -1 && // Memastikan komponen adalah 'card'
                panelName.equals(getCardName(comp))) { // Perlu cara untuk mendapatkan nama card dari komponen
                return (JPanel) comp;
            }
        }
        return null;
    }

    // Helper untuk mendapatkan nama card (ini asumsi, CardLayout tidak menyimpan nama di komponen)
    // Cara yang lebih baik adalah menyimpan referensi panel dalam Map jika perlu diakses sering.
    private String getCardName(Component comp) {
        // Ini adalah batasan CardLayout, kita tidak bisa langsung dapat nama dari komponen.
        // Saat menambahkan, kita tahu nama dan komponennya.
        if (comp instanceof PanelBeranda) return PANEL_BERANDA;
        // Tambahkan instanceof untuk panel lain
        // if (comp instanceof PanelDestinasi) return PANEL_DESTINASI;
        return null; 
    }


    // Main method untuk testing MainAppFrame secara terpisah (opsional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Jika MainAppFrame memerlukan UserModel, Anda bisa buat dummy user untuk testing
            // UserModel dummyUser = new UserModel();
            // dummyUser.setNamaLengkap("Test User");
            // MainAppFrame mainAppFrame = new MainAppFrame(dummyUser);
            
            MainAppFrame mainAppFrame = new MainAppFrame(); // Menggunakan konstruktor tanpa user
            mainAppFrame.setVisible(true);
        });
    }
}
