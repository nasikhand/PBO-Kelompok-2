package model;

public class Session {
    public static UserModel currentUser = null;

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}
