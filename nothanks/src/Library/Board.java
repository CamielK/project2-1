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
    private Player currentPlayer;

    private Board() {
    	logger = Logger.getInstance();
        cardDeck = new Deck();
        currentCard = cardDeck.get(0);
        players = new ArrayList<Player>();
        
        //Temporary way to add players
        for(int i = 0; i < 3; i++) {
    		players.add(new Player(11));
    	}
        currentPlayer = players.get(0);
        
        System.out.println("It's Player " + currentPlayer.getID() + "'s turn!");
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
    	if(players.indexOf(currentPlayer) + 1 >= players.size()) {
    		currentPlayer = players.get(0);
    	} else {
    		currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
    	}
    	
    	System.out.println("It's Player " + currentPlayer.getID() + "'s turn!");
    	System.out.println("Current Card: " + currentCard.getNumber() + " Current Chips: " + currentChips);
    }
    
    public void giveCardChips() {
    	logGameProgress(true);

    	currentPlayer.addCard(currentCard);

		currentPlayer.addChips(currentChips);
		currentChips = 0;

    	System.out.println("Current Card is " + currentCard.getNumber());
    	
    	System.out.println("Cards in deck: " + cardDeck.getNumCards());
    	if(cardDeck.getNumCards() <= 0) {
    		win();
    		return;
    	}
    	currentCard = cardDeck.removeCards(1);
    	
    	nextTurn();
    }
    
    public void tossChip() {
    	logGameProgress(false);

    	if(currentPlayer.addChips(-1)) {
    		currentChips++;
    		nextTurn();
    	}
    }
    
    public void win() {
    	ArrayList<Player> winners = new ArrayList<Player>();
    	winners.add(players.get(0));
    	
    	for(int i = 1; i < players.size(); i++) {
    		if(winners.get(0).getScore() < players.get(i).getScore()) {
    			winners = new ArrayList<Player>();
    			winners.add(players.get(i));
    		} else if(winners.get(0).getScore() == players.get(i).getScore()) {
    			winners.add(players.get(i));
    		}
    	}
    	
    	System.out.print("Winners are: ");
    	for(Player winner : winners) {
    		System.out.print("Player " + winner.getID() + " with a score of " + winner.getScore() + " ");
    	}
    	System.out.println("");
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
					players.get(i).getCards().size() + "," +
					players.get(i).getScore() + ",";
			//TODO: use range of cards instead of num cards (e.g. player has 3 cards in range 3-10, 1 cards in range 10-20 and 2 cards in range 10-35)
		}

		// Strip last comma
		csvProgress = csvProgress.substring(0, csvProgress.length()-1);
		System.out.println(csvProgress);

		logger.write(csvProgress);
	}

	public Player getCurrentPlayer () {
		return this.currentPlayer;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}
}
