//***************************************************************
// TODO: Nothing, all done. You may read this if you'd like,
// but you may not need to.
//***************************************************************


import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.*;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.*;

import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

import org.apache.commons.collections15.*;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;

/**
 *  GUI for forest interactions.
 *  
 *  @author Katherine (Raven) Russell
 */
class SimGUI {
	/**
	 *  Frame for the GUI.
	 */
	private JFrame frame;
	
	/**
	 *  The number of GUI trees.
	 */
	private static final int NUM_TREES = 3;
	
	/**
	 *  The panel containing the load buttons.
	 */
	private JPanel buttonPanel = null;
	
	/**
	 *  The file containing the first tree.
	 */
	private File file1 = null;
	
	/**
	 *  The file containing the second tree.
	 */
	private File file2 = null;
	
	/**
	 *  The temporary file to use for merging.
	 */
	private static final String TEMP_FILE = "temp.tree";
	
	/**
	 *  Load up the GUI.
	 */
	public SimGUI() {
		frame = new JFrame("Science Trees!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200, 700);
		frame.getContentPane().setLayout(new BorderLayout());
		
		resetDisplay();
		
		frame.setVisible(true);
	}
	
	/**
	 *  Load a new simulation.
	 */
	public void resetDisplay() {
		String[] locs = new String[] { BorderLayout.WEST, BorderLayout.CENTER, BorderLayout.EAST };
		frame.getContentPane().removeAll();
		
		for(int i = 0; i < NUM_TREES; i++) {
			JPanel panel = new JPanel(new BorderLayout());
			addArrayLinkedArray(i, panel);
			frame.add(panel, locs[i]);
		}
		
		frame.getContentPane().repaint();
		frame.revalidate();
		
		//menu needs to go after gm is set (if any)
		frame.setJMenuBar(null);
		frame.setJMenuBar(makeMenu());
		
		makeBottomButtons();
	}
	
	/**
	 *  Makes the menu for the simulation.
	 *  
	 *  @return the menu created
	 */
	public JMenuBar makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		//exit option
		JMenu simMenu = new JMenu("Simulation");
		simMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		simMenu.add(exit);
		menuBar.add(simMenu);
		
		return menuBar;
	}
	
