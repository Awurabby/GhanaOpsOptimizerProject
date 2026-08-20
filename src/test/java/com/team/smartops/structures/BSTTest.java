package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BST<T>, split into normal / boundary / invalid-input groups
 * to match the evidence structure required by the project brief
 * (Section 8: every structure needs tests for normal case, boundary
 * case, invalid input).
 *
 * Note: ArrayList is used here only as test scaffolding (to collect
 * traversal output for assertions), which the brief explicitly allows —
 * the "no built-in structures" rule applies to assessed core logic in
 * structures/ and algorithms/, not test code.
 */
class BSTTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {

        @Test
        @DisplayName("search finds inserted values and rejects absent ones")
        void searchFindsInsertedValues() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            for (int v : values) {
                assertTrue(tree.search(v), "expected to find " + v);
            }
            assertFalse(tree.search(999));
            assertFalse(tree.search(0));
        }

        @Test
        @DisplayName("inorderTraversal visits values in ascending order")
        void inorderTraversalIsAscending() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);

            List<Integer> expected = List.of(20, 30, 40, 50, 60, 70, 80);
            assertEquals(expected, visited);
        }

        @Test
        @DisplayName("toSortedArray matches inorder traversal output")
        void toSortedArrayMatchesInorder() {
            BST<Integer> tree = new BST<>();
            int[] values = {15, 5, 25, 1, 10, 20, 30};
            for (int v : values) {
                tree.insert(v);
            }

            Integer[] sorted = tree.toSortedArray(Integer[]::new);
            Integer[] expected = {1, 5, 10, 15, 20, 25, 30};
            assertArrayEquals(expected, sorted);
        }

        @Test
        @DisplayName("size tracks number of distinct inserted values")
        void sizeTracksInsertions() {
            BST<Integer> tree = new BST<>();
            assertEquals(0, tree.size());

            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            assertEquals(3, tree.size());
        }

        @Test
        @DisplayName("works with a Comparable domain object, not just Integer")
        void worksWithComparableObject() {
            BST<Task> tree = new BST<>();
            tree.insert(new Task("Fix water leak", 3));
            tree.insert(new Task("Power outage", 1));
            tree.insert(new Task("Routine inspection", 9));

            assertTrue(tree.search(new Task("Power outage", 1)));
            assertFalse(tree.search(new Task("Unrelated task", 5)));
        }

        @Test
        @DisplayName("delete on a leaf node removes it cleanly")
        void deleteLeafNode() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20, 40};
            for (int v : values) {
                tree.insert(v);
            }

            assertTrue(tree.delete(20)); // 20 is a leaf
            assertFalse(tree.search(20));
            assertEquals(4, tree.size());

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertEquals(List.of(30, 40, 50, 70), visited);
        }

        @Test
        @DisplayName("delete on a node with one child splices the child up")
        void deleteNodeWithOneChild() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20}; // 30 has only a left child (20)
            for (int v : values) {
                tree.insert(v);
            }

            assertTrue(tree.delete(30));
            assertFalse(tree.search(30));
            assertTrue(tree.search(20)); // 20 should still be reachable
            assertEquals(3, tree.size());

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertEquals(List.of(20, 50, 70), visited);
        }

        @Test
        @DisplayName("delete on a node with two children uses the inorder successor")
        void deleteNodeWithTwoChildren() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            assertTrue(tree.delete(30)); // has two children: 20 and 40
            assertFalse(tree.search(30));
            assertEquals(6, tree.size());

            // tree must still be a valid, fully sorted BST afterward
            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertEquals(List.of(20, 40, 50, 60, 70, 80), visited);
        }

        @Test
        @DisplayName("deleting the root with two children keeps the tree valid")
        void deleteRootWithTwoChildren() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            assertTrue(tree.delete(50));
            assertFalse(tree.search(50));
            assertEquals(6, tree.size());

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertEquals(List.of(20, 30, 40, 60, 70, 80), visited);
        }

        @Test
        @DisplayName("deleting a value that isn't present returns false and changes nothing")
        void deleteAbsentValueReturnsFalse() {
            BST<Integer> tree = new BST<>();
            tree.insert(50);
            tree.insert(30);
            tree.insert(70);

            assertFalse(tree.delete(999));
            assertEquals(3, tree.size());
            assertTrue(tree.search(50));
            assertTrue(tree.search(30));
            assertTrue(tree.search(70));
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {

        @Test
        @DisplayName("empty tree: search returns false, no crash")
        void emptyTreeSearchReturnsFalse() {
            BST<Integer> tree = new BST<>();
            assertFalse(tree.search(1));
            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
        }

        @Test
        @DisplayName("empty tree: inorderTraversal visits nothing")
        void emptyTreeTraversalVisitsNothing() {
            BST<Integer> tree = new BST<>();
            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertTrue(visited.isEmpty());
        }

        @Test
        @DisplayName("empty tree: height is -1")
        void emptyTreeHeightIsNegativeOne() {
            BST<Integer> tree = new BST<>();
            assertEquals(-1, tree.height());
        }

        @Test
        @DisplayName("single node: height is 0, found on search")
        void singleNodeHeightIsZero() {
            BST<Integer> tree = new BST<>();
            tree.insert(42);

            assertEquals(0, tree.height());
            assertEquals(1, tree.size());
            assertTrue(tree.search(42));
        }

        @Test
        @DisplayName("inserting a duplicate does not increase size or break the tree")
        void duplicateInsertIsNoOp() {
            BST<Integer> tree = new BST<>();
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            tree.insert(10); // duplicate

            assertEquals(3, tree.size());
            assertTrue(tree.search(10));

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertEquals(List.of(5, 10, 15), visited);
        }

        @Test
        @DisplayName("ascending insert order produces a fully skewed tree (height = n-1)")
        void ascendingInsertsProduceSkewedTree() {
            BST<Integer> tree = new BST<>();
            for (int i = 1; i <= 6; i++) {
                tree.insert(i);
            }
            // every node has only a right child -> height = size - 1
            assertEquals(5, tree.height());
            assertEquals(6, tree.size());
        }

        @Test
        @DisplayName("toSortedArray on an empty tree returns an empty array")
        void emptyTreeSortedArrayIsEmpty() {
            BST<Integer> tree = new BST<>();
            assertEquals(0, tree.toSortedArray(Integer[]::new).length);
        }

        @Test
        @DisplayName("deleting from an empty tree returns false, no crash")
        void deleteFromEmptyTreeReturnsFalse() {
            BST<Integer> tree = new BST<>();
            assertFalse(tree.delete(1));
            assertTrue(tree.isEmpty());
        }

        @Test
        @DisplayName("deleting the only node in the tree empties it")
        void deleteOnlyNodeEmptiesTree() {
            BST<Integer> tree = new BST<>();
            tree.insert(42);

            assertTrue(tree.delete(42));
            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
            assertFalse(tree.search(42));
        }

        @Test
        @DisplayName("deleting the same value twice: second call returns false")
        void deletingTwiceReturnsFalseSecondTime() {
            BST<Integer> tree = new BST<>();
            tree.insert(10);
            tree.insert(5);

            assertTrue(tree.delete(10));
            assertFalse(tree.delete(10)); // already gone
            assertEquals(1, tree.size());
        }

        @Test
        @DisplayName("deleting every node one at a time empties the tree correctly")
        void deletingAllNodesEmptiesTree() {
            BST<Integer> tree = new BST<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            for (int v : values) {
                assertTrue(tree.delete(v));
            }

            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
            for (int v : values) {
                assertFalse(tree.search(v));
            }
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {

        @Test
        @DisplayName("inserting null throws IllegalArgumentException")
        void insertNullThrows() {
            BST<Integer> tree = new BST<>();
            assertThrows(IllegalArgumentException.class, () -> tree.insert(null));
        }

        @Test
        @DisplayName("searching for null throws IllegalArgumentException")
        void searchNullThrows() {
            BST<Integer> tree = new BST<>();
            tree.insert(5); // non-empty tree, still should reject null before traversing
            assertThrows(IllegalArgumentException.class, () -> tree.search(null));
        }

        @Test
        @DisplayName("inorderTraversal with null visitor throws IllegalArgumentException")
        void inorderTraversalNullVisitorThrows() {
            BST<Integer> tree = new BST<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.inorderTraversal(null));
        }

        @Test
        @DisplayName("toSortedArray with null generator throws IllegalArgumentException")
        void toSortedArrayNullGeneratorThrows() {
            BST<Integer> tree = new BST<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.toSortedArray(null));
        }

        @Test
        @DisplayName("deleting null throws IllegalArgumentException")
        void deleteNullThrows() {
            BST<Integer> tree = new BST<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.delete(null));
        }
    }

    /**
     * Minimal Comparable domain object used only to prove BST works
     * generically, not just with Integer. Not part of the production
     * ServiceRequest model — swap in the real class once Team C/B agree
     * on its shape. Ordering and equality are both based on urgency,
     * matching how MyPriority_HeapTest's Task is defined.
     */
    private static class Task implements Comparable<Task> {
        final String name;
        final int urgency;

        Task(String name, int urgency) {
            this.name = name;
            this.urgency = urgency;
        }

        @Override
        public int compareTo(Task other) {
            return Integer.compare(this.urgency, other.urgency);
        }
    }
}