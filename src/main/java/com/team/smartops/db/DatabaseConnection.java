package com.team.smartops.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * OWNER: Team B (Database).
 * Opens/closes the JDBC connection used by every repository class below.
 * TODO: point this at schema.sql / your chosen DB (SQLite/MySQL/PostgreSQL).
 */
public class DatabaseConnection {
  private static final String URL = "jdbc:sqlite:smartops.db";

public static Connection connect() throws SQLException {
    return DriverManager.getConnection(URL);
}
}
