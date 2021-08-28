package pl.coderslab.entity;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DbUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = getPassword();



    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    }
    private static String getPassword() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter password to log into database");
        return sc.nextLine();
    }

}
