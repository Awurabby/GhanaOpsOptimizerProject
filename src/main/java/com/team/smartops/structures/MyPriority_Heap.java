/**
 * OWNER: Team C (C3).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */
package com.team.smartops.structures;

/**
 * MyHeap<T> — a custom binary min-heap backed by a plain array.
 *
 * Used by the dispatch engine to always serve the request with the
 * lowest urgency/priority score first (extractMin returns the highest-priority
 * job, assuming lower score == more urgent — flip the compareTo() checks
 * below if your team defines urgency the other way round).
 *
 * Array layout (0-indexed):
 *   parent(i) = (i - 1) / 2
 *   left(i)   = 2*i + 1
 *   right(i)  = 2*i + 2
 *
 * No java.util.PriorityQueue is used anywhere in this class, per the
 * project brief's implementation constraints (Section 8).
 */
public class MyPriority_Heap<T extends Comparable<T>> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] data;
    private int size;

    public MyPriority_Heap() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * @param initialCapacity starting backing-array size; must be positive
     * @throws IllegalArgumentException if initialCapacity <= 0
     */
    public MyPriority_Heap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the minimum element without removing it.
     * Throws if the heap is empty — callers must check isEmpty() first,
     * or catch this, depending on how your team wants invalid-input
     * handling documented for the M9 correctness evidence.
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Heap is empty");
        }
        return (T) data[0];
    }

    /**
     * Inserts a value and restores the heap property by bubbling up.
     *
     * @throws IllegalArgumentException if value is null
     * @throws IllegalStateException if the heap has reached maximum capacity
     */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null into heap");
        }
        ensureCapacity();
        data[size] = value;
        bubbleUp(size);
        size++;
    }

    /**
     * Removes and returns the minimum element, restoring the heap
     * property by moving the last element to the root and heapifying down.
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    @SuppressWarnings("unchecked")
    public T extractMin() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Cannot extract from an empty heap");
        }

        T min = (T) data[0];
        int lastIndex = size - 1;

        data[0] = data[lastIndex];
        data[lastIndex] = null; // avoid memory leak / stale reference
        size--;

        if (size > 0) {
            heapifyDown(0);
        }

        return min;
    }

    // ---------- internal helpers ----------

    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    private int leftChildIndex(int i) {
        return 2 * i + 1;
    }

    private int rightChildIndex(int i) {
        return 2 * i + 2;
    }

    @SuppressWarnings("unchecked")
    private void bubbleUp(int i) {
        while (i > 0) {
            int parent = parentIndex(i);
            T current = (T) data[i];
            T parentVal = (T) data[parent];

            if (current.compareTo(parentVal) < 0) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void heapifyDown(int i) {
        while (true) {
            int left = leftChildIndex(i);
            int right = rightChildIndex(i);
            int smallest = i;

            if (left < size && ((T) data[left]).compareTo((T) data[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && ((T) data[right]).compareTo((T) data[smallest]) < 0) {
                smallest = right;
            }

            if (smallest == i) {
                break;
            }

            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        Object temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Largest array size the JVM can reliably allocate. Some JVMs reserve
     * a few header words, so Integer.MAX_VALUE itself can fail; this is the
     * same defensive bound java.util.ArrayList uses.
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void ensureCapacity() {
        if (size != data.length) {
            return;
        }

        int oldCapacity = data.length;
        if (oldCapacity >= MAX_ARRAY_SIZE) {
            throw new IllegalStateException("Heap has reached maximum capacity (" + MAX_ARRAY_SIZE + " elements)");
        }

        // oldCapacity * 2 without risking int overflow
        int newCapacity = oldCapacity > MAX_ARRAY_SIZE / 2 ? MAX_ARRAY_SIZE : oldCapacity * 2;

        Object[] resized = new Object[newCapacity];
        System.arraycopy(data, 0, resized, 0, oldCapacity);
        data = resized;
    }
}