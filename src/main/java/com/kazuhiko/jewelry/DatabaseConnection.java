package com.kazuhiko.jewelry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:db/jewelry.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initialize() {
        String createJewelryTable = """
            CREATE TABLE IF NOT EXISTS jewelry (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                type TEXT NOT NULL,
                material TEXT NOT NULL,
                price REAL NOT NULL,
                quantity INTEGER NOT NULL DEFAULT 0
            )
            """;

        String createSalesTable = """
            CREATE TABLE IF NOT EXISTS sales (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                jewelry_id INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                total_price REAL NOT NULL,
                customer_name TEXT,
                sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (jewelry_id) REFERENCES jewelry(id)
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createJewelryTable);
            stmt.execute(createSalesTable);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
