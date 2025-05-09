package dev.gracco.inventorium.connection;

import dev.gracco.inventorium.utils.Security;
import dev.gracco.inventorium.utils.Utilities;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private enum Table {
        DEPARTMENTS, INVENTORY, ITEMS, LOGS, USERS
    }

    private static Connection connection;
    //insert env here
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorium";
    private static final String DB_USER = "inventorium";
    private static final String DB_PASSWORD = "LebronI$MyG0a7";

    public static void initialize() {
        try { connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); }
        catch (SQLException e) {
            Utilities.sendFatalError(e);
        }
    }

    @Nullable
    public static ResultSet getLoginResultSet(String email, String password) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return null;

            String hash = rs.getString("password");
            return Security.decrypt(password, hash) ? rs : null;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }
}
