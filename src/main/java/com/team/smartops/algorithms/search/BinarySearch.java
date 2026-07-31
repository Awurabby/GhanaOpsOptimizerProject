package com.team.smartops.algorithms.search;

/**
 * OWNER: Team D.
 * Binary search -- state and test the sorted-input precondition.
 */
public class BinarySearch {
    /**
     * Performs binary search on a sorted array.
     *
     * @param sortedArr the sorted array
     * @param target the value to search for
     * @param <T> the data type
     * @return index if found, otherwise -1
     */
    public static <T extends Comparable<T>> int binarySearch(T[] sortedArr, T target) {

        if (sortedArr == null || sortedArr.length == 0) {
            return -1;
        }

        int low = 0;
        int high = sortedArr.length - 1;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            int comparison = sortedArr[mid].compareTo(target);

            if (comparison == 0) {
                return mid;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }
}
