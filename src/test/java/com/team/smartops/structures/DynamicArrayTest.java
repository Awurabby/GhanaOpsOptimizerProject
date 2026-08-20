package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * OWNER: whoever owns DynamicArray (Team C1).
 * Template only -- copy this pattern for every structure/algorithm you own.
 * The brief requires normal + boundary + invalid-input coverage per structure.
 */
public class DynamicArrayTest {

    @Test
    void normalCase_insertAndGet() {
        DynamicArray<String> arr = new DynamicArray<>();
        arr.insert("a");
        arr.insert("b");
        arr.insert("c");

        assertEquals(3, arr.size());
        assertEquals("a", arr.get(0));
        assertEquals("b", arr.get(1));
        assertEquals("c", arr.get(2));

        arr.set(1, "B");
        assertEquals("B", arr.get(1));

        arr.remove(0); // shift left: [B, c]
        assertEquals(2, arr.size());
        assertEquals("B", arr.get(0));
        assertEquals("c", arr.get(1));
    }

    @Test
    void resizeEvidence_capacityDoublesAcrossNineInserts() {
        // Evidence required by the brief: force >= 2 resizes, trace capacity growth.
        DynamicArray<Integer> arr = new DynamicArray<>();
        assertEquals(4, arr.capacity());

        for (int i = 1; i <= 9; i++) {
            arr.insert(i);
        }
        // capacity 4 -> 8 -> 16 after 9 inserts
        assertEquals(16, arr.capacity());
        assertEquals(9, arr.size());
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, (int) arr.get(i));
        }
    }

    @Test
    void boundaryCase_emptyArray() {
        DynamicArray<Integer> arr = new DynamicArray<>();

        assertEquals(0, arr.size());
        assertTrue(arr.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(0));

        arr.insert(42);
        assertEquals(1, arr.size());
        arr.remove(0);
        assertEquals(0, arr.size());
        assertTrue(arr.isEmpty());
    }

    @Test
    void invalidInput_negativeIndexThrows() {
        DynamicArray<Integer> arr = new DynamicArray<>();
        arr.insert(10);

        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.set(-1, 99));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(5));
    }
}