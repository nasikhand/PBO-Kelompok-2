package managementtrevel.CustomTripBuilder;

import Asset.AppTheme; 
import managementtrevel.MainAppFrame; 

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Date; 
import java.text.SimpleDateFormat; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

// Impor JDateChooser
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;


public class PanelDateStep extends JPanel {

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

    private JPanel panelDateSelection;
    private JLabel lblStartDate;
    private JDateChooser dateChooserStartDate; 
    private JLabel lblEndDate;
    private JDateChooser dateChooserEndDate; 

    private JPanel panelTripSummary;
    private JPanel panelSummaryDates; 
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
    private Date selectedStartDate; 
    private Date selectedEndDate;   
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";
    // Placeholder tidak lagi relevan untuk JDateChooser dengan cara yang sama
    
    // Variabel instance yang mungkin sebelumnya lokal di initializeUI()
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

        // Inisialisasi panelMainHeader sebagai variabel instance
        this.panelMainHeader = new JPanel(new BorderLayout());
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

        dateChooserStartDate = new JDateChooser();
        dateChooserStartDate.setDateFormatString("yyyy-MM-dd"); 
        dateChooserStartDate.setPreferredSize(new Dimension(150, 28)); // Sesuaikan tinggi agar konsisten
        gbcDate.gridx = 1; gbcDate.gridy = 0; gbcDate.fill = GridBagConstraints.HORIZONTAL; gbcDate.weightx = 1.0;
        panelDateSelection.add(dateChooserStartDate, gbcDate);

        lblEndDate = new JLabel("Tanggal Selesai:");
        gbcDate.gridx = 0; gbcDate.gridy = 1; gbcDate.fill = GridBagConstraints.NONE; gbcDate.weightx = 0;
        panelDateSelection.add(lblEndDate, gbcDate);

        dateChooserEndDate = new JDateChooser();
        dateChooserEndDate.setDateFormatString("yyyy-MM-dd");
        dateChooserEndDate.setPreferredSize(new Dimension(150, 28)); // Sesuaikan tinggi
        gbcDate.gridx = 1; gbcDate.gridy = 1; gbcDate.fill = GridBagConstraints.HORIZONTAL; gbcDate.weightx = 1.0;
        panelDateSelection.add(dateChooserEndDate, gbcDate);
        
        gbcDate.gridx = 0; gbcDate.gridy = 2; gbcDate.gridwidth = 2; gbcDate.weighty = 1.0; 
        panelDateSelection.add(new JLabel(), gbcDate); 

        panelLeftContent.add(panelDateSelection);
        panelLeftContent.add(Box.createVerticalGlue()); 

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        
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

        // Inisialisasi panelMainFooter sebagai variabel instance
        this.panelMainFooter = new JPanel(new BorderLayout());
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
        
        if (panelSummaryDates != null) {
            panelSummaryDates.setOpaque(false);
            Component[] dateSummaryComponents = panelSummaryDates.getComponents();
            if (dateSummaryComponents.length > 0 && dateSummaryComponents[0] instanceof JLabel) { // "Tanggal Dipilih: "
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
        styleDateChooser(dateChooserStartDate);

        lblEndDate.setFont(AppTheme.FONT_LABEL_FORM);
        lblEndDate.setForeground(AppTheme.TEXT_DARK);
        styleDateChooser(dateChooserEndDate);
        
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

    private void styleDateChooser(JDateChooser dateChooser) {
        if (dateChooser == null) return;
        dateChooser.setFont(AppTheme.FONT_TEXT_FIELD);
        dateChooser.getCalendarButton().setFont(AppTheme.FONT_BUTTON);
        dateChooser.getCalendarButton().setBackground(AppTheme.PRIMARY_BLUE_LIGHT);
        dateChooser.getCalendarButton().setForeground(AppTheme.TEXT_WHITE);
        dateChooser.getCalendarButton().setFocusPainted(false);
        dateChooser.getCalendarButton().setBorder(BorderFactory.createEmptyBorder(2,5,2,5)); // Padding tombol kalender

        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setFont(AppTheme.FONT_TEXT_FIELD);
        editor.setBackground(AppTheme.INPUT_BACKGROUND);
        editor.setForeground(AppTheme.INPUT_TEXT); 
        editor.setBorder(AppTheme.createDefaultInputBorder());
        editor.setEditable(false); // Agar user hanya bisa memilih dari kalender

        dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
            Date newDate = (Date) evt.getNewValue();
            // Tidak perlu lagi set foreground untuk placeholder karena JDateChooser menanganinya
            // Update summary
            if (dateChooser == dateChooserStartDate) {
                selectedStartDate = newDate;
                lblSummaryStartDateDisplay.setText("Mulai: " + (newDate != null ? dateFormat.format(newDate) : "-"));
            } else if (dateChooser == dateChooserEndDate) {
                selectedEndDate = newDate;
                lblSummaryEndDateDisplay.setText("Selesai: " + (newDate != null ? dateFormat.format(newDate) : "-"));
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
        // selectedStartDate dan selectedEndDate sudah diupdate oleh listener JDateChooser
        
        if (selectedStartDate == null || selectedEndDate == null) {
            JOptionPane.showMessageDialog(this, "Pilih tanggal mulai dan selesai yang valid sebelum menyimpan.", "Tidak Dapat Menyimpan", JOptionPane.WARNING_MESSAGE);
            return;
        }
         if (selectedEndDate.before(selectedStartDate)) {
            JOptionPane.showMessageDialog(this, "Tanggal Selesai harus setelah atau sama dengan Tanggal Mulai.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String message = String.format(
            "Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal Mulai: %s\nTanggal Selesai: %s\nEstimasi Biaya: %s",
            currentDestinations, 
            dateFormat.format(selectedStartDate), 
            dateFormat.format(selectedEndDate),
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Simpan Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Saat kembali, kita tidak perlu mengirim data tanggal karena akan diisi ulang di DestinationStep
            mainAppFrame.showPanel(MainAppFrame.PANEL_DESTINATION_STEP); 
        } else {
            System.err.println("MainAppFrame reference is null in PanelDateStep (Prev).");
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        // selectedStartDate dan selectedEndDate sudah diupdate oleh listener JDateChooser
        if (selectedStartDate == null) {
            JOptionPane.showMessageDialog(this, "Masukkan Tanggal Mulai yang valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
            dateChooserStartDate.requestFocusInWindow(); 
            return;
        }
        if (selectedEndDate == null) {
            JOptionPane.showMessageDialog(this, "Masukkan Tanggal Selesai yang valid.", "Input Error", JOptionPane.ERROR_MESSAGE);
            dateChooserEndDate.requestFocusInWindow();
            return;
        }
        if (selectedEndDate.before(selectedStartDate)) {
            JOptionPane.showMessageDialog(this, "Tanggal Selesai harus setelah atau sama dengan Tanggal Mulai.", "Input Error", JOptionPane.ERROR_MESSAGE);
            dateChooserEndDate.requestFocusInWindow();
            return;
        }
        
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRANSPORT_STEP, 
                                   currentDestinations, 
                                   dateFormat.format(selectedStartDate), 
                                   dateFormat.format(selectedEndDate));
        } else {
             System.err.println("MainAppFrame reference is null in PanelDateStep (Next).");
        }
    }
}
