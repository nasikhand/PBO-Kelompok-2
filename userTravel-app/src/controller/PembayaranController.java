package controller;

import db.dao.PembayaranDAO;
import model.PembayaranModel;

import java.sql.Connection;
import java.util.Date;

public class PembayaranController {
    private PembayaranDAO pembayaranDAO;

    public PembayaranController(Connection conn) {
        this.pembayaranDAO = new PembayaranDAO(conn);
    }

    public boolean tambahPembayaran(PembayaranModel pembayaran) {
        return pembayaranDAO.insertPembayaran(pembayaran);
    }

    public double getTotalPembayaranByReservasi(int reservasiId) {
        return pembayaranDAO.getJumlahPembayaranByReservasiId(reservasiId);
    }

    // metode lain jika perlu
}
