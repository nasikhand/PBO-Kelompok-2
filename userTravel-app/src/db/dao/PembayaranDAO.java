package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public String getMetodePembayaranByReservasiId(int reservasiId) {
        String metode = "";
        String sql = "SELECT metode_pembayaran FROM pembayaran WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                metode = rs.getString("metode_pembayaran");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metode;
    }
}

