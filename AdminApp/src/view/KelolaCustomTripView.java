/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.CustomTripController;
import model.CustomTrip;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

//           vvvvvvvvvvvvv INI BAGIAN PERBAIKANNYA vvvvvvvvvvvvv
public class KelolaCustomTripView extends JPanel {
    private JTable tabel;
    private DefaultTableModel modelTabel;
    private CustomTripController controller;
    private List<CustomTrip> daftarTripCache;

    // Komponen Paginasi dan Filter
    private JTextField txtPencarianEmail;
    private JComboBox<String> cmbFilterStatus;
    private JButton btnSebelumnya, btnBerikutnya, btnTerapkanFilter;
    private JLabel lblInfoHalaman;
    private int halamanSaatIni = 1;
    private final int DATA_PER_HALAMAN = 15;
    private int totalHalaman = 1;

    public KelolaCustomTripView() {
        this.controller = new CustomTripController();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setOpaque(false);

        // Panel Atas
        JPanel panelAtas = new JPanel(new BorderLayout(10, 10));
        panelAtas.setOpaque(false);
        JLabel judul = new JLabel("Manajemen Custom Trip Pengguna");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 28));
        judul.setForeground(Color.WHITE);
        panelAtas.add(judul, BorderLayout.NORTH);

        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFilter.setOpaque(false);
        panelFilter.add(new JLabel("Filter Status:"));
        cmbFilterStatus = new JComboBox<>(new String[]{"Semua", "draft", "dipesan", "dibayar", "selesai"});
        panelFilter.add(cmbFilterStatus);
        panelFilter.add(new JLabel("   Cari Email Pemesan:"));
        txtPencarianEmail = new JTextField(20);
        panelFilter.add(txtPencarianEmail);
        btnTerapkanFilter = new JButton("Terapkan");
        panelFilter.add(btnTerapkanFilter);
        panelAtas.add(panelFilter, BorderLayout.CENTER);
        add(panelAtas, BorderLayout.NORTH);

        // Tabel
        modelTabel = new DefaultTableModel(new String[]{"ID", "Nama Trip", "Nama Pemesan", "Tgl Mulai", "Status", "Total Harga"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabel = new JTable(modelTabel);
        tabel.getColumnModel().getColumn(0).setMaxWidth(0); // Sembunyikan ID
        tabel.getColumnModel().getColumn(0).setMinWidth(0);
        add(new JScrollPane(tabel), BorderLayout.CENTER);

        // Panel Bawah
        JPanel panelAksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAksi.setOpaque(false);
        JButton btnLihatRincian = new JButton("Lihat Rincian Destinasi");
        panelAksi.add(btnLihatRincian);
        
        JPanel panelPaginasi = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelPaginasi.setOpaque(false);
        btnSebelumnya = new JButton("« Seb");
        lblInfoHalaman = new JLabel("Hal 1 dari 1");
        lblInfoHalaman.setForeground(Color.WHITE);
        btnBerikutnya = new JButton("Ber »");
        panelPaginasi.add(btnSebelumnya); panelPaginasi.add(lblInfoHalaman); panelPaginasi.add(btnBerikutnya);

        JPanel panelBawah = new JPanel(new BorderLayout());
        panelBawah.setOpaque(false);
        panelBawah.add(panelAksi, BorderLayout.WEST);
        panelBawah.add(panelPaginasi, BorderLayout.EAST);
        add(panelBawah, BorderLayout.SOUTH);
        
        // Listeners
        btnTerapkanFilter.addActionListener(e -> { halamanSaatIni = 1; muatData(); });
        btnSebelumnya.addActionListener(e -> { if (halamanSaatIni > 1) { halamanSaatIni--; muatData(); }});
        btnBerikutnya.addActionListener(e -> { if (halamanSaatIni < totalHalaman) { halamanSaatIni++; muatData(); }});
        btnLihatRincian.addActionListener(e -> lihatRincian());
        
        muatData();
    }

    private void muatData() {
        String filterEmail = txtPencarianEmail.getText().trim();
        String filterStatus = cmbFilterStatus.getSelectedItem().toString();
        if ("Semua".equalsIgnoreCase(filterStatus)) filterStatus = null;

        int totalData = controller.getTotalCustomTripCount(filterEmail, filterStatus);
        totalHalaman = (int) Math.ceil((double) totalData / DATA_PER_HALAMAN);
        if (totalHalaman == 0) totalHalaman = 1;
        if (halamanSaatIni > totalHalaman) halamanSaatIni = totalHalaman;

        daftarTripCache = controller.getCustomTripsWithPagination(halamanSaatIni, DATA_PER_HALAMAN, filterEmail, filterStatus);
        modelTabel.setRowCount(0);
        
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);
        
        for (CustomTrip trip : daftarTripCache) {
            modelTabel.addRow(new Object[]{
                trip.getId(),
                trip.getNamaTrip(),
                trip.getNamaPemesan(),
                trip.getTanggalMulai(),
                trip.getStatus(),
                trip.getTotalHarga() != null ? formatMataUang.format(trip.getTotalHarga()) : "N/A"
            });
        }
        lblInfoHalaman.setText("Hal " + halamanSaatIni + " dari " + totalHalaman + " ("+totalData+" data)");
        btnSebelumnya.setEnabled(halamanSaatIni > 1);
        btnBerikutnya.setEnabled(halamanSaatIni < totalHalaman);
    }
    
    private void lihatRincian() {
        int barisTerpilih = tabel.getSelectedRow();
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih custom trip untuk melihat rinciannya.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        CustomTrip tripTerpilih = daftarTripCache.get(barisTerpilih);
        DetailCustomTripDialog dialog = new DetailCustomTripDialog((Frame) SwingUtilities.getWindowAncestor(this), tripTerpilih, controller);
        dialog.setVisible(true);
    }
}