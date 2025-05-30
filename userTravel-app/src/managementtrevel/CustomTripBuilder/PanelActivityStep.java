package managementtrevel.CustomTripBuilder;

import Asset.AppTheme; // Impor AppTheme Anda
import managementtrevel.MainAppFrame; // Impor MainAppFrame

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

// Mengubah nama kelas dan extends JPanel
public class PanelActivityStep extends JPanel {

    private MainAppFrame mainAppFrame; // Referensi ke MainAppFrame

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

    private JPanel panelAddActivity;
    private JLabel lblActivityName;
    private JTextField txtActivityName;
    private JButton btnAddActivityToList;
    private JScrollPane scrollPaneAddedActivities;
    private JList<String> listAddedActivities;
    private JButton btnRemoveActivityFromList;

    private JPanel panelSuggestActivity;
    private JLabel lblSuggestActivityInfo;

    private JPanel panelActivityOption;
    private JLabel lblActivityOptionInfo;

    private JPanel panelTripSummary;
    private JLabel lblSummaryStartDateDisplay;
    private JLabel lblSummaryEndDateDisplay;
    private JLabel lblSummaryTransportDisplay;
    private JLabel lblSummaryAccommodationDisplay;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummaryDisplay;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryTitleInfo; 
    private JLabel lblEstimasiHargaValue;

    private JButton btnPrevStep;
    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasiDisplay;
    private DefaultListModel<String> listModelAddedActivities;

    private final List<String> currentDestinations;
    private final String currentStartDate;
    private final String currentEndDate;
    private final String currentTransportMode;
    private final String currentTransportDetails;
    private final String currentAccommodationName;
    private final String currentRoomType;
    private final String currentAccommodationNotes;
    private List<String> addedActivitiesList;


    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JComponent panelMainFooter;

    private JComponent panelMainHeader;

    private JComponent panelSummaryDetails;

    // Konstruktor diubah untuk menerima MainAppFrame dan data dari langkah sebelumnya
    public PanelActivityStep(MainAppFrame mainAppFrame, List<String> destinations, String startDate, String endDate, 
                                String transportMode, String transportDetails,
                                String accommodationName, String roomType, String accommodationNotes) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        this.addedActivitiesList = new ArrayList<>(); // Inisialisasi list untuk menyimpan aktivitas
        
        initializeUI();
        applyAppTheme(); // Terapkan tema setelah komponen diinisialisasi
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

        JPanel panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Tambah Kegiatan"); // Judul disesuaikan
        btnSaveTrip = new JButton("Simpan Trip");
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setBorder(new EmptyBorder(0,0,0,5)); 

