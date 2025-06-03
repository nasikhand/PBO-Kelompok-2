package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.CustomTripDetailModel;
import model.CustomTripModel;



public class CustomTripDAO {

    public CustomTripDAO(Connection conn) {
        //TODO Auto-generated constructor stub
    }

    public CustomTripModel getById(int customTripId) {
        CustomTripModel trip = new CustomTripModel();
        List<CustomTripDetailModel> details = new ArrayList<>();

        try (Connection conn = Koneksi.getConnection()) {
            // Ambil header trip
            String tripSql = "SELECT * FROM custom_trip WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(tripSql)) {
                stmt.setInt(1, customTripId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    trip.setId(rs.getInt("id"));
                    trip.setNamaTrip(rs.getString("nama_trip"));
                    trip.setTanggalMulai(rs.getDate("tanggal_mulai").toLocalDate());
                    trip.setTanggalAkhir(rs.getDate("tanggal_akhir").toLocalDate());
                }
            }

            // Ambil detail trip
            String detailSql = "SELECT * FROM rincian_custom_trip WHERE custom_trip_id = ? ORDER BY urutan_kunjungan ASC";
            try (PreparedStatement stmt = conn.prepareStatement(detailSql)) {
                stmt.setInt(1, customTripId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    CustomTripDetailModel detail = new CustomTripDetailModel();
                    detail.setId(rs.getInt("id"));
                    detail.setDestinasiId(rs.getInt("destinasi_id"));
                    detail.setTanggalKunjungan(rs.getDate("tanggal_kunjungan").toLocalDate());
                    detail.setDurasiJam(rs.getInt("durasi_jam"));
                    detail.setUrutanKunjungan(rs.getInt("urutan_kunjungan"));
                    detail.setHargaDestinasi(rs.getDouble("harga_destinasi"));
                    detail.setBiayaTransport(rs.getDouble("biaya_transport"));
                    details.add(detail);
                }
            }

            trip.setDetailList(details);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trip;
    }
}
