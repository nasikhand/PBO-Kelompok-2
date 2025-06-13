package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.text.NumberFormat;
import java.util.Locale;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;


public class PanelTransportStep extends JPanel {

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

    private JPanel panelTransportSelection;
    private JLabel lblTransportMode;
    private JComboBox<String> cmbTransportMode;
    private JLabel lblTransportDetails;
    private JTextField txtTransportDetails;

    private JPanel panelTripSummary;
    private JLabel lblSummaryDatesDisplay;
    private JLabel lblSummaryTransportDisplay;
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
    private double currentInitialEstimatedCost;

    private final String ACTIVE_STEP_ICON = "● "; 
    private final String INACTIVE_STEP_ICON = "○ ";
    private final String PLACEHOLDER_TEXT_DETAILS = "Contoh: Nama Maskapai/Nomor Kereta/Tipe Bus";

    private JPanel panelMainHeader;
    private JPanel panelMainFooter;
    private JPanel panelSummaryDates; // <--- DEKLARASI INI DITAMBAHKAN


    public PanelTransportStep(MainAppFrame mainAppFrame, List<String> destinations, String startDate, String endDate, double initialEstimatedCost) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Pilih Transportasi"); 
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

        panelTransportSelection = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTransport = new GridBagConstraints();
        gbcTransport.insets = new Insets(5, 10, 5, 10);
        gbcTransport.anchor = GridBagConstraints.WEST;
        gbcTransport.fill = GridBagConstraints.HORIZONTAL;

        lblTransportMode = new JLabel("Mode Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 0;
        panelTransportSelection.add(lblTransportMode, gbcTransport);

        String[] transportModes = {"Pilih Mode", "Pesawat", "Kereta", "Bus", "Mobil Pribadi"};
        cmbTransportMode = new JComboBox<>(transportModes);
        gbcTransport.gridx = 1; gbcTransport.gridy = 0; gbcTransport.weightx = 1.0;
        panelTransportSelection.add(cmbTransportMode, gbcTransport);

        lblTransportDetails = new JLabel("Detail Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 1; gbcTransport.weightx = 0;
        panelTransportSelection.add(lblTransportDetails, gbcTransport);

        txtTransportDetails = new JTextField(20);
        gbcTransport.gridx = 1; gbcTransport.gridy = 1; gbcTransport.weightx = 1.0;
        panelTransportSelection.add(txtTransportDetails, gbcTransport);
        
        gbcTransport.gridx = 0; gbcTransport.gridy = 2; gbcTransport.gridwidth = 2; gbcTransport.weighty = 1.0;
        panelTransportSelection.add(new JLabel(), gbcTransport);

        panelLeftContent.add(panelTransportSelection);
        panelLeftContent.add(Box.createVerticalGlue());

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        this.panelSummaryDates = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2)); // <--- INISIALISASI DI SINI
        lblSummaryDatesDisplay = new JLabel("Tanggal: - s/d -");
        panelSummaryDates.add(lblSummaryDatesDisplay, BorderLayout.NORTH);

