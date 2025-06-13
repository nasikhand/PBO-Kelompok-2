package managementtrevel.TripOrder;

import Asset.AppTheme;
import db.Koneksi;
import db.dao.PembayaranDAO;
import db.dao.PenumpangDAO;
import db.dao.ReservasiDAO;
import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;
import model.CustomTripModel;
import model.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

public class PanelOrderDetail extends JPanel {

    private MainAppFrame mainAppFrame;
    private ReservasiModel displayedReservasi;

    // DAO instances
    private PenumpangDAO penumpangDAO;
    private PembayaranDAO pembayaranDAO;
    private ReservasiDAO reservasiDAO;

    // UI Components
    private JLabel lblTitle;
    private JButton btnKembali;
    private JButton btnLanjutkanBooking;
    private JButton btnBayar;
    private JButton btnBatalkanPesanan; // <--- Tombol baru

    // Detail Labels
    private JLabel lblNamaKontak;
    private JLabel lblEmailKontak;
    private JLabel lblTeleponKontak;
    private JLabel lblJumlahPenumpang;
    private JLabel lblNamaPenumpang1;
    private JLabel lblNamaPenumpang2;
    private JLabel lblNamaPenumpang3;

    private JLabel lblKodeReservasi;
    private JLabel lblStatusReservasi;
    private JLabel lblTanggalReservasi;
    private JLabel lblNamaTrip;
    private JLabel lblKotaTujuan;
    private JLabel lblTanggalPerjalanan;
    private JLabel lblHargaPerOrang;
    private JLabel lblTotalHargaFinal;
    private JLabel lblRating;

    // Formatter for date and currency
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMMyyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public PanelOrderDetail(MainAppFrame mainAppFrame, ReservasiModel reservasi) {
        this.mainAppFrame = mainAppFrame;
        this.displayedReservasi = reservasi;

        this.penumpangDAO = new PenumpangDAO(Koneksi.getConnection());
        this.pembayaranDAO = new PembayaranDAO(Koneksi.getConnection());
        this.reservasiDAO = new ReservasiDAO(Koneksi.getConnection());

        initializeUI();
        applyAppTheme();
        setupActionListeners();
        loadOrderDetailData();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        btnKembali = new JButton("< Kembali");
        headerPanel.add(btnKembali, BorderLayout.WEST);

        lblTitle = new JLabel("Detail Pesanan Anda");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitle, BorderLayout.CENTER);

        JLabel dummyEast = new JLabel();
        dummyEast.setPreferredSize(btnKembali.getPreferredSize());
        headerPanel.add(dummyEast, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);

        JPanel mainContentArea = new JPanel(new GridBagLayout());
        mainContentArea.setOpaque(false);
        JScrollPane scrollableContent = new JScrollPane(mainContentArea);
        scrollableContent.setBorder(null);
        scrollableContent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollableContent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollableContent.getVerticalScrollBar().setUnitIncrement(16);
        scrollableContent.setOpaque(false);
        scrollableContent.getViewport().setOpaque(false);


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 0.5;
        gbc.weighty = 0.1;

        lblNamaKontak = new JLabel(); lblEmailKontak = new JLabel(); lblTeleponKontak = new JLabel();
        lblJumlahPenumpang = new JLabel(); lblNamaPenumpang1 = new JLabel(); lblNamaPenumpang2 = new JLabel(); lblNamaPenumpang3 = new JLabel();
        lblKodeReservasi = new JLabel(); lblStatusReservasi = new JLabel(); lblTanggalReservasi = new JLabel();
        lblNamaTrip = new JLabel(); lblKotaTujuan = new JLabel(); lblTanggalPerjalanan = new JLabel();
        lblHargaPerOrang = new JLabel(); lblRating = new JLabel();
        lblTotalHargaFinal = new JLabel();


