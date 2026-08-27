package com.team.smartops.db;

import com.team.smartops.model.ServiceRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestRepository {

    public List<ServiceRequest> findAll(Connection conn) throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            requests.add(new ServiceRequest(
                    rs.getInt("requestId"),
                    rs.getInt("source"),
                    rs.getInt("destination"),
                    rs.getString("category"),
                    rs.getString("urgency"),
                    rs.getString("timeSubmitted"),
                    rs.getString("deadline"),
                    rs.getString("status")
            ));
        }

        return requests;
    }

    public void insert(Connection conn,
                       int requestId,
                       int source,
                       int destination,
                       String category,
                       String urgency,
                       String timeSubmitted,
                       String deadline,
                       String status)
            throws SQLException {

        String sql =
                """
                INSERT INTO service_requests
                (requestId,source,destination,category,urgency,timeSubmitted,deadline,status)
                VALUES (?,?,?,?,?,?,?,?)
                """;

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, requestId);
        stmt.setInt(2, source);
        stmt.setInt(3, destination);
        stmt.setString(4, category);
        stmt.setString(5, urgency);
        stmt.setString(6, timeSubmitted);
        stmt.setString(7, deadline);
        stmt.setString(8, status);

        stmt.executeUpdate();
    }
}