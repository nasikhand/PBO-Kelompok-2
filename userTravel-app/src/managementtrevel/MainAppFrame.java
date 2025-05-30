package managementtrevel; // Pastikan package sesuai

import javax.swing.*;
import java.awt.*;
// import java.awt.event.ActionEvent; // Tidak lagi dibutuhkan jika topNav dihapus
// import java.awt.event.ActionListener; // Tidak lagi dibutuhkan jika topNav dihapus
import managementtrevel.HomeUser.PanelBeranda; // Impor PanelBeranda
import Asset.SidebarPanel; // Impor SidebarPanel
import model.UserModel; 

public class MainAppFrame extends JFrame {

    private JPanel mainPanelContainer; 
    private CardLayout cardLayout;
    private UserModel currentUser; 
    private SidebarPanel sidebarPanel; 

    // Konstanta untuk nama panel
    public static final String PANEL_BERANDA = "PanelBeranda";
    public static final String PANEL_DESTINASI = "PanelDestinasi";
    public static final String PANEL_PEMESANAN = "PanelPemesanan";
    public static final String PANEL_PROFIL = "PanelProfil"; // Ini bisa jadi profil umum
    public static final String PANEL_USER_PROFILE = "PanelUserProfile"; // Ini untuk UserProfile dari Sidebar

    public MainAppFrame(UserModel user) {
        this(); 
        this.currentUser = user;
        // Jika SidebarPanel atau panel lain perlu info user, teruskan di sini
        // Contoh: sidebarPanel.updateUserInfo(user); // Perlu metode ini di SidebarPanel
        // Atau jika PanelProfil (yang di CardLayout) perlu user:
        // Component profilCard = getPanelByName(PANEL_PROFIL);
        // if (profilCard instanceof PanelProfil) { // Asumsikan PanelProfil adalah kelas Anda
        //     ((PanelProfil) profilCard).setUserModel(user);
        // }
        Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
        // if (userProfilCard instanceof managementtrevel.HomeUser.UserProfile) { // Ganti dengan path kelas UserProfile Anda jika berbeda
        //     ((managementtrevel.HomeUser.UserProfile) userProfilCard).setUserModel(user); // Asumsikan ada metode setUserModel
        // }


    }

    public MainAppFrame() {
        setTitle("Travel App - Utama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 700); 
        setMinimumSize(new Dimension(850, 600));
        setLocationRelativeTo(null);

        // --- Inisialisasi Sidebar ---
        sidebarPanel = new SidebarPanel(this); 

        // --- Inisialisasi CardLayout dan Panel Konten Utama ---
        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);
        // Gunakan warna dari AppTheme jika tersedia, atau default
        // mainPanelContainer.setBackground(Asset.AppTheme.PANEL_BACKGROUND); 
        mainPanelContainer.setBackground(Color.decode("#F0F0F0"));


        // 1. Panel Beranda 
        PanelBeranda panelBeranda = new PanelBeranda(); 
        mainPanelContainer.add(panelBeranda, PANEL_BERANDA);

        // 2. Panel Destinasi (Placeholder)
        JPanel panelDestinasiPlaceholder = createPlaceholderPanel("Halaman Destinasi", Color.ORANGE);
        mainPanelContainer.add(panelDestinasiPlaceholder, PANEL_DESTINASI);

        // 3. Panel Pemesanan (Placeholder)
        JPanel panelPemesananPlaceholder = createPlaceholderPanel("Halaman Pemesanan", Color.GREEN);
        mainPanelContainer.add(panelPemesananPlaceholder, PANEL_PEMESANAN);

        // 4. Panel Profil (Placeholder untuk PANEL_PROFIL jika berbeda dari UserProfile)
        JPanel panelProfilUmumPlaceholder = createPlaceholderPanel("Halaman Pengaturan Akun (Umum)", Color.MAGENTA);
        mainPanelContainer.add(panelProfilUmumPlaceholder, PANEL_PROFIL);

        // 5. Panel UserProfile (dari Sidebar)
        // Pastikan Anda memiliki kelas UserProfile yang merupakan JPanel
        // dan tambahkan di sini. Untuk sekarang, saya gunakan placeholder.
        // Jika UserProfile Anda sudah merupakan JPanel:
        // managementtrevel.HomeUser.UserProfile panelUserProfile = new managementtrevel.HomeUser.UserProfile();
        // mainPanelContainer.add(panelUserProfile, PANEL_USER_PROFILE);
        JPanel panelUserProfilePlaceholder = createPlaceholderPanel("Halaman Profil Pengguna (dari Sidebar)", Color.CYAN);
        mainPanelContainer.add(panelUserProfilePlaceholder, PANEL_USER_PROFILE);


        // --- HAPUS Komponen Navigasi Atas ---
        /* JPanel topNavigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        topNavigationPanel.setBackground(Color.decode("#333333")); 

        JButton homeButtonTopNav = createNavButtonTop("Beranda", PANEL_BERANDA, true);
        JButton destinationsButtonTopNav = createNavButtonTop("Destinasi", PANEL_DESTINASI, true);
        JButton bookingsButtonTopNav = createNavButtonTop("Pemesanan", PANEL_PEMESANAN, true);
        JButton profileButtonTopNav = createNavButtonTop("Profil", PANEL_PROFIL, true);
        
        JButton logoutButtonTopNav = new JButton("Logout");
        styleNavButton(logoutButtonTopNav, true); 
        logoutButtonTopNav.addActionListener(e -> performLogout());

        topNavigationPanel.add(homeButtonTopNav);
        topNavigationPanel.add(destinationsButtonTopNav);
        topNavigationPanel.add(bookingsButtonTopNav);
        topNavigationPanel.add(profileButtonTopNav);
        topNavigationPanel.add(Box.createHorizontalStrut(30)); 
        topNavigationPanel.add(logoutButtonTopNav);
        */

        // --- Tata Letak Utama Frame ---
        // Menggunakan BorderLayout: Sidebar di KIRI, Konten di TENGAH
        add(sidebarPanel, BorderLayout.WEST);
        // add(topNavigationPanel, BorderLayout.NORTH); // BARIS INI DIHAPUS
        add(mainPanelContainer, BorderLayout.CENTER);

        showPanel(PANEL_BERANDA); 
    }

