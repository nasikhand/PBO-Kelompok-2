package managementtrevel.HomeUser;

import Asset.AppTheme;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import controller.CariCepatController; 
import controller.PaketPerjalananController;
import db.Koneksi;
import db.dao.KotaDAO;
import db.dao.PaketPerjalananDAO;
import db.dao.ReservasiDAO;
import managementtrevel.MainAppFrame;
import model.CustomTripModel;
import model.KotaModel; 
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.PenawaranModel;
import db.dao.PenawaranDAO;

public class PanelBeranda extends JPanel {

    private MainAppFrame mainAppFrame;
    private List<KotaModel> daftarKota; 
    private Date tanggalDipilihCariCepat;
    // private String jumlahTravelerDipilihCariCepat = null; // Dihapus
    private PaketPerjalananController paketPerjalananController;
    private PaketPerjalananDAO paketPerjalananDAO;

    private final String PLACEHOLDER_KOTA = "-- Pilih Kota --"; 
    // private final String PLACEHOLDER_TRAVELERS = "Travelers"; // Dihapus

    // Komponen UI Utama
    private JLabel labelNama;
    private JPanel panelCariCepat;
    private JComboBox<String> cmbKotaCariCepat; 
    private JDateChooser dateChooserCariCepat;
    // private JComboBox<String> cmbTravelersCariCepat; // Dihapus
    private JButton btnTombolCariCepat;
    private JButton btnCustomTrip;

    private JPanel panelPerjalananSebelumnyaContentHolder; 
    private JPanel panelPenawaranSpesialContentHolder;   
    private JPanel panelDestinasiPopulerContentHolder; 


    public PanelBeranda(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        Connection conn = Koneksi.getConnection();
        if (conn == null) {
            System.err.println("Koneksi database gagal di PanelBeranda.");
        }
        this.paketPerjalananController = new PaketPerjalananController(conn);
        this.paketPerjalananDAO = new PaketPerjalananDAO(conn);

        initializeUIProgrammatically(); 
        applyAppTheme();
        populateCariCepat();
        loadDynamicContent();
        setupActionListeners();
    }
    
    private PanelBeranda() { 
        this(null); 
        System.err.println("PERINGATAN: Konstruktor default PanelBeranda dipanggil. mainAppFrame akan null.");
    }

    private void initializeUIProgrammatically() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel topPanel = new JPanel(new BorderLayout(0, 15)); 
        topPanel.setOpaque(false);

        labelNama = new JLabel("Selamat Datang, Tamu");
        topPanel.add(labelNama, BorderLayout.NORTH);

        this.panelCariCepat = createSearchPanel(); 
        topPanel.add(this.panelCariCepat, BorderLayout.CENTER);

