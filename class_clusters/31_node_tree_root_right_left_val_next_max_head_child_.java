// -----------  Problem Synopsis  ----------- //
// Given a binary tree, return the inorder traversal of its nodes' values.
// ------------------------------------------ //

// This class is already defined in TreeNode.java
// class TreeNode {
//     int val;
//     TreeNode left;
//     TreeNode right;
//     TreeNode(int x) { val = x; }
// }
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class BinaryTreeInorderTraversal {
    // iterative traversal
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<Integer>();
        Stack<TreeNode> stack = new Stack<TreeNode>();

        TreeNode curr = root;
        while (null != curr || !stack.isEmpty()) {
            while (null != curr) {
                stack.push(curr);
                curr = curr.left;
            }
            curr = stack.pop();
            result.add(Integer.valueOf(curr.val));
            curr = curr.right;
        }
        return result;
    }
    
    public static void Run() {
        BinaryTreeInorderTraversal s = new BinaryTreeInorderTraversal();

        TreeNode node = new TreeNode(2);
        node.left = new TreeNode(1);
        node.right = new TreeNode(3);

        List<Integer> result = s.inorderTraversal(node);
        for (Integer var : result) {
            System.out.println(var.intValue());            
        }

        node = new TreeNode(5);
        node.left = new TreeNode(1);
        node.right = new TreeNode(4);
        node.right.left = new TreeNode(3);
        node.right.right = new TreeNode(6);

        result = s.inorderTraversal(node);
        for (Integer var : result) {
            System.out.println(var.intValue());            
        }
    }
}
--------------------

package test.tree;

public class TreeElement {
	
	protected TreeElement parentNode;
	protected TreeElement leftChild;
	protected TreeElement rightChild;
    
	protected Integer value;
	
	public TreeElement(Integer value) {
		parentNode = null;
		leftChild = null;
		rightChild = null;
		this.value = value;
	}
	
	public TreeElement(TreeElement thisParentNode, Integer value) {
		parentNode = thisParentNode;
		leftChild = null;
		rightChild = null;		
		this.value = value;
	}
	
	public String toString(){
		return "" + value;
	}
	
	public Integer getVaule() {
		return this.value;		
	}
	
	public void setVaule(Integer nodeValue) {
		 this.value = nodeValue;		
	}
	
	public void setParent(TreeElement parent) {		
		this.parentNode = parent;
	}
	
	public void setLeftChild(TreeElement lChild) {		
		this.leftChild = lChild;
	}
	
	public void setRightChild(TreeElement rChild) {		
		this.rightChild = rChild;
	}
	
	public TreeElement getParent() {		
		return this.parentNode;
	}
	
	public TreeElement getLeftChild() {		
		return this.leftChild ;
	}
	
	
	public TreeElement getRightChild() {		
		return this.rightChild ;
	}
	
	public boolean isRoot() {
		if (this.parentNode == null ){
			return true;			
		} else {
			return false;
		}
	}
	

}

--------------------

package partC;
/**
 * To get a better view of the tree, I have added a TreePrinter class, 
 * courtesy of Michal Kreuzman from Stackoverflow, original code example
 * can be found at the following link 
 * http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram 
 * To adapt it to our Binary Search Tree to this TreePrinter has suffered minor changes
 */
import java.util.*;

class TreePrinter {

	public static <T extends Comparable<?>> void printNode(BinarySearchTree<T> tree) {
		int maxLevel = tree.level(tree.root);
		System.out.println("");
		printNodeInternal(Collections.singletonList(tree.root), 1, maxLevel);
	}

	private static <T extends Comparable<?>> void printNodeInternal(
			List<BinarySearchTree.Entry<T>> nodes, int level, int maxLevel) {
		if (nodes.isEmpty() || TreePrinter.isAllElementsNull(nodes))
			return;

		int floor = maxLevel - level;
		int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
		int firstSpaces = (int) Math.pow(2, (floor)) - 1;
		int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

		// values spacing
		TreePrinter.printWhitespaces(firstSpaces);

		List<BinarySearchTree.Entry<T>> newNodes = new ArrayList<BinarySearchTree.Entry<T>>();
		for (BinarySearchTree.Entry<T> node : nodes) {
			if (node != null) {
				if (node.element == null)
					System.out.print("-");
				else
					System.out.print(node.element);
				newNodes.add(node.left);
				newNodes.add(node.right);
			} else {
				newNodes.add(null);
				newNodes.add(null);
				System.out.print(" ");
			}

			// seems like right side values separator
			TreePrinter.printWhitespaces(betweenSpaces);
		}
		System.out.println("");

		for (int i = 1; i <= endgeLines; i++) {
			for (int j = 0; j < nodes.size(); j++) {
				// spacing for the /\ (basically like the tab)
				TreePrinter.printWhitespaces(firstSpaces - i +1 );
				if (nodes.get(j) == null) {
					TreePrinter.printWhitespaces(endgeLines + endgeLines + i);
					continue;
				}

				if (nodes.get(j).left != null)
					System.out.print("/");
				else
					TreePrinter.printWhitespaces(1);

				// distance between /\
				TreePrinter.printWhitespaces(i + i - 2);

				if (nodes.get(j).right != null)
					System.out.print("\\");
				else
					TreePrinter.printWhitespaces(1);

				TreePrinter.printWhitespaces(endgeLines + endgeLines - i);
			}

			System.out.println(" ");
		}

		printNodeInternal(newNodes, level + 1, maxLevel);
	}

	private static void printWhitespaces(int count) {
		for (int i = 0; i < count; i++)
			System.out.print(" ");
	}

	private static <T extends Comparable<?>> int maxLevel(BinarySearchTree.Entry<T> node) {
		if (node == null)
			return 0;

		return Math.max(TreePrinter.maxLevel(node.left),
				TreePrinter.maxLevel(node.right)) + 1;
	}

	private static <T> boolean isAllElementsNull(List<T> list) {
		for (Object object : list) {
			if (object != null)
				return false;
		}

		return true;
	}

}
--------------------

