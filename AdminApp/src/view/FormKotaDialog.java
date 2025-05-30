/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.KotaController;
import model.Kota;
import javax.swing.*;
import java.awt.*;

public class FormKotaDialog extends JDialog {
    private KotaController controller;
    private Kota kotaToEdit;
    private JTextField txtNamaKota, txtProvinsi;
    private JButton btnSimpan;

    public FormKotaDialog(Frame owner, Kota kota) {
        super(owner, true); // Modal
        this.kotaToEdit = kota;
        this.controller = new KotaController();
        
        setTitle(kota == null ? "Tambah Kota Baru" : "Ubah Data Kota");
        setSize(400, 250);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtNamaKota = new JTextField(20);
        txtProvinsi = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Nama Kota:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNamaKota, gbc);
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Provinsi:"), gbc);
        gbc.gridx = 1; panelForm.add(txtProvinsi, gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSimpan = new JButton(kota == null ? "Simpan Baru" : "Simpan Perubahan");
        panelTombol.add(btnSimpan);
        add(panelTombol, BorderLayout.SOUTH);

        if (kotaToEdit != null) {
            txtNamaKota.setText(kotaToEdit.getNamaKota());
            txtProvinsi.setText(kotaToEdit.getProvinsi());
        }

        btnSimpan.addActionListener(e -> simpanData());
    }

    private void simpanData() {
        String nama = txtNamaKota.getText().trim();
        String provinsi = txtProvinsi.getText().trim();
        if (nama.isEmpty() || provinsi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kota dan provinsi tidak boleh kosong.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Kota kota = new Kota();
        kota.setNamaKota(nama);
        kota.setProvinsi(provinsi);

        boolean sukses;
        if (kotaToEdit == null) {
            sukses = controller.addKota(kota);
        } else {
            kota.setId(kotaToEdit.getId());
            sukses = controller.updateKota(kota);
        }

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data kota berhasil disimpan.");
            dispose();
        }
    }
}
