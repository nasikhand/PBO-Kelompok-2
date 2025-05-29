/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author NASIKH
 */
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import view.AdminLoginView;
import javax.swing.*;

public class main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception e) {
            System.err.println("Gagal memuat Tema FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> {
            new AdminLoginView().setVisible(true);
        });
    }
}