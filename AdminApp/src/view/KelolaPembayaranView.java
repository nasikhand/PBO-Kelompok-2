/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.PembayaranController;
import model.Pembayaran;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KelolaPembayaranView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private PembayaranController controller;
    private JTextField txtPencarian;
    private JComboBox<String> cmbFilterStatus;

    // Komponen Paginasi
    private JButton btnSebelumnya, btnBerikutnya;
    private JLabel lblInfoHalaman;
    private int halamanSaatIni = 1;
    private final int DATA_PER_HALAMAN = 15;
    private int totalHalaman = 1;
    private int totalData = 0;

    public KelolaPembayaranView() {
        this.controller = new PembayaranController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));
        panelAtas.setOpaque(false);
        JLabel judul = new JLabel("Manajemen Pembayaran");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        panelAtas.add(judul, BorderLayout.NORTH);

        JPanel panelFilterPencarian = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFilterPencarian.setOpaque(false);
        panelFilterPencarian.add(new JLabel("Filter Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", "pending", "lunas", "gagal"});
        panelFilterPencarian.add(cmbFilterStatus);
        txtPencarian = new JTextField(25);
        txtPencarian.putClientProperty("JTextField.placeholderText", "Cari berdasarkan Kode Reservasi...");
        panelFilterPencarian.add(new JLabel("   Cari:"));
        panelFilterPencarian.add(txtPencarian);
        panelAtas.add(panelFilterPencarian, BorderLayout.CENTER);
        add(panelAtas, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Bayar", "Kode Reservasi", "Metode", "Jumlah", "Tgl Bayar", "Status Bayar"}
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
        JButton btnSetLunas = new JButton("Verifikasi & Set Lunas");
        JButton btnSetGagal = new JButton("Tandai Gagal");
        panelTombolAksi.add(btnSetLunas);
        panelTombolAksi.add(btnSetGagal);

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

        btnSetLunas.addActionListener(e -> ubahStatusPembayaran("lunas"));
        btnSetGagal.addActionListener(e -> ubahStatusPembayaran("gagal"));
        
        DocumentListener searchListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { halamanSaatIni = 1; muatDataDenganPaginasi(); }
            public void removeUpdate(DocumentEvent e) { halamanSaatIni = 1; muatDataDenganPaginasi(); }
            public void changedUpdate(DocumentEvent e) { halamanSaatIni = 1; muatDataDenganPaginasi(); }
        };
        txtPencarian.getDocument().addDocumentListener(searchListener);
        cmbFilterStatus.addActionListener(e -> { halamanSaatIni = 1; muatDataDenganPaginasi(); });
        
        btnSebelumnya.addActionListener(e -> { if (halamanSaatIni > 1) { halamanSaatIni--; muatDataDenganPaginasi(); } });
        btnBerikutnya.addActionListener(e -> { if (halamanSaatIni < totalHalaman) { halamanSaatIni++; muatDataDenganPaginasi(); } });
    }

    private void muatDataDenganPaginasi() {
        String filterKode = txtPencarian.getText().trim();
        String filterStatus = (String) cmbFilterStatus.getSelectedItem();

        totalData = controller.getTotalPembayaranCount(filterKode, filterStatus);
        totalHalaman = (int) Math.ceil((double) totalData / DATA_PER_HALAMAN);
        if (totalHalaman == 0) totalHalaman = 1;
        if (halamanSaatIni > totalHalaman) halamanSaatIni = totalHalaman;
        if (halamanSaatIni < 1) halamanSaatIni = 1;

        List<Pembayaran> daftarPembayaran = controller.getPembayaranWithPagination(halamanSaatIni, DATA_PER_HALAMAN, filterKode, filterStatus);
        modelTabel.setRowCount(0);
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);

        for (Pembayaran bayar : daftarPembayaran) {
            modelTabel.addRow(new Object[]{
                bayar.getId(), bayar.getKodeReservasi(), bayar.getMetodePembayaran(),
                formatMataUang.format(bayar.getJumlahPembayaran()),
                bayar.getTanggalPembayaran(), bayar.getStatusPembayaran()
            });
        }
        lblInfoHalaman.setText("Hal " + halamanSaatIni + " dari " + totalHalaman + " (" + totalData + " data)");
        btnSebelumnya.setEnabled(halamanSaatIni > 1);
        btnBerikutnya.setEnabled(halamanSaatIni < totalHalaman);
    }

    private void ubahStatusPembayaran(String statusTarget) {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data pembayaran.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idModel = barisTerpilih; // Tidak perlu konversi jika tidak ada sorting client-side
        int idPembayaran = (int) modelTabel.getValueAt(idModel, 0);
        String statusSaatIni = (String) modelTabel.getValueAt(idModel, 5);

        if (statusSaatIni.equalsIgnoreCase(statusTarget)) {
             JOptionPane.showMessageDialog(this, "Pembayaran sudah dalam status '" + statusTarget + "'.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
             return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this, "Ubah status pembayaran ini menjadi '" + statusTarget + "'?", "Konfirmasi Perubahan", JOptionPane.YES_NO_OPTION);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (controller.updateStatusPembayaran(idPembayaran, statusTarget)) {
                JOptionPane.showMessageDialog(this, "Status pembayaran berhasil diperbarui.");
                muatDataDenganPaginasi();
            }
        }
    }
}