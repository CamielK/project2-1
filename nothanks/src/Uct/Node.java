package Uct;

public class Node {

	private int cardValue, timesVisited, gamesWon;
	private static boolean visited;
	private Node[] children;
	private Node parent;
	
	//Nodes for Subtree
	public Node(){
		if(children == null)children = new Node[0];
		
	}
	
	//Nodes for Supertree
	public Node(int cardValue){
		if(children == null)children = new Node[0];
		this.cardValue = cardValue;
	}
	
	public int getCardValue(){
		return cardValue;
	}
	
	public void addChild(Node child){
		Node[] newChildren = new Node[children.length+1];
		for(int i = 0; i<children.length; i++) newChildren[i]=children[i];
		newChildren[children.length] = child;
		children = newChildren;
		child.setParent(this);
	}
	
	public Node[] getChildren(){
		return children;
	}
	
	public void setParent(Node parent){
		this.parent = parent;
	}
	
	public Node getParent(){
		return parent;
	}
	
}
