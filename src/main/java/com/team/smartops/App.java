package com.team.smartops;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.team.smartops.algorithms.graph.Dijkstra;
import com.team.smartops.algorithms.graph.Kruskal;
import com.team.smartops.algorithms.graph.Prim;
import com.team.smartops.algorithms.optimisation.*;
import com.team.smartops.algorithms.search.BinarySearch;
import com.team.smartops.algorithms.search.LinearSearch;
import com.team.smartops.algorithms.sort.InsertionSort;
import com.team.smartops.algorithms.sort.MergeSort;
import com.team.smartops.algorithms.sort.QuickSort;
import com.team.smartops.algorithms.sort.SelectionSort;
import com.team.smartops.model.ModelAdapters;
import com.team.smartops.model.ServiceRequest;
import com.team.smartops.performance.ExperimentRunner;
import com.team.smartops.performance.OptimisationTimingExperiment;
import com.team.smartops.performance.Timer;
import com.team.smartops.structures.DynamicArray;
import com.team.smartops.ui.ConsoleMenu;

public class App {

    private static AppState state = new AppState();
    private static Scanner scanner = new Scanner(System.in);
    private static final int ROUTE_PENALTY = 8; // derived from index number 22152953: last two digits 5+3=8
    private static final int PRIORITY_WEIGHT = 3; // derived from index number

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            ConsoleMenu.printMainMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> state = DataLoader.loadEverything();
                    case "2" -> runSearchSortDemo();
                    case "3" -> runGraphDemo();
                    case "4" -> runOptimisationDemo();
                    case "5" -> runPerformanceDemo();
                    case "0" -> {
                        System.out.println("Exiting Ghana Smart Service Operations Optimizer. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Unknown option, try again.");
                }
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
        scanner.close();
    }

    // =========================================================
    // SEARCH & SORT DEMO (Team D)
    // =========================================================

    private static void runSearchSortDemo() {
        if (!state.dataLoaded || state.requests == null || state.requests.isEmpty()) {
            System.out.println("Data isn't loaded -- run option 1 first.");
            return;
        }

        System.out.println("""

            --- Search & Sort ---
            1. Search for a specific request by ID
            2. Show next request to dispatch (by urgency)
            3. Search for a location by name
            4. Run full sort algorithm comparison
            0. Back to main menu
            Choose an option:""");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> searchRequestById();
            case "2" -> showNextRequestToDispatch();
            case "3" -> searchLocationByName();
            case "4" -> runFullSortComparison();
            case "0" -> {}
            default -> System.out.println("Unknown option.");
        }
    }

    private static void searchRequestById() {
        System.out.print("Enter request ID to search for: ");
        String input = scanner.nextLine().trim();

        int targetId;
        try {
            targetId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid request ID: " + input);
            return;
        }

        Integer[] requestIds = new Integer[state.requests.size()];
        for (int i = 0; i < state.requests.size(); i++) {
            requestIds[i] = state.requests.get(i).getRequestId();
        }

        long time = Timer.timeInNanos(() -> LinearSearch.linearSearch(requestIds, targetId));
        int index = LinearSearch.linearSearch(requestIds, targetId);

        if (index == -1) {
            System.out.println("No request found with ID " + targetId + ".");
        } else {
            ServiceRequest found = state.requests.get(index);
            System.out.println("Found: " + found);
            System.out.println("(Linear search took " + time + " ns)");
        }
    }

    private static void showNextRequestToDispatch() {
        ServiceRequest best = null;
        int bestScore = -1;

        for (ServiceRequest req : state.requests) {
            if (req.isPending()) {
                int score = req.getUrgencyScore() * PRIORITY_WEIGHT;
                if (score > bestScore) {
                    bestScore = score;
                    best = req;
                }
            }
        }

        if (best == null) {
            System.out.println("No pending requests found.");
        } else {
            System.out.println("Next request to dispatch (highest urgency score " + bestScore + "): " + best);
        }
    }

    private static void searchLocationByName() {
        if (state.graph == null) {
            System.out.println("Graph isn't loaded -- run option 1 first.");
            return;
        }

        System.out.println("Available locations (" + state.graph.getNumNodes() + " nodes):");
        int show = Math.min(15, state.graph.getNumNodes());
        for (int i = 0; i < show; i++) {
            System.out.println("  " + state.graph.getName(i));
        }
        if (state.graph.getNumNodes() > show) {
            System.out.println("  ... (" + (state.graph.getNumNodes() - show) + " more locations)");
        }

        System.out.print("\nEnter a location name to search for: ");
        String input = scanner.nextLine().trim();

        int index = findLocationIndexByName(input);
        if (index == -1) {
            System.out.println("Location not found: " + input);
            return;
        }
        System.out.println("Found: " + state.graph.getName(index) + " (node index " + index + ")");
    }

    private static void runFullSortComparison() {
        int[] requestIds = new int[state.requests.size()];
        for (int i = 0; i < state.requests.size(); i++) {
            requestIds[i] = state.requests.get(i).getRequestId();
        }

        System.out.println("\nRequest IDs before sorting (sample):");
        printFirstValues(requestIds);

        int[] insertionData = requestIds.clone();
        InsertionSort.insertionSort(insertionData);
        System.out.println("\nInsertion Sort:");
        printFirstValues(insertionData);

        int[] mergeData = requestIds.clone();
        MergeSort.mergeSort(mergeData, 0, mergeData.length - 1);
        System.out.println("\nMerge Sort:");
        printFirstValues(mergeData);

        int[] quickData = requestIds.clone();
        QuickSort.quickSort(quickData, 0, quickData.length - 1);
        System.out.println("\nQuick Sort:");
        printFirstValues(quickData);

        int[] selectionData = requestIds.clone();
        SelectionSort.selectionSort(selectionData);
        System.out.println("\nSelection Sort:");
        printFirstValues(selectionData);

        // Binary Search on sorted data
        Integer[] sortedIds = new Integer[insertionData.length];
        for (int i = 0; i < insertionData.length; i++) sortedIds[i] = insertionData[i];
        int target = sortedIds[0];
        int binaryIndex = BinarySearch.binarySearch(sortedIds, target);
        System.out.println("\nBinary Search for ID " + target + " found at sorted index: " + binaryIndex);
    }

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
    // GRAPH ROUTING DEMO (Team E)
    // =========================================================

    private static void runGraphDemo() {
        if (!state.graphLoaded || state.graph == null) {
            System.out.println("Graph isn't loaded -- run option 1 first.");
            return;
        }

        ConsoleMenu.printHeader("CAMPUS GRAPH ROUTING (Team E Algorithms)");

        System.out.print("Enter your CURRENT location (or press Enter for 'Balme Library'): ");
        String sourceName = scanner.nextLine().trim();
        if (sourceName.isEmpty()) sourceName = "Balme Library";

        int source = findLocationIndexByName(sourceName);
        if (source == -1) {
            System.out.println("Location not found: " + sourceName);
            return;
        }

        System.out.println("\n1. Reachable locations from " + state.graph.getName(source) + " (BFS Traversal):");
        DynamicArray<Integer> bfsResult = state.graph.bfs(source);
        int showBfs = Math.min(8, bfsResult.size());
        for (int i = 0; i < showBfs; i++) {
            int node = bfsResult.get(i);
            if (node != source) {
                System.out.println("  - " + state.graph.getName(node));
            }
        }
        if (bfsResult.size() > showBfs) {
            System.out.println("  ... (" + (bfsResult.size() - showBfs) + " more locations reachable)");
        }

        System.out.print("\nEnter your DESTINATION location (or press Enter for 'Pentagon Hostel'): ");
        String destName = scanner.nextLine().trim();
        if (destName.isEmpty()) destName = "Pentagon Hostel";

        int destination = findLocationIndexByName(destName);
        if (destination == -1) {
            System.out.println("Location not found: " + destName);
            return;
        }

        System.out.println("\n2. Recommended Route (Dijkstra Shortest Path):");
        Dijkstra.Result result = Dijkstra.dijkstra(state.graph, source);
        double distance = result.distanceTo(destination);

        if (Double.isInfinite(distance)) {
            System.out.println("No route found from " + state.graph.getName(source)
                    + " to " + state.graph.getName(destination) + ".");
            return;
        }

        List<Integer> path = result.pathTo(destination);
        for (int i = 0; i < path.size(); i++) {
            System.out.print(state.graph.getName(path.get(i)));
            if (i < path.size() - 1) System.out.print(" -> ");
        }
        System.out.printf("%nTotal Shortest Path Distance: %.2f km%n", distance);

        // Network-wide Minimum Spanning Tree
        int[][] adjacencyMatrix = buildIntegerAdjacencyMatrix();

        System.out.println("\n3. Campus Minimum Spanning Tree (Prim):");
        DynamicArray<int[]> primEdges = Prim.buildMST(adjacencyMatrix, source);
        int showPrim = Math.min(8, primEdges.size());
        for (int i = 0; i < showPrim; i++) {
            int[] edge = primEdges.get(i);
            System.out.println("  " + state.graph.getName(edge[0]) + " -- " + state.graph.getName(edge[1])
                    + " (weight: " + state.graph.getWeight(edge[0], edge[1]) + " km)");
        }
        if (primEdges.size() > showPrim) {
            System.out.println("  ... (" + (primEdges.size() - showPrim) + " more MST edges)");
        }

        System.out.println("\n4. Campus Minimum Spanning Tree (Kruskal):");
        DynamicArray<int[]> kruskalEdges = Kruskal.buildMST(adjacencyMatrix);
        int showKruskal = Math.min(8, kruskalEdges.size());
        for (int i = 0; i < showKruskal; i++) {
            int[] edge = kruskalEdges.get(i);
            System.out.println("  " + state.graph.getName(edge[0]) + " -- " + state.graph.getName(edge[1])
                    + " (weight: " + state.graph.getWeight(edge[0], edge[1]) + " km)");
        }
        if (kruskalEdges.size() > showKruskal) {
            System.out.println("  ... (" + (kruskalEdges.size() - showKruskal) + " more MST edges)");
        }
    }

    // =========================================================
    // OPTIMISATION DEMO (Team F)
    // =========================================================

    private static void runOptimisationDemo() {
        if (!state.dataLoaded || state.requests == null || state.requests.isEmpty()) {
            System.out.println("Data isn't loaded -- run option 1 first.");
            return;
        }

        ConsoleMenu.printHeader("OPTIMISATION DEMO (Team F Algorithms)");

        List<com.team.smartops.algorithms.optimisation.ServiceRequest> optRequests =
                ModelAdapters.toOptPendingRequests(state.requests, state.locationNameIndex);
        List<com.team.smartops.algorithms.optimisation.Resource> optResources =
                ModelAdapters.toOptResources(state.resources, state.locationNameIndex);

        System.out.println("Pending service requests loaded from DB: " + optRequests.size());
        System.out.println("Active resources loaded from DB: " + optResources.size());

        // 1. Budget Selection Comparison (Greedy vs DP Knapsack)
        int budget = 30;
        ConsoleMenu.printSection("1. Budget Selection (Budget = " + budget + " units)");

        GreedyBudgetSelector.Result greedyResult = new GreedyBudgetSelector().select(optRequests, budget);
        KnapsackDP.Result dpResult = new KnapsackDP().solveWithReconstruction(optRequests, budget);

        System.out.printf("Greedy selected: %d requests | Total Cost = %d | Total Value = %d%n",
                greedyResult.selected.size(), greedyResult.totalCost, greedyResult.totalValue);
        System.out.printf("DP Knapsack selected: %d requests | Total Value = %d%n",
                dpResult.selected.size(), dpResult.bestValue);
        System.out.printf("DP Optimization Gain over Greedy: +%d value points%n",
                (dpResult.bestValue - greedyResult.totalValue));

        // 2. Greedy Resource Assignment with Graph-based Distance Lookup
        ConsoleMenu.printSection("2. Greedy Resource Assignment (Graph Routing Distance)");
        GreedyAssignment.DistanceLookup distanceLookup;
        if (state.graphLoaded && state.graph != null) {
            distanceLookup = new GraphDistanceLookup(state.graph);
            System.out.println("Using Dijkstra shortest-path distance over campus road graph.");
        } else {
            distanceLookup = (a, b) -> a.equals(b) ? 0.0 : 2.5;
            System.out.println("Using fallback straight-line distance.");
        }

        GreedyAssignment assignmentEngine = new GreedyAssignment(distanceLookup);
        List<com.team.smartops.algorithms.optimisation.ServiceRequest> sampleRequests =
                optRequests.subList(0, Math.min(5, optRequests.size()));
        List<Assignment> assignments = assignmentEngine.greedyAssign(sampleRequests, optResources);

        System.out.println("Assignments made:");
        for (Assignment a : assignments) {
            System.out.println("  Request " + a.getRequestId() + " -> Resource "
                    + a.getResourceId() + " (Distance: " + String.format("%.2f", a.getDistance()) + " km)");
        }
    }

    // =========================================================
    // PERFORMANCE DEMO (Team G)
    // =========================================================

    private static void runPerformanceDemo() {
        if (!state.dataLoaded) {
            System.out.println("Data isn't loaded -- run option 1 first.");
            return;
        }

        ConsoleMenu.printHeader("PERFORMANCE BENCHMARK RUNNER (Team G Timing Experiments)");
        System.out.println("Executing Team G sorting, graph, and optimisation benchmarks...");

        ExperimentRunner.runAll(state.graph);

        try {
            System.out.println("\n[3/3] Executing OptimisationTimingExperiment...");
            OptimisationTimingExperiment.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Optimisation benchmark failed: " + e.getMessage());
        }

        System.out.println("\nAll performance benchmarks completed!");
        System.out.println("Results exported to: data/results/");
    }

    private static int[][] buildIntegerAdjacencyMatrix() {
        int numberOfNodes = state.graph.getNumNodes();
        int[][] matrix = new int[numberOfNodes][numberOfNodes];

        for (int from = 0; from < numberOfNodes; from++) {
            for (int to = 0; to < numberOfNodes; to++) {
                double weight = state.graph.getWeight(from, to);
                if (weight != 0) {
                    matrix[from][to] = (int) Math.round(weight) + ROUTE_PENALTY;
                } else {
                    matrix[from][to] = 0;
                }
            }
        }
        return matrix;
    }

    private static int findLocationIndexByName(String input) {
        for (int i = 0; i < state.graph.getNumNodes(); i++) {
            if (state.graph.getName(i).equalsIgnoreCase(input)) return i;
        }
        for (int i = 0; i < state.graph.getNumNodes(); i++) {
            if (state.graph.getName(i).toLowerCase().contains(input.toLowerCase())) return i;
        }
        return -1;
    }
}