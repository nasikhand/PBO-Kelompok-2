package managementtrevel.TripDetailScreen;

import Asset.AppTheme; 
import managementtrevel.MainAppFrame; 
import model.PaketPerjalananModel; 
import db.dao.KotaDAO; 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.border.MatteBorder;

public class PanelTripDetail extends JPanel {

    private MainAppFrame mainAppFrame;
    private PaketPerjalananModel currentPaket; 
    private String originalSearchNamaKota;    
    private String originalSearchTanggal; 

    // Komponen Utama UI (dibuat programatik)
    private JLabel lblNamaPaket;
    private JLabel lblKotaTujuan;
    private JLabel lblTanggalTrip;
    private JLabel lblHargaPaket;
    private JLabel lblGambarUtamaPaket;
    private JTextArea taDeskripsiPaket;
    private JLabel lblKuota;
    private JLabel lblStatusPaket;
    private JLabel lblRatingPaket;
    private JPanel panelGallery;
    private JPanel panelItineraryItems;
    private JButton btnBookSekarang;
    private JButton btnKembali;

    public PanelTripDetail(MainAppFrame mainAppFrame, PaketPerjalananModel paket, String originalSearchNamaKota, String originalSearchTanggal) {
        this.mainAppFrame = mainAppFrame;
        this.currentPaket = paket;
        this.originalSearchNamaKota = originalSearchNamaKota;
        this.originalSearchTanggal = originalSearchTanggal;
        
        initializeUIProgrammatically(); 
        applyAppTheme();
        loadTripData(); 
        setupActionListeners();
    }
    
    private PanelTripDetail() { 
        this(null, null, null, null); 
        System.err.println("PERINGATAN: Konstruktor default PanelTripDetail dipanggil. Data tidak akan dimuat dengan benar.");
    }

    private void initializeUIProgrammatically() {
        setLayout(new BorderLayout(0, 0)); // Gap 0 karena padding akan diatur manual

        // 1. Header Panel (Tombol Kembali dan Judul Paket)
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setOpaque(false);

        btnKembali = new JButton("< Kembali");
        headerPanel.add(btnKembali, BorderLayout.WEST);

        lblNamaPaket = new JLabel("Nama Paket Perjalanan Detail");
        lblNamaPaket.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblNamaPaket, BorderLayout.CENTER);
        // Tambahkan komponen dummy di timur untuk membantu centering judul jika perlu
        JLabel dummyLabel = new JLabel();
        dummyLabel.setPreferredSize(btnKembali.getPreferredSize()); // Samakan ukuran dengan tombol kembali
        headerPanel.add(dummyLabel, BorderLayout.EAST);


