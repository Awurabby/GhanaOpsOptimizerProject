package com.team.smartops.structures;

/**
 * OWNER: Team C (C4).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Uses only MyLinkedList (C1) and MyQueue (C2) for internal storage/traversal.
 */
public class Graph {

    public static class Edge {
        public int destination;
        public double weight;

        public Edge(int destination, double weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    private final int numNodes;
    private final String[] locationNames;
    private final MyLinkedList<Edge>[] adjList;
    private final double[][] adjMatrix;

    @SuppressWarnings("unchecked")
    public Graph(int numNodes, String[] locationNames) {
        if (numNodes <= 0) {
            throw new IllegalArgumentException("numNodes must be positive");
        }
        if (locationNames == null || locationNames.length != numNodes) {
            throw new IllegalArgumentException("locationNames must be non-null and match numNodes");
        }
        this.numNodes = numNodes;
        this.locationNames = locationNames;
        this.adjList = new MyLinkedList[numNodes];
        for (int i = 0; i < numNodes; i++) {
            adjList[i] = new MyLinkedList<>();
        }
        this.adjMatrix = new double[numNodes][numNodes];
    }

    private void validateNode(int node) {
        if (node < 0 || node >= numNodes) {
            throw new IndexOutOfBoundsException("node " + node + " out of bounds for " + numNodes + " nodes");
        }
    }

    public void addEdge(int from, int to, double weight) {
        validateNode(from);
        validateNode(to);
        adjList[from].addLast(new Edge(to, weight));
        adjList[to].addLast(new Edge(from, weight));
        adjMatrix[from][to] = weight;
        adjMatrix[to][from] = weight;
    }

    public MyLinkedList<Edge> getNeighbours(int node) {
        validateNode(node);
        return adjList[node];
    }

    public boolean hasEdge(int from, int to) {
        validateNode(from);
        validateNode(to);
        return adjMatrix[from][to] != 0;
    }

    public double getWeight(int from, int to) {
        validateNode(from);
        validateNode(to);
        return adjMatrix[from][to];
    }

    public int getNumNodes() {
        return numNodes;
    }

    public String getName(int node) {
        validateNode(node);
        return locationNames[node];
    }

    /** BFS using MyQueue instead of java.util.Queue/LinkedList. */
    public DynamicArray<Integer> bfs(int start) {
        validateNode(start);
        boolean[] visited = new boolean[numNodes];
        DynamicArray<Integer> result = new DynamicArray<>();
        MyQueue<Integer> queue = new MyQueue<>();

        visited[start] = true;
        queue.enqueue(start);

        while (!queue.isEmpty()) {
            int node = queue.dequeue();
            result.insert(node);
            for (Edge edge : adjList[node]) {
                if (!visited[edge.destination]) {
                    visited[edge.destination] = true;
                    queue.enqueue(edge.destination);
                }
            }
        }
        return result;
    }

    /** DFS using recursion (implicit call stack) -- no built-in structure needed. */
    public DynamicArray<Integer> dfs(int start) {
        validateNode(start);
        boolean[] visited = new boolean[numNodes];
        DynamicArray<Integer> result = new DynamicArray<>();
        dfsVisit(start, visited, result);
        return result;
    }

    private void dfsVisit(int node, boolean[] visited, DynamicArray<Integer> result) {
        visited[node] = true;
        result.insert(node);
        for (Edge edge : adjList[node]) {
            if (!visited[edge.destination]) {
                dfsVisit(edge.destination, visited, result);
            }
        }
    }
}