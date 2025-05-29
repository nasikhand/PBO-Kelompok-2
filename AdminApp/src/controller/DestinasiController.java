/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Destinasi;
import model.Kota; // Perlu untuk mengambil daftar kota
import javax.swing.JOptionPane;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinasiController {

    /**
     * Mengambil semua data destinasi dengan nama kota terkait.
     */
    public List<Destinasi> getAllDestinasi() {
        List<Destinasi> daftarDestinasi = new ArrayList<>();
        String query = "SELECT d.id, d.kota_id, d.nama_destinasi, d.deskripsi, d.harga, d.gambar, k.nama_kota " +
                       "FROM destinasi d " +
                       "JOIN kota k ON d.kota_id = k.id " +
                       "ORDER BY d.nama_destinasi";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (conn == null) return daftarDestinasi;
            while (rs.next()) {
                Destinasi dest = new Destinasi();
                dest.setId(rs.getInt("id"));
                dest.setKotaId(rs.getInt("kota_id"));
                dest.setNamaDestinasi(rs.getString("nama_destinasi"));
                dest.setDeskripsi(rs.getString("deskripsi"));
                dest.setHarga(rs.getBigDecimal("harga"));
                dest.setGambar(rs.getString("gambar"));
                dest.setNamaKota(rs.getString("nama_kota"));
                daftarDestinasi.add(dest);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarDestinasi;
    }
    
    /**
     * Mengambil satu data destinasi berdasarkan ID.
     */
    public Destinasi getDestinasiById(int id) {
        Destinasi dest = null;
        String query = "SELECT d.*, k.nama_kota FROM destinasi d JOIN kota k ON d.kota_id = k.id WHERE d.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return null;
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    dest = new Destinasi();
                    dest.setId(rs.getInt("id"));
                    dest.setKotaId(rs.getInt("kota_id"));
                    dest.setNamaDestinasi(rs.getString("nama_destinasi"));
                    dest.setDeskripsi(rs.getString("deskripsi"));
                    dest.setHarga(rs.getBigDecimal("harga"));
                    dest.setGambar(rs.getString("gambar"));
                    dest.setNamaKota(rs.getString("nama_kota"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil detail destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * Menambah data destinasi baru.
     */
    public boolean addDestinasi(Destinasi dest) {
        // Pastikan kolom gambar ada di query jika tabel destinasi Anda memilikinya
        String query = "INSERT INTO destinasi (kota_id, nama_destinasi, deskripsi, harga, gambar) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, dest.getKotaId());
            stmt.setString(2, dest.getNamaDestinasi());
            stmt.setString(3, dest.getDeskripsi());
            stmt.setBigDecimal(4, dest.getHarga());
            stmt.setString(5, dest.getGambar()); // Nama file gambar
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui data destinasi.
     */
    public boolean updateDestinasi(Destinasi dest) {
        String query = "UPDATE destinasi SET kota_id = ?, nama_destinasi = ?, deskripsi = ?, harga = ?, gambar = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, dest.getKotaId());
            stmt.setString(2, dest.getNamaDestinasi());
            stmt.setString(3, dest.getDeskripsi());
            stmt.setBigDecimal(4, dest.getHarga());
            stmt.setString(5, dest.getGambar());
            stmt.setInt(6, dest.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus data destinasi.
     * Juga menghapus file gambar terkait jika ada.
     */
    public boolean deleteDestinasi(int id) {
        Destinasi dest = getDestinasiById(id); // Dapatkan info destinasi untuk nama gambar
        if (dest == null) return false;

        // PERHATIAN: Periksa apakah destinasi ini digunakan di rincian_paket_perjalanan atau rincian_custom_trip
        // Jika iya, mungkin tidak boleh dihapus atau ada penanganan khusus.
        // Untuk contoh ini, kita tambahkan pemeriksaan sederhana.
        if (isDestinasiUsed(id)) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus: Destinasi ini masih digunakan dalam rincian perjalanan.", "Kesalahan Hapus", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String query = "DELETE FROM destinasi WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Hapus file gambar dari folder jika ada
                if (dest.getGambar() != null && !dest.getGambar().isEmpty()) {
                    File fileGambar = new File("images/destinasi/" + dest.getGambar());
                    if (fileGambar.exists()) {
                        if (!fileGambar.delete()) {
                            System.err.println("Gagal menghapus file gambar destinasi: " + fileGambar.getAbsolutePath());
                        }
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    private boolean isDestinasiUsed(int destinasiId) {
        String queryCheckPaket = "SELECT COUNT(*) AS jumlah FROM rincian_paket_perjalanan WHERE destinasi_id = ?";
        String queryCheckCustom = "SELECT COUNT(*) AS jumlah FROM rincian_custom_trip WHERE destinasi_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return true; // Anggap digunakan jika koneksi gagal

            try (PreparedStatement stmt = conn.prepareStatement(queryCheckPaket)) {
                stmt.setInt(1, destinasiId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt("jumlah") > 0) return true;
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement(queryCheckCustom)) {
                stmt.setInt(1, destinasiId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt("jumlah") > 0) return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    /**
     * Mengambil semua data kota untuk dropdown di form destinasi.
     * (Bisa juga memanggil metode dari KotaController jika sudah ada dan di-refactor)
     */
    public List<Kota> getAllKota() {
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
                daftarKota.add(kota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarKota;
    }
}
