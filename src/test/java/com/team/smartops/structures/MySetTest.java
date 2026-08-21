package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class MySetTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("add then contains returns true")
        void addThenContainsTrue() {
            MySet<String> set = new MySet<>();
            set.add("Main Gate");
            assertTrue(set.contains("Main Gate"));
        }

        @Test
        @DisplayName("remove deletes the element")
        void removeDeletesElement() {
            MySet<String> set = new MySet<>();
            set.add("SRC");
            set.remove("SRC");
            assertFalse(set.contains("SRC"));
        }

        @Test
        @DisplayName("size reflects distinct elements added")
        void sizeReflectsElements() {
            MySet<String> set = new MySet<>();
            set.add("A");
            set.add("B");
            set.add("A"); // duplicate
            assertEquals(2, set.size());
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("empty set: contains returns false, size is 0")
        void emptySetContainsFalse() {
            MySet<String> set = new MySet<>();
            assertFalse(set.contains("anything"));
            assertEquals(0, set.size());
        }

        @Test
        @DisplayName("removing a non-existent element does not throw")
        void removeNonExistentIsNoOp() {
            MySet<String> set = new MySet<>();
            assertDoesNotThrow(() -> set.remove("nothing"));
        }

        @Test
        @DisplayName("adding the same element twice keeps size at one")
        void addingSameElementTwiceKeepsSizeOne() {
            MySet<String> set = new MySet<>();
            set.add("dup");
            set.add("dup");
            assertEquals(1, set.size());
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("add with null element throws (delegates to HashTable's null-key guard)")
        void addNullThrows() {
            MySet<String> set = new MySet<>();
            assertThrows(IllegalArgumentException.class, () -> set.add(null));
        }

        @Test
        @DisplayName("contains with null element throws")
        void containsNullThrows() {
            MySet<String> set = new MySet<>();
            assertThrows(IllegalArgumentException.class, () -> set.contains(null));
        }
    }
}
