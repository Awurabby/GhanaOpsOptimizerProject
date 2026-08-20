package com.team.smartops.performance;

import com.team.smartops.algorithms.optimisation.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
