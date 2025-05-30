package Asset; // Pastikan nama package ini sesuai dengan struktur proyek Anda

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import managementtrevel.MainAppFrame; // Impor MainAppFrame

public class SidebarPanel extends JPanel {
    private final int COLLAPSED_WIDTH = 60;
    private final int EXPANDED_WIDTH = 200;
    private final int ANIMATION_DELAY = 10;
    private final int ANIMATION_STEP = 15;

    private int currentWidth = COLLAPSED_WIDTH;
    private Timer animationTimer;
    private boolean isExpanded = false;

    private JButton homeButton;
    private JButton profileButton;
    private JButton logoutButton;
    // Tambahkan tombol lain jika perlu, contoh:
    // private JButton destinationsButton; 
    // private JButton bookingsButton;

    private MainAppFrame mainAppFrame;
    private final int ICON_WIDTH = 24;  // Lebar ikon yang diinginkan
    private final int ICON_HEIGHT = 24; // Tinggi ikon yang diinginkan

    public SidebarPanel(MainAppFrame frame) {
        this.mainAppFrame = frame;

        setLayout(null);
        setBackground(AppTheme.SIDEPANEL_BACKGROUND);
        setPreferredSize(new Dimension(COLLAPSED_WIDTH, 100)); 
        setOpaque(true);

        homeButton = new JButton();
        profileButton = new JButton();
        logoutButton = new JButton();
        // Contoh tombol tambahan:
        // destinationsButton = new JButton();
        // bookingsButton = new JButton();


        // Ganti path ke file .png Anda
        setupButton(homeButton, "Home", "/Asset/icon/home.png", 20, MainAppFrame.PANEL_BERANDA);
        setupButton(profileButton, "Profil", "/Asset/icon/profile.png", 70, MainAppFrame.PANEL_USER_PROFILE);
        // Jika ada tombol lain:
        // setupButton(destinationsButton, "Destinasi", "/Asset/icon/destinations.png", 120, MainAppFrame.PANEL_DESTINASI);
        // setupButton(bookingsButton, "Pemesanan", "/Asset/icon/bookings.png", 170, MainAppFrame.PANEL_PEMESANAN);
        // Sesuaikan nilai Y (posisi vertikal) untuk tombol logout jika ada tombol lain di atasnya
        setupLogoutButton(logoutButton, "Logout", "/Asset/icon/logout.png", 120); // Misal, jika hanya 3 tombol, Y tetap 120. Jika 5 tombol, Y bisa jadi 220.


        setButtonLabelsAndAlignment(isExpanded); // Panggil ini setelah semua tombol di-setup
        this.addMouseListener(new HoverHandler());
    }

