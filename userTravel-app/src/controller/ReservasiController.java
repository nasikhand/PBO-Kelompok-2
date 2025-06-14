package controller;

import db.Koneksi;
import db.dao.ReservasiDAO;
import db.dao.PenumpangDAO; // Import PenumpangDAO
import model.ReservasiModel;
import model.PenumpangModel; // Import PenumpangModel

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservasiController {
    private ReservasiDAO reservasiDAO;
    private PenumpangDAO penumpangDAO; // Deklarasikan PenumpangDAO

    public ReservasiController() {
        // Pastikan koneksi didapatkan dan DAO diinisialisasi di sini
        Connection conn = Koneksi.getConnection();
        if (conn == null) {
            System.err.println("ERROR: Koneksi database NULL di ReservasiController constructor.");
            // Handle error: mungkin throw exception atau set flag
        }
        this.reservasiDAO = new ReservasiDAO(conn);
        this.penumpangDAO = new PenumpangDAO(conn); // <--- Pastikan inisialisasi di sini
    }

    // Jika Anda memiliki konstruktor lain yang menerima Connection,
    // pastikan juga penumpangDAO diinisialisasi di sana.
    // public ReservasiController(Connection existingConn) {    
    //     this.reservasiDAO = new ReservasiDAO(existingConn);
    //     this.penumpangDAO = new PenumpangDAO(existingConn);
    // }


    public int buatReservasi(ReservasiModel reservasi) {
        return reservasiDAO.save(reservasi);
    }

    public boolean tambahPenumpang(int reservasiId, String namaPenumpang) {
        // Lakukan null check tambahan untuk penumpangDAO jika khawatir
        if (this.penumpangDAO == null) {
            System.err.println("ERROR: PenumpangDAO belum diinisialisasi di ReservasiController.tambahPenumpang.");
            return false;
        }
        System.out.println("DEBUG ReservasiController - Memanggil PenumpangDAO.tambahPenumpang...");
        return penumpangDAO.tambahPenumpang(reservasiId, namaPenumpang);
    }

    public boolean deleteReservasi(int reservasiId) {
        return reservasiDAO.deleteReservasi(reservasiId);
    }
}