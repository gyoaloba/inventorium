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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseConnection {

    @Getter
    public enum Department {
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

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy hh:mm a");

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
        try {
            connection.close();
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
        }
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

    public static boolean setPassword(JFrame frame, String oldPassword, String newPassword) {
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
            boolean nullDepartment = department == null;

            if (nullDepartment) {
                statement = connection.prepareStatement("""
                        SELECT inventory.dept_id, items.item_name, inventory.amount
                        FROM inventory
                        INNER JOIN items ON inventory.item_id = items.item_id
                        ORDER BY inventory.dept_id, items.item_name;""");
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
                if (nullDepartment) {
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
                    SELECT requests.request_id, requests.created_at, CONCAT(users.first_name, ' ', users.last_name) AS full_name
                    FROM requests INNER JOIN users ON requests.user_uuid = users.user_uuid
                    WHERE requests.dept_id = ? ORDER BY request_id DESC;""");

            statement.setString(1, UserManager.getDepartment().toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String request_id = rs.getString("request_id");
                String date = DATE_FORMAT.format(rs.getTimestamp("created_at"));
                String name = rs.getString("full_name");
                rows.add(new String[]{request_id, date, name});
            }

            return rows.toArray(new String[0][]);
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }

    public static List<String> getItems() {
        try {
            List<String> items = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement("SELECT item_name FROM items ORDER BY item_name");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                items.add(resultSet.getString("item_name"));
            }

            return items;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }

    public static int getItemCount(String item) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT amount
                    FROM inventory INNER JOIN items ON inventory.item_id = items.item_id
                    WHERE dept_id = ? AND items.item_name = ?
                    """);
            statement.setString(1, UserManager.getDepartment().toString());
            statement.setString(2, item.toUpperCase());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? resultSet.getInt("amount") : 0;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return 0;
        }
    }

    public static void setItemCount(String item, int amount) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                            UPDATE inventory
                            INNER JOIN items ON inventory.item_id = items.item_id
                            SET inventory.amount = ?
                            WHERE items.item_name = ? AND inventory.dept_id = ?;
                    """);
            statement.setString(1, String.valueOf(amount));
            statement.setString(2, item.toString().toUpperCase());
            statement.setString(3, UserManager.getDepartment().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
        }
    }

    public static void setNewItemCount(String item, int amount) {
        try {
            PreparedStatement stmtItems = connection.prepareStatement(
                    "INSERT INTO items (item_name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            stmtItems.setString(1, item.toString().toUpperCase());
            stmtItems.executeUpdate();

            ResultSet rs = stmtItems.getGeneratedKeys();
            int itemId;
            if (rs.next()) itemId = rs.getInt(1);
            else throw new SQLException("Key was not returned");

            PreparedStatement stmtInventory = connection.prepareStatement("INSERT INTO inventory(item_id, dept_id, amount) VALUES (?, ?, ?)");
            stmtInventory.setString(1, String.valueOf(itemId));
            stmtInventory.setString(2, UserManager.getDepartment().toString());
            stmtInventory.setInt(3, amount);
            stmtInventory.executeUpdate();
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
        }
    }

    public static String[][] getUsers() {
        try {
            List<String[]> rows = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement("""
                    SELECT email, user_level, CONCAT(users.first_name, ' ', users.last_name) AS full_name, dept_id 
                    FROM `users` ORDER BY user_level DESC, full_name""");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                String userLevel = rs.getString("user_level");
                String name = rs.getString("full_name");
                String department = rs.getString("dept_id");

                rows.add(new String[]{email, userLevel, name, department == null ? "NONE" : department});
            }

            return rows.toArray(new String[0][]);
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }

    public static void createAccount(String firstName, String lastName, String email, String password, UserManager.UserLevel userLevel, Department department) {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    INSERT INTO users (user_uuid, first_name, last_name, email, password, user_level, dept_id) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)""");
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, email);
            statement.setString(5, Security.encrypt(password));
            statement.setString(6, userLevel.toString());
            if (department == null) statement.setNull(7, java.sql.Types.VARCHAR);
            else statement.setString(7, department.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
        }
    }

    public static String[] getRequest(int request_id) {
        try {
            String[] data = new String[3];

            PreparedStatement statement = connection.prepareStatement("""
                    SELECT requests.created_at, CONCAT(users.first_name, ' ', users.last_name) AS full_name, requests.request
                    FROM requests INNER JOIN users ON requests.user_uuid = users.user_uuid
                    WHERE requests.request_id = ?;""");

            statement.setString(1, String.valueOf(request_id));

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                data[0] = DATE_FORMAT.format(rs.getTimestamp("created_at"));
                data[1] = rs.getString("full_name");
                data[2] = rs.getString("request");
            }

            return data;
        } catch (SQLException e) {
            Utilities.sendFatalError(e);
            return null;
        }
    }
}
