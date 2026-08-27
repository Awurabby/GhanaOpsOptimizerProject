package com.team.smartops.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseLoader {

    public static void main(String[] args) {
        SchemaSetup.setup();
        clearTables();
        loadLocations();
        loadRoads();
        loadResources();
        loadServiceRequests();
    }

    private static void clearTables() {
        try (Connection conn = DatabaseConnection.connect()) {
            var stmt = conn.createStatement();
            stmt.execute("DELETE FROM service_requests");
            stmt.execute("DELETE FROM resources");
            stmt.execute("DELETE FROM roads");
            stmt.execute("DELETE FROM locations");
            System.out.println("Existing data cleared.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadLocations() {
        String file = "data/csv/locations.csv";
        String sql = """
                INSERT INTO locations
                (locationId, name, area, type, latitude, longitude)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
            Connection conn = DatabaseConnection.connect();
            BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] data = line.split(",");
                pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                pstmt.setString(2, data[1].trim());
                pstmt.setString(3, data[2].trim());
                pstmt.setString(4, data[3].trim());
                pstmt.setDouble(5, Double.parseDouble(data[4].trim()));
                pstmt.setDouble(6, Double.parseDouble(data[5].trim()));
                pstmt.executeUpdate();
            }
            System.out.println("Locations loaded successfully!");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void loadRoads() {
        String file = "data/csv/roads.csv";

        String sql = """
                INSERT INTO roads
                (fromLocationId, toLocationId, distance, travelTime, roadConditionWeight)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
            Connection conn = DatabaseConnection.connect();
            BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] data = line.split(",");
                pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                pstmt.setInt(2, Integer.parseInt(data[1].trim()));
                pstmt.setDouble(3, Double.parseDouble(data[2].trim()));
                pstmt.setDouble(4, Double.parseDouble(data[3].trim()));
                pstmt.setDouble(5, Double.parseDouble(data[4].trim()));
                pstmt.executeUpdate();
            }
            System.out.println("Roads loaded successfully (" + file + ")!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadResources() {
        String file = "data/csv/resources.csv";
        String sql = """
                INSERT INTO resources
                (resourceId, type, homeLocation, capacity, availabilityStatus)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
            Connection conn = DatabaseConnection.connect();
            BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] data = line.split(",");
                pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                pstmt.setString(2, data[1].trim());
                pstmt.setInt(3, Integer.parseInt(data[2].trim()));
                pstmt.setInt(4, Integer.parseInt(data[3].trim()));
                pstmt.setString(5, data[4].trim());
                pstmt.executeUpdate();
            }
            System.out.println("Resources loaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadServiceRequests() {
        String file = "data/csv/service_requests.csv";
        String sql = """
                INSERT INTO service_requests
                (requestId, source, destination, category, urgency, timeSubmitted, deadline, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
            Connection conn = DatabaseConnection.connect();
            BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] data = line.split(",");
                pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                pstmt.setInt(2, Integer.parseInt(data[1].trim()));
                pstmt.setInt(3, Integer.parseInt(data[2].trim()));
                pstmt.setString(4, data[3].trim());
                pstmt.setString(5, data[4].trim());
                pstmt.setString(6, data[5].trim());
                pstmt.setString(7, data[6].trim());
                pstmt.setString(8, data[7].trim());
                pstmt.executeUpdate();
            }
            System.out.println("Service requests loaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}