        JPanel kontakPanel = createSectionPanel("Informasi Kontak Pemesan");
        kontakPanel.setLayout(new BoxLayout(kontakPanel, BoxLayout.Y_AXIS));
        addLabelToBoxLayoutPanel(kontakPanel, lblNamaKontak);
        addLabelToBoxLayoutPanel(kontakPanel, lblEmailKontak);
        addLabelToBoxLayoutPanel(kontakPanel, lblTeleponKontak);
        gbc.gridx = 0; gbc.gridy = 0;
        mainContentArea.add(kontakPanel, gbc);

        JPanel reservasiInfoPanel = createSectionPanel("Informasi Reservasi");
        reservasiInfoPanel.setLayout(new BoxLayout(reservasiInfoPanel, BoxLayout.Y_AXIS));
        addLabelToBoxLayoutPanel(reservasiInfoPanel, lblKodeReservasi);
        addLabelToBoxLayoutPanel(reservasiInfoPanel, lblStatusReservasi);
        addLabelToBoxLayoutPanel(reservasiInfoPanel, lblTanggalReservasi);
        gbc.gridx = 1; gbc.gridy = 0;
        mainContentArea.add(reservasiInfoPanel, gbc);

        JPanel penumpangPanel = createSectionPanel("Detail Penumpang");
        penumpangPanel.setLayout(new BoxLayout(penumpangPanel, BoxLayout.Y_AXIS));
        addLabelToBoxLayoutPanel(penumpangPanel, lblJumlahPenumpang);
        penumpangPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addLabelToBoxLayoutPanel(penumpangPanel, lblNamaPenumpang1);
        addLabelToBoxLayoutPanel(penumpangPanel, lblNamaPenumpang2);
        addLabelToBoxLayoutPanel(penumpangPanel, lblNamaPenumpang3);
        gbc.gridx = 0; gbc.gridy = 1;
        mainContentArea.add(penumpangPanel, gbc);

        JPanel ringkasanPerjalananPanel = createSectionPanel("Ringkasan Perjalanan");
        ringkasanPerjalananPanel.setLayout(new BoxLayout(ringkasanPerjalananPanel, BoxLayout.Y_AXIS));
        addLabelToBoxLayoutPanel(ringkasanPerjalananPanel, lblNamaTrip);
        addLabelToBoxLayoutPanel(ringkasanPerjalananPanel, lblKotaTujuan);
        addLabelToBoxLayoutPanel(ringkasanPerjalananPanel, lblTanggalPerjalanan);
        addLabelToBoxLayoutPanel(ringkasanPerjalananPanel, lblHargaPerOrang);
        addLabelToBoxLayoutPanel(ringkasanPerjalananPanel, lblRating);
        gbc.gridx = 1; gbc.gridy = 1;
        mainContentArea.add(ringkasanPerjalananPanel, gbc);
        
        JPanel totalHargaPanel = new JPanel();
        totalHargaPanel.setOpaque(false);
        totalHargaPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        totalHargaPanel.add(lblTotalHargaFinal);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.05;
        mainContentArea.add(totalHargaPanel, gbc);

        // --- Action Buttons Panel (Bottom) ---
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionButtonPanel.setOpaque(false);
        
        btnLanjutkanBooking = new JButton("Lanjutkan Booking");
        btnBayar = new JButton("Bayar Sekarang");
        btnBatalkanPesanan = new JButton("Batalkan Pesanan"); // <--- Inisialisasi tombol baru

        actionButtonPanel.add(btnLanjutkanBooking);
        actionButtonPanel.add(btnBayar);
        actionButtonPanel.add(btnBatalkanPesanan); // <--- Tambahkan tombol baru ke panel

