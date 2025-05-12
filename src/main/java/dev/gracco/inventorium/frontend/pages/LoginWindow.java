package dev.gracco.inventorium.frontend.pages;

import dev.gracco.inventorium.connection.DatabaseConnection;
import dev.gracco.inventorium.connection.UserManager;
import dev.gracco.inventorium.frontend.Theme;
import dev.gracco.inventorium.frontend.swing.JButtonRounded;
import dev.gracco.inventorium.frontend.swing.JPanelImage;
import dev.gracco.inventorium.frontend.swing.JTextFieldPrompt;
import dev.gracco.inventorium.utils.Security;
import dev.gracco.inventorium.utils.Utilities;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        labelEmail.setFont(Theme.REGULAR.deriveFont(20f));
        labelEmail.setText("Email:");
        inputEmail.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter your email", inputEmail);
        inputEmail.addActionListener(e -> inputPassword.grabFocus());

        labelPassword.setFont(Theme.REGULAR.deriveFont(20f));
        labelPassword.setText("Password:");
        inputPassword.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter your password", inputPassword);
        inputPassword.addActionListener(e -> submitButton.doClick());

        JButtonRounded.beautify(submitButton, "Login");
        submitButton.addActionListener(e -> {
            String email = inputEmail.getText();
            String password = new String(inputPassword.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                Utilities.sendError(this, "Please enter your email and password!");
                return;
            } else if (!email.matches(Security.EMAIL_REGEX)) {
                Utilities.sendError(this, "Enter a valid email!");
                return;
            } else if (password.length() < 8) {
                Utilities.sendError(this, "Password must contain more than 8 characters!");
                return;
            }

            if (UserManager.login(this, email, password)) {
                dispose();
                new MainWindow();
            }
        });

        //Window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                int choice = JOptionPane.showConfirmDialog(LoginWindow.this, "Are you sure you exit?", "Exit", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    DatabaseConnection.close();
                    dispose();
                    System.exit(0);
                }
            }
        });
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
