package managementtrevel.TripOrder;

import Asset.AppTheme; // Impor AppTheme Anda
import db.Koneksi;
import db.dao.ReservasiDAO;
import managementtrevel.MainAppFrame; // Impor MainAppFrame
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;

import javax.swing.JPanel; 
import java.awt.event.ActionEvent;
import java.awt.BorderLayout; 
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Dimension; 
import java.awt.Color; 
import java.awt.Cursor; 
import java.awt.Font;   
import javax.swing.BorderFactory; 
import javax.swing.border.EmptyBorder; 
import javax.swing.JLabel; 
import javax.swing.JButton; 
import javax.swing.JTextField; 
import javax.swing.SwingConstants; 
import javax.swing.JOptionPane; 
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent;
import java.util.List;  

/**
 * PanelUserOrder class sebagai JPanel untuk diintegrasikan dengan CardLayout di MainAppFrame.
 * Ini akan menampilkan pesanan pengguna.
 */
public class PanelUserOrder extends JPanel { 

    private MainAppFrame mainAppFrame; 

    // Deklarasi komponen UI
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_detailPesanan;
    private javax.swing.JLabel foto_user;
    private javax.swing.JLabel jLabel1; // Judul "Pesanan Anda"
    private javax.swing.JLabel jLabel7; // "Harga:"
    private javax.swing.JPanel panelTemplate;
    private javax.swing.JLabel status_pesanan;
    private javax.swing.JTextField tf_harga;
    private javax.swing.JTextField tf_hari;
    private javax.swing.JTextField tf_namakota;
    private javax.swing.JTextField tf_orang;
    private javax.swing.JTextField tf_orang1; // "Rating"
    private javax.swing.JTextField tf_statusPesanan;

    private ReservasiDAO reservasiDAO; 
    private static int userId;
    public static int getUserId() {
        return userId;
    }

