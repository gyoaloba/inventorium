package dev.gracco.inventorium.frontend;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

public class Theme {
    // Colors
    public static final Color COLOR_PRIMARY = new Color(0xF0A92D);
    public static final Color COLOR_SECONDARY = new Color(0xC9821A);
    public static final Color COLOR_BACKGROUND = new Color(0xFDFDFD);

    // Fonts
    public static final Font BOLD;
    public static final Font ITALIC;
    public static final Font REGULAR;

    // Strings
    public static final String WINDOW_TITLE = "Inventorium: Inventory Management System";

    // Images
    public static final ImageIcon ICON_DARK = new ImageIcon("src/main/resources/images/icon-dark.png");
    public static final ImageIcon ICON_LIGHT = new ImageIcon("src/main/resources/images/icon-light.png");

    static {
        try {
            BOLD = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Poppins-Bold.ttf"));
            ITALIC = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Poppins-Italic.ttf"));
            REGULAR = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/Poppins-Regular.ttf"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
