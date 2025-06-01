package model;

import java.math.BigDecimal;

public class PenawaranModel {
    private int id;
    private String nama;
    private BigDecimal harga;
    private BigDecimal diskon;
    private String deskripsi;
    private String gambar;

    // Getter
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public BigDecimal getDiskon() {
        return diskon;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public void setDiskon(BigDecimal diskon) {
        this.diskon = diskon;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
