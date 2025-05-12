package dev.gracco.inventorium.frontend.pages;

import dev.gracco.inventorium.connection.DatabaseConnection;
import dev.gracco.inventorium.connection.UserManager;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    private JButton searchButtonHome;
    private JTable homeTable;
    private JLabel homeTableLabel;
    private JScrollPane homeScrollPane;
    private JTable logsHeadTable;
    private JTable requestHeadTable;
    private JButton reloadRequests;
    private JButton previousLogs;
    private JButton nextLogs;
    private JButton submitRequest;
    private JLabel labelRequest;
    private JScrollPane requestPane;
    private JTextArea requestTextArea;
    private JLabel labelHeadRequest;
    private JScrollPane requestHeadPane;
    private JLabel labelLogsHead;
    private JScrollPane logsHeadPane;
    private JPanel requestPanel;
    private JLabel welcomeDept;
    private JTextField searchInventoryHome;

    public MainWindow(){
        setContentPane(mainPanel);
        setFontRecursively(this, Theme.REGULAR.deriveFont(20f));
        setupSettings();
        //TEMP
        setupAdmin();
        setupHead();
        setupHome();
        //switch (DatabaseManager.getLevel()) {
        //    case ADMIN -> {
        //        setupAdmin();
        //        tabs.remove(1); // Head
        //        tabs.remove(0); // User
        //    }
        //    case HEAD -> {
        //        setupHead();
        //        setupHome();
        //        tabs.remove(2); // Admin
        //    }
        //    case USER -> {
        //        setupHome();
        //        tabs.remove(2); // Admin
        //        tabs.remove(1); // Head
        //    }
        //}

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
                logoutButton.doClick();
            }
        });
        setMinimumSize(new Dimension(1000, 700));
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
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
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to log out?", "Log out", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                UserManager.logout();
                new LoginWindow();
            }
        });
    }

    private void setupAdmin(){
        tabAdmin.setBackground(Theme.COLOR_BACKGROUND);
    }

    private void setupHead(){
        tabHead.setBackground(Theme.COLOR_BACKGROUND);

        //Requests

        //Logs
    }

    private void setupHome(){
        tabHome.setBackground(Theme.COLOR_BACKGROUND);
        welcomeTitle.setFont(Theme.BOLD.deriveFont(22f));
        welcomeTitle.setText("Welcome, " + UserManager.getFirstName() + " " + UserManager.getLastName());
        welcomeDept.setText(UserManager.getDepartment().getFullName());

        // Inventory
        homeTableLabel.setFont(Theme.BOLD.deriveFont(22f));
        homeTableLabel.setText(UserManager.getDepartment().toString() + " Inventory");
        searchButtonHome.setFocusable(false);
        searchButtonHome.addActionListener(e -> {
            for (int row = 0; row < homeTable.getRowCount(); row++) {
                Object value = homeTable.getValueAt(row, 0);
                if (value != null && value.toString().toLowerCase().contains(searchInventoryHome.getText().toLowerCase())) {
                    homeTable.changeSelection(row, 0, false, false);
                    return; // stop after first match
                }
            }
        });
        searchInventoryHome.addActionListener(e -> searchButtonHome.doClick());

        Utilities.populateTable(homeTable, DatabaseConnection.getItems(DatabaseConnection.Department.CCS), new String[]{"Item Name", "Amount"});
        homeTable.setRowHeight(homeTable.getFontMetrics(homeTable.getFont()).getHeight() + 4);
        homeTable.getTableHeader().setBackground(Theme.COLOR_PRIMARY);  // Set header background color
        homeTable.getTableHeader().setFont(Theme.BOLD.deriveFont(18f));
        homeTable.setFont(Theme.REGULAR.deriveFont(14f));
        searchButtonHome.setText("Search");

        new JTextFieldPrompt("Search item", searchInventoryHome);

        // Request
        requestTextArea.setLineWrap(true);
        requestTextArea.setWrapStyleWord(true);
        requestTextArea.setFont(Theme.REGULAR.deriveFont(14f));
        submitRequest.addActionListener(e -> {
            if (requestTextArea.getText().isEmpty()) {
                Utilities.sendError(MainWindow.this, "Your request is empty!");
                return;
            }

            DatabaseConnection.createRequest(requestTextArea.getText());
            requestTextArea.setText("");
            Utilities.sendSuccess(MainWindow.this, "Successfully submitted request to your department head!");
        });

        JButtonRounded.beautify(submitRequest, "Send Request");
        labelRequest.setText("Send Request to Department Head");
    }

    private void createUIComponents() {
        inventoryDisplay = new JPanelImage(Theme.HOME_DESIGN.getImage());
        settingsImage = new JPanelImage(Theme.SETTINGS_DESIGN.getImage());
        submitRequest = new JButtonRounded();
        submitPassButton = new JButtonRounded();
        logoutButton = new JButtonRounded();
    }
}
