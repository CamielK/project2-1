package Library;

import java.util.ArrayList;

public class Board {
	
	private static Board board;

    private Card currentCard;
    private int currentChips = 0;
    private Deck cardDeck = null;
    private ArrayList<Player> players;
    private int currentPlayer = 0;

    private Board() {
        cardDeck = new Deck();
        currentCard = cardDeck.get(0);
        players = new ArrayList<Player>();
        
        //Temporary way to add players
        for(int i = 0; i < 3; i++) {
    		players.add(new Player(11));
    	}
        
        System.out.println("It's Player " + currentPlayer + "'s turn!");
        System.out.println("Current Card is " + currentCard.getNumber());
    }
    
    public static Board getInstance() {
		if (board == null) {
			return (board = new Board());
		} else {
			return board;
		}
	}
    
    public void nextTurn() {
    	if(currentPlayer + 1 >= players.size()) {
    		currentPlayer = 0;
    	} else {
    		currentPlayer++;
    	}
    	
    	System.out.println("It's Player " + currentPlayer + "'s turn!");
    	System.out.println("Current Card: " + currentCard.getNumber() + " Current Chips: " + currentChips);
    }
    
    public void giveCard() {
    	players.get(currentPlayer).addCard(currentCard);
    	currentCard = cardDeck.removeCards(1);
    	System.out.println("Current Card is " + currentCard.getNumber());
    	nextTurn();
    }
    
    public void giveChips() {
    	players.get(currentPlayer).addChips(currentChips);
    	currentChips = 0;
    	nextTurn();
    }
    
    public void tossChip() {
    	Player player = players.get(currentPlayer);
    	if(player.addChips(-1)) {
    		currentChips++;
    		nextTurn();
    	}
    }
    
    public int getCurrentChips() {
    	return currentChips;
    }
    
    public void setCurrentChips(int currentChips) {
    	this.currentChips = currentChips;
    }
    
    public void addCurrentChips(int currentChips) {
    	this.currentChips += currentChips;
    }
    
    public Card getCurrentCard() {
    	return currentCard;
    }
    
    public void setCurrentCard(Card currentCard) {
    	this.currentCard = currentCard;
    }
}
