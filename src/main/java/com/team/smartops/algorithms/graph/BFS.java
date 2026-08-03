package com.team.smartops.algorithms.graph;

import java.util.Arrays;
import com.team.smartops.structures.MyQueue;

/**
 * OWNER: Team E.
 * Reachability from a dispatch point.
 */
public class BFS {

    private BFS() {
    }

    public static int[] traverse(int[][] adjacencyMatrix, int startVertex) {
        validateInputs(adjacencyMatrix, startVertex);

        boolean[] visited = new boolean[adjacencyMatrix.length];
        int[] order = new int[adjacencyMatrix.length];
        int orderSize = 0;

        MyQueue<Integer> queue = new MyQueue<>();
        visited[startVertex] = true;
        queue.enqueue(startVertex);

        while (!queue.isEmpty()) {
            int currentVertex = queue.dequeue();
            order[orderSize++] = currentVertex;

            for (int nextVertex = 0; nextVertex < adjacencyMatrix[currentVertex].length; nextVertex++) {
                if (adjacencyMatrix[currentVertex][nextVertex] != 0 && !visited[nextVertex]) {
                    visited[nextVertex] = true;
                    queue.enqueue(nextVertex);
                }
            }
        }

        return Arrays.copyOf(order, orderSize);
    }

    private static void validateInputs(int[][] adjacencyMatrix, int startVertex) {
        if (adjacencyMatrix == null) {
            throw new IllegalArgumentException("Adjacency matrix cannot be null.");
        }
        if (adjacencyMatrix.length == 0) {
            throw new IllegalArgumentException("Adjacency matrix cannot be empty.");
        }
        if (startVertex < 0 || startVertex >= adjacencyMatrix.length) {
            throw new IllegalArgumentException("Start vertex is out of bounds.");
        }
        for (int row = 0; row < adjacencyMatrix.length; row++) {
            if (adjacencyMatrix[row] == null || adjacencyMatrix[row].length != adjacencyMatrix.length) {
                throw new IllegalArgumentException("Adjacency matrix must be square.");
            }
        }
    }
}
