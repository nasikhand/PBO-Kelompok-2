package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CustomTripModel {
    private int id;
    private int userId;
    private String namaTrip;
    private LocalDate tanggalMulai;
    private LocalDate tanggalAkhir;
    private int jumlahPeserta; // Pastikan field ini ada
    private String status;      // Pastikan field ini ada
    private double totalHarga;  // Pastikan field ini ada
    private String catatanUser; // Pastikan field ini ada
    private List<CustomTripDetailModel> detailList;

    private int kotaId;
    private String namaKota;
    private int jumlahHari;

    public CustomTripModel() {
    }

    // Konstruktor lengkap (sesuaikan dengan kebutuhan Anda)
    public CustomTripModel(int id, int userId, String namaTrip, LocalDate tanggalMulai, LocalDate tanggalAkhir,
                           int jumlahPeserta, String status, double totalHarga, String catatanUser) {
        this.id = id;
        this.userId = userId;
        this.namaTrip = namaTrip;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.jumlahPeserta = jumlahPeserta;
        this.status = status;
        this.totalHarga = totalHarga;
        this.catatanUser = catatanUser;
    }

    // --- Getter dan Setter ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getNamaTrip() { return namaTrip; }
    public void setNamaTrip(String namaTrip) { this.namaTrip = namaTrip; }

    public LocalDate getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDate tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public LocalDate getTanggalAkhir() { return tanggalAkhir; }
    public void setTanggalAkhir(LocalDate tanggalAkhir) { this.tanggalAkhir = tanggalAkhir; }

    // Getter dan Setter baru untuk field dari tabel custom_trip
    public int getJumlahPeserta() { return jumlahPeserta; }
    public void setJumlahPeserta(int jumlahPeserta) { this.jumlahPeserta = jumlahPeserta; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }

    public String getCatatanUser() { return catatanUser; }
    public void setCatatanUser(String catatanUser) { this.catatanUser = catatanUser; }


    public List<CustomTripDetailModel> getDetailList() { return detailList; }
    public void setDetailList(List<CustomTripDetailModel> detailList) { this.detailList = detailList; }

    public int getKotaId() { return kotaId; }
    public void setKotaId(int kotaId) { this.kotaId = kotaId; }

    public String getNamaKota() { return namaKota; }
    public void setNamaKota(String namaKota) { this.namaKota = namaKota; }

    public int getJumlahHari() {
        if (tanggalMulai != null && tanggalAkhir != null) {
            return (int) ChronoUnit.DAYS.between(tanggalMulai, tanggalAkhir) + 1;
        }
        return jumlahHari;
    }

    public void setJumlahHari(int jumlahHari) {
        this.jumlahHari = jumlahHari;
    }
}
