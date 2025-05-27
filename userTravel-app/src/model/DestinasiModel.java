package model;

public class DestinasiModel {
    private int id;
    private String namaDestinasi;
    private String kota;

    public DestinasiModel(int id, String namaDestinasi, String kota) {
        this.id = id;
        this.namaDestinasi = namaDestinasi;
        this.kota = kota;
    }

    public int getId() {
        return id;
    }

    public String getNamaDestinasi() {
        return namaDestinasi;
    }

    public String getKota() {
        return kota;
    }

    @Override
    public String toString() {
        return namaDestinasi + " - " + kota;
    }
}
