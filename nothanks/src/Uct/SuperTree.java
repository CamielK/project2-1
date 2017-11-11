package Uct;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;

public class SuperTree {
		private Node root;
		private String tree = "";
		private String path = "nothanks/src/Uct/SuperTree.txt";
		private File f = new File(path);
		private Formatter x = null;
		private Scanner s;
		
		public SuperTree(Node root){
			this.root = root;
		}
		
		public SubTree addSubTree(Node addSubHere, SubTree subTree){
			
			for(int i = 0; i<addSubHere.getChildren().length; i++){
				addSubHere.getChildren()[i].setParent(subTree.getUnifierNode());
			}
			subTree.getRoot().setParent(addSubHere);	
			return subTree;
		}
		
		/**
		 * Load the Tree from a txt File.
		 * @return
		 */
		public SuperTree loadSuperTree(){
			SuperTree tempSuperTree = null;
			String loadTree = "";
			String[] regex;
			initScanner();
			if(s.hasNext())loadTree += s.nextLine();
			regex = loadTree.split("\\s");
			if(regex[0].isEmpty())return new SuperTree(new Node(40));
			for(int i = 0; i< regex.length;i++)System.out.print(regex[i] + " ");
			tempSuperTree = new SuperTree(new Node(Integer.parseInt(regex[0])));
			//tempSuperTree = new SuperTree(new Node(0));
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
					tempSuperTree.addNode(parentNode, currentNode);
					parentNode = currentNode;
				}
			}
			return tempSuperTree;
		}
		
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
		public void saveSuperTree(){
			tree = "";
			preOrder(this.root);
			initFormatter();
			x.format(tree);
			x.close();
			System.out.println(tree);
		}
		
		/**
		 * 
		 * @param v
		 */
		public void preOrder(Node v){
			tree += (v.getCardValue() + " ");
			if(v.getChildren().length != 0){
				tree += ("> ");
			}
			for(int i = 0; i < v.getChildren().length; i++){
				if(v.getChildren().length != 0){
					preOrder(v.getChildren()[i]);
				}
				if(v.getChildren().length == i+1){
					tree += ("< ");
				}
			}
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
}
