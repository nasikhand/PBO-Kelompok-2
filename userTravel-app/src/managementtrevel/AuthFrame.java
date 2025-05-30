package managementtrevel;

import java.awt.*;
import javax.swing.*;
import managementtrevel.LoginAndRegist.LoginPanel;
import managementtrevel.LoginAndRegist.RegistrasiPanel;
import model.UserModel;

public class AuthFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanelContainer;

    private LoginPanel loginPanel;
    private RegistrasiPanel registrasiPanel;

    public static final String LOGIN_PANEL_AUTH = "LOGIN_PANEL_AUTH";
    public static final String REGISTRASI_PANEL_AUTH = "REGISTRASI_PANEL_AUTH";

    public AuthFrame() {
        initializeAuthFrame();
        initComponentsAndPanels();
        showPanel(LOGIN_PANEL_AUTH);
    }

    private void initializeAuthFrame() {
        setTitle("Sinar Jaya Travel - Authentication");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600); 
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        
        // Terapkan warna background utama jika diinginkan untuk AuthFrame
        // this.getContentPane().setBackground(AppTheme.PANEL_BACKGROUND);


        cardLayout = new CardLayout();
        cardPanelContainer = new JPanel(cardLayout);
        // Panel container bisa menggunakan warna tema atau dibiarkan default
        // cardPanelContainer.setBackground(AppTheme.PANEL_BACKGROUND); 
        add(cardPanelContainer);
    }

    private void initComponentsAndPanels() {
        loginPanel = new LoginPanel(this); 
        registrasiPanel = new RegistrasiPanel(this); 

        cardPanelContainer.add(loginPanel, LOGIN_PANEL_AUTH);
        cardPanelContainer.add(registrasiPanel, REGISTRASI_PANEL_AUTH);
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanelContainer, panelName);
    }

    public void navigateToLogin() {
        showPanel(LOGIN_PANEL_AUTH);
    }

    public void navigateToRegister() {
        showPanel(REGISTRASI_PANEL_AUTH);
    }

    public void onLoginSuccess(UserModel user) {
        boolean wasMaximized = (this.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
        
        MainAppFrame mainApp;
        if (user != null) {
            mainApp = new MainAppFrame(user); // Jika MainAppFrame punya konstruktor dengan UserModel
        } else {
            mainApp = new MainAppFrame(); // Jika menggunakan konstruktor default
        }

        if (wasMaximized) {
            mainApp.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        mainApp.setVisible(true);

        // HomeScreen homeScreen = new HomeScreen();
        
        // if (wasMaximized) {
        //     homeScreen.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // }

        // homeScreen.setVisible(true);

        this.dispose(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        lookAndFeel = info.getClassName();
                        break;
                    }
                }
                UIManager.setLookAndFeel(lookAndFeel);
                // Anda bisa set beberapa properti UIManager global di sini jika ingin
                // UIManager.put("Panel.background", AppTheme.PANEL_BACKGROUND);
                // UIManager.put("Button.font", AppTheme.FONT_BUTTON);
            } catch (Exception ex) {
                System.err.println("Failed to initialize LaF: " + ex.getMessage());
            }
            new AuthFrame().setVisible(true);
        });
    }
}