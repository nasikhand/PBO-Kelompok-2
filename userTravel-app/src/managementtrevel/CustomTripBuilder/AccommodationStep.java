package managementtrevel.CustomTripBuilder;

// import Asset.SidebarPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AccommodationStep extends JFrame {

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

    private final String ACTIVE_STEP = "● ";
    private final String INACTIVE_STEP = "○ ";

    public AccommodationStep(List<String> destinations, String startDate, String endDate, String transportMode, String transportDetails) {
        this.currentDestinations = destinations != null ? destinations : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        
        initializeUI();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        setTitle("Custom Trip - Select Accommodation");
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Select Accommodation");
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

        panelSelectAccommodation = new JPanel(new GridBagLayout());
        panelSelectAccommodation.setOpaque(false);
        panelSelectAccommodation.setBorder(BorderFactory.createTitledBorder("Select Accommodation"));
        GridBagConstraints gbcAcc = new GridBagConstraints();
        gbcAcc.insets = new Insets(5, 5, 5, 5);
        gbcAcc.anchor = GridBagConstraints.WEST;

        lblAccommodationName = new JLabel("Accommodation Name:");
        lblAccommodationName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAcc.gridx = 0; gbcAcc.gridy = 0;
        panelSelectAccommodation.add(lblAccommodationName, gbcAcc);

        txtAccommodationName = new JTextField(20);
        txtAccommodationName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAcc.gridx = 1; gbcAcc.gridy = 0; gbcAcc.fill = GridBagConstraints.HORIZONTAL; gbcAcc.weightx = 1.0;
        panelSelectAccommodation.add(txtAccommodationName, gbcAcc);

        lblRoomType = new JLabel("Room Type/Preference:");
        lblRoomType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAcc.gridx = 0; gbcAcc.gridy = 1; gbcAcc.fill = GridBagConstraints.NONE; gbcAcc.weightx = 0;
        panelSelectAccommodation.add(lblRoomType, gbcAcc);

        txtRoomType = new JTextField(20);
        txtRoomType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAcc.gridx = 1; gbcAcc.gridy = 1; gbcAcc.fill = GridBagConstraints.HORIZONTAL; gbcAcc.weightx = 1.0;
        panelSelectAccommodation.add(txtRoomType, gbcAcc);
        
        lblAccommodationNotes = new JLabel("Notes:");
        lblAccommodationNotes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAcc.gridx = 0; gbcAcc.gridy = 2;
        panelSelectAccommodation.add(lblAccommodationNotes, gbcAcc);

        txtAccommodationNotes = new JTextArea(4, 20);
        txtAccommodationNotes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAccommodationNotes.setLineWrap(true);
        txtAccommodationNotes.setWrapStyleWord(true);
        scrollPaneAccommodationNotes = new JScrollPane(txtAccommodationNotes);
        scrollPaneAccommodationNotes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gbcAcc.gridx = 1; gbcAcc.gridy = 2; gbcAcc.fill = GridBagConstraints.BOTH; gbcAcc.weighty = 0.5;
        panelSelectAccommodation.add(scrollPaneAccommodationNotes, gbcAcc);
        
        panelSelectAccommodation.setPreferredSize(new Dimension(0, 200)); 
        panelLeftContent.add(panelSelectAccommodation);

        panelSuggestAccommodation = new JPanel(new BorderLayout());
        panelSuggestAccommodation.setOpaque(false);
        panelSuggestAccommodation.setBorder(BorderFactory.createTitledBorder("Suggest Accommodation"));
        lblSuggestAccommodationInfo = new JLabel("Accommodation suggestions...", JLabel.CENTER);
        lblSuggestAccommodationInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSuggestAccommodationInfo.setForeground(Color.GRAY);
        panelSuggestAccommodation.add(lblSuggestAccommodationInfo, BorderLayout.CENTER);
        panelSuggestAccommodation.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelSuggestAccommodation);

        panelAccommodationOption = new JPanel(new BorderLayout());
        panelAccommodationOption.setOpaque(false);
        panelAccommodationOption.setBorder(BorderFactory.createTitledBorder("Accommodation Option"));
        lblAccommodationOptionInfo = new JLabel("Options for selected accommodation...", JLabel.CENTER);
        lblAccommodationOptionInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblAccommodationOptionInfo.setForeground(Color.GRAY);
        panelAccommodationOption.add(lblAccommodationOptionInfo, BorderLayout.CENTER);
        panelAccommodationOption.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelAccommodationOption);
        panelLeftContent.add(Box.createVerticalGlue());

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        panelTripSummary.setOpaque(false);
        panelTripSummary.setBorder(BorderFactory.createTitledBorder("Trip Summary"));
        
        JPanel panelSummaryDetails = new JPanel();
        panelSummaryDetails.setLayout(new BoxLayout(panelSummaryDetails, BoxLayout.Y_AXIS));
        panelSummaryDetails.setOpaque(false);
        panelSummaryDetails.setBorder(new EmptyBorder(2,5,2,5)); // Adjusted padding
        
        lblSummaryStartDateDisplay = new JLabel("Start: N/A");
        lblSummaryStartDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryStartDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryStartDateDisplay);
        
        lblSummaryEndDateDisplay = new JLabel("End: N/A");
        lblSummaryEndDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryEndDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryEndDateDisplay);

        lblSummaryTransportModeDisplay = new JLabel("Transport: N/A");
        lblSummaryTransportModeDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryTransportModeDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryTransportModeDisplay);
        
        panelSummaryDetails.add(new JSeparator(SwingConstants.HORIZONTAL));
        JLabel lblDestinationsTitle = new JLabel("Destinations:");
        lblDestinationsTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDestinationsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDestinationsTitle.setBorder(new EmptyBorder(3,0,3,0));
        panelSummaryDetails.add(lblDestinationsTitle);


        panelTripSummary.add(panelSummaryDetails, BorderLayout.NORTH);

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
        
        updateBuildStepLabels(4); 
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        lblSummaryStartDateDisplay.setText("Start: " + (currentStartDate != null ? currentStartDate : "N/A"));
        lblSummaryEndDateDisplay.setText("End: " + (currentEndDate != null ? currentEndDate : "N/A"));
        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "N/A");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (" + currentTransportDetails + ")";
        }
        lblSummaryTransportModeDisplay.setText("Transport: " + transportInfo);
        
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
        String accommodationName = txtAccommodationName.getText().trim();
        String roomType = txtRoomType.getText().trim();
        String accommodationNotes = txtAccommodationNotes.getText().trim();

        String message = """
            Trip configuration (with accommodation) saved (simulated).
            Destinations: %s
            Dates: %s to %s
            Transport: %s%s
            Accommodation: %s%s%s
            """.formatted(
                currentDestinations, 
                currentStartDate, 
                currentEndDate, 
                currentTransportMode,
                (currentTransportDetails != null && !currentTransportDetails.isEmpty() ? " (" + currentTransportDetails + ")" : ""),
                accommodationName.isEmpty() ? "Not specified" : accommodationName,
                roomType.isEmpty() ? "" : ", Room: " + roomType,
                accommodationNotes.isEmpty() ? "" : ", Notes: " + accommodationNotes
            );
        JOptionPane.showMessageDialog(this, message, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        TransportStep transportStep = new TransportStep(
            currentDestinations != null ? currentDestinations : new ArrayList<>(), 
            currentStartDate, 
            currentEndDate
        );
        transportStep.setVisible(true);
        this.dispose();
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        String accommodationName = txtAccommodationName.getText().trim();
        String roomType = txtRoomType.getText().trim();
        String accommodationNotes = txtAccommodationNotes.getText().trim();
        
        ActivityStep activityStepFrame = new ActivityStep(
            this.currentDestinations, 
            this.currentStartDate, 
            this.currentEndDate,
            this.currentTransportMode,
            this.currentTransportDetails,
            accommodationName,
            roomType,
            accommodationNotes
        );
        activityStepFrame.setVisible(true);
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
            java.util.logging.Logger.getLogger(AccommodationStep.class.getName()).log(java.util.logging.Level.INFO, "Could not set Look and Feel.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            List<String> sampleDestinations = new ArrayList<>();
            sampleDestinations.add("Kyoto, Japan");
            String sampleStartDate = "2026-03-15";
            String sampleEndDate = "2026-03-22";
            String sampleTransportMode = "Flight";
            String sampleTransportDetails = "JL707, Window Seat";
            
            new AccommodationStep(sampleDestinations, sampleStartDate, sampleEndDate, sampleTransportMode, sampleTransportDetails).setVisible(true);
        });
    }
}