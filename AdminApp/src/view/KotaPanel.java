/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.KotaController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KotaPanel extends JPanel {

    private JTable kotaTable;
    private DefaultTableModel model;
    private JTextField tfNama;
    private JButton btnTambah, btnUpdate, btnHapus;

    public KotaPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Kelola Kota", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        model = KotaController.getAllKota();
        kotaTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(kotaTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        tfNama = new JTextField();

        formPanel.setBorder(BorderFactory.createTitledBorder("Form Kota"));
        formPanel.add(new JLabel("Nama Kota:"));
        formPanel.add(tfNama);

        btnTambah = new JButton("Tambah");
        btnUpdate = new JButton("Update");
        btnHapus = new JButton("Hapus");

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        kotaTable.getSelectionModel().addListSelectionListener(e -> {
            int selected = kotaTable.getSelectedRow();
            if (selected >= 0) {
                tfNama.setText(model.getValueAt(selected, 1).toString());
            }
        });

        btnTambah.addActionListener(e -> {
            if (!tfNama.getText().isEmpty()) {
                boolean result = KotaController.insertKota(tfNama.getText());
                if (result) {
                    JOptionPane.showMessageDialog(this, "Data kota ditambahkan.");
                    refreshTable();
                    tfNama.setText("");
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            int selected = kotaTable.getSelectedRow();
            if (selected >= 0 && !tfNama.getText().isEmpty()) {
                int id = (int) model.getValueAt(selected, 0);
                boolean result = KotaController.updateKota(id, tfNama.getText());
                if (result) {
                    JOptionPane.showMessageDialog(this, "Data kota diperbarui.");
                    refreshTable();
                    tfNama.setText("");
                }
            }
        });

        btnHapus.addActionListener(e -> {
            int selected = kotaTable.getSelectedRow();
            if (selected >= 0) {
                int id = (int) model.getValueAt(selected, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus kota?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean result = KotaController.deleteKota(id);
                    if (result) {
                        JOptionPane.showMessageDialog(this, "Data kota dihapus.");
                        refreshTable();
                        tfNama.setText("");
                    }
                }
            }
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        DefaultTableModel newModel = KotaController.getAllKota();
        for (int i = 0; i < newModel.getRowCount(); i++) {
            model.addRow(newModel.getDataVector().elementAt(i));
        }
    }
}