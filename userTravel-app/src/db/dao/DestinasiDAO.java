package db.dao;

import model.DestinasiModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import db.Koneksi;


public class DestinasiDAO {

    private Connection conn;

    public DestinasiDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh DestinasiDAO.");
        }
    }

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
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // Use try-with-resources correctly
            
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { // Execute query inside try-with-resources
                if (rs.next()) {
                    d = new DestinasiModel();
                    d.setId(rs.getInt("id"));
                    d.setKotaId(rs.getInt("kota_id"));
                    d.setNamaDestinasi(rs.getString("nama_destinasi"));
                    d.setDeskripsi(rs.getString("deskripsi"));
                    d.setHarga(rs.getDouble("harga"));
                    d.setGambar(rs.getString("gambar"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil destinasi by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return d;
    }

    /**
     * Mengambil objek DestinasiModel berdasarkan nama destinasi.
     * @param namaDestinasi Nama destinasi yang dicari.
     * @return DestinasiModel jika ditemukan, null jika tidak.
     */
    public DestinasiModel getDestinasiByName(String namaDestinasi) {
        DestinasiModel d = null;
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getDestinasiByName.");
            return null;
        }

        String sql = "SELECT * FROM destinasi WHERE nama_destinasi = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, namaDestinasi);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new DestinasiModel();
                    d.setId(rs.getInt("id"));
                    d.setKotaId(rs.getInt("kota_id"));
                    d.setNamaDestinasi(rs.getString("nama_destinasi"));
                    d.setDeskripsi(rs.getString("deskripsi"));
                    d.setHarga(rs.getDouble("harga"));
                    d.setGambar(rs.getString("gambar"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil destinasi by Name: " + e.getMessage());
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
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paketId);
            try (ResultSet rs = ps.executeQuery()) {
            
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
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil destinasi by Paket ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
