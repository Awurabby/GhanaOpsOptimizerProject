package com.team.smartops.performance;

import com.team.smartops.algorithms.optimisation.GreedyBudgetSelector;
import com.team.smartops.algorithms.optimisation.KnapsackDP;
import com.team.smartops.algorithms.optimisation.ServiceRequest;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Times the greedy and DP solutions to the same budget-selection problem. */
public class OptimisationTimingExperiment {

    private static final int[] INPUT_SIZES = {50, 100, 200, 500, 1000};
    private static final int RUNS_PER_SIZE = 3;
    private static final long RANDOM_SEED = 204308L;
    private static final Path OUTPUT_PATH = Path.of("data/results/optimisation-timing.csv");
    private static final String[] CAMPUS_LOCATIONS = {
        "Balme Library", "Pentagon Hostel", "University Hospital",
        "Main Gate", "Commonwealth Hall", "Legon Hall"
    };

    public static class Measurement {
        public final String algorithmName;
        public final int inputSize;
        public final int budget;
        public final long averageTimeNs;
        public final int totalCost;
        public final int totalValue;

        Measurement(String algorithmName, int inputSize, int budget,
                    long averageTimeNs, int totalCost, int totalValue) {
            this.algorithmName = algorithmName;
            this.inputSize = inputSize;
            this.budget = budget;
            this.averageTimeNs = averageTimeNs;
            this.totalCost = totalCost;
            this.totalValue = totalValue;
        }
    }

    public List<Measurement> measure(List<ServiceRequest> requests, int budget, int runs) {
        if (runs <= 0) {
            throw new IllegalArgumentException("runs must be positive");
        }

        GreedyBudgetSelector greedy = new GreedyBudgetSelector();
        KnapsackDP dp = new KnapsackDP();

        // Warm up both code paths before collecting measurements.
        GreedyBudgetSelector.Result greedyResult = greedy.select(requests, budget);
        KnapsackDP.Result dpResult = dp.solveWithReconstruction(requests, budget);

        long greedyTotalTime = 0;
        long dpTotalTime = 0;

        for (int run = 0; run < runs; run++) {
            GreedyBudgetSelector.Result[] greedyHolder = new GreedyBudgetSelector.Result[1];
            greedyTotalTime += Timer.timeInNanos(
                () -> greedyHolder[0] = greedy.select(requests, budget));
            greedyResult = greedyHolder[0];

            KnapsackDP.Result[] dpHolder = new KnapsackDP.Result[1];
            dpTotalTime += Timer.timeInNanos(
                () -> dpHolder[0] = dp.solveWithReconstruction(requests, budget));
            dpResult = dpHolder[0];
        }

        int dpTotalCost = dpResult.selected.stream()
            .mapToInt(ServiceRequest::getCost)
            .sum();

        return List.of(
            new Measurement("GreedyBudgetSelector", requests.size(), budget,
                greedyTotalTime / runs, greedyResult.totalCost, greedyResult.totalValue),
            new Measurement("KnapsackDP", requests.size(), budget,
                dpTotalTime / runs, dpTotalCost, dpResult.bestValue)
        );
    }

    public static void main(String[] args) throws IOException {
        OptimisationTimingExperiment experiment = new OptimisationTimingExperiment();
        List<Measurement> allMeasurements = new ArrayList<>();

        for (int inputSize : INPUT_SIZES) {
            int budget = inputSize * 3;
            List<ServiceRequest> requests = generateRequests(inputSize);
            List<Measurement> measurements =
                experiment.measure(requests, budget, RUNS_PER_SIZE);
            allMeasurements.addAll(measurements);

            Measurement greedy = measurements.get(0);
            Measurement dp = measurements.get(1);
            System.out.printf(
                "n=%d, budget=%d | greedy=%d ns/value %d | DP=%d ns/value %d%n",
                inputSize, budget,
                greedy.averageTimeNs, greedy.totalValue,
                dp.averageTimeNs, dp.totalValue);
        }

        experiment.writeCsv(OUTPUT_PATH, allMeasurements);
        System.out.println("Results written to " + OUTPUT_PATH);
    }

    private static List<ServiceRequest> generateRequests(int inputSize) {
        Random random = new Random(RANDOM_SEED + inputSize);
        List<ServiceRequest> requests = new ArrayList<>(inputSize);

        for (int i = 0; i < inputSize; i++) {
            int urgency = random.nextInt(3) + 1;
            int cost = random.nextInt(10) + 1;
            int value = urgency * 10 + random.nextInt(10);
            String source = CAMPUS_LOCATIONS[random.nextInt(CAMPUS_LOCATIONS.length)];
            String destination = CAMPUS_LOCATIONS[random.nextInt(CAMPUS_LOCATIONS.length)];

            requests.add(new ServiceRequest(
                "REQ" + (i + 1), source, destination, "Campus Service",
                urgency, cost, value));
        }

        return requests;
    }

    public void writeCsv(Path output, List<Measurement> measurements) throws IOException {
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            writer.write("algorithmName,inputSize,budget,averageTimeNs,totalCost,totalValue,dateRun");
            writer.newLine();

            for (Measurement measurement : measurements) {
                writer.write(String.format("%s,%d,%d,%d,%d,%d,%s",
                    measurement.algorithmName,
                    measurement.inputSize,
                    measurement.budget,
                    measurement.averageTimeNs,
                    measurement.totalCost,
                    measurement.totalValue,
                    LocalDate.now()));
                writer.newLine();
            }
        }
    }
}
