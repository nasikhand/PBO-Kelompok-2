package db.dao;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ReservasiModel;   // Pastikan package ini sudah benar

public class ReservasiDAO {

    private Connection connection;

    // Constructor yang menerima Connection
    public ReservasiDAO(Connection connection) {
        this.connection = connection;
    }

    public List<ReservasiModel> getReservasiSebelumnya(int userId) {
        List<ReservasiModel> list = new ArrayList<>();
        String sql = "SELECT * FROM reservasi WHERE user_id = ? AND tanggal_akhir < CURRENT_DATE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReservasiModel r = new ReservasiModel();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setNamaTrip(rs.getString("nama_trip"));
                r.setTanggalMulai(rs.getDate("tanggal_mulai").toLocalDate());
                r.setTanggalAkhir(rs.getDate("tanggal_akhir").toLocalDate());
                r.setJumlahPeserta(rs.getInt("jumlah_peserta"));
                r.setStatus(rs.getString("status"));
                r.setTotalHarga(rs.getDouble("total_harga"));
                r.setCatatanUser(rs.getString("catatan_user"));
                
                // Contoh pengisian manual
                r.setTripType("custom_trip"); // bisa disesuaikan logika jenis trip
                r.setTripId(rs.getInt("id")); // contoh: pakai id reservasi sebagai tripId

                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
