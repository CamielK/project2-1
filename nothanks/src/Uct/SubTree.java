package Uct;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class SubTree {
	public static void main(String[] args){		
		
		Node root = new Node(0);
		Node child1 = new Node(1);
		Node child2 = new Node(2);
		Node child3 = new Node(3);
		Node child4 = new Node(4);
		Node child5 = new Node(5);
		Node child6 = new Node(6);
		Node child7 = new Node(7);
		Node child8 = new Node(8);
		Node child9 = new Node(9);
		Node child10 = new Node(10);
		Node child11 = new Node(11);
		Node child12 = new Node(12);
		Node child13 = new Node(13);
		Node child14 = new Node(14);
		
		root.addLeftChild(child1);
		root.addRightChild(child2);
		
		child1.addLeftChild(child11);
		child1.addRightChild(child12);
		
		child12.addRightChild(child13);
		
		child2.addLeftChild(child3);
		child2.addRightChild(child4);
		
		child4.addLeftChild(child5);
		child4.addRightChild(child6);
		
		child6.addLeftChild(child7);
		child6.addRightChild(child8);
		
		child8.addLeftChild(child9);
		child8.addRightChild(child10);
		
		saveSubTree(root);
		System.out.println();
		
		SubTree temp = loadSubTree();
		preOrder(temp.getRoot());
		System.out.println(tree);
		saveSubTree(temp.getRoot());
		
		/*Node[] layer1 = temp.getRoot().getChildren();
		Node[] layer2L = layer1[0].getChildren();
		Node[] layer2M = layer1[1].getChildren();
		Node[] layer2R = layer1[2].getChildren();
		System.out.println(temp.getRoot().getCardValue());
		for(int i = 0; i<layer1.length; i++)System.out.println(layer1[i].getCardValue());
		System.out.println();
		for(int i = 0; i<layer2L.length; i++)System.out.println(layer2L[i].getCardValue());
		System.out.println();
		for(int i = 0; i<layer2M.length; i++)System.out.println(layer2M[i].getCardValue());
		System.out.println();
		for(int i = 0; i<layer2R.length; i++)System.out.println(layer2R[i].getCardValue());
*/
	}
		private static Node root;
		private static String preOrder = "", tree = "";
		private static String path = "nothanks/src/Uct/SubTree.txt";
		private static File f = new File(path);
		private static Formatter x = null;
		private static Scanner s;
		
		public SubTree(Node root){
			preOrder = " ";
			this.root = root;
		}
		
		public void addLeftChild(Node parent, Node child){
			parent.addChild(child);
		}
		
		public void addRightChild(Node parent, Node child){
			parent.addChild(child);
		}
		
		
		/**
		 * 
		 * @return
		 */
		public Node getRoot(){
			return root;
		}
		
		//need to make sure String doesnt become too big --> rather add small bits to txt file
		/**
		 * 
		 * @param root
		 */
		public static void saveSubTree(Node root){
			tree = "";
			preOrder(root);
			initFormatter();
			x.format(tree);
			x.close();
		}
		
		/**
		 * Load the Tree from a txt File.
		 * @return
		 */
		public static SubTree loadSubTree(){
			SubTree tempSubTree = null;
			String loadTree = "";
			String[] regex;
			initScanner();
			if(s.hasNext())loadTree += s.nextLine();
			regex = loadTree.split("\\s");
			System.out.println(regex);
			if(!regex[0].isEmpty()){
				tempSubTree = new SubTree(new Node(Integer.parseInt(regex[0])));
				Node parentNode = tempSubTree.getRoot();
				for(int i = 1; i < regex.length; i++){
					System.out.println(regex[i]);
					if(regex[i].equals(">")){
						Node currentNode = new Node(Integer.parseInt(regex[i+2]));
						if(regex[i+1].equals("L")){
							parentNode.addLeftChild(currentNode);
						}
						else if(regex[i+1].equals("R")){
							parentNode.addRightChild(currentNode);
						}
						parentNode = currentNode;
						i=i+2;
					}
					if(regex[i].equals("R")){
						System.out.println("R: " + regex[i] + " " + regex[i+1]);
						parentNode = parentNode.getParent();
						Node currentNode = new Node(Integer.parseInt(regex[i+1]));
						parentNode.addRightChild(currentNode);
						parentNode = currentNode;
						i++;
					}
					if(regex[i].equals("<")){
						parentNode = parentNode.getParent();
					}
						
				}
				return tempSubTree;
			}
			else return null;
			/*tempSubTree = new SubTree(new Node(0));
			Node parentNode = tempSubTree.getRoot();
			for(int i = 1; i < regex.length; i++){
				if(regex[i].equals(">")){
					Node currentNode = new Node(Integer.parseInt(regex[i+1]));
					//tempSubTree.addNode(parentNode, currentNode);
					parentNode = currentNode;
					i++;
				}
				else if(regex[i].equals("<")){
					parentNode = parentNode.getParent();
				}
				else{
					parentNode = parentNode.getParent();
					Node currentNode = new Node(Integer.parseInt(regex[i]));
					System.out.println("CurrentNode: " + currentNode.getCardValue());
					//tempSubTree.addNode(parentNode, currentNode);
					parentNode = currentNode;
				}
			}*/
		}
		
		/**
		 * 
		 */
		public static void initFormatter(){
			try {
				x = new Formatter(f);
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 
		 */
		public static void initScanner(){
			try {
				s = new Scanner(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public static void preOrder(Node v){
			tree += (v.getCardValue() + " ");
			if(v.getLeftChild() == null && v.getRightChild() != null)tree += "> ";
			if(v.getLeftChild() != null){
				tree += "> ";
				tree += "L ";
				preOrder(v.getLeftChild());
			}
			if(v.getRightChild() != null){
				tree += "R ";
				preOrder(v.getRightChild());
				tree += "< ";	
			}
		}
}
