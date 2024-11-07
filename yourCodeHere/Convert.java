
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * An utility class that allows conversion of different types of tree
 * data structures.
 */
class Convert {

    /**
     * Reads the file at path {@code filename} and parses its first two lines into a
     * {@link ParentPointer} array.
     *
     * <p>Time Complexity: O(n) - linear.
     *
     * @param filename a path to the file to read the data from.
     * @return {@link ParentPointer} array representation of first two lines of
     *         {@code filename}. Not nullable. Never empty.
     *
     * @throws IOException if the {@code filename} couldn't be read.
     * @throws NullPointerException if {@code filename} is null.
     */
    public static ParentPointer[] parentPointerFormat(String filename) throws IOException {
        Objects.requireNonNull(filename, "File name cannot be null");

        // Using char arrays because we will access file lines using single character at
        // a time, so the use of the char array is more intuitive comparing to simple
        // strings
        ParentPointersAndPositions parentPointersAndPositions = getParentPointersAndPositions(filename);
        char[] parentPointers = parentPointersAndPositions.parentPointers;
        char[] positions = parentPointersAndPositions.positions;

        if (parentPointers.length == 0 && positions.length == 0) {
            return new ParentPointer[0];
        }

        // The positions array consists of positions (i.e. "L", "R", "x") and spaces
        // between them, by dividing in a half and incrementing by 1 the size of the
        // array, we will have a pure number of positions which are the number of nodes
        // too.
        ParentPointer[] result = new ParentPointer[positions.length / 2 + 1];

        int parentPointersIndex = 0;
        int positionsIndex = 0;

        // Incrementing positionsIndex by 2 to skip empty spaces
        for (int i = 0; i < result.length; i++, positionsIndex += 2) {
            ParentPointerForNodeAndIndex parentPointerAndPosition = getParentPointerAndIndex(parentPointers,
                    parentPointersIndex);

            int parentPointerId = parentPointerAndPosition.parentPointerId;
            // skipping the next empty char and start at the next parent pointer id of the
            // parentPointers array
            parentPointersIndex = parentPointerAndPosition.parentPointerIndex + 2;

            // Assertions for development process to check if the code works correctly.
            // These lines won't be compiled into the source code.
            // Plus, assertions increase the readability of the code by outlining the
            // desired result.
            assert (parentPointerId >= -2);

            if (parentPointerId == -2) { // the parent pointer id is 'x' so set as a null
                result[i] = null;
            } else {
                assert (parentPointerId >= -1);

                // we could one line these two statements but the intent is clearer with the
                // variable name
                boolean isLeft = positions[positionsIndex] == 'L';
                result[i] = new ParentPointer(parentPointerId, isLeft);
            }

        }

        assert (result != null);
        assert (result.length != 0);

        return result;
    }

    /**
     * Reads two first lines of {@code filename} and converts them to char arrays
     * and then sets them to {@link ParentPointersAndPositions}.
     * 
     * @param filename to convert to {@link ParentPointersAndPositions}
     * @return {@link ParentPointersAndPositions} representing the file
     *
     * @throws IOException              if there are issues with reading the file
     * @throws IllegalArgumentException if the file contains less than two lines
     */
    private static ParentPointersAndPositions getParentPointersAndPositions(String filename) throws IOException {
        Objects.requireNonNull(filename);

        // try with resource to handle auto closer of I/O
        try (BufferedReader buffReader = new BufferedReader(new FileReader(filename))) {
            char[] parentPointers = null;
            char[] positions = null;

            if (!buffReader.ready()) {
                throw new IllegalArgumentException(
                        "Couldn't read Parent Pointers line from the file: " + filename);
            }

            parentPointers = buffReader.readLine().toCharArray();

            if (!buffReader.ready()) {
                throw new IllegalArgumentException(
                        "Couldn't read Positions line from the file: " + filename);
            }

            positions = buffReader.readLine().toCharArray();

            assert (parentPointers != null);
            assert (positions != null);

            return new ParentPointersAndPositions(parentPointers, positions);
        }
    }

    /**
     * It's a class helper for {@link Convert#getParentPointersAndPositions(String)}
     * method. Allows to store in char array form two first lines of the read file
     * where the first line is stored as a
     * {@link ParentPointersAndPositions#parentPointers} and the second as a
     * {@link ParentPointersAndPositions#positions}
     */
    private static class ParentPointersAndPositions {
        /**
         * A char array of parent pointers. Not nullable
         */
        private final char[] parentPointers;
        /**
         * A char array of positions of the elements (i.e. whether it is left or right,
         * or the element is skipped)
         */
        private final char[] positions;

