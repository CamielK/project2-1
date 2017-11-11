package Uct;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int cardValue, timesPlayed, gamesWon;
	private double winrate;
	private boolean visited;
	private ArrayList<Node> children = null;
	private Node parent;
	//True -> take card; False -> toss chip;
	private boolean takeCard;
	
	public Node(){
		children = new ArrayList<Node>();
		winrate = 0;
	}
	
	public void setCardValue(int cardValue){
		
	}
	
	
	public void addGame(boolean win){
		if(win){
			gamesWon++;
		}
		timesPlayed++;
	}
	
	public boolean getVisited(){
		return visited;
	}
	
	public void visit(){
		visited = true;
	}
	
	public boolean takeCard(){
		return takeCard;
	}
	
	public double getWinrate(){
		return winrate;
	}
	
	public int getCardValue(){
		return cardValue;
	}
	
	public void addChild(Node child){
		children.add(child);
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
		
	public void setParent(Node parent){
		this.parent = parent;
	}
	
	public Node getParent(){
		return parent;
	}
	
}
