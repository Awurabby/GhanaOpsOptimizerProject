package com.team.smartops.algorithms.sort;

/**
 * OWNER: Team D.
 * From scratch, no Collections.sort.
 */
public class SelectionSort {
    public static void selectionSort(int[] arr) {

        if (arr == null || arr.length <= 1) {
            return;
        }

        for (int i = 0; i < arr.length - 1; i++) {

            int minIndex = i;

            for (int j = i + 1; j < arr.length; j++) {

                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }
}