package managementtrevel.HomeUser;

// Import yang relevan (beberapa mungkin tidak dibutuhkan lagi jika terkait JFrame)
import Asset.SidebarPanel; // Pastikan path ini benar
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel; // Mengganti JFrame dengan JPanel
import managementtrevel.CustomTripBuilder.DestinationStep;
// import managementtrevel.LoginAndRegist.LoginUser; // Mungkin tidak relevan lagi di sini
import managementtrevel.SearchResultScreen.SearchResult;
import managementtrevel.TripDetailScreen.TripDetail;
import model.Session;
import db.Koneksi;
import db.dao.KotaDAO;
import controller.CariCepatController;
import controller.PaketPerjalananController;
import model.DestinasiModel;
import model.PaketPerjalananModel;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Calendar; // Nama variabel Calendar bentrok dengan java.util.Calendar
import java.util.Date;
import java.awt.Image;
import java.io.File;
import java.awt.Dimension; // Untuk setPreferredSize jika perlu
import javax.swing.SwingUtilities;

/**
 * PanelBeranda adalah representasi JPanel dari HomeScreen.
 * @author aldy (dimodifikasi untuk menjadi JPanel)
 */
public class PanelBeranda extends JPanel { // Diubah dari JFrame ke JPanel

    // Variabel instance tetap sama
    private boolean isSidebarVisible = false;
    private List<DestinasiModel> daftarDestinasi;
    private Date tanggalDipilih;
    private String jumlahTravelerDipilih = null;
    private PaketPerjalananController paketPerjalananController;
    private SidebarPanel sidebar; // Deklarasikan sidebar di sini
    private JLayeredPane layeredPane; // Deklarasikan layeredPane di sini


