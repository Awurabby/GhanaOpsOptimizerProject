package com.team.smartops.structures;

/**
 * OWNER: Team C (C2).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */

public class MyDeque<T> {
    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public MyDeque() {
        head = null;
        tail = null;
        size = 0;
    }

    public void addFront(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    public void addRear(T value) {
        Node<T> node = new Node<>(value);
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public T removeFront() {
        if (isEmpty()) throw new CollectionStateException("Cannot remove from an empty deque");
        T value = head.value;
        head = head.next;
        if (head == null) {
            tail = null; // deque is now empty
        } else {
            head.prev = null;
        }
        size--;
        return value;
    }

    public T removeRear() {
        if (isEmpty()) throw new CollectionStateException("Cannot remove from an empty deque");
        T value = tail.value;
        tail = tail.prev;
        if (tail == null) {
            head = null; // deque is now empty
        } else {
            tail.next = null;
        }
        size--;
        return value;
    }

    public T peekFront() {
        if (isEmpty()) throw new CollectionStateException("Cannot peek an empty deque");
        return head.value;
    }

    public T peekRear() {
        if (isEmpty()) throw new CollectionStateException("Cannot peek an empty deque");
        return tail.value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
