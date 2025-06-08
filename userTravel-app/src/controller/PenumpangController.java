package controller;


import db.dao.PenumpangDAO;
import model.PenumpangModel;

import java.sql.Connection;
import java.util.List;

public class PenumpangController {
    private PenumpangDAO penumpangDAO;

    public PenumpangController(Connection conn) {
        this.penumpangDAO = new PenumpangDAO(conn);
    }

    public boolean tambahPenumpang(PenumpangModel penumpang) {
        return penumpangDAO.insertPenumpang(penumpang);
    }

    public int getJumlahPenumpangByReservasi(int reservasiId) {
        return penumpangDAO.getJumlahPenumpangByReservasiId(reservasiId);
    }

    // metode lain jika perlu
}
