package model;

import java.util.Date;

public class PembayaranModel {
    private int id;
    private int reservasiId;
    private String metodePembayaran;
    private double jumlahPembayaran;
    private Date tanggalPembayaran;
    private String statusPembayaran;

    // Constructor kosong
    public PembayaranModel() {}

    // Constructor lengkap
    public PembayaranModel(int reservasiId, String metodePembayaran, double jumlahPembayaran, Date tanggalPembayaran, String statusPembayaran) {
        this.reservasiId = reservasiId;
        this.metodePembayaran = metodePembayaran;
        this.jumlahPembayaran = jumlahPembayaran;
        this.tanggalPembayaran = tanggalPembayaran;
        this.statusPembayaran = statusPembayaran;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservasiId() { return reservasiId; }
    public void setReservasiId(int reservasiId) { this.reservasiId = reservasiId; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public double getJumlahPembayaran() { return jumlahPembayaran; }
    public void setJumlahPembayaran(double jumlahPembayaran) { this.jumlahPembayaran = jumlahPembayaran; }

    public Date getTanggalPembayaran() { return tanggalPembayaran; }
    public void setTanggalPembayaran(Date tanggalPembayaran) { this.tanggalPembayaran = tanggalPembayaran; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }
}
