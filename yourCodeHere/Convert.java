//Note: You have access to all of Java IO and Java Utility here. Make good use of them!
//No additional imports allowed.

import java.io.*;
import java.util.*;

/**************************************************
 * Random advice and tips:
 * (1) Don't forget to close your IO streams!
 * (2) Just because you _can_ implement something in
 * the same number of lines as me doesn't mean
 * that's the best or only solution. Do you!
 * (3) The class "TreeNode" is called "LinkedTree.TreeNode"
 * from this class
 **************************************************/

class Convert {
    /************************************************************************
     * You may add additional PRIVATE methods. You may NOT add static class
     * variables (this is for your own good... there is no "good" use of class
     * variables here and a very high chance of losing points if you don't
     * know how static "really" works... so don't do it!
     ************************************************************************/

    /********************************************************************************/
    /* Parent Pointer Format */
    /********************************************************************************/

    public static ParentPointer[] parentPointerFormat(String filename) throws IOException {
        // your code here
        // Big-O requirement: O(n), where n is the largest ID node in the tree

        // Hint: My solution is ~12 lines (including two helper functions, not but not
        // including headers, space, and comments)

        // Note: Any values NOT in the tree (x/x in the file) should be null in the
        // returned array of ParentPointer objects.

        return null; // replace this!
    }

    /********************************************************************************/
    /* Linked Format */
    /********************************************************************************/

    @SuppressWarnings("unchecked")
    public static LinkedTree<Integer> treeLinkedFormat(String filename) throws IOException {
        // Note: you _may not need_ the suppress warnings above, but it is there _if_
        // you need it

        // your code here
        // Big-O requirement: Can be done in O(n), where n is the largest ID node in the
        // tree
        // required to be done in no more than O(n^2)

        // Hint: My solution is ~12 lines (including one helper function, not but not
        // including headers, space, and comments)

        // Hint 2:

        return null; // replace this!
    }

    /********************************************************************************/
    /* Array Format */
    /********************************************************************************/

    @SuppressWarnings("unchecked")
    public static Integer[] treeArrayFormat(String filename) throws IOException {
        // Note: you _may not need_ the suppress warnings above, but it is there _if_
        // you need

        // your code here
        // Big-O requirement: Can be done in O(2^n), where n is the height of the tree +
        // 1,
        // no required big-O for this method, just make it work!

        // Hint: My solution is ~12 lines (including one helper function, not but not
        // including headers, space, and comments)... Yes, they really did all
        // come out as 12 lines, that's not a copy-and-paste error

        return null; // replace this!
    }

    public static <T> double arrayLoad(T[] tree) {
        // Note: This is a generic static method if you've never seen one before!
        // What's special? This _class_ is not generic, only this one _method_.

        // This method will be used to calculate the load (percentage of use) for
        // both ParentPointer[] and Integer[] trees, do NOT consider "extra" nulls
        // after the last "real" item when performing this calculation.

        // your code here
        // Big-O requirement: O(n) where n is the length of the input array "tree"

        // Hint: My solution is ~6 lines (not including headers and comments)

        return -1.0; // replace this!
    }

    /********************************************************************************/
    /* Merge Operations */
    /*
     * You must implement ONE of these options (each option contains three methods)
     */
    /*
     * IMPORTANT: You may NOT edit/destroy any of the input trees in these methods!
     */
    /********************************************************************************/

    // Option 1: Merging using linked structures

    @SuppressWarnings("unchecked")
    public static LinkedTree<Integer> merge(LinkedTree<Integer> tree1, LinkedTree<Integer> tree2) {
        // Note: you _may not need_ the suppress warnings above, but it is there _if_
        // you need

        // your code here
        // Big-O requirement: Can be done in O(n), where n is the size of the biggest
        // tree
        // no required big-O for this method, just make it work!

        throw new UnsupportedOperationException(); // replace this if you choose option 1
    }

    public static boolean containsDuplicates(LinkedTree<Integer> tree) {
        // your code here
        // Big-O requirement: Can be done in O(n), where n is the size of the biggest
        // tree
        // no required big-O for this method, just make it work!

        throw new UnsupportedOperationException(); // replace this if you choose option 1
    }

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

    // you may NOT edit/destroy either of the input trees
    public static Integer[] merge(Integer[] tree1, Integer[] tree2) {
        // your code here
        // Big-O requirement: Can be done in O(n), where n is biggest input array
        // no required big-O for this method, just make it work!

        // Hint: My solution is ~7 lines (not including headers, space, and comments)
        throw new UnsupportedOperationException(); // replace this if you choose option 2
    }

    public static boolean containsDuplicates(Integer[] tree) {
        // your code here
        // Big-O requirement: Can be done in O(n), where n is the number of nodes in the
        // tree
        // no required big-O for this method, just make it work!

        // Hint: My solution is ~6 lines (not including headers, space, and comments)
        throw new UnsupportedOperationException(); // replace this if you choose option 2
    }

    public static ParentPointer[] toParentPointer(Integer[] tree) {
        // your code here
        // Big-O requirement: Can be done in O(n), where n is the largest ID node in the
        // tree
        // no required big-O for this method, just make it work!

        // Hint: My solution is ~9 lines (not including headers, space, and comments)
        throw new UnsupportedOperationException(); // replace this if you choose option 2
    }

    /********************************************************************************/
    /* Support Classes */
    /* You may NOT edit anything below this line except to add JavaDocs. */
    /* This class must remain as-is for grading. */
    /********************************************************************************/

    public static class ParentPointer {
        int parentId;
        boolean isLeft; // note: it doesn't matter what you set this to for root, it will be ignored

        public ParentPointer() {
        }

        public ParentPointer(int parentId) {
            this.parentId = parentId;
        }

        public ParentPointer(int parentId, boolean isLeft) {
            this(parentId);
            this.isLeft = isLeft;
        }

        public String toString() {
            return parentId + ((parentId == -1) ? "" : (isLeft ? "L" : "R"));
        }
    }
}
