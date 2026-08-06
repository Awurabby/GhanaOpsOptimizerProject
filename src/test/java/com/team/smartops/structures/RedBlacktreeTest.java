package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RedBlackTree<T>, split into normal / boundary / invalid-input
 * groups per the project brief (Section 8), plus a dedicated invariant
 * group. Correctness for a red-black tree isn't just "search still works"
 * — a broken rotation can sometimes still return correct search() results
 * by luck on small inputs. So every test that inserts or deletes also
 * calls isValidRedBlackTree() to directly verify the tree's internal
 * color/black-height invariants weren't violated, not just its observable
 * behavior.
 *
 * Note: ArrayList is used here only as test scaffolding (to collect
 * traversal output for assertions), which the brief allows — the "no
 * built-in structures" rule applies to assessed core logic in
 * structures/ and algorithms/, not test code.
 */
class RedBlackTreeTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {

        @Test
        @DisplayName("search finds inserted values and rejects absent ones")
        void searchFindsInsertedValues() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            for (int v : values) {
                assertTrue(tree.search(v), "expected to find " + v);
            }
            assertFalse(tree.search(999));
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("inorderTraversal visits values in ascending order")
        void inorderTraversalIsAscending() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);

            assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), visited);
        }

        @Test
        @DisplayName("ascending insert sequence stays balanced (the case that breaks a plain BST)")
        void ascendingInsertsStayBalanced() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            for (int i = 1; i <= 15; i++) {
                tree.insert(i);
            }

            assertEquals(15, tree.size());
            assertTrue(tree.isValidRedBlackTree());

            // an unbalanced BST would have height 14 here (fully skewed);
            // a red-black tree on 15 nodes is bounded by 2*log2(16) = 8
            assertTrue(tree.height() <= 8,
                "expected balanced height <= 8 for 15 ascending inserts, got " + tree.height());
        }

        @Test
        @DisplayName("size tracks distinct inserted values")
        void sizeTracksInsertions() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            assertEquals(0, tree.size());

            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            assertEquals(3, tree.size());
        }

        @Test
        @DisplayName("works with a Comparable domain object, not just Integer")
        void worksWithComparableObject() {
            RedBlackTree<Task> tree = new RedBlackTree<>();
            tree.insert(new Task("Fix water leak", 3));
            tree.insert(new Task("Power outage", 1));
            tree.insert(new Task("Routine inspection", 9));

            assertTrue(tree.search(new Task("Power outage", 1)));
            assertFalse(tree.search(new Task("Unrelated task", 5)));
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("delete on a leaf-equivalent value keeps the tree valid")
        void deleteLeafKeepsTreeValid() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            assertTrue(tree.delete(20));
            assertFalse(tree.search(20));
            assertEquals(6, tree.size());
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("delete on the root keeps the tree valid")
        void deleteRootKeepsTreeValid() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80};
            for (int v : values) {
                tree.insert(v);
            }

            assertTrue(tree.delete(50));
            assertFalse(tree.search(50));
            assertEquals(6, tree.size());
            assertTrue(tree.isValidRedBlackTree());

            List<Integer> visited = new ArrayList<>();
            tree.inorderTraversal(visited::add);
            assertEquals(List.of(20, 30, 40, 60, 70, 80), visited);
        }

        @Test
        @DisplayName("deleting a value that isn't present returns false and changes nothing")
        void deleteAbsentValueReturnsFalse() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(50);
            tree.insert(30);
            tree.insert(70);

            assertFalse(tree.delete(999));
            assertEquals(3, tree.size());
            assertTrue(tree.isValidRedBlackTree());
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {

        @Test
        @DisplayName("empty tree: search returns false, no crash")
        void emptyTreeSearchReturnsFalse() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            assertFalse(tree.search(1));
            assertTrue(tree.isEmpty());
            assertEquals(-1, tree.height());
        }

        @Test
        @DisplayName("empty tree: isValidRedBlackTree still holds (vacuously true)")
        void emptyTreeIsValid() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("single node: height is 0, root is black")
        void singleNodeHeightIsZero() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(42);

            assertEquals(0, tree.height());
            assertEquals(1, tree.size());
            assertTrue(tree.search(42));
            assertTrue(tree.isValidRedBlackTree()); // also confirms root got recolored black
        }

        @Test
        @DisplayName("inserting a duplicate does not increase size or break invariants")
        void duplicateInsertIsNoOp() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(10);
            tree.insert(5);
            tree.insert(15);
            tree.insert(10); // duplicate

            assertEquals(3, tree.size());
            assertTrue(tree.search(10));
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("deleting from an empty tree returns false, no crash")
        void deleteFromEmptyTreeReturnsFalse() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            assertFalse(tree.delete(1));
            assertTrue(tree.isEmpty());
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("deleting the only node empties the tree and stays valid")
        void deleteOnlyNodeEmptiesTree() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(42);

            assertTrue(tree.delete(42));
            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
            assertFalse(tree.search(42));
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("deleting the same value twice: second call returns false")
        void deletingTwiceReturnsFalseSecondTime() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(10);
            tree.insert(5);

            assertTrue(tree.delete(10));
            assertFalse(tree.delete(10));
            assertEquals(1, tree.size());
            assertTrue(tree.isValidRedBlackTree());
        }

        @Test
        @DisplayName("deleting every node one at a time empties the tree, staying valid throughout")
        void deletingAllNodesEmptiesTreeAndStaysValid() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            int[] values = {50, 30, 70, 20, 40, 60, 80, 10, 25, 35, 45};
            for (int v : values) {
                tree.insert(v);
            }
            assertTrue(tree.isValidRedBlackTree());

            for (int v : values) {
                assertTrue(tree.delete(v));
                // check invariants hold after EVERY delete, not just the final one —
                // this is the strongest possible check on the fixup logic
                assertTrue(tree.isValidRedBlackTree(),
                    "invariant violated after deleting " + v);
            }

            assertTrue(tree.isEmpty());
            assertEquals(0, tree.size());
        }

        @Test
        @DisplayName("large ascending insert then full descending delete stays valid throughout")
        void largeInsertThenDeleteStaysValid() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            for (int i = 1; i <= 50; i++) {
                tree.insert(i);
                assertTrue(tree.isValidRedBlackTree(), "invariant violated after inserting " + i);
            }

            for (int i = 50; i >= 1; i--) {
                assertTrue(tree.delete(i));
                assertTrue(tree.isValidRedBlackTree(), "invariant violated after deleting " + i);
            }

            assertTrue(tree.isEmpty());
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {

        @Test
        @DisplayName("inserting null throws IllegalArgumentException")
        void insertNullThrows() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            assertThrows(IllegalArgumentException.class, () -> tree.insert(null));
        }

        @Test
        @DisplayName("searching for null throws IllegalArgumentException")
        void searchNullThrows() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.search(null));
        }

        @Test
        @DisplayName("deleting null throws IllegalArgumentException")
        void deleteNullThrows() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.delete(null));
        }

        @Test
        @DisplayName("inorderTraversal with null visitor throws IllegalArgumentException")
        void inorderTraversalNullVisitorThrows() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.inorderTraversal(null));
        }

        @Test
        @DisplayName("toSortedArray with null generator throws IllegalArgumentException")
        void toSortedArrayNullGeneratorThrows() {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            tree.insert(5);
            assertThrows(IllegalArgumentException.class, () -> tree.toSortedArray(null));
        }
    }

   
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
