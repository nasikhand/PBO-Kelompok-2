package controller;

import java.sql.Connection;
import java.util.List;

import db.dao.ReservasiDAO;
import model.ReservasiModel;

public class ReservasiController {
    private ReservasiDAO reservasiDAO;

    public ReservasiController(Connection conn) {
        this.reservasiDAO = new ReservasiDAO(conn);
    }

    public List<ReservasiModel> getHistoryUser(int userId) {
        return reservasiDAO.getHistoryReservasiByUser(userId);
    }
}
