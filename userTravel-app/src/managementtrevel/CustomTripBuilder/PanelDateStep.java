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

// Mengubah nama kelas dan extends JPanel
public class PanelDateStep extends JPanel {

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

    private JPanel panelDateSelection;
    private JLabel lblStartDate;
    private JTextField txtStartDate; 
    private JLabel lblEndDate;
    private JTextField txtEndDate;   

    private JPanel panelTripSummary;
    private JPanel panelSummaryDates; // Pastikan ini adalah variabel instance
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
    private String selectedStartDate;
    private String selectedEndDate;

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";
    private final String START_DATE_PLACEHOLDER = "YYYY-MM-DD"; 
    private final String END_DATE_PLACEHOLDER = "YYYY-MM-DD"; 
    
    private JPanel panelMainHeader;
    private JPanel panelMainFooter;  

    public PanelDateStep(MainAppFrame mainAppFrame, List<String> destinations) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
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

        JPanel panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Pilih Tanggal"); 
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

        panelDateSelection = new JPanel(new GridBagLayout());
        GridBagConstraints gbcDate = new GridBagConstraints();
        gbcDate.insets = new Insets(10, 10, 10, 10);
        gbcDate.anchor = GridBagConstraints.WEST;

        lblStartDate = new JLabel("Tanggal Mulai:");
        gbcDate.gridx = 0; gbcDate.gridy = 0;
        panelDateSelection.add(lblStartDate, gbcDate);

        txtStartDate = new JTextField(15);
        txtStartDate.setToolTipText("Masukkan tanggal mulai (YYYY-MM-DD)");
        gbcDate.gridx = 1; gbcDate.gridy = 0; gbcDate.fill = GridBagConstraints.HORIZONTAL; gbcDate.weightx = 1.0;
        panelDateSelection.add(txtStartDate, gbcDate);

        lblEndDate = new JLabel("Tanggal Selesai:");
        gbcDate.gridx = 0; gbcDate.gridy = 1; gbcDate.fill = GridBagConstraints.NONE; gbcDate.weightx = 0;
        panelDateSelection.add(lblEndDate, gbcDate);

        txtEndDate = new JTextField(15);
        txtEndDate.setToolTipText("Masukkan tanggal selesai (YYYY-MM-DD)");
        gbcDate.gridx = 1; gbcDate.gridy = 1; gbcDate.fill = GridBagConstraints.HORIZONTAL; gbcDate.weightx = 1.0;
        panelDateSelection.add(txtEndDate, gbcDate);
        
        gbcDate.gridx = 0; gbcDate.gridy = 2; gbcDate.gridwidth = 2; gbcDate.weighty = 1.0; 
        panelDateSelection.add(new JLabel(), gbcDate); 

        panelLeftContent.add(panelDateSelection);
        panelLeftContent.add(Box.createVerticalGlue()); 

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
        // Inisialisasi panelSummaryDates sebagai variabel instance
        this.panelSummaryDates = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        lblSummaryStartDateDisplay = new JLabel("Mulai: -");
        lblSummaryEndDateDisplay = new JLabel("Selesai: -");
        panelSummaryDates.add(new JLabel("Tanggal Dipilih: "));
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
        btnPrevStep = new JButton("< Kembali ke Destinasi"); 
        btnNextStep = new JButton("Lanjut ke Transportasi >");
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

        panelDateSelection.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Tanggal Perjalanan", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelTripSummary.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Trip (Destinasi & Tanggal)",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        
        panelDateSelection.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        
        // Pastikan panelSummaryDates tidak null sebelum diakses
        if (panelSummaryDates != null) {
            panelSummaryDates.setOpaque(false);
            // Styling komponen di dalam panelSummaryDates
            // Lebih aman untuk mengambil komponen berdasarkan referensi langsung atau nama jika memungkinkan,
            // daripada berdasarkan indeks, karena urutan bisa berubah.
            // Namun, jika urutannya tetap, ini bisa jalan.
            Component[] dateSummaryComponents = panelSummaryDates.getComponents();
            if (dateSummaryComponents.length > 0 && dateSummaryComponents[0] instanceof JLabel) {
                ((JLabel)dateSummaryComponents[0]).setFont(AppTheme.FONT_LABEL_FORM); 
                ((JLabel)dateSummaryComponents[0]).setForeground(AppTheme.TEXT_DARK);
            }
             if (dateSummaryComponents.length > 2 && dateSummaryComponents[2] instanceof JLabel) { // " s/d "
                ((JLabel)dateSummaryComponents[2]).setFont(AppTheme.FONT_LABEL_FORM);
                ((JLabel)dateSummaryComponents[2]).setForeground(AppTheme.TEXT_DARK);
            }
        }


