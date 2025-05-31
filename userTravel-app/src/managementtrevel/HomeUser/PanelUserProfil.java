package managementtrevel.HomeUser;

import Asset.AppTheme;
import controller.UserProfileController;
import db.dao.UserDAO;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter; // Asumsi ini adalah JPanel
import java.awt.event.MouseEvent;   // Asumsi ini adalah JPanel
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import managementtrevel.MainAppFrame;
import static managementtrevel.MainAppFrame.PANEL_RIWAYAT_PESANAN;
import model.Session;
import model.UserModel;

public class PanelUserProfil extends JPanel {

    private UserProfileController controller;
    private UserDAO userDAO = new UserDAO();
    private MainAppFrame mainAppFrame;

    private JButton btn_editFoto;
    private JButton btn_logout;
    private JButton btn_pesananSaya;
    private JButton btn_riwayatPesanan;
    private JButton btn_simpan;
    private JButton btn_ubahPassword;
    // private javax.swing.ButtonGroup buttonGroup1; // Dihapus karena tidak digunakan
    private JLabel foto_user;
    private JLabel jLabel1; // Judul Data Pribadi
    private JLabel jLabel2; // Label Nama
    private JLabel jLabel3; // Label Email
    private JLabel jLabel4; // Label No Telepon
    private JLabel jLabel5; // Label Alamat
    private JLabel jLabel6; // Judul Password & Keamanan
    private JLabel jLabel7; // Label Ubah Password
    private JPanel jPanel1; // Panel Kiri (Menu & Foto)
    private JPanel jPanel2; // Panel Konten Utama (Wrapper untuk jPanel1, jPanel3, jPanel4)
    private JPanel jPanel3; // Panel Data Pribadi
    private JPanel jPanel4; // Panel Password & Keamanan
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JSeparator jSeparator3;
    private JTextField txt_alamatUser;
    private JTextField txt_emailUser;
    private JTextField txt_namaUser;
    private JTextField txt_nomerTeleponUser;

