package com.team.smartops.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AlgorithmRunRepository {


    public List<String> findAll(Connection conn) throws SQLException {

        List<String> runs = new ArrayList<>();

        String sql = "SELECT * FROM algorithm_runs";


        PreparedStatement stmt = conn.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();


        while (rs.next()) {

            String run =
                    rs.getInt("runId") +
                    " - " +
                    rs.getString("algorithmName") +
                    " | Time: " +
                    rs.getLong("timeNs") +
                    " ns";


            runs.add(run);

        }


        return runs;
    }



    public void insert(Connection conn,
                       int runId,
                       String algorithmName,
                       int inputSize,
                       long timeNs,
                       int memoryKb,
                       String dateRun)
            throws SQLException {


        String sql =
                """
                INSERT INTO algorithm_runs
                (runId,algorithmName,inputSize,timeNs,memoryKb,dateRun)
                VALUES (?,?,?,?,?,?)
                """;


        PreparedStatement stmt =
                conn.prepareStatement(sql);


        stmt.setInt(1, runId);
        stmt.setString(2, algorithmName);
        stmt.setInt(3, inputSize);
        stmt.setLong(4, timeNs);
        stmt.setInt(5, memoryKb);
        stmt.setString(6, dateRun);


        stmt.executeUpdate();

    }

}