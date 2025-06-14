package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.ReservasiController;
import controller.CustomTripController;
import controller.DestinasiController; // Import DestinasiController for getting DestinasiModel by name
import model.ReservasiModel;
import model.CustomTripModel;
import model.Session;
import model.UserModel;
import model.CustomTripDetailModel; // Import CustomTripDetailModel
import model.DestinasiModel; // Import DestinasiModel

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
import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;

public class PanelFinalStep extends JPanel {

    private MainAppFrame mainAppFrame;

    private JPanel panelBuildSteps;
    private JPanel panelCustomTripMain;
    private JSplitPane splitPaneContent;
    private JPanel panelLeftContent;
    private JPanel panelRightContent;

    private JLabel lblBuildStepsTitle;
    private JLabel lblStep1Destinasi;
    private JLabel lblStep2Itinerary; // Updated label
    private JLabel lblStep3TransportCost; // Updated label
    private JLabel lblStep4Participants; // Updated label
    private JLabel lblStep5Final;     // Updated label

    private JLabel lblCustomTripBuilderTitle;
    private JButton btnSaveTrip;

    private JLabel lblFinalReviewMessage;
    private JTextArea txtAreaFinalNotes;
    private JScrollPane scrollPaneFinalNotes;

    // --- Passenger Input Components (from previous version, re-added for consistency) ---
    private JPanel panelPassengerInput; // This is now just a placeholder for summary, not input
    private JTextField txtJumlahPeserta; // Display field
    private JTextField txtNamaPenumpang1; // Display field
    private JTextField txtNamaPenumpang2; // Display field
    private JTextField txtNamaPenumpang3; // Display field
    private JLabel lblJumlahPesertaTitle;
    private JLabel lblPenumpang1Title;
    private JLabel lblPenumpang2Title;
    private JLabel lblPenumpang3Title;


    private JPanel panelTripSummaryFull;
    private JScrollPane scrollPaneTripSummaryFull;

    private JLabel lblDestinationsSummaryDisplay;
    private JList<String> listDestinationsDisplay;
    private JScrollPane scrollPaneDestinationsDisplay;
    private JLabel lblDatesSummaryDisplay;
    private JLabel lblTransportSummaryDisplay;
    private JLabel lblAccommodationSummaryDisplay;
    private JLabel lblActivitiesSummaryDisplay;
    private JLabel lblParticipantsSummaryDisplay; // New label for participants summary
    private JList<String> listActivitiesDisplay;
    private JScrollPane scrollPaneActivitiesDisplay;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnFinishTrip; // "Selesaikan & Pesan Trip"

    private final DefaultListModel<String> listModelDestinasiDisplay;
    private final DefaultListModel<String> listModelActivitiesDisplay;

    // --- ALL DATA PASSED FROM PREVIOUS STEPS ---
    private final List<String> currentDestinations; // From DestinationStep
    private final List<CustomTripDetailModel> itineraryDetails; // From ItineraryStep
    private final String currentTransportMode;      // From TransportCostStep
    private final String currentTransportDetails;   // From TransportCostStep
    private final String currentAccommodationName;  // From AccommodationStep
    private final String currentRoomType;           // From AccommodationStep
    private final String currentAccommodationNotes; // From AccommodationStep
    private final List<String> currentActivities;   // From ActivityStep
    private final double currentInitialEstimatedCost; // Cumulative cost from ActivityStep
    private final int numberOfParticipants; // From ParticipantsStep

    private DestinasiController destinasiController; // To get destinasi name for summary

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;

    // Constructor updated to receive all data for the final summary
    public PanelFinalStep(MainAppFrame mainAppFrame, List<String> destinations, List<CustomTripDetailModel> itineraryDetails,
                          String transportMode, String transportDetails, String accommodationName, 
                          String roomType, String accommodationNotes, List<String> activities, 
                          double totalEstimatedCost, int numberOfParticipants) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.itineraryDetails = itineraryDetails != null ? new ArrayList<>(itineraryDetails) : new ArrayList<>();
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        this.currentActivities = activities != null ? new ArrayList<>(activities) : new ArrayList<>();
        this.currentInitialEstimatedCost = totalEstimatedCost; // Now currentInitialEstimatedCost is the final total
        this.numberOfParticipants = numberOfParticipants;

        this.listModelDestinasiDisplay = new DefaultListModel<>();
        this.listModelActivitiesDisplay = new DefaultListModel<>();
        this.destinasiController = new DestinasiController(); // Initialize destinasiController

