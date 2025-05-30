/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomTripController;
import model.CustomTrip;
import model.RincianCustomTrip;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DetailCustomTripDialog extends JDialog {

    public DetailCustomTripDialog(Frame owner, CustomTrip trip, CustomTripController controller) {
        super(owner, "Detail Custom Trip: " + trip.getNamaTrip(), true);
        setSize(500, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel Info Atas
        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        infoPanel.setBorder(new TitledBorder("Informasi Utama"));
        infoPanel.add(new JLabel("Nama Trip:"));
        infoPanel.add(new JLabel(trip.getNamaTrip()));
        infoPanel.add(new JLabel("Pemesan:"));
        infoPanel.add(new JLabel(trip.getNamaPemesan()));
        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(new JLabel(trip.getStatus()));
        add(infoPanel, BorderLayout.NORTH);

        // Tabel Rincian Destinasi
        JTable tabelRincian = new JTable(new DefaultTableModel(new String[]{"No.", "Nama Destinasi", "Tanggal Kunjungan"}, 0));
        DefaultTableModel modelRincian = (DefaultTableModel) tabelRincian.getModel();
        
        List<RincianCustomTrip> daftarRincian = controller.getRincianByCustomTripId(trip.getId());
        int no = 1;
        for (RincianCustomTrip rincian : daftarRincian) {
            modelRincian.addRow(new Object[]{
                no++,
                rincian.getNamaDestinasi(),
                rincian.getTanggalKunjungan() != null ? rincian.getTanggalKunjungan().toString() : "N/A"
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(tabelRincian);
        scrollPane.setBorder(new TitledBorder("Daftar Destinasi yang Dipilih"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel Catatan dan Tombol
        JPanel panelBawah = new JPanel(new BorderLayout(10,10));
        JTextArea areaCatatan = new JTextArea(trip.getCatatanUser());
        areaCatatan.setEditable(false);
        areaCatatan.setLineWrap(true);
        areaCatatan.setWrapStyleWord(true);
        JScrollPane scrollCatatan = new JScrollPane(areaCatatan);
        scrollCatatan.setBorder(new TitledBorder("Catatan dari Pengguna"));
        panelBawah.add(scrollCatatan, BorderLayout.CENTER);
        
        JButton btnTutup = new JButton("Tutup");
        btnTutup.addActionListener(e -> dispose());
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombol.add(btnTutup);
        panelBawah.add(panelTombol, BorderLayout.SOUTH);
        
        add(panelBawah, BorderLayout.SOUTH);
    }
}
