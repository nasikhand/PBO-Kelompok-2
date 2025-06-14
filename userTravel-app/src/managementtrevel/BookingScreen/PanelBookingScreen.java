package managementtrevel.BookingScreen;

import Asset.AppTheme;
import controller.ReservasiController;
import db.dao.KotaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import com.toedter.calendar.JDateChooser;

import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;
import model.PenumpangModel;
import model.ReservasiModel;
import model.Session;
import model.UserModel;

public class PanelBookingScreen extends JPanel {

    private final MainAppFrame mainAppFrame;
    private final PaketPerjalananModel currentPaket;
    private final ReservasiController reservasiController;

    //<editor-fold defaultstate="collapsed" desc="UI Components">
    private JLabel lblTitle;
    private JButton btnKembali;
    private JTextField txtNamaKontak;
    private JTextField txtEmailKontak;
    private JTextField txtTeleponKontak;
    private JTextField tf_jumlahpenumpang;
    private JLabel lblRingkasanNamaPaket;
    private JLabel lblRingkasanKota;
    private JLabel lblRingkasanTanggal;
    private JLabel lblRingkasanKuota;
    private JLabel lblRingkasanHargaPerOrang;
    private JLabel lblRingkasanTotalHarga;
    private JCheckBox cbox_syaratdanketentuan;
    private JButton btnSimpanDraf;
    private JButton btnLanjutPembayaran;
    
    // Form Labels
    private JLabel lblNamaKontakTitle, lblEmailKontakTitle, lblTeleponKontakTitle;
    private JLabel lblJumlahPenumpangTitle;
    
    // Dynamic Panel
    private JPanel dynamicPassengerPanel;
    private List<PassengerDetailRow> passengerDetailRows;
    //</editor-fold>

    public PanelBookingScreen(MainAppFrame mainAppFrame, PaketPerjalananModel paket) {
        this.mainAppFrame = mainAppFrame;
        this.currentPaket = paket;
        this.reservasiController = new ReservasiController();
        this.passengerDetailRows = new ArrayList<>();

        initializeUIProgrammatically();
        applyAppTheme();
        loadBookingData();
        setupActionListeners();
        setupInputListeners();
        validateForm();
    }

    private void initializeUIProgrammatically() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        //<editor-fold defaultstate="collapsed" desc="Header Panel">
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        btnKembali = new JButton("< Kembali");
        headerPanel.add(btnKembali, BorderLayout.WEST);
        lblTitle = new JLabel("Selesaikan Pesanan Anda");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        JLabel dummyEast = new JLabel();
        dummyEast.setPreferredSize(btnKembali.getPreferredSize());
        headerPanel.add(dummyEast, BorderLayout.EAST);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Left Input Panel">
        JPanel panelInputData = new JPanel();
        panelInputData.setLayout(new BoxLayout(panelInputData, BoxLayout.Y_AXIS));
        panelInputData.setOpaque(false);
        panelInputData.setBorder(new EmptyBorder(0, 0, 0, 10));

        JPanel panelKontak = createInputSectionPanel("Informasi Kontak Pemesan");
        GridBagConstraints gbcKontak = new GridBagConstraints();
        gbcKontak.fill = GridBagConstraints.HORIZONTAL;
        gbcKontak.anchor = GridBagConstraints.WEST;
        gbcKontak.insets = new Insets(5, 5, 5, 5);
        gbcKontak.weightx = 1.0;
        
        lblNamaKontakTitle = new JLabel("Nama Kontak:");
        gbcKontak.gridy = 0; panelKontak.add(lblNamaKontakTitle, gbcKontak);
        gbcKontak.gridy = 1; txtNamaKontak = new JTextField(20); panelKontak.add(txtNamaKontak, gbcKontak);
        
        lblEmailKontakTitle = new JLabel("Email Kontak:");
        gbcKontak.gridy = 2; panelKontak.add(lblEmailKontakTitle, gbcKontak);
        gbcKontak.gridy = 3; txtEmailKontak = new JTextField(20); panelKontak.add(txtEmailKontak, gbcKontak);

        lblTeleponKontakTitle = new JLabel("No. Telepon Kontak:");
        gbcKontak.gridy = 4; panelKontak.add(lblTeleponKontakTitle, gbcKontak);
        gbcKontak.gridy = 5; txtTeleponKontak = new JTextField(20); panelKontak.add(txtTeleponKontak, gbcKontak);
        
