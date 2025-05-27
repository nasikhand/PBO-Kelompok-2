package controller;

import db.dao.PaketPerjalananDAO;
import model.PaketPerjalananModel;
import java.sql.Connection;
import java.util.List;

public class PaketPerjalananController {
    private PaketPerjalananDAO dao;

    public PaketPerjalananController(Connection conn) {
        this.dao = new PaketPerjalananDAO(conn);
    }

    // Cari paket berdasarkan destinasi (filter sederhana)
    public List<PaketPerjalananModel> cariPaket(String destinasi) {
        return dao.getPaketByDestinasi(destinasi);
    }

    // Ambil detail paket berdasarkan id
    public PaketPerjalananModel getById(int id) {
        return dao.getById(id);
    }

    // Booking cepat (dummy, nanti bisa diisi dengan implementasi sebenarnya)
    public boolean bookingCepat(int userId, int paketId, int jumlahOrang) {
        System.out.println("Booking paket cepat untuk user " + userId + " paket " + paketId + " orang " + jumlahOrang);
        return true;
    }
}
