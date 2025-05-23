/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class DestinasiController {

    public static DefaultTableModel getAllDestinasi() {
        String[] columnNames = {"ID", "Kota ID", "Nama", "Deskripsi", "Harga", "Gambar"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String query = "SELECT * FROM destinasi";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getInt("kota_id"),
                    rs.getString("nama_destinasi"),
                    rs.getString("deskripsi"),
                    rs.getDouble("harga"),
                    rs.getString("gambar")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static boolean insertDestinasi(int kotaId, String nama, String deskripsi, double harga, String gambar) {
        String query = "INSERT INTO destinasi (kota_id, nama_destinasi, deskripsi, harga, gambar) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, kotaId);
            stmt.setString(2, nama);
            stmt.setString(3, deskripsi);
            stmt.setDouble(4, harga);
            stmt.setString(5, gambar);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateDestinasi(int id, int kotaId, String nama, String deskripsi, double harga, String gambar) {
        String query = "UPDATE destinasi SET kota_id=?, nama_destinasi=?, deskripsi=?, harga=?, gambar=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, kotaId);
            stmt.setString(2, nama);
            stmt.setString(3, deskripsi);
            stmt.setDouble(4, harga);
            stmt.setString(5, gambar);
            stmt.setInt(6, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteDestinasi(int id) {
        String query = "DELETE FROM destinasi WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

