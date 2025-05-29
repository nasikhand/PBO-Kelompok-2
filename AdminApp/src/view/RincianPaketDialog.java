/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PerjalananController;
import model.Destinasi;
import model.PaketPerjalanan;
import model.RincianPaketPerjalanan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RincianPaketDialog extends JDialog {
    private PaketPerjalanan paketPerjalanan;
    private PerjalananController controller;

    private JTable tabelRincian;
    private DefaultTableModel modelTabelRincian;
    private JComboBox<Destinasi> cmbDestinasi;
    private JTextField txtUrutan, txtDurasi;
    private JButton btnTambahRincian, btnSimpanRincian, btnHapusRincian, btnBatalEditRincian;

    private RincianPaketPerjalanan rincianToEdit = null;

    public RincianPaketDialog(Frame owner, PaketPerjalanan paket) {
        super(owner, "Kelola Rincian Destinasi untuk: " + paket.getNamaPaket(), true);
        this.paketPerjalanan = paket;
        this.controller = new PerjalananController();

        setSize(700, 550);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));


        // Panel Form untuk Tambah/Edit Rincian
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Rincian Destinasi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        cmbDestinasi = new JComboBox<>();
        muatDataDestinasiKeComboBox();
        txtUrutan = new JTextField(5);
        txtDurasi = new JTextField(5);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Pilih Destinasi:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; panelForm.add(cmbDestinasi, gbc); gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Urutan Kunjungan:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtUrutan, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Durasi (jam):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtDurasi, gbc);

        JPanel panelTombolForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTambahRincian = new JButton("Tambah ke Paket");
        btnSimpanRincian = new JButton("Simpan Perubahan");
        btnBatalEditRincian = new JButton("Batal Edit");
        panelTombolForm.add(btnTambahRincian);
        panelTombolForm.add(btnSimpanRincian);
        panelTombolForm.add(btnBatalEditRincian);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panelForm.add(panelTombolForm, gbc);
        
        add(panelForm, BorderLayout.NORTH);

        // Tabel untuk menampilkan rincian yang sudah ada
        modelTabelRincian = new DefaultTableModel(
            new Object[]{"ID Rincian", "Nama Destinasi", "Urutan", "Durasi (Jam)"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelRincian = new JTable(modelTabelRincian);
        // Sembunyikan kolom ID Rincian
        tabelRincian.getColumnModel().getColumn(0).setMinWidth(0);
        tabelRincian.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelRincian.getColumnModel().getColumn(0).setWidth(0);

        add(new JScrollPane(tabelRincian), BorderLayout.CENTER);

        // Tombol Hapus Rincian
        btnHapusRincian = new JButton("Hapus Destinasi dari Paket");
        JPanel panelTombolAksi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTombolAksi.add(btnHapusRincian);
        add(panelTombolAksi, BorderLayout.SOUTH);

        // Action Listeners
        btnTambahRincian.addActionListener(e -> tambahRincian());
        btnSimpanRincian.addActionListener(e -> simpanPerubahanRincian());
        btnBatalEditRincian.addActionListener(e -> modeTambahRincian());
        btnHapusRincian.addActionListener(e -> hapusRincian());

        tabelRincian.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelRincian.getSelectedRow() != -1) {
                isiFormRincianDariTabel();
            }
        });
        
        muatDataRincianPaket();
        modeTambahRincian(); // Set form ke mode tambah awal
    }

    private void muatDataDestinasiKeComboBox() {
        cmbDestinasi.removeAllItems();
        List<Destinasi> daftarDestinasi = controller.getAllDestinasiForComboBox();
        for (Destinasi dest : daftarDestinasi) {
            cmbDestinasi.addItem(dest);
        }
    }

    private void muatDataRincianPaket() {
        modelTabelRincian.setRowCount(0);
        List<RincianPaketPerjalanan> daftarRincian = controller.getRincianByPaketId(paketPerjalanan.getId());
        for (RincianPaketPerjalanan rincian : daftarRincian) {
            modelTabelRincian.addRow(new Object[]{
                rincian.getId(),
                rincian.getNamaDestinasi(),
                rincian.getUrutanKunjungan() != null ? rincian.getUrutanKunjungan() : "-",
                rincian.getDurasiJam() != null ? rincian.getDurasiJam() : "-"
            });
        }
    }

    private void modeTambahRincian() {
        rincianToEdit = null;
        if (cmbDestinasi.getItemCount() > 0) cmbDestinasi.setSelectedIndex(0);
        txtUrutan.setText("");
        txtDurasi.setText("");
        tabelRincian.clearSelection();
        btnTambahRincian.setEnabled(true);
        btnSimpanRincian.setEnabled(false);
        cmbDestinasi.requestFocus();
    }

    private void isiFormRincianDariTabel() {
        int barisTerpilih = tabelRincian.getSelectedRow();
        if (barisTerpilih != -1) {
            int idRincian = (int) modelTabelRincian.getValueAt(barisTerpilih, 0);
            // Ambil data rincian lengkap dari list yang sudah dimuat atau query ulang jika perlu lebih detail
            // Untuk contoh ini, kita cari di list yang sudah ada di controller (jika ada metode getRincianById)
            // atau kita bisa buat objek RincianPaketPerjalanan baru dari data tabel
            List<RincianPaketPerjalanan> daftarRincian = controller.getRincianByPaketId(paketPerjalanan.getId());
            rincianToEdit = daftarRincian.stream().filter(r -> r.getId() == idRincian).findFirst().orElse(null);

            if (rincianToEdit != null) {
                for (int i = 0; i < cmbDestinasi.getItemCount(); i++) {
                    if (cmbDestinasi.getItemAt(i).getId() == rincianToEdit.getDestinasiId()) {
                        cmbDestinasi.setSelectedIndex(i);
                        break;
                    }
                }
                txtUrutan.setText(rincianToEdit.getUrutanKunjungan() != null ? String.valueOf(rincianToEdit.getUrutanKunjungan()) : "");
                txtDurasi.setText(rincianToEdit.getDurasiJam() != null ? String.valueOf(rincianToEdit.getDurasiJam()) : "");
                btnTambahRincian.setEnabled(false);
                btnSimpanRincian.setEnabled(true);
            }
        }
    }

    private void tambahRincian() {
        Destinasi destTerpilih = (Destinasi) cmbDestinasi.getSelectedItem();
        if (destTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih destinasi.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        RincianPaketPerjalanan rincian = new RincianPaketPerjalanan();
        rincian.setPaketPerjalananId(paketPerjalanan.getId());
        rincian.setDestinasiId(destTerpilih.getId());
        try {
            if (!txtUrutan.getText().trim().isEmpty()) rincian.setUrutanKunjungan(Integer.parseInt(txtUrutan.getText().trim()));
            if (!txtDurasi.getText().trim().isEmpty()) rincian.setDurasiJam(Integer.parseInt(txtDurasi.getText().trim()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Urutan dan Durasi harus berupa angka.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (controller.addRincianPaket(rincian)) {
            muatDataRincianPaket();
            modeTambahRincian();
        }
    }

    private void simpanPerubahanRincian() {
        if (rincianToEdit == null) return;
        Destinasi destTerpilih = (Destinasi) cmbDestinasi.getSelectedItem();
        if (destTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih destinasi.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        rincianToEdit.setDestinasiId(destTerpilih.getId());
        try {
            if (!txtUrutan.getText().trim().isEmpty()) rincianToEdit.setUrutanKunjungan(Integer.parseInt(txtUrutan.getText().trim())); else rincianToEdit.setUrutanKunjungan(null);
            if (!txtDurasi.getText().trim().isEmpty()) rincianToEdit.setDurasiJam(Integer.parseInt(txtDurasi.getText().trim())); else rincianToEdit.setDurasiJam(null);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Urutan dan Durasi harus berupa angka.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.updateRincianPaket(rincianToEdit)) {
            muatDataRincianPaket();
            modeTambahRincian();
        }
    }

    private void hapusRincian() {
        int barisTerpilih = tabelRincian.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih rincian destinasi yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idRincian = (int) modelTabelRincian.getValueAt(barisTerpilih, 0);
        String namaDestinasi = (String) modelTabelRincian.getValueAt(barisTerpilih, 1);

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Hapus destinasi '" + namaDestinasi + "' dari paket ini?",
            "Konfirmasi Hapus Rincian", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (controller.deleteRincianPaket(idRincian)) {
                muatDataRincianPaket();
                modeTambahRincian();
            }
        }
    }
}