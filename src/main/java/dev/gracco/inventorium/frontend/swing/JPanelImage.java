package dev.gracco.inventorium.frontend.swing;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;

public class JPanelImage extends JPanel {
    private final Image backgroundImage;

    public JPanelImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
