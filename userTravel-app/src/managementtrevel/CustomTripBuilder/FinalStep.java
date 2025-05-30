package managementtrevel.CustomTripBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FinalStep extends JFrame {

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
    private JList<String> listDestinationsDisplay; // Deklarasi menggunakan 'o'
    private JScrollPane scrollPaneDestinationsDisplay;
    private JLabel lblDatesSummaryDisplay;
    private JLabel lblTransportSummaryDisplay;
    private JLabel lblAccommodationSummaryDisplay;
    private JLabel lblActivitiesSummaryDisplay;
    private JList<String> listActivitiesDisplay;
    private JScrollPane scrollPaneActivitiesDisplay;
    
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

    private final String ACTIVE_STEP = "● ";
    private final String INACTIVE_STEP = "○ ";

    public FinalStep(List<String> destinations, String startDate, String endDate, 
                     String transportMode, String transportDetails,
                     String accommodationName, String roomType, String accommodationNotes,
                     List<String> activities) {
        this.currentDestinations = destinations != null ? destinations : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        this.currentActivities = activities != null ? activities : new ArrayList<>();
        
        this.listModelDestinasiDisplay = new DefaultListModel<>();
        this.listModelActivitiesDisplay = new DefaultListModel<>();

        initializeUI();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        setTitle("Custom Trip - Final Summary");
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Final Summary");
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
        panelLeftContent.setBorder(BorderFactory.createTitledBorder("Final Review"));
        
        lblFinalReviewMessage = new JLabel("<html>Please review your complete trip details on the right. <br>You can add any final notes or special requests below.</html>");
        lblFinalReviewMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFinalReviewMessage.setBorder(new EmptyBorder(10,10,10,10));
        panelLeftContent.add(lblFinalReviewMessage);

        txtAreaFinalNotes = new JTextArea(5, 20);
        txtAreaFinalNotes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAreaFinalNotes.setLineWrap(true);
        txtAreaFinalNotes.setWrapStyleWord(true);
        txtAreaFinalNotes.setBorder(BorderFactory.createTitledBorder("Final Notes / Special Requests"));
        scrollPaneFinalNotes = new JScrollPane(txtAreaFinalNotes);
        panelLeftContent.add(scrollPaneFinalNotes);
        panelLeftContent.add(Box.createVerticalGlue()); 

        panelRightContent = new JPanel(new BorderLayout()); 
        panelRightContent.setOpaque(false);

        panelTripSummaryFull = new JPanel();
        panelTripSummaryFull.setLayout(new BoxLayout(panelTripSummaryFull, BoxLayout.Y_AXIS));
        panelTripSummaryFull.setOpaque(false);
        panelTripSummaryFull.setBorder(new EmptyBorder(10,10,10,10)); 

        Font summaryFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font summaryHeaderFont = new Font("Segoe UI", Font.BOLD, 14);
        EmptyBorder summaryItemBorder = new EmptyBorder(5, 0, 5, 0); 

        lblDestinationsSummaryDisplay = new JLabel("Destinations:"); // Penggunaan 'o'
        lblDestinationsSummaryDisplay.setFont(summaryHeaderFont);
        lblDestinationsSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblDestinationsSummaryDisplay);
        listDestinationsDisplay = new JList<>(listModelDestinasiDisplay); // Penggunaan 'o'
        listDestinationsDisplay.setFont(summaryFont);
        listDestinationsDisplay.setEnabled(false);
        listDestinationsDisplay.setBackground(new Color(0,0,0,0)); 
        scrollPaneDestinationsDisplay = new JScrollPane(listDestinationsDisplay); // Penggunaan 'o'
        scrollPaneDestinationsDisplay.setPreferredSize(new Dimension(300, 80)); 
        scrollPaneDestinationsDisplay.setOpaque(false);
        scrollPaneDestinationsDisplay.getViewport().setOpaque(false);
        panelTripSummaryFull.add(scrollPaneDestinationsDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblDatesSummaryDisplay = new JLabel("Dates: Not Set");
        lblDatesSummaryDisplay.setFont(summaryHeaderFont);
        lblDatesSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblDatesSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblTransportSummaryDisplay = new JLabel("Transport: Not Set");
        lblTransportSummaryDisplay.setFont(summaryHeaderFont);
        lblTransportSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblTransportSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblAccommodationSummaryDisplay = new JLabel("Accommodation: Not Set");
        lblAccommodationSummaryDisplay.setFont(summaryHeaderFont);
        lblAccommodationSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblAccommodationSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblActivitiesSummaryDisplay = new JLabel("Activities:");
        lblActivitiesSummaryDisplay.setFont(summaryHeaderFont);
        lblActivitiesSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblActivitiesSummaryDisplay);
        listActivitiesDisplay = new JList<>(listModelActivitiesDisplay);
        listActivitiesDisplay.setFont(summaryFont);
        listActivitiesDisplay.setEnabled(false);
        listActivitiesDisplay.setBackground(new Color(0,0,0,0));
        scrollPaneActivitiesDisplay = new JScrollPane(listActivitiesDisplay);
        scrollPaneActivitiesDisplay.setPreferredSize(new Dimension(300, 100)); 
        scrollPaneActivitiesDisplay.setOpaque(false);
        scrollPaneActivitiesDisplay.getViewport().setOpaque(false);
        panelTripSummaryFull.add(scrollPaneActivitiesDisplay);
        
        scrollPaneTripSummaryFull = new JScrollPane(panelTripSummaryFull);
        scrollPaneTripSummaryFull.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneTripSummaryFull.setBorder(BorderFactory.createTitledBorder("Complete Trip Itinerary"));
        panelRightContent.add(scrollPaneTripSummaryFull, BorderLayout.CENTER);

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
        panelRightContent.add(panelEstimatedCost, BorderLayout.SOUTH);

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(350); 
        splitPaneContent.setResizeWeight(0.35); 
        splitPaneContent.setContinuousLayout(true);
        splitPaneContent.setOpaque(false);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        JPanel panelMainFooter = new JPanel(new BorderLayout());
        panelMainFooter.setOpaque(false);
        btnPrevStep = new JButton("[< Prev]"); 
        btnPrevStep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnFinishTrip = new JButton("[Finish Trip]"); 
        btnFinishTrip.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST); 
        panelMainFooter.add(btnFinishTrip, BorderLayout.EAST); 
        panelMainFooter.setBorder(new EmptyBorder(10,0,0,0)); 
        panelCustomTripMain.add(panelMainFooter, BorderLayout.SOUTH);

        add(panelCustomTripMain, BorderLayout.CENTER);
    }

    private void setupLogicAndVisuals() {
        JLayeredPane layeredPane = getLayeredPane();
        // SidebarPanel iconOnlySidebar = new SidebarPanel(); 
        // iconOnlySidebar.setBounds(0, 0, 65, Integer.MAX_VALUE); 
        // layeredPane.add(iconOnlySidebar, JLayeredPane.PALETTE_LAYER); 
        
        updateBuildStepLabels(6); 
        
        if (currentDestinations != null) {
            for (String dest : currentDestinations) {
                listModelDestinasiDisplay.addElement(dest);
            }
        }
        if (currentActivities != null) {
            for (String activity : currentActivities) {
                listModelActivitiesDisplay.addElement(activity);
            }
        }
        
        lblDatesSummaryDisplay.setText("<html><b>Dates:</b> " + 
            (currentStartDate != null ? currentStartDate : "N/A") + " to " + 
            (currentEndDate != null ? currentEndDate : "N/A") + "</html>");

        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "N/A");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (<i>" + currentTransportDetails + "</i>)";
        }
        lblTransportSummaryDisplay.setText("<html><b>Transport:</b> " + transportInfo + "</html>");
        
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "N/A");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (<i>Room: " + currentRoomType + "</i>)";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - <i>Notes: " + currentAccommodationNotes + "</i>";
        }
        lblAccommodationSummaryDisplay.setText("<html><b>Accommodation:</b> " + accommodationInfo + "</html>");

        if(listModelActivitiesDisplay.isEmpty()){
            listModelActivitiesDisplay.addElement("No specific activities planned.");
        }
        lblActivitiesSummaryDisplay.setText("<html><b>Activities:</b></html>");


        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnPrevStep.addActionListener(this::btnPrevStepActionPerformed);
        btnFinishTrip.addActionListener(this::btnFinishTripActionPerformed);
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
        String finalNotes = txtAreaFinalNotes.getText().trim();
        String message = """
            Complete trip configuration saved (simulated).
            Destinations: %s
            Dates: %s to %s
            Transport: %s%s
            Accommodation: %s%s%s
            Activities: %s
            Final Notes: %s
            """.formatted(
                currentDestinations, currentStartDate, currentEndDate, 
                currentTransportMode, (currentTransportDetails != null && !currentTransportDetails.isEmpty() ? " (" + currentTransportDetails + ")" : ""),
                currentAccommodationName == null || currentAccommodationName.isEmpty() ? "N/A" : currentAccommodationName,
                currentRoomType == null || currentRoomType.isEmpty() ? "" : ", Room: " + currentRoomType,
                currentAccommodationNotes == null || currentAccommodationNotes.isEmpty() ? "" : ", Notes: " + currentAccommodationNotes,
                currentActivities.isEmpty() ? "None" : currentActivities.toString(),
                finalNotes.isEmpty() ? "None" : finalNotes
            );
        JOptionPane.showMessageDialog(this, message, "Trip Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        ActivityStep activityStep = new ActivityStep(
            currentDestinations, currentStartDate, currentEndDate,
            currentTransportMode, currentTransportDetails,
            currentAccommodationName, currentRoomType, currentAccommodationNotes
        );
        activityStep.setVisible(true);
        this.dispose();
    }

    private void btnFinishTripActionPerformed(ActionEvent evt) {
        String finalNotes = txtAreaFinalNotes.getText().trim();
        String confirmationMessage = """
            Thank you for building your trip!
            Your trip is now complete.
            
            Summary:
            Destinations: %s
            Dates: %s - %s
            Transport: %s
            Accommodation: %s
            Activities: %s
            Final Notes: %s
            
            """.formatted(
                currentDestinations.toString(),
                currentStartDate, currentEndDate,
                currentTransportMode + (currentTransportDetails != null && !currentTransportDetails.isEmpty() ? " (" + currentTransportDetails + ")" : ""),
                currentAccommodationName + (currentRoomType != null && !currentRoomType.isEmpty() ? " (Room: " + currentRoomType + ")" : "") + (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty() ? " - Notes: " + currentAccommodationNotes : ""),
                currentActivities.isEmpty() ? "None" : currentActivities.toString(),
                finalNotes.isEmpty() ? "None" : finalNotes
            );

        JOptionPane.showMessageDialog(this, confirmationMessage, "Trip Finalized", JOptionPane.INFORMATION_MESSAGE);
        
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
            java.util.logging.Logger.getLogger(FinalStep.class.getName()).log(java.util.logging.Level.INFO, "Could not set Look and Feel.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            List<String> dest = List.of("Tokyo", "Kyoto");
            List<String> act = List.of("Visit Temple", "Eat Ramen", "Shopping at Shibuya");
            new FinalStep(dest, "2026-01-01", "2026-01-07", 
                          "Shinkansen", "Green Car", 
                          "Ryokan XYZ", "Tatami Room with View", "Near Gion station, late check-in",
                          act).setVisible(true);
        });
    }
}