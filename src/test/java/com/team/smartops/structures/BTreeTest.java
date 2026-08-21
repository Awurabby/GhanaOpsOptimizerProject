package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class BTreeTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("inserted keys are found by search")
        void insertedKeysAreFound() {
            BTree tree = new BTree();
            int[] keys = {10, 20, 30, 40, 50, 55};
            for (int k : keys) tree.insert(k);
            for (int k : keys) assertTrue(tree.contains(k), "expected to find " + k);
        }

        @Test
        @DisplayName("searching for an absent key returns false")
        void absentKeyNotFound() {
            BTree tree = new BTree();
            tree.insert(10);
            tree.insert(20);
            assertFalse(tree.contains(999));
        }

        @Test
        @DisplayName("inserting enough keys to force a node split still finds all keys")
        void insertForcesSplitAndAllKeysFound() {
            BTree tree = new BTree();
            // more than 2T-1 = 3 keys forces at least one split
            int[] keys = {1, 2, 3, 4, 5, 6, 7};
            for (int k : keys) tree.insert(k);
            for (int k : keys) assertTrue(tree.contains(k));
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("empty tree: search returns null / contains is false")
        void emptyTreeSearchReturnsNull() {
            BTree tree = new BTree();
            assertNull(tree.search(5));
            assertFalse(tree.contains(5));
        }

        @Test
        @DisplayName("single key insert then found")
        void singleKeyInsertThenFound() {
            BTree tree = new BTree();
            tree.insert(42);
            assertTrue(tree.contains(42));
        }

        @Test
        @DisplayName("inserting a duplicate key does not break search")
        void duplicateInsertDoesNotBreakSearch() {
            BTree tree = new BTree();
            tree.insert(10);
            tree.insert(10);
            assertTrue(tree.contains(10));
        }

        @Test
        @DisplayName("root split (>3 keys) still correctly roots the tree")
        void rootSplitKeepsTreeSearchable() {
            BTree tree = new BTree();
            for (int i = 1; i <= 20; i++) tree.insert(i);
            for (int i = 1; i <= 20; i++) assertTrue(tree.contains(i));
            assertFalse(tree.contains(21));
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("searching an empty tree does not throw, returns null")
        void searchEmptyTreeDoesNotThrow() {
            BTree tree = new BTree();
            assertDoesNotThrow(() -> tree.search(1));
        }
    }
}
