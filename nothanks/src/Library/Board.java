package Library;

import Helper.Config;
import Helper.Logger;
import Library.AI.AIInterface;
import Library.AI.GA.GeneticAI;
import Library.AI.RandomAI.RandomAI;
import Uct.UCT_AI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private static Board board;

    private Card currentCard;
    private Deck cardDeck = null;
    private Logger logger = null;
    private File f;
    FileWriter fileWriter;
    private ArrayList<Player> players;

	private int currentChips = 0;
	private boolean isFinished = false;
    private Player currentPlayer;

//    private int[] took= new int[100];

    private Board() {
    	logger = Logger.getInstance();
        cardDeck = new Deck();
        currentCard = cardDeck.get(0);
        players = new ArrayList<Player>();
        
        //Temporary way to add players
        for(int i = 0; i < 2; i++) {
    		players.add(new Player(11, i + 1));
    	}
        currentPlayer = players.get(0);

		// TEMP: Init second player as AI player
        players.get(0).SetAIAgent(new UCT_AI(this));
//        players.get(1).SetAIAgent(new RandomAI());
		//GeneticAI ga = new GeneticAI();
		//ga.setGene(0, 0.249870129);
		//ga.setGene(1, 0.269501);
		//ga.setGene(2, 0.347935064);
        //players.get(1).SetAIAgent(ga);

//        System.out.println("It's Player " + currentPlayer.getID() + "'s turn!");
//        System.out.println("Current Card is " + currentCard.getNumber());
	}
    
    public static Board getInstance() {
		if (board == null) {
			return (board = new Board());
		} else {
			return board;
		}
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
    	
    //	System.out.println("It's Player " + currentPlayer.getID() + "'s turn!");
    //	System.out.println("Current Card: " + currentCard.getNumber() + " Current Chips: " + currentChips);
    }
    
    public void giveCardChips() {

    	currentPlayer.addCard(currentCard);
		currentPlayer.addChips(currentChips);
		currentChips = 0;

		if(cardDeck.getNumCards() <= 1) {
			win();
			return;
		}

//    	System.out.println("Current Card is " + currentCard.getNumber());
//    	System.out.println("Cards in deck: " + cardDeck.getNumCards());

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
    	try {
			makeFile(getWinners()+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	if (Board.getInstance().getPlayers().get(1).getAgent() instanceof GeneticAI){
            GeneticAI ga = (GeneticAI) Board.getInstance().getPlayers().get(1).getAgent();
            String gene = new String("Fitness: " + ga.getFitness() + " || GENE: " + ga.getGene(0) + "/" +ga.getGene(1)+ "/" +ga.getGene(2));
            logger.getInstance().write(gene);
            try {
    			makeFile(gene+"\n");
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    	

    	isFinished = true;
    }

    public ArrayList<Player> getWinnerList(){
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
        return winners;
    }

    public String getWinners() {
		ArrayList<Player> winners = getWinnerList();

		StringBuilder winnersString = new StringBuilder();
		winnersString.append("Winners is ");//are: \n");
		for(Player winner : winners) {
			winnersString.append("Player " + winner.getID()+"\n"); // + " with a score of " + (winner.getScore()-winner.getChips()) + " ( "+winner.getScore()+" card points - "+winner.getChips()+" chips)\n");
			logger.getInstance().write("Winner : Player "+winner.getID());
		}
		/*for(Player players : players){
			players.gameIsFinished(winners);
		}*/
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

		//Format: playerID,pickedCard,CardNumber,ChipsOnCard,NumCardsLeft,Player0NumChips,Player0NumCards,Player0Score,Player1NumChips,Player1NumCards,Player1Score,Player2NumChips,Player2NumCards,Player2Score
		
		csvProgress += currentPlayer.getID() + ",";
		
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
//		System.out.println(csvProgress);

		logger.write(csvProgress);
		try {
			makeFile(csvProgress+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Player getCurrentPlayer () {
		return this.currentPlayer;
	}
	public List<Card> getCurrentDeck() {
		return cardDeck;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	///////// Added method

	//Get the rank of a specific player
	public int getRank(int id){
		int score = players.get(id).getScore();
		int rank = 1;
		for (int i = 0; i < players.size(); i++) {
			if (score > players.get(i).getScore()){
				rank++;
			}
		}
		return rank;
	}
	
	public void makeFile(String info) throws IOException {
    	f= new File(Config.logpath+"/Data/logs.txt");
		f.createNewFile();
		fileWriter = new FileWriter(f,true);
		fileWriter.write(info);
		fileWriter.close();	
    }

	///////////////////

	public static void reset() {
//		win();
		board = new Board();
	}

/*
	public int[] takes() {
		return took;
	}
*/

}
