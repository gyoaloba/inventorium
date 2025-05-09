package dev.gracco.inventorium.connection;

import dev.gracco.inventorium.utils.Security;
import dev.gracco.inventorium.utils.Utilities;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.JFrame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseManager {
    public enum UserLevel {
        USER, HEAD, ADMIN
    }

    public enum Department {
        CBE, CCS, CEA, COA, CWTS, ENG, GRAD, MAPD
    }

    @Getter private static UserLevel level;
    @Getter private static UUID userUUID;
    @Getter private static String lastName;
    @Getter private static String firstName;
    @Getter @Nullable private static Department department;

    public static boolean login(JFrame frame, String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Utilities.sendError(frame, "Please enter your email and password!");
            return false;
        } else if (!email.matches(Security.EMAIL_REGEX)) {
            Utilities.sendError(frame, "Enter a valid email!");
            return false;
        } else if (password.length() < 8) {
            Utilities.sendError(frame, "Password must contain more than 8 characters!");
            return false;
        }

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
            if (resultDepartment != null) department = Department.valueOf(resultDepartment);
        } catch (SQLException e){
            Utilities.sendFatalError(e);
        }

        return true;
    }
}
