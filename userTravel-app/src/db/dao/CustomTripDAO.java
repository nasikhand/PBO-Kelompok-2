package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Import ini untuk Statement.RETURN_GENERATED_KEYS
import java.time.LocalDate;
import java.sql.Date; // Import ini untuk java.sql.Date.valueOf()
import java.util.ArrayList;
import java.util.List;
import model.CustomTripDetailModel;
import model.CustomTripModel;


public class CustomTripDAO {

    private Connection conn;

    public CustomTripDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh CustomTripDAO.");
        }
    }

    public CustomTripDAO(Connection conn) {
        this.conn = conn;
        if (this.conn == null) {
            System.err.println("Koneksi yang diberikan ke CustomTripDAO adalah NULL.");
        }
    }

    /**
     * Menyimpan objek CustomTripModel baru ke database.
     * @param customTrip Objek CustomTripModel yang akan disimpan.
     * @return ID dari custom trip yang baru dibuat, atau -1 jika gagal.
     */
    public int save(CustomTripModel customTrip) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi save CustomTrip.");
            return -1;
        }
        String sql = "INSERT INTO custom_trip (user_id, nama_trip, tanggal_mulai, tanggal_akhir, jumlah_peserta, status, total_harga, catatan_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customTrip.getUserId());
            ps.setString(2, customTrip.getNamaTrip());
            
            // Konversi LocalDate ke java.sql.Date
            if (customTrip.getTanggalMulai() != null) {
                ps.setDate(3, Date.valueOf(customTrip.getTanggalMulai()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            if (customTrip.getTanggalAkhir() != null) {
                ps.setDate(4, Date.valueOf(customTrip.getTanggalAkhir()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            
            ps.setInt(5, customTrip.getJumlahPeserta());
            ps.setString(6, customTrip.getStatus());
            ps.setDouble(7, customTrip.getTotalHarga());
            ps.setString(8, customTrip.getCatatanUser());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idBaru = generatedKeys.getInt(1);
                        System.out.println("✅ Custom Trip berhasil disimpan dengan ID: " + idBaru);
                        return idBaru;
                    }
                }
            } else {
                System.err.println("❌ Tidak ada baris yang disisipkan ke database untuk Custom Trip.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error saat menyimpan Custom Trip: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public CustomTripModel getById(int id) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi getById CustomTrip.");
            return null;
        }

        CustomTripModel customTrip = null;
        String sql = "SELECT ct.* FROM custom_trip ct WHERE ct.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customTrip = new CustomTripModel();
                    customTrip.setId(rs.getInt("id"));
                    customTrip.setUserId(rs.getInt("user_id"));
                    customTrip.setNamaTrip(rs.getString("nama_trip"));

                    java.sql.Date sqlTanggalMulai = rs.getDate("tanggal_mulai");
                    if (sqlTanggalMulai != null) {
                        customTrip.setTanggalMulai(sqlTanggalMulai.toLocalDate());
                    } else {
                        customTrip.setTanggalMulai(null);
                    }

                    java.sql.Date sqlTanggalAkhir = rs.getDate("tanggal_akhir");
                    if (sqlTanggalAkhir != null) {
                        customTrip.setTanggalAkhir(sqlTanggalAkhir.toLocalDate());
                    } else {
                        customTrip.setTanggalAkhir(null);
                    }
                    
                    customTrip.setJumlahPeserta(rs.getInt("jumlah_peserta"));
                    customTrip.setStatus(rs.getString("status"));
                    customTrip.setTotalHarga(rs.getDouble("total_harga"));
                    customTrip.setCatatanUser(rs.getString("catatan_user"));

                    customTrip.setNamaKota(customTrip.getNamaTrip());

                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil custom trip dengan ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return customTrip;
    }

    public boolean deleteCustomTrip(int id) {
        // Implementasi DELETE FROM custom_trip WHERE id = ?
        // Hati-hati dengan foreign key jika ada di rincian_custom_trip
        // Anda mungkin perlu menghapus rinciannya dulu
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi delete CustomTrip.");
            return false;
        }
        String sql = "DELETE FROM custom_trip WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus Custom Trip: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
