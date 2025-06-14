package managementtrevel.Payment;

import Asset.AppTheme;
import db.Koneksi;
import db.dao.CustomTripDAO;
import db.dao.KotaDAO;
import db.dao.PaketPerjalananDAO;
import db.dao.PembayaranDAO;
import db.dao.ReservasiDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import managementtrevel.MainAppFrame;
import model.CustomTripModel;
import model.PaketPerjalananModel;
import model.PembayaranModel;
import model.ReservasiModel;
import model.Session;
import java.util.List;

public class PanelPayment extends JPanel {

    private final MainAppFrame mainAppFrame;
    private final int reservasiId;
    private PaketPerjalananModel paketDipesan;
    private CustomTripModel customTripDipesan;
    
    // --- FIXED: Removed the 'final' keyword to solve the initialization error ---
    private ReservasiModel currentReservasi;

    // DAOs
    private final ReservasiDAO reservasiDAO;
    private final PaketPerjalananDAO paketPerjalananDAO;
    private final CustomTripDAO customTripDAO;
    private final KotaDAO kotaDAO;
    private final PembayaranDAO pembayaranDAO;

    // UI Components
    private JComboBox<String> cmbMetodePembayaran;
    private JLabel lblTotalPembayaranValue;
    private JButton btnBayarSekarang;
    private JButton btnKembali;
    private JLabel lblTitle;
    private JTextField txtNamaPenumpangDisplay;
    private JTextField txtNoTeleponDisplay;
    private JTextField txtIdReservasiDisplay;
    private JTextField txtTanggalPembayaranDisplay;
    private JTextField txtStatusPembayaranDisplay;
    private JLabel lblRingkasanNamaTrip;
    private JLabel lblRingkasanKota;
    private JLabel lblRingkasanTanggalTrip;
    private JLabel lblRingkasanDurasi;
    private JLabel lblRingkasanHargaDasar;
    private JLabel lblBiayaAdminValue;
    private JLabel lblBiayaAsuransiValue;
    private JLabel lblBiayaPajakValue;
    private JPanel panelKiri;
    private JPanel panelKanan;
    private JLabel lblTitleTotalPembayaran;

    // Data passed from previous panel
    private final String namaKontak;
    private final String teleponKontak;

    public PanelPayment(MainAppFrame mainAppFrame, int reservasiId, String namaKontak, String emailKontak, List<String> penumpangList) {
        this.mainAppFrame = mainAppFrame;
        this.reservasiId = reservasiId;
        this.namaKontak = namaKontak;
        this.teleponKontak = emailKontak; // Assuming emailKontak should be the phone number based on your previous code

        Connection conn = Koneksi.getConnection(); 
        if (conn == null) {
            JOptionPane.showMessageDialog(mainAppFrame, "Gagal terhubung ke database.", "Koneksi Database Error", JOptionPane.ERROR_MESSAGE);
            // We exit here, so currentReservasi is never initialized.
            // By removing 'final', we tell the compiler this is okay.
            this.reservasiDAO = null;
            this.paketPerjalananDAO = null;
            this.customTripDAO = null;
            this.kotaDAO = null;
            this.pembayaranDAO = null;
            return;
        }
        
        // Initialize DAOs
        this.reservasiDAO = new ReservasiDAO(conn); 
        this.paketPerjalananDAO = new PaketPerjalananDAO(conn); 
        this.customTripDAO = new CustomTripDAO(conn); 
        this.kotaDAO = new KotaDAO(conn);
        this.pembayaranDAO = new PembayaranDAO(conn);

        // Now we can safely initialize currentReservasi
        this.currentReservasi = reservasiDAO.getReservasiById(this.reservasiId);
        
        if (currentReservasi == null) {
            JOptionPane.showMessageDialog(mainAppFrame, "Data reservasi tidak dapat dimuat.", "Data Reservasi Error", JOptionPane.ERROR_MESSAGE);
            mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); 
            return;
        }

