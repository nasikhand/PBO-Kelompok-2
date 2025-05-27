/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Perjalanan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerjalananController {
    public static List<Perjalanan> getAll() {
        List<Perjalanan> list = new ArrayList<>();
        String query = "SELECT pp.*, k.nama_kota FROM paket_perjalanan pp JOIN kota k ON pp.kota_id = k.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Perjalanan p = new Perjalanan();
                p.setId(rs.getInt("id"));
                p.setNamaPaket(rs.getString("nama_paket"));
                p.setKota(rs.getString("nama_kota"));
                p.setKotaId(rs.getInt("kota_id"));
                p.setTanggalMulai(rs.getString("tanggal_mulai"));
                p.setTanggalAkhir(rs.getString("tanggal_akhir"));
                p.setKuota(rs.getInt("kuota"));
                p.setHarga(rs.getDouble("harga"));
                p.setStatus(rs.getString("status"));
                p.setGambar(rs.getString("gambar"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean insertPerjalanan(Perjalanan p) {
        String query = "INSERT INTO paket_perjalanan (kota_id, nama_paket, tanggal_mulai, tanggal_akhir, kuota, harga, status, gambar) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, p.getKotaId());
            stmt.setString(2, p.getNamaPaket());
            stmt.setString(3, p.getTanggalMulai());
            stmt.setString(4, p.getTanggalAkhir());
            stmt.setInt(5, p.getKuota());
            stmt.setDouble(6, p.getHarga());
            stmt.setString(7, p.getStatus());
            stmt.setString(8, p.getGambar());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePerjalanan(int id, Perjalanan p) {
        String query = "UPDATE paket_perjalanan SET kota_id=?, nama_paket=?, tanggal_mulai=?, tanggal_akhir=?, kuota=?, harga=?, status=?, gambar=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, p.getKotaId());
            stmt.setString(2, p.getNamaPaket());
            stmt.setString(3, p.getTanggalMulai());
            stmt.setString(4, p.getTanggalAkhir());
            stmt.setInt(5, p.getKuota());
            stmt.setDouble(6, p.getHarga());
            stmt.setString(7, p.getStatus());
            stmt.setString(8, p.getGambar());
            stmt.setInt(9, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM paket_perjalanan WHERE id = ?")) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Perjalanan getById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM paket_perjalanan WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Perjalanan p = new Perjalanan();
                p.setId(rs.getInt("id"));
                p.setNamaPaket(rs.getString("nama_paket"));
                p.setKotaId(rs.getInt("kota_id"));
                p.setTanggalMulai(rs.getString("tanggal_mulai"));
                p.setTanggalAkhir(rs.getString("tanggal_akhir"));
                p.setKuota(rs.getInt("kuota"));
                p.setHarga(rs.getDouble("harga"));
                p.setStatus(rs.getString("status"));
                p.setGambar(rs.getString("gambar"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

