package dev.gracco.inventorium;

import dev.gracco.inventorium.connection.DatabaseConnection;
import dev.gracco.inventorium.frontend.pages.LoginWindow;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initialize();
        JFrame loginWindow = new LoginWindow();
        //JFrame mainWindow = new MainWindow();
    }
}