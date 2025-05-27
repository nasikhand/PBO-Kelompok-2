/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.pengguna;

import controller.PenggunaController;
import model.Pengguna;

import javax.swing.*;
import java.awt.*;

public class FormPenggunaDialog extends JDialog {
    private JTextField namaField, emailField, teleponField, alamatField;
    private JPasswordField passwordField;
    private boolean isEdit = false;
    private int editId = -1;
    private Runnable onSuccess;

    public FormPenggunaDialog(JFrame parent, Runnable onSuccess) {
        this(parent, onSuccess, -1);
    }

    public FormPenggunaDialog(JFrame parent, Runnable onSuccess, int editId) {
        super(parent, (editId == -1 ? "Tambah" : "Edit") + " Pengguna", true);
        this.onSuccess = onSuccess;
        this.editId = editId;
        this.isEdit = editId != -1;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        namaField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        teleponField = new JTextField();
        alamatField = new JTextField();

        formPanel.add(new JLabel("Nama Lengkap"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Email"));
        formPanel.add(emailField);
        if (!isEdit) {
            formPanel.add(new JLabel("Password"));
            formPanel.add(passwordField);
        }
        formPanel.add(new JLabel("No Telepon"));
        formPanel.add(teleponField);
        formPanel.add(new JLabel("Alamat"));
        formPanel.add(alamatField);

        JButton simpanBtn = new JButton("Simpan");
        simpanBtn.setBackground(new Color(58, 130, 247));
        simpanBtn.setForeground(Color.WHITE);

        simpanBtn.addActionListener(e -> {
            try {
                Pengguna u = new Pengguna();
                u.setNamaLengkap(namaField.getText());
                u.setEmail(emailField.getText());
                u.setTelepon(teleponField.getText());
                u.setAlamat(alamatField.getText());

                boolean result;
                if (isEdit) {
                    u.setId(editId);
                    result = PenggunaController.update(u);
                } else {
                    u.setPassword(new String(passwordField.getPassword()));
                    result = PenggunaController.insert(u);
                }

                if (result) {
                    JOptionPane.showMessageDialog(this, "Data berhasil disimpan.");
                    if (onSuccess != null) onSuccess.run();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        add(formPanel, BorderLayout.CENTER);
        add(simpanBtn, BorderLayout.SOUTH);

        if (isEdit) {
            Pengguna u = PenggunaController.getById(editId);
            if (u != null) {
                namaField.setText(u.getNamaLengkap());
                emailField.setText(u.getEmail());
                teleponField.setText(u.getTelepon());
                alamatField.setText(u.getAlamat());
            }
        }

        setVisible(true);
    }
}
