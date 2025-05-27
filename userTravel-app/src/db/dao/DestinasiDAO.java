package db.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.DestinasiModel;

public class DestinasiDAO {
    private Connection conn;

    public DestinasiDAO(Connection conn) {
        this.conn = conn;
    }

    public List<DestinasiModel> getAllDestinasi() {
        List<DestinasiModel> list = new ArrayList<>();
        String sql = "SELECT d.id, d.nama_destinasi, k.nama_kota FROM destinasi d JOIN kota k ON d.kota_id = k.id ORDER BY d.nama_destinasi";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new DestinasiModel(
                        rs.getInt("id"),
                        rs.getString("nama_destinasi"),
                        rs.getString("nama_kota")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
