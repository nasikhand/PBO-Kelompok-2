package managementtrevel.HomeUser;

// Import yang relevan
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory; // Untuk border
import javax.swing.UIDefaults; // Untuk placeholder JComboBox
import javax.swing.UIManager; // Untuk placeholder JComboBox
import javax.swing.plaf.basic.BasicComboBoxRenderer; // Untuk placeholder JComboBox


import managementtrevel.CustomTripBuilder.DestinationStep;
import managementtrevel.SearchResultScreen.SearchResult;
import managementtrevel.TripDetailScreen.TripDetail;
import model.Session;
import db.Koneksi;
import db.dao.KotaDAO;
import controller.CariCepatController;
import controller.PaketPerjalananController;
import model.DestinasiModel;
import model.PaketPerjalananModel;

// Impor AppTheme Anda
import Asset.AppTheme;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.awt.Image;
import java.io.File;
<<<<<<< HEAD
import java.awt.Dimension; // Untuk setPreferredSize jika perlu
import javax.swing.SwingUtilities;
=======
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component; // Untuk ComboBox Renderer
import java.awt.Cursor;
import javax.swing.JList; // Untuk ComboBox Renderer
import java.awt.event.FocusAdapter; // Untuk focus listener
import java.awt.event.FocusEvent;   // Untuk focus listener
import java.awt.event.MouseAdapter; // Untuk hover effect
import java.awt.event.MouseEvent;   // Untuk hover effect
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
>>>>>>> 2d86fa6403fb5379932c31ede5f865afd2bebb91


public class PanelBeranda extends JPanel {

    private List<DestinasiModel> daftarDestinasi;
    private Date tanggalDipilih;
    private String jumlahTravelerDipilih = null;
    private PaketPerjalananController paketPerjalananController;
    private final String PLACEHOLDER_DESTINASI = "-- Pilih Destinasi --";
    private final String PLACEHOLDER_TRAVELERS = "Travelers";


    public void loadTopRated(PaketPerjalananModel paket) {
        if (paket != null) {
            String namaKota = getNamaKotaById(paket.getKotaId());
            long durasi = paket.getDurasi();

            jLabel20.setText(namaKota + " - " + durasi + " hari - " + paket.getKuota() + " orang");
            jLabel21.setText("Rating: " + paket.getRating());
            jLabel22.setText("Harga: Rp " + String.format("%,.0f", paket.getHarga()));

            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir); 

            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            } else if (gambarRelatif == null) {
                System.out.println("Path gambar null untuk paket: " + (paket.getNamaPaket() != null ? paket.getNamaPaket() : "ID " + paket.getId()));
                setPlaceholderImage(jLabel10, "Gambar tidak tersedia");
                return;
            }
            
            File gambarFile = new File(baseDir, gambarRelatif);
            System.out.println("Mencoba memuat gambar dari: " + gambarFile.getAbsolutePath());

