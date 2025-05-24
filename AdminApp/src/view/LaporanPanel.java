/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.LaporanController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LaporanPanel extends JPanel {

    JTable laporanTable;
    JLabel totalPemesananLabel = new JLabel();
    JLabel totalPendapatanLabel = new JLabel();

    public LaporanPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Laporan Pemesanan dan Pendapatan", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Table laporan bulanan
        DefaultTableModel model = LaporanController.getLaporanPemesananBulanan();
        laporanTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(laporanTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel info ringkasan
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        totalPemesananLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalPendapatanLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        summaryPanel.setBorder(BorderFactory.createTitledBorder("Ringkasan"));
        summaryPanel.add(totalPemesananLabel);
        summaryPanel.add(totalPendapatanLabel);
        add(summaryPanel, BorderLayout.SOUTH);

        loadSummary();
    }

    private void loadSummary() {
        int totalPemesanan = LaporanController.getTotalPemesanan();
        double totalPendapatan = LaporanController.getTotalPendapatan();

        totalPemesananLabel.setText("Total Reservasi: " + totalPemesanan);
        totalPendapatanLabel.setText("Total Pendapatan: Rp " + totalPendapatan);
    }
}
