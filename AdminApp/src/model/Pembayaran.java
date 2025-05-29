/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date; // Pastikan menggunakan java.sql.Date untuk kompatibilitas dengan database
import java.sql.Timestamp; // Bisa juga Timestamp jika perlu waktu detail

public class Pembayaran {
    private int id;
    private int reservasiId;
    private String metodePembayaran;
    private BigDecimal jumlahPembayaran;
    private Date tanggalPembayaran; // atau Timestamp
    private String statusPembayaran;

    // Atribut tambahan untuk tampilan di tabel (hasil JOIN)
    private String kodeReservasi;
    // Anda bisa tambahkan nama pengguna atau nama paket jika diperlukan

    // Constructor
    public Pembayaran() {}

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservasiId() { return reservasiId; }
    public void setReservasiId(int reservasiId) { this.reservasiId = reservasiId; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public BigDecimal getJumlahPembayaran() { return jumlahPembayaran; }
    public void setJumlahPembayaran(BigDecimal jumlahPembayaran) { this.jumlahPembayaran = jumlahPembayaran; }

    public Date getTanggalPembayaran() { return tanggalPembayaran; }
    public void setTanggalPembayaran(Date tanggalPembayaran) { this.tanggalPembayaran = tanggalPembayaran; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }

    public String getKodeReservasi() { return kodeReservasi; }
    public void setKodeReservasi(String kodeReservasi) { this.kodeReservasi = kodeReservasi; }
}
