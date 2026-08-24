package com.team.smartops.performance;

import com.team.smartops.algorithms.optimisation.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptimisationTimingExperimentTest {

    @TempDir
    Path tempDirectory;

    @Test
    void measure_comparesGreedyAndDpUsingTheSameProblem() {
        List<ServiceRequest> requests = List.of(
            request("A", 3, 5),
            request("B", 2, 3),
            request("C", 2, 3)
        );

        List<OptimisationTimingExperiment.Measurement> measurements =
            new OptimisationTimingExperiment().measure(requests, 4, 3);

        assertEquals(2, measurements.size());
        assertMeasurement(measurements.get(0), "GreedyBudgetSelector", 3, 4, 3, 5);
        assertMeasurement(measurements.get(1), "KnapsackDP", 3, 4, 4, 6);
    }

    @Test
    void writeCsv_exportsRuntimeAndSolutionQualityForBothAlgorithms() throws IOException {
        OptimisationTimingExperiment experiment = new OptimisationTimingExperiment();
        List<OptimisationTimingExperiment.Measurement> measurements =
            experiment.measure(List.of(
                request("A", 3, 5),
                request("B", 2, 3),
                request("C", 2, 3)
            ), 4, 1);
        Path output = tempDirectory.resolve("optimisation-timing.csv");

        experiment.writeCsv(output, measurements);

        List<String> lines = Files.readAllLines(output);
        assertEquals("algorithmName,inputSize,budget,averageTimeNs,totalCost,totalValue,dateRun",
            lines.get(0));
        assertEquals("GreedyBudgetSelector", lines.get(1).split(",")[0]);
        assertEquals("3", lines.get(1).split(",")[1]);
        assertEquals("4", lines.get(1).split(",")[2]);
        assertEquals("5", lines.get(1).split(",")[5]);
        assertEquals("KnapsackDP", lines.get(2).split(",")[0]);
        assertEquals("6", lines.get(2).split(",")[5]);
    }

    @Test
    void measure_rejectsNonPositiveRunCount() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new OptimisationTimingExperiment().measure(List.of(), 4, 0)
        );

        assertEquals("runs must be positive", error.getMessage());
    }

    @Test
    void runRecorder_persistsMeasurementsThroughSharedRepository() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE algorithm_runs (
                    runId INTEGER PRIMARY KEY,
                    algorithmName TEXT NOT NULL,
                    inputSize INTEGER NOT NULL,
                    timeNs INTEGER NOT NULL,
                    memoryKb INTEGER NOT NULL,
                    dateRun TEXT NOT NULL
                )
                """);
            List<OptimisationTimingExperiment.Measurement> measurements =
                new OptimisationTimingExperiment().measure(List.of(
                    request("A", 3, 5), request("B", 2, 3)), 4, 1);

            new OptimisationRunRecorder().save(connection, measurements, 40);

            try (ResultSet rows = statement.executeQuery(
                    "SELECT * FROM algorithm_runs ORDER BY runId")) {
                rows.next();
                assertEquals(40, rows.getInt("runId"));
                assertEquals("GreedyBudgetSelector", rows.getString("algorithmName"));
                assertEquals(2, rows.getInt("inputSize"));
                assertEquals(0, rows.getInt("memoryKb"));
                rows.next();
                assertEquals(41, rows.getInt("runId"));
                assertEquals("KnapsackDP", rows.getString("algorithmName"));
                assertEquals(false, rows.next());
            }
        }
    }

    @Test
    void runRecorder_usesTheNextAvailableRunId() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE algorithm_runs (
                    runId INTEGER PRIMARY KEY, algorithmName TEXT, inputSize INTEGER,
                    timeNs INTEGER, memoryKb INTEGER, dateRun TEXT
                )
                """);
            statement.executeUpdate("""
                INSERT INTO algorithm_runs VALUES (9, 'ExistingRun', 1, 1, 0, '2026-01-01')
                """);
            List<OptimisationTimingExperiment.Measurement> measurements =
                new OptimisationTimingExperiment().measure(
                    List.of(request("A", 1, 1)), 1, 1);

            new OptimisationRunRecorder().save(connection, measurements);

            try (ResultSet result = statement.executeQuery(
                    "SELECT MIN(runId) AS firstId, MAX(runId) AS lastId FROM algorithm_runs "
                        + "WHERE algorithmName != 'ExistingRun'")) {
                assertEquals(10, result.getInt("firstId"));
                assertEquals(11, result.getInt("lastId"));
            }
        }
    }

    private void assertMeasurement(OptimisationTimingExperiment.Measurement measurement,
                                   String algorithmName, int inputSize, int budget,
                                   int totalCost, int totalValue) {
        assertEquals(algorithmName, measurement.algorithmName);
        assertEquals(inputSize, measurement.inputSize);
        assertEquals(budget, measurement.budget);
        assertEquals(totalCost, measurement.totalCost);
        assertEquals(totalValue, measurement.totalValue);
    }

    private ServiceRequest request(String id, int cost, int value) {
        return new ServiceRequest(id, "Balme Library", "Pentagon Hostel",
            "Maintenance", value, cost, value);
    }
}
