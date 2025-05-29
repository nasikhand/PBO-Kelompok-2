/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CekKoneksiDanData {
    private static final String URL = "jdbc:mysql://localhost:3306/travel_app";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            System.out.println("Mencoba menghubungkan ke basis data...");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi berhasil!");
            
            Statement stmt = conn.createStatement();
            
            // Mari kita cek jumlah data di beberapa tabel
            String[] tabelUntukDicek = {"user", "kota", "destinasi", "paket_perjalanan", "reservasi"};
            
            for (String namaTabel : tabelUntukDicek) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + namaTabel);
                if (rs.next()) {
                    System.out.println("Jumlah data di tabel '" + namaTabel + "': " + rs.getInt(1));
                }
                rs.close();
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan saat tes koneksi!");
            e.printStackTrace();
        }
    }
}
