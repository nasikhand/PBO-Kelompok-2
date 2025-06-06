/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package managementtrevel.TripOrder;

import java.util.List;

import javax.swing.JOptionPane;

import db.Koneksi;
import db.dao.ReservasiDAO;
import managementtrevel.HomeUser.UserProfile;
import model.PaketPerjalananModel;
import model.ReservasiModel;
import model.Session;
import db.dao.KotaDAO;

/**
 *
 * @author aldy
 */
public class UserOrder extends javax.swing.JFrame {

    private ReservasiDAO reservasiDAO; 
    private static int userId;
    public static int getUserId() {
        return userId;
    }

    /**
     * Creates new form UserOrder
     */
    public UserOrder() {
        System.out.println("Constructor PanelUserOrder dijalankan");
        initComponents();

        // Inisialisasi DAO (misal pakai koneksi dari class Koneksi)
        reservasiDAO = new ReservasiDAO(Koneksi.getConnection());
        System.out.println("DAO dibuat");

        // Setelah komponen UI siap, panggil method untuk load data
        loadDataReservasi();
    }

    private void loadDataReservasi() {
        try {
            if (Session.currentUser == null) {
                JOptionPane.showMessageDialog(this, "User belum login.");
                return;
            }
            
            int userId = Session.currentUser.getId();  // pastikan method getId() ada di User class
            System.out.println("User ID sekarang: " + userId);
            
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        foto_user.setFocusCycleRoot(true);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_back)
                        .addGap(103, 103, 103)
                        .addComponent(jLabel1)
                        .addGap(0, 182, Short.MAX_VALUE))
                    .addComponent(panelTemplate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btn_back))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTemplate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(168, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        new UserProfile().show();
        this.dispose();
    }//GEN-LAST:event_btn_backActionPerformed


    private void btn_detailPesananActionPerformed(java.awt.event.ActionEvent evt) {
        
        
        //GEN-FIRST:event_btn_detailPesananActionPerformed
        
    }//GEN-LAST:event_btn_detailPesananActionPerformed

    private void tf_hargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_hargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_hargaActionPerformed

    private void tf_statusPesananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_statusPesananActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tf_statusPesananActionPerformed

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
            java.util.logging.Logger.getLogger(UserOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_back;
    private javax.swing.JButton btn_detailPesanan;
    private javax.swing.JLabel foto_user;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel panelTemplate;
    private javax.swing.JLabel status_pesanan;
    private javax.swing.JTextField tf_harga;
    private javax.swing.JTextField tf_hari;
    private javax.swing.JTextField tf_namakota;
    private javax.swing.JTextField tf_orang;
    private javax.swing.JTextField tf_orang1;
    private javax.swing.JTextField tf_statusPesanan;
    // End of variables declaration//GEN-END:variables
}
