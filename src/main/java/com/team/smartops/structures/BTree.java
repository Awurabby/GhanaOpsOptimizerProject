package com.team.smartops.structures;
public class BTree {

    private static final int T = 2; // minimum degree: max keys = 2T-1 = 3

    // ── B-TREE NODE ───────────────────────────────────────────────────────────
    static class BTreeNode {
        int[]       keys;
        BTreeNode[] children;
        int         numKeys;
        boolean     isLeaf;

        BTreeNode(boolean isLeaf) {
            this.isLeaf   = isLeaf;
            this.keys     = new int[2 * T - 1];   // max 3 keys
            this.children = new BTreeNode[2 * T]; // max 4 children
            this.numKeys  = 0;
        }
    }

    private BTreeNode root;

    public BTree() {
        root = new BTreeNode(true); // empty tree: single empty leaf
    }

    // ── SEARCH ────────────────────────────────────────────────────────────────
    /**
     * search - find a key in the B-tree.
     * At each node: scan keys left to right.
     * If key found: return node.
     * If leaf and not found: key absent.
     * Else: descend to appropriate child.
     *
     * Pseudocode:
     *   B_TREE_SEARCH(node, target):
     *     i <- 0
     *     while i < numKeys AND target > keys[i]: i++
     *     if i < numKeys AND target == keys[i]: return node  // found
     *     if isLeaf: return null                             // absent
     *     return B_TREE_SEARCH(children[i], target)         // descend
     *
     * @param key the request ID to search for
     * @return node containing the key, or null if not found
     */
    public BTreeNode search(int key) {
        return searchNode(root, key);
    }

    private BTreeNode searchNode(BTreeNode node, int key) {
        int i = 0;

        // find first key >= target
        while (i < node.numKeys && key > node.keys[i]) i++;

        // found it at this node
        if (i < node.numKeys && key == node.keys[i]) {
            System.out.println("  Found " + key + " in node at key index " + i);
            return node;
        }

        // leaf and not found
        if (node.isLeaf) {
            System.out.println("  Key " + key + " not found.");
            return null;
        }

        // descend to child[i]
        System.out.println("  " + key + " not in this node -> descend to child " + i);
        return searchNode(node.children[i], key);
    }

    // ── INSERT ────────────────────────────────────────────────────────────────
    /**
     * insert - insert a key into the B-tree.
     * If root is full (3 keys): split root first, tree grows taller.
     * Then call insertNonFull on guaranteed non-full root.
     *
     * Pseudocode:
     *   INSERT(key):
     *     if root is full:
     *       newRoot <- empty node
     *       newRoot.children[0] <- root
     *       SPLIT_CHILD(newRoot, 0)
     *       root <- newRoot
     *     INSERT_NON_FULL(root, key)
     */
    public void insert(int key) {
        BTreeNode r = root;

        if (r.numKeys == 2 * T - 1) {
            // root full — split it, tree grows one level taller
            BTreeNode newRoot = new BTreeNode(false);
            newRoot.children[0] = r;
            splitChild(newRoot, 0, r);
            root = newRoot;
            System.out.println("  Root split! New root created.");
        }
        insertNonFull(root, key);
    }

    /**
     * insertNonFull - insert into a node guaranteed not to be full.
     *
     * Pseudocode:
     *   INSERT_NON_FULL(node, key):
     *     if isLeaf:
     *       shift keys right to make room, insert in sorted position
     *     else:
     *       find correct child
     *       if child full: SPLIT_CHILD then adjust index
     *       INSERT_NON_FULL(child, key)
     */
    private void insertNonFull(BTreeNode node, int key) {
        int i = node.numKeys - 1;

        if (node.isLeaf) {
            // shift keys right to make room
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.numKeys++;
        } else {
            // find correct child to descend into
            while (i >= 0 && key < node.keys[i]) i--;
            i++;

            if (node.children[i].numKeys == 2 * T - 1) {
                // child is full — split before descending
                splitChild(node, i, node.children[i]);
                System.out.println("  Node split at child " + i + ".");
                if (key > node.keys[i]) i++;
            }
            insertNonFull(node.children[i], key);
        }
    }

    /**
     * splitChild - split a full child of parent.
     * Middle key moves UP to parent.
     * Left half stays in fullChild.
     * Right half moves to new sibling node.
     *
     * Pseudocode:
     *   SPLIT_CHILD(parent, i, fullChild):
     *     newChild <- empty node
     *     copy right half of fullChild.keys to newChild.keys
     *     copy right half of fullChild.children to newChild.children
     *     fullChild.numKeys <- T-1  (left half stays)
     *     insert newChild into parent.children[i+1]
     *     move fullChild.keys[T-1] up to parent.keys[i]
     *     parent.numKeys++
     */
    private void splitChild(BTreeNode parent, int i, BTreeNode fullChild) {
        BTreeNode newChild = new BTreeNode(fullChild.isLeaf);
        newChild.numKeys = T - 1;

        // copy right half of fullChild keys to newChild
        for (int j = 0; j < T - 1; j++) {
            newChild.keys[j] = fullChild.keys[j + T];
        }

        // copy right half of fullChild children to newChild
        if (!fullChild.isLeaf) {
            for (int j = 0; j < T; j++) {
                newChild.children[j] = fullChild.children[j + T];
            }
        }

        fullChild.numKeys = T - 1; // left half stays

        // shift parent children right to make room for newChild
        for (int j = parent.numKeys; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = newChild;

        // shift parent keys right, insert middle key
        for (int j = parent.numKeys - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
        }
        parent.keys[i] = fullChild.keys[T - 1]; // middle key goes up
        parent.numKeys++;
    }

    // ── PRINT ─────────────────────────────────────────────────────────────────
    /**
     * print - displays the tree level by level.
     * Use this output as your trace evidence in the report.
     */
    public void print() {
        System.out.println("\nB-Tree structure (level by level):");
        printNode(root, 0);
    }

    private void printNode(BTreeNode node, int level) {
        System.out.print("  Level " + level + ": [");
        for (int i = 0; i < node.numKeys; i++) {
            System.out.print(node.keys[i]);
            if (i < node.numKeys - 1) System.out.print(" | ");
        }
        System.out.println("] " + (node.isLeaf ? "(leaf)" : ""));

        if (!node.isLeaf) {
            for (int i = 0; i <= node.numKeys; i++) {
                if (node.children[i] != null) {
                    printNode(node.children[i], level + 1);
                }
            }
        }
    }

    // ── DEMO ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("=== UG Campus BTree Demo ===");
        System.out.println("Indexing service request IDs (t=2, max 3 keys/node)\n");

        BTree tree = new BTree();
        int[] requestIds = {10, 20, 30, 40, 50, 55};

        for (int id : requestIds) {
            System.out.println("Inserting " + id + ":");
            tree.insert(id);
            tree.print();
        }

        System.out.println("\n--- Search traces ---");
        System.out.println("Searching for 40:");
        tree.search(40);

        System.out.println("\nSearching for 55:");
        tree.search(55);

        System.out.println("\nSearching for 99 (not in tree):");
        tree.search(99);
    }
}
