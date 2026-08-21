package com.team.smartops.structures;

import java.util.function.Consumer;

/**
 * BST<T> — a standard (unbalanced) binary search tree.
 *
 * Supports insert, search, and inorder traversal. This class is also the
 * "before" comparison point for the red-black / balanced tree evidence
 * required by the brief (tree height on the same input data).
 *
 * Design decisions:
 *  - Duplicate values are not stored twice; inserting an existing value
 *    is a no-op. (Standard BST convention — swap this for a count-based
 *    approach if the team's data model needs duplicate tracking.)
 *  - No java.util.* collections are used, per the project brief's
 *    implementation constraints (Section 8).
 */
public class BST<T extends Comparable<T>> {

    private Node root;
    private int size;

    private class Node {
        T value;
        Node left;
        Node right;

        Node(T value) {
            this.value = value;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Height of the tree: number of edges on the longest root-to-leaf path.
     * An empty tree has height -1; a single-node tree has height 0.
     * Used to compare this unbalanced BST against the balanced tree on
     * identical input, per the brief's evidence requirement.
     */
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Inserts a value. If the value already exists, this is a no-op.
     *
     * @throws IllegalArgumentException if value is null
     */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null into BST");
        }
        root = insert(root, value);
    }

    private Node insert(Node node, T value) {
        if (node == null) {
            size++;
            return new Node(value);
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        }
        // cmp == 0: duplicate, no-op, tree unchanged

        return node;
    }

    /**
     * @return true if value exists in the tree
     * @throws IllegalArgumentException if value is null
     */
    public boolean search(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot search for null in BST");
        }
        return search(root, value);
    }

    private boolean search(Node node, T value) {
        if (node == null) {
            return false;
        }

        int cmp = value.compareTo(node.value);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return search(node.left, value);
        } else {
            return search(node.right, value);
        }
    }

    /**
     * Visits every value in ascending order, calling visitor.accept(value)
     * for each one. Use this to prove the tree is correctly ordered without
     * needing a java.util collection to hold the results.
     *
     * @throws IllegalArgumentException if visitor is null
     */
    public void inorderTraversal(Consumer<T> visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("visitor cannot be null");
        }
        inorderTraversal(root, visitor);
    }

    private void inorderTraversal(Node node, Consumer<T> visitor) {
        if (node == null) {
            return;
        }
        inorderTraversal(node.left, visitor);
        visitor.accept(node.value);
        inorderTraversal(node.right, visitor);
    }

    /**
     * Convenience wrapper around inorderTraversal that collects the
     * ascending-order values into a properly-typed array, for callers that
     * need the data rather than just visiting it (e.g. building evidence
     * docs).
     *
     * Because generic type parameters are erased at runtime, this class
     * cannot construct a real T[] on its own — it needs the caller to
     * supply an array constructor reference, the same pattern
     * java.util.Collection.toArray(IntFunction) uses. Example call:
     *
     *   Integer[] sorted = tree.toSortedArray(Integer[]::new);
     *
     * @throws IllegalArgumentException if generator is null
     */
    public T[] toSortedArray(java.util.function.IntFunction<T[]> generator) {
        if (generator == null) {
            throw new IllegalArgumentException("generator cannot be null");
        }
        T[] result = generator.apply(size);
        int[] index = {0}; // mutable counter for the lambda to close over
        inorderTraversal(root, value -> result[index[0]++] = value);
        return result;
    }

    /**
     * Deletes value if present. Handles all three standard BST cases:
     *   - leaf node: just remove it
     *   - one child: replace the node with its child
     *   - two children: replace the node's value with its inorder
     *     successor (the minimum of the right subtree), then delete that
     *     successor node from the right subtree (which is guaranteed to
     *     have at most one child, so it recurses into a base case)
     *
     * @return true if a node was actually removed, false if value wasn't found
     * @throws IllegalArgumentException if value is null
     */
    public boolean delete(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot delete null from BST");
        }
        boolean[] removed = {false};
        root = delete(root, value, removed);
        return removed[0];
    }

    private Node delete(Node node, T value, boolean[] removed) {
        if (node == null) {
            return null; // value not found on this path
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = delete(node.left, value, removed);
        } else if (cmp > 0) {
            node.right = delete(node.right, value, removed);
        } else {
            removed[0] = true;

            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }

            // Two children: swap in the inorder successor's value, then
            // remove that successor from the right subtree. The successor
            // has no left child by definition, so this recursive call
            // always lands in one of the base cases above — size is
            // decremented exactly once, there.
            Node successor = findMin(node.right);
            node.value = successor.value;
            node.right = delete(node.right, successor.value, new boolean[1]);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

}