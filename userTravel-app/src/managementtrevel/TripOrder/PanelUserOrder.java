package managementtrevel.TripOrder;

import Asset.AppTheme;
import db.Koneksi;
import db.dao.PembayaranDAO;
import db.dao.PenumpangDAO;
import db.dao.ReservasiDAO;
import db.dao.PaketPerjalananDAO;
import db.dao.CustomTripDAO;
import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;
import model.CustomTripModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

public class PanelUserOrder extends JPanel {

    private MainAppFrame mainAppFrame;

    private JPanel mainContentPanel;
    private JScrollPane scrollPane;
    private JLabel titleLabel;
    private JButton btn_back;

    private ReservasiDAO reservasiDAO;
    private PenumpangDAO penumpangDAO;
    private PembayaranDAO pembayaranDAO;
    private PaketPerjalananDAO paketPerjalananDAO;
    private CustomTripDAO customTripDAO;

    public PanelUserOrder(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;

        reservasiDAO = new ReservasiDAO(Koneksi.getConnection());
        penumpangDAO = new PenumpangDAO(Koneksi.getConnection());
        pembayaranDAO = new PembayaranDAO(Koneksi.getConnection());
        paketPerjalananDAO = new PaketPerjalananDAO(Koneksi.getConnection());
        customTripDAO = new CustomTripDAO(Koneksi.getConnection());

        initializeUI();
        loadDataReservasi();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        btn_back = new JButton("< Kembali");
        btn_back.setFont(AppTheme.FONT_BUTTON);
        btn_back.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        btn_back.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        btn_back.setOpaque(true);
        btn_back.setBorderPainted(false);
        btn_back.setFocusPainted(false);
        btn_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn_back.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(btn_back, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
        btn_back.addActionListener(this::btn_backActionPerformed);
        headerPanel.add(btn_back, BorderLayout.WEST);

        titleLabel = new JLabel("Pesanan Anda");
        titleLabel.setFont(AppTheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel dummyEast = new JLabel();
        dummyEast.setPreferredSize(btn_back.getPreferredSize());
        headerPanel.add(dummyEast, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);

        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false);
        mainContentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        this.add(scrollPane, BorderLayout.CENTER);

        applyAppTheme();
    }

    private void loadDataReservasi() {
        mainContentPanel.removeAll();
        mainContentPanel.revalidate();
        mainContentPanel.repaint();

        try {
            if (Session.currentUser == null) {
                displayEmptyMessage("Pengguna belum login.");
                return;
            }

            int userId = Session.currentUser.getId();
            List<ReservasiModel> listReservasi = reservasiDAO.getReservasiAktifDenganTrip(userId);

            if (!listReservasi.isEmpty()) {
                for (ReservasiModel reservasi : listReservasi) {
                    mainContentPanel.add(createReservationCard(reservasi));
                    mainContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            } else {
                displayEmptyMessage("Tidak ada reservasi aktif ditemukan.");
            }
        } catch (Exception e) {
            displayEmptyMessage("Gagal mengambil data reservasi: " + e.getMessage());
            System.err.println("Error saat mengambil data reservasi di PanelUserOrder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayEmptyMessage(String message) {
        mainContentPanel.removeAll();
        JLabel emptyLabel = new JLabel(message, SwingConstants.CENTER);
        emptyLabel.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
        emptyLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalGlue());
        mainContentPanel.add(emptyLabel);
        mainContentPanel.add(Box.createVerticalGlue());
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }


    private JPanel createReservationCard(ReservasiModel reservasi) {
        JPanel cardPanel = new JPanel(new BorderLayout(15, 0));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel fotoLabel = new JLabel("FOTO", SwingConstants.CENTER);
        fotoLabel.setPreferredSize(new Dimension(100, 100));
        fotoLabel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        fotoLabel.setOpaque(true);
        fotoLabel.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        fotoLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        fotoLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        if ("paket_perjalanan".equals(reservasi.getTripType()) && reservasi.getPaket() != null) {
            String gambarPath = reservasi.getPaket().getGambar();
            if (gambarPath != null && !gambarPath.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        File baseDir = new File(System.getProperty("user.dir")).getParentFile();
                        String gambarRelatif = gambarPath.startsWith("/") || gambarPath.startsWith("\\") ? gambarPath.substring(1) : gambarPath;
                        File imageFile = new File(baseDir, gambarRelatif);

                        if (imageFile.exists()) {
                            ImageIcon originalIcon = new ImageIcon(imageFile.toURI().toURL());
                            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                            fotoLabel.setIcon(new ImageIcon(scaledImage));
                            fotoLabel.setText("");
                            fotoLabel.setOpaque(false);
                        } else {
                            fotoLabel.setText("Gambar tidak ditemukan");
                        }
                    } catch (Exception e) {
                        fotoLabel.setText("Gagal memuat gambar");
                        System.err.println("Error loading image for card: " + e.getMessage());
                    }
                });
            }
        } else if ("custom_trip".equals(reservasi.getTripType())) {
             fotoLabel.setText("Custom Trip");
        }
        cardPanel.add(fotoLabel, BorderLayout.WEST);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);
        detailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNamaTrip = new JLabel();
        lblNamaTrip.setFont(AppTheme.FONT_SUBTITLE);
        lblNamaTrip.setForeground(AppTheme.TEXT_DARK);
        lblNamaTrip.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblNamaTrip);

        JLabel lblDetail1 = new JLabel();
        lblDetail1.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
        lblDetail1.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblDetail1.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblDetail1);

        JLabel lblHarga = new JLabel();
        lblHarga.setFont(AppTheme.FONT_PRIMARY_BOLD);
        lblHarga.setForeground(AppTheme.ACCENT_ORANGE);
        lblHarga.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblHarga);

        JLabel lblStatus = new JLabel();
        lblStatus.setFont(AppTheme.FONT_LABEL_FORM);
        lblStatus.setForeground(AppTheme.TEXT_DARK);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblStatus);
        
        detailPanel.add(Box.createVerticalGlue());

        String namaTrip = "N/A";
        String detailText = "";
        String hargaText = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(0.0);
        int jumlahOrang = 0;
        double rating = 0.0;
        double currentTotalHarga = 0.0;

        // Debug prints untuk penumpang
        jumlahOrang = penumpangDAO.getJumlahPenumpangByReservasiId(reservasi.getId());
        System.out.println("DEBUG PanelUserOrder - Reservasi ID: " + reservasi.getId() + ", Jumlah Penumpang: " + jumlahOrang);


        if ("paket_perjalanan".equals(reservasi.getTripType()) && reservasi.getPaket() != null) {
            PaketPerjalananModel paket = reservasi.getPaket();
            namaTrip = paket.getNamaPaket() + " (Kota: " + paket.getNamaKota() + ")";
            rating = paket.getRating();
            detailText = paket.getJumlahHari() + " Hari | " + jumlahOrang + " Orang | Rating " + String.format("%.1f", rating);
            
            // Debug print: harga paket dasar
            System.out.println("DEBUG PanelUserOrder - Paket Harga Dasar: " + paket.getHarga());

            Double hargaPembayaranLunas = pembayaranDAO.getJumlahPembayaranByReservasiId(reservasi.getId());
            // Debug print: pembayaran lunas
            System.out.println("DEBUG PanelUserOrder - Pembayaran Lunas: " + hargaPembayaranLunas);

            if (hargaPembayaranLunas != null && hargaPembayaranLunas > 0) {
                currentTotalHarga = hargaPembayaranLunas;
            } else {
                currentTotalHarga = paket.getHarga() * jumlahOrang;
            }
            hargaText = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(currentTotalHarga);
            
            // Debug print: final harga text paket
            System.out.println("DEBUG PanelUserOrder - Final Harga Paket: " + hargaText);

        } else if ("custom_trip".equals(reservasi.getTripType()) && reservasi.getCustomTrip() != null) {
            CustomTripModel customTrip = reservasi.getCustomTrip();
            namaTrip = customTrip.getNamaTrip() + " (Kota: " + customTrip.getNamaKota() + ")";
            if (customTrip.getJumlahPeserta() > 0) {
                jumlahOrang = customTrip.getJumlahPeserta();
            } else {
                jumlahOrang = penumpangDAO.getJumlahPenumpangByReservasiId(reservasi.getId());
            }
            detailText = customTrip.getJumlahHari() + " Hari | " + jumlahOrang + " Orang";
            
            currentTotalHarga = customTrip.getTotalHarga(); // Mengambil total harga dari CustomTripModel
            hargaText = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(currentTotalHarga);

            // Debug print: final harga text custom trip
            System.out.println("DEBUG PanelUserOrder - Custom Trip Total Harga: " + customTrip.getTotalHarga());
            System.out.println("DEBUG PanelUserOrder - Final Harga Custom Trip: " + hargaText);
        }

        lblNamaTrip.setText(namaTrip);
        lblDetail1.setText(detailText);
        lblHarga.setText("Harga: " + hargaText);
        lblStatus.setText("Status Pesanan: " + reservasi.getStatus());

        cardPanel.add(detailPanel, BorderLayout.CENTER);

        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.setFont(AppTheme.FONT_BUTTON);
        btnDetail.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        btnDetail.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        btnDetail.setOpaque(true);
        btnDetail.setBorderPainted(false);
        btnDetail.setFocusPainted(false);
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(btnDetail, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
        btnDetail.addActionListener(e -> {
            mainAppFrame.showPanel(MainAppFrame.PANEL_ORDER_DETAIL, reservasi);
        });

        JPanel buttonWrapPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonWrapPanel.setOpaque(false);
        buttonWrapPanel.add(btnDetail);
        cardPanel.add(buttonWrapPanel, BorderLayout.EAST);

        return cardPanel;
    }


    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        if (titleLabel != null) {
            titleLabel.setFont(AppTheme.FONT_TITLE_LARGE);
            titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
        if (btn_back != null) {
            btn_back.setFont(AppTheme.FONT_BUTTON);
            btn_back.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
            btn_back.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
            btn_back.setOpaque(true);
            btn_back.setBorderPainted(false);
            btn_back.setFocusPainted(false);
            btn_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_back.setBorder(new EmptyBorder(8, 15, 8, 15));
            addHoverEffect(btn_back, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
        }
    }

    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        if (button == null) return;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_USER_PROFILE);
        } else {
            System.err.println("MainAppFrame is null in PanelUserOrder (btn_back)");
        }
    }

    private void initComponents() {
    }
}
