package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.PenumpangModel;

public class PenumpangDAO {
    private Connection conn;

    public PenumpangDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh PenumpangDAO.");
        }
    }

    public PenumpangDAO(Connection conn) {
        this.conn = conn;
        if (this.conn == null) {
            System.err.println("Koneksi yang diberikan ke PenumpangDAO adalah NULL.");
        }
    }

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
        System.out.println("DEBUG PenumpangDAO - getJumlahPenumpangByReservasiId(" + reservasiId + ") returns: " + count);
        return count;
    }

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
        System.out.println("DEBUG PenumpangDAO - getNamaPenumpangByReservasiId(" + reservasiId + ") returns: " + penumpangNames);
        return penumpangNames;
    }

    public boolean tambahPenumpang(int reservasiId, String namaPenumpang) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk tambahPenumpang.");
            return false;
        }
        String sql = "INSERT INTO penumpang (reservasi_id, nama_penumpang) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ps.setString(2, namaPenumpang);
            int rowsAffected = ps.executeUpdate();
            System.out.println("DEBUG PenumpangDAO - tambahPenumpang(" + reservasiId + ", " + namaPenumpang + "): Rows Affected = " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menambahkan penumpang: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPenumpang(PenumpangModel penumpang) {
        String sql = "INSERT INTO penumpang (reservasi_id, nama_penumpang, jenis_kelamin, tanggal_lahir, nomor_telepon, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, penumpang.getReservasiId());
            ps.setString(2, penumpang.getNamaPenumpang());
            ps.setString(3, penumpang.getJenisKelamin());
            ps.setDate(4, (Date) penumpang.getTanggalLahir());
            ps.setString(5, penumpang.getNomorTelepon());
            ps.setString(6, penumpang.getEmail()); 

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
