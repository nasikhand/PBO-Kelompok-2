package managementtrevel.CustomTripBuilder;

import Asset.AppTheme; // Impor AppTheme Anda
import managementtrevel.MainAppFrame; // Impor MainAppFrame
import controller.DestinasiController; // Import DestinasiController
import model.DestinasiModel; // Import DestinasiModel

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;

public class PanelDestinationStep extends JPanel {

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

    private JPanel panelSelectDestination;
    private JTextField txtfield_destinasi;
    private JButton btnCariDestinasi;
    private JLabel lblHasilPencarianInfo;
    private JButton btnTambahDestinasi;

    private JPanel panelAvailableDestinations;
    private DefaultListModel<String> listModelAvailableDestinations;
    private JList<String> listAvailableDestinations;
    private JScrollPane scrollPaneAvailableDestinations;
    private DestinasiModel selectedAvailableDestination;

    private JPanel panelTripSummary;
    private JScrollPane jScrollPaneDestinasiSummary;
    private JList<String> listDestinasiSummary;
    private JButton btnHapusDestinasi;
    
    private JPanel panelEstimatedCost;
    private JLabel lblTripSummaryDestinationLabel;
    private JLabel lblEstimasiHargaValue;

    private JButton btnNextStep;

    private DefaultListModel<String> listModelDestinasi;

    private JPanel panelMainHeader;
    private JPanel panelMainFooter;
    private JPanel panelHapusBtnWrapper;

    private DestinasiController destinasiController;
    private List<DestinasiModel> allLoadedDestinations;

    private final String ACTIVE_STEP_ICON = "● "; 
    private final String INACTIVE_STEP_ICON = "○ ";
    private final String PLACEHOLDER_TEXT = "Cari nama kota atau destinasi...";

    public PanelDestinationStep(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        this.destinasiController = new DestinasiController();
        this.listModelDestinasi = new DefaultListModel<>();
        this.listModelAvailableDestinations = new DefaultListModel<>();

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

        panelMainHeader = new JPanel(new BorderLayout());
        lblCustomTripBuilderTitle = new JLabel("Custom Trip Builder");
        btnSaveTrip = new JButton("Simpan Draf Trip"); 
        panelMainHeader.add(lblCustomTripBuilderTitle, BorderLayout.WEST);
        panelMainHeader.add(btnSaveTrip, BorderLayout.EAST);
        panelCustomTripMain.add(panelMainHeader, BorderLayout.NORTH);

        panelLeftContent = new JPanel();
        panelLeftContent.setLayout(new BoxLayout(panelLeftContent, BoxLayout.Y_AXIS));
        panelLeftContent.setBorder(new EmptyBorder(0,0,0,5)); 

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
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 1; gbcSelDest.weightx = 1.0; gbcSelDest.gridwidth = 2;
        panelSelectDestination.add(lblHasilPencarianInfo, gbcSelDest);

        btnTambahDestinasi = new JButton("Tambah ke Trip (+)");
        btnTambahDestinasi.setToolTipText("Tambahkan destinasi yang ditemukan ke ringkasan trip");
        gbcSelDest.gridx = 0; gbcSelDest.gridy = 2; gbcSelDest.gridwidth = 2;
        gbcSelDest.fill = GridBagConstraints.NONE; gbcSelDest.anchor = GridBagConstraints.EAST;
        panelSelectDestination.add(btnTambahDestinasi, gbcSelDest);
        panelLeftContent.add(panelSelectDestination);
        panelLeftContent.add(Box.createRigidArea(new Dimension(0,10)));

        panelAvailableDestinations = new JPanel(new BorderLayout());
        listAvailableDestinations = new JList<>(listModelAvailableDestinations);
        listAvailableDestinations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listAvailableDestinations.setToolTipText("Pilih dari daftar destinasi yang tersedia");
        scrollPaneAvailableDestinations = new JScrollPane(listAvailableDestinations);
        scrollPaneAvailableDestinations.setPreferredSize(new Dimension(0, 200));
        panelAvailableDestinations.add(scrollPaneAvailableDestinations, BorderLayout.CENTER);
        panelLeftContent.add(panelAvailableDestinations);
        panelLeftContent.add(Box.createVerticalGlue());

        panelRightContent = new JPanel();
        panelRightContent.setLayout(new BoxLayout(panelRightContent, BoxLayout.Y_AXIS));
        panelRightContent.setBorder(new EmptyBorder(0,5,0,0)); 

        panelTripSummary = new JPanel(new BorderLayout(5,5));
        listModelDestinasi = new DefaultListModel<>();
        listDestinasiSummary = new JList<>(listModelDestinasi);
        listDestinasiSummary.setToolTipText("Daftar destinasi yang sudah ditambahkan ke trip Anda");
        jScrollPaneDestinasiSummary = new JScrollPane(listDestinasiSummary);
        panelTripSummary.add(jScrollPaneDestinasiSummary, BorderLayout.CENTER);

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

        panelMainFooter = new JPanel(new BorderLayout());
        btnNextStep = new JButton("Lanjut ke Tanggal >"); // Akan diubah ke "Lanjut ke Itinerary >"
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

        if (panelMainHeader != null) panelMainHeader.setOpaque(false);
        lblCustomTripBuilderTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblCustomTripBuilderTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnSaveTrip, "Simpan Draf Trip"); 

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
        
