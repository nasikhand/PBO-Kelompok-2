package managementtrevel.CustomTripBuilder;

import Asset.AppTheme; // Impor AppTheme Anda
import managementtrevel.MainAppFrame; // Impor MainAppFrame

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;


public class PanelAccommodationStep extends JPanel {

    private MainAppFrame mainAppFrame;

    private JPanel panelBuildSteps;
    private JPanel panelCustomTripMain;
    private JSplitPane splitPaneContent;
    private JPanel panelLeftContent;
    private JPanel panelRightContent;

    private JLabel lblBuildStepsTitle;
    private JLabel lblStep1Destinasi;
    private JLabel lblStep2Tanggal;
    private JLabel lblStep3Transport;
    private JLabel lblStep4Akomodasi;
    private JLabel lblStep5Kegiatan;
    private JLabel lblStep6Final;

    private JLabel lblCustomTripBuilderTitle;
    private JButton btnSaveTrip;

    private JPanel panelSelectAccommodation;
    private JLabel lblAccommodationName;
    private JTextField txtAccommodationName;
    private JLabel lblRoomType;
    private JTextField txtRoomType;
    private JLabel lblAccommodationNotes;
    private JTextArea txtAccommodationNotes;
    private JScrollPane scrollPaneAccommodationNotes;

    private JPanel panelSuggestAccommodation;
    private JLabel lblSuggestAccommodationInfo;

    private JPanel panelAccommodationOption;
    private JLabel lblAccommodationOptionInfo;

    private JPanel panelTripSummary;
    private JLabel lblSummaryStartDateDisplay;
    private JLabel lblSummaryEndDateDisplay;
    private JLabel lblSummaryTransportModeDisplay;
    private JLabel lblSummaryAccommodationDisplay;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummary;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasiDisplay;
    private final List<String> currentDestinations;
    private final String currentStartDate;
    private final String currentEndDate;
    private final String currentTransportMode;
    private final String currentTransportDetails;
    private double currentInitialEstimatedCost;

    private String selectedAccommodationName;
    private String selectedRoomType;
    private String accommodationNotes;


    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;
    private JPanel panelSummaryDetails;


    public PanelAccommodationStep(MainAppFrame mainAppFrame, List<String> destinations, String startDate, String endDate, String transportMode, String transportDetails, double initialEstimatedCost) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentInitialEstimatedCost = initialEstimatedCost;
        
        this.listModelDestinasiDisplay = new DefaultListModel<>();
        
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
        lblStep2Tanggal = new JLabel();   lblStep2Tanggal.setBorder(stepLabelBorder);   lblStep2Tanggal.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep3Transport = new JLabel(); lblStep3Transport.setBorder(stepLabelBorder); lblStep3Transport.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep4Akomodasi = new JLabel(); lblStep4Akomodasi.setBorder(stepLabelBorder); lblStep4Akomodasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep5Kegiatan = new JLabel();  lblStep5Kegiatan.setBorder(stepLabelBorder);  lblStep5Kegiatan.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep6Final = new JLabel();     lblStep6Final.setBorder(stepLabelBorder);     lblStep6Final.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelBuildSteps.add(lblStep1Destinasi);
        panelBuildSteps.add(lblStep2Tanggal);
        panelBuildSteps.add(lblStep3Transport);
        panelBuildSteps.add(lblStep4Akomodasi);
        panelBuildSteps.add(lblStep5Kegiatan);
        panelBuildSteps.add(lblStep6Final);
        panelBuildSteps.add(Box.createVerticalGlue()); 

        add(panelBuildSteps, BorderLayout.WEST);

        panelCustomTripMain = new JPanel(new BorderLayout(10, 10));
        panelCustomTripMain.setBorder(new EmptyBorder(0, 10, 0, 0));

        this.panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Pilih Akomodasi");
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

        // Panel Select Accommodation
        panelSelectAccommodation = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAcc = new GridBagConstraints();
        gbcAcc.insets = new Insets(5, 5, 5, 5);
        gbcAcc.anchor = GridBagConstraints.WEST;

        lblAccommodationName = new JLabel("Nama Akomodasi:");
        gbcAcc.gridx = 0; gbcAcc.gridy = 0;
        panelSelectAccommodation.add(lblAccommodationName, gbcAcc);

        txtAccommodationName = new JTextField(20);
        gbcAcc.gridx = 1; gbcAcc.gridy = 0; gbcAcc.fill = GridBagConstraints.HORIZONTAL; gbcAcc.weightx = 1.0;
        panelSelectAccommodation.add(txtAccommodationName, gbcAcc);

