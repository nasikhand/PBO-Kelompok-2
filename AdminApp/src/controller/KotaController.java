/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class KotaController {

    public static DefaultTableModel getAllKota() {
        String[] columnNames = {"ID", "Nama Kota"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String query = "SELECT * FROM kota";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nama_kota")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

    public static boolean insertKota(String namaKota) {
        String query = "INSERT INTO kota (nama_kota) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, namaKota);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateKota(int id, String namaKota) {
        String query = "UPDATE kota SET nama_kota = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, namaKota);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteKota(int id) {
        String query = "DELETE FROM kota WHERE id = ?";

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

