package dev.gracco.inventorium.frontend;

import dev.gracco.inventorium.utils.Utilities;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.util.Objects;

public class Theme {
    // Colors
    public static final Color COLOR_PRIMARY = new Color(0xF0A92D);
    public static final Color COLOR_SECONDARY = new Color(0xD9901F);
    public static final Color COLOR_TERTIARY = new Color(0xAB7118);
    public static final Color COLOR_BACKGROUND = new Color(0xFDFDFD);
    public static final Color COLOR_BACKGROUND_UNFOCUSED = new Color(0xDFDFDF);
    public static final Color COLOR_UNFOCUSED_TEXT = new Color(0x7E7E7E);

    // Fonts
    public static final Font BOLD;
    public static final Font ITALIC;
    public static final Font REGULAR;

    // Strings
    public static final String WINDOW_TITLE = "Inventorium: Inventory Management System";

    // Images
    public static final ImageIcon ICON_DARK = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/images/icon.png")));
    public static final ImageIcon BACKGROUND_LOGIN = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/images/bg-login.png")));
    public static final ImageIcon SETTINGS_DESIGN = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/images/settings_design.png")));

    static {
        try {
            BOLD = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Theme.class.getResourceAsStream("/fonts/Poppins-Bold.ttf")));
            ITALIC = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Theme.class.getResourceAsStream("/fonts/Poppins-Italic.ttf")));
            REGULAR = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Theme.class.getResourceAsStream("/fonts/Poppins-Regular.ttf")));
        } catch (Exception e) {
            Utilities.sendFatalError(e);
            throw new ExceptionInInitializerError(e);
        }
    }
}