    /**
     * Memuat ikon raster (JPG, PNG) dari path resource dan mengubah ukurannya.
     * @param imagePath Path ke file gambar di dalam resources (misal, "/Asset/icon/nama.png")
     * @param width Lebar ikon yang diinginkan
     * @param height Tinggi ikon yang diinginkan
     * @return ImageIcon yang sudah di-scale, atau null jika gagal
     */
    private ImageIcon loadRasterIcon(String imagePath, int width, int height) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("Resource gambar tidak ditemukan: " + imagePath);
                return null;
            }

            ImageIcon originalIcon = new ImageIcon(imageUrl);
             if (originalIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                 System.err.println("Gagal memuat gambar sepenuhnya dari: " + imagePath);
                // Meskipun gagal load penuh, coba scale. Kadang masih bisa bekerja sebagian.
                // return null; // Aktifkan jika ingin strict
            }

            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        } catch (Exception e) {
            System.err.println("Error saat memuat atau mengubah ukuran gambar (" + imagePath + "): " + e.getMessage());
            return null;
        }
    }

    private void setupButton(JButton button, String text, String iconPath, int y, String panelNameTarget) {
        ImageIcon icon = loadRasterIcon(iconPath, ICON_WIDTH, ICON_HEIGHT);
        if (icon != null) {
            button.setIcon(icon);
        } else {
            // Fallback jika ikon gagal dimuat: gunakan huruf pertama dari teks
            button.setText(text.length() > 0 ? text.substring(0, 1) : "?");
            button.setFont(AppTheme.FONT_PRIMARY_BOLD); // Font tebal untuk fallback teks
        }

        button.setForeground(AppTheme.SIDEPANEL_TEXT_INACTIVE);
        button.setFont(AppTheme.FONT_PRIMARY_MEDIUM); // Font utama untuk teks saat expanded
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Bounds awal, akan disesuaikan oleh setButtonLabelsAndAlignment
        button.setBounds(0, y, EXPANDED_WIDTH, 40); 
        button.putClientProperty("originalText", text);

        button.addActionListener(e -> {
            if (mainAppFrame != null) {
                mainAppFrame.showPanel(panelNameTarget);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isExpanded) {
                    startAnimation(true);
                }
                button.setForeground(AppTheme.SIDEPANEL_TEXT_ACTIVE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(AppTheme.SIDEPANEL_TEXT_INACTIVE);
            }
        });
        add(button);
    }

    private void setupLogoutButton(JButton button, String text, String iconPath, int y) {
        ImageIcon icon = loadRasterIcon(iconPath, ICON_WIDTH, ICON_HEIGHT);
        if (icon != null) {
            button.setIcon(icon);
        } else {
            button.setText(text.length() > 0 ? text.substring(0, 1) : "L");
            button.setFont(AppTheme.FONT_PRIMARY_BOLD);
        }

        button.setForeground(AppTheme.SIDEPANEL_TEXT_INACTIVE);
        button.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBounds(0, y, EXPANDED_WIDTH, 40);
        button.putClientProperty("originalText", text);

        button.addActionListener(e -> {
            if (mainAppFrame != null) {
                mainAppFrame.performLogout();
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isExpanded) {
                    startAnimation(true);
                }
                button.setForeground(AppTheme.ACCENT_ORANGE); // Warna khusus untuk logout hover
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(AppTheme.SIDEPANEL_TEXT_INACTIVE);
            }
        });
        add(button);
    }

    private void startAnimation(boolean expand) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        final int targetWidth = expand ? EXPANDED_WIDTH : COLLAPSED_WIDTH;

        animationTimer = new Timer(ANIMATION_DELAY, null);
        animationTimer.addActionListener(e -> {
            if (expand) {
                currentWidth += ANIMATION_STEP;
                if (currentWidth >= targetWidth) {
                    currentWidth = targetWidth;
                    animationTimer.stop();
                    isExpanded = true;
                    setButtonLabelsAndAlignment(true);
                }
            } else {
                currentWidth -= ANIMATION_STEP;
                if (currentWidth <= targetWidth) {
                    currentWidth = targetWidth;
                    animationTimer.stop();
                    isExpanded = false;
                    setButtonLabelsAndAlignment(false);
                }
            }
            if (getParent() != null) {
                 setPreferredSize(new Dimension(currentWidth, getParent().getHeight()));
            } else {
                 setPreferredSize(new Dimension(currentWidth, (int)getPreferredSize().getHeight()));
            }
            revalidate(); 
        });
        animationTimer.start();
    }

    private void setButtonLabelsAndAlignment(boolean expanded) {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String originalText = (String) btn.getClientProperty("originalText");
                if (originalText == null) originalText = ""; 

                if (expanded) {
                    btn.setText(originalText);
                    btn.setHorizontalAlignment(SwingConstants.LEFT);
                    btn.setHorizontalTextPosition(SwingConstants.RIGHT); 
                    btn.setIconTextGap(15); 
                    btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); 
                    btn.setBounds(0, btn.getY(), EXPANDED_WIDTH, btn.getHeight()); // Posisi normal saat expanded
                } else { // Collapsed
                    btn.setText(null); // Hanya ikon
                    btn.setHorizontalAlignment(SwingConstants.CENTER); // Pusatkan ikon di dalam tombol
                    btn.setHorizontalTextPosition(SwingConstants.CENTER); 
                    btn.setIconTextGap(0);
                    btn.setBorder(null); 
                    // Atur bounds tombol agar mengisi lebar sidebar yang collapsed, ikon akan otomatis terpusat
                    btn.setBounds(0, btn.getY(), COLLAPSED_WIDTH, btn.getHeight());
                }
            }
        }
    }

    public boolean isExpandedPublic() {
        return isExpanded;
    }

    public void collapsePublic() {
        if (isExpanded) {
            startAnimation(false);
        }
    }

    private class HoverHandler extends MouseAdapter {
        private Timer exitTimer;

        @Override
        public void mouseEntered(MouseEvent e) {
            if (exitTimer != null && exitTimer.isRunning()) {
                exitTimer.stop();
            }
            if (!isExpanded) {
                startAnimation(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (exitTimer != null && exitTimer.isRunning()) {
                exitTimer.stop();
            }
            exitTimer = new Timer(200, ae -> {
                if (SidebarPanel.this.isShowing()) { 
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    SwingUtilities.convertPointFromScreen(p, SidebarPanel.this);
                    if (!SidebarPanel.this.contains(p)) { 
                        if (isExpanded) {
                            startAnimation(false);
                        }
                    }
                }
            });
            exitTimer.setRepeats(false);
            exitTimer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isExpanded) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(AppTheme.BORDER_COLOR.darker()); 
            g2d.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
            g2d.dispose();
        }
    }
}
