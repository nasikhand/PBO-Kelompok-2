/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent; 
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class KelolaPenggunaView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private UserController controller;
    private JTextField txtPencarian; // <-- Kolom pencarian
    private TableRowSorter<DefaultTableModel> sorter;

    public KelolaPenggunaView() {
        this.controller = new UserController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false); // Sesuaikan dengan tema dasbor
        
        // Panel Atas: Judul dan Kolom Pencarian
        JPanel panelAtas = new JPanel(new BorderLayout(10,5));
        panelAtas.setOpaque(false);

        JLabel judul = new JLabel("Manajemen Data Pengguna");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE); // Sesuaikan warna font
        add(judul, BorderLayout.NORTH);
        
        txtPencarian = new JTextField();
        txtPencarian.putClientProperty("JTextField.placeholderText", "Cari berdasarkan Nama, Email, atau No. Telepon...");
        panelAtas.add(txtPencarian, BorderLayout.SOUTH);
        
        add(panelAtas, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nama Lengkap", "Email", "No. Telepon", "Alamat", "Tanggal Daftar"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Sel tabel tidak bisa diedit langsung
            }
        };
        tabel.setModel(modelTabel);
        
        // Inisialisasi TableRowSorter
        sorter = new TableRowSorter<>(modelTabel);
        tabel.setRowSorter(sorter);
        // Sembunyikan kolom ID jika tidak ingin ditampilkan
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        tabel.getColumnModel().getColumn(0).setWidth(0);

        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.setOpaque(false);
        JButton btnUbah = new JButton("Ubah Data Pengguna");
        JButton btnHapus = new JButton("Hapus Pengguna");
        
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        add(panelTombol, BorderLayout.SOUTH);

        muatData();

        // Action Listeners
        btnUbah.addActionListener(e -> ubahDataTerpilih());
        btnHapus.addActionListener(e -> hapusDataTerpilih());
        
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
        List<User> daftarUser = controller.getAllUsers();

        for (User user : daftarUser) {
            modelTabel.addRow(new Object[]{
                user.getId(),
                user.getNamaLengkap(),
                user.getEmail(),
                user.getNoTelepon(),
                user.getAlamat(),
                user.getCreatedAt() // Menampilkan tanggal pendaftaran
            });
        }
    }

    private void ubahDataTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih pengguna yang ingin diubah datanya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idUser = (int) modelTabel.getValueAt(barisTerpilih, 0);
        User user = controller.getUserById(idUser);

        if (user != null) {
            // Kita akan buat dialog form terpisah untuk ubah data pengguna
            FormPenggunaDialog dialog = new FormPenggunaDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), user
            );
            dialog.setVisible(true);
            muatData(); // Muat ulang data setelah dialog ditutup
        } else {
            JOptionPane.showMessageDialog(this, "Data pengguna tidak ditemukan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusDataTerpilih() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih pengguna yang ingin dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idUser = (int) modelTabel.getValueAt(barisTerpilih, 0);
        String namaUser = (String) modelTabel.getValueAt(barisTerpilih, 1);

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus pengguna '" + namaUser + "'?\nMenghapus pengguna juga dapat menghapus data terkait lainnya.",
            "Konfirmasi Hapus Pengguna", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.deleteUser(idUser);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Pengguna berhasil dihapus.");
                muatData(); // Muat ulang data
            } else {
                // Pesan error sudah ditampilkan oleh controller
            }
        }
    }
    
    private void cariData() {
        String teks = txtPencarian.getText();
        if (teks.trim().length() == 0) {
            sorter.setRowFilter(null);
        } else {
            // Filter berdasarkan Nama (1), Email (2), No. Telepon (3)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + teks, 1, 2, 3));
        }
    }
    
}