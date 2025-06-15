package controller;

import db.dao.UserDAO;
import model.UserModel;
import java.security.MessageDigest;

public class AuthController {
    private UserDAO userDAO = new UserDAO();

    private final String DEFAULT_EMAIL = "123";
    private final String DEFAULT_PASSWORD = "123";
    private final String DEFAULT_NO_TELP = "123";
    private final String DEFAULT_ALAMAT = "surabaya";
    
    public UserModel login(String email, String password) {

        // cek default login dulu
        if (email.equals(DEFAULT_EMAIL) && password.equals(DEFAULT_PASSWORD)) {
            UserModel defaultUser = new UserModel(0, "Admin Default", DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_NO_TELP, DEFAULT_ALAMAT, null);
            return defaultUser;
        }

        // login biasa via database
        UserModel user = userDAO.findByEmail(email);
        if (user != null) {
            String hashedInputPassword = hashPassword(password);
            if (user.getPassword().equals(hashedInputPassword)) {
                return user;
            }
        }


        return null;
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
