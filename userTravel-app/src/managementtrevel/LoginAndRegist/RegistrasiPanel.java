package managementtrevel.LoginAndRegist;

import Asset.AppTheme;
import controller.RegisterController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import managementtrevel.AuthFrame;

public class RegistrasiPanel extends JPanel {

    private AuthFrame authFrame;

    private JPanel panelLeft;
    private JButton btnNavRegister;
    private JButton btnNavLogin;
    private JPanel panelWelcomeHeader;
    private JLabel lblWelcome;

    private JPanel panelRegisterFields;
    private JLabel lblRegisterTitle;
    private JLabel lblUsername;
    private JTextField txtUsername;
    private JLabel lblEmail;
    private JTextField txtEmail;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JLabel lblNoTelepon;
    private JTextField txtNoTelepon;
    private JLabel lblAlamat;
    private JTextField txtAlamat; 
    private JButton btnRegisterAction;
    private JLabel lblHaveAccount;
    private JButton btnLoginLink;

    public RegistrasiPanel(AuthFrame authFrame) {
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
        btnNavLogin.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        btnNavLogin.setForeground(AppTheme.SIDEPANEL_TEXT_INACTIVE);
        btnNavLogin.setBackground(AppTheme.PRIMARY_BLUE_LIGHT);
        btnNavLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNavLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnNavLogin.getPreferredSize().height + 10));
        btnNavLogin.setMargin(new Insets(10,0,10,0));
        btnNavLogin.setOpaque(true);
        btnNavLogin.setBorderPainted(false);

        btnNavRegister = new JButton("Register");
        btnNavRegister.setFont(AppTheme.FONT_PRIMARY_BOLD);
        btnNavRegister.setForeground(AppTheme.TEXT_WHITE);
        btnNavRegister.setBackground(AppTheme.ACCENT_ORANGE);
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

        panelRegisterFields = new JPanel(new GridBagLayout());
        panelRegisterFields.setBorder(new EmptyBorder(20, 40, 20, 40));
        panelRegisterFields.setBackground(AppTheme.PANEL_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        lblRegisterTitle = new JLabel("User Register");
        lblRegisterTitle.setFont(AppTheme.FONT_TITLE_LARGE);
        lblRegisterTitle.setForeground(AppTheme.TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 20, 5);
        panelRegisterFields.add(lblRegisterTitle, gbc);

        gbc.anchor = GridBagConstraints.WEST; gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        lblUsername = new JLabel("Username");
        lblUsername.setFont(AppTheme.FONT_LABEL_FORM);
        lblUsername.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 1;
        panelRegisterFields.add(lblUsername, gbc);

        txtUsername = new JTextField(20);
        txtUsername.setFont(AppTheme.FONT_TEXT_FIELD);
        txtUsername.setBorder(AppTheme.createDefaultInputBorder());
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelRegisterFields.add(txtUsername, gbc);

        lblEmail = new JLabel("Email");
        lblEmail.setFont(AppTheme.FONT_LABEL_FORM);
        lblEmail.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 2;
        panelRegisterFields.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(AppTheme.FONT_TEXT_FIELD);
        txtEmail.setBorder(AppTheme.createDefaultInputBorder());
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelRegisterFields.add(txtEmail, gbc);

        lblPassword = new JLabel("Password");
        lblPassword.setFont(AppTheme.FONT_LABEL_FORM);
        lblPassword.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 3;
        panelRegisterFields.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(AppTheme.FONT_TEXT_FIELD);
        txtPassword.setBorder(AppTheme.createDefaultInputBorder());
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelRegisterFields.add(txtPassword, gbc);

        lblNoTelepon = new JLabel("Nomor Telepon");
        lblNoTelepon.setFont(AppTheme.FONT_LABEL_FORM);
        lblNoTelepon.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 4;
        panelRegisterFields.add(lblNoTelepon, gbc);

        txtNoTelepon = new JTextField(20);
        txtNoTelepon.setFont(AppTheme.FONT_TEXT_FIELD);
        txtNoTelepon.setBorder(AppTheme.createDefaultInputBorder());
        gbc.gridx = 1; gbc.gridy = 4; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelRegisterFields.add(txtNoTelepon, gbc);

        lblAlamat = new JLabel("Alamat");
        lblAlamat.setFont(AppTheme.FONT_LABEL_FORM);
        lblAlamat.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 5;
        panelRegisterFields.add(lblAlamat, gbc);

        txtAlamat = new JTextField(20); // Ubah ke JTextArea jika perlu multi-baris
        txtAlamat.setFont(AppTheme.FONT_TEXT_FIELD);
        txtAlamat.setBorder(AppTheme.createDefaultInputBorder());
        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelRegisterFields.add(txtAlamat, gbc);

        btnRegisterAction = new JButton("Register");
        btnRegisterAction.setFont(AppTheme.FONT_BUTTON);
        btnRegisterAction.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        btnRegisterAction.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        btnRegisterAction.setOpaque(true);
        btnRegisterAction.setBorderPainted(false);
        btnRegisterAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegisterAction.setPreferredSize(new Dimension(100,35));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 5, 5, 5);
        panelRegisterFields.add(btnRegisterAction, gbc);

        lblHaveAccount = new JLabel("Already have an account?");
        lblHaveAccount.setFont(AppTheme.FONT_PRIMARY_DEFAULT);
        lblHaveAccount.setForeground(AppTheme.TEXT_SECONDARY_DARK);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 5, 5, 0);
        panelRegisterFields.add(lblHaveAccount, gbc);

        btnLoginLink = new JButton("Log In");
        btnLoginLink.setFont(AppTheme.FONT_LINK_BUTTON);
        btnLoginLink.setForeground(AppTheme.BUTTON_LINK_FOREGROUND);
        btnLoginLink.setBorderPainted(false);
        btnLoginLink.setContentAreaFilled(false);
        btnLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 0;
        gbc.insets = new Insets(15, 0, 5, 5);
        panelRegisterFields.add(btnLoginLink, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.weighty = 1.0; 
        panelRegisterFields.add(new JLabel(), gbc);

        this.add(panelRegisterFields, BorderLayout.CENTER);

        addFocusBorderEffect(txtUsername);
        addFocusBorderEffect(txtEmail);
        addFocusBorderEffect(txtPassword);
        addFocusBorderEffect(txtNoTelepon);
        addFocusBorderEffect(txtAlamat);
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
        btnRegisterAction.addActionListener(this::registerActionPerformed);
        btnLoginLink.addActionListener(this::navLoginActionPerformed);
    }

    private void navRegisterActionPerformed(ActionEvent evt) {
        authFrame.navigateToRegister();
    }

    private void navLoginActionPerformed(ActionEvent evt) {
        authFrame.navigateToLogin();
    }

    private void registerActionPerformed(ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String noTelepon = txtNoTelepon.getText().trim();
        String alamat = txtAlamat.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username Harus Diisi", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
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
        if (noTelepon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor Telepon Harus Diisi", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtNoTelepon.requestFocus();
            return;
        }
        if (alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alamat Harus Diisi", "Input Error", JOptionPane.ERROR_MESSAGE);
            txtAlamat.requestFocus();
            return;
        }

        RegisterController controller = new RegisterController();
        boolean sukses = controller.register(username, email, password, noTelepon, alamat);

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil, Silahkan Login Kembali", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
            authFrame.navigateToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Email sudah terdaftar, gunakan email lain", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
        }
    }
}