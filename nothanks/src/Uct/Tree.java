package Uct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Tree implements Serializable{
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node root;
	private String tree = "";
	private transient String path = System.getProperty("user.dir") + "\\src\\Uct\\Tree.ser";
	private transient File f = null;
	private int totalGames = 0, totalWins = 0;
	private int numberOfNodes = 1;
	
	public Tree(){
		this.root = new Node();
		root.setCardValue(-1);
		try {
			this.f = new File(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public Node addNode(Node parent, int cardValue, boolean takeCard){
		Node newNode = new Node();
		parent.addChild(newNode);
		newNode.setCardValue(cardValue);
		newNode.setTakeCard(takeCard);
		numberOfNodes++;
		return newNode;

		/*		for(int i = 0; i < parent.getChildren().size() && !secondNode; i++){
			if(parent.getChildren().get(i).getCardValue() == cardValue){
				newNode.setTakeCard(!parent.getChildren().get(i).takeCard());
				secondNode = true;
			}
		}
		if(!secondNode){
			newNode.setTakeCard(0.5<Math.random());
		}*/
	}
	
	/**
	 * 
	 * @return
	 */
	public Node getRoot(){
		return root;
	}
	
	public String preOrder(Node v){
		tree = "";
		System.out.println("Number of Nodes: " + numberOfNodes);
		executePreOrder(v);
		return tree;
	}
	
	/**
	 * 
	 * @param v
	 */
	public void executePreOrder(Node v){
		//System.out.println(v.toString());
		System.out.print(v.getCardValue() + " ");
		tree += (v.getCardValue() + "");
		if(v.takeCard())tree += "t ";
		else tree += "f ";
		if(v.getChildren().size() != 0){
			tree += ("> ");
			System.out.print("> ");
		}
		for(int i = 0; i < v.getChildren().size(); i++){
			if(v.getChildren().size() != 0){
				executePreOrder(v.getChildren().get(i));
			}
			if(v.getChildren().size() == i+1){
				tree += ("< ");
				System.out.print("< ");
			}
		}
	}
	
	public void save(){
		try {
			 f.createNewFile();
	         FileOutputStream fileOut =
	         new FileOutputStream(f);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(this);
	         out.close();
	         fileOut.close();
	         System.out.printf("Serialized data is saved in " + path);
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
	
	
	public Tree load(){
		Tree tempTree = new Tree();
		try {
			 tempTree.save();
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
}
