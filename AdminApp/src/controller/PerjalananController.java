/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import config.DatabaseConnection;
import model.Kota;
import model.PaketPerjalanan;
import model.RincianPaketPerjalanan;
import model.Destinasi;

import javax.swing.JOptionPane;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PerjalananController {
    
    // <<< METODE DIPERBARUI DENGAN filterStatus >>>
    public int getTotalPaketPerjalananCount(String filterNama, String filterStatus) {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM paket_perjalanan ");
        List<String> params = new ArrayList<>();
        boolean inWhere = false;

        if (filterNama != null && !filterNama.isEmpty()) {
            query.append("WHERE nama_paket LIKE ? ");
            params.add("%" + filterNama + "%");
            inWhere = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty()) {
            query.append(inWhere ? "AND " : "WHERE ");
            query.append("status = ?");
            params.add(filterStatus);
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            
            if (conn == null) return 0;
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghitung total paket perjalanan: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return 0;
    }

    // <<< METODE DIPERBARUI DENGAN filterStatus >>>
    public List<PaketPerjalanan> getPaketPerjalananWithPagination(int halaman, int dataPerHalaman, String filterNama, String filterStatus) {
        List<PaketPerjalanan> daftarPaket = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT pp.*, k.nama_kota FROM paket_perjalanan pp JOIN kota k ON pp.kota_id = k.id ");
        
        List<Object> params = new ArrayList<>();
        boolean inWhere = false;

        if (filterNama != null && !filterNama.isEmpty()) {
            query.append("WHERE pp.nama_paket LIKE ? ");
            params.add("%" + filterNama + "%");
            inWhere = true;
        }
        if (filterStatus != null && !filterStatus.isEmpty()) {
            query.append(inWhere ? "AND " : "WHERE ");
            query.append("pp.status = ? ");
            params.add(filterStatus);
        }
        
        query.append("ORDER BY pp.id DESC LIMIT ? OFFSET ?");
        params.add(dataPerHalaman);
        params.add((halaman - 1) * dataPerHalaman);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            if (conn == null) return daftarPaket;
            
            int paramIndex = 1;
            for (Object param : params) {
                stmt.setObject(paramIndex++, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PaketPerjalanan paket = new PaketPerjalanan();
                    paket.setId(rs.getInt("id"));
                    paket.setNamaPaket(rs.getString("nama_paket"));
                    paket.setNamaKota(rs.getString("nama_kota"));
                    paket.setKotaId(rs.getInt("kota_id"));
                    paket.setTanggalMulai(rs.getDate("tanggal_mulai"));
                    paket.setTanggalAkhir(rs.getDate("tanggal_akhir"));
                    paket.setHarga(rs.getBigDecimal("harga"));
                    paket.setKuota(rs.getInt("kuota"));
                    paket.setStatus(rs.getString("status"));
                    paket.setGambar(rs.getString("gambar"));
                    paket.setDeskripsi(rs.getString("deskripsi"));
                    daftarPaket.add(paket);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data perjalanan (paginasi): " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return daftarPaket;
    }
    
    // ... Sisa metode lain di bawah ini tidak perlu diubah ...
    public List<PaketPerjalanan> getAllPaketPerjalanan() {
        List<PaketPerjalanan> daftarPaket = new ArrayList<>();
        String query = "SELECT pp.*, k.nama_kota FROM paket_perjalanan pp JOIN kota k ON pp.kota_id = k.id ORDER BY pp.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (conn == null) return daftarPaket;
            while (rs.next()) {
                PaketPerjalanan paket = new PaketPerjalanan();
                paket.setId(rs.getInt("id"));
                paket.setNamaPaket(rs.getString("nama_paket"));
                paket.setNamaKota(rs.getString("nama_kota"));
                paket.setKotaId(rs.getInt("kota_id"));
                paket.setTanggalMulai(rs.getDate("tanggal_mulai"));
                paket.setTanggalAkhir(rs.getDate("tanggal_akhir"));
                paket.setHarga(rs.getBigDecimal("harga"));
                paket.setKuota(rs.getInt("kuota"));
                paket.setStatus(rs.getString("status"));
                paket.setGambar(rs.getString("gambar"));
                paket.setDeskripsi(rs.getString("deskripsi"));
                daftarPaket.add(paket);
            }
        } catch (SQLException e) {
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
                    if (fileGambar.exists()) fileGambar.delete();
                }
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menghapus paket perjalanan: " + e.getMessage() + "\nPastikan tidak ada reservasi atau rincian yang terkait.", "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }
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
                    rincian.setUrutanKunjungan(rs.getObject("urutan_kunjungan", Integer.class));
                    rincian.setDurasiJam(rs.getObject("durasi_jam", Integer.class));
                    daftarRincian.add(rincian);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarRincian;
    }

    public boolean addRincianPaket(RincianPaketPerjalanan rincian) {
        String query = "INSERT INTO rincian_paket_perjalanan (paket_perjalanan_id, destinasi_id, urutan_kunjungan, durasi_jam) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, rincian.getPaketPerjalananId());
            stmt.setInt(2, rincian.getDestinasiId());
            if (rincian.getUrutanKunjungan() != null) stmt.setInt(3, rincian.getUrutanKunjungan());
            else stmt.setNull(3, Types.INTEGER);
            if (rincian.getDurasiJam() != null) stmt.setInt(4, rincian.getDurasiJam());
            else stmt.setNull(4, Types.INTEGER);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRincianPaket(RincianPaketPerjalanan rincian) {
        String query = "UPDATE rincian_paket_perjalanan SET destinasi_id = ?, urutan_kunjungan = ?, durasi_jam = ? WHERE id = ? AND paket_perjalanan_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, rincian.getDestinasiId());
            if (rincian.getUrutanKunjungan() != null) stmt.setInt(2, rincian.getUrutanKunjungan());
            else stmt.setNull(2, Types.INTEGER);
            if (rincian.getDurasiJam() != null) stmt.setInt(3, rincian.getDurasiJam());
            else stmt.setNull(3, Types.INTEGER);
            stmt.setInt(4, rincian.getId());
            stmt.setInt(5, rincian.getPaketPerjalananId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRincianPaket(int idRincian) {
        String query = "DELETE FROM rincian_paket_perjalanan WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (conn == null) return false;
            stmt.setInt(1, idRincian);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
            e.printStackTrace();
        }
        return daftarDestinasi;
    }
    
    public List<Object[]> getLaporanDestinasiPopuler(Date tanggalMulai, Date tanggalAkhir) {
        List<Object[]> laporan = new ArrayList<>();
        String query = "SELECT d.nama_destinasi, COUNT(DISTINCT COALESCE(res_pp.id, res_ct.id)) AS jumlah_kunjungan_unik_reservasi " +
                       "FROM destinasi d " +
                       "LEFT JOIN rincian_paket_perjalanan rpp ON d.id = rpp.destinasi_id " +
                       "LEFT JOIN paket_perjalanan pp ON rpp.paket_perjalanan_id = pp.id " +
                       "LEFT JOIN reservasi res_pp ON pp.id = res_pp.trip_id AND res_pp.trip_type = 'paket_perjalanan' AND res_pp.status IN ('dibayar', 'selesai') " +
                       "LEFT JOIN rincian_custom_trip rct ON d.id = rct.destinasi_id " +
                       "LEFT JOIN custom_trip ct ON rct.custom_trip_id = ct.id " +
                       "LEFT JOIN reservasi res_ct ON ct.id = res_ct.trip_id AND res_ct.trip_type = 'custom_trip' AND res_ct.status IN ('dibayar', 'selesai') " +
                       "WHERE 1=1 ";

        List<Date> params = new ArrayList<>();
        if (tanggalMulai != null && tanggalAkhir != null) {
            query += "AND ( (res_pp.tanggal_reservasi BETWEEN ? AND ?) OR (res_ct.tanggal_reservasi BETWEEN ? AND ?) ) ";
            params.add(tanggalMulai);
            params.add(tanggalAkhir);
            params.add(tanggalMulai);
            params.add(tanggalAkhir);
        }
        
        query += "GROUP BY d.nama_destinasi HAVING COUNT(DISTINCT COALESCE(res_pp.id, res_ct.id)) > 0 ORDER BY jumlah_kunjungan_unik_reservasi DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (conn == null) return laporan;

            int paramIndex = 1;
            for (Date param : params) {
                stmt.setDate(paramIndex++, param);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    laporan.add(new Object[]{
                        rs.getString("nama_destinasi"),
                        rs.getInt("jumlah_kunjungan_unik_reservasi")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil laporan destinasi populer: " + e.getMessage(), "Kesalahan SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return laporan;
    }
}