        listModelDestinasiDisplay = new DefaultListModel<>();
        listDestinasiSummary = new JList<>(listModelDestinasiDisplay);
        listDestinasiSummary.setEnabled(false);
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        jScrollPaneDestinasiSummary.setPreferredSize(new Dimension(0, 100));
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);

        lblSummaryTransportDisplay = new JLabel("Transportasi: -");
        panelTripSummary.add(lblSummaryTransportDisplay, BorderLayout.SOUTH);

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

        panelTransportSelection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Transportasi", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelTripSummary.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Trip (Transportasi)",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        
        panelTransportSelection.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        if (panelSummaryDates != null) panelSummaryDates.setOpaque(false); // Cast removed because it's JPanel now

        lblTransportMode.setFont(AppTheme.FONT_LABEL_FORM);
        lblTransportMode.setForeground(AppTheme.TEXT_DARK);
        cmbTransportMode.setFont(AppTheme.FONT_TEXT_FIELD);
        cmbTransportMode.setBackground(AppTheme.INPUT_BACKGROUND);
        cmbTransportMode.setForeground(AppTheme.INPUT_TEXT);
        cmbTransportMode.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1));

        lblTransportDetails.setFont(AppTheme.FONT_LABEL_FORM);
        lblTransportDetails.setForeground(AppTheme.TEXT_DARK);
        styleInputField(txtTransportDetails, PLACEHOLDER_TEXT_DETAILS);
        
        lblSummaryDatesDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryDatesDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummary.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblSummaryTransportDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryTransportDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);


        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        styleSecondaryButton(btnPrevStep, "< Kembali ke Tanggal");
        stylePrimaryButton(btnNextStep, "Lanjut ke Akomodasi >");
        
        if (panelMainHeader != null) panelMainHeader.setOpaque(false);
        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
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
        updateBuildStepLabels(3); // Active step
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        lblSummaryDatesDisplay.setText("Tanggal: " + currentStartDate + " s/d " + currentEndDate);

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

    private void updateNextStepButtonState() {
        boolean isTransportSelected = cmbTransportMode.getSelectedIndex() > 0;
        btnNextStep.setEnabled(isTransportSelected);
    }

    private void cmbTransportModeItemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            updateEstimatedCost();
            updateNextStepButtonState();
        }
    }

    private void updateEstimatedCost() {
        double currentCost = currentInitialEstimatedCost;

        String selectedMode = (String) cmbTransportMode.getSelectedItem();
        if ("Pesawat".equals(selectedMode)) {
            currentCost += 1500000;
        } else if ("Kereta".equals(selectedMode)) {
            currentCost += 500000;
        } else if ("Bus".equals(selectedMode)) {
            currentCost += 200000;
        } else if ("Mobil Pribadi".equals(selectedMode)) {
            currentCost += 300000;
        }

        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentCost));
        lblSummaryTransportDisplay.setText("Transportasi: " + selectedMode + " (" + txtTransportDetails.getText().trim() + ")");
    }

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        String transportMode = (String) cmbTransportMode.getSelectedItem();
        String transportDetails = txtTransportDetails.getText().trim();

        if (cmbTransportMode.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih mode transportasi sebelum menyimpan.", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double currentEstimatedCostForSaving = 0.0;
        try {
            // Parse nilai dari JLabel lblEstimasiHargaValue, hapus format mata uang
            String formattedCost = lblEstimasiHargaValue.getText().replace(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).getCurrency().getSymbol(), "").replace(".", "").replace(",", ".");
            currentEstimatedCostForSaving = NumberFormat.getInstance(new Locale("id", "ID")).parse(formattedCost).doubleValue();
        } catch (ParseException e) {
            System.err.println("Error parsing estimated cost from label in TransportStep (Save): " + e.getMessage());
            currentEstimatedCostForSaving = currentInitialEstimatedCost; // Fallback jika parsing gagal
        }

        String message = String.format(
            "Draf Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s s/d %s\nTransportasi: %s (%s)\nEstimasi Biaya: %s",
            currentDestinations, currentStartDate, currentEndDate,
            transportMode, transportDetails.isEmpty() ? "-" : transportDetails,
            AppTheme.formatCurrency(currentEstimatedCostForSaving) // Gunakan formatCurrency
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Draf Trip Berhasil (Simulasi)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_DATE_STEP, currentDestinations, currentInitialEstimatedCost);
        } else {
            System.err.println("MainAppFrame reference is null in PanelTransportStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        String transportMode = (String) cmbTransportMode.getSelectedItem();
        String transportDetails = txtTransportDetails.getText().trim();

        if (cmbTransportMode.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih mode transportasi untuk melanjutkan.", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double currentTotalEstimatedCost = 0.0;
        try {
        // Parse nilai dari JLabel lblEstimasiHargaValue untuk mendapatkan total biaya kumulatif
            String formattedCost = lblEstimasiHargaValue.getText().replace(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).getCurrency().getSymbol(), "").replace(".", "").replace(",", ".");
            currentTotalEstimatedCost = NumberFormat.getInstance(new Locale("id", "ID")).parse(formattedCost).doubleValue();
        } catch (ParseException e) {
            System.err.println("Error parsing estimated cost from label in TransportStep (Next): " + e.getMessage());
            currentTotalEstimatedCost = currentInitialEstimatedCost; // Fallback jika parsing gagal
        }

        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_ACCOMMODATION_STEP,
                                   currentDestinations,
                                   currentStartDate,
                                   currentEndDate,
                                   transportMode,
                                   transportDetails,
                                   currentTotalEstimatedCost);
        } else {
            System.err.println("MainAppFrame reference is null in PanelTransportStep (Next).");
        }
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
}
