package com.team.smartops.performance;

import com.team.smartops.algorithms.search.BinarySearch;
import com.team.smartops.algorithms.search.LinearSearch;
import com.team.smartops.algorithms.sort.InsertionSort;
import com.team.smartops.algorithms.sort.MergeSort;
import com.team.smartops.algorithms.sort.QuickSort;
import com.team.smartops.algorithms.sort.SelectionSort;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

/**
 * OWNER: Team D.
 * Times linear vs. binary search, and all 4 sorts, at increasing input
 * sizes. Runs each 3x and averages, exports results to data/results/.
 *
 * Format follows Evidence_and_Testing_Standards.md Sec 5:
 * algorithmName,inputSize,timeNs,dateRun (one row per algorithm+size,
 * timeNs already averaged over 3 runs).
 */
public class SearchSortTimingExperiment {

    private static final int[] SIZES = {100, 500, 1000, 5000, 10000};
    private static final int RUNS_PER_SIZE = 3;
    private static final String OUTPUT_PATH = "data/results/search-sort-timing.csv";

    public static void main(String[] args) throws IOException {

        StringBuilder csv = new StringBuilder();
        csv.append("algorithmName,inputSize,timeNs,dateRun\n");

        for (int size : SIZES) {
            System.out.println("Running experiments at size " + size + "...");

            csv.append(timeLinearSearch(size));
            csv.append(timeBinarySearch(size));
            csv.append(timeSort("SelectionSort", size, arr -> SelectionSort.selectionSort(arr)));
            csv.append(timeSort("InsertionSort", size, arr -> InsertionSort.insertionSort(arr)));
            csv.append(timeSort("MergeSort", size, arr -> MergeSort.mergeSort(arr, 0, arr.length - 1)));
            csv.append(timeSort("QuickSort", size, arr -> QuickSort.quickSort(arr, 0, arr.length - 1)));
        }

        try (FileWriter writer = new FileWriter(OUTPUT_PATH)) {
            writer.write(csv.toString());
        }

        System.out.println("Done. Results written to " + OUTPUT_PATH);
    }

    private static String timeLinearSearch(int size) {
        long[] times = new long[RUNS_PER_SIZE];
        for (int r = 0; r < RUNS_PER_SIZE; r++) {
            Integer[] arr = randomIntegerArray(size);
            Integer target = arr[new Random().nextInt(size)];
            times[r] = Timer.timeInNanos(() -> LinearSearch.linearSearch(arr, target));
        }
        return formatRow("LinearSearch", size, times);
    }

    private static String timeBinarySearch(int size) {
        long[] times = new long[RUNS_PER_SIZE];
        for (int r = 0; r < RUNS_PER_SIZE; r++) {
            Integer[] arr = randomIntegerArray(size);
            Arrays.sort(arr); // binary search precondition: input must be sorted
            Integer target = arr[new Random().nextInt(size)];
            times[r] = Timer.timeInNanos(() -> BinarySearch.binarySearch(arr, target));
        }
        return formatRow("BinarySearch", size, times);
    }

    private interface SortCall {
        void run(int[] arr);
    }

    private static String timeSort(String name, int size, SortCall sortCall) {
        long[] times = new long[RUNS_PER_SIZE];
        for (int r = 0; r < RUNS_PER_SIZE; r++) {
            int[] arr = randomIntArray(size);
            times[r] = Timer.timeInNanos(() -> sortCall.run(arr));
        }
        return formatRow(name, size, times);
    }

    private static String formatRow(String algorithm, int size, long[] times) {
        long sum = 0;
        for (long t : times) sum += t;
        long average = sum / times.length;
        return String.format("%s,%d,%d,%s%n",
                algorithm, size, average, LocalDate.now());
    }

    private static int[] randomIntArray(int size) {
        Random random = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }

    private static Integer[] randomIntegerArray(int size) {
        Random random = new Random();
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
}