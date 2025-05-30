package managementtrevel.CustomTripBuilder;

import Asset.AppTheme; // Impor AppTheme Anda
import managementtrevel.MainAppFrame; // Impor MainAppFrame

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

// Mengubah nama kelas dan extends JPanel
public class PanelFinalStep extends JPanel {

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
    private JButton btnSaveTrip; // Tombol ini mungkin lebih cocok menjadi "Konfirmasi Pesanan" atau sejenisnya

    private JLabel lblFinalReviewMessage;
    private JTextArea txtAreaFinalNotes;
    private JScrollPane scrollPaneFinalNotes;

    private JPanel panelTripSummaryFull; // Panel yang akan di-scroll
    private JScrollPane scrollPaneTripSummaryFull; // ScrollPane untuk panel summary

    private JLabel lblDestinationsSummaryDisplay;
    private JList<String> listDestinationsDisplay;
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
    private JButton btnFinishTrip; // Tombol untuk menyelesaikan dan mungkin memesan

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

    private final String ACTIVE_STEP_ICON = "● ";
    private final String INACTIVE_STEP_ICON = "○ ";

    private JComponent panelMainFooter;

    private JComponent panelMainHeader;

    // Konstruktor diubah untuk menerima MainAppFrame dan semua data trip
    public PanelFinalStep(MainAppFrame mainAppFrame, List<String> destinations, String startDate, String endDate, 
                                String transportMode, String transportDetails,
                                String accommodationName, String roomType, String accommodationNotes,
                                List<String> activities) {
        this.mainAppFrame = mainAppFrame;
        this.currentDestinations = destinations != null ? new ArrayList<>(destinations) : new ArrayList<>();
        this.currentStartDate = startDate;
        this.currentEndDate = endDate;
        this.currentTransportMode = transportMode;
        this.currentTransportDetails = transportDetails;
        this.currentAccommodationName = accommodationName;
        this.currentRoomType = roomType;
        this.currentAccommodationNotes = accommodationNotes;
        this.currentActivities = activities != null ? new ArrayList<>(activities) : new ArrayList<>();
        
        this.listModelDestinasiDisplay = new DefaultListModel<>();
        this.listModelActivitiesDisplay = new DefaultListModel<>();

        initializeUI();
        applyAppTheme(); // Terapkan tema setelah komponen diinisialisasi
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
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder - Ringkasan Akhir"); // Judul disesuaikan
        btnSaveTrip = new JButton("Simpan Draf Trip"); // Diubah dari [Save]
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setBorder(BorderFactory.createTitledBorder("Catatan Akhir & Konfirmasi"));
        
        lblFinalReviewMessage = new JLabel("<html>Silakan tinjau ringkasan lengkap perjalanan Anda di sebelah kanan. <br>Anda dapat menambahkan catatan akhir atau permintaan khusus di bawah ini sebelum menyelesaikan.</html>");
        lblFinalReviewMessage.setBorder(new EmptyBorder(10,10,15,10));
        panelLeftContent.add(lblFinalReviewMessage);

        txtAreaFinalNotes = new JTextArea(5, 20);
        txtAreaFinalNotes.setLineWrap(true);
        txtAreaFinalNotes.setWrapStyleWord(true);
        // Border untuk JTextArea akan diatur di applyAppTheme
        scrollPaneFinalNotes = new JScrollPane(txtAreaFinalNotes);
        scrollPaneFinalNotes.setPreferredSize(new Dimension(0, 150)); // Beri tinggi preferensi
        panelLeftContent.add(scrollPaneFinalNotes);
        panelLeftContent.add(Box.createVerticalGlue()); 

        panelRightContent = new JPanel(new BorderLayout()); 

        panelTripSummaryFull = new JPanel();
        panelTripSummaryFull.setLayout(new BoxLayout(panelTripSummaryFull, BoxLayout.Y_AXIS));
        panelTripSummaryFull.setBorder(new EmptyBorder(10,10,10,10)); 

        Font summaryFont = AppTheme.FONT_PRIMARY_DEFAULT; // Menggunakan font dari AppTheme
        Font summaryHeaderFont = AppTheme.FONT_SUBTITLE; // Menggunakan font dari AppTheme
        EmptyBorder summaryItemBorder = new EmptyBorder(5, 0, 8, 0); 

        lblDestinationsSummaryDisplay = new JLabel("Destinasi:");
        lblDestinationsSummaryDisplay.setFont(summaryHeaderFont);
        lblDestinationsSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblDestinationsSummaryDisplay);
        listDestinationsDisplay = new JList<>(listModelDestinasiDisplay);
        listDestinationsDisplay.setFont(summaryFont);
        listDestinationsDisplay.setEnabled(false);
        scrollPaneDestinationsDisplay = new JScrollPane(listDestinationsDisplay);
        scrollPaneDestinationsDisplay.setPreferredSize(new Dimension(300, 80)); 
        panelTripSummaryFull.add(scrollPaneDestinationsDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblDatesSummaryDisplay = new JLabel("Tanggal:");
        lblDatesSummaryDisplay.setFont(summaryHeaderFont);
        lblDatesSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblDatesSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblTransportSummaryDisplay = new JLabel("Transportasi:");
        lblTransportSummaryDisplay.setFont(summaryHeaderFont);
        lblTransportSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblTransportSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblAccommodationSummaryDisplay = new JLabel("Akomodasi:");
        lblAccommodationSummaryDisplay.setFont(summaryHeaderFont);
        lblAccommodationSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblAccommodationSummaryDisplay);
        panelTripSummaryFull.add(Box.createRigidArea(new Dimension(0,10)));

