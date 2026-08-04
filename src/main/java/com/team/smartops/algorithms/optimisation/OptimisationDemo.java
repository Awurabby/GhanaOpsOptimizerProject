package com.team.smartops.algorithms.optimisation;

import java.util.*;

/**
 * Standalone demo you can run directly (or wire into the console menu once
 * that's built) to sanity-check both algorithms before hooking up real data.
 *
 * TODO once the DB/graph teams have working code:
 *   - load ServiceRequest / Resource lists from the database instead of the
 *     hardcoded lists below
 *   - swap the DistanceLookup lambda for Team C's real graph distance call
 *   - swap System.currentTimeMillis()/nanoTime() calls for Team B's Timer
 *     utility, and log results to algorithm_runs per the brief
 */
public class OptimisationDemo {

    public static void main(String[] args) {
        runGreedyDemo();
        System.out.println();
        GreedyAssignment.printCounterexampleScenario();
        System.out.println();
        runKnapsackDemo();
    }

    private static void runGreedyDemo() {
        System.out.println("=== Greedy Assignment Demo ===");

        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Zone1", "Zone3", "maintenance", 9, 3, 9),
            new ServiceRequest("R2", "Zone2", "Zone4", "delivery", 5, 2, 5),
            new ServiceRequest("R3", "Zone1", "Zone5", "maintenance", 7, 4, 7)
        );

        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Zone1", true),
            new Resource("Res2", "van", "Zone2", true)
        ));

        // Placeholder straight-line distance -- replace with the graph team's
        // real shortest-path/weighted-road distance lookup.
        GreedyAssignment.DistanceLookup placeholderDistance = (a, b) -> a.equals(b) ? 0.0 : 5.0;

        GreedyAssignment greedy = new GreedyAssignment(placeholderDistance);
        List<Assignment> assignments = greedy.greedyAssign(requests, resources);

        assignments.forEach(System.out::println);
    }

    private static void runKnapsackDemo() {
        System.out.println("=== Knapsack DP Demo ===");

        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Zone1", "Zone3", "maintenance", 9, 3, 9),
            new ServiceRequest("R2", "Zone2", "Zone4", "delivery", 5, 2, 5),
            new ServiceRequest("R3", "Zone1", "Zone5", "maintenance", 7, 4, 7),
            new ServiceRequest("R4", "Zone3", "Zone2", "delivery", 4, 1, 4),
            new ServiceRequest("R5", "Zone2", "Zone1", "maintenance", 6, 3, 6)
        );
        int budget = 6;

        KnapsackDP dp = new KnapsackDP();
        KnapsackDP.Result result = dp.solveWithReconstruction(requests, budget);

        dp.printTable(result.table, requests, budget);
        System.out.println("Best total value: " + result.bestValue);
        System.out.println("Requests selected: " + result.selected);
    }
}