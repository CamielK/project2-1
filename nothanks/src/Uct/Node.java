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
	private transient boolean visited;
	private ArrayList<Node> children = null;
	private Node parent;
	//True -> take card; False -> toss chip;
	private boolean takeCard;
	
	public Node(){
		children = new ArrayList<Node>();
		winrate = 0;
		timesPlayed = 0;
		gamesWon = 0;
		visited = false;
	}
	
	public void setCardValue(int cardValue){
		this.cardValue = cardValue;
	}
	
	
	public void addGame(boolean win){
		if(win){
			gamesWon++;
		}
		timesPlayed++;
		winrate = (double)gamesWon/timesPlayed; 
	}
	
	public boolean getVisited(){
		return visited;
	}
	
	public void visit(boolean visited){
		this.visited = visited;
	}
	
	public void setTakeCard(boolean takeCard){
		this.takeCard = takeCard;
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
		child.setParent(this);
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
		
	private void setParent(Node parent){
		this.parent = parent;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public String toString(){
		String toString = "CardValue " + cardValue + ", Times played: " + timesPlayed + ", Games won: " + gamesWon + ", win rate: " + winrate + ", takeCard: " + takeCard;  
		return toString;
	}
}
