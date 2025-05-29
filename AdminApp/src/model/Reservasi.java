/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

public class Reservasi {
    private int id;
    private String tripType;
    private int tripId;
    private String kodeReservasi;
    private Date tanggalReservasi;
    private String status;

    // Atribut tambahan untuk ditampilkan di tabel
    private String namaTrip; // Bisa nama paket atau nama custom trip
    private String namaPemesan; // Nama user yang memesan

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTripType() { return tripType; }
    public void setTripType(String tripType) { this.tripType = tripType; }
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public String getKodeReservasi() { return kodeReservasi; }
    public void setKodeReservasi(String kodeReservasi) { this.kodeReservasi = kodeReservasi; }
    public Date getTanggalReservasi() { return tanggalReservasi; }
    public void setTanggalReservasi(Date tanggalReservasi) { this.tanggalReservasi = tanggalReservasi; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNamaTrip() { return namaTrip; }
    public void setNamaTrip(String namaTrip) { this.namaTrip = namaTrip; }
    public String getNamaPemesan() { return namaPemesan; }
    public void setNamaPemesan(String namaPemesan) { this.namaPemesan = namaPemesan; }
}