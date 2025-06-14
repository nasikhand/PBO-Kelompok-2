package controller;

import db.Koneksi;
import db.dao.DestinasiDAO;
import model.DestinasiModel;

import java.sql.Connection;
import java.util.List;

public class DestinasiController {
    private DestinasiDAO dao;

    public DestinasiController() {
        Connection conn = Koneksi.getConnection();
        if (conn == null) {
            System.err.println("ERROR: Koneksi database NULL di DestinasiController constructor.");
        }
        this.dao = new DestinasiDAO(conn);
    }

    public List<DestinasiModel> tampilkanSemuaDestinasi() {
        return dao.getAllDestinasi();
    }

    public List<DestinasiModel> getDestinasiByPaketId(int paketId) {
        return dao.getDestinasiByPaketId(paketId);
    }

    public DestinasiModel getDestinasiByName(String namaDestinasi) {
        return dao.getDestinasiByName(namaDestinasi); // Memanggil metode di DestinasiDAO
    }

    public DestinasiModel getDestinasiById(int id) {
        return dao.getDestinasiById(id);
    }
}
