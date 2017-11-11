package Uct;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class SubTree {
		/*
	 	* Add method get Leafs
	 	*/

		private UnifierNode lastLeaf;
		private Node root;
		private String tree = "";
		private String path = "nothanks/src/Uct/SubTree";
		private File f;
		private Formatter x;
		private Scanner s;
		private static int id = 0;
		
		public SubTree(Node root){
			this.root = root;
			this.lastLeaf = new UnifierNode();
			
			path += (id + ".txt");
			f = new File(path);
			id++;
			System.out.println(path);
		}
		
		public void addLeftChild(Node parent, Node child){
			parent.addChild(child);
		}
		
		public void addRightChild(Node parent, Node child){
			parent.addChild(child);
		}
		
		public void assUnifierNode(UnifierNode lastLeaf){
			this.lastLeaf = lastLeaf;
		}
		
		public UnifierNode getUnifierNode(){
			return lastLeaf;
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
		public void saveSubTree(){
			tree = "";
			initFormatter();
			preOrder(this.root);
			x.format(tree);
			x.close();
		}
		
		/**
		 * Load the Tree from a txt File.
		 * @return
		 */
		public SubTree loadSubTree(){
			SubTree tempSubTree = null;
			String loadTree = "";
			String[] regex;
			initScanner();
			System.out.println(f.getAbsolutePath());
			if(s.hasNext())loadTree += s.nextLine();
			System.out.println("Load: " + loadTree);
			regex = loadTree.split("\\s");
			System.out.println("Regex: ");
			for(int i = 0; i < regex.length;i++)System.out.println("R:"+regex[i]);
			if(!regex[0].isEmpty()){
				tempSubTree = new SubTree(new Node(Integer.parseInt(regex[0])));
				Node parentNode = tempSubTree.getRoot();
				for(int i = 1; i < regex.length; i++){
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
		}
		
		/**
		 * 
		 */
		public void initFormatter(){
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
		public void initScanner(){
			try {
				s = new Scanner(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public void preOrder(Node v){
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