        panelInputData.add(panelKontak);
        panelInputData.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel panelPenumpang = createInputSectionPanel("Detail Penumpang");
        GridBagConstraints gbcPenumpang = new GridBagConstraints();
        gbcPenumpang.fill = GridBagConstraints.HORIZONTAL;
        gbcPenumpang.anchor = GridBagConstraints.WEST;
        gbcPenumpang.insets = new Insets(5, 5, 5, 5);
        gbcPenumpang.weightx = 1.0;

        lblJumlahPenumpangTitle = new JLabel("Jumlah Penumpang (angka):");
        gbcPenumpang.gridy = 0; panelPenumpang.add(lblJumlahPenumpangTitle, gbcPenumpang);
        gbcPenumpang.gridy = 1; tf_jumlahpenumpang = new JTextField(5); panelPenumpang.add(tf_jumlahpenumpang, gbcPenumpang);
        
        gbcPenumpang.gridy = 2;
        gbcPenumpang.gridwidth = 2;
        dynamicPassengerPanel = new JPanel();
        dynamicPassengerPanel.setLayout(new BoxLayout(dynamicPassengerPanel, BoxLayout.Y_AXIS));
        dynamicPassengerPanel.setOpaque(false);
        panelPenumpang.add(dynamicPassengerPanel, gbcPenumpang);
        
        panelInputData.add(panelPenumpang);
        panelInputData.add(Box.createVerticalGlue());
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Right Summary Panel">
        JPanel panelRingkasanDanAksi = new JPanel(new BorderLayout(0, 15));
        panelRingkasanDanAksi.setOpaque(false);
        panelRingkasanDanAksi.setBorder(new EmptyBorder(0, 10, 0, 0));
        JPanel panelRingkasan = createInputSectionPanel("Ringkasan Perjalanan");
        panelRingkasan.setLayout(new BoxLayout(panelRingkasan, BoxLayout.Y_AXIS));
        lblRingkasanNamaPaket = new JLabel("Nama Paket: -");
        lblRingkasanKota = new JLabel("Kota Tujuan: -");
        lblRingkasanTanggal = new JLabel("Tanggal: -");
        lblRingkasanKuota = new JLabel("Kapasitas: -");
        lblRingkasanHargaPerOrang = new JLabel("Harga per Orang: -");
        lblRingkasanTotalHarga = new JLabel("Total Harga: Rp 0");
        Component[] ringkasanLabels = {lblRingkasanNamaPaket, lblRingkasanKota, lblRingkasanTanggal, lblRingkasanKuota, lblRingkasanHargaPerOrang, lblRingkasanTotalHarga};
        for (Component comp : ringkasanLabels) {
            ((JLabel) comp).setAlignmentX(Component.LEFT_ALIGNMENT);
            panelRingkasan.add(comp);
            panelRingkasan.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        panelRingkasanDanAksi.add(panelRingkasan, BorderLayout.NORTH);
        JPanel panelAksiBawah = new JPanel();
        panelAksiBawah.setLayout(new BoxLayout(panelAksiBawah, BoxLayout.Y_AXIS));
        panelAksiBawah.setOpaque(false);
        cbox_syaratdanketentuan = new JCheckBox("Saya setuju dengan syarat dan ketentuan yang berlaku.");
        cbox_syaratdanketentuan.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelAksiBawah.add(cbox_syaratdanketentuan);
        panelAksiBawah.add(Box.createRigidArea(new Dimension(0, 15)));
        btnSimpanDraf = new JButton("Simpan Draf");
        btnSimpanDraf.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelAksiBawah.add(btnSimpanDraf);
        panelAksiBawah.add(Box.createRigidArea(new Dimension(0, 10)));
        btnLanjutPembayaran = new JButton("Lanjut ke Pembayaran >");
        btnLanjutPembayaran.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelAksiBawah.add(btnLanjutPembayaran);
        panelRingkasanDanAksi.add(panelAksiBawah, BorderLayout.SOUTH);
        //</editor-fold>

        JScrollPane scrollInput = new JScrollPane(panelInputData);
        scrollInput.setBorder(null);
        scrollInput.getViewport().setOpaque(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollInput, panelRingkasanDanAksi);
        splitPane.setDividerLocation(0.55);
        splitPane.setResizeWeight(0.55);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        lblTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnKembali, "< Kembali");

        // Apply theme to form labels
        styleFormLabel(lblNamaKontakTitle);
        styleFormLabel(lblEmailKontakTitle);
        styleFormLabel(lblTeleponKontakTitle);
        styleFormLabel(lblJumlahPenumpangTitle);