        lblRoomType = new JLabel("Tipe Kamar/Preferensi:");
        gbcAcc.gridx = 0; gbcAcc.gridy = 1; gbcAcc.fill = GridBagConstraints.NONE; gbcAcc.weightx = 0;
        panelSelectAccommodation.add(lblRoomType, gbcAcc);

        txtRoomType = new JTextField(20);
        gbcAcc.gridx = 1; gbcAcc.gridy = 1; gbcAcc.fill = GridBagConstraints.HORIZONTAL; gbcAcc.weightx = 1.0;
        panelSelectAccommodation.add(txtRoomType, gbcAcc);
        
        lblAccommodationNotes = new JLabel("Catatan Akomodasi:");
        gbcAcc.gridx = 0; gbcAcc.gridy = 2; gbcAcc.anchor = GridBagConstraints.NORTHWEST;
        panelSelectAccommodation.add(lblAccommodationNotes, gbcAcc);

        txtAccommodationNotes = new JTextArea(4, 20);
        txtAccommodationNotes.setLineWrap(true);
        txtAccommodationNotes.setWrapStyleWord(true);
        scrollPaneAccommodationNotes = new JScrollPane(txtAccommodationNotes);
        scrollPaneAccommodationNotes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gbcAcc.gridx = 1; gbcAcc.gridy = 2; gbcAcc.fill = GridBagConstraints.BOTH; gbcAcc.weighty = 0.5;
        panelSelectAccommodation.add(scrollPaneAccommodationNotes, gbcAcc);
        