        initializeUI();
        applyAppTheme();
        setupLogicAndVisuals();
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
        txtAreaFinalNotes.setLineWrap(true);
        txtAreaFinalNotes.setWrapStyleWord(true);
        scrollPaneFinalNotes = new JScrollPane(txtAreaFinalNotes);
        scrollPaneFinalNotes.setPreferredSize(new Dimension(0, 150));
        panelLeftContent.add(scrollPaneFinalNotes);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        // --- Passenger Display Panel (No longer input fields, just display) ---
        panelPassengerInput = new JPanel(new GridBagLayout());
        panelPassengerInput.setOpaque(false);
        panelPassengerInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Detail Peserta",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            AppTheme.FONT_SUBTITLE, AppTheme.PRIMARY_BLUE_DARK));

        GridBagConstraints gbcPass = new GridBagConstraints();
        gbcPass.insets = new Insets(5, 5, 5, 5);
        gbcPass.anchor = GridBagConstraints.WEST;
        gbcPass.fill = GridBagConstraints.HORIZONTAL;

        lblJumlahPesertaTitle = new JLabel("Jumlah Peserta:");
        gbcPass.gridx = 0; gbcPass.gridy = 0; gbcPass.weightx = 0;
        panelPassengerInput.add(lblJumlahPesertaTitle, gbcPass);
        txtJumlahPeserta = new JTextField(5); // This will now display the number of participants
        txtJumlahPeserta.setEditable(false); // Make it read-only
        gbcPass.gridx = 1; gbcPass.gridy = 0; gbcPass.weightx = 1.0;
        panelPassengerInput.add(txtJumlahPeserta, gbcPass);

        lblPenumpang1Title = new JLabel("Nama Peserta 1:");
        gbcPass.gridx = 0; gbcPass.gridy = 1;
        panelPassengerInput.add(lblPenumpang1Title, gbcPass);
        txtNamaPenumpang1 = new JTextField(20); // Display field
        txtNamaPenumpang1.setEditable(false); // Make it read-only
        gbcPass.gridx = 1; gbcPass.gridy = 1;
        panelPassengerInput.add(txtNamaPenumpang1, gbcPass);

        lblPenumpang2Title = new JLabel("Nama Peserta 2:");
        gbcPass.gridx = 0; gbcPass.gridy = 2;
        panelPassengerInput.add(lblPenumpang2Title, gbcPass);
        txtNamaPenumpang2 = new JTextField(20); // Display field
        txtNamaPenumpang2.setEditable(false); // Make it read-only
        gbcPass.gridx = 1; gbcPass.gridy = 2;
        panelPassengerInput.add(txtNamaPenumpang2, gbcPass);

        lblPenumpang3Title = new JLabel("Nama Peserta 3:");
        gbcPass.gridx = 0; gbcPass.gridy = 3;
        panelPassengerInput.add(lblPenumpang3Title, gbcPass);
        txtNamaPenumpang3 = new JTextField(20); // Display field
        txtNamaPenumpang3.setEditable(false); // Make it read-only
        gbcPass.gridx = 1; gbcPass.gridy = 3;
        panelPassengerInput.add(txtNamaPenumpang3, gbcPass);
        
        panelLeftContent.add(panelPassengerInput);
        panelLeftContent.add(Box.createVerticalGlue());

        panelRightContent = new JPanel(new BorderLayout());

        panelTripSummaryFull = new JPanel();
        panelTripSummaryFull.setLayout(new BoxLayout(panelTripSummaryFull, BoxLayout.Y_AXIS));
        panelTripSummaryFull.setBorder(new EmptyBorder(10,10,10,10)); 

        lblDestinationsSummaryDisplay = new JLabel("Destinasi:");
        listDestinationsDisplay = new JList<>(listModelDestinasiDisplay);
        scrollPaneDestinationsDisplay = new JScrollPane(listDestinationsDisplay);
        scrollPaneDestinationsDisplay.setPreferredSize(new Dimension(300, 80));
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

        lblActivitiesSummaryDisplay = new JLabel("Kegiatan:");
        listActivitiesDisplay = new JList<>(listModelActivitiesDisplay);
        scrollPaneActivitiesDisplay = new JScrollPane(listActivitiesDisplay);
        scrollPaneActivitiesDisplay.setPreferredSize(new Dimension(300, 100));
        panelTripSummaryFull.add(lblActivitiesSummaryDisplay);
        panelTripSummaryFull.add(scrollPaneActivitiesDisplay);

