/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.User;
import javax.swing.JOptionPane; // Untuk notifikasi error
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    /**
     * Mengambil semua data pengguna dari database.
     */
    public List<User> getAllUsers() {
        List<User> daftarUser = new ArrayList<>();
        String query = "SELECT id, nama_lengkap, email, no_telepon, alamat, created_at FROM user ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNamaLengkap(rs.getString("nama_lengkap"));
                user.setEmail(rs.getString("email"));
                user.setNoTelepon(rs.getString("no_telepon"));
                user.setAlamat(rs.getString("alamat"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                daftarUser.add(user);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pengguna: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarUser;
    }

    /**
     * Mengambil satu data pengguna berdasarkan ID.
     */
    public User getUserById(int id) {
        User user = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNamaLengkap(rs.getString("nama_lengkap"));
                    user.setEmail(rs.getString("email"));
                    user.setNoTelepon(rs.getString("no_telepon"));
                    user.setAlamat(rs.getString("alamat"));
                    // Password sebaiknya tidak diambil untuk diedit langsung
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil detail pengguna: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return user;
    }
    
    /**
     * Memperbarui data pengguna di database.
     * Perhatian: Mengubah password sebaiknya memiliki mekanisme terpisah (misal: reset password).
     * Di sini kita hanya ubah data non-sensitif.
     */
    public boolean updateUser(User user) {
        String query = "UPDATE user SET nama_lengkap = ?, email = ?, no_telepon = ?, alamat = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getNoTelepon());
            stmt.setString(4, user.getAlamat());
            stmt.setInt(5, user.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui data pengguna: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus data pengguna dari database.
     * PERHATIAN: Menghapus pengguna bisa berdampak pada data terkait (reservasi, dll.).
     * Pertimbangkan implementasi "soft delete" atau penanganan khusus.
     */
    public boolean deleteUser(int id) {
        // Di sini kita harus hati-hati dengan foreign key constraints ke tabel lain.
        // Untuk contoh ini, kita asumsikan penghapusan langsung.
        // Dalam aplikasi nyata, mungkin ada pemeriksaan atau pembatalan reservasi terkait.
        String query = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus pengguna: " + e.getMessage() + 
                "\n(Mungkin pengguna ini memiliki data reservasi terkait)", "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}
