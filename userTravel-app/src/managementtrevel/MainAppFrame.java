package managementtrevel;

import javax.swing.*;
import java.awt.*;
import managementtrevel.HomeUser.PanelBeranda;
import managementtrevel.HomeUser.PanelUserProfil; // Impor PanelUserProfil yang sudah diubah
import Asset.SidebarPanel;
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
    public static final String PANEL_USER_PROFILE = "PanelUserProfile"; // Ini untuk PanelUserProfil dari Sidebar

    public MainAppFrame(UserModel user) {
        this(); // Panggil konstruktor tanpa argumen untuk inisialisasi dasar
        this.currentUser = user;
        // Setelah komponen diinisialisasi, set data user ke PanelUserProfil
        Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
        if (userProfilCard instanceof PanelUserProfil) { // Menggunakan PanelUserProfil
            ((PanelUserProfil) userProfilCard).setProfileData(); // Panggil metode untuk memuat data user
        }
        // Anda juga bisa meneruskan user ke sidebar jika dibutuhkan
        // sidebarPanel.updateUserInfo(user); // Jika ada metode ini di SidebarPanel
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

        // 4. Panel Profil (Placeholder untuk PANEL_PROFIL jika berbeda dari PanelUserProfil)
        JPanel panelProfilUmumPlaceholder = createPlaceholderPanel("Halaman Pengaturan Akun (Umum)", Color.MAGENTA);
        mainPanelContainer.add(panelProfilUmumPlaceholder, PANEL_PROFIL);

        // 5. Panel UserProfile (dari Sidebar) - Menggunakan kelas PanelUserProfil yang sudah diubah
        PanelUserProfil panelUserProfil = new PanelUserProfil(this); // Teruskan referensi MainAppFrame
        mainPanelContainer.add(panelUserProfil, PANEL_USER_PROFILE);

        // --- Tata Letak Utama Frame ---
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanelContainer, BorderLayout.CENTER);

        showPanel(PANEL_BERANDA); // Tampilkan panel beranda saat aplikasi dimulai
    }

    private JPanel createPlaceholderPanel(String text, Color color) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(color);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(label);
        return panel;
    }

    /**
     * Menampilkan panel tertentu di mainPanelContainer.
     * @param panelName Nama panel yang akan ditampilkan.
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanelContainer, panelName);
        System.out.println("Menampilkan panel: " + panelName);
        // Jika Anda ingin sidebar collapse saat panel berubah (opsional)
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
           sidebarPanel.collapsePublic();
        }
        // Jika panel yang ditampilkan adalah PanelUserProfil, panggil setProfileData untuk refresh
        if (panelName.equals(PANEL_USER_PROFILE)) {
            Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
            if (userProfilCard instanceof PanelUserProfil) { // Menggunakan PanelUserProfil
                ((PanelUserProfil) userProfilCard).setProfileData();
            }
        }
    }

    /**
     * Metode untuk mendapatkan panel berdasarkan nama jika perlu interaksi.
     * @param panelName Nama panel yang dicari.
     * @return Komponen panel atau null jika tidak ditemukan.
     */
    public Component getPanelByName(String panelName) {
        for (Component comp : mainPanelContainer.getComponents()) {
            if (comp instanceof PanelBeranda && panelName.equals(PANEL_BERANDA)) return comp;
            if (comp instanceof PanelUserProfil && panelName.equals(PANEL_USER_PROFILE)) return comp; // Menggunakan PanelUserProfil
            // Tambahkan pengecekan untuk kelas panel Anda yang lain di sini
            // Contoh:
            // if (comp instanceof PanelDestinasi && panelName.equals(PANEL_DESTINASI)) return comp;
            // if (comp instanceof PanelPemesanan && panelName.equals(PANEL_PEMESANAN)) return comp;
            // if (comp instanceof UbahPasswordPanel && panelName.equals(PANEL_UBAH_PASSWORD)) return comp;
            // if (comp instanceof OrderHistoryPanel && panelName.equals(PANEL_RIWAYAT_PESANAN)) return comp;
            // if (comp instanceof UserOrderPanel && panelName.equals(PANEL_PESANAN_SAYA)) return comp;

            // Untuk placeholder, kita tidak bisa cek instanceof dengan mudah kecuali mereka kelas unik.
            // Jika Anda menggunakan placeholder dan perlu mengambilnya, Anda mungkin perlu
            // menyimpan referensinya dalam Map saat menambahkannya.
        }
        System.err.println("Peringatan: Panel dengan nama '" + panelName + "' tidak ditemukan atau tidak dapat dicocokkan tipenya.");
        return null;
    }

    /**
     * Melakukan proses logout dan kembali ke AuthFrame.
     */
    public void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Tutup MainAppFrame
            // Pastikan path ke AuthFrame benar
            new managementtrevel.AuthFrame().setVisible(true); // Kembali ke AuthFrame
            System.out.println("Logout berhasil. Kembali ke AuthFrame.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Untuk testing, Anda bisa buat dummy user jika diperlukan oleh konstruktor
            // UserModel dummyUser = new UserModel(1, "Test User", "test@example.com", "123456789", "Alamat Test");
            // Session.currentUser = dummyUser; // Set dummy user ke session untuk testing PanelUserProfil
            // Session.setLoggedIn(true);

            MainAppFrame mainAppFrame = new MainAppFrame(); // Atau new MainAppFrame(dummyUser);
            mainAppFrame.setVisible(true);
        });
    }
}