    public PanelUserOrder(MainAppFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        initComponents(); // PENTING: Pastikan ini adalah kode dari NetBeans Anda

        reservasiDAO = new ReservasiDAO(Koneksi.getConnection());

        loadDataReservasi();
        
        // Mengatur layout untuk JPanel utama ini (PanelUserOrder)
        // setelah initComponents() menginisialisasi komponen.
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btn_back)
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Dorong jLabel1 ke tengah jika bisa
                .addComponent(jLabel1)
                .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) // Dorong jLabel1 ke tengah jika bisa
            .addComponent(panelTemplate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // panelTemplate mengambil lebar penuh
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(btn_back)
                .addComponent(jLabel1))
            .addPreferredGap(ComponentPlacement.UNRELATED) // Beri jarak antara header dan template
            .addComponent(panelTemplate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // panelTemplate mengambil sisa tinggi
        );
        
        applyAppTheme(); 
        setupActionListeners(); 
    }

    private void loadDataReservasi() {
        try {
            if (Session.currentUser == null) {
                JOptionPane.showMessageDialog(this, "User belum login.");
                return;
            }
            
            int userId = Session.currentUser.getId();  // pastikan method getId() ada di User class
            
            List<ReservasiModel> list = reservasiDAO.getReservasiAktifDenganTrip(userId);

            System.out.println("Jumlah reservasi yang ditemukan: " + list.size());

            for (ReservasiModel r : list) {
                System.out.println("TripType: " + r.getTripType());
                if (r.getPaket() != null) {
                    System.out.println("Nama Kota: " + r.getPaket().getNamaKota());
                    System.out.println("Rating: " + r.getPaket().getRating());
                } else {
                    System.out.println("PAKET NULL");
                }
            }
    

            if (!list.isEmpty()) {
                ReservasiModel reservasi = list.get(0);  // contoh: ambil reservasi pertama
                PaketPerjalananModel paket = reservasi.getPaket();

                if (paket != null) {
                    tf_namakota.setText(paket.getNamaKota());
                    tf_orang1.setText(String.valueOf(paket.getRating()));
                } else {
                    tf_namakota.setText("Tidak ada data");
                    tf_orang1.setText("-");
                }
            } else {
                tf_namakota.setText("Tidak ada reservasi");
                tf_orang1.setText("-");
            }


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data reservasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        this.setBorder(new EmptyBorder(15, 20, 15, 20)); 

        if (jLabel1 != null) {
            jLabel1.setFont(AppTheme.FONT_TITLE_LARGE);
            jLabel1.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }

        if (btn_back != null) {
            btn_back.setFont(AppTheme.FONT_BUTTON);
            btn_back.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
            btn_back.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
            btn_back.setOpaque(true);
            btn_back.setBorderPainted(false);
            btn_back.setFocusPainted(false);
            btn_back.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_back.setBorder(new EmptyBorder(8, 15, 8, 15));
            addHoverEffect(btn_back, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
        }

        if (panelTemplate != null) {
            panelTemplate.setBackground(Color.WHITE);
            panelTemplate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
        }

        if (foto_user != null) {
            foto_user.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
            foto_user.setForeground(AppTheme.TEXT_SECONDARY_DARK);
            foto_user.setBackground(AppTheme.BACKGROUND_LIGHT_GRAY);
            foto_user.setOpaque(true);
            foto_user.setHorizontalAlignment(SwingConstants.CENTER);
            foto_user.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
            if (foto_user.getPreferredSize().width == 0 || foto_user.getPreferredSize().height == 0) {
                 foto_user.setPreferredSize(new Dimension(111, 111)); 
            }
        }

        styleDisplayTextField(tf_namakota, AppTheme.FONT_SUBTITLE, AppTheme.TEXT_DARK);
        styleDisplayTextField(tf_hari, AppTheme.FONT_PRIMARY_MEDIUM, AppTheme.TEXT_SECONDARY_DARK);
        styleDisplayTextField(tf_orang, AppTheme.FONT_PRIMARY_MEDIUM, AppTheme.TEXT_SECONDARY_DARK);
        styleDisplayTextField(tf_orang1, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_SECONDARY_DARK); 
        styleDisplayTextField(tf_harga, AppTheme.FONT_PRIMARY_BOLD, AppTheme.ACCENT_ORANGE); 
        styleDisplayTextField(tf_statusPesanan, AppTheme.FONT_PRIMARY_BOLD, AppTheme.PRIMARY_BLUE_DARK); 

        if (jLabel7 != null) { 
            jLabel7.setFont(AppTheme.FONT_LABEL_FORM);
            jLabel7.setForeground(AppTheme.TEXT_DARK);
        }
        if (status_pesanan != null) { 
            status_pesanan.setFont(AppTheme.FONT_LABEL_FORM);
            status_pesanan.setForeground(AppTheme.TEXT_DARK);
        }

        if (btn_detailPesanan != null) {
            btn_detailPesanan.setFont(AppTheme.FONT_BUTTON);
            btn_detailPesanan.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
            btn_detailPesanan.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
            btn_detailPesanan.setOpaque(true);
            btn_detailPesanan.setBorderPainted(false);
            btn_detailPesanan.setFocusPainted(false);
            btn_detailPesanan.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn_detailPesanan.setBorder(new EmptyBorder(8, 15, 8, 15));
            addHoverEffect(btn_detailPesanan, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
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
    
    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        if (button == null) return; // Tambahkan null check
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
        // Listener biasanya sudah diatur di initComponents oleh NetBeans.
        // Jika Anda memindahkannya ke sini, pastikan untuk menghapusnya dari initComponents
        // atau cek apakah listener sudah ada sebelum menambahkannya lagi.
        // Contoh:
        // if (btn_back != null && btn_back.getActionListeners().length == 0) {
        //     btn_back.addActionListener(this::btn_backActionPerformed);
        // }
        // if (btn_detailPesanan != null && btn_detailPesanan.getActionListeners().length == 0) {
        //     btn_detailPesanan.addActionListener(this::btn_detailPesananActionPerformed);
        // }
    }

    // PENTING: Anda HARUS menyalin kode initComponents() dari file UserOrder.java Anda
    // (yang dihasilkan oleh NetBeans GUI Builder) ke dalam metode di bawah ini.
    // Kode placeholder di bawah ini TIDAK akan menghasilkan UI yang benar.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();
        panelTemplate = new javax.swing.JPanel();
        btn_detailPesanan = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        tf_namakota = new javax.swing.JTextField();
        tf_hari = new javax.swing.JTextField();
        tf_orang = new javax.swing.JTextField();
        tf_orang1 = new javax.swing.JTextField();
        tf_harga = new javax.swing.JTextField();
        foto_user = new javax.swing.JLabel();
        status_pesanan = new javax.swing.JLabel();
        tf_statusPesanan = new javax.swing.JTextField();

        // Layout untuk PanelUserOrder (this) akan diatur di konstruktor
        // setelah initComponents() dipanggil.
        // Baris GroupLayout di konstruktor PanelUserOrder akan menggunakan komponen yang diinisialisasi di sini.

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Pesanan Anda");

        btn_back.setText("< Kembali");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        panelTemplate.setBackground(new java.awt.Color(255, 255, 255));

        btn_detailPesanan.setText("Lihat Detail Pesanan");
        btn_detailPesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_detailPesananActionPerformed(evt);
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
        tf_harga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_hargaActionPerformed(evt);
            }
        });

        foto_user.setBackground(new java.awt.Color(0, 0, 0));
        foto_user.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        foto_user.setText("FOTO");
        foto_user.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        foto_user.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        
        status_pesanan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        status_pesanan.setText("Status Pesanan:");

        tf_statusPesanan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tf_statusPesanan.setText(".....");
        tf_statusPesanan.setBorder(null);
        tf_statusPesanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_statusPesananActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTemplateLayout = new javax.swing.GroupLayout(panelTemplate);
        panelTemplate.setLayout(panelTemplateLayout);
        panelTemplateLayout.setHorizontalGroup(
            panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemplateLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTemplateLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
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
                            .addComponent(tf_orang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelTemplateLayout.createSequentialGroup()
                                .addComponent(status_pesanan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tf_statusPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(113, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTemplateLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_detailPesanan)
                        .addContainerGap())))
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
                            .addComponent(tf_harga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(status_pesanan)
                            .addComponent(tf_statusPesanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(btn_detailPesanan))
                    .addGroup(panelTemplateLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_USER_PROFILE); 
        } else {
            System.err.println("MainAppFrame is null in PanelUserOrder (btn_back)");
        }
    }                                        

    private void btn_detailPesananActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        if (mainAppFrame != null) {
            JOptionPane.showMessageDialog(this, "Navigasi ke Detail Pesanan (Belum diimplementasikan di MainAppFrame)", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
             System.err.println("MainAppFrame is null in PanelUserOrder (btn_detailPesanan)");
        }
    }                                                 

    private void tf_hargaActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void tf_statusPesananActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                
}
