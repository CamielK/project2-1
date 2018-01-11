package Simulate;

import java.util.List;

import Library.Board;
import Library.BoardNeural;
import Library.Deck;
import Library.Player;
import Library.AI.GeneticNeuralNetwork.NeuralNetwork;
import Library.AI.RandomAI.RandomAI;

public class ControllerNeural {

    private BoardNeural board;
    private int counter = 0;
    private int maxRounds = 0;
    
    private NeuralNetwork network;
    private boolean finished = false;

    /**
     * Start playing games until the max number of rounds is reached
     * @param numRounds max number of rounds
     */
    public void launch(int numRounds, NeuralNetwork network)
    {
    	this.network = network;
        this.maxRounds = numRounds;
        this.board = new BoardNeural(network);
        network.setBoardNeural(board);

        ensureAI();
        while (!finished) {
            playRound();
        }
    }

    /**
     * Make sure all players are AI agents before starting each simulation
     */
    public void ensureAI() {
        // Check AI agents
        List<Player> players = board.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).isAI()) {
                System.err.println("Warning: error while simulating gameplay; player " + i + " is not an AI agent. Setting player " + i + " as RandomAI. Edit the Board constructor to initialize the correct AI agents");
                board.setPlayerAsAI(i, new RandomAI());
            }
        }

//        board.setPlayerAsAI(0, new RandomAI());
//        board.setPlayerAsAI(1, new UCT_AI(board));
    }

    public void playRound()
    {
        if (board.getIsFinished()) {
            counter++;
            if (counter > maxRounds) {
                System.out.println("Finished simulating " + maxRounds + " rounds.");
                finished = true;
                return;
            }
            System.out.println("Game simulation finished.. Resetting board");
            this.board = new BoardNeural(network);
            ensureAI();
        } else {
            boolean move = this.board.getCurrentPlayer().GetAIMove();
            if (move) processTakeCard();
            else if (!move) processTossChip();
        }
    }

    public void processTakeCard() {
        board.giveCardChips();
    }

    public void processTossChip() {
        board.tossChip();
    }
}
