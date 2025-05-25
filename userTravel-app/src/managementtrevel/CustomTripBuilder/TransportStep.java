package managementtrevel.CustomTripBuilder;

import Asset.SidebarPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TransportStep extends JFrame {

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

    private final String ACTIVE_STEP = "● ";
    private final String INACTIVE_STEP = "○ ";

    public TransportStep(List<String> destinations, String startDate, String endDate) {
        this.currentDestinations = destinations != null ? destinations : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        
        initializeUI();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        setTitle("Custom Trip - Select Transport");
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Select Transport");
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

        panelSelectTransport = new JPanel(new GridBagLayout());
        panelSelectTransport.setOpaque(false);
        panelSelectTransport.setBorder(BorderFactory.createTitledBorder("Select Transport"));
        GridBagConstraints gbcTransport = new GridBagConstraints();
        gbcTransport.insets = new Insets(5, 5, 5, 5);
        gbcTransport.anchor = GridBagConstraints.WEST;

        lblTransportMode = new JLabel("Transport Mode:");
        lblTransportMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcTransport.gridx = 0; gbcTransport.gridy = 0;
        panelSelectTransport.add(lblTransportMode, gbcTransport);

        String[] transportOptions = {"Flight", "Train", "Bus", "Car Rental", "Private Car", "Other"};
        cmbTransportMode = new JComboBox<>(transportOptions);
        cmbTransportMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcTransport.gridx = 1; gbcTransport.gridy = 0; gbcTransport.fill = GridBagConstraints.HORIZONTAL; gbcTransport.weightx = 1.0;
        panelSelectTransport.add(cmbTransportMode, gbcTransport);

        lblTransportDetails = new JLabel("Details/Notes:");
        lblTransportDetails.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcTransport.gridx = 0; gbcTransport.gridy = 1; gbcTransport.fill = GridBagConstraints.NONE; gbcTransport.weightx = 0;
        panelSelectTransport.add(lblTransportDetails, gbcTransport);

        txtTransportDetails = new JTextArea(5, 20); 
        txtTransportDetails.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTransportDetails.setLineWrap(true);
        txtTransportDetails.setWrapStyleWord(true);
        txtTransportDetails.setToolTipText("Enter flight numbers, pickup locations, company names, etc.");
        scrollPaneTransportDetails = new JScrollPane(txtTransportDetails);
        scrollPaneTransportDetails.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gbcTransport.gridx = 1; gbcTransport.gridy = 1; gbcTransport.fill = GridBagConstraints.BOTH; gbcTransport.weightx = 1.0; gbcTransport.weighty = 0.5; 
        panelSelectTransport.add(scrollPaneTransportDetails, gbcTransport);
        
        panelSelectTransport.setPreferredSize(new Dimension(0, 180)); 
        panelLeftContent.add(panelSelectTransport);

        panelSuggestTransport = new JPanel(new BorderLayout());
        panelSuggestTransport.setOpaque(false);
        panelSuggestTransport.setBorder(BorderFactory.createTitledBorder("Suggest Transport"));
        lblSuggestTransportInfo = new JLabel("Transport suggestions will appear here...", JLabel.CENTER);
        lblSuggestTransportInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSuggestTransportInfo.setForeground(Color.GRAY);
        panelSuggestTransport.add(lblSuggestTransportInfo, BorderLayout.CENTER);
        panelSuggestTransport.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelSuggestTransport);

        panelTransportOption = new JPanel(new BorderLayout());
        panelTransportOption.setOpaque(false);
        panelTransportOption.setBorder(BorderFactory.createTitledBorder("Transport Option"));
        lblTransportOptionInfo = new JLabel("Options for selected transport...", JLabel.CENTER);
        lblTransportOptionInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblTransportOptionInfo.setForeground(Color.GRAY);
        panelTransportOption.add(lblTransportOptionInfo, BorderLayout.CENTER);
        panelTransportOption.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelTransportOption);
        panelLeftContent.add(Box.createVerticalGlue());

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        panelTripSummary.setOpaque(false);
        panelTripSummary.setBorder(BorderFactory.createTitledBorder("Trip Summary"));
        
        JPanel panelSummaryDates = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panelSummaryDates.setOpaque(false);
        lblSummaryStartDateDisplay = new JLabel("Start: N/A");
        lblSummaryStartDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryEndDateDisplay = new JLabel("End: N/A");
        lblSummaryEndDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panelSummaryDates.add(new JLabel("Dates: "));
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
        SidebarPanel iconOnlySidebar = new SidebarPanel(); 
        iconOnlySidebar.setBounds(0, 0, 65, Integer.MAX_VALUE); 
        layeredPane.add(iconOnlySidebar, JLayeredPane.PALETTE_LAYER); 
        
        updateBuildStepLabels(3); 
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        lblSummaryStartDateDisplay.setText("Start: " + (currentStartDate != null ? currentStartDate : "N/A"));
        lblSummaryEndDateDisplay.setText("End: " + (currentEndDate != null ? currentEndDate : "N/A"));
        
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
        String transportMode = "";
        if (cmbTransportMode.getSelectedItem() != null) {
            transportMode = cmbTransportMode.getSelectedItem().toString();
        }
        String transportDetails = txtTransportDetails.getText().trim();
        
        String message = """
            Trip configuration (with transport) saved (simulated).
            Destinations: %s
            Dates: %s to %s
            Transport: %s%s
            """.formatted(
                currentDestinations, 
                currentStartDate, 
                currentEndDate, 
                transportMode, 
                (transportDetails.isEmpty() ? "" : " (" + transportDetails + ")")
            );
        JOptionPane.showMessageDialog(this, message, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        DateStep dateStep = new DateStep(currentDestinations != null ? currentDestinations : new ArrayList<>());
        dateStep.setVisible(true);
        this.dispose();
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        String transportMode = "";
        if (cmbTransportMode.getSelectedItem() != null) {
            transportMode = cmbTransportMode.getSelectedItem().toString();
        }
        String transportDetails = txtTransportDetails.getText().trim();

        if (transportMode.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Please select a transport mode to proceed.", "Transport Not Selected", JOptionPane.WARNING_MESSAGE);
            cmbTransportMode.requestFocus();
            return;
        }
        
        AccommodationStep accommodationStepFrame = new AccommodationStep(
            this.currentDestinations, 
            this.currentStartDate, 
            this.currentEndDate,
            transportMode,
            transportDetails
        );
        accommodationStepFrame.setVisible(true);
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
            java.util.logging.Logger.getLogger(TransportStep.class.getName()).log(java.util.logging.Level.INFO, "Could not set Look and Feel.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            List<String> sampleDestinations = new ArrayList<>();
            sampleDestinations.add("Kyoto, Japan");
            String sampleStartDate = "2026-03-15";
            String sampleEndDate = "2026-03-22";
            
            new TransportStep(sampleDestinations, sampleStartDate, sampleEndDate).setVisible(true);
        });
    }
}