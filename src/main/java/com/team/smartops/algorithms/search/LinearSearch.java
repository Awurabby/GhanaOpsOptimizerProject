package com.team.smartops.algorithms.search;

/**
 * OWNER: Team D.
 * Linear search over service requests.
 */
public class LinearSearch {

    /**
     * Performs a linear search on an array.
     *
     * @param arr the array to search
     * @param target the value to search for
     * @param <T> the data type of the array elements
     * @return the index of the target if found, otherwise -1
     */
    public static <T> int linearSearch(T[] arr, T target) {

        if (arr == null || arr.length == 0) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
