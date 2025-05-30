package managementtrevel;

import Asset.AppTheme; 
import Asset.SidebarPanel;
import java.awt.*; // Untuk meneruskan data list
import java.util.List; // Untuk data default jika null
import javax.swing.*;
import managementtrevel.CustomTripBuilder.PanelAccommodationStep;
import managementtrevel.CustomTripBuilder.PanelActivityStep; 
import managementtrevel.CustomTripBuilder.PanelDateStep; // Pastikan AppTheme dapat diakses
import managementtrevel.CustomTripBuilder.PanelDestinationStep;
import managementtrevel.CustomTripBuilder.PanelFinalStep;
import managementtrevel.CustomTripBuilder.PanelTransportStep;
import managementtrevel.HomeUser.PanelBeranda;
import managementtrevel.HomeUser.PanelUserProfil;
import model.UserModel; 


public class MainAppFrame extends JFrame {

    private JPanel mainPanelContainer;
    private CardLayout cardLayout;
    private UserModel currentUser;
    private SidebarPanel sidebarPanel;

    // Konstanta untuk nama panel
    public static final String PANEL_BERANDA = "PanelBeranda";
    public static final String PANEL_USER_PROFILE = "PanelUserProfile";
    // Tambahkan konstanta untuk panel placeholder lainnya jika masih digunakan
    public static final String PANEL_DESTINASI_PLACEHOLDER = "PanelDestinasiPlaceholder";
    public static final String PANEL_PEMESANAN_PLACEHOLDER = "PanelPemesananPlaceholder";
    public static final String PANEL_PROFIL_UMUM_PLACEHOLDER = "PanelProfilUmumPlaceholder";


    // Konstanta untuk panel Custom Trip Builder
    public static final String PANEL_DESTINATION_STEP = "PanelDestinationStep";
    public static final String PANEL_DATE_STEP = "PanelDateStep";
    public static final String PANEL_TRANSPORT_STEP = "PanelTransportStep";
    public static final String PANEL_ACCOMMODATION_STEP = "PanelAccommodationStep";
    public static final String PANEL_ACTIVITY_STEP = "PanelActivityStep"; 
    public static final String PANEL_FINAL_STEP = "PanelFinalStep";


    public MainAppFrame(UserModel user) {
        this(); // Panggil konstruktor tanpa argumen untuk inisialisasi dasar
        this.currentUser = user;
        // Setelah komponen diinisialisasi, set data user ke PanelUserProfil jika ada
        Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
        if (userProfilCard instanceof PanelUserProfil) { 
            ((PanelUserProfil) userProfilCard).setProfileData(); 
        }
    }

    public MainAppFrame() {
        setTitle("Travel App"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 768); 
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        applyGlobalThemeSettings(); 

        // --- Inisialisasi Sidebar ---
        sidebarPanel = new SidebarPanel(this);

        // --- Inisialisasi CardLayout dan Panel Konten Utama ---
        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);
        mainPanelContainer.setBackground(AppTheme.PANEL_BACKGROUND); 

        // 1. Panel Beranda
        // Menggunakan konstruktor yang menerima MainAppFrame
        PanelBeranda panelBeranda = new PanelBeranda(this); 
        panelBeranda.setName(PANEL_BERANDA); 
        mainPanelContainer.add(panelBeranda, PANEL_BERANDA);

        // 2. Panel UserProfile (dari Sidebar)
        PanelUserProfil panelUserProfil = new PanelUserProfil(this);
        panelUserProfil.setName(PANEL_USER_PROFILE); 
        mainPanelContainer.add(panelUserProfil, PANEL_USER_PROFILE);
        
        // 3. Panel DestinationStep (Langkah pertama Custom Trip)
        PanelDestinationStep panelDestinationStep = new PanelDestinationStep(this);
        panelDestinationStep.setName(PANEL_DESTINATION_STEP); 
        mainPanelContainer.add(panelDestinationStep, PANEL_DESTINATION_STEP);

        // Panel-panel langkah berikutnya (Date, Transport, Acco, Activity, Final)
        // akan dibuat on-demand oleh metode showPanel() karena memerlukan data.

        // Placeholder panels (jika masih diperlukan untuk menu lain)
        JPanel panelDestinasiPlaceholder = createPlaceholderPanel("Halaman Destinasi (Placeholder)", AppTheme.PRIMARY_BLUE_LIGHT);
        panelDestinasiPlaceholder.setName(PANEL_DESTINASI_PLACEHOLDER); 
        mainPanelContainer.add(panelDestinasiPlaceholder, PANEL_DESTINASI_PLACEHOLDER);
        JPanel panelPemesananPlaceholder = createPlaceholderPanel("Halaman Pemesanan (Placeholder)", AppTheme.ACCENT_ORANGE);
        panelPemesananPlaceholder.setName(PANEL_PEMESANAN_PLACEHOLDER); 
        mainPanelContainer.add(panelPemesananPlaceholder, PANEL_PEMESANAN_PLACEHOLDER);


