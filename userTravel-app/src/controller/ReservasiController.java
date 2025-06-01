package controller;

import db.dao.ReservasiDAO;
import model.ReservasiModel;

import java.sql.Connection;
import java.util.List;

public class ReservasiController {
    private ReservasiDAO reservasiDAO;

    public ReservasiController(Connection connection) {
        this.reservasiDAO = new ReservasiDAO(connection);
    }

    // Method untuk ambil list reservasi sebelumnya user tertentu
    public List<ReservasiModel> getReservasiSebelumnya(int userId) {
        return reservasiDAO.getReservasiSebelumnya(userId);
    }
}
