/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class PaketController {

    public static DefaultTableModel getAllPaket() {
        String[] columnNames = {"ID", "Nama Paket", "Kota ID", "Tanggal Mulai", "Tanggal Akhir", "Kuota", "Harga", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String query = "SELECT * FROM paket_perjalanan";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("nama_paket"),
                        rs.getInt("kota_id"),
                        rs.getDate("tanggal_mulai"),
                        rs.getDate("tanggal_akhir"),
                        rs.getInt("kuota"),
                        rs.getDouble("harga"),
                        rs.getString("status")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static boolean insertPaket(String nama, int kotaId, String tglMulai, String tglAkhir, int kuota, double harga, String status) {
        String query = "INSERT INTO paket_perjalanan (nama_paket, kota_id, tanggal_mulai, tanggal_akhir, kuota, harga, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nama);
            stmt.setInt(2, kotaId);
            stmt.setDate(3, Date.valueOf(tglMulai));
            stmt.setDate(4, Date.valueOf(tglAkhir));
            stmt.setInt(5, kuota);
            stmt.setDouble(6, harga);
            stmt.setString(7, status);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deletePaket(int id) {
        String query = "DELETE FROM paket_perjalanan WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // (edit dan update status/kuota/harga menyusul setelah tampilan dasar)
}

