package com.team.smartops.db;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class SchemaSetup {

    public static void setup() {
        try (Connection conn = DatabaseConnection.connect()) {
            setup(conn);
            System.out.println("Database tables created/verified successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setup(Connection conn) throws Exception {
        String sql = Files.readString(
                Paths.get("src/main/resources/schema.sql")
        );

        Statement statement = conn.createStatement();
        String[] queries = sql.split(";");

        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                statement.execute(query);
            }
        }
    }

    public static void main(String[] args) {
        setup();
    }
}