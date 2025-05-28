package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.Koneksi;

public class KotaDAO {
    private Connection conn;

    public KotaDAO() {
        conn = Koneksi.getConnection(); // sesuaikan dengan cara kamu ambil koneksi
    }

    public String getNamaKotaById(int kotaId) {
        String nama = "";
        try {
            String sql = "SELECT nama_kota FROM kota WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, kotaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nama = rs.getString("nama_kota");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nama;
    }
}
