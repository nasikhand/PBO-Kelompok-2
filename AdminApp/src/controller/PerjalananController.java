/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import model.RincianPaketPerjalanan; 
import model.Destinasi; 

import config.DatabaseConnection;
import model.Kota;
import model.PaketPerjalanan;
import javax.swing.JOptionPane;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerjalananController {

    public List<PaketPerjalanan> getAllPaketPerjalanan() {
        List<PaketPerjalanan> daftarPaket = new ArrayList<>();
        String query = "SELECT pp.*, k.nama_kota FROM paket_perjalanan pp JOIN kota k ON pp.kota_id = k.id ORDER BY pp.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return daftarPaket; // Koneksi gagal

            while (rs.next()) {
                PaketPerjalanan paket = new PaketPerjalanan();
                paket.setId(rs.getInt("id"));
                paket.setNamaPaket(rs.getString("nama_paket"));
                paket.setNamaKota(rs.getString("nama_kota"));
                paket.setTanggalMulai(rs.getDate("tanggal_mulai"));
                paket.setTanggalAkhir(rs.getDate("tanggal_akhir"));
                paket.setHarga(rs.getBigDecimal("harga"));
                paket.setKuota(rs.getInt("kuota"));
                paket.setStatus(rs.getString("status"));
                paket.setGambar(rs.getString("gambar")); // Tambahkan pengambilan gambar
                daftarPaket.add(paket);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data perjalanan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarPaket;
    }

    public List<Kota> getAllKota() {
        List<Kota> daftarKota = new ArrayList<>();
        String query = "SELECT * FROM kota ORDER BY nama_kota";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (conn == null) return daftarKota;

            while (rs.next()) {
                Kota kota = new Kota();
                kota.setId(rs.getInt("id"));
                kota.setNamaKota(rs.getString("nama_kota"));
                kota.setProvinsi(rs.getString("provinsi"));
                daftarKota.add(kota);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data kota: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarKota;
    }

    public boolean addPaketPerjalanan(PaketPerjalanan paket) {
        String query = "INSERT INTO paket_perjalanan (kota_id, nama_paket, tanggal_mulai, tanggal_akhir, kuota, harga, deskripsi, status, gambar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return false;
            
            stmt.setInt(1, paket.getKotaId());
            stmt.setString(2, paket.getNamaPaket());
            stmt.setDate(3, paket.getTanggalMulai());
            stmt.setDate(4, paket.getTanggalAkhir());
            stmt.setInt(5, paket.getKuota());
            stmt.setBigDecimal(6, paket.getHarga());
            stmt.setString(7, paket.getDeskripsi());
            stmt.setString(8, paket.getStatus());
            stmt.setString(9, paket.getGambar());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah paket perjalanan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    public PaketPerjalanan getPaketById(int id) {
        PaketPerjalanan paket = null;
        String query = "SELECT * FROM paket_perjalanan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return null;

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    paket = new PaketPerjalanan();
                    paket.setId(rs.getInt("id"));
                    paket.setKotaId(rs.getInt("kota_id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));
                    paket.setTanggalMulai(rs.getDate("tanggal_mulai"));
                    paket.setTanggalAkhir(rs.getDate("tanggal_akhir"));
                    paket.setKuota(rs.getInt("kuota"));
                    paket.setHarga(rs.getBigDecimal("harga"));
                    paket.setDeskripsi(rs.getString("deskripsi"));
                    paket.setStatus(rs.getString("status"));
                    paket.setGambar(rs.getString("gambar"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil detail paket: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return paket;
    }
    
    public boolean updatePaketPerjalanan(PaketPerjalanan paket) {
        String query = "UPDATE paket_perjalanan SET kota_id=?, nama_paket=?, tanggal_mulai=?, tanggal_akhir=?, kuota=?, harga=?, deskripsi=?, status=?, gambar=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return false;

            stmt.setInt(1, paket.getKotaId());
            stmt.setString(2, paket.getNamaPaket());
            stmt.setDate(3, paket.getTanggalMulai());
            stmt.setDate(4, paket.getTanggalAkhir());
            stmt.setInt(5, paket.getKuota());
            stmt.setBigDecimal(6, paket.getHarga());
            stmt.setString(7, paket.getDeskripsi());
            stmt.setString(8, paket.getStatus());
            stmt.setString(9, paket.getGambar());
            stmt.setInt(10, paket.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui paket perjalanan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletePaketPerjalanan(int id) {
        PaketPerjalanan paket = getPaketById(id);
        if (paket == null) return false;

        String query = "DELETE FROM paket_perjalanan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (conn == null) return false;

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                if (paket.getGambar() != null && !paket.getGambar().isEmpty()) {
                    File fileGambar = new File("images/paket_perjalanan/" + paket.getGambar());
                    if (fileGambar.exists()) {
                        if(!fileGambar.delete()){
                             System.err.println("Gagal menghapus file gambar: " + fileGambar.getAbsolutePath());
                        }
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus paket perjalanan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    public int getTotalPaketPerjalananAktif() {
        String query = "SELECT COUNT(*) AS jumlah_paket FROM paket_perjalanan WHERE status = 'tersedia'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            if (conn == null) return 0;

            if (rs.next()) {
                return rs.getInt("jumlah_paket");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil total paket aktif: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getLaporanDestinasiPopuler() {
        List<Object[]> laporan = new ArrayList<>();
        String query = "SELECT d.nama_destinasi, COUNT(d.id) AS jumlah_kunjungan " +
                       "FROM destinasi d " +
                       "LEFT JOIN rincian_paket_perjalanan rpp ON d.id = rpp.destinasi_id " +
                       "LEFT JOIN rincian_custom_trip rct ON d.id = rct.destinasi_id " +
                       "GROUP BY d.nama_destinasi " +
                       "ORDER BY jumlah_kunjungan DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (conn == null) return laporan;

            while (rs.next()) {
                laporan.add(new Object[]{
                    rs.getString("nama_destinasi"),
                    rs.getInt("jumlah_kunjungan")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil laporan destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return laporan;
    }
    
    /**
     * Mengambil semua rincian destinasi untuk satu paket perjalanan.
     */
    public List<RincianPaketPerjalanan> getRincianByPaketId(int paketId) {
        List<RincianPaketPerjalanan> daftarRincian = new ArrayList<>();
        String query = "SELECT rpp.id, rpp.paket_perjalanan_id, rpp.destinasi_id, " +
                       "rpp.urutan_kunjungan, rpp.durasi_jam, d.nama_destinasi " +
                       "FROM rincian_paket_perjalanan rpp " +
                       "JOIN destinasi d ON rpp.destinasi_id = d.id " +
                       "WHERE rpp.paket_perjalanan_id = ? " +
                       "ORDER BY rpp.urutan_kunjungan ASC, rpp.id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return daftarRincian;
            stmt.setInt(1, paketId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RincianPaketPerjalanan rincian = new RincianPaketPerjalanan();
                    rincian.setId(rs.getInt("id"));
                    rincian.setPaketPerjalananId(rs.getInt("paket_perjalanan_id"));
                    rincian.setDestinasiId(rs.getInt("destinasi_id"));
                    rincian.setNamaDestinasi(rs.getString("nama_destinasi"));
                    // Cek null untuk Integer
                    int urutan = rs.getInt("urutan_kunjungan");
                    rincian.setUrutanKunjungan(rs.wasNull() ? null : urutan);
                    int durasi = rs.getInt("durasi_jam");
                    rincian.setDurasiJam(rs.wasNull() ? null : durasi);
                    daftarRincian.add(rincian);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil rincian paket: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarRincian;
    }

    /**
     * Menambah destinasi baru ke dalam rincian paket perjalanan.
     */
    public boolean addRincianPaket(RincianPaketPerjalanan rincian) {
        String query = "INSERT INTO rincian_paket_perjalanan (paket_perjalanan_id, destinasi_id, urutan_kunjungan, durasi_jam) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, rincian.getPaketPerjalananId());
            stmt.setInt(2, rincian.getDestinasiId());
            // Handle Integer yang mungkin null
            if (rincian.getUrutanKunjungan() != null) {
                stmt.setInt(3, rincian.getUrutanKunjungan());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            if (rincian.getDurasiJam() != null) {
                stmt.setInt(4, rincian.getDurasiJam());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menambah rincian destinasi ke paket: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui data rincian destinasi dalam paket perjalanan.
     */
    public boolean updateRincianPaket(RincianPaketPerjalanan rincian) {
        String query = "UPDATE rincian_paket_perjalanan SET destinasi_id = ?, urutan_kunjungan = ?, durasi_jam = ? WHERE id = ? AND paket_perjalanan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, rincian.getDestinasiId());
            if (rincian.getUrutanKunjungan() != null) {
                stmt.setInt(2, rincian.getUrutanKunjungan());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (rincian.getDurasiJam() != null) {
                stmt.setInt(3, rincian.getDurasiJam());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, rincian.getId());
            stmt.setInt(5, rincian.getPaketPerjalananId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui rincian destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus destinasi dari rincian paket perjalanan.
     */
    public boolean deleteRincianPaket(int idRincian) {
        String query = "DELETE FROM rincian_paket_perjalanan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, idRincian);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus rincian destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mengambil semua data destinasi untuk JComboBox.
     * (Jika DestinasiController sudah ada, lebih baik panggil dari sana)
     */
    public List<Destinasi> getAllDestinasiForComboBox() {
        List<Destinasi> daftarDestinasi = new ArrayList<>();
        String query = "SELECT id, nama_destinasi FROM destinasi ORDER BY nama_destinasi";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (conn == null) return daftarDestinasi;
            while (rs.next()) {
                Destinasi dest = new Destinasi();
                dest.setId(rs.getInt("id"));
                dest.setNamaDestinasi(rs.getString("nama_destinasi"));
                daftarDestinasi.add(dest);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil daftar destinasi: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarDestinasi;
    }
}