        lblActivitiesSummaryDisplay = new JLabel("Kegiatan:");
        lblActivitiesSummaryDisplay.setFont(summaryHeaderFont);
        lblActivitiesSummaryDisplay.setBorder(summaryItemBorder);
        panelTripSummaryFull.add(lblActivitiesSummaryDisplay);
        listActivitiesDisplay = new JList<>(listModelActivitiesDisplay);
        listActivitiesDisplay.setFont(summaryFont);
        listActivitiesDisplay.setEnabled(false);
        scrollPaneActivitiesDisplay = new JScrollPane(listActivitiesDisplay);
        scrollPaneActivitiesDisplay.setPreferredSize(new Dimension(300, 100)); 
        panelTripSummaryFull.add(scrollPaneActivitiesDisplay);
        
        scrollPaneTripSummaryFull = new JScrollPane(panelTripSummaryFull);
        scrollPaneTripSummaryFull.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelRightContent.add(scrollPaneTripSummaryFull, BorderLayout.CENTER);

        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        lblTripSummaryTitleInfo = new JLabel("Total Estimasi Biaya:"); 
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryTitleInfo, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelEstimatedCost.setPreferredSize(new Dimension(0, 60)); 
        panelRightContent.add(panelEstimatedCost, BorderLayout.SOUTH);

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(380); 
        splitPaneContent.setResizeWeight(0.4); 
        splitPaneContent.setContinuousLayout(true);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        JPanel panelMainFooter = new JPanel(new BorderLayout());
        btnPrevStep = new JButton("< Kembali ke Kegiatan"); 
        btnFinishTrip = new JButton("Selesaikan & Pesan Trip"); 
        panelMainFooter.add(btnPrevStep, BorderLayout.WEST); 
        panelMainFooter.add(btnFinishTrip, BorderLayout.EAST); 
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
        styleSecondaryButton(btnSaveTrip, "Simpan Draf");

        panelLeftContent.setOpaque(false); // Agar background panelCustomTripMain terlihat
        panelRightContent.setOpaque(false);
        splitPaneContent.setOpaque(false);
        splitPaneContent.setBorder(null);

        Font titledBorderFont = AppTheme.FONT_SUBTITLE;
        Color titledBorderColor = AppTheme.PRIMARY_BLUE_DARK;

        panelLeftContent.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Catatan Akhir & Konfirmasi", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        
        scrollPaneTripSummaryFull.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Lengkap Perjalanan Anda",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya Total",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        
        panelTripSummaryFull.setOpaque(false);
        panelEstimatedCost.setOpaque(false);
        if(scrollPaneTripSummaryFull.getViewport() != null) scrollPaneTripSummaryFull.getViewport().setOpaque(false);