    public PanelUserProfil(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        initComponents(); // Inisialisasi komponen UI dari NetBeans
        applyAppTheme();  // Terapkan tema kustom
        setupEventListeners(); // Atur event listener setelah komponen diinisialisasi

        this.controller = new UserProfileController();

        if (Session.isLoggedIn() && Session.currentUser != null) {
            setProfileData();
            // Pemanggilan loadUserPhoto sudah ada di setProfileData
        } else {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Sesi pengguna tidak ditemukan. Silakan login kembali.",
                    "Error Sesi", JOptionPane.ERROR_MESSAGE);
            if (this.mainAppFrame != null) {
                 this.mainAppFrame.performLogout(); // Arahkan ke login jika sesi tidak valid
            }
        }
    }

    private void applyAppTheme() {
        // Latar Belakang Utama
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        jPanel2.setBackground(AppTheme.PANEL_BACKGROUND); // Panel konten utama

        // Panel Kiri (Menu & Foto)
        jPanel1.setBackground(Color.WHITE); // Kartu putih untuk panel kiri
        jPanel1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                new EmptyBorder(20, 15, 20, 15) // Padding
        ));

        // Foto Pengguna
        foto_user.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY_BLUE_LIGHT, 2)); // Border menarik
        foto_user.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        foto_user.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        // Pastikan ukuran foto_user diatur di initComponents atau setPreferredSize
        if (foto_user.getIcon() == null) { // Jika belum ada foto, set background placeholder
            foto_user.setOpaque(true);
            foto_user.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
        }


        // Tombol di Panel Kiri
        styleSidebarButton(btn_editFoto, "Edit Foto");
        styleSidebarButton(btn_pesananSaya, "Pesanan Saya");
        styleSidebarButton(btn_riwayatPesanan, "Riwayat Pesanan");
        styleDestructiveButton(btn_logout, "Log Out"); // Tombol logout dengan gaya berbeda

        // Separator
        jSeparator1.setForeground(AppTheme.BORDER_COLOR);
        jSeparator2.setForeground(AppTheme.BORDER_COLOR);
        jSeparator3.setForeground(AppTheme.BORDER_COLOR);

        // Panel Data Pribadi (jPanel3) & Panel Password (jPanel4)
        setupCardPanelStyle(jPanel3, "Data Pribadi", jLabel1);
        setupCardPanelStyle(jPanel4, "Password & Keamanan", jLabel6);

        // Label dan Text Field di Data Pribadi
        styleFormLabel(jLabel2, "Nama Lengkap"); // Teks dari jLabel di initComponents
        styleFormLabel(jLabel3, "Email");
        styleFormLabel(jLabel4, "Nomor Telepon");
        styleFormLabel(jLabel5, "Alamat");

        styleTextField(txt_namaUser);
        styleTextField(txt_emailUser);
        styleTextField(txt_nomerTeleponUser);
        styleTextField(txt_alamatUser); // Jika ini JTextArea, perlu penyesuaian

        stylePrimaryButton(btn_simpan, "Simpan Perubahan");

        // Label dan Tombol di Password & Keamanan
        styleFormLabel(jLabel7, "Ubah Password Anda saat ini.");
        jLabel7.setFont(AppTheme.FONT_PRIMARY_DEFAULT); // Sedikit lebih kecil dari judul section
        styleSecondaryButton(btn_ubahPassword, "Ubah Password");
    }

    private void setupCardPanelStyle(JPanel panel, String title, JLabel titleLabel) {
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                new EmptyBorder(15, 20, 15, 20) // Padding dalam kartu
        ));
        if (titleLabel != null) {
            titleLabel.setText(title); // Set teks judul dari sini
            titleLabel.setFont(AppTheme.FONT_TITLE_MEDIUM);
            titleLabel.setForeground(AppTheme.PRIMARY_BLUE_DARK);
            titleLabel.setBorder(new EmptyBorder(0,0,10,0)); // Margin bawah untuk judul
        }
    }


    private void styleFormLabel(JLabel label, String text) {
        if (label != null) {
            label.setText(text); // Set teks label dari sini
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(AppTheme.FONT_TEXT_FIELD);
        textField.setBackground(AppTheme.INPUT_BACKGROUND);
        textField.setForeground(AppTheme.INPUT_TEXT);
        textField.setBorder(AppTheme.createDefaultInputBorder());
        textField.setMargin(new Insets(2, 5, 2, 5)); // Padding dalam text field
        addFocusBorderEffect(textField);
    }

    private void addFocusBorderEffect(JTextField textField) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }

    private void stylePrimaryButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding tombol
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }

    private void styleSecondaryButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
    }
    
    private void styleSidebarButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_PRIMARY_MEDIUM);
        button.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        button.setBackground(Color.WHITE); // Atau AppTheme.PANEL_BACKGROUND jika ingin menyatu
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 10, 8, 10));
        addHoverEffect(button, AppTheme.BACKGROUND_LIGHT_GRAY, Color.WHITE);
    }

    private void styleDestructiveButton(JButton button, String text) {
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.ACCENT_ORANGE.darker()); // Warna merah/oranye tua untuk logout
        button.setForeground(AppTheme.TEXT_WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        addHoverEffect(button, AppTheme.ACCENT_ORANGE.darker().darker(), AppTheme.ACCENT_ORANGE.darker());
    }


    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
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
    
    private void setupEventListeners() {
        btn_simpan.addActionListener(this::btn_simpanActionPerformed);
        btn_editFoto.addActionListener(this::btn_editFotoActionPerformed);
        btn_logout.addActionListener(this::btn_logoutActionPerformed);
        btn_ubahPassword.addActionListener(this::btn_ubahPasswordActionPerformed);
        btn_pesananSaya.addActionListener(this::btn_pesananSayaActionPerformed);
        btn_riwayatPesanan.addActionListener(this::btn_riwayatPesananActionPerformed);
        
        // Event listener untuk klik pada foto (jika ingin memperbesar)
        foto_user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (foto_user.getIcon() != null) { // Hanya jika ada foto
                    showFullSizeImage();
                } else { // Jika tidak ada foto, mungkin trigger edit foto
                    showEditPhotoOptions();
                }
            }
        });
    }


    public void setProfileData() {
        if (Session.isLoggedIn() && Session.currentUser != null) {
            UserModel user = Session.currentUser;
            txt_namaUser.setText(user.getNamaLengkap());
            txt_emailUser.setText(user.getEmail());
            txt_nomerTeleponUser.setText(user.getNomorTelepon());
            txt_alamatUser.setText(user.getAlamat());
            loadUserPhoto(); 
        }
    }

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {
        String nama = txt_namaUser.getText().trim();
        String email = txt_emailUser.getText().trim();
        String telepon = txt_nomerTeleponUser.getText().trim();
        String alamat = txt_alamatUser.getText().trim();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field data pribadi harus diisi!", "Input Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (Session.currentUser == null) {
            JOptionPane.showMessageDialog(this, "User belum login. Tidak dapat menyimpan data.", "Error Sesi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Session.currentUser.getId();
        // Password tidak diubah di sini, jadi ambil password yang ada dari session (atau biarkan kosong jika DAO menangani)
        UserModel updatedUser = new UserModel(id, nama, email, Session.currentUser.getPassword(), telepon, alamat, Session.currentUser.getGambar());
        // Sesuaikan konstruktor UserModel jika berbeda

        boolean success = controller.updateProfile(updatedUser);

        if (success) {
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            Session.currentUser = userDAO.findById(id); // Ambil data terbaru dari DB untuk session
            setProfileData(); // Refresh tampilan
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data. Pastikan email unik jika diubah.", "Gagal", JOptionPane.ERROR_MESSAGE);
            // Kembalikan ke data awal dari session jika gagal
            setProfileData(); // Untuk memastikan data kembali ke sebelum diedit jika ada perubahan di field
        }
    }

    private void chooseAndUploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Foto Profil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = Session.currentUser.getId() + "_" + System.currentTimeMillis() + "_" + selectedFile.getName(); // Nama file unik

            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File destDir = new File(projectRoot, "SharedAppImages/user-photos");

            if (!destDir.exists()) {
                if (!destDir.mkdirs()) {
                    JOptionPane.showMessageDialog(this, "Gagal membuat direktori penyimpanan foto.", "Error Penyimpanan", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            File destFile = new File(destDir, fileName);

            try {
                String gambarLamaPath = Session.currentUser.getGambar();
                if (gambarLamaPath != null && !gambarLamaPath.isEmpty()) {
                    File gambarLamaFile = new File(projectRoot, gambarLamaPath);
                    if (gambarLamaFile.exists()) {
                        gambarLamaFile.delete();
                    }
                }

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                String newGambarPath = "SharedAppImages/user-photos/" + fileName; // Path relatif dari root proyek
                Session.currentUser.setGambar(newGambarPath);
                userDAO.updateGambar(Session.currentUser.getId(), newGambarPath);
                
                loadUserPhoto(); // Muat foto yang baru diupload
                JOptionPane.showMessageDialog(this, "Foto berhasil diupload.", "Sukses", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal upload foto: " + e.getMessage(), "Error Upload", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void loadUserPhoto() {
        if (Session.currentUser == null) return;

        String gambarPathRelatif = Session.currentUser.getGambar();
        foto_user.setIcon(null); // Reset ikon dulu
        foto_user.setText("FOTO");
        foto_user.setOpaque(true);
        foto_user.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);


        if (gambarPathRelatif != null && !gambarPathRelatif.isEmpty()) {
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            File imageFile = new File(projectRoot, gambarPathRelatif);

            if (imageFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
                int width = foto_user.getWidth() > 0 ? foto_user.getWidth() : 111; 
                int height = foto_user.getHeight() > 0 ? foto_user.getHeight() : 111;
                
                if (width <=0 || height <=0) { // Jika ukuran label belum siap, tunda atau gunakan ukuran default
                    System.out.println("Ukuran foto_user belum siap, menunda pemuatan atau menggunakan default.");
                    // Anda bisa set preferred size di sini jika belum diatur di initcomponents
                    foto_user.setPreferredSize(new Dimension(111,111));
                    width = 111; height = 111;
                }

                Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                foto_user.setIcon(new ImageIcon(scaledImage));
                foto_user.setText(""); 
                foto_user.setOpaque(false); // Hapus background jika gambar ada
                foto_user.setBackground(null);
            } else {
                System.out.println("File gambar profil tidak ditemukan di: " + imageFile.getAbsolutePath());
            }
        }
    }

    private void showEditPhotoOptions() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Opsi Foto Profil", true);
        dialog.setSize(300, 150);
        dialog.setLayout(new GridLayout(2, 1, 10, 10)); // Beri jarak antar tombol
        dialog.setLocationRelativeTo(btn_editFoto); // Muncul dekat tombol edit foto
        ((JPanel)dialog.getContentPane()).setBorder(new EmptyBorder(15,15,15,15)); // Padding dialog

        JButton pilihBaruBtn = new JButton("Pilih Foto Baru");
        JButton hapusFotoBtn = new JButton("Hapus Foto Saat Ini");

        stylePrimaryButton(pilihBaruBtn, "Pilih Foto Baru");
        styleDestructiveButton(hapusFotoBtn, "Hapus Foto Saat Ini");
        
        // Nonaktifkan hapus jika tidak ada foto
        hapusFotoBtn.setEnabled(Session.currentUser != null && Session.currentUser.getGambar() != null && !Session.currentUser.getGambar().isEmpty());


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
        if (Session.currentUser == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus foto profil Anda?",
                "Konfirmasi Hapus Foto", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String gambarPathRelatif = Session.currentUser.getGambar();
            if (gambarPathRelatif != null && !gambarPathRelatif.isEmpty()) {
                File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
                File imageFile = new File(projectRoot, gambarPathRelatif);

                if (imageFile.exists()) {
                    if (!imageFile.delete()) {
                         JOptionPane.showMessageDialog(this, "Gagal menghapus file foto lama.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                Session.currentUser.setGambar(null);
                userDAO.updateGambar(Session.currentUser.getId(), null);

                loadUserPhoto(); // Refresh tampilan foto
                JOptionPane.showMessageDialog(this, "Foto berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Tidak ada foto untuk dihapus.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void showFullSizeImage() {
        if (Session.currentUser == null || Session.currentUser.getGambar() == null || Session.currentUser.getGambar().isEmpty()) return;

        File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
        File imageFile = new File(projectRoot, Session.currentUser.getGambar());

        if (imageFile.exists()) {
            ImageIcon originalIcon = new ImageIcon(imageFile.getAbsolutePath());
            JLabel imageLabel = new JLabel();
            
            // Skala gambar agar muat di dialog, dengan mempertahankan aspek rasio
            int maxWidth = 600;
            int maxHeight = 500;
            int originalWidth = originalIcon.getIconWidth();
            int originalHeight = originalIcon.getIconHeight();
            
            double scaleFactor = Math.min(1d, Math.min((double)maxWidth / originalWidth, (double)maxHeight / originalHeight));
            
            int newWidth = (int)(originalWidth * scaleFactor);
            int newHeight = (int)(originalHeight * scaleFactor);

            Image scaledImage = originalIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
            
            JDialog imageDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Foto Profil", true);
            imageDialog.getContentPane().setBackground(Color.BLACK); // Latar belakang gelap untuk fokus ke gambar
            imageDialog.add(new JScrollPane(imageLabel)); // Tambahkan scroll jika gambar masih besar
            imageDialog.pack(); // Sesuaikan ukuran dialog dengan gambar
            // Batasi ukuran dialog maksimal
            imageDialog.setSize(Math.min(imageDialog.getWidth(), maxWidth + 50), Math.min(imageDialog.getHeight(), maxHeight + 50));
            imageDialog.setLocationRelativeTo(this);
            imageDialog.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Kode initComponents() Anda yang dihasilkan NetBeans ada di sini.
        // Saya akan menyederhanakannya untuk fokus pada struktur dan penerapan tema.
        // Anda harus menyalin kode initComponents() asli Anda ke sini.
        // Pastikan semua komponen yang dideklarasikan di atas diinisialisasi di sini.

        // Contoh struktur dasar yang mungkin dihasilkan NetBeans, SANGAT DISARANKAN MENGGUNAKAN KODE ASLI ANDA
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

        // --- Mulai Konfigurasi Layout (Contoh, sesuaikan dengan GroupLayout Anda) ---
        // PanelUserProfil (this) menggunakan BorderLayout
        setLayout(new BorderLayout(10,10)); // Beri jarak antar region BorderLayout
        setBorder(new EmptyBorder(10,10,10,10)); // Padding luar untuk seluruh panel profil

        // jPanel2 sebagai panel utama yang berisi semuanya
        jPanel2.setLayout(new BorderLayout(15, 15)); // Jarak antar jPanel1 dan (jPanel3+jPanel4)

        // jPanel1 (Panel Kiri)
        jPanel1.setPreferredSize(new Dimension(230, 0)); // Lebar panel kiri
        jPanel1.setLayout(new GridBagLayout());
        GridBagConstraints gbcPanel1 = new GridBagConstraints();
        gbcPanel1.gridwidth = GridBagConstraints.REMAINDER;
        gbcPanel1.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel1.weightx = 1.0;
        gbcPanel1.insets = new Insets(5,0,5,0); 

        foto_user.setHorizontalAlignment(SwingConstants.CENTER);
        foto_user.setPreferredSize(new Dimension(120, 120)); 
        gbcPanel1.insets = new Insets(0,0,5,0);
        jPanel1.add(foto_user, gbcPanel1);

        btn_editFoto.setHorizontalAlignment(SwingConstants.CENTER);
        gbcPanel1.insets = new Insets(0,0,20,0); 
        jPanel1.add(btn_editFoto, gbcPanel1);
        
        gbcPanel1.insets = new Insets(5,0,5,0);
        jPanel1.add(btn_pesananSaya, gbcPanel1);
        jPanel1.add(btn_riwayatPesanan, gbcPanel1);
        
        gbcPanel1.insets = new Insets(15,0,15,0); 
        jPanel1.add(jSeparator1, gbcPanel1);
        
        gbcPanel1.insets = new Insets(5,0,5,0);
        jPanel1.add(btn_logout, gbcPanel1);

        gbcPanel1.weighty = 1.0;
        jPanel1.add(new JLabel(), gbcPanel1);


        // Panel Kanan (Wrapper untuk jPanel3 dan jPanel4)
        JPanel panelKanan = new JPanel(new BorderLayout(0, 15)); 
        panelKanan.setOpaque(false); 

        // jPanel3 (Data Pribadi)
        jPanel3.setLayout(new GridBagLayout());
        GridBagConstraints gbcPanel3 = new GridBagConstraints();
        gbcPanel3.anchor = GridBagConstraints.WEST;
        gbcPanel3.insets = new Insets(5,5,5,5);
        gbcPanel3.gridx = 0; gbcPanel3.gridy = 0; gbcPanel3.gridwidth = 2;
        jPanel3.add(jLabel1, gbcPanel3); 
        gbcPanel3.gridy++; gbcPanel3.fill = GridBagConstraints.HORIZONTAL;
        jPanel3.add(jSeparator2, gbcPanel3);

        gbcPanel3.gridwidth = 1; gbcPanel3.fill = GridBagConstraints.NONE;
        gbcPanel3.gridy++; gbcPanel3.gridx = 0; jPanel3.add(jLabel2, gbcPanel3); 
        gbcPanel3.gridx = 1; gbcPanel3.fill = GridBagConstraints.HORIZONTAL; gbcPanel3.weightx = 1.0;
        jPanel3.add(txt_namaUser, gbcPanel3); gbcPanel3.weightx = 0;

        gbcPanel3.gridy++; gbcPanel3.gridx = 0; gbcPanel3.fill = GridBagConstraints.NONE; jPanel3.add(jLabel3, gbcPanel3); 
        gbcPanel3.gridx = 1; gbcPanel3.fill = GridBagConstraints.HORIZONTAL; gbcPanel3.weightx = 1.0;
        jPanel3.add(txt_emailUser, gbcPanel3); gbcPanel3.weightx = 0;

        gbcPanel3.gridy++; gbcPanel3.gridx = 0; gbcPanel3.fill = GridBagConstraints.NONE; jPanel3.add(jLabel4, gbcPanel3); 
        gbcPanel3.gridx = 1; gbcPanel3.fill = GridBagConstraints.HORIZONTAL; gbcPanel3.weightx = 1.0;
        jPanel3.add(txt_nomerTeleponUser, gbcPanel3); gbcPanel3.weightx = 0;

        gbcPanel3.gridy++; gbcPanel3.gridx = 0; gbcPanel3.fill = GridBagConstraints.NONE; jPanel3.add(jLabel5, gbcPanel3); 
        gbcPanel3.gridx = 1; gbcPanel3.fill = GridBagConstraints.HORIZONTAL; gbcPanel3.weightx = 1.0;
        txt_alamatUser.setPreferredSize(new Dimension(100, 60)); 
        jPanel3.add(txt_alamatUser, gbcPanel3); gbcPanel3.weightx = 0;

        gbcPanel3.gridy++; gbcPanel3.gridx = 1; gbcPanel3.fill = GridBagConstraints.NONE;
        gbcPanel3.anchor = GridBagConstraints.EAST;
        gbcPanel3.insets = new Insets(15,5,5,5);
        jPanel3.add(btn_simpan, gbcPanel3);


        // jPanel4 (Password & Keamanan) - Modifikasi untuk centering
        jPanel4.setLayout(new GridBagLayout());
        GridBagConstraints gbcPanel4 = new GridBagConstraints();
        // Semua komponen di jPanel4 akan menggunakan gridwidth REMAINDER dan anchor CENTER
        gbcPanel4.gridwidth = GridBagConstraints.REMAINDER;
        gbcPanel4.anchor = GridBagConstraints.CENTER;
        gbcPanel4.insets = new Insets(5, 5, 5, 5); // Default insets

        gbcPanel4.gridx = 0; gbcPanel4.gridy = 0;
        gbcPanel4.fill = GridBagConstraints.NONE; // Judul tidak stretch
        jPanel4.add(jLabel6, gbcPanel4); // Judul Password

        gbcPanel4.gridy++;
        gbcPanel4.fill = GridBagConstraints.HORIZONTAL; // Separator stretch
        gbcPanel4.insets = new Insets(5, 20, 5, 20); // Padding horizontal untuk separator
        jPanel4.add(jSeparator3, gbcPanel4);
        gbcPanel4.insets = new Insets(5, 5, 5, 5); // Reset insets

        gbcPanel4.gridy++;
        gbcPanel4.fill = GridBagConstraints.NONE; // Label tidak stretch
        jPanel4.add(jLabel7, gbcPanel4); // Label ubah password
        
        gbcPanel4.gridy++;
        gbcPanel4.insets = new Insets(10, 5, 5, 5); // Padding atas untuk tombol
        // Tombol tidak stretch, akan terpusat karena anchor=CENTER
        jPanel4.add(btn_ubahPassword, gbcPanel4);
        
        // Komponen dummy untuk mendorong konten ke atas-tengah jika jPanel4 tinggi
        gbcPanel4.gridy++;
        gbcPanel4.weighty = 1.0; 
        gbcPanel4.fill = GridBagConstraints.BOTH; // Mengisi sisa ruang vertikal
        jPanel4.add(new JLabel(), gbcPanel4); // Komponen kosong


        panelKanan.add(jPanel3, BorderLayout.NORTH);
        panelKanan.add(jPanel4, BorderLayout.CENTER); 

        jPanel2.add(jPanel1, BorderLayout.WEST);
        jPanel2.add(panelKanan, BorderLayout.CENTER);

        add(jPanel2, BorderLayout.CENTER);
    }
    // --- Akhir Konfigurasi Layout Contoh ---


    private void txt_alamatUserActionPerformed(java.awt.event.ActionEvent evt) {
        // Aksi jika Enter ditekan di field alamat (jika diperlukan)
    }

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            mainAppFrame.performLogout();
        } else {
            JOptionPane.showMessageDialog(this, "Logout dipanggil (mainAppFrame null).", "Info Logout", JOptionPane.INFORMATION_MESSAGE);
            // System.exit(0); // Hindari System.exit jika ini bagian dari aplikasi besar
        }
    }

    private void btn_ubahPasswordActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
            // Ganti "PANEL_UBAH_PASSWORD" dengan konstanta nama panel yang benar di MainAppFrame
            // mainAppFrame.showPanel(MainAppFrame.PANEL_UBAH_PASSWORD);
            // JOptionPane.showMessageDialog(this, "Navigasi ke Ubah Password (Belum diimplementasikan di MainAppFrame)", "Info", JOptionPane.INFORMATION_MESSAGE);
            // Contoh jika UbahPassword adalah JFrame terpisah (kurang ideal untuk CardLayout)
            new UbahPassword().setVisible(true);
        }
    }

    private void btn_editFotoActionPerformed(java.awt.event.ActionEvent evt) {
        showEditPhotoOptions();
    }
    
    private void btn_riwayatPesananActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
             mainAppFrame.showPanel(MainAppFrame.PANEL_RIWAYAT_PESANAN); // Ganti dengan konstanta yang benar
        }
    }

    private void btn_pesananSayaActionPerformed(java.awt.event.ActionEvent evt) {
        if (mainAppFrame != null) {
             mainAppFrame.showPanel(MainAppFrame.PANEL_PESANAN_SAYA); // Ganti dengan konstanta yang benar
        }
    }
    
    // Metode dispose() tidak diperlukan untuk JPanel dan bisa menyebabkan error jika dipanggil.
    // Hapus atau komentari jika ada.
    // private void dispose() {
    // throw new UnsupportedOperationException("Not supported yet.");
    // }
}