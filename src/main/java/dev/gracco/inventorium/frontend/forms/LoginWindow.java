package dev.gracco.inventorium.frontend.forms;

import dev.gracco.inventorium.frontend.Theme;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginWindow extends JFrame {
    private JPanel mainPanel;
    private JLabel mainTitle;
    private JLabel mainSubtitle;
    private JTextField inputUsername;
    private JPasswordField inputPassword;
    private JButton submitButton;
    private JLabel labelUsername;
    private JLabel labelPassword;

    public LoginWindow() {
        setContentPane(mainPanel);

        // Theme
        setTitle(Theme.WINDOW_TITLE);
        getContentPane().setBackground(Theme.COLOR_BACKGROUND);
        setIconImage(Theme.ICON_DARK.getImage());

        // Elements
        mainTitle.setText("INVENTORIUM");
        mainTitle.setFont(Theme.BOLD.deriveFont(90f));

        mainSubtitle.setText("The Inventory Management System");
        mainSubtitle.setFont(Theme.REGULAR.deriveFont(30f));

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

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });

        labelUsername.setFont(Theme.REGULAR.deriveFont(20f));
        labelUsername.setText("Username or Email:");
        inputUsername.setFont(Theme.REGULAR.deriveFont(18f));
        inputUsername.setToolTipText("Username or Email");

        labelPassword.setFont(Theme.REGULAR.deriveFont(20f));
        labelPassword.setText("Password:");
        inputPassword.setFont(Theme.REGULAR.deriveFont(18f));
        inputPassword.setToolTipText("Password");


        // Window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
