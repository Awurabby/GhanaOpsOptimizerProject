package com.team.smartops.algorithms.graph;

import com.team.smartops.structures.DynamicArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("builds correct MST on a connected 4-node graph")
        void buildsCorrectMST() {
            int[][] matrix = {
                {0, 1, 4, 0},
                {1, 0, 2, 5},
                {4, 2, 0, 1},
                {0, 5, 1, 0}
            };

            DynamicArray<int[]> mst = Prim.buildMST(matrix, 0);
            assertNotNull(mst);
            assertEquals(3, mst.size(), "4-node graph MST must contain 3 edges");
        }

        @Test
        @DisplayName("respects startVertex parameter")
        void respectsStartVertex() {
            int[][] matrix = {
                {0, 2, 0},
                {2, 0, 3},
                {0, 3, 0}
            };

            DynamicArray<int[]> mstFrom0 = Prim.buildMST(matrix, 0);
            DynamicArray<int[]> mstFrom2 = Prim.buildMST(matrix, 2);

            assertEquals(2, mstFrom0.size());
            assertEquals(2, mstFrom2.size());
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("single node graph produces empty edge list without crashing")
        void singleNodeGraph() {
            int[][] matrix = {{0}};
            DynamicArray<int[]> mst = Prim.buildMST(matrix, 0);
            assertEquals(0, mst.size());
        }

        @Test
        @DisplayName("disconnected graph produces partial MST without throwing ArrayIndexOutOfBoundsException")
        void disconnectedGraphDoesNotCrash() {
            int[][] matrix = {
                {0, 2, 0, 0},
                {2, 0, 0, 0},
                {0, 0, 0, 3},
                {0, 0, 3, 0}
            };

            assertDoesNotThrow(() -> {
                DynamicArray<int[]> partialMST = Prim.buildMST(matrix, 0);
                assertNotNull(partialMST);
                assertEquals(1, partialMST.size()); // only component containing node 0
            });
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("null or empty matrix throws IllegalArgumentException")
        void nullOrEmptyMatrixThrows() {
            assertThrows(IllegalArgumentException.class, () -> Prim.buildMST(null, 0));
            assertThrows(IllegalArgumentException.class, () -> Prim.buildMST(new int[0][0], 0));
        }

        @Test
        @DisplayName("out of bounds start vertex throws IllegalArgumentException")
        void outOfBoundsStartVertexThrows() {
            int[][] matrix = {{0, 1}, {1, 0}};
            assertThrows(IllegalArgumentException.class, () -> Prim.buildMST(matrix, -1));
            assertThrows(IllegalArgumentException.class, () -> Prim.buildMST(matrix, 5));
        }
    }
}
