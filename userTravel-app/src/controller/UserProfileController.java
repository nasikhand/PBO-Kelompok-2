package controller;

import db.dao.UserDAO;
import model.UserModel;

public class UserProfileController {
    private UserDAO userDAO = new UserDAO();

    public boolean updateProfile(UserModel user) {
        // tambahkan validasi di sini nanti
        return userDAO.updateProfile(user);
    }

    public UserModel getProfile(int userId) {
        return userDAO.findById(userId);
    }
}
