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

import managementtrevel.MainAppFrame;
import managementtrevel.Payment.PanelPayment;
import managementtrevel.SearchResultScreen.PanelSearchResult;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;
import model.UserModel;

public class PanelBookingScreen extends JPanel {

    private MainAppFrame mainAppFrame;
    private PaketPerjalananModel currentPaket;
    private ReservasiController reservasiController;

    private JLabel lblTitle;
    private JButton btnKembali;

    private JPanel panelInputData;
    private JTextField txtNamaKontak;
    private JTextField txtEmailKontak;
    private JTextField txtTeleponKontak;
    private JTextField tf_jumlahpenumpang;
    private JTextField tb_passangerdata;
    private JTextField tb_passangerdata1;
    private JTextField tb_passangerdata2;

    private JPanel panelRingkasanDanAksi;
    private JLabel lblRingkasanNamaPaket;
    private JLabel lblRingkasanKota;
    private JLabel lblRingkasanTanggal;
    private JLabel lblRingkasanKuota;
    private JLabel lblRingkasanHargaPerOrang;
    private JLabel lblRingkasanTotalHarga;
    private JCheckBox cbox_syaratdanketentuan;
    private JButton btnSimpanDraf;
    private JButton btnLanjutPembayaran;
    private int reservasiId = -1; // Initialize to -1 to indicate no reservation created yet

    public PanelBookingScreen(MainAppFrame mainAppFrame, PaketPerjalananModel paket) {
        this.mainAppFrame = mainAppFrame;
        this.currentPaket = paket;
        this.reservasiController = new ReservasiController();

        initializeUIProgrammatically();
        applyAppTheme();
        loadBookingData();
        setupActionListeners();
        setupInputListeners();
        validateFormSyarat();
    }

    private void initializeUIProgrammatically() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

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

        panelInputData = new JPanel();
        panelInputData.setLayout(new BoxLayout(panelInputData, BoxLayout.Y_AXIS));
        panelInputData.setOpaque(false);
        panelInputData.setBorder(new EmptyBorder(0, 0, 0, 10));

        JPanel panelKontak = createInputSectionPanel("Informasi Kontak Pemesan");
        GridBagConstraints gbcKontak = new GridBagConstraints();
        gbcKontak.fill = GridBagConstraints.HORIZONTAL;
        gbcKontak.anchor = GridBagConstraints.WEST;
        gbcKontak.insets = new Insets(5, 5, 5, 5);
        gbcKontak.weightx = 1.0;

        gbcKontak.gridy = 0; panelKontak.add(new JLabel("Nama Kontak:"), gbcKontak);
        gbcKontak.gridy = 1; txtNamaKontak = new JTextField(20); panelKontak.add(txtNamaKontak, gbcKontak);
        gbcKontak.gridy = 2; panelKontak.add(new JLabel("Email Kontak:"), gbcKontak);
        gbcKontak.gridy = 3; txtEmailKontak = new JTextField(20); panelKontak.add(txtEmailKontak, gbcKontak);
        gbcKontak.gridy = 4; panelKontak.add(new JLabel("No. Telepon Kontak:"), gbcKontak);
        gbcKontak.gridy = 5; txtTeleponKontak = new JTextField(20); panelKontak.add(txtTeleponKontak, gbcKontak);
        panelInputData.add(panelKontak);
        panelInputData.add(Box.createRigidArea(new Dimension(0, 15)));

        if (Session.isLoggedIn() && Session.currentUser != null) {
            UserModel user = Session.currentUser;
            txtNamaKontak.setText(user.getNamaLengkap());
            txtEmailKontak.setText(user.getEmail());
            txtTeleponKontak.setText(user.getNomorTelepon());
        }

        JPanel panelPenumpang = createInputSectionPanel("Detail Penumpang");
        GridBagConstraints gbcPenumpang = new GridBagConstraints();
        gbcPenumpang.fill = GridBagConstraints.HORIZONTAL;
        gbcPenumpang.anchor = GridBagConstraints.WEST;
        gbcPenumpang.insets = new Insets(5, 5, 5, 5);
        gbcPenumpang.weightx = 1.0;