        this.add(topPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setOpaque(false); 

        JPanel sectionPerjalananSebelumnya = createSectionPanel("Perjalanan Sebelumnya");
        panelPerjalananSebelumnyaContentHolder = new JPanel(); 
        panelPerjalananSebelumnyaContentHolder.setOpaque(false); 
        sectionPerjalananSebelumnya.add(panelPerjalananSebelumnyaContentHolder, BorderLayout.CENTER);
        mainContentPanel.add(sectionPerjalananSebelumnya);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // JPanel sectionPenawaranSpesial = createSectionPanel("Penawaran Spesial");
        // panelPenawaranSpesialContentHolder = new JPanel();
        // panelPenawaranSpesialContentHolder.setOpaque(false);
        // sectionPenawaranSpesial.add(panelPenawaranSpesialContentHolder, BorderLayout.CENTER);
        // mainContentPanel.add(sectionPenawaranSpesial);
        // mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel sectionDestinasiPopuler = createSectionPanel("Destinasi Populer");
        panelDestinasiPopulerContentHolder = new JPanel();
        panelDestinasiPopulerContentHolder.setOpaque(false);
        sectionDestinasiPopuler.add(panelDestinasiPopulerContentHolder, BorderLayout.CENTER);
        mainContentPanel.add(sectionDestinasiPopuler);
        mainContentPanel.add(Box.createVerticalGlue());

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.getViewport().setOpaque(false); 
        mainScrollPane.setOpaque(false);

        this.add(mainScrollPane, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCariCepat = new JLabel("Cari Cepat");

        cmbKotaCariCepat = new JComboBox<>(); 
        dateChooserCariCepat = new JDateChooser();
        // cmbTravelersCariCepat = new JComboBox<>(); // Dihapus
        btnTombolCariCepat = new JButton(); 
        btnCustomTrip = new JButton();   

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.WEST; // gridwidth disesuaikan
        panel.add(lblCariCepat, gbc);
        gbc.gridwidth = 1; 

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0.4; panel.add(cmbKotaCariCepat, gbc); 
        gbc.gridx = 1; gbc.weightx = 0.3; panel.add(dateChooserCariCepat, gbc);
        // gbc.gridx = 2; gbc.weightx = 0.2; panel.add(cmbTravelersCariCepat, gbc); // Dihapus
        gbc.gridx = 2; gbc.weightx = 0.15; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER; // GridX disesuaikan
        panel.add(btnTombolCariCepat, gbc);
        gbc.gridx = 3; gbc.weightx = 0.15; panel.add(btnCustomTrip, gbc); // GridX disesuaikan
        
        return panel;
    }
    
    private JPanel createSectionPanel(String titleText) {
        JPanel sectionPanel = new JPanel(new BorderLayout(0,10)); 
        sectionPanel.setOpaque(false); 
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            new EmptyBorder(15,15,15,15)
        ));
        sectionPanel.setBackground(Color.WHITE); 

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(AppTheme.FONT_TITLE_MEDIUM);
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        titleLabel.setBorder(new EmptyBorder(0,0,10,0));
        sectionPanel.add(titleLabel, BorderLayout.NORTH);
        
        return sectionPanel;
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        labelNama.setFont(AppTheme.FONT_TITLE_LARGE);
        labelNama.setForeground(AppTheme.TEXT_DARK);
        labelNama.setBorder(new EmptyBorder(0, 0, 15, 0)); 

        if (this.panelCariCepat != null && this.panelCariCepat.getComponentCount() > 0) {
            Component lblCariCepatComp = this.panelCariCepat.getComponent(0); 
            if (lblCariCepatComp instanceof JLabel) { 
                JLabel lblCariCepat = (JLabel) lblCariCepatComp;
                lblCariCepat.setFont(AppTheme.FONT_SUBTITLE); 
                lblCariCepat.setForeground(AppTheme.TEXT_DARK);
            }
        }
        styleComboBox(cmbKotaCariCepat); 
        styleDateChooser(dateChooserCariCepat); 
        // styleComboBox(cmbTravelersCariCepat); // Dihapus
        stylePrimaryButton(btnTombolCariCepat, "Cari");
        styleSecondaryButton(btnCustomTrip, "Custom Trip");

