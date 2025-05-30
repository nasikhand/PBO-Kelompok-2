/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import config.ImageUtil;
import model.Admin;
import controller.ReservasiController;
import controller.UserController;
import controller.PerjalananController;
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

    private final Color WARNA_LATAR_UTAMA = new Color(30, 32, 34);
    private final Color WARNA_PANEL_SAMPING = new Color(38, 40, 42);
    private final Color WARNA_AKSEN_BIRU = new Color(20, 125, 250);
    private final Color WARNA_AKSEN_HOVER = new Color(50, 150, 255);
    private final Color WARNA_TEKS_UTAMA = new Color(220, 220, 220);
    private final Color WARNA_TEKS_SEKUNDER = new Color(160, 160, 160);
    private final Color WARNA_KARTU_LATAR = new Color(45, 48, 52);
    private final Color WARNA_KARTU_HOVER = new Color(55, 58, 62);
    private final Color WARNA_GRAFIK_LATAR_PANEL = new Color(45, 48, 52);
    private final Color WARNA_PLOT_GRAFIK = new Color(55, 58, 62);

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

        tambahItemNavigasi(panelSamping, "Dashboard", "/resources/icons/dashboard1.png", "DASBOR");
        tambahItemNavigasi(panelSamping, "Kelola Perjalanan", "/resources/icons/travel.png", "PERJALANAN");
        tambahItemNavigasi(panelSamping, "Kelola Reservasi", "/resources/icons/reservation.png", "RESERVASI");
        tambahItemNavigasi(panelSamping, "Kelola Custom Trip", "/resources/icons/custom_trip.png", "CUSTOM_TRIP");
        tambahItemNavigasi(panelSamping, "Kelola Pengguna", "/resources/icons/users.png", "PENGGUNA");
        tambahItemNavigasi(panelSamping, "Kelola Kota", "/resources/icons/city.png", "KOTA");
        tambahItemNavigasi(panelSamping, "Kelola Destinasi", "/resources/icons/destination.png", "DESTINASI");
        tambahItemNavigasi(panelSamping, "Kelola Pembayaran", "/resources/icons/payment.png", "PEMBAYARAN");
        tambahItemNavigasi(panelSamping, "Kelola Laporan", "/resources/icons/report.png", "LAPORAN");

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

        ImageIcon logoutIcon = ImageUtil.loadAndResizeFromResource("/resources/icons/logout.png", 16, 16);
        JLabel labelKeluar = new JLabel("Keluar", logoutIcon, SwingConstants.LEFT);
        labelKeluar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        labelKeluar.setForeground(WARNA_AKSEN_BIRU);
        labelKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelKeluar.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelKeluar.setIconTextGap(8);
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

    private void tambahItemNavigasi(JPanel parent, String text, String iconPath, String cardName) {
        JPanel itemNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        itemNav.setBackground(WARNA_PANEL_SAMPING);
        itemNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        itemNav.setCursor(new Cursor(Cursor.HAND_CURSOR));
        itemNav.setBorder(new EmptyBorder(0, 20, 0, 0));

        ImageIcon resizedIcon = ImageUtil.loadAndResizeFromResource(iconPath, 20, 20);
        JLabel iconLabel = new JLabel(resizedIcon != null ? resizedIcon : new JLabel("?").getIcon());
        itemNav.add(iconLabel);
        
        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(WARNA_TEKS_UTAMA);
        textLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
        itemNav.add(textLabel);
        
        itemNav.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { itemNav.setBackground(WARNA_AKSEN_HOVER.darker().darker()); }
            @Override public void mouseExited(MouseEvent e) { itemNav.setBackground(WARNA_PANEL_SAMPING); }
            @Override public void mouseClicked(MouseEvent e) {
                tampilkanPanel(cardName);
            }
        });
        parent.add(itemNav);
    }
    
    private void tampilkanPanel(String cardName) {
        if ("DASBOR".equals(cardName)) {
            panelKontenUtama.remove(panelCache.get(cardName));
            JPanel panelBerandaDasbor = buatPanelBerandaDasbor();
            panelKontenUtama.add(panelBerandaDasbor, "DASBOR");
            panelCache.put("DASBOR", panelBerandaDasbor);
            panelKontenUtama.revalidate();
            panelKontenUtama.repaint();
        } 
        else if (!panelCache.containsKey(cardName)) {
            JPanel newPanel;
            switch (cardName) {
                case "PERJALANAN": newPanel = new KelolaPerjalananView(); break;
                case "RESERVASI": newPanel = new KelolaReservasiView(); break;
                case "CUSTOM_TRIP": newPanel = new KelolaCustomTripView(); break;
                case "PENGGUNA": newPanel = new KelolaPenggunaView(); break;
                case "KOTA": newPanel = new KelolaKotaView(); break;
                case "DESTINASI": newPanel = new KelolaDestinasiView(); break;
                case "PEMBAYARAN": newPanel = new KelolaPembayaranView(); break;
                case "LAPORAN": newPanel = new KelolaLaporanView(); break;
                default: newPanel = new JPanel(); break;
            }
            panelKontenUtama.add(newPanel, cardName);
            panelCache.put(cardName, newPanel);
        }
        cardLayout.show(panelKontenUtama, cardName);
    }

    private JPanel buatPanelBerandaDasbor() {
        JPanel panelInduk = new JPanel(new BorderLayout(15, 20));
        panelInduk.setBorder(new EmptyBorder(20, 25, 20, 25));
        panelInduk.setOpaque(false);
        
        JPanel panelKontenAtas = new JPanel(new BorderLayout(15, 15));
        panelKontenAtas.setOpaque(false);
        
        JLabel judulHalaman = new JLabel("Gambaran Umum Dasbor");
        judulHalaman.setFont(new Font("Segoe UI Light", Font.PLAIN, 36));
        judulHalaman.setForeground(WARNA_TEKS_UTAMA);
        judulHalaman.setBorder(new EmptyBorder(0, 0, 15, 0));
        panelKontenAtas.add(judulHalaman, BorderLayout.NORTH);

        JPanel panelKartuStatistik = new JPanel(new GridLayout(1, 4, 15, 15));
        panelKartuStatistik.setOpaque(false);
        
        BigDecimal totalPendapatan = reservasiController.getTotalPendapatanLunas();
        int totalPemesanan = reservasiController.getTotalReservasi();
        int pemesananPending = reservasiController.getTotalReservasiByStatus("pending");
        int totalPengguna = userController.getTotalPengguna();
        NumberFormat formatMataUang = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatMataUang.setMaximumFractionDigits(0);
        
        panelKartuStatistik.add(buatKartuStatistikRingkas("PENDAPATAN", formatMataUang.format(totalPendapatan), new Color(26, 188, 156), "/resources/icons/money_bag.png", () -> tampilkanPanel("LAPORAN")));
        panelKartuStatistik.add(buatKartuStatistikRingkas("PEMESANAN", String.valueOf(totalPemesanan), new Color(52, 152, 219), "/resources/icons/keranjang.png", () -> tampilkanPanel("RESERVASI")));
        panelKartuStatistik.add(buatKartuStatistikRingkas("PENDING", String.valueOf(pemesananPending), new Color(243, 156, 18), "/resources/icons/pending.png", () -> {
            tampilkanPanel("RESERVASI");
            KelolaReservasiView panelReservasi = (KelolaReservasiView) panelCache.get("RESERVASI");
            if (panelReservasi != null) {
                panelReservasi.terapkanFilterStatusEksternal("pending");
            }
        }));
        panelKartuStatistik.add(buatKartuStatistikRingkas("PENGGUNA", String.valueOf(totalPengguna), new Color(155, 89, 182), "/resources/icons/orang.png", () -> tampilkanPanel("PENGGUNA")));
        
        panelKontenAtas.add(panelKartuStatistik, BorderLayout.CENTER);
        panelInduk.add(panelKontenAtas, BorderLayout.NORTH);

        JPanel panelGrafikKontainer = new JPanel(new GridLayout(1, 2, 20, 0));
        panelGrafikKontainer.setOpaque(false);
        
        DefaultCategoryDataset datasetPenjualanPaket = new DefaultCategoryDataset();
        List<Object[]> dataPenjualanPaket = reservasiController.getLaporanPenjualanPerPaket(null, null);
        int jumlahPaketDitampilkan = 0;
        for (Object[] item : dataPenjualanPaket) {
            if (jumlahPaketDitampilkan >= 5) break;
            String namaPaket = (String) item[0];
            datasetPenjualanPaket.addValue((BigDecimal) item[2], "Pendapatan (Rp)", namaPaket.length() > 15 ? namaPaket.substring(0,15)+"..." : namaPaket);
            jumlahPaketDitampilkan++;
        }
        if (datasetPenjualanPaket.getColumnCount() == 0) {
            datasetPenjualanPaket.addValue(0, "Pendapatan (Rp)", "Belum Ada Data");
        }
        JFreeChart barChartPenjualan = ChartFactory.createBarChart("Top 5 Pendapatan per Paket", "Paket", "Rp", datasetPenjualanPaket, PlotOrientation.VERTICAL, false, true, false);
        kustomisasiGrafik(barChartPenjualan, true);
        ChartPanel chartPanelPenjualan = new ChartPanel(barChartPenjualan);
        chartPanelPenjualan.setBackground(WARNA_GRAFIK_LATAR_PANEL);
        chartPanelPenjualan.setBorder(BorderFactory.createLineBorder(WARNA_PANEL_SAMPING,1));
        panelGrafikKontainer.add(chartPanelPenjualan);
        
        DefaultPieDataset<String> datasetStatusReservasi = new DefaultPieDataset<>();
        Map<String, Integer> dataStatus = reservasiController.getJumlahReservasiPerStatus();
        if (dataStatus.isEmpty()) {
            datasetStatusReservasi.setValue("Belum Ada Data", 1);
        } else {
            for (Map.Entry<String, Integer> entry : dataStatus.entrySet()) {
                datasetStatusReservasi.setValue(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
            }
        }
        JFreeChart pieChartStatus = ChartFactory.createPieChart("Status Reservasi", datasetStatusReservasi, true, true, false);
        kustomisasiGrafik(pieChartStatus, false);
        ChartPanel chartPanelStatus = new ChartPanel(pieChartStatus);
        chartPanelStatus.setBackground(WARNA_GRAFIK_LATAR_PANEL);
        chartPanelStatus.setBorder(BorderFactory.createLineBorder(WARNA_PANEL_SAMPING,1));
        panelGrafikKontainer.add(chartPanelStatus);
        
        panelInduk.add(panelGrafikKontainer, BorderLayout.CENTER);
        
        return panelInduk;
    }
    
    private JPanel buatKartuStatistikRingkas(String judul, String nilai, Color aksen, String iconPath, Runnable actionOnClick) {
        JPanel kartu = new JPanel(new BorderLayout(10, 2));
        kartu.setBackground(WARNA_KARTU_LATAR);
        kartu.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 5, 0, 0, aksen),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JPanel panelTeks = new JPanel();
        panelTeks.setOpaque(false);
        panelTeks.setLayout(new BoxLayout(panelTeks, BoxLayout.Y_AXIS));

        JLabel labelJudul = new JLabel(judul.toUpperCase());
        labelJudul.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
        labelJudul.setForeground(WARNA_TEKS_SEKUNDER);
        panelTeks.add(labelJudul);

        JLabel labelNilai = new JLabel(nilai);
        labelNilai.setFont(new Font("Segoe UI Black", Font.BOLD, 24));
        labelNilai.setForeground(WARNA_TEKS_UTAMA);
        panelTeks.add(labelNilai);

        kartu.add(panelTeks, BorderLayout.CENTER);
        
        ImageIcon icon = ImageUtil.loadAndResizeFromResource(iconPath, 32, 32);
        if (icon != null) {
            JLabel labelIkon = new JLabel(icon);
            kartu.add(labelIkon, BorderLayout.EAST);
        }

        if (actionOnClick != null) {
            kartu.setCursor(new Cursor(Cursor.HAND_CURSOR));
            kartu.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { actionOnClick.run(); }
                @Override public void mouseEntered(MouseEvent e) { kartu.setBackground(WARNA_KARTU_HOVER); }
                @Override public void mouseExited(MouseEvent e) { kartu.setBackground(WARNA_KARTU_LATAR); }
            });
        }
        
        return kartu;
    }
    
    private void kustomisasiGrafik(JFreeChart chart, boolean isBarChart) {
        chart.setBackgroundPaint(WARNA_GRAFIK_LATAR_PANEL);
        chart.getTitle().setPaint(WARNA_TEKS_UTAMA);
        chart.getTitle().setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
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
            plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
            plot.getRangeAxis().setTickLabelPaint(WARNA_TEKS_SEKUNDER);
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, WARNA_AKSEN_BIRU);
            renderer.setDrawBarOutline(false);
            renderer.setShadowVisible(false);
        } else {
            PiePlot<?> plot = (PiePlot<?>) chart.getPlot();
            plot.setBackgroundPaint(WARNA_PLOT_GRAFIK);
            plot.setLabelPaint(WARNA_TEKS_UTAMA);
            plot.setNoDataMessage("Tidak ada data");
            plot.setLabelBackgroundPaint(null);
            plot.setLabelOutlinePaint(null);
            plot.setLabelShadowPaint(null);
            plot.setShadowPaint(null);
            plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        }
    }
}