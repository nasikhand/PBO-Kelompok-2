/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.DestinasiController;
import controller.KotaController; // Untuk mengambil daftar kota
import model.Destinasi;
import model.Kota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KelolaDestinasiView extends JPanel {
    private JTable tabelDestinasi;
    private DefaultTableModel modelTabelDestinasi;
    private DestinasiController controller;
    private KotaController kotaController; // Untuk mengisi JComboBox kota

    // Komponen Form
    private JTextField txtNamaDestinasi, txtHarga;
    private JTextArea txtDeskripsi;
    private JComboBox<Kota> cmbKota;
    private JButton btnPilihGambar, btnSimpan, btnHapus, btnBaru, btnBatal;
    private JLabel lblPreviewGambar, lblNamaFileGambar;
    private File fileGambarDipilih;
    private Destinasi destinasiToEdit = null;

    // Direktori gambar destinasi
    private final String DIR_GAMBAR_DESTINASI = "images/destinasi";

    public KelolaDestinasiView() {
        this.controller = new DestinasiController();
        this.kotaController = new KotaController(); // Inisialisasi KotaController
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        // Judul Panel
        JLabel judul = new JLabel("Manajemen Data Destinasi Wisata");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        add(judul, BorderLayout.NORTH);

        // Panel Konten (Form di Kiri, Tabel di Kanan)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOpaque(false);
        splitPane.setDividerLocation(450); // Lebar panel form

        // Panel Form (Kiri)
        JPanel panelForm = createFormPanel();
        splitPane.setLeftComponent(panelForm);

        // Panel Tabel (Kanan)
        JPanel panelTabel = createTablePanel();
        splitPane.setRightComponent(panelTabel);
        
        add(splitPane, BorderLayout.CENTER);

        muatDataKotaKeComboBox();
        muatDataDestinasiKeTabel();
        modeTambah(); // Set form ke mode tambah awal
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Form Data Destinasi",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 14), Color.WHITE
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtNamaDestinasi = new JTextField(20);
        cmbKota = new JComboBox<>();
        txtDeskripsi = new JTextArea(4, 20);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtHarga = new JTextField(10);
        
        lblPreviewGambar = new JLabel("Preview");
        lblPreviewGambar.setPreferredSize(new Dimension(150, 100));
        lblPreviewGambar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewGambar.setHorizontalAlignment(SwingConstants.CENTER);
        lblNamaFileGambar = new JLabel("Belum ada gambar.");
        btnPilihGambar = new JButton("Pilih Gambar");

        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Nama Destinasi:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; gbc.weightx = 1.0; panel.add(txtNamaDestinasi, gbc); gbc.weightx = 0;
        
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Kota:"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(cmbKota, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.NORTHEAST; panel.add(new JLabel("Deskripsi:"), gbc); gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = y++; panel.add(new JScrollPane(txtDeskripsi), gbc);
        
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel("Harga Tiket (Rp):"), gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(txtHarga, gbc);
        
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.NORTHEAST; panel.add(new JLabel("Gambar:"), gbc); gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = y++; panel.add(btnPilihGambar, gbc);
        
        gbc.gridx = 1; gbc.gridy = y++; panel.add(lblPreviewGambar, gbc);
        gbc.gridx = 1; gbc.gridy = y++; panel.add(lblNamaFileGambar, gbc);
        
        // Tombol Aksi Form
        JPanel panelTombolForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelTombolForm.setOpaque(false);
        btnBaru = new JButton("Baru");
        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
        panelTombolForm.add(btnBaru);
        panelTombolForm.add(btnSimpan);
        panelTombolForm.add(btnBatal);
        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2; panel.add(panelTombolForm, gbc);

        // Spacer untuk mendorong form ke atas
        gbc.gridy = y; gbc.weighty = 1.0; panel.add(new JLabel(), gbc);


        // Action Listeners Form
        btnPilihGambar.addActionListener(e -> pilihGambarDialog());
        btnBaru.addActionListener(e -> modeTambah());
        btnSimpan.addActionListener(e -> simpanDataDestinasi());
        btnBatal.addActionListener(e -> modeTambah()); // Batal kembali ke mode tambah

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0,10));
        panel.setOpaque(false);

        modelTabelDestinasi = new DefaultTableModel(
            new Object[]{"ID", "Gambar", "Nama Destinasi", "Kota", "Harga"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return ImageIcon.class; // Kolom Gambar
                return Object.class;
            }
        };
        tabelDestinasi = new JTable(modelTabelDestinasi);
        tabelDestinasi.setRowHeight(60);
        TableColumn imgCol = tabelDestinasi.getColumn("Gambar");
        imgCol.setCellRenderer(new ImageRenderer()); // Gunakan ImageRenderer yang sudah dibuat
        imgCol.setPreferredWidth(100);
        
        // Sembunyikan kolom ID jika perlu
        tabelDestinasi.getColumnModel().getColumn(0).setMinWidth(0);
        tabelDestinasi.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelDestinasi.getColumnModel().getColumn(0).setWidth(0);
        
        tabelDestinasi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelDestinasi.getSelectedRow() != -1) {
                isiFormDariTabel();
            }
        });
        
        panel.add(new JScrollPane(tabelDestinasi), BorderLayout.CENTER);

        btnHapus = new JButton("Hapus Destinasi Terpilih");
        JPanel panelTombolTabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombolTabel.setOpaque(false);
        panelTombolTabel.add(btnHapus);
        panel.add(panelTombolTabel, BorderLayout.SOUTH);

        btnHapus.addActionListener(e -> hapusDataDestinasi());
        
        return panel;
    }

    private void muatDataKotaKeComboBox() {
        cmbKota.removeAllItems();
        List<Kota> daftarKota = kotaController.getAllKota(); // Ambil dari KotaController
        for (Kota kota : daftarKota) {
            cmbKota.addItem(kota);
        }
    }

    private void muatDataDestinasiKeTabel() {
        modelTabelDestinasi.setRowCount(0);
        List<Destinasi> daftarDestinasi = controller.getAllDestinasi();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);

        for (Destinasi dest : daftarDestinasi) {
            ImageIcon gambar = null;
            if (dest.getGambar() != null && !dest.getGambar().isEmpty()) {
                String imagePath = DIR_GAMBAR_DESTINASI + "/" + dest.getGambar();
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(imagePath);
                    Image resizedImage = originalIcon.getImage().getScaledInstance(80, 50, Image.SCALE_SMOOTH);
                    gambar = new ImageIcon(resizedImage);
                } else {
                     System.err.println("File gambar destinasi tidak ditemukan: " + imagePath);
                }
            }
            modelTabelDestinasi.addRow(new Object[]{
                dest.getId(),
                gambar,
                dest.getNamaDestinasi(),
                dest.getNamaKota(),
                formatMataUang.format(dest.getHarga())
            });
        }
    }

    private void modeTambah() {
        destinasiToEdit = null;
        txtNamaDestinasi.setText("");
        if (cmbKota.getItemCount() > 0) cmbKota.setSelectedIndex(0);
        txtDeskripsi.setText("");
        txtHarga.setText("");
        lblPreviewGambar.setIcon(null);
        lblPreviewGambar.setText("Preview");
        lblNamaFileGambar.setText("Belum ada gambar.");
        fileGambarDipilih = null;
        tabelDestinasi.clearSelection();
        btnSimpan.setText("Simpan Baru");
        txtNamaDestinasi.requestFocus();
    }

    private void isiFormDariTabel() {
        int barisTerpilih = tabelDestinasi.getSelectedRow();
        if (barisTerpilih != -1) {
            int id = (int) modelTabelDestinasi.getValueAt(barisTerpilih, 0);
            destinasiToEdit = controller.getDestinasiById(id); // Ambil data lengkap dari controller
            if (destinasiToEdit != null) {
                txtNamaDestinasi.setText(destinasiToEdit.getNamaDestinasi());
                for (int i = 0; i < cmbKota.getItemCount(); i++) {
                    if (cmbKota.getItemAt(i).getId() == destinasiToEdit.getKotaId()) {
                        cmbKota.setSelectedIndex(i);
                        break;
                    }
                }
                txtDeskripsi.setText(destinasiToEdit.getDeskripsi());
                txtHarga.setText(destinasiToEdit.getHarga().toPlainString());
                
                lblPreviewGambar.setText(""); // Hapus teks "Preview"
                if (destinasiToEdit.getGambar() != null && !destinasiToEdit.getGambar().isEmpty()) {
                    String imagePath = DIR_GAMBAR_DESTINASI + "/" + destinasiToEdit.getGambar();
                     File imgFile = new File(imagePath);
                    if(imgFile.exists()){
                        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH));
                        lblPreviewGambar.setIcon(icon);
                        lblNamaFileGambar.setText(destinasiToEdit.getGambar());
                    } else {
                        lblPreviewGambar.setIcon(null);
                        lblPreviewGambar.setText("Gbr hilang");
                        lblNamaFileGambar.setText(destinasiToEdit.getGambar() + " (hilang)");
                    }
                } else {
                    lblPreviewGambar.setIcon(null);
                    lblPreviewGambar.setText("Preview");
                    lblNamaFileGambar.setText("Belum ada gambar.");
                }
                fileGambarDipilih = null; // Reset file yang baru dipilih
                btnSimpan.setText("Simpan Perubahan");
            }
        }
    }

    private void pilihGambarDialog() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Gambar (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileGambarDipilih = chooser.getSelectedFile();
            lblNamaFileGambar.setText(fileGambarDipilih.getName());
            try {
                ImageIcon icon = new ImageIcon(new ImageIcon(fileGambarDipilih.getAbsolutePath()).getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH));
                lblPreviewGambar.setText("");
                lblPreviewGambar.setIcon(icon);
            } catch (Exception e) {
                lblPreviewGambar.setText("Gagal Preview");
                lblPreviewGambar.setIcon(null);
                e.printStackTrace();
            }
        }
    }

    private String simpanFileGambarKeDirektori(File fileSumber) {
        if (fileSumber == null) return null;
        Path direktoriTujuan = Paths.get(DIR_GAMBAR_DESTINASI);
        if (!Files.exists(direktoriTujuan)) {
            try { Files.createDirectories(direktoriTujuan); } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal membuat direktori gambar destinasi.", "Kesalahan File", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        String namaFileAsli = fileSumber.getName().replace(" ", "_");
        String namaFileUnik = System.currentTimeMillis() + "_" + namaFileAsli;
        Path pathFileTujuan = direktoriTujuan.resolve(namaFileUnik);
        try {
            Files.copy(fileSumber.toPath(), pathFileTujuan, StandardCopyOption.REPLACE_EXISTING);
            return namaFileUnik;
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyalin file gambar.", "Kesalahan File", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    private void simpanDataDestinasi() {
        String nama = txtNamaDestinasi.getText().trim();
        Kota kotaTerpilih = (Kota) cmbKota.getSelectedItem();
        String deskripsi = txtDeskripsi.getText().trim();
        String hargaStr = txtHarga.getText().trim();

        if (nama.isEmpty() || kotaTerpilih == null || deskripsi.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi (kecuali gambar).", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        BigDecimal harga;
        try {
            harga = new BigDecimal(hargaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Format harga tidak valid.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Destinasi dest = new Destinasi();
        dest.setNamaDestinasi(nama);
        dest.setKotaId(kotaTerpilih.getId());
        dest.setDeskripsi(deskripsi);
        dest.setHarga(harga);

        String namaFileGambarDisimpan = null;
        if (fileGambarDipilih != null) { // Jika ada file baru dipilih
            namaFileGambarDisimpan = simpanFileGambarKeDirektori(fileGambarDipilih);
            if (namaFileGambarDisimpan == null) return; // Gagal simpan file, batalkan
            dest.setGambar(namaFileGambarDisimpan);
        } else if (destinasiToEdit != null) { // Jika mode edit dan tidak ada file baru, gunakan gambar lama
            dest.setGambar(destinasiToEdit.getGambar());
        }

        boolean sukses;
        if (destinasiToEdit == null) { // Mode Tambah
            sukses = controller.addDestinasi(dest);
        } else { // Mode Ubah
            dest.setId(destinasiToEdit.getId());
            // Jika gambar diubah dan gambar lama ada, hapus gambar lama
            if (fileGambarDipilih != null && destinasiToEdit.getGambar() != null && !destinasiToEdit.getGambar().isEmpty()) {
                File gambarLama = new File(DIR_GAMBAR_DESTINASI + "/" + destinasiToEdit.getGambar());
                if (gambarLama.exists()) gambarLama.delete();
            }
            sukses = controller.updateDestinasi(dest);
        }

        if (sukses) {
            muatDataDestinasiKeTabel();
            modeTambah(); // Reset form
            JOptionPane.showMessageDialog(this, "Data destinasi berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void hapusDataDestinasi() {
        int barisTerpilih = tabelDestinasi.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih destinasi yang ingin dihapus dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idDestinasi = (int) modelTabelDestinasi.getValueAt(barisTerpilih, 0);
        String namaDestinasi = (String) modelTabelDestinasi.getValueAt(barisTerpilih, 2);

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus destinasi '" + namaDestinasi + "'?\nIni juga akan menghapus file gambarnya.",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.deleteDestinasi(idDestinasi);
            if (sukses) {
                muatDataDestinasiKeTabel();
                modeTambah();
                JOptionPane.showMessageDialog(this, "Destinasi berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
