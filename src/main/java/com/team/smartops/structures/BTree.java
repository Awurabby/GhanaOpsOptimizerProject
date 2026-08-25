package com.team.smartops.structures;

/**
 * OWNER: Team C (C4).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Intentionally int-keyed (typical B-tree use case: indexing numeric IDs).
 */
public class BTree {
    private static final int T = 2; // minimum degree: max keys = 2T-1 = 3

    static class BTreeNode {
        int[] keys;
        BTreeNode[] children;
        int numKeys;
        boolean isLeaf;

        BTreeNode(boolean isLeaf) {
            this.isLeaf = isLeaf;
            this.keys = new int[2 * T - 1];
            this.children = new BTreeNode[2 * T];
            this.numKeys = 0;
        }
    }

    private BTreeNode root;

    public BTree() {
        root = new BTreeNode(true);
    }

    public BTreeNode search(int key) {
        return searchNode(root, key);
    }

    private BTreeNode searchNode(BTreeNode node, int key) {
        int i = 0;
        while (i < node.numKeys && key > node.keys[i]) i++;
        if (i < node.numKeys && key == node.keys[i]) return node;
        if (node.isLeaf) return null;
        return searchNode(node.children[i], key);
    }

    public boolean contains(int key) {
        return search(key) != null;
    }

    public void insert(int key) {
        BTreeNode r = root;
        if (r.numKeys == 2 * T - 1) {
            BTreeNode newRoot = new BTreeNode(false);
            newRoot.children[0] = r;
            splitChild(newRoot, 0, r);
            root = newRoot;
        }
        insertNonFull(root, key);
    }

    private void insertNonFull(BTreeNode node, int key) {
        int i = node.numKeys - 1;
        if (node.isLeaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.numKeys++;
        } else {
            while (i >= 0 && key < node.keys[i]) i--;
            i++;
            if (node.children[i].numKeys == 2 * T - 1) {
                splitChild(node, i, node.children[i]);
                if (key > node.keys[i]) i++;
            }
            insertNonFull(node.children[i], key);
        }
    }

    private void splitChild(BTreeNode parent, int i, BTreeNode fullChild) {
        BTreeNode newChild = new BTreeNode(fullChild.isLeaf);
        newChild.numKeys = T - 1;
        for (int j = 0; j < T - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + T];
        }
        if (!fullChild.isLeaf) {
            for (int j = 0; j < T; j++) {
                newChild.children[j] = fullChild.children[j + T];
            }
        }
        fullChild.numKeys = T - 1;
        for (int j = parent.numKeys; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = newChild;
        for (int j = parent.numKeys - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[i] = fullChild.keys[T - 1];
        parent.numKeys++;
    }
}