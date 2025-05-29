/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PerjalananController;
import model.PaketPerjalanan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent; 
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KelolaPerjalananView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private PerjalananController controller;
    private JTextField txtPencarian; 
    private TableRowSorter<DefaultTableModel> sorter; 
    private JButton btnKelolaRincian;

    public KelolaPerjalananView() {
        this.controller = new PerjalananController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);
        
         // Panel Atas: Judul dan Kolom Pencarian
        JPanel panelAtas = new JPanel(new BorderLayout(10,5));
        panelAtas.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Paket Perjalanan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        add(judul, BorderLayout.NORTH);
        
        txtPencarian = new JTextField();
        txtPencarian.putClientProperty("JTextField.placeholderText", "Cari berdasarkan Nama Paket, Kota, atau Status...");
        panelAtas.add(txtPencarian, BorderLayout.SOUTH);
        
        add(panelAtas, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();

        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Gambar", "Nama Paket", "Kota Tujuan", "Tgl Mulai", "Tgl Akhir", "Harga", "Kuota", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) { return ImageIcon.class; }
                return Object.class;
            }
        };
        tabel.setModel(modelTabel);
        
        // Inisialisasi TableRowSorter
        sorter = new TableRowSorter<>(modelTabel);
        tabel.setRowSorter(sorter);
        
        tabel.setRowHeight(80);
        TableColumn imageColumn = tabel.getColumn("Gambar");
        imageColumn.setCellRenderer(new ImageRenderer());
        imageColumn.setPreferredWidth(120);

        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.setOpaque(false);
        JButton btnTambah = new JButton("Tambah Paket Baru");
        JButton btnUbah = new JButton("Ubah Paket");
        JButton btnHapus = new JButton("Hapus Paket");
        
        // vvv TOMBOL BARU UNTUK DEBUGGING vvv
        JButton btnCekGambar = new JButton("Cek Status Gambar");
        btnCekGambar.setBackground(new Color(255, 193, 7)); // Warna kuning
        btnCekGambar.setForeground(Color.BLACK);
        btnKelolaRincian = new JButton("Kelola Rincian Destinasi"); 
        
        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnCekGambar);
        panelTombol.add(btnKelolaRincian);
        add(panelTombol, BorderLayout.SOUTH);

        muatData();

        // Action Listeners
        btnTambah.addActionListener(e -> bukaDialogForm(null));
        btnUbah.addActionListener(e -> ubahDataTerpilih());
        btnHapus.addActionListener(e -> hapusDataTerpilih());
        btnCekGambar.addActionListener(e -> cekStatusGambarTerpilih());
         btnKelolaRincian.addActionListener(e -> bukaDialogKelolaRincian());
        
        // Listener untuk kolom pencarian
        txtPencarian.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { cariData(); }
            @Override
            public void removeUpdate(DocumentEvent e) { cariData(); }
            @Override
            public void changedUpdate(DocumentEvent e) { cariData(); }
        });

    }

    private void muatData() {
        modelTabel.setRowCount(0);
        List<PaketPerjalanan> daftarPaket = controller.getAllPaketPerjalanan();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        for (PaketPerjalanan paket : daftarPaket) {
            ImageIcon gambar = null;
            if (paket.getGambar() != null && !paket.getGambar().isEmpty()) {
                String imagePath = "images/paket_perjalanan/" + paket.getGambar();
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(imagePath);
                    Image resizedImage = originalIcon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
                    gambar = new ImageIcon(resizedImage);
                }
            }
            modelTabel.addRow(new Object[]{
                paket.getId(),
                gambar,
                paket.getNamaPaket(),
                paket.getNamaKota(),
                paket.getTanggalMulai(),
                paket.getTanggalAkhir(),
                formatMataUang.format(paket.getHarga()),
                paket.getKuota(),
                paket.getStatus()
            });
        }
    }

    // vvv METODE BARU UNTUK DIAGNOSIS vvv
    private void cekStatusGambarTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dicek gambarnya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        // Ambil data lengkap dari database untuk memastikan nama file tidak terpotong
        PaketPerjalanan paket = controller.getPaketById(idPaket);

        if (paket == null) {
            JOptionPane.showMessageDialog(this, "Data paket tidak ditemukan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String namaFileDariDB = paket.getGambar();
        if (namaFileDariDB == null || namaFileDariDB.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada nama file gambar yang tersimpan di database untuk data ini.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File fileGambar = new File("images/paket_perjalanan/" + namaFileDariDB);

        // Bangun pesan detail untuk ditampilkan
        StringBuilder pesan = new StringBuilder();
        pesan.append("--- Hasil Pengecekan Gambar ---\n\n");
        pesan.append("Nama File dari Database:\n  ").append(namaFileDariDB).append("\n\n");
        pesan.append("Program Mencari di Path Absolut:\n  ").append(fileGambar.getAbsolutePath()).append("\n\n");
        pesan.append("Apakah File Ditemukan? (exists()):\n  ").append(fileGambar.exists()).append("\n\n");
        pesan.append("Apakah File Bisa Dibaca? (canRead()):\n  ").append(fileGambar.canRead()).append("\n\n");

        if (!fileGambar.exists()) {
            pesan.append("KESIMPULAN: File TIDAK ADA di lokasi yang dicari program.\n");
            pesan.append("Pastikan folder dan nama file Anda SAMA PERSIS dengan path absolut di atas.");
        } else {
            pesan.append("KESIMPULAN: File DITEMUKAN. Jika gambar tetap tidak muncul, mungkin file gambarnya korup atau ada masalah izin baca.");
        }

        // Tampilkan semua informasi dalam satu popup
        JTextArea textArea = new JTextArea(pesan.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 250));
        JOptionPane.showMessageDialog(this, scrollPane, "Laporan Status Gambar", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cariData() {
        String teks = txtPencarian.getText();
        if (teks.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Filter berdasarkan beberapa kolom (indeks kolom dimulai dari 0)
            // Indeks kolom: 2 (Nama Paket), 3 (Kota Tujuan), 8 (Status)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + teks, 2, 3, 8));
        }
    }

    // ... (metode bukaDialogForm, ubahDataTerpilih, hapusDataTerpilih tidak berubah)
    private void bukaDialogForm(PaketPerjalanan paket) {
        FormPerjalananDialog dialog = new FormPerjalananDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), paket
        );
        dialog.setVisible(true);
        muatData();
    }
    
    private void ubahDataTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        PaketPerjalanan paket = controller.getPaketById(idPaket);
        if (paket != null) {
            bukaDialogForm(paket);
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan di basis data.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusDataTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        String namaPaket = (String) modelTabel.getValueAt(barisTerpilih, 2); // Indeks nama paket sekarang 2

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus paket '" + namaPaket + "'?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.deletePaketPerjalanan(idPaket);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                muatData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void bukaDialogKelolaRincian() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih paket perjalanan terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        // Ambil objek PaketPerjalanan lengkap, karena dialog mungkin butuh nama paket, dll.
        PaketPerjalanan paketTerpilih = controller.getPaketById(idPaket); 
        // (Pastikan getPaketById mengembalikan objek PaketPerjalanan lengkap)

        if (paketTerpilih != null) {
            RincianPaketDialog dialog = new RincianPaketDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), paketTerpilih
            );
            dialog.setVisible(true);
            // Tidak perlu muatData() di sini karena dialog rincian tidak mengubah tabel utama
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memuat data paket perjalanan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
}