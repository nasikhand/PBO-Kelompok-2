/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Admin;
import controller.ReservasiController;
import controller.UserController;
import controller.PerjalananController;
import view.KelolaPembayaranView;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminDashboardView extends JFrame {

    // Definisi warna yang lebih modern dan konsisten
    private final Color WARNA_LATAR_UTAMA = new Color(30, 32, 34);
    private final Color WARNA_PANEL_SAMPING = new Color(38, 40, 42);
    private final Color WARNA_AKSEN_BIRU = new Color(20, 125, 250);
    private final Color WARNA_AKSEN_HOVER = new Color(50, 150, 255);
    private final Color WARNA_TEKS_UTAMA = new Color(220, 220, 220);
    private final Color WARNA_TEKS_SEKUNDER = new Color(160, 160, 160);
    private final Color WARNA_KARTU_LATAR = new Color(45, 48, 52);
    // vvv PERBAIKI NAMA KONSTANTA INI (jika ada yang salah sebelumnya) vvv
    private final Color WARNA_GRAFIK_LATAR_PANEL = new Color(45, 48, 52); // Latar panel yang membungkus grafik
    private final Color WARNA_PLOT_GRAFIK = new Color(55, 58, 62);    // Latar area plot grafik

    private JPanel panelKontenUtama;
    private CardLayout cardLayout;
    private Map<String, JPanel> panelCache = new HashMap<>();

    private ReservasiController reservasiController;
    private UserController userController;
    private PerjalananController perjalananController;

    public AdminDashboardView(Admin admin) {
        setTitle("Dasbor Administrator - Sinar Jaya Travel");
        setMinimumSize(new Dimension(1280, 760));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.reservasiController = new ReservasiController();
        this.userController = new UserController();
        this.perjalananController = new PerjalananController();

        JPanel panelLatar = new JPanel(new BorderLayout());
        panelLatar.setBackground(WARNA_LATAR_UTAMA);
        setContentPane(panelLatar);

        JPanel panelNavigasiSamping = buatPanelNavigasiSamping(admin);
        panelLatar.add(panelNavigasiSamping, BorderLayout.WEST);

        cardLayout = new CardLayout();
        panelKontenUtama = new JPanel(cardLayout);
        panelKontenUtama.setBorder(new EmptyBorder(15, 20, 15, 20));
        panelKontenUtama.setOpaque(false);
        panelLatar.add(panelKontenUtama, BorderLayout.CENTER);

        JPanel panelBerandaDasbor = buatPanelBerandaDasbor();
        panelKontenUtama.add(panelBerandaDasbor, "DASBOR");
        panelCache.put("DASBOR", panelBerandaDasbor);

        cardLayout.show(panelKontenUtama, "DASBOR");
    }

    private JPanel buatPanelNavigasiSamping(Admin admin) {
        // ... (kode buatPanelNavigasiSamping tetap sama dari jawaban sebelumnya)
        JPanel panelSamping = new JPanel();
        panelSamping.setLayout(new BoxLayout(panelSamping, BoxLayout.Y_AXIS));
        panelSamping.setBackground(WARNA_PANEL_SAMPING);
        panelSamping.setPreferredSize(new Dimension(260, 0));
        panelSamping.setBorder(new MatteBorder(0, 0, 0, 1, new Color(55,55,55)));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(WARNA_PANEL_SAMPING);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.setBorder(new EmptyBorder(25, 15, 35, 15));

        JLabel judulAplikasi = new JLabel("SINAR JAYA ADMIN", SwingConstants.CENTER);
        judulAplikasi.setFont(new Font("Segoe UI Semibold", Font.BOLD, 20));
        judulAplikasi.setForeground(WARNA_TEKS_UTAMA);
        judulAplikasi.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(judulAplikasi);
        panelSamping.add(headerPanel);

        tambahItemNavigasi(panelSamping, "📊 Dasbor", "DASBOR");
        tambahItemNavigasi(panelSamping, "✈️ Kelola Perjalanan", "PERJALANAN");
        tambahItemNavigasi(panelSamping, "📋 Kelola Reservasi", "RESERVASI");
        tambahItemNavigasi(panelSamping, "👥 Kelola Pengguna", "PENGGUNA");
        tambahItemNavigasi(panelSamping, "📈 Kelola Laporan", "LAPORAN");
        tambahItemNavigasi(panelSamping, "💳 Kelola Pembayaran", "PEMBAYARAN"); 

        panelSamping.add(Box.createVerticalGlue());

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(WARNA_PANEL_SAMPING);
        footerPanel.setBorder(new EmptyBorder(15, 15, 20, 15));
        footerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelAdmin = new JLabel(admin.getNamaLengkap(), SwingConstants.CENTER);
        labelAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelAdmin.setForeground(WARNA_TEKS_UTAMA);
        labelAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.add(labelAdmin);
        
        JLabel emailAdminLabel = new JLabel(admin.getEmail(), SwingConstants.CENTER);
        emailAdminLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        emailAdminLabel.setForeground(WARNA_TEKS_SEKUNDER);
        emailAdminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.add(emailAdminLabel);
        footerPanel.add(Box.createRigidArea(new Dimension(0,10)));

        JLabel labelKeluar = new JLabel("🚪 Keluar", SwingConstants.CENTER);
        labelKeluar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        labelKeluar.setForeground(WARNA_AKSEN_BIRU);
        labelKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelKeluar.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelKeluar.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (JOptionPane.showConfirmDialog(AdminDashboardView.this, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) System.exit(0);
            }
            @Override public void mouseEntered(MouseEvent e) { labelKeluar.setForeground(WARNA_AKSEN_HOVER); }
            @Override public void mouseExited(MouseEvent e) { labelKeluar.setForeground(WARNA_AKSEN_BIRU); }
        });
        footerPanel.add(labelKeluar);
        panelSamping.add(footerPanel);
        
        return panelSamping;
    }

    private void tambahItemNavigasi(JPanel parent, String text, String cardName) {
        // ... (kode tambahItemNavigasi tetap sama dari jawaban sebelumnya)
        JPanel itemNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        itemNav.setBackground(WARNA_PANEL_SAMPING);
        itemNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        itemNav.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemNav.setBorder(new EmptyBorder(0,15,0,0));

        String iconText = text.substring(0, text.indexOf(" ") + 1);
        String menuText = text.substring(text.indexOf(" ") + 1);
        JLabel iconLabel = new JLabel(iconText); 
        iconLabel.setForeground(WARNA_TEKS_UTAMA);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        JLabel textLabel = new JLabel(menuText);
        textLabel.setForeground(WARNA_TEKS_UTAMA);
        textLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        itemNav.add(iconLabel); itemNav.add(textLabel);
        
        itemNav.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { itemNav.setBackground(WARNA_AKSEN_HOVER.darker().darker()); }
            @Override public void mouseExited(MouseEvent e) { itemNav.setBackground(WARNA_PANEL_SAMPING); }
            @Override public void mouseClicked(MouseEvent e) {
                if (cardName.equals("DASBOR")) {
                    panelKontenUtama.removeAll();
                    panelCache.clear();
                    JPanel panelBerandaDasbor = buatPanelBerandaDasbor();
                    panelKontenUtama.add(panelBerandaDasbor, "DASBOR");
                    panelCache.put("DASBOR", panelBerandaDasbor);
                    cardLayout.show(panelKontenUtama, "DASBOR");
                } else if (!panelCache.containsKey(cardName)) {
                    JPanel newPanel;
                    switch (cardName) {
                        case "PERJALANAN": newPanel = new KelolaPerjalananView(); break;
                        case "RESERVASI": newPanel = new KelolaReservasiView(); break;
                        case "PENGGUNA": newPanel = new KelolaPenggunaView(); break;
                        case "LAPORAN": newPanel = new KelolaLaporanView(); break;
                        case "PEMBAYARAN": newPanel = new KelolaPembayaranView(); break;
                        default: newPanel = buatPanelPlaceholder("Segera Hadir: " + cardName); break;
                    }
                    panelKontenUtama.add(newPanel, cardName);
                    panelCache.put(cardName, newPanel);
                }
                cardLayout.show(panelKontenUtama, cardName);
            }
        });
        parent.add(itemNav);
    }
    
    /**
     * Membuat panel beranda dasbor dengan TATA LETAK BARU:
     * Kartu statistik ringkas di atas, grafik di bawah.
     */
    private JPanel buatPanelBerandaDasbor() {
        JPanel panelInduk = new JPanel(new BorderLayout(15, 20)); // Jarak vertikal antar bagian
        panelInduk.setBorder(new EmptyBorder(20, 25, 20, 25));
        panelInduk.setOpaque(false);

        JLabel judulHalaman = new JLabel("Gambaran Umum Dasbor");
        judulHalaman.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
        judulHalaman.setForeground(WARNA_TEKS_UTAMA);
        judulHalaman.setBorder(new EmptyBorder(0, 0, 15, 0));
        panelInduk.add(judulHalaman, BorderLayout.NORTH);

        // --- BAGIAN ATAS: KARTU STATISTIK RINGKAS ---
        JPanel panelKartuStatistik = new JPanel(new GridLayout(1, 4, 15, 15)); // Jarak antar kartu
        panelKartuStatistik.setOpaque(false);

        BigDecimal totalPendapatan = reservasiController.getTotalPendapatanLunas();
        if (totalPendapatan == null) totalPendapatan = BigDecimal.ZERO;
        int totalPemesanan = reservasiController.getTotalReservasi();
        int pemesananPending = reservasiController.getTotalReservasiByStatus("pending");
        int totalPengguna = userController.getTotalPengguna(); // Jika Anda ingin menambahkannya kembali
        // int perjalananAktif = perjalananController.getTotalPaketPerjalananAktif(); // Jika ingin

        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);

        panelKartuStatistik.add(buatKartuStatistikRingkas("💰 Pendapatan", formatMataUang.format(totalPendapatan), new Color(26, 188, 156)));
        panelKartuStatistik.add(buatKartuStatistikRingkas("🛒 Pemesanan", String.valueOf(totalPemesanan), new Color(52, 152, 219)));
        panelKartuStatistik.add(buatKartuStatistikRingkas("⏳ Pending", String.valueOf(pemesananPending), new Color(243, 156, 18)));
        panelKartuStatistik.add(buatKartuStatistikRingkas("👤 Pengguna", String.valueOf(totalPengguna), new Color(155, 89, 182)));
        
        panelInduk.add(panelKartuStatistik, BorderLayout.CENTER);

        // --- BAGIAN BAWAH: GRAFIK (DIPERKECIL) ---
        JPanel panelGrafikKontainer = new JPanel(new GridLayout(1, 2, 20, 0));
        panelGrafikKontainer.setOpaque(false);
        panelGrafikKontainer.setBorder(new EmptyBorder(20, 0, 0, 0)); // Jarak atas dari kartu statistik

        // Grafik 1: Penjualan per Paket (Bar Chart)
        DefaultCategoryDataset datasetPenjualanPaket = new DefaultCategoryDataset();
        List<Object[]> dataPenjualanPaket = reservasiController.getLaporanPenjualanPerPaket();
        int jumlahPaketDitampilkan = 0;
        if (dataPenjualanPaket != null && !dataPenjualanPaket.isEmpty()) {
            for (Object[] item : dataPenjualanPaket) {
                if (jumlahPaketDitampilkan >= 5) break; // Batasi jumlah paket di grafik agar tidak terlalu ramai
                String namaPaket = (String) item[0];
                BigDecimal pendapatanPaket = (BigDecimal) item[2];
                if (pendapatanPaket == null) pendapatanPaket = BigDecimal.ZERO;
                datasetPenjualanPaket.addValue(pendapatanPaket, "Pendapatan (Rp)", namaPaket.length() > 15 ? namaPaket.substring(0,15)+"..." : namaPaket); // Potong nama paket jika terlalu panjang
                jumlahPaketDitampilkan++;
            }
        } else {
            datasetPenjualanPaket.addValue(0, "Pendapatan (Rp)", "Belum Ada Data");
        }
        JFreeChart barChartPenjualan = ChartFactory.createBarChart("Top 5 Pendapatan per Paket", "Paket", "Rp", datasetPenjualanPaket, PlotOrientation.VERTICAL, false, true, false);
        kustomisasiGrafik(barChartPenjualan, true);
        ChartPanel chartPanelPenjualan = new ChartPanel(barChartPenjualan);
        chartPanelPenjualan.setPreferredSize(new Dimension(400, 280)); // Atur ukuran grafik
        chartPanelPenjualan.setBackground(WARNA_GRAFIK_LATAR_PANEL);
        chartPanelPenjualan.setBorder(BorderFactory.createLineBorder(WARNA_PANEL_SAMPING,1));
        panelGrafikKontainer.add(chartPanelPenjualan);

        // Grafik 2: Komposisi Status Reservasi (Pie Chart)
        DefaultPieDataset<String> datasetStatusReservasi = new DefaultPieDataset<>();
        Map<String, Integer> dataStatus = reservasiController.getJumlahReservasiPerStatus();
        if (dataStatus != null && !dataStatus.isEmpty()) {
            for (Map.Entry<String, Integer> entry : dataStatus.entrySet()) {
                datasetStatusReservasi.setValue(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
            }
        } else {
             datasetStatusReservasi.setValue("Belum Ada Data", 1);
        }
        JFreeChart pieChartStatus = ChartFactory.createPieChart("Status Reservasi", datasetStatusReservasi, true, true, false);
        kustomisasiGrafik(pieChartStatus, false);
        ChartPanel chartPanelStatus = new ChartPanel(pieChartStatus);
        chartPanelStatus.setPreferredSize(new Dimension(400, 280)); // Atur ukuran grafik
        chartPanelStatus.setBackground(WARNA_GRAFIK_LATAR_PANEL);
        chartPanelStatus.setBorder(BorderFactory.createLineBorder(WARNA_PANEL_SAMPING,1));
        panelGrafikKontainer.add(chartPanelStatus);

        panelInduk.add(panelGrafikKontainer, BorderLayout.SOUTH);
        
        return panelInduk;
    }
    
    /**
     * Helper untuk membuat KARTU STATISTIK RINGKAS (versi lebih kecil).
     */
    private JPanel buatKartuStatistikRingkas(String judul, String nilai, Color aksen) {
        JPanel kartu = new JPanel(new BorderLayout(5,2));
        kartu.setBackground(WARNA_KARTU_LATAR);
        kartu.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 4, 0, aksen), // Aksen bawah
            new EmptyBorder(10, 15, 10, 15)
        ));

        String ikonJudul = judul.substring(0, judul.indexOf(" ") + 1);
        String teksJudul = judul.substring(judul.indexOf(" ") + 1);

        JLabel labelTeksJudul = new JLabel(teksJudul.toUpperCase());
        labelTeksJudul.setFont(new Font("Segoe UI Semibold", Font.BOLD, 11)); // Font judul lebih kecil
        labelTeksJudul.setForeground(WARNA_TEKS_SEKUNDER);
        labelTeksJudul.setHorizontalAlignment(SwingConstants.LEFT);
        kartu.add(labelTeksJudul, BorderLayout.NORTH);

        JLabel labelNilai = new JLabel(nilai);
        labelNilai.setFont(new Font("Segoe UI Black", Font.BOLD, 20)); // Font nilai lebih kecil
        labelNilai.setForeground(WARNA_TEKS_UTAMA);
        labelNilai.setHorizontalAlignment(SwingConstants.LEFT);
        kartu.add(labelNilai, BorderLayout.CENTER);
        
        return kartu;
    }
    
    private void kustomisasiGrafik(JFreeChart chart, boolean isBarChart) {
        chart.setBackgroundPaint(WARNA_GRAFIK_LATAR_PANEL);
        chart.getTitle().setPaint(WARNA_TEKS_UTAMA);
        chart.getTitle().setFont(new Font("Segoe UI Semibold", Font.BOLD, 14)); // Font judul grafik

        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(WARNA_GRAFIK_LATAR_PANEL);
            chart.getLegend().setItemPaint(WARNA_TEKS_SEKUNDER);
            chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 10));
        }

        if (isBarChart) {
            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(WARNA_PLOT_GRAFIK);
            plot.setDomainGridlinePaint(WARNA_PANEL_SAMPING);
            plot.setRangeGridlinePaint(WARNA_PANEL_SAMPING);
            plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 9));
            plot.getDomainAxis().setTickLabelPaint(WARNA_TEKS_SEKUNDER);
            plot.getDomainAxis().setLabelPaint(WARNA_TEKS_UTAMA);
            plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
            plot.getRangeAxis().setTickLabelPaint(WARNA_TEKS_SEKUNDER);
            plot.getRangeAxis().setLabelPaint(WARNA_TEKS_UTAMA);
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, WARNA_AKSEN_BIRU);
            renderer.setDrawBarOutline(false);
            renderer.setShadowVisible(false);
        } else { // PieChart
            PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
            plot.setBackgroundPaint(WARNA_PLOT_GRAFIK);
            plot.setLabelPaint(WARNA_TEKS_UTAMA);
            plot.setNoDataMessage("Tidak ada data");
            plot.setNoDataMessagePaint(WARNA_TEKS_SEKUNDER);
            plot.setLabelBackgroundPaint(null);
            plot.setLabelOutlinePaint(null);
            plot.setLabelShadowPaint(null);
            plot.setShadowPaint(null);
            plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        }
    }
    
    private JPanel buatPanelPlaceholder(String judulTeks) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(judulTeks);
        label.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
        label.setForeground(WARNA_TEKS_UTAMA);
        panel.add(label);
        return panel;
    }
}