        lblFinalReviewMessage.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblFinalReviewMessage.setForeground(AppTheme.TEXT_DARK);

        txtAreaFinalNotes.setFont(AppTheme.FONT_TEXT_FIELD);
        txtAreaFinalNotes.setBackground(AppTheme.INPUT_BACKGROUND);
        txtAreaFinalNotes.setForeground(AppTheme.INPUT_TEXT);
        txtAreaFinalNotes.setBorder(AppTheme.createDefaultInputBorder());
        txtAreaFinalNotes.setMargin(new Insets(5,5,5,5));
        scrollPaneFinalNotes.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        
        // Styling untuk label-label di panelTripSummaryFull
        Font summaryHeaderFont = AppTheme.FONT_SUBTITLE; // Atau FONT_LABEL_FORM yang lebih tebal
        Font summaryDetailFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color summaryHeaderColor = AppTheme.PRIMARY_BLUE_DARK;
        Color summaryDetailColor = AppTheme.TEXT_DARK;

        lblDestinationsSummaryDisplay.setFont(summaryHeaderFont);
        lblDestinationsSummaryDisplay.setForeground(summaryHeaderColor);
        listDestinationsDisplay.setFont(summaryDetailFont);
        listDestinationsDisplay.setBackground(new Color(0,0,0,0)); // Transparan
        listDestinationsDisplay.setForeground(summaryDetailColor);
        scrollPaneDestinationsDisplay.setBorder(BorderFactory.createEmptyBorder()); // Hapus border jika list transparan
        scrollPaneDestinationsDisplay.getViewport().setOpaque(false);


        lblDatesSummaryDisplay.setFont(summaryHeaderFont); // Ini akan diisi dengan HTML, jadi fontnya mungkin tidak terlalu berpengaruh
        lblDatesSummaryDisplay.setForeground(summaryHeaderColor);
        
        lblTransportSummaryDisplay.setFont(summaryHeaderFont);
        lblTransportSummaryDisplay.setForeground(summaryHeaderColor);
        
        lblAccommodationSummaryDisplay.setFont(summaryHeaderFont);
        lblAccommodationSummaryDisplay.setForeground(summaryHeaderColor);

        lblActivitiesSummaryDisplay.setFont(summaryHeaderFont);
        lblActivitiesSummaryDisplay.setForeground(summaryHeaderColor);
        listActivitiesDisplay.setFont(summaryDetailFont);
        listActivitiesDisplay.setBackground(new Color(0,0,0,0)); // Transparan
        listActivitiesDisplay.setForeground(summaryDetailColor);
        scrollPaneActivitiesDisplay.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneActivitiesDisplay.getViewport().setOpaque(false);


