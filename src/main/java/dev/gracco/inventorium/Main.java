package dev.gracco.inventorium;

import dev.gracco.inventorium.connection.SQLConnectionManager;
import dev.gracco.inventorium.frontend.pages.LoginWindow;

import javax.swing.JFrame;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorium";
    private static final String DB_USER = "inventorium";
    private static final String DB_PASSWORD = "LebronI$MyG0a7";
    public static SQLConnectionManager connection;

    public static void main(String[] args) {
        //try {
            //connection = new SQLConnectionManager(DB_URL, DB_USER, DB_PASSWORD);
        //} catch (SQLException e) {
            //throw new RuntimeException(e);
        //}
        JFrame loginWindow = new LoginWindow();
    }
}