    // Metode loadTopRated, getNamaKotaById, dll. tetap sama
    public void loadTopRated(PaketPerjalananModel paket) {
        if (paket != null) {
            String namaKota = getNamaKotaById(paket.getKotaId());
            long durasi = paket.getDurasi();

            jLabel20.setText(namaKota + " - " + durasi + " hari - " + paket.getKuota() + " orang");
            jLabel21.setText("Rating: " + paket.getRating());
            jLabel22.setText("Harga: Rp " + String.format("%,.0f", paket.getHarga()));

            String userDir = System.getProperty("user.dir");
            // Coba asumsikan struktur folder proyek Anda, sesuaikan jika perlu
            // Misal, jika gambar ada di folder 'src/Asset/images' relatif terhadap root proyek
            // atau 'travel_app/src/Asset/images'
            File baseDir = new File(userDir); // Atau new File(userDir).getParentFile() jika HomeScreen ada di sub-package
                                            // dan gambar relatif terhadap parent package.
                                            // Ini bagian yang paling sering butuh penyesuaian.

            String gambarRelatif = paket.getGambar();
            // Hapus slash di awal jika ada, karena File akan menggabungkannya dengan benar
            if (gambarRelatif != null && gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            } else if (gambarRelatif == null) {
                System.out.println("Path gambar null untuk paket: " + paket.getNamaPaket());
                jLabel10.setText("Gambar tidak tersedia");
                jLabel10.setIcon(null);
                return;
            }
            
            // Pastikan path gambar tidak absolut dari database, tapi relatif ke proyek.
            // Jika path dari DB adalah 'Asset/images/namafile.jpg', maka ini seharusnya bekerja.
            File gambarFile = new File(baseDir, gambarRelatif);
             // Untuk debugging path:
            System.out.println("Mencoba memuat gambar dari: " + gambarFile.getAbsolutePath());


            if (gambarFile.exists() && jLabel10.getWidth() > 0 && jLabel10.getHeight() > 0) {
                ImageIcon icon = new ImageIcon(
                        new ImageIcon(gambarFile.getAbsolutePath())
                                .getImage()
                                .getScaledInstance(jLabel10.getWidth(), jLabel10.getHeight(), Image.SCALE_SMOOTH)
                );
                jLabel10.setIcon(icon);
                jLabel10.setText("");
            } else {
                 if (!gambarFile.exists()) {
                    System.out.println("Gambar paket tidak ditemukan: " + gambarFile.getAbsolutePath());
                } else {
                    System.out.println("Ukuran jLabel10 belum siap untuk gambar: " + jLabel10.getWidth() + "x" + jLabel10.getHeight());
                }
                jLabel10.setText("Gambar tidak tersedia");
                jLabel10.setIcon(null); // Hapus ikon lama jika ada
            }
        } else {
            jLabel20.setText("Data tidak ditemukan");
            jLabel21.setText("Rating:");
            jLabel22.setText("Harga:");
            jLabel10.setText("FOTO");
            jLabel10.setIcon(null);
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
                 System.out.println("Path gambar null untuk paket: " + paket.getNamaPaket());
                jLabel12.setText("Gambar tidak tersedia");
                jLabel12.setIcon(null);
                return;
            }
            File gambarFile = new File(baseDir, gambarRelatif);
            System.out.println("Mencoba memuat gambar (2) dari: " + gambarFile.getAbsolutePath());


            if (gambarFile.exists() && jLabel12.getWidth() > 0 && jLabel12.getHeight() > 0) {
                ImageIcon icon = new ImageIcon(new ImageIcon(gambarFile.getAbsolutePath())
                        .getImage().getScaledInstance(jLabel12.getWidth(), jLabel12.getHeight(), Image.SCALE_SMOOTH));
                jLabel12.setIcon(icon);
                jLabel12.setText("");
            } else {
                if (!gambarFile.exists()) {
                    System.out.println("Gambar paket (2) tidak ditemukan: " + gambarFile.getAbsolutePath());
                } else {
                    System.out.println("Ukuran jLabel12 belum siap untuk gambar: " + jLabel12.getWidth() + "x" + jLabel12.getHeight());
                }
                jLabel12.setText("Gambar tidak tersedia");
                jLabel12.setIcon(null);
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

            String userDir = System.getProperty("user.dir");
            File baseDir = new File(userDir);
            String gambarRelatif = paket.getGambar();
            if (gambarRelatif != null && gambarRelatif.startsWith("/")) {
                gambarRelatif = gambarRelatif.substring(1);
            } else if (gambarRelatif == null) {
                System.out.println("Path gambar null untuk paket: " + paket.getNamaPaket());
                jLabel13.setText("Gambar tidak tersedia");
                jLabel13.setIcon(null);
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
            } else {
                 if (!gambarFile.exists()) {
                    System.out.println("Gambar paket (3) tidak ditemukan: " + gambarFile.getAbsolutePath());
                } else {
                    System.out.println("Ukuran jLabel13 belum siap untuk gambar: " + jLabel13.getWidth() + "x" + jLabel13.getHeight());
                }
                jLabel13.setIcon(null);
                jLabel13.setText("Gambar tidak tersedia");
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
        return new KotaDAO().getNamaKotaById(kotaId);
    }

    public PanelBeranda() {
        // Inisialisasi JLayeredPane dan SidebarPanel di sini
        layeredPane = new JLayeredPane();
        // Gunakan BorderLayout untuk PanelBeranda agar layeredPane mengisi seluruh area
        this.setLayout(new java.awt.BorderLayout()); 
        this.add(layeredPane, java.awt.BorderLayout.CENTER);

        initComponents(); // Panggil initComponents yang dihasilkan NetBeans

        // Logika konstruktor lainnya dipindahkan ke sini
        if (Session.currentUser != null) {
            labelNama.setText("Selamat Datang, " + Session.currentUser.getNamaLengkap());
        } else {
            // Jika belum login, idealnya ini ditangani oleh AuthFrame.
            // Untuk sementara, bisa tampilkan pesan atau biarkan kosong.
            // Atau, jika PanelBeranda hanya dibuat setelah login sukses, ini tidak akan terjadi.
            labelNama.setText("Selamat Datang, Tamu");
            // new LoginUser().setVisible(true); // Jangan buka JFrame baru dari dalam JPanel
            // this.dispose(); // JPanel tidak punya dispose()
        }

        Connection conn = Koneksi.getConnection();
        CariCepatController controller = new CariCepatController(conn);
        this.daftarDestinasi = controller.getDaftarDestinasi();

        destinasi.removeAllItems();
        destinasi.addItem("-- Pilih Destinasi --");
        if (this.daftarDestinasi != null) {
            for (DestinasiModel dest : daftarDestinasi) {
                destinasi.addItem(dest.getNamaDestinasi());
            }
        }

        // Ganti nama variabel 'Calendar' menjadi 'jDateChooserCalendar' atau sejenisnya
        // untuk menghindari konflik dengan java.util.Calendar
        jDateChooserCalendar.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                tanggalDipilih = jDateChooserCalendar.getDate();
                System.out.println("Tanggal dipilih: " + tanggalDipilih);
            }
        });

        paketPerjalananController = new PaketPerjalananController(conn);
        List<PaketPerjalananModel> topRated = paketPerjalananController.getTopRatedPakets(3);

