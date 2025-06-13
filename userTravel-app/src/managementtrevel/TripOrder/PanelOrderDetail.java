package managementtrevel.TripOrder;

import Asset.AppTheme;
import db.Koneksi; // Import Koneksi untuk inisialisasi DAO
import db.dao.KotaDAO; // Hanya jika diperlukan, tapi sepertinya sudah di-join di ReservasiDAO
import db.dao.PembayaranDAO; // Tambahkan import ini
import db.dao.PenumpangDAO; // Tambahkan import ini
import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session; // Untuk mendapatkan data user yang login
import model.CustomTripModel; // Tambahkan import ini
import db.dao.ReservasiDAO; // Mungkin diperlukan untuk mengambil reservasi by ID jika tidak passing objek penuh

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.time.format.DateTimeFormatter; // Untuk format tanggal
import java.text.NumberFormat; // Untuk format mata uang
import java.util.Locale; // Untuk format mata uang

public class PanelOrderDetail extends JPanel {

    private MainAppFrame mainAppFrame;
    private ReservasiModel displayedReservasi;
    // private PaketPerjalananModel displayedPaket; // Tidak diperlukan lagi, karena sudah di dalam displayedReservasi

    // DAO instances
    private PenumpangDAO penumpangDAO;
    private PembayaranDAO pembayaranDAO;
    private ReservasiDAO reservasiDAO; // Jika perlu mengambil ulang reservasi dari ID

    // Komponen UI Utama (dibuat programatik)
    private JLabel lblTitle;
    private JButton btnKembali;

    // Panel Kiri (Data Detail: Kontak & Penumpang)
    private JPanel panelDataDetail;
    private JLabel lblNamaKontak;
    private JLabel lblEmailKontak;
    private JLabel lblTeleponKontak;
    private JLabel lblJumlahPenumpang;
    private JLabel lblNamaPenumpang1;
    private JLabel lblNamaPenumpang2;
    private JLabel lblNamaPenumpang3;

    // Panel Kanan (Ringkasan Final & Informasi Reservasi)
    private JPanel panelRingkasanFinal;
    private JLabel lblKodeReservasi;
    private JLabel lblStatusReservasi;
    private JLabel lblTanggalReservasi;
    private JLabel lblNamaTrip; // Diubah dari lblNamaPaket agar lebih umum
    private JLabel lblKotaTujuan;
    private JLabel lblTanggalPerjalanan;
    private JLabel lblHargaPerOrang;
    private JLabel lblTotalHargaFinal;
    private JLabel lblRating; // Tambahan untuk rating paket jika ada

    // Formatter untuk tanggal dan mata uang
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(new Locale("id", "ID")); // IDR

