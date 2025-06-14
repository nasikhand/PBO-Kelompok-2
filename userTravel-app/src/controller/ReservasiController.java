package controller;

import db.Koneksi;
import db.dao.ReservasiDAO;
import db.dao.PenumpangDAO; // Import PenumpangDAO
import model.ReservasiModel;
import model.PenumpangModel; // Import PenumpangModel

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservasiController {
    private ReservasiDAO reservasiDAO;
    private PenumpangDAO penumpangDAO; // Deklarasikan PenumpangDAO

    public ReservasiController() {
        // Pastikan koneksi didapatkan dan DAO diinisialisasi di sini
        Connection conn = Koneksi.getConnection();
        if (conn == null) {
            System.err.println("ERROR: Koneksi database NULL di ReservasiController constructor.");
            // Handle error: mungkin throw exception atau set flag
        }
        this.reservasiDAO = new ReservasiDAO(conn);
        this.penumpangDAO = new PenumpangDAO(conn); // <--- Pastikan inisialisasi di sini
    }

    // Jika Anda memiliki konstruktor lain yang menerima Connection,
    // pastikan juga penumpangDAO diinisialisasi di sana.
    // public ReservasiController(Connection existingConn) {    
    //     this.reservasiDAO = new ReservasiDAO(existingConn);
    //     this.penumpangDAO = new PenumpangDAO(existingConn);
    // }


   public int buatReservasi(ReservasiModel reservasi) {
        String sql = "INSERT INTO reservasi (user_id, trip_type, trip_id, kode_reservasi, tanggal_reservasi, status) VALUES (?, ?, ?, ?, ?, ?)";
        // FIXED: Changed getKoneksi() to the standard getConnection()
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, reservasi.getUserId());
            ps.setString(2, reservasi.getTripType());
            ps.setInt(3, reservasi.getTripId());
            ps.setString(4, reservasi.getKodeReservasi());
            ps.setDate(5, java.sql.Date.valueOf(reservasi.getTanggalReservasi()));
            ps.setString(6, reservasi.getStatus());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println("SUCCESS: Reservation created with ID: " + generatedKeys.getInt(1));
                        return generatedKeys.getInt(1); // Return the new ID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQLException during reservation creation.");
            e.printStackTrace();
        }
        return -1; // Return -1 on failure
    }

    public boolean tambahPenumpang(int reservasiId, String namaPenumpang) {
        System.out.println("Attempting to save passenger: [reservasiId=" + reservasiId + ", namaPenumpang='" + namaPenumpang + "']");

        if (reservasiId <= 0 || namaPenumpang == null || namaPenumpang.trim().isEmpty()) {
            System.err.println("ERROR: Invalid data provided to tambahPenumpang. Cannot save.");
            return false;
        }

        String sql = "INSERT INTO penumpang (reservasi_id, nama_penumpang) VALUES (?, ?)";
        
        // FIXED: Changed getKoneksi() to the standard getConnection()
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservasiId);
            ps.setString(2, namaPenumpang);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("SUCCESS: Passenger '" + namaPenumpang + "' saved successfully for reservation ID " + reservasiId);
                return true;
            } else {
                System.err.println("ERROR: executeUpdate returned 0 rows affected. Passenger not saved.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("SQLException occurred while saving passenger '" + namaPenumpang + "'.");
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            
            return false;
        }
    }

    public boolean deleteReservasi(int reservasiId) {
        return reservasiDAO.deleteReservasi(reservasiId);
    }
}