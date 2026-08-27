package com.team.smartops.model;

import com.team.smartops.algorithms.optimisation.GreedyAssignment;
import com.team.smartops.algorithms.optimisation.GraphDistanceLookup;
import com.team.smartops.structures.Graph;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelAdaptersTest {

    @Test
    void toOptServiceRequest_convertsDbModelWithResolvedLocationNames() {
        ServiceRequest dbReq = new ServiceRequest(
                101, 1, 2, "Hostel Maintenance", "High",
                "2026-07-01 08:30", "2026-07-01 18:30", "Pending");

        Map<Integer, String> names = Map.of(1, "Balme Library", 2, "Pentagon Hostel");

        com.team.smartops.algorithms.optimisation.ServiceRequest optReq =
                ModelAdapters.toOptServiceRequest(dbReq, names);

        assertEquals("101", optReq.getRequestId());
        assertEquals("Balme Library", optReq.getSource());
        assertEquals("Pentagon Hostel", optReq.getDestination());
        assertEquals(3, optReq.getUrgency());
        assertEquals(4, optReq.getCost());
        assertEquals(30, optReq.getValue());
    }

    @Test
    void toOptPendingRequests_filtersOutNonPendingRequests() {
        ServiceRequest pending = new ServiceRequest(
                1, 1, 2, "Plumbing", "Critical", "time", "dead", "Pending");
        ServiceRequest resolved = new ServiceRequest(
                2, 1, 2, "Electrical", "Low", "time", "dead", "Resolved");

        Map<Integer, String> names = Map.of(1, "Balme Library", 2, "Pentagon Hostel");

        List<com.team.smartops.algorithms.optimisation.ServiceRequest> result =
                ModelAdapters.toOptPendingRequests(List.of(pending, resolved), names);

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getRequestId());
    }

    @Test
    void toOptResource_convertsDbResource() {
        Resource dbRes = new Resource(5, "van", 1, 4, "Available");
        Map<Integer, String> names = Map.of(1, "Balme Library");

        com.team.smartops.algorithms.optimisation.Resource optRes =
                ModelAdapters.toOptResource(dbRes, names);

        assertEquals("5", optRes.getResourceId());
        assertEquals("van", optRes.getType());
        assertEquals("Balme Library", optRes.getHomeLocation());
        assertTrue(optRes.isAvailable());
    }

    @Test
    void graphDistanceLookup_computesShortestPathDistance() {
        String[] nodes = {"Balme Library", "Pentagon Hostel", "University Hospital"};
        Graph graph = new Graph(3, nodes);
        graph.addEdge(0, 1, 1.5);
        graph.addEdge(1, 2, 2.0);

        GraphDistanceLookup lookup = new GraphDistanceLookup(graph);

        assertEquals(0.0, lookup.distanceBetween("Balme Library", "Balme Library"));
        assertEquals(1.5, lookup.distanceBetween("Balme Library", "Pentagon Hostel"));
        assertEquals(3.5, lookup.distanceBetween("Balme Library", "University Hospital"));
    }
}