        // 2. Main Content Panel (yang akan di-scroll)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new GridBagLayout());
        mainContent.setOpaque(false); // Agar background PanelTripDetail terlihat
        mainContent.setBorder(new EmptyBorder(0, 20, 0, 20)); // Padding kiri-kanan untuk konten

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0); // Jarak vertikal antar section
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // 2a. Gambar Utama Paket
        lblGambarUtamaPaket = new JLabel("Memuat Gambar...", SwingConstants.CENTER);
        lblGambarUtamaPaket.setPreferredSize(new Dimension(600, 300)); // Ukuran gambar utama
        lblGambarUtamaPaket.setOpaque(true); // Agar background placeholder terlihat
        mainContent.add(lblGambarUtamaPaket, gbc);
        gbc.gridy++;

        // 2b. Info Singkat (Kota, Tanggal, Durasi, Kuota, Status, Rating)
        JPanel shortInfoPanel = new JPanel(new GridLayout(0, 2, 15, 5)); // 2 kolom, gap
        shortInfoPanel.setOpaque(false);
        lblKotaTujuan = createInfoLabel("Kota Tujuan: ");
        lblTanggalTrip = createInfoLabel("Tanggal: ");
        // lblDurasiPaket = createInfoLabel("Durasi: "); // Akan di-set di loadTripData
        lblKuota = createInfoLabel("Kuota: ");
        lblStatusPaket = createInfoLabel("Status: ");
        lblRatingPaket = createInfoLabel("Rating: ");
        
        shortInfoPanel.add(lblKotaTujuan);
        shortInfoPanel.add(lblTanggalTrip);
        // shortInfoPanel.add(lblDurasiPaket);
        shortInfoPanel.add(lblKuota);
        shortInfoPanel.add(lblStatusPaket);
        shortInfoPanel.add(lblRatingPaket);
        gbc.insets = new Insets(15, 0, 15, 0);
        mainContent.add(shortInfoPanel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 10, 0);


        // 2c. Panel Galeri (Opsional)
        panelGallery = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelGallery.setOpaque(false);
        // Nanti di loadTripData, kita tambahkan gambar-gambar kecil ke sini
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(panelGallery, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;


        // 2d. Tentang Paket (Deskripsi)
        JPanel tentangPanel = createTitledSection("Tentang Paket Ini");
        taDeskripsiPaket = new JTextArea(5, 30);
        taDeskripsiPaket.setLineWrap(true);
        taDeskripsiPaket.setWrapStyleWord(true);
        taDeskripsiPaket.setEditable(false);
        JScrollPane scrollDeskripsi = new JScrollPane(taDeskripsiPaket);
        scrollDeskripsi.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tentangPanel.add(scrollDeskripsi, BorderLayout.CENTER);
        mainContent.add(tentangPanel, gbc);
        gbc.gridy++;

        // 2e. Itinerary
        JPanel itineraryPanel = createTitledSection("Rencana Perjalanan (Itinerary)");
        panelItineraryItems = new JPanel();
        panelItineraryItems.setLayout(new BoxLayout(panelItineraryItems, BoxLayout.Y_AXIS));
        panelItineraryItems.setOpaque(false);
        // Nanti di loadTripData, kita tambahkan item-item itinerary ke sini
        itineraryPanel.add(new JScrollPane(panelItineraryItems), BorderLayout.CENTER); // Buat itinerary scrollable
        mainContent.add(itineraryPanel, gbc);
        gbc.gridy++;

        // Komponen dummy untuk mendorong konten ke atas
        gbc.weighty = 1.0;
        mainContent.add(new JLabel(), gbc);


        JScrollPane mainScrollPane = new JScrollPane(mainContent);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setOpaque(false);
        mainScrollPane.getViewport().setOpaque(false);

        // 3. Footer Panel (Harga dan Tombol Book)
        JPanel footerPanel = new JPanel(new BorderLayout(10,0));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        footerPanel.setOpaque(false);

        lblHargaPaket = new JLabel("Rp 0");
        btnBookSekarang = new JButton("Book Sekarang");
        
        footerPanel.add(lblHargaPaket, BorderLayout.CENTER);
        footerPanel.add(btnBookSekarang, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(mainScrollPane, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createInfoLabel(String prefix) {
        JLabel label = new JLabel(prefix + "-");
        return label;
    }

    private JPanel createTitledSection(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false); // Agar background mainContent terlihat
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.FONT_SUBTITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        titleLabel.setBorder(new EmptyBorder(0,0,5,0)); // Margin bawah untuk judul section
        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }


    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        
        // Header
        if (btnKembali != null) styleSecondaryButton(btnKembali, "< Kembali");
        if (lblNamaPaket != null) {
            lblNamaPaket.setFont(AppTheme.FONT_TITLE_LARGE);
            lblNamaPaket.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }

        // Gambar Utama
        if (lblGambarUtamaPaket != null) {
            lblGambarUtamaPaket.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1));
            lblGambarUtamaPaket.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        }

        // Info Singkat
        Font infoFont = AppTheme.FONT_PRIMARY_MEDIUM;
        Color infoColor = AppTheme.TEXT_DARK;
        if(lblKotaTujuan != null) { lblKotaTujuan.setFont(infoFont); lblKotaTujuan.setForeground(infoColor); }
        if(lblTanggalTrip != null) { lblTanggalTrip.setFont(infoFont); lblTanggalTrip.setForeground(infoColor); }
        if(lblKuota != null) { lblKuota.setFont(infoFont); lblKuota.setForeground(infoColor); }
        if(lblStatusPaket != null) { lblStatusPaket.setFont(infoFont); lblStatusPaket.setForeground(infoColor); }
        if(lblRatingPaket != null) { lblRatingPaket.setFont(infoFont); lblRatingPaket.setForeground(infoColor); }

        // Tentang Paket
        if (taDeskripsiPaket != null) {
            taDeskripsiPaket.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            taDeskripsiPaket.setForeground(AppTheme.TEXT_DARK);
            taDeskripsiPaket.setBackground(AppTheme.INPUT_BACKGROUND); 
            taDeskripsiPaket.setBorder(new EmptyBorder(5,8,5,8));
            JScrollPane parentScrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, taDeskripsiPaket);
            if (parentScrollPane != null) {
                parentScrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
                parentScrollPane.getViewport().setBackground(AppTheme.INPUT_BACKGROUND);
            }
        }
        
        // Itinerary - styling untuk panelItineraryItems dan item-item di dalamnya akan di handle saat item dibuat

        // Footer
        if (lblHargaPaket != null) {
            lblHargaPaket.setFont(AppTheme.FONT_TITLE_MEDIUM); // FONT_TITLE_MEDIUM_BOLD perlu ada di AppTheme
            lblHargaPaket.setForeground(AppTheme.ACCENT_ORANGE);
        }
        if (btnBookSekarang != null) stylePrimaryButton(btnBookSekarang, "Book Sekarang");
    }
    
    private void loadTripData() {
        if (currentPaket != null) {
            if (lblNamaPaket != null) lblNamaPaket.setText(currentPaket.getNamaPaket());
            
            KotaDAO kotaDAO = new KotaDAO(); // Sebaiknya di-inject atau field
            String namaKota = currentPaket.getKotaId() > 0 ? kotaDAO.getNamaKotaById(currentPaket.getKotaId()) : "N/A";
            long durasi = currentPaket.getDurasi();

            if (lblKotaTujuan != null) lblKotaTujuan.setText("<html><b>Kota Tujuan:</b> " + namaKota + "</html>");
            if (lblTanggalTrip != null) lblTanggalTrip.setText(String.format("<html><b>Tanggal:</b> %s s/d %s (%d hari)</html>", currentPaket.getTanggalMulai(), currentPaket.getTanggalAkhir(), durasi));
            if (lblKuota != null) lblKuota.setText("<html><b>Kuota:</b> " + currentPaket.getKuota() + " orang</html>");
            if (lblStatusPaket != null) {
                 lblStatusPaket.setText("<html><b>Status:</b> " + currentPaket.getStatus() + "</html>");
                 // Beri warna berdasarkan status
                if ("tersedia".equalsIgnoreCase(currentPaket.getStatus())) {
                    lblStatusPaket.setForeground(AppTheme.TEXT_DARK); // Perlu TEXT_SUCCESS di AppTheme (misal: hijau)
                } else if ("penuh".equalsIgnoreCase(currentPaket.getStatus()) || "selesai".equalsIgnoreCase(currentPaket.getStatus())) {
                    lblStatusPaket.setForeground(AppTheme.TEXT_DARK); // Perlu TEXT_ERROR di AppTheme (misal: merah)
                }
            }
            if (lblRatingPaket != null) lblRatingPaket.setText("<html><b>Rating:</b> " + currentPaket.getRating() + "/5.0</html>");
            if (lblHargaPaket != null) lblHargaPaket.setText("Rp " + String.format("%,.0f", currentPaket.getHarga()));
            if (taDeskripsiPaket != null) taDeskripsiPaket.setText(currentPaket.getDeskripsi());


            if (lblGambarUtamaPaket != null) { 
                String gambarPath = currentPaket.getGambar();
                if (gambarPath != null && !gambarPath.isEmpty()) {
                    SwingUtilities.invokeLater(() -> { 
                        String userDir = System.getProperty("user.dir");
                        File baseDir = new File(userDir);
                        String gambarRelatif = gambarPath;
                        if (gambarRelatif.startsWith("/") || gambarRelatif.startsWith("\\")) gambarRelatif = gambarRelatif.substring(1);
                        File imageFile = new File(baseDir, gambarRelatif);
                         System.out.println("[PanelTripDetail] Mencoba memuat gambar utama: " + imageFile.getAbsolutePath());
                        if (imageFile.exists()) {
                            try {
                                ImageIcon icon = new ImageIcon(new ImageIcon(imageFile.toURI().toURL()).getImage().getScaledInstance(lblGambarUtamaPaket.getWidth() > 0 ? lblGambarUtamaPaket.getWidth() : 600, lblGambarUtamaPaket.getHeight() > 0 ? lblGambarUtamaPaket.getHeight() : 300, Image.SCALE_SMOOTH));
                                lblGambarUtamaPaket.setIcon(icon);
                                lblGambarUtamaPaket.setText("");
                                lblGambarUtamaPaket.setOpaque(false);
                            } catch (Exception e) {
                                System.err.println("Error saat memuat gambar utama: " + e.getMessage());
                                styleImagePlaceholder(lblGambarUtamaPaket, "Gbr Error");
                            }
                        } else {
                             System.err.println("File gambar utama tidak ditemukan: " + imageFile.getAbsolutePath());
                             styleImagePlaceholder(lblGambarUtamaPaket, "Gambar Tidak Tersedia");
                        }
                    });
                } else {
                     styleImagePlaceholder(lblGambarUtamaPaket, "Gambar Tidak Tersedia");
                }
            }
            
            // Placeholder untuk galeri dan itinerary
            // loadGalleryImages();
            loadItineraryItems();

        } else {
            if (lblNamaPaket != null) lblNamaPaket.setText("Data Paket Tidak Tersedia");
            // Kosongkan field lain atau tampilkan pesan
        }
    }

    // private void loadGalleryImages() {
    //     if (panelGallery == null) return;
    //     panelGallery.removeAll();
    //     // TODO: Ambil path gambar galeri dari database (jika ada tabel terpisah atau kolom multiple)
    //     // Untuk sekarang, tampilkan placeholder jika tidak ada gambar utama
    //     if (currentPaket == null || currentPaket.getGambar() == null || currentPaket.getGambar().isEmpty()) {
    //         JLabel noGallery = new JLabel("Tidak ada gambar galeri tambahan.");
    //         styleFormLabel(noGallery, "Tidak ada gambar galeri tambahan.");
    //         panelGallery.add(noGallery);
    //     } else {
    //         // Contoh jika ada beberapa gambar di field 'gambar' dipisah koma (TIDAK DIREKOMENDASIKAN, lebih baik tabel terpisah)
    //         // String[] imagePaths = currentPaket.getGambar().split(","); 
    //         // For now, just add one or two placeholders
    //         for (int i=0; i < 3; i++) { // Buat 3 placeholder galeri
    //              JLabel galleryImg = new JLabel();
    //              galleryImg.setPreferredSize(new Dimension(100,75));
    //              styleImagePlaceholder(galleryImg, "Foto " + (i+1));
    //              panelGallery.add(galleryImg);
    //         }
    //     }
    //     panelGallery.revalidate();
    //     panelGallery.repaint();
    // }

    private void styleFormLabel(JLabel label, String defaultText) {
        if (label != null) {
            // Biarkan teks dari Netbeans jika sudah ada, kecuali jika default "jLabelX"
            if(label.getText() == null || label.getText().isEmpty() || label.getText().matches("jLabel\\d+")){
                 label.setText(defaultText);
            }
            label.setFont(AppTheme.FONT_LABEL_FORM); // Gunakan FONT_LABEL_FORM atau FONT_PRIMARY_DEFAULT
            label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        }
    }
    
    private void loadItineraryItems() {
        if (panelItineraryItems == null) return;
        panelItineraryItems.removeAll();
        // TODO: Ambil data itinerary dari database (tabel rincian_paket_perjalanan)
        // Untuk setiap item itinerary, panggil createItineraryItemPanel()
        
        // Contoh dummy itinerary item
        for (int i=1; i <=2; i++) {
            panelItineraryItems.add(createItineraryItemPanel("Destinasi Ke-" + i, "Deskripsi singkat kegiatan di destinasi ke-" + i + ". Kunjungi tempat menarik dan nikmati kuliner lokal.", "Durasi: " + (i+1) + " Jam", "Asset/images/default_itin.png"));
            panelItineraryItems.add(Box.createRigidArea(new Dimension(0,10)));
        }
        // Jika tidak ada itinerary
        if (panelItineraryItems.getComponentCount() == 0) {
             JLabel noItin = new JLabel("Rencana perjalanan tidak tersedia untuk paket ini.");
            styleFormLabel(noItin, "Rencana perjalanan tidak tersedia untuk paket ini.");
            panelItineraryItems.add(noItin);
        }

        panelItineraryItems.revalidate();
        panelItineraryItems.repaint();
    }

    private JPanel createItineraryItemPanel(String namaDestinasi, String deskSingkat, String durasi, String imagePath) {
        JPanel itemPanel = new JPanel(new BorderLayout(10,5));
        itemPanel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR.brighter()),
            new EmptyBorder(10,10,10,10)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Agar tidak terlalu tinggi

        JLabel imageItinLabel = new JLabel();
        imageItinLabel.setPreferredSize(new Dimension(100,75));
        styleImagePlaceholder(imageItinLabel, "Gbr");
        // TODO: Load actual image for imagePath (mirip loadUserPhoto/loadGambarUtama)

        JPanel textItinPanel = new JPanel();
        textItinPanel.setOpaque(false);
        textItinPanel.setLayout(new BoxLayout(textItinPanel, BoxLayout.Y_AXIS));

        JLabel namaItinLabel = new JLabel(namaDestinasi);
        namaItinLabel.setFont(AppTheme.FONT_PRIMARY_BOLD);
        namaItinLabel.setForeground(AppTheme.TEXT_DARK);
        textItinPanel.add(namaItinLabel);

        JTextArea deskItinArea = new JTextArea(deskSingkat);
        deskItinArea.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        deskItinArea.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        deskItinArea.setWrapStyleWord(true);
        deskItinArea.setLineWrap(true);
        deskItinArea.setEditable(false);
        deskItinArea.setOpaque(false);
        textItinPanel.add(deskItinArea);

        JLabel durasiItinLabel = new JLabel(durasi);
        durasiItinLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT); // Perlu FONT_PRIMARY_SMALL di AppTheme
        durasiItinLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        textItinPanel.add(durasiItinLabel);

        itemPanel.add(imageItinLabel, BorderLayout.WEST);
        itemPanel.add(textItinPanel, BorderLayout.CENTER);
        return itemPanel;
    }
    
    private void styleImagePlaceholder(JLabel label, String text) {
        if (label == null) return;
        label.setText(text);
        label.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        label.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        label.setOpaque(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }
    
    private void styleDisplayTextField(JTextField textField, Font font, Color foregroundColor) {
        if (textField != null) {
            textField.setFont(font);
            textField.setForeground(foregroundColor);
            textField.setEditable(false);
            textField.setBorder(null); 
            textField.setOpaque(false); 
            textField.setBackground(new Color(0,0,0,0)); 
        }
    }

    private void stylePrimaryButton(JButton button, String text) {
        if (button == null) return;
        button.setText(text); 
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 25, 10, 25)); // Padding lebih besar untuk tombol utama
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }

    private void styleSecondaryButton(JButton button, String text) {
        if (button == null) return;
        button.setText(text); 
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
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
    
    private void setupActionListeners() {
        if (btnKembali != null) {
            btnKembali.addActionListener(this::btn_back_TDActionPerformed);
        }
        if (btnBookSekarang != null) {
            btnBookSekarang.addActionListener(this::btn_bookActionPerformed);
        }
    }

    // Metode initComponents() HARUS ANDA SALIN DARI FILE TripDetail.java LAMA ANDA
    // DAN SESUAIKAN JIKA ADA PERBEDAAN NAMA VARIABEL KOMPONEN.
    // Kode di bawah ini adalah placeholder yang SANGAT MINIMAL.
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // ===== AWAL PLACEHOLDER initComponents() =====
        // ANDA HARUS MENGGANTI BAGIAN INI DENGAN KODE initComponents() ASLI ANDA
        // DARI NETBEANS, LALU HAPUS BAGIAN YANG MENGATUR FRAME (setDefaultCloseOperation, dll.)
        // DAN PASTIKAN SEMUA VARIABEL FIELD KELAS DIINISIALISASI DI SINI.
        JPanel jPanel1 = new JPanel(); // Panel utama
        // Inisialisasi komponen lain yang dideklarasikan sebagai field jika ada di design Anda
        // Misalnya:
        // tf_namapaket = new JTextField();
        // tf_kotatujuan = new JTextField();
        // ... dan seterusnya untuk semua komponen yang dirujuk di applyAppTheme dan loadTripData
        // ... panel galeri jPanel2-5, label foto lbl_foto1-4
        // ... panel tentang jPanel6, lbl_tentangpkt, ta_deskripsi, jScrollPane1, tf_kuota, tf_status
        // ... panel itinerary jPanel7, lbl_destinasi, jPanel8, jLabel1 (foto itin), tf_namadestinasi, ta_desksingkat, jScrollPane2, tf_durasi
        // Jika initComponents() Anda mengatur layout untuk this (JPanel), maka itu akan dipakai.
        // Jika tidak, layout BorderLayout diatur di konstruktor PanelTripDetail.
        // Untuk contoh ini, saya asumsikan initComponents() akan mengurus layout internal jPanel1.
        // ===== AKHIR PLACEHOLDER initComponents() =====
    }                                         

    private void btn_back_TDActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (mainAppFrame != null) {
            // Kembali ke panel hasil pencarian dengan kriteria yang sama
            if (this.originalSearchNamaKota != null || this.originalSearchTanggal != null) {
                 mainAppFrame.showSearchResultPanel(this.originalSearchNamaKota, this.originalSearchTanggal);
            } else {
                // Fallback jika tidak ada kriteria pencarian, kembali ke beranda
                mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); 
            }
        } else {
             System.err.println("MainAppFrame reference is null in PanelTripDetail (btn_back_TD)");
        }
    }                                           
    private void btn_bookActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (mainAppFrame != null && currentPaket != null) {
            // mainAppFrame.showPanel(MainAppFrame.PANEL_BOOKING_SCREEN, currentPaket);
            JOptionPane.showMessageDialog(this, "Navigasi ke Booking untuk: " + currentPaket.getNamaPaket() + " (Belum diimplementasikan di MainAppFrame)");
        } else if (currentPaket == null) {
            JOptionPane.showMessageDialog(this, "Data paket tidak tersedia untuk dibooking.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            System.err.println("MainAppFrame is null in PanelTripDetail (btn_book)");
        }
    }                                        
}
