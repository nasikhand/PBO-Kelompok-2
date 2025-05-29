/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class FormPenggunaDialog extends JDialog {
    private UserController controller;
    private User userToEdit;

    private JTextField txtNamaLengkap, txtEmail, txtNoTelepon;
    private JTextArea txtAlamat;
    private JButton btnSimpan;

    public FormPenggunaDialog(Frame owner, User user) {
        super(owner, "Ubah Data Pengguna", true);
        this.userToEdit = user;
        this.controller = new UserController();

        setSize(450, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inisialisasi Komponen
        txtNamaLengkap = new JTextField(20);
        txtEmail = new JTextField(20);
        txtNoTelepon = new JTextField(20);
        txtAlamat = new JTextArea(3, 20);
        btnSimpan = new JButton("Simpan Perubahan");

        // Mengisi Form dengan Data User
        if (userToEdit != null) {
            txtNamaLengkap.setText(userToEdit.getNamaLengkap());
            txtEmail.setText(userToEdit.getEmail());
            txtNoTelepon.setText(userToEdit.getNoTelepon());
            txtAlamat.setText(userToEdit.getAlamat());
        }

        // Menambahkan komponen ke panel
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNamaLengkap, gbc);
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panelForm.add(txtEmail, gbc);
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("No. Telepon:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNoTelepon, gbc);
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; panelForm.add(new JLabel("Alamat:"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; panelForm.add(new JScrollPane(txtAlamat), gbc);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombol.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        panelTombol.add(btnSimpan);
        add(panelTombol, BorderLayout.SOUTH);

        btnSimpan.addActionListener(e -> simpanPerubahan());
    }

    private void simpanPerubahan() {
        if (userToEdit == null) return;

        userToEdit.setNamaLengkap(txtNamaLengkap.getText());
        userToEdit.setEmail(txtEmail.getText());
        userToEdit.setNoTelepon(txtNoTelepon.getText());
        userToEdit.setAlamat(txtAlamat.getText());

        boolean sukses = controller.updateUser(userToEdit);
        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data pengguna berhasil diperbarui.");
            dispose();
        } else {
            // Pesan error sudah ditampilkan oleh controller
        }
    }
}
