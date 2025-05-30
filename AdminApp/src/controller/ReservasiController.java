/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Reservasi;
import model.Penumpang;
import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // Pastikan import java.sql.Date ada
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservasiController {
    
    /**
     * Menghitung total jumlah reservasi dengan filter (opsional).
     */
    public int getTotalReservasiCount(String filterKode, String filterStatus) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM reservasi r ");
        // Kita mungkin perlu JOIN jika filter melibatkan nama trip, dll.
        // Untuk contoh ini, filter sederhana berdasarkan kode dan status di tabel reservasi saja.
        List<Object> params = new ArrayList<>();
        boolean inWhereClause = false;

        if (filterKode != null && !filterKode.isEmpty()) {
            queryBuilder.append(inWhereClause ? "AND " : "WHERE ");
            queryBuilder.append("r.kode_reservasi LIKE ? ");
            params.add("%" + filterKode + "%");
            inWhereClause = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty() && !"Semua".equalsIgnoreCase(filterStatus)) {
            queryBuilder.append(inWhereClause ? "AND " : "WHERE ");
            queryBuilder.append("r.status = ? ");
            params.add(filterStatus);
            inWhereClause = true;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            
            if (conn == null) return 0;

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghitung total reservasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Mengambil data reservasi dengan paginasi dan filter.
     */
    public List<Reservasi> getReservasiWithPagination(int halaman, int dataPerHalaman, String filterKode, String filterStatus) {
        List<Reservasi> daftarReservasi = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT r.id, r.kode_reservasi, r.tanggal_reservasi, r.status, r.trip_type, r.trip_id, " +
            "CASE " +
            "    WHEN r.trip_type = 'paket_perjalanan' THEN pp.nama_paket " +
            "    WHEN r.trip_type = 'custom_trip' THEN ct.nama_trip " +
            "END AS nama_trip " +
            "FROM reservasi r " +
            "LEFT JOIN paket_perjalanan pp ON r.trip_id = pp.id AND r.trip_type = 'paket_perjalanan' " +
            "LEFT JOIN custom_trip ct ON r.trip_id = ct.id AND r.trip_type = 'custom_trip' "
        );
        
        List<Object> params = new ArrayList<>();
        boolean inWhereClause = false;

        if (filterKode != null && !filterKode.isEmpty()) {
            queryBuilder.append(inWhereClause ? "AND " : "WHERE ");
            queryBuilder.append("r.kode_reservasi LIKE ? ");
            params.add("%" + filterKode + "%");
            inWhereClause = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty() && !"Semua".equalsIgnoreCase(filterStatus)) {
            queryBuilder.append(inWhereClause ? "AND " : "WHERE ");
            queryBuilder.append("r.status = ? ");
            params.add(filterStatus);
            // inWhereClause = true; // Tidak perlu jika ini kondisi terakhir sebelum ORDER BY
        }
        
        queryBuilder.append("ORDER BY r.tanggal_reservasi DESC, r.id DESC LIMIT ? OFFSET ?");
        params.add(dataPerHalaman);
        params.add((halaman - 1) * dataPerHalaman);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {

            if (conn == null) return daftarReservasi;
            
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Reservasi reservasi = new Reservasi();
                    reservasi.setId(rs.getInt("id"));
                    reservasi.setKodeReservasi(rs.getString("kode_reservasi"));
                    reservasi.setTanggalReservasi(rs.getDate("tanggal_reservasi"));
                    reservasi.setStatus(rs.getString("status"));
                    reservasi.setTripType(rs.getString("trip_type"));
                    reservasi.setTripId(rs.getInt("trip_id"));
                    reservasi.setNamaTrip(rs.getString("nama_trip"));
                    daftarReservasi.add(reservasi);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data reservasi (paginasi): " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarReservasi;
    }

    public List<Reservasi> getAllReservasi() {
        List<Reservasi> daftarReservasi = new ArrayList<>();
        String query = "SELECT r.id, r.kode_reservasi, r.tanggal_reservasi, r.status, r.trip_type, r.trip_id, " +
                       "CASE " +
                       "    WHEN r.trip_type = 'paket_perjalanan' THEN pp.nama_paket " +
                       "    WHEN r.trip_type = 'custom_trip' THEN ct.nama_trip " +
                       "END AS nama_trip " +
                       "FROM reservasi r " +
                       "LEFT JOIN paket_perjalanan pp ON r.trip_id = pp.id AND r.trip_type = 'paket_perjalanan' " +
                       "LEFT JOIN custom_trip ct ON r.trip_id = ct.id AND r.trip_type = 'custom_trip' " +
                       "ORDER BY r.tanggal_reservasi DESC, r.id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (conn == null) return daftarReservasi;

            while (rs.next()) {
                Reservasi reservasi = new Reservasi();
                reservasi.setId(rs.getInt("id"));
                reservasi.setKodeReservasi(rs.getString("kode_reservasi"));
                reservasi.setTanggalReservasi(rs.getDate("tanggal_reservasi"));
                reservasi.setStatus(rs.getString("status"));
                reservasi.setTripType(rs.getString("trip_type"));
                reservasi.setTripId(rs.getInt("trip_id"));
                reservasi.setNamaTrip(rs.getString("nama_trip"));
                // Anda mungkin ingin mengambil nama pemesan jika ada user_id di custom_trip
                // dan menghubungkannya ke tabel user
                daftarReservasi.add(reservasi);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data reservasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarReservasi;
    }

    public boolean updateStatusReservasi(int idReservasi, String status) {
        String query = "UPDATE reservasi SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return false;

            stmt.setString(1, status);
            stmt.setInt(2, idReservasi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui status reservasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public List<Penumpang> getPenumpangByReservasiId(int idReservasi) {
        List<Penumpang> daftarPenumpang = new ArrayList<>();
        String query = "SELECT * FROM penumpang WHERE reservasi_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return daftarPenumpang;

            stmt.setInt(1, idReservasi);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Penumpang penumpang = new Penumpang();
                    penumpang.setId(rs.getInt("id"));
                    penumpang.setReservasiId(idReservasi);
                    penumpang.setNamaPenumpang(rs.getString("nama_penumpang"));
                    penumpang.setJenisKelamin(rs.getString("jenis_kelamin"));
                    penumpang.setTanggalLahir(rs.getDate("tanggal_lahir"));
                    penumpang.setNomorTelepon(rs.getString("nomor_telepon"));
                    penumpang.setEmail(rs.getString("email"));
                    daftarPenumpang.add(penumpang);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data penumpang: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarPenumpang;
    }

    public BigDecimal getTotalPendapatanLunas() {
        String query = "SELECT SUM(jumlah_pembayaran) AS total_pendapatan " +
                       "FROM pembayaran WHERE status_pembayaran = 'lunas'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return BigDecimal.ZERO;

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total_pendapatan");
                return (total == null) ? BigDecimal.ZERO : total;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil total pendapatan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public int getTotalReservasi() {
        String query = "SELECT COUNT(*) AS jumlah_reservasi FROM reservasi";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return 0;

            if (rs.next()) {
                return rs.getInt("jumlah_reservasi");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil total reservasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalReservasiByStatus(String status) {
        String query = "SELECT COUNT(*) AS jumlah_reservasi FROM reservasi WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return 0;

            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("jumlah_reservasi");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil total reservasi by status: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getLaporanPenjualanPerPaket(Date tanggalMulai, Date tanggalAkhir) {
        List<Object[]> laporan = new ArrayList<>();
        String query = "SELECT pp.nama_paket, COUNT(r.id) AS jumlah_reservasi, SUM(p.jumlah_pembayaran) AS total_pendapatan " +
                       "FROM reservasi r " +
                       "JOIN paket_perjalanan pp ON r.trip_id = pp.id AND r.trip_type = 'paket_perjalanan' " +
                       "JOIN pembayaran p ON r.id = p.reservasi_id " +
                       "WHERE p.status_pembayaran = 'lunas' ";

        List<Date> params = new ArrayList<>();
        if (tanggalMulai != null && tanggalAkhir != null) {
            query += "AND p.tanggal_pembayaran BETWEEN ? AND ? ";
            params.add(tanggalMulai);
            params.add(tanggalAkhir);
        }
        query += "GROUP BY pp.nama_paket ORDER BY total_pendapatan DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return laporan;

            int paramIndex = 1;
            for(Date param : params){
                stmt.setDate(paramIndex++, param);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    laporan.add(new Object[]{
                        rs.getString("nama_paket"),
                        rs.getInt("jumlah_reservasi"),
                        rs.getBigDecimal("total_pendapatan")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil laporan penjualan per paket: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return laporan;
    }

    public List<Object[]> getLaporanPenjualanPerJenisTrip(Date tanggalMulai, Date tanggalAkhir) {
        List<Object[]> laporan = new ArrayList<>();
        String query = "SELECT r.trip_type, COUNT(r.id) AS jumlah_reservasi, SUM(p.jumlah_pembayaran) AS total_pendapatan " +
                       "FROM reservasi r " +
                       "JOIN pembayaran p ON r.id = p.reservasi_id " +
                       "WHERE p.status_pembayaran = 'lunas' ";

        List<Date> params = new ArrayList<>();
        if (tanggalMulai != null && tanggalAkhir != null) {
            query += "AND p.tanggal_pembayaran BETWEEN ? AND ? ";
            params.add(tanggalMulai);
            params.add(tanggalAkhir);
        }
        query += "GROUP BY r.trip_type ORDER BY total_pendapatan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return laporan;
            
            int paramIndex = 1;
            for(Date param : params){
                stmt.setDate(paramIndex++, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    laporan.add(new Object[]{
                        rs.getString("trip_type"),
                        rs.getInt("jumlah_reservasi"),
                        rs.getBigDecimal("total_pendapatan")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil laporan penjualan per jenis trip: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return laporan;
    }
    
    public Map<String, Integer> getJumlahReservasiPerStatus() {
        Map<String, Integer> dataStatus = new HashMap<>();
        String query = "SELECT status, COUNT(*) AS jumlah FROM reservasi GROUP BY status";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return dataStatus;

            while (rs.next()) {
                dataStatus.put(rs.getString("status"), rs.getInt("jumlah"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data status reservasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return dataStatus;
    }
}