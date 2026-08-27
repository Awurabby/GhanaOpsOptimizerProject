package com.team.smartops.db;

import com.team.smartops.model.AlgorithmRun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmRunRepository {

    public List<AlgorithmRun> findAll(Connection conn) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            runs.add(new AlgorithmRun(
                    rs.getInt("runId"),
                    rs.getString("algorithmName"),
                    rs.getInt("inputSize"),
                    rs.getLong("timeNs"),
                    rs.getInt("memoryKb"),
                    rs.getString("dateRun")
            ));
        }

        return runs;
    }

    public void insert(Connection conn, AlgorithmRun run) throws SQLException {
        insert(conn, run.getRunId(), run.getAlgorithmName(), run.getInputSize(),
                run.getTimeNs(), run.getMemoryKb(), run.getDateRun());
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

        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, runId);
        stmt.setString(2, algorithmName);
        stmt.setInt(3, inputSize);
        stmt.setLong(4, timeNs);
        stmt.setInt(5, memoryKb);
        stmt.setString(6, dateRun);

        stmt.executeUpdate();
    }
}