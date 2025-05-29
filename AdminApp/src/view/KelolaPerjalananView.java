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

    // Komponen dan variabel untuk Paginasi
    private JButton btnSebelumnya, btnBerikutnya;
    private JLabel lblInfoHalaman;
    private int halamanSaatIni = 1;
    private final int DATA_PER_HALAMAN = 10; // Atur jumlah data per halaman
    private int totalHalaman = 1;
    private int totalData = 0;

    public KelolaPerjalananView() {
        this.controller = new PerjalananController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false); // Sesuaikan dengan tema dasbor

        // Panel Atas: Judul dan Kolom Pencarian
        JPanel panelAtas = new JPanel(new BorderLayout(10, 5));
        panelAtas.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Paket Perjalanan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE); // Sesuaikan warna font
        panelAtas.add(judul, BorderLayout.NORTH);

        txtPencarian = new JTextField();
        txtPencarian.putClientProperty("JTextField.placeholderText", "Cari berdasarkan Nama Paket...");
        panelAtas.add(txtPencarian, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.NORTH);

        // Tabel untuk menampilkan data
        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
                new Object[][]{},
                // Kolom ID, Gambar, Nama Paket, Kota Tujuan, Tgl Mulai, Tgl Akhir, Harga, Kuota, Status
                new String[]{"ID", "Gambar", "Nama Paket", "Kota Tujuan", "Tgl Mulai", "Tgl Akhir", "Harga", "Kuota", "Status"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tidak bisa diedit
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) { // Kolom "Gambar" ada di indeks 1
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };
        tabel.setModel(modelTabel);

        // Pengaturan tampilan kolom
        tabel.setRowHeight(80); // Atur tinggi baris agar gambar muat
        TableColumn imageColumn = tabel.getColumn("Gambar");
        imageColumn.setCellRenderer(new ImageRenderer()); // Gunakan renderer kustom kita
        imageColumn.setPreferredWidth(120); // Atur lebar kolom gambar

        // Sembunyikan kolom ID jika tidak ingin ditampilkan
        TableColumn idColumn = tabel.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setWidth(0);
        idColumn.setPreferredWidth(0);


        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol Aksi CRUD
        JPanel panelTombolAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolAksi.setOpaque(false);
        JButton btnTambah = new JButton("Tambah Paket Baru");
        JButton btnUbah = new JButton("Ubah Paket");
        JButton btnHapus = new JButton("Hapus Paket");
        JButton btnCekGambar = new JButton("Cek Status Gambar");
        btnCekGambar.setBackground(new Color(255, 193, 7));
        btnCekGambar.setForeground(Color.BLACK);

        panelTombolAksi.add(btnTambah);
        panelTombolAksi.add(btnUbah);
        panelTombolAksi.add(btnHapus);
        panelTombolAksi.add(btnCekGambar);

        // Panel Paginasi
        JPanel panelPaginasi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPaginasi.setOpaque(false);
        btnSebelumnya = new JButton("« Seb");
        lblInfoHalaman = new JLabel("Hal 1 dari 1");
        lblInfoHalaman.setForeground(Color.WHITE); // Sesuaikan warna
        btnBerikutnya = new JButton("Ber »");
        panelPaginasi.add(btnSebelumnya);
        panelPaginasi.add(lblInfoHalaman);
        panelPaginasi.add(btnBerikutnya);

        // Panel Bawah Gabungan (Tombol Aksi dan Paginasi)
        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setOpaque(false);
        panelBawah.add(panelTombolAksi, BorderLayout.WEST);
        panelBawah.add(panelPaginasi, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);

        muatDataDenganPaginasi(); // Panggil metode baru untuk memuat data

        // Action Listeners
        btnTambah.addActionListener(e -> bukaDialogForm(null));
        btnUbah.addActionListener(e -> ubahDataTerpilih());
        btnHapus.addActionListener(e -> hapusDataTerpilih());
        btnCekGambar.addActionListener(e -> cekStatusGambarTerpilih());

        txtPencarian.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                halamanSaatIni = 1; // Reset ke halaman pertama saat pencarian
                muatDataDenganPaginasi();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                halamanSaatIni = 1; // Reset ke halaman pertama
                muatDataDenganPaginasi();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                halamanSaatIni = 1; // Reset ke halaman pertama
                muatDataDenganPaginasi();
            }
        });

        btnSebelumnya.addActionListener(e -> {
            if (halamanSaatIni > 1) {
                halamanSaatIni--;
                muatDataDenganPaginasi();
            }
        });

        btnBerikutnya.addActionListener(e -> {
            if (halamanSaatIni < totalHalaman) {
                halamanSaatIni++;
                muatDataDenganPaginasi();
            }
        });
        
        // Listener untuk pemilihan baris di tabel (jika diperlukan untuk aksi lain)
        tabel.getSelectionModel().addListSelectionListener(e -> {
            // Jika ada aksi yang perlu dilakukan saat baris dipilih, tambahkan di sini
            // Misalnya, mengaktifkan/menonaktifkan tombol ubah/hapus
            // boolean barisDipilih = tabel.getSelectedRow() != -1;
            // btnUbah.setEnabled(barisDipilih);
            // btnHapus.setEnabled(barisDipilih);
            // btnCekGambar.setEnabled(barisDipilih);
        });
    }

    private void muatDataDenganPaginasi() {
        String filterText = txtPencarian.getText().trim();

        totalData = controller.getTotalPaketPerjalananCount(filterText);
        totalHalaman = (int) Math.ceil((double) totalData / DATA_PER_HALAMAN);
        if (totalHalaman == 0) totalHalaman = 1;
        if (halamanSaatIni > totalHalaman) halamanSaatIni = totalHalaman;
        if (halamanSaatIni < 1) halamanSaatIni = 1;

        List<PaketPerjalanan> daftarPaket = controller.getPaketPerjalananWithPagination(halamanSaatIni, DATA_PER_HALAMAN, filterText);

        modelTabel.setRowCount(0);
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);

        for (PaketPerjalanan paket : daftarPaket) {
            ImageIcon gambar = null;
            String namaFileGambarPaket = paket.getGambar();

            if (namaFileGambarPaket != null && !namaFileGambarPaket.isEmpty()) {
                String imagePathRelatif = "images/paket_perjalanan/" + namaFileGambarPaket;
                File imgFile = new File(imagePathRelatif);

                if (imgFile.exists() && imgFile.isFile()) {
                    try {
                        ImageIcon originalIcon = new ImageIcon(imgFile.getAbsolutePath());
                        if (originalIcon.getIconWidth() != -1) {
                            Image resizedImage = originalIcon.getImage().getScaledInstance(100, 60, Image.SCALE_SMOOTH);
                            gambar = new ImageIcon(resizedImage);
                        } else {
                            System.err.println("Kelola Perjalanan - GAGAL memuat ImageIcon (mungkin file korup atau bukan format gambar): " + imgFile.getAbsolutePath());
                        }
                    } catch (Exception ex) {
                        System.err.println("Kelola Perjalanan - EXCEPTION saat memuat gambar: " + imgFile.getAbsolutePath());
                        ex.printStackTrace();
                    }
                } else {
                     System.err.println("Kelola Perjalanan - File gambar TIDAK DITEMUKAN di path absolut: " + imgFile.getAbsolutePath());
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

        lblInfoHalaman.setText("Hal " + halamanSaatIni + " dari " + totalHalaman + " (" + totalData + " data)");
        btnSebelumnya.setEnabled(halamanSaatIni > 1);
        btnBerikutnya.setEnabled(halamanSaatIni < totalHalaman);
    }

    private void bukaDialogForm(PaketPerjalanan paket) {
        FormPerjalananDialog dialog = new FormPerjalananDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), paket
        );
        dialog.setVisible(true);
        halamanSaatIni = 1; // Kembali ke halaman pertama setelah ada perubahan data
        muatDataDenganPaginasi(); // Muat ulang data
    }

    private void ubahDataTerpilih() {
        int barisTerpilihView = tabel.getSelectedRow();
        if (barisTerpilihView == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin diubah.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Konversi dari view index ke model index (jika ada sorting/filtering client-side, tapi kita tidak pakai lagi)
        // int barisTerpilihModel = tabel.convertRowIndexToModel(barisTerpilihView);
        int idPaket = (int) modelTabel.getValueAt(barisTerpilihView, 0); // Ambil ID dari kolom pertama model tabel

        PaketPerjalanan paket = controller.getPaketById(idPaket);
        if (paket != null) {
            bukaDialogForm(paket);
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan di basis data.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            muatDataDenganPaginasi(); // Muat ulang jika data ternyata sudah tidak ada
        }
    }

    private void hapusDataTerpilih() {
        int barisTerpilihView = tabel.getSelectedRow();
        if (barisTerpilihView == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // int barisTerpilihModel = tabel.convertRowIndexToModel(barisTerpilihView);
        int idPaket = (int) modelTabel.getValueAt(barisTerpilihView, 0);
        String namaPaket = (String) modelTabel.getValueAt(barisTerpilihView, 2); // Nama paket di kolom ke-3 (indeks 2)

        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus paket '" + namaPaket + "'?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.deletePaketPerjalanan(idPaket);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                // Jika data terakhir di halaman saat ini dihapus, mungkin perlu mundur satu halaman
                if (modelTabel.getRowCount() == 1 && halamanSaatIni > 1) {
                    halamanSaatIni--;
                }
                muatDataDenganPaginasi();
            } else {
                // Pesan error sudah ditampilkan oleh controller
            }
        }
    }

    private void cekStatusGambarTerpilih() {
        int barisTerpilihView = tabel.getSelectedRow();
        if (barisTerpilihView == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dicek gambarnya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // int barisTerpilihModel = tabel.convertRowIndexToModel(barisTerpilihView);
        int idPaket = (int) modelTabel.getValueAt(barisTerpilihView, 0);
        PaketPerjalanan paket = controller.getPaketById(idPaket);

        if (paket == null) {
            JOptionPane.showMessageDialog(this, "Data paket tidak ditemukan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String namaFileDariDB = paket.getGambar();
        if (namaFileDariDB == null || namaFileDariDB.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada nama file gambar yang tersimpan di database untuk data ini.", "Info Gambar", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        File fileGambar = new File("images/paket_perjalanan/" + namaFileDariDB);
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

        JTextArea textArea = new JTextArea(pesan.toString());
        textArea.setEditable(false);
        JScrollPane scrollPanePesan = new JScrollPane(textArea);
        scrollPanePesan.setPreferredSize(new Dimension(600, 250));
        JOptionPane.showMessageDialog(this, scrollPanePesan, "Laporan Status Gambar", JOptionPane.INFORMATION_MESSAGE);
    }
}