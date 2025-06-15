package managementtrevel.TripOrder;

import Asset.AppTheme;
import db.Koneksi;
import db.dao.PembayaranDAO;
import db.dao.PenumpangDAO;
import db.dao.ReservasiDAO;
import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;
import model.PenumpangModel;
import model.ReservasiModel;
import model.Session;
import model.CustomTripModel;
import model.UserModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PanelOrderDetail extends JPanel {

    private final MainAppFrame mainAppFrame;
    private ReservasiModel displayedReservasi;

    // DAO instances
    private final PenumpangDAO penumpangDAO;
    private final PembayaranDAO pembayaranDAO;
    private final ReservasiDAO reservasiDAO;

    // UI Components
    private JLabel lblTitle;
    private JButton btnKembali, btnLanjutkanBooking, btnBayar, btnBatalkanPesanan, btnSelesaikan;

    // Detail Labels
    private JLabel lblNamaKontak, lblEmailKontak, lblTeleponKontak;
    private JTextPane passengerDetailsPane;
    private JLabel lblKodeReservasi, lblStatusReservasi, lblTanggalReservasi;
    private JLabel lblNamaTrip, lblKotaTujuan, lblTanggalPerjalanan, lblHargaPerOrang, lblRating;
    private JLabel lblTotalHargaFinal;
    private JTextArea passengerDetailsArea;

    // Formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMMM uuuu");
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

        //<editor-fold defaultstate="collapsed" desc="Header Panel">
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        btnKembali = new JButton("< Kembali");
        headerPanel.add(btnKembali, BorderLayout.WEST);
        lblTitle = new JLabel("Detail Pesanan Anda");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        JLabel dummyEast = new JLabel();
        if (btnKembali != null) {
            dummyEast.setPreferredSize(btnKembali.getPreferredSize());
        }
        headerPanel.add(dummyEast, BorderLayout.EAST);
        this.add(headerPanel, BorderLayout.NORTH);
        //</editor-fold>

        JPanel mainContentArea = new JPanel(new GridBagLayout());
        mainContentArea.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 0.5;

        // Initialize JLabels
        lblNamaKontak = new JLabel(); lblEmailKontak = new JLabel(); lblTeleponKontak = new JLabel();
        lblKodeReservasi = new JLabel(); lblStatusReservasi = new JLabel(); lblTanggalReservasi = new JLabel();
        lblNamaTrip = new JLabel(); lblKotaTujuan = new JLabel(); lblTanggalPerjalanan = new JLabel();
        lblHargaPerOrang = new JLabel(); lblRating = new JLabel();
        lblTotalHargaFinal = new JLabel();

        // Row 0: Contact & Reservation Info
        JPanel kontakPanel = createSectionPanel("Informasi Kontak Pemesan");
        kontakPanel.setLayout(new BoxLayout(kontakPanel, BoxLayout.Y_AXIS));
        addLabelToBoxLayoutPanel(kontakPanel, lblNamaKontak);
        addLabelToBoxLayoutPanel(kontakPanel, lblEmailKontak);
        addLabelToBoxLayoutPanel(kontakPanel, lblTeleponKontak);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weighty = 0;
        mainContentArea.add(kontakPanel, gbc);

        JPanel reservasiInfoPanel = createSectionPanel("Informasi Reservasi");
        reservasiInfoPanel.setLayout(new BoxLayout(reservasiInfoPanel, BoxLayout.Y_AXIS));
        addLabelToBoxLayoutPanel(reservasiInfoPanel, lblKodeReservasi);
        addLabelToBoxLayoutPanel(reservasiInfoPanel, lblStatusReservasi);
        addLabelToBoxLayoutPanel(reservasiInfoPanel, lblTanggalReservasi);
        gbc.gridx = 1; gbc.gridy = 0;
        mainContentArea.add(reservasiInfoPanel, gbc);

        // Row 1: Passenger Details & Trip Summary
        gbc.weighty = 1.0; // Allow this row to stretch vertically

        // --- FIXED: Call the helper method to create the passenger panel ---
        JPanel penumpangPanel = createPassengerDetailPanel();
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
        
        //<editor-fold defaultstate="collapsed" desc="Total & Action Buttons">
        JPanel totalHargaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalHargaPanel.setOpaque(false);
        totalHargaPanel.add(lblTotalHargaFinal);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        mainContentArea.add(totalHargaPanel, gbc);

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionButtonPanel.setOpaque(false);
        btnLanjutkanBooking = new JButton("Lanjutkan Booking");
        btnBayar = new JButton("Bayar Sekarang");
        btnBatalkanPesanan = new JButton("Batalkan Pesanan");
        btnSelesaikan = new JButton("Selesaikan Pesanan");
        actionButtonPanel.add(btnLanjutkanBooking);
        actionButtonPanel.add(btnBayar);
        actionButtonPanel.add(btnBatalkanPesanan);
        actionButtonPanel.add(btnSelesaikan);
        //</editor-fold>

        this.add(mainContentArea, BorderLayout.CENTER); 
        this.add(actionButtonPanel, BorderLayout.SOUTH);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        lblTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        
        AppTheme.styleSecondaryButton(btnKembali, "< Kembali");
        AppTheme.stylePrimaryButton(btnLanjutkanBooking, "Lanjutkan Booking");
        AppTheme.stylePrimaryButton(btnBayar, "Bayar Sekarang");
        AppTheme.styleSecondaryButton(btnBatalkanPesanan, "Batalkan Pesanan");
        AppTheme.stylePrimaryButton(btnSelesaikan, "Selesaikan Pesanan");
        
        // --- STYLED: Terapkan gaya ke semua label ---
        styleDetailLabel(lblNamaKontak);
        styleDetailLabel(lblEmailKontak);
        styleDetailLabel(lblTeleponKontak);
        styleDetailLabel(lblKodeReservasi);
        styleDetailLabel(lblStatusReservasi);
        styleDetailLabel(lblTanggalReservasi);
        styleDetailLabel(lblKotaTujuan);
        styleDetailLabel(lblTanggalPerjalanan);
        styleDetailLabel(lblHargaPerOrang);
        styleDetailLabel(lblRating);
        
        lblNamaTrip.setFont(AppTheme.FONT_SUBTITLE);
        lblNamaTrip.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        
        lblTotalHargaFinal.setFont(AppTheme.FONT_TITLE_MEDIUM_BOLD);
        lblTotalHargaFinal.setForeground(AppTheme.ACCENT_ORANGE);
    }
    
    private void setupActionListeners() {
        btnKembali.addActionListener(this::btnKembaliActionPerformed);
        btnLanjutkanBooking.addActionListener(this::btnLanjutkanBookingActionPerformed);
        btnBayar.addActionListener(this::btnBayarActionPerformed);
        btnBatalkanPesanan.addActionListener(this::btnBatalkanPesananActionPerformed);
        btnSelesaikan.addActionListener(this::btnSelesaikanActionPerformed);
    }
    
    private void loadOrderDetailData() {
        if (displayedReservasi == null) { return; }

        UserModel currentUser = Session.currentUser;
        if (currentUser != null) {
            lblNamaKontak.setText("<html><b>Nama Kontak:</b> " + currentUser.getNamaLengkap() + "</html>");
            lblEmailKontak.setText("<html><b>Email:</b> " + currentUser.getEmail() + "</html>");
            lblTeleponKontak.setText("<html><b>No. Telepon:</b> " + currentUser.getNomorTelepon() + "</html>");
        }
        
        lblKodeReservasi.setText("<html><b>Kode Reservasi:</b> " + displayedReservasi.getKodeReservasi() + "</html>");
        lblStatusReservasi.setText("<html><b>Status:</b> " + displayedReservasi.getStatus().toUpperCase() + "</html>");
        lblTanggalReservasi.setText("<html><b>Tanggal Pesan:</b> " + displayedReservasi.getTanggalReservasi().format(DATE_FORMATTER) + "</html>");

        List<PenumpangModel> penumpangList = penumpangDAO.getDetailPenumpangByReservasiId(displayedReservasi.getId());
        StringBuilder passengerText = new StringBuilder("<html><body style='width: 100%;'>");
        passengerText.append("<b>Jumlah Penumpang:</b> ").append(penumpangList.size()).append(" Orang<br><br>");
        
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        for (int i = 0; i < penumpangList.size(); i++) {
            PenumpangModel p = penumpangList.get(i);
            passengerText.append("<b>Peserta ").append(i + 1).append(":</b><br>");
            passengerText.append("&nbsp;&nbsp;Nama: ").append(p.getNamaPenumpang() != null ? p.getNamaPenumpang() : "-").append("<br>");
            passengerText.append("&nbsp;&nbsp;Jenis Kelamin: ").append(p.getJenisKelamin() != null ? p.getJenisKelamin() : "-").append("<br>");
            String tglLahirStr = (p.getTanggalLahir() != null) ? sqlDateFormat.format(p.getTanggalLahir()) : "-";
            passengerText.append("&nbsp;&nbsp;Tanggal Lahir: ").append(tglLahirStr).append("<br>");
            passengerText.append("&nbsp;&nbsp;Email: ").append(p.getEmail() != null ? p.getEmail() : "-").append("<br>");
            passengerText.append("&nbsp;&nbsp;No. Telepon: ").append(p.getNomorTelepon() != null ? p.getNomorTelepon() : "-").append("<br><br>");
        }
        passengerDetailsPane.setText(passengerText.toString() + "</body></html>");
        passengerDetailsPane.setCaretPosition(0);

        double totalHarga = 0.0;
        if ("paket_perjalanan".equals(displayedReservasi.getTripType()) && displayedReservasi.getPaket() != null) {
            PaketPerjalananModel paket = displayedReservasi.getPaket();
            lblNamaTrip.setText("<html><b>Nama Paket:</b> " + paket.getNamaPaket() + "</html>");
            lblKotaTujuan.setText("<html><b>Kota:</b> " + paket.getNamaKota() + "</html>");
            lblTanggalPerjalanan.setText("<html><b>Tanggal:</b> " + paket.getTanggalMulai() + " s/d " + paket.getTanggalAkhir() + "</html>");
            lblHargaPerOrang.setText("<html><b>Harga/Orang:</b> " + CURRENCY_FORMATTER.format(paket.getHarga()) + "</html>");
            lblRating.setText("<html><b>Rating:</b> " + String.format("%.1f", paket.getRating()) + "</html>");
            lblRating.setVisible(true);
            totalHarga = paket.getHarga() * penumpangList.size();
        } else if ("custom_trip".equals(displayedReservasi.getTripType()) && displayedReservasi.getCustomTrip() != null) {
            CustomTripModel custom = displayedReservasi.getCustomTrip();
            lblNamaTrip.setText("<html><b>Nama Trip:</b> " + custom.getNamaTrip() + "</html>");
            lblKotaTujuan.setText("<html><b>Kota:</b> " + custom.getNamaKota() + "</html>");
            lblTanggalPerjalanan.setText("<html><b>Tanggal:</b> " + custom.getTanggalMulai().format(DATE_FORMATTER) + " s/d " + custom.getTanggalAkhir().format(DATE_FORMATTER) + "</html>");
            lblHargaPerOrang.setVisible(false);
            lblRating.setVisible(false);
            totalHarga = custom.getTotalHarga();
        }
        
        lblTotalHargaFinal.setText(CURRENCY_FORMATTER.format(totalHarga));
        
        String status = displayedReservasi.getStatus();
        btnLanjutkanBooking.setVisible("pending".equals(status) || "draft".equals(status));
        btnBayar.setVisible("dipesan".equals(status));
        btnBatalkanPesanan.setVisible("pending".equals(status) || "draft".equals(status));
        btnSelesaikan.setVisible("dibayar".equals(status));
    }

    //<editor-fold defaultstate="collapsed" desc="UI Helper Methods">
    private JPanel createPassengerDetailPanel() {
        
        JPanel panel = createSectionPanel("Detail Penumpang");
        this.passengerDetailsPane = new JTextPane();
        passengerDetailsPane.setContentType("text/html");
        passengerDetailsPane.setEditable(false);
        passengerDetailsPane.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        passengerDetailsPane.setOpaque(false);
        passengerDetailsPane.setForeground(AppTheme.TEXT_DARK);
        
        JScrollPane scrollPane = new JScrollPane(passengerDetailsPane);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void styleDetailLabel(JLabel label) {
        if (label != null) {
            label.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            label.setForeground(AppTheme.TEXT_DARK);
        }
    }

    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 5, 5, 5));
        JLabel title = new JLabel("Total Harga Akhir");
        title.setFont(AppTheme.FONT_SUBTITLE);
        lblTotalHargaFinal.setFont(AppTheme.FONT_TITLE_MEDIUM_BOLD);
        lblTotalHargaFinal.setForeground(AppTheme.ACCENT_ORANGE);
        panel.add(title, BorderLayout.WEST);
        panel.add(lblTotalHargaFinal, BorderLayout.EAST);
        return panel;
    }

    private void addLabelToBoxLayoutPanel(JPanel panel, JLabel label) {
        // Pastikan label rata kiri di dalam panel
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Tambahkan label ke panel
        panel.add(label);
        
        // Tambahkan sedikit ruang vertikal di bawah label agar tidak terlalu rapat
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private JPanel createSectionPanel(String title, JComponent... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            null, title, TitledBorder.LEADING, TitledBorder.TOP,
            AppTheme.FONT_SUBTITLE, AppTheme.PRIMARY_BLUE_DARK
        ));
        for (JComponent comp : components) {
            comp.setAlignmentX(Component.LEFT_ALIGNMENT);
            comp.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            panel.add(comp);
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        return panel;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Action Listeners (Full Implementation)">
    private void btnKembaliActionPerformed(ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
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
                    JOptionPane.showMessageDialog(this, "Status reservasi berhasil diperbarui. Anda sekarang dapat melakukan pembayaran.", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh data
                    this.displayedReservasi = reservasiDAO.getReservasiById(this.displayedReservasi.getId());
                    loadOrderDetailData();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui status reservasi.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void btnBayarActionPerformed(ActionEvent evt) {
        if ("dipesan".equals(displayedReservasi.getStatus())) {
            String namaKontak = Session.currentUser != null ? Session.currentUser.getNamaLengkap() : "";
            String emailKontak = Session.currentUser != null ? Session.currentUser.getEmail() : "";
            String teleponKontak = Session.currentUser != null ? Session.currentUser.getNomorTelepon() : "";
            
            List<PenumpangModel> penumpangList = penumpangDAO.getDetailPenumpangByReservasiId(displayedReservasi.getId());
            List<String> namaPenumpangList = new ArrayList<>();
            for(PenumpangModel p : penumpangList){
                namaPenumpangList.add(p.getNamaPenumpang());
            }

            mainAppFrame.showPaymentPanel(displayedReservasi.getId(), namaKontak, emailKontak, teleponKontak, namaPenumpangList);
        } else {
            JOptionPane.showMessageDialog(this, "Aksi 'Bayar Sekarang' hanya tersedia untuk reservasi berstatus 'dipesan'.", "Status Tidak Valid", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnBatalkanPesananActionPerformed(ActionEvent evt) {
        if ("pending".equals(displayedReservasi.getStatus())) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Anda yakin ingin membatalkan pesanan ini secara permanen?",
                    "Konfirmasi Pembatalan",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = reservasiDAO.deleteReservasi(displayedReservasi.getId());
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Pesanan berhasil dibatalkan dan dihapus.", "Pembatalan Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal membatalkan pesanan. Silakan coba lagi.", "Pembatalan Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pesanan hanya dapat dibatalkan jika berstatus 'pending'.", "Status Tidak Valid", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void btnSelesaikanActionPerformed(ActionEvent evt) {
        if (!"dibayar".equals(displayedReservasi.getStatus())) {
            JOptionPane.showMessageDialog(this, "Aksi ini hanya bisa dilakukan untuk pesanan yang sudah dibayar.", "Aksi Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menyelesaikan pesanan ini?\nStatus akan diubah menjadi 'selesai' dan tidak dapat diubah kembali.",
                "Konfirmasi Selesaikan Pesanan",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = reservasiDAO.updateStatusReservasi(displayedReservasi.getId(), "selesai");
            if (success) {
                JOptionPane.showMessageDialog(this, "Pesanan telah ditandai sebagai 'selesai'.\nTerima kasih telah menggunakan layanan kami!", "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                // Refresh the panel to show the new status
                this.displayedReservasi = reservasiDAO.getReservasiById(this.displayedReservasi.getId());
                loadOrderDetailData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui status pesanan. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</editor-fold>
}
