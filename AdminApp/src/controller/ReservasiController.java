/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Reservasi;
import model.Penumpang;

import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservasiController {

    /**
     * Mengambil semua data reservasi dari database.
     * Menggunakan CASE untuk mendapatkan nama trip dari tabel yang berbeda.
     */
    public List<Reservasi> getAllReservasi() {
        List<Reservasi> daftarReservasi = new ArrayList<>();
        String query = "SELECT r.id, r.kode_reservasi, r.tanggal_reservasi, r.status, r.trip_type, " +
                       "CASE " +
                       "    WHEN r.trip_type = 'paket_perjalanan' THEN pp.nama_paket " +
                       "    WHEN r.trip_type = 'custom_trip' THEN ct.nama_trip " +
                       "END AS nama_trip " +
                       "FROM reservasi r " +
                       "LEFT JOIN paket_perjalanan pp ON r.trip_id = pp.id AND r.trip_type = 'paket_perjalanan' " +
                       "LEFT JOIN custom_trip ct ON r.trip_id = ct.id AND r.trip_type = 'custom_trip' " +
                       "ORDER BY r.tanggal_reservasi DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reservasi reservasi = new Reservasi();
                reservasi.setId(rs.getInt("id"));
                reservasi.setKodeReservasi(rs.getString("kode_reservasi"));
                reservasi.setTanggalReservasi(rs.getDate("tanggal_reservasi"));
                reservasi.setStatus(rs.getString("status"));
                reservasi.setTripType(rs.getString("trip_type"));
                reservasi.setNamaTrip(rs.getString("nama_trip"));
                daftarReservasi.add(reservasi);
            }
        } catch (SQLException e) {
            // TAMPILKAN POPUP JIKA ADA ERROR SQL
            JOptionPane.showMessageDialog(null, "Gagal mengambil data reservasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarReservasi;
    }


    /**
     * Memperbarui status sebuah reservasi (misal: dari 'dipesan' menjadi 'dibayar').
     */
    public boolean updateStatusReservasi(int idReservasi, String status) {
        String query = "UPDATE reservasi SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, idReservasi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Penumpang> getPenumpangByReservasiId(int idReservasi) {
        List<Penumpang> daftarPenumpang = new ArrayList<>();
        String query = "SELECT * FROM penumpang WHERE reservasi_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idReservasi);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Penumpang penumpang = new Penumpang();
                    penumpang.setId(rs.getInt("id"));
                    penumpang.setNamaPenumpang(rs.getString("nama_penumpang"));
                    penumpang.setJenisKelamin(rs.getString("jenis_kelamin"));
                    penumpang.setTanggalLahir(rs.getDate("tanggal_lahir"));
                    penumpang.setNomorTelepon(rs.getString("nomor_telepon"));
                    penumpang.setEmail(rs.getString("email"));
                    daftarPenumpang.add(penumpang);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPenumpang;
    }
}
