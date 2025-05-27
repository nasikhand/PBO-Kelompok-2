package controller;

import java.sql.Connection;
import java.util.List;

import db.dao.DestinasiDAO;
import model.DestinasiModel;


public class CariCepatController {

    private DestinasiDAO destinasiDao;

    public CariCepatController(Connection conn) {
        this.destinasiDao = new DestinasiDAO(conn);
    }

    public List<DestinasiModel> getDaftarDestinasi() {
        return destinasiDao.getAllDestinasi();
    }
}
