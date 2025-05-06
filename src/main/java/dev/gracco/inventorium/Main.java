package dev.gracco.inventorium;

import dev.gracco.inventorium.frontend.forms.LoginWindow;

import javax.swing.JFrame;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {
        JFrame loginWindow = new LoginWindow();
    }
}