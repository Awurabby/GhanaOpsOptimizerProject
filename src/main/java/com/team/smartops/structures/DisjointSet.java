package com.team.smartops.structures;
public class DisjointSet {

    private int[] parent;
    private int[] rank;
    private int   numSets;

    /**
     * makeSet - initialise n disjoint sets.
     * Each campus location starts as its own isolated group.
     *
     * @param n number of elements (campus locations)
     */
    public DisjointSet(int n) {
        parent  = new int[n];
        rank    = new int[n];
        numSets = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;   // every element is its own root
            rank[i]   = 0;   // all trees start at height 0
        }
    }

    /**
     * find - returns the root representative of x's group.
     * Uses PATH COMPRESSION: after finding root, all nodes
     * on the path point directly to root (flattens the tree).
     *
     * Pseudocode:
     *   FIND(x):
     *     if parent[x] != x:
     *       parent[x] <- FIND(parent[x])   // path compression
     *     return parent[x]
     *
     * @param x element to find
     * @return root of x's group
     */
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    /**
     * union - merges the groups containing x and y.
     * Uses UNION BY RANK: shorter tree goes under taller tree
     * to keep the overall tree as flat as possible.
     *
     * Pseudocode:
     *   UNION(x, y):
     *     rootX <- FIND(x)
     *     rootY <- FIND(y)
     *     if rootX == rootY: return   // already same group
     *     if rank[rootX] < rank[rootY]: parent[rootX] <- rootY
     *     else if rank[rootX] > rank[rootY]: parent[rootY] <- rootX
     *     else: parent[rootY] <- rootX; rank[rootX]++
     *
     * @param x first element
     * @param y second element
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) return; // already in same group

        // union by rank — attach shorter tree under taller
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

    /**
     * connected - checks if two campus locations are in the same zone.
     * Used by Kruskal to avoid adding an edge that creates a cycle.
     *
     * @param x first location
     * @param y second location
     * @return true if same group, false otherwise
     */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public int getNumSets() { return numSets; }

    /**
     * printState - prints current parent array for trace tables.
     * Use this to generate evidence for your submission.
     */
    public void printState() {
        System.out.print("parent = [");
        for (int i = 0; i < parent.length; i++) {
            System.out.print(parent[i]);
            if (i < parent.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    // ── DEMO ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== UG Campus DisjointSet Demo ===");
        System.out.println("Locations: 0=MainGate 1=Balme 2=SRC 3=MedSchool 4=LegonHall 5=EngBlock");
        System.out.println();

        DisjointSet ds = new DisjointSet(6);
        System.out.print("Initial   "); ds.printState();

        ds.union(0, 2); // Main Gate connects to SRC
        System.out.print("union(0,2)"); ds.printState();

        ds.union(2, 4); // SRC connects to Legon Hall
        System.out.print("union(2,4)"); ds.printState();

        ds.union(3, 5); // Med School connects to Eng Block
        System.out.print("union(3,5)"); ds.printState();

        System.out.println();
        System.out.println("connected(0,4) Main Gate & Legon Hall? " + ds.connected(0, 4)); // true
        System.out.println("connected(1,2) Balme & SRC?           " + ds.connected(1, 2)); // false
        System.out.println("connected(3,5) MedSchool & EngBlock?  " + ds.connected(3, 5)); // true
    }
}
