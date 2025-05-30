package managementtrevel.CustomTripBuilder;

import Asset.AppTheme; // Impor AppTheme Anda
import managementtrevel.MainAppFrame; // Impor MainAppFrame

import java.awt.*;
import java.awt.event.ActionEvent;
// import java.awt.event.FocusAdapter; // Tidak digunakan secara eksplisit di sini, tapi bisa untuk JTextArea
// import java.awt.event.FocusEvent;   // Tidak digunakan secara eksplisit di sini
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

// Mengubah nama kelas dan extends JPanel
public class PanelTransportStep extends JPanel {

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

    private JPanel panelSelectTransport;
    private JLabel lblTransportMode;
    private JComboBox<String> cmbTransportMode;
    private JLabel lblTransportDetails;
    private JTextArea txtTransportDetails;
    private JScrollPane scrollPaneTransportDetails;

    private JPanel panelSuggestTransport;
    private JLabel lblSuggestTransportInfo;

    private JPanel panelTransportOption;
    private JLabel lblTransportOptionInfo;

    private JPanel panelTripSummary;
    private JLabel lblSummaryStartDateDisplay;
    private JLabel lblSummaryEndDateDisplay;
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
    private String selectedTransportMode;
    private String transportNotes;


    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JComponent panelSummaryDates;

    private JComponent panelMainFooter;

    private JComponent panelMainHeader;

    // Konstruktor diubah untuk menerima MainAppFrame dan data dari langkah sebelumnya
    public PanelTransportStep(MainAppFrame mainAppFrame, List<String> destinations, String startDate, String endDate) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Pilih Transportasi"); // Judul disesuaikan
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

        // Panel Select Transport
        panelSelectTransport = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTransport = new GridBagConstraints();
        gbcTransport.insets = new Insets(5, 5, 5, 5);
        gbcTransport.anchor = GridBagConstraints.WEST;

        lblTransportMode = new JLabel("Moda Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 0;
        panelSelectTransport.add(lblTransportMode, gbcTransport);

        String[] transportOptions = {"Pilih Moda...", "Pesawat", "Kereta Api", "Bus", "Sewa Mobil", "Mobil Pribadi", "Lainnya"};
        cmbTransportMode = new JComboBox<>(transportOptions);
        gbcTransport.gridx = 1; gbcTransport.gridy = 0; gbcTransport.fill = GridBagConstraints.HORIZONTAL; gbcTransport.weightx = 1.0;
        panelSelectTransport.add(cmbTransportMode, gbcTransport);

        lblTransportDetails = new JLabel("Detail/Catatan:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 1; gbcTransport.fill = GridBagConstraints.NONE; gbcTransport.weightx = 0; gbcTransport.anchor = GridBagConstraints.NORTHWEST; // Agar label di atas JTextArea
        panelSelectTransport.add(lblTransportDetails, gbcTransport);

        txtTransportDetails = new JTextArea(5, 20); 
        txtTransportDetails.setLineWrap(true);
        txtTransportDetails.setWrapStyleWord(true);
        txtTransportDetails.setToolTipText("Masukkan nomor penerbangan, lokasi jemput, nama perusahaan, dll.");
        scrollPaneTransportDetails = new JScrollPane(txtTransportDetails);
        scrollPaneTransportDetails.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gbcTransport.gridx = 1; gbcTransport.gridy = 1; gbcTransport.fill = GridBagConstraints.BOTH; gbcTransport.weightx = 1.0; gbcTransport.weighty = 0.5; 
        panelSelectTransport.add(scrollPaneTransportDetails, gbcTransport);
        
        panelSelectTransport.setPreferredSize(new Dimension(0, 180)); 
        panelLeftContent.add(panelSelectTransport);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        // Panel Suggest Transport
        panelSuggestTransport = new JPanel(new BorderLayout());
        lblSuggestTransportInfo = new JLabel("Saran transportasi akan muncul di sini...", JLabel.CENTER);
        panelSuggestTransport.add(lblSuggestTransportInfo, BorderLayout.CENTER);
        panelSuggestTransport.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelSuggestTransport);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        // Panel Transport Option
        panelTransportOption = new JPanel(new BorderLayout());
        lblTransportOptionInfo = new JLabel("Opsi untuk transportasi yang dipilih...", JLabel.CENTER);
        panelTransportOption.add(lblTransportOptionInfo, BorderLayout.CENTER);
        panelTransportOption.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelTransportOption);
        panelLeftContent.add(Box.createVerticalGlue());

        // Panel Trip Summary
        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        this.panelSummaryDates = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        lblSummaryStartDateDisplay = new JLabel("Mulai: -");
        lblSummaryEndDateDisplay = new JLabel("Selesai: -");
        panelSummaryDates.add(new JLabel("Tanggal: "));
        panelSummaryDates.add(lblSummaryStartDateDisplay);
        panelSummaryDates.add(new JLabel(" s/d "));
        panelSummaryDates.add(lblSummaryEndDateDisplay);
        panelTripSummary.add(panelSummaryDates, BorderLayout.NORTH);

