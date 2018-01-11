package Library;

import java.util.ArrayList;

import Library.AI.AIInterface;
import Library.AI.GeneticNeuralNetwork.GeneticAlgorithm;
import Library.AI.GeneticNeuralNetwork.NeuralNetwork;
import Library.AI.NevertakeAI.NevertakeAI;

public class BoardNeural {

    private Card currentCard;
    private Deck cardDeck = null;
    private ArrayList<Player> players;

	private int currentChips = 0;
	private boolean isFinished = false;
    private Player currentPlayer;

    public BoardNeural(NeuralNetwork network) {

        this.cardDeck = new Deck();
        System.out.println(cardDeck.get(0).getNumber() + " NUMBER");
        System.out.println(cardDeck.size() + " SIZE");
        currentCard = cardDeck.get(0);
        players = new ArrayList<Player>();
        
        //Temporary way to add players
        for(int i = 0; i < 2; i++) {
    		players.add(new Player(11, i + 1));
    	}
        currentPlayer = players.get(0);

        players.get(0).SetAIAgent(new NevertakeAI());
        players.get(1).SetAIAgent(network);

        System.out.println("TEST: It's Player " + currentPlayer.getID() + "'s turn!");
        System.out.println("TEST: Current Card is " + currentCard.getNumber());
    }

	public void setPlayerAsAI(int id, AIInterface agent) {
    	players.get(id).SetAIAgent(agent);
	}

    public void nextTurn() {
    	if(players.indexOf(currentPlayer) + 1 >= players.size()) {
    		currentPlayer = players.get(0);
    	} else {
    		currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
    	}
    	
    	//System.out.println("TEST: It's Player " + currentPlayer.getID() + "'s turn!");
    	//System.out.println("TEST: Current Card: " + currentCard.getNumber() + " Current Chips: " + currentChips);
    }
    
    public void giveCardChips() {

    	currentPlayer.addCard(currentCard);
		currentPlayer.addChips(currentChips);
		currentChips = 0;

		if(cardDeck.getNumCards() <= 1) {
			win();
			return;
		}

    	currentCard = cardDeck.removeCards(1);
    	
    	nextTurn();
    }
    
    public void tossChip() {
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
		for(Player players : players){
			players.gameIsFinished(winners);
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

	public Player getCurrentPlayer () {
		return this.currentPlayer;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public Deck getDeck() {
		return cardDeck;
	}
	
	public void setDeck(Deck cardDeck) {
		this.cardDeck = cardDeck;
	}
}
