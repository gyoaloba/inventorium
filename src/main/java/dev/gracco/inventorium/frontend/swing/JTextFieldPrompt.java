package dev.gracco.inventorium.frontend.swing;

import dev.gracco.inventorium.frontend.Theme;
import lombok.Getter;
import lombok.Setter;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldPrompt extends JLabel implements FocusListener, DocumentListener {

    public enum Show {
        ALWAYS, FOCUS_GAINED, FOCUS_LOST
    }

    private final JTextComponent component;
    private final Document document;

    @Getter @Setter private Show show;
    @Getter @Setter private boolean showPromptOnce;
    private int focusLost;

    public JTextFieldPrompt(String text, JTextComponent component) {
        this(text, component, Show.ALWAYS);
    }

    public JTextFieldPrompt(String text, JTextComponent component, Show show) {
        this.component = component;
        this.document = component.getDocument();
        this.show = show;

        setText(text);
        setFont(component.getFont());
        setForeground(Theme.COLOR_UNFOCUSED_TEXT);
        setBorder(new EmptyBorder(component.getInsets()));
        setHorizontalAlignment(LEADING);

        component.addFocusListener(this);
        document.addDocumentListener(this);
        component.setLayout(new BorderLayout());
        component.add(this);

        checkForPrompt();
    }

    //public void changeAlpha(float alpha) {
    //    changeAlpha((int) (alpha * 255));
    //}

    public void changeAlpha(int alpha) {
        alpha = Math.min(255, Math.max(0, alpha));
        Color fg = getForeground();
        setForeground(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), alpha));
    }

    //public void changeStyle(int style) {
    //    setFont(getFont().deriveFont(style));
    //}

    private void checkForPrompt() {
        if (document.getLength() > 0) {
            setVisible(false);
            return;
        }

        if (showPromptOnce && focusLost > 0) {
            setVisible(false);
            return;
        }

        if (component.hasFocus()) {
            setVisible(show == Show.ALWAYS || show == Show.FOCUS_GAINED);
        } else {
            setVisible(show == Show.ALWAYS || show == Show.FOCUS_LOST);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        checkForPrompt();
    }

    @Override
    public void focusLost(FocusEvent e) {
        focusLost++;
        checkForPrompt();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkForPrompt();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkForPrompt();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Not used
    }
}