package ca.cfrayne.loggy.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Adjust DB name / user / password if you changed them
    private static final String URL =
            "jdbc:mysql://localhost:3306/loggydb?useSSL=false&serverTimezone=UTC";
    private static final String USER =
        System.getenv().getOrDefault("LOGGY_DB_USER", "root");

    private static final String PASSWORD =
        System.getenv().getOrDefault("LOGGY_DB_PASSWORD", "");

    static {
        try {
            // MySQL Connector/J 8+
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load MySQL JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
