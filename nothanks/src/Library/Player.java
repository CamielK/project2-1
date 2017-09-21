package Library;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
	private static int idIncrement;
	private int id;
	private int chips;
	private int score;
	private ArrayList<Card> cards;
	
	public Player(int chips) {
		id = ++idIncrement;
		this.chips = chips;
		cards = new ArrayList<Card>();
	}
	
	public int getID() {
		return id;
	}
	
	public int getChips() {
		return chips;
	}
	
	public boolean addChips(int chips) {
		if(this.chips + chips < 0) {
			return false;
		}
		this.chips += chips;
		return true;
	}
	
	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void addCard(Card card) {
		cards.add(card);
		calculateScore();
	}
	
	public int getScore() {
		return score;
	}
	
	public void calculateScore() {
		Collections.sort(cards);
		
		Card scoreCard = cards.get(0);
		this.score = scoreCard.getNumber();
		for(Card card : cards) {
			if(card == scoreCard)
				continue;
			
			if(card.getNumber() - scoreCard.getNumber() != 1) {
				this.score += card.getNumber();
			}
			scoreCard = card;
		}
	}

	public Integer getScore() {
		return this.score;
	}
}
