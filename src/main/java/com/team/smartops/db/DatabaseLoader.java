package com.team.smartops.db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class DatabaseLoader {


    public static void main(String[] args) {

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

            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                pstmt.setInt(1, Integer.parseInt(data[0]));
                pstmt.setString(2, data[1]);
                pstmt.setString(3, data[2]);
                pstmt.setString(4, data[3]);
                pstmt.setDouble(5, Double.parseDouble(data[4]));
                pstmt.setDouble(6, Double.parseDouble(data[5]));

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

        String line;

        // skip header
        br.readLine();

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            pstmt.setInt(1, Integer.parseInt(data[0]));
            pstmt.setInt(2, Integer.parseInt(data[1]));
            pstmt.setDouble(3, Double.parseDouble(data[2]));
            pstmt.setDouble(4, Double.parseDouble(data[3]));
            pstmt.setDouble(5, Double.parseDouble(data[4]));

            pstmt.executeUpdate();
        }

        System.out.println("Roads loaded successfully!");

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

        String line;

        // skip header
        br.readLine();

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            pstmt.setInt(1, Integer.parseInt(data[0]));
            pstmt.setString(2, data[1]);
            pstmt.setInt(3, Integer.parseInt(data[2]));
            pstmt.setInt(4, Integer.parseInt(data[3]));
            pstmt.setString(5, data[4]);

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

        String line;

        // skip header
        br.readLine();

        while ((line = br.readLine()) != null) {

            String[] data = line.split(",");

            pstmt.setInt(1, Integer.parseInt(data[0]));
            pstmt.setInt(2, Integer.parseInt(data[1]));
            pstmt.setInt(3, Integer.parseInt(data[2]));
            pstmt.setString(4, data[3]);
            pstmt.setString(5, data[4]);
            pstmt.setString(6, data[5]);
            pstmt.setString(7, data[6]);
            pstmt.setString(8, data[7]);

            pstmt.executeUpdate();
        }

        System.out.println("Service requests loaded successfully!");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}