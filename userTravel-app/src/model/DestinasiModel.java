package model;

public class DestinasiModel {
    private int id;
    private int kotaId;
    private String namaDestinasi;
    private String deskripsi;
    private double harga;
    private String gambar;

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getKotaId() { return kotaId; }
    public void setKotaId(int kotaId) { this.kotaId = kotaId; }

    public String getNamaDestinasi() { return namaDestinasi; }
    public void setNamaDestinasi(String namaDestinasi) { this.namaDestinasi = namaDestinasi; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }
    @Override
    public String toString() {
        return namaDestinasi + " - " + kotaId;
    }
}
