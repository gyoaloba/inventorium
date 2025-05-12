package dev.gracco.inventorium.connection;

import dev.gracco.inventorium.utils.Security;
import dev.gracco.inventorium.utils.Utilities;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseConnection {

    @Getter public enum Department {
        CBE, CCS, CEA, COA, CWTS, ENG, GRAD, MAPD;

        @Setter
        private String fullName;

        public static void initialize(Map<Department, String> map) {
            for (Map.Entry<Department, String> entry : map.entrySet()) {
                Department department = entry.getKey();
                String fullName = entry.getValue();
                department.setFullName(fullName);
            }
        }
    }

    private static Connection connection;
    //insert env here
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventorium";
    private static final String DB_USER = "inventorium";
    private static final String DB_PASSWORD = "LebronI$MyG0a7";

    public static void initialize() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            ResultSet departmentSet = connection.prepareStatement("SELECT * FROM departments;").executeQuery();
            Map<Department, String> map = new HashMap<>();

            while (departmentSet.next()) {
                Department department = Department.valueOf(departmentSet.getString(1));
                String fullName = departmentSet.getString(2);
                map.put(department, fullName);
            }

            Department.initialize(map);
        } catch (SQLException e) {
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
            selectStatement.setString(1, UserManager.getUserUUID().toString());
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
            updateStatement.setString(2, UserManager.getUserUUID().toString());
            updateStatement.executeUpdate();

            Utilities.sendSuccess(frame, "Successfully changed password!");
            return true;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return false;
        }
    }

    public static String[][] getItems(Department department) {
        try {
            List<String[]> rows = new ArrayList<>();

            PreparedStatement statement;
            boolean hasDepartment = department == null;

            if (hasDepartment) {
                statement = connection.prepareStatement("""
                SELECT inventory.dept_id, items.item_name, inventory.amount
                FROM inventory
                INNER JOIN items ON inventory.item_id = items.item_id;""");
            } else {
                statement = connection.prepareStatement("""
                SELECT items.item_name, inventory.amount
                FROM inventory
                INNER JOIN items ON inventory.item_id = items.item_id
                WHERE dept_id = ?;""");
                statement.setString(1, department.toString());
            }

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                if (hasDepartment) {
                    String dept = rs.getString("dept_id");
                    String item = rs.getString("item_name");
                    int amount = rs.getInt("amount");
                    rows.add(new String[]{dept, item, String.valueOf(amount)});
                } else {
                    String item = rs.getString("item_name");
                    int amount = rs.getInt("amount");
                    rows.add(new String[]{item, String.valueOf(amount)});
                }
            }

            return rows.toArray(new String[0][]);
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }

    public static void createRequest(String request) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO requests (dept_id, user_uuid, request) VALUES (?, ?, ?);");
            statement.setString(1, UserManager.getDepartment().toString());
            statement.setString(2, UserManager.getUserUUID().toString());
            statement.setString(3, request);
            statement.executeUpdate();

        } catch (SQLException e) {
            Utilities.sendFatalError(e);
        }
    }

    public static String[][] getRequests() {
        try {
            List<String[]> rows = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement("""
            SELECT requests.request_id, CONCAT(users.first_name, ' ', users.last_name) AS full_name, requests.request
            FROM requests INNER JOIN users ON requests.user_uuid = users.user_uuid
            WHERE requests.dept_id = ?;""");

            statement.setString(1, UserManager.getDepartment().toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String dept = rs.getString("dept_id");
                String item = rs.getString("item_name");
                int amount = rs.getInt("amount");
            }

            return rows.toArray(new String[0][]);
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }

}
