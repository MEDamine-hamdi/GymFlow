package com.gymflow.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3307/gymflow?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = ""; // change if needed

    public static Connection getConnection() {
        try {
            // 🔴 VERY IMPORTANT
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}