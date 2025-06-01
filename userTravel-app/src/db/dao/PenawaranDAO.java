package db.dao;

import model.PenawaranModel; // pastikan ini sesuai dengan path class PenawaranModel
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PenawaranDAO {
    private Connection conn;

    public PenawaranDAO(Connection conn) {
        this.conn = conn;
    }

    public List<PenawaranModel> getPenawaranSpesial() {
        List<PenawaranModel> list = new ArrayList<>();
        String query = "SELECT * FROM paket_perjalanan WHERE diskon > 0 ORDER BY diskon DESC LIMIT 3";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PenawaranModel model = new PenawaranModel();
                model.setId(rs.getInt("id"));
                model.setNama(rs.getString("nama_paket"));
                model.setHarga(rs.getBigDecimal("harga"));
                model.setDiskon(rs.getBigDecimal("diskon"));
                model.setDeskripsi(rs.getString("deskripsi"));
                model.setGambar(rs.getString("gambar")); // jika ada
                list.add(model);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
