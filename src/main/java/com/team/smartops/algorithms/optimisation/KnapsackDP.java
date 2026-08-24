package com.team.smartops.algorithms.optimisation;

import java.util.*;

/**
 * OWNER: Team F.
 * Request selection under a budget/capacity constraint. Keeps the
 * memoisation/tabulation table + reconstruction step.
 *
 * 0/1 knapsack-style dynamic programming: pick the subset of requests that
 * maximizes total value without exceeding a budget/capacity, using each
 * request at most once.
 *
 * solveWithReconstruction(...) returns the best value, selected requests and
 * full table required for the project evidence.
 */
public class KnapsackDP {

    /** Result bundle: best value plus the actual requests selected to reach it. */
    public static class Result {
        public final int bestValue;
        public final List<ServiceRequest> selected;
        public final int[][] table; // kept for printing the trace table

        Result(int bestValue, List<ServiceRequest> selected, int[][] table) {
            this.bestValue = bestValue;
            this.selected = selected;
            this.table = table;
        }
    }

    /**
     * @param requests candidate requests, each with a cost and value
     * @param budget   total budget/capacity available
     */
    public Result solveWithReconstruction(List<ServiceRequest> requests, int budget) {
        validateInputs(requests, budget);

        int n = requests.size();
        int[][] dp = new int[n + 1][budget + 1];

        for (int i = 1; i <= n; i++) {
            int cost = requests.get(i - 1).getCost();
            int value = requests.get(i - 1).getValue();
            for (int b = 0; b <= budget; b++) {
                if (cost <= b) {
                    dp[i][b] = Math.max(dp[i - 1][b], value + dp[i - 1][b - cost]);
                } else {
                    dp[i][b] = dp[i - 1][b];
                }
            }
        }

        // Reconstruction: walk back from dp[n][budget]. If the value at row i
        // differs from row i-1 at the same budget, item i was included --
        // subtract its cost and continue from row i-1 at the reduced budget.
        // Otherwise item i was skipped -- move to row i-1 at the same budget.
        List<ServiceRequest> selected = new ArrayList<>();
        int b = budget;
        for (int i = n; i >= 1; i--) {
            if (dp[i][b] != dp[i - 1][b]) {
                ServiceRequest chosen = requests.get(i - 1);
                selected.add(chosen);
                b -= chosen.getCost();
            }
        }
        Collections.reverse(selected);

        return new Result(dp[n][budget], selected, dp);
    }

    private void validateInputs(List<ServiceRequest> requests, int budget) {
        if (requests == null) {
            throw new IllegalArgumentException("requests must not be null");
        }
        if (budget < 0) {
            throw new IllegalArgumentException("budget must not be negative");
        }

        for (ServiceRequest request : requests) {
            if (request.getCost() < 0) {
                throw new IllegalArgumentException(
                    "request cost must not be negative: " + request.getRequestId());
            }
            if (request.getValue() < 0) {
                throw new IllegalArgumentException(
                    "request value must not be negative: " + request.getRequestId());
            }
        }
    }

    /**
     * Prints the full DP table -- use this directly for the "full table for a
     * small example (5-8 requests)" evidence requirement. Keep the example
     * small enough that the table fits legibly in the report.
     */
    public void printTable(int[][] dp, List<ServiceRequest> requests, int budget) {
        System.out.print("i\\b\t");
        for (int b = 0; b <= budget; b++) System.out.print(b + "\t");
        System.out.println();

        System.out.print("0\t");
        for (int b = 0; b <= budget; b++) System.out.print(dp[0][b] + "\t");
        System.out.println("  (no requests considered)");

        for (int i = 1; i < dp.length; i++) {
            System.out.print(i + "\t");
            for (int b = 0; b <= budget; b++) System.out.print(dp[i][b] + "\t");
            System.out.println("  (" + requests.get(i - 1) + ")");
        }
    }
}
