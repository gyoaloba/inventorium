package dev.gracco.inventorium.frontend.pages;

import dev.gracco.inventorium.frontend.Theme;
import dev.gracco.inventorium.frontend.swing.JButtonRounded;
import dev.gracco.inventorium.frontend.swing.JTabbedPaneUI;
import dev.gracco.inventorium.frontend.swing.JTextFieldPrompt;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow extends JFrame {
    private JTabbedPane tabs;
    private JPanel mainPanel;
    private JPanel tabHome;
    private JPanel tabSettings;
    private JPasswordField inputPassOld;
    private JPasswordField inputPassNew;
    private JPasswordField inputPassConfirm;
    private JButton logoutButton;
    private JButton submitPassButton;
    private JLabel labelPasswords;
    private JLabel labelPassOld;
    private JLabel labelPassNew;
    private JLabel labelPassConfirm;

    public MainWindow(){
        setContentPane(mainPanel);
        setFont(Theme.REGULAR.deriveFont(20f));
        setupHome();
        setupSettings();

        tabs.setBackground(Theme.COLOR_BACKGROUND);
        tabs.setUI(new JTabbedPaneUI());

        //Theme
        setTitle(Theme.WINDOW_TITLE);
        getContentPane().setBackground(Theme.COLOR_BACKGROUND);
        setIconImage(Theme.ICON_DARK.getImage());

        //Window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 700));
        setSize(700, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    private void setupHome(){

    }

    private void setupSettings(){
        tabSettings.setBackground(Theme.COLOR_BACKGROUND);

        labelPasswords.setText("Change your password:");
        labelPasswords.setFont(Theme.REGULAR.deriveFont(20f));

        labelPassOld.setText("Old password:");
        labelPassOld.setFont(Theme.REGULAR.deriveFont(20f));
        inputPassOld.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter your old password", inputPassOld);

        labelPassNew.setText("New password:");
        labelPassNew.setFont(Theme.REGULAR.deriveFont(20f));
        inputPassNew.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter your new password", inputPassNew);

        labelPassConfirm.setText("Confirm password:");
        labelPassConfirm.setFont(Theme.REGULAR.deriveFont(20f));
        inputPassConfirm.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Re-enter your new password", inputPassConfirm);

        submitPassButton.setText("Change Password");
        submitPassButton.setFont(Theme.REGULAR.deriveFont(20f));
        submitPassButton.setBackground(Theme.COLOR_PRIMARY);
        submitPassButton.setFocusPainted(false);
        submitPassButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                submitPassButton.setBackground(Theme.COLOR_SECONDARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                submitPassButton.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                submitPassButton.setBackground(Theme.COLOR_TERTIARY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                submitPassButton.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                //enter success
            }
        });

        logoutButton.setText("Logout");
        logoutButton.setFont(Theme.REGULAR.deriveFont(20f));
        logoutButton.setBackground(Theme.COLOR_PRIMARY);
        logoutButton.setFocusPainted(false);
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(Theme.COLOR_SECONDARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                logoutButton.setBackground(Theme.COLOR_TERTIARY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                logoutButton.setBackground(Theme.COLOR_PRIMARY);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                //enter success
            }
        });
    }

    private void createUIComponents() {
        submitPassButton = new JButtonRounded();
        logoutButton = new JButtonRounded();
    }
}
