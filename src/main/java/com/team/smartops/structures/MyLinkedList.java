package com.team.smartops.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * OWNER: Team C (C1).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 *
 * A doubly linked list, nodes with next and prev pointers, built by hand.
 * No built-in collection types are used.
 */
public class MyLinkedList<T> implements Iterable<T> {

    private static class Node<T> {
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /**
     * Inserts newValue immediately after the first node holding target.
     * Throws NoSuchElementException if target is not found.
     */
    public void insertAfter(T target, T newValue) {
        Node<T> current = findNode(target);
        if (current == null) {
            throw new NoSuchElementException("target not found in list: " + target);
        }
        Node<T> node = new Node<>(newValue);
        node.prev = current;
        node.next = current.next;
        if (current.next != null) {
            current.next.prev = node;
        } else {
            tail = node; // current was the tail
        }
        current.next = node;
        size++;
    }

    /**
     * Removes the first node whose value equals target.
     * Returns true if a node was removed, false if target was not found.
     */
    public boolean remove(T target) {
        Node<T> current = findNode(target);
        if (current == null) return false;

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next; // removing head
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev; // removing tail
        }

        current.next = null;
        current.prev = null;
        size--;
        return true;
    }

    private Node<T> findNode(T target) {
        Node<T> current = head;
        while (current != null) {
            if (current.value == null ? target == null : current.value.equals(target)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cursor = head;

            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public T next() {
                if (cursor == null) throw new NoSuchElementException();
                T value = cursor.value;
                cursor = cursor.next;
                return value;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(" <-> ");
            current = current.next;
        }
        return sb.append("]").toString();
    }
}
