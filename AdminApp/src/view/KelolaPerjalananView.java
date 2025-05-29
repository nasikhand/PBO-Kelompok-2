/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PerjalananController;
import model.PaketPerjalanan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KelolaPerjalananView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private PerjalananController controller;

    public KelolaPerjalananView() {
        this.controller = new PerjalananController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JLabel judul = new JLabel("Manajemen Paket Perjalanan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        add(judul, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nama Paket", "Kota Tujuan", "Tgl Mulai", "Tgl Akhir", "Harga", "Kuota", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabel.setModel(modelTabel);
        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.setOpaque(false);
        JButton btnTambah = new JButton("Tambah Paket Baru");
        JButton btnUbah = new JButton("Ubah Paket");
        JButton btnHapus = new JButton("Hapus Paket");
        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        add(panelTombol, BorderLayout.SOUTH);

        muatData();

        btnTambah.addActionListener(e -> bukaDialogForm(null));
    }

    private void muatData() {
        modelTabel.setRowCount(0);
        List<PaketPerjalanan> daftarPaket = controller.getAllPaketPerjalanan();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        for (PaketPerjalanan paket : daftarPaket) {
            modelTabel.addRow(new Object[]{
                paket.getId(),
                paket.getNamaPaket(),
                paket.getNamaKota(),
                paket.getTanggalMulai(),
                paket.getTanggalAkhir(),
                formatMataUang.format(paket.getHarga()),
                paket.getKuota(),
                paket.getStatus()
            });
        }
    }

    private void bukaDialogForm(PaketPerjalanan paket) {
        FormPerjalananDialog dialog = new FormPerjalananDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), paket
        );
        dialog.setVisible(true);
        muatData();
    }
}
