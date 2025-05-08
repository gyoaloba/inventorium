package dev.gracco.inventorium.frontend.pages;

import dev.gracco.inventorium.frontend.Theme;
import dev.gracco.inventorium.frontend.swing.JButtonRounded;
import dev.gracco.inventorium.frontend.swing.JPanelImage;
import dev.gracco.inventorium.frontend.swing.JTextFieldPrompt;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginWindow extends JFrame {
    private JPanel mainPanel;
    private JFormattedTextField inputEmail;
    private JPasswordField inputPassword;
    private JButton submitButton;
    private JLabel pageTitle;
    private JLabel pageSubtitle;
    private JLabel labelEmail;
    private JLabel labelPassword;

    public LoginWindow() {
        setContentPane(mainPanel);

        //Theme
        setTitle(Theme.WINDOW_TITLE);
        getContentPane().setBackground(Theme.COLOR_BACKGROUND);
        setIconImage(Theme.ICON_DARK.getImage());

        //Elements
        pageTitle.setText("INVENTORIUM");
        pageTitle.setFont(Theme.BOLD.deriveFont(90f));

        pageSubtitle.setText("The Inventory Management System");
        pageSubtitle.setFont(Theme.REGULAR.deriveFont(30f));

        submitButton.setText("Login");
        submitButton.setFont(Theme.REGULAR.deriveFont(20f));
        submitButton.setBackground(Theme.COLOR_PRIMARY);
        submitButton.setFocusPainted(false);
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitButton.setBackground(Theme.COLOR_SECONDARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                submitButton.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                submitButton.setBackground(Theme.COLOR_TERTIARY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                submitButton.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        labelEmail.setFont(Theme.REGULAR.deriveFont(20f));
        labelEmail.setText("Email:");
        inputEmail.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter your email", inputEmail);

        labelPassword.setFont(Theme.REGULAR.deriveFont(20f));
        labelPassword.setText("Password:");
        inputPassword.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter your password", inputPassword);

        //Window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void createUIComponents() {
        mainPanel = new JPanelImage(Theme.BACKGROUND_LOGIN.getImage());
        submitButton = new JButtonRounded();
    }
}
