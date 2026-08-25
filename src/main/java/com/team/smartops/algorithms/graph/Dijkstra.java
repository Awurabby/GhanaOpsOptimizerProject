package com.team.smartops.algorithms.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.team.smartops.structures.Graph;
import com.team.smartops.structures.MyPriority_Heap;

/**
 * OWNER: Team E.
 * Shortest path -- produce a distance table + predecessor path.
 */
public class Dijkstra {
    public static final double INFINITY = Double.POSITIVE_INFINITY;

    /** A directed weighted edge in the graph. Weights must be non-negative. */
    public static final class Edge {
        private final int to;
        private final double weight;

        public Edge(int to, double weight) {
            if (to < 0) {
                throw new IllegalArgumentException("vertex id cannot be negative");
            }
            if (Double.isNaN(weight) || weight < 0.0) {
                throw new IllegalArgumentException("Dijkstra requires non-negative weights");
            }
            this.to = to;
            this.weight = weight;
        }

        public int to() {
            return to;
        }

        public double weight() {
            return weight;
        }
    }

    /** Minimal weighted adjacency-list graph used until Team C's Graph is wired in. */
    public static final class WeightedGraph {
        private final List<List<Edge>> adjacency;

        public WeightedGraph(int vertexCount) {
            if (vertexCount < 0) {
                throw new IllegalArgumentException("vertex count cannot be negative");
            }
            adjacency = new ArrayList<>(vertexCount);
            for (int vertex = 0; vertex < vertexCount; vertex++) {
                adjacency.add(new ArrayList<>());
            }
        }

        public int vertexCount() {
            return adjacency.size();
        }

        public void addEdge(int from, int to, double weight) {
            validateVertex(from);
            validateVertex(to);
            adjacency.get(from).add(new Edge(to, weight));
        }

        public void addUndirectedEdge(int first, int second, double weight) {
            addEdge(first, second, weight);
            addEdge(second, first, weight);
        }

        private void validateVertex(int vertex) {
            if (vertex < 0 || vertex >= adjacency.size()) {
                throw new IndexOutOfBoundsException("vertex id: " + vertex);
            }
        }
    }

    public static final class Result {
        private final double[] distances;
        private final int[] predecessors;

        private Result(double[] distances, int[] predecessors) {
            this.distances = distances;
            this.predecessors = predecessors;
        }

        public double distanceTo(int vertex) {
            checkVertex(vertex);
            return distances[vertex];
        }

        public int predecessorOf(int vertex) {
            checkVertex(vertex);
            return predecessors[vertex];
        }

        public List<Double> distances() {
            List<Double> copy = new ArrayList<>(distances.length);
            for (double distance : distances) {
                copy.add(distance);
            }
            return Collections.unmodifiableList(copy);
        }

        public List<Integer> predecessors() {
            List<Integer> copy = new ArrayList<>(predecessors.length);
            for (int predecessor : predecessors) {
                copy.add(predecessor);
            }
            return Collections.unmodifiableList(copy);
        }

        public List<Integer> pathTo(int destination) {
            checkVertex(destination);
            if (Double.isInfinite(distances[destination])) {
                return Collections.emptyList();
            }

            List<Integer> reversed = new ArrayList<>();
            for (int current = destination; current != -1; current = predecessors[current]) {
                reversed.add(current);
            }
            Collections.reverse(reversed);
            return Collections.unmodifiableList(reversed);
        }

        private void checkVertex(int vertex) {
            if (vertex < 0 || vertex >= distances.length) {
                throw new IndexOutOfBoundsException("vertex id: " + vertex);
            }
        }
    }

    public static Result dijkstra(WeightedGraph graph, int source) {
        if (graph == null) {
            throw new IllegalArgumentException("graph cannot be null");
        }
        if (source < 0 || source >= graph.vertexCount()) {
            throw new IndexOutOfBoundsException("source vertex: " + source);
        }

        double[] distances = new double[graph.vertexCount()];
        int[] predecessors = new int[graph.vertexCount()];
        Arrays.fill(distances, INFINITY);
        Arrays.fill(predecessors, -1);
        distances[source] = 0.0;

        MyPriority_Heap<QueueEntry> frontier = new MyPriority_Heap<>();
        frontier.insert(new QueueEntry(source, 0.0));
        while (!frontier.isEmpty()) {
            QueueEntry current = frontier.extractMin();
            if (current.distance > distances[current.vertex]) {
                continue;
            }

            for (Edge edge : graph.adjacency.get(current.vertex)) {
                double candidate = current.distance + edge.weight();
                if (candidate < distances[edge.to()]) {
                    distances[edge.to()] = candidate;
                    predecessors[edge.to()] = current.vertex;
                    frontier.insert(new QueueEntry(edge.to(), candidate));
                }
            }
        }
        return new Result(distances, predecessors);
    }

    /**
     * Computes shortest paths from source using Team C's weighted Graph.
     * Graph edges are undirected; all weights must be non-negative.
     */
    public static Result dijkstra(Graph graph, int source) {
        if (graph == null) {
            throw new IllegalArgumentException("graph cannot be null");
        }
        if (source < 0 || source >= graph.getNumNodes()) {
            throw new IndexOutOfBoundsException("source vertex: " + source);
        }

        double[] distances = new double[graph.getNumNodes()];
        int[] predecessors = new int[graph.getNumNodes()];
        Arrays.fill(distances, INFINITY);
        Arrays.fill(predecessors, -1);
        distances[source] = 0.0;

        MyPriority_Heap<QueueEntry> frontier = new MyPriority_Heap<>();
        frontier.insert(new QueueEntry(source, 0.0));
        while (!frontier.isEmpty()) {
            QueueEntry current = frontier.extractMin();
            if (current.distance > distances[current.vertex]) {
                continue;
            }

            for (Graph.Edge edge : graph.getNeighbours(current.vertex)) {
                if (Double.isNaN(edge.weight) || edge.weight < 0.0) {
                    throw new IllegalArgumentException("Dijkstra requires non-negative weights");
                }
                double candidate = current.distance + edge.weight;
                if (candidate < distances[edge.destination]) {
                    distances[edge.destination] = candidate;
                    predecessors[edge.destination] = current.vertex;
                    frontier.insert(new QueueEntry(edge.destination, candidate));
                }
            }
        }
        return new Result(distances, predecessors);
    }

    private static final class QueueEntry implements Comparable<QueueEntry> {
        private final int vertex;
        private final double distance;

        private QueueEntry(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(QueueEntry other) {
            return Double.compare(distance, other.distance);
        }
    }
}
