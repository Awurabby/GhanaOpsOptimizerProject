package com.team.smartops.algorithms.sort;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MergeSortTest {

    @Test
    void testNormalCase() {

        int[] arr = {38, 27, 43, 3, 9, 82, 10};

        MergeSort.mergeSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{3, 9, 10, 27, 38, 43, 82}, arr);
    }

    @Test
    void testAlreadySorted() {

        int[] arr = {1, 2, 3, 4, 5};

        MergeSort.mergeSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testDuplicates() {

        int[] arr = {4, 2, 4, 1, 2};

        MergeSort.mergeSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{1, 2, 2, 4, 4}, arr);
    }

    @Test
    void testSingleElement() {

        int[] arr = {9};

        MergeSort.mergeSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{9}, arr);
    }

    @Test
    void testEmptyArray() {

        int[] arr = {};

        MergeSort.mergeSort(arr, 0, arr.length - 1);

        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testNullArray() {

        int[] arr = null;

        MergeSort.mergeSort(arr, 0, 0);

        assertArrayEquals(null, arr);
    }
}