        panelRightContent = new JPanel();
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0)); 

        // Panel Add Activity
        panelAddActivity = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAct = new GridBagConstraints();
        gbcAct.insets = new Insets(5,5,5,5);
        gbcAct.anchor = GridBagConstraints.WEST;

        lblActivityName = new JLabel("Nama/Deskripsi Kegiatan:");
        gbcAct.gridx = 0; gbcAct.gridy = 0; gbcAct.gridwidth = 2;
        panelAddActivity.add(lblActivityName, gbcAct);

        txtActivityName = new JTextField(25);
        gbcAct.gridx = 0; gbcAct.gridy = 1; gbcAct.gridwidth = 1; gbcAct.fill = GridBagConstraints.HORIZONTAL; gbcAct.weightx = 1.0;
        panelAddActivity.add(txtActivityName, gbcAct);

        btnAddActivityToList = new JButton("Tambah ke Daftar");
        gbcAct.gridx = 1; gbcAct.gridy = 1; gbcAct.gridwidth = 1; gbcAct.fill = GridBagConstraints.NONE; gbcAct.weightx = 0;
        panelAddActivity.add(btnAddActivityToList, gbcAct);
        
        listModelAddedActivities = new DefaultListModel<>();
        listAddedActivities = new JList<>(listModelAddedActivities);
        listAddedActivities.setVisibleRowCount(5);
        scrollPaneAddedActivities = new JScrollPane(listAddedActivities);
        gbcAct.gridx = 0; gbcAct.gridy = 2; gbcAct.gridwidth = 2; gbcAct.fill = GridBagConstraints.BOTH; gbcAct.weighty = 1.0;
        panelAddActivity.add(scrollPaneAddedActivities, gbcAct);

        btnRemoveActivityFromList = new JButton("Hapus Terpilih");
        gbcAct.gridx = 0; gbcAct.gridy = 3; gbcAct.gridwidth = 2; gbcAct.anchor = GridBagConstraints.EAST; gbcAct.fill = GridBagConstraints.NONE; gbcAct.weighty = 0;
        panelAddActivity.add(btnRemoveActivityFromList, gbcAct);

        panelAddActivity.setPreferredSize(new Dimension(0, 220));
        panelLeftContent.add(panelAddActivity);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        // Panel Suggest Activity
        panelSuggestActivity = new JPanel(new BorderLayout());
        lblSuggestActivityInfo = new JLabel("Saran kegiatan akan muncul di sini...", JLabel.CENTER);
        panelSuggestActivity.add(lblSuggestActivityInfo, BorderLayout.CENTER);
        panelSuggestActivity.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelSuggestActivity);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        // Panel Activity Option
        panelActivityOption = new JPanel(new BorderLayout());
        lblActivityOptionInfo = new JLabel("Opsi untuk kegiatan yang dipilih...", JLabel.CENTER);
        panelActivityOption.add(lblActivityOptionInfo, BorderLayout.CENTER);
        panelActivityOption.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelActivityOption);
        panelLeftContent.add(Box.createVerticalGlue());

        // Panel Trip Summary
        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        this.panelSummaryDetails = new JPanel();
        panelSummaryDetails.setLayout(new BoxLayout(panelSummaryDetails, BoxLayout.Y_AXIS));
        panelSummaryDetails.setBorder(new EmptyBorder(5,5,5,5));
        
        lblSummaryStartDateDisplay = new JLabel("Mulai: -");
        lblSummaryStartDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryStartDateDisplay);
        
        lblSummaryEndDateDisplay = new JLabel("Selesai: -");
        lblSummaryEndDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryEndDateDisplay);

        lblSummaryTransportDisplay = new JLabel("Transportasi: -");
        lblSummaryTransportDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryTransportDisplay);

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
        listDestinasiSummaryDisplay = new JList<>(listModelDestinasiDisplay);
        listDestinasiSummaryDisplay.setEnabled(false); 
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummaryDisplay);
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);
        panelRightContent.add(panelTripSummary);
        panelRightContent.add(Box.createRigidArea(new Dimension(0,10)));

        // Panel Estimated Cost
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
        btnPrevStep = new JButton("< Kembali ke Akomodasi"); 
        btnNextStep = new JButton("Lanjut ke Finalisasi >");
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

        lblCustomTripBuilderTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblCustomTripBuilderTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnSaveTrip, "Simpan Trip");

        panelLeftContent.setOpaque(false);
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);

        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;

        panelAddActivity.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Tambah Kegiatan", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelSuggestActivity.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Saran Kegiatan",
             TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelActivityOption.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Opsi Kegiatan",
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
        
        panelAddActivity.setOpaque(false);
        panelSuggestActivity.setOpaque(false);
        panelActivityOption.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        if (panelSummaryDetails != null) panelSummaryDetails.setOpaque(false);


        lblActivityName.setFont(AppTheme.FONT_LABEL_FORM);
        lblActivityName.setForeground(AppTheme.TEXT_DARK);
        txtActivityName.setFont(AppTheme.FONT_TEXT_FIELD);
        txtActivityName.setBorder(AppTheme.createDefaultInputBorder());
        txtActivityName.setBackground(AppTheme.INPUT_BACKGROUND);
        txtActivityName.setForeground(AppTheme.INPUT_TEXT);
        addFocusBorderEffect(txtActivityName); // Tambahkan efek fokus

        stylePrimaryButton(btnAddActivityToList, "Tambah");
        styleSecondaryButton(btnRemoveActivityFromList, "Hapus");

        listAddedActivities.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listAddedActivities.setBackground(AppTheme.INPUT_BACKGROUND);
        listAddedActivities.setForeground(AppTheme.INPUT_TEXT);
        scrollPaneAddedActivities.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        
        lblSuggestActivityInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSuggestActivityInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblActivityOptionInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblActivityOptionInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        
        lblSummaryStartDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryStartDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryEndDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryEndDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryTransportDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryTransportDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryAccommodationDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryAccommodationDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        
        if (panelSummaryDetails.getComponent(0) instanceof JLabel) { 
             ((JLabel)panelSummaryDetails.getComponent(0)).setFont(AppTheme.FONT_LABEL_FORM);
             ((JLabel)panelSummaryDetails.getComponent(0)).setForeground(AppTheme.TEXT_DARK);
        }
         if (panelSummaryDetails.getComponent(5) instanceof JLabel) { // "Destinations:"
             ((JLabel)panelSummaryDetails.getComponent(5)).setFont(AppTheme.FONT_LABEL_FORM);
             ((JLabel)panelSummaryDetails.getComponent(5)).setForeground(AppTheme.TEXT_DARK);
             ((JLabel)panelSummaryDetails.getComponent(5)).setBorder(new EmptyBorder(3,0,3,0));
        }


        listDestinasiSummaryDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummaryDisplay.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummaryDisplay.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        styleSecondaryButton(btnPrevStep, "< Kembali");
        stylePrimaryButton(btnNextStep, "Lanjut ke Finalisasi >");
        
        if (panelMainHeader != null) panelMainHeader.setOpaque(false);
        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
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
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(AppTheme.createFocusBorder());
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }
    
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(5); // Langkah aktif adalah 5 (Kegiatan)
        
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
        lblSummaryTransportDisplay.setText("Transportasi: " + transportInfo);
        
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "-");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (Kamar: " + currentRoomType + ")";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - Catatan: " + currentAccommodationNotes;
        }
        lblSummaryAccommodationDisplay.setText("Akomodasi: " + accommodationInfo);

        btnAddActivityToList.addActionListener(this::btnAddActivityToListActionPerformed);
        btnRemoveActivityFromList.addActionListener(this::btnRemoveActivityFromListActionPerformed);
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);
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
    
    private void btnAddActivityToListActionPerformed(ActionEvent evt) {
        String activity = txtActivityName.getText().trim();
        if (!activity.isEmpty()) {
            if (listModelAddedActivities != null) { 
                listModelAddedActivities.addElement(activity);
                txtActivityName.setText(""); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Masukkan deskripsi kegiatan.", "Input Kosong", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnRemoveActivityFromListActionPerformed(ActionEvent evt) {
        if (listAddedActivities != null && listModelAddedActivities != null) {
            int selectedIndex = listAddedActivities.getSelectedIndex();
            if (selectedIndex != -1) {
                listModelAddedActivities.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih kegiatan dari daftar untuk dihapus.", "Tidak Ada Kegiatan Terpilih", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        addedActivitiesList.clear(); // Bersihkan dulu jika ada data lama
        if (listModelAddedActivities != null) {
            for (int i = 0; i < listModelAddedActivities.getSize(); i++) {
                addedActivitiesList.add(listModelAddedActivities.getElementAt(i));
            }
        }

        String message = String.format(
            "Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s s/d %s\nTransportasi: %s (%s)\nAkomodasi: %s (%s) - Catatan: %s\nKegiatan: %s\nEstimasi Biaya: %s",
            currentDestinations, 
            currentStartDate, 
            currentEndDate,
            currentTransportMode,
            currentTransportDetails != null && !currentTransportDetails.isEmpty() ? currentTransportDetails : "Tidak ada detail",
            currentAccommodationName == null || currentAccommodationName.isEmpty() ? "-" : currentAccommodationName,
            currentRoomType == null || currentRoomType.isEmpty() ? "" : ", Kamar: " + currentRoomType,
            currentAccommodationNotes == null || currentAccommodationNotes.isEmpty() ? "" : ", Catatan: " + currentAccommodationNotes,
            addedActivitiesList.isEmpty() ? "Tidak ada" : addedActivitiesList.toString(),
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Navigasi kembali ke PanelAccommodationStep
            mainAppFrame.showPanel(MainAppFrame.PANEL_ACCOMMODATION_STEP, 
                                   currentDestinations, currentStartDate, currentEndDate, 
                                   currentTransportMode, currentTransportDetails); 
        } else {
            System.err.println("MainAppFrame reference is null in PanelActivityStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        addedActivitiesList.clear();
        if (listModelAddedActivities != null) {
            for (int i = 0; i < listModelAddedActivities.getSize(); i++) {
                addedActivitiesList.add(listModelAddedActivities.getElementAt(i));
            }
        }
        
        if (mainAppFrame != null) {
            // Navigasi ke PanelFinalStep
            mainAppFrame.showPanel(MainAppFrame.PANEL_FINAL_STEP, currentDestinations, currentStartDate, currentEndDate, currentTransportMode, currentTransportDetails, currentAccommodationName, currentRoomType, currentAccommodationNotes, addedActivitiesList);
            // JOptionPane.showMessageDialog(this, 
            //     String.format("Akan navigasi ke Finalisasi.\n...Data sebelumnya...\nKegiatan: %s", 
            //     addedActivitiesList.isEmpty() ? "Tidak ada" : addedActivitiesList.toString()),
            //     "Info Navigasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
             System.err.println("MainAppFrame reference is null in PanelActivityStep (Next).");
        }
    }
}