        gbcPenumpang.gridy = 0; panelPenumpang.add(new JLabel("Jumlah Penumpang (angka - maksimal 3 orang):"), gbcPenumpang);
        gbcPenumpang.gridy = 1; tf_jumlahpenumpang = new JTextField(5); panelPenumpang.add(tf_jumlahpenumpang, gbcPenumpang);
        gbcPenumpang.gridy = 2; panelPenumpang.add(new JLabel("Nama Penumpang 1:"), gbcPenumpang);
        gbcPenumpang.gridy = 3; tb_passangerdata = new JTextField(20); panelPenumpang.add(tb_passangerdata, gbcPenumpang);
        gbcPenumpang.gridy = 4; panelPenumpang.add(new JLabel("Nama Penumpang 2:"), gbcPenumpang);
        gbcPenumpang.gridy = 5; tb_passangerdata1 = new JTextField(20); panelPenumpang.add(tb_passangerdata1, gbcPenumpang);
        gbcPenumpang.gridy = 6; panelPenumpang.add(new JLabel("Nama Penumpang 3:"), gbcPenumpang);
        gbcPenumpang.gridy = 7; tb_passangerdata2 = new JTextField(20); panelPenumpang.add(tb_passangerdata2, gbcPenumpang);
        panelInputData.add(panelPenumpang);
        panelInputData.add(Box.createVerticalGlue());

        tb_passangerdata.setEnabled(false);
        tb_passangerdata1.setEnabled(false);
        tb_passangerdata2.setEnabled(false);

        tf_jumlahpenumpang.getDocument().addDocumentListener(new DocumentListener() {
            private void updateFieldPenumpang() {
                try {
                    int jumlah = Integer.parseInt(tf_jumlahpenumpang.getText().trim());
                    
                    if (jumlah > 3) {
                        JOptionPane.showMessageDialog(PanelBookingScreen.this, "Jumlah penumpang maksimal 3 orang.", "Batas Penumpang", JOptionPane.WARNING_MESSAGE);
                        tf_jumlahpenumpang.setText("3");
                        jumlah = 3;
                    }

                    tb_passangerdata.setEnabled(jumlah >= 1);
                    tb_passangerdata1.setEnabled(jumlah >= 2);
                    tb_passangerdata2.setEnabled(jumlah >= 3);

                    if (jumlah < 1) tb_passangerdata.setText("");
                    if (jumlah < 2) tb_passangerdata1.setText("");
                    if (jumlah < 3) tb_passangerdata2.setText("");

                } catch (NumberFormatException e) {
                    tb_passangerdata.setEnabled(false);
                    tb_passangerdata1.setEnabled(false);
                    tb_passangerdata2.setEnabled(false);
                }
            }

            @Override public void insertUpdate(DocumentEvent e) { updateFieldPenumpang(); }
            @Override public void removeUpdate(DocumentEvent e) { updateFieldPenumpang(); }
            @Override public void changedUpdate(DocumentEvent e) { updateFieldPenumpang(); }
        });

