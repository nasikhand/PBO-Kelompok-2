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
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

public class KelolaPembayaranView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private PembayaranController controller;
    private JTextField txtPencarian;
    private TableRowSorter<DefaultTableModel> sorter;
    private JComboBox<String> cmbFilterStatus;

    public KelolaPembayaranView() {
        this.controller = new PembayaranController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        // Panel Atas: Judul, Filter, dan Kolom Pencarian
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

        // Tabel Pembayaran
        JScrollPane scrollPane = new JScrollPane();
        tabel = new JTable();
        modelTabel = new DefaultTableModel(
            new Object[][]{},
            new String[]{"ID Bayar", "Kode Reservasi", "Metode", "Jumlah", "Tgl Bayar", "Status Bayar"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabel.setModel(modelTabel);
        sorter = new TableRowSorter<>(modelTabel);
        tabel.setRowSorter(sorter);

        // Sembunyikan kolom ID Pembayaran
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0);
        tabel.getColumnModel().getColumn(0).setWidth(0);

        scrollPane.setViewportView(tabel);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Tombol Aksi
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTombol.setOpaque(false);
        JButton btnSetLunas = new JButton("Verifikasi & Set Lunas");
        JButton btnSetGagal = new JButton("Tandai Gagal");
        panelTombol.add(btnSetLunas);
        panelTombol.add(btnSetGagal);
        add(panelTombol, BorderLayout.SOUTH);

        muatData();

        // Action Listeners
        btnSetLunas.addActionListener(e -> ubahStatusPembayaran("lunas"));
        btnSetGagal.addActionListener(e -> ubahStatusPembayaran("gagal"));
        
        cmbFilterStatus.addActionListener(e -> cariData());
        txtPencarian.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { cariData(); }
            public void removeUpdate(DocumentEvent e) { cariData(); }
            public void changedUpdate(DocumentEvent e) { cariData(); }
        });
    }

    private void muatData() {
        modelTabel.setRowCount(0);
        List<Pembayaran> daftarPembayaran = controller.getAllPembayaran();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);

        for (Pembayaran bayar : daftarPembayaran) {
            modelTabel.addRow(new Object[]{
                bayar.getId(),
                bayar.getKodeReservasi(),
                bayar.getMetodePembayaran(),
                formatMataUang.format(bayar.getJumlahPembayaran()),
                bayar.getTanggalPembayaran(),
                bayar.getStatusPembayaran()
            });
        }
    }

    private void cariData() {
        String teksPencarian = txtPencarian.getText();
        String statusFilter = (String) cmbFilterStatus.getSelectedItem();
        
        RowFilter<DefaultTableModel, Object> rf = null;
        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        if (teksPencarian.trim().length() > 0) {
            filters.add(RowFilter.regexFilter("(?i)" + teksPencarian, 1)); // Kolom Kode Reservasi
        }
        if (!"Semua".equalsIgnoreCase(statusFilter)) {
            filters.add(RowFilter.regexFilter("(?i)" + statusFilter, 5)); // Kolom Status Bayar
        }

        if (!filters.isEmpty()) {
            rf = RowFilter.andFilter(filters);
        }
        sorter.setRowFilter(rf);
    }

    private void ubahStatusPembayaran(String statusTarget) {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data pembayaran yang ingin diubah statusnya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idModel = tabel.convertRowIndexToModel(barisTerpilih); // Konversi index view ke model jika ada sorting
        int idPembayaran = (int) modelTabel.getValueAt(idModel, 0);
        String kodeReservasi = (String) modelTabel.getValueAt(idModel, 1);
        String statusSaatIni = (String) modelTabel.getValueAt(idModel, 5);

        if (statusSaatIni.equalsIgnoreCase(statusTarget)) {
             JOptionPane.showMessageDialog(this, "Pembayaran sudah dalam status '" + statusTarget + "'.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
             return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(this,
            "Ubah status pembayaran untuk reservasi " + kodeReservasi + " menjadi '" + statusTarget + "'?",
            "Konfirmasi Perubahan Status", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean sukses = controller.updateStatusPembayaran(idPembayaran, statusTarget);
            if (sukses) {
                JOptionPane.showMessageDialog(this, "Status pembayaran berhasil diperbarui.");
                muatData(); // Muat ulang data untuk refresh tabel
                cariData(); // Terapkan filter kembali
            } else {
                // Pesan error sudah ditampilkan oleh controller
            }
        }
    }
}