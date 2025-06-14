package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.DestinasiController;
import model.CustomTripDetailModel;
import model.DestinasiModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PanelParticipantsStep extends JPanel {

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
    private JLabel lblStep4Participants; // This step's label
    private JLabel lblStep5Final;

    private JLabel lblCustomTripBuilderTitle;
    private JButton btnSaveTrip;

    private JPanel panelParticipantsInput;
    private JLabel lblJumlahPeserta;
    private JTextField txtJumlahPeserta;

    private JPanel panelTripSummary;
    private JLabel lblSummaryDestinationsDisplay;
    private JLabel lblSummaryDatesDisplay;
    private JLabel lblSummaryTransportDisplay;
    private JLabel lblSummaryAccommodationDisplay;
    private JLabel lblSummaryParticipantsDisplay;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummary;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasiSummaryDisplay;
    private final List<String> currentDestinations;
    private final List<CustomTripDetailModel> itineraryDetails;
    private double currentInitialEstimatedCost;
    private final String currentTransportMode;
    private final String currentTransportDetails;
    private final String currentAccommodationName;
    private final String currentRoomType;
    private final String currentAccommodationNotes;

    private int numberOfParticipants; // Field to store the parsed number of participants

    private DestinasiController destinasiController;

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";
    private final String PLACEHOLDER_TEXT_PARTICIPANTS = "Masukkan angka (min 1)";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;


    // --- KONTSTRUKTOR YANG DIUPDATE ---
    // Konstruktor yang paling komprehensif, menerima semua data yang mungkin
    public PanelParticipantsStep(MainAppFrame mainAppFrame, List<String> destinations, List<CustomTripDetailModel> itineraryDetails,
                                 double initialEstimatedCost, 
                                 String transportMode, String transportDetails,
                                 String accommodationName, String roomType, String accommodationNotes) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.itineraryDetails = itineraryDetails != null ? new ArrayList<>(itineraryDetails) : new ArrayList<>();
        this.currentInitialEstimatedCost = initialEstimatedCost;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        
        this.listModelDestinasiSummaryDisplay = new DefaultListModel<>();
        this.destinasiController = new DestinasiController(); // Inisialisasi DestinasiController

        initializeUI();
        applyAppTheme();
        setupLogicAndVisuals();
    }

    public PanelParticipantsStep(MainAppFrame aThis, List<String> selectedDestinationsFromItinerary, List<CustomTripDetailModel> itineraryDetails, double totalEstimatedCost, String fixedTransportCostLabel, String transportMode, String transportDetails) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


    private void initializeUI() {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

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
        lblStep5Final = new JLabel();     lblStep5Final.setBorder(stepLabelBorder);     lblStep5Final.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelBuildSteps.add(lblStep1Destinasi);
        panelBuildSteps.add(lblStep2Itinerary);
        panelBuildSteps.add(lblStep3TransportCost);
        panelBuildSteps.add(lblStep4Participants);
        panelBuildSteps.add(lblStep5Final);
        panelBuildSteps.add(Box.createVerticalGlue());

        add(panelBuildSteps, BorderLayout.WEST);

        panelCustomTripMain = new JPanel(new BorderLayout(10, 10));
        panelCustomTripMain.setBorder(new EmptyBorder(0, 10, 0, 0));

        this.panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Jumlah Peserta");
        btnSaveTrip = new JButton("Simpan Draf Trip");
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setBorder(new EmptyBorder(0,0,0,5));

        panelRightContent = new JPanel();
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0));

        // --- Participants Input Section ---
        panelParticipantsInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbcParticipants = new GridBagConstraints();
        gbcParticipants.insets = new Insets(5, 10, 5, 10);
        gbcParticipants.anchor = GridBagConstraints.WEST;
        gbcParticipants.fill = GridBagConstraints.HORIZONTAL;

        lblJumlahPeserta = new JLabel("Jumlah Peserta:");
        gbcParticipants.gridx = 0; gbcParticipants.gridy = 0; gbcParticipants.weightx = 0;
        panelParticipantsInput.add(lblJumlahPeserta, gbcParticipants);

        txtJumlahPeserta = new JTextField(5);
        gbcParticipants.gridx = 1; gbcParticipants.gridy = 0; gbcParticipants.weightx = 1.0;
        panelParticipantsInput.add(txtJumlahPeserta, gbcParticipants);
        
        gbcParticipants.gridx = 0; gbcParticipants.gridy = 1; gbcParticipants.gridwidth = 2; gbcParticipants.weighty = 1.0;
        panelParticipantsInput.add(new JLabel(), gbcParticipants); // Filler for vertical space

        panelLeftContent.add(panelParticipantsInput);
        panelLeftContent.add(Box.createVerticalGlue());

        // --- Trip Summary Section (Right Side) ---
        panelRightContent = new JPanel(); // Re-initialize to ensure no old content
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0));

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        // Destinations Summary
        lblSummaryDestinationsDisplay = new JLabel("Destinasi Terpilih:");
        listModelDestinasiSummaryDisplay = new DefaultListModel<>();
        listDestinasiSummary = new JList<>(listModelDestinasiSummaryDisplay);
        listDestinasiSummary.setEnabled(false);
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        jScrollPaneDestinasiSummary.setPreferredSize(new Dimension(0, 100));

        JPanel summaryContentTop = new JPanel();
        summaryContentTop.setLayout(new BoxLayout(summaryContentTop, BoxLayout.Y_AXIS));
        summaryContentTop.setOpaque(false);
        summaryContentTop.add(lblSummaryDestinationsDisplay);
        summaryContentTop.add(jScrollPaneDestinasiSummary);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Overall Trip Dates
        lblSummaryDatesDisplay = new JLabel("Tanggal Trip Keseluruhan: -");
        summaryContentTop.add(lblSummaryDatesDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));

        // Transport Summary
        lblSummaryTransportDisplay = new JLabel("Transportasi Dipilih: -");
        summaryContentTop.add(lblSummaryTransportDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));

        // Accommodation Summary
        lblSummaryAccommodationDisplay = new JLabel("Akomodasi Dipilih: -");
        summaryContentTop.add(lblSummaryAccommodationDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));

        lblSummaryParticipantsDisplay = new JLabel("Jumlah Peserta: -");
        summaryContentTop.add(lblSummaryParticipantsDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));

        panelTripSummary.add(summaryContentTop, BorderLayout.NORTH);

        panelRightContent.add(panelTripSummary);
        panelRightContent.add(Box.createRigidArea(new Dimension(0,10)));

        // --- Estimated Cost Section (Right Bottom) ---
        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        lblTripSummaryTitleInfo = new JLabel("Total Estimasi Biaya:"); 
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryTitleInfo, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelEstimatedCost.setPreferredSize(new Dimension(0, 60));
        panelRightContent.add(panelEstimatedCost);
        panelRightContent.add(Box.createVerticalGlue());

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(420);
        splitPaneContent.setResizeWeight(0.48);
        splitPaneContent.setContinuousLayout(true);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        this.panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Biaya Transport");
        btnNextStep = new JButton("Lanjut ke Finalisasi >");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnNextStep, BorderLayout.EAST);
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0));
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        this.add(panelMainHeader, BorderLayout.NORTH);
        this.add(splitPaneContent, BorderLayout.CENTER);
        this.add(panelMainFooter, BorderLayout.SOUTH);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        panelBuildSteps.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        panelBuildSteps.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER_COLOR),
            new EmptyBorder(10, 0, 10, 0)
        ));
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

        panelParticipantsInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Jumlah Peserta",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
        panelTripSummary.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Trip",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
        panelParticipantsInput.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);

        // Styling for participant input
        lblJumlahPeserta.setFont(AppTheme.FONT_LABEL_FORM);
        lblJumlahPeserta.setForeground(AppTheme.TEXT_DARK);
        styleInputField(txtJumlahPeserta, PLACEHOLDER_TEXT_PARTICIPANTS);

        // Styling for summary labels
        lblSummaryDestinationsDisplay.setFont(AppTheme.FONT_LABEL_FORM);
        lblSummaryDestinationsDisplay.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(new Color(0,0,0,0));
        listDestinasiSummary.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createEmptyBorder());
        jScrollPaneDestinasiSummary.getViewport().setOpaque(false);

        lblSummaryDatesDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryDatesDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        
        lblSummaryTransportDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryTransportDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        lblSummaryAccommodationDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryAccommodationDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        lblSummaryParticipantsDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryParticipantsDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);


        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        styleSecondaryButton(btnPrevStep, "< Kembali ke Biaya Transport");
        stylePrimaryButton(btnNextStep, "Lanjut ke Finalisasi >");
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
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private void styleInputField(JTextField textField, String placeholder) {
        if (textField == null) return;
        textField.setFont(AppTheme.FONT_TEXT_FIELD);
        textField.setBackground(AppTheme.INPUT_BACKGROUND);
        String currentText = textField.getText();
        if (currentText == null || currentText.isEmpty() || currentText.equals(placeholder)) {
            textField.setText(placeholder);
            textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        } else {
            textField.setForeground(AppTheme.INPUT_TEXT);
        }
        textField.setBorder(AppTheme.createDefaultInputBorder());
        textField.setMargin(new Insets(5, 8, 5, 8));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(AppTheme.INPUT_TEXT);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                }
            }
        });
    }

    private void setupLogicAndVisuals() {
        updateBuildStepLabels(4); // Active step is 4 (Participants)
        
        populateSummaryDisplay();

        styleInputField(txtJumlahPeserta, PLACEHOLDER_TEXT_PARTICIPANTS);
        txtJumlahPeserta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateParticipantsRelatedData(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateParticipantsRelatedData(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateParticipantsRelatedData(); }
        });
        
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);

        updateParticipantsRelatedData();
    }

    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {
            lblStep1Destinasi,
            lblStep2Itinerary,
            lblStep3TransportCost,
            lblStep4Participants,
            lblStep5Final
        };
        String[] stepTexts = {
            "1. Destinasi",
            "2. Itinerary",
            "3. Biaya Transport",
            "4. Peserta",
            "5. Finalisasi"
        };

        for (int i = 0; i < 5; i++) {
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? ACTIVE_STEP_ICON : INACTIVE_STEP_ICON) + stepTexts[i]);
                stepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                stepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }

    private void populateSummaryDisplay() {
        listModelDestinasiSummaryDisplay.clear();
        for (String dest : currentDestinations) {
            listModelDestinasiSummaryDisplay.addElement(dest);
        }

        LocalDate overallStartDate = null;
        LocalDate overallEndDate = null;
        for (CustomTripDetailModel detail : itineraryDetails) {
            LocalDate visitDate = detail.getTanggalKunjungan();
            if (visitDate != null) {
                if (overallStartDate == null || visitDate.isBefore(overallStartDate)) {
                    overallStartDate = visitDate;
                }
                if (overallEndDate == null || visitDate.isAfter(overallEndDate)) {
                    overallEndDate = visitDate;
                }
            }
        }
        String startDateFormatted = (overallStartDate != null) ? overallStartDate.format(DateTimeFormatter.ofPattern("dd MMMMyyyy")) : "-";
        String endDateFormatted = (overallEndDate != null) ? overallEndDate.format(DateTimeFormatter.ofPattern("dd MMMMyyyy")) : "-";
        lblSummaryDatesDisplay.setText("Tanggal Trip Keseluruhan: " + startDateFormatted + " s/d " + endDateFormatted);

        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "-");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (" + currentTransportDetails + ")";
        }
        lblSummaryTransportDisplay.setText("Transportasi Dipilih: " + transportInfo);

        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "-");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (Kamar: " + currentRoomType + ")";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - Catatan: " + currentAccommodationNotes;
        }
        lblSummaryAccommodationDisplay.setText("Akomodasi Dipilih: " + accommodationInfo);

        lblSummaryParticipantsDisplay.setText("Jumlah Peserta: " + numberOfParticipants + " Orang");
        
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentInitialEstimatedCost));
    }

    private void updateParticipantsRelatedData() {
        int parsedParticipants = 0;
        try {
            if (!txtJumlahPeserta.getText().trim().isEmpty()) {
                parsedParticipants = Integer.parseInt(txtJumlahPeserta.getText().trim());
                if (parsedParticipants < 1) {
                    parsedParticipants = 1;
                    txtJumlahPeserta.setText("1");
                }
            }
        } catch (NumberFormatException e) {
            parsedParticipants = 0;
        }
        numberOfParticipants = parsedParticipants;

        lblSummaryParticipantsDisplay.setText("Jumlah Peserta: " + numberOfParticipants + " Orang");

        updateEstimatedCost();

        btnNextStep.setEnabled(numberOfParticipants > 0);
    }

    private void updateEstimatedCost() {
        double currentCost = currentInitialEstimatedCost;

        currentCost += numberOfParticipants * 50000;

        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentCost));
    }

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        if (numberOfParticipants == 0) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah peserta (minimal 1) sebelum menyimpan draf.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = String.format(
            "Draf Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s\nTransportasi: %s\nAkomodasi: %s\nJumlah Peserta: %d\nEstimasi Biaya: %s",
            currentDestinations,
            lblSummaryDatesDisplay.getText(),
            lblSummaryTransportDisplay.getText(),
            lblSummaryAccommodationDisplay.getText(),
            numberOfParticipants,
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Draf Trip Berhasil (Simulasi)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRANSPORT_COST_STEP, 
                                   currentDestinations, 
                                   itineraryDetails, 
                                   currentInitialEstimatedCost,
                                   currentTransportMode,
                                   currentTransportDetails);
        } else {
            System.err.println("MainAppFrame reference is null in PanelParticipantsStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        if (numberOfParticipants == 0) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah peserta (minimal 1) untuk melanjutkan.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double currentTotalEstimatedCost = 0.0;
        try {
            String formattedCost = lblEstimasiHargaValue.getText().replace(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).getCurrency().getSymbol(), "").replace(".", "").replace(",", ".");
            currentTotalEstimatedCost = NumberFormat.getInstance(new Locale("id", "ID")).parse(formattedCost).doubleValue();
        } catch (ParseException e) {
            System.err.println("Error parsing estimated cost from label in ParticipantsStep: " + e.getMessage());
            currentTotalEstimatedCost = currentInitialEstimatedCost;
        }

        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_FINAL_STEP,
                                   currentDestinations,
                                   itineraryDetails,
                                   currentTransportMode,
                                   currentTransportDetails,
                                   currentAccommodationName,
                                   currentRoomType,
                                   currentAccommodationNotes,
                                   new ArrayList<>(), // Activities are not collected in this flow. Pass empty list.
                                   currentTotalEstimatedCost,
                                   numberOfParticipants);
        } else {
            System.err.println("MainAppFrame reference is null in PanelParticipantsStep (Next).");
        }
    }
}
