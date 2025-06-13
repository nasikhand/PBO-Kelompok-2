package controller;

import db.Koneksi;
import db.dao.CustomTripDAO;
import model.CustomTripModel;

import java.sql.Connection;

public class CustomTripController {
    private CustomTripDAO customTripDAO;

    public CustomTripController() {
        Connection conn = Koneksi.getConnection();
        if (conn == null) {
            System.err.println("ERROR: Koneksi database NULL di CustomTripController constructor.");
        }
        this.customTripDAO = new CustomTripDAO(conn);
    }

    /**
     * Menyimpan CustomTripModel ke database.
     * @param customTrip Objek CustomTripModel yang akan disimpan.
     * @return ID dari custom trip yang baru dibuat, atau -1 jika gagal.
     */
    public int saveCustomTrip(CustomTripModel customTrip) {
        return customTripDAO.save(customTrip);
    }

    public boolean deleteCustomTrip(int customTripId) {
        return customTripDAO.deleteCustomTrip(customTripId);
    }
}
