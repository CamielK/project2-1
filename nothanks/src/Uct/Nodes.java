package Uct;

import java.io.Serializable;


//Nodes Backup
public class Nodes implements Serializable{
	private static int id = 0;
	private int cardValue, timesVisited, gamesWon;
	private boolean visited;
	private Nodes[] children = null;
	private Nodes parent, leftChild, rightChild;
	
	//Nodess for Subtree
	public Nodes(){
		leftChild = null;
		rightChild = null;
		id++;
	}
	
	//Nodess for Supertree
	public Nodes(int cardValue){
		if(children == null)children = new Nodes[0];
		this.cardValue = cardValue;
		id++;
	}
	
	public int getCardValue(){
		return cardValue;
	}
	
	public void addLeftChild(Nodes leftChild){
		this.leftChild = leftChild;
		addChild(leftChild);
		leftChild.setParent(this);
	}
	
	public void addRightChild(Nodes rightChild){
		this.rightChild = rightChild;
		addChild(rightChild);
		rightChild.setParent(this);
	}
	
	public void addChild(Nodes child){
		Nodes[] newChildren = new Nodes[children.length+1];
		for(int i = 0; i<children.length; i++) newChildren[i]=children[i];
		newChildren[children.length] = child;
		children = newChildren;
		child.setParent(this);
	}
	
	public int getID(){
		return id;
	}
	
	public Nodes[] getChildren(){
		return children;
	}
	
	public Nodes getLeftChild(){
		return this.leftChild;
	}
	
	public Nodes getRightChild(){
		return this.rightChild;
	}
	
	public void setParent(Nodes parent){
		this.parent = parent;
	}
	
	public Nodes getParent(){
		return parent;
	}
	
}
