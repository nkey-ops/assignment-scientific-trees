import java.awt.Color;

//no arrays! (this file)

class LinkedTree<T> {
    /**************************************************
     * You may add additional PRIVATE instance variables
     * and methods. You may NOT use arrays in this file.
     **************************************************/

    public LinkedTree(TreeNode<T> root) {
        // your code here
    }

    public TreeNode<T> getRoot() {
        // your code here
        // Note: an empty tree should return null
        return null; // replace this!
    }

    public int getSize() {
        // your code here
        // Big-O requirement: O(n), where n is the number of elements in the tree
        // Hint: This can be done with ~2-3 lines in a recursive helper function.
        return -1; // replace this!
    }

    public int getHeight() {
        // your code here
        // Big-O requirement: O(n), where n is the number of elements in the tree
        // Hint: This can be done with ~2-3 lines in a recursive helper function.
        // Note: the height of a single-node tree (root and nothing else) is 0.
        // an empty tree should return -1
        return -1; // replace this!
    }

    public String toString() {
        // your code here
        // Big-O requirement: O(n) amortized, where n is the number of elements in the
        // tree

        // an empty tree should return ""
        // otherwise this should return a level order walk of the tree
        // with space separated elements

        // for example, the tree:
        // 0
        // / \
        // 1 2
        // should return the string "0 1 2"

        // and the tree:
        // 0
        // /
        // 1
        // /
        // 2
        // should also be the string "0 1 2"

        // you should use a StringBuilder to create the string
        // Why? String builders are dynamic array lists of characters,
        // while Strings are fixed arrays. So don't append to the end of a
        // string... append to a string builder for better runtime!

        // use a "queue" to create a level-order walk of the tree
        // you don't have access to any of the Java utility classes, and
        // you aren't allowed to use arrays here, so you have two options:

        // Option 1: Make a method-local linked-list node class and use that as a queue

        // Option 2: Since a degenerate tree is a linked list... use that to make a
        // queue!
        // Option 2 hint: the head and tail of the queue to be a TreeNode<TreeNode<T>>

        return null; // replace this!
    }

    /**************************************************
     * You may NOT edit anything below this line except to
     * add JavaDocs. This class must remain as-is for grading.
     **************************************************/
    public static class TreeNode<T> {
        private T data;
        private TreeNode<T> left;
        private TreeNode<T> right;
        private Color color = Color.WHITE;

        public TreeNode() {
        }

        public TreeNode(T data) {
            setData(data);
        }

        public T getData() {
            return this.data;
        }

        public T setData(T data) {
            T oldData = this.data;
            this.data = data;
            return oldData;
        }

        public TreeNode<T> getRight() {
            return this.right;
        }

        public TreeNode<T> setRight(TreeNode<T> right) {
            TreeNode<T> oldRight = this.right;
            this.right = right;
            return oldRight;
        }

        public TreeNode<T> getLeft() {
            return this.left;
        }

        public TreeNode<T> setLeft(TreeNode<T> left) {
            TreeNode<T> oldLeft = this.left;
            this.left = left;
            return oldLeft;
        }

        public Color getColor() {
            return color;
        }

        public Color setColor(Color color) {
            Color oldColor = this.color;
            this.color = color;
            return oldColor;
        }

        public String toString() {
            return this.data.toString();
        }
    }
}
