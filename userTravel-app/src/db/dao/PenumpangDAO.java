package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PenumpangDAO {
    private Connection conn;

    public PenumpangDAO(Connection conn) {
        this.conn = conn;
    }

    public int getJumlahPenumpangByReservasiId(int reservasiId) {
        int jumlah = 0;
        String sql = "SELECT COUNT(*) AS total FROM penumpang WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jumlah = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jumlah;
    }
}
