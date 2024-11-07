import java.io.IOException;
import java.util.Arrays;

/**
 *  Display driver.
 *  
 *  @author Dr. Raven Russell
 */
class Display {
	/**
	 *  Main method for driver class.
	 *  
	 *  @param args no args runs GUI, one runs single tree analytics, two runs merge
	 */
	public static void main(String[] args) {
		String usage = "java Display [filename1] [filename2]";
		try {
			//no files, interactive mode
			if(args.length == 0) {
				new SimGUI();
			}
			//one file, display trees in text format
			else if(args.length == 1) {
				Convert.ParentPointer[] tree1 = Convert.parentPointerFormat(args[0]);
				
				System.out.println("Parent Pointer Tree:");
				System.out.println(Arrays.toString(tree1));
				System.out.printf("Load: ~%.2f%%\n", Convert.arrayLoad(tree1)*100);
				
				System.out.println("Linked Tree:");
				System.out.println(Convert.treeLinkedFormat(args[0]));
				
				System.out.println("Array Tree:");
				Integer[] tree3 = Convert.treeArrayFormat(args[0]);
				System.out.println(Arrays.toString(tree3));
				System.out.printf("Load: ~%.2f%%\n", Convert.arrayLoad(tree3)*100);
			}
			//two files, merge trees and display in text
			else if(args.length == 2) {
				try {
					LinkedTree<Integer> tree3 = Convert.merge(Convert.treeLinkedFormat(args[0]), Convert.treeLinkedFormat(args[1]));
					System.out.println("Option 1:");
					
					if(tree3 == null) {
						System.out.println("Incompatible Trees");
					}
					else {
						System.out.println("Merged Tree:");
						System.out.println(tree3);
						
						boolean dup = Convert.containsDuplicates(tree3);
						System.out.println("Merge contains duplicates? " + dup);
						
						if(!dup) {
							System.out.println("Parent Pointer Format:");
							System.out.println(Arrays.toString(Convert.toParentPointer(tree3)));
						}
					}
				}
				catch(UnsupportedOperationException e) {
					System.out.println("Option 1 not chosen");
				}
				
				try {
					Integer[] tree3 = Convert.merge(Convert.treeArrayFormat(args[0]), Convert.treeArrayFormat(args[1]));
					System.out.println("\nOption 2:");
					
					if(tree3 == null) {
						System.out.println("Incompatible Trees");
					}
					else {
						System.out.println("Merged Tree:");
						System.out.println(Arrays.toString(tree3));
						
						boolean dup = Convert.containsDuplicates(tree3);
						System.out.println("Merge contains duplicates? " + dup);
						
						if(!dup) {
							System.out.println("Parent Pointer Format:");
							System.out.println(Arrays.toString(Convert.toParentPointer(tree3)));
						}
					}
				}
				catch(UnsupportedOperationException e) {
					System.out.println("\nOption 2 not chosen");
				}
				
			}
			else {
				System.out.println(usage);
			}
		}
		catch(IOException e) {
			System.out.println("Invalid file given: "+e.toString()+"\n" + usage);
		}
	}
}
