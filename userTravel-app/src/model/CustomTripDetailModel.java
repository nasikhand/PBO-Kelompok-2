package model;

import java.time.LocalDate;

public class CustomTripDetailModel {
    private int id;
    private int destinasiId;
    private LocalDate tanggalKunjungan;
    private int durasiJam;
    private int urutanKunjungan;
    private double hargaDestinasi;
    private double biayaTransport;
    private int customTripId;   

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setDestinasiId(int destinasiId) {
        this.destinasiId = destinasiId;
    }

    public void setTanggalKunjungan(LocalDate tanggalKunjungan) {
        this.tanggalKunjungan = tanggalKunjungan;
    }

    public void setDurasiJam(int durasiJam) {
        this.durasiJam = durasiJam;
    }

    public void setUrutanKunjungan(int urutanKunjungan) {
        this.urutanKunjungan = urutanKunjungan;
    }

    public void setHargaDestinasi(double hargaDestinasi) {
        this.hargaDestinasi = hargaDestinasi;
    }

    public void setBiayaTransport(double biayaTransport) {
        this.biayaTransport = biayaTransport;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getDestinasiId() {
        return destinasiId;
    }

    public LocalDate getTanggalKunjungan() {
        return tanggalKunjungan;
    }

    public int getDurasiJam() {
        return durasiJam;
    }

    public int getUrutanKunjungan() {
        return urutanKunjungan;
    }

    public double getHargaDestinasi() {
        return hargaDestinasi;
    }

    public double getBiayaTransport() {
        return biayaTransport;
    }

    public void setCustomTripId(int customTripId) {
    this.customTripId = customTripId; 
    }
}
