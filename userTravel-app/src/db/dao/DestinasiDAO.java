package db.dao;

import model.DestinasiModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Pastikan ini diimpor
import java.util.ArrayList;
import java.util.List;
import db.Koneksi; // Pastikan ini diimpor

public class DestinasiDAO {

    private Connection conn; // Deklarasikan Connection sebagai field

    // Konstruktor default yang mendapatkan koneksi dari Koneksi.getConnection()
    public DestinasiDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh DestinasiDAO.");
        }
    }

    // Konstruktor yang menerima objek Connection (untuk penggunaan di DAO lain)
    public DestinasiDAO(Connection conn) {
        this.conn = conn;
        if (this.conn == null) {
            System.err.println("Koneksi yang diberikan ke DestinasiDAO adalah NULL.");
        }
    }

    public List<DestinasiModel> getAllDestinasi() {
        List<DestinasiModel> list = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getAllDestinasi.");
            return list;
        }

        String sql = "SELECT * FROM destinasi";
        try (Statement stmt = conn.createStatement(); // Gunakan try-with-resources
             ResultSet rs = stmt.executeQuery(sql)) {
            
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
            System.err.println("Error saat mengambil semua destinasi: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public DestinasiModel getDestinasiById(int id) {
        DestinasiModel d = null;
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getDestinasiById.");
            return null;
        }

        String sql = "SELECT * FROM destinasi WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql); // Gunakan try-with-resources
             ResultSet rs = ps.executeQuery()) {
            
            ps.setInt(1, id); // Set parameter setelah PreparedStatement dibuat
            
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
            System.err.println("Error saat mengambil destinasi by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return d;
    }

    public List<DestinasiModel> getDestinasiByPaketId(int paketId) {
        List<DestinasiModel> list = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getDestinasiByPaketId.");
            return list;
        }

        String sql = "SELECT d.*, rpp.durasi_jam, rpp.urutan_kunjungan FROM destinasi d "
                    + "JOIN rincian_paket_perjalanan rpp ON d.id = rpp.destinasi_id "
                    + "WHERE rpp.paket_perjalanan_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql); // Gunakan try-with-resources
             ResultSet rs = ps.executeQuery()) {
            
            ps.setInt(1, paketId); // Set parameter setelah PreparedStatement dibuat
            
            while (rs.next()) {
                DestinasiModel d = new DestinasiModel();
                d.setId(rs.getInt("id"));
                d.setKotaId(rs.getInt("kota_id"));
                d.setNamaDestinasi(rs.getString("nama_destinasi"));
                d.setDeskripsi(rs.getString("deskripsi"));
                d.setHarga(rs.getDouble("harga"));
                d.setGambar(rs.getString("gambar"));
                d.setDurasiJam(rs.getInt("durasi_jam"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil destinasi by Paket ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Anda bisa menambahkan metode lain seperti save, update, delete di sini jika diperlukan
}
