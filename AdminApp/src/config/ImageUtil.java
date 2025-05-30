/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config; // atau package util

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageUtil {

    /**
     * Mengubah ukuran sebuah ImageIcon dengan kualitas rendering yang tinggi.
     * @param sourceIcon ImageIcon asli yang akan diubah ukurannya.
     * @param width Lebar baru yang diinginkan.
     * @param height Tinggi baru yang diinginkan.
     * @return ImageIcon baru yang sudah diubah ukurannya dengan kualitas tinggi.
     */
    public static ImageIcon resizeImage(ImageIcon sourceIcon, int width, int height) {
        Image sourceImage = sourceIcon.getImage();

        // Buat gambar baru (BufferedImage) dengan ukuran yang diinginkan
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        // Dapatkan objek Graphics2D untuk menggambar dengan kualitas tinggi
        Graphics2D g2d = resizedImage.createGraphics();

        // Atur hint untuk rendering berkualitas tinggi
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gambar ulang gambar asli ke gambar baru dengan ukuran yang baru
        g2d.drawImage(sourceImage, 0, 0, width, height, null);
        
        // Hapus objek graphics untuk membebaskan memori
        g2d.dispose();

        // Kembalikan sebagai ImageIcon baru
        return new ImageIcon(resizedImage);
    }
}