        /**
         * Creates an object with initialized 
         * {@link ParentPointersAndPositions#parentPointers} as {@code parentPointers} and
         * {@link ParentPointersAndPositions#positions} as {@code positions}.
         *
         * @param parentPointers to set. Not nullable
         * @param positions      to set. Not nullable
         */
        public ParentPointersAndPositions(char[] parentPointers, char[] positions) {
            this.parentPointers = Objects.requireNonNull(parentPointers);
            this.positions = Objects.requireNonNull(positions);
        }

        /**
         * A String representation of the array. Listing its parentPointers and
         * positions char arrays.
         */
        @Override
        public String toString() {
            return String.format(
                    "ParentPointersAndPositions=[ %1$s parentPointers=%2$s %1$s positions=%3$s]",
                    System.lineSeparator(),
                    Arrays.toString(parentPointers), Arrays.toString(positions));
        }

    }

    /**
     * Allows to retrieve a parent pointer id starting from the {@code startIndex}
     * and the position where the read operation stopped at (inclusive)
     *
     * {@link ParentPointerForNodeAndIndex#parentPointerId} will represent the
     * retrieved parent pointer id.
     * If the parent pointer is 'x',
     * {@link ParentPointerForNodeAndIndex#parentPointerId} will be equal to
     * "-2"
     *
     * {@link ParentPointerForNodeAndIndex#parentPointerIndex} will represent the
     * index of the position of the {@code parentPointers} array where the read
     * operation of the parent pointer id has stopped at (inclusive)
     * 
     * @param parentPointers array to read the parent pointer from
     * @param startIndex     to start reading the {@code parentPointers} array at
     *
     * @return {@link ParentPointerForNodeAndIndex}
     *
     *         throws NullPointerException if {@code parentPointers} is null
     */
    private static ParentPointerForNodeAndIndex getParentPointerAndIndex(
            char[] parentPointers, int startIndex) {

        Objects.requireNonNull(parentPointers);

        if (startIndex < 0 || startIndex >= parentPointers.length) {
            throw new IllegalArgumentException(
                    String.format(
                            "StartIndex cannot be less than zero or equal/more than %s, current value: %s",
                            parentPointers.length, startIndex));
        }

        int parentPointerId = -1;
        int parentPointerCharsIndex = 0;
        char[] parentPointerChars = new char[parentPointers.length - startIndex];

        for (int i = startIndex; i < parentPointers.length; i++) {
            char c = parentPointers[i];

            // If we encounter '-' it will always stand for '-1' so increase the
            // startIndex by 1 plus 1 for the next empty slot.
            if (c == '-') {
                startIndex = i + 1;
                break;
            } else if (c >= 48 && c <= 57) {// [0-9]
                parentPointerChars[parentPointerCharsIndex++] = c;
            } else if (c == 'x') {
                parentPointerId = -2; // special case for x
                break;
            } else if (c != ' ') {
                throw new IllegalArgumentException("Couldn't parse char: " + c);
            }

            // if c is ' ' or if it's the end of the array, it means we should stop
            // parsing the parent pointer id and convert it to the integer
            if (c == ' ' || i == parentPointers.length - 1) {
                parentPointerId = Integer.valueOf(
                        String.valueOf(parentPointerChars, 0, parentPointerCharsIndex));

                assert (parentPointerId >= -1);
                break;
            }
        }

        assert (parentPointerId >= -2);
        assert (startIndex >= 0 && startIndex < parentPointers.length);

        return new ParentPointerForNodeAndIndex(parentPointerId, startIndex);
    }

    /**
     * It's a class helper for {@link Convert#getParentPointerAndIndex(char[], int)}
     * method to return the results of the reading operation of the parent pointer
     * id from the array of parent pointers and the index where the reading
     * operation stopped at.
     */
    private static class ParentPointerForNodeAndIndex {
        /**
         * Represents a parent pointer Id.
         */
        private final int parentPointerId;

        /**
         * Represents a parent pointer index of the last read operation (inclusive).
         */
        private final int parentPointerIndex;

