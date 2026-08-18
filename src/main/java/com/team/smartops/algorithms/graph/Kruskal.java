package com.team.smartops.algorithms.graph;

import com.team.smartops.structures.DisjointSet;
import com.team.smartops.structures.DynamicArray;

/**
 * OWNER: Team E.
 * Minimum spanning tree (Kruskal) -- report total cost + edge list.
 *
 * Uses the hand-built structures.DisjointSet for union-find and the
 * hand-built structures.DynamicArray for the edge lists.
 */
public class Kruskal {

    /**
     * A single undirected edge (u, v) with weight w.
     * NOTE: this is a plain array {u, v, w} so no custom structure is needed.
     */
    private static class Edge {
        int u;
        int v;
        int w;

        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    /**
     * Builds the MST edge list using Kruskal's algorithm.
     *
     * @param adjacencyMatrix symmetric weight matrix (0 = no edge)
     * @return list of MST edges as {u, v} pairs
     */
    public static DynamicArray<int[]> buildMST(int[][] adjacencyMatrix) {
        int V = adjacencyMatrix.length;

        // ---- 1. Collect all edges (u, v, w) from the upper triangle ----
        // Custom structure: structures.DynamicArray.
        DynamicArray<Edge> edges = new DynamicArray<>();
        for (int u = 0; u < V; u++) {
            for (int v = u + 1; v < V; v++) {
                if (adjacencyMatrix[u][v] != 0) {
                    edges.insert(new Edge(u, v, adjacencyMatrix[u][v]));
                }
            }
        }

        // ---- 2. Sort edges by weight ascending ----
        // Custom sort on the DynamicArray (no java.util.Arrays).
        sortEdges(edges);

        // ---- 3. Union-find to detect cycles ----
        // Custom structure: structures.DisjointSet.
        DisjointSet dsu = new DisjointSet();
        for (int i = 0; i < V; i++) {
            dsu.makeSet(i);
        }

        // ---- 4. Add edges that do not form a cycle until V-1 edges ----
        // Custom structure: structures.DynamicArray.
        DynamicArray<int[]> mstEdges = new DynamicArray<>();

        for (int i = 0; i < edges.size(); i++) {
            if (mstEdges.size() == V - 1) {
                break; // MST complete
            }
            Edge e = edges.get(i);
            int rootU = dsu.find(e.u);
            int rootV = dsu.find(e.v);
            if (rootU != rootV) {
                dsu.union(rootU, rootV);
                mstEdges.insert(new int[]{e.u, e.v});
            }
        }

        return mstEdges;
    }

    /**
     * Sorts the edge list by weight ascending using a simple insertion sort.
     * Operates directly on the custom DynamicArray.
     */
    private static void sortEdges(DynamicArray<Edge> edges) {
        for (int i = 1; i < edges.size(); i++) {
            Edge key = edges.get(i);
            int j = i - 1;
            while (j >= 0 && edges.get(j).w > key.w) {
                edges.set(j + 1, edges.get(j));
                j--;
            }
            edges.set(j + 1, key);
        }
    }

    /**
     * Computes the total cost of the MST from the adjacency matrix.
     * Sums the weight of every edge present in the MST edge list.
     *
     * @param adjacencyMatrix symmetric weight matrix (0 = no edge)
     * @param mstEdges        the edge list produced by buildMST
     * @return total MST cost
     */
    public static int getCost(int[][] adjacencyMatrix, DynamicArray<int[]> mstEdges) {
        int totalCost = 0;
        for (int i = 0; i < mstEdges.size(); i++) {
            int[] e = mstEdges.get(i);
            totalCost += adjacencyMatrix[e[0]][e[1]];
        }
        return totalCost;
    }

}
