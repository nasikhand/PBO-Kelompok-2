/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package managementtrevel.HomeUser;

import Asset.SidebarPanel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import managementtrevel.CustomTripBuilder.DestinationStep;

import managementtrevel.LoginAndRegist.LoginUser;   
import managementtrevel.SearchResultScreen.SearchResult;
import managementtrevel.TripDetailScreen.TripDetail;
import model.Session;

import db.Koneksi;
import db.dao.KotaDAO;
import controller.CariCepatController;
import controller.PaketPerjalananController;
import model.DestinasiModel;
import model.KotaModel;
import model.PaketPerjalananModel;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.awt.Image;
import java.io.File;



/**
 *
 * @author aldy
 */
public class HomeScreen extends javax.swing.JFrame {

    private boolean isSidebarVisible = false;
    private List<KotaModel> daftarDestinasi;
    private Date tanggalDipilih;
    private String jumlahTravelerDipilih = null;
    private PaketPerjalananController paketPerjalananController;


    
    public void loadTopRated(PaketPerjalananModel paket) {
        
        if (paket != null) {
            String namaKota = getNamaKotaById(paket.getKotaId());
            long durasi = paket.getDurasi();

            jLabel20.setText(namaKota + " - " + durasi + " hari - " + paket.getKuota() + " orang");
            jLabel21.setText("Rating: " + paket.getRating());
            jLabel22.setText("Harga: Rp " + String.format("%,.0f", paket.getHarga()));

            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir).getParentFile();

            String gambarRelatif = paket.getGambar();
            if (gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            }

            File gambarFile = new File(baseDir, gambarRelatif);

            if (gambarFile.exists()) {
                ImageIcon icon = new ImageIcon(
                    new ImageIcon(gambarFile.getAbsolutePath())
                    .getImage()
                    .getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH)
                );
                jLabel10.setIcon(icon);
                jLabel10.setText("");
            } else {
                System.out.println("Gambar paket tidak ditemukan: " + gambarFile.getAbsolutePath());
                jLabel10.setText("Gambar tidak tersedia");
            }
        } else {
            jLabel20.setText("Data tidak ditemukan");
        }
    }

    public void loadTopRated2(PaketPerjalananModel paket) {
        if (paket != null) {
            // Set teks label
            String namaKota = getNamaKotaById(paket.getKotaId());
            long durasi = paket.getDurasi();

            jLabel31.setText(namaKota + " - " + durasi + " hari - " + paket.getKuota() + " orang");
            jLabel32.setText("Rating: " + paket.getRating());
            jLabel33.setText("Harga: Rp " + String.format("%,.0f", paket.getHarga()));

            // Set gambar
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            }
            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir).getParentFile();

            File gambarFile = new File(baseDir, gambarRelatif);
            if (gambarFile.exists()) {
                ImageIcon icon = new ImageIcon(new ImageIcon(gambarFile.getAbsolutePath())
                    .getImage().getScaledInstance(jLabel12.getWidth(), jLabel12.getHeight(), Image.SCALE_SMOOTH));
                jLabel12.setIcon(icon);
                jLabel12.setText("");
            } else {
                jLabel12.setText("Gambar tidak tersedia");
                System.out.println("Gambar paket tidak ditemukan: " + gambarFile.getAbsolutePath());
            }
        } else {
            jLabel31.setText("Data tidak ditemukan");
            jLabel12.setText("FOTO");
            jLabel12.setIcon(null);
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

            String gambarRelatif = paket.getGambar();
            if (gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            }

            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir).getParentFile();
            File gambarFile = new File(baseDir, gambarRelatif);

            if (gambarFile.exists()) {
                ImageIcon icon = new ImageIcon(
                    new ImageIcon(gambarFile.getAbsolutePath())
                    .getImage()
                    .getScaledInstance(jLabel13.getWidth(), jLabel13.getHeight(), Image.SCALE_SMOOTH)
                );
                jLabel13.setIcon(icon);
                jLabel13.setText(""); // Hapus teks FOTO jika gambar ada
            } else {
                jLabel13.setIcon(null);
                jLabel13.setText("Gambar tidak tersedia");
                System.out.println("Gambar paket tidak ditemukan: " + gambarFile.getAbsolutePath());
            }
        } else {
            jLabel34.setText("Data tidak ditemukan");
            jLabel35.setText("Rating:");
            jLabel36.setText("Harga:");
            jLabel13.setIcon(null);
            jLabel13.setText("FOTO");
        }
    }



    

    private String getNamaKotaById(int kotaId) {
        // implementasi mengambil nama kota dari ID
        return new KotaDAO().getNamaKotaById(kotaId); // atau sesuai DAO kamu
    }

    
    /**
     * Creates new form Home
     */
    public HomeScreen() {
    initComponents();
    
    if (Session.currentUser != null) {
        labelNama.setText("Selamat Datang, " + Session.currentUser.getNamaLengkap());
    } else {
        // Jika belum login, bisa arahkan ke login
        new LoginUser().setVisible(true);
        this.dispose();
    }
    
    // Ambil koneksi dan data destinasi
    Connection conn = Koneksi.getConnection();
    CariCepatController controller = new CariCepatController(conn);
    

    // Ambil data destinasi dari controller
    this.daftarDestinasi = controller.getDaftarKota();

    // Kosongkan combo box dulu
    destinasi.removeAllItems();

    // Tambahkan destinasi ke combo box, setelah data diisi
    destinasi.addItem("-- Pilih Destinasi --");
    for (KotaModel dest : daftarDestinasi) {
        destinasi.addItem(dest.getNamaKota());
    }

    // inisialisasi dan event listener calendar
    Calendar.getDateEditor().addPropertyChangeListener(evt -> {
        if ("date".equals(evt.getPropertyName())) {
            tanggalDipilih = Calendar.getDate();
            System.out.println("Tanggal dipilih: " + tanggalDipilih);
        }
    });


    // Inisialisasi paketPerjalananController dengan koneksi
    paketPerjalananController = new PaketPerjalananController(conn);
    
    List<PaketPerjalananModel> topRated = paketPerjalananController.getTopRatedPakets(3);

    if (topRated.size() > 0) {
        loadTopRated(topRated.get(0));
    }
    if (topRated.size() > 1) {
        loadTopRated2(topRated.get(1));
    }
    if (topRated.size() > 2) {
        loadTopRated3(topRated.get(2)); // Jangan lupa buat method ini jika belum ada
    }

    
    setTitle("Halaman Utama");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setLayout(null);

    // Buat layeredPane
    JLayeredPane layeredPane = getLayeredPane();

    // Tambahkan sidebar ke layeredPane
    // SidebarPanel sidebar = new SidebarPanel();
    // sidebar.setBounds(0, 0, 65, getHeight()); // atur ukuran dan posisi sidebar
    // layeredPane.add(sidebar, JLayeredPane.POPUP_LAYER); // tambahkan di atas layer default
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        Calendar = new com.toedter.calendar.JDateChooser();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        jPanel3.setPreferredSize(new java.awt.Dimension(740, 610));

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(326, 326, 326))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(destinasi, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(Calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(pilih_travelers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tombolCari)
                .addGap(26, 26, 26)
                .addComponent(btn_CustomTrip, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(destinasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Calendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(pilih_travelers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tombolCari)
                                .addComponent(btn_CustomTrip)))))
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel28))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel2)
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jPanel2.setPreferredSize(new java.awt.Dimension(0, 149));

        jLabel8.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel8.setText("Penawaran Spesial");

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setText("Pesan Sekarang");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                        .addGap(79, 79, 79))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 204));

        jButton4.setText("Pesan Sekarang");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

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
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(79, 79, 79))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(137, 137, 137))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel4.setText("Destinasi Populer");

        popular_destination1.setBackground(new java.awt.Color(204, 204, 204));
        popular_destination1.setPreferredSize(new java.awt.Dimension(731, 112));

        btn_detail1.setText("Detail");
        btn_detail1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detail1ActionPerformed(evt);
            }
        });

        jButton13.setText("Booking Cepat");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout popular_destination1Layout = new javax.swing.GroupLayout(popular_destination1);
        popular_destination1.setLayout(popular_destination1Layout);
        popular_destination1Layout.setHorizontalGroup(
            popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13)
                    .addComponent(btn_detail1))
                .addGap(105, 105, 105))
        );
        popular_destination1Layout.setVerticalGroup(
            popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_detail1)
                .addGap(28, 28, 28)
                .addComponent(jButton13)
                .addGap(16, 16, 16))
            .addGroup(popular_destination1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popular_destination1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(popular_destination1Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel21)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel22)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        popular_destination4.setBackground(new java.awt.Color(204, 204, 204));
        popular_destination4.setPreferredSize(new java.awt.Dimension(731, 112));

        btn_detail4.setText("Detail");
        btn_detail4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detail4ActionPerformed(evt);
            }
        });

        jButton14.setText("Booking Cepat");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout popular_destination4Layout = new javax.swing.GroupLayout(popular_destination4);
        popular_destination4.setLayout(popular_destination4Layout);
        popular_destination4Layout.setHorizontalGroup(
            popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton14)
                    .addComponent(btn_detail4))
                .addGap(105, 105, 105))
        );
        popular_destination4Layout.setVerticalGroup(
            popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_detail4)
                .addGap(28, 28, 28)
                .addComponent(jButton14)
                .addGap(16, 16, 16))
            .addGroup(popular_destination4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popular_destination4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(popular_destination4Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel33)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        popular_destination5.setBackground(new java.awt.Color(204, 204, 204));
        popular_destination5.setPreferredSize(new java.awt.Dimension(731, 112));

        btn_detail5.setText("Detail");
        btn_detail5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detail5ActionPerformed(evt);
            }
        });

        jButton16.setText("Booking Cepat");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout popular_destination5Layout = new javax.swing.GroupLayout(popular_destination5);
        popular_destination5.setLayout(popular_destination5Layout);
        popular_destination5Layout.setHorizontalGroup(
            popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
                .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton16)
                    .addComponent(btn_detail5))
                .addGap(105, 105, 105))
        );
        popular_destination5Layout.setVerticalGroup(
            popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popular_destination5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_detail5)
                .addGap(28, 28, 28)
                .addComponent(jButton16)
                .addGap(16, 16, 16))
            .addGroup(popular_destination5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popular_destination5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(popular_destination5Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel36)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(jLabel4)
                .addContainerGap(359, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(popular_destination1, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                    .addComponent(popular_destination4, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
                    .addComponent(popular_destination5, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(popular_destination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(popular_destination4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(popular_destination5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel7);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(labelNama, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(labelNama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 65, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pilih_travelersActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = pilih_travelers.getSelectedIndex();
    
        // Indeks 0 adalah "Travelers" (default)
        if (selectedIndex > 0) {
            jumlahTravelerDipilih = pilih_travelers.getSelectedItem().toString();
            System.out.println("Jumlah traveler dipilih: " + jumlahTravelerDipilih);
        } else {
            jumlahTravelerDipilih = null;
        }
//GEN-FIRST:event_pilih_travelersActionPerformed
 
    }//GEN-LAST:event_pilih_travelersActionPerformed

    private void pilih_travelersFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pilih_travelersFocusGained

    }//GEN-LAST:event_pilih_travelersFocusGained

    private void tombolCariActionPerformed(java.awt.event.ActionEvent evt) {
//GEN-FIRST:event_tombolCariActionPerformed
        if(destinasi.getSelectedIndex() == 0) {  // 0 adalah "-- Pilih Destinasi --"
        JOptionPane.showMessageDialog(null, "Masukkan Destinasi Anda");
        destinasi.requestFocus();
        } else if(tanggalDipilih == null) {
        JOptionPane.showMessageDialog(null, "Pilih Tanggal Terlebih Dahulu");
        Calendar.requestFocus();
        } else if(pilih_travelers.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(null,"Silahkan Pilih Jumlah Travelers");
            pilih_travelers.requestFocus();
        } else {
            // Ambil data setelah validasi
            String destinasiTerpilih = daftarDestinasi.get(destinasi.getSelectedIndex() - 1).getNamaKota();
            String tanggalStr = new SimpleDateFormat("yyyy-MM-dd").format(tanggalDipilih);
            String travelers = jumlahTravelerDipilih; // Ini sudah diset di actionPerformed sebelumnya

            // Cek hasilnya di console
            System.out.println("Destinasi: " + destinasiTerpilih);
            System.out.println("Tanggal: " + tanggalStr);
            System.out.println("Travelers: " + travelers);

            // Bisa lempar data ke layar berikutnya (misal lewat constructor)
            SearchResult hasil = new SearchResult(destinasiTerpilih, tanggalStr, travelers);
            hasil.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_tombolCariActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btn_CustomTripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CustomTripActionPerformed
        new DestinationStep().show();
        this.dispose();
    }//GEN-LAST:event_btn_CustomTripActionPerformed

    private void destinasiActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedIndex = destinasi.getSelectedIndex();

        if (selectedIndex > 0 && daftarDestinasi != null) {
            KotaModel selectedDest = daftarDestinasi.get(selectedIndex - 1); 
            System.out.println("Dipilih: " + selectedDest.getNamaKota());
            // Tambahkan aksi lain jika perlu, misalnya tampilkan detail ke textfield
        }
//GEN-FIRST:event_destinasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_destinasiActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    private void btn_detail1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detail1ActionPerformed
        new TripDetail().show();
        this.dispose();
    }//GEN-LAST:event_btn_detail1ActionPerformed

    private void btn_detail4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detail4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_detail4ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void btn_detail5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_detail5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_detail5ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    /**
     * @param args the command line arguments
     */
    // public static void main(String args[]) {
    //     /* Set the Nimbus look and feel */
    //     //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    //     /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
    //      * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
    //      */
    //     try {
    //         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
    //             if ("Nimbus".equals(info.getName())) {
    //                 javax.swing.UIManager.setLookAndFeel(info.getClassName());
    //                 break;
    //             }
    //         }
    //     } catch (ClassNotFoundException ex) {
    //         java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //     } catch (InstantiationException ex) {
    //         java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //     } catch (IllegalAccessException ex) {
    //         java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //     } catch (javax.swing.UnsupportedLookAndFeelException ex) {
    //         java.util.logging.Logger.getLogger(HomeScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    //     }
        

    //     /* Create and display the form */
    //     java.awt.EventQueue.invokeLater(new Runnable() {
    //         public void run() {
    //             new HomeScreen().setVisible(true);
    //         }
    //     });
    // }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser Calendar;
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
