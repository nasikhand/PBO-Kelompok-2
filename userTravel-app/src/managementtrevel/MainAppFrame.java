package managementtrevel;

import Asset.AppTheme;
import Asset.SidebarPanel;
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
import managementtrevel.TripOrder.PanelOrderDetail; // Import PanelOrderDetail
import managementtrevel.TripOrder.PanelOrderHistory;
import managementtrevel.TripOrder.PanelUserOrder;
import model.PaketPerjalananModel;
import model.ReservasiModel; // Import ReservasiModel
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

        sidebarPanel = new SidebarPanel(this);

        cardLayout = new CardLayout();
        mainPanelContainer = new JPanel(cardLayout);
        mainPanelContainer.setBackground(AppTheme.PANEL_BACKGROUND); 

        PanelBeranda panelBeranda = new PanelBeranda(this); 
        panelBeranda.setName(PANEL_BERANDA); 
        mainPanelContainer.add(panelBeranda, PANEL_BERANDA);

        PanelUserProfil panelUserProfil = new PanelUserProfil(this);
        panelUserProfil.setName(PANEL_USER_PROFILE); 
        mainPanelContainer.add(panelUserProfil, PANEL_USER_PROFILE);
        
        PanelDestinationStep panelDestinationStep = new PanelDestinationStep(this);
        panelDestinationStep.setName(PANEL_DESTINATION_STEP); 
        mainPanelContainer.add(panelDestinationStep, PANEL_DESTINATION_STEP);
        
        // PanelOrderDetail panelOrderDetail = new PanelOrderDetail(this, null, null); // Hapus atau sesuaikan inisialisasi ini
        // panelDestinationStep.setName(PANEL_ORDER_DETAIL); 
        // mainPanelContainer.add(panelOrderDetail, PANEL_ORDER_DETAIL);

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

    public void showPanel(String panelName) {
        if (panelName.equals(PANEL_USER_PROFILE)) {
            Component userProfilCard = getPanelByName(PANEL_USER_PROFILE);
            if (userProfilCard instanceof PanelUserProfil) {
                ((PanelUserProfil) userProfilCard).setProfileData(); 
            }
        } else if (panelName.equals(PANEL_DESTINATION_STEP)) {
            removePanelIfExists(panelName); 
            PanelDestinationStep panelDestination = new PanelDestinationStep(this);
            panelDestination.setName(panelName); 
            mainPanelContainer.add(panelDestination, panelName);
            System.out.println("Membuat dan menambahkan panel: " + panelName);
        } else if (panelName.equals(PANEL_PESANAN_SAYA)) {
            removePanelIfExists(panelName);
            PanelUserOrder panel = new PanelUserOrder(this); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            System.out.println("Membuat dan menambahkan panel: " + panelName);
        } else if (panelName.equals(PANEL_RIWAYAT_PESANAN)) {
            removePanelIfExists(panelName);
            PanelOrderHistory panel = new PanelOrderHistory(this); 
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            System.out.println("Membuat dan menambahkan panel: " + panelName);
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
            System.out.println("Menampilkan panel: " + panelName + " untuk paket: " + (paket != null ? paket.getNamaPaket() : "null"));
        } else {
            showPanel(panelName); 
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }
    
    // Untuk PanelPayment, reservasiId sekarang bertipe int
    public void showPaymentPanel(int reservasiId, String namaKontak, String emailKontak, String teleponKontak, List<String> penumpangList) {
        removePanelIfExists(PANEL_PAYMENT);
        PanelPayment panel = new PanelPayment(this, reservasiId, namaKontak, emailKontak, teleponKontak, penumpangList);
        panel.setName(PANEL_PAYMENT);
        mainPanelContainer.add(panel, PANEL_PAYMENT);
        cardLayout.show(mainPanelContainer, PANEL_PAYMENT);
        System.out.println("Menampilkan panel: " + PANEL_PAYMENT + " untuk reservasi ID: " + reservasiId);
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) sidebarPanel.collapsePublic();
    }

    // OVERLOAD BARU UNTUK PanelOrderDetail
    public void showPanel(String panelName, ReservasiModel reservasi) { // <--- OVERLOAD BARU
        if (panelName.equals(PANEL_ORDER_DETAIL)) {
            removePanelIfExists(panelName); // Hapus instance lama
            PanelOrderDetail panel = new PanelOrderDetail(this, reservasi); // Buat instance baru dengan data reservasi
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
            System.out.println("Menampilkan panel: " + panelName + " untuk reservasi Kode: " + reservasi.getKodeReservasi());
        } else {
            System.err.println("Peringatan: showPanel(String, ReservasiModel) dipanggil untuk panel yang tidak sesuai: " + panelName);
            showPanel(panelName); // Kembali ke overload umum jika nama panel tidak cocok
        }
        if (sidebarPanel != null && sidebarPanel.isExpandedPublic()) {
            sidebarPanel.collapsePublic();
        }
    }


    public void showPanel(String panelName, List<String> destinations) {
        if (panelName.equals(PANEL_DATE_STEP)) {
            removePanelIfExists(panelName); 
            PanelDateStep panel = new PanelDateStep(this, destinations);
            panel.setName(panelName);
            mainPanelContainer.add(panel, panelName);
            cardLayout.show(mainPanelContainer, panelName);
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
