package com.team.smartops.algorithms.sort;

/**
 * OWNER: Team D.
 * From scratch. Note the divide-and-conquer recurrence.
 */
public class MergeSort {
    public static void mergeSort(int[] arr, int left, int right) {

        if (arr == null || arr.length <= 1) {
            return;
        }

        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;

        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);

        merge(arr, left, mid, right);
    }

    private static void merge(int[] arr, int left, int mid, int right) {

        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = arr[left + i];
        }

        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = arr[mid + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftSize && j < rightSize) {

            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];
            }
        }
        while (i < leftSize) {
            arr[k++] = leftArray[i++];
        }
        while (j < rightSize) {
            arr[k++] = rightArray[j++];
        }
    }
}