        panelSelectAccommodation.setPreferredSize(new Dimension(0, 200));
        panelLeftContent.add(panelSelectAccommodation);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelSuggestAccommodation = new JPanel(new BorderLayout());
        lblSuggestAccommodationInfo = new JLabel("Saran akomodasi akan muncul di sini...", JLabel.CENTER);
        panelSuggestAccommodation.add(lblSuggestAccommodationInfo, BorderLayout.CENTER);
        panelSuggestAccommodation.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelSuggestAccommodation);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelAccommodationOption = new JPanel(new BorderLayout());
        lblAccommodationOptionInfo = new JLabel("Opsi untuk akomodasi yang dipilih...", JLabel.CENTER);
        panelAccommodationOption.add(lblAccommodationOptionInfo, BorderLayout.CENTER);
        panelAccommodationOption.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelAccommodationOption);
        panelLeftContent.add(Box.createVerticalGlue());

        // Panel Trip Summary
        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        this.panelSummaryDetails = new JPanel();
        panelSummaryDetails.setLayout(new BoxLayout(panelSummaryDetails, BoxLayout.Y_AXIS));
        panelSummaryDetails.setBorder(new EmptyBorder(2,5,5,5));
        
        lblSummaryStartDateDisplay = new JLabel("Mulai: -");
        lblSummaryStartDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryStartDateDisplay);
        
        lblSummaryEndDateDisplay = new JLabel("Selesai: -");
        lblSummaryEndDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryEndDateDisplay);

        lblSummaryTransportModeDisplay = new JLabel("Transportasi: -");
        lblSummaryTransportModeDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryTransportModeDisplay);

        lblSummaryAccommodationDisplay = new JLabel("Akomodasi: -");
        lblSummaryAccommodationDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryAccommodationDisplay);
        
        panelSummaryDetails.add(Box.createRigidArea(new Dimension(0,5)));
        JSeparator summarySeparator = new JSeparator(SwingConstants.HORIZONTAL);
        panelSummaryDetails.add(summarySeparator);
        panelSummaryDetails.add(Box.createRigidArea(new Dimension(0,3)));

        JLabel lblDestinationsTitle = new JLabel("Destinasi:");
        lblDestinationsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblDestinationsTitle);
        panelTripSummary.add(panelSummaryDetails, BorderLayout.NORTH);

        listModelDestinasiDisplay = new DefaultListModel<>(); 
        listDestinasiSummary = new JList<>(listModelDestinasiDisplay);
        listDestinasiSummary.setToolTipText("Destinasi yang telah dipilih");
        listDestinasiSummary.setEnabled(false); 
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);
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

        JPanel panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Transportasi"); 
        btnNextStep = new JButton("Lanjut ke Kegiatan >");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST); 
        panelMainFooter.add(btnNextStep, BorderLayout.EAST); 
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

        if (panelMainHeader != null) ((JComponent) panelMainHeader).setOpaque(false);
        lblCustomTripBuilderTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblCustomTripBuilderTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnSaveTrip, "Simpan Draf Trip");

        panelLeftContent.setOpaque(false);
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);

        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;

        panelSelectAccommodation.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Akomodasi", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelSuggestAccommodation.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Saran Akomodasi",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelAccommodationOption.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Opsi Akomodasi",
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
        
        panelSelectAccommodation.setOpaque(false);
        panelSuggestAccommodation.setOpaque(false);
        panelAccommodationOption.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        if (panelSummaryDetails != null) panelSummaryDetails.setOpaque(false);


        lblAccommodationName.setFont(AppTheme.FONT_LABEL_FORM);
        lblAccommodationName.setForeground(AppTheme.TEXT_DARK);
        txtAccommodationName.setFont(AppTheme.FONT_TEXT_FIELD);
        txtAccommodationName.setBorder(AppTheme.createDefaultInputBorder());
        txtAccommodationName.setBackground(AppTheme.INPUT_BACKGROUND);
        txtAccommodationName.setForeground(AppTheme.INPUT_TEXT);
        addFocusBorderEffect(txtAccommodationName);


        lblRoomType.setFont(AppTheme.FONT_LABEL_FORM);
        lblRoomType.setForeground(AppTheme.TEXT_DARK);
        txtRoomType.setFont(AppTheme.FONT_TEXT_FIELD);
        txtRoomType.setBorder(AppTheme.createDefaultInputBorder());
        txtRoomType.setBackground(AppTheme.INPUT_BACKGROUND);
        txtRoomType.setForeground(AppTheme.INPUT_TEXT);
        addFocusBorderEffect(txtRoomType);

        lblAccommodationNotes.setFont(AppTheme.FONT_LABEL_FORM);
        lblAccommodationNotes.setForeground(AppTheme.TEXT_DARK);
        txtAccommodationNotes.setFont(AppTheme.FONT_TEXT_FIELD);
        txtAccommodationNotes.setBackground(AppTheme.INPUT_BACKGROUND);
        txtAccommodationNotes.setForeground(AppTheme.INPUT_TEXT);
        txtAccommodationNotes.setBorder(AppTheme.createDefaultInputBorder());
        txtAccommodationNotes.setMargin(new Insets(5,5,5,5));
        scrollPaneAccommodationNotes.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        
        lblSuggestAccommodationInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSuggestAccommodationInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblAccommodationOptionInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblAccommodationOptionInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        
        lblSummaryStartDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryStartDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryEndDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryEndDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryTransportModeDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryTransportModeDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        
        if (panelSummaryDetails.getComponent(0) instanceof JLabel) { 
             ((JLabel)panelSummaryDetails.getComponent(0)).setFont(AppTheme.FONT_LABEL_FORM);
             ((JLabel)panelSummaryDetails.getComponent(0)).setForeground(AppTheme.TEXT_DARK);
        }
        if (panelSummaryDetails.getComponent(4) instanceof JLabel) { // "Destinations:"
             ((JLabel)panelSummaryDetails.getComponent(4)).setFont(AppTheme.FONT_LABEL_FORM);
             ((JLabel)panelSummaryDetails.getComponent(4)).setForeground(AppTheme.TEXT_DARK);
             ((JLabel)panelSummaryDetails.getComponent(4)).setBorder(new EmptyBorder(3,0,3,0));
        }


        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummary.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        styleSecondaryButton(btnPrevStep, "< Kembali");
        stylePrimaryButton(btnNextStep, "Lanjut ke Kegiatan >");
        
        if (panelMainHeader != null) ((JComponent) panelMainHeader).setOpaque(false);
        if (panelMainFooter != null) ((JComponent) panelMainFooter).setOpaque(false);
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
    
    private void addFocusBorderEffect(JTextField textField) {
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
    
    private void addFocusBorderEffect(JTextArea textArea) { // Overload for JTextArea
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


    private void setupLogicAndVisuals() {
        updateBuildStepLabels(4); // Langkah aktif adalah 4 (Akomodasi)
        
        // Populate summary details from previous steps
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        lblSummaryStartDateDisplay.setText("Mulai: " + (currentStartDate != null ? currentStartDate : "-"));
        lblSummaryEndDateDisplay.setText("Selesai: " + (currentEndDate != null ? currentEndDate : "-"));
        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "-");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (" + currentTransportDetails + ")";
        }
        lblSummaryTransportModeDisplay.setText("Transportasi: " + transportInfo);
        
        // Initial state for text fields
        addFocusBorderEffect(txtAccommodationName); // Apply focus effect
        addFocusBorderEffect(txtRoomType); // Apply focus effect
        addFocusBorderEffect(txtAccommodationNotes); // Apply focus effect

        // Listeners for Save/Prev/Next buttons
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);

        updateEstimatedCost(); // Calculate initial cost based on inherited cost and current step
    }
    
    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {
            lblStep1Destinasi, lblStep2Tanggal, lblStep3Transport,
            lblStep4Akomodasi, lblStep5Kegiatan, lblStep6Final
        };
        String[] stepTexts = {
            "1. Destinasi", "2. Tanggal", "3. Transportasi",
            "4. Akomodasi", "5. Kegiatan", "6. Finalisasi"
        };

        for (int i = 0; i < stepLabels.length; i++) {
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? ACTIVE_STEP_ICON : INACTIVE_STEP_ICON) + stepTexts[i]);
                stepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                stepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }

    private void updateEstimatedCost() {
        double currentCost = currentInitialEstimatedCost; // Start with cost from previous step

        // Example: Add flat cost for accommodation if name is entered
        if (txtAccommodationName != null && !txtAccommodationName.getText().trim().isEmpty()) {
            currentCost += 500000; // Example: Flat cost for accommodation
        }

        // Add more complex logic based on room type, notes, etc.
        // if (selectedRoomType != null && selectedRoomType.equals("Deluxe")) { currentCost += 200000; }

        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentCost));

        lblSummaryAccommodationDisplay.setText("Akomodasi: " + txtAccommodationName.getText().trim() + " (Kamar: " + txtRoomType.getText().trim() + ")");
    }


    private void btnSaveTripActionPerformed(ActionEvent evt) {
        selectedAccommodationName = txtAccommodationName.getText().trim();
        selectedRoomType = txtRoomType.getText().trim();
        accommodationNotes = txtAccommodationNotes.getText().trim();
        
        String message = String.format(
            "Draf Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s s/d %s\nTransportasi: %s (%s)\nAkomodasi: %s (%s) - Catatan: %s\nEstimasi Biaya: %s",
            currentDestinations, 
            currentStartDate, 
            currentEndDate,
            currentTransportMode,
            currentTransportDetails != null && !currentTransportDetails.isEmpty() ? currentTransportDetails : "Tidak ada detail",
            selectedAccommodationName.isEmpty() ? "Tidak ditentukan" : selectedAccommodationName,
            selectedRoomType.isEmpty() ? "Tidak ada preferensi" : selectedRoomType,
            accommodationNotes.isEmpty() ? "Tidak ada catatan" : accommodationNotes,
            AppTheme.formatCurrency(currentInitialEstimatedCost)
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Navigasi kembali ke PanelTransportStep, meneruskan data yang relevan
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRANSPORT_STEP, currentDestinations, currentStartDate, currentEndDate, currentTransportMode, currentTransportDetails, currentInitialEstimatedCost);
        } else {
            System.err.println("MainAppFrame reference is null in PanelAccommodationStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        selectedAccommodationName = txtAccommodationName.getText().trim();
        selectedRoomType = txtRoomType.getText().trim();
        accommodationNotes = txtAccommodationNotes.getText().trim();

        // Validasi sederhana, bisa ditambahkan
        if (selectedAccommodationName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nama akomodasi atau lewati jika tidak ada.", "Info Akomodasi", JOptionPane.INFORMATION_MESSAGE);
            // Tidak menghentikan, user bisa memilih untuk tidak mengisi
        }
        
        double currentTotalEstimatedCost = 0.0;
        try {
                currentTotalEstimatedCost = currentInitialEstimatedCost; // Mulai dari biaya awal
            if (txtAccommodationName != null && !txtAccommodationName.getText().trim().isEmpty()) {
                currentTotalEstimatedCost += 500000; // Logika perhitungan akomodasi
        }
        } catch (Exception e) { // Catch ParseException jika masih mem-parse dari label
            System.err.println("Error parsing estimated cost from label in AccommodationStep: " + e.getMessage());
        }

        if (mainAppFrame != null) {
            // Navigasi ke PanelActivityStep
            mainAppFrame.showPanel(MainAppFrame.PANEL_ACTIVITY_STEP, 
                                   currentDestinations, 
                                   currentStartDate, 
                                   currentEndDate, 
                                   currentTransportMode, 
                                   currentTransportDetails, 
                                   selectedAccommodationName, 
                                   selectedRoomType, 
                                   accommodationNotes,
                                   currentTotalEstimatedCost); // Pass cumulative cost
        } else {
            System.err.println("MainAppFrame reference is null in PanelAccommodationStep (Next).");
        }
    }
}
