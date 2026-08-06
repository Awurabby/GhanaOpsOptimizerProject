package com.team.smartops.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class LocationRepository {


    public List<String> findAll(Connection conn) throws SQLException {

        List<String> locations = new ArrayList<>();

        String sql = "SELECT * FROM locations";


        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {

            String location =
                    rs.getInt("locationId") + " - " +
                    rs.getString("name") + " - " +
                    rs.getString("area");

            locations.add(location);

        }


        return locations;
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