/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import java.sql.*;

public class AuthController {
    public static boolean login(String email, String password) {
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password); // Catatan: belum hash, masih plaintext

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // jika ada baris, maka login berhasil
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
            return false;
        }
    }
}

