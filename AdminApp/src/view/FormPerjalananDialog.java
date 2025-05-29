/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PerjalananController;
import model.Kota;
import model.PaketPerjalanan;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;

public class FormPerjalananDialog extends JDialog {

    // Deklarasi Komponen UI
    private JTextField txtNamaPaket, txtTanggalMulai, txtTanggalAkhir, txtKuota, txtHarga;
    private JTextArea txtDeskripsi;
    private JComboBox<Kota> cmbKota;
    private JComboBox<String> cmbStatus;
    private JButton btnSimpan;
    private JButton btnPilihGambar;
    private JLabel lblNamaFileGambar;

    // Atribut untuk data dan logic
    private PerjalananController controller;
    private File fileGambarDipilih;
    private PaketPerjalanan paketToEdit;

    /**
     * Constructor untuk membuat dialog form.
     * @param owner Frame induk dari dialog ini.
     * @param paket Objek PaketPerjalanan (null jika membuat baru, terisi jika mengedit).
     */
    public FormPerjalananDialog(Frame owner, PaketPerjalanan paket) {
        super(owner, "Formulir Paket Perjalanan", true);
        
        this.paketToEdit = paket;
        this.controller = new PerjalananController();

        // Pengaturan dasar JDialog
        setSize(550, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel utama untuk form dengan padding
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Inisialisasi, pengaturan layout, dan pengisian data komponen
        inisialisasiKomponen();
        muatDataKota();
        aturLayoutKomponen(panelForm);

        // Menambahkan panel form ke dialog
        add(panelForm, BorderLayout.CENTER);

        // Panel untuk tombol Simpan
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombol.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        panelTombol.add(btnSimpan);
        add(panelTombol, BorderLayout.SOUTH);

        // Menambahkan action listener untuk tombol-tombol
        aturActionListeners();

        // Jika ini adalah mode "Ubah", isi form dengan data yang ada
        if (paketToEdit != null) {
            setTitle("Ubah Paket Perjalanan");
            btnSimpan.setText("Perbarui");
            isiDataForm();
        }
    }

    /**
     * Inisialisasi semua komponen Swing.
     */
    private void inisialisasiKomponen() {
        txtNamaPaket = new JTextField(20);
        cmbKota = new JComboBox<>();
        txtTanggalMulai = new JTextField("yyyy-mm-dd");
        txtTanggalAkhir = new JTextField("yyyy-mm-dd");
        txtKuota = new JTextField();
        txtHarga = new JTextField();
        txtDeskripsi = new JTextArea(5, 20);
        cmbStatus = new JComboBox<>(new String[]{"tersedia", "penuh", "selesai"});
        btnPilihGambar = new JButton("Pilih Gambar...");
        lblNamaFileGambar = new JLabel("Belum ada gambar dipilih.");
        btnSimpan = new JButton("Simpan");
    }

    /**
     * Mengambil data kota dari controller dan mengisinya ke JComboBox.
     */
    private void muatDataKota() {
        List<Kota> daftarKota = controller.getAllKota();
        for (Kota kota : daftarKota) {
            cmbKota.addItem(kota);
        }
    }
    
    /**
     * Mengatur posisi semua komponen UI di dalam panel menggunakan GridBagLayout.
     * @param panelForm Panel tempat komponen akan diletakkan.
     */
    private void aturLayoutKomponen(JPanel panelForm) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Baris 0: Nama Paket
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Nama Paket:"), gbc);
        gbc.gridx = 1; panelForm.add(txtNamaPaket, gbc);
        
        // Baris 1: Kota Tujuan
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Kota Tujuan:"), gbc);
        gbc.gridx = 1; panelForm.add(cmbKota, gbc);
        
        // Baris-baris selanjutnya
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Tanggal Mulai:"), gbc);
        gbc.gridx = 1; panelForm.add(txtTanggalMulai, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Tanggal Akhir:"), gbc);
        gbc.gridx = 1; panelForm.add(txtTanggalAkhir, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Harga (Rp):"), gbc);
        gbc.gridx = 1; panelForm.add(txtHarga, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Kuota:"), gbc);
        gbc.gridx = 1; panelForm.add(txtKuota, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; panelForm.add(cmbStatus, gbc);
        
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; panelForm.add(new JLabel("Deskripsi:"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; panelForm.add(new JScrollPane(txtDeskripsi), gbc);
        
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Gambar Paket:"), gbc);
        JPanel panelGambar = new JPanel(new BorderLayout(5, 0));
        panelGambar.add(btnPilihGambar, BorderLayout.WEST);
        panelGambar.add(lblNamaFileGambar, BorderLayout.CENTER);
        gbc.gridx = 1; panelForm.add(panelGambar, gbc);
    }

    /**
     * Menetapkan action listener untuk tombol-tombol interaktif.
     */
    private void aturActionListeners() {
        btnPilihGambar.addActionListener(e -> pilihGambar());
        btnSimpan.addActionListener(e -> simpanData());
    }
    
    /**
     * Jika dalam mode Ubah, metode ini mengisi semua field form dengan data dari objek paket.
     */
    private void isiDataForm() {
        txtNamaPaket.setText(paketToEdit.getNamaPaket());
        txtTanggalMulai.setText(paketToEdit.getTanggalMulai().toString());
        txtTanggalAkhir.setText(paketToEdit.getTanggalAkhir().toString());
        txtKuota.setText(String.valueOf(paketToEdit.getKuota()));
        txtHarga.setText(paketToEdit.getHarga().toPlainString());
        txtDeskripsi.setText(paketToEdit.getDeskripsi());
        cmbStatus.setSelectedItem(paketToEdit.getStatus());

        for (int i = 0; i < cmbKota.getItemCount(); i++) {
            if (cmbKota.getItemAt(i).getId() == paketToEdit.getKotaId()) {
                cmbKota.setSelectedIndex(i);
                break;
            }
        }

        if (paketToEdit.getGambar() != null && !paketToEdit.getGambar().isEmpty()) {
            lblNamaFileGambar.setText(paketToEdit.getGambar());
        }
    }

    /**
     * Membuka JFileChooser untuk memilih file gambar.
     */
    private void pilihGambar() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Gambar (JPG, PNG, GIF)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileGambarDipilih = chooser.getSelectedFile();
            lblNamaFileGambar.setText(fileGambarDipilih.getName());
        }
    }

    /**
     * Menyalin file gambar yang dipilih ke folder tujuan dan mengganti spasi di nama filenya.
     * @return String nama file baru yang telah disalin, atau null jika gagal.
     */
    private String simpanGambarKeFolder() {
        if (fileGambarDipilih == null) return null;

        Path folderTujuan = Paths.get("images/paket_perjalanan");
        if (!Files.exists(folderTujuan)) {
            try { Files.createDirectories(folderTujuan); } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal membuat direktori gambar.", "Kesalahan File", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        String namaFileAsli = fileGambarDipilih.getName().replace(" ", "_");
        String namaFileBaru = System.currentTimeMillis() + "_" + namaFileAsli;
        Path fileTujuan = folderTujuan.resolve(namaFileBaru);

        try {
            Files.copy(fileGambarDipilih.toPath(), fileTujuan, StandardCopyOption.REPLACE_EXISTING);
            return namaFileBaru;
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyalin file gambar.", "Kesalahan File", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Mengumpulkan semua data dari form, memvalidasi, dan menyimpannya ke database.
     * Menangani logika untuk mode Tambah dan Ubah.
     */
    private void simpanData() {
        try {
            String namaFileGambar = (paketToEdit != null) ? paketToEdit.getGambar() : null;

            // Jika ada file gambar BARU yang dipilih
            if (fileGambarDipilih != null) {
                String namaFileLama = namaFileGambar;
                namaFileGambar = simpanGambarKeFolder();
                if (namaFileGambar == null) return; // Proses simpan gambar gagal
                
                // Hapus gambar lama jika ada (hanya dalam mode Ubah)
                if (namaFileLama != null && !namaFileLama.isEmpty()) {
                    File fileLama = new File("images/paket_perjalanan/" + namaFileLama);
                    if (fileLama.exists()) {
                        fileLama.delete();
                    }
                }
            }

            // Siapkan objek PaketPerjalanan dengan data dari form
            PaketPerjalanan paket = new PaketPerjalanan();
            paket.setNamaPaket(txtNamaPaket.getText());
            paket.setKotaId(((Kota) cmbKota.getSelectedItem()).getId());
            paket.setTanggalMulai(Date.valueOf(txtTanggalMulai.getText()));
            paket.setTanggalAkhir(Date.valueOf(txtTanggalAkhir.getText()));
            paket.setHarga(new BigDecimal(txtHarga.getText()));
            paket.setKuota(Integer.parseInt(txtKuota.getText()));
            paket.setStatus((String) cmbStatus.getSelectedItem());
            paket.setDeskripsi(txtDeskripsi.getText());
            paket.setGambar(namaFileGambar);

            boolean sukses;
            if (paketToEdit == null) {
                // Mode Tambah: Panggil controller untuk menambah data baru
                sukses = controller.addPaketPerjalanan(paket);
            } else {
                // Mode Ubah: Set ID dan panggil controller untuk memperbarui data
                paket.setId(paketToEdit.getId());
                sukses = controller.updatePaketPerjalanan(paket);
            }

            if (sukses) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
                dispose(); // Tutup dialog setelah berhasil
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke basis data.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid. Periksa format tanggal (yyyy-mm-dd) atau angka.", "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan yang tidak terduga: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
}