    // Metode createNavButtonTop dan styleNavButton(button, isTopNav) tidak lagi diperlukan
    // jika topNavigationPanel dihapus sepenuhnya.
    /*
    private JButton createNavButtonTop(String text, String panelName, boolean isTopNav) {
        JButton button = new JButton(text);
        styleNavButton(button, isTopNav);
        button.addActionListener(e -> showPanel(panelName));
        return button;
    }
    
    private void styleNavButton(JButton button, boolean isTopNav) {
        button.setForeground(Color.WHITE);
        button.setBackground(Color.decode("#555555"));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#777777"));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#555555"));
            }
        });
    }
    */
    
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
        // Jika Anda ingin sidebar collapse saat panel berubah (opsional)
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) { 
           sidebarPanel.collapsePublic(); 
        }
    }
    
    // Metode untuk mendapatkan panel berdasarkan nama jika perlu interaksi
    public Component getPanelByName(String panelName) { // Mengembalikan Component agar lebih fleksibel
        for (Component comp : mainPanelContainer.getComponents()) {
            // CardLayout menyimpan komponen dengan nama constraint-nya.
            // Kita perlu cara untuk mencocokkan komponen dengan nama yang kita berikan saat add().
            // Sayangnya, CardLayout tidak menyediakan cara mudah untuk mendapatkan nama dari komponen.
            // Salah satu cara adalah dengan memeriksa instanceof jika nama panel unik per kelas.
            if (comp instanceof PanelBeranda && panelName.equals(PANEL_BERANDA)) return comp;
            // if (comp instanceof PanelDestinasi && panelName.equals(PANEL_DESTINASI)) return comp; // Ganti dengan kelas PanelDestinasi Anda
            // if (comp instanceof PanelPemesanan && panelName.equals(PANEL_PEMESANAN)) return comp;
            // if (comp instanceof PanelProfilUmum && panelName.equals(PANEL_PROFIL)) return comp; // Jika ada kelas PanelProfilUmum
            if (comp instanceof managementtrevel.HomeUser.UserProfile && panelName.equals(PANEL_USER_PROFILE)) return comp; // Ganti dengan path kelas UserProfile Anda

            // Untuk placeholder, kita tidak bisa cek instanceof dengan mudah kecuali mereka kelas unik.
            // Jika Anda menggunakan placeholder dan perlu mengambilnya, Anda mungkin perlu
            // menyimpan referensinya dalam Map saat menambahkannya.
        }
        System.err.println("Peringatan: Panel dengan nama '" + panelName + "' tidak ditemukan atau tidak dapat dicocokkan tipenya.");
        return null; 
    }


    public void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // Pastikan path ke AuthFrame benar
            // Jika AuthFrame ada di package managementtrevel
             new AuthFrame().setVisible(true); 
            // Jika AuthFrame ada di package managementtrevel.LoginAndRegist
            // new managementtrevel.LoginAndRegist.AuthFrame().setVisible(true); 
            System.out.println("Logout berhasil. Kembali ke AuthFrame.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Untuk testing, Anda bisa buat dummy user jika diperlukan oleh konstruktor
            // UserModel dummyUser = new UserModel(); 
            // dummyUser.setNamaLengkap("Pengguna Uji");
            // MainAppFrame mainAppFrame = new MainAppFrame(dummyUser);
            MainAppFrame mainAppFrame = new MainAppFrame();
            mainAppFrame.setVisible(true);
        });
    }
}
