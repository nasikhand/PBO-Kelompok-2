package controller;

import db.Koneksi;
import db.dao.ReservasiDAO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import model.ReservasiModel; 

public class ReservasiController {
    private ReservasiDAO reservasiDAO;
    private Connection connection; 


    public ReservasiController(Connection conn) {
        this.connection = conn;
        if (this.connection != null) {
            this.reservasiDAO = new ReservasiDAO(this.connection);
        } else {
            System.err.println("Koneksi null di konstruktor ReservasiController. DAO tidak dapat diinisialisasi dengan benar.");
            this.reservasiDAO = new ReservasiDAO(); 
        }
    }

    public ReservasiController() {
        this.connection = Koneksi.getConnection();
        if (this.connection == null) {
            System.err.println("Gagal mendapatkan koneksi database di ReservasiController (konstruktor default).");
            this.reservasiDAO = new ReservasiDAO(); 
        } else {
            this.reservasiDAO = new ReservasiDAO(this.connection);
        }
    }

    public List<ReservasiModel> getHistoryUser(int userId) {
        if (reservasiDAO == null) {
             System.err.println("ReservasiDAO tidak diinisialisasi di controller (getHistoryUser).");
            return new ArrayList<>();
        }
        return reservasiDAO.getHistoryReservasiByUser(userId);
    }


    public int buatReservasi(ReservasiModel reservasi) {
        if (reservasiDAO == null) {
            System.err.println("ReservasiDAO tidak diinisialisasi di controller (buatReservasi).");
            return -1;
        }
        if (reservasi == null) {
            System.err.println("Objek reservasi tidak boleh null untuk disimpan.");
            return -1;
        }
      
        return reservasiDAO.save(reservasi);
    }
    

}
