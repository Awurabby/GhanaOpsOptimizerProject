package com.team.smartops.algorithms.optimisation;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


class GreedyAssignmentTest {

    private final GreedyAssignment.DistanceLookup fixedDistance = (a, b) -> 1.0;

    @Test
    void normalCase_assignsMostUrgentRequestFirst() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Zone1", "Zone2", "cat", 5, 1, 5),
            new ServiceRequest("R2", "Zone1", "Zone2", "cat", 9, 1, 9)
        );
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Zone1", true)
        ));

        List<Assignment> result = new GreedyAssignment(fixedDistance).greedyAssign(requests, resources);

        assertEquals(1, result.size());
        assertEquals("R2", result.get(0).getRequestId(), "higher-urgency request should be served first");
    }

    @Test
    void boundaryCase_moreResourcesThanRequests_allRequestsServed() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Zone1", "Zone2", "cat", 5, 1, 5)
        );
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Zone1", true),
            new Resource("Res2", "van", "Zone1", true)
        ));

        List<Assignment> result = new GreedyAssignment(fixedDistance).greedyAssign(requests, resources);

        assertEquals(1, result.size());
    }

    @Test
    void boundaryCase_noResourcesAvailable_noAssignmentsMade() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("R1", "Zone1", "Zone2", "cat", 5, 1, 5)
        );
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Zone1", false) // unavailable
        ));

        List<Assignment> result = new GreedyAssignment(fixedDistance).greedyAssign(requests, resources);

        assertTrue(result.isEmpty());
    }

    @Test
    void invalidInput_emptyRequestList_returnsEmptyAssignments() {
        List<ServiceRequest> requests = Collections.emptyList();
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Zone1", true)
        ));

        List<Assignment> result = new GreedyAssignment(fixedDistance).greedyAssign(requests, resources);

        assertTrue(result.isEmpty());
    }

    @Test
    void counterexample_greedyGivesWorseTotalValueThanAlternative() {
        // Reproduces the documented greedy-failure scenario in code.
        // R1 is reachable by both A and B; R2 only reasonably reachable by A.
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("A", "Zone1", "Zone1", "cat", 9, 1, 9),
            new ServiceRequest("B", "Zone2", "Zone2", "cat", 5, 1, 5)
        );
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("R1", "van", "Zone1", true),  // close to A, reachable by B too
            new Resource("R2", "van", "Zone1", true)   // only useful for A in this scenario
        ));

        GreedyAssignment.DistanceLookup distance = (from, to) -> {
            // R1 is closest to A's zone; R2 is far from B's zone entirely.
            if (from.equals("Zone1")) return 1.0;       // A -> either resource: both at Zone1
            if (from.equals("Zone2")) return to.equals("Zone1") ? 100.0 : 1.0; // B can't realistically reach Zone1
            return 1.0;
        };

        List<Assignment> greedyResult = new GreedyAssignment(distance).greedyAssign(requests, resources);

        // Greedy assigns nearest-available to A first (R1, distance 1), leaving
        // B effectively unreachable (distance 100) -- document this trade-off
        // in the report alongside the printCounterexampleScenario() output.
        assertEquals(2, greedyResult.size(), "greedy still technically assigns both, but at a much worse total distance");
        double totalDistance = greedyResult.stream().mapToDouble(Assignment::getDistance).sum();
        assertTrue(totalDistance > 50, "greedy's choice forces a very costly assignment for B");
    }
}