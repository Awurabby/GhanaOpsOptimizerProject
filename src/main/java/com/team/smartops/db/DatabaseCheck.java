package com.team.smartops.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseCheck {

    public static void main(String[] args) {

        try (Connection conn = DatabaseConnection.connect()) {

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                "SELECT name FROM sqlite_master WHERE type='table';"
            );

            System.out.println("Tables in database:");

            while (rs.next()) {
                System.out.println("- " + rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
