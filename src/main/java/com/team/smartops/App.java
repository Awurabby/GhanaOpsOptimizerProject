package com.team.smartops;

import java.util.Scanner;

/**
 * Entry point. This is the console menu the examiner will use to run
 * demonstrations without touching the source code (see brief, Section 8.iv).
 *
 * OWNER: Lead — wires the other modules together. Don't put real logic here;
 * call into db/, structures/, algorithms/, performance/ instead.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> System.out.println("TODO: load data from database into structures");
                case "2" -> System.out.println("TODO: run search/sort demo");
                case "3" -> System.out.println("TODO: run graph routing demo (BFS/DFS/Dijkstra/MST)");
                case "4" -> System.out.println("TODO: run optimisation demo (greedy + DP)");
                case "5" -> System.out.println("TODO: run performance experiment and export CSV");
                case "0" -> running = false;
                default -> System.out.println("Unknown option, try again.");
            }
        }
        scanner.close();
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
