package com.team.smartops;

import com.team.smartops.algorithms.graph.Dijkstra;
import com.team.smartops.algorithms.graph.Kruskal;
import com.team.smartops.algorithms.graph.Prim;
import com.team.smartops.algorithms.optimisation.OptimisationDemo;
import com.team.smartops.algorithms.search.BinarySearch;
import com.team.smartops.algorithms.search.LinearSearch;
import com.team.smartops.algorithms.sort.InsertionSort;
import com.team.smartops.algorithms.sort.MergeSort;
import com.team.smartops.algorithms.sort.QuickSort;
import com.team.smartops.algorithms.sort.SelectionSort;
import com.team.smartops.structures.DynamicArray;

public class App {

    private static AppState state = new AppState();

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> state = DataLoader.loadEverything();
                    case "2" -> runSearchSortDemo();
                    case "3" -> runGraphDemo();
                    case "4" -> OptimisationDemo.run();
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

    // =========================================================
    // SEARCH + SORT DEMO
    // =========================================================

    private static void runSearchSortDemo() {

        if (!state.dataLoaded || state.requests == null || state.requests.isEmpty()) {
            System.out.println("Data isn't loaded -- run option 1 first.");
            return;
        }

        System.out.println("\n=== SEARCH & SORT DEMO ===");

        System.out.println("Service requests loaded: " + state.requests.size());

        // -----------------------------------------------------
        // LINEAR SEARCH
        // -----------------------------------------------------

        String target = state.requests.get(0);

        String[] requestArray = state.requests.toArray(new String[0]);

        int linearIndex =
                LinearSearch.linearSearch(requestArray, target);

        System.out.println("\nLinear Search:");
        System.out.println("  Target: " + target);
        System.out.println("  Result index: " + linearIndex);

        // -----------------------------------------------------
        // BINARY SEARCH
        // -----------------------------------------------------
        // Binary search requires sorted input.
        // Therefore, we make a copy and sort the strings
        // lexicographically using Java's built-in sort only
        // for preparing the demonstration data.
        //
        // The actual binary search algorithm is still the
        // team's BinarySearch implementation.

        String[] sortedRequests = requestArray.clone();
        java.util.Arrays.sort(sortedRequests);

        String binaryTarget = sortedRequests[sortedRequests.length / 2];

        int binaryIndex =
                BinarySearch.binarySearch(sortedRequests, binaryTarget);

        System.out.println("\nBinary Search:");
        System.out.println("  Target: " + binaryTarget);
        System.out.println("  Result index: " + binaryIndex);
        System.out.println("  Precondition: input was sorted first.");

        // -----------------------------------------------------
        // PREPARE REQUEST IDs FOR SORTING
        // -----------------------------------------------------

        int[] requestIds = extractRequestIds(requestArray);

        System.out.println("\nRequest IDs before sorting:");
        printFirstValues(requestIds);

        // -----------------------------------------------------
        // INSERTION SORT
        // -----------------------------------------------------

        int[] insertionData = requestIds.clone();

        InsertionSort.insertionSort(insertionData);

        System.out.println("\nInsertion Sort:");
        printFirstValues(insertionData);

        // -----------------------------------------------------
        // MERGE SORT
        // -----------------------------------------------------

        int[] mergeData = requestIds.clone();

        MergeSort.mergeSort(
                mergeData,
                0,
                mergeData.length - 1
        );

        System.out.println("\nMerge Sort:");
        printFirstValues(mergeData);

        // -----------------------------------------------------
        // QUICK SORT
        // -----------------------------------------------------

        int[] quickData = requestIds.clone();

        QuickSort.quickSort(
                quickData,
                0,
                quickData.length - 1
        );

        System.out.println("\nQuick Sort:");
        printFirstValues(quickData);

        // -----------------------------------------------------
        // SELECTION SORT
        // -----------------------------------------------------

        int[] selectionData = requestIds.clone();

        SelectionSort.selectionSort(selectionData);

        System.out.println("\nSelection Sort:");
        printFirstValues(selectionData);

        System.out.println("\nSearch and sort demo completed successfully.");
    }

    /**
     * Extracts the request ID from strings such as:
     *
     * "123 - Electrical | Urgency: High | Status: Pending"
     */
    private static int[] extractRequestIds(String[] requests) {

        int[] ids = new int[requests.length];

        for (int i = 0; i < requests.length; i++) {

            String request = requests[i];

            int separator = request.indexOf(" - ");

            if (separator == -1) {
                throw new IllegalArgumentException(
                        "Unexpected service request format: " + request
                );
            }

            String idText = request.substring(0, separator).trim();

            ids[i] = Integer.parseInt(idText);
        }

        return ids;
    }