        this.add(scrollableContent, BorderLayout.CENTER);
        this.add(actionButtonPanel, BorderLayout.SOUTH);
    }

    private void addLabelToBoxLayoutPanel(JPanel panel, JLabel label) {
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
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

        stylePrimaryButton(btnLanjutkanBooking, "Lanjutkan Booking");
        stylePrimaryButton(btnBayar, "Bayar Sekarang");
        styleSecondaryButton(btnBatalkanPesanan, "Batalkan Pesanan"); // <--- Style tombol baru

        JLabel[] detailLabels = {
            lblKodeReservasi, lblStatusReservasi, lblTanggalReservasi,
            lblNamaTrip, lblKotaTujuan, lblTanggalPerjalanan,
            lblJumlahPenumpang, lblNamaKontak, lblEmailKontak, lblTeleponKontak,
            lblNamaPenumpang1, lblNamaPenumpang2, lblNamaPenumpang3
        };
        for (JLabel label : detailLabels) {
            if (label != null) {
                label.setFont(AppTheme.FONT_LABEL_FORM);
                label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            }
        }
        
        if (lblHargaPerOrang != null) {
            lblHargaPerOrang.setFont(AppTheme.FONT_PRIMARY_MEDIUM_BOLD);
            lblHargaPerOrang.setForeground(AppTheme.TEXT_DARK);
        }
        if (lblRating != null) {
            lblRating.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            lblRating.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        }
        if (lblNamaTrip != null) {
            lblNamaTrip.setFont(AppTheme.FONT_SUBTITLE);
            lblNamaTrip.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }

        if (lblTotalHargaFinal != null) {
            lblTotalHargaFinal.setFont(AppTheme.FONT_TITLE_MEDIUM_BOLD);
            lblTotalHargaFinal.setForeground(AppTheme.ACCENT_ORANGE);
            lblTotalHargaFinal.setBorder(new EmptyBorder(10, 0, 0, 0));
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
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private void setupActionListeners() {
        if (btnKembali != null) {
            btnKembali.addActionListener(this::btnKembaliActionPerformed);
        }
        if (btnLanjutkanBooking != null) {
            btnLanjutkanBooking.addActionListener(this::btnLanjutkanBookingActionPerformed);
        }
        if (btnBayar != null) {
            btnBayar.addActionListener(this::btnBayarActionPerformed);
        }
        if (btnBatalkanPesanan != null) { // <--- Tambahkan listener untuk tombol baru
            btnBatalkanPesanan.addActionListener(this::btnBatalkanPesananActionPerformed);
        }
    }

    /**
     * Memuat data detail pesanan dan paket/custom trip ke dalam label-label UI.
     */
    private void loadOrderDetailData() {
        if (displayedReservasi == null) {
            JOptionPane.showMessageDialog(this, "Tidak ada data reservasi untuk ditampilkan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // --- Populate Contact Information ---
        UserModel currentUser = Session.currentUser;
        if (currentUser != null) {
            lblNamaKontak.setText("Nama Kontak: " + (currentUser.getNamaLengkap() != null ? currentUser.getNamaLengkap() : "-"));
            lblEmailKontak.setText("Email Kontak: " + (currentUser.getEmail() != null ? currentUser.getEmail() : "-"));
            lblTeleponKontak.setText("No. Telepon Kontak: " + (currentUser.getNomorTelepon() != null ? currentUser.getNomorTelepon() : "-"));
        } else {
            lblNamaKontak.setText("Nama Kontak: N/A (belum login)");
            lblEmailKontak.setText("Email Kontak: N/A (belum login)");
            lblTeleponKontak.setText("No. Telepon Kontak: N/A (belum login)");
        }

        // --- Populate Reservation Information ---
        lblKodeReservasi.setText("Kode Reservasi: " + displayedReservasi.getKodeReservasi());
        lblStatusReservasi.setText("Status: " + displayedReservasi.getStatus());
        if (displayedReservasi.getTanggalReservasi() != null) {
            lblTanggalReservasi.setText("Tanggal Reservasi: " + displayedReservasi.getTanggalReservasi().format(DATE_FORMATTER));
        } else {
            lblTanggalReservasi.setText("Tanggal Reservasi: N/A");
        }

        // --- Populate Trip Summary & Passenger Details ---
        double totalHargaUntukTampilan = 0.0;
        int jumlahPenumpang = penumpangDAO.getJumlahPenumpangByReservasiId(displayedReservasi.getId());
        lblJumlahPenumpang.setText("Jumlah Penumpang: " + jumlahPenumpang);

        List<String> penumpangNames = penumpangDAO.getNamaPenumpangByReservasiId(displayedReservasi.getId());
        lblNamaPenumpang1.setText("Nama Penumpang 1: " + (penumpangNames.size() > 0 ? penumpangNames.get(0) : "-"));
        lblNamaPenumpang2.setText("Nama Penumpang 2: " + (penumpangNames.size() > 1 ? penumpangNames.get(1) : "-"));
        lblNamaPenumpang3.setText("Nama Penumpang 3: " + (penumpangNames.size() > 2 ? penumpangNames.get(2) : "-"));


        if ("paket_perjalanan".equals(displayedReservasi.getTripType()) && displayedReservasi.getPaket() != null) {
            PaketPerjalananModel paket = displayedReservasi.getPaket();
            lblNamaTrip.setText("Nama Paket: " + (paket.getNamaPaket() != null ? paket.getNamaPaket() : "-"));
            lblKotaTujuan.setText("Kota Tujuan: " + (paket.getNamaKota() != null ? paket.getNamaKota() : "-"));
            lblTanggalPerjalanan.setText("Tanggal: " + (paket.getTanggalMulai() != null ? paket.getTanggalMulai() : "-") + " s/d " + (paket.getTanggalAkhir() != null ? paket.getTanggalAkhir() : "-"));
            lblHargaPerOrang.setText("Harga per Orang: " + CURRENCY_FORMATTER.format(paket.getHarga()));
            lblRating.setText("Rating: " + String.format("%.1f", paket.getRating()));
            lblRating.setVisible(true);

            Double pembayaranLunas = pembayaranDAO.getJumlahPembayaranByReservasiId(displayedReservasi.getId());
            if (pembayaranLunas != null && pembayaranLunas > 0) {
                totalHargaUntukTampilan = pembayaranLunas;
            } else {
                totalHargaUntukTampilan = paket.getHarga() * jumlahPenumpang;
            }


        } else if ("custom_trip".equals(displayedReservasi.getTripType()) && displayedReservasi.getCustomTrip() != null) {
            CustomTripModel customTrip = displayedReservasi.getCustomTrip();
            lblNamaTrip.setText("Nama Trip Kustom: " + (customTrip.getNamaTrip() != null ? customTrip.getNamaTrip() : "-"));
            lblKotaTujuan.setText("Kota Tujuan: " + (customTrip.getNamaKota() != null ? customTrip.getNamaKota() : "-"));
            
            String tanggalMulaiStr = (customTrip.getTanggalMulai() != null ? customTrip.getTanggalMulai().format(DATE_FORMATTER) : "-");
            String tanggalAkhirStr = (customTrip.getTanggalAkhir() != null ? customTrip.getTanggalAkhir().format(DATE_FORMATTER) : "-");
            lblTanggalPerjalanan.setText("Tanggal: " + tanggalMulaiStr + " s/d " + tanggalAkhirStr);
            
            lblHargaPerOrang.setText("Harga Perjalanan: " + CURRENCY_FORMATTER.format(customTrip.getTotalHarga()));
            lblRating.setVisible(false);

            totalHargaUntukTampilan = customTrip.getTotalHarga();

        } else {
            lblNamaTrip.setText("Jenis Perjalanan: Tidak Diketahui");
            lblKotaTujuan.setText("Kota Tujuan: -");
            lblTanggalPerjalanan.setText("Tanggal: -");
            lblHargaPerOrang.setText("Harga: -");
            lblRating.setVisible(false);
            totalHargaUntukTampilan = 0.0;
        }

        lblTotalHargaFinal.setText("Total Harga: " + CURRENCY_FORMATTER.format(totalHargaUntukTampilan));

        // --- Conditional Logic for Action Buttons ---
        String status = displayedReservasi.getStatus();
        System.out.println("DEBUG PanelOrderDetail - Current Reservation Status: " + status);

        // Hide all buttons by default
        btnLanjutkanBooking.setVisible(false);
        btnBayar.setVisible(false);
        btnBatalkanPesanan.setVisible(false); // <--- Sembunyikan tombol batal default

        if ("pending".equals(status)) {
            btnLanjutkanBooking.setVisible(true);
            btnLanjutkanBooking.setEnabled(true);
            btnBatalkanPesanan.setVisible(true); // <--- Tampilkan tombol batal
            btnBatalkanPesanan.setEnabled(true); // <--- Aktifkan tombol batal
            btnBayar.setEnabled(false);
        } else if ("dipesan".equals(status)) {
            btnBayar.setVisible(true);
            btnBayar.setEnabled(true);
            btnLanjutkanBooking.setEnabled(false);
            btnBatalkanPesanan.setVisible(false); // Jangan tampilkan batal jika sudah dipesan
        } else if ("dibayar".equals(status) || "selesai".equals(status)) {
            btnLanjutkanBooking.setEnabled(false);
            btnBayar.setEnabled(false);
            btnBatalkanPesanan.setEnabled(false); // Disable batal
            // Anda bisa memilih untuk tetap menampilkan tombol dengan status disabled,
            // atau menyembunyikannya sepenuhnya. Untuk sekarang, saya akan menyembunyikan
            // tombol aksi yang tidak relevan.
        }
    }

    private void btnKembaliActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
        } else {
            System.err.println("MainAppFrame is null in PanelOrderDetail (btnKembali)");
        }
    }

    private void btnLanjutkanBookingActionPerformed(ActionEvent evt) {
        if ("pending".equals(displayedReservasi.getStatus())) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin melanjutkan booking ini? Status akan diubah menjadi 'dipesan'.",
                    "Konfirmasi Lanjutkan Booking",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = reservasiDAO.updateStatusReservasi(displayedReservasi.getId(), "dipesan");
                if (success) {
                    JOptionPane.showMessageDialog(this, "Status reservasi berhasil diperbarui menjadi 'dipesan'. Anda sekarang dapat melakukan pembayaran.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    displayedReservasi = reservasiDAO.getReservasiById(displayedReservasi.getId());
                    loadOrderDetailData();
                    mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui status reservasi.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Aksi 'Lanjutkan Booking' hanya tersedia untuk reservasi berstatus 'pending'.", "Invalid Status", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnBayarActionPerformed(ActionEvent evt) {
        if ("dipesan".equals(displayedReservasi.getStatus())) {
            String namaKontak = Session.currentUser != null ? Session.currentUser.getNamaLengkap() : "";
            String emailKontak = Session.currentUser != null ? Session.currentUser.getEmail() : "";
            String teleponKontak = Session.currentUser != null ? Session.currentUser.getNomorTelepon() : "";
            List<String> penumpangList = penumpangDAO.getNamaPenumpangByReservasiId(displayedReservasi.getId());

            mainAppFrame.showPaymentPanel(displayedReservasi.getId(), namaKontak, emailKontak, teleponKontak, penumpangList);
        } else {
            JOptionPane.showMessageDialog(this, "Aksi 'Bayar Sekarang' hanya tersedia untuk reservasi berstatus 'dipesan'.", "Invalid Status", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Action for the "Batalkan Pesanan" button.
     * This is typically for 'pending' reservations.
     * Deletes the reservation from the database.
     */
    private void btnBatalkanPesananActionPerformed(ActionEvent evt) {
        if ("pending".equals(displayedReservasi.getStatus())) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Anda yakin ingin membatalkan pesanan dengan kode: " + displayedReservasi.getKodeReservasi() + "?\n" +
                    "Pembatalan ini akan menghapus pesanan secara permanen.",
                    "Konfirmasi Pembatalan Pesanan",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = reservasiDAO.deleteReservasi(displayedReservasi.getId());
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Pesanan berhasil dibatalkan dan dihapus.", "Pembatalan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    // Setelah penghapusan, kembali ke daftar pesanan aktif (PANEL_PESANAN_SAYA)
                    mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal membatalkan pesanan. Silakan coba lagi.", "Pembatalan Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pesanan hanya dapat dibatalkan jika berstatus 'pending'.", "Status Tidak Valid", JOptionPane.WARNING_MESSAGE);
        }
    }
}
