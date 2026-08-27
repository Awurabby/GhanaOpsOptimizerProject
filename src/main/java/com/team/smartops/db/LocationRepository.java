package com.team.smartops.db;

import com.team.smartops.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LocationRepository {


    public List<Location> findAll(Connection conn) throws SQLException {

        List<Location> locations = new ArrayList<>();

        String sql = "SELECT * FROM locations";


        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {

            locations.add(new Location(
                    rs.getInt("locationId"),
                    rs.getString("name"),
                    rs.getString("area"),
                    rs.getString("type"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude")));

        }


        return locations;
    }


    public Location findById(Connection conn, int locationId) throws SQLException {

        String sql = "SELECT * FROM locations WHERE locationId = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, locationId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Location(
                    rs.getInt("locationId"),
                    rs.getString("name"),
                    rs.getString("area"),
                    rs.getString("type"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"));
        }

        return null;
    }



    public void insert(Connection conn,
                       int locationId,
                       String name,
                       String area,
                       String type,
                       double latitude,
                       double longitude)
            throws SQLException {


        String sql =
                """
                INSERT INTO locations
                (locationId,name,area,type,latitude,longitude)
                VALUES (?,?,?,?,?,?)
                """;


        PreparedStatement stmt =
                conn.prepareStatement(sql);


        stmt.setInt(1, locationId);
        stmt.setString(2, name);
        stmt.setString(3, area);
        stmt.setString(4, type);
        stmt.setDouble(5, latitude);
        stmt.setDouble(6, longitude);


        stmt.executeUpdate();

    }

}