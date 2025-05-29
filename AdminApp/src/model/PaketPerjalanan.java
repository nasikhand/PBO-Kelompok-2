/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date;

public class PaketPerjalanan {
    private int id;
    private int kotaId;
    private String namaPaket;
    private Date tanggalMulai;
    private Date tanggalAkhir;
    private int kuota;
    private BigDecimal harga;
    private String deskripsi;
    private String status;
    private String namaKota;// Untuk join table
    private String gambar; 

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getKotaId() { return kotaId; }
    public void setKotaId(int kotaId) { this.kotaId = kotaId; }
    public String getNamaPaket() { return namaPaket; }
    public void setNamaPaket(String namaPaket) { this.namaPaket = namaPaket; }
    public Date getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(Date tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public Date getTanggalAkhir() { return tanggalAkhir; }
    public void setTanggalAkhir(Date tanggalAkhir) { this.tanggalAkhir = tanggalAkhir; }
    public int getKuota() { return kuota; }
    public void setKuota(int kuota) { this.kuota = kuota; }
    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNamaKota() { return namaKota; }
    public void setNamaKota(String namaKota) { this.namaKota = namaKota; }
    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
