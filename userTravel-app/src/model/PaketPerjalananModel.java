package model;

public class PaketPerjalananModel {
    private int id;
    private int kotaId;
    private String namaPaket;
    private String tanggalMulai;
    private String tanggalAkhir;
    private int kuota;
    private double harga;
    private String deskripsi;
    private String status;
    private String gambar;
    private double rating;

    // Constructor

    public PaketPerjalananModel() {
    // kosong, tapi harus ada supaya bisa new PaketPerjalananModel()
    }
    
    public PaketPerjalananModel(int id, int kotaId, String namaPaket, String tanggalMulai,String tanggalAkhir, int kuota, double harga,String deskripsi, String status, String gambar, double rating) {

        this.id = id;
        this.kotaId = kotaId;
        this.namaPaket = namaPaket;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.kuota = kuota;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
        this.gambar = gambar;
        this.rating = rating;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getKotaId() { return kotaId; }
    public void setKotaId(int kotaId) { this.kotaId = kotaId; }

    public String getNamaPaket() { return namaPaket; }
    public void setNamaPaket(String namaPaket) { this.namaPaket = namaPaket; }

    public String getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(String tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public String getTanggalAkhir() { return tanggalAkhir; }
    public void setTanggalAkhir(String tanggalAkhir) { this.tanggalAkhir = tanggalAkhir; }

    public int getKuota() { return kuota; }
    public void setKuota(int kuota) { this.kuota = kuota; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
