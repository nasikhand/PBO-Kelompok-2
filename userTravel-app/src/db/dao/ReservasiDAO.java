package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import model.CustomTripModel;
import model.PaketPerjalananModel;
import model.ReservasiModel;

public class ReservasiDAO {
    private Connection conn;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReservasiDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh ReservasiDAO.");
        }
    }

    public ReservasiDAO(Connection conn) {
        this.conn = conn;
    }

    public int save(ReservasiModel reservasi) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi save Reservasi.");
            return -1;
        }
        String sql = "INSERT INTO reservasi (user_id, trip_type, trip_id, kode_reservasi, tanggal_reservasi, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reservasi.getUserId());
            ps.setString(2, reservasi.getTripType());

            if (reservasi.getTripId() != null) {
                ps.setInt(3, reservasi.getTripId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setString(4, reservasi.getKodeReservasi());
            if (reservasi.getTanggalReservasi() != null) {
                ps.setDate(5, Date.valueOf(reservasi.getTanggalReservasi()));
            } else {
                ps.setDate(5, Date.valueOf(LocalDate.now()));
            }
            ps.setString(6, reservasi.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idBaru = generatedKeys.getInt(1);
                        System.out.println("✅ ID reservasi berhasil disimpan: " + idBaru);
                        return idBaru;
                    }
                }
            } else {
                System.err.println("❌ Tidak ada baris yang disisipkan ke database.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error saat menyimpan reservasi: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Menghapus reservasi dan data terkait (penumpang, pembayaran) dari database.
     * @param reservasiId ID reservasi yang akan dihapus.
     * @return true jika berhasil menghapus, false jika gagal.
     */
    public boolean deleteReservasi(int reservasiId) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi delete Reservasi.");
            return false;
        }
        try {
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. Hapus data di tabel 'pembayaran' terkait reservasi ini
            String sqlDeletePembayaran = "DELETE FROM pembayaran WHERE reservasi_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeletePembayaran)) {
                ps.setInt(1, reservasiId);
                ps.executeUpdate();
                System.out.println("DEBUG ReservasiDAO - Dihapus pembayaran untuk reservasi ID: " + reservasiId);
            }

            // 2. Hapus data di tabel 'penumpang' terkait reservasi ini
            String sqlDeletePenumpang = "DELETE FROM penumpang WHERE reservasi_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeletePenumpang)) {
                ps.setInt(1, reservasiId);
                ps.executeUpdate();
                System.out.println("DEBUG ReservasiDAO - Dihapus penumpang untuk reservasi ID: " + reservasiId);
            }

            // 3. Hapus reservasi itu sendiri
            String sqlDeleteReservasi = "DELETE FROM reservasi WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteReservasi)) {
                ps.setInt(1, reservasiId);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // Commit transaksi jika berhasil
                    System.out.println("✅ Reservasi ID: " + reservasiId + " berhasil dihapus.");
                    return true;
                } else {
                    conn.rollback(); // Rollback jika tidak ada baris yang terpengaruh (reservasi tidak ditemukan)
                    System.err.println("❌ Reservasi ID: " + reservasiId + " tidak ditemukan atau tidak dapat dihapus.");
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback jika ada error
            } catch (SQLException ex) {
                System.err.println("Error saat rollback: " + ex.getMessage());
            }
            System.err.println("❌ Error saat menghapus reservasi: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Kembalikan auto-commit ke true
            } catch (SQLException ex) {
                System.err.println("Error saat mengembalikan auto-commit: " + ex.getMessage());
            }
        }
    }


    public List<ReservasiModel> getHistoryReservasiByUser(int userId) {
        List<ReservasiModel> listReservasi = new ArrayList<>();
        // Query ini mengambil reservasi yang statusnya 'dibayar' atau 'selesai'
        String sql = "SELECT r.*, " +
                 "pp.nama_paket, pp.tanggal_mulai AS pp_tgl_mulai, pp.tanggal_akhir AS pp_tgl_akhir, k_pp.nama_kota AS pp_nama_kota, " +
                 "ct.nama_trip, ct.tanggal_mulai AS ct_tgl_mulai, ct.tanggal_akhir AS ct_tgl_akhir " +
                 "FROM reservasi r " +
                 "LEFT JOIN paket_perjalanan pp ON r.trip_id = pp.id AND r.trip_type = 'paket_perjalanan' " +
                 "LEFT JOIN kota k_pp ON pp.kota_id = k_pp.id " +
                 "LEFT JOIN custom_trip ct ON r.trip_id = ct.id AND r.trip_type = 'custom_trip' " +
                 "WHERE r.user_id = ? AND r.status IN ('selesai') " +
                 "ORDER BY r.tanggal_reservasi DESC";
        
        try (Connection conn = this.conn != null ? this.conn : Koneksi.getConnection(); // Gunakan koneksi yang ada atau buat baru
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            // Inisialisasi DAO lain di luar loop untuk efisiensi
            PaketPerjalananDAO paketDAO = new PaketPerjalananDAO(conn);
            CustomTripDAO customTripDAO = new CustomTripDAO(conn);

            while (rs.next()) {
                ReservasiModel reservasi = new ReservasiModel();
                reservasi.setId(rs.getInt("id"));
                reservasi.setUserId(rs.getInt("user_id"));
                reservasi.setTripType(rs.getString("trip_type"));
                reservasi.setTripId(rs.getInt("trip_id"));
                reservasi.setKodeReservasi(rs.getString("kode_reservasi"));
                
                java.sql.Date tgl = rs.getDate("tanggal_reservasi");
                if (tgl != null) {
                    reservasi.setTanggalReservasi(tgl.toLocalDate());
                }
                reservasi.setStatus(rs.getString("status"));

                // Muat detail trip berdasarkan trip_type
                if ("paket_perjalanan".equals(reservasi.getTripType())) {
                    reservasi.setPaket(paketDAO.getById(reservasi.getTripId()));
                } else if ("custom_trip".equals(reservasi.getTripType())) {
                    reservasi.setCustomTrip(customTripDAO.getById(reservasi.getTripId()));
                }
                listReservasi.add(reservasi);
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil riwayat reservasi: " + e.getMessage());
            e.printStackTrace();
        }
        return listReservasi;
    }

    private PaketPerjalananModel getPaketById(int id) {
        if (this.conn == null) return null;
        PaketPerjalananDAO dao = new PaketPerjalananDAO(conn);
        return dao.getPaketPerjalananById(id);
    }

    private CustomTripModel getCustomTripById(int id) {
        if (this.conn == null) return null;
        CustomTripDAO dao = new CustomTripDAO(conn);
        return dao.getById(id);
    }

    public ReservasiModel getReservasiById(int reservasiId) {
    if (this.conn == null) {
        System.err.println("ERROR ReservasiDAO.getReservasiById: Connection is NULL!");
        return null;
    }
        String sql = "SELECT * FROM reservasi WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservasiId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ReservasiModel r = new ReservasiModel();
                r.setId(rs.getInt("id"));
                r.setUserId(rs.getInt("user_id"));
                r.setTripType(rs.getString("trip_type"));
                r.setTripId((Integer) rs.getObject("trip_id"));
                r.setKodeReservasi(rs.getString("kode_reservasi"));
                java.sql.Date sqlDate = rs.getDate("tanggal_reservasi");
                if (sqlDate != null) {
                    r.setTanggalReservasi(sqlDate.toLocalDate());
                }
                r.setStatus(rs.getString("status"));

                if ("paket_perjalanan".equals(r.getTripType()) && r.getTripId() != null) {
                    r.setPaket(getPaketById(r.getTripId()));
                } else if ("custom_trip".equals(r.getTripType()) && r.getTripId() != null) {
                    r.setCustomTrip(getCustomTripById(r.getTripId()));
                }
                return r;
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil reservasi by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStatusReservasi(int reservasiId, String newStatus) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk updateStatusReservasi.");
            return false;
        }
        String sql = "UPDATE reservasi SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, reservasiId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error saat mengupdate status reservasi: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<ReservasiModel> getReservasiAktifDenganTrip(int userId) throws SQLException {
    List<ReservasiModel> list = new ArrayList<>();

    // --- FIXED: Query sekarang melakukan JOIN untuk mendapatkan nama kota dari custom trip ---
    String sql = "SELECT r.id, r.user_id, r.trip_type, r.trip_id, r.kode_reservasi, r.tanggal_reservasi, r.status AS status_reservasi, " +
                 "p.id AS paket_id, p.nama_paket, p.rating AS paket_rating, k_paket.nama_kota AS paket_nama_kota, p.harga AS paket_harga, " +
                 "p.tanggal_mulai AS paket_mulai, p.tanggal_akhir AS paket_akhir, p.gambar AS paket_gambar, " +
                 "c.tanggal_mulai AS custom_mulai, c.tanggal_akhir AS custom_akhir, " +
                 "c.id AS custom_id, c.nama_trip AS custom_nama_trip, c.jumlah_peserta AS custom_jumlah_peserta, c.total_harga AS custom_total_harga, c.status AS custom_status, " +
                 "k_custom.nama_kota AS custom_nama_kota " + // <-- Kolom baru untuk nama kota custom trip
                 "FROM reservasi r " +
                 "LEFT JOIN paket_perjalanan p ON r.trip_type = 'paket_perjalanan' AND r.trip_id = p.id " +
                 "LEFT JOIN kota k_paket ON p.kota_id = k_paket.id " +
                 "LEFT JOIN custom_trip c ON r.trip_type = 'custom_trip' AND r.trip_id = c.id " +
                 // JOIN untuk menemukan kota dari destinasi pertama sebuah custom trip
                 "LEFT JOIN rincian_custom_trip rct ON c.id = rct.custom_trip_id AND rct.urutan_kunjungan = 1 " +
                 "LEFT JOIN destinasi d ON rct.destinasi_id = d.id " +
                 "LEFT JOIN kota k_custom ON d.kota_id = k_custom.id " +
                 "WHERE r.user_id = ? AND r.status IN ('pending', 'dibayar', 'dipesan', 'draft')"; // Menambahkan 'draft' jika perlu

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ReservasiModel reservasi = new ReservasiModel();
                reservasi.setId(rs.getInt("id"));
                reservasi.setUserId(rs.getInt("user_id"));
                String tripType = rs.getString("trip_type");
                reservasi.setTripType(tripType);
                reservasi.setKodeReservasi(rs.getString("kode_reservasi"));
                reservasi.setStatus(rs.getString("status_reservasi"));

                java.sql.Date sqlReservasiDate = rs.getDate("tanggal_reservasi");
                if (sqlReservasiDate != null) {
                    reservasi.setTanggalReservasi(sqlReservasiDate.toLocalDate());
                }

                reservasi.setTripId((Integer) rs.getObject("trip_id"));

                if ("paket_perjalanan".equals(tripType) && reservasi.getTripId() != null) {
                    PaketPerjalananModel paket = new PaketPerjalananModel();
                    paket.setId(rs.getInt("paket_id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));
                    paket.setNamaKota(rs.getString("paket_nama_kota")); // Mengambil dari alias k_paket
                    paket.setRating(rs.getDouble("paket_rating"));
                    paket.setGambar(rs.getString("paket_gambar"));
                    paket.setHarga(rs.getDouble("paket_harga"));

                    java.sql.Date mulai = rs.getDate("paket_mulai");
                    java.sql.Date akhir = rs.getDate("paket_akhir");
                    if (mulai != null) paket.setTanggalMulai(mulai.toLocalDate().format(DATE_FORMATTER));
                    if (akhir != null) paket.setTanggalAkhir(akhir.toLocalDate().format(DATE_FORMATTER));

                    paket.setJumlahHari((int) paket.getDurasi());
                    reservasi.setPaket(paket);

                } else if ("custom_trip".equals(tripType) && reservasi.getTripId() != null) {
                    CustomTripModel customTrip = new CustomTripModel();
                    customTrip.setId(rs.getInt("custom_id"));
                    customTrip.setNamaTrip(rs.getString("custom_nama_trip"));
                    
                    // --- FIXED: Mengambil nama kota dari alias k_custom ---
                    customTrip.setNamaKota(rs.getString("custom_nama_kota")); 
                    
                    customTrip.setJumlahPeserta(rs.getInt("custom_jumlah_peserta"));
                    customTrip.setTotalHarga(rs.getDouble("custom_total_harga"));
                    customTrip.setStatus(rs.getString("custom_status"));
                    
                    java.sql.Date customMulai = rs.getDate("custom_mulai");
                    java.sql.Date customAkhir = rs.getDate("custom_akhir");
                    if (customMulai != null) customTrip.setTanggalMulai(customMulai.toLocalDate());
                    if (customAkhir != null) customTrip.setTanggalAkhir(customAkhir.toLocalDate());

                    reservasi.setCustomTrip(customTrip);
                }
                list.add(reservasi);
            }
        }
    }
    return list;
}


    public List<ReservasiModel> getReservasiSelesaiDenganTrip(int userId) throws SQLException {
        List<ReservasiModel> list = new ArrayList<>();

        String sql = "SELECT r.id, r.user_id, r.trip_type, r.trip_id, r.kode_reservasi, r.tanggal_reservasi, r.status AS status_reservasi, " +
                    "p.id AS paket_id, p.nama_paket, p.rating AS paket_rating, k1.nama_kota AS paket_nama_kota, p.harga AS paket_harga, " +
                    "p.tanggal_mulai AS paket_mulai, p.tanggal_akhir AS paket_akhir, p.gambar AS paket_gambar, " +
                    "c.tanggal_mulai AS custom_mulai, c.tanggal_akhir AS custom_akhir, " +
                    "c.id AS custom_id, c.nama_trip AS custom_nama_trip, c.jumlah_peserta AS custom_jumlah_peserta, c.total_harga AS custom_total_harga, c.status AS custom_status " +
                    "FROM reservasi r " +
                    "LEFT JOIN paket_perjalanan p ON r.trip_type = 'paket_perjalanan' AND r.trip_id = p.id " +
                    "LEFT JOIN kota k1 ON p.kota_id = k1.id " +
                    "LEFT JOIN custom_trip c ON r.trip_type = 'custom_trip' AND r.trip_id = c.id " +
                    "WHERE r.user_id = ? AND r.status = 'selesai'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservasiModel reservasi = new ReservasiModel();
                    reservasi.setId(rs.getInt("id"));
                    reservasi.setUserId(rs.getInt("user_id"));
                    String tripType = rs.getString("trip_type");
                    reservasi.setTripType(tripType);
                    reservasi.setKodeReservasi(rs.getString("kode_reservasi"));
                    reservasi.setStatus(rs.getString("status_reservasi"));

                    java.sql.Date sqlReservasiDate = rs.getDate("tanggal_reservasi");
                    if (sqlReservasiDate != null) {
                        reservasi.setTanggalReservasi(sqlReservasiDate.toLocalDate());
                    }

                    reservasi.setTripId((Integer) rs.getObject("trip_id"));

                    if ("paket_perjalanan".equals(tripType) && reservasi.getTripId() != null) {
                        PaketPerjalananModel paket = new PaketPerjalananModel();
                        paket.setId(rs.getInt("paket_id"));
                        paket.setNamaPaket(rs.getString("nama_paket"));
                        paket.setNamaKota(rs.getString("paket_nama_kota"));
                        paket.setRating(rs.getDouble("paket_rating"));
                        paket.setGambar(rs.getString("paket_gambar"));
                        paket.setHarga(rs.getDouble("paket_harga"));

                        java.sql.Date mulai = rs.getDate("paket_mulai");
                        java.sql.Date akhir = rs.getDate("paket_akhir");
                        if (mulai != null) paket.setTanggalMulai(mulai.toLocalDate().format(DATE_FORMATTER));
                        if (akhir != null) paket.setTanggalAkhir(akhir.toLocalDate().format(DATE_FORMATTER));

                        paket.setJumlahHari((int) paket.getDurasi());
                        reservasi.setPaket(paket);

                    } else if ("custom_trip".equals(tripType) && reservasi.getTripId() != null) {
                        CustomTripModel customTrip = new CustomTripModel();
                        customTrip.setId(rs.getInt("custom_id"));
                        customTrip.setNamaTrip(rs.getString("custom_nama_trip"));
                        customTrip.setNamaKota(rs.getString("custom_nama_trip"));

                        customTrip.setJumlahPeserta(rs.getInt("custom_jumlah_peserta"));
                        customTrip.setTotalHarga(rs.getDouble("custom_total_harga"));
                        customTrip.setStatus(rs.getString("custom_status"));

                        java.sql.Date customMulai = rs.getDate("custom_mulai");
                        java.sql.Date customAkhir = rs.getDate("custom_akhir");
                        if (customMulai != null) customTrip.setTanggalMulai(customMulai.toLocalDate());
                        if (customAkhir != null) customTrip.setTanggalAkhir(customAkhir.toLocalDate());

                        if (customTrip.getTanggalMulai() != null && customTrip.getTanggalAkhir() != null) {
                            long jumlahHari = ChronoUnit.DAYS.between(
                                customTrip.getTanggalMulai(), customTrip.getTanggalAkhir()
                            ) + 1;
                            customTrip.setJumlahHari((int) jumlahHari);
                        }
                        reservasi.setCustomTrip(customTrip);
                    }
                    list.add(reservasi);
                }
            }
        }
        return list;
    }

    public ReservasiModel getReservasiByCustomTripId(int customTripId) {
        if (this.conn == null) {
            System.err.println("DAO Error: Koneksi database null.");
            return null;
        }

        String sql = "SELECT * FROM reservasi WHERE trip_id = ? AND trip_type = 'custom_trip' LIMIT 1";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customTripId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Gunakan metode getById yang sudah ada untuk memuat detail lengkap
                    return getReservasiById(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil reservasi by custom trip ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}