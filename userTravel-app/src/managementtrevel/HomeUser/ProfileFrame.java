package managementtrevel.HomeUser;

import Asset.SidebarPanel;
import java.awt.*;
import javax.swing.*;

public class ProfileFrame extends JFrame {

    public ProfileFrame() {
        setTitle("Halaman Profil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(null); // gunakan null layout agar bisa setBounds manual

        SidebarPanel sidebar = new SidebarPanel();
        add(sidebar); // sidebar sudah punya setBounds di dalamnya

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBounds(60, 0, 740, 600); // posisikan di kanan sidebar awal

        contentPanel.add(new JLabel("Ini adalah halaman profil"));
        add(contentPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProfileFrame().setVisible(true);
        });
    }
}
