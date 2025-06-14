package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.DestinasiController; // Import ini
import model.CustomTripDetailModel;
import model.DestinasiModel; // Import ini
import model.TransportOption;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat; // For SimpleDateFormat

public class PanelTransportCostStep extends JPanel {

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

    private JPanel panelTransportCostSelection;
    private JLabel lblTransportMode;
    private JComboBox<String> cmbTransportMode;
    private JLabel lblTransportDetails;
    private JTextField txtTransportDetails;

    private JPanel panelTripSummary;
    private JLabel lblSummaryDestinationsDisplay;
    private JLabel lblSummaryDatesDisplay;
    private JLabel lblSummaryTransportDisplay;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummary;
    private List<JComboBox<TransportOption>> transportOptionCombos;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasiSummaryDisplay;
    private final List<String> currentDestinations;
    private final List<CustomTripDetailModel> itineraryDetails;
    private double currentInitialEstimatedCost;
    private String selectedTransportMode;
    private String transportDetailsNotes;
    

    private DestinasiController destinasiController; // <--- DEKLARASI INI DITAMBAHKAN

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";
    private final String PLACEHOLDER_TEXT_DETAILS = "Contoh: Nama Maskapai/Nomor Kereta/Tipe Bus";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;


    public PanelTransportCostStep(MainAppFrame mainAppFrame, List<String> destinations, List<CustomTripDetailModel> itineraryDetails, double initialEstimatedCost) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.itineraryDetails = itineraryDetails != null ? new ArrayList<>(itineraryDetails) : new ArrayList<>();
        this.currentInitialEstimatedCost = initialEstimatedCost;
        this.listModelDestinasiSummaryDisplay = new DefaultListModel<>();
        this.destinasiController = new DestinasiController(); // <--- INISIALISASI DI SINI

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
        lblStep5Final = new JLabel(); lblStep5Final.setBorder(stepLabelBorder); lblStep5Final.setAlignmentX(Component.LEFT_ALIGNMENT);
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Biaya Transport");
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
        GridBagConstraints gbcTransport = new GridBagConstraints();
        gbcTransport.insets = new Insets(5, 10, 5, 10);
        gbcTransport.anchor = GridBagConstraints.WEST;
        gbcTransport.fill = GridBagConstraints.HORIZONTAL;
        lblTransportMode = new JLabel("Mode Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 0;
        String[] transportModes = {"Pilih Mode", "Pesawat", "Kereta", "Bus", "Mobil Pribadi", "Lainnya"};
        cmbTransportMode = new JComboBox<>(transportModes);
        gbcTransport.gridx = 1; gbcTransport.gridy = 0; gbcTransport.weightx = 1.0;
        lblTransportDetails = new JLabel("Detail Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 1; gbcTransport.weightx = 0;
        txtTransportDetails = new JTextField(20);
        gbcTransport.gridx = 1; gbcTransport.gridy = 1; gbcTransport.weightx = 1.0;
        gbcTransport.gridx = 0; gbcTransport.gridy = 2; gbcTransport.gridwidth = 2; gbcTransport.weighty = 1.0;
        
        // This is the only change in initializeUI: calling the styled component creation.
        panelLeftContent.add(createPerDestinationCostPanel());
        
        panelLeftContent.add(Box.createVerticalGlue());
        panelRightContent = new JPanel();
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0));
        panelTripSummary = new JPanel(new BorderLayout(5,5));
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
        lblSummaryDatesDisplay = new JLabel("Tanggal Trip Keseluruhan: -");
        summaryContentTop.add(lblSummaryDatesDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));
        lblSummaryTransportDisplay = new JLabel("Transportasi Dipilih: -");
        summaryContentTop.add(lblSummaryTransportDisplay);
        summaryContentTop.add(Box.createRigidArea(new Dimension(0, 10)));
        panelTripSummary.add(summaryContentTop, BorderLayout.NORTH);
        panelRightContent.add(panelTripSummary);
        panelRightContent.add(Box.createRigidArea(new Dimension(0,10)));
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
        btnPrevStep = new JButton("< Kembali ke Itinerary");
        btnNextStep = new JButton("Lanjut ke Peserta >");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnNextStep, BorderLayout.EAST);
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0));
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);
        this.add(panelMainHeader, BorderLayout.NORTH);
        this.add(splitPaneContent, BorderLayout.CENTER);
        this.add(panelMainFooter, BorderLayout.SOUTH);
    }

    // =================================================================================
    // == STYLED COMPONENT CREATION - THIS IS WHERE THE DESIGN MAGIC HAPPENS
    // =================================================================================
    private JScrollPane createPerDestinationCostPanel() {
        JPanel mainListPanel = new JPanel();
        mainListPanel.setLayout(new BoxLayout(mainListPanel, BoxLayout.Y_AXIS));
        mainListPanel.setBackground(Color.WHITE); // White background for the container

        this.transportOptionCombos = new ArrayList<>();

        TransportOption[] options = {
            new TransportOption("- Pilih Transportasi -", 0),
            new TransportOption("Mobil Pribadi (Sewa)", 350000),
            new TransportOption("Taksi Online (Mobil)", 150000),
            new TransportOption("Taksi Online (Motor)", 50000),
            new TransportOption("Bus Umum / Angkot", 15000),
            new TransportOption("Jalan Kaki", 0)
        };

        for (int i = 0; i < itineraryDetails.size(); i++) {
            CustomTripDetailModel detail = itineraryDetails.get(i);
            DestinasiModel destModel = destinasiController.getDestinasiById(detail.getDestinasiId());

            // --- Create a styled panel for each row ---
            JPanel rowPanel = new JPanel(new GridBagLayout());
            rowPanel.setBackground(Color.WHITE);
            // Add a clean bottom border to separate rows
            rowPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER_COLOR));

            GridBagConstraints gbcRow = new GridBagConstraints();
            gbcRow.insets = new Insets(10, 5, 10, 5); // Padding for each row
            gbcRow.anchor = GridBagConstraints.WEST;

            // --- Destination Info (Name and Date) ---
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            String destinasiName = (destModel != null) ? destModel.getNamaDestinasi() : "Destinasi " + (i + 1);
            JLabel destinationNameLabel = new JLabel("Transportasi ke " + destinasiName);
            destinationNameLabel.setFont(AppTheme.FONT_SUBTITLE);
            destinationNameLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String dateString = "Kunjungan: " + detail.getTanggalKunjungan().format(dtf);
            JLabel dateLabel = new JLabel(dateString);
            dateLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            dateLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            
            infoPanel.add(destinationNameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            infoPanel.add(dateLabel);

            gbcRow.gridx = 0;
            gbcRow.gridy = 0;
            gbcRow.weightx = 1.0; // Allow info to take up space
            gbcRow.fill = GridBagConstraints.HORIZONTAL;
            rowPanel.add(infoPanel, gbcRow);

            // --- ComboBox for transport selection ---
            JComboBox<TransportOption> optionCombo = new JComboBox<>(options);
            optionCombo.setFont(AppTheme.FONT_TEXT_FIELD);
            optionCombo.setPreferredSize(new Dimension(220, 30));
            optionCombo.addItemListener(e -> {
                if (e.getItemSelectable() == e.getSource()) {
                    updateEstimatedCost();
                    updateNextStepButtonState();
                }
            });

            gbcRow.gridx = 1;
            gbcRow.weightx = 0; // Don't allow combo box to grow
            gbcRow.fill = GridBagConstraints.NONE;
            rowPanel.add(optionCombo, gbcRow);
            
            // Add the fully styled row to the main list
            mainListPanel.add(rowPanel);
            transportOptionCombos.add(optionCombo);
        }

        // --- Final container with TitledBorder ---
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createTitledBorder(null, "Rincian Biaya Transportasi",
                TitledBorder.LEADING, TitledBorder.TOP, AppTheme.FONT_TITLE_MEDIUM, AppTheme.PRIMARY_BLUE_DARK));
        container.add(mainListPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
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

//        panelTransportCostSelection.setBorder(BorderFactory.createTitledBorder(
//            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Transportasi",
//            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
//            titledBorderFont, titledBorderColor));
        
        panelTripSummary.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Trip",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            titledBorderFont, titledBorderColor));
        
//        panelTransportCostSelection.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);

        lblTransportMode.setFont(AppTheme.FONT_LABEL_FORM);
        lblTransportMode.setForeground(AppTheme.TEXT_DARK);
        cmbTransportMode.setFont(AppTheme.FONT_TEXT_FIELD);
        cmbTransportMode.setBackground(AppTheme.INPUT_BACKGROUND);
        cmbTransportMode.setForeground(AppTheme.INPUT_TEXT);
        cmbTransportMode.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1));

        lblTransportDetails.setFont(AppTheme.FONT_LABEL_FORM);
        lblTransportDetails.setForeground(AppTheme.TEXT_DARK);
        styleInputField(txtTransportDetails, PLACEHOLDER_TEXT_DETAILS);
        
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


        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        styleSecondaryButton(btnPrevStep, "< Kembali ke Itinerary");
        stylePrimaryButton(btnNextStep, "Lanjut ke Peserta >");
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
        updateBuildStepLabels(3); // Active step is 3 (Transport Cost)
        
        populateSummaryDisplay();

        styleInputField(txtTransportDetails, PLACEHOLDER_TEXT_DETAILS);

        cmbTransportMode.addItemListener(this::cmbTransportModeItemStateChanged);
        txtTransportDetails.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateEstimatedCost();
            }
        });

        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);

        updateEstimatedCost();
        updateNextStepButtonState();
    }

    private void populateSummaryDisplay() {
        listModelDestinasiSummaryDisplay.clear();
        LocalDate overallStartDate = null;
        LocalDate overallEndDate = null;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMMyyyy");

        for (int i = 0; i < itineraryDetails.size(); i++) {
            CustomTripDetailModel detail = itineraryDetails.get(i);
            String destinasiName = "";
            DestinasiModel destModel = destinasiController.getDestinasiById(detail.getDestinasiId());
            if(destModel != null) {
                destinasiName = destModel.getNamaDestinasi();
            }

            LocalDate visitDate = detail.getTanggalKunjungan();
            String dateString = (visitDate != null) ? visitDate.format(dtf) : "Belum diatur";

            listModelDestinasiSummaryDisplay.addElement((i + 1) + ". " + destinasiName + " (" + dateString + ")");

            if (visitDate != null) {
                if (overallStartDate == null || visitDate.isBefore(overallStartDate)) {
                    overallStartDate = visitDate;
                }
                if (overallEndDate == null || visitDate.isAfter(overallEndDate)) {
                    overallEndDate = visitDate;
                }
            }
        }
        
        String startDateFormatted = (overallStartDate != null) ? overallStartDate.format(dtf) : "-";
        String endDateFormatted = (overallEndDate != null) ? overallEndDate.format(dtf) : "-";
        lblSummaryDatesDisplay.setText("Tanggal Trip Keseluruhan: " + startDateFormatted + " s/d " + endDateFormatted);

        lblSummaryDestinationsDisplay.setText("Destinasi Terpilih:");
    }

    private void updateNextStepButtonState() {
        boolean allOptionsSelected = true;
        if (transportOptionCombos == null || transportOptionCombos.isEmpty()) {
            allOptionsSelected = false;
        } else {
            for (JComboBox<TransportOption> combo : transportOptionCombos) {
                // The button is enabled only if every combo box does NOT have the first item ("- Pilih -") selected.
                if (combo.getSelectedIndex() == 0) {
                    allOptionsSelected = false;
                    break; // Found one that isn't selected, no need to check further.
                }
            }
        }
        btnNextStep.setEnabled(allOptionsSelected);
    }

    private void cmbTransportModeItemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            updateEstimatedCost();
            updateNextStepButtonState();
        }
    }

    private void updateEstimatedCost() {
        double baseCost = this.currentInitialEstimatedCost;
        double transportTotal = 0;

        // Calculate total from the selected options in our combo boxes
        if (transportOptionCombos != null) { // Check if list is initialized
            for (JComboBox<TransportOption> combo : transportOptionCombos) {
                TransportOption selected = (TransportOption) combo.getSelectedItem();
                if (selected != null) {
                    transportTotal += selected.getCost();
                }
            }
        }

        double finalTotalCost = baseCost + transportTotal;
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(finalTotalCost));
    }

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        selectedTransportMode = (String) cmbTransportMode.getSelectedItem();
        transportDetailsNotes = txtTransportDetails.getText().trim();

        if (cmbTransportMode.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih mode transportasi sebelum menyimpan draf.", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = String.format(
            "Draf Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s\nTransportasi: %s (%s)\nEstimasi Biaya: %s",
            currentDestinations, 
            lblSummaryDatesDisplay.getText(),
            selectedTransportMode, transportDetailsNotes.isEmpty() ? "-" : transportDetailsNotes,
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Draf Trip Berhasil (Simulasi)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_ITINERARY_STEP, currentDestinations, itineraryDetails, currentInitialEstimatedCost);
        } else {
            System.err.println("MainAppFrame reference is null in PanelTransportCostStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        // These are for the overall trip, can still be useful
        selectedTransportMode = (String) cmbTransportMode.getSelectedItem();
        transportDetailsNotes = txtTransportDetails.getText().trim();
        
        double totalTransportCost = 0;

        // --- CRUCIAL PART: Update the model objects ---
        for (int i = 0; i < itineraryDetails.size(); i++) {
            JComboBox<TransportOption> combo = transportOptionCombos.get(i);
            TransportOption selected = (TransportOption) combo.getSelectedItem();

            if (selected == null || selected.getCost() == 0 && !selected.getName().equals("Jalan Kaki")) {
                // This checks if a meaningful option was selected
                if(combo.getSelectedIndex() == 0){
                    JOptionPane.showMessageDialog(this, "Silakan pilih opsi transportasi untuk semua destinasi.", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
                    return; // Stop processing
                }
            }
            
            // UPDATE THE MODEL with the cost from the selected option
            itineraryDetails.get(i).setBiayaTransport(selected.getCost());
            totalTransportCost += selected.getCost();
        }

        // Recalculate the final total cost reliably
        double finalTotalCost = this.currentInitialEstimatedCost + totalTransportCost;

        // Proceed to the next step
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PARTICIPANTS_STEP,
                    currentDestinations,
                    itineraryDetails, // This list now contains the correct transport costs!
                    finalTotalCost,
                    selectedTransportMode,
                    transportDetailsNotes);
        } else {
            System.err.println("MainAppFrame reference is null in PanelTransportCostStep (Next).");
        }
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
}
