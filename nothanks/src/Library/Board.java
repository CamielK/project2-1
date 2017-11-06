package Library;

import Helper.Logger;
import Library.AI.RandomAI.RandomAI;

import java.util.ArrayList;

public class Board {
	
	private static Board board;

    private Card currentCard;
    private Deck cardDeck = null;
    private Logger logger = null;
    private ArrayList<Player> players;

	private int currentChips = 0;
	private boolean isFinished = false;
    private Player currentPlayer;

    private Board() {
    	logger = Logger.getInstance();
        cardDeck = new Deck();
        currentCard = cardDeck.get(0);
        players = new ArrayList<Player>();
        
        //Temporary way to add players
        for(int i = 0; i < 3; i++) {
    		players.add(new Player(11, i + 1));
    	}
        currentPlayer = players.get(0);

		// TEMP: Init second player as AI player
//		players.get(0).SetAIAgent(new RandomAI());
		players.get(1).SetAIAgent(new RandomAI());
//		players.get(2).SetAIAgent(new RandomAI());

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

    	currentPlayer.addCard(currentCard);
		currentPlayer.addChips(currentChips);
		currentChips = 0;

		if(cardDeck.getNumCards() <= 1) {
			win();
			return;
		}

    	System.out.println("Current Card is " + currentCard.getNumber());
    	
    	System.out.println("Cards in deck: " + cardDeck.getNumCards());
    	logGameProgress(true);
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

    public Integer getNumCardsLeft() {
    	return this.cardDeck.getNumCards();
	}
    
    public void win() {
    	System.out.println(getWinners());
    	logger.getInstance().write("ENDOFGAME");
    	isFinished = true;
    }

    public String getWinners() {
		ArrayList<Player> winners = new ArrayList<Player>();
		winners.add(players.get(0));

		for(int i = 1; i < players.size(); i++) {
			if((winners.get(0).getScore()-winners.get(0).getChips()) > (players.get(i).getScore()-players.get(i).getChips())) {
				winners = new ArrayList<Player>();
				winners.add(players.get(i));
			} else if((winners.get(0).getScore()-winners.get(0).getChips()) == (players.get(i).getScore()-players.get(i).getChips())) {
				winners.add(players.get(i));
			}
		}

		StringBuilder winnersString = new StringBuilder();
		winnersString.append("Winners are: \n");
		for(Player winner : winners) {
			winnersString.append("Player " + winner.getID() + " with a score of " + (winner.getScore()-winner.getChips()) + " ( "+winner.getScore()+" card points - "+winner.getChips()+" chips)\n");
		}
		return winnersString.toString();
	}

    public boolean getIsFinished() { return isFinished; }

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

		//Format: pickedCard,CardNumber,ChipsOnCard,NumCardsLeft,Player0NumChips,Player0NumCards,Player0Score,Player1NumChips,Player1NumCards,Player1Score,Player2NumChips,Player2NumCards,Player2Score

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

	public static void reset() {
		board = new Board();
	}
}
