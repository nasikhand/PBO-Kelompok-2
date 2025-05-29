/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AdminController {

    public Admin login(String email, String password) {
        Admin admin = null;
        // Perhatian: Di aplikasi production, password HARUS di-hash dan diverifikasi dengan hash.
        // Untuk proyek ini, kita masih menggunakan perbandingan plain text sesuai data sampel.
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";

        System.out.println("Mencoba login dengan email: " + email); // Pesan Debug

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) {
                // JOptionPane sudah ditampilkan oleh DatabaseConnection jika koneksi gagal
                return null;
            }

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login berhasil untuk: " + email);
                    admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setNamaLengkap(rs.getString("nama_lengkap"));
                    admin.setEmail(rs.getString("email"));
                    // Sebaiknya jangan simpan password di objek model setelah login
                } else {
                    System.out.println("Login gagal: Kombinasi email dan password tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan SQL saat login: " + e.getMessage(), "Kesalahan Login", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return admin;
    }
}