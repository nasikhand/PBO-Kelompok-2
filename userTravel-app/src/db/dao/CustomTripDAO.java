package db.dao;

import db.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import model.CustomTripDetailModel; // Pastikan ini diimpor
import model.CustomTripModel;
import model.DestinasiModel; // Diperlukan untuk DestinasiModel.getNamaDestinasi()


public class CustomTripDAO {

    private Connection conn;

    public CustomTripDAO() {
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            System.err.println("Koneksi ke database gagal didapatkan oleh CustomTripDAO.");
        }
    }

    public CustomTripDAO(Connection conn) {
        this.conn = conn;
        if (this.conn == null) {
            System.err.println("Koneksi yang diberikan ke CustomTripDAO adalah NULL.");
        }
    }

    /**
     * Menyimpan objek CustomTripModel baru ke database, termasuk rincian detail trip.
     * @param customTrip Objek CustomTripModel yang akan disimpan.
     * @return ID dari custom trip yang baru dibuat, atau -1 jika gagal.
     */
    public int save(CustomTripModel customTrip) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi save CustomTrip.");
            return -1;
        }

        int customTripId = -1;
        // Query untuk menyimpan CustomTrip utama
        String sqlCustomTrip = "INSERT INTO custom_trip (user_id, nama_trip, tanggal_mulai, tanggal_akhir, jumlah_peserta, status, total_harga, catatan_user, transport_mode, transport_details, accommodation_name, room_type, accommodation_notes, activities_summary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Query untuk menyimpan rincian_custom_trip
        String sqlRincianCustomTrip = "INSERT INTO rincian_custom_trip (custom_trip_id, destinasi_id, tanggal_kunjungan, durasi_jam, urutan_kunjungan, harga_destinasi, biaya_transport) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. Simpan CustomTrip utama
            try (PreparedStatement ps = conn.prepareStatement(sqlCustomTrip, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customTrip.getUserId());
                ps.setString(2, customTrip.getNamaTrip());
                
                if (customTrip.getTanggalMulai() != null) {
                    ps.setDate(3, Date.valueOf(customTrip.getTanggalMulai()));
                } else {
                    ps.setNull(3, java.sql.Types.DATE);
                }
                if (customTrip.getTanggalAkhir() != null) {
                    ps.setDate(4, Date.valueOf(customTrip.getTanggalAkhir()));
                } else {
                    ps.setNull(4, java.sql.Types.DATE);
                }
                
                ps.setInt(5, customTrip.getJumlahPeserta());
                ps.setString(6, customTrip.getStatus());
                ps.setDouble(7, customTrip.getTotalHarga());
                ps.setString(8, customTrip.getCatatanUser());
                
                // --- Tambahan untuk kolom baru di custom_trip ---
                ps.setString(9, customTrip.getDetailedTransportMode());
                ps.setString(10, customTrip.getDetailedTransportDetails());
                ps.setString(11, customTrip.getDetailedAccommodationName());
                ps.setString(12, customTrip.getDetailedRoomType());
                ps.setString(13, customTrip.getDetailedAccommodationNotes());
                ps.setString(14, customTrip.getDetailedActivities() != null ? String.join(";", customTrip.getDetailedActivities()) : ""); // Join activities
                // --- End Tambahan ---


                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            customTripId = generatedKeys.getInt(1);
                            System.out.println("✅ Custom Trip berhasil disimpan dengan ID: " + customTripId);
                        }
                    }
                } else {
                    System.err.println("❌ Tidak ada baris yang disisipkan ke database untuk Custom Trip utama.");
                    conn.rollback();
                    return -1;
                }
            }

            // 2. Simpan rincian_custom_trip (detail destinasi)
            if (customTripId != -1 && customTrip.getDetailList() != null && !customTrip.getDetailList().isEmpty()) {
                try (PreparedStatement psRincian = conn.prepareStatement(sqlRincianCustomTrip)) {
                    for (CustomTripDetailModel detail : customTrip.getDetailList()) {
                        psRincian.setInt(1, customTripId); // Gunakan customTripId yang baru didapatkan
                        psRincian.setInt(2, detail.getDestinasiId());
                        if (detail.getTanggalKunjungan() != null) {
                            psRincian.setDate(3, Date.valueOf(detail.getTanggalKunjungan()));
                        } else {
                            psRincian.setNull(3, java.sql.Types.DATE);
                        }
                        psRincian.setInt(4, detail.getDurasiJam());
                        psRincian.setInt(5, detail.getUrutanKunjungan());
                        psRincian.setDouble(6, detail.getHargaDestinasi());
                        psRincian.setDouble(7, detail.getBiayaTransport());
                        psRincian.addBatch();
                    }
                    psRincian.executeBatch();
                    System.out.println("✅ Rincian Custom Trip berhasil disimpan untuk ID: " + customTripId);
                }
            } else {
                System.out.println("DEBUG CustomTripDAO - Tidak ada rincian detail trip untuk disimpan.");
            }

            conn.commit();
            return customTripId;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error saat rollback transaksi save CustomTrip: " + ex.getMessage());
            }
            System.err.println("❌ Error saat menyimpan Custom Trip atau rinciannya: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error saat mengembalikan auto-commit (save CustomTrip): " + ex.getMessage());
            }
        }
        return -1;
    }

    /**
     * Mengambil objek CustomTripModel berdasarkan ID custom trip,
     * termasuk detail destinasi dan rincian_custom_trip terkait.
     */
    public CustomTripModel getById(int id) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi getById CustomTrip.");
            return null;
        }

        CustomTripModel customTrip = null;
        // Query utama untuk CustomTrip, sekarang dengan kolom detail baru
        String sqlCustomTrip = "SELECT ct.* FROM custom_trip ct WHERE ct.id = ?";
        
        // Query untuk detail destinasi terkait (dari rincian_custom_trip)
        String sqlRincianCustomTrip = "SELECT rct.*, d.nama_destinasi FROM rincian_custom_trip rct " +
                                      "JOIN destinasi d ON rct.destinasi_id = d.id " +
                                      "WHERE rct.custom_trip_id = ? ORDER BY rct.urutan_kunjungan";

        try (PreparedStatement psCustomTrip = conn.prepareStatement(sqlCustomTrip)) {
            psCustomTrip.setInt(1, id);
            try (ResultSet rs = psCustomTrip.executeQuery()) {
                if (rs.next()) {
                    customTrip = new CustomTripModel();
                    customTrip.setId(rs.getInt("id"));
                    customTrip.setUserId(rs.getInt("user_id"));
                    customTrip.setNamaTrip(rs.getString("nama_trip"));

                    java.sql.Date sqlTanggalMulai = rs.getDate("tanggal_mulai");
                    if (sqlTanggalMulai != null) {
                        customTrip.setTanggalMulai(sqlTanggalMulai.toLocalDate());
                    } else {
                        customTrip.setTanggalMulai(null);
                    }

                    java.sql.Date sqlTanggalAkhir = rs.getDate("tanggal_akhir");
                    if (sqlTanggalAkhir != null) {
                        customTrip.setTanggalAkhir(sqlTanggalAkhir.toLocalDate());
                    } else {
                        customTrip.setTanggalAkhir(null);
                    }
                    
                    customTrip.setJumlahPeserta(rs.getInt("jumlah_peserta"));
                    customTrip.setStatus(rs.getString("status"));
                    customTrip.setTotalHarga(rs.getDouble("total_harga"));
                    customTrip.setCatatanUser(rs.getString("catatan_user"));

                    customTrip.setNamaKota(customTrip.getNamaTrip()); // Fallback for namaKota

                    // --- Load Detailed Trip Components (Transport, Accommodation, Activities) ---
                    customTrip.setDetailedTransportMode(rs.getString("transport_mode"));
                    customTrip.setDetailedTransportDetails(rs.getString("transport_details"));
                    customTrip.setDetailedAccommodationName(rs.getString("accommodation_name"));
                    customTrip.setDetailedRoomType(rs.getString("room_type"));
                    customTrip.setDetailedAccommodationNotes(rs.getString("accommodation_notes"));
                    
                    // Activities are stored as a single string, parse it back to List<String>
                    String activitiesSummary = rs.getString("activities_summary");
                    if (activitiesSummary != null && !activitiesSummary.isEmpty()) {
                        customTrip.setDetailedActivities(List.of(activitiesSummary.split(";"))); // Split by semicolon
                    } else {
                        customTrip.setDetailedActivities(new ArrayList<>());
                    }


                    // --- Load Rincian Custom Trip Details (Destinations) ---
                    List<CustomTripDetailModel> detailList = new ArrayList<>();
                    List<String> detailedDestinations = new ArrayList<>(); // Also for the list of destination names
                    try (PreparedStatement psRincian = conn.prepareStatement(sqlRincianCustomTrip)) {
                        psRincian.setInt(1, id);
                        try (ResultSet rsRincian = psRincian.executeQuery()) {
                            while (rsRincian.next()) {
                                CustomTripDetailModel detail = new CustomTripDetailModel();
                                detail.setId(rsRincian.getInt("id"));
                                detail.setCustomTripId(rsRincian.getInt("custom_trip_id")); // Set custom_trip_id
                                detail.setDestinasiId(rsRincian.getInt("destinasi_id"));
                                
                                java.sql.Date sqlTglKunjungan = rsRincian.getDate("tanggal_kunjungan");
                                if (sqlTglKunjungan != null) detail.setTanggalKunjungan(sqlTglKunjungan.toLocalDate());
                                
                                detail.setDurasiJam(rsRincian.getInt("durasi_jam"));
                                detail.setUrutanKunjungan(rsRincian.getInt("urutan_kunjungan"));
                                detail.setHargaDestinasi(rsRincian.getDouble("harga_destinasi"));
                                detail.setBiayaTransport(rsRincian.getDouble("biaya_transport"));

                                detailList.add(detail);
                                detailedDestinations.add(rsRincian.getString("nama_destinasi")); // Get destination name
                            }
                        }
                    }
                    customTrip.setDetailList(detailList);
                    customTrip.setDetailedDestinations(detailedDestinations); // Set list of destination names

                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mengambil custom trip dengan ID " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return customTrip;
    }

    /**
     * Menghapus custom trip dari database.
     * Termasuk menghapus rincian_custom_trip terkait.
     */
    public boolean deleteCustomTrip(int id) {
        if (this.conn == null) {
            System.err.println("Tidak ada koneksi database untuk operasi delete CustomTrip.");
            return false;
        }
        try {
            conn.setAutoCommit(false); // Mulai transaksi

            // 1. Hapus rincian_custom_trip terkait
            String sqlDeleteRincian = "DELETE FROM rincian_custom_trip WHERE custom_trip_id = ?";
            try(PreparedStatement psRincian = conn.prepareStatement(sqlDeleteRincian)) {
                psRincian.setInt(1, id);
                psRincian.executeUpdate();
                System.out.println("DEBUG CustomTripDAO - Dihapus rincian_custom_trip untuk custom trip ID: " + id);
            }

            // 2. Hapus custom_trip itu sendiri
            String sqlDeleteCustomTrip = "DELETE FROM custom_trip WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDeleteCustomTrip)) {
                ps.setInt(1, id);
                int affectedRows = ps.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    System.out.println("✅ Custom Trip ID: " + id + " berhasil dihapus.");
                    return true;
                } else {
                    conn.rollback();
                    System.err.println("❌ Custom Trip ID: " + id + " tidak ditemukan atau tidak dapat dihapus.");
                    return false;
                }
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error saat rollback delete CustomTrip: " + ex.getMessage());
            }
            System.err.println("❌ Error saat menghapus Custom Trip: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.err.println("Error saat mengembalikan auto-commit (delete CustomTrip): " + ex.getMessage());
            }
        }
    }
}
