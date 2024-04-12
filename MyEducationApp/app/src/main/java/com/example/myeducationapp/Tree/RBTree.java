package com.example.myeducationapp.Tree;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * code for Red Black Tree
 * @author u7532738 Jinhan Tan
 * RBTree class
 * @param <T> data type
 */
public class RBTree<T extends Comparable<T>> implements Serializable {

    Node<T> root; // The root node of the tree

    /**
     * Initialize empty RBTree
     */
    public RBTree() {
        root = null;
    }

    /**
     * Add a new node into the tree with {@code root} node.
     *
     * @param root Node<T> The root node of the tree where x is being inserted.
     * @param x    Node<T> New node being inserted.
     */
    private void insertRecurse(Node<T> root, Node<T> x) {
        int cmp = root.key.compareTo(x.key);

        if (cmp > 0) {
            if (root.left.key == null) {
                root.left = x;
                x.parent = root;
            } else {
                insertRecurse(root.left, x);
            }
        } else{// if (cmp < 0) {
            if (root.right.key == null) {
                root.right = x;
                x.parent = root;
            } else {
                insertRecurse(root.right, x);
            }
        }
        // Do nothing if the tree already has a node with the same key.
    }

    /**
     * Insert node into RBTree.
     *
     * @param x Node<T> The new node being inserted into the tree.
     */
    private void insert(Node<T> x) {
        // Insert node into tree
        if (root == null) {
            root = x;
        } else {
            //if (search(x.key) != null) return;
            insertRecurse(root, x);
        }

        // Fix tree
        while (x.key != root.key && x.parent.colour == Colour.RED) {
            boolean left = x.parent == x.parent.parent.left; // Is parent a left node
            Node<T> uncle = left ? x.parent.parent.right : x.parent.parent.left; // Get opposite "uncle" node to parent

            if (uncle.colour == Colour.RED) {
                // Case 1: Recolour
                x.parent.colour = Colour.BLACK;
                uncle.colour = Colour.BLACK;
                uncle.parent.colour = Colour.RED;
                // Check if violated further up the tree
                x = x.parent.parent;
            } else {
                if (x.key == (left ? x.parent.right.key : x.parent.left.key)) {
                    // Case 2: Left Rotation, uncle is right node, x is on the right / Right Rotation, uncle is left node, x is on the left
                    x = x.parent;
                    if (left) {
                        // Perform left rotation
                        if (x.key == root.key)
                            root = x.right; // Update root
                        rotateLeft(x);
                    } else {
                        // This is part of the "then" clause where left and right are swapped
                        // Perform right rotation
                        if (x.key == root.key)
                            root = x.left; // Update root
                        rotateRight(x);
                    }
                }
                // Adjust colours to ensure correctness after rotation
                x.parent.colour = Colour.BLACK;
                x.parent.parent.colour = Colour.RED;
                if (left) {
                    // Perform right rotation
                    x = x.parent.parent;
                    if (x.key == root.key)
                        root = x.left;
                    rotateRight(x);
                    //rotateRight(x.parent.parent);
                } else {
                    // Perform left rotation
                    x = x.parent.parent;
                    if (x.key == root.key)
                        root = x.right;
                    rotateLeft(x);
                    //rotateLeft(x.parent.parent);
                }
            }
        }
        // Ensure property 2 (root and leaves are black) holds
        root.colour = Colour.BLACK;
    }

    // Rotate the node so it becomes the child of its right branch
    public void rotateLeft(Node<T> x) {
        // Make parent (if it exists) and right branch point to each other
        if (x.parent != null) {
            // Determine whether this node is the left or right child of its parent
            if (x.parent.left.key == x.key) {
                x.parent.left = x.right;
            } else {
                x.parent.right = x.right;
            }
        }
        x.right.parent = x.parent;

        x.parent = x.right;
        // Take right node's left branch
        x.right = x.parent.left;
        x.right.parent = x;
        // Take the place of the right node's left branch
        x.parent.left = x;
    }

    // Rotate the node so it becomes the child of its left branch

    public void rotateRight(Node<T> x) {
        // Make parent (if it exists) and right branch point to each other
        if (x.parent != null) {
            // Determine whether this node is the left or right child of its parent
            if (x.parent.left.key == x.key) {
                x.parent.left = x.left;
            } else {
                x.parent.right = x.left;
            }
        }
        x.left.parent = x.parent;
        x.parent = x.left;
        // Take right node's left branch
        x.left = x.parent.right;
        x.left.parent = x;
        // Take the place of the right node's left branch
        x.parent.right = x;
    }

