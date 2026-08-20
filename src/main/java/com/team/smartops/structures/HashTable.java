package com.team.smartops.structures;
public class HashTable<K, V> {

    // ── INNER CLASS: chain node ────────────────────────────────────────────────
    private static class Node<K, V> {
        K       key;
        V       value;
        Node<K, V> next;

        Node(K key, V value) {
            this.key   = key;
            this.value = value;
            this.next  = null;
        }
    }

    // ── FIELDS ─────────────────────────────────────────────────────────────────
    private Node<K, V>[] buckets;
    private int          size;
    private int          tableSize;
    private static final double LOAD_FACTOR_LIMIT = 0.75;

    // ── CONSTRUCTOR ────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public HashTable(int tableSize) {
        this.tableSize = tableSize;
        this.buckets   = new Node[tableSize];
        this.size      = 0;
    }

    // ── HASH FUNCTION ──────────────────────────────────────────────────────────
    /**
     * hash - maps a key to a bucket index.
     * Uses Java's built-in hashCode() then modulo by table size.
     * Math.abs() prevents negative indices.
     *
     * Pseudocode:
     *   HASH(key): return |key.hashCode()| mod tableSize
     */
    private int hash(K key) {
        return Math.abs(key.hashCode()) % tableSize;
    }

    // ── PUT ────────────────────────────────────────────────────────────────────
    /**
     * put - insert or update a key-value pair.
     * If key exists in chain: update its value.
     * If key not found: add new node at front of chain.
     * After insert: resize if load factor > 0.75.
     *
     * Pseudocode:
     *   PUT(key, value):
     *     bucket <- HASH(key)
     *     walk chain at bucket:
     *       if key found -> update value, return
     *     add new node at front of chain
     *     size++
     *     if load factor > 0.75 -> RESIZE()
     */
    public void put(K key, V value) {
        int bucket = hash(key);
        Node<K, V> current = buckets[bucket];

        // walk chain — update if key already exists
        while (current != null) {
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // key not found — add at front of chain (O(1))
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next    = buckets[bucket];
        buckets[bucket] = newNode;
        size++;

        // resize if load factor exceeded
        if ((double) size / tableSize > LOAD_FACTOR_LIMIT) {
            resize();
        }
    }

    // ── GET ────────────────────────────────────────────────────────────────────
    /**
     * get - retrieve value by key.
     * Hash to bucket, walk chain until key matches.
     * Returns null if key not found.
     *
     * Pseudocode:
     *   GET(key):
     *     bucket <- HASH(key)
     *     walk chain at bucket:
     *       if key matches -> return value
     *     return null
     */
    public V get(K key) {
        int bucket = hash(key);
        Node<K, V> current = buckets[bucket];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; // not found
    }

    // ── REMOVE ─────────────────────────────────────────────────────────────────
    /**
     * remove - delete a key-value pair.
     * Tracks previous node to bypass the removed node in the chain.
     *
     * Pseudocode:
     *   REMOVE(key):
     *     bucket <- HASH(key)
     *     walk chain tracking prev and current:
     *       if key matches:
     *         if prev == null: buckets[bucket] <- current.next
     *         else: prev.next <- current.next
     *         size--
     */
    public void remove(K key) {
        int bucket = hash(key);
        Node<K, V> current = buckets[bucket];
        Node<K, V> prev    = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    buckets[bucket] = current.next; // removing head
                } else {
                    prev.next = current.next;        // bypass node
                }
                size--;
                return;
            }
            prev    = current;
            current = current.next;
        }
    }

    // ── RESIZE ─────────────────────────────────────────────────────────────────
    /**
     * resize - doubles the table size and rehashes all entries.
     * Called automatically when load factor exceeds 0.75.
     *
     * Pseudocode:
     *   RESIZE():
     *     oldBuckets <- current array
     *     tableSize <- tableSize * 2
     *     create new empty bucket array
     *     for every entry in oldBuckets: rehash and re-insert
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldBuckets = buckets;
        tableSize = tableSize * 2;
        buckets   = new Node[tableSize];
        size      = 0;

        for (Node<K, V> head : oldBuckets) {
            Node<K, V> current = head;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
        System.out.println("[HashTable] Resized to tableSize=" + tableSize);
    }

    // ── STATS ──────────────────────────────────────────────────────────────────
    /** Count entries that collided into the same bucket. */
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

    public int    size()        { return size; }
    public int    tableSize()   { return tableSize; }
    public double loadFactor()  { return (double) size / tableSize; }

    /** Print all entries — for debugging and trace evidence. */
    public void print() {
        System.out.println("\nHashTable (size=" + size + ", tableSize=" + tableSize
            + ", loadFactor=" + String.format("%.2f", loadFactor()) + "):");
        for (int i = 0; i < tableSize; i++) {
            if (buckets[i] != null) {
                System.out.print("  Bucket " + i + ": ");
                Node<K, V> cur = buckets[i];
                while (cur != null) {
                    System.out.print("[" + cur.key + "=" + cur.value + "] ");
                    cur = cur.next;
                }
                System.out.println();
            }
        }
        System.out.println("  Collisions: " + countCollisions());
    }

    // ── DEMO ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== UG Campus HashTable Demo ===");
        System.out.println("Storing service requests by requestId\n");

        HashTable<String, String> table = new HashTable<>(5);

        table.put("REQ001", "Main Gate - maintenance request");
        table.put("REQ002", "Balme Library - AC repair");
        table.put("REQ003", "SRC - electrical fault");
        table.put("REQ004", "Commonwealth Hall - plumbing");
        table.print();

        System.out.println("\nGET REQ002: " + table.get("REQ002"));
        System.out.println("GET REQ999: " + table.get("REQ999") + " (not found)");

        table.remove("REQ002");
        System.out.println("\nAfter removing REQ002:");
        table.print();

        System.out.println("\n--- Load Factor Experiment ---");
        System.out.printf("%-15s %-12s %-15s %-12s%n",
            "TableSize", "Keys", "LoadFactor", "Collisions");
        System.out.println("-".repeat(54));

        for (int tableSize : new int[]{10, 10, 10}) {
            HashTable<String, String> t = new HashTable<>(tableSize);
            int[] keyCounts = {5, 10, 20};
            for (int keys : keyCounts) {
                t = new HashTable<>(tableSize);
                for (int i = 1; i <= keys; i++) {
                    t.put("KEY" + String.format("%03d", i), "value" + i);
                }
                System.out.printf("%-15d %-12d %-15.2f %-12d%n",
                    t.tableSize(), keys, t.loadFactor(), t.countCollisions());
            }
        }
    }
}
