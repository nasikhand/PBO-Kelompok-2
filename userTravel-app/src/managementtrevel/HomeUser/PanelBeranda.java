package managementtrevel.HomeUser;

import Asset.AppTheme;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import controller.CariCepatController;
import controller.PaketPerjalananController;
import db.Koneksi;
import db.dao.KotaDAO; // Diasumsikan Anda memiliki ini
// import db.dao.PenawaranSpesialDAO; // Untuk implementasi nanti
// import db.dao.PerjalananSebelumnyaDAO; // Untuk implementasi nanti
import managementtrevel.MainAppFrame;
import managementtrevel.SearchResultScreen.SearchResult;
import managementtrevel.TripDetailScreen.TripDetail;
import model.DestinasiModel;
import model.PaketPerjalananModel;
// import model.PenawaranSpesialModel; // Untuk implementasi nanti
// import model.PerjalananSebelumnyaModel; // Untuk implementasi nanti
import model.Session;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PanelBeranda extends JPanel {

    private MainAppFrame mainAppFrame;
    private List<DestinasiModel> daftarDestinasi;
    private Date tanggalDipilihCariCepat;
    private String jumlahTravelerDipilihCariCepat = null;
    private PaketPerjalananController paketPerjalananController;
    // private PenawaranSpesialDAO penawaranSpesialDAO; // Untuk nanti
    // private PerjalananSebelumnyaDAO perjalananSebelumnyaDAO; // Untuk nanti


    private final String PLACEHOLDER_DESTINASI = "-- Pilih Destinasi --";
    private final String PLACEHOLDER_TRAVELERS = "Travelers";

    // Komponen UI Utama
    private JLabel labelNama;
    private JPanel panelCariCepat;
    private JComboBox<String> cmbDestinasiCariCepat;
    private JDateChooser dateChooserCariCepat;
    private JComboBox<String> cmbTravelersCariCepat;
    private JButton btnTombolCariCepat;
    private JButton btnCustomTrip;

    private JPanel panelPerjalananSebelumnyaContent;
    private JPanel panelPenawaranSpesialContent;
    private JPanel panelDestinasiPopulerContent;


    public PanelBeranda(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        // Inisialisasi DAO jika sudah ada
        // this.penawaranSpesialDAO = new PenawaranSpesialDAO();
        // this.perjalananSebelumnyaDAO = new PerjalananSebelumnyaDAO();

        Connection conn = Koneksi.getConnection(); // Pastikan Koneksi.getConnection() berfungsi
        if (conn == null) {
            System.err.println("Koneksi database gagal di PanelBeranda.");
            // Handle error koneksi, mungkin tampilkan pesan ke user
        }
        this.paketPerjalananController = new PaketPerjalananController(conn);


        initializeUIStructure();
        applyAppTheme();
        populateCariCepat();
        loadDynamicContent();
        setupActionListeners();
    }

    private void initializeUIStructure() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(15, 20, 15, 20)); // Padding luar

        // === Bagian Atas: Welcome & Cari Cepat ===
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);

        labelNama = new JLabel("Selamat Datang, Tamu"); // Default text
        topPanel.add(labelNama, BorderLayout.NORTH);

        panelCariCepat = createSearchPanel();
        topPanel.add(panelCariCepat, BorderLayout.CENTER);

        this.add(topPanel, BorderLayout.NORTH);

        // === Bagian Tengah: Konten Utama dengan Scroll ===
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(AppTheme.PANEL_BACKGROUND); // Atau Color.WHITE jika ingin kontras

        // Section: Perjalanan Sebelumnya
        panelPerjalananSebelumnyaContent = createSectionCard("Perjalanan Sebelumnya");
        mainContentPanel.add(panelPerjalananSebelumnyaContent);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spasi antar section

        // Section: Penawaran Spesial
        panelPenawaranSpesialContent = createSectionCard("Penawaran Spesial");
        mainContentPanel.add(panelPenawaranSpesialContent);
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Section: Destinasi Populer
        panelDestinasiPopulerContent = createSectionCard("Destinasi Populer");
        mainContentPanel.add(panelDestinasiPopulerContent);
        mainContentPanel.add(Box.createVerticalGlue()); // Mendorong konten ke atas jika ruang berlebih

        JScrollPane mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.getViewport().setBackground(AppTheme.PANEL_BACKGROUND);


        this.add(mainScrollPane, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); // Transparan terhadap background topPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCariCepat = new JLabel("Cari Cepat");
        // Styling untuk lblCariCepat akan di applyAppTheme

        cmbDestinasiCariCepat = new JComboBox<>();
        dateChooserCariCepat = new JDateChooser();
        cmbTravelersCariCepat = new JComboBox<>();
        btnTombolCariCepat = new JButton("Cari");
        btnCustomTrip = new JButton("Custom Trip");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 5; gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblCariCepat, gbc);
        gbc.gridwidth = 1; // Reset gridwidth

        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0.3; panel.add(cmbDestinasiCariCepat, gbc);
        gbc.gridx = 1; gbc.weightx = 0.2; panel.add(dateChooserCariCepat, gbc);
        gbc.gridx = 2; gbc.weightx = 0.2; panel.add(cmbTravelersCariCepat, gbc);
        gbc.gridx = 3; gbc.weightx = 0.1; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        panel.add(btnTombolCariCepat, gbc);
        gbc.gridx = 4; gbc.weightx = 0.1; panel.add(btnCustomTrip, gbc);
        
        return panel;
    }
    
    private JPanel createSectionCard(String title) {
        JPanel sectionPanel = new JPanel(new BorderLayout(0,10));
        sectionPanel.setBackground(Color.WHITE); // Kartu putih
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            new EmptyBorder(15,15,15,15)
        ));

        JLabel titleLabel = new JLabel(title);
        // Font dan warna akan diatur di applyAppTheme
        sectionPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel contentHolder = new JPanel(); 
        contentHolder.setLayout(new BoxLayout(contentHolder, BoxLayout.Y_AXIS)); 
        contentHolder.setOpaque(false);
        sectionPanel.add(contentHolder, BorderLayout.CENTER);

        return sectionPanel;
    }


    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        // Welcome Label
        labelNama.setFont(AppTheme.FONT_TITLE_LARGE);
        labelNama.setForeground(AppTheme.TEXT_DARK);
        labelNama.setBorder(new EmptyBorder(0, 0, 10, 0)); 

        // Search Panel
        Component lblCariCepatComp = panelCariCepat.getComponent(0); // Asumsi lblCariCepat adalah komponen pertama
        if (lblCariCepatComp instanceof JLabel) { 
            JLabel lblCariCepat = (JLabel) lblCariCepatComp;
            lblCariCepat.setFont(AppTheme.FONT_SUBTITLE);
            lblCariCepat.setForeground(AppTheme.TEXT_DARK);
        }
        styleComboBox(cmbDestinasiCariCepat);
        styleDateChooser(dateChooserCariCepat);
        styleComboBox(cmbTravelersCariCepat);
        stylePrimaryButton(btnTombolCariCepat, "Cari");
        styleSecondaryButton(btnCustomTrip, "Custom Trip");

        // Styling untuk judul section di createSectionCard
        if (panelPerjalananSebelumnyaContent.getComponent(0) instanceof JLabel) {
            JLabel title = (JLabel) panelPerjalananSebelumnyaContent.getComponent(0);
            title.setFont(AppTheme.FONT_TITLE_MEDIUM);
            title.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            title.setBorder(new EmptyBorder(0,0,10,0));
        }
        if (panelPenawaranSpesialContent.getComponent(0) instanceof JLabel) {
             JLabel title = (JLabel) panelPenawaranSpesialContent.getComponent(0);
            title.setFont(AppTheme.FONT_TITLE_MEDIUM);
            title.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            title.setBorder(new EmptyBorder(0,0,10,0));
        }
        if (panelDestinasiPopulerContent.getComponent(0) instanceof JLabel) {
             JLabel title = (JLabel) panelDestinasiPopulerContent.getComponent(0);
            title.setFont(AppTheme.FONT_TITLE_MEDIUM);
            title.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            title.setBorder(new EmptyBorder(0,0,10,0));
        }
    }

    private void populateCariCepat() {
        Connection conn = Koneksi.getConnection();
        if (conn == null) return; 
        CariCepatController controller = new CariCepatController(conn);
        this.daftarDestinasi = controller.getDaftarDestinasi();

        cmbDestinasiCariCepat.removeAllItems();
        cmbDestinasiCariCepat.addItem(PLACEHOLDER_DESTINASI);
        if (this.daftarDestinasi != null) {
            for (DestinasiModel dest : daftarDestinasi) {
                cmbDestinasiCariCepat.addItem(dest.getNamaDestinasi());
            }
        }
        cmbDestinasiCariCepat.setRenderer(new PlaceholderComboBoxRenderer(PLACEHOLDER_DESTINASI));
        cmbDestinasiCariCepat.setSelectedIndex(0);

        cmbTravelersCariCepat.removeAllItems();
        cmbTravelersCariCepat.addItem(PLACEHOLDER_TRAVELERS);
        for (int i = 1; i <= 10; i++) { 
            cmbTravelersCariCepat.addItem(i + " Orang");
        }
        cmbTravelersCariCepat.setRenderer(new PlaceholderComboBoxRenderer(PLACEHOLDER_TRAVELERS));
        cmbTravelersCariCepat.setSelectedIndex(0);

        dateChooserCariCepat.setDate(null); 
        dateChooserCariCepat.setMinSelectableDate(new Date()); 
    }
    
    private void loadDynamicContent() {
        loadPerjalananSebelumnya();
        loadPenawaranSpesial();
        loadDestinasiPopuler();
    }

    private void loadPerjalananSebelumnya() {
        JPanel contentHolder = (JPanel) panelPerjalananSebelumnyaContent.getComponent(1); 
        contentHolder.removeAll(); 

        JLabel noDataLabel = new JLabel("Fitur Perjalanan Sebelumnya akan segera hadir.");
        noDataLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        noDataLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentHolder.setLayout(new BorderLayout()); 
        contentHolder.add(noDataLabel, BorderLayout.CENTER);
        
        panelPerjalananSebelumnyaContent.revalidate();
        panelPerjalananSebelumnyaContent.repaint();
    }

    private void loadPenawaranSpesial() {
        JPanel contentHolder = (JPanel) panelPenawaranSpesialContent.getComponent(1);
        contentHolder.removeAll();
        
        JLabel noDataLabel = new JLabel("Penawaran spesial menarik akan segera tersedia!");
        noDataLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        noDataLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentHolder.setLayout(new BorderLayout());
        contentHolder.add(noDataLabel, BorderLayout.CENTER);
        
        panelPenawaranSpesialContent.revalidate();
        panelPenawaranSpesialContent.repaint();
    }

    private void loadDestinasiPopuler() {
        JPanel contentHolder = (JPanel) panelDestinasiPopulerContent.getComponent(1);
        contentHolder.removeAll();
        
        List<PaketPerjalananModel> topRated = paketPerjalananController.getTopRatedPakets(3);
        if (topRated == null || topRated.isEmpty()) {
            JLabel noDataLabel = new JLabel("Tidak ada destinasi populer saat ini.");
            noDataLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            noDataLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentHolder.setLayout(new BorderLayout());
            contentHolder.add(noDataLabel, BorderLayout.CENTER);
        } else {
             contentHolder.setLayout(new GridLayout(0, 3, 15, 15)); 
            for (PaketPerjalananModel paket : topRated) {
                if (paket != null) { 
                    contentHolder.add(createTripPackageCard(paket, false));
                }
            }
        }
        panelDestinasiPopulerContent.revalidate();
        panelDestinasiPopulerContent.repaint();
    }
    
    // Metode yang hilang ditambahkan kembali
    private void setPlaceholderImage(JLabel label, String text) {
        label.setIcon(null);
        label.setText(text);
        label.setFont(AppTheme.FONT_PRIMARY_DEFAULT); // Atur font placeholder
        label.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY); // Latar belakang abu-abu muda untuk placeholder
        label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setOpaque(true); // Pastikan background terlihat
    }

    // Metode yang hilang ditambahkan kembali
    private String getNamaKotaById(int kotaId) {
        // Anda perlu memastikan KotaDAO diinisialisasi atau bisa diakses di sini
        // Jika KotaDAO memerlukan koneksi, pastikan itu tersedia.
        // Untuk contoh ini, saya buat instance baru, tapi ini mungkin tidak efisien.
        // Pertimbangkan untuk membuat UserDAO sebagai field instance jika sering digunakan.
        KotaDAO kotaDAO = new KotaDAO(); 
        return kotaDAO.getNamaKotaById(kotaId); 
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
            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir);
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && !gambarRelatif.isEmpty()) {
                if (gambarRelatif.startsWith("/")) gambarRelatif = gambarRelatif.substring(1);
                File gambarFile = new File(baseDir, gambarRelatif);
                if (gambarFile.exists()) {
                    ImageIcon icon = new ImageIcon(new ImageIcon(gambarFile.getAbsolutePath()).getImage().getScaledInstance(lblImage.getPreferredSize().width, lblImage.getPreferredSize().height, Image.SCALE_SMOOTH));
                    lblImage.setIcon(icon);
                    lblImage.setText("");
                    lblImage.setOpaque(false);
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
        JLabel lblInfo = new JLabel(String.format("%s - %d hari", kota, paket.getDurasi()));
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        
        JButton btnDetail = new JButton("Detail");
        styleLinkButton(btnDetail); 
        btnDetail.addActionListener(e -> {
            // Perubahan di sini: Panggil konstruktor default TripDetail
            TripDetail tripDetailFrame = new TripDetail();
            // Anda perlu cara untuk mengirim data 'paket' ke tripDetailFrame,
            // misalnya: tripDetailFrame.loadData(paket);
            // Untuk sekarang, kita hanya menampilkannya.
            // Jika TripDetail adalah JPanel yang dikelola MainAppFrame, navigasinya akan berbeda.
            tripDetailFrame.setVisible(true); 
        });
        buttonPanel.add(btnDetail);

        if (!isPreviousTrip) {
            JButton btnBooking = new JButton("Booking");
            styleSecondaryButton(btnBooking, "Booking"); 
            btnBooking.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Tombol Booking Cepat untuk '" + paket.getNamaPaket() + "' diklik.", "Info Booking", JOptionPane.INFORMATION_MESSAGE);
            });
            buttonPanel.add(btnBooking);
        } else {
             JButton btnPesanLagi = new JButton("Pesan Lagi");
            styleSecondaryButton(btnPesanLagi, "Pesan Lagi");
            btnPesanLagi.addActionListener(e -> {
                 JOptionPane.showMessageDialog(this, "Fitur 'Pesan Lagi' untuk '" + paket.getNamaPaket() + "' belum diimplementasikan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            });
            buttonPanel.add(btnPesanLagi);
        }
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Perubahan di sini: Panggil konstruktor default TripDetail
                TripDetail tripDetailFrame = new TripDetail();
                // Anda perlu cara untuk mengirim data 'paket' ke tripDetailFrame
                // tripDetailFrame.loadData(paket);
                tripDetailFrame.setVisible(true); 
            }
        });

        return cardPanel;
    }

    private void setupActionListeners(){
        btnTombolCariCepat.addActionListener(this::tombolCariCepatActionPerformed);
        btnCustomTrip.addActionListener(this::btnCustomTripActionPerformed); 
        cmbDestinasiCariCepat.addActionListener(this::cmbDestinasiCariCepatActionPerformed);
        cmbTravelersCariCepat.addActionListener(this::cmbTravelersCariCepatActionPerformed);
    }

    private void cmbDestinasiCariCepatActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int selectedIndex = cmbDestinasiCariCepat.getSelectedIndex();
        if (selectedIndex > 0 && daftarDestinasi != null && selectedIndex <= daftarDestinasi.size() && !cmbDestinasiCariCepat.getSelectedItem().toString().equals(PLACEHOLDER_DESTINASI)) {
            cmbDestinasiCariCepat.setForeground(AppTheme.INPUT_TEXT); 
        } else if (selectedIndex == 0) { 
            cmbDestinasiCariCepat.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        }
    }                                         

    private void cmbTravelersCariCepatActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        int selectedIndex = cmbTravelersCariCepat.getSelectedIndex();
        if (selectedIndex > 0 && !cmbTravelersCariCepat.getSelectedItem().toString().equals(PLACEHOLDER_TRAVELERS)) {
            jumlahTravelerDipilihCariCepat = cmbTravelersCariCepat.getSelectedItem().toString();
            cmbTravelersCariCepat.setForeground(AppTheme.INPUT_TEXT); 
        } else {
            jumlahTravelerDipilihCariCepat = null;
            if (selectedIndex == 0) { 
                 cmbTravelersCariCepat.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
            }
        }
    }                                                

    private void tombolCariCepatActionPerformed(java.awt.event.ActionEvent evt) {                                           
        boolean valid = true;
        String destinasiPilihan = null;
        if(cmbDestinasiCariCepat.getSelectedIndex() == 0 || cmbDestinasiCariCepat.getSelectedItem().toString().equals(PLACEHOLDER_DESTINASI)) {
            JOptionPane.showMessageDialog(this, "Masukkan Destinasi Anda", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            cmbDestinasiCariCepat.requestFocus();
            cmbDestinasiCariCepat.setBorder(AppTheme.createFocusBorder()); 
            valid = false;
        } else {
            destinasiPilihan = cmbDestinasiCariCepat.getSelectedItem().toString();
            cmbDestinasiCariCepat.setBorder(AppTheme.createDefaultInputBorder());
        }

        if(dateChooserCariCepat.getDate() == null && valid) {
            JOptionPane.showMessageDialog(this, "Pilih Tanggal Terlebih Dahulu", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            dateChooserCariCepat.requestFocus();
            ((JTextFieldDateEditor) dateChooserCariCepat.getDateEditor()).setBorder(AppTheme.createFocusBorder());
            valid = false;
        } else if (dateChooserCariCepat.getDate() != null) {
             ((JTextFieldDateEditor) dateChooserCariCepat.getDateEditor()).setBorder(AppTheme.createDefaultInputBorder());
        }

        if((cmbTravelersCariCepat.getSelectedIndex() == 0 || cmbTravelersCariCepat.getSelectedItem().toString().equals(PLACEHOLDER_TRAVELERS)) && valid){
            JOptionPane.showMessageDialog(this,"Silahkan Pilih Jumlah Travelers", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            cmbTravelersCariCepat.requestFocus();
            cmbTravelersCariCepat.setBorder(AppTheme.createFocusBorder());
            valid = false;
        } else if (cmbTravelersCariCepat.getSelectedIndex() != 0) {
            cmbTravelersCariCepat.setBorder(AppTheme.createDefaultInputBorder());
        }
        
        if (valid) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String tanggalStr = sdf.format(dateChooserCariCepat.getDate());
            String travelers = jumlahTravelerDipilihCariCepat;

            System.out.println("Pencarian Cepat:");
            System.out.println("Destinasi: " + destinasiPilihan);
            System.out.println("Tanggal: " + tanggalStr);
            System.out.println("Travelers: " + travelers);

            SearchResult hasil = new SearchResult(destinasiPilihan, tanggalStr, travelers);
            hasil.setVisible(true);
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
        dateChooser.setPreferredSize(new Dimension(dateChooser.getPreferredSize().width, 30)); // Samakan tinggi dengan ComboBox
        
        dateChooser.getCalendarButton().setFont(AppTheme.FONT_BUTTON);
        dateChooser.getCalendarButton().setBackground(AppTheme.PRIMARY_BLUE_LIGHT);
        dateChooser.getCalendarButton().setForeground(AppTheme.TEXT_WHITE);
        dateChooser.getCalendarButton().setFocusPainted(false);
        dateChooser.getCalendarButton().setBorder(BorderFactory.createEmptyBorder(2,5,2,5)); // Padding tombol kalender
        // Atur tinggi tombol kalender agar konsisten
        Dimension buttonSize = new Dimension(dateChooser.getCalendarButton().getPreferredSize().width, 26); // Tinggi tombol disesuaikan
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
                // JDateChooser biasanya menampilkan format tanggalnya sebagai placeholder
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
            if (value == null || value.toString().equals(placeholder)) {
                if (index == -1) { 
                    setText(placeholder);
                    setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                } else { 
                    setText("  " + placeholder); 
                    setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                }
            } else {
                setText("  " + value.toString()); 
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
