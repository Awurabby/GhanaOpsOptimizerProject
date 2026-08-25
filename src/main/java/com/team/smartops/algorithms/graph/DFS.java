package com.team.smartops.algorithms.graph;

import java.util.Arrays;

import com.team.smartops.structures.MyStack;
/**
 * OWNER: Team E.
 * Traversal order over the local network graph.
 */
public class DFS {

    private DFS() {
    }

    public static int[] traverse(int[][] adjacencyMatrix, int startVertex) {
        validateInputs(adjacencyMatrix, startVertex);

        boolean[] visited = new boolean[adjacencyMatrix.length];
        int[] order = new int[adjacencyMatrix.length];
        int orderSize = 0;

        MyStack<Integer> stack = new MyStack<>();
        stack.push(startVertex);

        while (!stack.isEmpty()) {
            int currentVertex = stack.pop();

            if (visited[currentVertex]) {
                continue;
            }

            visited[currentVertex] = true;
            order[orderSize++] = currentVertex;

            for (int nextVertex = adjacencyMatrix[currentVertex].length - 1; nextVertex >= 0; nextVertex--) {
                if (adjacencyMatrix[currentVertex][nextVertex] != 0 && !visited[nextVertex]) {
                    stack.push(nextVertex);
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
