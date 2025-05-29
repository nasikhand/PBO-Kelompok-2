/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.KotaController;
import model.Kota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KelolaKotaView extends JPanel {
    private JTable tabelKota;
    private DefaultTableModel modelTabelKota;
    private KotaController controller;

    private JTextField txtNamaKota, txtProvinsi;
    private JButton btnTambah, btnSimpan, btnHapus, btnBatal;
    private Kota kotaToEdit = null; // Untuk mode edit

    public KelolaKotaView() {
        this.controller = new KotaController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        // Judul Panel
        JLabel judul = new JLabel("Manajemen Data Kota");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE); // Sesuaikan dengan tema
        add(judul, BorderLayout.NORTH);

        // Panel Form Input
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Form Data Kota",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14), Color.WHITE
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNamaKota = new JTextField(20);
        txtProvinsi = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Nama Kota:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(txtNamaKota, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Provinsi:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtProvinsi, gbc);

        // Tombol Form
        JPanel panelTombolForm = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolForm.setOpaque(false);
        btnTambah = new JButton("Tambah Baru");
        btnSimpan = new JButton("Simpan");
        btnBatal = new JButton("Batal");
        panelTombolForm.add(btnTambah);
        panelTombolForm.add(btnSimpan);
        panelTombolForm.add(btnBatal);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panelForm.add(panelTombolForm, gbc);
        
        add(panelForm, BorderLayout.CENTER);


        // Tabel Kota
        modelTabelKota = new DefaultTableModel(new Object[]{"ID", "Nama Kota", "Provinsi"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelKota = new JTable(modelTabelKota);
        // Sembunyikan kolom ID
        tabelKota.getColumnModel().getColumn(0).setMinWidth(0);
        tabelKota.getColumnModel().getColumn(0).setMaxWidth(0);
        tabelKota.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPaneTabel = new JScrollPane(tabelKota);
        scrollPaneTabel.setPreferredSize(new Dimension(getWidth(), 250)); // Atur tinggi tabel
        add(scrollPaneTabel, BorderLayout.SOUTH);
        
        // Tombol Hapus di bawah tabel (opsional, bisa juga terintegrasi dengan pemilihan tabel)
        JPanel panelAksiTabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAksiTabel.setOpaque(false);
        btnHapus = new JButton("Hapus Kota Terpilih");
        panelAksiTabel.add(btnHapus);
        // Tambahkan panel aksi tabel di bawah scroll pane jika diperlukan, atau di atas
        // Untuk konsistensi, bisa kita letakkan bersama tombol form atau sebagai konteks menu di tabel

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panelForm.add(panelAksiTabel, gbc);


        // Action Listeners
        btnTambah.addActionListener(e -> modeTambah());
        btnSimpan.addActionListener(e -> simpanDataKota());
        btnBatal.addActionListener(e -> bersihkanForm());
        btnHapus.addActionListener(e -> hapusDataKota());

        tabelKota.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelKota.getSelectedRow() != -1) {
                isiFormDariTabel();
            }
        });

        muatDataKota();
        bersihkanForm(); // Set form ke kondisi awal
    }

    private void muatDataKota() {
        modelTabelKota.setRowCount(0); // Kosongkan tabel
        List<Kota> daftarKota = controller.getAllKota();
        for (Kota kota : daftarKota) {
            modelTabelKota.addRow(new Object[]{kota.getId(), kota.getNamaKota(), kota.getProvinsi()});
        }
    }

    private void modeTambah() {
        kotaToEdit = null;
        txtNamaKota.setText("");
        txtProvinsi.setText("");
        txtNamaKota.requestFocus();
        btnTambah.setEnabled(false);
        btnSimpan.setText("Simpan Baru");
    }

    private void bersihkanForm() {
        kotaToEdit = null;
        txtNamaKota.setText("");
        txtProvinsi.setText("");
        tabelKota.clearSelection();
        btnTambah.setEnabled(true);
        btnSimpan.setText("Simpan Perubahan");
    }

    private void isiFormDariTabel() {
        int barisTerpilih = tabelKota.getSelectedRow();
        if (barisTerpilih != -1) {
            int id = (int) modelTabelKota.getValueAt(barisTerpilih, 0);
            String nama = (String) modelTabelKota.getValueAt(barisTerpilih, 1);
            String provinsi = (String) modelTabelKota.getValueAt(barisTerpilih, 2);

            kotaToEdit = new Kota();
            kotaToEdit.setId(id);
            kotaToEdit.setNamaKota(nama);
            kotaToEdit.setProvinsi(provinsi);

            txtNamaKota.setText(nama);
            txtProvinsi.setText(provinsi);
            btnTambah.setEnabled(true);
            btnSimpan.setText("Simpan Perubahan");
        }
    }

    private void simpanDataKota() {
        String nama = txtNamaKota.getText().trim();
        String provinsi = txtProvinsi.getText().trim();

        if (nama.isEmpty() || provinsi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kota dan provinsi tidak boleh kosong.", "Input Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Kota kota = new Kota();
        kota.setNamaKota(nama);
        kota.setProvinsi(provinsi);

        boolean sukses;
        if (kotaToEdit == null) { // Mode Tambah
            sukses = controller.addKota(kota);
        } else { // Mode Ubah
            kota.setId(kotaToEdit.getId());
            sukses = controller.updateKota(kota);
        }

        if (sukses) {
            muatDataKota();
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Data kota berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Pesan error sudah ditampilkan oleh controller
        }
    }

    private void hapusDataKota() {
        int barisTerpilih = tabelKota.getSelectedRow();
        if (barisTerpilih == -1 && kotaToEdit == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih kota yang ingin dihapus dari tabel.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idKota;
        String namaKota;

        if (kotaToEdit != null && tabelKota.getSelectedRow() == -1) { // Hapus dari form setelah dipilih
            idKota = kotaToEdit.getId();
            namaKota = kotaToEdit.getNamaKota();
        } else if (barisTerpilih != -1) { // Hapus dari seleksi tabel langsung
            idKota = (int) modelTabelKota.getValueAt(barisTerpilih, 0);
            namaKota = (String) modelTabelKota.getValueAt(barisTerpilih, 1);
        } else {
            return; // Seharusnya tidak terjadi
        }


        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus kota '" + namaKota + "'?\nMenghapus kota dapat mempengaruhi data destinasi terkait.",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.deleteKota(idKota);
            if (sukses) {
                muatDataKota();
                bersihkanForm();
                JOptionPane.showMessageDialog(this, "Kota berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
