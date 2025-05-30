/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date;

public class CustomTrip {
    private int id;
    private int userId;
    private String namaTrip;
    private Date tanggalMulai;
    private Date tanggalAkhir;
    private int jumlahPeserta;
    private String status;
    private BigDecimal totalHarga;
    private String catatanUser;
    
    // Atribut tambahan untuk menampung nama pemesan dari hasil JOIN
    private String namaPemesan;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getNamaTrip() { return namaTrip; }
    public void setNamaTrip(String namaTrip) { this.namaTrip = namaTrip; }
    public Date getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(Date tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    public Date getTanggalAkhir() { return tanggalAkhir; }
    public void setTanggalAkhir(Date tanggalAkhir) { this.tanggalAkhir = tanggalAkhir; }
    public int getJumlahPeserta() { return jumlahPeserta; }
    public void setJumlahPeserta(int jumlahPeserta) { this.jumlahPeserta = jumlahPeserta; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotalHarga() { return totalHarga; }
    public void setTotalHarga(BigDecimal totalHarga) { this.totalHarga = totalHarga; }
    public String getCatatanUser() { return catatanUser; }
    public void setCatatanUser(String catatanUser) { this.catatanUser = catatanUser; }
    public String getNamaPemesan() { return namaPemesan; }
    public void setNamaPemesan(String namaPemesan) { this.namaPemesan = namaPemesan; }
}