        /**
         * Creates an object with initialized
         * {@link ParentPointerForNodeAndIndex#parentPointerId} as {@code parentPointerId}
         * {@link ParentPointerForNodeAndIndex#parentPointerIndex} as {@code parentPointerIndex}.
         *
         * @param parentPointerId    to set as a parent pointer id
         * @param parentPointerIndex to set as a parent pointer index
         *
         * @throws IllegalArgumentException if parentParentPoinerId < -2 or
         *                                  parentPointerIndex < 0
         */
        public ParentPointerForNodeAndIndex(int parentPointerId, int parentPointerIndex) {
            if (parentPointerId < -2) {
                throw new IllegalArgumentException("ParentPointerId cannot be below -2");
            }
            if (parentPointerIndex < 0) {
                throw new IllegalArgumentException("ParentPointerIndex cannot be below 0");
            }

            this.parentPointerId = parentPointerId;
            this.parentPointerIndex = parentPointerIndex;
        }

        @Override
        public String toString() {
            return "ParentPointerForNodeAndItsPosition [parentPointerId=" + parentPointerId
                    + ", parentPointerIndex=" + parentPointerIndex + "]";
        }

    }

    /**
     * Reads the file at path {@code filename} and parses its first two lines into a
     * {@link ParentPointer} array.
     *
     * <p>Time Complexity: O(n) - linear.
     *
     * @param filename a file path to the data from
     * @return {@link LinkedTree} representation of first two lines of
     *         {@code filename}. Not nullable.
     *
     * @throws IOException if the {@code filename} couldn't be read.
     * @throws NullPointerException if {@code filename} is null
     */
    @SuppressWarnings("unchecked")
    public static LinkedTree<Integer> treeLinkedFormat(String filename) throws IOException {
        Objects.requireNonNull(filename, "File name cannot be null");

        ParentPointersAndPositions parentPointersAndPositions = getParentPointersAndPositions(filename);
        char[] parentPointers = parentPointersAndPositions.parentPointers;
        char[] positions = parentPointersAndPositions.positions;

        if (parentPointers.length == 0 && positions.length == 0) {
            return new LinkedTree<Integer>(null);
        }

        // Index of the "nodes" array represents the id of that node, so
        // If the node was created it could be easily looked up by using its id as an
        // index
        //
        // If the child node cannot find its parent node, it will create an empty
        // node under parent's id here and the parent node will reference this child
        // node.
        // Later when its time to create the parent node itself it will simply use the
        // already created object with its children linked and just assign the data
        // value to the object
        LinkedTree.TreeNode<Integer>[] nodes = new LinkedTree.TreeNode[positions.length / 2 + 1];
        LinkedTree.TreeNode<Integer> root = null;

        int parentPointersIndex = 0;
        int positionsIndex = 0;

        for (int i = 0; i < nodes.length; i++, positionsIndex += 2) {
            ParentPointerForNodeAndIndex parentPointerAndPosition = getParentPointerAndIndex(
                    parentPointers, parentPointersIndex);

            int parentPointerId = parentPointerAndPosition.parentPointerId;
            parentPointersIndex = parentPointerAndPosition.parentPointerIndex + 2;
            assert (parentPointerId >= -2);

            if (parentPointerId == -2) {// the parent pointer id is 'x' skip it
                continue;
            }

            // checking if the node was already created by its children otherwise create a
            // new one
            LinkedTree.TreeNode<Integer> current = nodes[i];
            if (current == null) {
                nodes[i] = current = new LinkedTree.TreeNode<Integer>();
            }
            current.setData(i);

            // In case if the current node is the root node, assign this node as a root one
            // and skip parent finding process.
            if (parentPointerId == -1) {
                root = current;
                continue;
            }

            // Search for the parent node and if found, reference current node by the
            // parent one.
            // If the parent node wasn't found, create an empty node (without the data)
            // and reference the current node by the parent one.
            LinkedTree.TreeNode<Integer> parent = nodes[parentPointerId];
            if (parent == null) {
                nodes[parentPointerId] = parent = new LinkedTree.TreeNode<Integer>();
            }

            if (positions[positionsIndex] == 'L') {
                parent.setLeft(current);
            } else {
                parent.setRight(current);
            }

        }

        assert (root != null);
        return new LinkedTree<Integer>(root);
    }

