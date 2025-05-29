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

    // Atribut untuk data
    private PerjalananController controller;
    private File fileGambarDipilih;

    /**
     * Constructor untuk membuat dialog form.
     * @param owner Frame induk dari dialog ini.
     * @param paket Objek PaketPerjalanan (null jika membuat baru, terisi jika mengedit).
     */
    public FormPerjalananDialog(Frame owner, PaketPerjalanan paket) {
        super(owner, "Formulir Paket Perjalanan", true);
        
        this.controller = new PerjalananController();

        // Pengaturan dasar JDialog
        setSize(550, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // Panel utama untuk form dengan padding
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Inisialisasi semua komponen
        inisialisasiKomponen();

        // Mengisi dropdown kota dari database
        muatDataKota();

        // Mengatur layout komponen pada panel
        aturLayoutKomponen(panelForm);

        // Menambahkan panel form ke bagian tengah dialog
        add(panelForm, BorderLayout.CENTER);

        // Panel untuk tombol Simpan
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombol.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        panelTombol.add(btnSimpan);
        add(panelTombol, BorderLayout.SOUTH);

        // Menambahkan action listener untuk tombol
        aturActionListeners();
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
        
        // Baris 2: Tanggal Mulai
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Tanggal Mulai:"), gbc);
        gbc.gridx = 1; panelForm.add(txtTanggalMulai, gbc);
        
        // Baris 3: Tanggal Akhir
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Tanggal Akhir:"), gbc);
        gbc.gridx = 1; panelForm.add(txtTanggalAkhir, gbc);
        
        // Baris 4: Harga
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Harga (Rp):"), gbc);
        gbc.gridx = 1; panelForm.add(txtHarga, gbc);
        
        // Baris 5: Kuota
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Kuota:"), gbc);
        gbc.gridx = 1; panelForm.add(txtKuota, gbc);
        
        // Baris 6: Status
        gbc.gridy++;
        gbc.gridx = 0; panelForm.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; panelForm.add(cmbStatus, gbc);
        
        // Baris 7: Deskripsi
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Agar label "Deskripsi" rata atas
        gbc.gridx = 0; panelForm.add(new JLabel("Deskripsi:"), gbc);
        gbc.anchor = GridBagConstraints.WEST; // Kembalikan ke default
        gbc.gridx = 1; panelForm.add(new JScrollPane(txtDeskripsi), gbc);
        
        // Baris 8: Gambar Paket
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
     * Membuka JFileChooser untuk memilih file gambar.
     */
    private void pilihGambar() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Gambar (JPG, PNG, GIF)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fileGambarDipilih = chooser.getSelectedFile();
            lblNamaFileGambar.setText(fileGambarDipilih.getName());
        }
    }

    /**
     * Menyalin file gambar yang dipilih ke folder tujuan di dalam proyek.
     * @param file File sumber yang akan disalin.
     * @return String nama file baru yang telah disalin, atau null jika gagal.
     */
    private String simpanGambarKeFolder(File file) {
        if (file == null) return null;

        Path folderTujuan = Paths.get("images/paket_perjalanan");
        if (!Files.exists(folderTujuan)) {
            try {
                Files.createDirectories(folderTujuan);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal membuat direktori gambar.", "Kesalahan File", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        String namaFileAsli = file.getName();
        String namaFileBaru = System.currentTimeMillis() + "_" + namaFileAsli;
        Path fileTujuan = folderTujuan.resolve(namaFileBaru);

        try {
            Files.copy(file.toPath(), fileTujuan, StandardCopyOption.REPLACE_EXISTING);
            return namaFileBaru;
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyalin file gambar.", "Kesalahan File", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Mengumpulkan semua data dari form, memvalidasi, dan menyimpannya ke database.
     */
    private void simpanData() {
        try {
            String namaFileGambar = simpanGambarKeFolder(fileGambarDipilih);

            PaketPerjalanan paket = new PaketPerjalanan();
            paket.setNamaPaket(txtNamaPaket.getText());
            paket.setKotaId(((Kota) cmbKota.getSelectedItem()).getId());
            paket.setTanggalMulai(Date.valueOf(txtTanggalMulai.getText()));
            paket.setTanggalAkhir(Date.valueOf(txtTanggalAkhir.getText()));
            paket.setHarga(new BigDecimal(txtHarga.getText()));
            paket.setKuota(Integer.parseInt(txtKuota.getText()));
            paket.setStatus((String) cmbStatus.getSelectedItem());
            paket.setDeskripsi(txtDeskripsi.getText());
            if (namaFileGambar != null) {
                paket.setGambar(namaFileGambar);
            }

            boolean sukses = controller.addPaketPerjalanan(paket);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Data paket perjalanan berhasil disimpan!");
                dispose(); // Menutup dialog setelah berhasil
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke basis data.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Input tidak valid. Periksa format tanggal (yyyy-mm-dd) atau angka.\nError: " + ex.getMessage(), "Input Tidak Valid", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan yang tidak terduga: " + ex.getMessage(), "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }
}
