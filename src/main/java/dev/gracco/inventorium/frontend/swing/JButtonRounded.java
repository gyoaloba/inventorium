package dev.gracco.inventorium.frontend.swing;

import dev.gracco.inventorium.frontend.Theme;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JButtonRounded extends JButton {
    public JButtonRounded() {
        this("");
    }

    public JButtonRounded(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // no border
    }

    // Has to be static cuz I can't directly assign this class to the JButton
    public static void beautify(JButton button, String label){
        button.setText(label);
        button.setFont(Theme.REGULAR.deriveFont(20f));
        button.setBackground(Theme.COLOR_PRIMARY);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Theme.COLOR_SECONDARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(Theme.COLOR_TERTIARY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(Theme.COLOR_PRIMARY);
            }
        });
    }
}
