/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class Penumpang {
    private int id;
    private int reservasiId;
    private String namaPenumpang;
    private String jenisKelamin;
    private Date tanggalLahir;
    private String nomorTelepon;
    private String email;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getReservasiId() { return reservasiId; }
    public void setReservasiId(int reservasiId) { this.reservasiId = reservasiId; }
    public String getNamaPenumpang() { return namaPenumpang; }
    public void setNamaPenumpang(String namaPenumpang) { this.namaPenumpang = namaPenumpang; }
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }
    public Date getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(Date tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    public String getNomorTelepon() { return nomorTelepon; }
    public void setNomorTelepon(String nomorTelepon) { this.nomorTelepon = nomorTelepon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
