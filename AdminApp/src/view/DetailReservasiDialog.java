/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.ReservasiController;
import model.Penumpang;
import model.Reservasi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DetailReservasiDialog extends JDialog {
    private Reservasi reservasi;
    private ReservasiController controller;
    private DefaultTableModel modelTabelPenumpang;

    public DetailReservasiDialog(Frame owner, Reservasi reservasi) {
        super(owner, "Detail Reservasi - " + reservasi.getKodeReservasi(), true);
        this.reservasi = reservasi;
        this.controller = new ReservasiController();
        
        setSize(800, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel utama dengan padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Panel Info Reservasi
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        infoPanel.setBorder(new TitledBorder("Informasi Reservasi"));
        infoPanel.add(new JLabel("Kode Reservasi:"));
        infoPanel.add(new JLabel(reservasi.getKodeReservasi()));
        infoPanel.add(new JLabel("Nama Trip/Paket:"));
        infoPanel.add(new JLabel(reservasi.getNamaTrip()));
        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Tabel Penumpang
        JScrollPane scrollPane = new JScrollPane();
        JTable tabelPenumpang = new JTable();
        modelTabelPenumpang = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Nama Penumpang", "Jenis Kelamin", "Tgl Lahir", "No. Telepon", "Email"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelPenumpang.setModel(modelTabelPenumpang);
        scrollPane.setViewportView(tabelPenumpang);
        scrollPane.setBorder(new TitledBorder("Daftar Penumpang"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Tombol tutup
        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnTutup);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        muatDataPenumpang();
    }

    private void muatDataPenumpang() {
        modelTabelPenumpang.setRowCount(0);
        List<Penumpang> daftarPenumpang = controller.getPenumpangByReservasiId(reservasi.getId());
        for (Penumpang p : daftarPenumpang) {
            modelTabelPenumpang.addRow(new Object[]{
                p.getNamaPenumpang(),
                p.getJenisKelamin(),
                p.getTanggalLahir(),
                p.getNomorTelepon(),
                p.getEmail()
            });
        }
    }
}