package com.team.smartops;

import java.util.Scanner;
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
        System.out.println("BFS from node 0:");
        DynamicArray<Integer> bfsResult = state.graph.bfs(0);
        for (int i = 0; i < bfsResult.size(); i++) {
            int node = bfsResult.get(i);
            System.out.println("  " + node + ": " + state.graph.getName(node));
        }

        System.out.println("DFS from node 0:");
        DynamicArray<Integer> dfsResult = state.graph.dfs(0);
        for (int i = 0; i < dfsResult.size(); i++) {
            int node = dfsResult.get(i);
            System.out.println("  " + node + ": " + state.graph.getName(node));
        }
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