/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// File: DestinasiPanel.java
package view;

import controller.DestinasiController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class DestinasiPanel extends JPanel {
    JTable destinasiTable;
    DefaultTableModel model;
    
    JLabel lblPreview = new JLabel();
    JTextField tfKotaId = new JTextField();
    JTextField tfNama = new JTextField();
    JTextArea taDeskripsi = new JTextArea(3, 20);
    JTextField tfHarga = new JTextField();
    JTextField tfGambar = new JTextField();
    JButton btnPilihGambar = new JButton("Pilih Gambar");

    JButton btnTambah = new JButton("Tambah Destinasi");
    JButton btnUpdate = new JButton("Update Destinasi");
    JButton btnHapus = new JButton("Hapus Destinasi");

    public DestinasiPanel() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Kelola Destinasi Wisata", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        model = DestinasiController.getAllDestinasi();
        destinasiTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(destinasiTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Destinasi"));

        inputPanel.add(new JLabel("Kota ID:")); inputPanel.add(tfKotaId);
        inputPanel.add(new JLabel("Nama Destinasi:")); inputPanel.add(tfNama);
        inputPanel.add(new JLabel("Deskripsi:")); inputPanel.add(new JScrollPane(taDeskripsi));
        inputPanel.add(new JLabel("Harga:")); inputPanel.add(tfHarga);
        inputPanel.add(new JLabel("Gambar:")); inputPanel.add(tfGambar);
        inputPanel.add(new JLabel("")); inputPanel.add(btnPilihGambar);

        JPanel actionPanel = new JPanel();
        actionPanel.add(btnTambah);
        actionPanel.add(btnUpdate);
        actionPanel.add(btnHapus);

        add(inputPanel, BorderLayout.SOUTH);
        add(actionPanel, BorderLayout.PAGE_END);

        btnPilihGambar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                tfGambar.setText(path);
            }
        });

        btnTambah.addActionListener(e -> {
            String namaFile = copyGambar(tfGambar.getText());
            boolean inserted = DestinasiController.insertDestinasi(
                    Integer.parseInt(tfKotaId.getText()),
                    tfNama.getText(),
                    taDeskripsi.getText(),
                    Double.parseDouble(tfHarga.getText()),
                    namaFile
            );
            if (inserted) {
                JOptionPane.showMessageDialog(this, "Destinasi berhasil ditambah!");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal tambah destinasi!");
            }
        });

        btnUpdate.addActionListener(e -> {
            int selected = destinasiTable.getSelectedRow();
            if (selected >= 0) {
                int id = (int) model.getValueAt(selected, 0);
                String namaFile = copyGambar(tfGambar.getText());
                boolean updated = DestinasiController.updateDestinasi(
                        id,
                        Integer.parseInt(tfKotaId.getText()),
                        tfNama.getText(),
                        taDeskripsi.getText(),
                        Double.parseDouble(tfHarga.getText()),
                        namaFile
                );
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Destinasi berhasil diperbarui!");
                    refreshTable();
                }
            }
        });

        btnHapus.addActionListener(e -> {
            int selected = destinasiTable.getSelectedRow();
            if (selected >= 0) {
                int id = (int) model.getValueAt(selected, 0);
                if (DestinasiController.deleteDestinasi(id)) {
                    JOptionPane.showMessageDialog(this, "Destinasi berhasil dihapus!");
                    refreshTable();
                }
            }
        });

        destinasiTable.getSelectionModel().addListSelectionListener(e -> {
            int selected = destinasiTable.getSelectedRow();
            if (selected >= 0) {
                tfKotaId.setText(model.getValueAt(selected, 1).toString());
                tfNama.setText(model.getValueAt(selected, 2).toString());
                taDeskripsi.setText(model.getValueAt(selected, 3).toString());
                tfHarga.setText(model.getValueAt(selected, 4).toString());
                String namaGambar = model.getValueAt(selected, 5) != null ? model.getValueAt(selected, 5).toString() : "";
                tfGambar.setText(namaGambar);

                // Tampilkan gambar
                lblPreview.setIcon(null);
                if (!namaGambar.isEmpty()) {
                    File imgFile = new File("images/" + namaGambar);
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
                        lblPreview.setIcon(new ImageIcon(img));
                    }
                }
            }
        });

        
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview Gambar"));
        previewPanel.add(lblPreview, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.EAST);

    }

    private void refreshTable() {
        model.setRowCount(0);
        DefaultTableModel newModel = DestinasiController.getAllDestinasi();
        for (int i = 0; i < newModel.getRowCount(); i++) {
            model.addRow(newModel.getDataVector().elementAt(i));
        }
    }

    private String copyGambar(String sourcePath) {
        if (sourcePath == null || sourcePath.isEmpty()) return "";

        File source = new File(sourcePath);
        String fileName = source.getName();
        File dest = new File("images/" + fileName);

        try {
            Files.createDirectories(Paths.get("images"));
            Files.copy(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}

