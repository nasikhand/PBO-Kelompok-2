package managementtrevel.CustomTripBuilder;

// import Asset.SidebarPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ActivityStep extends JFrame {

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

    private final String ACTIVE_STEP = "● ";
    private final String INACTIVE_STEP = "○ ";

    public ActivityStep(List<String> destinations, String startDate, String endDate, 
                        String transportMode, String transportDetails,
                        String accommodationName, String roomType, String accommodationNotes) {
        this.currentDestinations = destinations != null ? destinations : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        
        initializeUI();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        setTitle("Custom Trip - Add Activities");
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Add Activities");
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

        panelAddActivity = new JPanel(new GridBagLayout());
        panelAddActivity.setOpaque(false);
        panelAddActivity.setBorder(BorderFactory.createTitledBorder("Add Activity"));
        GridBagConstraints gbcAct = new GridBagConstraints();
        gbcAct.insets = new Insets(5,5,5,5);
        gbcAct.anchor = GridBagConstraints.WEST;

        lblActivityName = new JLabel("Activity Name/Description:");
        lblActivityName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAct.gridx = 0; gbcAct.gridy = 0; gbcAct.gridwidth = 2;
        panelAddActivity.add(lblActivityName, gbcAct);

        txtActivityName = new JTextField(25);
        txtActivityName.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAct.gridx = 0; gbcAct.gridy = 1; gbcAct.gridwidth = 1; gbcAct.fill = GridBagConstraints.HORIZONTAL; gbcAct.weightx = 1.0;
        panelAddActivity.add(txtActivityName, gbcAct);

        btnAddActivityToList = new JButton("Add to List");
        btnAddActivityToList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAct.gridx = 1; gbcAct.gridy = 1; gbcAct.gridwidth = 1; gbcAct.fill = GridBagConstraints.NONE; gbcAct.weightx = 0;
        panelAddActivity.add(btnAddActivityToList, gbcAct);
        
        listModelAddedActivities = new DefaultListModel<>();
        listAddedActivities = new JList<>(listModelAddedActivities);
        listAddedActivities.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listAddedActivities.setVisibleRowCount(5);
        scrollPaneAddedActivities = new JScrollPane(listAddedActivities);
        gbcAct.gridx = 0; gbcAct.gridy = 2; gbcAct.gridwidth = 2; gbcAct.fill = GridBagConstraints.BOTH; gbcAct.weighty = 1.0;
        panelAddActivity.add(scrollPaneAddedActivities, gbcAct);

        btnRemoveActivityFromList = new JButton("Remove Selected");
        btnRemoveActivityFromList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcAct.gridx = 0; gbcAct.gridy = 3; gbcAct.gridwidth = 2; gbcAct.anchor = GridBagConstraints.EAST; gbcAct.fill = GridBagConstraints.NONE; gbcAct.weighty = 0;
        panelAddActivity.add(btnRemoveActivityFromList, gbcAct);

        panelAddActivity.setPreferredSize(new Dimension(0, 220));
        panelLeftContent.add(panelAddActivity);

        panelSuggestActivity = new JPanel(new BorderLayout());
        panelSuggestActivity.setOpaque(false);
        panelSuggestActivity.setBorder(BorderFactory.createTitledBorder("Suggest Activity"));
        lblSuggestActivityInfo = new JLabel("Activity suggestions will appear here...", JLabel.CENTER);
        lblSuggestActivityInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSuggestActivityInfo.setForeground(Color.GRAY);
        panelSuggestActivity.add(lblSuggestActivityInfo, BorderLayout.CENTER);
        panelSuggestActivity.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelSuggestActivity);

        panelActivityOption = new JPanel(new BorderLayout());
        panelActivityOption.setOpaque(false);
        panelActivityOption.setBorder(BorderFactory.createTitledBorder("Activity Option"));
        lblActivityOptionInfo = new JLabel("Options for selected activity...", JLabel.CENTER);
        lblActivityOptionInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblActivityOptionInfo.setForeground(Color.GRAY);
        panelActivityOption.add(lblActivityOptionInfo, BorderLayout.CENTER);
        panelActivityOption.setPreferredSize(new Dimension(0, 100));
        panelLeftContent.add(panelActivityOption);
        panelLeftContent.add(Box.createVerticalGlue());

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        panelTripSummary.setOpaque(false);
        panelTripSummary.setBorder(BorderFactory.createTitledBorder("Trip Summary"));
        
        JPanel panelSummaryDetails = new JPanel();
        panelSummaryDetails.setLayout(new BoxLayout(panelSummaryDetails, BoxLayout.Y_AXIS));
        panelSummaryDetails.setOpaque(false);
        panelSummaryDetails.setBorder(new EmptyBorder(5,5,5,5));
        
        lblSummaryStartDateDisplay = new JLabel("Start: N/A");
        lblSummaryStartDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryStartDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryStartDateDisplay);
        
        lblSummaryEndDateDisplay = new JLabel("End: N/A");
        lblSummaryEndDateDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryEndDateDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryEndDateDisplay);

        lblSummaryTransportDisplay = new JLabel("Transport: N/A");
        lblSummaryTransportDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryTransportDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryTransportDisplay);

        lblSummaryAccommodationDisplay = new JLabel("Accommodation: N/A");
        lblSummaryAccommodationDisplay.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSummaryAccommodationDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelSummaryDetails.add(lblSummaryAccommodationDisplay);
        
        panelSummaryDetails.add(new JSeparator(SwingConstants.HORIZONTAL));
        JLabel lblDestinationsTitle = new JLabel("Destinations:");
        lblDestinationsTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblDestinationsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDestinationsTitle.setBorder(new EmptyBorder(3,0,3,0));
        panelSummaryDetails.add(lblDestinationsTitle);

        panelTripSummary.add(panelSummaryDetails, BorderLayout.NORTH);

        listModelDestinasiDisplay = new DefaultListModel<>(); 
        listDestinasiSummaryDisplay = new JList<>(listModelDestinasiDisplay);
        listDestinasiSummaryDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listDestinasiSummaryDisplay.setEnabled(false); 
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummaryDisplay);
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
        
        updateBuildStepLabels(5); 
        
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
        lblSummaryTransportDisplay.setText("Transport: " + transportInfo);
        
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "N/A");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (Room: " + currentRoomType + ")";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - Notes: " + currentAccommodationNotes;
        }
        lblSummaryAccommodationDisplay.setText("Accommodation: " + accommodationInfo);

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
    
    private void btnAddActivityToListActionPerformed(ActionEvent evt) {
        String activity = txtActivityName.getText().trim();
        if (!activity.isEmpty()) {
            if (listModelAddedActivities != null) { // Pastikan model sudah diinisialisasi
                listModelAddedActivities.addElement(activity);
                txtActivityName.setText(""); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter an activity description.", "Input Empty", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnRemoveActivityFromListActionPerformed(ActionEvent evt) {
        if (listAddedActivities != null && listModelAddedActivities != null) {
            int selectedIndex = listAddedActivities.getSelectedIndex();
            if (selectedIndex != -1) {
                listModelAddedActivities.remove(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an activity from the list to remove.", "No Activity Selected", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void btnSaveTripActionPerformed(ActionEvent evt) {
        List<String> activities = new ArrayList<>();
        if (listModelAddedActivities != null) {
            for (int i = 0; i < listModelAddedActivities.getSize(); i++) {
                activities.add(listModelAddedActivities.getElementAt(i));
            }
        }

        String message = """
            Trip configuration (with activities) saved (simulated).
            Destinations: %s
            Dates: %s to %s
            Transport: %s%s
            Accommodation: %s%s%s
            Activities: %s
            """.formatted(
                currentDestinations, 
                currentStartDate, 
                currentEndDate, 
                currentTransportMode,
                (currentTransportDetails != null && !currentTransportDetails.isEmpty() ? " (" + currentTransportDetails + ")" : ""),
                currentAccommodationName == null || currentAccommodationName.isEmpty() ? "N/A" : currentAccommodationName,
                currentRoomType == null || currentRoomType.isEmpty() ? "" : ", Room: " + currentRoomType,
                currentAccommodationNotes == null || currentAccommodationNotes.isEmpty() ? "" : ", Notes: " + currentAccommodationNotes,
                activities.isEmpty() ? "None" : activities.toString()
            );
        JOptionPane.showMessageDialog(this, message, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        AccommodationStep accommodationStep = new AccommodationStep(
            currentDestinations, 
            currentStartDate, 
            currentEndDate,
            currentTransportMode,
            currentTransportDetails
        );
        accommodationStep.setVisible(true);
        this.dispose();
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        List<String> activities = new ArrayList<>();
        if (listModelAddedActivities != null) {
            for (int i = 0; i < listModelAddedActivities.getSize(); i++) {
                activities.add(listModelAddedActivities.getElementAt(i));
            }
        }
        
        FinalStep finalStepFrame = new FinalStep(
            this.currentDestinations, 
            this.currentStartDate, 
            this.currentEndDate,
            this.currentTransportMode,
            this.currentTransportDetails,
            this.currentAccommodationName,
            this.currentRoomType,
            this.currentAccommodationNotes,
            activities
        );
        finalStepFrame.setVisible(true);
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
            java.util.logging.Logger.getLogger(ActivityStep.class.getName()).log(java.util.logging.Level.INFO, "Could not set Look and Feel.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            List<String> sampleDestinations = new ArrayList<>();
            sampleDestinations.add("Paris, France");
            String sampleStartDate = "2026-07-10";
            String sampleEndDate = "2026-07-17";
            String sampleTransportMode = "Eurostar";
            String sampleTransportDetails = "Business Class, Coach 5";
            String sampleAccoName = "Hotel Eiffel";
            String sampleRoomType = "Suite with View";
            String sampleAccoNotes = "Late check-in requested";
            
            new ActivityStep(sampleDestinations, sampleStartDate, sampleEndDate, 
                             sampleTransportMode, sampleTransportDetails,
                             sampleAccoName, sampleRoomType, sampleAccoNotes).setVisible(true);
        });
    }
}