package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class DisjointSetTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("union merges two elements into the same set")
        void unionMergesElements() {
            DisjointSet ds = new DisjointSet(5);
            ds.union(0, 1);
            assertTrue(ds.connected(0, 1));
        }

        @Test
        @DisplayName("unrelated elements are not connected")
        void unrelatedElementsNotConnected() {
            DisjointSet ds = new DisjointSet(5);
            ds.union(0, 1);
            assertFalse(ds.connected(0, 2));
        }

        @Test
        @DisplayName("union reduces the number of sets")
        void unionReducesSetCount() {
            DisjointSet ds = new DisjointSet(5);
            assertEquals(5, ds.getNumSets());
            ds.union(0, 1);
            assertEquals(4, ds.getNumSets());
        }

        @Test
        @DisplayName("chained unions connect transitively")
        void chainedUnionsConnectTransitively() {
            DisjointSet ds = new DisjointSet(4);
            ds.union(0, 1);
            ds.union(1, 2);
            assertTrue(ds.connected(0, 2));
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("single-element set: connected to itself")
        void singleElementConnectedToSelf() {
            DisjointSet ds = new DisjointSet(1);
            assertTrue(ds.connected(0, 0));
            assertEquals(1, ds.getNumSets());
        }

        @Test
        @DisplayName("unioning an element with itself does not change set count")
        void unionSameElementNoOp() {
            DisjointSet ds = new DisjointSet(3);
            ds.union(0, 0);
            assertEquals(3, ds.getNumSets());
        }

        @Test
        @DisplayName("unioning an already-connected pair again is a no-op")
        void unionAlreadyConnectedIsNoOp() {
            DisjointSet ds = new DisjointSet(3);
            ds.union(0, 1);
            ds.union(0, 1);
            assertEquals(2, ds.getNumSets());
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("constructing with zero or negative size throws")
        void nonPositiveSizeThrows() {
            assertThrows(IllegalArgumentException.class, () -> new DisjointSet(0));
            assertThrows(IllegalArgumentException.class, () -> new DisjointSet(-3));
        }

        @Test
        @DisplayName("find with out-of-bounds index throws")
        void findOutOfBoundsThrows() {
            DisjointSet ds = new DisjointSet(3);
            assertThrows(IndexOutOfBoundsException.class, () -> ds.find(10));
        }

        @Test
        @DisplayName("union with out-of-bounds index throws")
        void unionOutOfBoundsThrows() {
            DisjointSet ds = new DisjointSet(3);
            assertThrows(IndexOutOfBoundsException.class, () -> ds.union(0, 10));
        }
    }
}
