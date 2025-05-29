/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Admin;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardView extends JFrame {

    // Definisi warna untuk konsistensi tema
    private final Color PANEL_LATAR = new Color(33, 42, 53);
    private final Color PANEL_SAMPING_LATAR = new Color(40, 50, 63);
    private final Color WARNA_AKSEN = new Color(2, 117, 216);

    // Komponen utama untuk layout
    private JPanel panelKontenUtama;
    private CardLayout cardLayout;

    // Peta untuk menyimpan panel yang sudah dibuat (untuk Lazy Loading)
    private Map<String, JPanel> panelCache = new HashMap<>();

    public AdminDashboardView(Admin admin) {
        // Pengaturan Frame Utama
        setTitle("Dasbor Administrator - Sinar Jaya Travel");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Latar Belakang Utama
        JPanel panelLatar = new JPanel(new BorderLayout());
        panelLatar.setBackground(PANEL_LATAR);
        setContentPane(panelLatar);

        // Membuat Panel Navigasi Samping
        JPanel panelNavigasiSamping = buatPanelNavigasiSamping(admin);
        panelLatar.add(panelNavigasiSamping, BorderLayout.WEST);

        // Membuat Panel Konten Utama dengan CardLayout
        cardLayout = new CardLayout();
        panelKontenUtama = new JPanel(cardLayout);
        panelKontenUtama.setOpaque(false); // Transparan agar latar belakang utama terlihat
        panelLatar.add(panelKontenUtama, BorderLayout.CENTER);

        // Hanya buat dan tampilkan panel beranda dasbor saat pertama kali dibuka
        JPanel panelBerandaDasbor = buatPanelBerandaDasbor();
        panelKontenUtama.add(panelBerandaDasbor, "DASBOR");
        panelCache.put("DASBOR", panelBerandaDasbor); // Simpan panel ke cache

        // Tampilkan dasbor sebagai default
        cardLayout.show(panelKontenUtama, "DASBOR");
    }

    /**
     * Membuat panel navigasi samping yang berisi judul, daftar menu, dan info admin.
     * @param admin Objek admin yang sedang login.
     * @return JPanel yang sudah jadi.
     */
    private JPanel buatPanelNavigasiSamping(Admin admin) {
        JPanel panelSamping = new JPanel();
        panelSamping.setLayout(new BoxLayout(panelSamping, BoxLayout.Y_AXIS));
        panelSamping.setBackground(PANEL_SAMPING_LATAR);
        panelSamping.setPreferredSize(new Dimension(240, 0));

        JLabel judulAplikasi = new JLabel("SINAR JAYA TRAVEL", SwingConstants.CENTER);
        judulAplikasi.setFont(new Font("Segoe UI", Font.BOLD, 18));
        judulAplikasi.setForeground(Color.WHITE);
        judulAplikasi.setAlignmentX(Component.CENTER_ALIGNMENT);
        judulAplikasi.setBorder(new EmptyBorder(20, 10, 20, 10));
        panelSamping.add(judulAplikasi);

        // Menambahkan item-item menu navigasi
        tambahItemNavigasi(panelSamping, "Dasbor", "DASBOR");
        tambahItemNavigasi(panelSamping, "Kelola Perjalanan", "PERJALANAN");
        tambahItemNavigasi(panelSamping, "Kelola Reservasi", "RESERVASI");
        tambahItemNavigasi(panelSamping, "Kelola Pengguna", "PENGGUNA");
        tambahItemNavigasi(panelSamping, "Kelola Laporan", "LAPORAN");

        panelSamping.add(Box.createVerticalGlue()); // Mendorong info admin ke bawah

        // Info admin dan tombol logout
        JLabel labelAdmin = new JLabel(admin.getNamaLengkap(), SwingConstants.CENTER);
        labelAdmin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelAdmin.setForeground(Color.WHITE);
        labelAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelKeluar = new JLabel("Keluar", SwingConstants.CENTER);
        labelKeluar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelKeluar.setForeground(WARNA_AKSEN);
        labelKeluar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelKeluar.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelKeluar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        panelSamping.add(labelAdmin);
        panelSamping.add(labelKeluar);
        panelSamping.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return panelSamping;
    }

    /**
     * Helper untuk membuat satu item menu navigasi yang interaktif dan menerapkan lazy loading.
     * @param parent Panel induk tempat item ini akan ditambahkan.
     * @param text Teks yang akan ditampilkan.
     * @param cardName Nama unik untuk CardLayout.
     */
    private void tambahItemNavigasi(JPanel parent, String text, String cardName) {
        JPanel itemNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        itemNav.setBackground(PANEL_SAMPING_LATAR);
        itemNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        itemNav.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel("●");
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        itemNav.add(iconLabel);
        itemNav.add(textLabel);
        
        itemNav.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { itemNav.setBackground(WARNA_AKSEN); }
            @Override
            public void mouseExited(MouseEvent e) { itemNav.setBackground(PANEL_SAMPING_LATAR); }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // Logika LAZY LOADING: Buat panel hanya saat pertama kali diklik
                if (!panelCache.containsKey(cardName)) {
                    System.out.println("Membuat panel baru untuk: " + cardName);
                    JPanel newPanel;
                    switch (cardName) {
                        case "PERJALANAN":
                            newPanel = new KelolaPerjalananView();
                            break;
                        case "RESERVASI":
                            newPanel = new KelolaReservasiView();
                            break;
                        // Tambahkan case lain di sini untuk menu selanjutnya
                        case "PENGGUNA":
                            newPanel = buatPanelPlaceholder("Manajemen Pengguna");
                            break;
                        case "LAPORAN":
                            newPanel = buatPanelPlaceholder("Manajemen Laporan");
                            break;
                        default:
                            newPanel = buatPanelPlaceholder("Segera Hadir");
                            break;
                    }
                    panelKontenUtama.add(newPanel, cardName);
                    panelCache.put(cardName, newPanel);
                }
                cardLayout.show(panelKontenUtama, cardName);
            }
        });
        
        parent.add(itemNav);
    }
    
    /**
     * Membuat panel beranda dasbor yang menampilkan kartu-kartu statistik.
     * @return JPanel yang sudah jadi.
     */
    private JPanel buatPanelBerandaDasbor() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBorder(new EmptyBorder(20, 25, 20, 25));
        panel.setOpaque(false);
        
        JLabel judul = new JLabel("Gambaran Umum Dasbor");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 32));
        judul.setForeground(Color.WHITE);
        panel.add(judul, BorderLayout.NORTH);

        JPanel panelStatistik = new JPanel(new GridLayout(1, 4, 20, 0));
        panelStatistik.setOpaque(false);
        panelStatistik.add(buatKartuStatistik("Total Pendapatan", "Rp 25.4Jt", "Grafik Naik", new Color(0, 184, 148)));
        panelStatistik.add(buatKartuStatistik("Pemesanan", "3,241", "+15% dari minggu lalu", new Color(108, 92, 231)));
        panelStatistik.add(buatKartuStatistik("Perjalanan Aktif", "124", "5 perjalanan mulai hari ini", new Color(0, 206, 201)));
        panelStatistik.add(buatKartuStatistik("Pengguna Baru", "512", "Bergabung bulan ini", new Color(253, 203, 110)));
        panel.add(panelStatistik, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Helper untuk membuat satu kotak statistik.
     */
    private JPanel buatKartuStatistik(String judul, String nilai, String subteks, Color aksen) {
        JPanel kartu = new JPanel();
        kartu.setLayout(new BoxLayout(kartu, BoxLayout.Y_AXIS));
        kartu.setBackground(PANEL_SAMPING_LATAR);
        kartu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(5, 0, 0, 0, aksen),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel labelJudul = new JLabel(judul.toUpperCase());
        labelJudul.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelJudul.setForeground(Color.LIGHT_GRAY);
        kartu.add(labelJudul);
        kartu.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel labelNilai = new JLabel(nilai);
        labelNilai.setFont(new Font("Segoe UI", Font.BOLD, 28));
        labelNilai.setForeground(Color.WHITE);
        kartu.add(labelNilai);
        kartu.add(Box.createVerticalGlue());

        JLabel labelSubteks = new JLabel(subteks);
        labelSubteks.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelSubteks.setForeground(aksen);
        kartu.add(labelSubteks);
        
        return kartu;
    }
    
    /**
     * Membuat panel placeholder untuk fitur yang belum diimplementasikan.
     */
    private JPanel buatPanelPlaceholder(String judul) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        JLabel label = new JLabel(judul);
        label.setFont(new Font("Segoe UI", Font.BOLD, 36));
        label.setForeground(Color.WHITE);
        panel.add(label);
        return panel;
    }
}