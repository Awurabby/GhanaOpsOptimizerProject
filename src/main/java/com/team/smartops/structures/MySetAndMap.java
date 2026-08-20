package com.team.smartops.structures;

// ── MySet ─────────────────────────────────────────────────────────────────────
class MySet<T> {
    private HashTable<T, Boolean> table;

    public MySet() {
        table = new HashTable<>(16);
    }

    /** add - insert element into the set. O(1) average. */
    public void add(T element) {
        table.put(element, true);
    }

    /** contains - check if element is in the set. O(1) average. */
    public boolean contains(T element) {
        return table.get(element) != null;
    }

    /** remove - delete element from set. O(1) average. */
    public void remove(T element) {
        table.remove(element);
    }

    public int size() { return table.size(); }
}


// ── MyMap ─────────────────────────────────────────────────────────────────────
class MyMap<K, V> {
    private HashTable<K, V> table;

    public MyMap() {
        table = new HashTable<>(16);
    }

    public void put(K key, V value) { table.put(key, value); }
    public V    get(K key)          { return table.get(key); }
    public void remove(K key)       { table.remove(key); }
    public boolean containsKey(K key) { return table.get(key) != null; }
    public int  size()              { return table.size(); }
}


// ── DEMO ──────────────────────────────────────────────────────────────────────
public class MySetAndMap {
    public static void main(String[] args) {

        System.out.println("=== MySet Demo: Visited Campus Locations ===");
        MySet<String> visited = new MySet<>();

        visited.add("Main Gate");
        visited.add("Balme Library");
        visited.add("SRC");

        System.out.println("Visited Main Gate?     " + visited.contains("Main Gate"));    // true
        System.out.println("Visited Engineering?   " + visited.contains("Engineering Block")); // false

        visited.remove("SRC");
        System.out.println("After removing SRC, visited SRC? " + visited.contains("SRC")); // false

        System.out.println("\n=== MyMap Demo: LocationId -> LocationName ===");
        MyMap<Integer, String> locationMap = new MyMap<>();

        locationMap.put(0, "Main Gate");
        locationMap.put(1, "Balme Library");
        locationMap.put(2, "SRC");
        locationMap.put(3, "Commonwealth Hall");
        locationMap.put(4, "Engineering Block");

        System.out.println("Location 2: " + locationMap.get(2));    // SRC
        System.out.println("Location 4: " + locationMap.get(4));    // Engineering Block
        System.out.println("Location 9: " + locationMap.get(9));    // null - not found
        System.out.println("Has key 3?  " + locationMap.containsKey(3)); // true
    }
}
