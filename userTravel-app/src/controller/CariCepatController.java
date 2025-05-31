package controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

// import db.dao.DestinasiDAO; // Ganti dengan KotaDAO
// import model.DestinasiModel; // Ganti dengan KotaModel
import db.dao.KotaDAO; // Impor KotaDAO
import model.KotaModel; // Impor KotaModel
import db.Koneksi; // Untuk fallback jika koneksi tidak di-inject

public class CariCepatController {

    // private DestinasiDAO destinasiDao; // Ganti dengan KotaDAO
    private KotaDAO kotaDao; // Menggunakan KotaDAO
    private Connection connection;

    // Konstruktor utama yang menerima koneksi
    public CariCepatController(Connection conn) {
        this.connection = conn;
        // this.destinasiDao = new DestinasiDAO(conn); // Ganti dengan KotaDAO
        if (this.connection != null) {
            this.kotaDao = new KotaDAO(this.connection); // Inisialisasi KotaDAO dengan koneksi
        } else {
            System.err.println("Koneksi null di konstruktor CariCepatController. Mencoba fallback.");
            // Fallback jika koneksi null, meskipun idealnya koneksi selalu valid
            this.kotaDao = new KotaDAO(); // Asumsi KotaDAO() bisa mengambil koneksi dari Koneksi.java
        }
    }
    
    // Konstruktor alternatif jika ingin mengambil koneksi dari kelas Koneksi secara default
    public CariCepatController() {
        this.connection = Koneksi.getConnection();
        if (this.connection == null) {
            System.err.println("Gagal mendapatkan koneksi database di CariCepatController (konstruktor default).");
            // Inisialisasi DAO dengan null connection akan menyebabkan error nanti,
            // jadi lebih baik handle di sini atau pastikan Koneksi.getConnection() selalu berhasil.
            // Untuk sementara, biarkan DAO diinisialisasi (mungkin DAO punya fallback sendiri)
             this.kotaDao = new KotaDAO(); // Asumsi KotaDAO() bisa mengambil koneksi dari Koneksi.java
        } else {
            this.kotaDao = new KotaDAO(this.connection);
        }
    }


    /**
     * Mengambil daftar semua kota yang tersedia.
     * @return List dari KotaModel.
     */
    public List<KotaModel> getDaftarKota() {
        // return destinasiDao.getAllDestinasi(); // Ganti dengan metode dari KotaDAO
        if (this.kotaDao == null) {
            System.err.println("KotaDAO tidak diinisialisasi di CariCepatController.");
            return new ArrayList<>(); // Kembalikan list kosong jika DAO null
        }
        // Asumsi KotaDAO memiliki metode getAllKota()
        // Jika nama metodenya berbeda, sesuaikan di sini.
        return kotaDao.getAllKota(); 
    }

    // Jika Anda memerlukan metode lain di controller ini terkait pencarian cepat
    // (misalnya, validasi input, dll.), Anda bisa menambahkannya di sini.
}
