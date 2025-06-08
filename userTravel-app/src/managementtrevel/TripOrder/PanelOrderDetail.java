package managementtrevel.TripOrder;

import Asset.AppTheme;
import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;
import model.ReservasiModel; // Menggunakan ReservasiModel yang telah disesuaikan
import db.dao.KotaDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.util.List; // Import List for penumpangNames

public class PanelOrderDetail extends JPanel {

    private MainAppFrame mainAppFrame;
    private PaketPerjalananModel displayedPaket;
    private ReservasiModel displayedReservasi;

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
    private JLabel lblNamaPaket;
    private JLabel lblKotaTujuan;
    private JLabel lblTanggalPerjalanan;
    private JLabel lblHargaPerOrang;
    private JLabel lblTotalHargaFinal;


    /**
     * Konstruktor untuk PanelOrderDetail.
     * Menerima MainAppFrame dan data reservasi/paket untuk ditampilkan.
     * @param mainAppFrame Referensi ke MainAppFrame.
     * @param reservasi Model Reservasi yang akan ditampilkan detailnya.
     * @param paket Model PaketPerjalanan yang terkait dengan reservasi.
     */
    public PanelOrderDetail(MainAppFrame mainAppFrame, ReservasiModel reservasi, PaketPerjalananModel paket) {
        this.mainAppFrame = mainAppFrame;
        this.displayedReservasi = reservasi;
        this.displayedPaket = paket;

        initializeUI();
        applyAppTheme();
        setupActionListeners();
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

        // Dummy label untuk balancing visual
        JLabel dummyEast = new JLabel();
        dummyEast.setPreferredSize(btnKembali.getPreferredSize());
        headerPanel.add(dummyEast, BorderLayout.EAST);

        // 2. Main Content Area (Split Pane)
        // Panel Kiri: Data Detail (Informasi Kontak & Penumpang)
        panelDataDetail = new JPanel();
        panelDataDetail.setLayout(new BoxLayout(panelDataDetail, BoxLayout.Y_AXIS));
        panelDataDetail.setOpaque(false);
        panelDataDetail.setBorder(new EmptyBorder(0, 0, 0, 10)); // Padding kanan untuk pemisah

        // Section Informasi Kontak
        JPanel panelKontak = createSectionPanel("Informasi Kontak Pemesan");
        panelKontak.setLayout(new BoxLayout(panelKontak, BoxLayout.Y_AXIS)); // Override ke BoxLayout
        lblNamaKontak = new JLabel("Nama Kontak: -");
        lblEmailKontak = new JLabel("Email Kontak: -");
        lblTeleponKontak = new JLabel("No. Telepon Kontak: -");
        addLabelToPanel(panelKontak, lblNamaKontak);
        addLabelToPanel(panelKontak, lblEmailKontak);
        addLabelToPanel(panelKontak, lblTeleponKontak);
        panelDataDetail.add(panelKontak);
        panelDataDetail.add(Box.createRigidArea(new Dimension(0, 15)));

        // Section Detail Penumpang
        JPanel panelPenumpang = createSectionPanel("Detail Penumpang");
        panelPenumpang.setLayout(new BoxLayout(panelPenumpang, BoxLayout.Y_AXIS)); // Override ke BoxLayout
        lblJumlahPenumpang = new JLabel("Jumlah Penumpang: -");
        lblNamaPenumpang1 = new JLabel("Nama Penumpang 1: -");
        lblNamaPenumpang2 = new JLabel("Nama Penumpang 2: -");
        lblNamaPenumpang3 = new JLabel("Nama Penumpang 3: -");
        addLabelToPanel(panelPenumpang, lblJumlahPenumpang);
        panelPenumpang.add(Box.createRigidArea(new Dimension(0, 5))); // Spasi setelah jumlah
        addLabelToPanel(panelPenumpang, lblNamaPenumpang1);
        addLabelToPanel(panelPenumpang, lblNamaPenumpang2);
        addLabelToPanel(panelPenumpang, lblNamaPenumpang3);
        panelDataDetail.add(panelPenumpang);
        panelDataDetail.add(Box.createVerticalGlue()); // Mendorong ke atas

        // Panel Kanan: Ringkasan Final & Informasi Reservasi
        panelRingkasanFinal = new JPanel();
        panelRingkasanFinal.setLayout(new BoxLayout(panelRingkasanFinal, BoxLayout.Y_AXIS));
        panelRingkasanFinal.setOpaque(false);
        panelRingkasanFinal.setBorder(new EmptyBorder(0, 10, 0, 0));

        // Section Informasi Reservasi
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

        // Section Ringkasan Perjalanan
        JPanel panelRingkasan = createSectionPanel("Ringkasan Perjalanan");
        panelRingkasan.setLayout(new BoxLayout(panelRingkasan, BoxLayout.Y_AXIS));
        lblNamaPaket = new JLabel("Nama Paket: -");
        lblKotaTujuan = new JLabel("Kota Tujuan: -");
        lblTanggalPerjalanan = new JLabel("Tanggal Perjalanan: -");
        lblHargaPerOrang = new JLabel("Harga per Orang: -");
        addLabelToPanel(panelRingkasan, lblNamaPaket);
        addLabelToPanel(panelRingkasan, lblKotaTujuan);
        addLabelToPanel(panelRingkasan, lblTanggalPerjalanan);
        addLabelToPanel(panelRingkasan, lblHargaPerOrang);
        panelRingkasanFinal.add(panelRingkasan);
        panelRingkasanFinal.add(Box.createVerticalGlue());

        // Total harga di bagian bawah panelRingkasanFinal
        JPanel totalHargaPanel = new JPanel();
        totalHargaPanel.setOpaque(false);
        totalHargaPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Agar total harga rata kanan
        lblTotalHargaFinal = new JLabel("Total Harga: Rp 0");
        totalHargaPanel.add(lblTotalHargaFinal);
        panelRingkasanFinal.add(totalHargaPanel);


        // Split Pane untuk konten utama
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(panelDataDetail), new JScrollPane(panelRingkasanFinal));
        splitPane.setDividerLocation(0.5); // Bagi layar 50% kiri, 50% kanan
        splitPane.setResizeWeight(0.5);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        ((JScrollPane)splitPane.getLeftComponent()).getViewport().setOpaque(false);
        ((JScrollPane)splitPane.getLeftComponent()).setOpaque(false);
        ((JScrollPane)splitPane.getLeftComponent()).setBorder(null);
        ((JScrollPane)splitPane.getRightComponent()).getViewport().setOpaque(false);
        ((JScrollPane)splitPane.getRightComponent()).setOpaque(false);
        ((JScrollPane)splitPane.getRightComponent()).setBorder(null);


        this.add(headerPanel, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private void addLabelToPanel(JPanel panel, JLabel label) {
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5))); // Sedikit spasi antar label
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout()); // Menggunakan GridBagLayout untuk border title
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

        // Apply theme to all detail labels
        JLabel[] detailLabels = {
            lblKodeReservasi, lblStatusReservasi, lblTanggalReservasi,
            lblNamaPaket, lblKotaTujuan, lblTanggalPerjalanan,
            lblHargaPerOrang, lblJumlahPenumpang,
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
     * Memuat data detail pesanan dan paket ke dalam label-label UI.
     */
    

    /**
     * Aksi saat tombol "Kembali" ditekan.
     * Mengarahkan kembali ke PanelOrderHistory atau PanelUserOrder.
     */
    private void btnKembaliActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            // Asumsi kembali ke riwayat pesanan setelah melihat detail
            mainAppFrame.showPanel(MainAppFrame.PANEL_RIWAYAT_PESANAN);
        }
    }
}