        styleInputField(txtNamaKontak, "Nama Lengkap (Pemesanan)");
        styleInputField(txtEmailKontak, "Email (Pemesanan)");
        styleInputField(txtTeleponKontak, "No. Telepon (Pemesanan)");
        styleInputField(tf_jumlahpenumpang, "Contoh: 2");

        // Apply theme to summary
        Font ringkasanLabelFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color ringkasanTextColor = AppTheme.TEXT_SECONDARY_DARK;
        lblRingkasanNamaPaket.setFont(AppTheme.FONT_SUBTITLE); 
        lblRingkasanNamaPaket.setForeground(AppTheme.TEXT_DARK);
        lblRingkasanKota.setFont(ringkasanLabelFont); 
        lblRingkasanKota.setForeground(ringkasanTextColor);
        lblRingkasanTanggal.setFont(ringkasanLabelFont); 
        lblRingkasanTanggal.setForeground(ringkasanTextColor);
        lblRingkasanKuota.setFont(ringkasanLabelFont); 
        lblRingkasanKuota.setForeground(ringkasanTextColor);
        lblRingkasanHargaPerOrang.setFont(ringkasanLabelFont); 
        lblRingkasanHargaPerOrang.setForeground(ringkasanTextColor);

        lblRingkasanTotalHarga.setFont(AppTheme.FONT_TITLE_MEDIUM.deriveFont(Font.BOLD));
        lblRingkasanTotalHarga.setForeground(AppTheme.ACCENT_ORANGE);
        lblRingkasanTotalHarga.setBorder(new EmptyBorder(10, 0, 0, 0));

        cbox_syaratdanketentuan.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        cbox_syaratdanketentuan.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        cbox_syaratdanketentuan.setOpaque(false);

