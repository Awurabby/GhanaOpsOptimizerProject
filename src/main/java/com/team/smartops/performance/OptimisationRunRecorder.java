package com.team.smartops.performance;

import com.team.smartops.db.AlgorithmRunRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/** Stores optimisation timing measurements using the shared database contract. */
public class OptimisationRunRecorder {

    private final AlgorithmRunRepository repository;

    public OptimisationRunRecorder() {
        this(new AlgorithmRunRepository());
    }

    OptimisationRunRecorder(AlgorithmRunRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public void save(Connection connection,
                     List<OptimisationTimingExperiment.Measurement> measurements)
            throws SQLException {
        save(connection, measurements, nextRunId(connection));
    }

    public void save(Connection connection,
                     List<OptimisationTimingExperiment.Measurement> measurements,
                     int firstRunId) throws SQLException {
        Objects.requireNonNull(connection, "connection must not be null");
        Objects.requireNonNull(measurements, "measurements must not be null");
        if (firstRunId < 0) {
            throw new IllegalArgumentException("firstRunId must not be negative");
        }

        int runId = firstRunId;
        String dateRun = LocalDate.now().toString();
        for (OptimisationTimingExperiment.Measurement measurement : measurements) {
            repository.insert(connection, runId++, measurement.algorithmName,
                measurement.inputSize, measurement.averageTimeNs, 0, dateRun);
        }
    }

    private int nextRunId(Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection must not be null");
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                 "SELECT COALESCE(MAX(runId), 0) + 1 AS nextRunId FROM algorithm_runs")) {
            return result.getInt("nextRunId");
        }
    }
}
