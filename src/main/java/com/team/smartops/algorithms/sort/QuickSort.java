package com.team.smartops.algorithms.sort;

/**
 * OWNER: Team D.
 * From scratch. Note partitioning + worst-case discussion.
 */
public class QuickSort {

    public static void quickSort(int[] arr, int low, int high) {

        if (arr == null || arr.length <= 1) {
            return;
        }

        if (low < high) {

            int pivotIndex = partition(arr, low, high);

            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    /**
     * Partitions the array using the last element as the pivot.
     */
    private static int partition(int[] arr, int low, int high) {

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {

            if (arr[j] <= pivot) {
                i++;

                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}