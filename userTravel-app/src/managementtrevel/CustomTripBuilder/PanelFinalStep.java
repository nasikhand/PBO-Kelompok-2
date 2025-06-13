package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.ReservasiController;
import controller.CustomTripController;
import model.ReservasiModel;
import model.CustomTripModel;
import model.Session;
import model.UserModel;

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
    private JLabel lblStep2Tanggal;
    private JLabel lblStep3Transport;
    private JLabel lblStep4Akomodasi;
    private JLabel lblStep5Kegiatan;
    private JLabel lblStep6Final;

    private JLabel lblCustomTripBuilderTitle;
    private JButton btnSaveTrip;

    private JLabel lblFinalReviewMessage;
    private JTextArea txtAreaFinalNotes;
    private JScrollPane scrollPaneFinalNotes;

    private JPanel panelTripSummaryFull;
    private JScrollPane scrollPaneTripSummaryFull;

    private JLabel lblDestinationsSummaryDisplay;
    private JList<String> listDestinationsDisplay;
    private JScrollPane scrollPaneDestinationsDisplay;
    private JLabel lblDatesSummaryDisplay;
    private JLabel lblTransportSummaryDisplay;
    private JLabel lblAccommodationSummaryDisplay;
    private JLabel lblActivitiesSummaryDisplay;
    private JList<String> listActivitiesDisplay;
    private JScrollPane scrollPaneActivitiesDisplay;
    private JPanel panelPassengerInput;
    private JTextField txtJumlahPeserta;
    private JTextField txtNamaPenumpang1;
    private JTextField txtNamaPenumpang2;
    private JTextField txtNamaPenumpang3;
    private JLabel lblJumlahPesertaTitle;
    private JLabel lblPenumpang1Title;
    private JLabel lblPenumpang2Title;
    private JLabel lblPenumpang3Title;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnFinishTrip;

    private final DefaultListModel<String> listModelDestinasiDisplay;
    private final DefaultListModel<String> listModelActivitiesDisplay;

    private final List<String> currentDestinations;
    private final String currentStartDate;
    private final String currentEndDate;
    private final String currentTransportMode;
    private final String currentTransportDetails;
    private final String currentAccommodationName;
    private final String currentRoomType;
    private final String currentAccommodationNotes;
    private final List<String> currentActivities;
    private double currentInitialEstimatedCost;

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JPanel panelMainFooter;
    private JPanel panelMainHeader;

    public PanelFinalStep(MainAppFrame mainAppFrame, List<String> destinations, String startDate, String endDate, 
                          String transportMode, String transportDetails,
                          String accommodationName, String roomType, String accommodationNotes,
                          List<String> activities, double initialEstimatedCost) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        this.currentActivities = activities != null ? new ArrayList<>(activities) : new ArrayList<>();
        this.currentInitialEstimatedCost = initialEstimatedCost;
        
        this.listModelDestinasiDisplay = new DefaultListModel<>();
        this.listModelActivitiesDisplay = new DefaultListModel<>();

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
        panelLeftContent.add(Box.createVerticalGlue());
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelPassengerInput = new JPanel(new GridBagLayout());
        panelPassengerInput.setOpaque(false); // Make transparent
        panelPassengerInput.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Detail Peserta",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
            AppTheme.FONT_SUBTITLE, AppTheme.PRIMARY_BLUE_DARK));

        GridBagConstraints gbcPass = new GridBagConstraints();
        gbcPass.insets = new Insets(5, 5, 5, 5);
        gbcPass.anchor = GridBagConstraints.WEST;
        gbcPass.fill = GridBagConstraints.HORIZONTAL;

        lblJumlahPesertaTitle = new JLabel("Jumlah Peserta (maks 3):");
        gbcPass.gridx = 0; gbcPass.gridy = 0; gbcPass.weightx = 0;
        panelPassengerInput.add(lblJumlahPesertaTitle, gbcPass);
        txtJumlahPeserta = new JTextField(5);
        gbcPass.gridx = 1; gbcPass.gridy = 0; gbcPass.weightx = 1.0;
        panelPassengerInput.add(txtJumlahPeserta, gbcPass);

        lblPenumpang1Title = new JLabel("Nama Peserta 1:");
        gbcPass.gridx = 0; gbcPass.gridy = 1;
        panelPassengerInput.add(lblPenumpang1Title, gbcPass);
        txtNamaPenumpang1 = new JTextField(20);
        gbcPass.gridx = 1; gbcPass.gridy = 1;
        panelPassengerInput.add(txtNamaPenumpang1, gbcPass);

        lblPenumpang2Title = new JLabel("Nama Peserta 2:");
        gbcPass.gridx = 0; gbcPass.gridy = 2;
        panelPassengerInput.add(lblPenumpang2Title, gbcPass);
        txtNamaPenumpang2 = new JTextField(20);
        gbcPass.gridx = 1; gbcPass.gridy = 2;
        panelPassengerInput.add(txtNamaPenumpang2, gbcPass);

        lblPenumpang3Title = new JLabel("Nama Peserta 3:");
        gbcPass.gridx = 0; gbcPass.gridy = 3;
        panelPassengerInput.add(lblPenumpang3Title, gbcPass);
        txtNamaPenumpang3 = new JTextField(20);
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
        btnPrevStep = new JButton("< Kembali ke Kegiatan"); 
        btnFinishTrip = new JButton("Selesaikan & Pesan Trip");
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST);
        panelMainFooter.add(btnFinishTrip, BorderLayout.EAST);
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0));
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        add(panelCustomTripMain, BorderLayout.CENTER);
    }

    /**
     * Applies the defined AppTheme styles to all UI components.
     */
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

        if (panelPassengerInput != null) {
            panelPassengerInput.setOpaque(false);
            panelPassengerInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Detail Peserta",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                AppTheme.FONT_SUBTITLE, AppTheme.PRIMARY_BLUE_DARK));
        }
        
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
                styleInputField(textField, ""); // Gunakan placeholder jika Anda mau
                addFocusBorderEffect(textField);
            }
        }
        if (txtNamaPenumpang1 != null) txtNamaPenumpang1.setEnabled(false);
        if (txtNamaPenumpang2 != null) txtNamaPenumpang2.setEnabled(false);
        if (txtNamaPenumpang3 != null) txtNamaPenumpang3.setEnabled(false);

        panelLeftContent.setOpaque(false);
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);

        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;

        panelLeftContent.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Catatan Akhir & Konfirmasi",
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

        lblActivitiesSummaryDisplay.setFont(summaryHeaderFont);
        lblActivitiesSummaryDisplay.setForeground(summaryHeaderColor);
        listActivitiesDisplay.setFont(summaryDetailFont);
        listActivitiesDisplay.setBackground(new Color(0,0,0,0));
        listActivitiesDisplay.setForeground(summaryDetailColor);
        scrollPaneActivitiesDisplay.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneActivitiesDisplay.getViewport().setOpaque(false);


        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_LARGE);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);
        
        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        styleSecondaryButton(btnPrevStep, "< Kembali ke Kegiatan");
        stylePrimaryButton(btnFinishTrip, "Selesaikan & Pesan Trip");
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
    }

    // Helper method to add focus border effect (for JTextField)
    private void addFocusBorderEffect(JTextField textField) {
        if (textField == null) return;
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
            }
            public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }

    // Helper method to add focus border effect (for JTextArea)
    private void addFocusBorderEffect(JTextArea textArea) {
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
    
    /**
     * Sets up the logic and populates initial visuals based on constructor data.
     */
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(6); // Set step 6 (Final) as active

        // Populate destination list
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        // Populate activities list
        if (currentActivities != null) {
            for (String activity : currentActivities) {
                listModelActivitiesDisplay.addElement(activity);
            }
        }
        
        // Populate summary labels
        lblDatesSummaryDisplay.setText(String.format("<html><b>Tanggal:</b> %s s/d %s</html>", 
            (currentStartDate != null ? currentStartDate : "-"), 
            (currentEndDate != null ? currentEndDate : "-")));

        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "-");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (<i>" + currentTransportDetails + "</i>)";
        }
        lblTransportSummaryDisplay.setText("<html><b>Transportasi:</b> " + transportInfo + "</html>");
        
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "-");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (<i>Kamar: " + currentRoomType + "</i>)";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - <i>Catatan: " + currentAccommodationNotes + "</i>";
        }
        lblAccommodationSummaryDisplay.setText("<html><b>Akomodasi:</b> " + accommodationInfo + "</html>");

        if(listModelActivitiesDisplay.isEmpty()){
            listModelActivitiesDisplay.addElement("Tidak ada kegiatan spesifik yang direncanakan.");
        }
        lblActivitiesSummaryDisplay.setText("<html><b>Kegiatan:</b></html>");

        if (txtJumlahPeserta != null) {
            txtJumlahPeserta.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                private void updatePassengerFields() {
                    try {
                        int jumlah = Integer.parseInt(txtJumlahPeserta.getText().trim());
                        if (jumlah < 0) {
                            txtJumlahPeserta.setText("0"); // Minimum 0
                            jumlah = 0;
                        } else if (jumlah > 3) {
                            JOptionPane.showMessageDialog(PanelFinalStep.this, "Jumlah peserta maksimal 3 orang.", "Batas Peserta", JOptionPane.WARNING_MESSAGE);
                            txtJumlahPeserta.setText("3"); // Set to max 3
                            jumlah = 3;
                        }
                        txtNamaPenumpang1.setEnabled(jumlah >= 1);
                        txtNamaPenumpang2.setEnabled(jumlah >= 2);
                        txtNamaPenumpang3.setEnabled(jumlah >= 3);

                        if (jumlah < 1) txtNamaPenumpang1.setText("");
                        if (jumlah < 2) txtNamaPenumpang2.setText("");
                        if (jumlah < 3) txtNamaPenumpang3.setText("");

                    } catch (NumberFormatException e) {
                        txtNamaPenumpang1.setEnabled(false);
                        txtNamaPenumpang2.setEnabled(false);
                        txtNamaPenumpang3.setEnabled(false);
                    }
                }
                @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { updatePassengerFields(); }
                @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { updatePassengerFields(); }
                @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { updatePassengerFields(); }
            });
        }

        // Calculate and display estimated price
        // In FinalStep, this mostly just reflects the cumulative cost from previous steps.
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentInitialEstimatedCost));
        
        // Setup action listeners for buttons
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnFinishTrip.addActionListener(this::btnFinishTripActionPerformed);
    }
    
    /**
     * Placeholder method to calculate estimated custom trip price.
     * In FinalStep, this should mostly just reflect the cumulative cost from previous steps.
     * @return The estimated total price (cumulative from previous steps).
     */
    private double calculateEstimatedCustomTripPrice() {
        return currentInitialEstimatedCost; // Final step mostly just reflects the cumulative cost
    }

    /**
     * Updates the visual state of the build step labels in the sidebar.
     * @param activeStep The current active step number (1-6).
     */
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
    
    /**
     * Action performed when the "Simpan Draf Trip" button is clicked.
     * This simulates saving the current custom trip configuration.
     */
    private void btnSaveTripActionPerformed(ActionEvent evt) {
        String finalNotes = txtAreaFinalNotes.getText().trim();
        
        int jumlahPesertaDraf = 0;
        List<String> penumpangListDraf = new ArrayList<>();
        try {
            if (!txtJumlahPeserta.getText().trim().isEmpty()) {
                jumlahPesertaDraf = Integer.parseInt(txtJumlahPeserta.getText().trim());
            }
            if (jumlahPesertaDraf < 0 || jumlahPesertaDraf > 3) {
                JOptionPane.showMessageDialog(this, "Jumlah peserta harus antara 0 dan 3.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (jumlahPesertaDraf >= 1) penumpangListDraf.add(txtNamaPenumpang1.getText().trim());
            if (jumlahPesertaDraf >= 2) penumpangListDraf.add(txtNamaPenumpang2.getText().trim());
            if (jumlahPesertaDraf >= 3) penumpangListDraf.add(txtNamaPenumpang3.getText().trim());

            for (int i = 0; i < jumlahPesertaDraf; i++) {
                if (penumpangListDraf.get(i).isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nama peserta ke-" + (i+1) + " harus diisi untuk draf.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        } catch (NumberFormatException e) {
            // Biarkan jumlahPesertaDraf 0 jika input tidak valid untuk draf
        }
        
        if (Session.currentUser == null || Session.currentUser.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyimpan draf trip kustom.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- Persiapan CustomTripModel untuk Draf ---
        String namaTripDraf = currentDestinations.isEmpty() ? "Custom Trip" : String.join(", ", currentDestinations);
        if (namaTripDraf.length() > 100) namaTripDraf = namaTripDraf.substring(0, 97) + "...";

        LocalDate parsedStartDateDraf = null;
        if (currentStartDate != null && !currentStartDate.isEmpty()) {
            try { parsedStartDateDraf = LocalDate.parse(currentStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")); } catch (Exception e) {}
        }
        LocalDate parsedEndDateDraf = null;
        if (currentEndDate != null && !currentEndDate.isEmpty()) {
            try { parsedEndDateDraf = LocalDate.parse(currentEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")); } catch (Exception e) {}
        }
        
        CustomTripModel customTripDraf = new CustomTripModel();
        customTripDraf.setUserId(Session.currentUser.getId());
        customTripDraf.setNamaTrip(namaTripDraf);
        customTripDraf.setTanggalMulai(parsedStartDateDraf);
        customTripDraf.setTanggalAkhir(parsedEndDateDraf);
        customTripDraf.setJumlahPeserta(jumlahPesertaDraf); // Set jumlah peserta draf
        customTripDraf.setStatus("draft"); // Status draf
        customTripDraf.setTotalHarga(currentInitialEstimatedCost); // Gunakan total biaya kumulatif
        customTripDraf.setCatatanUser(txtAreaFinalNotes.getText().trim());

        // --- Simpan CustomTripModel Draf ke DB ---
        CustomTripController customTripController = new CustomTripController();
        int customTripIdDraf = customTripController.saveCustomTrip(customTripDraf);

        if (customTripIdDraf != -1) {
            customTripDraf.setId(customTripIdDraf);

            // --- Buat ReservasiModel untuk Custom Trip Draf ---
            ReservasiModel reservasiDraf = new ReservasiModel();
            reservasiDraf.setUserId(Session.currentUser.getId());
            reservasiDraf.setTripType("custom_trip");
            reservasiDraf.setTripId(customTripIdDraf);
            reservasiDraf.setKodeReservasi("DRFCT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            reservasiDraf.setTanggalReservasi(LocalDate.now());
            reservasiDraf.setStatus("pending"); // Status reservasi pending untuk draf

            ReservasiController reservasiController = new ReservasiController();
            int reservasiIdDraf = reservasiController.buatReservasi(reservasiDraf);

            if (reservasiIdDraf != -1) {
                // Simpan penumpang untuk draf
                boolean allPassengersSavedDraf = true;
                for (String penumpangNamaDraf : penumpangListDraf) {
                    if (!penumpangNamaDraf.isEmpty()) { // Hanya simpan yang tidak kosong
                        if (!reservasiController.tambahPenumpang(reservasiIdDraf, penumpangNamaDraf)) {
                            allPassengersSavedDraf = false;
                        }
                    }
                }
                
                String msg = "Draf trip kustom berhasil disimpan. ID Reservasi: " + reservasiIdDraf + ".";
                if (!allPassengersSavedDraf) {
                    msg += "\nNamun, beberapa data penumpang mungkin tidak tersimpan.";
                }
                JOptionPane.showMessageDialog(this, msg + "\nAnda akan dialihkan ke halaman Pesanan Saya.", "Draf Disimpan", JOptionPane.INFORMATION_MESSAGE);
                
                // --- Arahkan ke PanelUserOrder ---
                mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat reservasi draf untuk Custom Trip Anda.", "Error Draf", JOptionPane.ERROR_MESSAGE);
                customTripController.deleteCustomTrip(customTripIdDraf); // Bersihkan custom trip jika reservasi gagal
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan Custom Trip sebagai draf.", "Error Draf", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Action performed when the "Kembali ke Kegiatan" (Prev Step) button is clicked.
     * Navigates back to the previous step (PanelActivityStep).
     */
    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_ACTIVITY_STEP, 
                                   currentDestinations, currentStartDate, currentEndDate, 
                                   currentTransportMode, currentTransportDetails,
                                   currentAccommodationName, currentRoomType, currentAccommodationNotes,
                                   currentInitialEstimatedCost); // Pass current cost back
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
        // --- Validasi Login (PENTING) ---
        if (Session.currentUser == null || Session.currentUser.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyelesaikan dan memesan trip kustom.", "Login Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate parsedStartDate = null;
        LocalDate parsedEndDate = null;
        int jumlahPeserta = 0;
        String statusCustomTrip;
        double totalHargaEstimasi;
        CustomTripModel customTrip = new CustomTripModel();
        List<String> penumpangList = new ArrayList<>();
        try {
            if (txtJumlahPeserta.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Jumlah peserta harus diisi.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                txtJumlahPeserta.requestFocusInWindow();
                return;
            }
            jumlahPeserta = Integer.parseInt(txtJumlahPeserta.getText().trim());
            if (jumlahPeserta <= 0 || jumlahPeserta > 3) {
                JOptionPane.showMessageDialog(this, "Jumlah peserta harus antara 1 dan 3.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                txtJumlahPeserta.requestFocusInWindow();
                return;
            }

            if (jumlahPeserta >= 1 && txtNamaPenumpang1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama peserta 1 harus diisi.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                txtNamaPenumpang1.requestFocusInWindow(); return;
            }
            if (jumlahPeserta >= 2 && txtNamaPenumpang2.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama peserta 2 harus diisi.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                txtNamaPenumpang2.requestFocusInWindow(); return;
            }
            if (jumlahPeserta >= 3 && txtNamaPenumpang3.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama peserta 3 harus diisi.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
                txtNamaPenumpang3.requestFocusInWindow(); return;
            }
            // Kumpulkan nama penumpang yang valid
            if (jumlahPeserta >= 1) penumpangList.add(txtNamaPenumpang1.getText().trim());
            if (jumlahPeserta >= 2) penumpangList.add(txtNamaPenumpang2.getText().trim());
            if (jumlahPeserta >= 3) penumpangList.add(txtNamaPenumpang3.getText().trim());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah peserta harus berupa angka valid.", "Validasi Peserta", JOptionPane.WARNING_MESSAGE);
            txtJumlahPeserta.requestFocusInWindow();
            return;
        }

        // --- 1. Prepare CustomTripModel ---
        String namaTrip = currentDestinations.isEmpty() ? "Custom Trip" : String.join(", ", currentDestinations);
        if (namaTrip.length() > 100) namaTrip = namaTrip.substring(0, 97) + "...";
        
        try { parsedStartDate = LocalDate.parse(currentStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")); } catch (Exception e) {
            System.err.println("Error parsing start date for CustomTripModel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Format tanggal mulai tidak valid. Periksa kembali tanggal.", "Error Tanggal", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try { parsedEndDate = LocalDate.parse(currentEndDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")); } catch (Exception e) {
            System.err.println("Error parsing end date for CustomTripModel: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Format tanggal akhir tidak valid. Periksa kembali tanggal.", "Error Tanggal", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        totalHargaEstimasi = calculateEstimatedCustomTripPrice(); // Gunakan total biaya kumulatif
        statusCustomTrip = "dipesan"; // Status awal setelah membuat trip, siap untuk pembayaran

        customTrip.setUserId(Session.currentUser.getId()); // Use logged-in user's ID
        customTrip.setNamaTrip(namaTrip);
        customTrip.setTanggalMulai(parsedStartDate);
        customTrip.setTanggalAkhir(parsedEndDate);
        customTrip.setJumlahPeserta(jumlahPeserta); // Set jumlah peserta
        customTrip.setStatus(statusCustomTrip);
        customTrip.setTotalHarga(totalHargaEstimasi);
        customTrip.setCatatanUser(txtAreaFinalNotes.getText().trim());

        // --- 2. Save CustomTripModel to DB ---
        CustomTripController customTripController = new CustomTripController();
        int customTripId = customTripController.saveCustomTrip(customTrip);

        if (customTripId != -1) {
            customTrip.setId(customTripId);

            // --- 3. Create ReservasiModel untuk Custom Trip ---
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
                // Simpan penumpang untuk reservasi ini
                boolean allPassengersSaved = true;
                for (String penumpangNama : penumpangList) {
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
                
                mainAppFrame.showPaymentPanel(reservasiId, namaKontak, emailKontak, teleponKontak, penumpangList);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat reservasi untuk Custom Trip Anda. Silakan coba lagi.", "Error Reservasi", JOptionPane.ERROR_MESSAGE);
                customTripController.deleteCustomTrip(customTripId); // Bersihkan custom trip jika reservasi gagal
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan Custom Trip Anda. Silakan coba lagi.", "Error Penyimpanan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
