package dev.gracco.inventorium;

import dev.gracco.inventorium.connection.DatabaseConnection;
import dev.gracco.inventorium.frontend.Theme;
import dev.gracco.inventorium.frontend.pages.MainWindow;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection.initialize();
        Theme.initialize();
        new MainWindow();
    }
}