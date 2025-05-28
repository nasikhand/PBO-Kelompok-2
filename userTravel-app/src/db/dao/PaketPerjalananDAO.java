package db.dao;
import model.PaketPerjalananModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaketPerjalananDAO {
    private Connection conn;

    public PaketPerjalananDAO() {
        // Inisialisasi jika perlu
    }
    
    public PaketPerjalananDAO(Connection conn) {
        this.conn = conn;
    }

    public List<PaketPerjalananModel> getAllPaket() {
        List<PaketPerjalananModel> list = new ArrayList<>();
        String query = "SELECT * FROM paket_perjalanan";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                PaketPerjalananModel p = new PaketPerjalananModel(
                    rs.getInt("id"),
                    rs.getInt("kota_id"),
                    rs.getString("nama_paket"),
                    rs.getString("tanggal_mulai"),
                    rs.getString("tanggal_akhir"),
                    rs.getInt("kuota"),
                    rs.getDouble("harga"),
                    rs.getString("deskripsi"),
                    rs.getString("status"),
                    rs.getString("gambar"),
                    rs.getDouble("rating")
                );
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public PaketPerjalananModel getById(int id) {
        String query = "SELECT * FROM paket_perjalanan WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PaketPerjalananModel(
                    rs.getInt("id"),
                    rs.getInt("kota_id"),
                    rs.getString("nama_paket"),
                    rs.getString("tanggal_mulai"),
                    rs.getString("tanggal_akhir"),
                    rs.getInt("kuota"),
                    rs.getDouble("harga"),
                    rs.getString("deskripsi"),
                    rs.getString("status"),
                    rs.getString("gambar"),
                    rs.getDouble("rating")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<PaketPerjalananModel> getPaketByDestinasi(String destinasi) {
        List<PaketPerjalananModel> list = new ArrayList<>();
        String sql = "SELECT * FROM paket_perjalanan WHERE nama_paket LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + destinasi + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PaketPerjalananModel p = new PaketPerjalananModel();
                p.setId(rs.getInt("id"));
                p.setKotaId(rs.getInt("kota_id"));
                p.setNamaPaket(rs.getString("nama_paket"));
                p.setTanggalMulai(rs.getString("tanggal_mulai"));
                p.setTanggalAkhir(rs.getString("tanggal_akhir"));
                p.setKuota(rs.getInt("kuota"));
                p.setHarga(rs.getDouble("harga"));
                p.setDeskripsi(rs.getString("deskripsi"));
                p.setStatus(rs.getString("status"));
                p.setGambar(rs.getString("gambar"));
                p.setRating(rs.getDouble("rating"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Method untuk mengambil paket perjalanan berdasarkan rating tertinggi
    public List<PaketPerjalananModel> getTopRatedPakets(int limit) {
        List<PaketPerjalananModel> list = new ArrayList<>();
        String query = "SELECT * FROM paket_perjalanan ORDER BY rating DESC LIMIT ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PaketPerjalananModel paket = new PaketPerjalananModel();
                    paket.setId(rs.getInt("id"));
                    paket.setKotaId(rs.getInt("kota_id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));
                    paket.setTanggalMulai(rs.getString("tanggal_mulai"));
                    paket.setTanggalAkhir(rs.getString("tanggal_akhir"));
                    paket.setKuota(rs.getInt("kuota"));
                    paket.setHarga(rs.getDouble("harga"));
                    paket.setDeskripsi(rs.getString("deskripsi"));
                    paket.setStatus(rs.getString("status"));
                    paket.setGambar(rs.getString("gambar"));
                    paket.setRating(rs.getDouble("rating"));
                    list.add(paket);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // bisa diganti dengan Logger jika ingin lebih rapi
        }

        return list;
    }
}
