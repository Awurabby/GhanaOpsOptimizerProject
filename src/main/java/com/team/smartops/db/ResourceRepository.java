package com.team.smartops.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ResourceRepository {


    public List<String> findAll(Connection conn) throws SQLException {

        List<String> resources = new ArrayList<>();

        String sql = "SELECT * FROM resources";


        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {

            String resource =
                    rs.getInt("resourceId") +
                    " - " +
                    rs.getString("type") +
                    " | Status: " +
                    rs.getString("availabilityStatus");

            resources.add(resource);

        }


        return resources;
    }



    public void insert(Connection conn,
                       int resourceId,
                       String type,
                       int homeLocation,
                       int capacity,
                       String availabilityStatus)
            throws SQLException {


        String sql =
                """
                INSERT INTO resources
                (resourceId,type,homeLocation,capacity,availabilityStatus)
                VALUES (?,?,?,?,?)
                """;


        PreparedStatement stmt =
                conn.prepareStatement(sql);


        stmt.setInt(1, resourceId);
        stmt.setString(2, type);
        stmt.setInt(3, homeLocation);
        stmt.setInt(4, capacity);
        stmt.setString(5, availabilityStatus);


        stmt.executeUpdate();

    }

}