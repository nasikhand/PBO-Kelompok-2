package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // For equals and hashCode, if needed

public class CustomTripModel {
    private int id;
    private int userId;
    private String namaTrip;
    private LocalDate tanggalMulai;
    private LocalDate tanggalAkhir;
    private int jumlahPeserta;
    private String status;
    private double totalHarga;
    private String catatanUser;
    
    // Detailed list of trip items (like destinations, activities) which are stored in rincian_custom_trip
    // This is different from the overall trip properties.
    private List<CustomTripDetailModel> detailList; 

    // Fields from the 'custom_trip' table that represent the whole trip's details
    private String detailedTransportMode;
    private String detailedTransportDetails;
    private String detailedAccommodationName;
    private String detailedRoomType;
    private String detailedAccommodationNotes;
    private String activitiesSummary; // The joined string of activities for DB storage

    // Fields used for display or internal logic, derived from other data
    private String namaKota; // Derived from namaTrip or initial destination
    private int jumlahHari; // Derived from tanggalMulai and tanggalAkhir
    private List<String> detailedDestinations; // List of destination names (e.g., from rincian_custom_trip)
    private List<String> detailedActivities;   // List of activity names (parsed from activitiesSummary)
    private int kotaId;

    public CustomTripModel() {
        this.detailedDestinations = new ArrayList<>();
        this.detailedActivities = new ArrayList<>();
        this.detailList = new ArrayList<>(); // Initialize the list for CustomTripDetailModel
    }

    // Full constructor (consider adding new fields here if you need to create models fully populated)
    public CustomTripModel(int id, int userId, String namaTrip, LocalDate tanggalMulai, LocalDate tanggalAkhir,
                           int jumlahPeserta, String status, double totalHarga, String catatanUser,
                           String detailedTransportMode, String detailedTransportDetails,
                           String detailedAccommodationName, String detailedRoomType,
                           String detailedAccommodationNotes, String activitiesSummary) {
        this(); // Call default constructor to initialize lists
        this.id = id;
        this.userId = userId;
        this.namaTrip = namaTrip;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.jumlahPeserta = jumlahPeserta;
        this.status = status;
        this.totalHarga = totalHarga;
        this.catatanUser = catatanUser;
        this.detailedTransportMode = detailedTransportMode;
        this.detailedTransportDetails = detailedTransportDetails;
        this.detailedAccommodationName = detailedAccommodationName;
        this.detailedRoomType = detailedRoomType;
        this.detailedAccommodationNotes = detailedAccommodationNotes;
        this.activitiesSummary = activitiesSummary;
        // If activitiesSummary is not null, parse it into detailedActivities
        if (activitiesSummary != null && !activitiesSummary.isEmpty()) {
            this.detailedActivities = new ArrayList<>(List.of(activitiesSummary.split(";")));
        }
    }


    // --- Existing Getters and Setters ---
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

    public int getKotaId() { return getKotaId(); }
    public void setKotaId(int kotaId) { this.kotaId = kotaId; }

    public String getNamaKota() { return namaKota; }
    public void setNamaKota(String namaKota) { this.namaKota = namaKota; }

    // This method calculates on the fly; it's a getter, not a setter for a stored field
    public int getJumlahHari() {
        if (tanggalMulai != null && tanggalAkhir != null) {
            return (int) ChronoUnit.DAYS.between(tanggalMulai, tanggalAkhir) + 1;
        }
        return jumlahHari; // Fallback if dates are not set or calculated externally
    }
    public void setJumlahHari(int jumlahHari) { // If you need to explicitly set it
        this.jumlahHari = jumlahHari;
    }


    // --- New Getters and Setters for Detailed Trip Components ---
    public String getDetailedTransportMode() { return detailedTransportMode; }
    public void setDetailedTransportMode(String detailedTransportMode) { this.detailedTransportMode = detailedTransportMode; }

    public String getDetailedTransportDetails() { return detailedTransportDetails; }
    public void setDetailedTransportDetails(String detailedTransportDetails) { this.detailedTransportDetails = detailedTransportDetails; }

