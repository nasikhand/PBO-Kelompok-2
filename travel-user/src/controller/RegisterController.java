package controller;

import db.dao.UserDAO;
import model.UserModel;
import java.security.MessageDigest;

public class RegisterController {
    private UserDAO userDAO = new UserDAO();

    public boolean register(String namaLengkap, String email, String password) {
        // cek email sudah ada atau belum via DAO
        if (userDAO.findByEmail(email) != null) {
            return false; // email sudah terdaftar
        }

        String hashedPassword = hashPassword(password);
        UserModel newUser = new UserModel(namaLengkap, email, hashedPassword);
        return userDAO.save(newUser);
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
}
