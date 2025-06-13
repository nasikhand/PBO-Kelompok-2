package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.PembayaranModel;

public class PembayaranDAO {
    private Connection conn;

    public PembayaranDAO(Connection conn) {
        this.conn = conn;
    }

    public Double getJumlahPembayaranByReservasiId(int reservasiId) {
        Double jumlahPembayaran = null;
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getJumlahPembayaranByReservasiId.");
            return null;
        }
        // Ambil jumlah pembayaran yang berstatus 'lunas'
        String sql = "SELECT SUM(jumlah_pembayaran) AS total_lunas FROM pembayaran WHERE reservasi_id = ? AND status_pembayaran = 'lunas'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble("total_lunas");
                    if (!rs.wasNull()) { // Cek apakah nilai dari DB bukan NULL
                        jumlahPembayaran = total;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil jumlah pembayaran: " + e.getMessage());
            e.printStackTrace();
        }
        return jumlahPembayaran;
    }

    // di PembayaranDAO
    public boolean insertPembayaran(PembayaranModel pembayaran) {
        String sql = "INSERT INTO pembayaran (reservasi_id, metode_pembayaran, jumlah_pembayaran, tanggal_pembayaran, status_pembayaran) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pembayaran.getReservasiId());
            ps.setString(2, pembayaran.getMetodePembayaran());
            ps.setDouble(3, pembayaran.getJumlahPembayaran());
            ps.setDate(4, new java.sql.Date(pembayaran.getTanggalPembayaran().getTime()));
            ps.setString(5, pembayaran.getStatusPembayaran());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

