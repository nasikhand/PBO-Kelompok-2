/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Kota;
import model.PaketPerjalanan;

import javax.swing.JOptionPane;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerjalananController {

    /**
     * Mengambil semua data paket perjalanan dari database untuk ditampilkan di tabel.
     * Metode ini dibutuhkan oleh KelolaPerjalananView.
     */
    public List<PaketPerjalanan> getAllPaketPerjalanan() {
        List<PaketPerjalanan> daftarPaket = new ArrayList<>();
        String query = "SELECT pp.*, k.nama_kota FROM paket_perjalanan pp JOIN kota k ON pp.kota_id = k.id ORDER BY pp.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                PaketPerjalanan paket = new PaketPerjalanan();
                paket.setId(rs.getInt("id"));
                paket.setNamaPaket(rs.getString("nama_paket"));
                paket.setNamaKota(rs.getString("nama_kota"));
                paket.setTanggalMulai(rs.getDate("tanggal_mulai"));
                paket.setTanggalAkhir(rs.getDate("tanggal_akhir"));
                paket.setHarga(rs.getBigDecimal("harga"));
                paket.setKuota(rs.getInt("kuota"));
                paket.setStatus(rs.getString("status"));
                daftarPaket.add(paket);
            }
        } catch (SQLException e) {
            // TAMPILKAN POPUP JIKA ADA ERROR SQL
            JOptionPane.showMessageDialog(null, "Gagal mengambil data perjalanan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarPaket;
    }


    /**
     * Mengambil semua data kota untuk mengisi JComboBox (dropdown) di form.
     * Metode ini dibutuhkan oleh FormPerjalananDialog.
     */
    public List<Kota> getAllKota() {
        List<Kota> daftarKota = new ArrayList<>();
        String query = "SELECT * FROM kota";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Kota kota = new Kota();
                kota.setId(rs.getInt("id"));
                kota.setNamaKota(rs.getString("nama_kota"));
                kota.setProvinsi(rs.getString("provinsi"));
                daftarKota.add(kota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarKota;
    }

    /**
     * Menambahkan data paket perjalanan baru ke database.
     * Metode ini dibutuhkan oleh FormPerjalananDialog.
     */
    public boolean addPaketPerjalanan(PaketPerjalanan paket) {
        // Edit query untuk menyertakan kolom 'gambar'
        String query = "INSERT INTO paket_perjalanan (kota_id, nama_paket, tanggal_mulai, tanggal_akhir, kuota, harga, deskripsi, status, gambar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paket.getKotaId());
            stmt.setString(2, paket.getNamaPaket());
            stmt.setDate(3, paket.getTanggalMulai());
            stmt.setDate(4, paket.getTanggalAkhir());
            stmt.setInt(5, paket.getKuota());
            stmt.setBigDecimal(6, paket.getHarga());
            stmt.setString(7, paket.getDeskripsi());
            stmt.setString(8, paket.getStatus());
            stmt.setString(9, paket.getGambar()); // <-- TAMBAHKAN INI
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Mengambil satu data PaketPerjalanan berdasarkan ID-nya.
     * Diperlukan saat akan mengisi form untuk mode 'Ubah'.
     */
    public PaketPerjalanan getPaketById(int id) {
        PaketPerjalanan paket = null;
        String query = "SELECT * FROM paket_perjalanan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paket = new PaketPerjalanan();
                    paket.setId(rs.getInt("id"));
                    paket.setKotaId(rs.getInt("kota_id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));
                    paket.setTanggalMulai(rs.getDate("tanggal_mulai"));
                    paket.setTanggalAkhir(rs.getDate("tanggal_akhir"));
                    paket.setKuota(rs.getInt("kuota"));
                    paket.setHarga(rs.getBigDecimal("harga"));
                    paket.setDeskripsi(rs.getString("deskripsi"));
                    paket.setStatus(rs.getString("status"));
                    paket.setGambar(rs.getString("gambar"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paket;
    }

    /**
     * Memperbarui data paket perjalanan yang ada di database.
     */
    public boolean updatePaketPerjalanan(PaketPerjalanan paket) {
        String query = "UPDATE paket_perjalanan SET kota_id=?, nama_paket=?, tanggal_mulai=?, tanggal_akhir=?, kuota=?, harga=?, deskripsi=?, status=?, gambar=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paket.getKotaId());
            stmt.setString(2, paket.getNamaPaket());
            stmt.setDate(3, paket.getTanggalMulai());
            stmt.setDate(4, paket.getTanggalAkhir());
            stmt.setInt(5, paket.getKuota());
            stmt.setBigDecimal(6, paket.getHarga());
            stmt.setString(7, paket.getDeskripsi());
            stmt.setString(8, paket.getStatus());
            stmt.setString(9, paket.getGambar());
            stmt.setInt(10, paket.getId()); // WHERE clause
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus data paket perjalanan dari database berdasarkan ID.
     */
    public boolean deletePaketPerjalanan(int id) {
        // 1. Dapatkan nama file gambar sebelum dihapus dari DB
        PaketPerjalanan paket = getPaketById(id);
        if (paket == null) return false; // Data tidak ditemukan

        // 2. Hapus data dari database
        String query = "DELETE FROM paket_perjalanan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // 3. Jika berhasil, hapus file gambar dari folder
                if (paket.getGambar() != null && !paket.getGambar().isEmpty()) {
                    File fileGambar = new File("images/paket_perjalanan/" + paket.getGambar());
                    if (fileGambar.exists()) {
                        fileGambar.delete();
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // NANTI KITA TAMBAHKAN FUNGSI UBAH DAN HAPUS DI SINI
}
