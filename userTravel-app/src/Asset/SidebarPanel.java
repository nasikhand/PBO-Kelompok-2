package Asset;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import managementtrevel.HomeUser.HomeScreen;
import managementtrevel.HomeUser.ProfileFrame;

public class SidebarPanel extends JPanel {
    private final int COLLAPSED_WIDTH = 60;
    private final int EXPANDED_WIDTH = 200;
    private final int ANIMATION_DELAY = 10;
    private final int ANIMATION_STEP = 10;

    private int currentWidth = COLLAPSED_WIDTH;
    private Timer animationTimer;
    private boolean isExpanded = false;

    private JButton homeButton;
    private JButton profileButton;
    private JButton logoutButton;

    public SidebarPanel() {
        setLayout(null);
        setBackground(Color.DARK_GRAY);
        setBounds(0, 0, COLLAPSED_WIDTH, 600); // overlay posisi kiri atas
        setOpaque(true);

        homeButton = new JButton("🏠");
        profileButton = new JButton("👤");
        logoutButton = new JButton("⏻");

        setupButton(homeButton, 20);
        setupButton(profileButton, 70);
        setupButton(logoutButton, 120);

        // Navigasi
        homeButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new HomeScreen().setVisible(true);
        });

        profileButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new ProfileFrame().setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logout berhasil!");
            System.exit(0);
        });

        // Hover listener rekursif
        addRecursiveHoverListener(this);
    }

    private void setupButton(JButton button, int y) {
        button.setBounds(10, y, EXPANDED_WIDTH - 20, 40);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(Color.DARK_GRAY);
        button.setOpaque(true);
        add(button);
    }

    private void addRecursiveHoverListener(Component comp) {
        comp.addMouseListener(new HoverHandler());
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                addRecursiveHoverListener(child);
            }
        }
    }

    private void startAnimation(boolean expand) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        animationTimer = new Timer(ANIMATION_DELAY, null);
        animationTimer.addActionListener(e -> {
            if (expand) {
                currentWidth += ANIMATION_STEP;
                if (currentWidth >= EXPANDED_WIDTH) {
                    currentWidth = EXPANDED_WIDTH;
                    animationTimer.stop();
                    setButtonLabels(true);
                    isExpanded = true;
                }
            } else {
                currentWidth -= ANIMATION_STEP;
                if (currentWidth <= COLLAPSED_WIDTH) {
                    currentWidth = COLLAPSED_WIDTH;
                    animationTimer.stop();
                    setButtonLabels(false);
                    isExpanded = false;
                }
            }
            setBounds(0, 0, currentWidth, getHeight());
            revalidate();
            repaint();
        });
        animationTimer.start();
    }

    private void setButtonLabels(boolean expanded) {
        homeButton.setText(expanded ? "🏠 Home" : "🏠");
        profileButton.setText(expanded ? "👤 Profile" : "👤");
        logoutButton.setText(expanded ? "⏻ Logout" : "⏻");
    }

    private class HoverHandler extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isExpanded) {
                startAnimation(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Timer exitTimer = new Timer(100, ev -> {
                Point p = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(p, SidebarPanel.this);
                if (!SidebarPanel.this.contains(p)) {
                    if (isExpanded) {
                        startAnimation(false);
                    }
                }
            });
            exitTimer.setRepeats(false);
            exitTimer.start();
        }
    }
}
