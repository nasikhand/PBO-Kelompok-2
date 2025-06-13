package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate; // Import untuk LocalDate jika PenumpangModel menggunakannya
import java.util.ArrayList;
import java.util.List;

import model.PenumpangModel; // Pastikan model ini sudah ada

public class PenumpangDAO {
    private Connection conn;

    // Konstruktor default: mendapatkan koneksi dari Koneksi.getConnection()
    public PenumpangDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh PenumpangDAO.");
        }
    }

    // Konstruktor yang menerima objek Connection
    public PenumpangDAO(Connection conn) {
        this.conn = conn;
        if (this.conn == null) {
            System.err.println("Koneksi yang diberikan ke PenumpangDAO adalah NULL.");
        }
    }

    /**
     * Mengambil jumlah penumpang untuk reservasi tertentu.
     * @param reservasiId ID reservasi.
     * @return Jumlah penumpang, atau 0 jika tidak ada atau terjadi error.
     */
    public int getJumlahPenumpangByReservasiId(int reservasiId) {
        int count = 0;
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getJumlahPenumpangByReservasiId.");
            return 0;
        }
        String sql = "SELECT COUNT(*) AS total FROM penumpang WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil jumlah penumpang: " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Mengambil daftar nama penumpang untuk reservasi tertentu.
     * @param reservasiId ID reservasi.
     * @return List String nama penumpang.
     */
    public List<String> getNamaPenumpangByReservasiId(int reservasiId) {
        List<String> penumpangNames = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getNamaPenumpangByReservasiId.");
            return penumpangNames;
        }
        String sql = "SELECT nama_penumpang FROM penumpang WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    penumpangNames.add(rs.getString("nama_penumpang"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil nama penumpang: " + e.getMessage());
            e.printStackTrace();
        }
        return penumpangNames;
    }

    /**
     * Menambahkan penumpang baru ke reservasi.
     * Metode ini bernama `tambahPenumpang` untuk konsistensi dengan Controller/BookingScreen.
     * @param reservasiId ID reservasi terkait.
     * @param namaPenumpang Nama lengkap penumpang.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean tambahPenumpang(int reservasiId, String namaPenumpang) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk tambahPenumpang.");
            return false;
        }
        // Menggunakan SQL dasar karena BookingScreen hanya memberikan nama penumpang
        String sql = "INSERT INTO penumpang (reservasi_id, nama_penumpang) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ps.setString(2, namaPenumpang);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan penumpang: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Metode insert penumpang lengkap menggunakan PenumpangModel.
     * Anda bisa menggunakan ini jika Anda mengumpulkan semua detail penumpang.
     * @param penumpang Objek PenumpangModel yang akan diinsert.
     * @return true jika berhasil, false jika gagal.
     */
    public boolean insertPenumpang(PenumpangModel penumpang) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk insertPenumpang (model).");
            return false;
        }
        String sql = "INSERT INTO penumpang (reservasi_id, nama_penumpang, jenis_kelamin, tanggal_lahir, nomor_telepon, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, penumpang.getReservasiId());
            ps.setString(2, penumpang.getNamaPenumpang());
            ps.setString(3, penumpang.getJenisKelamin());

            // Konversi java.util.Date ke java.sql.Date
            if (penumpang.getTanggalLahir() != null) {
                ps.setDate(4, new java.sql.Date(penumpang.getTanggalLahir().getTime())); // <--- Perbaikan di sini
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            
            ps.setString(5, penumpang.getNomorTelepon());
            ps.setString(6, penumpang.getEmail());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saat insert penumpang (model): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