        lblStartDate.setFont(AppTheme.FONT_LABEL_FORM);
        lblStartDate.setForeground(AppTheme.TEXT_DARK);
        txtStartDate.setFont(AppTheme.FONT_TEXT_FIELD);
        txtStartDate.setBorder(AppTheme.createDefaultInputBorder());
        txtStartDate.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        txtStartDate.setText(START_DATE_PLACEHOLDER);
        addFocusBorderEffect(txtStartDate, START_DATE_PLACEHOLDER);

        lblEndDate.setFont(AppTheme.FONT_LABEL_FORM);
        lblEndDate.setForeground(AppTheme.TEXT_DARK);
        txtEndDate.setFont(AppTheme.FONT_TEXT_FIELD);
        txtEndDate.setBorder(AppTheme.createDefaultInputBorder());
        txtEndDate.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        txtEndDate.setText(END_DATE_PLACEHOLDER);
        addFocusBorderEffect(txtEndDate, END_DATE_PLACEHOLDER);
        
        lblSummaryStartDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryStartDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSummaryEndDateDisplay.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSummaryEndDateDisplay.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummary.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        styleSecondaryButton(btnPrevStep, "< Kembali");
        stylePrimaryButton(btnNextStep, "Lanjut ke Transportasi >");
        
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
    
    private void addFocusBorderEffect(JTextField textField, String placeholder) {
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
                // Update summary on focus lost if text is valid (not placeholder)
                if (textField == txtStartDate && !txtStartDate.getText().equals(START_DATE_PLACEHOLDER) && !txtStartDate.getText().isEmpty()) {
                    lblSummaryStartDateDisplay.setText("Mulai: " + txtStartDate.getText());
                } else if (textField == txtStartDate) { // Jika kosong atau placeholder setelah focus lost
                     lblSummaryStartDateDisplay.setText("Mulai: -");
                }
                if (textField == txtEndDate && !txtEndDate.getText().equals(END_DATE_PLACEHOLDER) && !txtEndDate.getText().isEmpty()) {
                    lblSummaryEndDateDisplay.setText("Selesai: " + txtEndDate.getText());
                } else if (textField == txtEndDate) { // Jika kosong atau placeholder setelah focus lost
                    lblSummaryEndDateDisplay.setText("Selesai: -");
                }
            }
        });
    }

    private void setupLogicAndVisuals() {
        updateBuildStepLabels(2); 
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        
        lblSummaryStartDateDisplay.setText("Mulai: -");
        lblSummaryEndDateDisplay.setText("Selesai: -");

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
        selectedStartDate = txtStartDate.getText().trim();
        selectedEndDate = txtEndDate.getText().trim();
        
        if (selectedStartDate.equals(START_DATE_PLACEHOLDER) || selectedEndDate.equals(END_DATE_PLACEHOLDER) || 
            selectedStartDate.isEmpty() || selectedEndDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih tanggal mulai dan selesai yang valid sebelum menyimpan.", "Tidak Dapat Menyimpan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = String.format(
            "Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal Mulai: %s\nTanggal Selesai: %s\nEstimasi Biaya: %s",
            currentDestinations, 
            selectedStartDate, 
            selectedEndDate,
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_DESTINATION_STEP); 
        } else {
            System.err.println("MainAppFrame reference is null in PanelDateStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        selectedStartDate = txtStartDate.getText().trim();
        selectedEndDate = txtEndDate.getText().trim();

        if (selectedStartDate.isEmpty() || selectedStartDate.equals(START_DATE_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Masukkan Tanggal Mulai yang valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtStartDate.requestFocus();
            return;
        }
        if (selectedEndDate.isEmpty() || selectedEndDate.equals(END_DATE_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Masukkan Tanggal Selesai yang valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtEndDate.requestFocus();
            return;
        }
        
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRANSPORT_STEP, currentDestinations, selectedStartDate, selectedEndDate);
        } else {
             System.err.println("MainAppFrame reference is null in PanelDateStep (Next).");
        }
    }
}
