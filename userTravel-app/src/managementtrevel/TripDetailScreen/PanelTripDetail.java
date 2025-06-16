package managementtrevel.TripDetailScreen;

import Asset.AppTheme;
import controller.DestinasiController;
import db.dao.KotaDAO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import managementtrevel.MainAppFrame;
import model.DestinasiModel;
import model.PaketPerjalananModel; // Added for resource loading

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

    private int paketId;


    public PanelTripDetail(MainAppFrame mainAppFrame, PaketPerjalananModel paket, String originalSearchNamaKota, String originalSearchTanggal) {
        this.mainAppFrame = mainAppFrame;
        this.currentPaket = paket;
        this.originalSearchNamaKota = originalSearchNamaKota;
        this.originalSearchTanggal = originalSearchTanggal;

        this.paketId = paket.getId();

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
        // Set preferred size here for the main image
        lblGambarUtamaPaket.setPreferredSize(new Dimension(600, 300));
        lblGambarUtamaPaket.setOpaque(true); // Agar background placeholder terlihat
        mainContent.add(lblGambarUtamaPaket, gbc);
        gbc.gridy++;

        // 2b. Info Singkat (Kota, Tanggal, Durasi, Kuota, Status, Rating)
        JPanel shortInfoPanel = new JPanel(new GridLayout(0, 2, 15, 5)); // 2 kolom, gap
        shortInfoPanel.setOpaque(false);
        lblKotaTujuan = createInfoLabel("Kota Tujuan: ");
        lblTanggalTrip = createInfoLabel("Tanggal: ");
        lblKuota = createInfoLabel("Kuota: ");
        lblStatusPaket = createInfoLabel("Status: ");
        lblRatingPaket = createInfoLabel("Rating: ");

        shortInfoPanel.add(lblKotaTujuan);
        shortInfoPanel.add(lblTanggalTrip);
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
        panel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.FONT_SUBTITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        titleLabel.setBorder(new EmptyBorder(0,0,5,0));
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

        // Footer
        if (lblHargaPaket != null) {
            lblHargaPaket.setFont(AppTheme.FONT_TITLE_MEDIUM);
            lblHargaPaket.setForeground(AppTheme.ACCENT_ORANGE);
        }
        if (btnBookSekarang != null) stylePrimaryButton(btnBookSekarang, "Book Sekarang");
    }

    private void loadTripData() {
        if (currentPaket != null) {
            if (lblNamaPaket != null) lblNamaPaket.setText(currentPaket.getNamaPaket());

            KotaDAO kotaDAO = new KotaDAO();
            String namaKota = currentPaket.getKotaId() > 0 ? kotaDAO.getNamaKotaById(currentPaket.getKotaId()) : "N/A";
            long durasi = currentPaket.getDurasi();

            if (lblKotaTujuan != null) lblKotaTujuan.setText("<html><b>Kota Tujuan:</b> " + namaKota + "</html>");
            if (lblTanggalTrip != null) lblTanggalTrip.setText(String.format("<html><b>Tanggal:</b> %s s/d %s (%d hari)</html>", currentPaket.getTanggalMulai(), currentPaket.getTanggalAkhir(), durasi));
            if (lblKuota != null) lblKuota.setText("<html><b>Kuota:</b> " + currentPaket.getKuota() + " orang</html>");
            if (lblStatusPaket != null) {
                lblStatusPaket.setText("<html><b>Status:</b> " + currentPaket.getStatus() + "</html>");
                if ("tersedia".equalsIgnoreCase(currentPaket.getStatus())) {
                    lblStatusPaket.setForeground(AppTheme.TEXT_DARK);
                } else if ("penuh".equalsIgnoreCase(currentPaket.getStatus()) || "selesai".equalsIgnoreCase(currentPaket.getStatus())) {
                    lblStatusPaket.setForeground(AppTheme.TEXT_DARK);
                }
            }
            if (lblRatingPaket != null) lblRatingPaket.setText("<html><b>Rating:</b> " + currentPaket.getRating() + "/5.0</html>");
            if (lblHargaPaket != null) lblHargaPaket.setText("Rp " + String.format("%,.0f", currentPaket.getHarga()));
            if (taDeskripsiPaket != null) taDeskripsiPaket.setText(currentPaket.getDeskripsi());


            if (lblGambarUtamaPaket != null) {
                String gambarPath = currentPaket.getGambar();
                if (gambarPath != null && !gambarPath.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        final String IMAGE_BASE_PATH = "SharedAppImages/paket_perjalanan/";
                        File projectBaseDir = new File(System.getProperty("user.dir")).getParentFile();
                        String gambarRelatif = gambarPath;

                        if (gambarRelatif.startsWith("/") || gambarRelatif.startsWith("\\")) {
                            gambarRelatif = gambarRelatif.substring(1);
                        }

                        File imageFile = new File(projectBaseDir, IMAGE_BASE_PATH + gambarRelatif);
                        System.out.println("[PanelTripDetail] Mencoba memuat gambar utama: " + imageFile.getAbsolutePath());

                        if (imageFile.exists()) {
                            try {
                                int targetWidth = lblGambarUtamaPaket.getPreferredSize().width; // 600
                                int targetHeight = lblGambarUtamaPaket.getPreferredSize().height; // 300

                                Image img = new ImageIcon(imageFile.toURI().toURL()).getImage();
                                Image scaledImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                                ImageIcon icon = new ImageIcon(scaledImg);

                                lblGambarUtamaPaket.setIcon(icon);
                                lblGambarUtamaPaket.setText("");
                                lblGambarUtamaPaket.setOpaque(false);
                                lblGambarUtamaPaket.setBackground(null);
                            } catch (Exception e) {
                                System.err.println("Error saat memuat gambar utama: " + e.getMessage());
                                // Use the general image placeholder method with an error message
                                styleImagePlaceholder(lblGambarUtamaPaket, "Gbr Error");
                            }
                        } else {
                            System.err.println("File gambar utama tidak ditemukan: " + imageFile.getAbsolutePath());
                            // Call the placeholder with the default image path
                            styleImagePlaceholder(lblGambarUtamaPaket, "/resources/images/default_package.png"); // Path to your default image
                        }
                        lblGambarUtamaPaket.revalidate();
                        lblGambarUtamaPaket.repaint();
                    });
                } else {
                    // Call the placeholder with the default image path when gambarPath is null/empty
                    styleImagePlaceholder(lblGambarUtamaPaket, "/resources/images/default_package.png"); // Path to your default image
                }
            }

            loadItineraryItems(paketId);

        } else {
            if (lblNamaPaket != null) lblNamaPaket.setText("Data Paket Tidak Tersedia");
        }
    }

    private void styleFormLabel(JLabel label, String defaultText) {
        if (label != null) {
            if(label.getText() == null || label.getText().isEmpty() || label.getText().matches("jLabel\\d+")){
                label.setText(defaultText);
            }
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        }
    }

    private void loadItineraryItems(int paketId) {
        if (panelItineraryItems == null) return;
        panelItineraryItems.removeAll();

        DestinasiController controller = new DestinasiController();
        List<DestinasiModel> daftarDestinasi = controller.getDestinasiByPaketId(paketId);

        if (daftarDestinasi.isEmpty()) {
            JLabel noItin = new JLabel("Rencana perjalanan tidak tersedia untuk paket ini.");
            styleFormLabel(noItin, "Rencana perjalanan tidak tersedia untuk paket ini.");
            panelItineraryItems.add(noItin);
        } else {
            for (DestinasiModel d : daftarDestinasi) {
                String nama = d.getNamaDestinasi();
                String deskripsi = d.getDeskripsi();
                String durasi = "Durasi: " + d.getDurasiJam() + " Jam";

                String userDir = System.getProperty("user.dir");
                File baseDir = new File(userDir).getParentFile();

                String gambarRelatif = d.getGambar();

                ImageIcon iconUntukItem = null;

                if (gambarRelatif != null && !gambarRelatif.trim().isEmpty()) {
                    if (gambarRelatif.startsWith("/") || gambarRelatif.startsWith("\\")) {
                        gambarRelatif = gambarRelatif.substring(1);
                    }

                    File imageFile = new File(baseDir, gambarRelatif);
                    if (imageFile.exists()) {
                        iconUntukItem = new ImageIcon(imageFile.getAbsolutePath());
                    } else {
                        System.out.println("Gambar tidak ditemukan: " + imageFile.getAbsolutePath());
                        // Pass the default image path to styleImagePlaceholder
                        iconUntukItem = null; // Ensure iconUntukItem is null so placeholder can be drawn
                    }
                } else {
                    System.out.println("Gambar destinasi kosong atau null untuk: " + nama);
                    iconUntukItem = null; // iconUntukItem remains null here
                }

                panelItineraryItems.add(createItineraryItemPanel(nama, deskripsi, durasi, iconUntukItem));
                panelItineraryItems.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        panelItineraryItems.revalidate();
        panelItineraryItems.repaint();
    }

    private JPanel createItineraryItemPanel(String namaDestinasi, String deskSingkat, String durasi, ImageIcon gambarIcon) {
        JPanel itemPanel = new JPanel(new BorderLayout(10,5));
        itemPanel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR.brighter()),
            new EmptyBorder(10,10,10,10)
        ));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel imageItinLabel = new JLabel();
        imageItinLabel.setPreferredSize(new Dimension(100,75));
        imageItinLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageItinLabel.setVerticalAlignment(SwingConstants.CENTER);

        if (gambarIcon != null) {
            Image img = gambarIcon.getImage();
            Image scaledImg = img.getScaledInstance(
                imageItinLabel.getPreferredSize().width,
                imageItinLabel.getPreferredSize().height,
                Image.SCALE_SMOOTH
            );
            imageItinLabel.setIcon(new ImageIcon(scaledImg));
            imageItinLabel.setText("");
            imageItinLabel.setOpaque(false);
            imageItinLabel.setBackground(null);
        } else {
            // If gambarIcon is null, use the default itinerary image placeholder
            styleImagePlaceholder(imageItinLabel, "/resources/images/default_itinerary.png"); // Path to your default itinerary image
        }

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
        durasiItinLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        durasiItinLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        textItinPanel.add(durasiItinLabel);

        itemPanel.add(imageItinLabel, BorderLayout.WEST);
        itemPanel.add(textItinPanel, BorderLayout.CENTER);
        return itemPanel;
    }

    // --- MODIFIED styleImagePlaceholder METHOD ---
    private void styleImagePlaceholder(JLabel label, String imagePathOrText) {
        if (label == null) return;

        // Reset previous icon/text settings
        label.setIcon(null);
        label.setText("");

        // Attempt to load image from resources first
        URL imageUrl = getClass().getResource(imagePathOrText);
        if (imageUrl != null) {
            try {
                ImageIcon originalIcon = new ImageIcon(imageUrl);
                Image img = originalIcon.getImage();

                // Scale to fit the label's preferred size
                int targetWidth = label.getPreferredSize().width > 0 ? label.getPreferredSize().width : 100; // Fallback default if preferred size is 0
                int targetHeight = label.getPreferredSize().height > 0 ? label.getPreferredSize().height : 75; // Fallback default

                Image scaledImg = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaledImg));
                label.setOpaque(false); // No need for opaque background if image fills
                label.setBackground(null);
            } catch (Exception e) {
                System.err.println("Error loading placeholder image from resources: " + e.getMessage());
                // Fallback to text if resource image loading fails
                label.setText(imagePathOrText.contains("/") ? "Gbr Gagal" : imagePathOrText); // Display "Gbr Gagal" or original text
                label.setOpaque(true);
                label.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
            }
        } else {
            // If not a resource path or resource not found, treat as plain text placeholder
            label.setText(imagePathOrText);
            label.setOpaque(true);
            label.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        }

        label.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }
    // --- END MODIFIED styleImagePlaceholder METHOD ---

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
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
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

    @SuppressWarnings("unchecked")
    private void initComponents() {
        JPanel jPanel1 = new JPanel();
    }

    private void btn_back_TDActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            if (this.originalSearchNamaKota != null || this.originalSearchTanggal != null) {
                mainAppFrame.showSearchResultPanel(this.originalSearchNamaKota, this.originalSearchTanggal);
            } else {
                mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA);
            }
        } else {
            System.err.println("MainAppFrame reference is null in PanelTripDetail (btn_back_TD)");
        }
    }
    private void btn_bookActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null && currentPaket != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_BOOKING_SCREEN, currentPaket);
        } else if (currentPaket == null) {
            JOptionPane.showMessageDialog(this, "Data paket tidak tersedia untuk dibooking.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            System.err.println("MainAppFrame is null in PanelTripDetail (btn_book)");
        }
    }
}