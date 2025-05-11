package dev.gracco.inventorium.frontend.swing;

import dev.gracco.inventorium.frontend.Theme;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class JTabbedPaneUI extends BasicTabbedPaneUI {
    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabAreaInsets.top = 10;
        tabAreaInsets.left = 0;
        tabAreaInsets.bottom = 0;
        tabAreaInsets.right = 0;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        g.setColor(isSelected ? Theme.COLOR_BACKGROUND : Theme.COLOR_BACKGROUND_UNFOCUSED);
        g.fillRect(x, y, w, h);
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        g.setColor(Theme.COLOR_BACKGROUND);
        g.fillRect(0, 0, tabPane.getWidth(), calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight));
        super.paintTabArea(g, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        if (isSelected) {
            g.setColor(Theme.COLOR_PRIMARY);
            g.fillRect(x, y + 2, w, 5); // Top border
            g.setColor(Theme.COLOR_UNFOCUSED_TEXT);
            g.fillRect(x + 4, y + 50, w - 8, 1); // Bottom border
        }
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return 50;
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        int tabCount = tabPane.getTabCount();
        int totalWidth = tabPane.getWidth(); //TAE Method (Two pixels are taken by the window per side)
        return totalWidth / tabCount;
    }
}
