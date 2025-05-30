/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date;

public class RincianCustomTrip {
    private int id;
    private int customTripId;
    private int destinasiId;
    private Date tanggalKunjungan;
    
    // Atribut tambahan untuk menampung nama destinasi dari hasil JOIN
    private String namaDestinasi;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomTripId() { return customTripId; }
    public void setCustomTripId(int customTripId) { this.customTripId = customTripId; }
    public int getDestinasiId() { return destinasiId; }
    public void setDestinasiId(int destinasiId) { this.destinasiId = destinasiId; }
    public Date getTanggalKunjungan() { return tanggalKunjungan; }
    public void setTanggalKunjungan(Date tanggalKunjungan) { this.tanggalKunjungan = tanggalKunjungan; }
    public String getNamaDestinasi() { return namaDestinasi; }
    public void setNamaDestinasi(String namaDestinasi) { this.namaDestinasi = namaDestinasi; }
}
