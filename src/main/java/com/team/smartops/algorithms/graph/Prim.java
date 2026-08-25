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


        int V = adjacencyMatrix.length;
        int[] key = new int[V];
        int[] parent = new int[V];

        // vertext included in our current mst
        boolean[] mstSet = new boolean[adjacencyMatrix.length];

        // initialize all keys.
        for (int i=0; i<adjacencyMatrix.length; i++){
            mstSet[i] = false;
            key[i] = Integer.MAX_VALUE;
        }

        parent[0] = -1;
        key[0] = 0;

        for (int count =0; count < V; count++) {
            int minVertex = getMinWeightedVertex(key, mstSet);

            mstSet[minVertex] = true;

            for (int i = 0; i<V; i++){
                if (adjacencyMatrix[minVertex][i] != 0 && mstSet[i] == false && adjacencyMatrix[minVertex][i] < key[i]) {
                    parent[i] = minVertex;
                    key[i] = adjacencyMatrix[minVertex][i];
                }
            }
        }

        // build the edge list from parent[]
        // Custom structure: structures.DynamicArray.
        DynamicArray<int[]> edges = new DynamicArray<>();

        for (int i = 1; i < V; i++) {
            edges.insert(new int[]{parent[i], i});
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
