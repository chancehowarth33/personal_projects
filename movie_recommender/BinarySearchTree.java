// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.LinkedList;
import java.util.Stack;


public class BinarySearchTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

    /**
     * this class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;
        public Node<T> up;
        @SuppressWarnings("unchecked")
        public Node<T>[] down = (Node<T>[])new Node[2];
        public Node(T data) { this.data = data; }
        public boolean isRightChild() {
            return this.up != null && this.up.down[1] == this;
        }

    }
    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    public boolean insert(T data) throws NullPointerException {
        if (data == null)
            throw new NullPointerException("Cannot insert data value null into the tree.");
        return this.insertHelper(new Node<>(data));
    }

    protected boolean insertHelper(Node<T> newNode) throws NullPointerException {
        if(newNode == null) throw new NullPointerException("new node cannot be null");

        if (this.root == null) {
            // add first node to an empty tree
            root = newNode;
            size++;
            return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                    return false;
                } else if (compare < 0) {
                    // insert in left subtree
                    if (current.down[0] == null) {
                        // empty space to insert into
                        current.down[0] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[0];
                    }
                } else {
                    // insert in right subtree
                    if (current.down[1] == null) {
                        // empty space to insert into
                        current.down[1] = newNode;
                        newNode.up = current;
                        this.size++;
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.down[1];
                    }
                }
            }
        }
    }

    protected void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        // If child or parent is null or if child's parent isn't the provided parent
        if (child == null || parent == null || child.up != parent) {
            throw new IllegalArgumentException("Invalid node combination.");
        }

        if (child.isRightChild()) {
            // left rotation, adjust the parent's right child
            parent.down[1] = child.down[0];
            if (child.down[0] != null) {
                child.down[0].up = parent;
            }
            // adjust the child's left child
            child.down[0] = parent;
        } else {
            // right rotation, adjust the parent's left child
            parent.down[0] = child.down[1];
            if (child.down[1] != null) {
                child.down[1].up = parent;
            }
            // adjust the child's right child
            child.down[1] = parent;
        }

        // set child's parent
        child.up = parent.up;

        // update root or grandparent's link
        if (parent.up == null) {
            root = child;
        } else if (parent.isRightChild()) {
            parent.up.down[1] = child;
        } else {
            parent.up.down[0] = child;
        }
        // sets parents new parent as the child node we choose above
        parent.up = child;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean contains(Comparable<T> data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This tree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNode(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    public void clear() {
        this.root = null;
        this.size = 0;
    }

    protected Node<T> findNode(Comparable<T> data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                if (current.down[0] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the left subtree
                current = current.down[0];
            } else {
                if (current.down[1] == null) {
                    // we have hit a null node and did not find our node
                    return null;
                }
                // keep looking in the right subtree
                current = current.down[1];
            }
        }
        return null;
    }

    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    sb.append(popped.data.toString());
                    if(!nodeStack.isEmpty() || popped.down[1] != null) sb.append(", ");
                    current = popped.down[1];
                } else {
                    nodeStack.add(current);
                    current = current.down[0];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if(next.down[0] != null) q.add(next.down[0]);
                if(next.down[1] != null) q.add(next.down[1]);
                sb.append(next.data.toString());
                if(!q.isEmpty()) sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }

    public static boolean test1() {
        // testing a right rotation
        BinarySearchTree<Integer> bstright = new BinarySearchTree<>();
        bstright.insert(5);
        bstright.insert(4);
        bstright.insert(6);
        bstright.insert(3);
        bstright.rotate(bstright.root.down[0], bstright.root);
        return bstright.toLevelOrderString().equals("[ 4, 3, 5, 6 ]");
    }

    public static boolean test2() {
        // testing a left rotation
        BinarySearchTree<Integer> bstleft = new BinarySearchTree<>();
        bstleft.insert(4);
        bstleft.insert(3);
        bstleft.insert(5);
        bstleft.insert(6);
        bstleft.rotate(bstleft.root.down[1], bstleft.root);
        return bstleft.toLevelOrderString().equals("[ 5, 4, 6, 3 ]");
    }

    public static boolean test3() {
        // Test a right rotation that isn't at the root
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        bst.insert(5);
        bst.insert(3);
        bst.insert(6);
        bst.insert(2);
        bst.insert(4);
        bst.rotate(bst.root.down[0].down[0], bst.root.down[0]);
        return bst.toLevelOrderString().equals("[ 5, 2, 6, 3, 4 ]");
    }

    /**
     * Main method to run tests. If you'd like to add additional test methods, add a line for each
     * of them.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Test 1 passed: " + test1());
        System.out.println("Test 2 passed: " + test2());
        System.out.println("Test 3 passed: " + test3());
    }

}
