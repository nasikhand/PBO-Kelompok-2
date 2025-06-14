package managementtrevel; // Pastikan package ini benar

import Asset.AppTheme;
import Asset.SidebarPanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import managementtrevel.BookingScreen.PanelBookingScreen;
// Hapus import untuk panel-panel lama yang akan dihilangkan
// import managementtrevel.CustomTripBuilder.PanelAccommodationStep;
// import managementtrevel.CustomTripBuilder.PanelActivityStep;
// import managementtrevel.CustomTripBuilder.PanelDateStep;
import managementtrevel.CustomTripBuilder.PanelDestinationStep;
import managementtrevel.CustomTripBuilder.PanelFinalStep;
// import managementtrevel.CustomTripBuilder.PanelTransportStep;

// Import panel-panel baru (akan dibuat atau sudah ada)
import managementtrevel.CustomTripBuilder.PanelItineraryStep;   // NEW Step 2
import managementtrevel.CustomTripBuilder.PanelTransportCostStep; // NEW Step 3
import managementtrevel.CustomTripBuilder.PanelParticipantsStep; // NEW Step 4

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
import model.CustomTripDetailModel; // <--- PASTIKAN IMPORT INI ADA DAN TIDAK DIKOMENTARI


public class MainAppFrame extends JFrame {

    private JPanel mainPanelContainer;
    private CardLayout cardLayout;
    private UserModel currentUser;
    private SidebarPanel sidebarPanel;

    // Konstanta untuk nama panel - DIUPDATE UNTUK ALUR BARU
    public static final String PANEL_BERANDA = "PanelBeranda";
    public static final String PANEL_USER_PROFILE = "PanelUserProfile";
    
    // Custom Trip Builder - KONSTANTA BARU/DIUPDATE UNTUK 5 LANGKAH
    public static final String PANEL_DESTINATION_STEP = "PanelDestinationStep"; // Step 1
    public static final String PANEL_ITINERARY_STEP = "PanelItineraryStep";     // NEW Step 2
    public static final String PANEL_TRANSPORT_COST_STEP = "PanelTransportCostStep"; // NEW Step 3
    public static final String PANEL_PARTICIPANTS_STEP = "PanelParticipantsStep"; // NEW Step 4
    public static final String PANEL_FINAL_STEP = "PanelFinalStep";             // Step 5 (Final)

    // Hapus konstanta lama yang tidak digunakan lagi jika Anda sudah menghapus panelnya
    // public static final String PANEL_DATE_STEP = "PanelDateStep";
    // public static final String PANEL_TRANSPORT_STEP = "PanelTransportStep";
    // public static final String PANEL_ACCOMMODATION_STEP = "PanelAccommodationStep";
    // public static final String PANEL_ACTIVITY_STEP = "PanelActivityStep";

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

