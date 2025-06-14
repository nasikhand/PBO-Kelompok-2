package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.ReservasiController;
import controller.CustomTripController;
import controller.DestinasiController;
import model.ReservasiModel;
import model.CustomTripModel;
import model.Session;
import model.CustomTripDetailModel;
import model.DestinasiModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private MainAppFrame mainAppFrame;

    private JPanel panelBuildSteps;
    private JPanel panelCustomTripMain;
    private JSplitPane splitPaneContent;
    private JPanel panelLeftContent;
    private JPanel panelRightContent;

    private JLabel lblBuildStepsTitle;
    private JLabel lblStep1Destinasi;
    private JLabel lblStep2Itinerary;
    private JLabel lblStep3TransportCost;
    private JLabel lblStep4Participants;
    private JLabel lblStep5Final;

    private JLabel lblCustomTripBuilderTitle;
    private JButton btnSaveTrip;

    private JLabel lblFinalReviewMessage;
    private JTextArea txtAreaFinalNotes;
    private JScrollPane scrollPaneFinalNotes;

    private JPanel panelPassengerInput;
    private JTextArea passengerNamesArea;
    private JScrollPane passengerNamesScrollPane;
    
    private JPanel panelTripSummaryFull;
    private JScrollPane scrollPaneTripSummaryFull;

    private JLabel lblDestinationsSummaryDisplay;
    private JList<String> listDestinationsDisplay;
    private JScrollPane scrollPaneDestinationsDisplay;
    private JLabel lblDatesSummaryDisplay;
    private JLabel lblTransportSummaryDisplay;
    private JLabel lblAccommodationSummaryDisplay;
    private JLabel lblParticipantsSummaryDisplay;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnFinishTrip;

    private final DefaultListModel<String> listModelDestinasiDisplay;

    private final List<String> currentDestinations;
    private final List<CustomTripDetailModel> itineraryDetails;
    private final String currentTransportMode;
    private final String currentTransportDetails;
    private final String currentAccommodationName;
    private final String currentRoomType;
    private final String currentAccommodationNotes;
    private final double currentTotalCost;
    private final int numberOfParticipants;
    private final List<String> participantNames;

    private DestinasiController destinasiController;

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;
    //</editor-fold>

    public PanelFinalStep(MainAppFrame mainAppFrame, List<String> destinations, List<CustomTripDetailModel> itineraryDetails,
                          String transportMode, String transportDetails, String accommodationName, 
                          String roomType, String accommodationNotes, List<String> participantNames,
                          double totalEstimatedCost, int numberOfParticipants) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.itineraryDetails = itineraryDetails != null ? new ArrayList<>(itineraryDetails) : new ArrayList<>();
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        this.currentTotalCost = totalEstimatedCost;
        this.numberOfParticipants = numberOfParticipants;
        this.participantNames = participantNames != null ? new ArrayList<>(participantNames) : new ArrayList<>();

        this.listModelDestinasiDisplay = new DefaultListModel<>();
        this.destinasiController = new DestinasiController();

        initializeUI();
        applyAppTheme();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(5, 5)); 
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        //<editor-fold defaultstate="collapsed" desc="Sidebar (Build Steps)">
        panelBuildSteps = new JPanel();
        panelBuildSteps.setLayout(new BoxLayout(panelBuildSteps, BoxLayout.Y_AXIS));
        panelBuildSteps.setPreferredSize(new Dimension(230, 0)); 
        lblBuildStepsTitle = new JLabel(" Langkah Pembangunan");
        lblBuildStepsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBuildSteps.add(lblBuildStepsTitle);
        EmptyBorder stepLabelBorder = new EmptyBorder(8, 15, 8, 10);
        lblStep1Destinasi = new JLabel(); lblStep1Destinasi.setBorder(stepLabelBorder); lblStep1Destinasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep2Itinerary = new JLabel(); lblStep2Itinerary.setBorder(stepLabelBorder); lblStep2Itinerary.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep3TransportCost = new JLabel(); lblStep3TransportCost.setBorder(stepLabelBorder); lblStep3TransportCost.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep4Participants = new JLabel(); lblStep4Participants.setBorder(stepLabelBorder); lblStep4Participants.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep5Final = new JLabel(); lblStep5Final.setBorder(stepLabelBorder); lblStep5Final.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBuildSteps.add(lblStep1Destinasi);
        panelBuildSteps.add(lblStep2Itinerary);
        panelBuildSteps.add(lblStep3TransportCost);
        panelBuildSteps.add(lblStep4Participants);
        panelBuildSteps.add(lblStep5Final);
        panelBuildSteps.add(Box.createVerticalGlue()); 
        add(panelBuildSteps, BorderLayout.WEST);
        //</editor-fold>

        panelCustomTripMain = new JPanel(new BorderLayout(10, 10));
        panelCustomTripMain.setBorder(new EmptyBorder(0, 10, 0, 0));

        this.panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Ringkasan Akhir");
        btnSaveTrip = new JButton("Simpan Draf Trip");
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setBorder(new EmptyBorder(0,0,0,5)); 
        
        lblFinalReviewMessage = new JLabel("<html>Silakan tinjau ringkasan lengkap perjalanan Anda di sebelah kanan. <br>Anda dapat menambahkan catatan akhir atau permintaan khusus di bawah ini sebelum menyelesaikan.</html>");
        lblFinalReviewMessage.setBorder(new EmptyBorder(10,10,15,10));
        panelLeftContent.add(lblFinalReviewMessage);

        txtAreaFinalNotes = new JTextArea(5, 20);
        scrollPaneFinalNotes = new JScrollPane(txtAreaFinalNotes);
        panelLeftContent.add(scrollPaneFinalNotes);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelPassengerInput = new JPanel(new BorderLayout());
        passengerNamesArea = new JTextArea();
        passengerNamesScrollPane = new JScrollPane(passengerNamesArea);
        panelPassengerInput.add(passengerNamesScrollPane, BorderLayout.CENTER);
        panelLeftContent.add(panelPassengerInput);
        panelLeftContent.add(Box.createVerticalGlue());
        
        panelRightContent = new JPanel(new BorderLayout());
        panelTripSummaryFull = new JPanel();
        panelTripSummaryFull.setLayout(new BoxLayout(panelTripSummaryFull, BoxLayout.Y_AXIS));
        panelTripSummaryFull.setBorder(new EmptyBorder(10,10,10,10)); 
        lblDestinationsSummaryDisplay = new JLabel("Destinasi:");
        listDestinationsDisplay = new JList<>(listModelDestinasiDisplay);
        scrollPaneDestinationsDisplay = new JScrollPane(listDestinationsDisplay);
        panelTripSummaryFull.add(lblDestinationsSummaryDisplay);
        panelTripSummaryFull.add(scrollPaneDestinationsDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));
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
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryTitleInfo, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelRightContent.add(panelEstimatedCost, BorderLayout.SOUTH);

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(420);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);
        this.panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Peserta");
        btnFinishTrip = new JButton("Selesaikan & Pesan Trip");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnFinishTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        add(panelCustomTripMain, BorderLayout.CENTER);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        panelBuildSteps.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        panelBuildSteps.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER_COLOR), new EmptyBorder(10, 0, 10, 0)));
        lblBuildStepsTitle.setFont(AppTheme.FONT_SUBTITLE);
        lblBuildStepsTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        lblBuildStepsTitle.setBorder(new EmptyBorder(10, 10, 15, 10));
        panelCustomTripMain.setBackground(Color.WHITE);
        panelCustomTripMain.setBorder(new EmptyBorder(15,20,15,20));
        if (panelMainHeader != null) panelMainHeader.setOpaque(false);
        lblCustomTripBuilderTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblCustomTripBuilderTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnSaveTrip, "Simpan Draf Trip");
        panelLeftContent.setOpaque(false);
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);
        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;
        
        panelPassengerInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Detail Peserta", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titledBorderFont, titledBorderColor));
        passengerNamesArea.setFont(AppTheme.FONT_TEXT_FIELD);
        passengerNamesArea.setEditable(false);
        passengerNamesArea.setOpaque(false);
        passengerNamesArea.setForeground(AppTheme.TEXT_DARK);
        passengerNamesScrollPane.setBorder(null);
        passengerNamesScrollPane.getViewport().setOpaque(false);

        scrollPaneTripSummaryFull.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Lengkap Perjalanan Anda", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya Total", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, titledBorderFont, titledBorderColor));
        panelTripSummaryFull.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        if(scrollPaneTripSummaryFull.getViewport() != null) scrollPaneTripSummaryFull.getViewport().setOpaque(false);
        lblFinalReviewMessage.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblFinalReviewMessage.setForeground(AppTheme.TEXT_DARK);
        txtAreaFinalNotes.setFont(AppTheme.FONT_TEXT_FIELD);
        txtAreaFinalNotes.setBackground(AppTheme.INPUT_BACKGROUND);
        txtAreaFinalNotes.setForeground(AppTheme.INPUT_TEXT);
        txtAreaFinalNotes.setBorder(AppTheme.createDefaultInputBorder());
        txtAreaFinalNotes.setMargin(new Insets(5,5,5,5));
        scrollPaneFinalNotes.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        
        Font summaryHeaderFont = AppTheme.FONT_SUBTITLE;
        Font summaryDetailFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color summaryHeaderColor = AppTheme.PRIMARY_BLUE_DARK;
        Color summaryDetailColor = AppTheme.TEXT_DARK;
        lblDestinationsSummaryDisplay.setFont(summaryHeaderFont);
        lblDestinationsSummaryDisplay.setForeground(summaryHeaderColor);
        listDestinationsDisplay.setFont(summaryDetailFont);
        listDestinationsDisplay.setBackground(new Color(0,0,0,0));
        listDestinationsDisplay.setForeground(summaryDetailColor);
        scrollPaneDestinationsDisplay.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneDestinationsDisplay.getViewport().setOpaque(false);
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
        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        styleSecondaryButton(btnPrevStep, "< Kembali ke Peserta");
        stylePrimaryButton(btnFinishTrip, "Selesaikan & Pesan Trip");
    }
    
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(5);

        String participantsHeader = "Jumlah Peserta: " + this.numberOfParticipants + " Orang";
        StringBuilder namesList = new StringBuilder();
        for(int i = 0; i < this.participantNames.size(); i++) {
            namesList.append((i + 1)).append(". ").append(this.participantNames.get(i)).append("\n");
        }
        passengerNamesArea.setText(participantsHeader + "\n\n" + namesList.toString());
        passengerNamesArea.setCaretPosition(0);

        populateSummaryDisplay();

        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnFinishTrip.addActionListener(this::btnFinishTripActionPerformed);
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
        lblDatesSummaryDisplay.setText("Tanggal: " + startDateFormatted + " s/d " + endDateFormatted);

        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "-");
        lblTransportSummaryDisplay.setText("Transportasi Utama: " + transportInfo);
        
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "-");
        lblAccommodationSummaryDisplay.setText("Akomodasi: " + accommodationInfo);
        
        lblParticipantsSummaryDisplay.setText("Jumlah Peserta: " + numberOfParticipants + " Orang");
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentTotalCost));
    }
    
    // =========================================================================
    // == FIXED METHOD WITH ERROR HANDLING
    // =========================================================================
    private void btnFinishTripActionPerformed(ActionEvent evt) {
        if (Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk memesan trip.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // --- Prepare Models to Save to DB ---
            CustomTripController tripController = new CustomTripController();
            ReservasiController reservasiController = new ReservasiController();

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
            customTrip.setDetailedTransportMode(currentTransportMode);
            customTrip.setDetailedTransportDetails(currentTransportDetails);
            customTrip.setDetailedAccommodationName(currentAccommodationName);
            customTrip.setDetailedRoomType(currentRoomType);
            customTrip.setDetailedAccommodationNotes(currentAccommodationNotes);

            int customTripId = tripController.saveCustomTrip(customTrip);

            if (customTripId != -1) {
                ReservasiModel reservasi = new ReservasiModel();
                reservasi.setUserId(Session.currentUser.getId());
                reservasi.setTripType("custom_trip");
                reservasi.setTripId(customTripId);
                reservasi.setKodeReservasi("CTRIP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                reservasi.setTanggalReservasi(LocalDate.now());
                reservasi.setStatus("dipesan");

                int reservasiId = reservasiController.buatReservasi(reservasi);

                if (reservasiId != -1) {
                    boolean allPassengersSaved = true;
                    for (String penumpangNama : this.participantNames) {
                        if (!reservasiController.tambahPenumpang(reservasiId, penumpangNama)) {
                            allPassengersSaved = false;
                        }
                    }

                    JOptionPane.showMessageDialog(this, "Custom Trip Anda berhasil dipesan!", "Pemesanan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    mainAppFrame.showPaymentPanel(reservasiId, Session.currentUser.getNamaLengkap(), Session.currentUser.getEmail(), Session.currentUser.getNomorTelepon(), this.participantNames);
                } else {
                    tripController.deleteCustomTrip(customTripId); // Rollback
                    JOptionPane.showMessageDialog(this, "Gagal membuat reservasi untuk trip ini.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan detail trip utama.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            // --- THIS IS THE NEW DEBUGGING BLOCK ---
            // It will catch any unexpected error during the process and show you the details.
            e.printStackTrace(); // Print the full error to the console for detailed analysis
            JOptionPane.showMessageDialog(this, 
                "Terjadi error yang tidak terduga saat menyelesaikan pesanan.\n\n" +
                "Detail Error: " + e.getMessage(), 
                "Error Kritis", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PARTICIPANTS_STEP,
                                  currentDestinations,
                                  itineraryDetails,
                                  currentTotalCost,
                                  currentTransportMode,
                                  currentTransportDetails,
                                  currentAccommodationName,
                                  currentRoomType,
                                  currentAccommodationNotes);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Other Helper and Action Methods">
    private void btnSaveTripActionPerformed(ActionEvent evt) {
        if (Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyimpan draf.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CustomTripController customTripController = new CustomTripController();
        ReservasiController reservasiController = new ReservasiController();

        CustomTripModel customTripDraf = new CustomTripModel();
        // ... (populate draft object)
        customTripDraf.setUserId(Session.currentUser.getId());
        String namaTripDraf = currentDestinations.isEmpty() ? "Custom Trip" : String.join(", ", currentDestinations);
        if (namaTripDraf.length() > 100) namaTripDraf = namaTripDraf.substring(0, 97) + "...";
        customTripDraf.setNamaTrip(namaTripDraf);
        customTripDraf.setTanggalMulai(itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).min(LocalDate::compareTo).orElse(null));
        customTripDraf.setTanggalAkhir(itineraryDetails.stream().map(CustomTripDetailModel::getTanggalKunjungan).max(LocalDate::compareTo).orElse(null));
        customTripDraf.setJumlahPeserta(numberOfParticipants);
        customTripDraf.setStatus("draft");
        customTripDraf.setTotalHarga(currentTotalCost); // FIXED: Use correct cost variable
        customTripDraf.setCatatanUser(txtAreaFinalNotes.getText().trim());
        customTripDraf.setDetailList(itineraryDetails);
        // ... (set other detailed fields if necessary)

        int customTripIdDraf = customTripController.saveCustomTrip(customTripDraf);

        if (customTripIdDraf != -1) {
            ReservasiModel reservasiDraf = new ReservasiModel();
            // ... (populate draft reservation object)
            reservasiDraf.setUserId(Session.currentUser.getId());
            reservasiDraf.setTripType("custom_trip");
            reservasiDraf.setTripId(customTripIdDraf);
            reservasiDraf.setKodeReservasi("DRFCT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            reservasiDraf.setTanggalReservasi(LocalDate.now());
            reservasiDraf.setStatus("pending");

            int reservasiIdDraf = reservasiController.buatReservasi(reservasiDraf);

            if (reservasiIdDraf != -1) {
                // FIXED: Use the correct list of participant names.
                boolean allPassengersSavedDraf = true;
                for (String penumpangNamaDraf : this.participantNames) {
                    if (!reservasiController.tambahPenumpang(reservasiIdDraf, penumpangNamaDraf)) {
                        allPassengersSavedDraf = false;
                    }
                }
                
                String msg = "Draf trip kustom berhasil disimpan.";
                JOptionPane.showMessageDialog(this, msg + "\nAnda akan dialihkan ke halaman Pesanan Saya.", "Draf Disimpan", JOptionPane.INFORMATION_MESSAGE);
                mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat reservasi draf.", "Error Draf", JOptionPane.ERROR_MESSAGE);
                customTripController.deleteCustomTrip(customTripIdDraf);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan Custom Trip sebagai draf.", "Error Draf", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {lblStep1Destinasi, lblStep2Itinerary, lblStep3TransportCost, lblStep4Participants, lblStep5Final};
        String[] stepTexts = {"1. Destinasi", "2. Itinerary", "3. Biaya Transport", "4. Peserta", "5. Finalisasi"};
        for (int i = 0; i < 5; i++) {
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? ACTIVE_STEP_ICON : INACTIVE_STEP_ICON) + stepTexts[i]);
                stepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                stepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }
    private void stylePrimaryButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }
    private void styleSecondaryButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
    }
    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        if (button == null) return;
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(originalColor); }
        });
    }
    //</editor-fold>
}
