package Uct;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int cardValue;
	private double winrate, timesPlayed, gamesWon;
	private transient boolean visited;
	private ArrayList<Node> children = null;
	private Node parent;
	//True -> take card; False -> toss chip;
	private boolean takeCard;
	
	/**
	 * Construct a Node that holds the cardValue and the winrate
	 */
	public Node(){
		children = new ArrayList<Node>();
		winrate = 0;
		timesPlayed = 0;
		gamesWon = 0;
		visited = false;
	}
	
	/**
	 * Sets the cardvalue of a Node
	 * @param cardValue cardValue of a Node
	 */
	public void setCardValue(int cardValue){
		this.cardValue = cardValue;
	}
	
	/**
	 * Add a Game, where that node was visited and update the winrate.
	 * @param win indicates, if the played game was a win
	 * @param amount the weight of the game. Determined by the depth of the taken path. Nodes in deeper layers are weightend higher.
	 */
	public void addGame(boolean win, double amount){
		System.out.println(gamesWon + " " + timesPlayed + " " + winrate + " " + this.takeCard);
		if(win){
			gamesWon += amount;
		}
		timesPlayed += amount;
		winrate = gamesWon/timesPlayed; 
	}
	
	/**
	 * Returns if a Node was  visited in a game
	 * @return true if the node was visited
	 */
	public boolean getVisited(){
		return visited;
	}
	
	/**
	 * Visit a node
	 * @param visited true, if the node was visited
	 */
	public void visit(boolean visited){
		this.visited = visited;
	}
	
	/**
	 * Determines, if the card will be taken or a chip will be tossed
	 * @param takeCard true if the card is taken, false if a chip is tossed
	 */
	public void setTakeCard(boolean takeCard){
		this.takeCard = takeCard;
	}
	
	/**
	 * Returns, if a card will be taken or not
	 * @return if a card will be taken (true) or not (false)
	 */
	public boolean takeCard(){
		return takeCard;
	}
	
	/**
	 * Returns the winrate of the node
	 * @return winrate
	 */
	public double getWinrate(){
		return winrate;
	}
	
	/**
	 * Returns the number, the node is assigned to
	 * @return cardValue
	 */
	public int getCardValue(){
		return cardValue;
	}
	
	/**
	 * Add a child to the Node
	 * @param child children Node
	 */
	public void addChild(Node child){
		children.add(child);
		child.setParent(this);
	}
	
	/**
	 * Retrieve all children of the node
	 * @return all children of the node
	 */
	public ArrayList<Node> getChildren(){
		return children;
	}
		
	/**
	 * Set the parent of a Node
	 * @param parent the parent node of a node
	 */
	private void setParent(Node parent){
		this.parent = parent;
	}
	
	/**
	 * Returns the nodes parent
	 * @return parent node
	 */
	public Node getParent(){
		return parent;
	}
	
	/**
	 * Retrieve the Node as a string.
	 * @return The parameters of the node returned in a String;
	 */
	public String toString(){
		String toString = "CardValue " + cardValue + ", Times played: " + timesPlayed + ", Games won: " + gamesWon + ", win rate: " + winrate + ", takeCard: " + takeCard;  
		return toString;
	}
}
