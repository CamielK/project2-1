package Uct;

import Helper.Config;

import java.io.*;

public class Tree implements Serializable{
	/*
	 * Optimize Tree as drawn on my block
	 */
	private static final long serialVersionUID = 1L;
	private Node root;
	private String tree = "";
	private transient boolean visualize = true;
	private transient String path; // = Config.logpath + "/src/Uct/Tree_c.ser";
	private File f = null;
	private int numberOfNodes = 1, nrPlayers = 0;
	private transient int layer = 0;
	/**
	 * Construct a Tree
	 * @param nrPlayers the number of players, participating in this game.
	 */
	public Tree(int nrPlayers){
		layer = 0;
		this.nrPlayers = nrPlayers;
		path = Config.logpath + "/src/Uct/Logs/Tree" + nrPlayers + ".ser";
		this.f = new File(path);
		this.root = new Node();
		root.setCardValue(-1);
	}		
	/**
	 * Add a Node to the tree
	 * @param parent parent Node
	 * @param cardValue Value of the current Card
	 * @param takeCard Wheter to take the card or not
	 * @return returns the newly added node
	 */
	public Node addNode(Node parent, int cardValue, boolean takeCard){
		Node newNode = new Node();
		parent.addChild(newNode);
		newNode.setCardValue(cardValue);
		newNode.setTakeCard(takeCard);
		numberOfNodes++;
		return newNode;
	}
	
	/**
	 * Returns the root of the tree
	 * @return root of the tree
	 **/
	public Node getRoot(){
		return root;
	}
	
	/**
	 * Starts preorder traversal
	 * @param v startnode (usually root)
	 * @return the preordertraversal string
	 */
	public String preOrder(Node v){
		tree = "";
		executePreOrder(v);
		System.out.println("Number of Nodes: " + numberOfNodes);
		return tree;
	}
	
	/**
	 * Executes Preorder on the Tree and makes a String out of the tree 
	 * @param v node from where to start the preorder
	 **/
	private void executePreOrder(Node v){
		//System.out.println(v.toString());
		if(!visualize) {
			for(int i = 0; i < layer; i++) System.out.print("  ");
			System.out.println(v.getCardValue() + " "); 
		}
		else System.out.print(v.getCardValue() + " ");
		tree += (v.getCardValue() + "");
		if(v.takeCard())tree += "t ";
		else tree += "f ";
		if(v.getChildren().size() != 0){
			tree += ("> ");
			System.out.print("> ");
			layer ++;
		}
		for(int i = 0; i < v.getChildren().size(); i++){
			if(v.getChildren().size() != 0){
				executePreOrder(v.getChildren().get(i));
			}
			if(v.getChildren().size() == i+1){
				tree += ("< ");
				System.out.print("< ");
				layer --;
			}
		}
	}
	
	/**
	 * Save the tree as a Treei.ser file, where i is the amount of players
	 */
	public void save(){
		initFile();
		try {
			System.out.println(f.getAbsolutePath());
			 f.createNewFile();
	         FileOutputStream fileOut =
	         new FileOutputStream(f);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	         //System.out.println("Serialized data is saved in " + path);
	         System.out.printf("Serialized data is saved in " + path);
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
	
	/**
	 * Loads the tree structure
	 * @return returns the tree;
	 */
	public Tree load(){
		Tree tempTree = new Tree(nrPlayers);
		initFile();
		if(!f.exists()) {
			 tempTree.save();
		}
		try {
			 f.createNewFile();
			 FileInputStream fileIn = new FileInputStream(f);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         tempTree = (Tree) in.readObject();
	         in.close();
	         fileIn.close();
	    } 
		catch (IOException i) {
			i.printStackTrace();
	    }
		catch (ClassNotFoundException c) {
	         System.out.println("Tree class not found");
	         c.printStackTrace();
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
		return tempTree;
	}
	
	/**
	 * Resets all trees and all Gameplaylogs
	 */
	//Doesnt overwrite old savefiles
	public void resetAllTrees(){
		for(int i = 2; i<7;i++){
			Tree temp = new Tree(i);
			temp.save();
		}
		String logpath = Config.logpath + "/src/Uct/Logs/Gamelog";
		File log;
		for(int i = 2; i < 7; i++){
			log = new File(logpath + i + ".txt");
			try {
				log.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			log.delete();
			try {
				log.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * initializes the File, where the trees are stored. The saved path is dependend on the amount of players
	 */
	private void initFile() {
		if(nrPlayers > 6 || nrPlayers <2) {
			if(nrPlayers > 6) nrPlayers = 6;
			if(nrPlayers < 2) nrPlayers = 2;
			
			try {
				throw new Exception("Number of players has to be between 2 and 6");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
		path = Config.logpath + "/src/Uct/Logs/Tree" + nrPlayers + ".ser";
		f = new File(path);
		}
	}
}
