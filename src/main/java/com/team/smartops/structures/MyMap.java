package com.team.smartops.structures;

/**
 * OWNER: Team C (C4).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */

public class MyMap<K, V> {
    private final HashTable<K, V> table;

    public MyMap() {
        table = new HashTable<>(16);
    }

    public void put(K key, V value) { table.put(key, value); }
    public V get(K key) { return table.get(key); }
    public void remove(K key) { table.remove(key); }
    public boolean containsKey(K key) { return table.get(key) != null; }
    public int size() { return table.size(); }
}