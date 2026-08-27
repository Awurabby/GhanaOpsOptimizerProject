package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("put then get returns the stored value")
        void putThenGetReturnsValue() {
            HashTable<String, Integer> table = new HashTable<>(8);
            table.put("a", 1);
            assertEquals(1, table.get("a"));
        }

        @Test
        @DisplayName("put with existing key updates the value, not size")
        void putExistingKeyUpdates() {
            HashTable<String, Integer> table = new HashTable<>(8);
            table.put("a", 1);
            table.put("a", 2);
            assertEquals(2, table.get("a"));
            assertEquals(1, table.size());
        }

        @Test
        @DisplayName("remove deletes the key")
        void removeDeletesKey() {
            HashTable<String, Integer> table = new HashTable<>(8);
            table.put("a", 1);
            table.remove("a");
            assertNull(table.get("a"));
            assertEquals(0, table.size());
        }

        @Test
        @DisplayName("get on a missing key returns null")
        void getMissingKeyReturnsNull() {
            HashTable<String, Integer> table = new HashTable<>(8);
            assertNull(table.get("missing"));
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("resizes automatically past the load factor limit")
        void resizesPastLoadFactor() {
            HashTable<Integer, String> table = new HashTable<>(4);
            int originalCapacity = table.tableSize();
            for (int i = 0; i < 10; i++) {
                table.put(i, "v" + i);
            }
            assertTrue(table.tableSize() > originalCapacity);
            for (int i = 0; i < 10; i++) {
                assertEquals("v" + i, table.get(i));
            }
        }

        @Test
        @DisplayName("colliding keys in a small table are all retrievable")
        void collidingKeysAllRetrievable() {
            HashTable<Integer, String> table = new HashTable<>(1); // forces every key into bucket 0
            table.put(1, "one");
            table.put(2, "two");
            table.put(3, "three");
            assertEquals("one", table.get(1));
            assertEquals("two", table.get(2));
            assertEquals("three", table.get(3));
        }

        @Test
        @DisplayName("removing from an empty table does nothing and does not throw")
        void removeFromEmptyTableIsNoOp() {
            HashTable<String, Integer> table = new HashTable<>(8);
            assertDoesNotThrow(() -> table.remove("nothing-here"));
            assertEquals(0, table.size());
        }

        @Test
        @DisplayName("handles Integer.MIN_VALUE hashCode without negative index crash")
        void handlesIntegerMinValueHashCode() {
            HashTable<Integer, String> table = new HashTable<>(8);
            assertDoesNotThrow(() -> table.put(Integer.MIN_VALUE, "min_val"));
            assertEquals("min_val", table.get(Integer.MIN_VALUE));
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("constructing with zero or negative table size throws")
        void nonPositiveTableSizeThrows() {
            assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(0));
            assertThrows(IllegalArgumentException.class, () -> new HashTable<String, Integer>(-2));
        }

        @Test
        @DisplayName("put with null key throws")
        void putNullKeyThrows() {
            HashTable<String, Integer> table = new HashTable<>(8);
            assertThrows(IllegalArgumentException.class, () -> table.put(null, 1));
        }

        @Test
        @DisplayName("get with null key throws")
        void getNullKeyThrows() {
            HashTable<String, Integer> table = new HashTable<>(8);
            assertThrows(IllegalArgumentException.class, () -> table.get(null));
        }
    }
}
