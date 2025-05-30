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
public class PanelDestinationStep extends JPanel {

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
    private JLabel lblTripSummaryDestinationLabel; // Diubah namanya agar lebih jelas
    private JLabel lblEstimasiHargaValue;

    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasi;

    // Variabel instance yang sebelumnya mungkin lokal di initializeUI()
    private JPanel panelMainHeader;
    private JPanel panelMainFooter;
    private JPanel panelHapusBtnWrapper;


    private final String ACTIVE_STEP_ICON = "● "; 
    private final String INACTIVE_STEP_ICON = "○ ";
    private final String PLACEHOLDER_TEXT = "Masukkan nama destinasi...";

    // Konstruktor diubah untuk menerima MainAppFrame
    public PanelDestinationStep(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
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

        // Inisialisasi panelMainHeader sebagai variabel instance
        panelMainHeader = new JPanel(new BorderLayout()); 
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder");
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

        panelSelectDestination = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSelDest = new GridBagConstraints();
        gbcSelDest.insets = new Insets(5,5,5,5);
        gbcSelDest.fill = GridBagConstraints.HORIZONTAL;

        txtfield_destinasi = new JTextField(20);
        txtfield_destinasi.setToolTipText("Masukkan nama kota atau tempat tujuan Anda");
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 0; gbcSelDest.weightx = 1.0;
        panelSelectDestination.add(txtfield_destinasi, gbcSelDest);

        btnCariDestinasi = new JButton("Cari");
        btnCariDestinasi.setToolTipText("Cari destinasi yang dimasukkan");
        gbcSelDest.gridx = 1; gbcSelDest.gridy = 0; gbcSelDest.weightx = 0;
        panelSelectDestination.add(btnCariDestinasi, gbcSelDest);
        
        lblHasilPencarianInfo = new JLabel("Status: -");
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 1; gbcSelDest.weightx = 1.0; gbcSelDest.gridwidth = 2; // Span 2 kolom
        panelSelectDestination.add(lblHasilPencarianInfo, gbcSelDest);

        btnTambahDestinasi = new JButton("Tambah ke Trip (+)");
        btnTambahDestinasi.setToolTipText("Tambahkan destinasi yang ditemukan ke ringkasan trip");
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 2; gbcSelDest.gridwidth = 2; // Span 2 kolom
        gbcSelDest.fill = GridBagConstraints.NONE; gbcSelDest.anchor = GridBagConstraints.EAST; // Rata kanan
        panelSelectDestination.add(btnTambahDestinasi, gbcSelDest);
        panelLeftContent.add(panelSelectDestination);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));


        panelSuggestDestination = new JPanel(new BorderLayout());
        lblSuggestInfo = new JLabel("Saran destinasi akan muncul di sini...", JLabel.CENTER);
        panelSuggestDestination.add(lblSuggestInfo, BorderLayout.CENTER);
        panelSuggestDestination.setPreferredSize(new Dimension(0, 120)); 
        panelLeftContent.add(panelSuggestDestination);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelDestinationOption = new JPanel(new BorderLayout());
        lblOptionInfo = new JLabel("Opsi untuk destinasi yang dipilih...", JLabel.CENTER);
        panelDestinationOption.add(lblOptionInfo, BorderLayout.CENTER);
        panelDestinationOption.setPreferredSize(new Dimension(0, 120)); 
        panelLeftContent.add(panelDestinationOption);
        panelLeftContent.add(Box.createVerticalGlue());

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        listModelDestinasi = new DefaultListModel<>(); 
        listDestinasiSummary = new JList<>(listModelDestinasi);
        listDestinasiSummary.setToolTipText("Daftar destinasi yang sudah ditambahkan");
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);

        // Inisialisasi panelHapusBtnWrapper sebagai variabel instance
        panelHapusBtnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,0));
        btnHapusDestinasi = new JButton("Hapus Destinasi (-)");
        btnHapusDestinasi.setToolTipText("Hapus destinasi yang dipilih dari ringkasan");
        panelHapusBtnWrapper.add(btnHapusDestinasi);
        panelTripSummary.add(panelHapusBtnWrapper, BorderLayout.SOUTH);
        panelRightContent.add(panelTripSummary);
        panelRightContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelEstimatedCost = new JPanel(new BorderLayout(10,0));
        lblTripSummaryDestinationLabel = new JLabel("Total Estimasi Biaya:"); 
        lblEstimasiHargaValue = new JLabel("Rp 0"); 
        lblEstimasiHargaValue.setHorizontalAlignment(SwingConstants.RIGHT);
        panelEstimatedCost.add(lblTripSummaryDestinationLabel, BorderLayout.WEST);
        panelEstimatedCost.add(lblEstimasiHargaValue, BorderLayout.CENTER);
        panelEstimatedCost.setPreferredSize(new Dimension(0, 60)); 
        panelRightContent.add(panelEstimatedCost);
        panelRightContent.add(Box.createVerticalGlue());

        splitPaneContent = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeftContent, panelRightContent);
        splitPaneContent.setDividerLocation(420); 
        splitPaneContent.setResizeWeight(0.4); 
        splitPaneContent.setContinuousLayout(true);
        panelCustomTripMain.add(splitPaneContent, BorderLayout.CENTER);

        // Inisialisasi panelMainFooter sebagai variabel instance
        panelMainFooter = new JPanel(new BorderLayout());
        btnNextStep = new JButton("Lanjut ke Tanggal >"); 
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

        panelSelectDestination.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Pilih Destinasi", 
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelSuggestDestination.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Saran Destinasi",
             TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelDestinationOption.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Opsi Destinasi",
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
        
        panelSelectDestination.setOpaque(false);
        panelSuggestDestination.setOpaque(false);
        panelDestinationOption.setOpaque(false);
        panelTripSummary.setOpaque(false);
        panelEstimatedCost.setOpaque(false);


        txtfield_destinasi.setFont(AppTheme.FONT_TEXT_FIELD);
        txtfield_destinasi.setBorder(AppTheme.createDefaultInputBorder());
        txtfield_destinasi.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR); 
        txtfield_destinasi.setText(PLACEHOLDER_TEXT); 
        addFocusBorderEffect(txtfield_destinasi, PLACEHOLDER_TEXT);


        stylePrimaryButton(btnCariDestinasi, "Cari");
        stylePrimaryButton(btnTambahDestinasi, "Tambah (+)");
        styleSecondaryButton(btnHapusDestinasi, "Hapus (-)");
        stylePrimaryButton(btnNextStep, "Lanjut ke Tanggal >");

        lblHasilPencarianInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblHasilPencarianInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblSuggestInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblSuggestInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblOptionInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblOptionInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummary.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblTripSummaryDestinationLabel.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryDestinationLabel.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM); 
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        // Pastikan panelMainHeader, panelMainFooter, dan panelHapusBtnWrapper tidak null
        if (panelMainHeader != null) panelMainHeader.setOpaque(false); // Ini baris yang menyebabkan error sebelumnya
        if (panelMainFooter != null) panelMainFooter.setOpaque(false);
        if (panelHapusBtnWrapper != null) panelHapusBtnWrapper.setOpaque(false);
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
        // Menambahkan pengecekan null untuk textField
        if (textField == null) return; 
        
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
        // JLayeredPane dan SidebarPanel lokal dihapus
        updateBuildStepLabels(1); 
        
        btnCariDestinasi.addActionListener(this::btnCariDestinasiActionPerformed);
        btnTambahDestinasi.addActionListener(this::btnTambahDestinasiActionPerformed);
        btnHapusDestinasi.addActionListener(this::btnHapusDestinasiActionPerformed);
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);
        
        // Focus listener untuk txtfield_destinasi sudah ditambahkan melalui addFocusBorderEffect di applyAppTheme
        txtfield_destinasi.addActionListener(this::txtfield_destinasiActionPerformed);

        lblHasilPencarianInfo.setText("Status: Masukkan destinasi dan klik Cari."); 
        btnTambahDestinasi.setEnabled(false); 
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
        if (listModelDestinasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tambahkan minimal satu destinasi sebelum menyimpan.", "Trip Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Logika penyimpanan trip (simulasi)
        String tripDetails = "Trip Disimpan:\n";
        for(int i=0; i < listModelDestinasi.getSize(); i++){
            tripDetails += "- " + listModelDestinasi.getElementAt(i) + "\n";
        }
        tripDetails += "Estimasi Biaya: " + lblEstimasiHargaValue.getText();

        JOptionPane.showMessageDialog(this, tripDetails, "Simpan Trip Berhasil (Simulasi)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void txtfield_destinasiActionPerformed(ActionEvent evt) {
        btnCariDestinasi.doClick();
    }

    private void btnCariDestinasiActionPerformed(ActionEvent evt) {
        String destinasiDicari = txtfield_destinasi.getText().trim();
        if (destinasiDicari.isEmpty() || destinasiDicari.equals(PLACEHOLDER_TEXT)) {
            JOptionPane.showMessageDialog(this, "Masukkan nama destinasi untuk dicari.", "Input Kosong", JOptionPane.WARNING_MESSAGE);
            lblHasilPencarianInfo.setText("Status: Masukkan destinasi.");
            lblHasilPencarianInfo.setForeground(AppTheme.ACCENT_ORANGE); // Warna warning
            btnTambahDestinasi.setEnabled(false);
            return;
        }

        // Simulasi pencarian
        String hasilPencarianValid = null;
        if (destinasiDicari.equalsIgnoreCase("Bali")) hasilPencarianValid = "Bali, Indonesia (Populer)";
        else if (destinasiDicari.equalsIgnoreCase("Jakarta")) hasilPencarianValid = "Jakarta, DKI Jakarta (Ibukota)";
        else if (destinasiDicari.equalsIgnoreCase("Bandung")) hasilPencarianValid = "Bandung, Jawa Barat (Sejuk)";
        else if (!destinasiDicari.isEmpty()) hasilPencarianValid = destinasiDicari + " (Input Pengguna)";

        if (hasilPencarianValid != null) {
            lblHasilPencarianInfo.setText("Ditemukan: " + hasilPencarianValid);
            lblHasilPencarianInfo.setForeground(AppTheme.PRIMARY_BLUE_DARK); // Warna sukses/info
            btnTambahDestinasi.setEnabled(true);
        } else {
            lblHasilPencarianInfo.setText("Tidak ditemukan: " + destinasiDicari);
            lblHasilPencarianInfo.setForeground(AppTheme.ACCENT_ORANGE.darker()); // Warna error
            btnTambahDestinasi.setEnabled(false);
        }
    }

    private void btnTambahDestinasiActionPerformed(ActionEvent evt) {
        String destinasiInfo = lblHasilPencarianInfo.getText();
        if (!destinasiInfo.startsWith("Ditemukan: ")) {
            JOptionPane.showMessageDialog(this, "Cari dan temukan destinasi yang valid terlebih dahulu.", "Destinasi Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String destinasiDitambahkan = destinasiInfo.substring("Ditemukan: ".length());

        if (listModelDestinasi.contains(destinasiDitambahkan)) {
            JOptionPane.showMessageDialog(this, "'" + destinasiDitambahkan + "' sudah ada dalam ringkasan trip Anda.", "Destinasi Sudah Ditambahkan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        listModelDestinasi.addElement(destinasiDitambahkan);
        txtfield_destinasi.setText(PLACEHOLDER_TEXT);
        txtfield_destinasi.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        lblHasilPencarianInfo.setText("Status: Masukkan destinasi dan klik Cari.");
        lblHasilPencarianInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        btnTambahDestinasi.setEnabled(false);
    }

    private void btnHapusDestinasiActionPerformed(ActionEvent evt) {
        int selectedIndex = listDestinasiSummary.getSelectedIndex();
        if (selectedIndex != -1) {
            listModelDestinasi.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Pilih destinasi dari ringkasan untuk dihapus.", "Tidak Ada Destinasi Terpilih", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnNextStepActionPerformed(ActionEvent evt) {
        if (listModelDestinasi == null || listModelDestinasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                                          "Tambahkan minimal satu destinasi untuk melanjutkan.", 
                                          "Ringkasan Trip Kosong", 
                                          JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<String> destinationsForNextStep = new ArrayList<>();
        for (int i = 0; i < listModelDestinasi.getSize(); i++) {
            destinationsForNextStep.add(listModelDestinasi.getElementAt(i));
        }
        
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_DATE_STEP, destinationsForNextStep); 
        } else {
             System.err.println("MainAppFrame reference is null in PanelDestinationStep.");
        }
    }
}
