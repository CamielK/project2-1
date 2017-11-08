package Uct;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class SuperTree {
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
		
		root.addChild(child1);
		root.addChild(child2);
		root.addChild(child3);
		
		child1.addChild(child4);
		child1.addChild(child5);
		child1.addChild(child6);
		
		child2.addChild(child7);
		child2.addChild(child8);
		child2.addChild(child9);

		child3.addChild(child10);
		child3.addChild(child11);
		child3.addChild(child12);

		//saveSuperTree(root);
		SuperTree temp = loadSuperTree();
		preOrder(temp.getRoot());
		System.out.println(tree);
		saveSuperTree(temp.getRoot());

	}
		private static Node root;
		private int amountOfNodes;
		private static String preOrder = "", tree = "";
		private static String path = "nothanks/src/Uct/SuperTree.txt";
		private static File f = new File(path);
		private static Formatter x = null;
		private static Scanner s;
		
		public SuperTree(Node root){
			preOrder = " ";
			this.root = root;
			amountOfNodes = 1;
		}
		/**
		 * Load the Tree from a txt File.
		 * @return
		 */
		public static SuperTree loadSuperTree(){
			SuperTree tempSuperTree = null;
			String loadTree = "";
			String[] regex;
			initScanner();
			if(s.hasNext())loadTree += s.nextLine();
				System.out.println(loadTree);
			regex = loadTree.split("\\s");
			//regex = loadTree.split("(?=>)|(?=<)");
				for(int i = 0; i<regex.length;i++) System.out.println(regex[i]);
			tempSuperTree = new SuperTree(new Node(0));
			Node parentNode = tempSuperTree.getRoot();
			for(int i = 1; i < regex.length; i++){
				if(regex[i].equals(">")){
					Node currentNode = new Node(Integer.parseInt(regex[i+1]));
					tempSuperTree.addNode(parentNode, currentNode);
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
					tempSuperTree.addNode(parentNode, currentNode);
					parentNode = currentNode;
				}
			}
			return tempSuperTree;
		}
		
	/*for(int i = 1; i<regex.length; i++){
				if(regex[i].contains("> ")){
					String[] currentValue = regex[i].split("(> )| ( )");
					for(int j = 0; j<currentValue.length; j++){
						if(currentValue[j].matches(".*\\d\\s\\d.*")){
							String[] siblings = currentValue[j].split("\\s");
							for(int k = 0; k<currentValue.length; k++){
								Node currentNode = new Node(Integer.parseInt(siblings[k]));
								tempSuperTree.addNode(parentNode, currentNode);
								parentNode = currentNode;
							}
						}	
						else{
							Node currentNode = new Node(Integer.parseInt(currentValue[1]));
							tempSuperTree.addNode(parentNode, currentNode);
							parentNode = currentNode;
						}
					}
				}
				if(regex[i].contains("< ")){
					String[] currentValue = regex[i].split("< ");
						
					for(int j = 1; j<currentValue.length; j++){
						Node currentNode = new Node(Integer.parseInt(currentValue[j]));
						parentNode = tempSuperTree.getParent(parentNode);
						tempSuperTree.addNode(parentNode, currentNode);
						parentNode = currentNode;	
					}
				}					
			}
			//tempSuperTree = new SuperTree(new Node(Integer.parseInt(regex[0])));
			saveSuperTree(tempSuperTree.getRoot());		
			return tempSuperTree;
		}
	
		/**
		 * Add a child node to the supertree
		 * 
		 * @param parent parent of the added node
		 * @param child	currently added node
		 */
		public void addNode(Node parent, Node child){
			parent.addChild(child);
		}
		
		/**
		 * 
		 * @param child
		 * @return
		 */
		public Node getParent(Node child){
			return child.getParent();
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
		public static void saveSuperTree(Node root){
			tree = "";
			preOrder(root);
			initFormatter();
			x.format(tree);
			x.close();
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
		
		/**
		 * 
		 * @param v
		 */
		public static void preOrder(Node v){
			tree += (v.getCardValue() + " ");
			if(v.getChildren().length != 0){
				tree += ("> ");
			}
			/*if(v.getChildren().length == 0 && v.getParent().getChildren().length > 1){
				tree += ", ";
			}*/
			for(int i = 0; i < v.getChildren().length; i++){
				if(v.getChildren().length != 0){
					preOrder(v.getChildren()[i]);
				}
				if(v.getChildren().length == i+1){
					tree += ("< ");
				}
			}
		}		
}
