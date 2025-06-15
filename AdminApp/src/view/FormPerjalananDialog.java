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

    private JTextField txtNamaPaket, txtKuota, txtHarga;
    private JTextArea txtDeskripsi;
    private JComboBox<Kota> cmbKota;
    private JComboBox<String> cmbStatus;
    private JButton btnSimpan;
    private JButton btnPilihGambar;
    private JLabel lblNamaFileGambar;
    private com.toedter.calendar.JDateChooser dateChooserMulai, dateChooserAkhir; // Menggunakan JDateChooser

    private PerjalananController controller;
    private File fileGambarDipilih;
    private PaketPerjalanan paketToEdit;

    public FormPerjalananDialog(Frame owner, PaketPerjalanan paket) {
        super(owner, "Formulir Paket Perjalanan", true);
        
        this.paketToEdit = paket;
        this.controller = new PerjalananController();

        setSize(550, 650);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        inisialisasiKomponen();
        muatDataKota();
        aturLayoutKomponen(panelForm);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombol.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15));
        panelTombol.add(btnSimpan);
        add(panelTombol, BorderLayout.SOUTH);

        aturActionListeners();

        if (paketToEdit != null) {
            setTitle("Ubah Paket Perjalanan");
            btnSimpan.setText("Perbarui");
            isiDataForm();
        }
    }

    private void inisialisasiKomponen() {
        txtNamaPaket = new JTextField(20);
        cmbKota = new JComboBox<>();
        dateChooserMulai = new com.toedter.calendar.JDateChooser();
        dateChooserAkhir = new com.toedter.calendar.JDateChooser();
        dateChooserMulai.setDateFormatString("yyyy-MM-dd");
        dateChooserAkhir.setDateFormatString("yyyy-MM-dd");
        txtKuota = new JTextField();
        txtHarga = new JTextField();
        txtDeskripsi = new JTextArea(5, 20);
        cmbStatus = new JComboBox<>(new String[]{"tersedia", "penuh", "selesai"});
        btnPilihGambar = new JButton("Pilih Gambar...");
        lblNamaFileGambar = new JLabel("Belum ada gambar dipilih.");
        btnSimpan = new JButton("Simpan");
    }

    private void muatDataKota() {
        List<Kota> daftarKota = controller.getAllKota();
        for (Kota kota : daftarKota) {
            cmbKota.addItem(kota);
        }
    }
    
    private void aturLayoutKomponen(JPanel panelForm) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Nama Paket:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(txtNamaPaket, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Kota Tujuan:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(cmbKota, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Tanggal Mulai:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(dateChooserMulai, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Tanggal Akhir:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(dateChooserAkhir, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Harga (Rp):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(txtHarga, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Kuota:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(txtKuota, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(cmbStatus, gbc);
        
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Deskripsi:"), gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1.0; panelForm.add(new JScrollPane(txtDeskripsi), gbc); gbc.weightx = 0;
        
        gbc.gridy++;
        gbc.gridx = 0; gbc.gridy = y; panelForm.add(new JLabel("Gambar Paket:"), gbc);
        JPanel panelGambar = new JPanel(new BorderLayout(5, 0));
        panelGambar.add(btnPilihGambar, BorderLayout.WEST);
        panelGambar.add(lblNamaFileGambar, BorderLayout.CENTER);
        gbc.gridx = 1; gbc.gridy = y++; panelForm.add(panelGambar, gbc);
    }

    private void aturActionListeners() {
        btnPilihGambar.addActionListener(e -> pilihGambar());
        btnSimpan.addActionListener(e -> simpanData());
    }
    
    private void isiDataForm() {
        txtNamaPaket.setText(paketToEdit.getNamaPaket());
        dateChooserMulai.setDate(paketToEdit.getTanggalMulai());
        dateChooserAkhir.setDate(paketToEdit.getTanggalAkhir());
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

    private void pilihGambar() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Gambar (JPG, PNG, GIF)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileGambarDipilih = chooser.getSelectedFile();
            lblNamaFileGambar.setText(fileGambarDipilih.getName());
        }
    }

    private String simpanGambarKeFolder() {
        if (fileGambarDipilih == null) return null;

        // <<< PERUBAHAN PATH DIMULAI DI SINI >>>
        Path folderTujuan = Paths.get("../SharedAppImages/paket_perjalanan");
        // <<< AKHIR PERUBAHAN PATH >>>

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

    private void simpanData() {
        // ... (Kode validasi yang sudah kita buat sebelumnya tetap sama)
        String namaPaketStr = txtNamaPaket.getText().trim();
        java.util.Date tglMulaiUtil = dateChooserMulai.getDate();
        java.util.Date tglAkhirUtil = dateChooserAkhir.getDate();
        String hargaStr = txtHarga.getText().trim();
        String kuotaStr = txtKuota.getText().trim();
        Kota kotaTerpilih = (Kota) cmbKota.getSelectedItem();

        if (namaPaketStr.isEmpty() || tglMulaiUtil == null || tglAkhirUtil == null || hargaStr.isEmpty() || kuotaStr.isEmpty() || kotaTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Date tanggalMulai = new Date(tglMulaiUtil.getTime());
        Date tanggalAkhir = new Date(tglAkhirUtil.getTime());
        BigDecimal harga;
        int kuota;

        try {
            harga = new BigDecimal(hargaStr);
            kuota = Integer.parseInt(kuotaStr);
            if (harga.compareTo(BigDecimal.ZERO) < 0 || kuota < 0) {
                JOptionPane.showMessageDialog(this, "Harga dan Kuota tidak boleh negatif.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan Kuota harus angka.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tanggalMulai.after(tanggalAkhir)) {
            JOptionPane.showMessageDialog(this, "Tanggal Mulai tidak boleh setelah Tanggal Akhir.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String namaFileGambar = (paketToEdit != null) ? paketToEdit.getGambar() : null;
        if (fileGambarDipilih != null) {
            String namaFileLama = namaFileGambar;
            namaFileGambar = simpanGambarKeFolder();
            if (namaFileGambar == null) return;
            
            if (namaFileLama != null && !namaFileLama.isEmpty()) {
                // <<< PERUBAHAN PATH HAPUS GAMBAR LAMA >>>
                File fileLama = new File("../SharedAppImages/paket_perjalanan/" + namaFileLama);
                if (fileLama.exists()) {
                    fileLama.delete();
                }
            }
        }

        PaketPerjalanan paket = new PaketPerjalanan();
        paket.setNamaPaket(namaPaketStr);
        paket.setKotaId(kotaTerpilih.getId());
        paket.setTanggalMulai(tanggalMulai);
        paket.setTanggalAkhir(tanggalAkhir);
        paket.setHarga(harga);
        paket.setKuota(kuota);
        paket.setStatus((String) cmbStatus.getSelectedItem());
        paket.setDeskripsi(txtDeskripsi.getText());
        paket.setGambar(namaFileGambar);

        boolean sukses;
        if (paketToEdit == null) {
            sukses = controller.addPaketPerjalanan(paket);
        } else {
            paket.setId(paketToEdit.getId());
            sukses = controller.updatePaketPerjalanan(paket);
        }

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");
            dispose();
        }
    }
}