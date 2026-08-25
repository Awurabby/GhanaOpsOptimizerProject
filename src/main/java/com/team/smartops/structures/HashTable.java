package com.team.smartops.structures;

/**
 * OWNER: Team C (C4).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 */
public class HashTable<K, V> {

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V>[] buckets;
    private int size;
    private int tableSize;
    private static final double LOAD_FACTOR_LIMIT = 0.75;

    @SuppressWarnings("unchecked")
    public HashTable(int tableSize) {
        if (tableSize <= 0) {
            throw new IllegalArgumentException("tableSize must be positive");
        }
        this.tableSize = tableSize;
        this.buckets = new Node[tableSize];
        this.size = 0;
    }

    private void validateKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % tableSize;
    }

    public void put(K key, V value) {
        validateKey(key);
        int bucket = hash(key);
        Node<K, V> current = buckets[bucket];
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[bucket];
        buckets[bucket] = newNode;
        size++;
        if ((double) size / tableSize > LOAD_FACTOR_LIMIT) {
            resize();
        }
    }

    public V get(K key) {
        validateKey(key);
        int bucket = hash(key);
        Node<K, V> current = buckets[bucket];
        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }
        return null;
    }

    public void remove(K key) {
        validateKey(key);
        int bucket = hash(key);
        Node<K, V> current = buckets[bucket];
        Node<K, V> prev = null;
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[bucket] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        tableSize = tableSize * 2;
        buckets = new Node[tableSize];
        size = 0;
        for (Node<K, V> head : oldBuckets) {
            Node<K, V> current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    public int countCollisions() {
        int collisions = 0;
        for (Node<K, V> head : buckets) {
            if (head != null && head.next != null) {
                Node<K, V> current = head.next;
                while (current != null) {
                    collisions++;
                    current = current.next;
                }
            }
        }
        return collisions;
    }

    public int size() { return size; }
    public int tableSize() { return tableSize; }
    public double loadFactor() { return (double) size / tableSize; }
}