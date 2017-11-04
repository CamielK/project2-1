package Uct;

public class Vertex {

	private Edge takeCard, tossChip;
	private Edge nextCard;
	private int cardValue, timesVisited, gamesWon;
	
	public Vertex(int cardValue){
		this.cardValue = cardValue;
		timesVisited = 0;
		gamesWon = 0;
	}
	
	public void addSuperChild(Vertex superChild){
		nextCard = new Edge(this, superChild);
	}
	
	public void addSubChild(Vertex subChild, int move){
		if(move == 0 || move == 1) takeCard = new Edge(this, subChild, move);
	}
	
	public Vertex getChild(){
		return nextCard.getChild();
	}
	
	public int getCardValue(){
		return cardValue;
	}
}
