package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_URL =
            "jdbc:sqlite:library.db";

    public static Connection connect() {

        try {

            Class.forName("org.sqlite.JDBC");

            Connection conn =
                    DriverManager.getConnection(DB_URL);

            Statement stmt =
                    conn.createStatement();

            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS books (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        title TEXT NOT NULL,
                        author TEXT NOT NULL,
                        status TEXT NOT NULL
                    )
                    """);

            return conn;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
}