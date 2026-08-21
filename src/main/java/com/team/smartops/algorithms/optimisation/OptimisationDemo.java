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
        runBudgetSelectionComparisonDemo();
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

    private static void runBudgetSelectionComparisonDemo() {
        System.out.println("=== Greedy vs DP Budget Selection ===");

        List<ServiceRequest> requests = List.of(
            new ServiceRequest("A", "Balme Library", "Pentagon Hostel", "maintenance", 5, 3, 5),
            new ServiceRequest("B", "University Hospital", "Main Gate", "delivery", 3, 2, 3),
            new ServiceRequest("C", "Commonwealth Hall", "Balme Library", "maintenance", 3, 2, 3)
        );
        int budget = 4;

        GreedyBudgetSelector.Result greedy =
            new GreedyBudgetSelector().select(requests, budget);
        KnapsackDP dp = new KnapsackDP();
        KnapsackDP.Result optimal = dp.solveWithReconstruction(requests, budget);

        System.out.println("Budget: " + budget);
        System.out.println("Greedy selected: " + greedy.selected
            + " | cost=" + greedy.totalCost + " | value=" + greedy.totalValue);
        System.out.println("DP selected: " + optimal.selected
            + " | value=" + optimal.bestValue);
        System.out.println("DP improvement: " + (optimal.bestValue - greedy.totalValue));
    }
}
