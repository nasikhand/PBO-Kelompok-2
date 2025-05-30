package managementtrevel.HomeUser;


import Asset.AppTheme; // Pastikan AppTheme ada dan dapat diakses
import controller.UserProfileController;
import db.dao.UserDAO;
import model.Session;
import model.UserModel;
import managementtrevel.MainAppFrame; // Impor MainAppFrame

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.net.URL; // Untuk memuat gambar dari resources
import managementtrevel.TripOrder.OrderHistory;
import managementtrevel.TripOrder.UserOrder;

/**
 * PanelUserProfil class sebagai JPanel untuk diintegrasikan dengan CardLayout di MainAppFrame.
 * Ini akan menampilkan dan mengelola data profil pengguna.
 */
public class PanelUserProfil extends JPanel { // Mengubah dari UserProfile menjadi PanelUserProfil

    private UserProfileController controller;
    private UserDAO userDAO = new UserDAO();
    private MainAppFrame mainAppFrame; // Referensi ke MainAppFrame

    // Deklarasi komponen UI (sesuai dengan yang dihasilkan oleh NetBeans atau Anda buat manual)
    private javax.swing.JButton btn_editFoto;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_pesananSaya;
    private javax.swing.JButton btn_riwayatPesanan;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_ubahPassword;
    private javax.swing.ButtonGroup buttonGroup1; // Jika ini tidak digunakan, bisa dihapus
    private javax.swing.JLabel foto_user;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField txt_alamatUser;
    private javax.swing.JTextField txt_emailUser;
    private javax.swing.JTextField txt_namaUser;
    private javax.swing.JTextField txt_nomerTeleponUser;

    /**
     * Konstruktor untuk PanelUserProfil.
     * Menerima referensi ke MainAppFrame untuk navigasi.
     * @param mainAppFrame Referensi ke MainAppFrame induk.
     */
    public PanelUserProfil(MainAppFrame mainAppFrame) { // Mengubah dari UserProfile menjadi PanelUserProfil
        this.mainAppFrame = mainAppFrame;
        initComponents(); // Inisialisasi komponen UI yang dibuat oleh NetBeans/manual

        // Inisialisasi controller
        this.controller = new UserProfileController();

        // Pastikan Session.currentUser sudah ada sebelum memanggil setProfileData
        if (Session.isLoggedIn() && Session.currentUser != null) {
            setProfileData();
            loadUserPhoto();
        } else {
            // Jika user belum login, bisa tampilkan pesan atau arahkan ke halaman login
            // Menggunakan SwingUtilities.getWindowAncestor(this) untuk mendapatkan Frame induk
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "User belum login!");
            // Opsional: Arahkan kembali ke AuthFrame jika user belum login
            // mainAppFrame.performLogout(); // Ini akan mengarahkan ke AuthFrame
        }

