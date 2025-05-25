package model;

public class PaketPerjalananModel {
    private int id;
    private String namaPaket;
    private String namaKota;
    private String tanggalMulai;
    private String tanggalAkhir;
    private int kuota;
    private double harga;
    private String deskripsi;
    private String status;

    // Constructor
    public PaketPerjalananModel(int id, String namaPaket, String namaKota, String tanggalMulai, String tanggalAkhir, int kuota, double harga, String deskripsi, String status) {
        this.id = id;
        this.namaPaket = namaPaket;
        this.namaKota = namaKota;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.kuota = kuota;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
    }

    // Getter dan Setter (opsional, tergantung kebutuhan)
    public int getId() {
        return id;
    }

    public String getNamaPaket() {
        return namaPaket;
    }

    public String getNamaKota() {
        return namaKota;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public String getTanggalAkhir() {
        return tanggalAkhir;
    }

    public int getKuota() {
        return kuota;
    }

    public double getHarga() {
        return harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getStatus() {
        return status;
    }
}
