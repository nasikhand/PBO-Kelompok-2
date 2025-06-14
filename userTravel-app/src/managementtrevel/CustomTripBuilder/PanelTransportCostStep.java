package managementtrevel.CustomTripBuilder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import controller.DestinasiController; // Import ini
import model.CustomTripDetailModel;
import model.DestinasiModel; // Import ini

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

        panelTransportCostSelection = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTransport = new GridBagConstraints();
        gbcTransport.insets = new Insets(5, 10, 5, 10);
        gbcTransport.anchor = GridBagConstraints.WEST;
        gbcTransport.fill = GridBagConstraints.HORIZONTAL;

        lblTransportMode = new JLabel("Mode Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 0;
        panelTransportCostSelection.add(lblTransportMode, gbcTransport);

        String[] transportModes = {"Pilih Mode", "Pesawat", "Kereta", "Bus", "Mobil Pribadi", "Lainnya"};
        cmbTransportMode = new JComboBox<>(transportModes);
        gbcTransport.gridx = 1; gbcTransport.gridy = 0; gbcTransport.weightx = 1.0;
        panelTransportCostSelection.add(cmbTransportMode, gbcTransport);

        lblTransportDetails = new JLabel("Detail Transportasi:");
        gbcTransport.gridx = 0; gbcTransport.gridy = 1; gbcTransport.weightx = 0;
        panelTransportCostSelection.add(lblTransportDetails, gbcTransport);

        txtTransportDetails = new JTextField(20);
        gbcTransport.gridx = 1; gbcTransport.gridy = 1; gbcTransport.weightx = 1.0;
        panelTransportCostSelection.add(txtTransportDetails, gbcTransport);
        
        gbcTransport.gridx = 0; gbcTransport.gridy = 2; gbcTransport.gridwidth = 2; gbcTransport.weighty = 1.0;
        panelTransportCostSelection.add(new JLabel(), gbcTransport);

        panelLeftContent.add(panelTransportCostSelection);
        panelLeftContent.add(Box.createVerticalGlue());

        panelRightContent = new JPanel(); // Re-initialize to ensure no old content
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

        panelTransportCostSelection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Transportasi",
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
        
        panelTransportCostSelection.setOpaque(false);
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
        } else if ("Lainnya".equals(selectedMode)) {
            currentCost += 100000;
        }

        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentCost));
        String transportDetailsPreview = txtTransportDetails.getText().trim();
        if (transportDetailsPreview.isEmpty()) {
            transportDetailsPreview = "Belum ada detail";
        }
        lblSummaryTransportDisplay.setText("Transportasi Dipilih: " + selectedMode + " (" + transportDetailsPreview + ")");
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
        selectedTransportMode = (String) cmbTransportMode.getSelectedItem();
        transportDetailsNotes = txtTransportDetails.getText().trim();

        if (cmbTransportMode.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Pilih mode transportasi untuk melanjutkan.", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double currentTotalEstimatedCost = 0.0;
        try {
            String formattedCost = lblEstimasiHargaValue.getText().replace(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).getCurrency().getSymbol(), "").replace(".", "").replace(",", ".");
            currentTotalEstimatedCost = NumberFormat.getInstance(new Locale("id", "ID")).parse(formattedCost).doubleValue();
        } catch (ParseException e) {
            System.err.println("Error parsing estimated cost from label in TransportCostStep: " + e.getMessage());
            currentTotalEstimatedCost = currentInitialEstimatedCost;
        }

        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PARTICIPANTS_STEP,
                                   currentDestinations,
                                   itineraryDetails,
                                   currentTotalEstimatedCost,
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
