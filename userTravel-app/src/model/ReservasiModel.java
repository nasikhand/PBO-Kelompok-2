package model;

import java.time.LocalDate;
import java.util.Objects;

public class ReservasiModel {
    private int id;
    private int userId;
    private String tripType;
    private Integer tripId; // <--- UBAH INI dari 'int' menjadi 'Integer'
    private String kodeReservasi;
    private LocalDate tanggalReservasi;
    private String status;
    private PaketPerjalananModel paket;
    private CustomTripModel customTrip;
    private int jumlahHari; // Ini tidak digunakan di toString atau equals/hashCode

    // Konstruktor default
    public ReservasiModel() {
    }

    // Konstruktor untuk membuat reservasi baru (sebelum disimpan ke DB, tanpa ID auto-increment)
    public ReservasiModel(int userId, String tripType, Integer tripId, String kodeReservasi, LocalDate tanggalReservasi, String status) {
        this.userId = userId;
        this.tripType = tripType;
        this.tripId = tripId;
        this.kodeReservasi = kodeReservasi;
        this.tanggalReservasi = tanggalReservasi;
        this.status = status;
    }

    public ReservasiModel(int id, int userId, String tripType, Integer tripId, String kodeReservasi, LocalDate tanggalReservasi, String status) {
        this.id = id;
        this.userId = userId;
        this.tripType = tripType;
        this.tripId = tripId;
        this.kodeReservasi = kodeReservasi;
        this.tanggalReservasi = tanggalReservasi;
        this.status = status;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public Integer getTripId() { // Return type sudah Integer, sesuai
        return tripId;
    }

    public void setTripId(Integer tripId) { // Parameter type sudah Integer, sesuai
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

    public int getJumlahHari() { // Asumsi ada getter/setter untuk ini
        return jumlahHari;
    }

    public void setJumlahHari(int jumlahHari) {
        this.jumlahHari = jumlahHari;
    }

    @Override
    public String toString() {
        return "ReservasiModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", tripType='" + tripType + '\'' +
                ", tripId=" + tripId +
                ", kodeReservasi='" + kodeReservasi + '\'' +
                ", tanggalReservasi=" + tanggalReservasi +
                ", status='" + status + '\'' +
                ", paket=" + (paket != null ? paket.getNamaPaket() : "null") +
                ", customTrip=" + (customTrip != null ? customTrip.getNamaTrip() : "null") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservasiModel that = (ReservasiModel) o;
        // Gunakan Objects.equals untuk tipe wrapper (Integer) untuk menangani null dengan benar
        return id == that.id &&
                userId == that.userId &&
                Objects.equals(tripId, that.tripId) && // <--- UBAH INI
                Objects.equals(tripType, that.tripType) &&
                Objects.equals(kodeReservasi, that.kodeReservasi) &&
                Objects.equals(tanggalReservasi, that.tanggalReservasi) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, tripType, tripId, kodeReservasi, tanggalReservasi, status);
    }
}
