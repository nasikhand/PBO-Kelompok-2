/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Kota;
import model.PaketPerjalanan;

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
        // Query dengan JOIN untuk mendapatkan nama kota agar bisa ditampilkan
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
                // Deskripsi bisa diambil jika diperlukan
                // paket.setDeskripsi(rs.getString("deskripsi"));
                daftarPaket.add(paket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Di aplikasi nyata, ini seharusnya menampilkan pesan error ke pengguna
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

    // NANTI KITA TAMBAHKAN FUNGSI UBAH DAN HAPUS DI SINI
}
