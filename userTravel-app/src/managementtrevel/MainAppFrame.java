package managementtrevel; // Pastikan package ini benar

import Asset.AppTheme;
import Asset.SidebarPanel; // Pastikan package Asset sudah benar
import java.awt.*;
import java.util.List;
import javax.swing.*;
import managementtrevel.BookingScreen.PanelBookingScreen;
import managementtrevel.CustomTripBuilder.PanelAccommodationStep;
import managementtrevel.CustomTripBuilder.PanelActivityStep; 
import managementtrevel.CustomTripBuilder.PanelDateStep;
import managementtrevel.CustomTripBuilder.PanelDestinationStep;
import managementtrevel.CustomTripBuilder.PanelFinalStep;
import managementtrevel.CustomTripBuilder.PanelTransportStep;
import managementtrevel.HomeUser.PanelBeranda;
import managementtrevel.HomeUser.PanelUserProfil;
import managementtrevel.Payment.PanelPayment;
import managementtrevel.SearchResultScreen.PanelSearchResult;
import managementtrevel.TripDetailScreen.PanelTripDetail;
import managementtrevel.TripOrder.PanelOrderDetail;
import managementtrevel.TripOrder.PanelOrderHistory;
import managementtrevel.TripOrder.PanelUserOrder;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.UserModel; 


public class MainAppFrame extends JFrame {

    private JPanel mainPanelContainer;
    private CardLayout cardLayout;
    private UserModel currentUser;
    private SidebarPanel sidebarPanel;

    // Konstanta untuk nama panel
    public static final String PANEL_BERANDA = "PanelBeranda";
    public static final String PANEL_USER_PROFILE = "PanelUserProfile";
    
    public static final String PANEL_DESTINATION_STEP = "PanelDestinationStep";
    public static final String PANEL_DATE_STEP = "PanelDateStep";
    public static final String PANEL_TRANSPORT_STEP = "PanelTransportStep";
    public static final String PANEL_ACCOMMODATION_STEP = "PanelAccommodationStep";
    public static final String PANEL_ACTIVITY_STEP = "PanelActivityStep"; 
    public static final String PANEL_FINAL_STEP = "PanelFinalStep";

    public static final String PANEL_PESANAN_SAYA = "PanelUserOrder"; 
    public static final String PANEL_ORDER_DETAIL = "PanelOrderDetail";
    public static final String PANEL_RIWAYAT_PESANAN = "PanelOrderHistory"; 
    public static final String PANEL_SEARCH_RESULT = "PanelSearchResult"; 
    public static final String PANEL_TRIP_DETAIL = "PanelTripDetail";     
    public static final String PANEL_BOOKING_SCREEN = "PanelBookingScreen"; 
    public static final String PANEL_PAYMENT = "PanelPayment"; 


    public MainAppFrame(UserModel user) {
        this(); 
        this.currentUser = user;
        SwingUtilities.invokeLater(() -> { 
            Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
            if (userProfilCard instanceof PanelUserProfil) { 
                ((PanelUserProfil) userProfilCard).setProfileData(); 
            }
        });
    }

    public MainAppFrame() {
        setTitle("Travel App"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 768); 
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        applyGlobalThemeSettings(); 

        // Penting: Pastikan SidebarPanel diinisialisasi setelah MainAppFrame
        sidebarPanel = new SidebarPanel(this);

        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);
        mainPanelContainer.setBackground(AppTheme.PANEL_BACKGROUND); 

        // Tambahkan semua panel utama yang akan digunakan di CardLayout
        mainPanelContainer.add(new PanelBeranda(this), PANEL_BERANDA);
        mainPanelContainer.add(new PanelUserProfil(this), PANEL_USER_PROFILE);
        mainPanelContainer.add(new PanelOrderHistory(this), PANEL_RIWAYAT_PESANAN);
        mainPanelContainer.add(new PanelUserOrder(this), PANEL_PESANAN_SAYA);

        // Panel Custom Trip Builder (hanya step pertama yang diinisialisasi di sini)
        mainPanelContainer.add(new PanelDestinationStep(this), PANEL_DESTINATION_STEP);

        // Panel-panel lain akan ditambahkan dan dihapus secara dinamis oleh showPanel
        
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

    // --- Overload showPanel methods ---

