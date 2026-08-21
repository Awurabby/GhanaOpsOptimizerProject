package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("addEdge creates a bidirectional connection in list and matrix")
        void addEdgeIsBidirectional() {
            Graph g = new Graph(3, new String[]{"A", "B", "C"});
            g.addEdge(0, 1, 5.0);
            assertTrue(g.hasEdge(0, 1));
            assertTrue(g.hasEdge(1, 0));
            assertEquals(5.0, g.getWeight(0, 1));
            assertEquals(5.0, g.getWeight(1, 0));
        }

        @Test
        @DisplayName("bfs visits all reachable nodes")
        void bfsVisitsAllReachableNodes() {
            Graph g = new Graph(4, new String[]{"A", "B", "C", "D"});
            g.addEdge(0, 1, 1);
            g.addEdge(1, 2, 1);
            g.addEdge(2, 3, 1);
            DynamicArray<Integer> order = g.bfs(0);
            assertEquals(4, order.size());
            assertEquals(0, (int) order.get(0));
        }

        @Test
        @DisplayName("dfs visits all reachable nodes")
        void dfsVisitsAllReachableNodes() {
            Graph g = new Graph(4, new String[]{"A", "B", "C", "D"});
            g.addEdge(0, 1, 1);
            g.addEdge(1, 2, 1);
            g.addEdge(2, 3, 1);
            DynamicArray<Integer> order = g.dfs(0);
            assertEquals(4, order.size());
            assertEquals(0, (int) order.get(0));
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("single-node graph: bfs/dfs return just that node")
        void singleNodeGraph() {
            Graph g = new Graph(1, new String[]{"Solo"});
            assertEquals(1, g.bfs(0).size());
            assertEquals(1, g.dfs(0).size());
        }

        @Test
        @DisplayName("disconnected node is not reached by bfs from another component")
        void disconnectedNodeNotReached() {
            Graph g = new Graph(3, new String[]{"A", "B", "C"});
            g.addEdge(0, 1, 1); // C (index 2) stays isolated
            DynamicArray<Integer> order = g.bfs(0);
            assertEquals(2, order.size());
        }

        @Test
        @DisplayName("hasEdge is false for nodes with no connection")
        void noEdgeReturnsFalse() {
            Graph g = new Graph(2, new String[]{"A", "B"});
            assertFalse(g.hasEdge(0, 1));
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("constructing with zero or negative nodes throws")
        void nonPositiveNumNodesThrows() {
            assertThrows(IllegalArgumentException.class, () -> new Graph(0, new String[]{}));
        }

        @Test
        @DisplayName("mismatched locationNames length throws")
        void mismatchedNamesLengthThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Graph(2, new String[]{"OnlyOne"}));
        }

        @Test
        @DisplayName("addEdge with out-of-bounds node throws")
        void addEdgeOutOfBoundsThrows() {
            Graph g = new Graph(2, new String[]{"A", "B"});
            assertThrows(IndexOutOfBoundsException.class, () -> g.addEdge(0, 5, 1.0));
        }

        @Test
        @DisplayName("bfs from an out-of-bounds start throws")
        void bfsOutOfBoundsStartThrows() {
            Graph g = new Graph(2, new String[]{"A", "B"});
            assertThrows(IndexOutOfBoundsException.class, () -> g.bfs(9));
        }
    }
}
