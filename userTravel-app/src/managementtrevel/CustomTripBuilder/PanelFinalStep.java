package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.ReservasiController;
import controller.CustomTripController;
import model.ReservasiModel;
import model.CustomTripModel;
import model.Session;
import model.CustomTripDetailModel;
import model.PenumpangModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class PanelFinalStep extends JPanel {

    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private final MainAppFrame mainAppFrame;
    private final List<String> currentDestinations;
    private final List<CustomTripDetailModel> itineraryDetails;

    private final double currentTotalCost;
    private final int numberOfParticipants;
    private final List<PenumpangModel> participantDetails;

    // --- All UI components are declared as member variables ---
    private JPanel panelBuildSteps, panelCustomTripMain, panelMainHeader, panelLeftContent, panelRightContent, panelPassengerInput, panelTripSummaryFull, panelEstimatedCost, panelMainFooter;
    private JSplitPane splitPaneContent;
    private JLabel lblBuildStepsTitle, lblStep1Destinasi, lblStep2Itinerary, lblStep3TransportCost, lblStep4Participants, lblStep5Final, lblCustomTripBuilderTitle, lblFinalReviewMessage, lblDestinationsSummaryDisplay, lblDatesSummaryDisplay, lblTransportSummaryDisplay, lblAccommodationSummaryDisplay, lblParticipantsSummaryDisplay, lblTripSummaryTitleInfo, lblEstimasiHargaValue;
    private JButton btnSaveTrip, btnPrevStep, btnFinishTrip;
    private JTextArea txtAreaFinalNotes, passengerDetailsArea;
    private JScrollPane scrollPaneFinalNotes, passengerDetailsScrollPane, scrollPaneDestinationsDisplay, scrollPaneTripSummaryFull;
    private JList<String> listDestinationsDisplay;
    private DefaultListModel<String> listModelDestinasiDisplay;
    //</editor-fold>

    public PanelFinalStep(MainAppFrame mainAppFrame, List<String> destinations, List<CustomTripDetailModel> itineraryDetails,
                      List<PenumpangModel> participantDetails,
                      double totalEstimatedCost, int numberOfParticipants) {
    
    this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
    this.itineraryDetails = itineraryDetails != null ? new ArrayList<>(itineraryDetails) : new ArrayList<>();
    this.participantDetails = participantDetails != null ? new ArrayList<>(participantDetails) : new ArrayList<>();
    this.mainAppFrame = mainAppFrame;
    this.currentTotalCost = totalEstimatedCost;
    this.numberOfParticipants = numberOfParticipants;

        this.listModelDestinasiDisplay = new DefaultListModel<>();

        initializeUI();
        applyAppTheme();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(5, 5)); 
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- FIXED: Initialize all main panels correctly ---
        panelBuildSteps = createSidebarPanel();
        add(panelBuildSteps, BorderLayout.WEST);

        panelCustomTripMain = new JPanel(new BorderLayout(10, 10));
        add(panelCustomTripMain, BorderLayout.CENTER);

        //<editor-fold defaultstate="collapsed" desc="Header">
        panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Ringkasan Akhir");
        btnSaveTrip = new JButton("Simpan Draf Trip");
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Left Panel">
        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        
        lblFinalReviewMessage = new JLabel("<html>Silakan tinjau ringkasan lengkap perjalanan Anda. <br>Anda dapat menambahkan catatan akhir di bawah ini.</html>");
        panelLeftContent.add(lblFinalReviewMessage);

        txtAreaFinalNotes = new JTextArea(5, 20);
        scrollPaneFinalNotes = new JScrollPane(txtAreaFinalNotes);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));
        panelLeftContent.add(scrollPaneFinalNotes);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelPassengerInput = createInputSectionPanel("Detail Peserta");
        passengerDetailsArea = new JTextArea();
        passengerDetailsScrollPane = new JScrollPane(passengerDetailsArea);
        panelPassengerInput.add(passengerDetailsScrollPane, BorderLayout.CENTER);
        
        panelLeftContent.add(panelPassengerInput);
        panelLeftContent.add(Box.createVerticalGlue());
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Right Panel (Summary)">
        panelRightContent = new JPanel(new BorderLayout());
        
        panelTripSummaryFull = new JPanel();
        panelTripSummaryFull.setLayout(new BoxLayout(panelTripSummaryFull, BoxLayout.Y_AXIS));
        
        lblDestinationsSummaryDisplay = new JLabel("Destinasi:");
        listDestinationsDisplay = new JList<>(listModelDestinasiDisplay);
        scrollPaneDestinationsDisplay = new JScrollPane(listDestinationsDisplay);
        panelTripSummaryFull.add(lblDestinationsSummaryDisplay);
        panelTripSummaryFull.add(scrollPaneDestinationsDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,15)));

        lblDatesSummaryDisplay = new JLabel("Tanggal:");
        panelTripSummaryFull.add(lblDatesSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblTransportSummaryDisplay = new JLabel("Transportasi:");
        panelTripSummaryFull.add(lblTransportSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblAccommodationSummaryDisplay = new JLabel("Akomodasi:");
        panelTripSummaryFull.add(lblAccommodationSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));
        
        lblParticipantsSummaryDisplay = new JLabel("Jumlah Peserta:");
        panelTripSummaryFull.add(lblParticipantsSummaryDisplay);

        scrollPaneTripSummaryFull = new JScrollPane(panelTripSummaryFull);
        panelRightContent.add(scrollPaneTripSummaryFull, BorderLayout.CENTER);

        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        lblTripSummaryTitleInfo = new JLabel("Total Estimasi Biaya:"); 
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        panelEstimatedCost.add(lblTripSummaryTitleInfo, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelRightContent.add(panelEstimatedCost, BorderLayout.SOUTH);
        //</editor-fold>
        
        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(450);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        //<editor-fold defaultstate="collapsed" desc="Footer">
        panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Peserta");
        btnFinishTrip = new JButton("Selesaikan & Pesan Trip");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnFinishTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);
        //</editor-fold>
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        panelBuildSteps.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        panelBuildSteps.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER_COLOR), new EmptyBorder(10, 0, 10, 0)));
        
        // --- FIXED: All components are now guaranteed to be non-null ---
        lblBuildStepsTitle.setFont(AppTheme.FONT_SUBTITLE);
        lblBuildStepsTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        lblBuildStepsTitle.setBorder(new EmptyBorder(10, 10, 15, 10));
        
        panelCustomTripMain.setOpaque(false);
        panelCustomTripMain.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        panelMainHeader.setOpaque(false);
        lblCustomTripBuilderTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblCustomTripBuilderTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        AppTheme.styleSecondaryButton(btnSaveTrip, "Simpan Draf Trip");
        
        panelLeftContent.setOpaque(false);
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);

        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;
        
        panelPassengerInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Detail Peserta", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titledBorderFont, titledBorderColor));
        passengerDetailsArea.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        passengerDetailsArea.setEditable(false);
        passengerDetailsArea.setOpaque(false);
        passengerDetailsArea.setForeground(AppTheme.TEXT_DARK);
        passengerDetailsScrollPane.setBorder(null);
        passengerDetailsScrollPane.getViewport().setOpaque(false);

        scrollPaneTripSummaryFull.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Lengkap Perjalanan Anda", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya Total", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titledBorderFont, titledBorderColor));
        panelTripSummaryFull.setOpaque(false);
        panelTripSummaryFull.setBorder(new EmptyBorder(10,10,10,10));
        panelEstimatedCost.setOpaque(false);
        scrollPaneTripSummaryFull.getViewport().setOpaque(false);
        
        lblFinalReviewMessage.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblFinalReviewMessage.setForeground(AppTheme.TEXT_DARK);
        
        AppTheme.styleInputField(txtAreaFinalNotes, "Tambahkan catatan atau permintaan khusus di sini...");
        scrollPaneFinalNotes.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        
        Font summaryHeaderFont = AppTheme.FONT_SUBTITLE;
        Font summaryDetailFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color summaryHeaderColor = AppTheme.PRIMARY_BLUE_DARK;
        Color summaryDetailColor = AppTheme.TEXT_DARK;
        lblDestinationsSummaryDisplay.setFont(summaryHeaderFont);
        lblDestinationsSummaryDisplay.setForeground(summaryHeaderColor);
        listDestinationsDisplay.setFont(summaryDetailFont);
        listDestinationsDisplay.setOpaque(false);
        scrollPaneDestinationsDisplay.setOpaque(false);
        scrollPaneDestinationsDisplay.getViewport().setOpaque(false);
        scrollPaneDestinationsDisplay.setBorder(BorderFactory.createEmptyBorder());

        lblDatesSummaryDisplay.setFont(summaryHeaderFont);
        lblDatesSummaryDisplay.setForeground(summaryHeaderColor);
        lblTransportSummaryDisplay.setFont(summaryHeaderFont);
        lblTransportSummaryDisplay.setForeground(summaryHeaderColor);
        lblAccommodationSummaryDisplay.setFont(summaryHeaderFont);
        lblAccommodationSummaryDisplay.setForeground(summaryHeaderColor);
        lblParticipantsSummaryDisplay.setFont(summaryHeaderFont);
        lblParticipantsSummaryDisplay.setForeground(summaryHeaderColor);
        
        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_LARGE);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);
        
        panelMainFooter.setOpaque(false);
        AppTheme.styleSecondaryButton(btnPrevStep, "< Kembali ke Peserta");
        AppTheme.stylePrimaryButton(btnFinishTrip, "Selesaikan & Pesan Trip");
    }
    
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(5);
        
        StringBuilder passengerText = new StringBuilder();
        passengerText.append("Jumlah Peserta: ").append(numberOfParticipants).append(" Orang\n\n");
        for (int i = 0; i < participantDetails.size(); i++) {
            PenumpangModel p = participantDetails.get(i);
            passengerText.append("Peserta ").append(i + 1).append(":\n");
            passengerText.append("  Nama: ").append(p.getNamaPenumpang() != null ? p.getNamaPenumpang() : "-").append("\n");
            passengerText.append("  Jenis Kelamin: ").append(p.getJenisKelamin() != null ? p.getJenisKelamin() : "-").append("\n");
            passengerText.append("  Email: ").append(p.getEmail() != null ? p.getEmail() : "-").append("\n");
            passengerText.append("  No. Telepon: ").append(p.getNomorTelepon() != null ? p.getNomorTelepon() : "-").append("\n\n");
        }
        passengerDetailsArea.setText(passengerText.toString());
        passengerDetailsArea.setCaretPosition(0);

        populateSummaryDisplay();
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnFinishTrip.addActionListener(this::btnFinishTripActionPerformed);
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
    }
    
    private void populateSummaryDisplay() {
        listModelDestinasiDisplay.clear();
        for (String destName : currentDestinations) {
            listModelDestinasiDisplay.addElement("• " + destName);
        }

        LocalDate overallStartDate = itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).min(LocalDate::compareTo).orElse(null);
        LocalDate overallEndDate = itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).max(LocalDate::compareTo).orElse(null);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM uuuu");
        String startDateFormatted = (overallStartDate != null) ? overallStartDate.format(dtf) : "-";
        String endDateFormatted = (overallEndDate != null) ? overallEndDate.format(dtf) : "-";
        lblDatesSummaryDisplay.setText("<html><b>Tanggal:</b> " + startDateFormatted + " s/d " + endDateFormatted + "</html>");
        
        lblParticipantsSummaryDisplay.setText("<html><b>Jumlah Peserta:</b> " + numberOfParticipants + " Orang</html>");
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentTotalCost));
    }
    
     private void btnFinishTripActionPerformed(ActionEvent evt) {
        if (Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk memesan trip.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CustomTripController tripController = new CustomTripController();
            ReservasiController reservasiController = new ReservasiController();

            // 1. Buat dan simpan CustomTripModel
            CustomTripModel customTrip = new CustomTripModel();
            customTrip.setUserId(Session.currentUser.getId());
            String namaTrip = currentDestinations.isEmpty() ? "Custom Trip" : String.join(", ", currentDestinations);
            if (namaTrip.length() > 100) namaTrip = namaTrip.substring(0, 97) + "...";
            customTrip.setNamaTrip(namaTrip);
            customTrip.setTanggalMulai(itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).min(LocalDate::compareTo).orElse(null));
            customTrip.setTanggalAkhir(itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).max(LocalDate::compareTo).orElse(null));
            customTrip.setJumlahPeserta(numberOfParticipants);
            customTrip.setStatus("dipesan");
            customTrip.setTotalHarga(currentTotalCost);
            customTrip.setCatatanUser(txtAreaFinalNotes.getText().trim());
            customTrip.setDetailList(itineraryDetails);

            int customTripId = tripController.saveCustomTrip(customTrip);

            if (customTripId != -1) {
                // 2. Buat dan simpan ReservasiModel
                ReservasiModel reservasi = new ReservasiModel();
                reservasi.setUserId(Session.currentUser.getId());
                reservasi.setTripType("custom_trip");
                reservasi.setTripId(customTripId);
                reservasi.setKodeReservasi("CTRIP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                reservasi.setTanggalReservasi(LocalDate.now());
                reservasi.setStatus("dipesan");

                int reservasiId = reservasiController.buatReservasi(reservasi);

                if (reservasiId != -1) {
                    // 3. Simpan semua detail penumpang
                    boolean allPassengersSaved = true;
                    for (PenumpangModel penumpang : this.participantDetails) {
                        if (!reservasiController.tambahPenumpangLengkap(reservasiId, penumpang)) {
                            allPassengersSaved = false;
                        }
                    }
                    if (!allPassengersSaved) {
                        JOptionPane.showMessageDialog(this, "Peringatan: Beberapa data penumpang gagal disimpan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                    
                    JOptionPane.showMessageDialog(this, "Custom Trip Anda berhasil dipesan! Lanjutkan ke pembayaran.", "Pemesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    
                    List<String> namaPenumpangSaja = new ArrayList<>();
                    for(PenumpangModel p : this.participantDetails) {
                        namaPenumpangSaja.add(p.getNamaPenumpang());
                    }
                    mainAppFrame.showPaymentPanel(reservasiId, Session.currentUser.getNamaLengkap(), Session.currentUser.getEmail(), Session.currentUser.getNomorTelepon(), namaPenumpangSaja);

                } else {
                    tripController.deleteCustomTrip(customTripId, reservasiId); // Rollback
                    JOptionPane.showMessageDialog(this, "Gagal membuat reservasi untuk trip ini.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan detail trip utama.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi error yang tidak terduga: " + e.getMessage(), "Error Kritis", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Panggil showPanel untuk kembali ke PanelParticipantsStep
            // Pastikan MainAppFrame memiliki metode showPanel yang cocok dengan parameter ini
            mainAppFrame.showPanel(
                MainAppFrame.PANEL_PARTICIPANTS_STEP,
                this.currentDestinations,
                this.itineraryDetails,
                this.currentTotalCost
            );
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Helper Methods">
    private JPanel createInputSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(5, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            null, title, TitledBorder.LEADING, TitledBorder.TOP,
            AppTheme.FONT_SUBTITLE, AppTheme.PRIMARY_BLUE_DARK
        ));
        return panel;
    }
    
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(230, 0)); 
        
        lblBuildStepsTitle = new JLabel(" Langkah Pembangunan");
        panel.add(lblBuildStepsTitle);

        EmptyBorder stepLabelBorder = new EmptyBorder(8, 15, 8, 10);
        lblStep1Destinasi = new JLabel(); lblStep1Destinasi.setBorder(stepLabelBorder); lblStep1Destinasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep2Itinerary = new JLabel(); lblStep2Itinerary.setBorder(stepLabelBorder); lblStep2Itinerary.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep3TransportCost = new JLabel(); lblStep3TransportCost.setBorder(stepLabelBorder); lblStep3TransportCost.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep4Participants = new JLabel(); lblStep4Participants.setBorder(stepLabelBorder); lblStep4Participants.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep5Final = new JLabel(); lblStep5Final.setBorder(stepLabelBorder); lblStep5Final.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lblStep1Destinasi);
        panel.add(lblStep2Itinerary);
        panel.add(lblStep3TransportCost);
        panel.add(lblStep4Participants);
        panel.add(lblStep5Final);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {lblStep1Destinasi, lblStep2Itinerary, lblStep3TransportCost, lblStep4Participants, lblStep5Final};
        String[] stepTexts = {"1. Destinasi", "2. Itinerary", "3. Biaya Transport", "4. Peserta", "5. Finalisasi"};
        for (int i = 0; i < 5; i++) {
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? "● " : "○ ") + stepTexts[i]);
                stepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                stepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }
    
    private void btnSaveTripActionPerformed(ActionEvent evt) {
        if (Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyimpan draf.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validasi dasar: pastikan setidaknya ada peserta sebelum menyimpan draf
        if (this.numberOfParticipants <= 0) {
            JOptionPane.showMessageDialog(this, "Harap tambahkan setidaknya satu peserta sebelum menyimpan draf.", "Informasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CustomTripController tripController = new CustomTripController();
            ReservasiController reservasiController = new ReservasiController();

            // 1. Siapkan CustomTripModel dengan status "draft"
            CustomTripModel customTrip = new CustomTripModel();
            customTrip.setUserId(Session.currentUser.getId());
            String namaTrip = currentDestinations.isEmpty() ? "Custom Trip Draft" : String.join(", ", currentDestinations);
            if (namaTrip.length() > 100) namaTrip = namaTrip.substring(0, 97) + "...";
            customTrip.setNamaTrip(namaTrip);
            customTrip.setTanggalMulai(itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).min(LocalDate::compareTo).orElse(null));
            customTrip.setTanggalAkhir(itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).max(LocalDate::compareTo).orElse(null));
            customTrip.setJumlahPeserta(numberOfParticipants);
            customTrip.setStatus("draft"); // <-- Status diatur menjadi DRAFT
            customTrip.setTotalHarga(currentTotalCost);
            customTrip.setCatatanUser(txtAreaFinalNotes.getText().trim());
            customTrip.setDetailList(itineraryDetails);

            int customTripId = tripController.saveCustomTrip(customTrip);

            if (customTripId != -1) {
                // 2. Buat ReservasiModel yang sesuai dengan status "draft" atau "pending"
                ReservasiModel reservasi = new ReservasiModel();
                reservasi.setUserId(Session.currentUser.getId());
                reservasi.setTripType("custom_trip");
                reservasi.setTripId(customTripId);
                reservasi.setKodeReservasi("DRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                reservasi.setTanggalReservasi(LocalDate.now());
                reservasi.setStatus("pending"); // Atur status reservasi menjadi draft juga

                int reservasiId = reservasiController.buatReservasi(reservasi);

                if (reservasiId != -1) {
                    // 3. Simpan semua detail penumpang
                    for (PenumpangModel penumpang : this.participantDetails) {
                        reservasiController.tambahPenumpangLengkap(reservasiId, penumpang);
                    }
                    
                    JOptionPane.showMessageDialog(this, "Trip berhasil disimpan sebagai draf!", "Draf Disimpan", JOptionPane.INFORMATION_MESSAGE);
                    mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);

                } else {
                    tripController.deleteCustomTrip(customTripId, reservasiId); // Rollback
                    JOptionPane.showMessageDialog(this, "Gagal membuat reservasi untuk draf.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan draf trip.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi error yang tidak terduga saat menyimpan draf: " + e.getMessage(), "Error Kritis", JOptionPane.ERROR_MESSAGE);
        }
    }
}
