package com.team.smartops;

import java.util.Scanner;

import com.team.smartops.algorithms.graph.Dijkstra;
import com.team.smartops.algorithms.graph.Kruskal;
import com.team.smartops.algorithms.graph.Prim;
import com.team.smartops.structures.DynamicArray;

public class App {

    private static AppState state = new AppState();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> state = DataLoader.loadEverything();
                    case "2" -> System.out.println("TODO: search/sort demo");
                    case "3" -> runGraphDemo();
                    case "4" -> System.out.println("TODO: optimisation demo");
                    case "5" -> System.out.println("TODO: performance demo");
                    case "0" -> running = false;
                    default -> System.out.println("Unknown option, try again.");
                }
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void runGraphDemo() {
        if (!state.graphLoaded) {
            System.out.println("Graph isn't loaded -- run option 1 first.");
            return;
        }
        System.out.println("\n=== GRAPH ROUTING DEMO ===");

        // ---------------------------------------------------------
        // 1. BFS
        // ---------------------------------------------------------
        System.out.println("\nBFS from node 0:");

        DynamicArray<Integer> bfsResult = state.graph.bfs(0);

        for (int i = 0; i < bfsResult.size(); i++) {
            int node = bfsResult.get(i);
            System.out.println("  " + node + ": " + state.graph.getName(node));
        }

        // ---------------------------------------------------------
        // 2. DFS
        // ---------------------------------------------------------
        System.out.println("\nDFS from node 0:");

        DynamicArray<Integer> dfsResult = state.graph.dfs(0);

        for (int i = 0; i < dfsResult.size(); i++) {
            int node = dfsResult.get(i);
            System.out.println("  " + node + ": " + state.graph.getName(node));
        }

        // ---------------------------------------------------------
        // 3. Dijkstra
        // ---------------------------------------------------------
        System.out.println("\nDijkstra shortest paths from node 0:");

        Dijkstra.Result dijkstraResult = Dijkstra.dijkstra(state.graph, 0);

        for (int node = 0; node < state.graph.getNumNodes(); node++) {
            double distance = dijkstraResult.distanceTo(node);

            if (Double.isInfinite(distance)) {
                System.out.println(
                        "  " + node + ": " + state.graph.getName(node)
                                + " -> unreachable");
            } else {
                System.out.println(
                        "  " + node + ": " + state.graph.getName(node)
                                + " -> distance = " + distance);
            }
        }
        // ---------------------------------------------------------
        // 4. Build adjacency matrix for Prim/Kruskal
        // ---------------------------------------------------------
        int[][] adjacencyMatrix = buildIntegerAdjacencyMatrix();

        // Check whether the graph is connected before attempting MST.
        if (bfsResult.size() < state.graph.getNumNodes()) {
            System.out.println("\nPrim/Kruskal:");
            System.out.println(
                    "  MST cannot currently be generated because the graph "
                            + "is disconnected.");
            System.out.println(
                    "  Connected nodes from node 0: "
                            + bfsResult.size() + "/" + state.graph.getNumNodes());
            System.out.println(
                    "  Add more roads/edges later so the campus graph becomes connected.");
            return;
        }
        // ---------------------------------------------------------
        // 5. Prim
        // ---------------------------------------------------------
        System.out.println("\nPrim Minimum Spanning Tree:");

        DynamicArray<int[]> primEdges = Prim.buildMST(adjacencyMatrix, 0);

        for (int i = 0; i < primEdges.size(); i++) {
            int[] edge = primEdges.get(i);

            System.out.println(
                    "  " + state.graph.getName(edge[0])
                            + " -- "
                            + state.graph.getName(edge[1])
                            + " (weight: "
                            + state.graph.getWeight(edge[0], edge[1])
                            + ")");
        }
        // ---------------------------------------------------------
        // 6. Kruskal
        // ---------------------------------------------------------
        System.out.println("\nKruskal Minimum Spanning Tree:");

        DynamicArray<int[]> kruskalEdges = Kruskal.buildMST(adjacencyMatrix);

        for (int i = 0; i < kruskalEdges.size(); i++) {
            int[] edge = kruskalEdges.get(i);

            System.out.println(
                    "  " + state.graph.getName(edge[0])
                            + " -- "
                            + state.graph.getName(edge[1])
                            + " (weight: "
                            + state.graph.getWeight(edge[0], edge[1])
                            + ")");
        }
    }

    /**
     * Converts the project's Graph representation into the int[][]
     * adjacency matrix expected by Prim and Kruskal.
     */
    private static int[][] buildIntegerAdjacencyMatrix() {
        int numberOfNodes = state.graph.getNumNodes();

        int[][] matrix = new int[numberOfNodes][numberOfNodes];

        for (int from = 0; from < numberOfNodes; from++) {
            for (int to = 0; to < numberOfNodes; to++) {

                double weight = state.graph.getWeight(from, to);

                if (weight != 0) {
                    matrix[from][to] = (int) Math.round(weight);
                } else {
                    matrix[from][to] = 0;
                }
            }
        }

        return matrix;

        // System.out.println("BFS from node 0:");
        // DynamicArray<Integer> bfsResult = state.graph.bfs(0);
        // for (int i = 0; i < bfsResult.size(); i++) {
        // int node = bfsResult.get(i);
        // System.out.println(" " + node + ": " + state.graph.getName(node));
        // }

        // System.out.println("DFS from node 0:");
        // DynamicArray<Integer> dfsResult = state.graph.dfs(0);
        // for (int i = 0; i < dfsResult.size(); i++) {
        // int node = dfsResult.get(i);
        // System.out.println(" " + node + ": " + state.graph.getName(node));
        // }
    }

    private static void printMenu() {
        System.out.println("""

                === Ghana Smart Service Operations Optimizer ===
                1. Load data from database
                2. Run search/sort demo
                3. Run graph routing demo
                4. Run optimisation demo (greedy + DP)
                5. Run performance experiment
                0. Exit
                Choose an option:""");
    }
}