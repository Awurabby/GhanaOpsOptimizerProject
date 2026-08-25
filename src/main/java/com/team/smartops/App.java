package com.team.smartops;

import java.util.Scanner;

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
                    case "3" -> System.out.println("TODO: graph demo");
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