        styleSecondaryButton(btnSimpanDraf, "Simpan Draf");
        stylePrimaryButton(btnLanjutPembayaran, "Lanjut ke Pembayaran >");
    }

    private void loadBookingData() {
        if (currentPaket != null) {
            lblRingkasanNamaPaket.setText(currentPaket.getNamaPaket());
            KotaDAO kotaDAO = new KotaDAO();
            lblRingkasanKota.setText("Kota Tujuan: " + kotaDAO.getNamaKotaById(currentPaket.getKotaId()));
            lblRingkasanTanggal.setText("Tanggal: " + currentPaket.getTanggalMulai() + " s/d " + currentPaket.getTanggalAkhir());
            lblRingkasanKuota.setText("Kapasitas: " + currentPaket.getKuota() + " Orang");
            lblRingkasanHargaPerOrang.setText("Harga per Orang: " + AppTheme.formatCurrency(currentPaket.getHarga()));
            updateTotalPrice();
        }
        if (Session.isLoggedIn() && Session.currentUser != null) {
            UserModel user = Session.currentUser;
            txtNamaKontak.setText(user.getNamaLengkap());
            txtEmailKontak.setText(user.getEmail());
            txtTeleponKontak.setText(user.getNomorTelepon());
        }
    }

    private void setupActionListeners() {
        btnKembali.addActionListener(this::btn_backActionPerformed);
        btnLanjutPembayaran.addActionListener(this::btn_lanjutActionPerformed);
        btnSimpanDraf.addActionListener(this::btnSimpanDrafActionPerformed);
    }
    
    private void setupInputListeners() {
        addValidationListener(txtNamaKontak);
        addValidationListener(txtEmailKontak);
        addValidationListener(txtTeleponKontak);
        addValidationListener(tf_jumlahpenumpang);
        cbox_syaratdanketentuan.addActionListener(e -> validateForm());

        tf_jumlahpenumpang.getDocument().addDocumentListener(new SimpleDocumentListener(this::handlePassengerCountChange));
    }

    private void handlePassengerCountChange() {
        int jumlah = 0;
        try {
            if (!tf_jumlahpenumpang.getText().trim().isEmpty()) {
                jumlah = Integer.parseInt(tf_jumlahpenumpang.getText().trim());
            }
            if (jumlah > currentPaket.getKuota()) {
                JOptionPane.showMessageDialog(PanelBookingScreen.this, "Jumlah penumpang melebihi kuota paket (" + currentPaket.getKuota() + ").", "Batas Penumpang", JOptionPane.WARNING_MESSAGE);
                jumlah = currentPaket.getKuota();
                tf_jumlahpenumpang.setText(String.valueOf(jumlah));
            } else if (jumlah < 0) {
                jumlah = 0;
                tf_jumlahpenumpang.setText("0");
            }
        } catch (NumberFormatException e) {
            jumlah = 0;
        }
        updatePassengerFields(jumlah);
        updateTotalPrice();
        validateForm();
    }

    private void updatePassengerFields(int count) {
        dynamicPassengerPanel.removeAll();
        passengerDetailRows.clear();

        for (int i = 0; i < count; i++) {
            PassengerDetailRow row = new PassengerDetailRow(i + 1);
            
            row.getNameField().getDocument().addDocumentListener(new SimpleDocumentListener(this::validateForm));
            row.getGenderComboBox().addActionListener(e -> validateForm());
            row.getDateChooser().addPropertyChangeListener("date", e -> validateForm());
            row.getPhoneField().getDocument().addDocumentListener(new SimpleDocumentListener(this::validateForm));
            row.getEmailField().getDocument().addDocumentListener(new SimpleDocumentListener(this::validateForm));
            
            passengerDetailRows.add(row);
            dynamicPassengerPanel.add(row);
        }
        dynamicPassengerPanel.revalidate();
        dynamicPassengerPanel.repaint();
    }
    
    private void updateTotalPrice() {
        int jumlah = 0;
        try {
            if (!tf_jumlahpenumpang.getText().trim().isEmpty()) {
                jumlah = Integer.parseInt(tf_jumlahpenumpang.getText().trim());
            }
        } catch (NumberFormatException e) {
            jumlah = 0;
        }
        double total = currentPaket.getHarga() * jumlah;
        lblRingkasanTotalHarga.setText("Total Harga: " + AppTheme.formatCurrency(total));
    }
    
    private List<PenumpangModel> getPassengerDetails() {
        List<PenumpangModel> passengers = new ArrayList<>();
        for (PassengerDetailRow row : passengerDetailRows) {
            PenumpangModel p = new PenumpangModel();
            p.setNamaPenumpang(row.getPassengerName());
            p.setJenisKelamin(row.getGender());
            p.setEmail(row.getEmail());
            p.setNomorTelepon(row.getPhoneNumber());
            
            java.util.Date dob = row.getDateOfBirth();
            if (dob != null) {
                p.setTanggalLahir(new java.sql.Date(dob.getTime()));
            }
            passengers.add(p);
        }
        return passengers;
    }

    private boolean validateForm() {
        boolean isContactValid = !txtNamaKontak.getText().trim().isEmpty() && !txtNamaKontak.getText().equals("Nama Lengkap (Pemesanan)")
                && !txtEmailKontak.getText().trim().isEmpty() && !txtEmailKontak.getText().equals("Email (Pemesanan)")
                && !txtTeleponKontak.getText().trim().isEmpty() && !txtTeleponKontak.getText().equals("No. Telepon (Pemesanan)");

        boolean isPassengerCountValid;
        boolean allPassengerRowsValid = true;
        int jumlah = 0;
        try {
            jumlah = Integer.parseInt(tf_jumlahpenumpang.getText().trim());
            isPassengerCountValid = jumlah > 0 && jumlah <= currentPaket.getKuota();
        } catch (NumberFormatException e) {
            isPassengerCountValid = false;
        }

        if (isPassengerCountValid) {
            if (passengerDetailRows.size() == jumlah) {
                for (PassengerDetailRow row : passengerDetailRows) {
                    if (!row.isDataValid()) {
                        allPassengerRowsValid = false;
                        break;
                    }
                }
            } else {
                allPassengerRowsValid = false;
            }
        } else {
             allPassengerRowsValid = false;
        }

        boolean isTermsChecked = cbox_syaratdanketentuan.isSelected();
        boolean isFormValid = isContactValid && isPassengerCountValid && allPassengerRowsValid && isTermsChecked;
        
        btnLanjutPembayaran.setEnabled(isFormValid);
        btnSimpanDraf.setEnabled(isContactValid && isPassengerCountValid);
        
        return isFormValid;
    }

    private void btn_backActionPerformed(ActionEvent evt) {
        int result = JOptionPane.showConfirmDialog(mainAppFrame, "Apakah Anda yakin ingin kembali? Data yang sudah diisi akan hilang.", "Konfirmasi", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRIP_DETAIL, currentPaket, null, null);
        }
    }

    private void btn_lanjutActionPerformed(ActionEvent evt) { handleBooking("pending"); }
    private void btnSimpanDrafActionPerformed(ActionEvent evt) { handleBooking("draft"); }
    
    private void handleBooking(String status) {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk melanjutkan.", "Login Diperlukan", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if ("pending".equals(status) && !validateForm()) {
             JOptionPane.showMessageDialog(this, "Harap lengkapi semua data sebelum melanjutkan.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String namaKontak = txtNamaKontak.getText().trim();
        String emailKontak = txtEmailKontak.getText().trim();
        String teleponKontak = txtTeleponKontak.getText().trim();
        List<PenumpangModel> penumpangList = getPassengerDetails();
        List<String> namaPenumpangSaja = new ArrayList<>();
        for (PenumpangModel p : penumpangList) {
            namaPenumpangSaja.add(p.getNamaPenumpang());
        }

        ReservasiModel reservasi = new ReservasiModel();
        reservasi.setUserId(Session.currentUser.getId());
        reservasi.setTripType("paket_perjalanan");
        reservasi.setTripId(currentPaket.getId());
        reservasi.setKodeReservasi("RSV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        reservasi.setTanggalReservasi(LocalDate.now());
        reservasi.setStatus(status);

        int newReservasiId = reservasiController.buatReservasi(reservasi);

        if (newReservasiId != -1) {
            boolean allPassengersSaved = true;
            for (PenumpangModel penumpang : penumpangList) {
                if (!reservasiController.tambahPenumpangLengkap(newReservasiId, penumpang)) { 
                    allPassengersSaved = false;
                }
            }
            
            if ("pending".equals(status)) {
                 JOptionPane.showMessageDialog(this, "Reservasi berhasil dibuat. Lanjutkan ke pembayaran.", "Reservasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                 mainAppFrame.showPaymentPanel(newReservasiId, namaKontak, emailKontak, teleponKontak, namaPenumpangSaja);
            } else {
                 JOptionPane.showMessageDialog(this, "Pesanan berhasil disimpan sebagai draf.", "Draf Disimpan", JOptionPane.INFORMATION_MESSAGE);
                 mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Gagal membuat reservasi. Silakan coba lagi.", "Error Booking", JOptionPane.ERROR_MESSAGE);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Helper and Styling Methods">
    private void addValidationListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new SimpleDocumentListener(this::validateForm));
    }
    private JPanel createInputSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR.darker()), title,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                AppTheme.FONT_SUBTITLE, AppTheme.PRIMARY_BLUE_DARK));
        return panel;
    }
    private void styleFormLabel(JLabel label) {
        if (label != null) {
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_DARK);
            label.setBorder(new EmptyBorder(0, 0, 2, 0)); 
        }
    }
    private void styleInputField(JTextField textField, String placeholder) {
        if (textField == null) return;
        textField.setFont(AppTheme.FONT_TEXT_FIELD);
        textField.setBackground(AppTheme.INPUT_BACKGROUND);
        textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        textField.setText(placeholder);
        textField.setBorder(AppTheme.createDefaultInputBorder());
        textField.setMargin(new Insets(5, 8, 5, 8));
        textField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(AppTheme.INPUT_TEXT);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                }
            }
        });
    }
    private void stylePrimaryButton(JButton button, String text) {
        if (button == null) return;
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }
    private void styleSecondaryButton(JButton button, String text) {
        if (button == null) return;
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
        if (button == null) return;
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(originalColor); }
        });
    }
    //</editor-fold>

    // =========================================================================
    // == INNER CLASS UNTUK BARIS DETAIL PENUMPANG (UPDATED)
    // =========================================================================
    private static class PassengerDetailRow extends JPanel {
        private final JTextField nameField;
        private final JComboBox<String> genderComboBox;
        private final JDateChooser dateChooser;
        private final JTextField phoneField;
        private final JTextField emailField;

        public PassengerDetailRow(int passengerNumber) {
            setLayout(new GridBagLayout());
            setOpaque(false);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER_COLOR));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 2, 5, 2);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            // --- Nama Penumpang ---
            JLabel nameLabel = new JLabel("Nama Penumpang " + passengerNumber + ":");
            styleFormLabel(nameLabel);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            add(nameLabel, gbc);

            nameField = new JTextField();
            styleInputField(nameField, "Nama sesuai KTP");
            gbc.gridy = 1; add(nameField, gbc);

            // --- Jenis Kelamin & Tanggal Lahir (satu baris) ---
            JLabel genderLabel = new JLabel("Jenis Kelamin:");
            styleFormLabel(genderLabel);
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.4;
            add(genderLabel, gbc);

            JLabel dobLabel = new JLabel("Tanggal Lahir:");
            styleFormLabel(dobLabel);
            gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.6;
            add(dobLabel, gbc);
            
            genderComboBox = new JComboBox<>(new String[]{"- Pilih -", "pria", "wanita"});
            styleComboBox(genderComboBox);
            gbc.gridx = 0; gbc.gridy = 3;
            add(genderComboBox, gbc);
            
            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("dd MMM yyyy");
            dateChooser.setFont(AppTheme.FONT_TEXT_FIELD);
            gbc.gridx = 1; gbc.gridy = 3;
            add(dateChooser, gbc);
            
            // --- No. Telepon & Email (satu baris) ---
            JLabel phoneLabel = new JLabel("No. Telepon:");
            styleFormLabel(phoneLabel);
            gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.4;
            add(phoneLabel, gbc);
            
            JLabel emailLabel = new JLabel("Email:");
            styleFormLabel(emailLabel);
            gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.6;
            add(emailLabel, gbc);

            phoneField = new JTextField();
            styleInputField(phoneField, "Contoh: 08123456789");
            gbc.gridx = 0; gbc.gridy = 5;
            add(phoneField, gbc);
            
            emailField = new JTextField();
            styleInputField(emailField, "Contoh: nama@email.com");
            gbc.gridx = 1; gbc.gridy = 5;
            add(emailField, gbc);
        }

        public JTextField getNameField() { return nameField; }
        public JComboBox<String> getGenderComboBox() { return genderComboBox; }
        public JDateChooser getDateChooser() { return dateChooser; }
        public JTextField getPhoneField() { return phoneField; }
        public JTextField getEmailField() { return emailField; }
        public String getPassengerName() { return nameField.getText().trim(); }
        public String getGender() { return (String) genderComboBox.getSelectedItem(); }
        public java.util.Date getDateOfBirth() { return dateChooser.getDate(); }
        public String getPhoneNumber() { return phoneField.getText().trim(); }
        public String getEmail() { return emailField.getText().trim(); }

        public boolean isDataValid() {
            boolean nameValid = !getPassengerName().isEmpty() && !getPassengerName().equals("Nama sesuai KTP");
            boolean genderValid = genderComboBox.getSelectedIndex() > 0;
            boolean dobValid = dateChooser.getDate() != null;
            boolean phoneValid = !getPhoneNumber().isEmpty() && !getPhoneNumber().equals("Contoh: 08123456789");
            boolean emailValid = !getEmail().isEmpty() && !getEmail().equals("Contoh: nama@email.com") && getEmail().contains("@");
            return nameValid && genderValid && dobValid && phoneValid && emailValid;
        }
        
        //<editor-fold defaultstate="collapsed" desc="Inner Class Styling Helpers">
        private void styleFormLabel(JLabel label) {
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_DARK);
        }
        private void styleInputField(JTextField textField, String placeholder) {
            if (textField == null) return;
            textField.setFont(AppTheme.FONT_TEXT_FIELD);
            textField.setBackground(AppTheme.INPUT_BACKGROUND);
            textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
            textField.setText(placeholder);
            textField.setBorder(AppTheme.createDefaultInputBorder());
            textField.setMargin(new Insets(5, 8, 5, 8));
            textField.addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) {
                    textField.setBorder(AppTheme.createFocusBorder());
                    if (textField.getText().equals(placeholder)) {
                        textField.setText("");
                        textField.setForeground(AppTheme.INPUT_TEXT);
                    }
                }
                @Override public void focusLost(FocusEvent e) {
                    textField.setBorder(AppTheme.createDefaultInputBorder());
                    if (textField.getText().isEmpty()) {
                        textField.setText(placeholder);
                        textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                    }
                }
            });
        }
        private void styleComboBox(JComboBox<String> comboBox) {
            if (comboBox == null) return;
            comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
            comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        }
        //</editor-fold>
    }
}

// Helper class for DocumentListener
class SimpleDocumentListener implements DocumentListener {
    private Runnable action;
    public SimpleDocumentListener(Runnable action) { this.action = action; }
    @Override public void insertUpdate(DocumentEvent e) { action.run(); }
    @Override public void removeUpdate(DocumentEvent e) { action.run(); }
    @Override public void changedUpdate(DocumentEvent e) { action.run(); }
}
