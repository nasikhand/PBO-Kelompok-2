package model;

import java.util.Date;

public class PenumpangModel {
    private int id;
    private int reservasiId;
    private String namaPenumpang;
    private String jenisKelamin;
    private Date tanggalLahir;
    private String nomorTelepon;
    private String email;
    

    // Constructor kosong
    public PenumpangModel() {}

    // Constructor lengkap
    public PenumpangModel(int reservasiId, String namaPenumpang, String jenisKelamin, Date tanggalLahir, String nomorTelepon, String email) {
        this.reservasiId = reservasiId;
        this.namaPenumpang = namaPenumpang;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
    }

    // Getter & Setter
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