    /**
     * Konstruktor untuk PanelOrderDetail.
     * Menerima MainAppFrame dan objek ReservasiModel yang sudah lengkap.
     * @param mainAppFrame Referensi ke MainAppFrame.
     * @param reservasi Model Reservasi yang akan ditampilkan detailnya.
     */
    public PanelOrderDetail(MainAppFrame mainAppFrame, ReservasiModel reservasi) {
        this.mainAppFrame = mainAppFrame;
        this.displayedReservasi = reservasi;
        // this.displayedPaket = paket; // Tidak perlu lagi

        // Inisialisasi DAO
        this.penumpangDAO = new PenumpangDAO(Koneksi.getConnection());
        this.pembayaranDAO = new PembayaranDAO(Koneksi.getConnection());
        this.reservasiDAO = new ReservasiDAO(Koneksi.getConnection()); // Untuk jaga-jaga jika perlu fetch ulang

        initializeUI();
        applyAppTheme();
        setupActionListeners();
        loadOrderDetailData(); // Panggil metode untuk mengisi data
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 1. Header Panel
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

        // 2. Main Content Area (Split Pane)
        panelDataDetail = new JPanel();
        panelDataDetail.setLayout(new BoxLayout(panelDataDetail, BoxLayout.Y_AXIS));
        panelDataDetail.setOpaque(false);
        panelDataDetail.setBorder(new EmptyBorder(0, 0, 0, 10));

        JPanel panelKontak = createSectionPanel("Informasi Kontak Pemesan");
        panelKontak.setLayout(new BoxLayout(panelKontak, BoxLayout.Y_AXIS));
        lblNamaKontak = new JLabel("Nama Kontak: -");
        lblEmailKontak = new JLabel("Email Kontak: -");
        lblTeleponKontak = new JLabel("No. Telepon Kontak: -");
        addLabelToPanel(panelKontak, lblNamaKontak);
        addLabelToPanel(panelKontak, lblEmailKontak);
        addLabelToPanel(panelKontak, lblTeleponKontak);
        panelDataDetail.add(panelKontak);
        panelDataDetail.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel panelPenumpang = createSectionPanel("Detail Penumpang");
        panelPenumpang.setLayout(new BoxLayout(panelPenumpang, BoxLayout.Y_AXIS));
        lblJumlahPenumpang = new JLabel("Jumlah Penumpang: -");
        lblNamaPenumpang1 = new JLabel("Nama Penumpang 1: -");
        lblNamaPenumpang2 = new JLabel("Nama Penumpang 2: -");
        lblNamaPenumpang3 = new JLabel("Nama Penumpang 3: -");
        addLabelToPanel(panelPenumpang, lblJumlahPenumpang);
        panelPenumpang.add(Box.createRigidArea(new Dimension(0, 5)));
        addLabelToPanel(panelPenumpang, lblNamaPenumpang1);
        addLabelToPanel(panelPenumpang, lblNamaPenumpang2);
        addLabelToPanel(panelPenumpang, lblNamaPenumpang3);
        panelDataDetail.add(panelPenumpang);
        panelDataDetail.add(Box.createVerticalGlue());

        panelRingkasanFinal = new JPanel();
        panelRingkasanFinal.setLayout(new BoxLayout(panelRingkasanFinal, BoxLayout.Y_AXIS));
        panelRingkasanFinal.setOpaque(false);
        panelRingkasanFinal.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel panelReservasiInfo = createSectionPanel("Informasi Reservasi");
        panelReservasiInfo.setLayout(new BoxLayout(panelReservasiInfo, BoxLayout.Y_AXIS));
        lblKodeReservasi = new JLabel("Kode Reservasi: -");
        lblStatusReservasi = new JLabel("Status: -");
        lblTanggalReservasi = new JLabel("Tanggal Reservasi: -");
        addLabelToPanel(panelReservasiInfo, lblKodeReservasi);
        addLabelToPanel(panelReservasiInfo, lblStatusReservasi);
        addLabelToPanel(panelReservasiInfo, lblTanggalReservasi);
        panelRingkasanFinal.add(panelReservasiInfo);
        panelRingkasanFinal.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel panelRingkasan = createSectionPanel("Ringkasan Perjalanan");
        panelRingkasan.setLayout(new BoxLayout(panelRingkasan, BoxLayout.Y_AXIS));
        lblNamaTrip = new JLabel("Nama Perjalanan: -"); // Diubah
        lblKotaTujuan = new JLabel("Kota Tujuan: -");
        lblTanggalPerjalanan = new JLabel("Tanggal Perjalanan: -");
        lblHargaPerOrang = new JLabel("Harga per Orang: -");
        lblRating = new JLabel("Rating: -"); // Label baru untuk rating
        addLabelToPanel(panelRingkasan, lblNamaTrip);
        addLabelToPanel(panelRingkasan, lblKotaTujuan);
        addLabelToPanel(panelRingkasan, lblTanggalPerjalanan);
        addLabelToPanel(panelRingkasan, lblHargaPerOrang);
        addLabelToPanel(panelRingkasan, lblRating); // Tambahkan rating
        panelRingkasanFinal.add(panelRingkasan);
        panelRingkasanFinal.add(Box.createVerticalGlue());

        JPanel totalHargaPanel = new JPanel();
        totalHargaPanel.setOpaque(false);
        totalHargaPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        lblTotalHargaFinal = new JLabel("Total Harga: Rp 0");
        totalHargaPanel.add(lblTotalHargaFinal);
        panelRingkasanFinal.add(totalHargaPanel);

        JScrollPane scrollPanelDataDetail = new JScrollPane(panelDataDetail);
        scrollPanelDataDetail.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanelDataDetail.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelDataDetail.setOpaque(false);
        scrollPanelDataDetail.getViewport().setOpaque(false);
        scrollPanelDataDetail.setBorder(null);

        JScrollPane scrollPanelRingkasanFinal = new JScrollPane(panelRingkasanFinal);
        scrollPanelRingkasanFinal.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanelRingkasanFinal.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPanelRingkasanFinal.setOpaque(false);
        scrollPanelRingkasanFinal.getViewport().setOpaque(false);
        scrollPanelRingkasanFinal.setBorder(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelDataDetail, scrollPanelRingkasanFinal);
        splitPane.setDividerLocation(0.5);
        splitPane.setResizeWeight(0.5);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void addLabelToPanel(JPanel panel, JLabel label) {
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JPanel createSectionPanel(String title) {
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

        Font detailLabelFont = AppTheme.FONT_PRIMARY_DEFAULT;
        Color detailTextColor = AppTheme.TEXT_SECONDARY_DARK;

        JLabel[] detailLabels = {
            lblKodeReservasi, lblStatusReservasi, lblTanggalReservasi,
            lblNamaTrip, lblKotaTujuan, lblTanggalPerjalanan,
            lblHargaPerOrang, lblJumlahPenumpang, lblRating, // Tambahkan lblRating
            lblNamaKontak, lblEmailKontak, lblTeleponKontak,
            lblNamaPenumpang1, lblNamaPenumpang2, lblNamaPenumpang3
        };
        for (JLabel label : detailLabels) {
            if (label != null) {
                label.setFont(detailLabelFont);
                label.setForeground(detailTextColor);
            }
        }

        if (lblTotalHargaFinal != null) {
            Font totalHargaFont = AppTheme.FONT_TITLE_MEDIUM != null ? AppTheme.FONT_TITLE_MEDIUM.deriveFont(Font.BOLD) : new Font("Segoe UI", Font.BOLD, 18);
            lblTotalHargaFinal.setFont(totalHargaFont);
            lblTotalHargaFinal.setForeground(AppTheme.ACCENT_ORANGE);
            lblTotalHargaFinal.setBorder(new EmptyBorder(10, 0, 0, 0));
        }
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
    }

    /**
     * Memuat data detail pesanan dan paket/custom trip ke dalam label-label UI.
     */
    private void loadOrderDetailData() {
        if (displayedReservasi == null) {
            JOptionPane.showMessageDialog(this, "Tidak ada data reservasi untuk ditampilkan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Isi Informasi Kontak Pemesan
        if (Session.isLoggedIn() && Session.currentUser != null) {
            lblNamaKontak.setText("Nama Kontak: " + Session.currentUser.getNamaLengkap());
            lblEmailKontak.setText("Email Kontak: " + Session.currentUser.getEmail());
            lblTeleponKontak.setText("No. Telepon Kontak: " + Session.currentUser.getNomorTelepon());
        } else {
            lblNamaKontak.setText("Nama Kontak: N/A (belum login)");
            lblEmailKontak.setText("Email Kontak: N/A (belum login)");
            lblTeleponKontak.setText("No. Telepon Kontak: N/A (belum login)");
        }

        // Isi Informasi Reservasi
        lblKodeReservasi.setText("Kode Reservasi: " + displayedReservasi.getKodeReservasi());
        lblStatusReservasi.setText("Status: " + displayedReservasi.getStatus());
        if (displayedReservasi.getTanggalReservasi() != null) {
            lblTanggalReservasi.setText("Tanggal Reservasi: " + displayedReservasi.getTanggalReservasi().format(DATE_FORMATTER));
        } else {
            lblTanggalReservasi.setText("Tanggal Reservasi: N/A");
        }

        // Isi Ringkasan Perjalanan & Detail Penumpang
        double totalHarga = 0.0;
        int jumlahPenumpang = penumpangDAO.getJumlahPenumpangByReservasiId(displayedReservasi.getId());
        lblJumlahPenumpang.setText("Jumlah Penumpang: " + jumlahPenumpang);

        List<String> penumpangNames = penumpangDAO.getNamaPenumpangByReservasiId(displayedReservasi.getId());
        lblNamaPenumpang1.setText("Nama Penumpang 1: " + (penumpangNames.size() > 0 ? penumpangNames.get(0) : "-"));
        lblNamaPenumpang2.setText("Nama Penumpang 2: " + (penumpangNames.size() > 1 ? penumpangNames.get(1) : "-"));
        lblNamaPenumpang3.setText("Nama Penumpang 3: " + (penumpangNames.size() > 2 ? penumpangNames.get(2) : "-"));


        if ("paket_perjalanan".equals(displayedReservasi.getTripType()) && displayedReservasi.getPaket() != null) {
            PaketPerjalananModel paket = displayedReservasi.getPaket();
            lblNamaTrip.setText("Nama Paket: " + paket.getNamaPaket());
            lblKotaTujuan.setText("Kota Tujuan: " + paket.getNamaKota());
            lblTanggalPerjalanan.setText("Tanggal: " + paket.getTanggalMulai() + " s/d " + paket.getTanggalAkhir());
            lblHargaPerOrang.setText("Harga per Orang: " + CURRENCY_FORMATTER.format(paket.getHarga()));
            lblRating.setText("Rating: " + String.format("%.1f", paket.getRating()));
            lblRating.setVisible(true); // Tampilkan rating untuk paket

            // Hitung total harga paket
            Double pembayaranLunas = pembayaranDAO.getJumlahPembayaranByReservasiId(displayedReservasi.getId());
            if (pembayaranLunas != null && pembayaranLunas > 0) {
                totalHarga = pembayaranLunas; // Jika sudah ada pembayaran, gunakan itu
            } else {
                totalHarga = paket.getHarga() * jumlahPenumpang; // Jika belum, hitung dari harga per orang * jumlah penumpang
            }


        } else if ("custom_trip".equals(displayedReservasi.getTripType()) && displayedReservasi.getCustomTrip() != null) {
            CustomTripModel customTrip = displayedReservasi.getCustomTrip();
            lblNamaTrip.setText("Nama Trip Kustom: " + customTrip.getNamaTrip());
            lblKotaTujuan.setText("Kota Tujuan: " + customTrip.getNamaKota()); // Nama kota dari CustomTripModel
            if (customTrip.getTanggalMulai() != null && customTrip.getTanggalAkhir() != null) {
                lblTanggalPerjalanan.setText("Tanggal: " + customTrip.getTanggalMulai().format(DATE_FORMATTER) + " s/d " + customTrip.getTanggalAkhir().format(DATE_FORMATTER));
            } else {
                lblTanggalPerjalanan.setText("Tanggal: N/A");
            }
            lblHargaPerOrang.setText("Harga Perjalanan: " + CURRENCY_FORMATTER.format(customTrip.getTotalHarga())); // Harga total untuk custom trip
            lblRating.setVisible(false); // Sembunyikan rating untuk custom trip

            totalHarga = customTrip.getTotalHarga(); // Gunakan total harga dari CustomTripModel

        } else {
            lblNamaTrip.setText("Jenis Perjalanan: Tidak Diketahui");
            lblKotaTujuan.setText("Kota Tujuan: -");
            lblTanggalPerjalanan.setText("Tanggal: -");
            lblHargaPerOrang.setText("Harga: -");
            lblRating.setVisible(false);
            totalHarga = 0.0;
        }

        lblTotalHargaFinal.setText("Total Harga: " + CURRENCY_FORMATTER.format(totalHarga));
    }

    /**
     * Aksi saat tombol "Kembali" ditekan.
     * Mengarahkan kembali ke PanelUserOrder (daftar reservasi aktif).
     */
    private void btnKembaliActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA); // Kembali ke daftar pesanan aktif
        } else {
            System.err.println("MainAppFrame is null in PanelOrderDetail (btnKembali)");
        }
    }
}
