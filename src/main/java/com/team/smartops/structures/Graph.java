package com.team.smartops.structures;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class Graph {

    // ── INNER CLASS: weighted edge ─────────────────────────────────────────────
    public static class Edge {
        public int    destination;
        public double weight;       // distance or travel time in minutes

        public Edge(int destination, double weight) {
            this.destination = destination;
            this.weight      = weight;
        }
    }

    // ── FIELDS ─────────────────────────────────────────────────────────────────
    private int              numNodes;
    private String[]         locationNames; // human-readable campus names
    private ArrayList<Edge>[] adjList;      // adjacency list
    private double[][]        adjMatrix;    // adjacency matrix

    // ── CONSTRUCTOR ────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public Graph(int numNodes, String[] locationNames) {
        this.numNodes      = numNodes;
        this.locationNames = locationNames;

        // initialise adjacency list — one empty list per node
        adjList = new ArrayList[numNodes];
        for (int i = 0; i < numNodes; i++) {
            adjList[i] = new ArrayList<>();
        }

        // initialise adjacency matrix — 0 means no edge
        adjMatrix = new double[numNodes][numNodes];
    }

    // ── ADD EDGE ───────────────────────────────────────────────────────────────
    /**
     * addEdge - adds an undirected weighted edge between two campus locations.
     * Updates BOTH adjacency list and adjacency matrix.
     *
     * Pseudocode:
     *   ADD_EDGE(from, to, weight):
     *     adjList[from].add((to, weight))
     *     adjList[to].add((from, weight))
     *     adjMatrix[from][to] <- weight
     *     adjMatrix[to][from] <- weight
     */
    public void addEdge(int from, int to, double weight) {
        adjList[from].add(new Edge(to, weight));
        adjList[to].add(new Edge(from, weight));
        adjMatrix[from][to] = weight;
        adjMatrix[to][from] = weight;
    }

    // ── QUERY METHODS ──────────────────────────────────────────────────────────
    public ArrayList<Edge> getNeighbours(int node) { return adjList[node]; }
    public boolean hasEdge(int from, int to)       { return adjMatrix[from][to] != 0; }
    public double  getWeight(int from, int to)     { return adjMatrix[from][to]; }
    public int     getNumNodes()                   { return numNodes; }
    public String  getName(int node)               { return locationNames[node]; }

    // ── BFS ────────────────────────────────────────────────────────────────────
    /**
     * bfs - Breadth First Search from a starting campus location.
     * Visits all reachable locations level by level (closest first).
     * Uses a QUEUE — FIFO ensures level-by-level order.
     *
     * Pseudocode:
     *   BFS(start):
     *     visited[start] <- true
     *     enqueue(queue, start)
     *     while queue not empty:
     *       node <- dequeue
     *       result.add(node)
     *       for each neighbour of node:
     *         if not visited: visited[neighbour] <- true; enqueue
     *
     * @param start starting location index
     * @return list of visited nodes in BFS order
     */
    public List<Integer> bfs(int start) {
        boolean[]     visited = new boolean[numNodes];
        List<Integer> result  = new ArrayList<>();
        Queue<Integer> queue  = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        System.out.println("\nBFS from " + locationNames[start] + ":");
        System.out.printf("%-6s %-25s %-40s%n", "Step", "Visit", "Queue after");
        System.out.println("-".repeat(72));

        int step = 1;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            StringBuilder queueState = new StringBuilder("[");
            for (Edge edge : adjList[node]) {
                if (!visited[edge.destination]) {
                    visited[edge.destination] = true;
                    queue.add(edge.destination);
                }
            }
            for (Integer q : queue) queueState.append(locationNames[q]).append(" ");
            queueState.append("]");

            System.out.printf("%-6d %-25s %-40s%n",
                step++, locationNames[node], queueState);
        }
        return result;
    }

    // ── DFS ────────────────────────────────────────────────────────────────────
    /**
     * dfs - Depth First Search from a starting campus location.
     * Goes as deep as possible before backtracking.
     * Uses RECURSION (implicit call stack).
     *
     * Pseudocode:
     *   DFS(start):
     *     visited[start] <- true
     *     result.add(start)
     *     for each neighbour:
     *       if not visited: DFS(neighbour)
     *
     * @param start starting location index
     * @return list of visited nodes in DFS order
     */
    public List<Integer> dfs(int start) {
        boolean[]     visited = new boolean[numNodes];
        List<Integer> result  = new ArrayList<>();

        System.out.println("\nDFS from " + locationNames[start] + ":");
        dfsVisit(start, visited, result, 1);
        return result;
    }

    private void dfsVisit(int node, boolean[] visited,
                           List<Integer> result, int step) {
        visited[node] = true;
        result.add(node);
        System.out.println("Step " + step + ": Visit " + locationNames[node]);

        for (Edge edge : adjList[node]) {
            if (!visited[edge.destination]) {
                dfsVisit(edge.destination, visited, result, step + 1);
            }
        }
    }

    // ── PRINT METHODS ─────────────────────────────────────────────────────────
    public void printAdjList() {
        System.out.println("\nAdjacency List:");
        for (int i = 0; i < numNodes; i++) {
            System.out.printf("%-20s: ", locationNames[i]);
            for (Edge e : adjList[i]) {
                System.out.printf("->(%s, %.1f) ", locationNames[e.destination], e.weight);
            }
            System.out.println();
        }
    }

    public void printAdjMatrix() {
        System.out.println("\nAdjacency Matrix (distance in minutes):");
        System.out.printf("%-20s", "");
        for (String name : locationNames)
            System.out.printf("%-12s", name.length() > 10 ? name.substring(0,10) : name);
        System.out.println();

        for (int i = 0; i < numNodes; i++) {
            System.out.printf("%-20s", locationNames[i].length() > 18
                ? locationNames[i].substring(0,18) : locationNames[i]);
            for (int j = 0; j < numNodes; j++) {
                System.out.printf("%-12.1f", adjMatrix[i][j]);
            }
            System.out.println();
        }
    }

    // ── DEMO ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== UG Campus Graph Demo ===");

        String[] locations = {
            "Main Gate", "Balme Library", "SRC",
            "Commonwealth", "Engineering Block"
        };

        Graph g = new Graph(5, locations);
        g.addEdge(0, 1, 5.0);   // Main Gate  -- Balme Library  : 5 min walk
        g.addEdge(1, 2, 3.0);   // Balme      -- SRC            : 3 min
        g.addEdge(2, 3, 4.0);   // SRC        -- Commonwealth   : 4 min
        g.addEdge(3, 4, 6.0);   // Commonwealth-- Engineering   : 6 min
        g.addEdge(0, 4, 8.0);   // Main Gate  -- Engineering    : 8 min

        g.printAdjList();
        g.printAdjMatrix();
        g.bfs(0);
        g.dfs(0);
    }
}
