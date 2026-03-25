package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/diary_system",
                    "root",
                    "" // XAMPP default MySQL password empty
            );
            return con;
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return null;
        }
    }
}