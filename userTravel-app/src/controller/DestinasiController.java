package controller;

import db.dao.DestinasiDAO;
import model.DestinasiModel;

import java.util.List;

public class DestinasiController {
    DestinasiDAO dao = new DestinasiDAO();

    public List<DestinasiModel> tampilkanSemuaDestinasi() {
        return dao.getAllDestinasi();
    }

    public List<DestinasiModel> getDestinasiByPaketId(int paketId) {
        return dao.getDestinasiByPaketId(paketId);
    }

}