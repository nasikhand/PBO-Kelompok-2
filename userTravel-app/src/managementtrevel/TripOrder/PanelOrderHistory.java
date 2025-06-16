package managementtrevel.TripOrder;

import Asset.AppTheme;
import db.Koneksi;
import db.dao.PembayaranDAO;
import db.dao.PenumpangDAO;
import db.dao.ReservasiDAO;
import db.dao.PaketPerjalananDAO;
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

public class PanelOrderHistory extends JPanel {

    private MainAppFrame mainAppFrame;

    private JPanel mainContentPanel; // Panel to hold the list of history cards
    private JScrollPane scrollPane; // Scroll pane for mainContentPanel
    private JLabel titleLabel; // Title "Riwayat Pesanan"
    private JButton btn_back;

    private ReservasiDAO reservasiDAO;
    private PenumpangDAO penumpangDAO;
    private PembayaranDAO pembayaranDAO;
    private PaketPerjalananDAO paketPerjalananDAO;

    public PanelOrderHistory(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;

        // Initialize DAOs
        reservasiDAO = new ReservasiDAO(Koneksi.getConnection());
        penumpangDAO = new PenumpangDAO(Koneksi.getConnection());
        pembayaranDAO = new PembayaranDAO(Koneksi.getConnection());
        paketPerjalananDAO = new PaketPerjalananDAO(Koneksi.getConnection());

        initializeUI(); // Call to set up the UI
        loadDataReservasi(); // Load history data after UI is initialized
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(15, 20, 15, 20)); // General padding

        // Header Panel (Back button + Title)
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

        titleLabel = new JLabel("Riwayat Pesanan");
        titleLabel.setFont(AppTheme.FONT_TITLE_LARGE);
        titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Dummy label for visual balance in the header
        JLabel dummyEast = new JLabel();
        dummyEast.setPreferredSize(btn_back.getPreferredSize());
        headerPanel.add(dummyEast, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);

        // Main content panel to hold each history card
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

