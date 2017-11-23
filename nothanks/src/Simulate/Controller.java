package Simulate;

import Library.AI.RandomAI.RandomAI;
import Library.Board;
import Library.Player;
import Uct.UCT_AI;

import java.util.List;

public class Controller {

    private Board board;
    private int counter = 0;
    private int maxRounds = 0;

    /**
     * Start playing games untill the max number of rounds is reached
     * @param numRounds max number of rounds
     */
    public void launch(int numRounds)
    {
        this.maxRounds = numRounds;
        this.board = Board.getInstance();

        ensureAI();
        while (true) {
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
                System.exit(0);
            }
            System.out.println("Game simulation finished " + counter + "/" + maxRounds + " ("+ (double) 100*counter/maxRounds + "%) .. Resetting board");
            Board.reset();
            this.board = Board.getInstance();
            ensureAI();
        } else {
            boolean move = Board.getInstance().getCurrentPlayer().GetAIMove();
            if (move) processTakeCard();
            else if (!move) processTossChip();
        }
    }

    public void processTakeCard() {
        Board.getInstance().giveCardChips();
    }

    public void processTossChip() {
        Board.getInstance().tossChip();
    }
}
