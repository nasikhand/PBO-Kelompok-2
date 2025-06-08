package db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.PenumpangModel;

public class PenumpangDAO {
    private Connection conn;

    public PenumpangDAO(Connection conn) {
        this.conn = conn;
    }

    public int getJumlahPenumpangByReservasiId(int reservasiId) {
        int jumlah = 0;
        String sql = "SELECT COUNT(*) AS total FROM penumpang WHERE reservasi_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                jumlah = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jumlah;
    }

    // di PenumpangDAO
    public boolean insertPenumpang(PenumpangModel penumpang) {
        String sql = "INSERT INTO penumpang (reservasi_id, nama_penumpang, jenis_kelamin, tanggal_lahir, nomor_telepon, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, penumpang.getReservasiId());
            ps.setString(2, penumpang.getNamaPenumpang());
            ps.setString(3, penumpang.getJenisKelamin());

            if (penumpang.getTanggalLahir() != null) {
                ps.setDate(4, new java.sql.Date(penumpang.getTanggalLahir().getTime()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }
            
            ps.setString(5, penumpang.getNomorTelepon());
            ps.setString(6, penumpang.getEmail());
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
