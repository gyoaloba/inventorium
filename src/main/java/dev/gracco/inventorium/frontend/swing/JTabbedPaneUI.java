package dev.gracco.inventorium.frontend.swing;

import dev.gracco.inventorium.frontend.Theme;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

public class JTabbedPaneUI extends BasicTabbedPaneUI {
    @Override
    protected void installDefaults() {
        super.installDefaults();
        tabAreaInsets.right = 0;
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        g.setColor(isSelected ? Theme.COLOR_BACKGROUND : Theme.COLOR_BACKGROUND_UNFOCUSED);
        g.fillRect(x, y, w, h);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Theme.COLOR_BACKGROUND_UNFOCUSED);
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(4)); // 2px border
        g2.drawRect(x, y, w - 1, h - 1);
        g2.setStroke(oldStroke);
    }

    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return 40;
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        int tabCount = tabPane.getTabCount();
        int totalWidth = tabPane.getWidth() - 4; //TAE Method (Two pixels are taken by the window per side)
        return totalWidth / tabCount;
    }
}
