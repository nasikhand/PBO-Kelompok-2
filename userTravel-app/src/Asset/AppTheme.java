package Asset; 

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border; 
import javax.swing.border.EmptyBorder;
import java.text.NumberFormat; 
import java.util.Locale; 

/**
 * A utility class that holds all static constants and helper methods for the application's theme.
 * This includes colors, fonts, borders, and common component styling methods.
 */
public class AppTheme {

    // Private constructor to prevent instantiation of this utility class.
    private AppTheme() {}

    //<editor-fold defaultstate="collapsed" desc="Color Palette">
    public static final Color PRIMARY_BLUE_DARK = new Color(28, 73, 107); 
    public static final Color PRIMARY_BLUE_LIGHT = new Color(78, 154, 187); 
    public static final Color ACCENT_ORANGE = new Color(228, 107, 62);

    public static final Color BACKGROUND_LIGHT_GRAY = new Color(235, 237, 238); 
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(45, 45, 45); 
    public static final Color TEXT_SECONDARY_DARK = new Color(80, 80, 80);

    public static final Color PANEL_BACKGROUND = BACKGROUND_LIGHT_GRAY; 
    public static final Color SIDEPANEL_BACKGROUND = PRIMARY_BLUE_DARK; 
    public static final Color SIDEPANEL_TEXT_ACTIVE = ACCENT_ORANGE;
    public static final Color SIDEPANEL_TEXT_INACTIVE = new Color(170, 190, 210); 
    public static final Color SIDEPANEL_TITLE_TEXT = TEXT_WHITE;

    public static final Color BUTTON_PRIMARY_BACKGROUND = ACCENT_ORANGE;
    public static final Color BUTTON_PRIMARY_TEXT = TEXT_WHITE;
    public static final Color BUTTON_SECONDARY_BACKGROUND = PRIMARY_BLUE_LIGHT;
    public static final Color BUTTON_SECONDARY_TEXT = TEXT_WHITE;
    public static final Color BUTTON_LINK_FOREGROUND = PRIMARY_BLUE_DARK;

    public static final Color BORDER_COLOR = new Color(200, 200, 200);
    public static final Color INPUT_BACKGROUND = Color.WHITE;
    public static final Color INPUT_TEXT = TEXT_DARK;
    public static final Color INPUT_BORDER_FOCUS = ACCENT_ORANGE;
    public static final Color PLACEHOLDER_TEXT_COLOR = Color.GRAY;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Font Definitions">
    public static final String FONT_FAMILY_SANS_SERIF = "Segoe UI"; 

    public static final Font FONT_PRIMARY_DEFAULT = new Font(FONT_FAMILY_SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_PRIMARY_MEDIUM = new Font(FONT_FAMILY_SANS_SERIF, Font.PLAIN, 14);
    public static final Font FONT_PRIMARY_BOLD = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 13);

