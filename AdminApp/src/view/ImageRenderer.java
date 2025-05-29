/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Kelas ini bertanggung jawab untuk merender sebuah ImageIcon di dalam sel JTable.
 * Ia akan menampilkan ikon, bukan teks.
 */
public class ImageRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        // value adalah objek yang ada di dalam sel, kita harapkan ini adalah ImageIcon
        if (value instanceof ImageIcon) {
            setIcon((ImageIcon) value); // Set ikon untuk JLabel ini
            setText(""); // Tidak ada teks
            setHorizontalAlignment(JLabel.CENTER); // Pusatkan gambar
        } else {
            // Jika bukan gambar, tampilkan teks "Gambar tidak ada"
            setText("Gambar tidak ada");
            setIcon(null);
        }
        return this;
    }
}
