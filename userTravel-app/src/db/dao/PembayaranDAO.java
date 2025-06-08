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

    public double getJumlahPembayaranByReservasiId(int reservasiId) {
        double total = 0;
        String sql = "SELECT jumlah_pembayaran FROM pembayaran WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("jumlah_pembayaran");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
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

