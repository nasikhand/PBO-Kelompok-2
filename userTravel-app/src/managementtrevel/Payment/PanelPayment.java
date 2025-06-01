package managementtrevel.Payment;

import Asset.AppTheme;
import db.dao.KotaDAO;
import db.dao.PaketPerjalananDAO;
import db.dao.ReservasiDAO;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import managementtrevel.MainAppFrame;
import model.CustomTripModel;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;


public class PanelPayment extends JPanel {

    private MainAppFrame mainAppFrame;
    private int reservasiId;
    private PaketPerjalananModel paketDipesan;
    private CustomTripModel customTripDipesan;
    private ReservasiModel currentReservasi;

    private ReservasiDAO reservasiDAO;
    private PaketPerjalananDAO paketPerjalananDAO;
    // private CustomTripDAO customTripDAO;
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


    public PanelPayment(MainAppFrame mainAppFrame, int reservasiId) {
        this.mainAppFrame = mainAppFrame;
        this.reservasiId = reservasiId;
        this.reservasiDAO = new ReservasiDAO();
        this.paketPerjalananDAO = new PaketPerjalananDAO();
        // this.customTripDAO = new CustomTripDAO();
        this.kotaDAO = new KotaDAO();

        initializeUIProgrammatically();
        applyAppTheme();
        loadPaymentDetails();
        setupActionListeners();
    }

    private PanelPayment() {
        this(null, -1);
        System.err.println("PERINGATAN: Konstruktor default PanelPayment dipanggil. Data tidak akan dimuat dengan benar.");
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
             dummyEast.setPreferredSize(new Dimension(80,10));
        }
        headerPanel.add(dummyEast, BorderLayout.EAST);

