package com.team.smartops.algorithms.sort;

/**
 * OWNER: Team D.
 * From scratch. Discuss stability + in-place.
 */
public class InsertionSort {

    public static void insertionSort(int[] arr) {

        if (arr == null || arr.length <= 1) {
            return;
        }

        for (int i = 1; i < arr.length; i++) {

            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
    }
}