package managementtrevel.SearchResultScreen;

import Asset.AppTheme;
import controller.PaketPerjalananController;
import db.dao.KotaDAO;
import java.awt.BorderLayout; // Impor Controller
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator; 
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import managementtrevel.MainAppFrame;
import model.PaketPerjalananModel;


public class PanelSearchResult extends JPanel {

    private MainAppFrame mainAppFrame;
    private String namaKotaAtauDestinasi; 
    private String tanggalKeberangkatan;
    private PaketPerjalananController paketController; 

    // Deklarasi komponen UI
    private JButton btn_back;
    private JButton btn_reset;
    private JButton btn_sebelum;
    private JButton btn_selanjutnya;
    private JComboBox<String> cb_durasi;
    private JComboBox<String> cb_urutkan;
    private JLabel jLabel1; // Filter title
    private JLabel jLabel2; // Urutkan Dari label
    private JLabel jLabel3; // Durasi label
    private JLabel jLabel4; // Hasil Search: title
    private JLabel jLabel8; // Page info label
    
    private JPanel panelFilter; 
    private JPanel panelResultsContainer; 
    private JPanel resultsDisplayPanel; 
    private List<PaketPerjalananModel> allFetchedPackages; 
    private List<PaketPerjalananModel> currentlyDisplayedPackages; 


    public PanelSearchResult(MainAppFrame mainAppFrame, String namaKotaAtauDestinasi, String tanggalKeberangkatan) { 
        this.mainAppFrame = mainAppFrame;
        this.namaKotaAtauDestinasi = namaKotaAtauDestinasi;
        this.tanggalKeberangkatan = tanggalKeberangkatan;
        this.paketController = new PaketPerjalananController(); 
        
        initializeUIProgrammatically(); 
        applyAppTheme();
        setupActionListeners(); 
        fetchAndDisplayInitialResults(); 

        System.out.println("PanelSearchResult Dibuat:");
        System.out.println("Kota/Destinasi: " + this.namaKotaAtauDestinasi);
        System.out.println("Tanggal: " + this.tanggalKeberangkatan);
    }

