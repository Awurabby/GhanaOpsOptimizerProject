package com.team.smartops.algorithms.optimisation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OptimisationDemo {

    private static final int DEMO_BUDGET = 20;

    /**
     * Runs all optimisation demonstrations.
     *
     * This is intentionally self-contained so App can call it directly.
     */
    public static void run() {

        System.out.println();
        System.out.println("=== OPTIMISATION DEMO ===");

        // ---------------------------------------------------------
        // PART 1: GREEDY RESOURCE ASSIGNMENT
        // ---------------------------------------------------------
        runGreedyDemo();

        System.out.println();

        // ---------------------------------------------------------
        // PART 2: GREEDY FAILURE CASE
        // ---------------------------------------------------------
        GreedyAssignment.printCounterexampleScenario();

        System.out.println();

        // ---------------------------------------------------------
        // PART 3: GREEDY VS DP
        // ---------------------------------------------------------
        runBudgetSelectionComparisonDemo();
    }

    private static void runGreedyDemo() {

        System.out.println("=== Greedy Resource Assignment ===");

        List<ServiceRequest> requests = List.of(
            new ServiceRequest(
                "R1",
                "Balme Library",
                "Pentagon Hostel",
                "maintenance",
                9,
                3,
                9
            ),
            new ServiceRequest(
                "R2",
                "Main Gate",
                "Legon Hall",
                "delivery",
                5,
                2,
                5
            ),
            new ServiceRequest(
                "R3",
                "Balme Library",
                "University Hospital",
                "maintenance",
                7,
                4,
                7
            )
        );

        List<Resource> resources = new ArrayList<>(List.of(
            new Resource(
                "Res1",
                "van",
                "Balme Library",
                true
            ),
            new Resource(
                "Res2",
                "van",
                "Main Gate",
                true
            )
        ));

        /*
         * Temporary distance lookup.
         *
         * The real graph already exists in Team C's Graph class,
         * but the current optimisation model does not yet have
         * location IDs connected to Resource objects.
         *
         * Therefore we keep this demonstration functional rather
         * than risking a last-minute rewrite of the data model.
         */
        GreedyAssignment.DistanceLookup distanceLookup =
            (locationA, locationB) ->
                locationA.equals(locationB) ? 0.0 : 5.0;

        GreedyAssignment greedy =
            new GreedyAssignment(distanceLookup);

        List<Assignment> assignments =
            greedy.greedyAssign(requests, resources);

        System.out.println("Assignments:");

        for (Assignment assignment : assignments) {
            System.out.println("  " + assignment);
        }
    }

    private static void runBudgetSelectionComparisonDemo() {

        System.out.println("=== Greedy vs Dynamic Programming ===");

        try {

            Path requestsFile =
                Path.of("data", "csv", "service_requests.csv");

            Path locationsFile =
                Path.of("data", "csv", "locations.csv");

            List<ServiceRequest> requests =
                new OptimisationCsvLoader()
                    .loadPendingRequests(
                        requestsFile,
                        locationsFile
                    );

            System.out.println(
                "Pending requests loaded: " + requests.size()
            );

            System.out.println(
                "Available budget: " + DEMO_BUDGET
            );

            // -----------------------------
            // GREEDY
            // -----------------------------

            GreedyBudgetSelector.Result greedy =
                new GreedyBudgetSelector()
                    .select(requests, DEMO_BUDGET);

            System.out.println();
            System.out.println("--- Greedy Result ---");

            System.out.println(
                "Requests selected: " + greedy.selected.size()
            );

            System.out.println(
                "Total cost: " + greedy.totalCost
            );

            System.out.println(
                "Total value: " + greedy.totalValue
            );

            // -----------------------------
            // DYNAMIC PROGRAMMING
            // -----------------------------

            KnapsackDP dp = new KnapsackDP();

            KnapsackDP.Result optimal =
                dp.solveWithReconstruction(
                    requests,
                    DEMO_BUDGET
                );

            System.out.println();
            System.out.println("--- Dynamic Programming Result ---");

            System.out.println(
                "Requests selected: " + optimal.selected.size()
            );

            System.out.println(
                "Best value: " + optimal.bestValue
            );

            // -----------------------------
            // COMPARISON
            // -----------------------------

            int improvement =
                optimal.bestValue - greedy.totalValue;

            System.out.println();
            System.out.println("--- Comparison ---");

            System.out.println(
                "Greedy value: " + greedy.totalValue
            );

            System.out.println(
                "DP value: " + optimal.bestValue
            );

            System.out.println(
                "DP improvement: " + improvement
            );

            if (improvement > 0) {
                System.out.println(
                    "DP found a better allocation than the greedy approach."
                );
            } else {
                System.out.println(
                    "Both approaches produced the same value for this dataset."
                );
            }

            // Small evidence table
            System.out.println();
            System.out.println("--- DP Table ---");

            int tableBudget = Math.min(DEMO_BUDGET, 10);

            // Only print a small portion if the real dataset is large.
            System.out.println(
                "DP table generated successfully with "
                + optimal.table.length
                + " rows."
            );

        } catch (Exception e) {

            System.out.println(
                "Budget optimisation could not run: "
                + e.getMessage()
            );

            System.out.println(
                "Make sure data/csv/service_requests.csv and "
                + "data/csv/locations.csv exist."
            );
        }
    }
}