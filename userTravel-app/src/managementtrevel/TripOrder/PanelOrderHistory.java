package managementtrevel.TripOrder;

import Asset.AppTheme; // Impor AppTheme Anda
import managementtrevel.MainAppFrame; // Impor MainAppFrame

import javax.swing.JPanel;
import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener; // Tidak diperlukan jika menggunakan method reference
import java.awt.Dimension; 
import javax.swing.GroupLayout; 
import javax.swing.LayoutStyle.ComponentPlacement; 
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


public class PanelOrderHistory extends JPanel { 

    private MainAppFrame mainAppFrame; 

    // Deklarasi komponen UI (sesuai dengan yang dihasilkan oleh NetBeans atau Anda buat manual)
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_pesanLagi;
    private javax.swing.JLabel foto_user;
    private javax.swing.JLabel jLabel1; // Judul "Riwayat Pesanan"
    private javax.swing.JLabel jLabel2; // "Tanggal Pemesanan"
    private javax.swing.JLabel jLabel7; // "Harga"
    private javax.swing.JPanel jPanel1; // Panel untuk "Tanggal Pemesanan"
    private javax.swing.JPanel jPanel2; // Panel header dengan judul dan tombol back
    private javax.swing.JPanel panelTemplate; // Panel template untuk setiap item riwayat
    private javax.swing.JTextField tf_harga;
    private javax.swing.JTextField tf_hari;
    private javax.swing.JTextField tf_namakota;
    private javax.swing.JTextField tf_orang;
    private javax.swing.JTextField tf_orang1; // "Rating"

    public PanelOrderHistory(MainAppFrame mainAppFrame) { 
        this.mainAppFrame = mainAppFrame;
        initComponents(); // PENTING: Pastikan ini adalah kode dari NetBeans Anda
        applyAppTheme();  // Terapkan tema setelah komponen diinisialisasi
        setupActionListeners(); // Atur listener jika belum diatur oleh initComponents
    }