        listModelDestinasiDisplay = new DefaultListModel<>(); 
        listDestinasiSummary = new JList<>(listModelDestinasiDisplay);
        listDestinasiSummary.setToolTipText("Destinasi yang telah dipilih");
        listDestinasiSummary.setEnabled(false); 
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
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
        btnPrevStep = new JButton("< Kembali ke Tanggal"); 
        btnNextStep = new JButton("Lanjut ke Akomodasi >");
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

        panelSelectTransport.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Transportasi", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelSuggestTransport.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Saran Transportasi",
             TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelTransportOption.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Opsi Transportasi",
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
        
        panelSelectTransport.setOpaque(false);
        panelSuggestTransport.setOpaque(false);
        panelTransportOption.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        if (panelSummaryDates != null) ((JComponent) panelSummaryDates).setOpaque(false);

        lblTransportMode.setFont(AppTheme.FONT_LABEL_FORM);
        lblTransportMode.setForeground(AppTheme.TEXT_DARK);
        cmbTransportMode.setFont(AppTheme.FONT_TEXT_FIELD);
        cmbTransportMode.setBackground(AppTheme.INPUT_BACKGROUND);
        cmbTransportMode.setForeground(AppTheme.INPUT_TEXT);
        cmbTransportMode.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1)); // Simple border for ComboBox

        lblTransportDetails.setFont(AppTheme.FONT_LABEL_FORM);
        lblTransportDetails.setForeground(AppTheme.TEXT_DARK);
        txtTransportDetails.setFont(AppTheme.FONT_TEXT_FIELD);
        txtTransportDetails.setBackground(AppTheme.INPUT_BACKGROUND);
        txtTransportDetails.setForeground(AppTheme.INPUT_TEXT);
        txtTransportDetails.setBorder(AppTheme.createDefaultInputBorder()); // Use consistent input border
        txtTransportDetails.setMargin(new Insets(5,5,5,5)); // Padding dalam JTextArea
        scrollPaneTransportDetails.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR)); // Border untuk scrollpane
        
        lblSuggestTransportInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSuggestTransportInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblTransportOptionInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblTransportOptionInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        
        lblSummaryStartDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryStartDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryEndDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryEndDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        ((JLabel)panelSummaryDates.getComponent(0)).setFont(AppTheme.FONT_LABEL_FORM);
        ((JLabel)panelSummaryDates.getComponent(0)).setForeground(AppTheme.TEXT_DARK);
        ((JLabel)panelSummaryDates.getComponent(2)).setFont(AppTheme.FONT_LABEL_FORM);
        ((JLabel)panelSummaryDates.getComponent(2)).setForeground(AppTheme.TEXT_DARK);

        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummary.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        styleSecondaryButton(btnPrevStep, "< Kembali");
        stylePrimaryButton(btnNextStep, "Lanjut ke Akomodasi >");
        
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
    
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(3); // Langkah aktif adalah 3 (Transportasi)
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        lblSummaryStartDateDisplay.setText("Mulai: " + (currentStartDate != null ? currentStartDate : "-"));
        lblSummaryEndDateDisplay.setText("Selesai: " + (currentEndDate != null ? currentEndDate : "-"));
        
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

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        selectedTransportMode = cmbTransportMode.getSelectedIndex() > 0 ? cmbTransportMode.getSelectedItem().toString() : "";
        transportNotes = txtTransportDetails.getText().trim();
        
        String message = String.format(
            "Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s s/d %s\nTransportasi: %s (%s)\nEstimasi Biaya: %s",
            currentDestinations, 
            currentStartDate, 
            currentEndDate,
            selectedTransportMode,
            transportNotes.isEmpty() ? "Tidak ada catatan" : transportNotes,
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Navigasi kembali ke PanelDateStep
            // Anda mungkin perlu meneruskan kembali data destinasi jika ada kemungkinan berubah
            mainAppFrame.showPanel(MainAppFrame.PANEL_DATE_STEP, currentDestinations); 
        } else {
            System.err.println("MainAppFrame reference is null in PanelTransportStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        selectedTransportMode = cmbTransportMode.getSelectedIndex() > 0 ? cmbTransportMode.getSelectedItem().toString() : "";
        transportNotes = txtTransportDetails.getText().trim();

        if (selectedTransportMode.isEmpty() || selectedTransportMode.equals("Pilih Moda...")) {
            JOptionPane.showMessageDialog(this, "Pilih moda transportasi untuk melanjutkan.", "Transportasi Belum Dipilih", JOptionPane.WARNING_MESSAGE);
            cmbTransportMode.requestFocus();
            return;
        }
        
        if (mainAppFrame != null) {
            // Navigasi ke PanelAccommodationStep
            mainAppFrame.showPanel(MainAppFrame.PANEL_ACCOMMODATION_STEP, currentDestinations, currentStartDate, currentEndDate, selectedTransportMode, transportNotes);
            // JOptionPane.showMessageDialog(this, 
            //     String.format("Akan navigasi ke Akomodasi.\nDestinasi: %s\nTanggal: %s - %s\nTransport: %s (%s)", 
            //     currentDestinations, currentStartDate, currentEndDate, selectedTransportMode, transportNotes),
            //     "Info Navigasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
             System.err.println("MainAppFrame reference is null in PanelTransportStep (Next).");
        }
    }
}
