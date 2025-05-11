package dev.gracco.inventorium.connection;

import dev.gracco.inventorium.utils.Security;
import dev.gracco.inventorium.utils.Utilities;
import org.jetbrains.annotations.Nullable;

import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private enum Table {
        //Remove after done
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

    public static void close() {
        try { connection.close(); }
        catch (SQLException e) { Utilities.sendFatalError(e); }
    }

    @Nullable
    public static ResultSet getLoginResultSet(String email, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) return null;

            String hash = rs.getString("password");
            return Security.decrypt(password, hash) ? rs : null;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }

    public static boolean setPassword(JFrame frame, String oldPassword, String newPassword){
        try {
            PreparedStatement selectStatement = connection.prepareStatement("SELECT password FROM users WHERE user_uuid = ?");
            selectStatement.setString(1, DatabaseManager.getUserUUID().toString());
            ResultSet rs = selectStatement.executeQuery();

            if (!rs.next()) {
                Utilities.sendFatalError(new SQLException("User not found"));
                return false;
            }

            String hash = rs.getString("password");
            if (!Security.decrypt(oldPassword, hash)) {
                Utilities.sendError(frame, "Wrong password!");
                return false;
            }

            String newHash = Security.encrypt(newPassword);
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE user_uuid = ?");
            updateStatement.setString(1, newHash);
            updateStatement.setString(2, DatabaseManager.getUserUUID().toString());
            updateStatement.executeUpdate();

            Utilities.sendSuccess(frame, "Successfully changed password!");
            return true;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return false;
        }
    }

    //public static int countItems(){
    //    try {
    //        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
    //        stmt.setString(1, email);
    //        ResultSet rs = stmt.executeQuery();
//
    //        if (!rs.next()) return null;
//
    //        String hash = rs.getString("password");
    //        return Security.decrypt(password, hash) ? rs : null;
    //    } catch (SQLException e) {
    //        Utilities.sendFatalError(e);
    //        return 0;
    //    }
    //}
}
