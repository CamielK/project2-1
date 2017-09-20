package Library;

import java.util.ArrayList;

public class Player {
	private int chips;
	private int score;
	private ArrayList<Card> cards;
	
	public Player(int chips) {
		this.chips = chips;
		cards = new ArrayList<Card>();
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
	}
}