            if (gambarFile.exists() && jLabel10.getWidth() > 0 && jLabel10.getHeight() > 0) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(gambarFile.getAbsolutePath())
                                .getImage()
                                .getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH)
                );
                jLabel10.setIcon(icon);
                jLabel10.setText("");
                jLabel10.setBackground(AppTheme.PANEL_BACKGROUND); // Hapus background jika gambar ada
            } else {
                 if (!gambarFile.exists()) {
                    System.out.println("Gambar paket tidak ditemukan: " + gambarFile.getAbsolutePath());
                } else {
                    System.out.println("Ukuran jLabel10 belum siap untuk gambar: " + jLabel10.getWidth() + "x" + jLabel10.getHeight());
                }
                setPlaceholderImage(jLabel10, "Gambar tidak tersedia");
            }
        } else {
            jLabel20.setText("Data tidak ditemukan");
            jLabel21.setText("Rating:");
            jLabel22.setText("Harga:");
            setPlaceholderImage(jLabel10, "FOTO");
        }
    }

    public void loadTopRated2(PaketPerjalananModel paket) {
        if (paket != null) {
            String namaKota = getNamaKotaById(paket.getKotaId());
            long durasi = paket.getDurasi();

            jLabel31.setText(namaKota + " - " + durasi + " hari - " + paket.getKuota() + " orang");
            jLabel32.setText("Rating: " + paket.getRating());
            jLabel33.setText("Harga: Rp " + String.format("%,.0f", paket.getHarga()));

            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir);
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            } else if (gambarRelatif == null) {
                 System.out.println("Path gambar null untuk paket: " + (paket.getNamaPaket() != null ? paket.getNamaPaket() : "ID " + paket.getId()));
                setPlaceholderImage(jLabel12, "Gambar tidak tersedia");
                return;
            }
            File gambarFile = new File(baseDir, gambarRelatif);
            System.out.println("Mencoba memuat gambar (2) dari: " + gambarFile.getAbsolutePath());

            if (gambarFile.exists() && jLabel12.getWidth() > 0 && jLabel12.getHeight() > 0) {
                ImageIcon icon = new ImageIcon(new ImageIcon(gambarFile.getAbsolutePath())
                        .getImage().getScaledInstance(jLabel12.getWidth(), jLabel12.getHeight(), Image.SCALE_SMOOTH));
                jLabel12.setIcon(icon);
                jLabel12.setText("");
                jLabel12.setBackground(AppTheme.PANEL_BACKGROUND);
            } else {
                if (!gambarFile.exists()) {
                    System.out.println("Gambar paket (2) tidak ditemukan: " + gambarFile.getAbsolutePath());
                } else {
                    System.out.println("Ukuran jLabel12 belum siap untuk gambar: " + jLabel12.getWidth() + "x" + jLabel12.getHeight());
                }
                setPlaceholderImage(jLabel12, "Gambar tidak tersedia");
            }
        } else {
            jLabel31.setText("Data tidak ditemukan");
            setPlaceholderImage(jLabel12, "FOTO");
            jLabel32.setText("Rating:");
            jLabel33.setText("Harga:");
        }
    }

    public void loadTopRated3(PaketPerjalananModel paket) {
        if (paket != null) {
            String namaKota = getNamaKotaById(paket.getKotaId());
            long durasi = paket.getDurasi();

            jLabel34.setText(namaKota + " - " + durasi + " hari - " + paket.getKuota() + " orang");
            jLabel35.setText("Rating: " + paket.getRating());
            jLabel36.setText("Harga: Rp " + String.format("%,.0f", paket.getHarga()));

            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir);
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            } else if (gambarRelatif == null) {
                System.out.println("Path gambar null untuk paket: " + (paket.getNamaPaket() != null ? paket.getNamaPaket() : "ID " + paket.getId()));
                setPlaceholderImage(jLabel13, "Gambar tidak tersedia");
                return;
            }
            File gambarFile = new File(baseDir, gambarRelatif);
            System.out.println("Mencoba memuat gambar (3) dari: " + gambarFile.getAbsolutePath());

            if (gambarFile.exists() && jLabel13.getWidth() > 0 && jLabel13.getHeight() > 0) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(gambarFile.getAbsolutePath())
                                .getImage()
                                .getScaledInstance(jLabel13.getWidth(), jLabel13.getHeight(), Image.SCALE_SMOOTH)
                );
                jLabel13.setIcon(icon);
                jLabel13.setText("");
                jLabel13.setBackground(AppTheme.PANEL_BACKGROUND);
            } else {
                 if (!gambarFile.exists()) {
                    System.out.println("Gambar paket (3) tidak ditemukan: " + gambarFile.getAbsolutePath());
                } else {
                    System.out.println("Ukuran jLabel13 belum siap untuk gambar: " + jLabel13.getWidth() + "x" + jLabel13.getHeight());
                }
                setPlaceholderImage(jLabel13, "Gambar tidak tersedia");
            }
        } else {
            jLabel34.setText("Data tidak ditemukan");
            jLabel35.setText("Rating:");
            jLabel36.setText("Harga:");
            setPlaceholderImage(jLabel13, "FOTO");
        }
    }
    
    private void setPlaceholderImage(JLabel label, String text) {
        label.setIcon(null);
        label.setText(text);
        label.setBackground(new Color(220,220,220)); // Latar belakang abu-abu muda untuk placeholder
        label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        label.setOpaque(true); // Pastikan background terlihat
    }


    private String getNamaKotaById(int kotaId) {
        return new KotaDAO().getNamaKotaById(kotaId);
    }

    public PanelBeranda() {
        initComponents(); 
        applyAppTheme(); // Panggil metode untuk menerapkan tema

        if (Session.currentUser != null) {
            labelNama.setText("Selamat Datang, " + Session.currentUser.getNamaLengkap());
        } else {
            labelNama.setText("Selamat Datang, Tamu");
        }

        Connection conn = Koneksi.getConnection();
        CariCepatController controller = new CariCepatController(conn);
        this.daftarDestinasi = controller.getDaftarDestinasi();

        destinasi.removeAllItems();
        destinasi.addItem(PLACEHOLDER_DESTINASI); // Placeholder
        if (this.daftarDestinasi != null) {
            for (DestinasiModel dest : daftarDestinasi) {
                destinasi.addItem(dest.getNamaDestinasi());
            }
        }
        // Atur renderer untuk placeholder ComboBox
        destinasi.setRenderer(new PlaceholderComboBoxRenderer(PLACEHOLDER_DESTINASI));
        destinasi.setSelectedIndex(0); // Pilih placeholder


        pilih_travelers.removeAllItems();
        pilih_travelers.addItem(PLACEHOLDER_TRAVELERS);
        pilih_travelers.addItem("1 Orang");
        pilih_travelers.addItem("2 Orang");
        pilih_travelers.addItem("3 Orang");
        pilih_travelers.addItem("4 Orang");
        pilih_travelers.addItem("5 Orang");
        pilih_travelers.setRenderer(new PlaceholderComboBoxRenderer(PLACEHOLDER_TRAVELERS));
        pilih_travelers.setSelectedIndex(0);


        jDateChooserCalendar.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                tanggalDipilih = jDateChooserCalendar.getDate();
                System.out.println("Tanggal dipilih: " + tanggalDipilih);
                 // Hapus border fokus jika tanggal dipilih
                jDateChooserCalendar.setBorder(AppTheme.createDefaultInputBorder());
            }
        });

        paketPerjalananController = new PaketPerjalananController(conn);
        
        SwingUtilities.invokeLater(() -> {
            List<PaketPerjalananModel> topRated = paketPerjalananController.getTopRatedPakets(3);
            if (topRated != null) {
                if (topRated.size() > 0) loadTopRated(topRated.get(0)); else loadTopRated(null);
                if (topRated.size() > 1) loadTopRated2(topRated.get(1)); else loadTopRated2(null);
                if (topRated.size() > 2) loadTopRated3(topRated.get(2)); else loadTopRated3(null);
            }
        });
    }

    private void applyAppTheme() {
        // Latar belakang utama panel
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        if (jPanel3 != null) jPanel3.setBackground(AppTheme.PANEL_BACKGROUND);
        if (jPanel4 != null) jPanel4.setBackground(AppTheme.PANEL_BACKGROUND); // Panel Cari Cepat
        if (jPanel7 != null) jPanel7.setBackground(AppTheme.PANEL_BACKGROUND); // Panel dalam scrollpane
        
        // Latar belakang untuk "kartu" atau section
        Color cardBackgroundColor = Color.WHITE; // Atau AppTheme.BACKGROUND_LIGHT_GRAY jika ingin sama
        if (jPanel5 != null) jPanel5.setBackground(cardBackgroundColor); // Perjalanan Sebelumnya
        if (jPanel1 != null) jPanel1.setBackground(cardBackgroundColor); // Detail dalam Perjalanan Sebelumnya
        if (jPanel2 != null) jPanel2.setBackground(cardBackgroundColor); // Penawaran Spesial
        if (jPanel8 != null) jPanel8.setBackground(cardBackgroundColor); // Detail dalam Penawaran Spesial
        if (jPanel9 != null) jPanel9.setBackground(cardBackgroundColor); // Detail dalam Penawaran Spesial
        if (popular_destination1 != null) popular_destination1.setBackground(cardBackgroundColor);
        if (popular_destination4 != null) popular_destination4.setBackground(cardBackgroundColor);
        if (popular_destination5 != null) popular_destination5.setBackground(cardBackgroundColor);

        // Border untuk "kartu"
        javax.swing.border.Border cardBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding dalam kartu
        );
        if (jPanel5 != null) jPanel5.setBorder(cardBorder);
        if (jPanel2 != null) jPanel2.setBorder(cardBorder);
        if (popular_destination1 != null) popular_destination1.setBorder(cardBorder);
        if (popular_destination4 != null) popular_destination4.setBorder(cardBorder);
        if (popular_destination5 != null) popular_destination5.setBorder(cardBorder);


        // Label Nama Pengguna
        if (labelNama != null) {
            labelNama.setFont(AppTheme.FONT_TITLE_MEDIUM);
            labelNama.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            labelNama.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // Padding
        }

        // Judul Section
        if (jLabel1 != null) { // Cari Cepat
            jLabel1.setFont(AppTheme.FONT_SUBTITLE);
            jLabel1.setForeground(AppTheme.TEXT_DARK);
        }
        if (jLabel2 != null) { // Perjalanan Sebelumnya
            jLabel2.setFont(AppTheme.FONT_SUBTITLE);
            jLabel2.setForeground(AppTheme.TEXT_DARK);
        }
         if (jLabel8 != null) { // Penawaran Spesial
            jLabel8.setFont(AppTheme.FONT_SUBTITLE);
            jLabel8.setForeground(AppTheme.TEXT_DARK);
        }
        if (jLabel4 != null) { // Destinasi Populer
            jLabel4.setFont(AppTheme.FONT_SUBTITLE);
            jLabel4.setForeground(AppTheme.TEXT_DARK);
        }

        // Input Fields (ComboBox, DateChooser)
        if (destinasi != null) styleComboBox(destinasi);
        if (pilih_travelers != null) styleComboBox(pilih_travelers);
        if (jDateChooserCalendar != null) {
            jDateChooserCalendar.setFont(AppTheme.FONT_TEXT_FIELD);
            jDateChooserCalendar.getCalendarButton().setFont(AppTheme.FONT_BUTTON); // Tombol kalender
            jDateChooserCalendar.getCalendarButton().setBackground(AppTheme.PRIMARY_BLUE_LIGHT);
            jDateChooserCalendar.getCalendarButton().setForeground(AppTheme.TEXT_WHITE);
            jDateChooserCalendar.getCalendarButton().setFocusPainted(false);
            jDateChooserCalendar.getCalendarButton().setBorder(BorderFactory.createEmptyBorder(5,8,5,8));

            // Styling untuk text field di JDateChooser
            com.toedter.calendar.JTextFieldDateEditor editor = (com.toedter.calendar.JTextFieldDateEditor) jDateChooserCalendar.getDateEditor();
            editor.setFont(AppTheme.FONT_TEXT_FIELD);
            editor.setBackground(AppTheme.INPUT_BACKGROUND);
            editor.setForeground(AppTheme.INPUT_TEXT);
            editor.setBorder(AppTheme.createDefaultInputBorder());
            editor.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    editor.setBorder(AppTheme.createFocusBorder());
                }
                @Override
                public void focusLost(FocusEvent e) {
                    editor.setBorder(AppTheme.createDefaultInputBorder());
                }
            });
        }
        
        // Buttons
        if (tombolCari != null) stylePrimaryButton(tombolCari);
        if (btn_CustomTrip != null) styleSecondaryButton(btn_CustomTrip); // Atau Primary jika dianggap aksi utama juga

        // Tombol dalam kartu (Detail, Booking Cepat)
        if (btn_detail1 != null) styleLinkButton(btn_detail1); // Gaya link untuk detail
        if (jButton13 != null) styleSecondaryButton(jButton13); // Booking Cepat sebagai secondary
        if (btn_detail4 != null) styleLinkButton(btn_detail4);
        if (jButton14 != null) styleSecondaryButton(jButton14);
        if (btn_detail5 != null) styleLinkButton(btn_detail5);
        if (jButton16 != null) styleSecondaryButton(jButton16);
        
        // Tombol di Penawaran Spesial (Pesan Sekarang)
        if (jButton1 != null) stylePrimaryButton(jButton1);
        if (jButton4 != null) stylePrimaryButton(jButton4);


        // Label Teks dalam Kartu (Destinasi Populer, Perjalanan Sebelumnya, Penawaran)
        JLabel[] cardTextLabels = {
            jLabel20, jLabel21, jLabel22, // Populer 1
            jLabel31, jLabel32, jLabel33, // Populer 2
            jLabel34, jLabel35, jLabel36, // Populer 3
            jLabel26, jLabel27, jLabel28, // Perjalanan Sebelumnya
            jLabel3, jLabel29,             // Penawaran 1
            jLabel9, jLabel30              // Penawaran 2
        };
        for (JLabel label : cardTextLabels) {
            if (label != null) {
                label.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
                label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
                if (label.getText().toLowerCase().contains("harga")) {
                    label.setFont(AppTheme.FONT_PRIMARY_BOLD); // Harga dibuat bold
                    label.setForeground(AppTheme.ACCENT_ORANGE); // Harga dengan warna aksen
                }
                 if (label.getText().toLowerCase().contains("rating")) {
                    label.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
                }
            }
        }

        // Placeholder Gambar
        JLabel[] imageLabels = {jLabel10, jLabel11, jLabel12, jLabel13};
        for (JLabel imgLabel : imageLabels) {
            if (imgLabel != null) {
                imgLabel.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                setPlaceholderImage(imgLabel, "FOTO"); // Set default placeholder
            }
        }
        
        // ScrollPane
        if (jScrollPane2 != null) {
            jScrollPane2.setBackground(AppTheme.PANEL_BACKGROUND);
            jScrollPane2.getViewport().setBackground(AppTheme.PANEL_BACKGROUND);
            jScrollPane2.setBorder(BorderFactory.createEmptyBorder()); // Hapus border default scrollpane
        }
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setForeground(AppTheme.INPUT_TEXT);
        comboBox.setBorder(AppTheme.createDefaultInputBorder());
        // Tambahkan focus listener untuk mengubah border
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
    
    private void stylePrimaryButton(JButton button) {
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding
        button.setOpaque(true); // Agar background terlihat
        button.setBorderPainted(false); // Matikan border default jika menggunakan padding sebagai border
        
        // Hover effect
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

    private void styleSecondaryButton(JButton button) {
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setOpaque(true);
        button.setBorderPainted(false);

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
        button.setBackground(Color.WHITE); // Atau TRANSPARENT jika didukung
        button.setOpaque(false); // Agar background panel tembus jika diinginkan
        button.setContentAreaFilled(false); // Tidak mengisi area tombol
        button.setBorderPainted(false); // Tidak ada border
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color originalFg = AppTheme.BUTTON_LINK_FOREGROUND;
        Color hoverFg = AppTheme.ACCENT_ORANGE; // Warna aksen saat hover
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


    // Custom Renderer untuk ComboBox dengan Placeholder
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
                if (index == -1) { // Hanya untuk item yang terpilih (bukan di dropdown list)
                    setText(placeholder);
                    setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                } else { // Untuk item di dropdown list
                    setText("  " + placeholder); // Beri sedikit indentasi
                    setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                }
            } else {
                setText("  " + value.toString()); // Beri sedikit indentasi untuk item non-placeholder juga
                setForeground(isSelected ? list.getSelectionForeground() : AppTheme.INPUT_TEXT);
                setBackground(isSelected ? list.getSelectionBackground() : AppTheme.INPUT_BACKGROUND);
            }
            
            if (!isSelected) { // Pastikan background default untuk item yang tidak dipilih
                 setBackground(AppTheme.INPUT_BACKGROUND);
            }
            setFont(AppTheme.FONT_TEXT_FIELD);
            return this;
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        labelNama = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pilih_travelers = new javax.swing.JComboBox<>();
        tombolCari = new javax.swing.JButton();
        btn_CustomTrip = new javax.swing.JButton();
        jDateChooserCalendar = new com.toedter.calendar.JDateChooser();
        destinasi = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        popular_destination1 = new javax.swing.JPanel();
        btn_detail1 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        popular_destination4 = new javax.swing.JPanel();
        btn_detail4 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        popular_destination5 = new javax.swing.JPanel();
        btn_detail5 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding luar untuk jPanel3

        labelNama.setText(" Selamat Datang");

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel1.setText("Cari Cepat");

        pilih_travelers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Travelers", "1 Orang", "2 Orang", "3 Orang", "4 Orang", "5 Orang" }));
        pilih_travelers.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pilih_travelersFocusGained(evt);
            }
        });
        pilih_travelers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilih_travelersActionPerformed(evt);
            }
        });

        tombolCari.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        tombolCari.setText("Cari");
        tombolCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolCariActionPerformed(evt);
            }
        });

        btn_CustomTrip.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btn_CustomTrip.setText("Custom Trip");
        btn_CustomTrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CustomTripActionPerformed(evt);
            }
        });

        destinasi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        destinasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinasiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(destinasi, 0, 170, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jDateChooserCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(pilih_travelers, 0, 150, Short.MAX_VALUE)
                        .addGap(28, 28, 28)
                        .addComponent(tombolCari, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_CustomTrip, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooserCalendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(destinasi)
                    .addComponent(pilih_travelers)
                    .addComponent(tombolCari, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(btn_CustomTrip, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel2.setText("Perjalanan Sebelumnya");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Tanggal:");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Nama Kota:");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("Harga:");

        jLabel11.setBackground(new java.awt.Color(153, 153, 153));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("FOTO");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 80));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setPreferredSize(new java.awt.Dimension(0, 149));

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel8.setText("Penawaran Spesial");

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setText("Pesan Sekarang");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jLabel3.setText("Diskon 10% untuk destinasi");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("Nama Kota - ... hari - ... orang ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));

        jButton4.setText("Pesan Sekarang");
        jButton4.addActionListener(evt -> jButton4ActionPerformed(evt));

        jLabel9.setText("Diskon 15% untuk destinasi");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setText("Nama Kota - ... hari - ... orang ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel4.setText("Destinasi Populer");

        popular_destination1.setBackground(new java.awt.Color(204, 204, 204));
        popular_destination1.setPreferredSize(new java.awt.Dimension(200, 112)); // Adjusted preferred width

        btn_detail1.setText("Detail");
        btn_detail1.addActionListener(evt -> btn_detail1ActionPerformed(evt));

        jButton13.setText("Booking Cepat");
        jButton13.addActionListener(evt -> jButton13ActionPerformed(evt));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Nama Kota - ... hari - ... orang ");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Rating:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Harga:");

        jLabel10.setBackground(new java.awt.Color(153, 153, 153));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("FOTO");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.setPreferredSize(new java.awt.Dimension(120, 90)); // Adjusted image placeholder size

        javax.swing.GroupLayout popular_destination1Layout = new javax.swing.GroupLayout(popular_destination1);
        popular_destination1.setLayout(popular_destination1Layout);
        popular_destination1Layout.setHorizontalGroup(
            popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE) // Adjusted spacing
                .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_detail1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        popular_destination1Layout.setVerticalGroup(
            popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(popular_destination1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                    .addGroup(popular_destination1Layout.createSequentialGroup()
                        .addComponent(btn_detail1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        popular_destination4.setBackground(new java.awt.Color(204, 204, 204));
        popular_destination4.setPreferredSize(new java.awt.Dimension(200, 112));

        btn_detail4.setText("Detail");
        btn_detail4.addActionListener(evt -> btn_detail4ActionPerformed(evt));

        jButton14.setText("Booking Cepat");
        jButton14.addActionListener(evt -> jButton14ActionPerformed(evt));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setText("Nama Kota - ... hari - ... orang ");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setText("Rating:");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setText("Harga:");

        jLabel12.setBackground(new java.awt.Color(153, 153, 153));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("FOTO");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel12.setPreferredSize(new java.awt.Dimension(120, 90));

        javax.swing.GroupLayout popular_destination4Layout = new javax.swing.GroupLayout(popular_destination4);
        popular_destination4.setLayout(popular_destination4Layout);
        popular_destination4Layout.setHorizontalGroup(
            popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_detail4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        popular_destination4Layout.setVerticalGroup(
            popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(popular_destination4Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33))
                    .addGroup(popular_destination4Layout.createSequentialGroup()
                        .addComponent(btn_detail4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        popular_destination5.setBackground(new java.awt.Color(204, 204, 204));
        popular_destination5.setPreferredSize(new java.awt.Dimension(200, 112));

        btn_detail5.setText("Detail");
        btn_detail5.addActionListener(evt -> btn_detail5ActionPerformed(evt));

        jButton16.setText("Booking Cepat");
        jButton16.addActionListener(evt -> jButton16ActionPerformed(evt));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setText("Nama Kota - ... hari - ... orang ");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setText("Rating:");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel36.setText("Harga:");

        jLabel13.setBackground(new java.awt.Color(153, 153, 153));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("FOTO");
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel13.setPreferredSize(new java.awt.Dimension(120, 90));

        javax.swing.GroupLayout popular_destination5Layout = new javax.swing.GroupLayout(popular_destination5);
        popular_destination5.setLayout(popular_destination5Layout);
        popular_destination5Layout.setHorizontalGroup(
            popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_detail5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        popular_destination5Layout.setVerticalGroup(
            popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(popular_destination5Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36))
                    .addGroup(popular_destination5Layout.createSequentialGroup()
                        .addComponent(btn_detail5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(popular_destination1, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                    .addComponent(popular_destination4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                    .addComponent(popular_destination5, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(popular_destination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(popular_destination4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(popular_destination5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel7);

        // Layout untuk jPanel3 (panel konten utama)
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelNama)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)) // Adjusted height for jPanel2
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE) // Adjusted height
                .addContainerGap())
        );
        
        add(jPanel3, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void pilih_travelersActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        int selectedIndex = pilih_travelers.getSelectedIndex();
        if (selectedIndex > 0 && !pilih_travelers.getSelectedItem().toString().equals(PLACEHOLDER_TRAVELERS)) {
            jumlahTravelerDipilih = pilih_travelers.getSelectedItem().toString();
            System.out.println("Jumlah traveler dipilih: " + jumlahTravelerDipilih);
            pilih_travelers.setForeground(AppTheme.INPUT_TEXT); // Kembalikan warna teks normal
        } else {
            jumlahTravelerDipilih = null;
            if (selectedIndex == 0) { // Jika placeholder yang dipilih
                 pilih_travelers.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
            }
        }
    }                                                

    private void pilih_travelersFocusGained(java.awt.event.FocusEvent evt) {                                            
        //
    }                                           

    private void tombolCariActionPerformed(java.awt.event.ActionEvent evt) {                                           
        boolean valid = true;
        if(destinasi.getSelectedIndex() == 0 || destinasi.getSelectedItem().toString().equals(PLACEHOLDER_DESTINASI)) {
            JOptionPane.showMessageDialog(this, "Masukkan Destinasi Anda", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            destinasi.requestFocus();
            destinasi.setBorder(AppTheme.createFocusBorder()); // Tunjukkan error
            valid = false;
        } else {
            destinasi.setBorder(AppTheme.createDefaultInputBorder());
        }

        if(tanggalDipilih == null && valid) {
            JOptionPane.showMessageDialog(this, "Pilih Tanggal Terlebih Dahulu", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            jDateChooserCalendar.requestFocus();
            ((com.toedter.calendar.JTextFieldDateEditor) jDateChooserCalendar.getDateEditor()).setBorder(AppTheme.createFocusBorder());
            valid = false;
        } else if (tanggalDipilih != null) {
             ((com.toedter.calendar.JTextFieldDateEditor) jDateChooserCalendar.getDateEditor()).setBorder(AppTheme.createDefaultInputBorder());
        }

        if((pilih_travelers.getSelectedIndex() == 0 || pilih_travelers.getSelectedItem().toString().equals(PLACEHOLDER_TRAVELERS)) && valid){
            JOptionPane.showMessageDialog(this,"Silahkan Pilih Jumlah Travelers", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            pilih_travelers.requestFocus();
            pilih_travelers.setBorder(AppTheme.createFocusBorder());
            valid = false;
        } else if (pilih_travelers.getSelectedIndex() != 0) {
            pilih_travelers.setBorder(AppTheme.createDefaultInputBorder());
        }
        
        if (valid) {
            String destinasiTerpilih = daftarDestinasi.get(destinasi.getSelectedIndex() - 1).getNamaDestinasi();
            String tanggalStr = new SimpleDateFormat("yyyy-MM-dd").format(tanggalDipilih);
            String travelers = jumlahTravelerDipilih;

            System.out.println("Destinasi: " + destinasiTerpilih);
            System.out.println("Tanggal: " + tanggalStr);
            System.out.println("Travelers: " + travelers);

            SearchResult hasil = new SearchResult(destinasiTerpilih, tanggalStr, travelers);
            hasil.setVisible(true);
        }
    }                                          

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
         JOptionPane.showMessageDialog(this, "Tombol Pesan Sekarang (Penawaran 1) diklik!");
    }                                        

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
         JOptionPane.showMessageDialog(this, "Tombol Pesan Sekarang (Penawaran 2) diklik!");
    }                                        

    private void btn_CustomTripActionPerformed(java.awt.event.ActionEvent evt) {                                               
        new DestinationStep().setVisible(true); 
    }                                              

    private void destinasiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int selectedIndex = destinasi.getSelectedIndex();
        if (selectedIndex > 0 && daftarDestinasi != null && selectedIndex <= daftarDestinasi.size() && !destinasi.getSelectedItem().toString().equals(PLACEHOLDER_DESTINASI)) {
            DestinasiModel selectedDest = daftarDestinasi.get(selectedIndex - 1); 
            System.out.println("Dipilih: " + selectedDest.getNamaDestinasi());
            destinasi.setForeground(AppTheme.INPUT_TEXT); // Kembalikan warna teks normal
        } else if (selectedIndex == 0) { // Jika placeholder yang dipilih
            destinasi.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        }
    }                                         

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        JOptionPane.showMessageDialog(this, "Tombol Booking Cepat (Populer 1) diklik!");
    }                                         

    private void btn_detail1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // Ambil data paket terkait jika perlu, lalu kirim ke TripDetail
        // Untuk sekarang, hanya buka TripDetail kosong
        new TripDetail().setVisible(true); 
    }                                           

    private void btn_detail4ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        new TripDetail().setVisible(true); 
    }                                           

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        JOptionPane.showMessageDialog(this, "Tombol Booking Cepat (Populer 2) diklik!");
    }                                         

    private void btn_detail5ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        new TripDetail().setVisible(true); 
    }                                           

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {                                          
       JOptionPane.showMessageDialog(this, "Tombol Booking Cepat (Populer 3) diklik!");
    }      


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser jDateChooserCalendar;
    private javax.swing.JButton btn_CustomTrip;
    private javax.swing.JButton btn_detail1;
    private javax.swing.JButton btn_detail4;
    private javax.swing.JButton btn_detail5;
    private javax.swing.JComboBox<String> destinasi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelNama;
    private javax.swing.JComboBox<String> pilih_travelers;
    private javax.swing.JPanel popular_destination1;
    private javax.swing.JPanel popular_destination4;
    private javax.swing.JPanel popular_destination5;
    private javax.swing.JButton tombolCari;
    // End of variables declaration//GEN-END:variables
}
