package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MyHeap<T>, split into normal / boundary / invalid-input
 * groups to match the evidence structure required by the project brief
 * (Section 8: every structure needs tests for normal case, boundary
 * case, invalid input).
 */
class MyPriority_HeapTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {

        @Test
        @DisplayName("extractMin returns elements in ascending order")
        void extractMinReturnsAscendingOrder() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            int[] values = {40, 10, 30, 5, 25, 15, 20};
            for (int v : values) {
                heap.insert(v);
            }

            int[] expectedSorted = {5, 10, 15, 20, 25, 30, 40};
            for (int expected : expectedSorted) {
                assertEquals(expected, heap.extractMin());
            }
        }

        @Test
        @DisplayName("peek returns the minimum without removing it")
        void peekDoesNotRemove() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            heap.insert(50);
            heap.insert(20);
            heap.insert(35);

            assertEquals(20, heap.peek());
            assertEquals(3, heap.size());
            assertEquals(20, heap.peek()); // still there second time
        }

        @Test
        @DisplayName("size and isEmpty track insert/extract correctly")
        void sizeAndIsEmptyTrackState() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            assertTrue(heap.isEmpty());
            assertEquals(0, heap.size());

            heap.insert(10);
            heap.insert(5);
            assertFalse(heap.isEmpty());
            assertEquals(2, heap.size());

            heap.extractMin();
            assertEquals(1, heap.size());

            heap.extractMin();
            assertTrue(heap.isEmpty());
            assertEquals(0, heap.size());
        }

        @Test
        @DisplayName("handles duplicate priority values correctly")
        void handlesDuplicateValues() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            heap.insert(10);
            heap.insert(10);
            heap.insert(5);
            heap.insert(5);
            heap.insert(20);

            assertEquals(5, heap.extractMin());
            assertEquals(5, heap.extractMin());
            assertEquals(10, heap.extractMin());
            assertEquals(10, heap.extractMin());
            assertEquals(20, heap.extractMin());
        }

        @Test
        @DisplayName("works with a Comparable domain object, not just Integer")
        void worksWithComparableObject() {
            MyPriority_Heap<Task> heap = new MyPriority_Heap<>();
            heap.insert(new Task("Fix water leak", 3));
            heap.insert(new Task("Routine inspection", 9));
            heap.insert(new Task("Power outage", 1));

            assertEquals("Power outage", heap.extractMin().name);
            assertEquals("Fix water leak", heap.extractMin().name);
            assertEquals("Routine inspection", heap.extractMin().name);
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {

        @Test
        @DisplayName("single element insert then extract")
        void singleElement() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            heap.insert(42);

            assertEquals(1, heap.size());
            assertEquals(42, heap.peek());
            assertEquals(42, heap.extractMin());
            assertTrue(heap.isEmpty());
        }

        @Test
        @DisplayName("grows past initial capacity without losing heap order")
        void growsPastInitialCapacity() {
            // start tiny so we're guaranteed to exercise the resize path
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>(2);

            for (int i = 20; i >= 1; i--) {
                heap.insert(i);
            }
            assertEquals(20, heap.size());

            for (int expected = 1; expected <= 20; expected++) {
                assertEquals(expected, heap.extractMin());
            }
        }

        @Test
        @DisplayName("re-extracting after emptying throws, then insert works again")
        void reusableAfterEmptying() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            heap.insert(1);
            heap.extractMin();

            assertThrows(java.util.NoSuchElementException.class, heap::extractMin);

            // heap should still be usable after hitting empty
            heap.insert(99);
            assertEquals(99, heap.extractMin());
        }

        @Test
        @DisplayName("already-sorted ascending input still heapifies correctly")
        void alreadySortedAscendingInput() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            for (int i = 1; i <= 10; i++) {
                heap.insert(i);
            }
            for (int expected = 1; expected <= 10; expected++) {
                assertEquals(expected, heap.extractMin());
            }
        }

        @Test
        @DisplayName("already-sorted descending input still heapifies correctly")
        void alreadySortedDescendingInput() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            for (int i = 10; i >= 1; i--) {
                heap.insert(i);
            }
            for (int expected = 1; expected <= 10; expected++) {
                assertEquals(expected, heap.extractMin());
            }
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {

        @Test
        @DisplayName("inserting null throws IllegalArgumentException")
        void insertNullThrows() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            assertThrows(IllegalArgumentException.class, () -> heap.insert(null));
        }

        @Test
        @DisplayName("extractMin on empty heap throws NoSuchElementException")
        void extractMinOnEmptyThrows() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            assertThrows(java.util.NoSuchElementException.class, heap::extractMin);
        }

        @Test
        @DisplayName("peek on empty heap throws NoSuchElementException")
        void peekOnEmptyThrows() {
            MyPriority_Heap<Integer> heap = new MyPriority_Heap<>();
            assertThrows(java.util.NoSuchElementException.class, heap::peek);
        }

        @Test
        @DisplayName("zero initial capacity throws IllegalArgumentException")
        void zeroInitialCapacityThrows() {
            assertThrows(IllegalArgumentException.class, () -> new MyPriority_Heap<Integer>(0));
        }

        @Test
        @DisplayName("negative initial capacity throws IllegalArgumentException")
        void negativeInitialCapacityThrows() {
            assertThrows(IllegalArgumentException.class, () -> new MyPriority_Heap<Integer>(-5));
        }
    }

    /**
     * Minimal Comparable domain object used only to prove MyHeap works
     * generically, not just with Integer. Not part of the production
     * ServiceRequest model — swap in the real class once Team C/B agree
     * on its shape.
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