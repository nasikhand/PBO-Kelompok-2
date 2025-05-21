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
                    rs.getString("password")
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
            String sql = "INSERT INTO user (nama_lengkap, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());  // password sudah harus dalam kondisi hashed
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
