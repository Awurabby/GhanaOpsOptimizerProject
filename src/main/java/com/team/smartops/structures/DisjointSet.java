package com.team.smartops.structures;

/**
 * OWNER: Team C (C4).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 */
public class DisjointSet {
    private final int[] parent;
    private final int[] rank;
    private int numSets;

    public DisjointSet(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        parent = new int[n];
        rank = new int[n];
        numSets = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    private void validate(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IndexOutOfBoundsException("index " + x + " out of bounds for " + parent.length + " elements");
        }
    }

    public int find(int x) {
        validate(x);
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        validate(x);
        validate(y);
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        numSets--;
    }

    public boolean connected(int x, int y) {
        validate(x);
        validate(y);
        return find(x) == find(y);
    }

    public int getNumSets() {
        return numSets;
    }
}