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
        String sqlCustomTrip = "INSERT INTO custom_trip (user_id, nama_trip, tanggal_mulai, tanggal_akhir, jumlah_peserta, status, total_harga, catatan_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlRincianCustomTrip = "INSERT INTO rincian_custom_trip (custom_trip_id, destinasi_id, tanggal_kunjungan, durasi_jam, urutan_kunjungan, harga_destinasi, biaya_transport) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            // Start transaction
            conn.setAutoCommit(false);

            // 1. Save the main CustomTrip record
            try (PreparedStatement ps = conn.prepareStatement(sqlCustomTrip, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customTrip.getUserId());
                ps.setString(2, customTrip.getNamaTrip());
                
                if (customTrip.getTanggalMulai() != null) ps.setDate(3, Date.valueOf(customTrip.getTanggalMulai())); else ps.setNull(3, java.sql.Types.DATE);
                if (customTrip.getTanggalAkhir() != null) ps.setDate(4, Date.valueOf(customTrip.getTanggalAkhir())); else ps.setNull(4, java.sql.Types.DATE);
                
                ps.setInt(5, customTrip.getJumlahPeserta());
                ps.setString(6, customTrip.getStatus());
                ps.setDouble(7, customTrip.getTotalHarga());
                ps.setString(8, customTrip.getCatatanUser());
                
                // Save new detailed columns
//                ps.setString(9, customTrip.getDetailedTransportMode());
//                ps.setString(10, customTrip.getDetailedTransportDetails());
//                ps.setString(11, customTrip.getDetailedAccommodationName());
//                ps.setString(12, customTrip.getDetailedRoomType());
//                ps.setString(13, customTrip.getDetailedAccommodationNotes());
//                ps.setString(14, customTrip.getDetailedActivities() != null ? String.join(";", customTrip.getDetailedActivities()) : "");

                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            customTripId = generatedKeys.getInt(1);
                            System.out.println("✅ DEBUG: Custom Trip saved successfully to 'custom_trip' table with ID: " + customTripId);
                        }
                    }
                } else {
                    System.err.println("❌ DAO Error: Failed to insert main Custom Trip record, no rows affected.");
                    conn.rollback();
                    return -1;
                }
            }

            // 2. Save the itinerary details (rincian_custom_trip)
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
                    System.out.println("✅ DEBUG: Itinerary details saved successfully for Custom Trip ID: " + customTripId);
                }
            } else {
                System.out.println("INFO: No itinerary details to save for Custom Trip ID: " + customTripId);
            }

            // If everything is successful, commit the transaction
            conn.commit();
            return customTripId;

        } catch (SQLException e) {
            System.err.println("❌ DAO Error: SQLException occurred during save operation. Rolling back transaction.");
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("DAO Error: Failed to rollback transaction: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("DAO Error: Failed to restore auto-commit mode: " + ex.getMessage());
            }
        }
        return -1; // Return -1 if any error occurred
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

    /**
     * Deletes a custom trip and its associated details from the database within a transaction.
     * @param id The ID of the custom trip to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteCustomTrip(int id) {
        if (this.conn == null) {
            System.err.println("DAO Error: Database connection is null. Cannot delete CustomTrip.");
            return false;
        }

        String sqlDeleteRincian = "DELETE FROM rincian_custom_trip WHERE custom_trip_id = ?";
        String sqlDeleteCustomTrip = "DELETE FROM custom_trip WHERE id = ?";
        
        try {
            conn.setAutoCommit(false);

            // 1. Delete details first to avoid foreign key constraint violations
            try(PreparedStatement psRincian = conn.prepareStatement(sqlDeleteRincian)) {
                psRincian.setInt(1, id);
                psRincian.executeUpdate();
            }

            // 2. Delete the main custom trip record
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteCustomTrip)) {
                ps.setInt(1, id);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    System.out.println("✅ DEBUG: Custom Trip ID: " + id + " deleted successfully.");
                    return true;
                } else {
                    System.err.println("❌ DAO Warning: Custom Trip ID: " + id + " not found or could not be deleted. Rolling back.");
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ DAO Error: Failed to delete Custom Trip. Rolling back. " + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { /* ignored */ }
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException ex) { /* ignored */ }
        }
    }
}