    /**
     * Accepts a filename and returns the traditional array storage for trees (level
     * order storage).
     *
     * <p>Time Complexity: Best case O(n) - linear; Worst case O(n^2) - quadratic.
     *
     * <p>Note: Time complexity is calculated where we don't account for the copy
     * operation of the resulting array up to the index where the last node was
     * stored.
     *
     * <p>The "Pure" Worst case could have been achieved by setting the size of the
     * resulting array to the 2^(number of nodes; even if they are null) but I
     * considered returning a "cleaner" array a much better option then dealing with
     * padding nulls.
     *
     * <p>Best Case: When there are no children nodes that are referenced by
     * parents node that are defined later (i.e. first children node is parsed then
     * its parent)
     *
     * <p>For example: [-1, 0R, 1L, null, 2R] each children node references the parent
     * that was already defined
     * 
     * <p>Worst Case: all the children are defined before their parents;
     *
     * <p>For example: [1L, 2L, 3L, 4L, -1] each children node references the parent
     * that was already defined.
     *
     * @param filename a file path to the data to read from
     * @return {@link Integer} array representing the traditional array storage for
     *         trees (level order storage).
     *
     * @throws IOException if the {@code filename} couldn't be read.
     * @throws NullPointerException if {@code filename} is null.
     */
    @SuppressWarnings("unchecked")
    public static Integer[] treeArrayFormat(String filename) throws IOException {
        Objects.requireNonNull(filename, "File name cannot be null");

        ParentPointersAndPositions parentPointersAndPositions = getParentPointersAndPositions(filename);
        char[] parentPointers = parentPointersAndPositions.parentPointers;
        char[] positions = parentPointersAndPositions.positions;

        if (parentPointers.length == 0 && positions.length == 0) {
            return new Integer[0];
        }

        int numberOfNodes = positions.length / 2 + 1;
        Integer[] result = new Integer[(int) Math.pow(2, numberOfNodes)];

        // Index of the "nodes" array represents the id of that node, so
        // If the node was created it could be easily looked up by using its id as an
        // index
        //
        // Same working logic as in treeLinkedFormat() but here it is used
        // for the cases when the child node needs parent's index withing the
        // resulting array but the parent node hasn't been defined yet.
        // So, when the parent node actually gets defined, it can recursively go through
        // all of its children nodes and initialize them because at this point it can
        // provide to the children nodes its index within the resulting array.
        LinkedTree.TreeNode<Integer>[] nodes = new LinkedTree.TreeNode[numberOfNodes];

        // Allows to get an index of the node within the resulting level ordered array
        // using the node's id.
        // Used by children nodes that need to find the position of the parent's node
        // within the resulting array using parentPointerId so the child node can
        // calculate it's own location in that array.
        int[] pointers = new int[numberOfNodes];

        int lastNodeIndex = 0;
        int parentPointersIndex = 0;
        int positionsIndex = 0;

        for (int i = 0; i < numberOfNodes; i++, positionsIndex += 2) {
            ParentPointerForNodeAndIndex parentPointerAndPosition = getParentPointerAndIndex(parentPointers,
                    parentPointersIndex);

            int parentPointerId = parentPointerAndPosition.parentPointerId;
            parentPointersIndex = parentPointerAndPosition.parentPointerIndex + 2;
            assert (parentPointerId >= -2);

            if (parentPointerId == -2) {
                continue;
            }

            // Checking if the node was already created by its children otherwise create a
            // new one
            LinkedTree.TreeNode<Integer> current = nodes[i];
            if (current == null) {
                nodes[i] = current = new LinkedTree.TreeNode<Integer>();
            }
            current.setData(i);

            // If the it's the root node, add it to the beginning of the resulting array
            if (parentPointerId == -1) {
                result[0] = i;
                pointers[i] = 0;
                lastNodeIndex = Math.max(lastNodeIndex, initialize(nodes[i], result, pointers));

                continue;
            }

            LinkedTree.TreeNode<Integer> parent = nodes[parentPointerId];
            if (parent == null) {
                nodes[parentPointerId] = parent = new LinkedTree.TreeNode<Integer>();
            }

            if (positions[positionsIndex] == 'L') {
                assert (parent.getLeft() == null);
                parent.setLeft(current);
            } else {
                assert (parent.getRight() == null);
                parent.setRight(current);
            }

            // If the parent node have its data set, it means it was already defined
            // so initialize all the children nodes including the current one of the parent
            if (parent.getData() != null) {
                lastNodeIndex = Math.max(lastNodeIndex, initialize(parent, result, pointers));
            }
        }

        assert (result != null);
        assert (result.length != 0);
        assert (lastNodeIndex >= 0);
        assert (lastNodeIndex < result.length);

        return Arrays.copyOf(result, lastNodeIndex + 1);
    }

