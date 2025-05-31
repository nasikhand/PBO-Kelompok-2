package managementtrevel.SearchResultScreen;

// Import yang relevan
import managementtrevel.MainAppFrame; // Untuk navigasi CardLayout
import javax.swing.JPanel;
import javax.swing.JLayeredPane; // Meskipun ini JPanel, impor tetap ada jika ada kode terkait yang tidak dihapus
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * JPanel ini menampilkan hasil pencarian dan diintegrasikan dengan MainAppFrame menggunakan CardLayout.
 * Ini adalah versi JPanel dari JFrame SearchResult sebelumnya.
 */
public class PanelSearchResult extends javax.swing.JPanel {

    private MainAppFrame mainAppFrame;
    private String namaDestinasi;
    private String tanggalKeberangkatan;
    private String jumlahTraveler;

    // Deklarasi komponen UI (sesuai dengan yang dihasilkan NetBeans)
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_bookingcepat;
    private javax.swing.JButton btn_bookingcepat1;
    private javax.swing.JButton btn_detail;
    private javax.swing.JButton btn_detail1;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sebelum;
    private javax.swing.JButton btn_selanjutnya;
    private javax.swing.JComboBox<String> cb_durasi;
    private javax.swing.JComboBox<String> cb_urutkan;
    private javax.swing.JLabel foto_user;
    private javax.swing.JLabel foto_user1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel panelTemplate;
    private javax.swing.JTextField tf_harga;
    private javax.swing.JTextField tf_harga1;
    private javax.swing.JTextField tf_hari;
    private javax.swing.JTextField tf_hari1;
    private javax.swing.JTextField tf_namakota;
    private javax.swing.JTextField tf_namakota1;
    private javax.swing.JTextField tf_orang;
    private javax.swing.JTextField tf_orang1;
    private javax.swing.JTextField tf_orang2;
    private javax.swing.JTextField tf_orang3;

    /**
     * Membuat form PanelSearchResult baru.
     *
     * @param mainAppFrame Instance dari MainAppFrame yang menampung panel ini,
     * digunakan untuk navigasi.
     * @param namaDestinasi Parameter pencarian destinasi.
     * @param tanggalKeberangkatan Parameter pencarian tanggal keberangkatan.
     * @param jumlahTraveler Parameter pencarian jumlah traveler.
     */
    public PanelSearchResult(MainAppFrame mainAppFrame, String namaDestinasi, String tanggalKeberangkatan, String jumlahTraveler) {
        initComponents();
        this.mainAppFrame = mainAppFrame;
        this.namaDestinasi = namaDestinasi;
        this.tanggalKeberangkatan = tanggalKeberangkatan;
        this.jumlahTraveler = jumlahTraveler;

        // Contoh tampilkan di console (logika asli dari JFrame)
        System.out.println("Destinasi: " + namaDestinasi);
        System.out.println("Tanggal: " + tanggalKeberangkatan);
        System.out.println("Jumlah Traveler: " + jumlahTraveler);

        // Bagian yang dihapus dari JFrame:
        // setTitle("Search Result"); // JPanel tidak punya judul
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JPanel tidak punya operasi default close
        // setSize(800, 600); // Ukuran akan diatur oleh parent container
        // setLocationRelativeTo(null); // Posisi akan diatur oleh parent container
        // setLayout(null); // Layout akan diatur oleh parent container (CardLayout)

        // JLayeredPane biasanya digunakan di JFrame, mungkin tidak relevan untuk JPanel yang di CardLayout
        // JLayeredPane layeredPane = getLayeredPane(); // JPanel tidak punya getLayeredPane()
        // SidebarPanel sidebar = new SidebarPanel(); // Jika SidebarPanel juga JPanel, bisa ditambahkan ke MainAppFrame
        // sidebar.setBounds(0, 0, 65, getHeight());
        // layeredPane.add(sidebar, JLayeredPane.POPUP_LAYER);
    }

