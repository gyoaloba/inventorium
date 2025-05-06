package dev.gracco.inventorium.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConnectionManager {
    public SQLConnectionManager(String url, String user, String password) throws SQLException {
       Connection conn = DriverManager.getConnection(url, user, password);
       Statement stmt = conn.createStatement();

        String query = "SELECT * FROM users";
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String username = rs.getString("username");
            System.out.println("User: " + username);
        }
    }
}
