package db.dao;

import static com.mysql.cj.util.TimeUtil.DATE_FORMATTER;
import db.Koneksi;
import java.sql.*; // Untuk mendapatkan koneksi jika tidak di-inject
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.PaketPerjalananModel;
import java.time.format.DateTimeFormatter;

public class PaketPerjalananDAO {
    private Connection conn;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Konstruktor default, bisa mengambil koneksi dari kelas Koneksi
    public PaketPerjalananDAO() {
        this.conn = Koneksi.getConnection(); // Mengambil koneksi dari kelas Koneksi
                                           // Pastikan Koneksi.getConnection() sudah diimplementasikan dengan benar
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh PaketPerjalananDAO.");
            // Pertimbangkan untuk throw exception di sini atau handle lebih lanjut
        }
    }
    
    // Konstruktor untuk injeksi koneksi (baik untuk testing atau manajemen koneksi eksternal)
    public PaketPerjalananDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Menyimpan paket perjalanan baru ke database.
     * @param paket Objek PaketPerjalananModel yang akan disimpan.
     * @return true jika berhasil disimpan, false jika gagal.
     */
    public boolean save(PaketPerjalananModel paket) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi save.");
            return false;
        }
        // Kolom di query harus sama dengan di tabel: id, kota_id, nama_paket, tanggal_mulai, tanggal_akhir, kuota, harga, deskripsi, status, gambar, rating
        String query = "INSERT INTO paket_perjalanan (kota_id, nama_paket, tanggal_mulai, tanggal_akhir, kuota, harga, deskripsi, status, gambar, rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, paket.getKotaId());
            ps.setString(2, paket.getNamaPaket());
            // Konversi String tanggal ke java.sql.Date
            ps.setDate(3, Date.valueOf(LocalDate.parse(paket.getTanggalMulai(), dateFormatter)));
            ps.setDate(4, Date.valueOf(LocalDate.parse(paket.getTanggalAkhir(), dateFormatter)));
            ps.setInt(5, paket.getKuota());
            ps.setDouble(6, paket.getHarga());
            ps.setString(7, paket.getDeskripsi());
            ps.setString(8, paket.getStatus());
            ps.setString(9, paket.getGambar());
            ps.setDouble(10, paket.getRating());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        paket.setId(generatedKeys.getInt(1)); // Set ID yang digenerate ke objek model
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saat menyimpan paket perjalanan: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // Tangkap DateTimeParseException atau error konversi lainnya
            System.err.println("Error konversi tanggal saat menyimpan paket: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Mengupdate data paket perjalanan yang sudah ada di database.
     * @param paket Objek PaketPerjalananModel dengan data yang akan diupdate. ID harus ada.
     * @return true jika berhasil diupdate, false jika gagal.
     */
    public boolean update(PaketPerjalananModel paket) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi update.");
            return false;
        }
        String query = "UPDATE paket_perjalanan SET kota_id = ?, nama_paket = ?, tanggal_mulai = ?, tanggal_akhir = ?, kuota = ?, harga = ?, deskripsi = ?, status = ?, gambar = ?, rating = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, paket.getKotaId());
            ps.setString(2, paket.getNamaPaket());
            ps.setDate(3, Date.valueOf(LocalDate.parse(paket.getTanggalMulai(), dateFormatter)));
            ps.setDate(4, Date.valueOf(LocalDate.parse(paket.getTanggalAkhir(), dateFormatter)));
            ps.setInt(5, paket.getKuota());
            ps.setDouble(6, paket.getHarga());
            ps.setString(7, paket.getDeskripsi());
            ps.setString(8, paket.getStatus());
            ps.setString(9, paket.getGambar());
            ps.setDouble(10, paket.getRating());
            ps.setInt(11, paket.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate paket perjalanan: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { // Tangkap DateTimeParseException atau error konversi lainnya
            System.err.println("Error konversi tanggal saat mengupdate paket: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Menghapus paket perjalanan dari database berdasarkan ID.
     * @param id ID paket perjalanan yang akan dihapus.
     * @return true jika berhasil dihapus, false jika gagal.
     */
    public boolean delete(int id) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi delete.");
            return false;
        }
        String query = "DELETE FROM paket_perjalanan WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saat menghapus paket perjalanan: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public List<PaketPerjalananModel> getAllPaket() {
        List<PaketPerjalananModel> list = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getAllPaket.");
            return list; // Kembalikan list kosong
        }
        String query = "SELECT * FROM paket_perjalanan";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                PaketPerjalananModel p = new PaketPerjalananModel(
                        rs.getInt("id"),
                        rs.getInt("kota_id"),
                        rs.getString("nama_paket"),
                        rs.getString("tanggal_mulai"), // Diambil sebagai String, sudah sesuai model
                        rs.getString("tanggal_akhir"),// Diambil sebagai String, sudah sesuai model
                        rs.getInt("kuota"),
                        rs.getDouble("harga"),
                        rs.getString("deskripsi"),
                        rs.getString("status"),
                        rs.getString("gambar"),
                        rs.getDouble("rating")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil semua paket perjalanan: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public PaketPerjalananModel getById(int id) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getById.");
            return null;
        }
        String query = "SELECT * FROM paket_perjalanan WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new PaketPerjalananModel(
                        rs.getInt("id"),
                        rs.getInt("kota_id"),
                        rs.getString("nama_paket"),
                        rs.getString("tanggal_mulai"),
                        rs.getString("tanggal_akhir"),
                        rs.getInt("kuota"),
                        rs.getDouble("harga"),
                        rs.getString("deskripsi"),
                        rs.getString("status"),
                        rs.getString("gambar"),
                        rs.getDouble("rating")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil paket perjalanan by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<PaketPerjalananModel> getPaketByNama(String namaPaketSubstring) {
        List<PaketPerjalananModel> list = new ArrayList<>();
         if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getPaketByNama.");
            return list;
        }
        String sql = "SELECT * FROM paket_perjalanan WHERE nama_paket LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + namaPaketSubstring + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PaketPerjalananModel p = new PaketPerjalananModel();
                p.setId(rs.getInt("id"));
                p.setKotaId(rs.getInt("kota_id"));
                p.setNamaPaket(rs.getString("nama_paket"));
                p.setTanggalMulai(rs.getString("tanggal_mulai"));
                p.setTanggalAkhir(rs.getString("tanggal_akhir"));
                p.setKuota(rs.getInt("kuota"));
                p.setHarga(rs.getDouble("harga"));
                p.setDeskripsi(rs.getString("deskripsi"));
                p.setStatus(rs.getString("status"));
                p.setGambar(rs.getString("gambar"));
                p.setRating(rs.getDouble("rating"));
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari paket by nama: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    public List<PaketPerjalananModel> getPaketByKotaId(int kotaId) {
        List<PaketPerjalananModel> list = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getPaketByKotaId.");
            return list;
        }
        String query = "SELECT * FROM paket_perjalanan WHERE kota_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, kotaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PaketPerjalananModel p = new PaketPerjalananModel(
                    rs.getInt("id"),
                    rs.getInt("kota_id"),
                    rs.getString("nama_paket"),
                    rs.getString("tanggal_mulai"),
                    rs.getString("tanggal_akhir"),
                    rs.getInt("kuota"),
                    rs.getDouble("harga"),
                    rs.getString("deskripsi"),
                    rs.getString("status"),
                    rs.getString("gambar"),
                    rs.getDouble("rating")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil paket perjalanan by Kota ID: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }


    public List<PaketPerjalananModel> getTopRatedPakets(int limit) {
        List<PaketPerjalananModel> list = new ArrayList<>();
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk getTopRatedPakets.");
            return list;
        }
        String query = "SELECT * FROM paket_perjalanan ORDER BY rating DESC LIMIT ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PaketPerjalananModel paket = new PaketPerjalananModel();
                    paket.setId(rs.getInt("id"));
                    paket.setKotaId(rs.getInt("kota_id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));
                    paket.setTanggalMulai(rs.getString("tanggal_mulai"));
                    paket.setTanggalAkhir(rs.getString("tanggal_akhir"));
                    paket.setKuota(rs.getInt("kuota"));
                    paket.setHarga(rs.getDouble("harga"));
                    paket.setDeskripsi(rs.getString("deskripsi"));
                    paket.setStatus(rs.getString("status"));
                    paket.setGambar(rs.getString("gambar"));
                    paket.setRating(rs.getDouble("rating"));
                    list.add(paket);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil top rated paket: " + e.getMessage());
            e.printStackTrace(); 
        }
        return list;
    }

    public List<PaketPerjalananModel> getPreviousTripsByUser(int userId) {
    List<PaketPerjalananModel> list = new ArrayList<>();

    String sql = "SELECT pp.* FROM paket_perjalanan pp " +
                 "JOIN reservasi r ON pp.id = r.trip_id " +
                 "WHERE r.trip_type = 'paket_perjalanan' " +
                 "AND r.status = 'selesai' " +
                 "AND r.user_id = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            PaketPerjalananModel paket = new PaketPerjalananModel();
            paket.setId(rs.getInt("id"));
            paket.setNamaPaket(rs.getString("nama_paket"));
            paket.setDeskripsi(rs.getString("deskripsi"));
            paket.setHarga(rs.getDouble("harga"));
            paket.setTanggalMulai(rs.getString("tanggal_mulai"));
            // tambahkan field lain jika ada

            list.add(paket);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

 public PaketPerjalananModel getPaketPerjalananById(int id) { // Metode ini menerima 'int'
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi getPaketPerjalananById.");
            return null;
        }

        PaketPerjalananModel paket = null;
        String sql = "SELECT pp.*, k.nama_kota FROM paket_perjalanan pp " +
                     "JOIN kota k ON pp.kota_id = k.id WHERE pp.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    paket = new PaketPerjalananModel();
                    paket.setId(rs.getInt("id"));
                    paket.setKotaId(rs.getInt("kota_id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));

                    java.sql.Date sqlTanggalMulai = rs.getDate("tanggal_mulai");
                    if (sqlTanggalMulai != null) {
                        // Konversi java.sql.Date ke LocalDate lalu format ke String
                        paket.setTanggalMulai(sqlTanggalMulai.toLocalDate().format(DATE_FORMATTER));
                    } else {
                        paket.setTanggalMulai(null); // Penting: set null jika memang null dari DB
                    }

                    java.sql.Date sqlTanggalAkhir = rs.getDate("tanggal_akhir");
                    if (sqlTanggalAkhir != null) {
                        // Konversi java.sql.Date ke LocalDate lalu format ke String
                        paket.setTanggalAkhir(sqlTanggalAkhir.toLocalDate().format(DATE_FORMATTER));
                    } else {
                        paket.setTanggalAkhir(null); // Penting: set null jika memang null dari DB
                    }

                    paket.setKuota(rs.getInt("kuota"));
                    paket.setHarga(rs.getDouble("harga"));
                    paket.setDeskripsi(rs.getString("deskripsi"));
                    paket.setStatus(rs.getString("status"));
                    paket.setGambar(rs.getString("gambar"));
                    paket.setRating(rs.getDouble("rating"));

                    paket.setNamaKota(rs.getString("nama_kota"));
                    
                    // Jumlah hari dihitung di PaketPerjalananModel.getDurasi()
                    paket.setJumlahHari((int) paket.getDurasi());

                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil paket perjalanan dengan ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return paket;
    }

    
}