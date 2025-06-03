package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.CustomTripModel;
import model.PaketPerjalananModel;
import model.ReservasiModel; 

public class ReservasiDAO {
    private Connection conn;

    public ReservasiDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh ReservasiDAO.");
        }
    }

    public ReservasiDAO(Connection conn) {
        this.conn = conn;
    }

    public int save(ReservasiModel reservasi) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi save Reservasi.");
            return -1;
        }
        // Pastikan kolom user_id ada di tabel reservasi Anda
        String sql = "INSERT INTO reservasi (user_id, trip_type, trip_id, kode_reservasi, tanggal_reservasi, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reservasi.getUserId());
            ps.setString(2, reservasi.getTripType());
            ps.setInt(3, reservasi.getTripId());
            ps.setString(4, reservasi.getKodeReservasi());
            if (reservasi.getTanggalReservasi() != null) {
                ps.setDate(5, Date.valueOf(reservasi.getTanggalReservasi()));
            } else {
                ps.setDate(5, Date.valueOf(LocalDate.now())); 
            }
            ps.setString(6, reservasi.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); 
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan reservasi: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public List<ReservasiModel> getHistoryReservasiByUser(int userId) {
        List<ReservasiModel> list = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getHistoryReservasiByUser.");
            return list;
        }
        // Query disesuaikan untuk menyertakan user_id dan memastikan trip_id valid
        String sql = "SELECT r.id, r.user_id, r.trip_type, r.trip_id, r.kode_reservasi, r.tanggal_reservasi, r.status " +
                     "FROM reservasi r " +
                     "LEFT JOIN paket_perjalanan pp ON r.trip_type = 'paket_perjalanan' AND r.trip_id = pp.id " +
                     "LEFT JOIN custom_trip ct ON r.trip_type = 'custom_trip' AND r.trip_id = ct.id " +
                     "WHERE r.user_id = ? AND r.status = 'selesai' " + 
                     "AND (pp.id IS NOT NULL OR ct.id IS NOT NULL)"; 

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ReservasiModel r = new ReservasiModel();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id")); 
                r.setTripType(rs.getString("trip_type"));
                r.setTripId(rs.getInt("trip_id"));
                r.setKodeReservasi(rs.getString("kode_reservasi"));

                java.sql.Date sqlDate = rs.getDate("tanggal_reservasi");
                if (sqlDate != null) {
                    r.setTanggalReservasi(sqlDate.toLocalDate());
                }
                r.setStatus(rs.getString("status"));

                if ("paket_perjalanan".equals(r.getTripType())) {
                    r.setPaket(getPaketById(r.getTripId()));
                } else if ("custom_trip".equals(r.getTripType())) {
                    r.setCustomTrip(getCustomTripById(r.getTripId()));
                }
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil riwayat reservasi: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    private PaketPerjalananModel getPaketById(int id) {
        if (this.conn == null) return null;
        PaketPerjalananDAO dao = new PaketPerjalananDAO(conn); 
        return dao.getById(id);
    }

    private CustomTripModel getCustomTripById(int id) {
        if (this.conn == null) return null;
        CustomTripDAO dao = new CustomTripDAO(conn); 
        return dao.getById(id);
    }

    public ReservasiModel getReservasiById(int reservasiId) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getReservasiById.");
            return null;
        }
        String sql = "SELECT * FROM reservasi WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ReservasiModel r = new ReservasiModel();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setTripType(rs.getString("trip_type"));
                r.setTripId(rs.getInt("trip_id"));
                r.setKodeReservasi(rs.getString("kode_reservasi"));
                java.sql.Date sqlDate = rs.getDate("tanggal_reservasi");
                if (sqlDate != null) {
                    r.setTanggalReservasi(sqlDate.toLocalDate());
                }
                r.setStatus(rs.getString("status"));
                
                // Ambil detail paket atau custom trip terkait
                if ("paket_perjalanan".equals(r.getTripType())) {
                    r.setPaket(getPaketById(r.getTripId()));
                } else if ("custom_trip".equals(r.getTripType())) {
                    r.setCustomTrip(getCustomTripById(r.getTripId()));
                }
                return r;
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil reservasi by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStatusReservasi(int reservasiId, String newStatus) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk updateStatusReservasi.");
            return false;
        }
        String sql = "UPDATE reservasi SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, reservasiId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate status reservasi: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<ReservasiModel> getReservasiAktifDenganTrip(int userId) throws SQLException {
        List<ReservasiModel> list = new ArrayList<>();

        String sql = "SELECT r.id, r.user_id, r.trip_type, r.trip_id, " +
                    "p.id AS paket_id, p.rating AS paket_rating, k1.nama_kota AS paket_nama_kota, " +
                    "c.id AS custom_id, c.kota_id AS custom_kota_id, k2.nama_kota AS custom_nama_kota " +
                    "FROM reservasi r " +
                    "LEFT JOIN paket_perjalanan p ON r.trip_type = 'paket_perjalanan' AND r.trip_id = p.id " +
                    "LEFT JOIN kota k1 ON p.kota_id = k1.id " +
                    "LEFT JOIN custom_trip c ON r.trip_type = 'custom_trip' AND r.trip_id = c.id " +
                    "LEFT JOIN kota k2 ON c.kota_id = k2.id " +
                    "WHERE r.user_id = ? AND r.status IN ('pending', 'dibayar', 'selesai')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservasiModel reservasi = new ReservasiModel();
                    reservasi.setId(rs.getInt("id"));
                    reservasi.setUserId(rs.getInt("user_id"));
                    String tripType = rs.getString("trip_type");
                    reservasi.setTripType(tripType);

                    PaketPerjalananModel paket = new PaketPerjalananModel();

                    if ("paket_perjalanan".equals(tripType)) {
                        paket.setId(rs.getInt("paket_id"));
                        paket.setNamaKota(rs.getString("paket_nama_kota"));
                        paket.setRating(rs.getDouble("paket_rating"));
                    } else if ("custom_trip".equals(tripType)) {
                        paket.setId(rs.getInt("custom_id"));
                        paket.setNamaKota(rs.getString("custom_nama_kota"));
                        paket.setRating(0.0); // Atau sesuai kebutuhan
                    }

                    reservasi.setPaket(paket);

                    list.add(reservasi);
                }
            }
        }

        return list;
    }

}
