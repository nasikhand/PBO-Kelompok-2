/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import config.ImageUtil;
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
    private final int DATA_PER_HALAMAN = 10;
    private int totalHalaman = 1;
    private int totalData = 0;

    public KelolaPerjalananView() {
        this.controller = new PerjalananController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        // Panel Atas: Judul dan Kolom Pencarian
        JPanel panelAtas = new JPanel(new BorderLayout(10, 5));
        panelAtas.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Paket Perjalanan");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        panelAtas.add(judul, BorderLayout.NORTH);

        txtPencarian = new JTextField();
        txtPencarian.putClientProperty("JTextField.placeholderText", "Cari berdasarkan Nama Paket...");
        panelAtas.add(txtPencarian, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.NORTH);

        // Tabel untuk menampilkan data
        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "ID", "Gambar", "Nama Paket", "Kota Tujuan", "Tgl Mulai", "Tgl Akhir", "Harga", "Kuota",
                        "Status" }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };
        tabel.setModel(modelTabel);
        tabel.setRowHeight(80);
        TableColumn imageColumn = tabel.getColumn("Gambar");
        imageColumn.setCellRenderer(new ImageRenderer());
        imageColumn.setPreferredWidth(120);

        TableColumn idColumn = tabel.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setWidth(0);

        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol Aksi CRUD
        JPanel panelTombolAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolAksi.setOpaque(false);
        JButton btnTambah = new JButton("Tambah Paket Baru");
        JButton btnUbah = new JButton("Ubah Paket");
        JButton btnHapus = new JButton("Hapus Paket");
        JButton btnKelolaRincian = new JButton("Kelola Rincian Destinasi"); // <-- TOMBOL BARU

        panelTombolAksi.add(btnTambah);
        panelTombolAksi.add(btnUbah);
        panelTombolAksi.add(btnKelolaRincian); // <-- PENAMBAHAN TOMBOL BARU
        panelTombolAksi.add(btnHapus);

        // Panel Paginasi
        JPanel panelPaginasi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPaginasi.setOpaque(false);
        btnSebelumnya = new JButton("« Seb");
        lblInfoHalaman = new JLabel("Hal 1 dari 1");
        lblInfoHalaman.setForeground(Color.WHITE);
        btnBerikutnya = new JButton("Ber »");
        panelPaginasi.add(btnSebelumnya);
        panelPaginasi.add(lblInfoHalaman);
        panelPaginasi.add(btnBerikutnya);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setOpaque(false);
        panelBawah.add(panelTombolAksi, BorderLayout.WEST);
        panelBawah.add(panelPaginasi, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);

        muatDataDenganPaginasi();

        // Action Listeners
        btnTambah.addActionListener(e -> bukaDialogForm(null));
        btnUbah.addActionListener(e -> ubahDataTerpilih());
        btnHapus.addActionListener(e -> hapusDataTerpilih());
        btnKelolaRincian.addActionListener(e -> bukaDialogRincian()); // <-- ACTION LISTENER BARU

        txtPencarian.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                halamanSaatIni = 1;
                muatDataDenganPaginasi();
            }

            public void removeUpdate(DocumentEvent e) {
                halamanSaatIni = 1;
                muatDataDenganPaginasi();
            }

            public void changedUpdate(DocumentEvent e) {
                halamanSaatIni = 1;
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
                            // Menggunakan ImageUtil untuk resizing berkualitas tinggi
                            gambar = config.ImageUtil.resizeImage(originalIcon, 100, 60);
                        } else {
                            System.err.println("Kelola Perjalanan - GAGAL memuat ImageIcon: " + imgFile.getAbsolutePath());
                        }
                    } catch (Exception ex) {
                        System.err.println("Kelola Perjalanan - EXCEPTION saat memuat gambar: " + imgFile.getAbsolutePath());
                        ex.printStackTrace();
                    }
                } else {
                     System.err.println("Kelola Perjalanan - File gambar TIDAK DITEMUKAN di path: " + imgFile.getAbsolutePath());
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
        FormPerjalananDialog dialog = new FormPerjalananDialog((Frame) SwingUtilities.getWindowAncestor(this), paket);
        dialog.setVisible(true);
        muatDataDenganPaginasi();
    }

    private void ubahDataTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin diubah.", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        PaketPerjalanan paket = controller.getPaketById(idPaket);
        if (paket != null) {
            bukaDialogForm(paket);
        } else {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan di basis data.", "Kesalahan",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusDataTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dihapus.", "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        String namaPaket = (String) modelTabel.getValueAt(barisTerpilih, 2);
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus paket '" + namaPaket + "'?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (controller.deletePaketPerjalanan(idPaket)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                if (modelTabel.getRowCount() == 1 && halamanSaatIni > 1) {
                    halamanSaatIni--;
                }
                muatDataDenganPaginasi();
            }
        }
    }

    // vvv METODE BARU UNTUK MEMBUKA DIALOG RINCIAN vvv
    private void bukaDialogRincian() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih paket perjalanan untuk mengelola rinciannya.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPaket = (int) modelTabel.getValueAt(barisTerpilih, 0);
        PaketPerjalanan paketTerpilih = controller.getPaketById(idPaket);

        if (paketTerpilih != null) {
            RincianPaketDialog dialog = new RincianPaketDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    paketTerpilih);
            dialog.setVisible(true);
            // Tidak perlu muat ulang data di sini karena rincian tidak ditampilkan di tabel
            // utama
        } else {
            JOptionPane.showMessageDialog(this, "Data paket tidak ditemukan di basis data.", "Kesalahan",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}