        // Panggil loadTopRated setelah komponen diinisialisasi dan memiliki ukuran
        // Sebaiknya dipanggil setelah panel ditampilkan atau menggunakan SwingUtilities.invokeLater
        // untuk memastikan komponen sudah dirender. Untuk sementara, kita panggil langsung.
        // Namun, ukuran JLabel mungkin 0 saat ini.
        SwingUtilities.invokeLater(() -> {
            if (topRated != null) {
                if (topRated.size() > 0) {
                    loadTopRated(topRated.get(0));
                } else {
                    loadTopRated(null); // Handle jika tidak ada data
                }
                if (topRated.size() > 1) {
                    loadTopRated2(topRated.get(1));
                } else {
                    loadTopRated2(null);
                }
                if (topRated.size() > 2) {
                    loadTopRated3(topRated.get(2));
                } else {
                    loadTopRated3(null);
                }
            }
        });


        // Pengaturan JFrame seperti setTitle, setDefaultCloseOperation, setSize, setLocationRelativeTo
        // sudah tidak diperlukan di sini karena PanelBeranda akan dimasukkan ke MainAppFrame.
        // setLayout(null); // Ini juga mungkin perlu diubah jika jPanel3 adalah konten utama.
                         // Jika jPanel3 mengisi seluruh PanelBeranda, maka PanelBeranda bisa
                         // menggunakan BorderLayout dan menambahkan jPanel3 ke BorderLayout.CENTER.

        // Tambahkan jPanel3 (konten utama) ke JLayeredPane.DEFAULT_LAYER
        // dan SidebarPanel ke JLayeredPane.POPUP_LAYER
        // Pastikan jPanel3 di-set bounds atau layout manager-nya diatur di layeredPane
        if (jPanel3 != null) { // jPanel3 diinisialisasi di initComponents()
            jPanel3.setBounds(0, 0, 740, 610); // Sesuaikan ukuran ini atau gunakan layout manager
                                                // Ukuran ini harus sesuai dengan preferensi Anda
                                                // atau ukuran MainAppFrame
            layeredPane.add(jPanel3, JLayeredPane.DEFAULT_LAYER);
        }


        sidebar = new SidebarPanel(); // Inisialisasi sidebar
        // Ukuran sidebar mungkin perlu disesuaikan dengan tinggi PanelBeranda saat ditampilkan
        // Untuk sekarang, kita set ukuran awal.
        // Anda mungkin perlu listener untuk menyesuaikan tinggi sidebar jika ukuran PanelBeranda berubah.
        sidebar.setBounds(0, 0, 65, 610); // Sesuaikan tinggi dengan tinggi jPanel3 atau PanelBeranda
        layeredPane.add(sidebar, JLayeredPane.PALETTE_LAYER); // PALETTE_LAYER atau POPUP_LAYER

        // Pastikan PanelBeranda memiliki ukuran yang disukai
        this.setPreferredSize(new Dimension(740 + 65, 610)); // Lebar total = sidebar + jPanel3
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        // PENTING: Variabel 'Calendar' bentrok dengan kelas java.util.Calendar.
        // Ganti nama variabel JDateChooser Anda di NetBeans GUI Builder
        // dari 'Calendar' menjadi misalnya 'jDateChooserCalendar'.
        // Saya akan menggunakan 'jDateChooserCalendar' di bawah ini.

        jPanel3 = new javax.swing.JPanel();
        labelNama = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pilih_travelers = new javax.swing.JComboBox<>();
        tombolCari = new javax.swing.JButton();
        btn_CustomTrip = new javax.swing.JButton();
        jDateChooserCalendar = new com.toedter.calendar.JDateChooser(); // Nama variabel diubah
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

        // Hapus setDefaultCloseOperation karena ini JPanel
        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // jPanel3 adalah panel utama yang berisi semua komponen UI Anda.
        // Kita akan menambahkan jPanel3 ini ke PanelBeranda (this)
        // atau lebih baik, PanelBeranda akan mengatur layoutnya sendiri dan
        // komponen-komponen ini akan menjadi bagian dari PanelBeranda.

