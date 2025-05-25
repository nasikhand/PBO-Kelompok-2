package controller;

import db.dao.UserDAO;
import java.security.MessageDigest;  

public class UbahPasswordController {
    private UserDAO userDAO;

    public UbahPasswordController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String ubahPassword(int userId, String passwordLama, String passwordBaru, String konfirmasiPassword) {
        if (!passwordBaru.equals(konfirmasiPassword)) {
            return "Password baru dan konfirmasi tidak sama!";
        }

        // Hash password lama sebelum dicek
        String hashedPasswordLama = hashPassword(passwordLama);
        if (!userDAO.cekPasswordLama(userId, hashedPasswordLama)) {
            return "Password lama salah!";
        }

        // Hash password baru sebelum diupdate
        String hashedPasswordBaru = hashPassword(passwordBaru);
        boolean berhasil = userDAO.updatePassword(userId, hashedPasswordBaru);

        if (berhasil) {
            return "Password berhasil diubah.";
        } else {
            return "Gagal mengubah password, coba lagi.";
        }
    }
}