	/**
	 *  Creates one forest panel.
	 *  
	 *  @param index which tree to create (0=file1, 1=file2, 2=merged)
	 *  @param panel the panel to add it to
	 */
	public void addArrayLinkedArray(int index, JPanel panel) {
		if(index == 2) {
			if(file1 != null && file2 != null) {
				addMergedInfo(panel);
			}
		}
		else {
			if(index == 0 && file1 == null) return;
			if(index == 1 && file2 == null) return;
			
			String filename = (index == 0) ? file1.getPath() : file2.getPath();
				
			try{
				Convert.ParentPointer[] tree1 = Convert.parentPointerFormat(filename);
				String load1 = String.format("Load: ~%.2f%%\n", Convert.arrayLoad(tree1)*100);
				addOneArray("Parent Pointer Storage", tree1, load1, panel, true);
				
				LinkedTree<Integer> tree2 = Convert.treeLinkedFormat(filename);
				if(tree2 != null) {
					OrderedDelegateForest tree = new OrderedDelegateForest(tree2.getRoot());
					addOneLinkedTree(panel, tree, tree2.getRoot());
				}
				Integer[] tree3 = Convert.treeArrayFormat(filename);
				String load2 = String.format("Load: ~%.2f%%\n", Convert.arrayLoad(tree3)*100);
				addOneArray("Traditional Array Storage", tree3, load2, panel, false);
			}
			catch(IOException e) {
				JOptionPane.showMessageDialog(frame, e.toString(), "File Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	/**
	 *  Creates one merged forest panel.
	 *  
	 *  @param panel the panel tp add it to
	 */
	private void addMergedInfo(JPanel panel) {
		Integer[] arrayTree = null;
		LinkedTree<Integer> linkedTree = null;
		boolean dup = false;
		Convert.ParentPointer[] parentTree = null;
		OrderedDelegateForest forest = null;
		String error = "No Option Implemented";
		
		try {
			arrayTree = Convert.merge(Convert.treeArrayFormat(file1.getPath()), Convert.treeArrayFormat(file2.getPath()));
			
			if(arrayTree != null) {
				dup = Convert.containsDuplicates(arrayTree);
				
				if(!dup) {
					parentTree = Convert.toParentPointer(arrayTree);
					writeToFile(parentTree);
					linkedTree = Convert.treeLinkedFormat(TEMP_FILE);
					(new File(TEMP_FILE)).delete();
				}
				else {
					error = "Duplicates Detected";
				}
			}
			else {
				error = "Incompatible Trees";
			}
		}
		catch(UnsupportedOperationException e) { }
		catch(IOException e) { JOptionPane.showMessageDialog(frame, e.toString(), "File Error!", JOptionPane.ERROR_MESSAGE); }
		
		try {
			linkedTree = Convert.merge(Convert.treeLinkedFormat(file1.getPath()), Convert.treeLinkedFormat(file2.getPath()));
			
			if(linkedTree != null) {
				dup = Convert.containsDuplicates(linkedTree);
				
				if(!dup) {
					parentTree = Convert.toParentPointer(linkedTree);
					writeToFile(parentTree);
					arrayTree = Convert.treeArrayFormat(TEMP_FILE);
					(new File(TEMP_FILE)).delete();
				}
				else {
					error = "Duplicates Detected";
				}
			}
			else {
				error = "Incompatible Trees";
			}
		}
		catch(UnsupportedOperationException e) { }
		catch(IOException e) { JOptionPane.showMessageDialog(frame, e.toString(), "File Error!", JOptionPane.ERROR_MESSAGE); }
		
		addOneArray("Merged Parent Pointers", parentTree, (parentTree == null ? error : ""), panel, true);
		if(linkedTree != null) addOneLinkedTree(panel, new OrderedDelegateForest(linkedTree.getRoot()), linkedTree.getRoot());
		addOneArray("Merged Array Tree", arrayTree, (parentTree == null ? error : ""), panel, false);
	}
	
	/**
	 *  Writes parent pointer array to a file
	 *  so it can be read back in.
	 *  
	 *  @param arr the array to print out
	 *  @throws IOException if temp file can't be written to
	 */
	private void writeToFile(Convert.ParentPointer[] arr) throws IOException {
		StringBuilder line1 = new StringBuilder();
		for(Convert.ParentPointer p : arr) {
			if(p == null) line1.append("x ");
			else line1.append(""+p.parentId + " ");
		}
		
		StringBuilder line2 = new StringBuilder();
		for(Convert.ParentPointer p : arr) {
			if(p == null || p.parentId == -1) line2.append("x ");
			else line2.append(p.isLeft ? "L " : "R ");
		}
			
		try(BufferedWriter b = new BufferedWriter(new FileWriter(TEMP_FILE))) {
			b.write(line1.toString().trim());
			b.write("\n");
			b.write(line2.toString().trim());
			b.write("\n");
		}
	}
		
	/**
	 * The image panel with the graphics.
	 */
	private static class ImagePanel extends JPanel {
		
		/**
		 *  The image itself.
		 */
		private BufferedImage image;
		
		/**
		 *  Creates an image panel from an image.
		 *  
		 *  @param image the image to use for drawing
		 */
		public ImagePanel(BufferedImage image) { setImage(image); }
		
		/**
		 * Set the image to be displayed.
		 * @param image the image to display
		 */
		public void setImage(BufferedImage image) {
			this.image = image;
			this.setAlignmentY(Component.TOP_ALIGNMENT);
			this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}
	
	/**
	 *  Creates one array panel.
	 *  
	 *  @param label the top label for the array
	 *  @param arr the array to draw
	 *  @param label2 the bottom label for the array
	 *  @param panel the panel to put it in
	 *  @param top whether to place at the top or bottom of the panel
	 *  @param <T> the type of array being worked with
	 */
	public <T> void addOneArray(String label, T[] arr, String label2, JPanel panel, boolean top) {
		int imageHeight = frame.getHeight()/6;
		int imageWidth = (frame.getWidth()/3)-6;
		
		int arrayHeight = imageHeight/4;
		int arrayLine1 = arrayHeight; //start one array size down
		int arrayLine2 = arrayHeight*3; //start two array size down
		
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		
		//setup graphics for drawing
		Graphics2D g = image.createGraphics();
		
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLACK);
		
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		//make the image white to start with
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, image.getWidth(), image.getHeight());
		
		//draw the outline of the array (gray with black box around)
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, arrayLine1, imageWidth, arrayHeight);
		
		g.setColor(Color.BLACK);
		g.drawRect(0, arrayLine1, imageWidth, arrayHeight);
		
		//label them
		g.drawString(label, 4, arrayLine1 - 16);
		
		//draw lower label
		g.drawString(label2, 4, imageHeight - 8);
		
		//draw the actual array?
		if(arr != null && arr.length > 0) {
			
			//figure out the actual length ignoring nulls at the end
			int arrayLen = arr.length;
			for(arrayLen = arr.length; arrayLen >= 0 && arr[arrayLen-1] == null; arrayLen--); 
			
			//how big should the boxes be?
			int colWidth = imageWidth / arrayLen;
			if(colWidth > arrayHeight) colWidth = arrayHeight; //max = a square
			
			for(int currCol = 0; currCol < arrayLen; currCol++) {
				g.setColor(Color.WHITE);
				g.fillRect(currCol * colWidth, arrayLine1, colWidth, arrayHeight);
				g.setColor(Color.BLACK);
				g.drawRect(currCol * colWidth, arrayLine1, colWidth, arrayHeight);
				
				g.setFont(new Font("SansSerif", Font.BOLD, 12));
				String dataString = ((arr[currCol] == null) ? "n" : arr[currCol].toString());
				int dataStringWidth = g.getFontMetrics().stringWidth(dataString);
				g.drawString(dataString, currCol * colWidth + colWidth/2 - dataStringWidth/2, arrayLine1 + arrayHeight/2 + 6);
				
				g.setFont(new Font("SansSerif", Font.BOLD, 8));
				String indexString = ""+currCol;
				int indexStringWidth = g.getFontMetrics().stringWidth(indexString);
				g.drawString(indexString, currCol * colWidth + colWidth/2 - indexStringWidth/2, arrayLine1 + arrayHeight + 8);
			}
		}
		
		panel.add(new ImagePanel(image), (top ? BorderLayout.NORTH : BorderLayout.SOUTH));
		g.setStroke(oldStroke);
		
	}
	
	/**
	 *  Binary tree layout for JUNG. This is not completed.
	 */
	static class BinaryTreeLayout extends TreeLayout<LinkedTree.TreeNode, DisplayEdge> {
		/**
		 *  Constructs the layout for a given tree.
		 *  
		 *  @param g the tree
		 */
		public BinaryTreeLayout(Forest<LinkedTree.TreeNode, DisplayEdge> g) {
			super(g);
		}
		
		/**
		 *  Builds the tree from the layout.
		 */
		protected void buildTree() {
			this.m_currentPoint = new Point(0, 20);
			Collection<LinkedTree.TreeNode> roots = TreeUtils.getRoots(graph);
			if (roots.size() > 0 && graph != null) {
				calculateDimensionX(roots);
				for(LinkedTree.TreeNode v : roots) {
					calculateDimensionX(v);
					m_currentPoint.x += this.getSize().getWidth()/4;
					buildTree(v, this.m_currentPoint.x);
				}
			}
			int width = 0;
			for(LinkedTree.TreeNode v : roots) {
				width += basePositions.get(v);
			}
		}
    
		/**
		 *  Copied from parent.
		 *  
		 *  @param v the vertex
		 *  @return the size
		 */
		private int calculateDimensionX(LinkedTree.TreeNode v) {

			int size = 0;
			int childrenNum = graph.getSuccessors(v).size();

			if (childrenNum != 0) {
				for (LinkedTree.TreeNode element : graph.getSuccessors(v)) {
					size += calculateDimensionX(element) + distX;
				}
			}
			size = Math.max(0, size - distX);
			basePositions.put(v, size);

			return size;
		}

		/**
		 *  Copied from parent.
		 *  
		 *  @param roots the roots
		 *  @return the size
		 */
		private int calculateDimensionX(Collection<LinkedTree.TreeNode> roots) {

			int size = 0;
			for(LinkedTree.TreeNode v : roots) {
				int childrenNum = graph.getSuccessors(v).size();

				if (childrenNum != 0) {
					for (LinkedTree.TreeNode element : graph.getSuccessors(v)) {
						size += calculateDimensionX(element) + distX;
					}
				}
				size = Math.max(0, size - distX);
				basePositions.put(v, size);
			}

			return size;
		}
		
		
		/**
		 *  Builds the tree in the layout.
		 *  
		 *  @param v the vertex
		 *  @param x the x dimension
		 */
		protected void buildTree(LinkedTree.TreeNode v, int x) {
			if (!alreadyDone.contains(v)) {
				alreadyDone.add(v);

				//go one level further down
				this.m_currentPoint.y += this.distY;
				this.m_currentPoint.x = x;

				this.setCurrentPositionFor(v);

				int sizeXofCurrent = basePositions.get(v);

				int lastX = x - sizeXofCurrent / 2;

				int sizeXofChild;
				int startXofChild;

				if(v.getLeft() != null) {
					sizeXofChild = this.basePositions.get(v.getLeft());
					startXofChild = lastX - (distX/2) + (sizeXofChild/2);
					buildTree(v.getLeft(), startXofChild);
					lastX += distX;
				}
				
				if(v.getRight() != null) {
					sizeXofChild = this.basePositions.get(v.getRight());
					startXofChild = lastX + (distX/2) + (sizeXofChild/2);
					buildTree(v.getRight(), startXofChild);
				}
				
				this.m_currentPoint.y -= this.distY;
			}
		}
	}
	
	/**
	 *  Makes one linked tree panel.
	 *  
	 *  @param panel which panel to put it on
	 *  @param tree the tree to add
	 *  @param root the root of the tree
	 */
	public void addOneLinkedTree(JPanel panel, OrderedDelegateForest tree, LinkedTree.TreeNode root) {
		int width = (frame.getWidth()/3)-6;
		int height = frame.getHeight()/6*4;
		
		//make new ones
		TreeLayout<LinkedTree.TreeNode, DisplayEdge> treeLayout = new BinaryTreeLayout(tree);
		VisualizationViewer<LinkedTree.TreeNode, DisplayEdge> visServer = new VisualizationViewer<>(treeLayout);
		visServer.setPreferredSize(new Dimension(width,height));
		visServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		RenderContext<LinkedTree.TreeNode, DisplayEdge> context = visServer.getRenderContext();
		
		//label edges with toString()
		context.setEdgeLabelTransformer(
			new Transformer<DisplayEdge,String>(){
				public String transform(DisplayEdge e) {
					return e.toString();
				}
			}
		);
		
		//color arrows with edge color
		context.setArrowFillPaintTransformer(
			new Transformer<DisplayEdge,Paint>(){
				public Paint transform(DisplayEdge e) {
					return e.getColor();
				}
			}
		);
		
		//color arrow outlines with edge color
		context.setArrowDrawPaintTransformer(
			new Transformer<DisplayEdge,Paint>(){
				public Paint transform(DisplayEdge e) {
					return e.getColor();
				}
			}
		);
		
		//color lines with edge color
		context.setEdgeDrawPaintTransformer(
			new Transformer<DisplayEdge,Paint>(){
				public Paint transform(DisplayEdge e) {
					return e.getColor();
				}
			}
		);
		
		//set edge line stroke to bolder
		context.setEdgeStrokeTransformer(
			new Transformer<DisplayEdge,Stroke>(){
				public Stroke transform(DisplayEdge e) {
					return new BasicStroke(3);
				}
			}
		);
		
		//make nodes
		context.setVertexShapeTransformer(
			new Transformer<LinkedTree.TreeNode,Shape>(){
				public Shape transform(LinkedTree.TreeNode v) {
					int s = 40;
					return new Ellipse2D.Double(-s/2.0, -s/4.0, s, s/2);
				}
			}
		);
		
		//make lines straight
		context.setEdgeShapeTransformer(new EdgeShape.Line<>());
		
		//put text in middle
		context.setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<>(0.5, 0.5));
		
		//move edge labels off the lines
		context.setLabelOffset(5);
		
		//label vertices with toString()
		context.setVertexLabelTransformer(
			new Transformer<LinkedTree.TreeNode,String>(){
				public String transform(LinkedTree.TreeNode v) {
					return v.toString();
				}
			}
		);
		
		//color vertices with node color
		context.setVertexFillPaintTransformer(
			new Transformer<LinkedTree.TreeNode,Paint>(){
				public Paint transform(LinkedTree.TreeNode v) {
					return v.getColor();
				}
			}
		);
		
		//make nodes bigger
		context.setVertexFontTransformer(
			new Transformer<LinkedTree.TreeNode,Font>(){
				public Font transform(LinkedTree.TreeNode v) {
					return new Font("Serif",Font.PLAIN,14);
				}
			}
		);
		
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.PICKING);
		visServer.setGraphMouse(gm);
		
		panel.add(visServer, BorderLayout.CENTER);
	}
	
	/**
	 *  Makes the panel containing the step, reset, and play buttons.
	 */
	public void makeBottomButtons() {
		if(buttonPanel != null) frame.remove(buttonPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));
		
		//file choosers buttons
		JButton tree1chooser = new JButton("Tree 1 File");
		tree1chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory((file1 == null) ? new File(".") : file1);
				fileChooser.setDialogTitle("Choose Tree 1 File" );
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
				int ret = fileChooser.showOpenDialog(frame);
				if(ret == JFileChooser.APPROVE_OPTION) {
					file1 = fileChooser.getSelectedFile();
					resetDisplay();
				}
			}
		});
		buttonPanel.add(tree1chooser);
		
		JButton tree2chooser = new JButton("Tree 2 File");
		tree2chooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory((file2 == null) ? new File(".") : file1);
				fileChooser.setDialogTitle("Choose Tree 2 File" );
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
				int ret = fileChooser.showOpenDialog(frame);
				if(ret == JFileChooser.APPROVE_OPTION) {
					file2 = fileChooser.getSelectedFile();
					resetDisplay();
				}
			}
		});
		buttonPanel.add(tree2chooser);
		
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.revalidate();
	}
}