    /**
     * Initialize the {@code result} array using the children nodes of {@code root}
     * in traditional array storage for trees manner.
     *
     * @param root     of the tree to use for initialization
     * @param result   where the children nodes of the root will be initialized to
     * @param pointers allows to access to the index of the node within the
     *                 {@code result} array by using node's data as index
     *
     * @return the biggest index of the initialized nodes
     *
     * @throws NullPointerException if the {@code root} or its data or the data of
     *                              its children nodes is {@code null}
     */
    private static int initialize(LinkedTree.TreeNode<Integer> root, Integer[] result, int[] pointers) {
        Objects.requireNonNull(root);
        Objects.requireNonNull(root.getData());
        Objects.requireNonNull(result);

        int rootPos = pointers[root.getData()];

        int lastNodeIndex = rootPos;

        if (root.getLeft() != null) {
            int id = 2 * rootPos + 1;
            Integer data = root.getLeft().getData();

            Objects.requireNonNull(data);
            result[id] = data;
            pointers[data] = id;
            lastNodeIndex = Math.max(lastNodeIndex, initialize(root.getLeft(), result, pointers));
        }

        if (root.getRight() != null) {
            int id = 2 * rootPos + 2;
            Integer data = root.getRight().getData();

            Objects.requireNonNull(data);
            result[id] = data;
            pointers[data] = id;

            lastNodeIndex = Math.max(lastNodeIndex, initialize(root.getRight(), result, pointers));
        }

        return lastNodeIndex;
    }