        applyAppTheme(); // Apply theme to initialized components
    }

    private void loadDataReservasi() {
        mainContentPanel.removeAll(); // Hapus semua kartu lama

        try {
            if (Session.currentUser == null) {
                displayEmptyMessage("Anda harus login untuk melihat riwayat pesanan.");
                return;
            }

            int userId = Session.currentUser.getId();
            
            // --- FIXED: Panggil metode DAO baru untuk mendapatkan riwayat lengkap ---
            List<ReservasiModel> listReservasi = reservasiDAO.getHistoryReservasiByUser(userId);

            if (listReservasi != null && !listReservasi.isEmpty()) {
                System.out.println("DEBUG PanelOrderHistory - Ditemukan " + listReservasi.size() + " riwayat pesanan (Paket & Custom).");
                for (ReservasiModel reservasi : listReservasi) {
                    // Metode createHistoryCard Anda sekarang harus bisa menangani
                    // baik reservasi.getPaket() maupun reservasi.getCustomTrip()
                    mainContentPanel.add(createHistoryCard(reservasi));
                    mainContentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            } else {
                displayEmptyMessage("Tidak ada riwayat pesanan ditemukan.");
            }
        } catch (Exception e) {
            displayEmptyMessage("Gagal mengambil data riwayat: " + e.getMessage());
            System.err.println("Error saat mengambil data riwayat di PanelOrderHistory: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Pindahkan revalidate dan repaint ke dalam blok finally
            // agar panel selalu diperbarui, bahkan jika tidak ada hasil.
            mainContentPanel.revalidate();
            mainContentPanel.repaint();
        }
    }

    private void displayEmptyMessage(String message) {
        mainContentPanel.removeAll();
        JLabel emptyLabel = new JLabel(message, SwingConstants.CENTER);
        emptyLabel.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
        emptyLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContentPanel.add(Box.createVerticalGlue()); // Push label to center
        mainContentPanel.add(emptyLabel);
        mainContentPanel.add(Box.createVerticalGlue()); // Push label to center
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private JPanel createHistoryCard(ReservasiModel reservasi) {
        JPanel cardPanel = new JPanel(new BorderLayout(15, 0));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Left: Photo / Placeholder
        JLabel fotoLabel = new JLabel("FOTO", SwingConstants.CENTER);
        fotoLabel.setPreferredSize(new Dimension(100, 100));
        fotoLabel.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        fotoLabel.setOpaque(true);
        fotoLabel.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        fotoLabel.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        fotoLabel.setForeground(AppTheme.TEXT_SECONDARY_DARK);

        // Load image if available for PaketPerjalananModel
        if ("paket_perjalanan".equals(reservasi.getTripType()) && reservasi.getPaket() != null) {
            String gambarPath = reservasi.getPaket().getGambar();
            if (gambarPath != null && !gambarPath.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        final String IMAGE_BASE_PATH = "SharedAppImages/paket_perjalanan/";
                        File projectBaseDir = new File(System.getProperty("user.dir")).getParentFile();
                        String gambarRelatif = gambarPath.startsWith("/") ? gambarPath.substring(1) : gambarPath;
                        File imageFile = new File(projectBaseDir,IMAGE_BASE_PATH + gambarRelatif);

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
                        System.err.println("Error loading image for history card: " + e.getMessage());
                    }
                });
            }
        } else if ("custom_trip".equals(reservasi.getTripType())) {
             fotoLabel.setText("Custom Trip");
        }
        cardPanel.add(fotoLabel, BorderLayout.WEST);

        // Center: Details
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);
        detailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNamaTrip = new JLabel();
        lblNamaTrip.setFont(AppTheme.FONT_SUBTITLE);
        lblNamaTrip.setForeground(AppTheme.TEXT_DARK);
        lblNamaTrip.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblNamaTrip);

        JLabel lblDetail1 = new JLabel(); // For Days, People, Rating
        lblDetail1.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
        lblDetail1.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        lblDetail1.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblDetail1);

        JLabel lblHarga = new JLabel();
        lblHarga.setFont(AppTheme.FONT_PRIMARY_BOLD);
        lblHarga.setForeground(AppTheme.ACCENT_ORANGE);
        lblHarga.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblHarga);

        JLabel lblTanggalPemesanan = new JLabel(); // Label for order date
        lblTanggalPemesanan.setFont(AppTheme.FONT_LABEL_FORM);
        lblTanggalPemesanan.setForeground(AppTheme.TEXT_DARK);
        lblTanggalPemesanan.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblTanggalPemesanan);
        
        // Add Status label for history view
        JLabel lblStatus = new JLabel();
        lblStatus.setFont(AppTheme.FONT_LABEL_FORM);
        lblStatus.setForeground(AppTheme.TEXT_DARK);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailPanel.add(lblStatus);
        
        detailPanel.add(Box.createVerticalGlue());

        // Populate details
        String namaTrip = "N/A";
        String detailText = "";
        String hargaText = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(0.0);
        int jumlahOrang = 0;
        double rating = 0.0;
        double currentTotalHarga = 0.0;

        jumlahOrang = penumpangDAO.getJumlahPenumpangByReservasiId(reservasi.getId());
        System.out.println("DEBUG PanelOrderHistory - Reservasi ID: " + reservasi.getId() + ", Jumlah Penumpang: " + jumlahOrang);


        if ("paket_perjalanan".equals(reservasi.getTripType()) && reservasi.getPaket() != null) {
            PaketPerjalananModel paket = reservasi.getPaket();
            namaTrip = paket.getNamaPaket() + " (Kota: " + paket.getNamaKota() + ")";
            rating = paket.getRating();
            detailText = paket.getDurasi() + " Hari | " + jumlahOrang + " Orang | Rating " + String.format("%.1f", rating);
            
            Double hargaLunas = pembayaranDAO.getJumlahPembayaranByReservasiId(reservasi.getId());
            if (hargaLunas != null && hargaLunas > 0) {
                currentTotalHarga = hargaLunas;
            } else {
                currentTotalHarga = paket.getHarga() * jumlahOrang;
            }
            hargaText = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(currentTotalHarga);
            System.out.println("DEBUG PanelOrderHistory - Paket: " + namaTrip + ", Final Harga: " + hargaText);

        } else if ("custom_trip".equals(reservasi.getTripType()) && reservasi.getCustomTrip() != null) {
            CustomTripModel customTrip = reservasi.getCustomTrip();
            namaTrip = customTrip.getNamaTrip() + " (Kota: " + customTrip.getNamaKota() + ")";
            if (customTrip.getJumlahPeserta() > 0) {
                jumlahOrang = customTrip.getJumlahPeserta();
            } else {
                jumlahOrang = penumpangDAO.getJumlahPenumpangByReservasiId(reservasi.getId());
            }
            detailText = customTrip.getDurasi() + " Hari | " + jumlahOrang + " Orang";
            
            currentTotalHarga = customTrip.getTotalHarga();
            hargaText = NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(currentTotalHarga);
            System.out.println("DEBUG PanelOrderHistory - Custom Trip: " + namaTrip + ", Total Harga: " + hargaText);
        }

        lblNamaTrip.setText(namaTrip);
        lblDetail1.setText(detailText);
        lblHarga.setText("Harga: " + hargaText);
        lblTanggalPemesanan.setText("Tanggal Pemesanan: " + (reservasi.getTanggalReservasi() != null ? reservasi.getTanggalReservasi().format(DateTimeFormatter.ofPattern("dd MM YYYY")) : "N/A"));
        lblStatus.setText("Status: " + reservasi.getStatus()); // Display actual status

        cardPanel.add(detailPanel, BorderLayout.CENTER);

        // Right: "Pesan Lagi" Button (Only this button for history)
        JPanel actionButtonsPanel = new JPanel();
        actionButtonsPanel.setLayout(new BoxLayout(actionButtonsPanel, BoxLayout.Y_AXIS));
        actionButtonsPanel.setOpaque(false);
        actionButtonsPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton btnPesanLagi = new JButton("Pesan Lagi");
        stylePrimaryButton(btnPesanLagi, "Pesan Lagi");
        btnPesanLagi.addActionListener(e -> btn_pesanLagiActionPerformed(reservasi));
        actionButtonsPanel.add(btnPesanLagi);

        // No "Batalkan Pesanan" button here in PanelOrderHistory
        
        cardPanel.add(actionButtonsPanel, BorderLayout.EAST);

        return cardPanel;
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        if (titleLabel != null) {
            titleLabel.setFont(AppTheme.FONT_TITLE_LARGE);
            titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
        if (btn_back != null) {
            styleSecondaryButton(btn_back, "< Kembali");
        }
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
        if (btn_back != null) {
            btn_back.addActionListener(this::btn_backActionPerformed);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // This method will be empty as UI is built programmatically
    }

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_USER_PROFILE);
        } else {
            System.err.println("MainAppFrame is null in PanelOrderHistory (btn_back)");
        }
    }

    private void btn_pesanLagiActionPerformed(ReservasiModel reservasi) {
        if (mainAppFrame != null) {
            if ("paket_perjalanan".equals(reservasi.getTripType()) && reservasi.getPaket() != null) {
                JOptionPane.showMessageDialog(this, "Mengarahkan untuk memesan lagi Paket: " + reservasi.getPaket().getNamaPaket(), "Pesan Lagi", JOptionPane.INFORMATION_MESSAGE);
                mainAppFrame.showPanel(MainAppFrame.PANEL_TRIP_DETAIL, reservasi.getPaket(), null, null);
            } else if ("custom_trip".equals(reservasi.getTripType()) && reservasi.getCustomTrip() != null) {
                JOptionPane.showMessageDialog(this, "Mengarahkan untuk memesan lagi Custom Trip: " + reservasi.getCustomTrip().getNamaTrip(), "Pesan Lagi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tidak dapat memesan lagi jenis perjalanan ini.", "Pesan Lagi", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            System.err.println("MainAppFrame is null in PanelOrderHistory (btn_pesanLagi)");
        }
    }
}
