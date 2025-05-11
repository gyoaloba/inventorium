package dev.gracco.inventorium.frontend.pages;

import dev.gracco.inventorium.connection.DatabaseConnection;
import dev.gracco.inventorium.connection.DatabaseManager;
import dev.gracco.inventorium.frontend.Theme;
import dev.gracco.inventorium.frontend.swing.JButtonRounded;
import dev.gracco.inventorium.frontend.swing.JPanelImage;
import dev.gracco.inventorium.frontend.swing.JTabbedPaneUI;
import dev.gracco.inventorium.frontend.swing.JTextFieldPrompt;
import dev.gracco.inventorium.utils.Utilities;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabs;

    private JPanel tabHome;

    private JPanel tabHead;

    private JPanel tabAdmin;

    private JPanel tabSettings;
    private JPanel settingsImage;
    private JPasswordField inputPassOld;
    private JPasswordField inputPassNew;
    private JPasswordField inputPassConfirm;
    private JButton logoutButton;
    private JButton submitPassButton;
    private JLabel labelPasswords;
    private JLabel labelPassOld;
    private JLabel labelPassNew;
    private JLabel labelPassConfirm;
    private JPanel innerSettings;
    private JLabel welcomeTitle;
    private JPanel inventoryDisplay;
    private JButton button1;
    private JButton button2;
    private JTable homeTable;
    private JPanel designImage;

    public MainWindow(){
        setContentPane(mainPanel);
        setFontRecursively(this, Theme.REGULAR.deriveFont(20f));
        UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
        setupSettings();
        //TEMP
        setupAdmin();
        setupHead();
        setupHome();
        switch (DatabaseManager.getLevel()) {
            case ADMIN -> {
                setupAdmin();
                tabs.remove(1); // Head
                tabs.remove(0); // User
            }
            case HEAD -> {
                setupHead();
                setupHome();
                tabs.remove(2); // Admin
            }
            case USER -> {
                setupHome();
                tabs.remove(2); // Admin
                tabs.remove(1); // Head
            }
        }

        tabs.setBackground(Theme.COLOR_BACKGROUND);
        tabs.setUI(new JTabbedPaneUI());

        //Theme
        setTitle(Theme.WINDOW_TITLE);
        getContentPane().setBackground(Theme.COLOR_BACKGROUND);
        setIconImage(Theme.ICON_DARK.getImage());

        //Window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                askLogout();
            }
        });
        setMinimumSize(new Dimension(700, 700));
        setSize(700, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
    }

    private void askLogout(){
        int choice = JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to log out?", "Log out", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            DatabaseManager.logout();
            new LoginWindow();
        }
    }

    private static void setFontRecursively(Container container, Font font) {
        for (Component component : container.getComponents()) {
            component.setFont(font);
            if (component instanceof Container) {
                setFontRecursively((Container) component, font);
            }
        }
    }

    private void setupSettings(){
        System.out.println(DatabaseManager.getUserUUID().toString());
        tabSettings.setBackground(Theme.COLOR_BACKGROUND);
        innerSettings.setBackground(Theme.COLOR_BACKGROUND);

        labelPasswords.setText("Change your password:");

        labelPassOld.setText("Old password:");
        inputPassOld.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter old password", inputPassOld);
        inputPassOld.addActionListener(e -> inputPassNew.grabFocus());

        labelPassNew.setText("New password:");
        inputPassNew.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Enter new password", inputPassNew);
        inputPassNew.addActionListener(e -> inputPassConfirm.grabFocus());

        labelPassConfirm.setText("Confirm password:");
        inputPassConfirm.setFont(Theme.REGULAR.deriveFont(18f));
        new JTextFieldPrompt("Re-enter new password", inputPassConfirm);
        inputPassConfirm.addActionListener(e -> submitPassButton.doClick());

        JButtonRounded.beautify(submitPassButton, "Change Password");
        submitPassButton.addActionListener(e -> {
            String oldPassword = new String(inputPassOld.getPassword());
            String newPassword = new String(inputPassNew.getPassword());
            String confirmPassword = new String(inputPassConfirm.getPassword());

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(MainWindow.this, "Password field is empty!");
                return;
            }

            else if (oldPassword.length() < 8 || newPassword.length() < 8 || confirmPassword.length() < 8) {
                JOptionPane.showMessageDialog(MainWindow.this, "Password must contain more than 8 characters!");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to change your password?", "Change Password", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (!newPassword.equals(confirmPassword)) {
                    Utilities.sendError(MainWindow.this, "Password and confirmation do not match! Try again.");
                    return;
                }

                else if (oldPassword.equals(newPassword)) {
                    Utilities.sendError(MainWindow.this, "Your old password cannot be the same as your new one! Try again.");
                    return;
                }

                if (DatabaseConnection.setPassword(MainWindow.this, oldPassword, newPassword)) {
                    inputPassOld.setText(null);
                    inputPassNew.setText(null);
                    inputPassConfirm.setText(null);
                }
            }
        });

        JButtonRounded.beautify(logoutButton, "Log out");
        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                askLogout();
            }
        });
    }

    private void setupAdmin(){
        tabAdmin.setBackground(Theme.COLOR_BACKGROUND);
    }

    private void setupHead(){
        tabHead.setBackground(Theme.COLOR_BACKGROUND);
    }

    private void setupHome(){
        tabHome.setBackground(Theme.COLOR_BACKGROUND);
        welcomeTitle.setFont(Theme.BOLD.deriveFont(22f));
        welcomeTitle.setText("Welcome, Gracco Aloba");

        Utilities.populateTable(homeTable, Map.of("bruh1", "bruh1", "bru2", "bruh2", "bruh3", "bruh3","bruh4", "bruh4"), new String[]{"ska", "doosh"});
    }

    private void createUIComponents() {
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        settingsImage = new JPanelImage(Theme.SETTINGS_DESIGN.getImage());
        submitPassButton = new JButtonRounded();
        logoutButton = new JButtonRounded();
    }
}