    /**
     * Iterates over the {@code tree} and counts all non-null values.
     * The number of all non-null values then will be divided by the index of the
     * last non-null value plus one.
     *
     * <p>Time Complexity: O(n) - linear.
     *
     * @param <T> the type of the array
     * @param tree to calculate the load of
     * @return the load of the of {@code tree} excluding padded {@code null}s
     *
     * @throws NullPointerException if the {@code tree} is {@code null}
     */
    public static <T> double arrayLoad(T[] tree) {
        Objects.requireNonNull(tree);

        int usedSlots = 0;
        int lastUsedSlotId = -1;
        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                usedSlots++;
                lastUsedSlotId = i;
            }
        }

        if (usedSlots == 0 || lastUsedSlotId == -1) {
            return 0;
        }

        return (double) usedSlots / ++lastUsedSlotId;
    }

    /**
     * Merges two {@link LinkedTree}s or returns null if they are incompatible.
     *
     * @param tree1 to merge
     * @param tree2 to merge
     *
     * @return a merged {@link LinkedTree} or null if the they are incompatible
     *
     * @throws UnsupportedOperationException because unsupported
     */
    @SuppressWarnings("unchecked")
    public static LinkedTree<Integer> merge(LinkedTree<Integer> tree1, LinkedTree<Integer> tree2) {
        // Note: you _may not need_ the suppress warnings above, but it is there _if_
        // you need

        // your code here
        // Big-O requirement: Can be done in O(n), where n is the size of the biggest
        // tree no required big-O for this method, just make it work!

        throw new UnsupportedOperationException(); // replace this if you choose option 1
    }

    /**
     * Checks whether the {@code tree} contains duplicates.
     *
     * @param tree to check whether contains duplicates
     * @return true if the {@code tree} contains duplicates, false if otherwise
     *
     * @throws UnsupportedOperationException because unsupported
     */
    public static boolean containsDuplicates(LinkedTree<Integer> tree) {
        // your code here
        // Big-O requirement: Can be done in O(n), where n is the size of the biggest
        // tree
        // no required big-O for this method, just make it work!

        throw new UnsupportedOperationException(); // replace this if you choose option 1
    }


    /**
     * Returns an array of {@link ParentPointer}s that were converted from the {@code tree}.
     *
     * @param tree to convert parent pointers from
     * @return {@link ParentPointer} array of parent pointers
     *
     * @throws UnsupportedOperationException because unsupported
     */
    @SuppressWarnings("unchecked")
    public static ParentPointer[] toParentPointer(LinkedTree<Integer> tree) {
        // Note: you _may not need_ the suppress warnings above, but it is there _if_
        // you need

        // your code here
        // Big-O requirement: Can be done in O(n), where n is the largest ID node in the
        // tree
        // no required big-O for this method, just make it work!

        throw new UnsupportedOperationException(); // replace this if you choose option 1
    }

    // Option 2: Merging using array structures

    /**
     * Time complexity: O(n) linear where n is the length of the biggest tree.
     * The resulting tree will have the length of biggest tree.
     *
     * @param tree1 the tree to merge
     * @param tree2 the tree to merge
     *
     * @return {@link Integer} array of the merged trees, if the trees are
     *         incompatible a {@code null} is returned
     */
    public static Integer[] merge(Integer[] tree1, Integer[] tree2) {
        Objects.requireNonNull(tree1);
        Objects.requireNonNull(tree2);

        // find the biggest and the smallest trees
        Integer[] bigTree = tree1.length > tree2.length ? tree1 : tree2;
        Integer[] smallTree = tree2.length < tree1.length ? tree2 : tree1;

        for (int i = 0; i < smallTree.length; i++) {
            if (bigTree[i] == null) {
                bigTree[i] = smallTree[i];
            } else if (smallTree[i] == null) {
                continue;
            } else if (bigTree[i].compareTo(smallTree[i]) != 0) {
                return null; // incompatible nodes;
            }

            bigTree[i] = smallTree[i];
        }

        return bigTree;

    }

    /**
     * Checks whether the {@code tree} contains duplicates.
     *
     * <p>Time Complexity: O(n) - linear.
     * 
     * @param tree to check whether contains duplicates
     * @return whether the array contains duplicates, nulls aren't accounted
     *
     * @throws NullPointerException if the {@code tree} is {@code null}
     */
    public static boolean containsDuplicates(Integer[] tree) {
        Objects.requireNonNull(tree);

        // arrays used to mark the values of found nodes
        boolean[] foundNodes = new boolean[tree.length];

        for (int i = 0; i < tree.length; i++) {
            if (tree[i] == null) {
                continue;
            }
            if (!foundNodes[tree[i]]) {
                foundNodes[tree[i]] = true;
            } else {
                return true;
            }

        }
        return false;
    }

    /**
     * Returns an array of {@link ParentPointer}s that were converted from the {@code tree}.
     *
     * <p>Time Complexity: O(n) - linear.
     * 
     * @param tree to convert to the parent pointer array
     * @return {@link ParentPointer} array that was converted from {@code tree} that
     *         represents a traditional array storage for trees
     *
     * @throws NullPointerException if the {@code tree} is {@code null}
     */
    public static ParentPointer[] toParentPointer(Integer[] tree) {
        Objects.requireNonNull(tree);

        ParentPointer[] parentPointers = new ParentPointer[tree.length];

        for (int i = 0; i < tree.length; i++) {
            Integer pointerId = tree[i];

            if (pointerId == null)
                continue;

            int parentPointerIndex = (i - 1) / 2;
            int parentPointerId = i == 0 ? -1 : tree[parentPointerIndex];
            parentPointers[pointerId] = new ParentPointer(parentPointerId, (i - 1) % 2 == 0);
        }

        return parentPointers;
    }

    /**
     * Represents an element that contains information about its parent id and
     * whether it is a left or right child.
     */
    public static class ParentPointer {
        /**
         * Parent's pointer id.
         */
        int parentId;
        /**
         * Whether the element is a left or right child of the parent.
         */
        boolean isLeft; // note: it doesn't matter what you set this to for root, it will be ignored

        /**
         * Creates an empty parent pointer, where {@link ParentPointer#parentId} is "0"
         * and {@link ParentPointer#isLeft} false.
         */
        public ParentPointer() {
        }

        /**
         * Creates a parent pointer, where {@link ParentPointer#parentId} is set to
         * {@code parentId} and {@link ParentPointer#isLeft} false.
         *
         * @param parentId to set to {@link ParentPointer#parentId}
         */
        public ParentPointer(int parentId) {
            this.parentId = parentId;
        }

        /**
         * Creates a parent pointer, where {@link ParentPointer#parentId} is set to
         * {@code parentId} and {@link ParentPointer#isLeft} set to {@code isLeft}.
         * 
         * @param parentId to set to {@link ParentPointer#parentId}
         * @param isLeft to set to {@link ParentPointer#isLeft}
         */
        public ParentPointer(int parentId, boolean isLeft) {
            this(parentId);
            this.isLeft = isLeft;
        }

        /**
         * A string representation of the parent pointer, if the
         * {@link ParentPointer#parentId} is = "-1" only "-1" is returned.
         * Otherwise the parent pointer id and "L" or "R" together
         *
         * <p>For example:
         * "-1" when parentId = -1
         * "1L" when parentId = 1 and isLeft = true
         *
         * @return a string representation of the object
         */
        public String toString() {
            return parentId + ((parentId == -1) ? "" : (isLeft ? "L" : "R"));
        }
    }
}
