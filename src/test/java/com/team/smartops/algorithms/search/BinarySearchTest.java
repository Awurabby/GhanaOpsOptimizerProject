package com.team.smartops.algorithms.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BinarySearchTest {

    @Test
    void testNormalCase() {

        Integer[] arr = {3, 7, 10, 15, 22};

        assertEquals(2, BinarySearch.binarySearch(arr, 10));
    }

    @Test
    void testNotFound() {

        Integer[] arr = {3, 7, 10, 15, 22};

        assertEquals(-1, BinarySearch.binarySearch(arr, 100));
    }

    @Test
    void testEmptyArray() {

        Integer[] arr = {};

        assertEquals(-1, BinarySearch.binarySearch(arr, 5));
    }

    @Test
    void testSingleElementFound() {

        Integer[] arr = {50};

        assertEquals(0, BinarySearch.binarySearch(arr, 50));
    }

    @Test
    void testNullArray() {

        Integer[] arr = null;

        assertEquals(-1, BinarySearch.binarySearch(arr, 10));
    }

    @Test
    void testUnsortedArrayCounterExample() {

        Integer[] arr = {10, 2, 15, 6, 8};

        // Binary search requires sorted input.
        // This test demonstrates that using an unsorted array
        // produces an unreliable result.

        int result = BinarySearch.binarySearch(arr, 15);

        System.out.println("Result on unsorted array: " + result);
    }
}