        panelRingkasanDanAksi = new JPanel(new BorderLayout(0, 15));
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
            if (comp instanceof JLabel) {
                ((JLabel) comp).setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
                panelRingkasan.add(comp);
                panelRingkasan.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        panelRingkasanDanAksi.add(panelRingkasan, BorderLayout.NORTH);

        JPanel panelAksiBawah = new JPanel();
        panelAksiBawah.setLayout(new BoxLayout(panelAksiBawah, BoxLayout.Y_AXIS));
        panelAksiBawah.setOpaque(false);

        cbox_syaratdanketentuan = new JCheckBox("Saya setuju dengan syarat dan ketentuan yang berlaku.");
        cbox_syaratdanketentuan.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panelAksiBawah.add(cbox_syaratdanketentuan);
        panelAksiBawah.add(Box.createRigidArea(new Dimension(0, 15)));

        btnSimpanDraf = new JButton("Simpan Draf");
        btnSimpanDraf.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panelAksiBawah.add(btnSimpanDraf);
        panelAksiBawah.add(Box.createRigidArea(new Dimension(0, 10)));

        btnLanjutPembayaran = new JButton("Lanjut ke Pembayaran >");
        btnLanjutPembayaran.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        panelAksiBawah.add(btnLanjutPembayaran);

        panelRingkasanDanAksi.add(panelAksiBawah, BorderLayout.SOUTH);

        JScrollPane scrollInput = new JScrollPane(panelInputData);
        scrollInput.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollInput.setOpaque(false);
        scrollInput.getViewport().setOpaque(false);
        scrollInput.setBorder(null);

        JScrollPane scrollSummary = new JScrollPane(panelRingkasanDanAksi);
        scrollSummary.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollSummary.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSummary.setOpaque(false);
        scrollSummary.getViewport().setOpaque(false);
        scrollSummary.setBorder(null);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollInput, scrollSummary);
        splitPane.setDividerLocation(0.55);
        splitPane.setResizeWeight(0.55);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);
        
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createInputSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR.darker()),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                AppTheme.FONT_SUBTITLE,
                AppTheme.PRIMARY_BLUE_DARK
        ));
        return panel;
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        if (lblTitle != null) {
            lblTitle.setFont(AppTheme.FONT_TITLE_LARGE);
            lblTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
        if (btnKembali != null) styleSecondaryButton(btnKembali, "< Kembali");

        styleInputField(txtNamaKontak, "Nama Lengkap (Pemesanan)");
        styleInputField(txtEmailKontak, "Email (Pemesanan)");
        styleInputField(txtTeleponKontak, "No. Telepon (Pemesanan)");
        styleInputField(tf_jumlahpenumpang, "Jumlah Penumpang (Angka - maksimal 3 orang)");
        styleInputField(tb_passangerdata, "Nama Penumpang 1 (sesuai KTP)");
        styleInputField(tb_passangerdata1, "Nama Penumpang 2 (sesuai KTP)");
        styleInputField(tb_passangerdata2, "Nama Penumpang 3 (sesuai KTP)");

        Font ringkasanLabelFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color ringkasanTextColor = AppTheme.TEXT_SECONDARY_DARK;
        if (lblRingkasanNamaPaket != null) { lblRingkasanNamaPaket.setFont(AppTheme.FONT_SUBTITLE); lblRingkasanNamaPaket.setForeground(AppTheme.TEXT_DARK); }
        if (lblRingkasanKota != null) { lblRingkasanKota.setFont(ringkasanLabelFont); lblRingkasanKota.setForeground(ringkasanTextColor); }
        if (lblRingkasanTanggal != null) { lblRingkasanTanggal.setFont(ringkasanLabelFont); lblRingkasanTanggal.setForeground(ringkasanTextColor); }
        if (lblRingkasanKuota != null) { lblRingkasanKuota.setFont(ringkasanLabelFont); lblRingkasanKuota.setForeground(ringkasanTextColor); }
        if (lblRingkasanHargaPerOrang != null) { lblRingkasanHargaPerOrang.setFont(ringkasanLabelFont); lblRingkasanHargaPerOrang.setForeground(ringkasanTextColor); }

        if (lblRingkasanTotalHarga != null) {
            Font totalHargaFont = AppTheme.FONT_TITLE_MEDIUM != null ? AppTheme.FONT_TITLE_MEDIUM.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 18);
            lblRingkasanTotalHarga.setFont(totalHargaFont);
            lblRingkasanTotalHarga.setForeground(AppTheme.ACCENT_ORANGE);
            lblRingkasanTotalHarga.setBorder(new EmptyBorder(10, 0, 0, 0));
        }

        if (cbox_syaratdanketentuan != null) {
            cbox_syaratdanketentuan.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            cbox_syaratdanketentuan.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            cbox_syaratdanketentuan.setOpaque(false);
        }

        if (btnSimpanDraf != null) styleSecondaryButton(btnSimpanDraf, "Simpan Draf");
        if (btnLanjutPembayaran != null) stylePrimaryButton(btnLanjutPembayaran, "Lanjut ke Pembayaran >");
    }

    private void styleInputField(JTextField textField, String placeholder) {
        if (textField == null) return;
        textField.setFont(AppTheme.FONT_TEXT_FIELD);
        textField.setBackground(AppTheme.INPUT_BACKGROUND);

        String currentText = textField.getText();
        if (currentText != null && (currentText.startsWith("Informasi Penumpang") || currentText.equals("Jumlah Penumpang") || currentText.isEmpty() || currentText.equals(placeholder))) {
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

    private void setupInputListeners() {
        addDocumentListener(txtNamaKontak);
        addDocumentListener(txtEmailKontak);
        addDocumentListener(txtTeleponKontak);

        addDocumentListener(tf_jumlahpenumpang);
        addDocumentListener(tb_passangerdata);
        addDocumentListener(tb_passangerdata1);
        addDocumentListener(tb_passangerdata2);

        if (cbox_syaratdanketentuan != null) {
            cbox_syaratdanketentuan.addActionListener(e -> validateFormSyarat());
        }
    }

    private void addDocumentListener(JTextField textField) {
        if (textField != null) {
            textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    validateFormSyarat();
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    validateFormSyarat();
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    validateFormSyarat();
                }
            });
        }
    }

    private boolean validateFormSyarat() {
        boolean syaratCentang = cbox_syaratdanketentuan.isSelected();

        boolean namaKontakIsi = !txtNamaKontak.getText().trim().isEmpty() && !txtNamaKontak.getText().trim().equals("Nama Lengkap (Pemesanan)");
        boolean emailKontakIsi = !txtEmailKontak.getText().trim().isEmpty() && !txtEmailKontak.getText().trim().equals("Email (Pemesanan)");
        boolean teleponKontakIsi = !txtTeleponKontak.getText().trim().isEmpty() && !txtTeleponKontak.getText().trim().equals("No. Telepon (Pemesanan)");
        boolean semuaKontakIsi = namaKontakIsi && emailKontakIsi && teleponKontakIsi;

        boolean penumpangValid = false;
        try {
            int jumlah = Integer.parseInt(tf_jumlahpenumpang.getText().trim());

            if (jumlah > 3) jumlah = 3;

            // Pastikan field penumpang yang aktif tidak berisi placeholder
            if (jumlah >= 1 && (tb_passangerdata.getText().trim().isEmpty() || tb_passangerdata.getText().trim().equals("Nama Penumpang 1 (sesuai KTP)"))) {
                penumpangValid = false;
            } else if (jumlah >= 2 && (tb_passangerdata1.getText().trim().isEmpty() || tb_passangerdata1.getText().trim().equals("Nama Penumpang 2 (sesuai KTP)"))) {
                penumpangValid = false;
            } else if (jumlah >= 3 && (tb_passangerdata2.getText().trim().isEmpty() || tb_passangerdata2.getText().trim().equals("Nama Penumpang 3 (sesuai KTP)"))) {
                penumpangValid = false;
            } else {
                penumpangValid = (jumlah >= 1);
            }
        } catch (NumberFormatException e) {
            penumpangValid = false;
        }

        boolean isValid = syaratCentang && semuaKontakIsi && penumpangValid;

        btnLanjutPembayaran.setEnabled(isValid);
        return isValid;
    }

    private void setupActionListeners() {
        if (btnKembali != null) btnKembali.addActionListener(this::btn_backActionPerformed);
        if (btnLanjutPembayaran != null) btnLanjutPembayaran.addActionListener(this::btn_selanjutnyaActionPerformed);
        if (cbox_syaratdanketentuan != null) cbox_syaratdanketentuan.addActionListener(this::cbox_syaratdanketentuanActionPerformed);
        if (btnSimpanDraf != null) btnSimpanDraf.addActionListener(this::btnSimpanDrafActionPerformed);
    }

    private void loadBookingData() {
        if (currentPaket != null) {
            if (lblRingkasanNamaPaket != null) lblRingkasanNamaPaket.setText(currentPaket.getNamaPaket());
            if (lblRingkasanKota != null) {
                KotaDAO kotaDAO = new KotaDAO();
                lblRingkasanKota.setText("Kota Tujuan: " + kotaDAO.getNamaKotaById(currentPaket.getKotaId()));
            }
            if (lblRingkasanTanggal != null) lblRingkasanTanggal.setText("Tanggal: " + currentPaket.getTanggalMulai() + " s/d " + currentPaket.getTanggalAkhir());
            if (lblRingkasanKuota != null) lblRingkasanKuota.setText("Kapasitas: " + currentPaket.getKuota() + " Orang");
            if (lblRingkasanHargaPerOrang != null) lblRingkasanHargaPerOrang.setText("Harga per Orang: Rp " + String.format("%,.0f", currentPaket.getHarga()));

            // Asumsi total harga sama dengan harga paket jika jumlah penumpang belum diimplementasikan
            if (lblRingkasanTotalHarga != null) lblRingkasanTotalHarga.setText("Total Harga: Rp " + String.format("%,.0f", currentPaket.getHarga()));

            if (tf_jumlahpenumpang != null) styleInputField(tf_jumlahpenumpang, "Contoh: 1");
            if (tb_passangerdata != null) styleInputField(tb_passangerdata, "Nama Penumpang 1 (sesuai KTP)");
            if (tb_passangerdata1 != null) styleInputField(tb_passangerdata1, "Nama Penumpang 2 (sesuai KTP)");
            if (tb_passangerdata2 != null) styleInputField(tb_passangerdata2, "Nama Penumpang 3 (sesuai KTP)");

        } else {
            if (lblRingkasanNamaPaket != null) lblRingkasanNamaPaket.setText("Ringkasan Perjalanan (Data Paket Tidak Tersedia)");
        }
        if (btnLanjutPembayaran != null && cbox_syaratdanketentuan != null) {
            validateFormSyarat();
        }
    }

    private void initComponents() { }

    private void cbox_syaratdanketentuanActionPerformed(java.awt.event.ActionEvent evt) {
        validateFormSyarat();
    }

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
        // Hapus logika delete reservasi dari sini.
        // Cukup kembali ke panel sebelumnya.
        if (mainAppFrame != null && currentPaket != null) {
            String originalSearchKota = null;
            String originalSearchTgl = null;
            Component prevPanel = mainAppFrame.getPanelByName(MainAppFrame.PANEL_SEARCH_RESULT);
            if (prevPanel instanceof PanelSearchResult) {
                originalSearchKota = ((PanelSearchResult) prevPanel).getNamaKotaAtauDestinasi();
                originalSearchTgl = ((PanelSearchResult) prevPanel).getTanggalKeberangkatan();
            }
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRIP_DETAIL, currentPaket, originalSearchKota, originalSearchTgl);
        } else if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA);
        } else {
            System.err.println("MainAppFrame is null in PanelBookingScreen (btn_back - no reservasiId)");
        }
    }

    private void btn_selanjutnyaActionPerformed(java.awt.event.ActionEvent evt) {
        if (!validateFormSyarat()) {
             JOptionPane.showMessageDialog(this, "Harap lengkapi semua field dan setujui syarat & ketentuan.", "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
             return;
        }

        int jumlahPenumpang = 0;
        try {
            jumlahPenumpang = Integer.parseInt(tf_jumlahpenumpang.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah penumpang harus berupa angka valid.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> penumpangList = new ArrayList<>();
        if (jumlahPenumpang >= 1 && !tb_passangerdata.getText().trim().equals("Nama Penumpang 1 (sesuai KTP)")) penumpangList.add(tb_passangerdata.getText().trim());
        if (jumlahPenumpang >= 2 && !tb_passangerdata1.getText().trim().equals("Nama Penumpang 2 (sesuai KTP)")) penumpangList.add(tb_passangerdata1.getText().trim());
        if (jumlahPenumpang >= 3 && !tb_passangerdata2.getText().trim().equals("Nama Penumpang 3 (sesuai KTP)")) penumpangList.add(tb_passangerdata2.getText().trim());

        System.out.println("DEBUG PanelBookingScreen - Penumpang List (btn_selanjutnya): " + penumpangList);
        System.out.println("DEBUG PanelBookingScreen - Jumlah Penumpang dari field: " + jumlahPenumpang);


        if (mainAppFrame != null && currentPaket != null && Session.isLoggedIn() && Session.currentUser != null) {
            String namaKontak = txtNamaKontak.getText().trim();
            String emailKontak = txtEmailKontak.getText().trim();
            String teleponKontak = txtTeleponKontak.getText().trim();

            ReservasiModel reservasi = new ReservasiModel();
            reservasi.setUserId(Session.currentUser.getId());
            reservasi.setTripType("paket_perjalanan");
            reservasi.setTripId(currentPaket.getId());
            reservasi.setKodeReservasi("RSV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            reservasi.setTanggalReservasi(LocalDate.now());
            reservasi.setStatus("pending");

            reservasiId = reservasiController.buatReservasi(reservasi); // Store the new reservation ID

            if (reservasiId != -1) {
                boolean allPassengersSaved = true;
                for (String penumpangNama : penumpangList) {
                    if (!reservasiController.tambahPenumpang(reservasiId, penumpangNama)) {
                        allPassengersSaved = false;
                    }
                    System.out.println("DEBUG PanelBookingScreen - Menambahkan penumpang: " + penumpangNama + " untuk Reservasi ID: " + reservasiId);
                }

                if (allPassengersSaved) {
                    JOptionPane.showMessageDialog(this, "Reservasi berhasil dibuat. Lanjutkan ke pembayaran.", "Reservasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Reservasi berhasil dibuat, tetapi gagal menyimpan beberapa data penumpang. Silakan cek detail reservasi Anda.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
                mainAppFrame.showPaymentPanel(reservasiId, namaKontak, emailKontak, teleponKontak, penumpangList);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membuat reservasi. Silakan coba lagi.", "Error Booking", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            if (!Session.isLoggedIn() || Session.currentUser == null) {
                JOptionPane.showMessageDialog(this, "Anda harus login untuk melakukan booking.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else if (currentPaket == null) {
                JOptionPane.showMessageDialog(this, "Data paket tidak tersedia.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                System.err.println("MainAppFrame is null in PanelBookingScreen (btn_selanjutnya)");
            }
        }
    }

    private void btnSimpanDrafActionPerformed(ActionEvent evt) {
        if (!Session.isLoggedIn() || Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "Anda harus login untuk menyimpan draf.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (currentPaket == null) {
            JOptionPane.showMessageDialog(this, "Tidak ada paket perjalanan yang dipilih untuk disimpan sebagai draf.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtNamaKontak.getText().trim().isEmpty() || txtEmailKontak.getText().trim().isEmpty() || txtTeleponKontak.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi setidaknya Nama, Email, dan Telepon kontak untuk draf.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String namaKontak = txtNamaKontak.getText().trim();
        String emailKontak = txtEmailKontak.getText().trim();
        String teleponKontak = txtTeleponKontak.getText().trim();
        
        List<String> penumpangList = new ArrayList<>();
        try {
            int jumlah = Integer.parseInt(tf_jumlahpenumpang.getText().trim());
            if (jumlah > 3) jumlah = 3; 

            if (jumlah >= 1 && !tb_passangerdata.getText().trim().equals("Nama Penumpang 1 (sesuai KTP)")) penumpangList.add(tb_passangerdata.getText().trim());
            if (jumlah >= 2 && !tb_passangerdata1.getText().trim().equals("Nama Penumpang 2 (sesuai KTP)")) penumpangList.add(tb_passangerdata1.getText().trim());
            if (jumlah >= 3 && !tb_passangerdata2.getText().trim().equals("Nama Penumpang 3 (sesuai KTP)")) penumpangList.add(tb_passangerdata2.getText().trim());
        } catch (NumberFormatException e) { 
            System.out.println("No valid passenger count for draft, skipping passenger save.");
        }

        System.out.println("DEBUG PanelBookingScreen - Penumpang List (btnSimpanDraf): " + penumpangList);
        System.out.println("DEBUG PanelBookingScreen - Jumlah Penumpang dari field (draf): " + tf_jumlahpenumpang.getText().trim());


        ReservasiModel reservasiDraf = new ReservasiModel();
        reservasiDraf.setUserId(Session.currentUser.getId());
        reservasiDraf.setTripType("paket_perjalanan");
        reservasiDraf.setTripId(currentPaket.getId());
        reservasiDraf.setKodeReservasi("DRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        reservasiDraf.setTanggalReservasi(LocalDate.now());
        reservasiDraf.setStatus("pending");

        reservasiId = reservasiController.buatReservasi(reservasiDraf); // Store the new reservation ID

        if (reservasiId != -1) {
            boolean allPassengersSaved = true;
            for (String penumpangNama : penumpangList) {
                if (!penumpangNama.isEmpty()) {
                    if (!reservasiController.tambahPenumpang(reservasiId, penumpangNama)) {
                        allPassengersSaved = false;
                    }
                    System.out.println("DEBUG PanelBookingScreen - Menambahkan draf penumpang: " + penumpangNama + " untuk Reservasi ID: " + reservasiId);
                }
            }

            if (allPassengersSaved) {
                 JOptionPane.showMessageDialog(this, "Draf pesanan berhasil disimpan dengan ID: " + reservasiId + ". Anda akan dialihkan ke halaman Pesanan Saya.", "Draf Disimpan", JOptionPane.INFORMATION_MESSAGE);
            } else {
                 JOptionPane.showMessageDialog(this, "Draf reservasi berhasil disimpan, tetapi gagal menyimpan beberapa data penumpang. Silakan cek detail reservasi Anda.", "Peringatan Draf", JOptionPane.WARNING_MESSAGE);
            }
            mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan draf pesanan. Silakan coba lagi.", "Error Draf", JOptionPane.ERROR_MESSAGE);
        }
    }
}
