/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import config.PdfGenerator;
import controller.PerjalananController;
import controller.ReservasiController;
import model.PaketPerjalanan;
import model.Reservasi;
import model.RincianPaketPerjalanan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class KelolaReservasiView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private ReservasiController controller;
    private List<Reservasi> daftarReservasiCache;

    private JTextField txtPencarianKode;
    private JComboBox<String> cmbFilterStatus;
    private JButton btnSebelumnya, btnBerikutnya, btnFilter;
    private JLabel lblInfoHalaman;
    private int halamanSaatIni = 1;
    private final int DATA_PER_HALAMAN = 10;
    private int totalHalaman = 1;
    private int totalData = 0;
    
    // <<< KOMPONEN BARU UNTUK EDIT STATUS >>>
    private JComboBox<String> cmbEditStatus;
    private JButton btnUbahStatus;

    public KelolaReservasiView() {
        this.controller = new ReservasiController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));
        panelAtas.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Reservasi");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        panelAtas.add(judul, BorderLayout.NORTH);

        JPanel panelFilterPencarian = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFilterPencarian.setOpaque(false);
        panelFilterPencarian.add(new JLabel("Filter Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", "pending", "dipesan", "dibayar", "selesai", "dibatalkan"});
        panelFilterPencarian.add(cmbFilterStatus);
        panelFilterPencarian.add(new JLabel("   Cari Kode:"));
        txtPencarianKode = new JTextField(15);
        panelFilterPencarian.add(txtPencarianKode);
        btnFilter = new JButton("Terapkan");
        panelFilterPencarian.add(btnFilter);
        panelAtas.add(panelFilterPencarian, BorderLayout.CENTER);
        add(panelAtas, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Kode Reservasi", "Nama Trip", "Jenis", "Tgl Reservasi", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabel.setModel(modelTabel);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTombolAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolAksi.setOpaque(false);
        JButton btnLihatDetail = new JButton("Lihat Detail Penumpang");
        JButton btnCetakItinerary = new JButton("Cetak Itinerary (PDF)");
        panelTombolAksi.add(btnLihatDetail);
        panelTombolAksi.add(btnCetakItinerary);

        // <<< PANEL BARU UNTUK EDIT STATUS >>>
        JPanel panelEditStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelEditStatus.setOpaque(false);
        panelEditStatus.setBorder(BorderFactory.createTitledBorder("Ubah Status Pilihan"));
        cmbEditStatus = new JComboBox<>(new String[]{"pending", "dipesan", "dibayar", "selesai", "dibatalkan"});
        btnUbahStatus = new JButton("Simpan Status");
        cmbEditStatus.setEnabled(false); // Awalnya non-aktif
        btnUbahStatus.setEnabled(false); // Awalnya non-aktif
        panelEditStatus.add(new JLabel("Set Status ke:"));
        panelEditStatus.add(cmbEditStatus);
        panelEditStatus.add(btnUbahStatus);

        JPanel panelPaginasi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPaginasi.setOpaque(false);
        btnSebelumnya = new JButton("« Seb");
        lblInfoHalaman = new JLabel("Hal 1 dari 1");
        lblInfoHalaman.setForeground(Color.WHITE);
        btnBerikutnya = new JButton("Ber »");
        panelPaginasi.add(btnSebelumnya);
        panelPaginasi.add(lblInfoHalaman);
        panelPaginasi.add(btnBerikutnya);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setOpaque(false);
        panelBawah.add(panelTombolAksi, BorderLayout.WEST);
        
        // Panel untuk menggabungkan edit status dan paginasi
        JPanel panelKananBawah = new JPanel(new BorderLayout());
        panelKananBawah.setOpaque(false);
        panelKananBawah.add(panelEditStatus, BorderLayout.NORTH);
        panelKananBawah.add(panelPaginasi, BorderLayout.SOUTH);
        panelBawah.add(panelKananBawah, BorderLayout.EAST);
        
        add(panelBawah, BorderLayout.SOUTH);

        muatDataDenganPaginasi();

        // Action Listeners
        btnFilter.addActionListener(e -> {
            halamanSaatIni = 1;
            muatDataDenganPaginasi();
        });
        btnSebelumnya.addActionListener(e -> { if (halamanSaatIni > 1) { halamanSaatIni--; muatDataDenganPaginasi(); } });
        btnBerikutnya.addActionListener(e -> { if (halamanSaatIni < totalHalaman) { halamanSaatIni++; muatDataDenganPaginasi(); } });
        btnLihatDetail.addActionListener(e -> lihatDetail());
        btnCetakItinerary.addActionListener(e -> cetakItineraryTerpilih());
        btnUbahStatus.addActionListener(e -> ubahStatusTerpilih());
        
        // Listener untuk tabel, untuk mengaktifkan form edit status
        tabel.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean isSelected = tabel.getSelectedRow() != -1;
                cmbEditStatus.setEnabled(isSelected);
                btnUbahStatus.setEnabled(isSelected);
                if (isSelected) {
                    // Set combobox ke status baris yang dipilih
                    String statusSaatIni = (String) tabel.getValueAt(tabel.getSelectedRow(), 5);
                    cmbEditStatus.setSelectedItem(statusSaatIni);
                }
            }
        });
    }
    
    public void terapkanFilterStatusEksternal(String status) {
        cmbFilterStatus.setSelectedItem(status);
        halamanSaatIni = 1;
        muatDataDenganPaginasi();
    }

    private void muatDataDenganPaginasi() {
        // ... (kode metode ini tidak berubah) ...
        String filterKode = txtPencarianKode.getText().trim();
        String filterStatus = (String) cmbFilterStatus.getSelectedItem();
        if ("Semua".equalsIgnoreCase(filterStatus)) {
            filterStatus = ""; 
        }

        totalData = controller.getTotalReservasiCount(filterKode, filterStatus);
        totalHalaman = (int) Math.ceil((double) totalData / DATA_PER_HALAMAN);
        if (totalHalaman == 0) totalHalaman = 1;
        if (halamanSaatIni > totalHalaman) halamanSaatIni = totalHalaman;
        if (halamanSaatIni < 1) halamanSaatIni = 1;

        this.daftarReservasiCache = controller.getReservasiWithPagination(halamanSaatIni, DATA_PER_HALAMAN, filterKode, filterStatus);
        
        modelTabel.setRowCount(0);
        for (Reservasi reservasi : daftarReservasiCache) {
            modelTabel.addRow(new Object[]{
                reservasi.getId(),
                reservasi.getKodeReservasi(),
                reservasi.getNamaTrip(),
                reservasi.getTripType(),
                reservasi.getTanggalReservasi(),
                reservasi.getStatus()
            });
        }
        
        lblInfoHalaman.setText("Hal " + halamanSaatIni + " dari " + totalHalaman + " (" + totalData + " data)");
        btnSebelumnya.setEnabled(halamanSaatIni > 1);
        btnBerikutnya.setEnabled(halamanSaatIni < totalHalaman);
    }
    
    // <<< METODE BARU UNTUK LOGIKA UBAH STATUS >>>
    private void ubahStatusTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data reservasi terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReservasi = (int) modelTabel.getValueAt(barisTerpilih, 0);
        String statusSaatIni = (String) modelTabel.getValueAt(barisTerpilih, 5);
        String statusTarget = (String) cmbEditStatus.getSelectedItem();

        if (statusSaatIni.equalsIgnoreCase(statusTarget)) {
             JOptionPane.showMessageDialog(this, "Reservasi sudah dalam status '" + statusTarget + "'.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
             return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Ubah status reservasi ini menjadi '" + statusTarget + "'?", "Konfirmasi Perubahan Status",
            JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (controller.updateStatusReservasi(idReservasi, statusTarget)) {
                JOptionPane.showMessageDialog(this, "Status reservasi berhasil diperbarui.");
                muatDataDenganPaginasi(); // Muat ulang data untuk menampilkan perubahan
            } else {
                 JOptionPane.showMessageDialog(this, "Gagal memperbarui status.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void lihatDetail() {
        // ... (kode metode ini tidak berubah) ...
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih reservasi untuk melihat detailnya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Reservasi reservasiTerpilih = daftarReservasiCache.get(barisTerpilih);
        DetailReservasiDialog dialog = new DetailReservasiDialog((Frame) SwingUtilities.getWindowAncestor(this), reservasiTerpilih);
        dialog.setVisible(true);
    }
    
    private void cetakItineraryTerpilih() {
        // ... (kode metode ini tidak berubah) ...
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih reservasi yang ingin dicetak.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Reservasi reservasi = daftarReservasiCache.get(barisTerpilih);
        if (!"paket_perjalanan".equalsIgnoreCase(reservasi.getTripType())) {
            JOptionPane.showMessageDialog(this, "Fitur ini baru tersedia untuk 'Paket Perjalanan'.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        PerjalananController perjalananController = new PerjalananController();
        PaketPerjalanan paket = perjalananController.getPaketById(reservasi.getTripId());
        List<RincianPaketPerjalanan> daftarRincian = perjalananController.getRincianByPaketId(reservasi.getTripId());
        if (paket == null || daftarRincian.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data lengkap atau rincian destinasi untuk paket ini tidak ditemukan.", "Data Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Itinerary PDF");
        fileChooser.setSelectedFile(new File("Itinerary-" + reservasi.getKodeReservasi() + ".pdf"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            PdfGenerator.createPaketItinerary(reservasi, paket, daftarRincian, fileToSave.getAbsolutePath());
        }
    }
}