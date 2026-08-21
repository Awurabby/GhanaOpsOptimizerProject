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
    void boundaryCase_emptyRequestList_returnsEmptyAssignments() {
        List<ServiceRequest> requests = Collections.emptyList();
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("Res1", "van", "Zone1", true)
        ));

        List<Assignment> result = new GreedyAssignment(fixedDistance).greedyAssign(requests, resources);

        assertTrue(result.isEmpty());
    }

    @Test
    void invalidInput_nullRequestList_isRejected() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new GreedyAssignment(fixedDistance).greedyAssign(null, new ArrayList<>())
        );

        assertEquals("requests must not be null", error.getMessage());
    }

    @Test
    void invalidInput_nullResourceList_isRejected() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> new GreedyAssignment(fixedDistance).greedyAssign(Collections.emptyList(), null)
        );

        assertEquals("resources must not be null", error.getMessage());
    }

    @Test
    void equalDistance_usesLowestResourceIdAsTieBreaker() {
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("REQ1", "Balme Library", "Pentagon Hostel", "Maintenance", 5, 1, 5)
        );
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("R2", "van", "Main Gate", true),
            new Resource("R1", "van", "University Hospital", true)
        ));

        List<Assignment> result = new GreedyAssignment(fixedDistance).greedyAssign(requests, resources);

        assertEquals("R1", result.get(0).getResourceId());
    }

    @Test
    void counterexample_greedyGivesWorseTotalValueThanAlternative() {
        // Greedy uses the resource closest to Balme Library first, leaving the
        // Pentagon Hostel request with a very expensive remaining assignment.
        List<ServiceRequest> requests = List.of(
            new ServiceRequest("A", "Balme Library", "Balme Library", "Maintenance", 9, 1, 9),
            new ServiceRequest("B", "Pentagon Hostel", "Pentagon Hostel", "Maintenance", 5, 1, 5)
        );
        List<Resource> resources = new ArrayList<>(List.of(
            new Resource("R1", "van", "Main Gate", true),
            new Resource("R2", "van", "University Hospital", true)
        ));

        GreedyAssignment.DistanceLookup distance = (from, to) -> {
            if (from.equals("Balme Library") && to.equals("Main Gate")) return 1.0;
            if (from.equals("Balme Library") && to.equals("University Hospital")) return 3.0;
            if (from.equals("Pentagon Hostel") && to.equals("Main Gate")) return 1.0;
            if (from.equals("Pentagon Hostel") && to.equals("University Hospital")) return 100.0;
            throw new IllegalArgumentException("unexpected route");
        };

        List<Assignment> greedyResult = new GreedyAssignment(distance).greedyAssign(requests, resources);

        assertEquals(List.of("R1", "R2"),
            greedyResult.stream().map(Assignment::getResourceId).toList());
        double totalDistance = greedyResult.stream().mapToDouble(Assignment::getDistance).sum();
        assertEquals(101.0, totalDistance);

        double alternativeDistance =
            distance.distanceBetween("Balme Library", "University Hospital")
                + distance.distanceBetween("Pentagon Hostel", "Main Gate");
        assertEquals(4.0, alternativeDistance);
        assertTrue(totalDistance > alternativeDistance);
    }
}
