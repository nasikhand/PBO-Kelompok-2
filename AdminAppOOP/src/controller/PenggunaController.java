/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Pengguna;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenggunaController {
    public static List<Pengguna> getAll() {
        List<Pengguna> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pengguna u = new Pengguna();
                u.setId(rs.getInt("id"));
                u.setNamaLengkap(rs.getString("nama_lengkap"));
                u.setEmail(rs.getString("email"));
                u.setTelepon(rs.getString("no_telepon"));
                u.setAlamat(rs.getString("alamat"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insert(Pengguna u) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO user (nama_lengkap, email, password, no_telepon, alamat) VALUES (?, ?, ?, ?, ?)");) {
            stmt.setString(1, u.getNamaLengkap());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getPassword());
            stmt.setString(4, u.getTelepon());
            stmt.setString(5, u.getAlamat());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Pengguna u) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user SET nama_lengkap=?, email=?, no_telepon=?, alamat=? WHERE id=?");) {
            stmt.setString(1, u.getNamaLengkap());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getTelepon());
            stmt.setString(4, u.getAlamat());
            stmt.setInt(5, u.getId());
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE id = ?");) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Pengguna getById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE id = ?");) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pengguna u = new Pengguna();
                u.setId(rs.getInt("id"));
                u.setNamaLengkap(rs.getString("nama_lengkap"));
                u.setEmail(rs.getString("email"));
                u.setTelepon(rs.getString("no_telepon"));
                u.setAlamat(rs.getString("alamat"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

