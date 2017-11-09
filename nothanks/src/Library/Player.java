package Library;

import Library.AI.AIInterface;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
	private int id;
	private int chips;
	private int score;
	private ArrayList<Card> cards;

	private AIInterface agent = null;
	
	public Player(int chips, int id) {
		this.chips = chips;
		this.id = id;
		cards = new ArrayList<Card>();
	}

	/**
	 * Sets the optional AI agent of this player
	 * @param agent AIInterface
	 */
	public void SetAIAgent(AIInterface agent) {
		this.agent = agent;
	}

	/**
	 * Returns true if the player has been initialized as an AI agent
	 * @return boolean
	 */
	public boolean isAI() {
		if (this.agent == null) return false;
		return true;
	}

	/**
	 * Returns true if the player should pick a card and returns false if the player tosses a chip
	 * @return boolean Pick card (true) or toss chip (false)
	 */
	public boolean GetAIMove() {
		if (this.chips <= 0) return false;
		if (this.agent != null) return this.agent.GetMove();
		return false;
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
}
