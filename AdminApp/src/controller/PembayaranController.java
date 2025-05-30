/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Pembayaran;
import javax.swing.JOptionPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembayaranController {

    public int getTotalPembayaranCount(String filterKode, String filterStatus) {
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT COUNT(*) FROM pembayaran p JOIN reservasi r ON p.reservasi_id = r.id ");
        List<Object> params = new ArrayList<>();
        boolean inWhereClause = false;

        if (filterKode != null && !filterKode.isEmpty()) {
            queryBuilder.append("WHERE r.kode_reservasi LIKE ? ");
            params.add("%" + filterKode + "%");
            inWhereClause = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty() && !"Semua".equalsIgnoreCase(filterStatus)) {
            queryBuilder.append(inWhereClause ? "AND " : "WHERE ");
            queryBuilder.append("p.status_pembayaran = ? ");
            params.add(filterStatus);
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            
            if (conn == null) return 0;
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Pembayaran> getPembayaranWithPagination(int halaman, int dataPerHalaman, String filterKode, String filterStatus) {
        List<Pembayaran> daftarPembayaran = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
            "SELECT p.id, p.reservasi_id, p.metode_pembayaran, p.jumlah_pembayaran, " +
            "p.tanggal_pembayaran, p.status_pembayaran, r.kode_reservasi " +
            "FROM pembayaran p JOIN reservasi r ON p.reservasi_id = r.id ");
        
        List<Object> params = new ArrayList<>();
        boolean inWhereClause = false;

        if (filterKode != null && !filterKode.isEmpty()) {
            queryBuilder.append("WHERE r.kode_reservasi LIKE ? ");
            params.add("%" + filterKode + "%");
            inWhereClause = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty() && !"Semua".equalsIgnoreCase(filterStatus)) {
            queryBuilder.append(inWhereClause ? "AND " : "WHERE ");
            queryBuilder.append("p.status_pembayaran = ? ");
            params.add(filterStatus);
        }
        
        queryBuilder.append("ORDER BY p.tanggal_pembayaran DESC, p.id DESC LIMIT ? OFFSET ?");
        params.add(dataPerHalaman);
        params.add((halaman - 1) * dataPerHalaman);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(queryBuilder.toString())) {
            
            if (conn == null) return daftarPembayaran;
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pembayaran bayar = new Pembayaran();
                    bayar.setId(rs.getInt("id"));
                    bayar.setReservasiId(rs.getInt("reservasi_id"));
                    bayar.setMetodePembayaran(rs.getString("metode_pembayaran"));
                    bayar.setJumlahPembayaran(rs.getBigDecimal("jumlah_pembayaran"));
                    bayar.setTanggalPembayaran(rs.getDate("tanggal_pembayaran"));
                    bayar.setStatusPembayaran(rs.getString("status_pembayaran"));
                    bayar.setKodeReservasi(rs.getString("kode_reservasi"));
                    daftarPembayaran.add(bayar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPembayaran;
    }
    
    public boolean updateStatusPembayaran(int idPembayaran, String statusBaru) {
        String queryPembayaran = "UPDATE pembayaran SET status_pembayaran = ? WHERE id = ?";
        String queryReservasi = "UPDATE reservasi SET status = ? WHERE id = (SELECT reservasi_id FROM pembayaran WHERE id = ?)";
        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPembayaran = conn.prepareStatement(queryPembayaran)) {
                stmtPembayaran.setString(1, statusBaru);
                stmtPembayaran.setInt(2, idPembayaran);
                stmtPembayaran.executeUpdate();
            }

            if ("lunas".equalsIgnoreCase(statusBaru)) {
                try (PreparedStatement stmtReservasi = conn.prepareStatement(queryReservasi)) {
                    stmtReservasi.setString(1, "dibayar");
                    stmtReservasi.setInt(2, idPembayaran);
                    stmtReservasi.executeUpdate();
                }
            }
            
            conn.commit();
            success = true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui status: " + e.getMessage(), "Kesalahan Transaksi", JOptionPane.ERROR_MESSAGE);
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); } }
        }
        return success;
    }
}
