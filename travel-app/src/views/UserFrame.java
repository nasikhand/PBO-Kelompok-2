package views;
import db.Koneksi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserFrame extends JFrame {
    private JTextField namaField, emailField, teleponField;
    private JTextArea alamatArea;
    private JPasswordField passwordField;
    private JButton btnSimpan;

    public UserFrame() {
        setTitle("Tambah User");
        setSize(400, 400);
        setLayout(new GridLayout(6, 2));

        add(new JLabel("Nama Lengkap"));
        namaField = new JTextField();
        add(namaField);

        add(new JLabel("Email"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("No Telepon"));
        teleponField = new JTextField();
        add(teleponField);

        add(new JLabel("Alamat"));
        alamatArea = new JTextArea();
        add(alamatArea);

        btnSimpan = new JButton("Simpan");
        add(btnSimpan);

        btnSimpan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simpanUser();
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void simpanUser() {
        String sql = "INSERT INTO user (nama_lengkap, email, password, no_telepon, alamat) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, namaField.getText());
            stmt.setString(2, emailField.getText());
            stmt.setString(3, new String(passwordField.getPassword()));
            stmt.setString(4, teleponField.getText());
            stmt.setString(5, alamatArea.getText());

            int hasil = stmt.executeUpdate();
            if (hasil > 0) {
                JOptionPane.showMessageDialog(this, "User berhasil disimpan!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }
}
