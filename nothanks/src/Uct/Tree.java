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
		
		return newNode;
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
	
	/**
	 * 
	 * @param v
	 */
	public void preOrder(Node v){
		tree += (v.getCardValue() + " ");
		if(v.getChildren().size() != 0){
			tree += ("> ");
		}
		for(int i = 0; i < v.getChildren().size(); i++){
			if(v.getChildren().size() != 0){
				preOrder(v.getChildren().get(i));
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
