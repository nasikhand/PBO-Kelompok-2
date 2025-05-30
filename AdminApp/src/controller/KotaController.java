/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Kota;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KotaController {
    
     public List<Kota> getAllKotaForComboBox() {
        List<Kota> daftarKota = new ArrayList<>();
        String query = "SELECT * FROM kota ORDER BY nama_kota";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (conn == null) return daftarKota;

            while (rs.next()) {
                Kota kota = new Kota();
                kota.setId(rs.getInt("id"));
                kota.setNamaKota(rs.getString("nama_kota"));
                kota.setProvinsi(rs.getString("provinsi"));
                daftarKota.add(kota);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil daftar kota untuk ComboBox: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarKota;
    }

    public int getTotalKotaCount(String filterNama) {
        String query = "SELECT COUNT(*) FROM kota ";
        if (filterNama != null && !filterNama.isEmpty()) {
            query += "WHERE nama_kota LIKE ? OR provinsi LIKE ?";
        }
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return 0;
            if (filterNama != null && !filterNama.isEmpty()) {
                stmt.setString(1, "%" + filterNama + "%");
                stmt.setString(2, "%" + filterNama + "%");
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Kota> getKotaWithPagination(int halaman, int dataPerHalaman, String filterNama) {
        List<Kota> daftarKota = new ArrayList<>();
        String query = "SELECT * FROM kota ";
        if (filterNama != null && !filterNama.isEmpty()) {
            query += "WHERE nama_kota LIKE ? OR provinsi LIKE ? ";
        }
        query += "ORDER BY nama_kota ASC LIMIT ? OFFSET ?";
        
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return daftarKota;
            int paramIndex = 1;
            if (filterNama != null && !filterNama.isEmpty()) {
                stmt.setString(paramIndex++, "%" + filterNama + "%");
                stmt.setString(paramIndex++, "%" + filterNama + "%");
            }
            stmt.setInt(paramIndex++, dataPerHalaman);
            stmt.setInt(paramIndex, (halaman - 1) * dataPerHalaman);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Kota kota = new Kota();
                    kota.setId(rs.getInt("id"));
                    kota.setNamaKota(rs.getString("nama_kota"));
                    kota.setProvinsi(rs.getString("provinsi"));
                    daftarKota.add(kota);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarKota;
    }

    public Kota getKotaById(int id) {
        Kota kota = null;
        String query = "SELECT * FROM kota WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return null;
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    kota = new Kota();
                    kota.setId(rs.getInt("id"));
                    kota.setNamaKota(rs.getString("nama_kota"));
                    kota.setProvinsi(rs.getString("provinsi"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kota;
    }
    
    public boolean addKota(Kota kota) {
        String query = "INSERT INTO kota (nama_kota, provinsi) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setString(1, kota.getNamaKota());
            stmt.setString(2, kota.getProvinsi());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah kota: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateKota(Kota kota) {
        String query = "UPDATE kota SET nama_kota = ?, provinsi = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setString(1, kota.getNamaKota());
            stmt.setString(2, kota.getProvinsi());
            stmt.setInt(3, kota.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui kota: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteKota(int id) {
        if (isKotaUsedInDestinasi(id)) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus: Kota ini masih digunakan oleh data destinasi.", "Kesalahan Hapus", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String query = "DELETE FROM kota WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus kota: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean isKotaUsedInDestinasi(int kotaId) {
        String query = "SELECT COUNT(*) FROM destinasi WHERE kota_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return true;
            stmt.setInt(1, kotaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); return true; }
        return false;
    }
}
