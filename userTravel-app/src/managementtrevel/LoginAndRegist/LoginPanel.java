package managementtrevel.LoginAndRegist;

import controller.AuthController;
import managementtrevel.AuthFrame; 
import Asset.AppTheme; 
import model.Session;
import model.UserModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginPanel extends JPanel {

    private AuthFrame authFrame;

    private JPanel panelLeft;
    private JButton btnNavRegister;
    private JButton btnNavLogin;
    private JPanel panelWelcomeHeader;
    private JLabel lblWelcome;

    private JPanel panelLoginFields;
    private JLabel lblLoginTitle;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JButton btnLoginAction;
    private JLabel lblNoAccount;
    private JButton btnRegisterLink;

    public LoginPanel(AuthFrame authFrame) {
        this.authFrame = authFrame;
        initializeUI();
        setupEventListeners();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(AppTheme.PANEL_BACKGROUND);

        panelLeft = new JPanel(new BorderLayout(10, 10));
        panelLeft.setBackground(AppTheme.SIDEPANEL_BACKGROUND);
        panelLeft.setPreferredSize(new Dimension(220, 0));
        panelLeft.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelWelcomeHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelWelcomeHeader.setBackground(AppTheme.SIDEPANEL_BACKGROUND);
        lblWelcome = new JLabel("WELCOME");
        lblWelcome.setFont(AppTheme.FONT_TITLE_MEDIUM);
        lblWelcome.setForeground(AppTheme.TEXT_WHITE);
        panelWelcomeHeader.add(lblWelcome);
        panelLeft.add(panelWelcomeHeader, BorderLayout.NORTH);

        JPanel panelNavButtons = new JPanel();
        panelNavButtons.setLayout(new BoxLayout(panelNavButtons, BoxLayout.Y_AXIS));
        panelNavButtons.setOpaque(false);

        btnNavLogin = new JButton("Log In");
        btnNavLogin.setFont(AppTheme.FONT_PRIMARY_BOLD);
        btnNavLogin.setForeground(AppTheme.TEXT_WHITE); 
        btnNavLogin.setBackground(AppTheme.ACCENT_ORANGE); 
        btnNavLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNavLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnNavLogin.getPreferredSize().height + 10));
        btnNavLogin.setMargin(new Insets(10,0,10,0));
        btnNavLogin.setOpaque(true);
        btnNavLogin.setBorderPainted(false);

        btnNavRegister = new JButton("Register");
        btnNavRegister.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        btnNavRegister.setForeground(AppTheme.SIDEPANEL_TEXT_INACTIVE);
        btnNavRegister.setBackground(AppTheme.PRIMARY_BLUE_LIGHT);
        btnNavRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNavRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnNavRegister.getPreferredSize().height + 10));
        btnNavRegister.setMargin(new Insets(10,0,10,0));
        btnNavRegister.setOpaque(true);
        btnNavRegister.setBorderPainted(false);

        panelNavButtons.add(Box.createVerticalGlue());
        panelNavButtons.add(btnNavLogin);
        panelNavButtons.add(Box.createRigidArea(new Dimension(0, 10)));
        panelNavButtons.add(btnNavRegister);
        panelNavButtons.add(Box.createVerticalGlue());

        panelLeft.add(panelNavButtons, BorderLayout.CENTER);
        this.add(panelLeft, BorderLayout.WEST);

        panelLoginFields = new JPanel(new GridBagLayout());
        panelLoginFields.setBorder(new EmptyBorder(40, 40, 40, 40));
        panelLoginFields.setBackground(AppTheme.PANEL_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        lblLoginTitle = new JLabel("User Log In");
        lblLoginTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblLoginTitle.setForeground(AppTheme.TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 25, 5);
        panelLoginFields.add(lblLoginTitle, gbc);

        gbc.anchor = GridBagConstraints.WEST; gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        lblEmail = new JLabel("Email");
        lblEmail.setFont(AppTheme.FONT_LABEL_FORM);
        lblEmail.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 1;
        panelLoginFields.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(AppTheme.FONT_TEXT_FIELD);
        txtEmail.setBackground(AppTheme.INPUT_BACKGROUND);
        txtEmail.setForeground(AppTheme.INPUT_TEXT);
        txtEmail.setCaretColor(AppTheme.ACCENT_ORANGE);
        txtEmail.setSelectionColor(AppTheme.PRIMARY_BLUE_LIGHT.brighter());
        txtEmail.setSelectedTextColor(AppTheme.TEXT_WHITE);
        txtEmail.setBorder(AppTheme.createDefaultInputBorder());
        txtEmail.setMinimumSize(new Dimension(200, txtEmail.getPreferredSize().height + 5));
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelLoginFields.add(txtEmail, gbc);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(AppTheme.FONT_LABEL_FORM);
        lblPassword.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        panelLoginFields.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(AppTheme.FONT_TEXT_FIELD);
        txtPassword.setBackground(AppTheme.INPUT_BACKGROUND);
        txtPassword.setForeground(AppTheme.INPUT_TEXT);
        txtPassword.setCaretColor(AppTheme.ACCENT_ORANGE);
        txtPassword.setSelectionColor(AppTheme.PRIMARY_BLUE_LIGHT.brighter());
        txtPassword.setSelectedTextColor(AppTheme.TEXT_WHITE);
        txtPassword.setBorder(AppTheme.createDefaultInputBorder());
        txtPassword.setMinimumSize(new Dimension(200, txtPassword.getPreferredSize().height + 5));
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelLoginFields.add(txtPassword, gbc);

        btnLoginAction = new JButton("Log In");
        btnLoginAction.setFont(AppTheme.FONT_BUTTON);
        btnLoginAction.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        btnLoginAction.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        btnLoginAction.setOpaque(true);
        btnLoginAction.setBorderPainted(false);
        btnLoginAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLoginAction.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 5, 5);
        panelLoginFields.add(btnLoginAction, gbc);

        lblNoAccount = new JLabel("Don't have an account?");
        lblNoAccount.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblNoAccount.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 5, 5, 0);
        panelLoginFields.add(lblNoAccount, gbc);

        btnRegisterLink = new JButton("Register");
        btnRegisterLink.setFont(AppTheme.FONT_LINK_BUTTON);
        btnRegisterLink.setForeground(AppTheme.BUTTON_LINK_FOREGROUND);
        btnRegisterLink.setBorderPainted(false);
        btnRegisterLink.setContentAreaFilled(false);
        btnRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0;
        gbc.insets = new Insets(15, 0, 5, 5);
        panelLoginFields.add(btnRegisterLink, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weighty = 1.0;
        panelLoginFields.add(new JLabel(), gbc);

        this.add(panelLoginFields, BorderLayout.CENTER);

        addFocusBorderEffect(txtEmail);
        addFocusBorderEffect(txtPassword);
    }

    private void addFocusBorderEffect(JTextField textField) {
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
            }
        });
    }

    private void setupEventListeners() {
        btnNavRegister.addActionListener(this::navRegisterActionPerformed);
        btnNavLogin.addActionListener(this::navLoginActionPerformed);
        btnLoginAction.addActionListener(this::loginActionPerformed);
        btnRegisterLink.addActionListener(this::navRegisterActionPerformed);
    }

    private void navLoginActionPerformed(ActionEvent evt) {
        authFrame.navigateToLogin();
    }

    private void loginActionPerformed(ActionEvent evt) {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email Harus Diisi", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password Harus Diisi", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        AuthController auth = new AuthController();
        UserModel loggedUser = auth.login(email, password);

        if (loggedUser != null) {
            Session.currentUser = loggedUser;
            JOptionPane.showMessageDialog(this, "Login berhasil, Selamat datang " + loggedUser.getNamaLengkap(), "Login Success", JOptionPane.INFORMATION_MESSAGE);
            authFrame.onLoginSuccess(loggedUser); 
        } else {
            JOptionPane.showMessageDialog(this, "Email atau password salah", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void navRegisterActionPerformed(ActionEvent evt) {
        authFrame.navigateToRegister();
    }
}