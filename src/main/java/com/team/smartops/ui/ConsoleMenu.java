package com.team.smartops.ui;

/**
 * Console menu presentation and helper utility for the CLI application.
 */
public class ConsoleMenu {

    public static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("  " + title);
        System.out.println("=".repeat(50));
    }

    public static void printSection(String sectionTitle) {
        System.out.println("\n--- " + sectionTitle + " ---");
    }

    public static void printMainMenu() {
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
