package com.team.smartops.algorithms.search;

/**
 * OWNER: Team D.
 * Linear search over service requests.
 */
public class LinearSearch {
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
