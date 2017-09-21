package Library;

import Helper.Logger;

import java.util.ArrayList;

public class Board {
	
	private static Board board;

    private Card currentCard;
    private Deck cardDeck = null;
    private Logger logger = null;
    private ArrayList<Player> players;

	private int currentChips = 0;
    private int currentPlayer = 0;

    private Board() {
    	logger = Logger.getInstance();
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
    
    public void giveCardChips() {
    	logGameProgress(true);

    	players.get(currentPlayer).addCard(currentCard);
    	currentCard = cardDeck.removeCards(1);

		players.get(currentPlayer).addChips(currentChips);
		currentChips = 0;

    	System.out.println("Current Card is " + currentCard.getNumber());
    	nextTurn();
    }
    
    public void tossChip() {
    	logGameProgress(false);

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

	/**
	 * Writes the decision and current game status to disk
	 *
	 * @param pickedCard True if player picked a card, false if player tossed a chip.
	 */
	public void logGameProgress(boolean pickedCard) {
		String csvProgress = "";

		// Classifier
		if (pickedCard) csvProgress += "1,";
		else csvProgress += "0,";

		// Global game state
		csvProgress += currentCard.getNumber() + "," +
				currentChips + "," +
				cardDeck.getNumCards() + ",";
		//TODO: use range of cards instead of num cards (e.g. there is 5 cards left in range 3-10, 2 cards left in range 10-20 and 4 cards left in range 10-35)


		// Players state
		for (int i=0; i < players.size(); i++) {
			csvProgress += players.get(i).getChips()  + "," +
					players.get(i).getCards().size();
			//TODO: use range of cards instead of num cards (e.g. player has 3 cards in range 3-10, 1 cards in range 10-20 and 2 cards in range 10-35)
		}


		logger.write(csvProgress);
	}
}