        lblParticipantsSummaryDisplay = new JLabel("Jumlah Peserta:"); // New summary label for participants
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));
        panelTripSummaryFull.add(lblParticipantsSummaryDisplay);
        
        scrollPaneTripSummaryFull = new JScrollPane(panelTripSummaryFull);
        scrollPaneTripSummaryFull.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelRightContent.add(scrollPaneTripSummaryFull, BorderLayout.CENTER);

        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        lblTripSummaryTitleInfo = new JLabel("Total Estimasi Biaya:"); 
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryTitleInfo, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelEstimatedCost.setPreferredSize(new Dimension(0, 60));
        panelRightContent.add(panelEstimatedCost, BorderLayout.SOUTH);

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(380);
        splitPaneContent.setResizeWeight(0.4);
        splitPaneContent.setContinuousLayout(true);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        this.panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Peserta"); // Changed Prev button text
        btnFinishTrip = new JButton("Selesaikan & Pesan Trip");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnFinishTrip, BorderLayout.EAST);
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0));
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        add(panelCustomTripMain, BorderLayout.CENTER);
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

        // Styling for main sections
        panelLeftContent.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Catatan Akhir & Konfirmasi",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        panelPassengerInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Detail Peserta",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
        scrollPaneTripSummaryFull.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Lengkap Perjalanan Anda",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya Total",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
        panelPassengerInput.setOpaque(false);
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
        
        // Styling for passenger display fields (now read-only)
        JLabel[] passengerLabels = {lblJumlahPesertaTitle, lblPenumpang1Title, lblPenumpang2Title, lblPenumpang3Title};
        for (JLabel label : passengerLabels) {
            if (label != null) {
                label.setFont(AppTheme.FONT_LABEL_FORM);
                label.setForeground(AppTheme.TEXT_DARK);
            }
        }
        JTextField[] passengerTextFields = {txtJumlahPeserta, txtNamaPenumpang1, txtNamaPenumpang2, txtNamaPenumpang3};
        for (JTextField textField : passengerTextFields) {
            if (textField != null) {
                styleInputField(textField, ""); // No placeholder for display fields
                textField.setBorder(null); // No border for read-only display
                textField.setOpaque(false); // Transparent background
                textField.setEditable(false);
                textField.setForeground(AppTheme.TEXT_DARK); // Text color for display
            }
        }

        // Summary Display Labels and Lists
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

        lblActivitiesSummaryDisplay.setFont(summaryHeaderFont);
        lblActivitiesSummaryDisplay.setForeground(summaryHeaderColor);
        listActivitiesDisplay.setFont(summaryDetailFont);
        listActivitiesDisplay.setBackground(new Color(0,0,0,0));
        listActivitiesDisplay.setForeground(summaryDetailColor);
        scrollPaneActivitiesDisplay.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneActivitiesDisplay.getViewport().setOpaque(false);

        lblParticipantsSummaryDisplay.setFont(summaryHeaderFont); // Style for new participants label
        lblParticipantsSummaryDisplay.setForeground(summaryHeaderColor);


        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_LARGE);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);
        
        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        styleSecondaryButton(btnPrevStep, "< Kembali ke Peserta"); // Updated Prev button text
        stylePrimaryButton(btnFinishTrip, "Selesaikan & Pesan Trip");
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
    
    private void styleInputField(JTextField textField, String placeholder) { // Helper for JTextField styling
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

    private void addFocusBorderEffect(JTextField textField) { // Helper for JTextField focus (no placeholder logic)
        if (textField == null) return; 
        
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }
    
    private void addFocusBorderEffect(JTextArea textArea) { // Helper for JTextArea focus
        if (textArea == null) return;
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textArea.setBorder(AppTheme.createFocusBorder());
            }
            @Override
            public void focusLost(FocusEvent e) {
                textArea.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }

    /**
     * Sets up the logic and populates initial visuals based on constructor data.
     */
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(5); // Set step 5 (Final) as active

        // Populate summary displays from previous steps
        populateSummaryDisplay();

        // Populate participant display fields (now read-only)
        txtJumlahPeserta.setText(String.valueOf(numberOfParticipants));
        // For names, you'd need to store actual names passed from ParticipantsStep,
        // but currently, only the count is passed. For now, display placeholders.
        txtNamaPenumpang1.setText(numberOfParticipants >= 1 ? "Peserta 1" : "-");
        txtNamaPenumpang2.setText(numberOfParticipants >= 2 ? "Peserta 2" : "-");
        txtNamaPenumpang3.setText(numberOfParticipants >= 3 ? "Peserta 3" : "-");
        
        // Setup action listeners for buttons
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnFinishTrip.addActionListener(this::btnFinishTripActionPerformed);
    }
    
    /**
     * Updates the visual state of the build step labels in the sidebar.
     * @param activeStep The current active step number (1-5).
     */
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

        for (int i = 0; i < 5; i++) { // Iterate through 5 steps
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? ACTIVE_STEP_ICON : INACTIVE_STEP_ICON) + stepTexts[i]);
                stepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                stepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }

    /**
     * Populates the trip summary display based on data from previous steps.
     */
    private void populateSummaryDisplay() {
        // Populate destinations list
        listModelDestinasiDisplay.clear();
        for (String destName : currentDestinations) {
            listModelDestinasiDisplay.addElement(destName);
        }

        // Calculate and display overall trip dates from itineraryDetails
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
        lblDatesSummaryDisplay.setText("Tanggal Trip Keseluruhan: " + startDateFormatted + " s/d " + endDateFormatted);

        // Populate transport summary
        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "-");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (" + currentTransportDetails + ")";
        }
        lblTransportSummaryDisplay.setText("Transportasi: " + transportInfo);

        // Populate accommodation summary
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "-");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (Kamar: " + currentRoomType + ")";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - Catatan: " + currentAccommodationNotes;
        }
        lblAccommodationSummaryDisplay.setText("Akomodasi: " + accommodationInfo);

        // Populate activities list
        if(listModelActivitiesDisplay.isEmpty() && currentActivities != null){ // Populate only if empty and activities exist
            for(String activity : currentActivities){
                listModelActivitiesDisplay.addElement(activity);
            }
        }
        if(listModelActivitiesDisplay.isEmpty()){
            listModelActivitiesDisplay.addElement("Tidak ada kegiatan spesifik yang direncanakan.");
        }
        lblActivitiesSummaryDisplay.setText("Kegiatan:"); // Label for activities list

        // Populate participants summary
        lblParticipantsSummaryDisplay.setText("Jumlah Peserta: " + numberOfParticipants + " Orang");
        
        // Display final estimated cost
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentInitialEstimatedCost));
    }
    
    /**
     * Placeholder method to calculate estimated custom trip price.
     * In FinalStep, this mostly just reflects the cumulative cost from previous steps.
     * @return The estimated total price (cumulative from previous steps).
     */
    private double calculateEstimatedCustomTripPrice() {
        return currentInitialEstimatedCost; // Final step mostly just reflects the cumulative cost
    }

    /**
     * Action performed when the "Simpan Draf Trip" button is clicked.
     */
    private void btnSaveTripActionPerformed(ActionEvent evt) {
        // Validate login and participants
        if (Session.currentUser == null || Session.currentUser.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyimpan draf trip kustom.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (numberOfParticipants <= 0) { // Check if participants is valid for saving draft
            JOptionPane.showMessageDialog(this, "Masukkan jumlah peserta (minimal 1) untuk menyimpan draf.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Prepare CustomTripModel for Draft ---
        String namaTripDraf = currentDestinations.isEmpty() ? "Custom Trip" : String.join(", ", currentDestinations);
        if (namaTripDraf.length() > 100) namaTripDraf = namaTripDraf.substring(0, 97) + "...";

        LocalDate parsedStartDateDraf = itineraryDetails.isEmpty() ? null : itineraryDetails.get(0).getTanggalKunjungan();
        LocalDate parsedEndDateDraf = itineraryDetails.isEmpty() ? null : itineraryDetails.get(itineraryDetails.size()-1).getTanggalKunjungan();
        
        CustomTripModel customTripDraf = new CustomTripModel();
        customTripDraf.setUserId(Session.currentUser.getId());
        customTripDraf.setNamaTrip(namaTripDraf);
        customTripDraf.setTanggalMulai(parsedStartDateDraf);
        customTripDraf.setTanggalAkhir(parsedEndDateDraf);
        customTripDraf.setJumlahPeserta(numberOfParticipants);
        customTripDraf.setStatus("draft");
        customTripDraf.setTotalHarga(currentInitialEstimatedCost);
        customTripDraf.setCatatanUser(txtAreaFinalNotes.getText().trim());
        
        // Set detailed fields for saving
        customTripDraf.setDetailedTransportMode(currentTransportMode);
        customTripDraf.setDetailedTransportDetails(currentTransportDetails);
        customTripDraf.setDetailedAccommodationName(currentAccommodationName);
        customTripDraf.setDetailedRoomType(currentRoomType);
        customTripDraf.setDetailedAccommodationNotes(currentAccommodationNotes);
        customTripDraf.setDetailedActivities(currentActivities); // Save activities list

        // --- Add itinerary details (CustomTripDetailModel list) to CustomTripModel ---
        customTripDraf.setDetailList(itineraryDetails); // Set the list of CustomTripDetailModel

        // --- Save CustomTripModel Draft to DB ---
        CustomTripController customTripController = new CustomTripController();
        int customTripIdDraf = customTripController.saveCustomTrip(customTripDraf);

        if (customTripIdDraf != -1) {
            customTripDraf.setId(customTripIdDraf); // Update ID in model after saving

            // --- Create ReservasiModel for this Custom Trip Draft ---
            ReservasiModel reservasiDraf = new ReservasiModel();
            reservasiDraf.setUserId(Session.currentUser.getId());
            reservasiDraf.setTripType("custom_trip");
            reservasiDraf.setTripId(customTripIdDraf);
            reservasiDraf.setKodeReservasi("DRFCT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            reservasiDraf.setTanggalReservasi(LocalDate.now());
            reservasiDraf.setStatus("pending");

            ReservasiController reservasiController = new ReservasiController();
            int reservasiIdDraf = reservasiController.buatReservasi(reservasiDraf);

            if (reservasiIdDraf != -1) {
                // Passengers are collected in PanelParticipantsStep,
                // but PanelFinalStep collects them directly via JTextfields if not passed.
                // Assuming penumpangList holds actual names for the reservation from this point.
                List<String> collectedPassengerNames = new ArrayList<>();
                if (!txtNamaPenumpang1.getText().trim().isEmpty()) collectedPassengerNames.add(txtNamaPenumpang1.getText().trim());
                if (!txtNamaPenumpang2.getText().trim().isEmpty()) collectedPassengerNames.add(txtNamaPenumpang2.getText().trim());
                if (!txtNamaPenumpang3.getText().trim().isEmpty()) collectedPassengerNames.add(txtNamaPenumpang3.getText().trim());


                boolean allPassengersSavedDraf = true;
                for (String penumpangNamaDraf : collectedPassengerNames) {
                    if (!reservasiController.tambahPenumpang(reservasiIdDraf, penumpangNamaDraf)) {
                        allPassengersSavedDraf = false;
                    }
                }
                
                String msg = "Draf trip kustom berhasil disimpan. ID Reservasi: " + reservasiIdDraf + ".";
                if (!allPassengersSavedDraf) {
                    msg += "\nNamun, beberapa data penumpang mungkin tidak tersimpan.";
                }
                JOptionPane.showMessageDialog(this, msg + "\nAnda akan dialihkan ke halaman Pesanan Saya.", "Draf Disimpan", JOptionPane.INFORMATION_MESSAGE);
                
                mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat reservasi draf untuk Custom Trip Anda.", "Error Draf", JOptionPane.ERROR_MESSAGE);
                customTripController.deleteCustomTrip(customTripIdDraf); // Clean up custom trip if reservation fails
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan Custom Trip sebagai draf.", "Error Draf", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Action performed when the "Kembali ke Peserta" (Prev Step) button is clicked.
     * Navigates back to PanelParticipantsStep.
     */
    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PARTICIPANTS_STEP,
                                   currentDestinations,
                                   itineraryDetails,
                                   currentInitialEstimatedCost, // Cost up to Accommodation/Activities
                                   currentTransportMode,
                                   currentTransportDetails,
                                   currentAccommodationName,
                                   currentRoomType,
                                   currentAccommodationNotes); // Pass all data back to ParticipantsStep
        } else {
            System.err.println("MainAppFrame reference is null in PanelFinalStep (Prev). Cannot navigate.");
        }
    }

    /**
     * Action performed when the "Selesaikan & Pesan Trip" (Finish Trip) button is clicked.
     * This method handles saving the custom trip, creating a reservation, and
     * navigating to the payment screen.
     */
    private void btnFinishTripActionPerformed(ActionEvent evt) {
        if (Session.currentUser == null || Session.currentUser.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyelesaikan dan memesan trip kustom.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (numberOfParticipants <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah peserta harus lebih dari 0 untuk memesan trip.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Prepare CustomTripModel ---
        String namaTrip = currentDestinations.isEmpty() ? "Custom Trip" : String.join(", ", currentDestinations);
        if (namaTrip.length() > 100) namaTrip = namaTrip.substring(0, 97) + "...";
        
        LocalDate parsedStartDate = itineraryDetails.isEmpty() ? null : itineraryDetails.get(0).getTanggalKunjungan();
        LocalDate parsedEndDate = itineraryDetails.isEmpty() ? null : itineraryDetails.get(itineraryDetails.size()-1).getTanggalKunjungan();
        
        // Use currentInitialEstimatedCost as the final total price
        double totalHargaEstimasi = currentInitialEstimatedCost; 
        String statusCustomTrip = "dipesan";

        CustomTripModel customTrip = new CustomTripModel();
        customTrip.setUserId(Session.currentUser.getId());
        customTrip.setNamaTrip(namaTrip);
        customTrip.setTanggalMulai(parsedStartDate);
        customTrip.setTanggalAkhir(parsedEndDate);
        customTrip.setJumlahPeserta(numberOfParticipants);
        customTrip.setStatus(statusCustomTrip);
        customTrip.setTotalHarga(totalHargaEstimasi);
        customTrip.setCatatanUser(txtAreaFinalNotes.getText().trim());

        // Set detailed fields for saving
        customTrip.setDetailedTransportMode(currentTransportMode);
        customTrip.setDetailedTransportDetails(currentTransportDetails);
        customTrip.setDetailedAccommodationName(currentAccommodationName);
        customTrip.setDetailedRoomType(currentRoomType);
        customTrip.setDetailedAccommodationNotes(currentAccommodationNotes);
        customTrip.setDetailedActivities(currentActivities); // Save activities list

        // --- Add itinerary details (CustomTripDetailModel list) to CustomTripModel ---
        customTrip.setDetailList(itineraryDetails); // Set the list of CustomTripDetailModel


        // --- Save CustomTripModel to DB ---
        CustomTripController customTripController = new CustomTripController();
        int customTripId = customTripController.saveCustomTrip(customTrip);

        if (customTripId != -1) {
            customTrip.setId(customTripId); // Update ID in model after saving

            // --- Create ReservasiModel for this Custom Trip ---
            ReservasiModel reservasi = new ReservasiModel();
            reservasi.setUserId(Session.currentUser.getId());
            reservasi.setTripType("custom_trip");
            reservasi.setTripId(customTripId);
            reservasi.setKodeReservasi("CTRIP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            reservasi.setTanggalReservasi(LocalDate.now());
            reservasi.setStatus("dipesan");

            ReservasiController reservasiController = new ReservasiController();
            int reservasiId = reservasiController.buatReservasi(reservasi);

            if (reservasiId != -1) {
                // Simpan penumpang
                List<String> collectedPassengerNames = new ArrayList<>();
                collectedPassengerNames.add(txtNamaPenumpang1.getText().trim()); // Assuming txtJumlahPeserta correctly controls active fields
                if (numberOfParticipants >= 2) collectedPassengerNames.add(txtNamaPenumpang2.getText().trim());
                if (numberOfParticipants >= 3) collectedPassengerNames.add(txtNamaPenumpang3.getText().trim());
                
                boolean allPassengersSaved = true;
                for (String penumpangNama : collectedPassengerNames) {
                    if (!reservasiController.tambahPenumpang(reservasiId, penumpangNama)) {
                        allPassengersSaved = false;
                    }
                }

                if (!allPassengersSaved) {
                    JOptionPane.showMessageDialog(this, "Beberapa data penumpang gagal disimpan.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }

                JOptionPane.showMessageDialog(this, "Custom Trip Anda telah dibuat dan siap untuk pembayaran. Mengarahkan ke halaman pembayaran.", "Custom Trip Siap", JOptionPane.INFORMATION_MESSAGE);
                
                String namaKontak = Session.currentUser.getNamaLengkap();
                String emailKontak = Session.currentUser.getEmail();
                String teleponKontak = Session.currentUser.getNomorTelepon();
                
                mainAppFrame.showPaymentPanel(reservasiId, namaKontak, emailKontak, teleponKontak, collectedPassengerNames);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat reservasi untuk Custom Trip Anda. Silakan coba lagi.", "Error Reservasi", JOptionPane.ERROR_MESSAGE);
                customTripController.deleteCustomTrip(customTripId); // Clean up custom trip if reservation failed
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan Custom Trip Anda. Silakan coba lagi.", "Error Penyimpanan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
