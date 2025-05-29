/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Kelas ini sekarang berfungsi sebagai "pabrik" koneksi.
 * Setiap kali metode getConnection() dipanggil, ia akan membuat dan mengembalikan
 * sebuah objek koneksi yang baru dan segar.
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/travel_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Membuat dan mengembalikan sebuah koneksi BARU ke database.
     * Tidak lagi menggunakan satu objek koneksi statis.
     * @return Objek Connection yang baru, atau null jika gagal.
     */
    public static Connection getConnection() {
        try {
            // Tidak perlu Class.forName() untuk driver JDBC modern
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Koneksi ke basis data gagal!\n" +
                    "Pesan Kesalahan: " + e.getMessage(),
                    "Kesalahan Basis Data",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            // Mengembalikan null agar aplikasi tahu koneksi gagal
            return null;
        }
    }
}
