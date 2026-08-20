package com.team.smartops.structures;

/**
 * OWNER: Team C (C1).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 *
 * A manually-resized array — the idea behind ArrayList, built by hand.
 * Raw storage is a plain Object[]; no built-in collection types are used.
 */
public class DynamicArray<T> {
    private Object[] data;
    private int size;

    public DynamicArray() {
        data = new Object[4];   // start small on purpose, to force resizing
        size = 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(T value) {
        if (size == data.length) resize();
        data[size++] = value;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + " out of bounds for size " + size);
        return (T) data[index];
    }

    public void set(int index, T value) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + " out of bounds for size " + size);
        data[index] = value;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index " + index + " out of bounds for size " + size);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
    }

    private void resize() {
        Object[] bigger = new Object[data.length * 2];
        for (int i = 0; i < data.length; i++) bigger[i] = data[i];
        data = bigger;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}