        panelKiri = new JPanel();
        panelKiri.setLayout(new BoxLayout(panelKiri, BoxLayout.Y_AXIS));
        panelKiri.setOpaque(true);
        panelKiri.setBackground(Color.WHITE);
        panelKiri.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR),
            new EmptyBorder(15,15,15,15)
        ));

        lblTitleKontakPemesan = createSectionTitleLabel("Informasi Kontak Pemesan");
        panelKiri.add(lblTitleKontakPemesan);
        txtNamaPenumpangDisplay = createDisplayTextField("Nama Pemesan: Memuat...");
        panelKiri.add(txtNamaPenumpangDisplay);
        txtNoTeleponDisplay = createDisplayTextField("No. Telepon: Memuat...");
        panelKiri.add(txtNoTeleponDisplay);
        panelKiri.add(Box.createRigidArea(new Dimension(0,20))); // Index 3

        lblTitleDetailPembayaran = createSectionTitleLabel("Detail Pembayaran");
        panelKiri.add(lblTitleDetailPembayaran); // Index 4
        txtIdReservasiDisplay = createDisplayTextField("ID Reservasi: -");
        panelKiri.add(txtIdReservasiDisplay); // Index 5
        txtTanggalPembayaranDisplay = createDisplayTextField("Tanggal Pembayaran: -");
        panelKiri.add(txtTanggalPembayaranDisplay); // Index 6
        txtStatusPembayaranDisplay = createDisplayTextField("Status Pembayaran: -");
        panelKiri.add(txtStatusPembayaranDisplay); // Index 7
        panelKiri.add(Box.createRigidArea(new Dimension(0,20))); // Index 8 

        lblTitleMetodePembayaran = createSectionTitleLabel("Pilih Metode Pembayaran");
        panelKiri.add(lblTitleMetodePembayaran); // Index 9
        panelKiri.add(createMetodePembayaranPanel()); // Index 10 (Panel yang dicari)
        panelKiri.add(Box.createVerticalGlue()); // Index 11

        panelKanan = new JPanel();
        panelKanan.setLayout(new BoxLayout(panelKanan, BoxLayout.Y_AXIS));
        panelKanan.setOpaque(true);
        panelKanan.setBackground(Color.WHITE);
        panelKanan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR),
            new EmptyBorder(15,15,15,15)
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
        panelKanan.add(Box.createRigidArea(new Dimension(0,15)));

        JSeparator separatorRingkasan = new JSeparator(SwingConstants.HORIZONTAL);
        separatorRingkasan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        panelKanan.add(separatorRingkasan);
        panelKanan.add(Box.createRigidArea(new Dimension(0,15)));

        lblRincianHargaDasar = createRincianBiayaLabel("Harga Dasar Paket/Trip:");
        panelKanan.add(lblRincianHargaDasar);
        lblRingkasanHargaDasar = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblRingkasanHargaDasar);
        panelKanan.add(Box.createRigidArea(new Dimension(0,5)));

        lblRincianBiayaLayanan = createRincianBiayaLabel("Biaya Layanan:");
        panelKanan.add(lblRincianBiayaLayanan);
        lblBiayaAdminValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaAdminValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0,5)));

        lblRincianBiayaAsuransi = createRincianBiayaLabel("Biaya Asuransi (Opsional):");
        panelKanan.add(lblRincianBiayaAsuransi);
        lblBiayaAsuransiValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaAsuransiValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0,5)));

        lblRincianBiayaPajak = createRincianBiayaLabel("Pajak & Biaya Lain:");
        panelKanan.add(lblRincianBiayaPajak);
        lblBiayaPajakValue = createRincianBiayaValueLabel("Rp 0");
        panelKanan.add(lblBiayaPajakValue);
        panelKanan.add(Box.createRigidArea(new Dimension(0,15)));

        JSeparator separatorTotal = new JSeparator(SwingConstants.HORIZONTAL);
        separatorTotal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        panelKanan.add(separatorTotal);
        panelKanan.add(Box.createRigidArea(new Dimension(0,10)));

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

        JScrollPane leftScrollPane = (JScrollPane)splitPane.getLeftComponent();
        if (leftScrollPane != null) {
            if(leftScrollPane.getViewport() != null) leftScrollPane.getViewport().setOpaque(false);
            leftScrollPane.setOpaque(false);
            leftScrollPane.setBorder(null);
        }
        JScrollPane rightScrollPane = (JScrollPane)splitPane.getRightComponent();
         if (rightScrollPane != null) {
            if(rightScrollPane.getViewport() != null) rightScrollPane.getViewport().setOpaque(false);
            rightScrollPane.setOpaque(false);
            rightScrollPane.setBorder(null);
        }

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private JLabel createSectionTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0,0,8,0));
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

        JLabel lblBank = new JLabel("Transfer Bank / Virtual Account:"); // Index 0 in this panel
        lblBank.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeBank = new JComboBox<>(new String[]{"-- Pilih Bank --", "Bank BCA", "Bank BNI", "Bank BRI", "Bank Mandiri"});
        cmbMetodeBank.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeBank.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMetodeBank.getPreferredSize().height + 5));

        JLabel lblEwallet = new JLabel("E-Wallet:"); // Index 3 in this panel
        lblEwallet.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeEwallet = new JComboBox<>(new String[]{"-- Pilih E-Wallet --", "GoPay", "OVO", "Dana", "ShopeePay"});
        cmbMetodeEwallet.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeEwallet.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMetodeEwallet.getPreferredSize().height + 5));

        JLabel lblKartuKredit = new JLabel("Kartu Kredit/Debit:"); // Index 6 in this panel
        lblKartuKredit.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeKartuKredit = new JComboBox<>(new String[]{"-- Pilih Jenis Kartu --", "Visa", "Mastercard", "JCB"});
        cmbMetodeKartuKredit.setAlignmentX(Component.LEFT_ALIGNMENT);
        cmbMetodeKartuKredit.setMaximumSize(new Dimension(Integer.MAX_VALUE, cmbMetodeKartuKredit.getPreferredSize().height + 5));

        panel.add(lblBank);
        panel.add(cmbMetodeBank);
        panel.add(Box.createRigidArea(new Dimension(0,10))); // Index 2
        panel.add(lblEwallet);
        panel.add(cmbMetodeEwallet);
        panel.add(Box.createRigidArea(new Dimension(0,10))); // Index 5
        panel.add(lblKartuKredit);
        panel.add(cmbMetodeKartuKredit);
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
            // Pastikan jumlah komponen sesuai sebelum mengaksesnya
            // Panel metode pembayaran memiliki 8 komponen: JLabel, JComboBox, RigidArea, JLabel, JComboBox, RigidArea, JLabel, JComboBox
            if (metodePanel != null && metodePanel.getComponentCount() >= 8) {
                // Komponen pada indeks 0 adalah lblBank
                if (metodePanel.getComponent(0) instanceof JLabel) {
                    styleFormLabel((JLabel) metodePanel.getComponent(0), "Transfer Bank / Virtual Account:");
                }
                styleComboBox(cmbMetodeBank);

                // Komponen pada indeks 3 adalah lblEwallet
                if (metodePanel.getComponent(3) instanceof JLabel) { 
                    styleFormLabel((JLabel) metodePanel.getComponent(3), "E-Wallet:");
                }
                styleComboBox(cmbMetodeEwallet);

                // Komponen pada indeks 6 adalah lblKartuKredit
                if (metodePanel.getComponent(6) instanceof JLabel) { 
                    styleFormLabel((JLabel) metodePanel.getComponent(6), "Kartu Kredit/Debit:");
                }
                styleComboBox(cmbMetodeKartuKredit);
            }
        }

        Font ringkasanInfoFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color ringkasanInfoColor = AppTheme.TEXT_SECONDARY_DARK;
        if(lblRingkasanNamaTrip != null) {
            Font subtitleBold = AppTheme.FONT_SUBTITLE != null ? AppTheme.FONT_SUBTITLE.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 16);
            lblRingkasanNamaTrip.setFont(subtitleBold);
            lblRingkasanNamaTrip.setForeground(AppTheme.TEXT_DARK);
        }
        if(lblRingkasanKota != null) { lblRingkasanKota.setFont(ringkasanInfoFont); lblRingkasanKota.setForeground(ringkasanInfoColor); }
        if(lblRingkasanTanggalTrip != null) { lblRingkasanTanggalTrip.setFont(ringkasanInfoFont); lblRingkasanTanggalTrip.setForeground(ringkasanInfoColor); }
        if(lblRingkasanDurasi != null) { lblRingkasanDurasi.setFont(ringkasanInfoFont); lblRingkasanDurasi.setForeground(ringkasanInfoColor); }

        styleFormLabel(lblRincianHargaDasar, "Harga Dasar Paket/Trip:");
        if (lblRingkasanHargaDasar != null) styleRincianValueLabel(lblRingkasanHargaDasar);
        styleFormLabel(lblRincianBiayaLayanan, "Biaya Layanan:");
        if (lblBiayaAdminValue != null) styleRincianValueLabel(lblBiayaAdminValue);
        styleFormLabel(lblRincianBiayaAsuransi, "Biaya Asuransi (Opsional):");
        if (lblBiayaAsuransiValue != null) styleRincianValueLabel(lblBiayaAsuransiValue);
        styleFormLabel(lblRincianBiayaPajak, "Pajak & Biaya Lain:");
        if (lblBiayaPajakValue != null) styleRincianValueLabel(lblBiayaPajakValue);

        if(lblTitleTotalPembayaran != null) {
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

            if(label.getText() != null && label.getText().matches("jLabel\\d+")){
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
            textField.setBackground(new Color(0,0,0,0));
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

    private void loadPaymentDetails() {
        if (txtIdReservasiDisplay != null) {
            txtIdReservasiDisplay.setText("ID Reservasi: " + (this.reservasiId > 0 ? String.valueOf(this.reservasiId) : "N/A"));
        }
        if (txtTanggalPembayaranDisplay != null ) {
            txtTanggalPembayaranDisplay.setText("Tanggal Pembayaran: " + new SimpleDateFormat("dd MMMM yyyy").format(new Date()));
        }
        if (txtStatusPembayaranDisplay != null ) {
            txtStatusPembayaranDisplay.setText("Status Pembayaran: Pending");
        }

        if (Session.isLoggedIn() && Session.currentUser != null) {
            if (txtNamaPenumpangDisplay != null) txtNamaPenumpangDisplay.setText("Nama Pemesan: " + Session.currentUser.getNamaLengkap());
            if (txtNoTeleponDisplay != null) txtNoTeleponDisplay.setText("No. Telepon: " + Session.currentUser.getNomorTelepon());
        } else {
            if (txtNamaPenumpangDisplay != null) txtNamaPenumpangDisplay.setText("Nama Pemesan: -");
            if (txtNoTeleponDisplay != null) txtNoTeleponDisplay.setText("No. Telepon: -");
        }

        // TODO: Ambil detail reservasi dari DAO untuk mendapatkan tripId dan tripType
        ReservasiModel reservasi = reservasiDAO.getReservasiById(this.reservasiId);
        if (reservasi != null) {
            if ("paket_perjalanan".equals(reservasi.getTripType())) {
                this.paketDipesan = paketPerjalananDAO.getById(reservasi.getTripId());
            } else if ("custom_trip".equals(reservasi.getTripType())) {
                // this.customTripDipesan = customTripDAO.getById(reservasi.getTripId());
            }
        }

        if (this.paketDipesan == null && this.customTripDipesan == null) {
            if(lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText("Contoh: Paket Wisata Danau Toba");
            if(lblRingkasanKota != null) lblRingkasanKota.setText("Kota Tujuan: -");
            if(lblRingkasanTanggalTrip != null) lblRingkasanTanggalTrip.setText("Tanggal: yyyy-MM-dd s/d yyyy-MM-dd");
            if(lblRingkasanDurasi != null) lblRingkasanDurasi.setText("Durasi: X Hari");
            if(lblRingkasanHargaDasar != null) lblRingkasanHargaDasar.setText("Rp 0");
            if(lblTotalPembayaranValue != null) lblTotalPembayaranValue.setText("Rp 0");

        } else if (this.paketDipesan != null) {
            if(lblRingkasanNamaTrip != null) lblRingkasanNamaTrip.setText(paketDipesan.getNamaPaket());
            if(lblRingkasanKota != null && kotaDAO != null) lblRingkasanKota.setText("Kota Tujuan: " + kotaDAO.getNamaKotaById(paketDipesan.getKotaId()));
            if(lblRingkasanTanggalTrip != null) lblRingkasanTanggalTrip.setText("Tanggal: " + paketDipesan.getTanggalMulai() + " s/d " + paketDipesan.getTanggalAkhir());
            if(lblRingkasanDurasi != null) lblRingkasanDurasi.setText("Durasi: " + paketDipesan.getDurasi() + " Hari");
            if(lblRingkasanHargaDasar != null) lblRingkasanHargaDasar.setText("Rp " + String.format("%,.0f", paketDipesan.getHarga()));
            if(lblTotalPembayaranValue != null) lblTotalPembayaranValue.setText("Rp " + String.format("%,.0f", paketDipesan.getHarga())); // TODO: Hitung total sebenarnya
        } else if (this.customTripDipesan != null) {
            // TODO: Logika untuk mengisi ringkasan dari customTripDipesan
        }

        if(lblBiayaAdminValue != null) lblBiayaAdminValue.setText("Rp 0");
        if(lblBiayaAsuransiValue != null) lblBiayaAsuransiValue.setText("Rp 0");
        if(lblBiayaPajakValue != null) lblBiayaPajakValue.setText("Rp 0");
    }

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

    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {
        String metodeBank = cmbMetodeBank.getSelectedIndex() > 0 ? (String) cmbMetodeBank.getSelectedItem() : null;
        String metodeEwallet = cmbMetodeEwallet.getSelectedIndex() > 0 ? (String) cmbMetodeEwallet.getSelectedItem() : null;
        String metodeKartuKredit = cmbMetodeKartuKredit.getSelectedIndex() > 0 ? (String) cmbMetodeKartuKredit.getSelectedItem() : null;

        if (metodeBank == null && metodeEwallet == null && metodeKartuKredit == null) {
            JOptionPane.showMessageDialog(this, "Pilih salah satu metode pembayaran.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Tombol Bayar Sekarang diklik! Metode: " + (metodeBank != null ? metodeBank : metodeEwallet != null ? metodeEwallet : metodeKartuKredit) + " (Logika pembayaran belum diimplementasikan sepenuhnya)");
    }
}