package managementtrevel.CustomTripBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DestinationStep extends JFrame {

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

    private JPanel panelSelectDestination;
    private JTextField txtfield_destinasi;
    private JButton btnCariDestinasi;
    private JLabel lblHasilPencarianInfo;
    private JButton btnTambahDestinasi;

    private JPanel panelSuggestDestination;
    private JLabel lblSuggestInfo;

    private JPanel panelDestinationOption;
    private JLabel lblOptionInfo;

    private JPanel panelTripSummary;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummary;
    private JButton btnHapusDestinasi;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryDestinationLabel;
    private JLabel lblEstimasiHargaValue;

    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasi;

    private final String ACTIVE_STEP = "● ";
    private final String INACTIVE_STEP = "○ ";
    private final String PLACEHOLDER_TEXT = "Enter destination name";

    public DestinationStep() {
        initializeUI();
        setupLogicAndVisuals();
    }

    private void initializeUI() {
        setTitle("Custom Trip - Destination");
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder");
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

        panelSelectDestination = new JPanel(new GridBagLayout());
        panelSelectDestination.setOpaque(false);
        panelSelectDestination.setBorder(BorderFactory.createTitledBorder("Select Destination"));
        GridBagConstraints gbcSelDest = new GridBagConstraints();
        gbcSelDest.insets = new Insets(5,5,5,5);
        gbcSelDest.fill = GridBagConstraints.HORIZONTAL;

        txtfield_destinasi = new JTextField(20);
        txtfield_destinasi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtfield_destinasi.setToolTipText("Enter the name of your destination city or place");
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 0; gbcSelDest.weightx = 1.0;
        panelSelectDestination.add(txtfield_destinasi, gbcSelDest);

        btnCariDestinasi = new JButton("Search");
        btnCariDestinasi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCariDestinasi.setToolTipText("Search for the entered destination");
        gbcSelDest.gridx = 1; gbcSelDest.gridy = 0; gbcSelDest.weightx = 0;
        panelSelectDestination.add(btnCariDestinasi, gbcSelDest);
        
        lblHasilPencarianInfo = new JLabel("Status: -");
        lblHasilPencarianInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 1; gbcSelDest.weightx = 1.0;
        panelSelectDestination.add(lblHasilPencarianInfo, gbcSelDest);

        btnTambahDestinasi = new JButton("Add (+)");
        btnTambahDestinasi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTambahDestinasi.setToolTipText("Add found destination to trip summary");
        gbcSelDest.gridx = 1; gbcSelDest.gridy = 1; gbcSelDest.weightx = 0;
        panelSelectDestination.add(btnTambahDestinasi, gbcSelDest);
        panelLeftContent.add(panelSelectDestination);

        panelSuggestDestination = new JPanel(new BorderLayout());
        panelSuggestDestination.setOpaque(false);
        panelSuggestDestination.setBorder(BorderFactory.createTitledBorder("Suggest Destination"));
        lblSuggestInfo = new JLabel("Suggestions will appear here...", JLabel.CENTER);
        lblSuggestInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblSuggestInfo.setForeground(Color.GRAY);
        panelSuggestDestination.add(lblSuggestInfo, BorderLayout.CENTER);
        panelSuggestDestination.setPreferredSize(new Dimension(0, 100)); 
        panelLeftContent.add(panelSuggestDestination);

        panelDestinationOption = new JPanel(new BorderLayout());
        panelDestinationOption.setOpaque(false);
        panelDestinationOption.setBorder(BorderFactory.createTitledBorder("Destination Option"));
        lblOptionInfo = new JLabel("Options for selected destination...", JLabel.CENTER);
        lblOptionInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblOptionInfo.setForeground(Color.GRAY);
        panelDestinationOption.add(lblOptionInfo, BorderLayout.CENTER);
        panelDestinationOption.setPreferredSize(new Dimension(0, 100)); 
        panelLeftContent.add(panelDestinationOption);
        panelLeftContent.add(Box.createVerticalGlue());

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        panelTripSummary.setOpaque(false);
        panelTripSummary.setBorder(BorderFactory.createTitledBorder("Trip Summary"));
        
        listModelDestinasi = new DefaultListModel<>(); 
        listDestinasiSummary = new JList<>(listModelDestinasi);
        listDestinasiSummary.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        listDestinasiSummary.setToolTipText("List of currently added destinations");
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);

        btnHapusDestinasi = new JButton("Remove (-)");
        btnHapusDestinasi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnHapusDestinasi.setToolTipText("Remove selected destination from summary");
        JPanel panelHapusBtnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        panelHapusBtnWrapper.setOpaque(false);
        panelHapusBtnWrapper.add(btnHapusDestinasi);
        panelTripSummary.add(panelHapusBtnWrapper, BorderLayout.SOUTH);
        panelRightContent.add(panelTripSummary);

        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        panelEstimatedCost.setOpaque(false);
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder("Estimated Cost"));
        lblTripSummaryDestinationLabel = new JLabel("Total Estimated Cost:"); 
        lblTripSummaryDestinationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryDestinationLabel, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelEstimatedCost.setPreferredSize(new Dimension(0, 60)); 
        panelRightContent.add(panelEstimatedCost);
        panelRightContent.add(Box.createVerticalGlue());

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(400); 
        splitPaneContent.setResizeWeight(0.45); 
        splitPaneContent.setContinuousLayout(true);
        splitPaneContent.setOpaque(false);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        JPanel panelMainFooter = new JPanel(new BorderLayout());
        panelMainFooter.setOpaque(false);
        btnNextStep = new JButton("[Next Step >]");
        btnNextStep.setFont(new Font("Segoe UI", Font.PLAIN, 12));
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
        
        updateBuildStepLabels(1); 
        
        btnCariDestinasi.addActionListener(this::btnCariDestinasiActionPerformed);
        btnTambahDestinasi.addActionListener(this::btnTambahDestinasiActionPerformed);
        btnHapusDestinasi.addActionListener(this::btnHapusDestinasiActionPerformed);
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);
        
        txtfield_destinasi.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtfield_destinasiFocusGained(e);
            }
            @Override
            public void focusLost(FocusEvent e) {
                txtfield_destinasiFocusLost(e);
            }
        });
        txtfield_destinasi.addActionListener(this::txtfield_destinasiActionPerformed);

        lblHasilPencarianInfo.setText("Status: -"); 
        btnTambahDestinasi.setEnabled(false); 
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
        if (listModelDestinasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one destination before saving.", "Cannot Save Empty Trip", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Trip saved (simulated). Destinations: " + listModelDestinasi.toString(), "Save Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void txtfield_destinasiFocusGained(FocusEvent evt) {
        if (txtfield_destinasi.getText().equals(PLACEHOLDER_TEXT)) {
            txtfield_destinasi.setText("");
            txtfield_destinasi.setForeground(Color.BLACK);
        }
    }

    private void txtfield_destinasiFocusLost(FocusEvent evt) {
        if (txtfield_destinasi.getText().isEmpty()) {
            txtfield_destinasi.setText(PLACEHOLDER_TEXT);
            txtfield_destinasi.setForeground(Color.GRAY);
        }
    }

    private void txtfield_destinasiActionPerformed(ActionEvent evt) {
        btnCariDestinasi.doClick();
    }

    private void btnCariDestinasiActionPerformed(ActionEvent evt) {
        String destinasiDicari = txtfield_destinasi.getText().trim();
        if (destinasiDicari.isEmpty() || destinasiDicari.equals(PLACEHOLDER_TEXT)) {
            JOptionPane.showMessageDialog(this, "Please enter a destination name to search.", "Input Empty", JOptionPane.WARNING_MESSAGE);
            lblHasilPencarianInfo.setText("Status: Please enter destination");
            lblHasilPencarianInfo.setForeground(Color.RED);
            btnTambahDestinasi.setEnabled(false);
            return;
        }

        String hasilPencarianValid = null;
        if (destinasiDicari.equalsIgnoreCase("Bali")) hasilPencarianValid = "Bali, Indonesia";
        else if (destinasiDicari.equalsIgnoreCase("Jakarta")) hasilPencarianValid = "Jakarta, DKI Jakarta";
        else if (destinasiDicari.equalsIgnoreCase("Bandung")) hasilPencarianValid = "Bandung, Jawa Barat";
        else if (!destinasiDicari.isEmpty()) hasilPencarianValid = destinasiDicari + " (User Input)";

        if (hasilPencarianValid != null) {
            lblHasilPencarianInfo.setText("Found: " + hasilPencarianValid);
            lblHasilPencarianInfo.setForeground(new Color(0,128,0));
            btnTambahDestinasi.setEnabled(true);
        } else {
            lblHasilPencarianInfo.setText("Not found: " + destinasiDicari);
            lblHasilPencarianInfo.setForeground(Color.RED);
            btnTambahDestinasi.setEnabled(false);
        }
    }

    private void btnTambahDestinasiActionPerformed(ActionEvent evt) {
        String destinasiInfo = lblHasilPencarianInfo.getText();
        if (!destinasiInfo.startsWith("Found: ")) {
            JOptionPane.showMessageDialog(this, "Please search and find a valid destination first.", "No Valid Destination", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String destinasiDitambahkan = destinasiInfo.substring("Found: ".length());

        if (listModelDestinasi.contains(destinasiDitambahkan)) {
            JOptionPane.showMessageDialog(this, "'" + destinasiDitambahkan + "' is already in your trip summary.", "Destination Already Added", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        listModelDestinasi.addElement(destinasiDitambahkan);
        txtfield_destinasi.setText(PLACEHOLDER_TEXT);
        txtfield_destinasi.setForeground(Color.GRAY);
        lblHasilPencarianInfo.setText("Status: -");
        lblHasilPencarianInfo.setForeground(Color.BLACK);
        btnTambahDestinasi.setEnabled(false);
    }

    private void btnHapusDestinasiActionPerformed(ActionEvent evt) {
        int selectedIndex = listDestinasiSummary.getSelectedIndex();
        if (selectedIndex != -1) {
            listModelDestinasi.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a destination from the summary to remove.", "No Destination Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        if (listModelDestinasi == null || listModelDestinasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                                          "Please add at least one destination to proceed.", 
                                          "Trip Summary Empty", 
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<String> destinationsForNextStep = new ArrayList<>();
        for (int i = 0; i < listModelDestinasi.getSize(); i++) {
            destinationsForNextStep.add(listModelDestinasi.getElementAt(i));
        }
        
        DateStep dateStepFrame = new DateStep(destinationsForNextStep);
        dateStepFrame.setVisible(true);
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
            java.util.logging.Logger.getLogger(DestinationStep.class.getName()).log(java.util.logging.Level.INFO, "Could not set Look and Feel.", ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new DestinationStep().setVisible(true);
        });
    }
}