        lblTripSummaryTitleInfo.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryTitleInfo.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_LARGE); // Lebih besar
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        styleSecondaryButton(btnPrevStep, "< Kembali");
        stylePrimaryButton(btnFinishTrip, "Selesaikan & Pesan Trip");
        
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
    
    private void setupLogicAndVisuals() {
        updateBuildStepLabels(6); // Langkah aktif adalah 6 (Finalisasi)
        
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
        
        lblDatesSummaryDisplay.setText(String.format("<html><b>Tanggal:</b> %s s/d %s</html>", 
            (currentStartDate != null ? currentStartDate : "-"), 
            (currentEndDate != null ? currentEndDate : "-")));

        String transportInfo = (currentTransportMode != null && !currentTransportMode.isEmpty() ? currentTransportMode : "-");
        if (currentTransportDetails != null && !currentTransportDetails.isEmpty()){
            transportInfo += " (<i>" + currentTransportDetails + "</i>)";
        }
        lblTransportSummaryDisplay.setText("<html><b>Transportasi:</b> " + transportInfo + "</html>");
        
        String accommodationInfo = (currentAccommodationName != null && !currentAccommodationName.isEmpty() ? currentAccommodationName : "-");
        if (currentRoomType != null && !currentRoomType.isEmpty()){
            accommodationInfo += " (<i>Kamar: " + currentRoomType + "</i>)";
        }
        if (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty()){
            accommodationInfo += " - <i>Catatan: " + currentAccommodationNotes + "</i>";
        }
        lblAccommodationSummaryDisplay.setText("<html><b>Akomodasi:</b> " + accommodationInfo + "</html>");

        if(listModelActivitiesDisplay.isEmpty()){
            listModelActivitiesDisplay.addElement("Tidak ada kegiatan spesifik yang direncanakan.");
        }
        lblActivitiesSummaryDisplay.setText("<html><b>Kegiatan:</b></html>");
        
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
        String finalNotes = txtAreaFinalNotes.getText().trim();
        String message = String.format(
            "Draf Trip Disimpan (Simulasi):\nDestinasi: %s\nTanggal: %s s/d %s\nTransportasi: %s (%s)\nAkomodasi: %s (%s) - Catatan: %s\nKegiatan: %s\nCatatan Akhir: %s\nEstimasi Biaya: %s",
            currentDestinations, currentStartDate, currentEndDate, 
            currentTransportMode, (currentTransportDetails != null && !currentTransportDetails.isEmpty() ? currentTransportDetails : "-"),
            currentAccommodationName == null || currentAccommodationName.isEmpty() ? "-" : currentAccommodationName,
            currentRoomType == null || currentRoomType.isEmpty() ? "" : ", Kamar: " + currentRoomType,
            currentAccommodationNotes == null || currentAccommodationNotes.isEmpty() ? "" : ", Catatan: " + currentAccommodationNotes,
            currentActivities.isEmpty() ? "Tidak ada" : currentActivities.toString(),
            finalNotes.isEmpty() ? "Tidak ada" : finalNotes,
            lblEstimasiHargaValue.getText()
        );
        JOptionPane.showMessageDialog(this, message, "Draf Trip Disimpan", JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnPrevStepActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Navigasi kembali ke PanelActivityStep
            mainAppFrame.showPanel(MainAppFrame.PANEL_ACTIVITY_STEP, 
                                   currentDestinations, currentStartDate, currentEndDate, 
                                   currentTransportMode, currentTransportDetails,
                                   currentAccommodationName, currentRoomType, currentAccommodationNotes); 
        } else {
            System.err.println("MainAppFrame reference is null in PanelFinalStep (Prev).");
        }
    }

    private void btnFinishTripActionPerformed(ActionEvent evt) {
        String finalNotes = txtAreaFinalNotes.getText().trim();
        // Di sini Anda akan melakukan proses finalisasi, misalnya menyimpan ke database,
        // mengirim notifikasi, atau mengarahkan ke halaman pembayaran/konfirmasi.

        String confirmationMessage = String.format(
            "Terima kasih telah membuat perjalanan Anda!\n\nRingkasan:\n" +
            "Destinasi: %s\nTanggal: %s - %s\nTransportasi: %s\n" +
            "Akomodasi: %s\nKegiatan: %s\nCatatan Akhir: %s\n\n" +
            "Perjalanan Anda akan diproses (simulasi).",
            currentDestinations.toString(),
            currentStartDate, currentEndDate,
            currentTransportMode + (currentTransportDetails != null && !currentTransportDetails.isEmpty() ? " (" + currentTransportDetails + ")" : ""),
            currentAccommodationName + (currentRoomType != null && !currentRoomType.isEmpty() ? " (Kamar: " + currentRoomType + ")" : "") + (currentAccommodationNotes != null && !currentAccommodationNotes.isEmpty() ? " - Catatan: " + currentAccommodationNotes : ""),
            currentActivities.isEmpty() ? "Tidak ada" : currentActivities.toString(),
            finalNotes.isEmpty() ? "Tidak ada" : finalNotes
        );

        JOptionPane.showMessageDialog(this, confirmationMessage, "Perjalanan Selesai & Dipesan", JOptionPane.INFORMATION_MESSAGE);
        
        if (mainAppFrame != null) {
            // Arahkan kembali ke halaman utama atau halaman riwayat pesanan setelah selesai
            mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); // Atau panel lain yang sesuai
        } else {
            System.err.println("MainAppFrame reference is null. Cannot navigate after finishing trip.");
            // Jika dijalankan standalone, mungkin tutup frame ini
            // Window window = SwingUtilities.getWindowAncestor(this);
            // if (window instanceof JFrame) {
            //     ((JFrame) window).dispose();
            // }
        }
    }
}
