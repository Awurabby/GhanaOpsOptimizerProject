package com.team.smartops.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RoadRepository {


    public List<String> findAll(Connection conn) throws SQLException {

        List<String> roads = new ArrayList<>();

        String sql = "SELECT * FROM roads";


        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {

            String road =
                    rs.getInt("fromLocationId") +
                    " -> " +
                    rs.getInt("toLocationId") +
                    " | Distance: " +
                    rs.getDouble("distance") +
                    "km";

            roads.add(road);

        }


        return roads;
    }



    public void insert(Connection conn,
                       int fromLocationId,
                       int toLocationId,
                       double distance,
                       double travelTime,
                       double roadConditionWeight)
            throws SQLException {


        String sql =
                """
                INSERT INTO roads
                (fromLocationId,toLocationId,distance,travelTime,roadConditionWeight)
                VALUES (?,?,?,?,?)
                """;


        PreparedStatement stmt =
                conn.prepareStatement(sql);


        stmt.setInt(1, fromLocationId);
        stmt.setInt(2, toLocationId);
        stmt.setDouble(3, distance);
        stmt.setDouble(4, travelTime);
        stmt.setDouble(5, roadConditionWeight);


        stmt.executeUpdate();

    }

}