    /**
     * Demo functions (Safely) insert a key into the tree
     *
     * @param key T The key of the new node being inserted.
     */
//    public void insert(T key,Object object) {
//        Node<T> node = new Node<T>(key,object);
//        if (node != null)
//            insert(node);
//    }
    public void insert(T key) {
        Node<T> node = new Node<T>(key);
        if (node != null)
            insert(node);
    }

    /**
     * Return the result of a pre-order traversal of the tree
     *
     * @param tree Tree we want to pre-order traverse
     * @return pre-order traversed tree
     */
    private String preOrder(Node<T> tree) {
        if (tree != null && tree.key != null) {
            String leftStr = preOrder(tree.left);
            String rightStr = preOrder(tree.right);
            return tree.key + (leftStr.isEmpty() ? leftStr : " " + leftStr)
                    + (rightStr.isEmpty() ? rightStr : " " + rightStr);
        }
        return "";
    }

    public String preOrder() {
        return preOrder(root);
    }

    /**
     * Return the result of a in-order traversal of the tree
     *
     * @param tree Tree we want to in-order traverse
     * @return in-order traversed tree
     */
    private String inOrder(Node<T> tree) {
        if (tree != null && tree.key != null) {
            String leftStr = preOrder(tree.left);
            String rightStr = preOrder(tree.right);
            return (leftStr.isEmpty() ? leftStr : leftStr + " ") + tree.key + " "
                    + rightStr;
        }
        return "";
    }

    public String inOrder() {
        return inOrder(root);
    }

    /**
     * Return the result of a post-order traversal of the tree
     *
     * @param tree Tree we want to post-order traverse
     * @return post-order traversed tree
     */
    private String postOrder(Node<T> tree) {
        if (tree != null && tree.key != null) {
            String leftStr = preOrder(tree.left);
            String rightStr = preOrder(tree.right);
            return (leftStr.isEmpty() ? leftStr : leftStr + " ")
                    + (rightStr.isEmpty() ? rightStr : rightStr + " ") + tree.key;
        }
        return "";
    }

    public String postOrder() {
        return postOrder(root);
    }

    /**
     * get List from tree inorder
     *
     * @param arrayList List to be obtained
     * @param node
     */
    private void toList(ArrayList<T> arrayList, Node<T> node) {
        if (node != null && node.key != null) {
            toList(arrayList, node.left);
            arrayList.add(node.key);
            toList(arrayList, node.right);
        }
    }

    public void toList(ArrayList<T> arrayList) {
        toList(arrayList, root);
    }

    /**
     * Return the corresponding node of a key, if it exists in the tree
     *
     * @param x Node<T> The root node of the tree we search for the key {@code v}
     * @param v Node<T> The node that we are looking for
     * @return
     */
    private Node<T> find(Node<T> x, T v) {
        if (x.key == null)
            return null;

        int cmp = v.compareTo(x.key);
        if (cmp < 0)
            return find(x.left, v);
        else if (cmp > 0)
            return find(x.right, v);
        else
            return x;
    }

    /**
     * Returns a node if the key of the node is {@code key}.
     *
     * @param key T The key we are looking for
     * @return
     */
    public Node<T> search(T key) {
        return find(root, key);
    }

    /**
     * search all Node that has a key
     * @param key
     * @return
     */
//    public List<Node<T>> searchAll(T key) {
//        List<Node<T>> res = new ArrayList<>();
//        Node<T> node = search(key);
//        res.add(node);
//        findAll(res, key, node.left);
//        findAll(res, key, node.right);
//        return res;
//    }
//
//    private void findAll(List<Node<T>> list, T key, Node<T> node) {
//        if (node.key.compareTo(key) == 0) {
//            list.add(node);
//            findAll(list, key, node.left);
//            findAll(list, key, node.right);
//        }
//    }

    /**
     * Delete a node with the given key from the RBTree.
     *
     * @param key T The key of the node to be deleted.
     */
    public void delete(T key) {
        Node<T> nodeToDelete = search(key);
        if (nodeToDelete != null) {
            deleteNode(nodeToDelete);
        }
    }