    public String getDetailedAccommodationName() { return detailedAccommodationName; }
    public void setDetailedAccommodationName(String detailedAccommodationName) { this.detailedAccommodationName = detailedAccommodationName; }

    public String getDetailedRoomType() { return detailedRoomType; }
    public void setDetailedRoomType(String detailedRoomType) { this.detailedRoomType = detailedRoomType; }

    public String getDetailedAccommodationNotes() { return detailedAccommodationNotes; }
    public void setDetailedAccommodationNotes(String detailedAccommodationNotes) { this.detailedAccommodationNotes = detailedAccommodationNotes; }

    public String getActivitiesSummary() { return activitiesSummary; }
    public void setActivitiesSummary(String activitiesSummary) { this.activitiesSummary = activitiesSummary; }

    public List<String> getDetailedDestinations() { return detailedDestinations; }
    public void setDetailedDestinations(List<String> detailedDestinations) { this.detailedDestinations = detailedDestinations; }

    public List<String> getDetailedActivities() { return detailedActivities; }
    public void setDetailedActivities(List<String> detailedActivities) { 
        this.detailedActivities = detailedActivities; 
        // Also update the activitiesSummary string when the list is set
        if (detailedActivities != null && !detailedActivities.isEmpty()) {
            this.activitiesSummary = String.join(";", detailedActivities);
        } else {
            this.activitiesSummary = "";
        }
    }

    @Override
    public String toString() {
        return "CustomTripModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", namaTrip='" + namaTrip + '\'' +
                ", tanggalMulai=" + tanggalMulai +
                ", tanggalAkhir=" + tanggalAkhir +
                ", jumlahPeserta=" + jumlahPeserta +
                ", status='" + status + '\'' +
                ", totalHarga=" + totalHarga +
                ", catatanUser='" + catatanUser + '\'' +
                ", detailedTransportMode='" + detailedTransportMode + '\'' +
                ", detailedTransportDetails='" + detailedTransportDetails + '\'' +
                ", detailedAccommodationName='" + detailedAccommodationName + '\'' +
                ", detailedRoomType='" + detailedRoomType + '\'' +
                ", detailedAccommodationNotes='" + detailedAccommodationNotes + '\'' +
                ", activitiesSummary='" + activitiesSummary + '\'' +
                ", namaKota='" + namaKota + '\'' +
                ", jumlahHari=" + getJumlahHari() +
                ", detailedDestinations=" + detailedDestinations +
                ", detailedActivities=" + detailedActivities +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomTripModel that = (CustomTripModel) o;
        return id == that.id &&
                userId == that.userId &&
                jumlahPeserta == that.jumlahPeserta &&
                Double.compare(that.totalHarga, totalHarga) == 0 &&
                Objects.equals(namaTrip, that.namaTrip) &&
                Objects.equals(tanggalMulai, that.tanggalMulai) &&
                Objects.equals(tanggalAkhir, that.tanggalAkhir) &&
                Objects.equals(status, that.status) &&
                Objects.equals(catatanUser, that.catatanUser) &&
                Objects.equals(detailedTransportMode, that.detailedTransportMode) &&
                Objects.equals(detailedTransportDetails, that.detailedTransportDetails) &&
                Objects.equals(detailedAccommodationName, that.detailedAccommodationName) &&
                Objects.equals(detailedRoomType, that.detailedRoomType) &&
                Objects.equals(detailedAccommodationNotes, that.detailedAccommodationNotes) &&
                Objects.equals(activitiesSummary, that.activitiesSummary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, namaTrip, tanggalMulai, tanggalAkhir, jumlahPeserta, status, totalHarga, catatanUser,
                detailedTransportMode, detailedTransportDetails, detailedAccommodationName, detailedRoomType,
                detailedAccommodationNotes, activitiesSummary);
    }

    public int getDurasi() {
        if (tanggalMulai != null && tanggalAkhir != null) {
            // Ensure end date is not before start date
            if (tanggalAkhir.isBefore(tanggalMulai)) {
                return 0; // Invalid date range
            }
            return (int) ChronoUnit.DAYS.between(tanggalMulai, tanggalAkhir) + 1; // +1 for inclusive days
        }
        return 0; // Return 0 if dates are not set
    }
}
