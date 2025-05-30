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
    
    // Komponen Paginasi
    private JButton btnSebelumnya, btnBerikutnya;
    private JLabel lblInfoHalaman;
    private int halamanSaatIni = 1;
    private final int DATA_PER_HALAMAN = 15;
    private int totalHalaman = 1;
    private int totalData = 0;


    public KelolaPenggunaView() {
        this.controller = new UserController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JPanel panelAtas = new JPanel(new BorderLayout(10, 5));
        panelAtas.setOpaque(false);
        JLabel judul = new JLabel("Manajemen Data Pengguna");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        txtPencarian = new JTextField();
        txtPencarian.putClientProperty("JTextField.placeholderText", "Cari berdasarkan Nama atau Email...");
        panelAtas.add(judul, BorderLayout.NORTH);
        panelAtas.add(txtPencarian, BorderLayout.CENTER);
        add(panelAtas, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID", "Nama Lengkap", "Email", "No. Telepon", "Alamat", "Tanggal Daftar"}
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabel.setModel(modelTabel);
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        tabel.getColumnModel().getColumn(0).setWidth(0);
        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelTombolAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombolAksi.setOpaque(false);
        JButton btnUbah = new JButton("Ubah Data Pengguna");
        JButton btnHapus = new JButton("Hapus Pengguna");
        panelTombolAksi.add(btnUbah);
        panelTombolAksi.add(btnHapus);

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

        btnUbah.addActionListener(e -> ubahDataTerpilih());
        btnHapus.addActionListener(e -> hapusDataTerpilih());
        txtPencarian.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { halamanSaatIni = 1; muatDataDenganPaginasi(); }
            public void removeUpdate(DocumentEvent e) { halamanSaatIni = 1; muatDataDenganPaginasi(); }
            public void changedUpdate(DocumentEvent e) { halamanSaatIni = 1; muatDataDenganPaginasi(); }
        });
        btnSebelumnya.addActionListener(e -> { if (halamanSaatIni > 1) { halamanSaatIni--; muatDataDenganPaginasi(); } });
        btnBerikutnya.addActionListener(e -> { if (halamanSaatIni < totalHalaman) { halamanSaatIni++; muatDataDenganPaginasi(); } });
    }

    private void muatDataDenganPaginasi() {
        String filterText = txtPencarian.getText().trim();
        totalData = controller.getTotalUserCount(filterText);
        totalHalaman = (int) Math.ceil((double) totalData / DATA_PER_HALAMAN);
        if (totalHalaman == 0) totalHalaman = 1;
        if (halamanSaatIni > totalHalaman) halamanSaatIni = totalHalaman;
        if (halamanSaatIni < 1) halamanSaatIni = 1;

        List<User> daftarUser = controller.getUsersWithPagination(halamanSaatIni, DATA_PER_HALAMAN, filterText);
        modelTabel.setRowCount(0);
        for (User user : daftarUser) {
            modelTabel.addRow(new Object[]{
                user.getId(), user.getNamaLengkap(), user.getEmail(),
                user.getNoTelepon(), user.getAlamat(), user.getCreatedAt()
            });
        }
        lblInfoHalaman.setText("Hal " + halamanSaatIni + " dari " + totalHalaman + " (" + totalData + " data)");
        btnSebelumnya.setEnabled(halamanSaatIni > 1);
        btnBerikutnya.setEnabled(halamanSaatIni < totalHalaman);
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
            FormPenggunaDialog dialog = new FormPenggunaDialog((Frame) SwingUtilities.getWindowAncestor(this), user);
            dialog.setVisible(true);
            muatDataDenganPaginasi();
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
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus pengguna '" + namaUser + "'?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (controller.deleteUser(idUser)) {
                JOptionPane.showMessageDialog(this, "Pengguna berhasil dihapus.");
                if (modelTabel.getRowCount() == 1 && halamanSaatIni > 1) {
                    halamanSaatIni--;
                }
                muatDataDenganPaginasi();
            }
        }
    }
    
}