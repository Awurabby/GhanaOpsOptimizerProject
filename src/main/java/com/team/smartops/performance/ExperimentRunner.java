package com.team.smartops.performance;

import com.team.smartops.structures.Graph;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * OWNER: Team G.
 * Times sorting algorithms and graph algorithms at increasing input sizes,
 * averages 3 runs each, prints results, and exports CSV to data/results/.
 */
public class ExperimentRunner {

    private static final int[] SORT_SIZES = {100, 500, 1000, 5000, 10000};

    public static void runAll(Graph graph) {
        runSortExperiments();
        if (graph != null) {
            runGraphExperiments(graph);
        }
    }

    private static void runSortExperiments() {
        System.out.println("\n=== SORTING PERFORMANCE (avg of 3 runs) ===");
        System.out.printf("%-8s %-15s %-15s %-15s %-15s%n",
            "Size", "Insertion(ns)", "Merge(ns)", "Quick(ns)", "Selection(ns)");

        StringBuilder csv = new StringBuilder("algorithm,inputSize,avgTimeNs\n");

        for (int size : SORT_SIZES) {
            int[] base = randomArray(size);

            long insertionTime = averageTime(() -> {
                int[] copy = base.clone();
                com.team.smartops.algorithms.sort.InsertionSort.insertionSort(copy);
            });
            long mergeTime = averageTime(() -> {
                int[] copy = base.clone();
                com.team.smartops.algorithms.sort.MergeSort.mergeSort(copy, 0, copy.length - 1);
            });
            long quickTime = averageTime(() -> {
                int[] copy = base.clone();
                com.team.smartops.algorithms.sort.QuickSort.quickSort(copy, 0, copy.length - 1);
            });

            String selectionDisplay;
            if (size <= 5000) {
                long selectionTime = averageTime(() -> {
                    int[] copy = base.clone();
                    com.team.smartops.algorithms.sort.SelectionSort.selectionSort(copy);
                });
                selectionDisplay = String.valueOf(selectionTime);
                csv.append("SelectionSort,").append(size).append(",").append(selectionTime).append("\n");
            } else {
                selectionDisplay = "skipped (too slow at this size)";
            }

            System.out.printf("%-8d %-15d %-15d %-15d %-15s%n",
                size, insertionTime, mergeTime, quickTime, selectionDisplay);

            csv.append("InsertionSort,").append(size).append(",").append(insertionTime).append("\n");
            csv.append("MergeSort,").append(size).append(",").append(mergeTime).append("\n");
            csv.append("QuickSort,").append(size).append(",").append(quickTime).append("\n");
        }

        writeCsv("data/results/sort_performance.csv", csv.toString());
    }

    private static void runGraphExperiments(Graph graph) {
        System.out.println("\n=== GRAPH ALGORITHM PERFORMANCE (avg of 3 runs) ===");

        long bfsTime = averageTime(() -> graph.bfs(0));
        long dfsTime = averageTime(() -> graph.dfs(0));
        long dijkstraTime = averageTime(() ->
            com.team.smartops.algorithms.graph.Dijkstra.dijkstra(graph, 0));

        System.out.println("  BFS:      " + bfsTime + " ns (graph size: " + graph.getNumNodes() + " nodes)");
        System.out.println("  DFS:      " + dfsTime + " ns");
        System.out.println("  Dijkstra: " + dijkstraTime + " ns");

        String csv = "algorithm,inputSize,avgTimeNs\n"
            + "BFS," + graph.getNumNodes() + "," + bfsTime + "\n"
            + "DFS," + graph.getNumNodes() + "," + dfsTime + "\n"
            + "Dijkstra," + graph.getNumNodes() + "," + dijkstraTime + "\n";

        writeCsv("data/results/graph_performance.csv", csv);
    }

    private static long averageTime(Runnable task) {
        long total = 0;
        for (int i = 0; i < 3; i++) {
            total += Timer.timeInNanos(task);
        }
        return total / 3;
    }

    private static int[] randomArray(int size) {
        Random rand = new Random(55225); //last digit of L300 student's index number except group leader
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1_000_000);
        }
        return arr;
    }

    private static void writeCsv(String path, String content) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
            System.out.println("Results written to " + path);
        } catch (IOException e) {
            System.out.println("Could not write CSV to " + path + ": " + e.getMessage());
        }
    }
}