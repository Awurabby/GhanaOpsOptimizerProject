package com.team.smartops.algorithms.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SelectionSortTest {

    @Test
    void testNormalCase() {

        int[] arr = {64, 25, 12, 22, 11};

        SelectionSort.selectionSort(arr);

        assertArrayEquals(new int[]{11, 12, 22, 25, 64}, arr);
    }

    @Test
    void testAlreadySorted() {

        int[] arr = {1, 2, 3, 4, 5};

        SelectionSort.selectionSort(arr);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testDuplicates() {

        int[] arr = {4, 2, 4, 1, 2};

        SelectionSort.selectionSort(arr);

        assertArrayEquals(new int[]{1, 2, 2, 4, 4}, arr);
    }

    @Test
    void testSingleElement() {

        int[] arr = {10};

        SelectionSort.selectionSort(arr);

        assertArrayEquals(new int[]{10}, arr);
    }

    @Test
    void testEmptyArray() {

        int[] arr = {};

        SelectionSort.selectionSort(arr);

        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testNullArray() {

        int[] arr = null;

        SelectionSort.selectionSort(arr);

        assertArrayEquals(null, arr);
    }
}