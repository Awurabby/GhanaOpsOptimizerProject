
/**
 * OWNER: Team C (C3).
 * Hand-built implementation -- no java.util.HashMap/Stack/PriorityQueue/etc.
 * Must support: normal case, boundary case, invalid input case (brief Sec 8.iii).
 */

package com.team.smartops.structures;

import java.util.function.Consumer;
import java.util.function.IntFunction;

public class RedBlackTree<T extends Comparable<T>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private final Node nil = new Node(null); // shared sentinel, always BLACK
    private Node root = nil;
    private int size;

    private class Node {
        T value;
        Node left;
        Node right;
        Node parent;
        boolean color;

        Node(T value) {
            this.value = value;
            this.left = nil;
            this.right = nil;
            this.parent = nil;
            this.color = BLACK; // sentinel default; real nodes set RED on insert
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
     * Directly comparable to BST.height() on the same input data.
     */
    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == nil) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // ---------- search ----------

    /**
     * @return true if value exists in the tree
     * @throws IllegalArgumentException if value is null
     */
    public boolean search(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot search for null in RedBlackTree");
        }
        return findNode(value) != nil;
    }

    private Node findNode(T value) {
        Node current = root;
        while (current != nil) {
            int cmp = value.compareTo(current.value);
            if (cmp == 0) {
                return current;
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return nil;
    }

    // ---------- traversal ----------

    /**
     * Visits every value in ascending order.
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
        if (node == nil) {
            return;
        }
        inorderTraversal(node.left, visitor);
        visitor.accept(node.value);
        inorderTraversal(node.right, visitor);
    }

    /**
     * Collects ascending-order values into a properly-typed array. See
     * BST.toSortedArray for why a generator is required (generics erasure).
     *
     *   Integer[] sorted = tree.toSortedArray(Integer[]::new);
     *
     * @throws IllegalArgumentException if generator is null
     */
    public T[] toSortedArray(IntFunction<T[]> generator) {
        if (generator == null) {
            throw new IllegalArgumentException("generator cannot be null");
        }
        T[] result = generator.apply(size);
        int[] index = {0};
        inorderTraversal(root, value -> result[index[0]++] = value);
        return result;
    }

    // ---------- deletion ----------

    /**
     * Replaces the subtree rooted at u with the subtree rooted at v,
     * rewiring u's parent to point at v. Standard CLRS TRANSPLANT.
     * Does not touch v's children — callers handle that separately.
     */
    private void transplant(Node u, Node v) {
        if (u.parent == nil) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node x) {
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }

    /**
     * Deletes value if present.
     *
     * @return true if a node was actually removed, false if value wasn't found
     * @throws IllegalArgumentException if value is null
     */
    public boolean delete(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot delete null from RedBlackTree");
        }

        Node z = findNode(value);
        if (z == nil) {
            return false;
        }

        deleteNode(z);
        size--;
        return true;
    }

    /**
     * Standard CLRS RB-DELETE. Removes z from the tree; if the physically
     * removed node was black, hands off to deleteFixup to restore the
     * black-height invariant (removing a black node can leave a "double
     * black" defect on the path that has to be resolved by rotation,
     * recoloring, or pushed further up the tree).
     */
    private void deleteNode(Node z) {
        Node y = z;
        boolean yOriginalColor = y.color;
        Node x;

        if (z.left == nil) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == nil) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                // x may be nil — still set its parent so fixup has correct
                // context to walk up from, per CLRS.
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == BLACK) {
            deleteFixup(x);
        }
    }

    /**
     * Standard CLRS RB-DELETE-FIXUP. x is the node that moved into the
     * deleted node's position and may carry an "extra" black (a defect in
     * the black-height count on its path). Each iteration either resolves
     * the defect via rotation + recoloring, or pushes it up to x's parent.
     */
    private void deleteFixup(Node x) {
        while (x != root && x.color == BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right; // sibling

                if (w.color == RED) {
                    // Case 1: red sibling -> recolor + rotate to get a black sibling
                    w.color = BLACK;
                    x.parent.color = RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == BLACK && w.right.color == BLACK) {
                    // Case 2: black sibling with two black children -> push defect up
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.right.color == BLACK) {
                        // Case 3: black sibling, near nephew red, far nephew black
                        // -> rotate to convert into case 4
                        w.left.color = BLACK;
                        w.color = RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    // Case 4: black sibling, far nephew red -> rotate + recolor, done
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                // Mirror image: x is a right child
                Node w = x.parent.left;

                if (w.color == RED) {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == BLACK && w.left.color == BLACK) {
                    w.color = RED;
                    x = x.parent;
                } else {
                    if (w.left.color == BLACK) {
                        w.right.color = BLACK;
                        w.color = RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK;
    }

    // ---------- rotations ----------

    /**
     * Left-rotates around x, promoting x's right child into x's place.
     * Standard CLRS LEFT-ROTATE.
     */
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != nil) {
            y.left.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }

        y.left = x;
        x.parent = y;
    }

    /**
     * Right-rotates around x, promoting x's left child into x's place.
     * Mirror image of leftRotate.
     */
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != nil) {
            y.right.parent = x;
        }
        y.parent = x.parent;

        if (x.parent == nil) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }

        y.right = x;
        x.parent = y;
    }

    // ---------- insertion ----------

    /**
     * Inserts a value, then restores red-black invariants via rotations
     * and recoloring. If the value already exists, this is a no-op
     * (same duplicate convention as BST, for consistency).
     *
     * @throws IllegalArgumentException if value is null
     */
    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Cannot insert null into RedBlackTree");
        }

        Node parent = nil;
        Node current = root;

        while (current != nil) {
            parent = current;
            int cmp = value.compareTo(current.value);
            if (cmp == 0) {
                return; // duplicate, no-op
            } else if (cmp < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        Node newNode = new Node(value);
        newNode.parent = parent;
        newNode.color = RED; // new nodes always start red

        if (parent == nil) {
            root = newNode;
        } else if (value.compareTo(parent.value) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        size++;
        fixInsert(newNode);
    }

    /**
     * Standard CLRS RB-INSERT-FIXUP. Walks up from the newly-inserted red
     * node, resolving red-red violations either by recoloring (when the
     * uncle is red) or by rotation + recoloring (when the uncle is black).
     */
    private void fixInsert(Node z) {
        while (z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node uncle = z.parent.parent.right;

                if (uncle.color == RED) {
                    // Case 1: uncle is red -> recolor and move violation up
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        // Case 2: uncle is black, z is a right child -> rotate to case 3
                        z = z.parent;
                        leftRotate(z);
                    }
                    // Case 3: uncle is black, z is a left child -> recolor + rotate
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rightRotate(z.parent.parent);
                }
            } else {
                // Mirror image: parent is a right child
                Node uncle = z.parent.parent.left;

                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rightRotate(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        root.color = BLACK; // property 2: root is always black
    }

    // ---------- invariant checking ----------

    /**
     * Verifies all five red-black properties hold across the whole tree.
     * Intended for tests and correctness evidence — a rotation or fixup
     * bug can sometimes still return correct search() results by luck on
     * small inputs, so checking the actual invariants (not just observed
     * behavior) is a stronger correctness guarantee.
     *
     * Checks:
     *   2. root is black
     *   4. no red node has a red child
     *   5. every root-to-NIL path has the same black-height
     * (Properties 1 and 3 — every node is red/black, every leaf is NIL —
     * are structurally guaranteed by the Node class itself and can't be
     * violated at runtime.)
     *
     * @return true if the tree satisfies every red-black invariant
     */
    public boolean isValidRedBlackTree() {
        if (root.color != BLACK) {
            return false; // property 2
        }
        return blackHeightOrViolation(root) != -1;
    }

    /**
     * Returns the black-height of the subtree rooted at node if every path
     * within it is consistent and no red-red violation exists; returns -1
     * to signal a violation was found anywhere in the subtree.
     */
    private int blackHeightOrViolation(Node node) {
        if (node == nil) {
            return 1; // NIL counts as one black node on every path (property 3/5)
        }

        if (node.color == RED) {
            if (node.left.color == RED || node.right.color == RED) {
                return -1; // property 4 violation: red node with a red child
            }
        }

        int leftBlackHeight = blackHeightOrViolation(node.left);
        if (leftBlackHeight == -1) {
            return -1;
        }
        int rightBlackHeight = blackHeightOrViolation(node.right);
        if (rightBlackHeight == -1) {
            return -1;
        }
        if (leftBlackHeight != rightBlackHeight) {
            return -1; // property 5 violation: unequal black-height across children
        }

        return leftBlackHeight + (node.color == BLACK ? 1 : 0);
    }
}