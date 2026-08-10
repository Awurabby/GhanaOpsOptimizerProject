package com.team.smartops.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * OWNER: whoever owns MyLinkedList (Team C1).
 * Template only -- copy this pattern for every structure/algorithm you own.
 * The brief requires normal + boundary + invalid-input coverage per structure.
 */
public class MyLinkedListTest {

    @Test
    void normalCase_addAndIterateInOrder() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("b");
        list.addLast("c");
        list.addFirst("a");
        // Expected order: a <-> b <-> c

        StringBuilder sb = new StringBuilder();
        for (String value : list) {
            sb.append(value);
        }
        assertEquals("abc", sb.toString());
        assertEquals(3, list.size());
    }

    @Test
    void normalCase_insertAfterAndRemove() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(4);
        list.insertAfter(2, 3); // 1 <-> 2 <-> 3 <-> 4

        StringBuilder sb = new StringBuilder();
        for (int v : list) sb.append(v);
        assertEquals("1234", sb.toString());

        assertTrue(list.remove(1)); // remove head value
        sb = new StringBuilder();
        for (int v : list) sb.append(v);
        assertEquals("234", sb.toString());

        assertTrue(list.remove(4)); // remove tail value
        sb = new StringBuilder();
        for (int v : list) sb.append(v);
        assertEquals("23", sb.toString());
    }

    @Test
    void boundaryCase_emptyAndSingleNodeList() {
        MyLinkedList<Integer> list = new MyLinkedList<>();

        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        assertFalse(list.iterator().hasNext());

        list.addFirst(99);
        assertEquals(1, list.size());
        assertTrue(list.remove(99));
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void invalidInput_insertAfterMissingTargetThrows() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("x");
        list.addLast("y");

        assertThrows(java.util.NoSuchElementException.class,
                () -> list.insertAfter("not-present", "z"));

        // remove on a missing target should NOT throw -- it returns false
        assertFalse(list.remove("not-present"));

        // calling next() past the end of an empty list must throw
        MyLinkedList<Integer> empty = new MyLinkedList<>();
        assertThrows(java.util.NoSuchElementException.class,
                () -> empty.iterator().next());
    }
}