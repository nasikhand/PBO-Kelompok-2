/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.CustomTrip;
import model.RincianCustomTrip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CustomTripController {

    public int getTotalCustomTripCount(String filterEmail, String filterStatus) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM custom_trip ct JOIN user u ON ct.user_id = u.id ");
        List<String> params = new ArrayList<>();
        boolean inWhere = false;

        if (filterEmail != null && !filterEmail.isEmpty()) {
            query.append("WHERE u.email LIKE ? ");
            params.add("%" + filterEmail + "%");
            inWhere = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty()) {
            query.append(inWhere ? "AND " : "WHERE ");
            query.append("ct.status = ?");
            params.add(filterStatus);
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            if (conn == null) return 0;
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<CustomTrip> getCustomTripsWithPagination(int halaman, int dataPerHalaman, String filterEmail, String filterStatus) {
        List<CustomTrip> daftarTrip = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT ct.*, u.nama_lengkap FROM custom_trip ct JOIN user u ON ct.user_id = u.id ");
        List<Object> params = new ArrayList<>();
        boolean inWhere = false;

        if (filterEmail != null && !filterEmail.isEmpty()) {
            query.append("WHERE u.email LIKE ? ");
            params.add("%" + filterEmail + "%");
            inWhere = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty()) {
            query.append(inWhere ? "AND " : "WHERE ");
            query.append("ct.status = ? ");
            params.add(filterStatus);
        }
        query.append("ORDER BY ct.id DESC LIMIT ? OFFSET ?");
        params.add(dataPerHalaman);
        params.add((halaman - 1) * dataPerHalaman);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            if (conn == null) return daftarTrip;
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CustomTrip trip = new CustomTrip();
                    trip.setId(rs.getInt("id"));
                    trip.setNamaTrip(rs.getString("nama_trip"));
                    trip.setNamaPemesan(rs.getString("nama_lengkap"));
                    trip.setTanggalMulai(rs.getDate("tanggal_mulai"));
                    trip.setStatus(rs.getString("status"));
                    trip.setTotalHarga(rs.getBigDecimal("total_harga"));
                    trip.setCatatanUser(rs.getString("catatan_user"));
                    daftarTrip.add(trip);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarTrip;
    }
    
    public List<RincianCustomTrip> getRincianByCustomTripId(int customTripId) {
        List<RincianCustomTrip> daftarRincian = new ArrayList<>();
        String query = "SELECT rct.*, d.nama_destinasi FROM rincian_custom_trip rct " +
                       "JOIN destinasi d ON rct.destinasi_id = d.id " +
                       "WHERE rct.custom_trip_id = ? ORDER BY rct.urutan_kunjungan";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return daftarRincian;
            stmt.setInt(1, customTripId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RincianCustomTrip rincian = new RincianCustomTrip();
                    rincian.setId(rs.getInt("id"));
                    rincian.setNamaDestinasi(rs.getString("nama_destinasi"));
                    rincian.setTanggalKunjungan(rs.getDate("tanggal_kunjungan"));
                    daftarRincian.add(rincian);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mengambil rincian custom trip: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
        }
        return daftarRincian;
    }
}
