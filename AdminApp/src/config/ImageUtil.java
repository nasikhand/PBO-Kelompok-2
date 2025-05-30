/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtil {

    /**
     * Metode inti untuk mengubah ukuran ImageIcon dengan kualitas tinggi.
     * Metode ini private karena hanya akan dipanggil oleh metode publik lain di kelas ini.
     */
    private static ImageIcon resize(ImageIcon sourceIcon, int width, int height) {
        Image sourceImage = sourceIcon.getImage();
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(sourceImage, 0, 0, width, height, null);
        g2d.dispose();
        return new ImageIcon(resizedImage);
    }

    /**
     * Memuat dan me-resize gambar dari CLASSPATH (folder 'src/resources').
     * @param resourcePath Path resource, contoh: "/resources/icons/dashboard.png"
     * @return ImageIcon yang sudah di-resize, atau null jika tidak ditemukan.
     */
    public static ImageIcon loadAndResizeFromResource(String resourcePath, int width, int height) {
        try {
            ImageIcon sourceIcon = new ImageIcon(ImageUtil.class.getResource(resourcePath));
            if (sourceIcon.getImage() == null || sourceIcon.getIconWidth() == -1) {
                return null; // Resource tidak ditemukan atau bukan gambar valid
            }
            return resize(sourceIcon, width, height);
        } catch (Exception e) {
            System.err.println("Gagal memuat resource gambar: " + resourcePath);
            return null;
        }
    }

    /**
     * Memuat dan me-resize gambar dari FILE SYSTEM (folder 'images' di root proyek).
     * @param filePath Path file, contoh: "images/paket_perjalanan/nama_file.jpg"
     * @return ImageIcon yang sudah di-resize, atau null jika file tidak ada.
     */
    public static ImageIcon loadAndResizeFromFile(String filePath, int width, int height) {
        File imgFile = new File(filePath);
        if (!imgFile.exists() || !imgFile.isFile()) {
            return null; // File tidak ada
        }
        ImageIcon sourceIcon = new ImageIcon(filePath);
        if (sourceIcon.getImage() == null || sourceIcon.getIconWidth() == -1) {
            return null; // Bukan file gambar yang valid
        }
        return resize(sourceIcon, width, height);
    }
}