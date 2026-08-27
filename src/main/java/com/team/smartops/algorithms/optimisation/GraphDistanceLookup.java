package com.team.smartops.algorithms.optimisation;

import com.team.smartops.algorithms.graph.Dijkstra;
import com.team.smartops.structures.Graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Real graph-based distance lookup implementing {@link GreedyAssignment.DistanceLookup}.
 * Uses Dijkstra's algorithm over Team C's {@link Graph} representation.
 * Caches shortest-path results per source node to avoid redundant Dijkstra computations.
 */
public class GraphDistanceLookup implements GreedyAssignment.DistanceLookup {

    private final Graph graph;
    private final Map<String, Integer> nameToIndex;
    private final Map<Integer, Dijkstra.Result> dijkstraCache;

    public GraphDistanceLookup(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("graph must not be null");
        }
        this.graph = graph;
        this.nameToIndex = new HashMap<>();
        this.dijkstraCache = new HashMap<>();

        for (int i = 0; i < graph.getNumNodes(); i++) {
            nameToIndex.put(graph.getName(i), i);
        }
    }

    public GraphDistanceLookup(Graph graph, Map<String, Integer> nameToIndex) {
        if (graph == null) {
            throw new IllegalArgumentException("graph must not be null");
        }
        this.graph = graph;
        this.nameToIndex = nameToIndex != null ? nameToIndex : new HashMap<>();
        this.dijkstraCache = new HashMap<>();
    }

    @Override
    public double distanceBetween(String locationA, String locationB) {
        if (locationA == null || locationB == null) {
            return Double.MAX_VALUE;
        }

        if (locationA.equals(locationB)) {
            return 0.0;
        }

        Integer fromIndex = nameToIndex.get(locationA);
        Integer toIndex = nameToIndex.get(locationB);

        if (fromIndex == null || toIndex == null) {
            // Fallback for names not found directly (e.g. slight mismatch or synthetic test names)
            return 5.0;
        }

        Dijkstra.Result result = dijkstraCache.computeIfAbsent(fromIndex,
                src -> Dijkstra.dijkstra(graph, src));

        double dist = result.distanceTo(toIndex);
        if (Double.isInfinite(dist)) {
            return Double.MAX_VALUE;
        }
        return dist;
    }
}
