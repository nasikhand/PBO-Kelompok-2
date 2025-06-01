package model;

import java.time.LocalDate;

public class ReservasiModel {
    private int id;
    private String tripType; // "custom_trip" atau "paket_perjalanan"
    private int tripId;
    private String kodeReservasi;
    private LocalDate tanggalReservasi; // Ganti dari Date ke LocalDate
    private String status;
    private PaketPerjalananModel paket;
    private CustomTripModel customTrip;

    // Getter dan Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getKodeReservasi() {
        return kodeReservasi;
    }

    public void setKodeReservasi(String kodeReservasi) {
        this.kodeReservasi = kodeReservasi;
    }

    public LocalDate getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(LocalDate tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaketPerjalananModel getPaket() {
        return paket;
    }

    public void setPaket(PaketPerjalananModel paket) {
        this.paket = paket;
    }

    public CustomTripModel getCustomTrip() {
        return customTrip;
    }

    public void setCustomTrip(CustomTripModel customTrip) {
        this.customTrip = customTrip;
    }
}