        initializeUIProgrammatically();
        applyAppTheme();
        loadPaymentDetails();
        setupActionListeners();
    }

    private void initializeUIProgrammatically() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        btnKembali = new JButton("< Kembali");
        headerPanel.add(btnKembali, BorderLayout.WEST);
        lblTitle = new JLabel("Pembayaran");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        JLabel dummyEast = new JLabel();
        dummyEast.setPreferredSize(btnKembali.getPreferredSize());
        headerPanel.add(dummyEast, BorderLayout.EAST);

        panelKiri = new JPanel();
        panelKiri.setLayout(new BoxLayout(panelKiri, BoxLayout.Y_AXIS));
        panelKiri.setOpaque(true);
        panelKiri.setBackground(Color.WHITE);
        panelKiri.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), new EmptyBorder(15, 15, 15, 15)));
        
        panelKiri.add(createSectionTitleLabel("Informasi Kontak Pemesan"));
        txtNamaPenumpangDisplay = createDisplayTextField("");
        panelKiri.add(txtNamaPenumpangDisplay);
        txtNoTeleponDisplay = createDisplayTextField("");
        panelKiri.add(txtNoTeleponDisplay);
        panelKiri.add(Box.createRigidArea(new Dimension(0, 20)));
        
        panelKiri.add(createSectionTitleLabel("Detail Pembayaran"));
        txtIdReservasiDisplay = createDisplayTextField("");
        panelKiri.add(txtIdReservasiDisplay);
        txtTanggalPembayaranDisplay = createDisplayTextField("");
        panelKiri.add(txtTanggalPembayaranDisplay);
        txtStatusPembayaranDisplay = createDisplayTextField("");
        panelKiri.add(txtStatusPembayaranDisplay);
        panelKiri.add(Box.createRigidArea(new Dimension(0, 20)));

        panelKiri.add(createSectionTitleLabel("Metode Pembayaran"));
        panelKiri.add(createMetodePembayaranPanel());
        panelKiri.add(Box.createVerticalGlue());

        panelKanan = new JPanel();
        panelKanan.setLayout(new BoxLayout(panelKanan, BoxLayout.Y_AXIS));
        panelKanan.setOpaque(true);
        panelKanan.setBackground(Color.WHITE);
        panelKanan.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR), new EmptyBorder(15, 15, 15, 15)));

        panelKanan.add(createSectionTitleLabel("Ringkasan Pesanan"));
        lblRingkasanNamaTrip = createInfoValueLabel("-");
        panelKanan.add(lblRingkasanNamaTrip);
        lblRingkasanKota = createInfoValueLabel("-");
        panelKanan.add(lblRingkasanKota);
        lblRingkasanTanggalTrip = createInfoValueLabel("-");
        panelKanan.add(lblRingkasanTanggalTrip);
        lblRingkasanDurasi = createInfoValueLabel("-");
        panelKanan.add(lblRingkasanDurasi);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator separatorRingkasan = new JSeparator(SwingConstants.HORIZONTAL);
        separatorRingkasan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        panelKanan.add(separatorRingkasan);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 15)));
        panelKanan.add(createRincianBiayaLabel("Harga Dasar Paket/Trip:"));
        lblRingkasanHargaDasar = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblRingkasanHargaDasar);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 5)));
        panelKanan.add(createRincianBiayaLabel("Biaya Layanan (5%):"));
        lblBiayaAdminValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaAdminValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 5)));
        panelKanan.add(createRincianBiayaLabel("Biaya Asuransi (Opsional):"));
        lblBiayaAsuransiValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaAsuransiValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 5)));
        panelKanan.add(createRincianBiayaLabel("Pajak & Biaya Lain (11%):"));
        lblBiayaPajakValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaPajakValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator separatorTotal = new JSeparator(SwingConstants.HORIZONTAL);
        separatorTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        panelKanan.add(separatorTotal);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 10)));
        lblTitleTotalPembayaran = new JLabel("Total Pembayaran");
        lblTitleTotalPembayaran.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelKanan.add(lblTitleTotalPembayaran);
        lblTotalPembayaranValue = new JLabel("Rp 0");
        lblTotalPembayaranValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelKanan.add(lblTotalPembayaranValue);
        panelKanan.add(Box.createVerticalGlue());
        btnBayarSekarang = new JButton("Bayar Sekarang");
        btnBayarSekarang.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelKanan.add(btnBayarSekarang);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(panelKiri), new JScrollPane(panelKanan));
        splitPane.setDividerLocation(0.55);
        splitPane.setResizeWeight(0.55);
        splitPane.setBorder(null);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        lblTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        styleSecondaryButton(btnKembali, "< Kembali");
        
        styleDisplayTextField(txtNamaPenumpangDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtNoTeleponDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtIdReservasiDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtTanggalPembayaranDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtStatusPembayaranDisplay, AppTheme.FONT_PRIMARY_BOLD, AppTheme.PRIMARY_BLUE_DARK);
        
        styleComboBox(cmbMetodePembayaran);
        
        lblRingkasanNamaTrip.setFont(AppTheme.FONT_SUBTITLE.deriveFont(Font.BOLD));
        lblRingkasanNamaTrip.setForeground(AppTheme.TEXT_DARK);

        Font ringkasanInfoFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color ringkasanInfoColor = AppTheme.TEXT_SECONDARY_DARK;
        lblRingkasanKota.setFont(ringkasanInfoFont);
        lblRingkasanKota.setForeground(ringkasanInfoColor);
        lblRingkasanTanggalTrip.setFont(ringkasanInfoFont);
        lblRingkasanTanggalTrip.setForeground(ringkasanInfoColor);
        lblRingkasanDurasi.setFont(ringkasanInfoFont);
        lblRingkasanDurasi.setForeground(ringkasanInfoColor);

        styleRincianValueLabel(lblRingkasanHargaDasar);
        styleRincianValueLabel(lblBiayaAdminValue);
        styleRincianValueLabel(lblBiayaAsuransiValue);
        styleRincianValueLabel(lblBiayaPajakValue);
        
        lblTitleTotalPembayaran.setFont(AppTheme.FONT_SUBTITLE.deriveFont(Font.BOLD));
        lblTitleTotalPembayaran.setForeground(AppTheme.TEXT_DARK);
        lblTotalPembayaranValue.setFont(AppTheme.FONT_TITLE_MEDIUM.deriveFont(Font.BOLD));
        lblTotalPembayaranValue.setForeground(AppTheme.ACCENT_ORANGE);

        stylePrimaryButton(btnBayarSekarang, "Bayar Sekarang");
    }
    
    private JPanel createMetodePembayaranPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));

        String[] paymentOptions = { "-- Pilih Metode --", "Transfer Bank", "Kartu Kredit/Debit", "Cash / E-Wallet" };
        cmbMetodePembayaran = new JComboBox<>(paymentOptions);
        
        panel.add(cmbMetodePembayaran, BorderLayout.CENTER);
        return panel;
    }

    private void loadPaymentDetails() {
        if (currentReservasi == null) return;
        
        txtIdReservasiDisplay.setText("Kode Reservasi: " + currentReservasi.getKodeReservasi());
        txtTanggalPembayaranDisplay.setText("Tanggal Pembayaran: " + new SimpleDateFormat("dd MMMM YYYY").format(new Date()));
        txtStatusPembayaranDisplay.setText("Status Pembayaran: " + currentReservasi.getStatus());
        txtNamaPenumpangDisplay.setText("Nama Pemesan: " + namaKontak);
        txtNoTeleponDisplay.setText("No. Telepon: " + teleponKontak);

        double hargaDasar = 0;
        if ("paket_perjalanan".equals(currentReservasi.getTripType())) {
            this.paketDipesan = paketPerjalananDAO.getById(currentReservasi.getTripId());
            if (this.paketDipesan != null) {
                hargaDasar = paketDipesan.getHarga();
                lblRingkasanNamaTrip.setText(paketDipesan.getNamaPaket());
                lblRingkasanKota.setText("Kota Tujuan: " + kotaDAO.getNamaKotaById(paketDipesan.getKotaId()));
                lblRingkasanTanggalTrip.setText("Tanggal: " + paketDipesan.getTanggalMulai() + " s/d " + paketDipesan.getTanggalAkhir());
                lblRingkasanDurasi.setText("Durasi: " + paketDipesan.getDurasi() + " Hari");
            }
        } else if ("custom_trip".equals(currentReservasi.getTripType())) {
            this.customTripDipesan = customTripDAO.getById(currentReservasi.getTripId());
            if (this.customTripDipesan != null) {
                hargaDasar = customTripDipesan.getTotalHarga();
                lblRingkasanNamaTrip.setText(customTripDipesan.getNamaTrip());
                lblRingkasanKota.setText("Kota Tujuan: " + customTripDipesan.getNamaKota());
                lblRingkasanTanggalTrip.setText("Tanggal: " + customTripDipesan.getTanggalMulai() + " s/d " + customTripDipesan.getTanggalAkhir());
                lblRingkasanDurasi.setText("Durasi: " + customTripDipesan.getDurasi() + " Hari");
            }
        }
        updateHargaDanTotal(hargaDasar);
    }
    
    private void updateHargaDanTotal(double hargaDasar) {
        lblRingkasanHargaDasar.setText(AppTheme.formatCurrency(hargaDasar));
        double biayaLayanan = hargaDasar * 0.05;
        lblBiayaAdminValue.setText(AppTheme.formatCurrency(biayaLayanan));
        double biayaAsuransi = 0;
        lblBiayaAsuransiValue.setText(AppTheme.formatCurrency(biayaAsuransi));
        double pajak = hargaDasar * 0.11;
        lblBiayaPajakValue.setText(AppTheme.formatCurrency(pajak));
        double totalPembayaran = hargaDasar + biayaLayanan + biayaAsuransi + pajak;
        lblTotalPembayaranValue.setText(AppTheme.formatCurrency(totalPembayaran));
    }
    
    private void setupActionListeners() {
        btnKembali.addActionListener(e -> {
            // Opsi untuk dialog konfirmasi
            String[] options = {"Ya, Batalkan", "Tidak"};
            int result = JOptionPane.showOptionDialog(
                mainAppFrame, // Parent component
                "Apakah Anda yakin ingin membatalkan pembayaran?\nAnda akan diarahkan kembali ke halaman Beranda\nPesanan anda akan tersimpan di draft", // Pesan
                "Konfirmasi Pembatalan", // Judul dialog
                JOptionPane.YES_NO_OPTION, // Tipe opsi
                JOptionPane.QUESTION_MESSAGE, // Tipe pesan
                null, // Tidak ada ikon kustom
                options, // Teks tombol
                options[1] // Tombol default
            );

            // Jika pengguna memilih "Ya, Batalkan" (indeks 0)
            if (result == JOptionPane.YES_OPTION) {
                mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA);
            }
        });
        btnBayarSekarang.addActionListener(this::btn_bayarActionPerformed);
    }
    
    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = cmbMetodePembayaran.getSelectedIndex();
        if (selectedIndex <= 0) {
            JOptionPane.showMessageDialog(this, "Silakan pilih metode pembayaran.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedMethodDisplay = (String) cmbMetodePembayaran.getSelectedItem();
        String metodePembayaranForDB;

        switch (selectedMethodDisplay) {
            case "Transfer Bank":
                metodePembayaranForDB = "transfer";
                break;
            case "Kartu Kredit/Debit":
                metodePembayaranForDB = "kartu kredit";
                break;
            case "Cash / E-Wallet":
                metodePembayaranForDB = "cash";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Metode pembayaran tidak valid.", "Error Internal", JOptionPane.ERROR_MESSAGE);
                return;
        }

        double hargaDasar = (paketDipesan != null) ? paketDipesan.getHarga() : (customTripDipesan != null ? customTripDipesan.getTotalHarga() : 0);
        if (hargaDasar == 0) {
            JOptionPane.showMessageDialog(this, "Tidak dapat menghitung total pembayaran.", "Error Kalkulasi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double totalPembayaran = hargaDasar + (hargaDasar * 0.05) + (hargaDasar * 0.11);

        PembayaranModel pembayaran = new PembayaranModel();
        pembayaran.setReservasiId(this.reservasiId);
        pembayaran.setMetodePembayaran(metodePembayaranForDB);
        pembayaran.setJumlahPembayaran(totalPembayaran);
        pembayaran.setTanggalPembayaran(new Date());
        pembayaran.setStatusPembayaran("lunas");

        if (pembayaranDAO.insertPembayaran(pembayaran)) {
            if (reservasiDAO.updateStatusReservasi(this.reservasiId, "dibayar")) {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil dan reservasi dikonfirmasi!", "Pembayaran Berhasil", JOptionPane.INFORMATION_MESSAGE);
                mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
            } else {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil, tetapi gagal mengupdate status reservasi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memproses pembayaran. Silakan coba lagi.", "Error Pembayaran", JOptionPane.ERROR_MESSAGE);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="UI Helper and Styling Methods">
    private void styleComboBox(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setForeground(AppTheme.INPUT_TEXT);
        comboBox.setBorder(AppTheme.createDefaultInputBorder());
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 32));
    }
    private JLabel createSectionTitleLabel(String text) { JLabel label = new JLabel(text); label.setFont(AppTheme.FONT_SUBTITLE); label.setBorder(new EmptyBorder(0,0,8,0)); return label; }
    private JTextField createDisplayTextField(String initialText) { JTextField textField = new JTextField(initialText); textField.setEditable(false); textField.setOpaque(false); textField.setBorder(null); return textField; }
    private void styleDisplayTextField(JTextField textField, Font font, Color foregroundColor) { if (textField != null) { textField.setFont(font); textField.setForeground(foregroundColor); textField.setEditable(false); textField.setBorder(null); textField.setOpaque(false); } }
    private JLabel createInfoValueLabel(String initialText) { return new JLabel(initialText); }
    private JLabel createRincianBiayaLabel(String text) { JLabel label = new JLabel(text); label.setFont(AppTheme.FONT_PRIMARY_DEFAULT); return label; }
    private JLabel createRincianBiayaValueLabel(String text) { JLabel label = new JLabel(text); label.setFont(AppTheme.FONT_PRIMARY_BOLD); return label; }
    private void styleRincianValueLabel(JLabel label) { if (label != null) { label.setFont(AppTheme.FONT_PRIMARY_BOLD); label.setForeground(AppTheme.TEXT_DARK); } }
    private void stylePrimaryButton(JButton button, String text) { if (button == null) return; button.setText(text); button.setFont(AppTheme.FONT_BUTTON); button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND); button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT); button.setOpaque(true); button.setBorderPainted(false); button.setFocusPainted(false); button.setCursor(new Cursor(Cursor.HAND_CURSOR)); button.setBorder(new EmptyBorder(10, 25, 10, 25)); addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND); }
    private void styleSecondaryButton(JButton button, String text) { if (button == null) return; button.setText(text); button.setFont(AppTheme.FONT_BUTTON); button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND); button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT); button.setOpaque(true); button.setBorderPainted(false); button.setFocusPainted(false); button.setCursor(new Cursor(Cursor.HAND_CURSOR)); button.setBorder(new EmptyBorder(8, 15, 8, 15)); addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND); }
    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) { if (button == null) return; button.addMouseListener(new MouseAdapter() { @Override public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); } @Override public void mouseExited(MouseEvent e) { button.setBackground(originalColor); } }); }
    //</editor-fold>
    
}