    /**
     * Prints a small portion of an array so that the
     * console isn't flooded with all 301 values.
     */
    private static void printFirstValues(int[] values) {

        int amountToShow = Math.min(values.length, 10);

        System.out.print("  ");

        for (int i = 0; i < amountToShow; i++) {
            System.out.print(values[i]);

            if (i < amountToShow - 1) {
                System.out.print(", ");
            }
        }

        if (values.length > amountToShow) {
            System.out.print(", ...");
        }

        System.out.println();
    }

    // =========================================================
    // GRAPH DEMO
    // =========================================================

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

            System.out.println(
                    "  " + node + ": " + state.graph.getName(node)
            );
        }

        // ---------------------------------------------------------
        // 2. DFS
        // ---------------------------------------------------------

        System.out.println("\nDFS from node 0:");

        DynamicArray<Integer> dfsResult = state.graph.dfs(0);

        for (int i = 0; i < dfsResult.size(); i++) {
            int node = dfsResult.get(i);

            System.out.println(
                    "  " + node + ": " + state.graph.getName(node)
            );
        }

        // ---------------------------------------------------------
        // 3. Dijkstra
        // ---------------------------------------------------------

        System.out.println("\nDijkstra shortest paths from node 0:");

        Dijkstra.Result dijkstraResult =
                Dijkstra.dijkstra(state.graph, 0);

        for (int node = 0; node < state.graph.getNumNodes(); node++) {

            double distance = dijkstraResult.distanceTo(node);

            if (Double.isInfinite(distance)) {

                System.out.println(
                        "  " + node + ": "
                                + state.graph.getName(node)
                                + " -> unreachable"
                );

            } else {

                System.out.println(
                        "  " + node + ": "
                                + state.graph.getName(node)
                                + " -> distance = "
                                + distance
                );
            }
        }

        // ---------------------------------------------------------
        // 4. Build adjacency matrix
        // ---------------------------------------------------------

        int[][] adjacencyMatrix = buildIntegerAdjacencyMatrix();

        // ---------------------------------------------------------
        // 5. Check connectivity before MST
        // ---------------------------------------------------------

        if (bfsResult.size() < state.graph.getNumNodes()) {

            System.out.println("\nPrim/Kruskal:");
            System.out.println(
                    "  MST cannot currently be generated because "
                            + "the graph is disconnected."
            );

            System.out.println(
                    "  Connected nodes from node 0: "
                            + bfsResult.size()
                            + "/"
                            + state.graph.getNumNodes()
            );

            System.out.println(
                    "  Add more roads/edges later so the campus "
                            + "graph becomes connected."
            );

            return;
        }

        // ---------------------------------------------------------
        // 6. Prim
        // ---------------------------------------------------------

        System.out.println("\nPrim Minimum Spanning Tree:");

        DynamicArray<int[]> primEdges =
                Prim.buildMST(adjacencyMatrix, 0);

        for (int i = 0; i < primEdges.size(); i++) {

            int[] edge = primEdges.get(i);

            System.out.println(
                    "  " + state.graph.getName(edge[0])
                            + " -- "
                            + state.graph.getName(edge[1])
                            + " (weight: "
                            + state.graph.getWeight(
                                    edge[0],
                                    edge[1])
                            + ")"
            );
        }

        // ---------------------------------------------------------
        // 7. Kruskal
        // ---------------------------------------------------------

        System.out.println("\nKruskal Minimum Spanning Tree:");

        DynamicArray<int[]> kruskalEdges =
                Kruskal.buildMST(adjacencyMatrix);

        for (int i = 0; i < kruskalEdges.size(); i++) {

            int[] edge = kruskalEdges.get(i);

            System.out.println(
                    "  " + state.graph.getName(edge[0])
                            + " -- "
                            + state.graph.getName(edge[1])
                            + " (weight: "
                            + state.graph.getWeight(
                                    edge[0],
                                    edge[1])
                            + ")"
            );
        }
    }

    /**
     * Converts the project's Graph representation into
     * the int[][] adjacency matrix expected by Prim/Kruskal.
     */
    private static int[][] buildIntegerAdjacencyMatrix() {

        int numberOfNodes = state.graph.getNumNodes();

        int[][] matrix = new int[numberOfNodes][numberOfNodes];

        for (int from = 0; from < numberOfNodes; from++) {

            for (int to = 0; to < numberOfNodes; to++) {

                double weight =
                        state.graph.getWeight(from, to);

                if (weight != 0) {
                    matrix[from][to] =
                            (int) Math.round(weight);
                } else {
                    matrix[from][to] = 0;
                }
            }
        }

        return matrix;
    }

    // =========================================================
    // MENU
    // =========================================================

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