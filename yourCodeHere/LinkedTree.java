import java.awt.Color;

//no arrays! (this file)

/**
 * A class that stores a root of the tree and allows to return its size and
 * height by iterating over the tree.
 * 
 * @param <T> represents the type of {@link LinkedTree#root}
 */
class LinkedTree<T> {
    /**
     * The root of the LinkedTree.
     */
    private final TreeNode<T> root;

    /**
     * Creates a LinkedTree with root as as {@code root}.
     * 
     * @param root to set as a root node of the LinkedTree
     */
    public LinkedTree(TreeNode<T> root) {
        this.root = root;
    }

    /**
     * Returns the {@link LinkedTree#root}}.
     *
     * @return {@link TreeNode} root of the LinkedTree if the tree is not empty,
     *         otherwise a {@code null} is returned.
     */
    public TreeNode<T> getRoot() {
        return root;
    }

    /**
     * Linear Time Complexity is achieved by performing an inorder iteration over
     * the tree using recursion.
     *
     * <p>Time Complexity: O(n) - linear.
     * 
     * @return the size of the LinkedTree. If the tree is empty (i.e
     *         {@link LinkedTree#getRoot} == {@code null}) "0" is returned
     */
    public int getSize() {
        return getSize(root);
    }

    /**
     * Uses inorder iteration over the tree to find its size. Recursion is used on
     * each of the children nodes of the current one and after retrieving
     * their (children's) sizes, the method will add them up and the result will be
     * incremented by one to get the final size of the current node.
     * 
     * @param node is the root of the tree to find the size of
     * @return the size of the tree where {@code node} is the root. If the
     *         {@code node} is {@code null} "0" is returned
     */
    private int getSize(TreeNode<T> node) {
        if (node == null) {
            return 0;
        }

        return getSize(node.left) + getSize(node.right) + 1;
    }

    /**
     * Linear Time Complexity is achieved by performing an inorder iteration over
     * the tree using recursion.
     *
     * <p>Time Complexity: O(n) - linear.
     * 
     * @return the height of the LinkedTree. If the tree is empty (i.e
     *         {@link LinkedTree#getRoot} == {@code null}) "-1" is returned
     */
    public int getHeight() {
        if (root == null) {
            return -1;
        }

        return getHeight(root) - 1;
    }

    /**
     * Uses inorder iteration over the tree to find its height. Recursion is used
     * for each of the children nodes of the current one and after retrieving
     * their (children's) heights the method will find the highest height of them
     * and then it will be incremented by one to get the final height of the current
     * node.
     * 
     * @param node is the root of the tree to find the height of
     * @return the height of the tree where {@code node} is the root. If the
     *         {@code node} is {@code null} "0" is returned
     */
    private int getHeight(TreeNode<T> node) {
        if (node == null) {
            return 0;
        }

        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    /**
     * Returns a string representation of the LinkedTree (level ordered).
     *
     * @return a level ordered String representation of the tree, if the tree is
     *         empty an empty string is returned
     */
    public String toString() {
        if (root == null) {
            return ""; // Return an empty string for an empty tree
        }

        /**
         * A QueueNode serves the porpose of the linked queue.
         *
         * @param <P> used to define the type of {@lini QueueNode#value}.
         */
        class QueueNode<P> {
            P value;
            QueueNode<P> next;

            /**
             * Creates a QeueNode with value set as {@code value}.
             *
             * @param value set the {@link QueueNode#value} with. Nullable. 
             */
            QueueNode(P value) {
                this.value = value;
            }
        }

        StringBuilder result = new StringBuilder();
        // A simple queue implementation using a linked list
        QueueNode<TreeNode<T>> head = new QueueNode<TreeNode<T>>(root);
        QueueNode<TreeNode<T>> tail = head;

        while (head != null) {
            TreeNode<T> current = head.value;
            result.append(current.data).append(" ");

            // Enqueue left child
            if (current.left != null) {
                tail.next = new QueueNode<TreeNode<T>>(current.left);
                tail = tail.next;
            }

            // Enqueue right child
            if (current.right != null) {
                tail.next = new QueueNode<TreeNode<T>>(current.right);
                tail = tail.next;
            }

            head = head.next; // Dequeue
        }

        return result.toString().trim(); // Trim to remove the last space
    }

    /**
     * Represents a node of the binary tree, that can contain two children nodes of
     * the same type and data that is type parameterized using generics.
     *
     * @param <T> represents the type of {@link TreeNode#data}
     */
    public static class TreeNode<T> {

        /**
         * The data of the tree node. Nullable
         */
        private T data;
        /**
         * The left children of the tree node. Nullable
         */
        private TreeNode<T> left;
        /**
         * The right children of the tree node. Nullable
         */
        private TreeNode<T> right;
        /**
         * The color of the tree node. Nullable
         */
        private Color color = Color.WHITE;

        /**
         * Create an empty tree node, with data and children nodes set as {@code null}s.
         * The color is set as {@link Color#WHITE}
         */
        public TreeNode() {
        }

        /**
         * Create a tree node with provided data, and children nodes set as
         * {@code null}s.
         * The color is set as {@link Color#WHITE}
         * 
         * @param data to set to the tree node. {@code null} is permitted.
         */
        public TreeNode(T data) {
            setData(data);
        }

        /**
         * Returns the {@link TreeNode#data}.
         *
         * @return  the data assigned to the tree node. The returned value is nullable.
         */
        public T getData() {
            return this.data;
        }

        /**
         * Sets the data to the tree and returns the old one.
         * 
         * @param data to set to the tree node.
         * @return the previously set data to the tree node. The returned value is
         *         nullable.
         */
        public T setData(T data) {
            T oldData = this.data;
            this.data = data;
            return oldData;
        }

        /**
         * Return the right node {@link TreeNode#right}.
         *
         * @return the right children of the tree node, The returned value is nullable.
         */
        public TreeNode<T> getRight() {
            return this.right;
        }

        /**
         * Sets the right children node of the tree and returns the old one.
         * 
         * @param right to set as the right children node of the tree.
         * @return the previously set right children of the tree node, The returned
         *         value is nullable.
         */
        public TreeNode<T> setRight(TreeNode<T> right) {
            TreeNode<T> oldRight = this.right;
            this.right = right;
            return oldRight;
        }

        /**
         * Return the left node {@link TreeNode#left}.
         *
         * @return the left children of the tree node. The returned value is nullable.
         */
        public TreeNode<T> getLeft() {
            return this.left;
        }

        /**
         * Sets the left children node of the tree and returns the old one.
         * 
         * @param left to set as the left children node of the tree.
         * @return the previously set left children of the tree node, The returned value
         *         is nullable.
         */
        public TreeNode<T> setLeft(TreeNode<T> left) {
            TreeNode<T> oldLeft = this.left;
            this.left = left;
            return oldLeft;
        }

        /**
         * Returns the colodr {@link TreeNode#color}.
         *
         * @return the {@link Color} of the tree node
         */
        public Color getColor() {
            return color;
        }

        /**
         * Sets the color of the tree node and returns the old one.
         *
         * @param color to set
         * @return the previously set color of the tree node. The returned value is
         *         nullable.
         */
        public Color setColor(Color color) {
            Color oldColor = this.color;
            this.color = color;
            return oldColor;
        }

        /**
         * Returns a string representation of the {@link TreeNode#data}.
         *
         * @return a string representation of the data set. If the data is {@code null}
         *         a {@link NullPointerException} will be thrown
         */
        public String toString() {
            return this.data.toString();
        }
    }
}
