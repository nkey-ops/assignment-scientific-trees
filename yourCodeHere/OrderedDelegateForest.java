//***************************************************************
// TODO: Nothing, all done. You may read this if you'd like,
// but you may not need to.
//***************************************************************

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import java.awt.Color;

import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.DelegateForest;

import edu.uci.ics.jung.graph.util.EdgeType;

/**
 *  Forest with ordered edges.
 *  
 *  @author Katherine (Raven) Russell
 */
class OrderedDelegateForest extends DelegateForest<LinkedTree.TreeNode,DisplayEdge> {
	/**
	 *  Constructs a forest with a specific tree root.
	 *  
	 *  @param root the root of the new tree
	 */
	public OrderedDelegateForest(LinkedTree.TreeNode root) {
		super();
		if(root != null) addVertex(root);
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public boolean addVertex(LinkedTree.TreeNode vertex) {
		super.addVertex(vertex);
		
		if(vertex.getLeft() != null) {
			addVertex(vertex.getLeft());
			addEdge(new DisplayEdge("left", Color.MAGENTA), vertex, vertex.getLeft());
		}
		if(vertex.getRight() != null) {
			addVertex(vertex.getRight());
			addEdge(new DisplayEdge("right", Color.BLACK), vertex, vertex.getRight());
		}
		
		return true;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Collection<DisplayEdge> getChildEdges(LinkedTree.TreeNode vertex) {
		List<DisplayEdge> c = new ArrayList<>(super.getChildEdges(vertex));
		Collections.sort(c, new Comparator<DisplayEdge>() {
			public int compare(DisplayEdge thing1, DisplayEdge thing2) {
				return (getDest(thing1) == vertex.getLeft()) ? -1 : 1;
			}
		});
		
		return c;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Collection<LinkedTree.TreeNode> getChildren(LinkedTree.TreeNode vertex) {
		List<LinkedTree.TreeNode> c = new ArrayList<>(super.getChildren(vertex));
		Collections.sort(c, new Comparator<LinkedTree.TreeNode>() {
			public int compare(LinkedTree.TreeNode thing1, LinkedTree.TreeNode thing2) {
				return (thing1 == vertex.getLeft()) ? -1 : 1;
			}
		});
		
		return c;
	}
}