package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class MyMapTest {

    @Nested
    @DisplayName("Normal case")
    class NormalCase {
        @Test
        @DisplayName("put then get returns the value")
        void putThenGetReturnsValue() {
            MyMap<Integer, String> map = new MyMap<>();
            map.put(0, "Main Gate");
            assertEquals("Main Gate", map.get(0));
        }

        @Test
        @DisplayName("containsKey reflects presence correctly")
        void containsKeyReflectsPresence() {
            MyMap<Integer, String> map = new MyMap<>();
            map.put(1, "Balme Library");
            assertTrue(map.containsKey(1));
            assertFalse(map.containsKey(99));
        }

        @Test
        @DisplayName("remove deletes the key")
        void removeDeletesKey() {
            MyMap<Integer, String> map = new MyMap<>();
            map.put(2, "SRC");
            map.remove(2);
            assertFalse(map.containsKey(2));
        }

        @Test
        @DisplayName("size reflects distinct keys stored")
        void sizeReflectsDistinctKeys() {
            MyMap<Integer, String> map = new MyMap<>();
            map.put(1, "a");
            map.put(2, "b");
            assertEquals(2, map.size());
        }
    }

    @Nested
    @DisplayName("Boundary case")
    class BoundaryCase {
        @Test
        @DisplayName("empty map: get returns null, containsKey is false")
        void emptyMapGetReturnsNull() {
            MyMap<Integer, String> map = new MyMap<>();
            assertNull(map.get(0));
            assertFalse(map.containsKey(0));
        }

        @Test
        @DisplayName("put with existing key overwrites the value, size unchanged")
        void putExistingKeyOverwrites() {
            MyMap<Integer, String> map = new MyMap<>();
            map.put(1, "old");
            map.put(1, "new");
            assertEquals("new", map.get(1));
            assertEquals(1, map.size());
        }

        @Test
        @DisplayName("removing a non-existent key does not throw")
        void removeNonExistentKeyIsNoOp() {
            MyMap<Integer, String> map = new MyMap<>();
            assertDoesNotThrow(() -> map.remove(42));
        }
    }

    @Nested
    @DisplayName("Invalid input")
    class InvalidInput {
        @Test
        @DisplayName("put with null key throws (delegates to HashTable's null-key guard)")
        void putNullKeyThrows() {
            MyMap<String, Integer> map = new MyMap<>();
            assertThrows(IllegalArgumentException.class, () -> map.put(null, 1));
        }

        @Test
        @DisplayName("get with null key throws")
        void getNullKeyThrows() {
            MyMap<String, Integer> map = new MyMap<>();
            assertThrows(IllegalArgumentException.class, () -> map.get(null));
        }
    }
}