    private void applyAppTheme() {
        this.setBackground(AppTheme.PANEL_BACKGROUND);
        // Beri padding pada panel utama ini jika GroupLayout di initComponents tidak mengaturnya
        this.setBorder(new EmptyBorder(15, 20, 15, 20));


        // Panel Header (jPanel2)
        if (jPanel2 != null) {
            jPanel2.setBackground(AppTheme.PANEL_BACKGROUND); // Atau warna lain jika header beda
            jPanel2.setOpaque(false); // Jika ingin background PanelOrderHistory yang terlihat
        }
        if (jLabel1 != null) { // Judul "Riwayat Pesanan"
            jLabel1.setFont(AppTheme.FONT_TITLE_LARGE);
            jLabel1.setForeground(AppTheme.PRIMARY_BLUE_DARK);
        }
        if (btn_back != null) {
            styleSecondaryButton(btn_back, "< Kembali");
        }

        // Panel Tanggal Pemesanan (jPanel1)
        if (jPanel1 != null) {
            jPanel1.setBackground(Color.WHITE); // Atau AppTheme.BACKGROUND_LIGHT_GRAY
            jPanel1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,1,0, AppTheme.BORDER_COLOR), // Garis bawah
                new EmptyBorder(5,10,5,10)
            ));
        }
        if (jLabel2 != null) { // "Tanggal Pemesanan"
            jLabel2.setFont(AppTheme.FONT_LABEL_FORM);
            jLabel2.setForeground(AppTheme.TEXT_DARK);
        }
        

        // Panel Template (Kartu Riwayat Pesanan)
        if (panelTemplate != null) {
            panelTemplate.setBackground(Color.WHITE);
            panelTemplate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
        }

        // Foto User Placeholder di Kartu
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

        // TextFields yang berfungsi sebagai Label
        styleDisplayTextField(tf_namakota, AppTheme.FONT_SUBTITLE, AppTheme.TEXT_DARK);
        styleDisplayTextField(tf_hari, AppTheme.FONT_PRIMARY_MEDIUM, AppTheme.TEXT_SECONDARY_DARK);
        styleDisplayTextField(tf_orang, AppTheme.FONT_PRIMARY_MEDIUM, AppTheme.TEXT_SECONDARY_DARK);
        styleDisplayTextField(tf_orang1, AppTheme.FONT_PRIMARY_DEFAULT, AppTheme.TEXT_SECONDARY_DARK); // Rating
        styleDisplayTextField(tf_harga, AppTheme.FONT_PRIMARY_BOLD, AppTheme.ACCENT_ORANGE); // Harga

        // Label
        if (jLabel7 != null) { // "Harga:"
            jLabel7.setFont(AppTheme.FONT_LABEL_FORM);
            jLabel7.setForeground(AppTheme.TEXT_DARK);
        }
        
        // Tombol Pesan Lagi
        if (btn_pesanLagi != null) {
            stylePrimaryButton(btn_pesanLagi, "Pesan Lagi");
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

    private void stylePrimaryButton(JButton button, String text) {
        // button.setText(text); // Teks diasumsikan sudah diatur oleh NetBeans
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }

    private void styleSecondaryButton(JButton button, String text) {
        // button.setText(text); // Teks diasumsikan sudah diatur oleh NetBeans
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
    }
    
    private void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        if (button == null) return;
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
        // if (btn_pesanLagi != null && btn_pesanLagi.getActionListeners().length == 0) {
        //     btn_pesanLagi.addActionListener(this::btn_pesanLagiActionPerformed);
        // }
    }

    // PENTING: Anda HARUS menyalin kode initComponents() dari file OrderHistory.java Anda
    // (yang dihasilkan oleh NetBeans GUI Builder) ke dalam metode di bawah ini.
    // Kode placeholder di bawah ini TIDAK akan menghasilkan UI yang benar.
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelTemplate = new javax.swing.JPanel();
        btn_pesanLagi = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        tf_namakota = new javax.swing.JTextField();
        tf_hari = new javax.swing.JTextField();
        tf_orang = new javax.swing.JTextField();
        tf_orang1 = new javax.swing.JTextField();
        tf_harga = new javax.swing.JTextField();
        foto_user = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_back = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        jLabel2.setText("Tanggal Pemesanan ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(375, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        panelTemplate.setBackground(new java.awt.Color(255, 255, 255));

        btn_pesanLagi.setText("Pesan Lagi");
        btn_pesanLagi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pesanLagiActionPerformed(evt);
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
                            .addComponent(tf_orang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTemplateLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_pesanLagi)
                        .addContainerGap())))
        );
        panelTemplateLayout.setVerticalGroup(
            panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTemplateLayout.createSequentialGroup()
                .addGroup(panelTemplateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_pesanLagi)
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
                            .addComponent(foto_user, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Riwayat Pesanan"); // Teks judul

        btn_back.setText("< Kembali");
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_back)
                .addGap(18, 18, Short.MAX_VALUE) 
                .addComponent(jLabel1) 
                .addContainerGap(194, Short.MAX_VALUE)) // Sesuaikan agar judul lebih ke tengah
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        // Mengatur tata letak utama untuk PanelOrderHistory
        // Ini adalah contoh, Anda harus menyesuaikannya dengan struktur GroupLayout dari NetBeans Anda
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTemplate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(panelTemplate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (mainAppFrame != null) {
            mainAppFrame.showPanel(MainAppFrame.PANEL_USER_PROFILE); 
        } else {
            System.err.println("MainAppFrame is null in PanelOrderHistory (btn_back)");
        }
    }                                        

    private void btn_pesanLagiActionPerformed(java.awt.event.ActionEvent evt) {                                              
        if (mainAppFrame != null) {
            // Logika untuk "Pesan Lagi" bisa lebih kompleks,
            // mungkin pre-fill Custom Trip atau navigasi ke detail produk
            JOptionPane.showMessageDialog(this, "Fitur 'Pesan Lagi' akan mengarahkan ke detail atau custom trip (Belum diimplementasikan di MainAppFrame)", "Info", JOptionPane.INFORMATION_MESSAGE);
            // Contoh: mainAppFrame.showPanel(MainAppFrame.PANEL_TRIP_DETAIL, idPesanan);
        } else {
            System.err.println("MainAppFrame is null in PanelOrderHistory (btn_pesanLagi)");
        }
    }                                             
}
