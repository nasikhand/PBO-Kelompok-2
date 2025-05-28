package controller;

import db.dao.PaketPerjalananDAO;
import model.PaketPerjalananModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaketPerjalananController {
    private PaketPerjalananDAO dao;
     private Connection conn;

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

    public List<PaketPerjalananModel> getTop3Rating() {
    List<PaketPerjalananModel> list = new ArrayList<>();
    try {
        String query = "SELECT * FROM paketperjalanan ORDER BY rating DESC LIMIT 3";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            PaketPerjalananModel paket = new PaketPerjalananModel();
            paket.setId(rs.getInt("id"));
            paket.setNamaPaket(rs.getString("nama_paket"));
            paket.setTanggalMulai(rs.getString("tanggal_mulai"));
            paket.setTanggalAkhir(rs.getString("tanggal_akhir"));
            paket.setKuota(rs.getInt("kuota"));
            paket.setRating(rs.getDouble("rating"));
            paket.setHarga(rs.getDouble("harga"));
            paket.setGambar(rs.getString("gambar")); // misal ini path gambar
            list.add(paket);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

}
