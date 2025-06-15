package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import model.CustomTripDetailModel;
import model.CustomTripModel;
import java.util.Arrays;


/**
 * DAO (Data Access Object) for handling all database operations
 * related to the 'custom_trip' and 'rincian_custom_trip' tables.
 */
public class CustomTripDAO {

    private final Connection conn;

    /**
     * Constructor that gets a connection from the connection pool.
     */
    public CustomTripDAO(Connection conn) {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("FATAL: CustomTripDAO failed to get a database connection.");
        }
    }

    /**
     * Saves a new CustomTripModel to the database, including its detail items,
     * within a single transaction.
     * @param customTrip The CustomTripModel object to be saved.
     * @return The auto-generated ID of the newly created custom trip, or -1 if failed.
     */
    public int save(CustomTripModel customTrip) {
        if (this.conn == null) {
            System.err.println("DAO Error: Database connection is null. Cannot save CustomTrip.");
            return -1;
        }

        int customTripId = -1;
        // --- FIXED: Query SQL sekarang lebih sederhana ---
        String sqlCustomTrip = "INSERT INTO custom_trip (user_id, nama_trip, tanggal_mulai, tanggal_akhir, jumlah_peserta, status, total_harga, catatan_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        String sqlRincianCustomTrip = "INSERT INTO rincian_custom_trip (custom_trip_id, destinasi_id, tanggal_kunjungan, durasi_jam, urutan_kunjungan, harga_destinasi, biaya_transport) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            // 1. Simpan CustomTrip utama
            try (PreparedStatement ps = conn.prepareStatement(sqlCustomTrip, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customTrip.getUserId());
                ps.setString(2, customTrip.getNamaTrip());
                
                if (customTrip.getTanggalMulai() != null) ps.setDate(3, Date.valueOf(customTrip.getTanggalMulai())); else ps.setNull(3, java.sql.Types.DATE);
                if (customTrip.getTanggalAkhir() != null) ps.setDate(4, Date.valueOf(customTrip.getTanggalAkhir())); else ps.setNull(4, java.sql.Types.DATE);
                
                ps.setInt(5, customTrip.getJumlahPeserta());
                ps.setString(6, customTrip.getStatus());
                ps.setDouble(7, customTrip.getTotalHarga());
                ps.setString(8, customTrip.getCatatanUser());
                
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            customTripId = generatedKeys.getInt(1);
                        }
                    }
                } else {
                    conn.rollback();
                    return -1;
                }
            }

            // 2. Simpan rincian itinerary
            if (customTripId != -1 && customTrip.getDetailList() != null && !customTrip.getDetailList().isEmpty()) {
                try (PreparedStatement psRincian = conn.prepareStatement(sqlRincianCustomTrip)) {
                    for (CustomTripDetailModel detail : customTrip.getDetailList()) {
                        psRincian.setInt(1, customTripId);
                        psRincian.setInt(2, detail.getDestinasiId());
                        if (detail.getTanggalKunjungan() != null) psRincian.setDate(3, Date.valueOf(detail.getTanggalKunjungan())); else psRincian.setNull(3, java.sql.Types.DATE);
                        psRincian.setInt(4, detail.getDurasiJam());
                        psRincian.setInt(5, detail.getUrutanKunjungan());
                        psRincian.setDouble(6, detail.getHargaDestinasi());
                        psRincian.setDouble(7, detail.getBiayaTransport());
                        psRincian.addBatch();
                    }
                    psRincian.executeBatch();
                }
            }

            conn.commit();
            return customTripId;

        } catch (SQLException e) {
            System.err.println("❌ DAO Error: SQLException during save operation. Rolling back transaction.");
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignored */ }
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { /* ignored */ }
        }
        return -1;
    }

    /**
     * Retrieves a CustomTripModel object by its ID, complete with all its details.
     * @param id The ID of the custom trip to retrieve.
     * @return A complete CustomTripModel object, or null if not found or an error occurs.
     */
    public CustomTripModel getById(int id) {
        if (this.conn == null) {
            System.err.println("DAO Error: Database connection is null. Cannot get CustomTrip by ID.");
            return null;
        }

        CustomTripModel customTrip = null;
        String sqlCustomTrip = "SELECT * FROM custom_trip WHERE id = ?";
        String sqlRincian = "SELECT rct.*, d.nama_destinasi FROM rincian_custom_trip rct JOIN destinasi d ON rct.destinasi_id = d.id WHERE rct.custom_trip_id = ? ORDER BY rct.urutan_kunjungan";

        try (PreparedStatement psCustomTrip = conn.prepareStatement(sqlCustomTrip)) {
            psCustomTrip.setInt(1, id);
            try (ResultSet rs = psCustomTrip.executeQuery()) {
                if (rs.next()) {
                    customTrip = new CustomTripModel();
                    customTrip.setId(rs.getInt("id"));
                    customTrip.setUserId(rs.getInt("user_id"));
                    customTrip.setNamaTrip(rs.getString("nama_trip"));
                    
                    Date tglMulai = rs.getDate("tanggal_mulai");
                    if(tglMulai != null) customTrip.setTanggalMulai(tglMulai.toLocalDate());
                    
                    Date tglAkhir = rs.getDate("tanggal_akhir");
                    if(tglAkhir != null) customTrip.setTanggalAkhir(tglAkhir.toLocalDate());
                    
                    customTrip.setJumlahPeserta(rs.getInt("jumlah_peserta"));
                    customTrip.setStatus(rs.getString("status"));
                    customTrip.setTotalHarga(rs.getDouble("total_harga"));
                    customTrip.setCatatanUser(rs.getString("catatan_user"));
                    
                    // --- FIXED: Load all detailed fields from custom_trip table ---
                    // customTrip.setDetailedTransportMode(rs.getString("transport_mode"));
                    // customTrip.setDetailedTransportDetails(rs.getString("transport_details"));
                    // customTrip.setDetailedAccommodationName(rs.getString("accommodation_name"));
                    // customTrip.setDetailedRoomType(rs.getString("room_type"));
                    // customTrip.setDetailedAccommodationNotes(rs.getString("accommodation_notes"));
                    
                    // String activitiesSummary = rs.getString("activities_summary");
                    // if (activitiesSummary != null && !activitiesSummary.isEmpty()) {
                    //     customTrip.setDetailedActivities(new ArrayList<>(Arrays.asList(activitiesSummary.split(";"))));
                    // } else {
                    //     customTrip.setDetailedActivities(new ArrayList<>());
                    // }

                    // --- Load associated itinerary details ---
                    List<CustomTripDetailModel> detailList = new ArrayList<>();
                    try (PreparedStatement psRincian = conn.prepareStatement(sqlRincian)) {
                        psRincian.setInt(1, id);
                        try (ResultSet rsRincian = psRincian.executeQuery()) {
                            while (rsRincian.next()) {
                                CustomTripDetailModel detail = new CustomTripDetailModel();
                                detail.setId(rsRincian.getInt("id"));
                                detail.setCustomTripId(rsRincian.getInt("custom_trip_id"));
                                detail.setDestinasiId(rsRincian.getInt("destinasi_id"));
                                Date tglKunjungan = rsRincian.getDate("tanggal_kunjungan");
                                if(tglKunjungan != null) detail.setTanggalKunjungan(tglKunjungan.toLocalDate());
                                detail.setDurasiJam(rsRincian.getInt("durasi_jam"));
                                detail.setUrutanKunjungan(rsRincian.getInt("urutan_kunjungan"));
                                detail.setHargaDestinasi(rsRincian.getDouble("harga_destinasi"));
                                detail.setBiayaTransport(rsRincian.getDouble("biaya_transport"));
                                detailList.add(detail);
                            }
                        }
                    }
                    customTrip.setDetailList(detailList);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ DAO Error: Failed to retrieve Custom Trip with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return customTrip;
    }

    public boolean deleteCustomTripAndReservation(int customTripId, int reservasiId) {
        if (this.conn == null) {
            System.err.println("DAO Error: Database connection is null. Cannot delete CustomTrip.");
            return false;
        }

        // Siapkan semua perintah SQL yang diperlukan
        String sqlDeleteRincian = "DELETE FROM rincian_custom_trip WHERE custom_trip_id = ?";
        String sqlDeletePenumpang = "DELETE FROM penumpang WHERE reservasi_id = ?";
        String sqlDeleteCustomTrip = "DELETE FROM custom_trip WHERE id = ?";
        String sqlDeleteReservasi = "DELETE FROM reservasi WHERE id = ?";
        
        try {
            // Mulai transaksi untuk memastikan semua perintah berhasil atau tidak sama sekali
            conn.setAutoCommit(false);

            // 1. Hapus Penumpang terlebih dahulu
            try(PreparedStatement ps = conn.prepareStatement(sqlDeletePenumpang)) {
                ps.setInt(1, reservasiId);
                ps.executeUpdate();
            }
            
            // 2. Hapus Rincian Itinerary
            try(PreparedStatement ps = conn.prepareStatement(sqlDeleteRincian)) {
                ps.setInt(1, customTripId);
                ps.executeUpdate();
            }

            // 3. Hapus Trip Utama
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteCustomTrip)) {
                ps.setInt(1, customTripId);
                ps.executeUpdate();
            }
            
            // 4. Terakhir, hapus entri Reservasi
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteReservasi)) {
                ps.setInt(1, reservasiId);
                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    // Jika reservasi tidak ditemukan, batalkan semuanya
                    throw new SQLException("Gagal menghapus reservasi, ID tidak ditemukan: " + reservasiId);
                }
            }
            
            // Jika semua berhasil, commit perubahan ke database
            conn.commit();
            System.out.println("✅ DEBUG: Custom Trip (ID: " + customTripId + ") dan semua data terkait berhasil dihapus.");
            return true;

        } catch (SQLException e) {
            System.err.println("❌ DAO Error: Gagal membatalkan Custom Trip. Melakukan rollback. " + e.getMessage());
            try { 
                if (conn != null) conn.rollback(); // Batalkan semua perubahan jika terjadi error
            } catch (SQLException ex) { 
                System.err.println("DAO Error: Gagal melakukan rollback: " + ex.getMessage());
            }
            e.printStackTrace();
            return false;
        } finally {
            try { 
                if (conn != null) conn.setAutoCommit(true); // Kembalikan ke mode auto-commit
            } catch (SQLException ex) { 
                ex.printStackTrace();
            }
        }
    }
}