        panelAvailableDestinations.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Destinasi Tersedia",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));

        panelTripSummary.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Ringkasan Trip Anda",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        panelEstimatedCost.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), "Estimasi Biaya",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
            titledBorderFont, titledBorderColor));
        
        panelSelectDestination.setOpaque(false);
        panelAvailableDestinations.setOpaque(false);
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
        stylePrimaryButton(btnNextStep, "Lanjut ke Itinerary >"); // Update button text

        lblHasilPencarianInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblHasilPencarianInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        listAvailableDestinations.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listAvailableDestinations.setBackground(AppTheme.INPUT_BACKGROUND);
        listAvailableDestinations.setForeground(AppTheme.INPUT_TEXT);
        scrollPaneAvailableDestinations.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        scrollPaneAvailableDestinations.getViewport().setOpaque(false);


        listDestinasiSummary.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        listDestinasiSummary.setBackground(AppTheme.INPUT_BACKGROUND);
        listDestinasiSummary.setForeground(AppTheme.INPUT_TEXT);
        jScrollPaneDestinasiSummary.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        lblTripSummaryDestinationLabel.setFont(AppTheme.FONT_LABEL_FORM);
        lblTripSummaryDestinationLabel.setForeground(AppTheme.TEXT_DARK);
        lblEstimasiHargaValue.setFont(AppTheme.FONT_TITLE_MEDIUM); 
        lblEstimasiHargaValue.setForeground(AppTheme.ACCENT_ORANGE);

        if (panelMainHeader != null) panelMainHeader.setOpaque(false);
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
        updateBuildStepLabels(1); // Set active step
        
        allLoadedDestinations = destinasiController.tampilkanSemuaDestinasi();
        for (DestinasiModel dest : allLoadedDestinations) {
            listModelAvailableDestinations.addElement(dest.getNamaDestinasi());
        }

        listAvailableDestinations.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = listAvailableDestinations.getSelectedIndex();
                    if (selectedIndex != -1) {
                        selectedAvailableDestination = allLoadedDestinations.get(selectedIndex);
                        lblHasilPencarianInfo.setText("Destinasi dipilih: " + selectedAvailableDestination.getNamaDestinasi());
                        lblHasilPencarianInfo.setForeground(AppTheme.PRIMARY_BLUE_DARK);
                        btnTambahDestinasi.setEnabled(true);
                    } else {
                        selectedAvailableDestination = null;
                        lblHasilPencarianInfo.setText("Status: Pilih destinasi dari daftar.");
                        lblHasilPencarianInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
                        btnTambahDestinasi.setEnabled(false);
                    }
                }
            }
        });

        btnCariDestinasi.addActionListener(this::btnCariDestinasiActionPerformed);
        btnTambahDestinasi.addActionListener(this::btnTambahDestinasiActionPerformed);
        btnHapusDestinasi.addActionListener(this::btnHapusDestinasiActionPerformed);
        btnSaveTrip.addActionListener(this::btnSaveTripActionPerformed);
        btnNextStep.addActionListener(this::btnNextStepActionPerformed);
        
        txtfield_destinasi.addActionListener(this::txtfield_destinasiActionPerformed);

        lblHasilPencarianInfo.setText("Status: Pilih destinasi dari daftar atau cari."); 
        btnTambahDestinasi.setEnabled(false);
        btnHapusDestinasi.setEnabled(false);
        btnNextStep.setEnabled(false);

        listDestinasiSummary.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    btnHapusDestinasi.setEnabled(listDestinasiSummary.getSelectedIndex() != -1);
                }
            }
        });
        
        listModelDestinasi.addListDataListener(new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent e) { updateNextStepButtonState(); }
            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent e) { updateNextStepButtonState(); }
            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent e) { updateNextStepButtonState(); }
        });

        updateEstimatedCost();
    }
    
    private void updateNextStepButtonState() {
        btnNextStep.setEnabled(!listModelDestinasi.isEmpty());
    }

    private void updateEstimatedCost() {
        double currentCost = 0.0;
        for (int i = 0; i < listModelDestinasi.getSize(); i++) {
            String destName = listModelDestinasi.getElementAt(i);
            for (DestinasiModel dest : allLoadedDestinations) {
                if (dest.getNamaDestinasi().equals(destName)) {
                    currentCost += dest.getHarga();
                    break;
                }
            }
        }
        lblEstimasiHargaValue.setText(AppTheme.formatCurrency(currentCost));
    }


    private void btnSaveTripActionPerformed(ActionEvent evt) {
        if (listModelDestinasi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tambahkan minimal satu destinasi sebelum menyimpan draf.", "Trip Kosong", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<String> destinationsToSave = new ArrayList<>();
        for (int i = 0; i < listModelDestinasi.getSize(); i++) {
            destinationsToSave.add(listModelDestinasi.getElementAt(i));
        }

        String tripDetails = "Draf Trip Disimpan:\n";
        for(int i=0; i < destinationsToSave.size(); i++){
            tripDetails += "- " + destinationsToSave.get(i) + "\n";
        }
        tripDetails += "Estimasi Biaya: " + lblEstimasiHargaValue.getText();

        JOptionPane.showMessageDialog(this, tripDetails, "Simpan Draf Trip Berhasil (Simulasi)", JOptionPane.INFORMATION_MESSAGE);
    }

    private void txtfield_destinasiActionPerformed(ActionEvent evt) {
        btnCariDestinasi.doClick();
    }

    private void btnCariDestinasiActionPerformed(ActionEvent evt) {
        String searchText = txtfield_destinasi.getText().trim();
        if (searchText.isEmpty() || searchText.equals(PLACEHOLDER_TEXT)) {
            listModelAvailableDestinations.clear();
            for (DestinasiModel dest : allLoadedDestinations) {
                listModelAvailableDestinations.addElement(dest.getNamaDestinasi());
            }
            lblHasilPencarianInfo.setText("Status: Menampilkan semua destinasi.");
            lblHasilPencarianInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            btnTambahDestinasi.setEnabled(false);
            return;
        }

        listModelAvailableDestinations.clear();
        boolean foundExactMatch = false;
        for (DestinasiModel dest : allLoadedDestinations) {
            if (dest.getNamaDestinasi().toLowerCase().contains(searchText.toLowerCase())) {
                listModelAvailableDestinations.addElement(dest.getNamaDestinasi());
                if (dest.getNamaDestinasi().equalsIgnoreCase(searchText)) {
                    foundExactMatch = true;
                }
            }
        }

        if (listModelAvailableDestinations.isEmpty()) {
            lblHasilPencarianInfo.setText("Tidak ditemukan destinasi untuk '" + searchText + "'.");
            lblHasilPencarianInfo.setForeground(AppTheme.ACCENT_ORANGE.darker());
            btnTambahDestinasi.setEnabled(false);
        } else if (foundExactMatch && listModelAvailableDestinations.getSize() == 1) {
            listAvailableDestinations.setSelectedIndex(0);
            lblHasilPencarianInfo.setText("Ditemukan & dipilih: " + listModelAvailableDestinations.getElementAt(0));
            lblHasilPencarianInfo.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            btnTambahDestinasi.setEnabled(true);
        } else {
            lblHasilPencarianInfo.setText("Ditemukan " + listModelAvailableDestinations.getSize() + " hasil untuk '" + searchText + "'. Pilih dari daftar.");
            lblHasilPencarianInfo.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            btnTambahDestinasi.setEnabled(listAvailableDestinations.getSelectedIndex() != -1);
        }
    }

    private void btnTambahDestinasiActionPerformed(ActionEvent evt) {
        if (selectedAvailableDestination == null) {
            JOptionPane.showMessageDialog(this, "Pilih destinasi dari daftar 'Destinasi Tersedia' untuk ditambahkan.", "Tidak Ada Destinasi Terpilih", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String destinasiDitambahkan = selectedAvailableDestination.getNamaDestinasi();

        if (listModelDestinasi.contains(destinasiDitambahkan)) {
            JOptionPane.showMessageDialog(this, "'" + destinasiDitambahkan + "' sudah ada dalam ringkasan trip Anda.", "Destinasi Sudah Ditambahkan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        listModelDestinasi.addElement(destinasiDitambahkan);
        updateEstimatedCost();
        
        txtfield_destinasi.setText(PLACEHOLDER_TEXT);
        txtfield_destinasi.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        lblHasilPencarianInfo.setText("Status: Destinasi ditambahkan. Pilih destinasi lain atau cari.");
        lblHasilPencarianInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        btnTambahDestinasi.setEnabled(false);
        listAvailableDestinations.clearSelection();
        selectedAvailableDestination = null;
    }

    private void btnHapusDestinasiActionPerformed(ActionEvent evt) {
        int selectedIndex = listDestinasiSummary.getSelectedIndex();
        if (selectedIndex != -1) {
            listModelDestinasi.remove(selectedIndex);
            updateEstimatedCost();
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
            double currentEstimatedCost = 0.0;
            try {
                String formattedCost = lblEstimasiHargaValue.getText().replace(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).getCurrency().getSymbol(), "").replace(".", "").replace(",", ".");
                currentEstimatedCost = NumberFormat.getInstance(new Locale("id", "ID")).parse(formattedCost).doubleValue();
            } catch (ParseException e) {
                System.err.println("Error parsing estimated cost from label: " + e.getMessage());
            }

            // PENTING: Mengarahkan ke PANEL_ITINERARY_STEP yang baru
            mainAppFrame.showPanel(MainAppFrame.PANEL_ITINERARY_STEP, 
                                   destinationsForNextStep,
                                   currentEstimatedCost);
        } else {
            System.err.println("MainAppFrame reference is null in PanelDestinationStep.");
        }
    }

    // --- START: Metode updateBuildStepLabels ---
    private void updateBuildStepLabels(int activeStep) {
        JLabel[] stepLabels = {
            lblStep1Destinasi,      // Step 1
            lblStep2Tanggal,        // Ini akan jadi lblStep2Itinerary
            lblStep3Transport,      // Ini akan jadi lblStep3TransportCost
            lblStep4Akomodasi,      // Ini akan jadi lblStep4Participants
            lblStep5Kegiatan        // Ini akan jadi lblStep5Final (sebelumnya lblStep6Final)
            // lblStep6Final tidak ada lagi di alur 5 langkah
        };
        
        // Sesuaikan teks untuk 5 langkah baru
        String[] stepTexts = {
            "1. Destinasi",
            "2. Itinerary",      // Menggantikan "Tanggal"
            "3. Transportasi",   // Menggantikan "Transportasi" lama, atau lebih spesifik "Biaya Transport"
            "4. Peserta",        // Menggantikan "Akomodasi" lama
            "5. Finalisasi"      // Menggantikan "Kegiatan" lama, dan ini adalah final
        };

        // Jika Anda ingin mengubah nama label GUI Anda agar lebih sesuai dengan alur baru,
        // Anda harus mengubah deklarasi JLabel di bagian atas kelas dan di initializeUI().
        // Contoh:
        // private JLabel lblStep2Itinerary; // Menggantikan lblStep2Tanggal
        // private JLabel lblStep3TransportCost; // Menggantikan lblStep3Transport
        // private JLabel lblStep4Participants; // Menggantikan lblStep4Akomodasi
        // private JLabel lblStep5Final; // Menggantikan lblStep5Kegiatan, lblStep6Final menjadi tidak ada

        // Jika Anda belum mengganti nama JLabel di UI, mapping di atas akan mengacu ke yang lama.
        // Untuk menjaga kompatibilitas dengan nama JLabel yang ada (lblStep1Destinasi, lblStep2Tanggal, dst.)
        // kita bisa memetakan seperti ini:
        JLabel[] currentStepLabels = {
            lblStep1Destinasi,
            lblStep2Tanggal, // Akan digunakan untuk Itinerary
            lblStep3Transport, // Akan digunakan untuk Transport Cost
            lblStep4Akomodasi, // Akan digunakan untuk Participants
            lblStep5Kegiatan   // Akan digunakan untuk Finalisasi (karena sekarang ada 5 langkah)
        };

        // Logika update loop tetap sama, tetapi sekarang hanya untuk 5 langkah
        for (int i = 0; i < 5; i++) { // Iterasi hanya sampai 5 langkah
            if (currentStepLabels[i] != null) {
                boolean isActive = (i + 1 == activeStep);
                currentStepLabels[i].setText((isActive ? ACTIVE_STEP_ICON : INACTIVE_STEP_ICON) + stepTexts[i]);
                currentStepLabels[i].setFont(isActive ? AppTheme.FONT_STEP_LABEL_ACTIVE : AppTheme.FONT_STEP_LABEL);
                currentStepLabels[i].setForeground(isActive ? AppTheme.ACCENT_ORANGE : AppTheme.TEXT_SECONDARY_DARK);
            }
        }
    }
    // --- END: Metode updateBuildStepLabels ---
}
