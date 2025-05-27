/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.pengguna;

import controller.PenggunaController; // Pastikan kelas ini ada dan berfungsi
import model.Pengguna; // Pastikan kelas ini ada

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder; // Ditambahkan untuk styling
import java.awt.*;
import java.util.List;
import view.DashboardAdmin; // Pastikan ini diimpor

public class KelolaPenggunaPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private DashboardAdmin parentFrame; // Tambahkan referensi ke DashboardAdmin

    // Konstruktor disesuaikan untuk menerima referensi DashboardAdmin
    public KelolaPenggunaPanel(DashboardAdmin parentFrame) {
        this.parentFrame = parentFrame; // Simpan referensi
        setLayout(new BorderLayout());
        setBackground(new Color(248, 248, 248)); // Warna latar belakang yang seragam dengan dashboard
        setBorder(new EmptyBorder(25, 25, 25, 25)); // Padding yang konsisten

        // Panel Judul dan Tombol Aksi
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Kelola Pengguna");
        title.setFont(new Font("SansSerif", Font.BOLD, 26)); // Ukuran font disesuaikan
        title.setForeground(new Color(40, 40, 40));
        topPanel.add(title, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        // Tombol Tambah
        JButton tambahBtn = new JButton("Tambah Pengguna");
        tambahBtn.setBackground(new Color(129, 199, 132)); // Warna hijau konsisten
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        tambahBtn.setFocusPainted(false);
        tambahBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 170, 100), 1),
                new EmptyBorder(8, 15, 8, 15)
        ));
        tambahBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tambahBtn.addActionListener(e -> {
            // Asumsi FormPenggunaDialog memiliki konstruktor yang menerima JFrame induk dan Runnable untuk refresh
            new FormPenggunaDialog((JFrame) SwingUtilities.getWindowAncestor(this), this::loadData);
        });
        buttonPanel.add(tambahBtn);

        // Tombol Edit
        JButton editBtn = new JButton("Edit");
        editBtn.setBackground(new Color(100, 181, 246)); // Warna biru konsisten
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        editBtn.setFocusPainted(false);
        editBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 160, 220), 1),
                new EmptyBorder(8, 15, 8, 15)
        ));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0); // Asumsi ID adalah kolom pertama dan int
                // Asumsi FormPenggunaDialog memiliki konstruktor untuk edit
                new FormPenggunaDialog((JFrame) SwingUtilities.getWindowAncestor(this), this::loadData, id);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit.");
            }
        });
        buttonPanel.add(editBtn);

        // Tombol Hapus
        JButton hapusBtn = new JButton("Hapus");
        hapusBtn.setBackground(new Color(229, 115, 115)); // Warna merah konsisten
        hapusBtn.setForeground(Color.WHITE);
        hapusBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        hapusBtn.setFocusPainted(false);
        hapusBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 90, 90), 1),
                new EmptyBorder(8, 15, 8, 15)
        ));
        hapusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        hapusBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) table.getValueAt(row, 0); // Asumsi ID adalah kolom pertama dan int
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus pengguna ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    PenggunaController.delete(id); // Pastikan metode delete ada di PenggunaController
                    loadData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            }
        });
        buttonPanel.add(hapusBtn);

        // Tombol Kembali ke Dashboard (INI YANG DIUBAH)
        JButton kembaliBtn = new JButton("Kembali"); // Nama lebih ringkas
        kembaliBtn.setBackground(new Color(58, 130, 247)); // Warna biru konsisten
        kembaliBtn.setForeground(Color.WHITE);
        kembaliBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        kembaliBtn.setFocusPainted(false);
        kembaliBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 110, 220), 1),
                new EmptyBorder(8, 15, 8, 15)
        ));
        kembaliBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        kembaliBtn.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.backToDashboard(); // Panggil metode backToDashboard dari DashboardAdmin
            }
        });
        buttonPanel.add(kembaliBtn);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Tabel Data Pengguna
        // Sesuaikan kolom jika model Pengguna Anda memiliki atribut berbeda
        String[] columns = {"ID", "Nama", "Email", "Telepon", "Alamat"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.setRowHeight(25);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Hanya bisa pilih satu baris

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadData(); // Muat data saat panel diinisialisasi
    }

    public void loadData() {
        // Pastikan PenggunaController.getAll() mengembalikan List<Pengguna>
        List<Pengguna> list = PenggunaController.getAll();
        tableModel.setRowCount(0); // Bersihkan data lama
        if (list != null) {
            for (Pengguna u : list) {
                tableModel.addRow(new Object[]{
                    u.getId(),
                    u.getNamaLengkap(),
                    u.getEmail(),
                    u.getTelepon(),
                    u.getAlamat()
                });
            }
        } else {
             System.out.println("Tidak ada data pengguna yang ditemukan atau PenggunaController.getAll() mengembalikan null.");
             // Contoh data dummy jika controller belum terhubung/mengembalikan data
             // tableModel.addRow(new Object[]{1, "John Doe", "john.doe@example.com", "081234567890", "Jl. Contoh No. 1"});
             // tableModel.addRow(new Object[]{2, "Jane Smith", "jane.smith@example.com", "087654321098", "Jl. Percobaan No. 2"});
        }
    }
}

