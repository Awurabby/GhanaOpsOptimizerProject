package com.team.smartops.algorithms.optimisation;

import java.util.*;

/**
 * OWNER: Team F.
 * Priority-based resource/route choice. MUST include one documented
 * failure case (brief Sec 7).
 *
 * Rule: sort requests by urgency (most urgent first), then for each request
 * in order, assign the CLOSEST currently-available resource. Once assigned,
 * that resource is marked unavailable for the rest of the run.
 *
 * This is a "locally optimal, never revisited" strategy -- which is exactly
 * why it can produce a worse overall outcome than a smarter first choice.
 * See buildCounterexample() below for the required failure case.
 *
 * TODO: replace the DistanceLookup interface usage with Team C's real
 * graph/distance API once it's available (e.g. graph.shortestDistance(a, b)).
 * TODO: replace the manual sort in sortByUrgency() with Team D's real sort
 * implementation (their custom merge sort / quicksort), since built-in
 * sort utilities may not be allowed for assessed core logic -- confirm
 * whether this counts as "core logic" or orchestration glue.
 */
public class GreedyAssignment {

    /** Pluggable so this can be swapped for the real graph-based distance calculator. */
    public interface DistanceLookup {
        double distanceBetween(String locationA, String locationB);
    }

    private final DistanceLookup distanceLookup;

    public GreedyAssignment(DistanceLookup distanceLookup) {
        this.distanceLookup = distanceLookup;
    }

    /**
     * @param requests  requests to serve, in any order
     * @param resources available resources; availability is mutated as the
     *                  algorithm runs (each resource can only be used once)
     * @return the list of assignments made, in the order requests were processed
     */
    public List<Assignment> greedyAssign(List<ServiceRequest> requests, List<Resource> resources) {
        List<ServiceRequest> sorted = sortByUrgencyDescending(requests);
        List<Assignment> assignments = new ArrayList<>();

        for (ServiceRequest request : sorted) {
            Resource nearest = null;
            double bestDistance = Double.MAX_VALUE;

            for (Resource candidate : resources) {
                if (!candidate.isAvailable()) continue;
                double d = distanceLookup.distanceBetween(request.getSource(), candidate.getHomeLocation());
                if (d < bestDistance) {
                    bestDistance = d;
                    nearest = candidate;
                }
            }

            if (nearest != null) {
                nearest.setAvailable(false);
                assignments.add(new Assignment(request.getRequestId(), nearest.getResourceId(), bestDistance));
            }
            // else: no resource available -- request goes unserved.
            // Decide with your team whether unserved requests should be
            // logged/returned separately for the report.
        }
        return assignments;
    }

    /**
     * TEMPORARY: simple descending-urgency sort using Collections.sort.
     * Replace with Team D's from-scratch sort before final submission --
     * built-in sorts are fine for demo/glue code but check the brief's
     * rule on assessed core logic before leaving this in.
     */
    private List<ServiceRequest> sortByUrgencyDescending(List<ServiceRequest> requests) {
        List<ServiceRequest> copy = new ArrayList<>(requests);
        copy.sort((a, b) -> Integer.compare(b.getUrgency(), a.getUrgency()));
        return copy;
    }

    /**
     * Builds the required greedy-failure counterexample: a small scenario
     * where "always take the nearest resource for the most urgent request"
     * produces a worse total outcome than a different first choice would.
     *
     * Scenario (work this out on paper first, then compare to this code):
     *   - Resource R1 is very close to urgent Request A, but R1 is the ONLY
     *     resource within reach of Request B.
     *   - Resource R2 is a bit farther from Request A, but useless for B.
     *   - Greedy assigns R1 to A (nearest), leaving B unreachable by R2.
     *   - A smarter choice assigns R2 to A and saves R1 for B, serving both.
     *
     * Use this method's output directly in your report as the documented
     * counterexample trace.
     */
    public static void printCounterexampleScenario() {
        System.out.println("=== Greedy Counterexample ===");
        System.out.println("Requests: A (urgency=9, source=Zone1), B (urgency=5, source=Zone2)");
        System.out.println("Resources: R1@Zone1 (reachable by A and B), R2@Zone1-far (reachable by A only)");
        System.out.println();
        System.out.println("Greedy trace:");
        System.out.println("  1. Sort by urgency: [A, B]");
        System.out.println("  2. A's nearest available resource -> R1 (distance 1) -- ASSIGNED");
        System.out.println("  3. B's nearest available resource -> none left that can reach Zone2 -- UNSERVED");
        System.out.println("  Total value served: value(A) only");
        System.out.println();
        System.out.println("Alternative (non-greedy) trace:");
        System.out.println("  1. Assign R2 to A instead (distance 3, still reachable within deadline)");
        System.out.println("  2. R1 remains free -> assign to B (distance 1) -- ASSIGNED");
        System.out.println("  Total value served: value(A) + value(B)  <-- strictly better");
        System.out.println();
        System.out.println("Conclusion: greedy's 'always nearest' rule is locally optimal at each step");
        System.out.println("but globally suboptimal here, because it doesn't account for which future");
        System.out.println("requests a resource is uniquely able to serve.");
        System.out.println();
        System.out.println("NOTE: replace Zone1/Zone2/R1/R2 with real locations and distances from your");
        System.out.println("team's actual dataset before putting this in the report.");
    }
}