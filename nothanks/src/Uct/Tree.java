package Uct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Tree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Node root;
	private String tree = "";
	private String path = "nothanks/src/Uct/Tree.ser";
	private File f = new File(path);
	
	public Tree(){
	}
	
	public Tree(Node root){
		this.root = root;
	}
	
	public Node addNode(Node parent, int cardValue){
		Node newNode = new Node();
		parent.addChild(newNode);
		newNode.setCardValue(cardValue);
		for(int i = 0; i < parent.getChildren().size(); i++){
			if(parent.getChildren().get(i).getCardValue() == newNode.getCardValue()){
				newNode.setTakeCard(!parent.getChildren().get(i).takeCard());
			}
			else{
				newNode.setTakeCard(0.5<Math.random());
			}
		}
		
		return newNode;
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
		executePreOrder(v);
		return tree;
	}
	
	/**
	 * 
	 * @param v
	 */
	public void executePreOrder(Node v){
		System.out.println(v.toString());
		tree += (v.getCardValue() + " ");
		if(v.getChildren().size() != 0){
			tree += ("> ");
		}
		for(int i = 0; i < v.getChildren().size(); i++){
			if(v.getChildren().size() != 0){
				executePreOrder(v.getChildren().get(i));
			}
			if(v.getChildren().size() == i+1){
				tree += ("< ");
			}
		}
	}
	
	public void save(){
		try {
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
		Tree tempTree = null;
		try {
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
		return tempTree;
	}
}
