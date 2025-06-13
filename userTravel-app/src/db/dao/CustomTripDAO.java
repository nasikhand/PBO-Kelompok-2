package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate; // Tambahkan import ini jika diperlukan untuk tanggal
import java.util.ArrayList;
import java.util.List;
import model.CustomTripDetailModel;
import model.CustomTripModel;


public class CustomTripDAO {

    private Connection conn; // <--- DEKLARASI INI DITAMBAHKAN

    // Konstruktor default yang mendapatkan koneksi dari Koneksi.getConnection()
    public CustomTripDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh CustomTripDAO.");
        }
    }

    // Konstruktor yang menerima objek Connection (seperti yang sudah ada)
    public CustomTripDAO(Connection conn) {
        this.conn = conn; // <--- INISIALISASI INI DITAMBAHKAN
        if (this.conn == null) {
            System.err.println("Koneksi yang diberikan ke CustomTripDAO adalah NULL.");
        }
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
                    customTrip.setUserId(rs.getInt("user_id")); // Pastikan field ini ada di CustomTripModel
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
                    
                    customTrip.setJumlahPeserta(rs.getInt("jumlah_peserta")); // Pastikan field ini ada di CustomTripModel
                    customTrip.setStatus(rs.getString("status")); // Pastikan field ini ada di CustomTripModel
                    customTrip.setTotalHarga(rs.getDouble("total_harga")); // Pastikan field ini ada di CustomTripModel
                    customTrip.setCatatanUser(rs.getString("catatan_user")); // Pastikan field ini ada di CustomTripModel

                    // Karena tabel custom_trip tidak punya kota_id, kita bisa set namaKota
                    // dengan nama_trip atau string default jika tidak ada nama kota spesifik.
                    customTrip.setNamaKota(customTrip.getNamaTrip()); // Set nama kota di model dari nama_trip

                    // Jumlah hari akan dihitung oleh CustomTripModel.getJumlahHari()
                    // customTrip.setJumlahHari((int) customTrip.getJumlahHari()); // Tidak perlu ini jika getter menghitungnya

                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil custom trip dengan ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return customTrip;
    }
}
