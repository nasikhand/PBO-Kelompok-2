package model;

public class KotaModel {
    private int id;
    private String namaKota;
    // Tambahkan field lain seperti provinsi jika perlu
    public KotaModel(int id, String namaKota) { this.id = id; this.namaKota = namaKota; }
    public int getId() { return id; }
    public String getNamaKota() { return namaKota; }
    // Setter jika perlu
    public String getNamaDestinasi() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNamaDestinasi'");
    }
}