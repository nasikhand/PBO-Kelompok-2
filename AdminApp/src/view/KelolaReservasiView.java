/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ReservasiController;
import model.Reservasi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KelolaReservasiView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private ReservasiController controller;
    private List<Reservasi> daftarReservasiCache; // Cache untuk data reservasi

    public KelolaReservasiView() {
        this.controller = new ReservasiController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JLabel judul = new JLabel("Manajemen Reservasi");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        add(judul, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Kode Reservasi", "Nama Trip", "Jenis", "Tgl Reservasi", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel.setModel(modelTabel);
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        tabel.getColumnModel().getColumn(0).setWidth(0);

        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.setOpaque(false);
        JButton btnKonfirmasi = new JButton("Konfirmasi Pembayaran");
        JButton btnBatalkan = new JButton("Batalkan Reservasi");
        JButton btnLihatDetail = new JButton("Lihat Detail Penumpang");

        panelTombol.add(btnKonfirmasi);
        panelTombol.add(btnBatalkan);
        panelTombol.add(btnLihatDetail);
        add(panelTombol, BorderLayout.SOUTH);

        muatData();

        // Action Listeners
        btnKonfirmasi.addActionListener(e -> ubahStatus("dibayar"));
        btnBatalkan.addActionListener(e -> ubahStatus("dibatalkan"));
        btnLihatDetail.addActionListener(e -> lihatDetail()); // <-- Aktifkan tombol ini
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
}
