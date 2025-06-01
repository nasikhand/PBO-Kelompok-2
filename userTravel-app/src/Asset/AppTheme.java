package Asset; 

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.border.Border; 
import javax.swing.border.EmptyBorder;

public class AppTheme {

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


    // Helper method untuk border (opsional)
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
}
