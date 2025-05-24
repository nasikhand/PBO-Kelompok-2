/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LaporanController {

    public static DefaultTableModel getLaporanPemesananBulanan() {
        String[] columnNames = {"Bulan", "Jumlah Pemesanan", "Total Pendapatan"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String query = "SELECT DATE_FORMAT(tanggal_reservasi, '%Y-%m') AS bulan, " +
                       "COUNT(*) AS jumlah_pemesanan, " +
                       "SUM(jumlah_pembayaran) AS total_pendapatan " +
                       "FROM reservasi r JOIN pembayaran p ON r.id = p.reservasi_id " +
                       "WHERE p.status_pembayaran = 'lunas' " +
                       "GROUP BY bulan ORDER BY bulan DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getString("bulan"),
                        rs.getInt("jumlah_pemesanan"),
                        rs.getDouble("total_pendapatan")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static int getTotalPemesanan() {
        String query = "SELECT COUNT(*) FROM reservasi";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    public static double getTotalPendapatan() {
        String query = "SELECT SUM(jumlah_pembayaran) FROM pembayaran WHERE status_pembayaran = 'lunas'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (SQLException e) {
            return 0.0;
        }
    }
}

