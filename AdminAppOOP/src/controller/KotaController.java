/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Kota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KotaController {
    public static List<Kota> getAllKota() {
        List<Kota> list = new ArrayList<>();
        String query = "SELECT * FROM kota";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Kota(rs.getInt("id"), rs.getString("nama_kota")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

