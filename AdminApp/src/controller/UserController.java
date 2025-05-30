/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.User;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    public int getTotalUserCount(String filterNamaAtauEmail) {
        String query = "SELECT COUNT(*) FROM user ";
        if (filterNamaAtauEmail != null && !filterNamaAtauEmail.isEmpty()) {
            query += "WHERE nama_lengkap LIKE ? OR email LIKE ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return 0;
            if (filterNamaAtauEmail != null && !filterNamaAtauEmail.isEmpty()) {
                stmt.setString(1, "%" + filterNamaAtauEmail + "%");
                stmt.setString(2, "%" + filterNamaAtauEmail + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghitung total pengguna: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getUsersWithPagination(int halaman, int dataPerHalaman, String filterNamaAtauEmail) {
        List<User> daftarUser = new ArrayList<>();
        String query = "SELECT id, nama_lengkap, email, no_telepon, alamat, created_at FROM user ";
        
        if (filterNamaAtauEmail != null && !filterNamaAtauEmail.isEmpty()) {
            query += "WHERE nama_lengkap LIKE ? OR email LIKE ? ";
        }
        
        query += "ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return daftarUser;

            int paramIndex = 1;
            if (filterNamaAtauEmail != null && !filterNamaAtauEmail.isEmpty()) {
                stmt.setString(paramIndex++, "%" + filterNamaAtauEmail + "%");
                stmt.setString(paramIndex++, "%" + filterNamaAtauEmail + "%");
            }
            stmt.setInt(paramIndex++, dataPerHalaman);
            stmt.setInt(paramIndex, (halaman - 1) * dataPerHalaman);

            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pengguna (paginasi): " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarUser;
    }

    public User getUserById(int id) {
        User user = null;
        String query = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return null;

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNamaLengkap(rs.getString("nama_lengkap"));
                    user.setEmail(rs.getString("email"));
                    user.setNoTelepon(rs.getString("no_telepon"));
                    user.setAlamat(rs.getString("alamat"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil detail pengguna: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return user;
    }
    
    public boolean updateUser(User user) {
        String query = "UPDATE user SET nama_lengkap = ?, email = ?, no_telepon = ?, alamat = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return false;
            
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

    public boolean deleteUser(int id) {
        String query = "DELETE FROM user WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return false;

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus pengguna: " + e.getMessage() + 
                "\n(Mungkin pengguna ini memiliki data reservasi atau custom trip terkait)", "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public int getTotalPengguna() {
        String query = "SELECT COUNT(*) AS jumlah_pengguna FROM user";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return 0;

            if (rs.next()) {
                return rs.getInt("jumlah_pengguna");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil total pengguna: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }
}