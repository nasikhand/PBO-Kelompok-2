package db;
import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    private static final String URL = "jdbc:mysql://localhost:3306/travel_app";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver ditemukan");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Berhasil terkoneksi ke database");
            return conn;
        } catch (Exception e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
}
