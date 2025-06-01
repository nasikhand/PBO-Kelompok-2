package db.dao;

import model.DestinasiModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.Koneksi;



public class DestinasiDAO {

    public List<DestinasiModel> getAllDestinasi() {
        List<DestinasiModel> list = new ArrayList<>();

        try {
            Connection conn = Koneksi.getConnection();
            String sql = "SELECT * FROM destinasi";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                DestinasiModel d = new DestinasiModel();
                d.setId(rs.getInt("id"));
                d.setKotaId(rs.getInt("kota_id"));
                d.setNamaDestinasi(rs.getString("nama_destinasi"));
                d.setDeskripsi(rs.getString("deskripsi"));
                d.setHarga(rs.getDouble("harga"));
                d.setGambar(rs.getString("gambar"));

                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public DestinasiModel getDestinasiById(int id) {
        DestinasiModel d = null;

        try {
            Connection conn = Koneksi.getConnection();
            String sql = "SELECT * FROM destinasi WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                d = new DestinasiModel();
                d.setId(rs.getInt("id"));
                d.setKotaId(rs.getInt("kota_id"));
                d.setNamaDestinasi(rs.getString("nama_destinasi"));
                d.setDeskripsi(rs.getString("deskripsi"));
                d.setHarga(rs.getDouble("harga"));
                d.setGambar(rs.getString("gambar"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return d;
    }

    public List<DestinasiModel> getDestinasiByPaketId(int paketId) {
        List<DestinasiModel> list = new ArrayList<>();
        String sql = "SELECT d.* FROM destinasi d "
                + "JOIN rincian_paket_perjalanan rpp ON d.id = rpp.id_destinasi "
                + "WHERE rpp.paket_perjalanan_id = ?";
        try (Connection con = Koneksi.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, paketId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DestinasiModel d = new DestinasiModel();
                d.setId(rs.getInt("id"));
                d.setKotaId(rs.getInt("kota_id"));
                d.setNamaDestinasi(rs.getString("nama_destinasi"));
                d.setDeskripsi(rs.getString("deskripsi"));
                d.setHarga(rs.getDouble("harga"));
                d.setGambar(rs.getString("gambar"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
