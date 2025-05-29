/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Pembayaran;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembayaranController {

    /**
     * Mengambil semua data pembayaran dari database, di-join dengan kode reservasi.
     */
    public List<Pembayaran> getAllPembayaran() {
        List<Pembayaran> daftarPembayaran = new ArrayList<>();
        String query = "SELECT p.id, p.reservasi_id, p.metode_pembayaran, p.jumlah_pembayaran, " +
                       "p.tanggal_pembayaran, p.status_pembayaran, r.kode_reservasi " +
                       "FROM pembayaran p " +
                       "JOIN reservasi r ON p.reservasi_id = r.id " +
                       "ORDER BY p.tanggal_pembayaran DESC, p.id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return daftarPembayaran; // Koneksi gagal

            while (rs.next()) {
                Pembayaran bayar = new Pembayaran();
                bayar.setId(rs.getInt("id"));
                bayar.setReservasiId(rs.getInt("reservasi_id"));
                bayar.setMetodePembayaran(rs.getString("metode_pembayaran"));
                bayar.setJumlahPembayaran(rs.getBigDecimal("jumlah_pembayaran"));
                bayar.setTanggalPembayaran(rs.getDate("tanggal_pembayaran"));
                bayar.setStatusPembayaran(rs.getString("status_pembayaran"));
                bayar.setKodeReservasi(rs.getString("kode_reservasi"));
                daftarPembayaran.add(bayar);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data pembayaran: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarPembayaran;
    }

    /**
     * Memperbarui status pembayaran (misal: dari 'pending' menjadi 'lunas').
     * Juga memperbarui status reservasi terkait jika pembayaran lunas.
     */
    public boolean updateStatusPembayaran(int idPembayaran, String statusBaru) {
        String queryPembayaran = "UPDATE pembayaran SET status_pembayaran = ? WHERE id = ?";
        String queryReservasi = "UPDATE reservasi SET status = ? WHERE id = (SELECT reservasi_id FROM pembayaran WHERE id = ?)";
        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false); // Mulai transaksi

            // Update status di tabel pembayaran
            try (PreparedStatement stmtPembayaran = conn.prepareStatement(queryPembayaran)) {
                stmtPembayaran.setString(1, statusBaru);
                stmtPembayaran.setInt(2, idPembayaran);
                stmtPembayaran.executeUpdate();
            }

            // Jika status pembayaran baru adalah 'lunas', update status reservasi menjadi 'dibayar'
            if ("lunas".equalsIgnoreCase(statusBaru)) {
                try (PreparedStatement stmtReservasi = conn.prepareStatement(queryReservasi)) {
                    stmtReservasi.setString(1, "dibayar"); // Atau status lain yang sesuai
                    stmtReservasi.setInt(2, idPembayaran);
                    stmtReservasi.executeUpdate();
                }
            }
            // Anda bisa menambahkan logika lain di sini, misalnya jika status 'gagal',
            // maka status reservasi diubah menjadi 'dibatalkan' atau 'pending pembayaran ulang'.

            conn.commit(); // Akhiri transaksi dengan sukses
            success = true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui status pembayaran: " + e.getMessage(), "Kesalahan SQL Transaksi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // Batalkan transaksi jika terjadi error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Kembalikan ke mode auto-commit
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return success;
    }
}
