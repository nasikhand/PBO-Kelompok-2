package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.PembayaranModel;

/**
 * DAO (Data Access Object) untuk menangani semua operasi database
 * yang terkait dengan tabel 'pembayaran'.
 */
public class PembayaranDAO {
    private Connection conn;

    /**
     * Konstruktor yang menerima koneksi database.
     * @param conn Koneksi ke database.
     */
    public PembayaranDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Mengambil total jumlah pembayaran yang lunas untuk reservasi tertentu.
     * @param reservasiId ID dari reservasi.
     * @return Total jumlah pembayaran yang lunas, atau null jika tidak ada atau terjadi error.
     */
    public Double getJumlahPembayaranByReservasiId(int reservasiId) {
        Double jumlahPembayaran = null;
        if (this.conn == null) {
            System.err.println("DAO Error: Tidak ada koneksi database untuk getJumlahPembayaranByReservasiId.");
            return null;
        }
        
        String sql = "SELECT SUM(jumlah_pembayaran) AS total_lunas FROM pembayaran WHERE reservasi_id = ? AND status_pembayaran = 'lunas'";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble("total_lunas");
                    if (!rs.wasNull()) { // Cek apakah nilai dari DB bukan NULL
                        jumlahPembayaran = total;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ DAO Error: Gagal saat mengambil jumlah pembayaran: " + e.getMessage());
            e.printStackTrace();
        }
        return jumlahPembayaran;
    }

    /**
     * Memasukkan data pembayaran baru ke dalam database.
     * Versi ini mencakup LOGGING DETAIL untuk membantu menemukan bug.
     * @param pembayaran Objek PembayaranModel yang akan disimpan.
     * @return true jika pembayaran berhasil disimpan, false jika gagal.
     */
    public boolean insertPembayaran(PembayaranModel pembayaran) {
        if (this.conn == null) {
            System.err.println("DAO Error: Koneksi database null. Tidak dapat menyimpan pembayaran.");
            return false;
        }
        
        // --- DEBUGGING: Cetak data yang akan disimpan ---
        System.out.println("DEBUG: Mencoba menyimpan pembayaran...");
        System.out.println("  -> Reservasi ID: " + pembayaran.getReservasiId());
        System.out.println("  -> Metode: " + pembayaran.getMetodePembayaran());
        System.out.println("  -> Jumlah: " + pembayaran.getJumlahPembayaran()); // Pastikan nominal ini benar
        System.out.println("  -> Tanggal: " + pembayaran.getTanggalPembayaran());
        System.out.println("  -> Status: " + pembayaran.getStatusPembayaran());

        String sql = "INSERT INTO pembayaran (reservasi_id, metode_pembayaran, jumlah_pembayaran, tanggal_pembayaran, status_pembayaran) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pembayaran.getReservasiId());
            ps.setString(2, pembayaran.getMetodePembayaran());
            ps.setDouble(3, pembayaran.getJumlahPembayaran());
            ps.setDate(4, new java.sql.Date(pembayaran.getTanggalPembayaran().getTime()));
            ps.setString(5, pembayaran.getStatusPembayaran());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                System.out.println("✅ SUCCESS: Pembayaran berhasil disimpan ke database.");
                return true;
            } else {
                System.err.println("❌ DAO Error: executeUpdate mengembalikan 0 baris terpengaruh. Pembayaran tidak disimpan.");
                return false;
            }
            
        } catch (SQLException e) {
            // --- BLOK PENANGKAPAN ERROR YANG DITINGKATKAN ---
            System.err.println("❌ DAO Error: Terjadi SQLException saat menyimpan pembayaran.");
            System.err.println("  -> Pesan Error: " + e.getMessage());
            System.err.println("  -> SQL State: " + e.getSQLState());
            System.err.println("  -> Error Code: " + e.getErrorCode());
            e.printStackTrace(); // Cetak jejak lengkap error untuk analisis mendalam
        }
        return false;
    }
}