    private void initializeUIProgrammatically() {
        this.setLayout(new BorderLayout(15, 0)); 
        this.setBorder(new EmptyBorder(15, 15, 15, 15)); 

        panelFilter = new JPanel();
        panelFilter.setLayout(new BoxLayout(panelFilter, BoxLayout.Y_AXIS));
        panelFilter.setPreferredSize(new Dimension(230, 0)); 

        jLabel1 = new JLabel("Filter");
        jLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        jLabel1.setBorder(new EmptyBorder(0, 0, 10, 0));
        panelFilter.add(jLabel1);

        jLabel2 = new JLabel("Urutkan Dari");
        jLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFilter.add(jLabel2);

        cb_urutkan = new JComboBox<>(new String[]{"Relevansi", "Harga: Rendah ke Tinggi", "Harga: Tinggi ke Rendah", "Rating Tertinggi"});
        cb_urutkan.setAlignmentX(Component.LEFT_ALIGNMENT);
        cb_urutkan.setMaximumSize(new Dimension(Integer.MAX_VALUE, cb_urutkan.getPreferredSize().height)); 
        panelFilter.add(cb_urutkan);
        panelFilter.add(Box.createRigidArea(new Dimension(0, 10)));

        jLabel3 = new JLabel("Durasi");
        jLabel3.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFilter.add(jLabel3);

        cb_durasi = new JComboBox<>(new String[]{"Semua Durasi", "1-3 Hari", "4-6 Hari", "7+ Hari"});
        cb_durasi.setAlignmentX(Component.LEFT_ALIGNMENT);
        cb_durasi.setMaximumSize(new Dimension(Integer.MAX_VALUE, cb_durasi.getPreferredSize().height));
        panelFilter.add(cb_durasi);
        
        panelFilter.add(Box.createVerticalGlue()); 

        btn_reset = new JButton("Reset Filter");
        btn_reset.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panelFilter.add(btn_reset);

        this.add(panelFilter, BorderLayout.WEST);

        panelResultsContainer = new JPanel(new BorderLayout(0, 10)); 
        panelResultsContainer.setOpaque(false);

        JPanel resultsHeaderPanel = new JPanel(new BorderLayout());
        resultsHeaderPanel.setOpaque(false);
        btn_back = new JButton("< Kembali ke Beranda");
        jLabel4 = new JLabel("Hasil Pencarian untuk: " + this.namaKotaAtauDestinasi); 
        resultsHeaderPanel.add(btn_back, BorderLayout.WEST);
        resultsHeaderPanel.add(jLabel4, BorderLayout.CENTER); 
        panelResultsContainer.add(resultsHeaderPanel, BorderLayout.NORTH);

        resultsDisplayPanel = new JPanel();
        resultsDisplayPanel.setLayout(new BoxLayout(resultsDisplayPanel, BoxLayout.Y_AXIS)); 
        resultsDisplayPanel.setOpaque(false); 

        JScrollPane scrollPaneResults = new JScrollPane(resultsDisplayPanel);
        scrollPaneResults.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneResults.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPaneResults.getVerticalScrollBar().setUnitIncrement(16);
        scrollPaneResults.getViewport().setOpaque(false);
        scrollPaneResults.setOpaque(false);
        panelResultsContainer.add(scrollPaneResults, BorderLayout.CENTER);

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        paginationPanel.setOpaque(false);
        btn_sebelum = new JButton("< Sebelumnya");
        jLabel8 = new JLabel("Halaman - dari -"); 
        btn_selanjutnya = new JButton("Selanjutnya >");
        paginationPanel.add(btn_sebelum);
        paginationPanel.add(jLabel8);
        paginationPanel.add(btn_selanjutnya);
        panelResultsContainer.add(paginationPanel, BorderLayout.SOUTH);

        this.add(panelResultsContainer, BorderLayout.CENTER);
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        if (panelFilter != null) {
            panelFilter.setBackground(Color.WHITE); 
            panelFilter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR),
                new EmptyBorder(15,15,15,15)
            ));
        }
        if (jLabel1 != null) { 
            jLabel1.setFont(AppTheme.FONT_TITLE_MEDIUM);
            jLabel1.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
        if (jLabel2 != null) { 
            styleFormLabel(jLabel2, "Urutkan Dari:");
        }
        if (cb_urutkan != null) {
            styleComboBox(cb_urutkan);
        }
        if (jLabel3 != null) { 
            styleFormLabel(jLabel3, "Durasi:");
        }
        if (cb_durasi != null) {
            styleComboBox(cb_durasi);
        }
        if (btn_reset != null) {
            styleSecondaryButton(btn_reset, "Reset Filter");
        }

        if (jLabel4 != null) { 
            jLabel4.setFont(AppTheme.FONT_TITLE_MEDIUM);
            jLabel4.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            jLabel4.setBorder(new EmptyBorder(0,10,10,0)); 
        }
        if (btn_back != null) { 
             styleSecondaryButton(btn_back, "< Kembali");
        }
        
        if(btn_sebelum != null) styleSecondaryButton(btn_sebelum, "< Sebelumnya");
        if(btn_selanjutnya != null) stylePrimaryButton(btn_selanjutnya, "Selanjutnya >");
        if(jLabel8 != null) { 
            jLabel8.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            jLabel8.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        }
        if(resultsDisplayPanel != null) resultsDisplayPanel.setBackground(AppTheme.PANEL_BACKGROUND);
    }
    
    private void fetchAndDisplayInitialResults() {
        if (this.paketController == null) {
            System.err.println("PaketPerjalananController tidak diinisialisasi di PanelSearchResult.");
            displayErrorMessage("Gagal memuat data: Layanan tidak tersedia.");
            return;
        }
        System.out.println("[PanelSearchResult] Mencari paket untuk kota: " + this.namaKotaAtauDestinasi);
        // Menggunakan controller untuk mencari berdasarkan nama kota
        this.allFetchedPackages = paketController.cariPaketByNamaKota(this.namaKotaAtauDestinasi);
        
        // Tambahkan logging untuk melihat apa yang dikembalikan oleh controller
        if (this.allFetchedPackages == null) {
            System.out.println("[PanelSearchResult] Controller mengembalikan null untuk allFetchedPackages.");
            this.allFetchedPackages = new ArrayList<>(); // Hindari NullPointerException lebih lanjut
        } else {
            System.out.println("[PanelSearchResult] Jumlah paket ditemukan dari Controller: " + this.allFetchedPackages.size());
        }
        
        applyFiltersAndSort(); 
    }
    
    private void applyFiltersAndSort() {
        if (allFetchedPackages == null) {
            this.currentlyDisplayedPackages = new ArrayList<>();
        } else {
            this.currentlyDisplayedPackages = new ArrayList<>(allFetchedPackages); 
        }

        // Filter berdasarkan Durasi
        String durasiFilter = cb_durasi.getSelectedIndex() > 0 ? (String) cb_durasi.getSelectedItem() : "Semua Durasi";
        if (!durasiFilter.equals("Semua Durasi")) {
            this.currentlyDisplayedPackages = this.currentlyDisplayedPackages.stream().filter(p -> {
                if (p == null) return false; // Pengecekan null untuk paket
                long durasiPaket = p.getDurasi(); 
                if (durasiFilter.equals("1-3 Hari")) return durasiPaket >= 1 && durasiPaket <= 3;
                if (durasiFilter.equals("4-6 Hari")) return durasiPaket >= 4 && durasiPaket <= 6;
                if (durasiFilter.equals("7+ Hari")) return durasiPaket >= 7;
                return true; 
            }).collect(Collectors.toList());
        }

        // Pengurutan
        String urutkanFilter = cb_urutkan.getSelectedIndex() > 0 ? (String) cb_urutkan.getSelectedItem() : "Relevansi";
        if (urutkanFilter.equals("Harga: Rendah ke Tinggi")) {
            this.currentlyDisplayedPackages.sort(Comparator.comparingDouble(PaketPerjalananModel::getHarga));
        } else if (urutkanFilter.equals("Harga: Tinggi ke Rendah")) {
            this.currentlyDisplayedPackages.sort(Comparator.comparingDouble(PaketPerjalananModel::getHarga).reversed());
        } else if (urutkanFilter.equals("Rating Tertinggi")) {
            this.currentlyDisplayedPackages.sort(Comparator.comparingDouble(PaketPerjalananModel::getRating).reversed());
        }

        displayResults();
    }


    private void displayResults() {
        resultsDisplayPanel.removeAll(); 

        if (currentlyDisplayedPackages == null || currentlyDisplayedPackages.isEmpty()) {
            resultsDisplayPanel.setLayout(new BorderLayout()); 
            String message = "Tidak ada paket perjalanan yang cocok untuk '" + this.namaKotaAtauDestinasi + "'.";
            if (this.allFetchedPackages != null && !this.allFetchedPackages.isEmpty() && currentlyDisplayedPackages.isEmpty()){ 
                message = "Tidak ada paket yang cocok dengan filter saat ini untuk '" + this.namaKotaAtauDestinasi + "'.";
            }

            JLabel noResultsLabel = new JLabel(message);
            noResultsLabel.setFont(AppTheme.FONT_SUBTITLE);
            noResultsLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            resultsDisplayPanel.add(noResultsLabel, BorderLayout.CENTER);
        } else {
            resultsDisplayPanel.setLayout(new BoxLayout(resultsDisplayPanel, BoxLayout.Y_AXIS)); 
            for (PaketPerjalananModel paket : currentlyDisplayedPackages) {
                if (paket != null) { // Pengecekan null untuk paket sebelum membuat kartu
                    resultsDisplayPanel.add(createSearchResultCardPanel(paket));
                    resultsDisplayPanel.add(Box.createRigidArea(new Dimension(0, 15))); 
                }
            }
        }
        resultsDisplayPanel.revalidate();
        resultsDisplayPanel.repaint();
    }
    
    private void displayErrorMessage(String message) {
        resultsDisplayPanel.removeAll();
        resultsDisplayPanel.setLayout(new BorderLayout());
        JLabel errorLabel = new JLabel(message);
        errorLabel.setFont(AppTheme.FONT_SUBTITLE);
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultsDisplayPanel.add(errorLabel, BorderLayout.CENTER);
        resultsDisplayPanel.revalidate();
        resultsDisplayPanel.repaint();
    }


    private JPanel createSearchResultCardPanel(PaketPerjalananModel paket) {
        JPanel cardPanel = new JPanel(new BorderLayout(10, 5)); 
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); 
        cardPanel.setMinimumSize(new Dimension(300, 140)); 
        cardPanel.setPreferredSize(new Dimension(400, 160)); 


        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(120, 90)); 
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        
        SwingUtilities.invokeLater(() -> {
            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir);
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && !gambarRelatif.isEmpty()) {
                if (gambarRelatif.startsWith("/") || gambarRelatif.startsWith("\\")) {
                    gambarRelatif = gambarRelatif.substring(1);
                }
                File gambarFile = new File(baseDir, gambarRelatif); 
                 System.out.println("[PanelSearchResult] Mencoba memuat gambar kartu: " + gambarFile.getAbsolutePath()); 
                if (gambarFile.exists()) {
                    try {
                        ImageIcon icon = new ImageIcon(new ImageIcon(gambarFile.toURI().toURL()).getImage().getScaledInstance(imageLabel.getPreferredSize().width, imageLabel.getPreferredSize().height, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                        imageLabel.setOpaque(false);
                        imageLabel.setBackground(null);
                    } catch (Exception e) {
                        System.err.println("Error memuat gambar: " + gambarFile.getPath() + " - " + e.getMessage());
                        imageLabel.setText("Gbr Error"); imageLabel.setOpaque(true); imageLabel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
                    }
                } else {
                    System.err.println("File gambar tidak ditemukan: " + gambarFile.getAbsolutePath());
                    imageLabel.setText("Gbr Tdk Ada"); imageLabel.setOpaque(true); imageLabel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
                }
            } else {
                 imageLabel.setText("Gbr Tdk Ada"); imageLabel.setOpaque(true); imageLabel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
            }
        });
        cardPanel.add(imageLabel, BorderLayout.WEST);

        JPanel textDetailsPanel = new JPanel();
        textDetailsPanel.setLayout(new BoxLayout(textDetailsPanel, BoxLayout.Y_AXIS));
        textDetailsPanel.setOpaque(false);
        textDetailsPanel.setBorder(new EmptyBorder(0,10,0,0)); 

        JLabel nameLabel = new JLabel(paket.getNamaPaket());
        nameLabel.setFont(AppTheme.FONT_SUBTITLE);
        nameLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        textDetailsPanel.add(nameLabel);

        String kota = paket.getKotaId() > 0 ? new KotaDAO().getNamaKotaById(paket.getKotaId()) : "N/A"; 
        JLabel infoLabel = new JLabel(String.format("%s - %d hari - %d orang", kota, paket.getDurasi(), paket.getKuota()));
        infoLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        infoLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        textDetailsPanel.add(infoLabel);

        JLabel ratingLabel = new JLabel("Rating: " + paket.getRating() + "/5.0");
        ratingLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        ratingLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        textDetailsPanel.add(ratingLabel);
        
        textDetailsPanel.add(Box.createVerticalStrut(5)); 

        JLabel priceTitleLabel = new JLabel("Harga:");
        priceTitleLabel.setFont(AppTheme.FONT_LABEL_FORM);
        priceTitleLabel.setForeground(AppTheme.TEXT_DARK);
        textDetailsPanel.add(priceTitleLabel);
        
        JLabel priceLabel = new JLabel("Rp " + String.format("%,.0f", paket.getHarga()));
        priceLabel.setFont(AppTheme.FONT_PRIMARY_BOLD);
        priceLabel.setForeground(AppTheme.ACCENT_ORANGE);
        textDetailsPanel.add(priceLabel);

        cardPanel.add(textDetailsPanel, BorderLayout.CENTER);

        JPanel buttonPanelCard = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,0));
        buttonPanelCard.setOpaque(false);
        JButton detailButtonCard = new JButton();
        styleLinkButton(detailButtonCard, "Detail");
        detailButtonCard.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Lihat Detail untuk: " + paket.getNamaPaket());
        });
        JButton bookingButtonCard = new JButton();
        stylePrimaryButton(bookingButtonCard, "Booking Cepat");
         bookingButtonCard.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Booking Cepat untuk: " + paket.getNamaPaket());
        });
        buttonPanelCard.add(detailButtonCard);
        buttonPanelCard.add(bookingButtonCard);
        cardPanel.add(buttonPanelCard, BorderLayout.SOUTH);

        return cardPanel;
    }


    private void styleFormLabel(JLabel label, String defaultText) {
        if (label != null) {
            if(label.getText() == null || label.getText().isEmpty() || label.getText().matches("jLabel\\d+")){
                 label.setText(defaultText);
            }
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_DARK);
        }
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        if (comboBox == null) return;
        comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setForeground(AppTheme.INPUT_TEXT);
        comboBox.setBorder(AppTheme.createDefaultInputBorder());
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 30));
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
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
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
    
    private void styleLinkButton(JButton button, String text) {
        if (button == null) return;
        button.setText(text); 
        button.setFont(AppTheme.FONT_LINK_BUTTON);
        button.setForeground(AppTheme.BUTTON_LINK_FOREGROUND);
        button.setOpaque(false); 
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect(button, AppTheme.ACCENT_ORANGE, AppTheme.BUTTON_LINK_FOREGROUND, true); 
    }
    
    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        addHoverEffect(button, hoverColor, originalColor, false);
    }

    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor, boolean changeForeground) {
        if (button == null) return;
        button.addMouseListener(new MouseAdapter() { 
            @Override
            public void mouseEntered(MouseEvent e) { 
                if (changeForeground) button.setForeground(hoverColor);
                else button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                if (changeForeground) button.setForeground(originalColor);
                else button.setBackground(originalColor);
            }
        });
    }
    
    private void setupActionListeners() {
        if(btn_back != null) btn_back.addActionListener(this::btn_backActionPerformed);
        if(btn_reset != null) btn_reset.addActionListener(this::btn_resetActionPerformed);
        if(btn_sebelum != null) btn_sebelum.addActionListener(this::btn_sebelumActionPerformed);
        if(btn_selanjutnya != null) btn_selanjutnya.addActionListener(this::btn_selanjutnyaActionPerformed);
        if(cb_urutkan != null) cb_urutkan.addActionListener(this::cb_urutkanActionPerformed);
        if(cb_durasi != null) cb_durasi.addActionListener(this::cb_durasiActionPerformed); 
    }

    private void cb_urutkanActionPerformed(java.awt.event.ActionEvent evt) {                                           
        System.out.println("Urutkan berdasarkan: " + cb_urutkan.getSelectedItem());
        applyFiltersAndSort(); 
    }                                          

    private void cb_durasiActionPerformed(java.awt.event.ActionEvent evt) {                                           
        System.out.println("Filter durasi: " + cb_durasi.getSelectedItem());
        applyFiltersAndSort(); 
    }   

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(cb_urutkan != null) cb_urutkan.setSelectedIndex(0);
        if(cb_durasi != null) cb_durasi.setSelectedIndex(0);
        System.out.println("Filter direset");
        fetchAndDisplayInitialResults(); 
    }       

    private void btn_sebelumActionPerformed(java.awt.event.ActionEvent evt) {                                            
        JOptionPane.showMessageDialog(this, "Tombol Sebelumnya diklik!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }                                           

    private void btn_selanjutnyaActionPerformed(java.awt.event.ActionEvent evt) {                                            
        JOptionPane.showMessageDialog(this, "Tombol Selanjutnya diklik!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }  

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); 
        } else {
            System.err.println("MainAppFrame is null in PanelSearchResult (btn_back)");
        }
    }                                        
}
