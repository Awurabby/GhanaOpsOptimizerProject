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
import com.team.smartops.performance.ExperimentRunner;
import com.team.smartops.performance.Timer;

public class App {

    private static AppState state = new AppState();
    private static java.util.Scanner scanner = new java.util.Scanner(System.in);

    public static void main(String[] args) {
       
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
                    case "5" -> runPerformanceDemo();
                    case "0" -> running = false;
                    default -> System.out.println("Unknown option, try again.");
                }
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void runPerformanceDemo() {
    if (!state.dataLoaded) {
        System.out.println("Data isn't loaded -- run option 1 first.");
        return;
    }
    ExperimentRunner.runAll(state.graph);
}
   
    // SEARCH + SORT DEMO

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

    String[] requestArray = state.requests.toArray(new String[0]);
    String target = null;
    for (String request : requestArray) {
        if (request.startsWith(input + " - ")) {
            target = request;
            break;
        }
    }

    if (target == null) {
        System.out.println("No request found with ID " + input + ".");
        return;
    }

    final String targetCopy = target;
long time = Timer.timeInNanos(() ->
    LinearSearch.linearSearch(requestArray, targetCopy));
    System.out.println("Found: " + target);
    System.out.println("(Linear search took " + time + " ns)");
}

private static void showNextRequestToDispatch() {
    String best = null;
    int bestRank = -1;

    for (String request : state.requests) {
        int rank = urgencyRank(extractUrgency(request));
        if (rank > bestRank) {
            bestRank = rank;
            best = request;
        }
    }

    if (best == null) {
        System.out.println("No pending requests found.");
    } else {
        System.out.println("Next request to dispatch (highest urgency): " + best);
    }
}

private static void searchLocationByName() {
    System.out.println("Available locations:");
    for (int i = 0; i < state.graph.getNumNodes(); i++) {
        System.out.println("  " + state.graph.getName(i));
    }

    System.out.print("\nEnter a location name to search for: ");
    String input = scanner.nextLine().trim();

    int index = findLocationIndexByName(input);
    if (index == -1) {
        System.out.println("Location not found: " + input);
        return;
    }
    System.out.println("Found: " + state.graph.getName(index) + " (node " + index + ")");
}

private static void runFullSortComparison() {
    String[] requestArray = state.requests.toArray(new String[0]);
    int[] requestIds = extractRequestIds(requestArray);

    System.out.println("\nRequest IDs before sorting:");
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

    System.out.println("Available locations:");
    for (int i = 0; i < state.graph.getNumNodes(); i++) {
        System.out.println("  " + state.graph.getName(i));
    }

    System.out.print("\nEnter your CURRENT location: ");
    String sourceName = scanner.nextLine().trim();
    int source = findLocationIndexByName(sourceName);
    if (source == -1) {
        System.out.println("Location not found: " + sourceName);
        return;
    }

    System.out.println("\nLocations reachable from " + state.graph.getName(source) + ":");
    DynamicArray<Integer> bfsResult = state.graph.bfs(source);
    for (int i = 0; i < bfsResult.size(); i++) {
        int node = bfsResult.get(i);
        if (node != source) {
            System.out.println("  " + state.graph.getName(node));
        }
    }

    System.out.print("\nEnter your DESTINATION: ");
    String destName = scanner.nextLine().trim();
    int destination = findLocationIndexByName(destName);
    if (destination == -1) {
        System.out.println("Location not found: " + destName);
        return;
    }

    Dijkstra.Result result = Dijkstra.dijkstra(state.graph, source);
    double distance = result.distanceTo(destination);

    if (Double.isInfinite(distance)) {
        System.out.println("\nNo route found from " + state.graph.getName(source)
            + " to " + state.graph.getName(destination) + ".");
        return;
    }

    System.out.println("\nRecommended route (Dijkstra):");
    java.util.List<Integer> path = result.pathTo(destination);
    for (int i = 0; i < path.size(); i++) {
        System.out.print(state.graph.getName(path.get(i)));
        if (i < path.size() - 1) System.out.print("  ->  ");
    }
    System.out.println("\nTotal distance: " + distance);

    // --- Network-wide minimum spanning tree, for the report/demo ---
    DynamicArray<Integer> fullReach = state.graph.bfs(0);
    if (fullReach.size() < state.graph.getNumNodes()) {
        System.out.println("\n(Network-wide MST skipped -- graph is not fully connected.)");
        return;
    }

    int[][] adjacencyMatrix = buildIntegerAdjacencyMatrix();

    System.out.println("\nNetwork-wide minimum spanning tree (Prim):");
    DynamicArray<int[]> primEdges = Prim.buildMST(adjacencyMatrix, source);
    for (int i = 0; i < primEdges.size(); i++) {
        int[] edge = primEdges.get(i);
        System.out.println("  " + state.graph.getName(edge[0]) + " -- " + state.graph.getName(edge[1])
            + " (weight: " + state.graph.getWeight(edge[0], edge[1]) + ")");
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

    private static int findLocationIndexByName(String input) {
    for (int i = 0; i < state.graph.getNumNodes(); i++) {
        if (state.graph.getName(i).equalsIgnoreCase(input)) return i;
    }
    for (int i = 0; i < state.graph.getNumNodes(); i++) {
        if (state.graph.getName(i).toLowerCase().contains(input.toLowerCase())) return i;
    }
    return -1;
}

private static String extractUrgency(String request) {
    int start = request.indexOf("Urgency: ");
    if (start == -1) return "";
    start += "Urgency: ".length();
    int end = request.indexOf(" |", start);
    return end == -1 ? request.substring(start).trim() : request.substring(start, end).trim();
}

private static int urgencyRank(String urgency) {
    return switch (urgency.toLowerCase()) {
        case "critical" -> 4;
        case "high" -> 3;
        case "medium" -> 2;
        case "low" -> 1;
        default -> 0;
    };
}
   
    // MENU
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