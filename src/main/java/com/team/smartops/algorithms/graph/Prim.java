package com.team.smartops.algorithms.graph;

import com.team.smartops.structures.DynamicArray;

public class Prim {



    public static int getMinWeightedVertex(int[] l, boolean[] trackingSet) {
        int curMin = Integer.MAX_VALUE;
        int curMinVertex = -1;

        for (int i = 0; i < l.length; i++) {
            // get the minimum value not yet included in the mst
            if (l[i] < curMin && trackingSet[i] == false) {
                curMin = l[i];
                curMinVertex = i;
            }
        }

        return curMinVertex;
    }


    // Custom structure: structures.DynamicArray.
    public static DynamicArray<int[]> buildMST(int[][] adjacencyMatrix, int startVertex) {

        if (adjacencyMatrix == null) {
            throw new IllegalArgumentException("Adjacency matrix cannot be null.");
        }
        if (adjacencyMatrix.length == 0) {
            throw new IllegalArgumentException("Adjacency matrix cannot be empty.");
        }
        if (startVertex < 0 || startVertex >= adjacencyMatrix.length) {
            throw new IllegalArgumentException("Start vertex is out of bounds.");
        }

        int V = adjacencyMatrix.length;
        int[] key = new int[V];
        int[] parent = new int[V];

        // vertex included in our current mst
        boolean[] mstSet = new boolean[V];

        // initialize all keys.
        for (int i = 0; i < V; i++) {
            mstSet[i] = false;
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }

        parent[startVertex] = -1;
        key[startVertex] = 0;

        for (int count = 0; count < V; count++) {
            int minVertex = getMinWeightedVertex(key, mstSet);

            // If no reachable vertex remains, the graph is disconnected.
            // Build a partial MST from the reachable component only.
            if (minVertex == -1) {
                break;
            }

            mstSet[minVertex] = true;

            for (int i = 0; i < V; i++) {
                if (adjacencyMatrix[minVertex][i] != 0 && mstSet[i] == false && adjacencyMatrix[minVertex][i] < key[i]) {
                    parent[i] = minVertex;
                    key[i] = adjacencyMatrix[minVertex][i];
                }
            }
        }

        // build the edge list from parent[]
        // Custom structure: structures.DynamicArray.
        DynamicArray<int[]> edges = new DynamicArray<>();

        for (int i = 0; i < V; i++) {
            if (parent[i] != -1) {
                edges.insert(new int[]{parent[i], i});
            }
        }

        return edges;
    }

    public static int getCost(int[] key) {
        int totalCost = 0;

        for (int i = 1; i < key.length; i++) {
            totalCost += key[i];
        }

        return totalCost;
    }

}
