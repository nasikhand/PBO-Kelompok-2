package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException; // Untuk menangani error parsing tanggal
import java.time.temporal.ChronoUnit;
import java.util.Objects; // Untuk equals dan hashCode

public class PaketPerjalananModel {
    private int id;
    private int kotaId;
    private String namaPaket;
    private String tanggalMulai; // Format "yyyy-MM-dd"
    private String tanggalAkhir; // Format "yyyy-MM-dd"
    private int kuota;
    private double harga;
    private String deskripsi;
    private String status; // ENUM('tersedia', 'penuh', 'selesai')
    private String gambar; // Path relatif ke gambar
    private double rating;
    private String namaKota;

    // Konstruktor default
    public PaketPerjalananModel() {
        // Kosong, memungkinkan pembuatan objek tanpa parameter awal
    }

    // Konstruktor untuk membuat objek baru sebelum disimpan ke DB (tanpa ID)
    public PaketPerjalananModel(int kotaId, String namaPaket, String tanggalMulai, String tanggalAkhir,
                                 int kuota, double harga, String deskripsi, String status,
                                 String gambar, double rating) {
        this.kotaId = kotaId;
        this.namaPaket = namaPaket;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.kuota = kuota;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
        this.gambar = gambar;
        this.rating = rating;
    }

    // Konstruktor lengkap (biasanya digunakan saat mengambil data dari DB)
    public PaketPerjalananModel(int id, int kotaId, String namaPaket, String tanggalMulai, String tanggalAkhir,
                                 int kuota, double harga, String deskripsi, String status,
                                 String gambar, double rating) {
        this.id = id;
        this.kotaId = kotaId;
        this.namaPaket = namaPaket;
        this.tanggalMulai = tanggalMulai;
        this.tanggalAkhir = tanggalAkhir;
        this.kuota = kuota;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.status = status;
        this.gambar = gambar;
        this.rating = rating;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getKotaId() { return kotaId; }
    public void setKotaId(int kotaId) { this.kotaId = kotaId; }

    // Tambahkan getter dan setter untuk namaKota
    public String getNamaKota() {
        return namaKota;
    }

    public void setNamaKota(String namaKota) {
        this.namaKota = namaKota;
    }

    public String getNamaPaket() { return namaPaket; }
    public void setNamaPaket(String namaPaket) { this.namaPaket = namaPaket; }

    public String getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(String tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public String getTanggalAkhir() { return tanggalAkhir; }
    public void setTanggalAkhir(String tanggalAkhir) { this.tanggalAkhir = tanggalAkhir; }

    private int jumlahHari;

    public int getJumlahHari() {
        return jumlahHari;
    }

    public void setJumlahHari(int jumlahHari) {
        this.jumlahHari = jumlahHari;
    }


    public int getKuota() { return kuota; }
    public void setKuota(int kuota) { this.kuota = kuota; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    /**
     * Menghitung durasi perjalanan dalam hari.
     * @return Durasi dalam hari, atau 0 jika tanggal tidak valid.
     */
    public long getDurasi() {
        if (this.tanggalMulai == null || this.tanggalAkhir == null ||
            this.tanggalMulai.isEmpty() || this.tanggalAkhir.isEmpty()) {
            return 0;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate mulai = LocalDate.parse(this.tanggalMulai, formatter);
            LocalDate akhir = LocalDate.parse(this.tanggalAkhir, formatter);
            if (akhir.isBefore(mulai)) {
                return 0;
            }
            return ChronoUnit.DAYS.between(mulai, akhir) + 1;
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing tanggal di getDurasi() untuk paket ID " + this.id + ": " + e.getMessage());
            return 0;
        } catch (Exception e) {
            System.err.println("Error tidak terduga di getDurasi() untuk paket ID " + this.id + ": " + e.getMessage());
            return 0;
        }
    }

    @Override
    public String toString() {
        return "PaketPerjalananModel{" +
                "id=" + id +
                ", kotaId=" + kotaId +
                ", namaPaket='" + namaPaket + '\'' +
                ", tanggalMulai='" + tanggalMulai + '\'' +
                ", tanggalAkhir='" + tanggalAkhir + '\'' +
                ", kuota=" + kuota +
                ", harga=" + harga +
                ", deskripsi='" + deskripsi + '\'' +
                ", status='" + status + '\'' +
                ", gambar='" + gambar + '\'' +
                ", rating=" + rating +
                ", durasi=" + getDurasi() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaketPerjalananModel that = (PaketPerjalananModel) o;
        return id == that.id &&
               kotaId == that.kotaId &&
               kuota == that.kuota &&
               Double.compare(that.harga, harga) == 0 &&
               Double.compare(that.rating, rating) == 0 &&
               Objects.equals(namaPaket, that.namaPaket) &&
               Objects.equals(tanggalMulai, that.tanggalMulai) &&
               Objects.equals(tanggalAkhir, that.tanggalAkhir) &&
               Objects.equals(deskripsi, that.deskripsi) &&
               Objects.equals(status, that.status) &&
               Objects.equals(gambar, that.gambar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, kotaId, namaPaket, tanggalMulai, tanggalAkhir, kuota, harga, deskripsi, status, gambar, rating);
    }
}
