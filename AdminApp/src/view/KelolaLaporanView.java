/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PerjalananController;
import controller.ReservasiController;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter; // <-- Import baru
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedWriter; // <-- Import baru
import java.io.File; // <-- Import baru
import java.io.FileWriter; // <-- Import baru
import java.io.IOException; // <-- Import baru
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat; // Untuk nama file default
import java.util.List;
import java.util.Locale;
// import java.util.Calendar; // Dihapus jika tidak ada set tanggal default


public class KelolaLaporanView extends JPanel {
    private ReservasiController reservasiController;
    private PerjalananController perjalananController;
    private JTabbedPane tabbedPane;

    private JDateChooser dateChooserMulai;
    private JDateChooser dateChooserAkhir;
    private JButton btnTerapkanFilter;
    private JButton btnHapusFilter;

    public KelolaLaporanView() {
        this.reservasiController = new ReservasiController();
        this.perjalananController = new PerjalananController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JPanel panelHeader = new JPanel(new BorderLayout(10,10));
        panelHeader.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Laporan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        panelHeader.add(judul, BorderLayout.NORTH);

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFilter.setOpaque(false);
        panelFilter.add(new JLabel("Dari Tanggal:"));
        dateChooserMulai = new JDateChooser();
        dateChooserMulai.setDateFormatString("yyyy-MM-dd");
        dateChooserMulai.setPreferredSize(new Dimension(150, 25));
        panelFilter.add(dateChooserMulai);

        panelFilter.add(new JLabel("Sampai Tanggal:"));
        dateChooserAkhir = new JDateChooser();
        dateChooserAkhir.setDateFormatString("yyyy-MM-dd");
        dateChooserAkhir.setPreferredSize(new Dimension(150, 25));
        panelFilter.add(dateChooserAkhir);
        
        btnTerapkanFilter = new JButton("Terapkan Filter");
        panelFilter.add(btnTerapkanFilter);
        btnHapusFilter = new JButton("Hapus Filter");
        panelFilter.add(btnHapusFilter);
        panelHeader.add(panelFilter, BorderLayout.CENTER);
        add(panelHeader, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(false);
        
        muatSemuaTabLaporan(null, null); 
        add(tabbedPane, BorderLayout.CENTER);

        JPanel panelTombolBawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolBawah.setOpaque(false);
        JButton btnEksporCsv = new JButton("Ekspor Laporan ke CSV"); // <-- Ubah teks tombol
        panelTombolBawah.add(btnEksporCsv);
        add(panelTombolBawah, BorderLayout.SOUTH);

        btnTerapkanFilter.addActionListener(e -> terapkanFilterTanggal());
        btnHapusFilter.addActionListener(e -> {
            dateChooserMulai.setDate(null);
            dateChooserAkhir.setDate(null);
            muatSemuaTabLaporan(null, null);
        });

        // vvv ACTION LISTENER BARU UNTUK EKSPOR CSV vvv
        btnEksporCsv.addActionListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                Component selectedComponent = tabbedPane.getComponentAt(selectedIndex);
                if (selectedComponent instanceof JPanel) {
                    JPanel selectedPanel = (JPanel) selectedComponent;
                    for (Component comp : selectedPanel.getComponents()) {
                        if (comp instanceof JScrollPane) {
                            JScrollPane scrollPane = (JScrollPane) comp;
                            JTable tabelLaporan = (JTable) scrollPane.getViewport().getView();
                            pilihLokasiDanEksporKeCSV(tabelLaporan, tabbedPane.getTitleAt(selectedIndex));
                            return;
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(this, "Tidak ada tabel laporan aktif untuk diekspor.", "Info Ekspor", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void terapkanFilterTanggal() {
        java.util.Date utilTglMulai = dateChooserMulai.getDate();
        java.util.Date utilTglAkhir = dateChooserAkhir.getDate();

        Date sqlTglMulai = null;
        Date sqlTglAkhir = null;

        if (utilTglMulai != null && utilTglAkhir != null) {
            if (utilTglMulai.after(utilTglAkhir)) {
                JOptionPane.showMessageDialog(this, "Tanggal mulai tidak boleh setelah tanggal akhir.", "Kesalahan Rentang Tanggal", JOptionPane.WARNING_MESSAGE);
                return;
            }
            sqlTglMulai = new Date(utilTglMulai.getTime());
            sqlTglAkhir = new Date(utilTglAkhir.getTime());
            muatSemuaTabLaporan(sqlTglMulai, sqlTglAkhir);
        } else if (utilTglMulai == null && utilTglAkhir == null) {
            // Jika keduanya kosong, berarti admin ingin melihat semua data (atau baru menghapus filter)
            muatSemuaTabLaporan(null, null);
        } else {
            // Jika hanya salah satu yang diisi
            JOptionPane.showMessageDialog(this, "Harap isi kedua field tanggal untuk filter, atau kosongkan keduanya untuk menampilkan semua data.", "Input Filter Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void muatSemuaTabLaporan(Date tanggalMulai, Date tanggalAkhir) {
        int selectedIndex = tabbedPane.getSelectedIndex();
        tabbedPane.removeAll();

        // String di judul tab bisa disesuaikan jika ada filter aktif
        String suffixJudul = "";
        if (tanggalMulai != null && tanggalAkhir != null) {
            suffixJudul = " (" + tanggalMulai.toString() + " s/d " + tanggalAkhir.toString() + ")";
        }

        tabbedPane.addTab("Penjualan per Paket" + suffixJudul, buatPanelLaporanPenjualanPaket(tanggalMulai, tanggalAkhir));
        tabbedPane.addTab("Penjualan per Jenis Trip" + suffixJudul, buatPanelLaporanPenjualanJenis(tanggalMulai, tanggalAkhir));
        tabbedPane.addTab("Destinasi Populer" + suffixJudul, buatPanelLaporanDestinasi(tanggalMulai, tanggalAkhir));

        if (selectedIndex != -1 && selectedIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(selectedIndex);
        } else if (tabbedPane.getTabCount() > 0) {
            tabbedPane.setSelectedIndex(0);
        }
    }

    // Metode buatPanelLaporanPenjualanPaket, buatPanelLaporanPenjualanJenis, 
    // dan buatPanelLaporanDestinasi TIDAK BERUBAH dari versi sebelumnya
    // karena mereka sudah menerima java.sql.Date dan bisa menangani null.
    // Pastikan Anda menyalinnya kembali secara lengkap jika Anda mengganti seluruh file.

    private JPanel buatPanelLaporanPenjualanPaket(Date tanggalMulai, Date tanggalAkhir) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Nama Paket", "Jumlah Reservasi", "Total Pendapatan (Lunas)"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabel = new JTable(model);
        
        List<Object[]> dataLaporan = reservasiController.getLaporanPenjualanPerPaket(tanggalMulai, tanggalAkhir);
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);
        if (dataLaporan != null) { // Tambahkan null check
            for (Object[] baris : dataLaporan) {
                model.addRow(new Object[]{ baris[0], baris[1], formatMataUang.format(baris[2]) });
            }
        }
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatPanelLaporanPenjualanJenis(Date tanggalMulai, Date tanggalAkhir) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Jenis Trip", "Jumlah Reservasi", "Total Pendapatan (Lunas)"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabel = new JTable(model);
        List<Object[]> dataLaporan = reservasiController.getLaporanPenjualanPerJenisTrip(tanggalMulai, tanggalAkhir);
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);
        if (dataLaporan != null) { // Tambahkan null check
            for (Object[] baris : dataLaporan) {
                model.addRow(new Object[]{ baris[0], baris[1], formatMataUang.format(baris[2]) });
            }
        }
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel buatPanelLaporanDestinasi(Date tanggalMulai, Date tanggalAkhir) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        DefaultTableModel model = new DefaultTableModel(new String[]{"Nama Destinasi", "Jumlah Kunjungan"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tabel = new JTable(model);
        List<Object[]> dataLaporan = perjalananController.getLaporanDestinasiPopuler(tanggalMulai, tanggalAkhir);
        if (dataLaporan != null) { // Tambahkan null check
            for (Object[] baris : dataLaporan) {
                model.addRow(baris);
            }
        }
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

    // vvv METODE BARU UNTUK MEMILIH LOKASI DAN EKSPOR KE CSV vvv
    private void pilihLokasiDanEksporKeCSV(JTable tabel, String judulLaporanDefault) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan CSV");
        // Buat nama file default yang lebih baik
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String namaFileDefault = "Laporan_" + judulLaporanDefault.replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "") + "_" + dateFormat.format(new java.util.Date()) + ".csv";
        fileChooser.setSelectedFile(new File(namaFileDefault));
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Pastikan ekstensi .csv
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".csv");
            }
            tulisDataKeCSV(fileToSave, tabel, judulLaporanDefault);
        }
    }

    private void tulisDataKeCSV(File file, JTable tabel, String judulLaporan) {
        try (FileWriter fw = new FileWriter(file);
             BufferedWriter bw = new BufferedWriter(fw)) {

            DefaultTableModel model = (DefaultTableModel) tabel.getModel();
            int columnCount = model.getColumnCount();

            // Tulis Judul Laporan (opsional, sebagai baris pertama)
            // bw.write(judulLaporan);
            // bw.newLine();
            // bw.newLine(); // Baris kosong sebagai pemisah

            // Tulis Header Kolom
            for (int i = 0; i < columnCount; i++) {
                bw.write(model.getColumnName(i));
                if (i < columnCount - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();

            // Tulis Data Baris
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < columnCount; j++) {
                    Object cellValue = model.getValueAt(i, j);
                    String valueToWrite = (cellValue == null) ? "" : cellValue.toString();
                    
                    // Basic CSV escaping: jika ada koma, kelilingi dengan kutip ganda
                    if (valueToWrite.contains(",")) {
                        valueToWrite = "\"" + valueToWrite.replace("\"", "\"\"") + "\"";
                    }
                    bw.write(valueToWrite);
                    if (j < columnCount - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Laporan berhasil diekspor ke:\n" + file.getAbsolutePath(), "Ekspor Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengekspor laporan ke CSV: " + e.getMessage(), "Kesalahan Ekspor", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    

    private void eksporDataTabelKeKonsol(JTable tabel, String judulLaporan) {
        System.out.println("\n--- Laporan: " + judulLaporan + " ---");
        DefaultTableModel model = (DefaultTableModel) tabel.getModel();
        // Cetak Header
        for (int i = 0; i < model.getColumnCount(); i++) {
            System.out.print(model.getColumnName(i) + "\t|\t");
        }
        System.out.println();
        // Cetak Data
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                System.out.print(model.getValueAt(i, j) + "\t|\t");
            }
            System.out.println();
        }
        System.out.println("--- Akhir Laporan ---");
        JOptionPane.showMessageDialog(this, "Data laporan '" + judulLaporan + "' telah dicetak ke konsol IDE.", "Info Ekspor", JOptionPane.INFORMATION_MESSAGE);
    }
    // ^^^ AKHIR DARI METODE-METODE YANG PERLU ADA ^^^
}