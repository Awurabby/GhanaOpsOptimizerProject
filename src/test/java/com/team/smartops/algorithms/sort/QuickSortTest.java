package com.team.smartops.algorithms.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class QuickSortTest {

    @Test
    void testNormalCase() {

        int[] arr = {10, 7, 8, 9, 1, 5};

        QuickSort.quickSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{1, 5, 7, 8, 9, 10}, arr);
    }

    @Test
    void testAlreadySorted() {

        int[] arr = {1, 2, 3, 4, 5};

        QuickSort.quickSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testDuplicates() {

        int[] arr = {4, 2, 4, 1, 2};

        QuickSort.quickSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{1, 2, 2, 4, 4}, arr);
    }

    @Test
    void testSingleElement() {

        int[] arr = {99};

        QuickSort.quickSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{99}, arr);
    }

    @Test
    void testEmptyArray() {

        int[] arr = {};

        QuickSort.quickSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testNullArray() {

        int[] arr = null;

        QuickSort.quickSort(arr, 0, 0);

        assertArrayEquals(null, arr);
    }
}