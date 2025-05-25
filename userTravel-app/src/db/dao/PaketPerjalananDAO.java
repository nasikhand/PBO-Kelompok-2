package db.dao;

import model.PaketPerjalananModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaketPerjalananDAO {
    private Connection conn;

    public PaketPerjalananDAO(Connection conn) {
        this.conn = conn;
    }

    public List<PaketPerjalananModel> cariCepat(String destinasi, String tanggal, int jumlahTravelers) {
        List<PaketPerjalananModel> hasil = new ArrayList<>();
        String query = """
            SELECT pp.id, pp.nama_paket, k.nama_kota, pp.tanggal_mulai, pp.tanggal_akhir, 
                   pp.kuota, pp.harga, pp.deskripsi, pp.status
            FROM paket_perjalanan pp
            JOIN kota k ON pp.kota_id = k.id
            WHERE k.nama_kota LIKE ? 
              AND ? BETWEEN pp.tanggal_mulai AND pp.tanggal_akhir
              AND pp.kuota >= ?
              AND pp.status = 'tersedia'
        """;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + destinasi + "%");
            stmt.setString(2, tanggal);
            stmt.setInt(3, jumlahTravelers);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PaketPerjalananModel p = new PaketPerjalananModel(
                    rs.getInt("id"),
                    rs.getString("nama_paket"),
                    rs.getString("nama_kota"),
                    rs.getString("tanggal_mulai"),
                    rs.getString("tanggal_akhir"),
                    rs.getInt("kuota"),
                    rs.getDouble("harga"),
                    rs.getString("deskripsi"),
                    rs.getString("status")
                );
                hasil.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasil;
    }
}