        sidebarPanel = new SidebarPanel(this);

        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);
        mainPanelContainer.setBackground(AppTheme.PANEL_BACKGROUND); 

        // Inisialisasi panel utama yang akan digunakan di CardLayout
        mainPanelContainer.add(new PanelBeranda(this), PANEL_BERANDA);
        mainPanelContainer.add(new PanelUserProfil(this), PANEL_USER_PROFILE);
        mainPanelContainer.add(new PanelOrderHistory(this), PANEL_RIWAYAT_PESANAN);
        mainPanelContainer.add(new PanelUserOrder(this), PANEL_PESANAN_SAYA);
        mainPanelContainer.add(new PanelDestinationStep(this), PANEL_DESTINATION_STEP);


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

    // --- Overload showPanel methods - DIUPDATE UNTUK ALUR BARU ---

    public void showPanel(String panelName) {
        if (panelName.equals(PANEL_USER_PROFILE)) {
            Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
            if (userProfilCard instanceof PanelUserProfil) {
                ((PanelUserProfil) userProfilCard).setProfileData(); 
            }
        } else if (panelName.equals(PANEL_PESANAN_SAYA)) {
            removePanelIfExists(panelName);
            PanelUserOrder panel = new PanelUserOrder(this); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
        } else if (panelName.equals(PANEL_RIWAYAT_PESANAN)) {
            removePanelIfExists(panelName);
            PanelOrderHistory panel = new PanelOrderHistory(this); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
        } else if (panelName.equals(PANEL_DESTINATION_STEP)) {
            removePanelIfExists(panelName);
            PanelDestinationStep panel = new PanelDestinationStep(this);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
        }
        
        cardLayout.show(mainPanelContainer, panelName);
        System.out.println("Menampilkan panel: " + panelName);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
           sidebarPanel.collapsePublic();
        }
    }

    public void showSearchResultPanel(String namaKotaAtauDestinasi, String tanggalKeberangkatan) {
        removePanelIfExists(PANEL_SEARCH_RESULT);
        PanelSearchResult searchResultPanel = new PanelSearchResult(this, namaKotaAtauDestinasi, tanggalKeberangkatan);
        searchResultPanel.setName(PANEL_SEARCH_RESULT);
        mainPanelContainer.add(searchResultPanel, PANEL_SEARCH_RESULT);
        cardLayout.show(mainPanelContainer, PANEL_SEARCH_RESULT);
        System.out.println("Menampilkan panel: " + PANEL_SEARCH_RESULT + " untuk " + namaKotaAtauDestinasi);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

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

    public void showPanel(String panelName, PaketPerjalananModel paket) {
        if (panelName.equals(PANEL_BOOKING_SCREEN)) {
            removePanelIfExists(panelName);
            PanelBookingScreen panel = new PanelBookingScreen(this, paket);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + PANEL_BOOKING_SCREEN + " untuk paket: " + (paket != null ? paket.getNamaPaket() : "null"));
        } else {
            System.err.println("Peringatan: showPanel(String, PaketPerjalananModel) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    
    public void showPaymentPanel(int reservasiId, String namaKontak, String emailKontak, String teleponKontak, List<String> penumpangList) {
        removePanelIfExists(PANEL_PAYMENT);

        // --- FIXED: Removed the extra 'teleponKontak' argument to match the new constructor ---
        PanelPayment panel = new PanelPayment(this, reservasiId, namaKontak, emailKontak, penumpangList);

        panel.setName(PANEL_PAYMENT);
        mainPanelContainer.add(panel, PANEL_PAYMENT);
        cardLayout.show(mainPanelContainer, PANEL_PAYMENT);
        System.out.println("Menampilkan panel: " + PANEL_PAYMENT + " untuk reservasi ID: " + reservasiId);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
            sidebarPanel.collapsePublic();
        }
    }

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

    // --- Custom Trip Builder Overloads (5 Langkah) ---

    // 1. PanelDestinationStep (Step 1) -> PanelItineraryStep (NEW Step 2)
    // Menerima destinasi yang dipilih dan estimasi biaya dari PanelDestinationStep
    public void showPanel(String panelName, List<String> destinations, double initialEstimatedCost) {
        if (panelName.equals(PANEL_ITINERARY_STEP)) { // NEW Step 2
            removePanelIfExists(panelName); 
            PanelItineraryStep panel = new PanelItineraryStep(this, destinations, initialEstimatedCost);
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

    // 2. PanelItineraryStep (NEW Step 2) -> PanelTransportCostStep (NEW Step 3)
    // Menerima destinasi yang dipilih, detail itinerary (termasuk tanggal kunjungan per destinasi), dan total biaya kumulatif
    public void showPanel(String panelName, List<String> selectedDestinationsFromItinerary, List<CustomTripDetailModel> itineraryDetails, double totalEstimatedCost) {
        if (panelName.equals(PANEL_TRANSPORT_COST_STEP)) { // NEW Step 3
            removePanelIfExists(panelName);
            PanelTransportCostStep panel = new PanelTransportCostStep(this, selectedDestinationsFromItinerary, itineraryDetails, totalEstimatedCost);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan detail itinerari dan estimasi biaya total: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, List<CustomTripDetailModel>, double) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }


    // 3. PanelTransportCostStep (NEW Step 3) -> PanelParticipantsStep (NEW Step 4)
    // Menerima data transport yang difinalisasi (string label), dan total biaya kumulatif
    public void showPanel(String panelName, List<String> selectedDestinationsFromItinerary, List<CustomTripDetailModel> itineraryDetails, double totalEstimatedCost, String fixedTransportCostLabel, String transportMode, String transportDetails) {
        if (panelName.equals(PANEL_PARTICIPANTS_STEP)) { // NEW Step 4
            removePanelIfExists(panelName);
            PanelParticipantsStep panel = new PanelParticipantsStep(this, selectedDestinationsFromItinerary, itineraryDetails, totalEstimatedCost, fixedTransportCostLabel, transportMode, transportDetails);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan biaya transport dan estimasi biaya total: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, List<CustomTripDetailModel>, double, String, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }


    // 4. PanelParticipantsStep (NEW Step 4) -> PanelFinalStep (Step 5)
    // Menerima semua data yang dikumpulkan sebelumnya, ditambah jumlah peserta
    public void showPanel(String panelName, List<String> destinations, List<CustomTripDetailModel> itineraryDetails,
                      String transportMode, String transportDetails, String accommodationName, 
                      String roomType, String accommodationNotes, List<String> participantNames, // <-- RENAMED
                      double totalEstimatedCost, int numberOfParticipants) { 
    if (panelName.equals(PANEL_FINAL_STEP)) {
        removePanelIfExists(panelName);
        // Make sure PanelFinalStep's constructor also accepts this new list
        PanelFinalStep panel = new PanelFinalStep(this, destinations, itineraryDetails, transportMode, transportDetails, 
                                                  accommodationName, roomType, accommodationNotes, 
                                                  participantNames, // <-- Pass it here
                                                  totalEstimatedCost, numberOfParticipants);
        panel.setName(panelName);
        mainPanelContainer.add(panel, panelName);
        cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " dengan kegiatan dan estimasi biaya total: " + totalEstimatedCost + ", Peserta: " + numberOfParticipants);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, List<CustomTripDetailModel>, String, String, String, String, String, List<String>, double, int) dipanggil untuk panel yang tidak sesuai: " + panelName);
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

    public void showPanel(String panelName, List<String> selectedDestinationsFromItinerary, List<CustomTripDetailModel> itineraryDetails, double totalEstimatedCost, String transportMode, String transportDetails) {
        if (panelName.equals(PANEL_PARTICIPANTS_STEP)) { 
            removePanelIfExists(panelName);
            // Panggil konstruktor PanelParticipantsStep yang sesuai (tanpa akomodasi)
            // Kita akan meneruskan string kosong untuk akomodasi jika tidak ada di langkah ini.
            PanelParticipantsStep panel = new PanelParticipantsStep(this, selectedDestinationsFromItinerary, itineraryDetails, totalEstimatedCost, transportMode, transportDetails, "", "", ""); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " (dari TransportCostStep) dengan estimasi biaya: " + totalEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, List<CustomTripDetailModel>, double, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    // --- IMPLEMENTASI OVERLOAD 2 (dari PanelAccommodationStep atau ActivityStep untuk kembali) ---
    // Ini menerima semua detail transport dan akomodasi.
    public void showPanel(String panelName, List<String> selectedDestinationsFromItinerary, List<CustomTripDetailModel> itineraryDetails, double initialEstimatedCost, String currentTransportMode, String currentTransportDetails, String currentAccommodationName, String currentRoomType, String currentAccommodationNotes) {
        if (panelName.equals(PANEL_PARTICIPANTS_STEP)) { 
            removePanelIfExists(panelName);
            // Panggil konstruktor PanelParticipantsStep yang paling komprehensif
            PanelParticipantsStep panel = new PanelParticipantsStep(this, selectedDestinationsFromItinerary, itineraryDetails, initialEstimatedCost, currentTransportMode, currentTransportDetails, currentAccommodationName, currentRoomType, currentAccommodationNotes);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " (dengan Akmodasi/Activities) dengan estimasi biaya: " + initialEstimatedCost);
        } else {
            System.err.println("Peringatan: showPanel(String, List<String>, List<CustomTripDetailModel>, double, String, String, String, String, String) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName);
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
}