    /**
     * Delete a node from the RBTree.
     *
     * @param nodeToDelete Node<T> The node to be deleted.
     */
    private void deleteNode(Node<T> nodeToDelete) {
        Node<T> nodeToReplace;
        if (nodeToDelete.left.key == null || nodeToDelete.right.key == null) {
            // The node to delete has at most one child
            nodeToReplace = nodeToDelete;
        } else {
            // The node to delete has two children
            nodeToReplace = successor(nodeToDelete);
            nodeToDelete.key = nodeToReplace.key;
        }

        Node<T> childNode;
        if (nodeToReplace.left.key != null) {
            childNode = nodeToReplace.left;
        } else {
            childNode = nodeToReplace.right;
        }

        childNode.parent = nodeToReplace.parent;

        if (nodeToReplace.parent == null) {
            root = childNode;
        } else if (nodeToReplace == nodeToReplace.parent.left) {
            nodeToReplace.parent.left = childNode;
        } else {
            nodeToReplace.parent.right = childNode;
        }

        if (nodeToReplace != nodeToDelete) {
            nodeToDelete.key = nodeToReplace.key;
            // Copy any additional attributes from the nodeToReplace to nodeToDelete
            // if there are any other attributes in the Node class
        }

        if (nodeToReplace.colour == Colour.BLACK) {
            deleteFixup(childNode);
        }
    }

    /**
     * Returns the node with the minimum key value in the tree rooted at the given node.
     *
     * @param node The root of the tree from which to find the minimum node.
     * @return The node with the minimum key value, or null if the tree is empty.
     */
    private Node<T> minimum(Node<T> node) {
        if (node == null || node.key == null) {
            return null;
        }

        while (node.left.key != null) {
            node = node.left;
        }

        return node;
    }
    /**
     * Get the successor node of a given node.
     *
     * @param node Node<T> The node to find the successor for.
     * @return Node<T> The successor node.
     */
    private Node<T> successor(Node<T> node) {
        if (node.right.key != null) {
            return minimum(node.right);
        }

        Node<T> parentNode = node.parent;
        while (parentNode != null && node == parentNode.right) {
            node = parentNode;
            parentNode = parentNode.parent;
        }
        return parentNode;
    }

    /**
     * Fix the RBTree properties after a deletion operation.
     *
     * @param node Node<T> The node to start the fixup from.
     */
    private void deleteFixup(Node<T> node) {
        while (node != root && node.colour == Colour.BLACK) {
            if (node == node.parent.left) {
                Node<T> sibling = node.parent.right;

                if (sibling.colour == Colour.RED) {
                    sibling.colour = Colour.BLACK;
                    node.parent.colour = Colour.RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }

                if (sibling.left.colour == Colour.BLACK && sibling.right.colour == Colour.BLACK) {
                    sibling.colour = Colour.RED;
                    node = node.parent;
                } else {
                    if (sibling.right.colour == Colour.BLACK) {
                        sibling.left.colour = Colour.BLACK;
                        sibling.colour = Colour.RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }

                    sibling.colour = node.parent.colour;
                    node.parent.colour = Colour.BLACK;
                    sibling.right.colour = Colour.BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node<T> sibling = node.parent.left;

                if (sibling.colour == Colour.RED) {
                    sibling.colour = Colour.BLACK;
                    node.parent.colour = Colour.RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }

                if (sibling.right.colour == Colour.BLACK && sibling.left.colour == Colour.BLACK) {
                    sibling.colour = Colour.RED;
                    node = node.parent;
                } else {
                    if (sibling.left.colour == Colour.BLACK) {
                        sibling.right.colour = Colour.BLACK;
                        sibling.colour = Colour.RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }

                    sibling.colour = node.parent.colour;
                    node.parent.colour = Colour.BLACK;
                    sibling.left.colour = Colour.BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }

        node.colour = Colour.BLACK;
    }


    public enum Colour {
        RED, BLACK;
    }

    public class Node<T> {

        public Colour colour;            // Node colour
        public T key;                // Node key
        Node<T> parent;        // Parent node
        Node<T> left, right;    // Child nodes
        //Object object;

        public Node(T key) {//} ,Object object) {
            this.key = key;
            this.colour = Colour.RED; //property 3 (if a node is red, both children are black) may be violated if parent is red

            this.parent = null;
            //this.object=object;

            // Initialise children leaf nodes
            this.left = new Node<T>();  //leaf node
            this.right = new Node<T>();  //leaf node
            this.left.parent = this; //reference to parent
            this.right.parent = this; //reference to parent
        }

//        public Object getObject() {
//            return object;
//        }

        // Leaf node
        public Node() {
            this.key = null; //leaf nodes are null
            this.colour = Colour.BLACK; //leaf nodes are always black
        }
    }
}
