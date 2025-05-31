package controller;

import db.Koneksi; 
import db.dao.KotaDAO;
import db.dao.PaketPerjalananDAO;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import model.PaketPerjalananModel;


public class PaketPerjalananController {
    private PaketPerjalananDAO paketDao;
    private KotaDAO kotaDao; 
    private Connection connection; 
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public PaketPerjalananController(Connection connection) {
        this.connection = connection;
        if (this.connection != null) {
            this.paketDao = new PaketPerjalananDAO(this.connection);
            this.kotaDao = new KotaDAO(this.connection); 
        } else {
            System.err.println("Koneksi null di konstruktor PaketPerjalananController. DAO tidak diinisialisasi.");
        }
    }

    public PaketPerjalananController() {
        this.connection = Koneksi.getConnection();
        if (this.connection == null) {
            System.err.println("Gagal mendapatkan koneksi database di PaketPerjalananController (konstruktor default).");
            this.paketDao = new PaketPerjalananDAO(); 
            this.kotaDao = new KotaDAO();
        } else {
            this.paketDao = new PaketPerjalananDAO(this.connection);
            this.kotaDao = new KotaDAO(this.connection);
        }
    }

    public List<PaketPerjalananModel> getAllPaket() {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (getAllPaket).");
            return new ArrayList<>();
        }
        return paketDao.getAllPaket();
    }

    public PaketPerjalananModel getPaketById(int id) {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (getPaketById).");
            return null;
        }
        return paketDao.getById(id);
    }

    /**
     * Mencari paket perjalanan berdasarkan sebagian NAMA PAKET.
     * @param namaPaketSubstring Bagian dari nama paket yang dicari.
     * @return List PaketPerjalananModel yang cocok.
     */
    public List<PaketPerjalananModel> cariPaketByNamaPaket(String namaPaketSubstring) {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (cariPaketByNamaPaket).");
            return new ArrayList<>();
        }
        return paketDao.getPaketByNama(namaPaketSubstring);
    }

    /**
     * Mencari paket perjalanan berdasarkan NAMA KOTA.
     * @param namaKota Nama kota yang dicari.
     * @return List PaketPerjalananModel yang cocok.
     */
    public List<PaketPerjalananModel> cariPaketByNamaKota(String namaKota) {
        if (paketDao == null || kotaDao == null) {
            System.err.println("DAO tidak diinisialisasi di PaketPerjalananController (cariPaketByNamaKota).");
            return new ArrayList<>();
        }
        int kotaId = kotaDao.getKotaIdByNama(namaKota);
        if (kotaId != -1) {
            return paketDao.getPaketByKotaId(kotaId);
        }
        System.out.println("[Controller] Kota '" + namaKota + "' tidak ditemukan ID-nya.");
        return new ArrayList<>(); 
    }


    public List<PaketPerjalananModel> getTopRatedPakets(int limit) {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (getTopRatedPakets).");
            return new ArrayList<>();
        }
        return paketDao.getTopRatedPakets(limit);
    }

    public boolean tambahPaket(PaketPerjalananModel paket) {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (tambahPaket).");
            return false;
        }
        return paketDao.save(paket);
    }

    public boolean updatePaket(PaketPerjalananModel paket) {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (updatePaket).");
            return false;
        }
        return paketDao.update(paket);
    }

    public boolean hapusPaket(int id) {
        if (paketDao == null) {
            System.err.println("PaketPerjalananDAO tidak diinisialisasi di controller (hapusPaket).");
            return false;
        }
        return paketDao.delete(id);
    }
    
    /**
     * Memfilter dan mengurutkan daftar paket perjalanan berdasarkan kriteria.
     * @param namaKota Nama kota untuk filter (bisa null atau kosong jika tidak ada filter kota).
     * @param tanggalStr String tanggal pencarian (format yyyy-MM-dd, bisa null atau kosong).
     * @param urutkan Kriteria pengurutan (misalnya, "Harga: Rendah ke Tinggi").
     * @param durasiStr Kriteria durasi (misalnya, "1-3 Hari").
     * @return List PaketPerjalananModel yang sudah difilter dan diurutkan.
     */
    public List<PaketPerjalananModel> filterAndSortPaket(String namaKota, String tanggalStr, String urutkan, String durasiStr) {
        if (paketDao == null || kotaDao == null) {
            System.err.println("DAO tidak diinisialisasi di controller (filterAndSortPaket).");
            return new ArrayList<>();
        }

        List<PaketPerjalananModel> hasil;
        // Langkah 1: Ambil paket berdasarkan nama kota jika ada, jika tidak ambil semua
        if (namaKota != null && !namaKota.isEmpty() && !namaKota.equals("-- Pilih Kota --")){ 
            int kotaId = kotaDao.getKotaIdByNama(namaKota);
            if (kotaId != -1) {
                hasil = paketDao.getPaketByKotaId(kotaId);
            } else {
                System.out.println("[Controller] Kota '" + namaKota + "' tidak ditemukan untuk filter, menampilkan semua paket.");
                hasil = paketDao.getAllPaket(); // Atau kembalikan list kosong jika kota harus valid
            }
        } else {
            hasil = paketDao.getAllPaket(); 
        }

        // Langkah 2: Filter berdasarkan tanggal jika ada
        if (tanggalStr != null && !tanggalStr.isEmpty()) {
            try {
                LocalDate tanggalCari = LocalDate.parse(tanggalStr, dateFormatter);
                hasil = hasil.stream()
                             .filter(p -> tanggalBeradaDalamRentangPaket(tanggalCari, p.getTanggalMulai(), p.getTanggalAkhir()))
                             .collect(Collectors.toList());
            } catch (DateTimeParseException e) {
                System.err.println("Format tanggal pencarian tidak valid: " + tanggalStr + " - " + e.getMessage());
                // Mungkin tampilkan pesan error atau abaikan filter tanggal
            }
        }
        
        // Langkah 3: Filter berdasarkan durasi
        if (durasiStr != null && !durasiStr.equals("...") && !durasiStr.equalsIgnoreCase("Semua Durasi")) {
            final String finalDurasi = durasiStr;
            hasil = hasil.stream().filter(p -> {
                long durasiPaket = p.getDurasi(); // getDurasi() sudah ada di model
                if (finalDurasi.equals("1-3 Hari")) return durasiPaket >= 1 && durasiPaket <= 3;
                if (finalDurasi.equals("4-6 Hari")) return durasiPaket >= 4 && durasiPaket <= 6;
                if (finalDurasi.equals("7+ Hari")) return durasiPaket >= 7;
                return true; 
            }).collect(Collectors.toList());
        }

        // Langkah 4: Pengurutan
        if (urutkan != null && !urutkan.equals("...") && !urutkan.equals("Relevansi")) { 
            if (urutkan.equals("Harga: Rendah ke Tinggi")) {
                hasil.sort(Comparator.comparingDouble(PaketPerjalananModel::getHarga));
            } else if (urutkan.equals("Harga: Tinggi ke Rendah")) {
                hasil.sort(Comparator.comparingDouble(PaketPerjalananModel::getHarga).reversed());
            } else if (urutkan.equals("Rating Tertinggi")) {
                hasil.sort(Comparator.comparingDouble(PaketPerjalananModel::getRating).reversed());
            }
        }
        return hasil;
    }

    // Helper method untuk cek apakah suatu tanggal berada di antara dua tanggal lain (inklusif)
    // atau jika tanggal mulai paket sama dengan tanggal cari
    private boolean tanggalBeradaDalamRentangPaket(LocalDate tanggalCari, String tanggalMulaiPaketStr, String tanggalAkhirPaketStr) {
        if (tanggalMulaiPaketStr == null || tanggalMulaiPaketStr.isEmpty() || 
            tanggalAkhirPaketStr == null || tanggalAkhirPaketStr.isEmpty()) {
            return false; // Paket tidak memiliki rentang tanggal yang valid
        }
        try {
            LocalDate tanggalMulaiPaket = LocalDate.parse(tanggalMulaiPaketStr, dateFormatter);
            LocalDate tanggalAkhirPaket = LocalDate.parse(tanggalAkhirPaketStr, dateFormatter);
            
            // Cek apakah tanggalCari sama dengan tanggalMulaiPaket
            // ATAU tanggalCari berada di antara tanggalMulaiPaket dan tanggalAkhirPaket (inklusif)
            return tanggalCari.equals(tanggalMulaiPaket) || 
                   (!tanggalCari.isBefore(tanggalMulaiPaket) && !tanggalCari.isAfter(tanggalAkhirPaket));
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing tanggal paket: " + tanggalMulaiPaketStr + " atau " + tanggalAkhirPaketStr + " - " + e.getMessage());
            return false; 
        }
    }
}
