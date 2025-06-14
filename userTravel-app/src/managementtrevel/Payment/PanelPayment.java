package managementtrevel.Payment;

import Asset.AppTheme;
import controller.ReservasiController;
import db.Koneksi;
import db.dao.CustomTripDAO;
import db.dao.KotaDAO;
import db.dao.PaketPerjalananDAO;
import db.dao.PembayaranDAO;
import db.dao.PenumpangDAO;
import db.dao.ReservasiDAO;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import managementtrevel.MainAppFrame;
import model.CustomTripModel;
import model.PaketPerjalananModel;
import model.PembayaranModel;
import model.PenumpangModel;
import model.ReservasiModel;
import model.Session;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanelPayment extends JPanel {

    private MainAppFrame mainAppFrame;
    private int reservasiId;
    private PaketPerjalananModel paketDipesan;
    private CustomTripModel customTripDipesan; // Uncomment and use if CustomTrip logic is implemented
    private ReservasiModel currentReservasi;

    private ReservasiDAO reservasiDAO;
    private PaketPerjalananDAO paketPerjalananDAO;
    // private CustomTripDAO customTripDAO; // Uncomment if CustomTripDAO is used
    private KotaDAO kotaDAO;

    private JLabel lblTitle;
    private JButton btnKembali;

    private JPanel panelKiri;
    private JPanel panelKanan;

    private JLabel lblTitleKontakPemesan;
    private JTextField txtNamaPenumpangDisplay;
    private JTextField txtNoTeleponDisplay;
    private JLabel lblTitleDetailPembayaran;
    private JTextField txtIdReservasiDisplay;
    private JTextField txtTanggalPembayaranDisplay;
    private JTextField txtStatusPembayaranDisplay;
    private JLabel lblTitleMetodePembayaran;
    private JComboBox<String> cmbMetodeBank;
    private JComboBox<String> cmbMetodeEwallet;
    private JComboBox<String> cmbMetodeKartuKredit;

    private JLabel lblTitleRingkasanPesanan;
    private JLabel lblRingkasanNamaTrip;
    private JLabel lblRingkasanKota;
    private JLabel lblRingkasanTanggalTrip;
    private JLabel lblRingkasanDurasi;
    private JLabel lblRincianHargaDasar;
    private JLabel lblRingkasanHargaDasar;
    private JLabel lblRincianBiayaLayanan;
    private JLabel lblBiayaAdminValue;
    private JLabel lblRincianBiayaAsuransi;
    private JLabel lblBiayaAsuransiValue;
    private JLabel lblRincianBiayaPajak;
    private JLabel lblBiayaPajakValue;
    private JLabel lblTitleTotalPembayaran;
    private JLabel lblTotalPembayaranValue;
    private JButton btnBayarSekarang;

    private String namaKontak;
    private String emailKontak;
    private String teleponKontak;
    private List<String> namaPenumpangList;
    // private int jumlahPenumpang; // This member is not used

    // Mapping for UI payment method names to DB ENUM values
    private final Map<String, String> paymentMethodMap;
    private CustomTripDAO customTripDAO;
    private PenumpangDAO penumpangDAO;
    private PembayaranDAO pembayaranDAO;
    private ReservasiController reservasiController;

    public PanelPayment(MainAppFrame mainAppFrame, int reservasiId, String namaKontak, String emailKontak, String teleponKontak, List<String> penumpangList) {
        this.mainAppFrame = mainAppFrame;
        this.reservasiId = reservasiId;
        this.namaKontak = namaKontak;
        this.emailKontak = emailKontak;
        this.teleponKontak = teleponKontak;
        this.namaPenumpangList = penumpangList;

        // Initialize paymentMethodMap with correct ENUM values from DB
        this.paymentMethodMap = new HashMap<>();
        paymentMethodMap.put("Bank BCA", "transfer");
        paymentMethodMap.put("Bank BNI", "transfer");
        paymentMethodMap.put("Bank BRI", "transfer");
        paymentMethodMap.put("Bank Mandiri", "transfer");

        paymentMethodMap.put("GoPay", "cash"); 
        paymentMethodMap.put("OVO", "cash");
        paymentMethodMap.put("Dana", "cash");
        paymentMethodMap.put("ShopeePay", "cash");

        paymentMethodMap.put("Visa", "kartu kredit");
        paymentMethodMap.put("Mastercard", "kartu kredit");
        paymentMethodMap.put("JCB", "kartu kredit");

        // Initialize DAOs with a single connection (best practice)
        Connection conn = Koneksi.getConnection(); 
        if (conn == null) {
            System.err.println("ERROR: Koneksi database tidak tersedia saat inisialisasi PanelPayment.");
            JOptionPane.showMessageDialog(mainAppFrame, "Gagal terhubung ke database. Harap coba lagi nanti.", "Koneksi Database Error", JOptionPane.ERROR_MESSAGE);
            // Consider redirecting to a previous safe panel or exiting
            // mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); 
            return; 
        }
        this.reservasiDAO = new ReservasiDAO(conn); 
        this.paketPerjalananDAO = new PaketPerjalananDAO(conn); 
        this.customTripDAO = new CustomTripDAO(conn); 
        this.penumpangDAO = new PenumpangDAO(conn); 
        this.pembayaranDAO = new PembayaranDAO(conn); 
        this.kotaDAO = new KotaDAO(conn); 
        this.reservasiController = new ReservasiController(); // Controller inits its own DAOs (so this is fine)

        // Load the current reservation model from DB
        // currentReservasi harus dimuat SEBELUM UI diinisialisasi agar datanya ada saat UI dibangun
        this.currentReservasi = reservasiDAO.getReservasiById(this.reservasiId);
        
        // Handle case where currentReservasi is still null after loading (e.g. invalid ID)
        if (currentReservasi == null) {
            System.err.println("ERROR: Reservasi dengan ID " + this.reservasiId + " tidak ditemukan.");
            JOptionPane.showMessageDialog(mainAppFrame, "Data reservasi tidak dapat dimuat. Harap kembali dan coba lagi.", "Data Reservasi Error", JOptionPane.ERROR_MESSAGE);
            // Redirect to a previous panel, as this panel cannot function without reservation data
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
        if (btnKembali != null && btnKembali.getPreferredSize() != null) {
            dummyEast.setPreferredSize(btnKembali.getPreferredSize());
        } else {
            dummyEast.setPreferredSize(new Dimension(80, 10));
        }
        headerPanel.add(dummyEast, BorderLayout.EAST);

        panelKiri = new JPanel();
        panelKiri.setLayout(new BoxLayout(panelKiri, BoxLayout.Y_AXIS));
        panelKiri.setOpaque(true);
        panelKiri.setBackground(Color.WHITE);
        panelKiri.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR),
                new EmptyBorder(15, 15, 15, 15)
        ));

        lblTitleKontakPemesan = createSectionTitleLabel("Informasi Kontak Pemesan");
        panelKiri.add(lblTitleKontakPemesan);
        txtNamaPenumpangDisplay = createDisplayTextField("Nama Pemesan: Memuat...");
        panelKiri.add(txtNamaPenumpangDisplay);
        txtNoTeleponDisplay = createDisplayTextField("No. Telepon: Memuat...");
        panelKiri.add(txtNoTeleponDisplay);
        panelKiri.add(Box.createRigidArea(new Dimension(0, 20)));

        lblTitleDetailPembayaran = createSectionTitleLabel("Detail Pembayaran");
        panelKiri.add(lblTitleDetailPembayaran);
        txtIdReservasiDisplay = createDisplayTextField("Kode Reservasi: -");
        panelKiri.add(txtIdReservasiDisplay);
        txtTanggalPembayaranDisplay = createDisplayTextField("Tanggal Pembayaran: -");
        panelKiri.add(txtTanggalPembayaranDisplay);
        txtStatusPembayaranDisplay = createDisplayTextField("Status Pembayaran: -");
        panelKiri.add(txtStatusPembayaranDisplay);
        panelKiri.add(Box.createRigidArea(new Dimension(0, 20)));

        lblTitleMetodePembayaran = createSectionTitleLabel("Pilih Metode Pembayaran");
        panelKiri.add(lblTitleMetodePembayaran);
        panelKiri.add(createMetodePembayaranPanel());
        panelKiri.add(Box.createVerticalGlue());

        panelKanan = new JPanel();
        panelKanan.setLayout(new BoxLayout(panelKanan, BoxLayout.Y_AXIS));
        panelKanan.setOpaque(true);
        panelKanan.setBackground(Color.WHITE);
        panelKanan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR),
                new EmptyBorder(15, 15, 15, 15)
        ));

        lblTitleRingkasanPesanan = createSectionTitleLabel("Ringkasan Pesanan");
        panelKanan.add(lblTitleRingkasanPesanan);
        lblRingkasanNamaTrip = createInfoValueLabel("Nama Trip: -");
        panelKanan.add(lblRingkasanNamaTrip);
        lblRingkasanKota = createInfoValueLabel("Kota Tujuan: -");
        panelKanan.add(lblRingkasanKota);
        lblRingkasanTanggalTrip = createInfoValueLabel("Tanggal: -");
        panelKanan.add(lblRingkasanTanggalTrip);
        lblRingkasanDurasi = createInfoValueLabel("Durasi: -");
        panelKanan.add(lblRingkasanDurasi);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 15)));

        JSeparator separatorRingkasan = new JSeparator(SwingConstants.HORIZONTAL);
        separatorRingkasan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        panelKanan.add(separatorRingkasan);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 15)));

        lblRincianHargaDasar = createRincianBiayaLabel("Harga Dasar Paket/Trip:");
        panelKanan.add(lblRincianHargaDasar);
        lblRingkasanHargaDasar = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblRingkasanHargaDasar);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 5)));

        lblRincianBiayaLayanan = createRincianBiayaLabel("Biaya Layanan (5%):");
        panelKanan.add(lblRincianBiayaLayanan);
        lblBiayaAdminValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaAdminValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 5)));

        lblRincianBiayaAsuransi = createRincianBiayaLabel("Biaya Asuransi (Opsional):");
        panelKanan.add(lblRincianBiayaAsuransi);
        lblBiayaAsuransiValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaAsuransiValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0, 5)));

        lblRincianBiayaPajak = createRincianBiayaLabel("Pajak & Biaya Lain (11%):");
        panelKanan.add(lblRincianBiayaPajak);
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
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        JScrollPane leftScrollPane = (JScrollPane) splitPane.getLeftComponent();
        if (leftScrollPane != null) {
            if (leftScrollPane.getViewport() != null) leftScrollPane.getViewport().setOpaque(false);
            leftScrollPane.setOpaque(false);
            leftScrollPane.setBorder(null);
        }
        JScrollPane rightScrollPane = (JScrollPane) splitPane.getRightComponent();
        if (rightScrollPane != null) {
            if (rightScrollPane.getViewport() != null) rightScrollPane.getViewport().setOpaque(false);
            rightScrollPane.setOpaque(false);
            rightScrollPane.setBorder(null);
        }

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private JLabel createSectionTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }

    private JTextField createDisplayTextField(String initialText) {
        JTextField textField = new JTextField(initialText);
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, textField.getPreferredSize().height + 5));
        textField.setEditable(false);
        textField.setOpaque(false);
        textField.setBorder(null);
        return textField;
    }

    private JLabel createInfoValueLabel(String initialText) {
        JLabel label = new JLabel(initialText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createRincianBiayaLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createRincianBiayaValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createMetodePembayaranPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBank = new JLabel("Transfer Bank / Virtual Account:");
        lblBank.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeBank = new JComboBox<>(new String[]{"-- Pilih Bank --", "Bank BCA", "Bank BNI", "Bank BRI", "Bank Mandiri"});
        cmbMetodeBank.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeBank.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMetodeBank.getPreferredSize().height + 10));

        JLabel lblEwallet = new JLabel("E-Wallet:");
        lblEwallet.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeEwallet = new JComboBox<>(new String[]{"-- Pilih E-Wallet --", "GoPay", "OVO", "Dana", "ShopeePay"});
        cmbMetodeEwallet.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeEwallet.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMetodeEwallet.getPreferredSize().height + 10));

        JLabel lblKartuKredit = new JLabel("Kartu Kredit/Debit:");
        lblKartuKredit.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeKartuKredit = new JComboBox<>(new String[]{"-- Pilih Jenis Kartu --", "Visa", "Mastercard", "JCB"});
        cmbMetodeKartuKredit.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeKartuKredit.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMetodeKartuKredit.getPreferredSize().height + 10));

        panel.add(lblBank);
        panel.add(cmbMetodeBank);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblEwallet);
        panel.add(cmbMetodeEwallet);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblKartuKredit);
        panel.add(cmbMetodeKartuKredit);

        setupMetodePembayaranListener();
        return panel;
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        if (lblTitle != null) {
            lblTitle.setFont(AppTheme.FONT_TITLE_LARGE);
            lblTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
        if (btnKembali != null) styleSecondaryButton(btnKembali, "< Kembali");

        styleSectionTitleLabel(lblTitleKontakPemesan);
        styleSectionTitleLabel(lblTitleDetailPembayaran);
        styleSectionTitleLabel(lblTitleMetodePembayaran);
        styleSectionTitleLabel(lblTitleRingkasanPesanan);

        styleDisplayTextField(txtNamaPenumpangDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtNoTeleponDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtIdReservasiDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtTanggalPembayaranDisplay, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_DARK);
        styleDisplayTextField(txtStatusPembayaranDisplay, AppTheme.FONT_PRIMARY_BOLD, AppTheme.PRIMARY_BLUE_DARK);

        Component compMetodePanel = panelKiri.getComponent(10);
        if (compMetodePanel instanceof JPanel) {
            JPanel metodePanel = (JPanel) compMetodePanel;
            if (metodePanel != null && metodePanel.getComponentCount() >= 8) {
                if (metodePanel.getComponent(0) instanceof JLabel) {
                    styleFormLabel((JLabel) metodePanel.getComponent(0), "Transfer Bank / Virtual Account:");
                }
                styleComboBox(cmbMetodeBank);

                if (metodePanel.getComponent(3) instanceof JLabel) {
                    styleFormLabel((JLabel) metodePanel.getComponent(3), "E-Wallet:");
                }
                styleComboBox(cmbMetodeEwallet);

                if (metodePanel.getComponent(6) instanceof JLabel) {
                    styleFormLabel((JLabel) metodePanel.getComponent(6), "Kartu Kredit/Debit:");
                }
                styleComboBox(cmbMetodeKartuKredit);
            }
        }

        Font ringkasanInfoFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color ringkasanInfoColor = AppTheme.TEXT_SECONDARY_DARK;
        if (lblRingkasanNamaTrip != null) {
            Font subtitleBold = AppTheme.FONT_SUBTITLE != null ? AppTheme.FONT_SUBTITLE.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 16);
            lblRingkasanNamaTrip.setFont(subtitleBold);
            lblRingkasanNamaTrip.setForeground(AppTheme.TEXT_DARK);
        }
        if (lblRingkasanKota != null) {
            lblRingkasanKota.setFont(ringkasanInfoFont);
            lblRingkasanKota.setForeground(ringkasanInfoColor);
        }
        if (lblRingkasanTanggalTrip != null) {
            lblRingkasanTanggalTrip.setFont(ringkasanInfoFont);
            lblRingkasanTanggalTrip.setForeground(ringkasanInfoColor);
        }
        if (lblRingkasanDurasi != null) {
            lblRingkasanDurasi.setFont(ringkasanInfoFont);
            lblRingkasanDurasi.setForeground(ringkasanInfoColor);
        }

        styleFormLabel(lblRincianHargaDasar, "Harga Dasar Paket/Trip:");
        if (lblRingkasanHargaDasar != null) styleRincianValueLabel(lblRingkasanHargaDasar);
        styleFormLabel(lblRincianBiayaLayanan, "Biaya Layanan (5%):");
        if (lblBiayaAdminValue != null) styleRincianValueLabel(lblBiayaAdminValue);
        styleFormLabel(lblRincianBiayaAsuransi, "Biaya Asuransi (Opsional):");
        if (lblBiayaAsuransiValue != null) styleRincianValueLabel(lblBiayaAsuransiValue);
        styleFormLabel(lblRincianBiayaPajak, "Pajak & Biaya Lain (11%):");
        if (lblBiayaPajakValue != null) styleRincianValueLabel(lblBiayaPajakValue);

        if (lblTitleTotalPembayaran != null) {
            Font subtitleBold = AppTheme.FONT_SUBTITLE != null ? AppTheme.FONT_SUBTITLE.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 16);
            lblTitleTotalPembayaran.setFont(subtitleBold);
            lblTitleTotalPembayaran.setForeground(AppTheme.TEXT_DARK);
        }
        if (lblTotalPembayaranValue != null) {
            Font totalFont = AppTheme.FONT_TITLE_MEDIUM;
            lblTotalPembayaranValue.setFont(totalFont != null ? totalFont.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 18));
            lblTotalPembayaranValue.setForeground(AppTheme.ACCENT_ORANGE);
        }

        if (btnBayarSekarang != null) stylePrimaryButton(btnBayarSekarang, "Bayar Sekarang");
    }

    private void styleSectionTitleLabel(JLabel titleLabel) {
        if (titleLabel != null) {
            Font subtitleBold = AppTheme.FONT_SUBTITLE != null ? AppTheme.FONT_SUBTITLE.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 16);
            titleLabel.setFont(subtitleBold);
            titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
    }

    private void styleRincianValueLabel(JLabel label) {
        if (label != null) {
            label.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            label.setForeground(AppTheme.TEXT_DARK);
        }
    }

     private void styleFormLabel(JLabel label, String defaultText) {
        if (label != null) {
            if (label.getText() != null && label.getText().matches("jLabel\\d+")) {
                label.setText(defaultText);
            } else if (label.getText() == null || label.getText().isEmpty()) {
                label.setText(defaultText);
            }
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        }
    }

    private void styleInputField(JTextField textField, String placeholder) {
        if (textField == null) return;
        textField.setFont(AppTheme.FONT_TEXT_FIELD);
        textField.setBackground(AppTheme.INPUT_BACKGROUND);

        String currentText = textField.getText();
        if (currentText != null && (currentText.startsWith("Nama Pemesan:") || currentText.startsWith("No. Telepon:") || currentText.isEmpty() || currentText.equals(placeholder))) {
            textField.setText(placeholder);
            textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        } else {
            textField.setForeground(AppTheme.INPUT_TEXT);
        }
        textField.setBorder(AppTheme.createDefaultInputBorder());
        textField.setMargin(new Insets(5, 8, 5, 8));

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

    private void styleComboBox(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setForeground(AppTheme.INPUT_TEXT);
        comboBox.setBorder(AppTheme.createDefaultInputBorder());
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 32));
    }

    private void styleDisplayTextField(JTextField textField, Font font, Color foregroundColor) {
        if (textField != null) {
            textField.setFont(font);
            textField.setForeground(foregroundColor);
            textField.setEditable(false);
            textField.setBorder(null);
            textField.setOpaque(false);
            textField.setBackground(new Color(0, 0, 0, 0));
        }
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

    private void setupActionListeners() {
        if (btnKembali != null) btnKembali.addActionListener(this::btn_backActionPerformed);
        if (btnBayarSekarang != null) btnBayarSekarang.addActionListener(this::btn_bayarActionPerformed);
    }

    private String generateKodeReservasi() {
        String prefix = "RSV";
        String tanggal = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int angkaRandom = (int) (Math.random() * 900) + 100; // hasil 3 digit: 100-999
        return prefix + tanggal + angkaRandom;
    }


    private void loadPaymentDetails() {
        // This method will now rely on 'this.currentReservasi' which is loaded in the constructor

        if (txtIdReservasiDisplay != null && currentReservasi != null) {
            txtIdReservasiDisplay.setText("Kode Reservasi: " + currentReservasi.getKodeReservasi());
        } else {
            txtIdReservasiDisplay.setText("Kode Reservasi: N/A"); // Fallback if reservasi is null
        }
        if (txtTanggalPembayaranDisplay != null) {
            txtTanggalPembayaranDisplay.setText("Tanggal Pembayaran: " + new SimpleDateFormat("dd MMMM yyyy").format(new Date()));
        }
        if (txtStatusPembayaranDisplay != null && currentReservasi != null) {
            txtStatusPembayaranDisplay.setText("Status Pembayaran: " + currentReservasi.getStatus());
        } else {
            txtStatusPembayaranDisplay.setText("Status Pembayaran: Memuat...");
        }

        if (txtNamaPenumpangDisplay != null) txtNamaPenumpangDisplay.setText("Nama Pemesan: " + namaKontak);
        if (txtNoTeleponDisplay != null) txtNoTeleponDisplay.setText("No. Telepon: " + teleponKontak);

        if (this.currentReservasi != null) {
            if ("paket_perjalanan".equals(currentReservasi.getTripType())) {
                this.paketDipesan = paketPerjalananDAO.getById(currentReservasi.getTripId());
                if (this.paketDipesan != null) {
                    if (lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText(paketDipesan.getNamaPaket());
                    if (lblRingkasanKota != null && kotaDAO != null)
                        lblRingkasanKota.setText("Kota Tujuan: " + kotaDAO.getNamaKotaById(paketDipesan.getKotaId()));
                    if (lblRingkasanTanggalTrip != null)
                        lblRingkasanTanggalTrip.setText("Tanggal: " + paketDipesan.getTanggalMulai() + " s/d " + paketDipesan.getTanggalAkhir());
                    if (lblRingkasanDurasi != null)
                        lblRingkasanDurasi.setText("Durasi: " + paketDipesan.getDurasi() + " Hari");
                    updateHargaDasarDanPajak(paketDipesan.getHarga());
                } else {
                    System.err.println("Paket Perjalanan dengan ID " + currentReservasi.getTripId() + " tidak ditemukan.");
                    if (lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText("Data Paket Tidak Ditemukan");
                    updateHargaDasarDanPajak(0);
                }
            } else if ("custom_trip".equals(currentReservasi.getTripType())) {
                this.customTripDipesan = customTripDAO.getById(currentReservasi.getTripId()); 
                if (this.customTripDipesan != null) {
                    lblRingkasanNamaTrip.setText(customTripDipesan.getNamaTrip());
                    lblRingkasanKota.setText("Kota Tujuan: " + customTripDipesan.getNamaKota());
                    // Format tanggal custom trip
                    lblRingkasanTanggalTrip.setText("Tanggal: " + customTripDipesan.getTanggalMulai() + " s/d " + customTripDipesan.getTanggalAkhir());
                    
                    lblRingkasanDurasi.setText("Durasi: " + customTripDipesan.getDurasi() + " Hari");
                    
                    updateHargaDasarDanPajak(customTripDipesan.getTotalHarga());
  
                } else {
                    System.err.println("Custom Trip dengan Id " + currentReservasi.getTripId() + " tidak ditemukan.");
                    if (lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText("Data Paket Tidak Ditemukan");
                    updateHargaDasarDanPajak(0);
                }
            } else {
                System.err.println("Jenis Trip Tidak Dikenal: " + currentReservasi.getTripType());
                if (lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText("Jenis Trip Tidak Dikenal");
                updateHargaDasarDanPajak(0);
            }
        } else {
            // This else block might be hit if currentReservasi was null from constructor
            if (lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText("Ringkasan Pesanan (Data Tidak Tersedia)");
            if (lblRingkasanKota != null) lblRingkasanKota.setText("Kota Tujuan: -");
            if (lblRingkasanTanggalTrip != null) lblRingkasanTanggalTrip.setText("Tanggal: -");
            if (lblRingkasanDurasi != null) lblRingkasanDurasi.setText("Durasi: -");
            if (lblRingkasanHargaDasar != null) lblRingkasanHargaDasar.setText("Rp 0");
            if (lblTotalPembayaranValue != null) lblTotalPembayaranValue.setText("Rp 0");
            updateHargaDasarDanPajak(0);
        }
    }

    private void updateHargaDasarDanPajak(double hargaDasar) {
        if (lblRingkasanHargaDasar != null) {
            lblRingkasanHargaDasar.setText("Rp " + String.format("%,.0f", hargaDasar));
        }

        double pajak = hargaDasar * 0.11;
        if (lblBiayaPajakValue != null) {
            lblBiayaPajakValue.setText("Rp " + String.format("%,.0f", pajak));
        }

        double biayaLayanan = hargaDasar * 0.05;
        if (lblBiayaAdminValue != null) {
            lblBiayaAdminValue.setText("Rp " + String.format("%,.0f", biayaLayanan));
        }

        double biayaAsuransi = 0;
        if (lblBiayaAsuransiValue != null) {
            lblBiayaAsuransiValue.setText("Rp " + String.format("%,.0f", biayaAsuransi));
        }

        double totalPembayaran = hargaDasar + pajak + biayaLayanan + biayaAsuransi;
        if (lblTotalPembayaranValue != null) {
            lblTotalPembayaranValue.setText("Rp " + String.format("%,.0f", totalPembayaran));
        }
    }

    // private void buatReservasiSementaraBaru() { /* Removed logic that created new temporary reservation */ }

    private void initComponents() {
        // KOSONGKAN ATAU HAPUS METODE INI
    }

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            PaketPerjalananModel paketUntukBooking = this.paketDipesan;
            if (this.customTripDipesan != null && this.paketDipesan == null) {
                System.out.println("Kembali dari pembayaran custom trip, logika belum diimplementasikan sepenuhnya untuk meneruskan data custom trip ke booking screen.");
            }
            mainAppFrame.showPanel(MainAppFrame.PANEL_BOOKING_SCREEN, paketUntukBooking);
        } else {
            System.err.println("MainAppFrame is null in PanelPayment (btn_back)");
        }
    }

    private void setupMetodePembayaranListener() {
        cmbMetodeBank.addActionListener(e -> {
            boolean isSelected = cmbMetodeBank.getSelectedIndex() > 0;
            cmbMetodeEwallet.setEnabled(!isSelected);
            cmbMetodeKartuKredit.setEnabled(!isSelected);
        });

        cmbMetodeEwallet.addActionListener(e -> {
            boolean isSelected = cmbMetodeEwallet.getSelectedIndex() > 0;
            cmbMetodeBank.setEnabled(!isSelected);
            cmbMetodeKartuKredit.setEnabled(!isSelected);
        });

        cmbMetodeKartuKredit.addActionListener(e -> {
            boolean isSelected = cmbMetodeKartuKredit.getSelectedIndex() > 0;
            cmbMetodeBank.setEnabled(!isSelected);
            cmbMetodeEwallet.setEnabled(!isSelected);
        });
    }

    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedMethodDisplayName = null; // Holds the exact string from the JComboBox

        int idxBank = cmbMetodeBank.getSelectedIndex();
        int idxEwallet = cmbMetodeEwallet.getSelectedIndex();
        int idxKartuKredit = cmbMetodeKartuKredit.getSelectedIndex();

        int selectedCount = 0;
        if (idxBank > 0) {
            selectedCount++;
            selectedMethodDisplayName = (String) cmbMetodeBank.getSelectedItem();
        }
        if (idxEwallet > 0) {
            selectedCount++;
            selectedMethodDisplayName = (String) cmbMetodeEwallet.getSelectedItem();
        }
        if (idxKartuKredit > 0) {
            selectedCount++;
            selectedMethodDisplayName = (String) cmbMetodeKartuKredit.getSelectedItem();
        }

        if (selectedCount != 1) {
            JOptionPane.showMessageDialog(this, "Pilih **hanya satu** metode pembayaran.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the DB-compatible ENUM value from the map
        String metodePembayaranForDB = null;
        if (selectedMethodDisplayName != null) {
            metodePembayaranForDB = paymentMethodMap.get(selectedMethodDisplayName);
            if (metodePembayaranForDB == null) {
                JOptionPane.showMessageDialog(this, "Metode pembayaran '" + selectedMethodDisplayName + "' tidak dapat diproses (pemetaan ke ENUM DB tidak ditemukan).", "Error Internal", JOptionPane.ERROR_MESSAGE);
                System.err.println("ERROR: No ENUM mapping found for display name: '" + selectedMethodDisplayName + "'");
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Metode pembayaran tidak terpilih.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ensure currentReservasi is properly loaded and user is logged in
        if (currentReservasi == null || Session.currentUser == null || !Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Data reservasi tidak tersedia atau user belum login.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Set user ID on the existing reservation object
        currentReservasi.setUserId(Session.currentUser.getId());
        // Set the status to the desired ENUM value for a completed payment
        final String RESERVASI_COMPLETED_STATUS = "dibayar"; // Use 'dibayar' from reservasi.status ENUM
        final String RESERVASI_PENDING_STATUS = "pending";   // Use 'pending' for rollback status

        currentReservasi.setStatus(RESERVASI_COMPLETED_STATUS); 

        // Call the update method in ReservasiDAO
        boolean reservasiStatusUpdated = reservasiDAO.updateStatusReservasi(currentReservasi.getId(), currentReservasi.getStatus());

        if (reservasiStatusUpdated) {
            Connection conn = Koneksi.getConnection(); 
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Koneksi database gagal.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                PenumpangDAO penumpangDAO = new PenumpangDAO(conn);
                boolean allPassengersSaved = true;

                // Save passengers for the updated reservation (if not already saved in booking screen)
                for (String namaPenumpang : namaPenumpangList) {
                    PenumpangModel penumpang = new PenumpangModel();
                    penumpang.setReservasiId(this.reservasiId); 
                    penumpang.setNamaPenumpang(namaPenumpang);
                    // Set other passenger data if available

                    boolean success = penumpangDAO.insertPenumpang(penumpang);
                    if (!success) {
                        System.err.println("❌ Gagal menyimpan data penumpang: " + namaPenumpang);
                        allPassengersSaved = false;
                    }
                }

                if (allPassengersSaved) {
                    // Calculate total payment
                    double hargaDasar = paketDipesan != null ? paketDipesan.getHarga() : 0;
                    double pajak = hargaDasar * 0.11;
                    double biayaLayanan = hargaDasar * 0.05;
                    double biayaAsuransi = 0; 
                    double totalPembayaran = hargaDasar + pajak + biayaLayanan + biayaAsuransi;

                    PembayaranModel pembayaran = new PembayaranModel();
                    pembayaran.setReservasiId(this.reservasiId); 
                    pembayaran.setMetodePembayaran(metodePembayaranForDB); // This is the ENUM value
                    pembayaran.setJumlahPembayaran(totalPembayaran);
                    pembayaran.setTanggalPembayaran(new Date());
                    pembayaran.setStatusPembayaran("lunas"); // This is allowed for 'pembayaran.status_pembayaran' ENUM

                    PembayaranDAO pembayaranDAO = new PembayaranDAO(conn);
                    boolean berhasilSimpanPembayaran = pembayaranDAO.insertPembayaran(pembayaran);

                    if (berhasilSimpanPembayaran) {
                        JOptionPane.showMessageDialog(this, "✅ Pembayaran berhasil dan reservasi dikonfirmasi!\nKode Reservasi Anda: " + currentReservasi.getKodeReservasi(), "Pembayaran Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); 
                    } else {
                        JOptionPane.showMessageDialog(this, "⚠️ Pembayaran berhasil, tetapi gagal menyimpan data pembayaran.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                        currentReservasi.setStatus(RESERVASI_PENDING_STATUS); 
                        reservasiDAO.updateStatusReservasi(currentReservasi.getId(), currentReservasi.getStatus());
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Beberapa data penumpang gagal disimpan. Pembayaran tidak akan diproses.", "Error", JOptionPane.ERROR_MESSAGE);
                    currentReservasi.setStatus(RESERVASI_PENDING_STATUS);
                    reservasiDAO.updateStatusReservasi(currentReservasi.getId(), currentReservasi.getStatus());
                }
            } finally {
                // Ensure connection is closed/managed if opened specifically here
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui status reservasi. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            // This else block is reached if reservasiStatusUpdated is false
            // You might also want to rollback passengers here if they were inserted before this update failed.
        }
    }
}