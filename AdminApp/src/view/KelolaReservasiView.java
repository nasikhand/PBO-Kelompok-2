/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ReservasiController;
import model.Reservasi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent; 
import javax.swing.event.DocumentListener; 
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList; // Diperlukan untuk RowFilter
import java.util.List;

public class KelolaReservasiView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private ReservasiController controller;
    private List<Reservasi> daftarReservasiCache;
    private JTextField txtPencarian; // <-- Kolom pencarian
    private TableRowSorter<DefaultTableModel> sorter;
        // Komponen Paginasi dan Pencarian
    private JTextField txtPencarianKode;
    private JComboBox<String> cmbFilterStatus;
    private JButton btnSebelumnya, btnBerikutnya, btnFilter;
    private JLabel lblInfoHalaman;
    private int halamanSaatIni = 1;
    private final int DATA_PER_HALAMAN = 10;
    private int totalHalaman = 1;
    private int totalData = 0;

    public KelolaReservasiView() {
        this.controller = new ReservasiController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        // Panel Atas: Judul, Filter, dan Kolom Pencarian
        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));
        panelAtas.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Reservasi");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        panelAtas.add(judul, BorderLayout.NORTH);

        JPanel panelFilterPencarian = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFilterPencarian.setOpaque(false);
        
        panelFilterPencarian.add(new JLabel("Filter Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", "pending", "dipesan", "dibayar", "selesai", "dibatalkan"}); // Tambah opsi "dibatalkan" jika ada
        panelFilterPencarian.add(cmbFilterStatus);

        panelFilterPencarian.add(new JLabel("   Cari Kode:"));
        txtPencarianKode = new JTextField(15);
        txtPencarianKode.putClientProperty("JTextField.placeholderText", "Kode Reservasi...");
        panelFilterPencarian.add(txtPencarianKode);
        
        btnFilter = new JButton("Terapkan"); // Tombol untuk menerapkan filter & pencarian
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
        // Sorter client-side tidak lagi digunakan jika paginasi server-side
        // sorter = new TableRowSorter<>(modelTabel);
        // tabel.setRowSorter(sorter);
        
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        tabel.getColumnModel().getColumn(0).setWidth(0);

        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol Aksi
        JPanel panelTombolAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolAksi.setOpaque(false);
        JButton btnKonfirmasi = new JButton("Konfirmasi Pembayaran");
        JButton btnBatalkan = new JButton("Batalkan Reservasi");
        JButton btnLihatDetail = new JButton("Lihat Detail Penumpang");
        panelTombolAksi.add(btnKonfirmasi);
        panelTombolAksi.add(btnBatalkan);
        panelTombolAksi.add(btnLihatDetail);

        // Panel Paginasi
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
        panelBawah.add(panelPaginasi, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);

        muatDataDenganPaginasi();

        // Action Listeners
        btnFilter.addActionListener(e -> {
            halamanSaatIni = 1; // Selalu reset ke halaman 1 saat filter baru diterapkan
            muatDataDenganPaginasi();
        });
        // Untuk pencarian real-time, Anda bisa menambahkan DocumentListener ke txtPencarianKode
        // dan memanggil muatDataDenganPaginasi() di sana juga.
        // Untuk saat ini, pencarian hanya aktif saat tombol "Terapkan" diklik.

        btnSebelumnya.addActionListener(e -> {
            if (halamanSaatIni > 1) {
                halamanSaatIni--;
                muatDataDenganPaginasi();
            }
        });
        btnBerikutnya.addActionListener(e -> {
            if (halamanSaatIni < totalHalaman) {
                halamanSaatIni++;
                muatDataDenganPaginasi();
            }
        });
        
        btnKonfirmasi.addActionListener(e -> ubahStatus("dibayar"));
        btnBatalkan.addActionListener(e -> ubahStatus("dibatalkan"));
        btnLihatDetail.addActionListener(e -> lihatDetail());
    }
    
    private void muatDataDenganPaginasi() {
        String filterKode = txtPencarianKode.getText().trim();
        String filterStatus = (String) cmbFilterStatus.getSelectedItem();
        if ("Semua".equalsIgnoreCase(filterStatus)) {
            filterStatus = ""; // Kirim string kosong jika "Semua" untuk tidak memfilter status
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



    private void muatData() {
        modelTabel.setRowCount(0);
        this.daftarReservasiCache = controller.getAllReservasi();

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
    }

    private void ubahStatus(String status) {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data reservasi terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReservasi = (int) modelTabel.getValueAt(barisTerpilih, 0);
        String kodeReservasi = (String) modelTabel.getValueAt(barisTerpilih, 1);
        String statusSaatIni = (String) modelTabel.getValueAt(barisTerpilih, 5);

        if (statusSaatIni.equalsIgnoreCase(status)) {
             JOptionPane.showMessageDialog(this, "Reservasi sudah dalam status '" + status + "'.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
             return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Ubah status untuk reservasi " + kodeReservasi + " menjadi '" + status + "'?",
            "Konfirmasi Perubahan Status", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.updateStatusReservasi(idReservasi, status);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Status reservasi berhasil diperbarui.");
                muatData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui status.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // vvv METODE BARU UNTUK MELIHAT DETAIL vvv
    private void lihatDetail() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih reservasi untuk melihat detailnya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Dapatkan objek Reservasi dari cache berdasarkan baris yang dipilih
        Reservasi reservasiTerpilih = daftarReservasiCache.get(barisTerpilih);

        // Tampilkan dialog detail
        DetailReservasiDialog dialog = new DetailReservasiDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), reservasiTerpilih
        );
        dialog.setVisible(true);
    }
    
    private void cariData() {
        String teks = txtPencarian.getText();
        if (teks.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Filter berdasarkan Kode (1), Nama Trip (2), Jenis (3), Status (5)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + teks, 1, 2, 3, 5));
        }
    }
}
