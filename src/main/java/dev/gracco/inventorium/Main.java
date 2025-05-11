package dev.gracco.inventorium;

import dev.gracco.inventorium.connection.DatabaseConnection;
import dev.gracco.inventorium.frontend.pages.LoginWindow;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initialize();
        new LoginWindow();
    }
}