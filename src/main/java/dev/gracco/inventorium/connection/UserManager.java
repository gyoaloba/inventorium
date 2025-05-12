package dev.gracco.inventorium.connection;

import dev.gracco.inventorium.utils.Utilities;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.JFrame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserManager {
    public enum UserLevel {
        USER, HEAD, ADMIN
    }

    @Getter private static UserLevel level = UserLevel.USER;
    @Getter private static UUID userUUID  = UUID.fromString("539d5d36-7d48-48ff-8b5f-5bbf94946ce9");
    @Getter private static String lastName = "Amba";
    @Getter private static String firstName = "James";
    @Getter @Nullable private static DatabaseConnection.Department department = DatabaseConnection.Department.CCS;

    public static boolean login(JFrame frame, String email, String password) {
        ResultSet resultSet = DatabaseConnection.getLoginResultSet(email, password);
        if (resultSet == null) {
            Utilities.sendError(frame, "Invalid email or password");
            return false;
        }

        try {
            level = UserLevel.valueOf(resultSet.getString("user_level"));
            userUUID = UUID.fromString(resultSet.getString("user_uuid"));
            lastName = resultSet.getString("last_name");
            firstName = resultSet.getString("first_name");

            String resultDepartment = resultSet.getString("dept_id");
            if (resultDepartment != null) department = DatabaseConnection.Department.valueOf(resultDepartment);
        } catch (SQLException e){
            Utilities.sendFatalError(e);
        }

        return true;
    }

    public static void logout(){
        level = null;
        userUUID = null;
        lastName = null;
        firstName = null;
        department = null;
    }
}
