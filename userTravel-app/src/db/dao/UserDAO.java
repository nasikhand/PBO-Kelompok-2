package db.dao;

import java.sql.*;
import db.Koneksi;
import model.UserModel;

public class UserDAO {
     // Cari user berdasarkan email (dipakai untuk login dan cek registrasi)
    public UserModel findByEmail(String email) {
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM user WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserModel(
                    rs.getInt("id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat")

                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Simpan user baru (registrasi)
    public boolean save(UserModel user) {
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "INSERT INTO user (nama_lengkap, email, password, no_telepon, alamat) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());  // password sudah harus dalam kondisi hashed
            stmt.setString(4, user.getNomorTelepon());  // Tambahkan ini
            stmt.setString(5, user.getAlamat());  
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    Ambil data profil untuk user profile
    public UserModel findById(int id) {
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "SELECT * FROM user WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserModel(
                    rs.getInt("id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("email"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProfile(UserModel user) {
        try (Connection conn = Koneksi.getConnection()) {
            String sql = "UPDATE user SET nama_lengkap = ?, email = ?, no_telepon = ?, alamat = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getNomorTelepon());
            stmt.setString(4, user.getAlamat());
            stmt.setInt(5, user.getId());
            int updated = stmt.executeUpdate();

            
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
