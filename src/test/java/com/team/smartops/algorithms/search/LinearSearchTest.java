package com.team.smartops.algorithms.search;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearSearchTest {

    @Test
    void testNormalCase() {

        Integer[] arr = {12, 8, 20, 15, 7};

        assertEquals(2, LinearSearch.linearSearch(arr, 20));
    }

    @Test
    void testNotFound() {

        Integer[] arr = {12, 8, 20, 15, 7};

        assertEquals(-1, LinearSearch.linearSearch(arr, 100));
    }

    @Test
    void testEmptyArray() {

        Integer[] arr = {};

        assertEquals(-1, LinearSearch.linearSearch(arr, 5));
    }

    @Test
    void testSingleElementFound() {

        Integer[] arr = {50};

        assertEquals(0, LinearSearch.linearSearch(arr, 50));
    }

    @Test
    void testSingleElementNotFound() {

        Integer[] arr = {50};

        assertEquals(-1, LinearSearch.linearSearch(arr, 10));
    }

    @Test
    void testNullArray() {

        Integer[] arr = null;

        assertEquals(-1, LinearSearch.linearSearch(arr, 10));
    }
}