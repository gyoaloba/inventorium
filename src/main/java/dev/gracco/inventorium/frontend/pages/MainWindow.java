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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

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
    private JTable requestHeadTable;
    private JButton reloadRequests;
    private JButton submitRequest;
    private JLabel labelRequest;
    private JScrollPane requestPane;
    private JTextArea requestTextArea;
    private JLabel labelHeadRequest;
    private JScrollPane requestHeadPane;
    private JPanel requestPanel;
    private JLabel welcomeDept;
    private JTextField searchInventoryHome;
    private JComboBox itemCombo;
    private JButton updateButton;
    private JTextField inputItemName;
    private JButton createButton;
    private JLabel labelUpdate;
    private JLabel labelComboItem;
    private JLabel labelCreate;
    private JLabel labelCreateName;
    private JSpinner updateSpinner;
    private JSpinner createSpinner;
    private JPanel headItemsPanel;
    private JPanel adminUsers;
    private JPanel adminInventory;
    private JPanel adminCreate;
    private JTable adminTableUsers;
    private JTable adminTableInventory;
    private JTextField inputFirstName;
    private JTextField inputLastName;
    private JTextField inputEmail;
    private JPasswordField inputPassword;
    private JComboBox userLevelCombo;
    private JComboBox departmentCombo;
    private JButton createAccount;
    private JLabel labelCreateAccount;
    private JLabel labelFirstName;
    private JLabel labelLastName;
    private JLabel labelEmail;
    private JLabel labelPassword;
    private JLabel labelUserLevel;
    private JLabel labelDepartment;
    private JButton reloadUsers;
    private JButton reloadInventory;
    private JLabel labelAdminUsers;
    private JLabel labelAdminInventory;

    public MainWindow() {
        setContentPane(mainPanel);
        setFontRecursively(this, Theme.REGULAR.deriveFont(20f));
        setupSettings();
        switch (UserManager.getLevel()) {
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
            public void windowClosing(WindowEvent e) {
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

    private void setupSettings() {
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
            } else if (oldPassword.length() < 8 || newPassword.length() < 8 || confirmPassword.length() < 8) {
                JOptionPane.showMessageDialog(MainWindow.this, "Password must contain more than 8 characters!");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to change your password?", "Change Password", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                if (!newPassword.equals(confirmPassword)) {
                    Utilities.sendError(MainWindow.this, "Password and confirmation do not match! Try again.");
                    return;
                } else if (oldPassword.equals(newPassword)) {
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

    private void setupAdmin() {
        tabAdmin.setBackground(Theme.COLOR_BACKGROUND);

        //Users
        labelAdminUsers.setText("Users Table");
        labelAdminUsers.setFont(Theme.BOLD.deriveFont(20f));

        reloadUsers.setFocusPainted(false);
        JButtonRounded.beautify(reloadUsers, "Reload Users Table");
        reloadUsers.addActionListener(e -> Utilities.populateTable(adminTableUsers, DatabaseConnection.getUsers(), new String[]{"Email", " Level", "Name", "Dept."}));

        reloadUsers.doClick();
        adminTableUsers.setRowHeight(adminTableUsers.getFontMetrics(adminTableUsers.getFont()).getHeight() + 4);
        adminTableUsers.getTableHeader().setBackground(Theme.COLOR_PRIMARY);  // Set header background color
        adminTableUsers.getTableHeader().setFont(Theme.BOLD.deriveFont(18f));
        adminTableUsers.setFont(Theme.REGULAR.deriveFont(14f));

        //Inventory
        labelAdminInventory.setText("Inventory Table");
        labelAdminInventory.setFont(Theme.BOLD.deriveFont(20f));

        reloadInventory.setFocusPainted(false);
        JButtonRounded.beautify(reloadInventory, "Reload Inventory Table");
        reloadInventory.addActionListener(e -> Utilities.populateTable(adminTableInventory, DatabaseConnection.getItems(null), new String[]{"Dept.", " Item Name", "Amount"}));

        reloadInventory.doClick();
        adminTableInventory.setRowHeight(adminTableInventory.getFontMetrics(adminTableInventory.getFont()).getHeight() + 4);
        adminTableInventory.getTableHeader().setBackground(Theme.COLOR_PRIMARY);  // Set header background color
        adminTableInventory.getTableHeader().setFont(Theme.BOLD.deriveFont(18f));
        adminTableInventory.setFont(Theme.REGULAR.deriveFont(14f));

        //Create account
        labelCreateAccount.setText("Create New User");
        labelCreateAccount.setFont(Theme.BOLD.deriveFont(20f));

        labelFirstName.setText("First Name:");
        new JTextFieldPrompt("First Name", inputFirstName);

        labelLastName.setText("Last Name:");
        new JTextFieldPrompt("Last Name", inputLastName);

        labelEmail.setText("Email:");
        new JTextFieldPrompt("Email", inputEmail);

        labelPassword.setText("Password:");
        new JTextFieldPrompt("Password", inputPassword);

        labelUserLevel.setText("User Level:");
        Arrays.asList(UserManager.UserLevel.values()).forEach(userLevelCombo::addItem);
        userLevelCombo.setBackground(Theme.COLOR_BACKGROUND);

        labelDepartment.setText("Department");
        departmentCombo.addItem("NONE");
        Arrays.asList(DatabaseConnection.Department.values()).forEach(departmentCombo::addItem);
        departmentCombo.setBackground(Theme.COLOR_BACKGROUND);

        JButtonRounded.beautify(createAccount, "Create Account");
        createAccount.addActionListener(e -> {
            String firstName = inputFirstName.getText();
            String lastName = inputLastName.getText();
            String email = inputEmail.getText();
            String password = new String(inputPassword.getPassword());
            UserManager.UserLevel userLevel = (UserManager.UserLevel) userLevelCombo.getSelectedItem();
            DatabaseConnection.Department department;
            try {
                department = DatabaseConnection.Department.valueOf(departmentCombo.getSelectedItem().toString());
            } catch (ClassCastException exc) {
                department = null;
            }

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Utilities.sendError(MainWindow.this, "You cannot leave any field as blank!");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(MainWindow.this,
                    "Create this account?\n\nFirst Name: " + firstName +
                            "\nLast Name: " + lastName +
                            "\nEmail: " + email +
                            "\nPassword: " + password +
                            "\nUser Level: " + userLevel.toString() +
                            "\nDepartment: " + (department == null ? "NONE" : department.toString())
                    , "Create Account", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                DatabaseConnection.createAccount(firstName, lastName, email, password, userLevel, department);
                Utilities.sendSuccess(MainWindow.this, "Successfully created user!");
                inputFirstName.setText(null);
                inputLastName.setText(null);
                inputEmail.setText(null);
                inputPassword.setText(null);
            }
        });
    }

    private void setupHead() {
        tabHead.setBackground(Theme.COLOR_BACKGROUND);

        //Requests
        labelHeadRequest.setText("Requests");
        labelHeadRequest.setFont(Theme.BOLD.deriveFont(20f));

        reloadRequests.setFocusPainted(false);
        JButtonRounded.beautify(reloadRequests, "Reload Requests");
        reloadRequests.addActionListener(e -> Utilities.populateTable(requestHeadTable, DatabaseConnection.getRequests(), new String[]{"ID", "Created at", "Name"}));

        reloadRequests.doClick();
        requestHeadTable.setRowHeight(requestHeadTable.getFontMetrics(requestHeadTable.getFont()).getHeight() + 4);
        requestHeadTable.getTableHeader().setBackground(Theme.COLOR_PRIMARY);  // Set header background color
        requestHeadTable.getTableHeader().setFont(Theme.BOLD.deriveFont(18f));
        requestHeadTable.setFont(Theme.REGULAR.deriveFont(14f));
        requestHeadTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int row = requestHeadTable.rowAtPoint(e.getPoint());
                    if (row < 0) return;
                    int requestId = Integer.parseInt((String) requestHeadTable.getValueAt(row, 0));

                    String[] data = DatabaseConnection.getRequest(requestId);
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Created in: " + data[0] +
                                    "\nMade by: " + data[1] +
                            "\n\nRequest: \n" + Utilities.splitIntoLines(data[2], 20),
                            "Request No. " + requestId, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //Update
        labelUpdate.setText("Update Item");
        labelUpdate.setFont(Theme.BOLD.deriveFont(20f));

        labelComboItem.setText("Choose item");
        labelComboItem.setFont(Theme.REGULAR.deriveFont(18f));

        DatabaseConnection.getItems().forEach(itemCombo::addItem);
        itemCombo.setBackground(Theme.COLOR_BACKGROUND);
        itemCombo.addActionListener(e -> updateSpinner.setValue(DatabaseConnection.getItemCount((String) itemCombo.getSelectedItem())));
        updateSpinner.setValue(DatabaseConnection.getItemCount((String) itemCombo.getSelectedItem()));

        updateButton.setFocusPainted(false);
        updateButton.setText("Update");
        updateButton.addActionListener(e -> {
            String id = (String) itemCombo.getSelectedItem();
            int amount = (int) updateSpinner.getValue();

            DatabaseConnection.setItemCount(id, amount);
            Utilities.sendSuccess(MainWindow.this, "Successfully updated amount to " + amount);
        });

        labelCreate.setText("Create New Item");
        labelCreate.setFont(Theme.BOLD.deriveFont(20f));

        labelCreateName.setText("Input non-duplicate name");
        labelCreateName.setFont(Theme.REGULAR.deriveFont(18f));

        createButton.setFocusPainted(false);
        createButton.setText("Update");
        createButton.addActionListener(e -> {
            String id = inputItemName.getText().trim().toUpperCase();
            if (id.isEmpty() || id == null) {
                Utilities.sendError(MainWindow.this, "Item name cannot be empty!");
                return;
            } else if (DatabaseConnection.getItems().contains(id)) {
                Utilities.sendError(MainWindow.this, "Item already exists!");
                return;
            }

            int amount = (int) createSpinner.getValue();

            int choice = JOptionPane.showConfirmDialog(MainWindow.this, "Are you sure you want to add " + id + " to the list of items?", "Create New Item", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                DatabaseConnection.setNewItemCount(id, amount);
                itemCombo.addItem(id);
                inputItemName.setText(null);
                createSpinner.setValue(1);
                Utilities.sendSuccess(MainWindow.this, "Successfully updated amount to " + amount);
            }
        });

    }

    private void setupHome() {
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
        homeTable.getTableHeader().setBackground(Theme.COLOR_PRIMARY);
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
        headItemsPanel = new JPanelImage(Theme.HOME_DESIGN.getImage());
        settingsImage = new JPanelImage(Theme.SETTINGS_DESIGN.getImage());
        submitRequest = new JButtonRounded();
        submitPassButton = new JButtonRounded();
        logoutButton = new JButtonRounded();
        reloadRequests = new JButtonRounded();
        updateSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        createSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 100, 1));
        createAccount = new JButtonRounded();
        reloadUsers = new JButtonRounded();
        reloadInventory = new JButtonRounded();
    }
}
