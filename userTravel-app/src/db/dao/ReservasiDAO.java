package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.List;
import java.util.ArrayList;

import model.ReservasiModel;
import model.CustomTripModel;
import model.PaketPerjalananModel;

public class ReservasiDAO {
    private Connection conn;

    public ReservasiDAO(Connection conn) {
        this.conn = conn;
    }

    public List<ReservasiModel> getHistoryReservasiByUser(int userId) {
        List<ReservasiModel> list = new ArrayList<>();
        String sql = "SELECT * FROM reservasi WHERE status = 'selesai' AND user_id = ? " +
                    "AND ((trip_type = 'paket_perjalanan' AND trip_id IN (SELECT id FROM paket_perjalanan)) " +
                    "OR (trip_type = 'custom_trip' AND trip_id IN (SELECT id FROM custom_trip)))";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReservasiModel r = new ReservasiModel();
                r.setId(rs.getInt("id"));
                r.setTripType(rs.getString("trip_type"));
                r.setTripId(rs.getInt("trip_id"));
                r.setKodeReservasi(rs.getString("kode_reservasi"));

                java.sql.Date sqlDate = rs.getDate("tanggal_reservasi");
                if (sqlDate != null) {
                    r.setTanggalReservasi(sqlDate.toLocalDate());
                }

                r.setStatus(rs.getString("status"));

                if (r.getTripType().equals("paket_perjalanan")) {
                    r.setPaket(getPaketById(r.getTripId()));
                } else {
                    r.setCustomTrip(getCustomTripById(r.getTripId()));
                }

                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private PaketPerjalananModel getPaketById(int id) {
        PaketPerjalananDAO dao = new PaketPerjalananDAO(conn);
        return dao.getById(id);
    }

    private CustomTripModel getCustomTripById(int id) {
        CustomTripDAO dao = new CustomTripDAO();
        return dao.getById(id);
    }
}