        // Set layout untuk JPanel ini (jika Anda ingin mengatur layout secara manual)
        // Jika initComponents() sudah mengatur layout dengan GroupLayout, ini tidak perlu
        setLayout(new BorderLayout()); // Contoh jika ingin pakai BorderLayout
        add(jPanel2, BorderLayout.CENTER); // Tambahkan panel utama ke PanelUserProfil JPanel
    }

    /**
     * Memperbarui data profil yang ditampilkan di UI.
     */
    public void setProfileData() {
        if (Session.isLoggedIn() && Session.currentUser != null) {
            UserModel user = Session.currentUser;
            txt_namaUser.setText(user.getNamaLengkap());
            txt_emailUser.setText(user.getEmail());
            txt_nomerTeleponUser.setText(user.getNomorTelepon());
            txt_alamatUser.setText(user.getAlamat());
            loadUserPhoto(); // Muat ulang foto jika data profil diperbarui
        }
    }

    /**
     * Handler untuk tombol simpan. Memperbarui data user di database dan session.
     * @param evt Event aksi.
     */
    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {
        String nama = txt_namaUser.getText();
        String email = txt_emailUser.getText();
        String telepon = txt_nomerTeleponUser.getText();
        String alamat = txt_alamatUser.getText();

        if (Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "User belum login. Tidak dapat menyimpan data.");
            return;
        }

        int id = Session.currentUser.getId();
        UserModel user = new UserModel(id, nama, email, telepon, alamat);

        boolean success = controller.updateProfile(user);

        if (success) {
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");
            Session.currentUser = user; // update session agar data sinkron
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data.");
            // Kembalikan ke data awal dari session jika gagal
            UserModel currentUser = Session.currentUser;
            txt_namaUser.setText(currentUser.getNamaLengkap());
            txt_emailUser.setText(currentUser.getEmail());
            txt_nomerTeleponUser.setText(currentUser.getNomorTelepon());
            txt_alamatUser.setText(currentUser.getAlamat());
        }
    }

    /**
     * Memilih dan mengunggah foto profil baru.
     */
    private void chooseAndUploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this); // Menggunakan 'this' karena sekarang JPanel

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();

            // Mendapatkan root proyek (keluar dari userTravel-app)
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File destDir = new File(projectRoot, "SharedAppImages/user-photos");

            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File destFile = new File(destDir, fileName);

            try {
                // Hapus gambar lama jika ada
                String gambarLama = Session.currentUser.getGambar();
                if (gambarLama != null && !gambarLama.isEmpty()) {
                    File gambarLamaFile = new File(projectRoot, gambarLama);
                    if (gambarLamaFile.exists()) {
                        gambarLamaFile.delete();
                    }
                }

                // Salin gambar baru
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                foto_user.setText("");
                foto_user.setBackground(Color.WHITE);
                // Ambil ukuran dari JLabel (foto_user)
                int labelWidth = foto_user.getWidth();
                int labelHeight = foto_user.getHeight();

                // Baca dan resize gambar
                ImageIcon originalIcon = new ImageIcon(destFile.getAbsolutePath());
                Image scaledImage = originalIcon.getImage().getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);

                // Set icon yang sudah di-resize
                foto_user.setIcon(new ImageIcon(scaledImage));

                // Simpan path ke database
                String gambarPath = "/SharedAppImages/user-photos/" + fileName;
                Session.currentUser.setGambar(gambarPath); // Simpan ke objek Session juga
                userDAO.updateGambar(Session.currentUser.getId(), gambarPath);

                JOptionPane.showMessageDialog(this, "Foto berhasil diupload.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal upload foto: " + e.getMessage());
            }
        }
    }

    /**
     * Memuat foto profil pengguna dari path yang tersimpan.
     */
    private void loadUserPhoto() {
        if (Session.currentUser == null) return; // Pastikan ada user di session

        String gambarPath = Session.currentUser.getGambar();

        if (gambarPath != null && !gambarPath.isEmpty()) {
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File imageFile = new File(projectRoot, gambarPath);

            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
                // Pastikan foto_user sudah memiliki ukuran yang valid
                // Jika belum, mungkin perlu menunda pemuatan gambar atau menggunakan ukuran default
                int width = foto_user.getWidth() > 0 ? foto_user.getWidth() : 111; // Default size if not yet laid out
                int height = foto_user.getHeight() > 0 ? foto_user.getHeight() : 111; // Default size
                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                foto_user.setIcon(new ImageIcon(scaledImage));
                foto_user.setText(""); // Hapus teks "FOTO" jika gambar berhasil dimuat
            } else {
                foto_user.setText("FOTO");
                foto_user.setIcon(null);
            }
        } else {
            foto_user.setText("FOTO");
            foto_user.setIcon(null);
        }
    }

    /**
     * Menampilkan opsi untuk mengedit atau menghapus foto.
     */
    private void showEditPhotoOptions() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Foto", true); // Menggunakan getWindowAncestor
        dialog.setSize(250, 120);
        dialog.setLayout(new GridLayout(2, 1));
        dialog.setLocationRelativeTo(this);

        JButton pilihBaruBtn = new JButton("Pilih Foto Baru");
        JButton hapusFotoBtn = new JButton("Hapus Foto");

        pilihBaruBtn.addActionListener(e -> {
            dialog.dispose();
            chooseAndUploadPhoto();
        });

        hapusFotoBtn.addActionListener(e -> {
            dialog.dispose();
            deletePhoto();
        });

        dialog.add(pilihBaruBtn);
        dialog.add(hapusFotoBtn);
        dialog.setVisible(true);
    }

    /**
     * Menghapus foto profil pengguna.
     */
    private void deletePhoto() {
        if (Session.currentUser == null) return; // Pastikan ada user di session

        String gambarPath = Session.currentUser.getGambar();
        if (gambarPath != null && !gambarPath.isEmpty()) {
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File imageFile = new File(projectRoot, gambarPath);

            if (imageFile.exists()) {
                imageFile.delete();
            }

            Session.currentUser.setGambar(null);
            userDAO.updateGambar(Session.currentUser.getId(), null);

            foto_user.setIcon(null);
            foto_user.setText("FOTO");
            JOptionPane.showMessageDialog(this, "Foto berhasil dihapus.");
        }
    }

    /**
     * Menampilkan gambar profil dalam ukuran penuh di dialog terpisah.
     */
    private void showFullSizeImage() {
        if (Session.currentUser == null) return; // Pastikan ada user di session

        String gambarPath = Session.currentUser.getGambar();
        if (gambarPath != null && !gambarPath.isEmpty()) {
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File imageFile = new File(projectRoot, gambarPath);

            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());

                int dialogWidth = 400;
                int dialogHeight = 400;

                Image scaledImage = originalIcon.getImage().getScaledInstance(dialogWidth, dialogHeight, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

                JLabel label = new JLabel(resizedIcon);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);

                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Foto Profil", true);
                dialog.setSize(dialogWidth, dialogHeight);
                dialog.setLocationRelativeTo(this);
                dialog.add(label);
                dialog.setVisible(true);
            }
        }
    }


    /**
     * Metode ini dipanggil dari dalam konstruktor untuk menginisialisasi form.
     * WARNING: Jangan memodifikasi kode ini. Konten metode ini selalu
     * dihasilkan ulang oleh Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btn_pesananSaya = new javax.swing.JButton();
        btn_riwayatPesanan = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btn_logout = new javax.swing.JButton();
        foto_user = new javax.swing.JLabel();
        btn_editFoto = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_namaUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_emailUser = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_nomerTeleponUser = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_alamatUser = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        btn_simpan = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        btn_ubahPassword = new javax.swing.JButton();

        // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); // Dihapus karena ini JPanel

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setPreferredSize(new java.awt.Dimension(740, 600));

        btn_pesananSaya.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        btn_pesananSaya.setText("Pesanan Saya");
        btn_pesananSaya.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pesananSayaActionPerformed(evt);
            }
        });

        btn_riwayatPesanan.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        btn_riwayatPesanan.setText("Riwayat Pesanan");
        btn_riwayatPesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_riwayatPesananActionPerformed(evt);
            }
        });

        btn_logout.setBackground(new java.awt.Color(255, 51, 51));
        btn_logout.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btn_logout.setText("Log Out");
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        foto_user.setBackground(new java.awt.Color(0, 0, 0));
        foto_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto_user.setText("FOTO");
        foto_user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        foto_user.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        // foto_user.setFocusCycleRoot(true); // Dihapus karena ini JPanel

        btn_editFoto.setText("Edit Foto");
        btn_editFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editFotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_logout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_pesananSaya, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_riwayatPesanan, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(btn_editFoto)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_editFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_pesananSaya)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_riwayatPesanan)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_logout)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        jPanel3.setPreferredSize(new java.awt.Dimension(498, 300));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Data Pribadi");

        jLabel2.setText("Nama Lengkap");

        txt_namaUser.setText("Nama User");

        jLabel3.setText("Email");

        txt_emailUser.setText("Email User");

        jLabel4.setText("Nomor Telepon");

        txt_nomerTeleponUser.setText("Nomor Telepon User");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Alamat");

        txt_alamatUser.setText("Alamat User");
        txt_alamatUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_alamatUserActionPerformed(evt);
            }
        });

        btn_simpan.setBackground(new java.awt.Color(51, 102, 255));
        btn_simpan.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        btn_simpan.setForeground(new java.awt.Color(255, 255, 255));
        btn_simpan.setText("Simpan");
        btn_simpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn_simpanActionPerformed(e);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(txt_namaUser)
                            .addComponent(txt_emailUser)
                            .addComponent(txt_nomerTeleponUser, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(txt_alamatUser))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_namaUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_emailUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_nomerTeleponUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_alamatUser, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_simpan, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setPreferredSize(new java.awt.Dimension(498, 300));

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel6.setText("Password dan Keamanan");

        jLabel7.setFont(new java.awt.Font("Helvetica Neue", 0, 15)); // NOI18N
        jLabel7.setText("Ubah Password");

        btn_ubahPassword.setBackground(new java.awt.Color(255, 0, 51));
        btn_ubahPassword.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        btn_ubahPassword.setForeground(new java.awt.Color(255, 255, 255));
        btn_ubahPassword.setText("Ubah Password Anda");
        btn_ubahPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ubahPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_ubahPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 252, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ubahPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 228, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)))
                .addContainerGap())
        );

        // Menambahkan jPanel2 ke PanelUserProfil (yang sekarang JPanel)
        setLayout(new BorderLayout()); // Atur layout untuk PanelUserProfil JPanel
        add(jPanel2, BorderLayout.CENTER); // Tambahkan panel utama ke PanelUserProfil JPanel
    }


    private void txt_alamatUserActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * Handler untuk tombol logout. Memanggil metode performLogout di MainAppFrame.
     * @param evt Event aksi.
     */
    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.performLogout(); // Panggil metode logout di MainAppFrame
        } else {
            // Fallback jika mainAppFrame null (misalnya, saat testing terpisah)
            JOptionPane.showMessageDialog(this, "Logout berhasil!");
            System.exit(0);
        }
    }

    /**
     * Handler untuk tombol ubah password. Memanggil showPanel di MainAppFrame.
     * Asumsi: Halaman UbahPassword akan menjadi JPanel terpisah yang juga dikelola CardLayout.
     * @param evt Event aksi.
     */
    private void btn_ubahPasswordActionPerformed(java.awt.event.ActionEvent evt) {
        new UbahPassword().show();
        this.dispose();
    }

    /**
     * Handler untuk tombol riwayat pesanan. Memanggil showPanel di MainAppFrame.
     * Asumsi: Halaman OrderHistory akan menjadi JPanel terpisah yang juga dikelola CardLayout.
     * @param evt Event aksi.
     */
    private void btn_riwayatPesananActionPerformed(java.awt.event.ActionEvent evt) {
        new OrderHistory().show();
        this.dispose();
    }

    private void btn_editFotoActionPerformed(java.awt.event.ActionEvent evt) {
        showEditPhotoOptions();
    }

    /**
     * Handler untuk tombol pesanan saya. Memanggil showPanel di MainAppFrame.
     * Asumsi: Halaman UserOrder akan menjadi JPanel terpisah yang juga dikelola CardLayout.
     * @param evt Event aksi.
     */
    private void btn_pesananSayaActionPerformed(java.awt.event.ActionEvent evt) {
        new UserOrder().show();
        this.dispose();
    }

    private void dispose() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
