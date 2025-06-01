package model;

import java.time.LocalDate;
import java.util.List;

public class CustomTripModel {
    private int id;
    private String namaTrip;
    private LocalDate tanggalMulai;
    private LocalDate tanggalAkhir;
    private List<CustomTripDetailModel> detailList;

    // Getter dan Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaTrip() {
        return namaTrip;
    }

    public void setNamaTrip(String namaTrip) {
        this.namaTrip = namaTrip;
    }

    public LocalDate getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDate tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDate getTanggalAkhir() {
        return tanggalAkhir;
    }

    public void setTanggalAkhir(LocalDate tanggalAkhir) {
        this.tanggalAkhir = tanggalAkhir;
    }

    public List<CustomTripDetailModel> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<CustomTripDetailModel> detailList) {
        this.detailList = detailList;
    }
}
