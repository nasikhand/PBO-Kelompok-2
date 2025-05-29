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

public class AdminController {

    public Admin login(String email, String password) {
        Admin admin = null;
        // Query ini melakukan perbandingan password sebagai plain text
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";

        System.out.println("Mencoba login dengan email: " + email); // Pesan Debug

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login berhasil untuk: " + email); // Pesan Debug
                    admin = new Admin();
                    admin.setId(rs.getInt("id"));
                    admin.setNamaLengkap(rs.getString("nama_lengkap"));
                    admin.setEmail(rs.getString("email"));
                } else {
                    // Ini berarti email atau password salah
                    System.out.println("Login gagal: Kombinasi email dan password tidak ditemukan."); // Pesan Debug
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error saat login:");
            e.printStackTrace();
        }
        return admin;
    }
}