    public void showPanel(String panelName) {
        // Logika spesifik untuk beberapa panel yang mungkin perlu refresh data
        if (panelName.equals(PANEL_USER_PROFILE)) {
            Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
            if (userProfilCard instanceof PanelUserProfil) {
                ((PanelUserProfil) userProfilCard).setProfileData(); 
            }
        } else if (panelName.equals(PANEL_PESANAN_SAYA)) {
            // Recreate PanelUserOrder to ensure latest data is loaded
            removePanelIfExists(panelName);
            PanelUserOrder panel = new PanelUserOrder(this); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
        } else if (panelName.equals(PANEL_RIWAYAT_PESANAN)) {
            // Recreate PanelOrderHistory to ensure latest data is loaded
            removePanelIfExists(panelName);
            PanelOrderHistory panel = new PanelOrderHistory(this); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
        } else if (panelName.equals(PANEL_DESTINATION_STEP)) {
            // Recreate PanelDestinationStep for fresh state if navigating back to it
            removePanelIfExists(panelName);
            PanelDestinationStep panel = new PanelDestinationStep(this);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
        }
        
        // Show the panel
        cardLayout.show(mainPanelContainer, panelName);
        System.out.println("Menampilkan panel: " + panelName);
        // Collapse sidebar if expanded
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
           sidebarPanel.collapsePublic();
        }
    }

    // Overload for search results
    public void showSearchResultPanel(String namaKotaAtauDestinasi, String tanggalKeberangkatan) {
        removePanelIfExists(PANEL_SEARCH_RESULT);
        PanelSearchResult searchResultPanel = new PanelSearchResult(this, namaKotaAtauDestinasi, tanggalKeberangkatan);
        searchResultPanel.setName(PANEL_SEARCH_RESULT);
        mainPanelContainer.add(searchResultPanel, PANEL_SEARCH_RESULT);
        cardLayout.show(mainPanelContainer, PANEL_SEARCH_RESULT);
        System.out.println("Menampilkan panel: " + PANEL_SEARCH_RESULT + " untuk " + namaKotaAtauDestinasi);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    // Overload for trip detail (PaketPerjalananModel)
    public void showPanel(String panelName, PaketPerjalananModel paket, String originalSearchNamaKota, String originalSearchTanggal) {
        if (panelName.equals(PANEL_TRIP_DETAIL)) {
            removePanelIfExists(panelName);
            PanelTripDetail panel = new PanelTripDetail(this, paket, originalSearchNamaKota, originalSearchTanggal); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " untuk paket: " + (paket != null ? paket.getNamaPaket() : "null"));
        } else {
            System.err.println("Peringatan: showPanel(String, PaketPerjalananModel, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
            sidebarPanel.collapsePublic();
        }
    }

    // Overload for booking screen (PaketPerjalananModel)
    public void showPanel(String panelName, PaketPerjalananModel paket) {
        if (panelName.equals(PANEL_BOOKING_SCREEN)) {
            removePanelIfExists(panelName);
            PanelBookingScreen panel = new PanelBookingScreen(this, paket);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " untuk paket: " + (paket != null ? paket.getNamaPaket() : "null"));
        } else {
            System.err.println("Peringatan: showPanel(String, PaketPerjalananModel) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    
    // Overload for payment screen
    public void showPaymentPanel(int reservasiId, String namaKontak, String emailKontak, String teleponKontak, List<String> penumpangList) {
        removePanelIfExists(PANEL_PAYMENT);
        PanelPayment panel = new PanelPayment(this, reservasiId, namaKontak, emailKontak, teleponKontak, penumpangList);
        panel.setName(PANEL_PAYMENT);
        mainPanelContainer.add(panel, PANEL_PAYMENT);
        cardLayout.show(mainPanelContainer, PANEL_PAYMENT);
        System.out.println("Menampilkan panel: " + PANEL_PAYMENT + " untuk reservasi ID: " + reservasiId);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    // Overload for order detail (ReservasiModel)
    public void showPanel(String panelName, ReservasiModel reservasi) {
        if (panelName.equals(PANEL_ORDER_DETAIL)) {
            removePanelIfExists(panelName);
            PanelOrderDetail panel = new PanelOrderDetail(this, reservasi);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " untuk reservasi Kode: " + reservasi.getKodeReservasi());
        } else {
            System.err.println("Peringatan: showPanel(String, ReservasiModel) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
            sidebarPanel.collapsePublic();
        }
    }

    // Custom Trip Builder Overloads (passing data between steps)

    // Overload for PanelDateStep (receiving destinations and initial cost from DestinationStep)
    public void showPanel(String panelName, List<String> destinations, double initialEstimatedCost) {
        if (panelName.equals(PANEL_DATE_STEP)) {
            removePanelIfExists(panelName); 
            PanelDateStep panel = new PanelDateStep(this, destinations, initialEstimatedCost);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan destinasi dan estimasi biaya awal: " + initialEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, double) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    // Overload for PanelTransportStep (receiving data from PanelDateStep, including cumulative cost)
    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, double totalEstimatedCost) {
        if (panelName.equals(PANEL_TRANSPORT_STEP)) {
            removePanelIfExists(panelName);
            PanelTransportStep panel = new PanelTransportStep(this, destinations, startDate, endDate, totalEstimatedCost);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan tanggal dan estimasi biaya total: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, double) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); // Fallback to simpler overload if needed
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    // Overload for PanelAccommodationStep (receiving data from PanelTransportStep, including cumulative cost)
    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, String transportMode, String transportDetails, double totalEstimatedCost) {
        if (panelName.equals(PANEL_ACCOMMODATION_STEP)) {
            removePanelIfExists(panelName);
            PanelAccommodationStep panel = new PanelAccommodationStep(this, destinations, startDate, endDate, transportMode, transportDetails, totalEstimatedCost);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan transportasi dan estimasi biaya total: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, String, String, double) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    
    // Overload for PanelActivityStep (receiving data from PanelAccommodationStep, including cumulative cost)
    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, String transportMode, String transportDetails, String accommodationName, String roomType, String accommodationNotes, double totalEstimatedCost) {
        if (panelName.equals(PANEL_ACTIVITY_STEP)) {
            removePanelIfExists(panelName);
            PanelActivityStep panel = new PanelActivityStep(this, destinations, startDate, endDate, transportMode, transportDetails, accommodationName, roomType, accommodationNotes, totalEstimatedCost);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan akomodasi dan estimasi biaya total: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, String, String, String, String, String, double) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
 
    // Overload for PanelFinalStep (receiving data from PanelActivityStep, including cumulative cost)
    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate, String transportMode, String transportDetails, String accommodationName, String roomType, String accommodationNotes, List<String> activities, double totalEstimatedCost) {
        if (panelName.equals(PANEL_FINAL_STEP)) {
            removePanelIfExists(panelName);
            PanelFinalStep panel = new PanelFinalStep(this, destinations, startDate, endDate, transportMode, transportDetails, accommodationName, roomType, accommodationNotes, activities, totalEstimatedCost);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan kegiatan dan estimasi biaya total: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, String, String, String, String, String, List<String>, double) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }


    // --- Generic showPanel overloads for older paths (can be removed if no longer used) ---
    // These might be causing ambiguities if not commented out when more specific overloads exist.
    // Consider replacing calls to these with the more specific ones or ensuring defaults are passed.
    /*
    public void showPanel(String panelName, List<String> destinations) {
        if (panelName.equals(PANEL_DATE_STEP)) {
            removePanelIfExists(panelName);
            PanelDateStep panel = new PanelDateStep(this, destinations, 0.0); // Default cost
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan destinasi (default cost).");
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate) {
        if (panelName.equals(PANEL_TRANSPORT_STEP)) {
            removePanelIfExists(panelName);
            PanelTransportStep panel = new PanelTransportStep(this, destinations, startDate, endDate, 0.0); // Default cost
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan tanggal (default cost).");
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate,
                            String transportMode, String transportDetails) {
        if (panelName.equals(PANEL_ACCOMMODATION_STEP)) {
            removePanelIfExists(panelName);
            PanelAccommodationStep panel = new PanelAccommodationStep(this, destinations, startDate, endDate, transportMode, transportDetails, 0.0); // Default cost
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan transportasi (default cost).");
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate,
                            String transportMode, String transportDetails, String accommodationName,
                            String roomType, String accommodationNotes) {
        if (panelName.equals(PANEL_ACTIVITY_STEP)) {
            removePanelIfExists(panelName);
            PanelActivityStep panel = new PanelActivityStep(this, destinations, startDate, endDate, transportMode, transportDetails, accommodationName, roomType, accommodationNotes, 0.0); // Default cost
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan akomodasi (default cost).");
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, String, String, String, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    public void showPanel(String panelName, List<String> destinations, String startDate, String endDate,
                            String transportMode, String transportDetails, String accommodationName,
                            String roomType, String accommodationNotes, List<String> activities) {
        if (panelName.equals(PANEL_FINAL_STEP)) {
            removePanelIfExists(panelName);
            PanelFinalStep panel = new PanelFinalStep(this, destinations, startDate, endDate, transportMode, transportDetails, accommodationName, roomType, accommodationNotes, activities, 0.0); // Default cost
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan kegiatan (default cost).");
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, String, String, String, String, String, String, String, List<String>) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    */
    
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
