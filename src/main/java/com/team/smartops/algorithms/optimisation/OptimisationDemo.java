package com.team.smartops.algorithms.optimisation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Standalone demo for the optimisation algorithms. Budget selection uses the
 * shared project CSV files; assignment remains synthetic until Team C exposes
 * a graph-distance implementation through {@link GreedyAssignment.DistanceLookup}.
 */
public class OptimisationDemo {

    private static final int DEMO_BUDGET = 20;

    public static void main(String[] args) throws IOException {
        runGreedyDemo();
        System.out.println();
        GreedyAssignment.printCounterexampleScenario();
        System.out.println();
        runBudgetSelectionComparisonDemo();
    }

    private static void runGreedyDemo() {
        System.out.println("=== Greedy Assignment Demo ===");

        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Balme Library", "Pentagon Hostel", "maintenance", 9, 3, 9),
            new ServiceRequest("R2", "Main Gate", "Legon Hall", "delivery", 5, 2, 5),
            new ServiceRequest("R3", "Balme Library", "University Hospital", "maintenance", 7, 4, 7)
        );

        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Balme Library", true),
            new Resource("Res2", "van", "Main Gate", true)
        ));

        // Placeholder straight-line distance -- replace with the graph team's
        // real shortest-path/weighted-road distance lookup.
        GreedyAssignment.DistanceLookup placeholderDistance = (a, b) -> a.equals(b) ? 0.0 : 5.0;

        GreedyAssignment greedy = new GreedyAssignment(placeholderDistance);
        List<Assignment> assignments = greedy.greedyAssign(requests, resources);

        assignments.forEach(System.out::println);
    }

    private static void runBudgetSelectionComparisonDemo() throws IOException {
        System.out.println("=== Greedy vs DP Budget Selection ===");

        List<ServiceRequest> requests = new OptimisationCsvLoader().loadPendingRequests(
            Path.of("data", "csv", "service_requests.csv"),
            Path.of("data", "csv", "locations.csv")
        );

        GreedyBudgetSelector.Result greedy =
            new GreedyBudgetSelector().select(requests, DEMO_BUDGET);
        KnapsackDP dp = new KnapsackDP();
        KnapsackDP.Result optimal = dp.solveWithReconstruction(requests, DEMO_BUDGET);

        System.out.println("Pending requests loaded: " + requests.size());
        System.out.println("Budget: " + DEMO_BUDGET);
        System.out.println("Greedy selected: " + greedy.selected
            + " | cost=" + greedy.totalCost + " | value=" + greedy.totalValue);
        System.out.println("DP selected: " + optimal.selected
            + " | value=" + optimal.bestValue);
        System.out.println("DP improvement: " + (optimal.bestValue - greedy.totalValue));
    }
}