    /**
     * Method ini dipanggil dari dalam konstruktor untuk menginisialisasi komponen UI.
     * WARNING: Jangan memodifikasi kode ini. Konten dari method ini selalu
     * diregenerasi oleh Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cb_urutkan = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cb_durasi = new javax.swing.JComboBox<>();
        btn_reset = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        panelTemplate = new javax.swing.JPanel();
        btn_detail = new javax.swing.JButton();
        btn_bookingcepat = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        tf_namakota = new javax.swing.JTextField();
        tf_hari = new javax.swing.JTextField();
        tf_orang = new javax.swing.JTextField();
        tf_orang1 = new javax.swing.JTextField();
        tf_harga = new javax.swing.JTextField();
        foto_user = new javax.swing.JLabel();
        btn_sebelum = new javax.swing.JButton();
        btn_selanjutnya = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btn_detail1 = new javax.swing.JButton();
        btn_bookingcepat1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        tf_namakota1 = new javax.swing.JTextField();
        tf_hari1 = new javax.swing.JTextField();
        tf_orang2 = new javax.swing.JTextField();
        tf_orang3 = new javax.swing.JTextField();
        tf_harga1 = new javax.swing.JTextField();
        foto_user1 = new javax.swing.JLabel();

        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); // Dihapus karena ini JPanel

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Filter");

        jLabel2.setText("Urutkan Dari");

        cb_urutkan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "...", "Harga: Rendah ke Tinggi", "Harga: Tinggi ke Rendah", " " }));
        cb_urutkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_urutkanActionPerformed(evt);
            }
        });

        jLabel3.setText("Durasi");

        cb_durasi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "...", "1 Hari", "2 Hari", "3 Hari", "4 Hari", "5 Hari", "6 Hari", "7 Hari" }));

        btn_reset.setText("Reset ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_reset)
                .addGap(50, 50, 50))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addGap(60, 60, 60))
                    .addComponent(cb_durasi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cb_urutkan, 0, 175, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_urutkan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_durasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_reset)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Hasil Search:");

        panelTemplate.setBackground(new java.awt.Color(255, 255, 255));

        btn_detail.setText("Detail");
        btn_detail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detailActionPerformed(evt);
            }
        });

        btn_bookingcepat.setText("Booking Cepat");
        btn_bookingcepat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bookingcepatActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Harga:");

        tf_namakota.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_namakota.setText("Nama kota");
        tf_namakota.setBorder(null);

        tf_hari.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_hari.setText("...Hari");
        tf_hari.setBorder(null);

        tf_orang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_orang.setText("...Orang");
        tf_orang.setBorder(null);

        tf_orang1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tf_orang1.setText("Rating");
        tf_orang1.setBorder(null);

        tf_harga.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tf_harga.setText("Rp...");
        tf_harga.setBorder(null);

        foto_user.setBackground(new java.awt.Color(0, 0, 0));
        foto_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto_user.setText("FOTO");
        foto_user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        foto_user.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        foto_user.setFocusCycleRoot(true);

        javax.swing.GroupLayout panelTemplateLayout = new javax.swing.GroupLayout(panelTemplate);
        panelTemplate.setLayout(panelTemplateLayout);
        panelTemplateLayout.setHorizontalGroup(
            panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemplateLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemplateLayout.createSequentialGroup()
                        .addComponent(btn_detail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(btn_bookingcepat)
                        .addGap(105, 105, 105))
                    .addGroup(panelTemplateLayout.createSequentialGroup()
                        .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTemplateLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tf_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelTemplateLayout.createSequentialGroup()
                                .addComponent(tf_namakota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_hari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_orang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tf_orang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelTemplateLayout.setVerticalGroup(
            panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemplateLayout.createSequentialGroup()
                .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemplateLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_namakota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_hari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_orang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_orang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tf_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelTemplateLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_detail)
                    .addComponent(btn_bookingcepat))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btn_sebelum.setText("< Sebelum");
        btn_sebelum.setMaximumSize(new java.awt.Dimension(101, 23));
        btn_sebelum.setMinimumSize(new java.awt.Dimension(101, 23));
        btn_sebelum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sebelumActionPerformed(evt);
            }
        });

        btn_selanjutnya.setText("Selanjutnya >");

        jLabel8.setText("Page 1 of 3");

        btn_back.setText("< Back");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        btn_detail1.setText("Detail");
        btn_detail1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detail1ActionPerformed(evt);
            }
        });

        btn_bookingcepat1.setText("Booking Cepat");
        btn_bookingcepat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bookingcepat1ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Harga:");

        tf_namakota1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_namakota1.setText("Nama kota");
        tf_namakota1.setBorder(null);

        tf_hari1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_hari1.setText("...Hari");
        tf_hari1.setBorder(null);

        tf_orang2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tf_orang2.setText("...Orang");
        tf_orang2.setBorder(null);

        tf_orang3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tf_orang3.setText("Rating");
        tf_orang3.setBorder(null);

        tf_harga1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tf_harga1.setText("Rp...");
        tf_harga1.setBorder(null);

        foto_user1.setBackground(new java.awt.Color(0, 0, 0));
        foto_user1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto_user1.setText("FOTO");
        foto_user1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        foto_user1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        foto_user1.setFocusCycleRoot(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(foto_user1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_detail1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(btn_bookingcepat1)
                        .addGap(105, 105, 105))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tf_harga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(tf_namakota1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_hari1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_orang2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tf_orang3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_namakota1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_hari1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tf_orang2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tf_orang3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(tf_harga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(foto_user1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_detail1)
                    .addComponent(btn_bookingcepat1))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_sebelum, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_selanjutnya)
                        .addGap(115, 115, 115))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4)
                                .addComponent(panelTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btn_back)
                                .addGap(417, 417, 417)))
                        .addGap(32, 32, 32))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btn_back)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_sebelum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_selanjutnya)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        // Layout untuk PanelSearchResult itu sendiri
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this); // Menggunakan 'this' karena ini JPanel
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        // pack(); // Dihapus karena ini JPanel
    }// </editor-fold>

    private void cb_urutkanActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_detailActionPerformed(java.awt.event.ActionEvent evt) {
        // Mengganti navigasi JFrame dengan navigasi CardLayout di MainAppFrame
        // Asumsi ada konstanta PANEL_TRIP_DETAIL di MainAppFrame
        // Dan Anda perlu cara untuk meneruskan ID perjalanan ke panel detail
        if (mainAppFrame != null) {
            // Contoh: mainAppFrame.showTripDetailPanel(tripId);
            mainAppFrame.showPanel("detail trip"); // Placeholder
        }
    }

    private void btn_bookingcepatActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_sebelumActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {
        // Mengganti navigasi JFrame dengan navigasi CardLayout di MainAppFrame
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_BERANDA); // Kembali ke HomeScreen
        }
    }

    private void btn_detail1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Mengganti navigasi JFrame dengan navigasi CardLayout di MainAppFrame
        // Asumsi ada konstanta PANEL_TRIP_DETAIL di MainAppFrame
        if (mainAppFrame != null) {
            // Contoh: mainAppFrame.showTripDetailPanel(tripId);
            mainAppFrame.showPanel("detail trip"); // Placeholder
        }
    }

    private void btn_bookingcepat1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    // Method main tidak diperlukan lagi di JPanel
}
