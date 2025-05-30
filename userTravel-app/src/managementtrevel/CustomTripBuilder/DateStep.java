package managementtrevel.CustomTripBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DateStep extends JFrame {

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

    private final String ACTIVE_STEP = "● ";
    private final String INACTIVE_STEP = "○ ";
    private final String START_DATE_PLACEHOLDER = "YYYY-MM-DD";
    private final String END_DATE_PLACEHOLDER = "YYYY-MM-DD";

    public DateStep(List<String> destinations) {
        this.currentDestinations = destinations != null ? destinations : new ArrayList<>();
        initializeUI();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        setTitle("Custom Trip - Select Dates");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5)); 

        ((JPanel)getContentPane()).setBorder(new EmptyBorder(0, 65, 10, 10));

        panelBuildSteps = new JPanel();
        panelBuildSteps.setLayout(new BoxLayout(panelBuildSteps, BoxLayout.Y_AXIS));
        panelBuildSteps.setPreferredSize(new Dimension(220, 0)); 
        panelBuildSteps.setBackground(new Color(230, 230, 230));
        panelBuildSteps.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY), 
            new EmptyBorder(10, 0, 10, 0) 
        ));

        lblBuildStepsTitle = new JLabel(" Build Steps");
        lblBuildStepsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBuildStepsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBuildStepsTitle.setBorder(new EmptyBorder(10, 10, 15, 10));
        panelBuildSteps.add(lblBuildStepsTitle);

        Font stepFont = new Font("Segoe UI", Font.PLAIN, 13);
        EmptyBorder stepLabelBorder = new EmptyBorder(8, 15, 8, 10);

        lblStep1Destinasi = new JLabel("TEMP"); lblStep1Destinasi.setFont(stepFont); lblStep1Destinasi.setBorder(stepLabelBorder); lblStep1Destinasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep2Tanggal = new JLabel("TEMP");    lblStep2Tanggal.setFont(stepFont);    lblStep2Tanggal.setBorder(stepLabelBorder);    lblStep2Tanggal.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep3Transport = new JLabel("TEMP");  lblStep3Transport.setFont(stepFont);  lblStep3Transport.setBorder(stepLabelBorder);  lblStep3Transport.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep4Akomodasi = new JLabel("TEMP"); lblStep4Akomodasi.setFont(stepFont); lblStep4Akomodasi.setBorder(stepLabelBorder); lblStep4Akomodasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep5Kegiatan = new JLabel("TEMP");  lblStep5Kegiatan.setFont(stepFont);  lblStep5Kegiatan.setBorder(stepLabelBorder);  lblStep5Kegiatan.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblStep6Final = new JLabel("TEMP");     lblStep6Final.setFont(stepFont);     lblStep6Final.setBorder(stepLabelBorder);     lblStep6Final.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelBuildSteps.add(lblStep1Destinasi);
        panelBuildSteps.add(lblStep2Tanggal);
        panelBuildSteps.add(lblStep3Transport);
        panelBuildSteps.add(lblStep4Akomodasi);
        panelBuildSteps.add(lblStep5Kegiatan);
        panelBuildSteps.add(lblStep6Final);
        panelBuildSteps.add(Box.createVerticalGlue()); 

        add(panelBuildSteps, BorderLayout.WEST);

        panelCustomTripMain = new JPanel(new BorderLayout(10, 10));
        panelCustomTripMain.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelCustomTripMain.setBackground(Color.WHITE);

        JPanel panelMainHeader = new JPanel(new BorderLayout());
        panelMainHeader.setOpaque(false); 
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Select Dates");
        lblCustomTripBuilderTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnSaveTrip = new JButton("[Save]");
        btnSaveTrip.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSaveTrip.setToolTipText("Save current trip configuration");
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setOpaque(false);
        panelLeftContent.setBorder(new EmptyBorder(0,0,0,5)); 

        panelRightContent = new JPanel();
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setOpaque(false);
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0)); 

        panelDateSelection = new JPanel(new GridBagLayout());
        panelDateSelection.setOpaque(false);
        panelDateSelection.setBorder(BorderFactory.createTitledBorder("Select Travel Dates"));
        GridBagConstraints gbcDate = new GridBagConstraints();
        gbcDate.insets = new Insets(10, 10, 10, 10);
        gbcDate.anchor = GridBagConstraints.WEST;

        lblStartDate = new JLabel("Start Date:");
        lblStartDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcDate.gridx = 0; gbcDate.gridy = 0;
        panelDateSelection.add(lblStartDate, gbcDate);

        txtStartDate = new JTextField(15);
        txtStartDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtStartDate.setToolTipText("Enter start date in YYYY-MM-DD format");
        gbcDate.gridx = 1; gbcDate.gridy = 0; gbcDate.fill = GridBagConstraints.HORIZONTAL; gbcDate.weightx = 1.0;
        panelDateSelection.add(txtStartDate, gbcDate);

        lblEndDate = new JLabel("End Date:");
        lblEndDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcDate.gridx = 0; gbcDate.gridy = 1; gbcDate.fill = GridBagConstraints.NONE; gbcDate.weightx = 0;
        panelDateSelection.add(lblEndDate, gbcDate);

        txtEndDate = new JTextField(15);
        txtEndDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEndDate.setToolTipText("Enter end date in YYYY-MM-DD format");
        gbcDate.gridx = 1; gbcDate.gridy = 1; gbcDate.fill = GridBagConstraints.HORIZONTAL; gbcDate.weightx = 1.0;
        panelDateSelection.add(txtEndDate, gbcDate);
        
        gbcDate.gridx = 0; gbcDate.gridy = 2; gbcDate.gridwidth = 2; gbcDate.weighty = 1.0;
        panelDateSelection.add(new JLabel(), gbcDate); 

        panelLeftContent.add(panelDateSelection);
        panelLeftContent.add(Box.createVerticalGlue()); 

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        panelTripSummary.setOpaque(false);
        panelTripSummary.setBorder(BorderFactory.createTitledBorder("Trip Summary (Destinations & Dates)"));
        
        JPanel panelSummaryDates = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panelSummaryDates.setOpaque(false);
        lblSummaryStartDateDisplay = new JLabel("Start: N/A");
        lblSummaryStartDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryEndDateDisplay = new JLabel("End: N/A");
        lblSummaryEndDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panelSummaryDates.add(new JLabel("Selected Dates: "));
        panelSummaryDates.add(lblSummaryStartDateDisplay);
        panelSummaryDates.add(new JLabel(" to "));
        panelSummaryDates.add(lblSummaryEndDateDisplay);
        panelTripSummary.add(panelSummaryDates, BorderLayout.NORTH);

        listModelDestinasiDisplay = new DefaultListModel<>(); 
        listDestinasiSummary = new JList<>(listModelDestinasiDisplay);
        listDestinasiSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listDestinasiSummary.setToolTipText("Previously selected destinations");
        listDestinasiSummary.setEnabled(false); 
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);
        panelRightContent.add(panelTripSummary);

        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        panelEstimatedCost.setOpaque(false);
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder("Estimated Cost"));
        lblTripSummaryTitleInfo = new JLabel("Total Estimated Cost:"); 
        lblTripSummaryTitleInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
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
        splitPaneContent.setOpaque(false);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        JPanel panelMainFooter = new JPanel(new BorderLayout());
        panelMainFooter.setOpaque(false);
        btnPrevStep = new JButton("[< Prev]"); 
        btnPrevStep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnNextStep = new JButton("[Next Step >]");
        btnNextStep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST); 
        panelMainFooter.add(btnNextStep, BorderLayout.EAST); 
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0)); 
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        add(panelCustomTripMain, BorderLayout.CENTER);
    }

    private void setupLogicAndVisuals() {
        JLayeredPane layeredPane = getLayeredPane();
        // SidebarPanel iconOnlySidebar = new SidebarPanel(); 
        // iconOnlySidebar.setBounds(0, 0, 65, Integer.MAX_VALUE); 
        // layeredPane.add(iconOnlySidebar, JLayeredPane.PALETTE_LAYER); 
        
        updateBuildStepLabels(2); 
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        
        txtStartDate.setText(START_DATE_PLACEHOLDER);
        txtStartDate.setForeground(Color.GRAY);
        txtEndDate.setText(END_DATE_PLACEHOLDER);
        txtEndDate.setForeground(Color.GRAY);
        lblSummaryStartDateDisplay.setText("Start: N/A");
        lblSummaryEndDateDisplay.setText("End: N/A");

        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);
        
        txtStartDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtStartDate.getText().equals(START_DATE_PLACEHOLDER)) {
                    txtStartDate.setText("");
                    txtStartDate.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtStartDate.getText().isEmpty()) {
                    txtStartDate.setText(START_DATE_PLACEHOLDER);
                    txtStartDate.setForeground(Color.GRAY);
                    lblSummaryStartDateDisplay.setText("Start: N/A");
                } else {
                    if(!txtStartDate.getText().equals(START_DATE_PLACEHOLDER)){
                        lblSummaryStartDateDisplay.setText("Start: " + txtStartDate.getText());
                    }
                }
            }
        });
        txtEndDate.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtEndDate.getText().equals(END_DATE_PLACEHOLDER)) {
                    txtEndDate.setText("");
                    txtEndDate.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtEndDate.getText().isEmpty()) {
                    txtEndDate.setText(END_DATE_PLACEHOLDER);
                    txtEndDate.setForeground(Color.GRAY);
                    lblSummaryEndDateDisplay.setText("End: N/A");
                } else {
                    if(!txtEndDate.getText().equals(END_DATE_PLACEHOLDER)){
                        lblSummaryEndDateDisplay.setText("End: " + txtEndDate.getText());
                    }
                }
            }
        });
    }
    
    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {
            lblStep1Destinasi, lblStep2Tanggal, lblStep3Transport,
            lblStep4Akomodasi, lblStep5Kegiatan, lblStep6Final
        };
        String[] stepTexts = {
            "1. Destination", "2. Dates", "3. Transport",
            "4. Accommodation", "5. Activities", "6. Final"
        };

        for (int i = 0; i < stepLabels.length; i++) {
            if (stepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                stepLabels[i].setText((isActive ? ACTIVE_STEP : INACTIVE_STEP) + stepTexts[i]);
                stepLabels[i].setFont(new Font("Segoe UI", (isActive ? Font.BOLD : Font.PLAIN), 13));
                stepLabels[i].setForeground(isActive ? new Color(0, 102, 204) : Color.DARK_GRAY);
            }
        }
    }

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        selectedStartDate = txtStartDate.getText().trim();
        selectedEndDate = txtEndDate.getText().trim();
        
        if (selectedStartDate.equals(START_DATE_PLACEHOLDER) || selectedEndDate.equals(END_DATE_PLACEHOLDER) || 
            selectedStartDate.isEmpty() || selectedEndDate.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please select valid start and end dates before saving.", "Cannot Save", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String message = """
            Trip configuration (with dates) saved (simulated).
            Destinations: %s
            Start Date: %s
            End Date: %s
            """.formatted(
                currentDestinations, 
                selectedStartDate, 
                selectedEndDate
            );
        JOptionPane.showMessageDialog(this, message, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        DestinationStep destinationStep = new DestinationStep(); 
        destinationStep.setVisible(true);
        this.dispose(); 
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        selectedStartDate = txtStartDate.getText().trim();
        selectedEndDate = txtEndDate.getText().trim();

        if (selectedStartDate.isEmpty() || selectedStartDate.equals(START_DATE_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Start Date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtStartDate.requestFocus();
            return;
        }
        if (selectedEndDate.isEmpty() || selectedEndDate.equals(END_DATE_PLACEHOLDER)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid End Date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtEndDate.requestFocus();
            return;
        }
        if (selectedStartDate.length() < 8 || selectedEndDate.length() < 8) { 
             JOptionPane.showMessageDialog(this, "Please ensure dates are in a valid format (e.g., YYYY-MM-DD).", "Date Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TransportStep transportStepFrame = new TransportStep(this.currentDestinations, selectedStartDate, selectedEndDate);
        transportStepFrame.setVisible(true);
        this.dispose(); 
    }

    public static void main(String args[]) {
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
             for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) { 
                    lookAndFeel = info.getClassName();
                    break;
                }
            }
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DateStep.class.getName()).log(java.util.logging.Level.INFO, "Could not set Look and Feel", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            List<String> sampleDestinations = new ArrayList<>();
            sampleDestinations.add("Bali, Indonesia");
            sampleDestinations.add("Lombok, Indonesia");
            
            new DateStep(sampleDestinations).setVisible(true);
        });
    }
}