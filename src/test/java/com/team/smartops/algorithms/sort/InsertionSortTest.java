package com.team.smartops.algorithms.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class InsertionSortTest {

    @Test
    void testNormalCase() {

        int[] arr = {12, 11, 13, 5, 6};

        InsertionSort.insertionSort(arr);

        assertArrayEquals(new int[]{5, 6, 11, 12, 13}, arr);
    }

    @Test
    void testAlreadySorted() {

        int[] arr = {1, 2, 3, 4, 5};

        InsertionSort.insertionSort(arr);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testDuplicates() {

        int[] arr = {4, 2, 4, 1, 2};

        InsertionSort.insertionSort(arr);

        assertArrayEquals(new int[]{1, 2, 2, 4, 4}, arr);
    }

    @Test
    void testSingleElement() {

        int[] arr = {7};

        InsertionSort.insertionSort(arr);

        assertArrayEquals(new int[]{7}, arr);
    }

    @Test
    void testEmptyArray() {

        int[] arr = {};

        InsertionSort.insertionSort(arr);

        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testNullArray() {

        int[] arr = null;

        InsertionSort.insertionSort(arr);

        assertArrayEquals(null, arr);
    }
}