        // Jika jPanel3 adalah container utama, maka PanelBeranda bisa jadi hanya wrapper.
        // Untuk menjaga struktur GUI Builder, kita biarkan jPanel3.
        // PanelBeranda akan menggunakan BorderLayout dan menambahkan jPanel3 ke tengah.
        // Namun, karena Anda menggunakan JLayeredPane, kita akan menambahkan jPanel3 ke layeredPane.

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));
        // jPanel3.setPreferredSize(new java.awt.Dimension(740, 610)); // Ukuran ini akan diatur oleh layeredPane atau layout

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
                .addComponent(jDateChooserCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE) // Nama diubah
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
                            .addComponent(jDateChooserCalendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) // Nama diubah
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

        // Kode GroupLayout untuk PanelBeranda (this) yang sebelumnya untuk getContentPane()
        // Ini perlu disesuaikan. Jika PanelBeranda hanya berisi jPanel3 dan sidebar di layeredPane,
        // maka PanelBeranda sendiri tidak memerlukan GroupLayout yang kompleks ini.
        // Kita sudah mengatur layeredPane sebagai konten utama PanelBeranda.

        // Hapus bagian ini:
        /*
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 65, Short.MAX_VALUE) // Ini mungkin terkait sidebar
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        pack(); // Hapus pack()
        */
    }// </editor-fold>//GEN-END:initComponents

    // Metode event handler (tombolCariActionPerformed, dll.) tetap sama,
    // namun perhatikan cara navigasi. Membuka JFrame baru dari dalam JPanel
    // biasanya bukan praktik terbaik dalam model CardLayout.
    // Idealnya, aksi ini akan memberi tahu MainAppFrame untuk beralih panel.
    // Untuk saat ini, kita biarkan dulu agar fungsionalitas internal tetap ada.

    private void pilih_travelersActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        int selectedIndex = pilih_travelers.getSelectedIndex();
        if (selectedIndex > 0) {
            jumlahTravelerDipilih = pilih_travelers.getSelectedItem().toString();
            System.out.println("Jumlah traveler dipilih: " + jumlahTravelerDipilih);
        } else {
            jumlahTravelerDipilih = null;
        }
    }                                                

    private void pilih_travelersFocusGained(java.awt.event.FocusEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void tombolCariActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if(destinasi.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Masukkan Destinasi Anda"); // Gunakan 'this' untuk parent
            destinasi.requestFocus();
        } else if(tanggalDipilih == null) {
            JOptionPane.showMessageDialog(this, "Pilih Tanggal Terlebih Dahulu");
            jDateChooserCalendar.requestFocus(); // Gunakan nama variabel yang sudah diubah
        } else if(pilih_travelers.getSelectedIndex() == 0){
            JOptionPane.showMessageDialog(this,"Silahkan Pilih Jumlah Travelers");
            pilih_travelers.requestFocus();
        } else {
            String destinasiTerpilih = daftarDestinasi.get(destinasi.getSelectedIndex() - 1).getNamaDestinasi();
            String tanggalStr = new SimpleDateFormat("yyyy-MM-dd").format(tanggalDipilih);
            String travelers = jumlahTravelerDipilih;

            System.out.println("Destinasi: " + destinasiTerpilih);
            System.out.println("Tanggal: " + tanggalStr);
            System.out.println("Travelers: " + travelers);

            // Navigasi ke SearchResult (JFrame baru)
            // Ini akan tetap membuka JFrame baru. Untuk integrasi penuh dengan CardLayout,
            // SearchResult juga perlu menjadi JPanel dan MainAppFrame yang mengelola perpindahannya.
            SearchResult hasil = new SearchResult(destinasiTerpilih, tanggalStr, travelers);
            hasil.setVisible(true);
            // this.dispose(); // JPanel tidak bisa di-dispose. Frame induknya (MainAppFrame) yang bisa.
            // Mungkin Anda ingin memberi tahu MainAppFrame untuk menyembunyikan PanelBeranda
            // dan menampilkan PanelHasilPencarian. Ini langkah lanjutan.
            javax.swing.SwingUtilities.getWindowAncestor(this).dispose(); // Menutup MainAppFrame jika ini dipanggil
                                                                        // Sebaiknya jangan dispose di sini.
        }
    }                                          

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void btn_CustomTripActionPerformed(java.awt.event.ActionEvent evt) {                                               
        new DestinationStep().setVisible(true); // .show() deprecated
        // this.dispose(); // Sama seperti di atas
        javax.swing.SwingUtilities.getWindowAncestor(this).dispose(); // Sama seperti di atas
    }                                              

    private void destinasiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int selectedIndex = destinasi.getSelectedIndex();
        if (selectedIndex > 0 && daftarDestinasi != null && selectedIndex <= daftarDestinasi.size()) {
            DestinasiModel selectedDest = daftarDestinasi.get(selectedIndex - 1); 
            System.out.println("Dipilih: " + selectedDest.getNamaDestinasi());
        }
    }                                         

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void btn_detail1ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        new TripDetail().setVisible(true); // .show() deprecated
        // this.dispose(); // Sama seperti di atas
         javax.swing.SwingUtilities.getWindowAncestor(this).dispose(); // Sama seperti di atas
    }                                           

    private void btn_detail4ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void btn_detail5ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    // Hapus main method dari PanelBeranda, karena akan dijalankan dari AuthFrame -> MainAppFrame
    /*
    public static void main(String args[]) {
        // ... look and feel setup ...
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new PanelBeranda().setVisible(true); // JPanel tidak punya setVisible secara langsung untuk ditampilkan sebagai window
            }
        });
    }
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser jDateChooserCalendar; // Nama variabel diubah
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
    private javax.swing.JPanel jPanel3; // Ini adalah panel konten utama Anda
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
