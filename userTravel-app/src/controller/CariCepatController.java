package controller;

import java.sql.Connection;
import java.util.List;

import db.dao.PaketPerjalananDAO;
import model.PaketPerjalananModel;

public class CariCepatController {
    private PaketPerjalananDAO dao;

    public CariCepatController(Connection conn) {
        this.dao = new PaketPerjalananDAO(conn);
    }

    public List<PaketPerjalananModel> cari(String destinasi, String tanggal, String travelersStr) {
        int jumlahTraveler = parseJumlahTraveler(travelersStr);
        return dao.cariCepat(destinasi, tanggal, jumlahTraveler);
    }

    private int parseJumlahTraveler(String input) {
        // "1 Orang" -> 1, "Travelers" -> 1 default
        try {
            return Integer.parseInt(input.split(" ")[0]);
        } catch (Exception e) {
            return 1;
        }
    }
}
