package com.team.smartops.structures;

/**
 * OWNER: Team C (C4).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */
public class MySet<T> {
    private final HashTable<T, Boolean> table;

    public MySet() {
        table = new HashTable<>(16);
    }

    public void add(T element) { table.put(element, true); }
    public boolean contains(T element) { return table.get(element) != null; }
    public void remove(T element) { table.remove(element); }
    public int size() { return table.size(); }
}