        // --- Tata Letak Utama Frame ---
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanelContainer, BorderLayout.CENTER);

        showPanel(PANEL_BERANDA); 
    }
    
    private void applyGlobalThemeSettings() {
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) { 
                    lookAndFeel = info.getClassName();
                    break;
                }
            }
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception ex) {
            System.err.println("Gagal menerapkan Look and Feel: " + ex.getMessage());
        }
    }


    private JPanel createPlaceholderPanel(String text, Color color) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(color);
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.FONT_TITLE_LARGE); 
        label.setForeground(AppTheme.TEXT_WHITE); 
        panel.add(label);
        return panel;
    }

    public void showPanel(String panelName) {
        if (panelName.equals(PANEL_USER_PROFILE)) {
            Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
            if (userProfilCard instanceof PanelUserProfil) {
                ((PanelUserProfil) userProfilCard).setProfileData(); 
            }
        }
        
        if (panelName.equals(PANEL_DESTINATION_STEP)) {
            Component existingPanel = getPanelByName(panelName);
            if(!(existingPanel instanceof PanelDestinationStep)){ 
                removePanelIfExists(panelName); 
                PanelDestinationStep panelDestination = new PanelDestinationStep(this);
                panelDestination.setName(panelName); 
                mainPanelContainer.add(panelDestination, panelName);
            }
        }

        cardLayout.show(mainPanelContainer, panelName);
        System.out.println("Menampilkan panel: " + panelName);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
           sidebarPanel.collapsePublic();
        }
    }

    // --- Overload showPanel untuk Custom Trip Builder Steps ---

    public void showPanel(String panelName, List<String> destinations) {
        if (panelName.equals(PANEL_DATE_STEP)) {
            removePanelIfExists(panelName); 
            PanelDateStep panel = new PanelDateStep(this, destinations);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan destinasi.");
        } else if (panelName.equals(PANEL_DESTINATION_STEP)) { 
             showPanel(panelName); 
        } else {
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate) {
        if (panelName.equals(PANEL_TRANSPORT_STEP)) {
            removePanelIfExists(panelName);
            PanelTransportStep panel = new PanelTransportStep(this, destinations, startDate, endDate);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan tanggal.");
        } else {
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, 
                          String transportMode, String transportDetails) {
        if (panelName.equals(PANEL_ACCOMMODATION_STEP)) {
            removePanelIfExists(panelName);
            PanelAccommodationStep panel = new PanelAccommodationStep(this, destinations, startDate, endDate, transportMode, transportDetails);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan transport.");
        } else {
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    
    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, 
                          String transportMode, String transportDetails, String accommodationName, 
                          String roomType, String accommodationNotes) {
        if (panelName.equals(PANEL_ACTIVITY_STEP)) {
            removePanelIfExists(panelName);
            PanelActivityStep panel = new PanelActivityStep(this, destinations, startDate, endDate, transportMode, transportDetails, accommodationName, roomType, accommodationNotes);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan akomodasi.");
        } else {
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
 
    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, 
                          String transportMode, String transportDetails, String accommodationName, 
                          String roomType, String accommodationNotes, List<String> activities) {
        if (panelName.equals(PANEL_FINAL_STEP)) {
            removePanelIfExists(panelName);
            PanelFinalStep panel = new PanelFinalStep(this, destinations, startDate, endDate, transportMode, transportDetails, accommodationName, roomType, accommodationNotes, activities);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan semua data.");
        } else {
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    
    private void removePanelIfExists(String panelName){
        Component[] components = mainPanelContainer.getComponents();
        for(Component comp : components){
            if(comp.getName() != null && comp.getName().equals(panelName)){
                mainPanelContainer.remove(comp);
                System.out.println("Menghapus instance lama dari panel: " + panelName);
                break; 
            }
        }
    }


    public Component getPanelByName(String panelName) {
        for (Component comp : mainPanelContainer.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(panelName)) {
                return comp;
            }
        }
        return null;
    }

    public void performLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin logout?", "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); 
            new managementtrevel.AuthFrame().setVisible(true); 
            System.out.println("Logout berhasil. Kembali ke AuthFrame.");
        }
    }
    
    public UserModel getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainAppFrame mainAppFrame = new MainAppFrame();
            mainAppFrame.setVisible(true);
        });
    }
}