        if(panelPerjalananSebelumnyaContentHolder != null && panelPerjalananSebelumnyaContentHolder.getParent() instanceof JPanel){
            JPanel parentPanel = (JPanel) panelPerjalananSebelumnyaContentHolder.getParent();
            if(parentPanel.getComponentCount() > 0 && parentPanel.getComponent(0) instanceof JLabel) {
                 styleSectionTitle((JLabel)parentPanel.getComponent(0));
            }
        }
         if(panelPenawaranSpesialContentHolder != null && panelPenawaranSpesialContentHolder.getParent() instanceof JPanel){
            JPanel parentPanel = (JPanel) panelPenawaranSpesialContentHolder.getParent();
            if(parentPanel.getComponentCount() > 0 && parentPanel.getComponent(0) instanceof JLabel) {
                 styleSectionTitle((JLabel)parentPanel.getComponent(0));
            }
        }
        if(panelDestinasiPopulerContentHolder != null && panelDestinasiPopulerContentHolder.getParent() instanceof JPanel){
            JPanel parentPanel = (JPanel) panelDestinasiPopulerContentHolder.getParent();
            if(parentPanel.getComponentCount() > 0 && parentPanel.getComponent(0) instanceof JLabel) {
                 styleSectionTitle((JLabel)parentPanel.getComponent(0));
            }
        }
    }

    private void styleSectionTitle(JLabel titleLabel) {
        if (titleLabel != null) {
            // Font dan warna sudah diatur di createSectionPanel
        }
    }

    private void populateCariCepat() {
        if (Session.currentUser != null) {
            labelNama.setText("Selamat Datang, " + Session.currentUser.getNamaLengkap());
        } else {
            labelNama.setText("Selamat Datang, Tamu");
        }

        Connection conn = Koneksi.getConnection();
        if (conn == null) return; 
        CariCepatController controller = new CariCepatController(conn);
        this.daftarKota = controller.getDaftarKota(); 

        cmbKotaCariCepat.removeAllItems();
        cmbKotaCariCepat.addItem(PLACEHOLDER_KOTA); 
        if (this.daftarKota != null) {
            for (KotaModel kota : daftarKota) { 
                if (kota != null) { 
                    cmbKotaCariCepat.addItem(kota.getNamaKota()); 
                } else {
                    System.err.println("Peringatan: Objek KotaModel null ditemukan dalam daftarKota.");
                }
            }
        }
        cmbKotaCariCepat.setRenderer(new PlaceholderComboBoxRenderer(PLACEHOLDER_KOTA));
        cmbKotaCariCepat.setSelectedIndex(0);

        // Bagian untuk cmbTravelersCariCepat dihapus
        // cmbTravelersCariCepat.removeAllItems();
        // cmbTravelersCariCepat.addItem(PLACEHOLDER_TRAVELERS);
        // ... (loop untuk menambahkan jumlah traveler) ...
        // cmbTravelersCariCepat.setRenderer(new PlaceholderComboBoxRenderer(PLACEHOLDER_TRAVELERS));
        // cmbTravelersCariCepat.setSelectedIndex(0);

        dateChooserCariCepat.setDate(null); 
        // dateChooserCariCepat.setMinSelectableDate(new Date()); 
    }

    private void loadDynamicContent() {
        loadPerjalananSebelumnya();
//        loadPenawaranSpesial();
        loadDestinasiPopuler();

        revalidate();
        repaint();
    }

    private void loadPerjalananSebelumnya() {
        panelPerjalananSebelumnyaContentHolder.removeAll(); 
        panelPerjalananSebelumnyaContentHolder.setLayout(new GridLayout(1, 3, 15, 15)); // max 3 card sejajar

        if (Session.currentUser == null) {
            System.err.println("User belum login. Tidak bisa load perjalanan sebelumnya.");
            return;
        }

        ReservasiDAO reservasiDAO = new ReservasiDAO(Koneksi.getConnection());
        List<ReservasiModel> previousTrips = reservasiDAO.getHistoryReservasiByUser(Session.currentUser.getId());


        if (previousTrips.isEmpty()) {
            panelPerjalananSebelumnyaContentHolder.setLayout(new BorderLayout());
            JLabel noDataLabel = new JLabel("Belum ada perjalanan sebelumnya.");
            noDataLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            noDataLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelPerjalananSebelumnyaContentHolder.add(noDataLabel, BorderLayout.CENTER);
        } else {
            int maxToShow = Math.min(3, previousTrips.size()); // tampilkan max 3 card
            for (int i = 0; i < maxToShow; i++) {
                ReservasiModel r = previousTrips.get(i);
                if (r.getTripType().equals("paket_perjalanan")) {
                    panelPerjalananSebelumnyaContentHolder.add(createTripPackageCard(r.getPaket(), true));
                } else if ("custom_trip".equals(r.getTripType()) && r.getCustomTrip() != null) {
                panelPerjalananSebelumnyaContentHolder.add(createCustomTripCard(r.getCustomTrip()));
                 }
            }

        }

        panelPerjalananSebelumnyaContentHolder.revalidate();
        panelPerjalananSebelumnyaContentHolder.repaint();
    }



    // private void loadPenawaranSpesial() {
    //     panelPenawaranSpesialContentHolder.removeAll();
    //     panelPenawaranSpesialContentHolder.setLayout(new GridLayout(1, 3, 15, 15)); // misal 3 card max

    //     PenawaranDAO penawaranDAO = new PenawaranDAO(Koneksi.getConnection());
    //     List<PenawaranModel> penawaranList = penawaranDAO.getPenawaranSpesial();

    //     if (penawaranList == null || penawaranList.isEmpty()) {
    //         panelPenawaranSpesialContentHolder.setLayout(new BorderLayout());
    //         JLabel noDataLabel = new JLabel("Penawaran spesial menarik akan segera tersedia!");
    //         noDataLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
    //         noDataLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
    //         noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
    //         panelPenawaranSpesialContentHolder.add(noDataLabel, BorderLayout.CENTER);
    //     } else {
    //         int maxToShow = Math.min(3, penawaranList.size());
    //        for (int i = 0; i < maxToShow; i++) {
    //            PenawaranModel p = penawaranList.get(i);
    //            panelPenawaranSpesialContentHolder.add(createPenawaranCard(p));
    //        }
    //     }

    //     panelPenawaranSpesialContentHolder.revalidate();
    //     panelPenawaranSpesialContentHolder.repaint();
    // }

    private void loadDestinasiPopuler() {
        panelDestinasiPopulerContentHolder.removeAll();
        List<PaketPerjalananModel> topRated = paketPerjalananController.getTopRatedPakets(3);
        
        if (topRated == null || topRated.isEmpty()) {
            panelDestinasiPopulerContentHolder.setLayout(new BorderLayout());
            JLabel noDataLabel = new JLabel("Tidak ada destinasi populer saat ini.");
            noDataLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            noDataLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelDestinasiPopulerContentHolder.add(noDataLabel, BorderLayout.CENTER);
        } else {
            panelDestinasiPopulerContentHolder.setLayout(new GridLayout(0, 3, 15, 15)); 
            for (PaketPerjalananModel paket : topRated) {
                if (paket != null) { 
                    panelDestinasiPopulerContentHolder.add(createTripPackageCard(paket, false));
                }
            }
        }
        panelDestinasiPopulerContentHolder.revalidate();
        panelDestinasiPopulerContentHolder.repaint();
    }
    
    private void setPlaceholderImage(JLabel label, String text) {
        label.setIcon(null);
        label.setText(text);
        label.setFont(AppTheme.FONT_PRIMARY_DEFAULT); 
        label.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY); 
        label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setOpaque(true); 
    }

    private String getNamaKotaById(int kotaId) {
        KotaDAO kotaDAO = new KotaDAO(); 
        return kotaDAO.getNamaKotaById(kotaId); 
    }

    // private JPanel createPenawaranCard(PenawaranModel penawaran) {
    //     JPanel card = new JPanel();
    //     card.setLayout(new BorderLayout());
    //     card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    //     card.setBackground(Color.WHITE);

    //     JLabel titleLabel = new JLabel(penawaran.getNama());
    //     titleLabel.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
    //     titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    //     JTextArea deskripsiArea = new JTextArea(penawaran.getDeskripsi());
    //     deskripsiArea.setLineWrap(true);
    //     deskripsiArea.setWrapStyleWord(true);
    //     deskripsiArea.setEditable(false);
    //     deskripsiArea.setBackground(null);
    //     deskripsiArea.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
    //     deskripsiArea.setForeground(AppTheme.TEXT_SECONDARY_DARK);

    //     card.add(titleLabel, BorderLayout.NORTH);
    //     card.add(deskripsiArea, BorderLayout.CENTER);

    //     return card;
    // }

    private JPanel createCustomTripCard(CustomTripModel customTrip) {
        // Metode ini mirip dengan createPaketCard, tetapi mengambil data dari CustomTripModel
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setPreferredSize(new Dimension(220, 280)); // Sesuaikan ukurannya
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        // Panel untuk detail teks
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);
        detailPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel namaTrip = new JLabel(customTrip.getNamaTrip());
        namaTrip.setFont(AppTheme.FONT_SUBTITLE);

        JLabel tanggal = new JLabel(customTrip.getTanggalMulai().toString() + " - " + customTrip.getTanggalAkhir().toString());
        tanggal.setFont(AppTheme.FONT_PRIMARY_DEFAULT);

        JLabel harga = new JLabel(AppTheme.formatCurrency(customTrip.getTotalHarga()));
        harga.setFont(AppTheme.FONT_PRIMARY_MEDIUM_BOLD);
        harga.setForeground(AppTheme.ACCENT_ORANGE);

        JButton detailButton = new JButton("Lihat Detail");
        AppTheme.stylePrimaryButton(detailButton, "Lihat Detail");
        detailButton.addActionListener(e -> {
            // Navigasi ke halaman detail pesanan
            // Anda perlu mengambil ReservasiModel yang sesuai untuk custom trip ini
            ReservasiDAO dao = new ReservasiDAO();
            ReservasiModel reservasi = dao.getReservasiByCustomTripId(customTrip.getId());
            if (reservasi != null) {
                mainAppFrame.showPanel(MainAppFrame.PANEL_ORDER_DETAIL, reservasi);
            }
        });

        detailPanel.add(namaTrip);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailPanel.add(tanggal);
        detailPanel.add(Box.createVerticalGlue());
        detailPanel.add(harga);
        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailPanel.add(detailButton);

        card.add(detailPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTripPackageCard(PaketPerjalananModel paket, boolean isPreviousTrip) {
        JPanel cardPanel = new JPanel(new BorderLayout(5, 8)); 
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR.brighter(), 1),
            new EmptyBorder(10,10,10,10)
        ));
        cardPanel.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(180, 120)); 
        setPlaceholderImage(lblImage, "Memuat..."); 
        
        SwingUtilities.invokeLater(() -> {
            final String IMAGE_BASE_PATH = "SharedAppImages/paket_perjalanan/";
            File projectBaseDir = new File(System.getProperty("user.dir")).getParentFile();
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && !gambarRelatif.isEmpty()) {
                if (gambarRelatif.startsWith("/")) gambarRelatif = gambarRelatif.substring(1);
                File gambarFile = new File(projectBaseDir, IMAGE_BASE_PATH + gambarRelatif);
                if (gambarFile.exists()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(gambarFile.getAbsolutePath()).getImage().getScaledInstance(lblImage.getPreferredSize().width, lblImage.getPreferredSize().height, Image.SCALE_SMOOTH));
                    lblImage.setIcon(icon);
                    lblImage.setText("");
                    lblImage.setOpaque(false);
                    lblImage.setBackground(null);
                } else {
                    setPlaceholderImage(lblImage, "Gbr Tdk Ada");
                }
            } else {
                 setPlaceholderImage(lblImage, "Gbr Tdk Ada");
            }
        });
        
        cardPanel.add(lblImage, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(new EmptyBorder(8,0,8,0));

        JLabel lblNamaPaket = new JLabel(paket.getNamaPaket());
        lblNamaPaket.setFont(AppTheme.FONT_SUBTITLE);
        lblNamaPaket.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        lblNamaPaket.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblNamaPaket);

        String kota = paket.getKotaId() > 0 ? getNamaKotaById(paket.getKotaId()) : "N/A"; 
        JLabel lblInfo = new JLabel(String.format("%s - %d hari - %d orang", kota, paket.getDurasi(), paket.getKuota()));
        lblInfo.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblInfo.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.add(lblInfo);

        if (!isPreviousTrip) { 
            JLabel lblRating = new JLabel("Rating: " + paket.getRating() + "/5.0");
            lblRating.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            lblRating.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            lblRating.setAlignmentX(Component.LEFT_ALIGNMENT);
            detailsPanel.add(lblRating);

            JLabel lblHarga = new JLabel("Rp " + String.format("%,.0f", paket.getHarga()));
            lblHarga.setFont(AppTheme.FONT_PRIMARY_BOLD);
            lblHarga.setForeground(AppTheme.ACCENT_ORANGE);
            lblHarga.setAlignmentX(Component.LEFT_ALIGNMENT);
            detailsPanel.add(lblHarga);
        }
        cardPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanelCard = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanelCard.setOpaque(false);
        
        JButton btnDetail = new JButton("Detail");
        styleLinkButton(btnDetail); 
        btnDetail.addActionListener(e -> {
            // int idPaket = paket.getId(); // ambil id paket dari model yang sedang dibuat card-nya
            // TripDetail tripDetailFrame = new TripDetail(idPaket);

            // tripDetailFrame.setVisible(true); 
            mainAppFrame.showPanel(MainAppFrame.PANEL_TRIP_DETAIL, paket, null, null); 
        });
        buttonPanelCard.add(btnDetail);

        if (!isPreviousTrip) {
            JButton btnBooking = new JButton("Booking");
            styleSecondaryButton(btnBooking, "Booking"); 
            btnBooking.addActionListener(e -> {
                mainAppFrame.showPanel(MainAppFrame.PANEL_BOOKING_SCREEN, paket);
            });
            buttonPanelCard.add(btnBooking);
        } else {
             JButton btnPesanLagi = new JButton("Pesan Lagi");
            styleSecondaryButton(btnPesanLagi, "Pesan Lagi");
            btnPesanLagi.addActionListener(e -> {
                 mainAppFrame.showPanel(MainAppFrame.PANEL_BOOKING_SCREEN, paket);
            });
            buttonPanelCard.add(btnPesanLagi);
        }
        cardPanel.add(buttonPanelCard, BorderLayout.SOUTH);
        
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainAppFrame != null) {
                    mainAppFrame.showPanel(MainAppFrame.PANEL_TRIP_DETAIL, paket, null, null);
                }
            }
        });

        return cardPanel;
    }

    private void setupActionListeners(){
        btnTombolCariCepat.addActionListener(this::tombolCariCepatActionPerformed);
        btnCustomTrip.addActionListener(this::btnCustomTripActionPerformed); 
        cmbKotaCariCepat.addActionListener(this::cmbKotaCariCepatActionPerformed); 
        // cmbTravelersCariCepat.addActionListener(this::cmbTravelersCariCepatActionPerformed); // Dihapus
    }

    private void cmbKotaCariCepatActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int selectedIndex = cmbKotaCariCepat.getSelectedIndex();
        if (selectedIndex > 0 && daftarKota != null && selectedIndex <= daftarKota.size() && !cmbKotaCariCepat.getSelectedItem().toString().equals(PLACEHOLDER_KOTA)) {
            cmbKotaCariCepat.setForeground(AppTheme.INPUT_TEXT); 
        } else if (selectedIndex == 0) { 
            cmbKotaCariCepat.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        }
    }                                         

    // Metode cmbTravelersCariCepatActionPerformed dihapus

    private void tombolCariCepatActionPerformed(java.awt.event.ActionEvent evt) {                                             
        boolean valid = true;
        String kotaPilihan = null; 

        // Validasi Kota Tujuan
        if(cmbKotaCariCepat.getSelectedIndex() == 0 || cmbKotaCariCepat.getSelectedItem().toString().equals(PLACEHOLDER_KOTA)) {
            JOptionPane.showMessageDialog(this, "Masukkan Kota Tujuan Anda", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            cmbKotaCariCepat.requestFocus();
            valid = false;
        } else {
            kotaPilihan = cmbKotaCariCepat.getSelectedItem().toString();
        }

        // Ambil tanggal yang dipilih
        java.util.Date tanggalDipilih = dateChooserCariCepat.getDate();

        // Validasi Tanggal
        if(tanggalDipilih == null && valid) {
            JOptionPane.showMessageDialog(this, "Pilih Tanggal Keberangkatan Terlebih Dahulu", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            dateChooserCariCepat.requestFocus();
            valid = false;
        } else if (tanggalDipilih != null && valid) {
            // --- LOGIKA VALIDASI TANGGAL YANG BARU ---
            
            // Konversi java.util.Date ke java.time.LocalDate untuk perbandingan yang lebih mudah
            LocalDate selectedDate = tanggalDipilih.toInstant()
                                                .atZone(java.time.ZoneId.systemDefault())
                                                .toLocalDate();
            LocalDate today = LocalDate.now();

        }
        
        // Jika semua validasi lolos, lanjutkan pencarian
        if (valid) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggalStr = sdf.format(dateChooserCariCepat.getDate());

            System.out.println("Pencarian Cepat:");
            System.out.println("Kota Tujuan: " + kotaPilihan); 
            System.out.println("Tanggal: " + tanggalStr);

            mainAppFrame.showSearchResultPanel(kotaPilihan, tanggalStr);
        }
    }                                          

    private void btnCustomTripActionPerformed(java.awt.event.ActionEvent evt) {                                               
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_DESTINATION_STEP);
        } else {
            System.err.println("MainAppFrame reference is null in PanelBeranda. Cannot navigate to Custom Trip.");
        }
    }                                              
    
    private void styleDateChooser(JDateChooser dateChooser) {
        if (dateChooser == null) return;
        dateChooser.setFont(AppTheme.FONT_TEXT_FIELD);
        dateChooser.setPreferredSize(new Dimension(dateChooser.getPreferredSize().width, 30)); 
        
        dateChooser.getCalendarButton().setFont(AppTheme.FONT_BUTTON);
        dateChooser.getCalendarButton().setBackground(AppTheme.PRIMARY_BLUE_LIGHT);
        dateChooser.getCalendarButton().setForeground(AppTheme.TEXT_WHITE);
        dateChooser.getCalendarButton().setFocusPainted(false);
        dateChooser.getCalendarButton().setBorder(BorderFactory.createEmptyBorder(2,5,2,5)); 
        Dimension buttonSize = new Dimension(dateChooser.getCalendarButton().getPreferredSize().width, 26); 
        dateChooser.getCalendarButton().setPreferredSize(buttonSize);
        dateChooser.getCalendarButton().setMaximumSize(buttonSize);
        dateChooser.getCalendarButton().setMinimumSize(buttonSize);

        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setFont(AppTheme.FONT_TEXT_FIELD);
        editor.setBackground(AppTheme.INPUT_BACKGROUND);
        editor.setForeground(AppTheme.INPUT_TEXT); 
        editor.setBorder(AppTheme.createDefaultInputBorder());
        editor.setEditable(false); 

        dateChooser.getDateEditor().addPropertyChangeListener("date", pcevt -> {
            Date newDate = (Date) pcevt.getNewValue();
            if (newDate == null) {
            } else {
                editor.setForeground(AppTheme.INPUT_TEXT);
            }
            editor.setBorder(AppTheme.createDefaultInputBorder());
        });
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setForeground(AppTheme.INPUT_TEXT); 
        comboBox.setBorder(AppTheme.createDefaultInputBorder());
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 30)); 
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                comboBox.setBorder(AppTheme.createFocusBorder());
            }
            @Override
            public void focusLost(FocusEvent e) {
                comboBox.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }
    
    private void stylePrimaryButton(JButton button, String text) { 
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); 
        button.setOpaque(true); 
        button.setBorderPainted(false); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Color originalBg = AppTheme.BUTTON_PRIMARY_BACKGROUND;
        Color hoverBg = originalBg.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
    }

    private void styleSecondaryButton(JButton button, String text) { 
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color originalBg = AppTheme.BUTTON_SECONDARY_BACKGROUND;
        Color hoverBg = originalBg.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
    }
    
    private void styleLinkButton(JButton button) { 
        button.setFont(AppTheme.FONT_LINK_BUTTON);
        button.setForeground(AppTheme.BUTTON_LINK_FOREGROUND);
        button.setBackground(Color.WHITE); 
        button.setOpaque(false); 
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color originalFg = AppTheme.BUTTON_LINK_FOREGROUND;
        Color hoverFg = AppTheme.ACCENT_ORANGE; 
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(hoverFg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(originalFg);
            }
        });
    }

    private class PlaceholderComboBoxRenderer extends BasicComboBoxRenderer {
        private String placeholder;
        public PlaceholderComboBoxRenderer(String placeholder) {
            this.placeholder = placeholder;
        }
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String textValue = (value == null) ? "" : value.toString(); 

            if (value == null || textValue.equals(placeholder)) { 
                if (index == -1) { 
                    setText(placeholder);
                    setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                } else { 
                    setText("  " + placeholder); 
                    setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                }
            } else {
                setText("  " + textValue); 
                setForeground(isSelected ? list.getSelectionForeground() : AppTheme.INPUT_TEXT);
            }
            if (!isSelected) { 
                 setBackground(AppTheme.INPUT_BACKGROUND);
            } else {
                 setBackground(list.getSelectionBackground()); 
            }
            setFont(AppTheme.FONT_TEXT_FIELD);
            return this;
        }
    }
}