    public static final Font FONT_PRIMARY_MEDIUM_BOLD = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 14);
    public static final Font FONT_LABEL_FORM_BOLD = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 14);
    public static final Font FONT_TITLE_MEDIUM_BOLD = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 20);
    
    public static final Font FONT_TITLE_LARGE = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 24);
    public static final Font FONT_TITLE_MEDIUM = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 16);
    
    public static final Font FONT_STEP_LABEL = new Font(FONT_FAMILY_SANS_SERIF, Font.PLAIN, 13);
    public static final Font FONT_STEP_LABEL_ACTIVE = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 13);

    public static final Font FONT_BUTTON = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 13);
    public static final Font FONT_LINK_BUTTON = new Font(FONT_FAMILY_SANS_SERIF, Font.BOLD, 12);
    public static final Font FONT_LABEL_FORM = new Font(FONT_FAMILY_SANS_SERIF, Font.PLAIN, 14); 
    public static final Font FONT_TEXT_FIELD = new Font(FONT_FAMILY_SANS_SERIF, Font.PLAIN, 14);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Border Definitions">
    public static Border createMatteBorderBottom(Color color) {
        return BorderFactory.createMatteBorder(0, 0, 1, 0, color);
    }

    public static Border createFocusBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER_FOCUS, 2),
            new EmptyBorder(4, 7, 4, 7) 
        );
    }

    public static Border createDefaultInputBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 8, 5, 8)
        );
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Static Component Styling Methods">
    
    /**
     * Applies the standard theme styling to a form label.
     * @param label The JLabel to be styled.
     */
    public static void styleFormLabel(JLabel label) {
        if (label != null) {
            label.setFont(AppTheme.FONT_LABEL_FORM);
            label.setForeground(AppTheme.TEXT_DARK);
            label.setBorder(new EmptyBorder(0, 0, 2, 0)); 
        }
    }

    /**
     * Applies the standard theme styling to a JTextField, including placeholder text logic.
     * @param textField The JTextField to be styled.
     * @param placeholder The placeholder text to display when the field is empty.
     */
    public static void styleInputField(JTextField textField, String placeholder) {
        if (textField == null) return;
        textField.setFont(AppTheme.FONT_TEXT_FIELD);
        textField.setBackground(AppTheme.INPUT_BACKGROUND);
        textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
        textField.setText(placeholder);
        textField.setBorder(AppTheme.createDefaultInputBorder());
        textField.setMargin(new Insets(5, 8, 5, 8));
        
        textField.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                textField.setBorder(AppTheme.createFocusBorder());
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(AppTheme.INPUT_TEXT);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                textField.setBorder(AppTheme.createDefaultInputBorder());
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
                }
            }
        });
    }

    /**
     * Applies the standard theme styling to a JComboBox.
     * @param comboBox The JComboBox to be styled.
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        if (comboBox == null) return;
        comboBox.setFont(AppTheme.FONT_TEXT_FIELD);
        comboBox.setBackground(AppTheme.INPUT_BACKGROUND);
        comboBox.setForeground(AppTheme.INPUT_TEXT);
        comboBox.setBorder(new EmptyBorder(2,2,2,2)); // Add some padding
    }

    /**
     * Applies the standard primary action button style.
     * @param button The JButton to be styled.
     * @param text The text to display on the button.
     */
    public static void stylePrimaryButton(JButton button, String text) {
        if (button == null) return;
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_PRIMARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_PRIMARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
        addHoverEffect(button, AppTheme.BUTTON_PRIMARY_BACKGROUND.darker(), AppTheme.BUTTON_PRIMARY_BACKGROUND);
    }
    
    /**
     * Applies the standard secondary action button style.
     * @param button The JButton to be styled.
     * @param text The text to display on the button.
     */
    public static void styleSecondaryButton(JButton button, String text) {
        if (button == null) return;
        button.setText(text);
        button.setFont(AppTheme.FONT_BUTTON);
        button.setBackground(AppTheme.BUTTON_SECONDARY_BACKGROUND);
        button.setForeground(AppTheme.BUTTON_SECONDARY_TEXT);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        addHoverEffect(button, AppTheme.BUTTON_SECONDARY_BACKGROUND.darker(), AppTheme.BUTTON_SECONDARY_BACKGROUND);
    }
    
    // Private helper for button hover effects, only used within this class.
    private static void addHoverEffect(JButton button, Color hoverColor, Color originalColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { button.setBackground(hoverColor); }
            @Override public void mouseExited(MouseEvent e) { button.setBackground(originalColor); }
        });
    }
    //</editor-fold>

    /**
     * Helper method to format currency. Displays no decimal places if the amount is a whole number.
     * @param amount The double value to format as currency.
     * @return The formatted currency string (e.g., "Rp1.000.000").
     */
    public static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(amount);
    }

    public static void styleInputField(JTextArea textArea, String placeholder) {
    if (textArea == null) return;
    textArea.setFont(AppTheme.FONT_TEXT_FIELD);
    textArea.setBackground(AppTheme.INPUT_BACKGROUND);
    textArea.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
    textArea.setText(placeholder);
    textArea.setBorder(AppTheme.createDefaultInputBorder());
    textArea.setMargin(new Insets(5, 8, 5, 8));
    
    textArea.addFocusListener(new FocusAdapter() {
        @Override public void focusGained(FocusEvent e) {
            textArea.setBorder(AppTheme.createFocusBorder());
            if (textArea.getText().equals(placeholder)) {
                textArea.setText("");
                textArea.setForeground(AppTheme.INPUT_TEXT);
            }
        }
        @Override public void focusLost(FocusEvent e) {
            textArea.setBorder(AppTheme.createDefaultInputBorder());
            if (textArea.getText().isEmpty()) {
                textArea.setText(placeholder);
                textArea.setForeground(AppTheme.PLACEHOLDER_TEXT_COLOR);
            }
        }
    });
}
}
