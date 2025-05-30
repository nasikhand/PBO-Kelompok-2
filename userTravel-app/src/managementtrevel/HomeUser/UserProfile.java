/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package managementtrevel.HomeUser;

import Asset.SidebarPanel;
import controller.UserProfileController;
import db.dao.UserDAO;
import model.Session;
import model.UserModel;
import managementtrevel.TripOrder.OrderHistory;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import managementtrevel.TripOrder.UserOrder;


/**
 *
 * @author aldy
 */
public class UserProfile extends javax.swing.JFrame {

    /**
     * Creates new form UserProfile
     */
    
    private UserProfileController controller;
    private int userId;
    private UserDAO userDAO = new UserDAO();


    private void setProfileData() {
        if (Session.isLoggedIn()) {
            UserModel user = Session.currentUser;

            txt_namaUser.setText(user.getNamaLengkap());
            txt_emailUser.setText(user.getEmail());
            txt_nomerTeleponUser.setText(user.getNomorTelepon());
            txt_alamatUser.setText(user.getAlamat());
        }
    }

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {
        String nama = txt_namaUser.getText();
        String email = txt_emailUser.getText();
        String telepon = txt_nomerTeleponUser.getText();
        String alamat = txt_alamatUser.getText();

        // Ambil ID langsung dari Session
        int id = Session.currentUser.getId();
        UserModel user = new UserModel(id, nama, email, telepon, alamat);

        boolean success = controller.updateProfile(user);

        if (success) {
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!");

            Session.currentUser = user;  // update session agar data sinkron
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data.");
            // Kembalikan ke data awal dari session
            UserModel currentUser = Session.currentUser;

            txt_namaUser.setText(currentUser.getNamaLengkap());
            txt_emailUser.setText(currentUser.getEmail());
            txt_nomerTeleponUser.setText(currentUser.getNomorTelepon());
            txt_alamatUser.setText(currentUser.getAlamat());
            }
    }

    private void chooseAndUploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();

            // Buat path ke folder SharedAppImages/user-photos yang sejajar dengan userTravel-app
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile(); // keluar dari userTravel-app
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

    private void loadUserPhoto() {
        String gambarPath = Session.currentUser.getGambar();

        if (gambarPath != null && !gambarPath.isEmpty()) {
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File imageFile = new File(projectRoot, gambarPath);

            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = originalIcon.getImage().getScaledInstance(foto_user.getWidth(), foto_user.getHeight(), Image.SCALE_SMOOTH);
                foto_user.setIcon(new ImageIcon(scaledImage));
                foto_user.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    showFullSizeImage();
                }
            });
                foto_user.setText("");
            } else {
                foto_user.setText("FOTO");
                foto_user.setIcon(null);
                
            }
        }
    }
    
    private void showEditPhotoOptions() {
        JDialog dialog = new JDialog(this, "Edit Foto", true);
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
    
    private void deletePhoto() {
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
    
    private void showFullSizeImage() {
        String gambarPath = Session.currentUser.getGambar();
        if (gambarPath != null && !gambarPath.isEmpty()) {
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File imageFile = new File(projectRoot, gambarPath);

            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());

                // Ukuran window dialog
                int dialogWidth = 400;
                int dialogHeight = 400;

                // Resize gambar agar sesuai window
                Image scaledImage = originalIcon.getImage().getScaledInstance(dialogWidth, dialogHeight, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(scaledImage);

                JLabel label = new JLabel(resizedIcon);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);

                JDialog dialog = new JDialog(this, "Foto Profil", true);
                dialog.setSize(dialogWidth, dialogHeight);
                dialog.setLocationRelativeTo(this);
                dialog.add(label);
                dialog.setVisible(true);
            }
        }
    }


    
    public UserProfile() {
        initComponents();

        // Inisialisasi controller
        this.controller = new UserProfileController();

        // Ambil user ID dari session
        userId = Session.currentUser.getId();


        if (Session.isLoggedIn()) {
            UserModel user = Session.currentUser;
            this.userId = user.getId();
            
            // Tampilkan data user ke form
            setProfileData();

            this.userId = user.getId(); // Simpan id user jika diperlukan
        } else {
            JOptionPane.showMessageDialog(this, "User belum login!");
            // Bisa arahkan ke halaman login atau tutup form
        }
        
        loadUserPhoto();
        

        setTitle("Halaman Profile");
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        foto_user.setFocusCycleRoot(true);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 65, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_alamatUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_alamatUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_alamatUserActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
        JOptionPane.showMessageDialog(this, "Logout berhasil!");
            System.exit(0);
    }//GEN-LAST:event_btn_logoutActionPerformed

    private void btn_ubahPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ubahPasswordActionPerformed
        new UbahPassword().show();
        this.dispose();
    }//GEN-LAST:event_btn_ubahPasswordActionPerformed

    private void btn_riwayatPesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_riwayatPesananActionPerformed
        new OrderHistory().show();
        this.dispose();
    }//GEN-LAST:event_btn_riwayatPesananActionPerformed

    private void btn_editFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editFotoActionPerformed
        showEditPhotoOptions();
    }//GEN-LAST:event_btn_editFotoActionPerformed

    private void btn_pesananSayaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pesananSayaActionPerformed
        new UserOrder().show();
        this.dispose();
    }//GEN-LAST:event_btn_pesananSayaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserProfile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_editFoto;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_pesananSaya;
    private javax.swing.JButton btn_riwayatPesanan;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_ubahPassword;
    private javax.swing.ButtonGroup buttonGroup1;
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
    // End of variables declaration//GEN-END:variables
}
