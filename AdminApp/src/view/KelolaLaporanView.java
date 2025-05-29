/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PerjalananController;
import controller.ReservasiController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KelolaLaporanView extends JPanel {
    private ReservasiController reservasiController;
    private PerjalananController perjalananController;
    private JTabbedPane tabbedPane;

    public KelolaLaporanView() {
        this.reservasiController = new ReservasiController();
        this.perjalananController = new PerjalananController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JLabel judul = new JLabel("Manajemen Laporan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        add(judul, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);

        // Tambahkan Tab Laporan
        tabbedPane.addTab("Penjualan per Paket", buatPanelLaporanPenjualanPaket());
        tabbedPane.addTab("Penjualan per Jenis Trip", buatPanelLaporanPenjualanJenis());
        tabbedPane.addTab("Destinasi Populer", buatPanelLaporanDestinasi());
        
        add(tabbedPane, BorderLayout.CENTER);

        // Panel Tombol (misal untuk ekspor)
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.setOpaque(false);
        JButton btnEkspor = new JButton("Ekspor Laporan (Contoh)");
        panelTombol.add(btnEkspor);
        add(panelTombol, BorderLayout.SOUTH);

        btnEkspor.addActionListener(e -> {
            // Logika ekspor sederhana (misal: print ke console)
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                JPanel selectedPanel = (JPanel) tabbedPane.getSelectedComponent();
                JScrollPane scrollPane = (JScrollPane) selectedPanel.getComponent(0); // Asumsi JScrollPane adalah komponen pertama
                JTable tabelLaporan = (JTable) scrollPane.getViewport().getView();
                eksporDataTabelKeKonsol(tabelLaporan, tabbedPane.getTitleAt(selectedIndex));
            }
        });
    }

    private JPanel buatPanelLaporanPenjualanPaket() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Nama Paket", "Jumlah Reservasi", "Total Pendapatan (Lunas)"}
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabel = new JTable(model);
        
        List<Object[]> dataLaporan = reservasiController.getLaporanPenjualanPerPaket();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        for (Object[] baris : dataLaporan) {
            model.addRow(new Object[]{
                baris[0], // Nama Paket
                baris[1], // Jumlah Reservasi
                formatMataUang.format(baris[2]) // Total Pendapatan
            });
        }
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatPanelLaporanPenjualanJenis() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Jenis Trip", "Jumlah Reservasi", "Total Pendapatan (Lunas)"}
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabel = new JTable(model);

        List<Object[]> dataLaporan = reservasiController.getLaporanPenjualanPerJenisTrip();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        for (Object[] baris : dataLaporan) {
            model.addRow(new Object[]{
                baris[0], // Jenis Trip
                baris[1], // Jumlah Reservasi
                formatMataUang.format(baris[2]) // Total Pendapatan
            });
        }
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel buatPanelLaporanDestinasi() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{},
            new String[]{"Nama Destinasi", "Jumlah Kunjungan"}
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabel = new JTable(model);
        
        List<Object[]> dataLaporan = perjalananController.getLaporanDestinasiPopuler();
        for (Object[] baris : dataLaporan) {
            model.addRow(baris);
        }
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

    private void eksporDataTabelKeKonsol(JTable tabel, String judulLaporan) {
        System.out.println("\n--- Laporan: " + judulLaporan + " ---");
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        // Cetak Header
        for (int i = 0; i < model.getColumnCount(); i++) {
            System.out.print(model.getColumnName(i) + "\t|\t");
        }
        System.out.println();
        // Cetak Data
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                System.out.print(model.getValueAt(i, j) + "\t|\t");
            }
            System.out.println();
        }
        System.out.println("--- Akhir Laporan ---");
        JOptionPane.showMessageDialog(this, "Data laporan '" + judulLaporan + "' telah dicetak ke konsol IDE.", "Info Ekspor", JOptionPane.INFORMATION_MESSAGE);
    }
}
