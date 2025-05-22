/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PaketController;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;

public class PaketPanel extends JPanel {

    JTable paketTable;
    DefaultTableModel model;

    public PaketPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Kelola Paket Perjalanan", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Table
        model = PaketController.getAllPaket();
        paketTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(paketTable);
        add(scrollPane, BorderLayout.CENTER);

        // Form Input
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        JTextField tfNama = new JTextField();
        JTextField tfKotaId = new JTextField();
        JTextField tfTglMulai = new JTextField(); // yyyy-mm-dd
        JTextField tfTglAkhir = new JTextField();
        JTextField tfKuota = new JTextField();
        JTextField tfHarga = new JTextField();
        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"tersedia", "penuh", "selesai"});

        inputPanel.setBorder(BorderFactory.createTitledBorder("Tambah Paket"));
        inputPanel.add(new JLabel("Nama Paket:"));
        inputPanel.add(tfNama);
        inputPanel.add(new JLabel("Kota ID:"));
        inputPanel.add(tfKotaId);
        inputPanel.add(new JLabel("Tgl Mulai:"));
        inputPanel.add(tfTglMulai);
        inputPanel.add(new JLabel("Tgl Akhir:"));
        inputPanel.add(tfTglAkhir);
        inputPanel.add(new JLabel("Kuota:"));
        inputPanel.add(tfKuota);
        inputPanel.add(new JLabel("Harga:"));
        inputPanel.add(tfHarga);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(cbStatus);

        // Tombol
        JButton btnTambah = new JButton("Tambah Paket");
        btnTambah.addActionListener(e -> {
            boolean inserted = PaketController.insertPaket(
                    tfNama.getText(),
                    Integer.parseInt(tfKotaId.getText()),
                    tfTglMulai.getText(),
                    tfTglAkhir.getText(),
                    Integer.parseInt(tfKuota.getText()),
                    Double.parseDouble(tfHarga.getText()),
                    cbStatus.getSelectedItem().toString()
            );
            if (inserted) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambah!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal tambah data!");
            }
        });

        JButton btnHapus = new JButton("Hapus Paket");
        btnHapus.addActionListener(e -> {
            int selected = paketTable.getSelectedRow();
            if (selected >= 0) {
                int id = (int) model.getValueAt(selected, 0);
                if (PaketController.deletePaket(id)) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                    refreshTable();
                }
            }
        });

        JPanel actionPanel = new JPanel();
        actionPanel.add(btnTambah);
        actionPanel.add(btnHapus);

        add(inputPanel, BorderLayout.SOUTH);
        add(actionPanel, BorderLayout.PAGE_END);
    }

    private void refreshTable() {
        model.setRowCount(0);
        DefaultTableModel newModel = PaketController.getAllPaket();
        for (int i = 0; i < newModel.getRowCount(); i++) {
            model.addRow(newModel.getDataVector().elementAt(i));
        }
    }
}

