package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 
import java.util.ArrayList;
import java.util.List;
import model.KotaModel;

public class KotaDAO {
    private Connection conn;

    public KotaDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh KotaDAO.");
        }
    }

    public KotaDAO(Connection conn) {
        this.conn = conn;
    }

    public String getNamaKotaById(int kotaId) {
        String nama = "";
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getNamaKotaById.");
            return nama;
        }
        String sql = "SELECT nama_kota FROM kota WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, kotaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nama = rs.getString("nama_kota");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama kota by ID: " + e.getMessage());
            e.printStackTrace(); 
        }
        return nama;
    }

    public List<KotaModel> getAllKota() {
        List<KotaModel> daftarKota = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getAllKota.");
            return daftarKota; 
        }
        String query = "SELECT id, nama_kota, provinsi FROM kota ORDER BY nama_kota ASC"; 

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                KotaModel kota = new KotaModel(
                        rs.getInt("id"),
                        rs.getString("nama_kota")
                );
                daftarKota.add(kota);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua data kota: " + e.getMessage());
            e.printStackTrace(); 
        }
        return daftarKota;
    }

    /**
     * Mengambil ID kota berdasarkan nama kota.
     * @param namaKota Nama kota yang dicari.
     * @return ID kota, atau -1 jika tidak ditemukan atau error.
     */
    public int getKotaIdByNama(String namaKota) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getKotaIdByNama.");
            return -1;
        }
        String sql = "SELECT id FROM kota WHERE nama_kota LIKE ?"; // Menggunakan LIKE untuk pencarian fleksibel
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + namaKota + "%"); // Cari nama kota yang mengandung string
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari kota_id by nama: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Tidak ditemukan
    }
}
