package Simulate;

import Library.AI.RandomAI.RandomAI;
import Library.Board;
import Library.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {

    private Board board;
    private int counter = 0;
    private int maxRounds = 0;

    // Statistics
    private int p1_wins = 0;
    private int p2_wins = 0;
    private ArrayList<Long> p1_times = new ArrayList<>();
    private ArrayList<Long> p2_times = new ArrayList<>();

    /**
     * Start playing games untill the max number of rounds is reached
     * @param numRounds max number of rounds
     */
    public void launch(int numRounds)
    {
        this.maxRounds = numRounds;
        this.board = Board.getInstance();

        Board.setLogState(false);

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
            Player winner = Board.getInstance().getWinner();
            if (winner.getID() == 1) p1_wins++;
            else if (winner.getID() == 2) p2_wins++;

            counter++;
            if (counter > maxRounds) {
                System.out.println("Finished simulating " + maxRounds + " rounds.");

                // P1 stats
                Collections.sort(p1_times);
                Collections.sort(p2_times);
                List<Long> p1_times_top5 = new ArrayList<>();
                List<Long> p2_times_top5 = new ArrayList<>();
                for (int i = p1_times.size()-1; i>(p1_times.size()*0.95); i--) p1_times_top5.add(p1_times.get(i));
                for (int i = p2_times.size()-1; i>(p2_times.size()*0.95); i--) p2_times_top5.add(p2_times.get(i));

                System.out.println("P1 ["+Board.getInstance().getPlayers().get(0).getAgent().getClass().getSimpleName()+"] wins: " + p1_wins
                        + ". Average move time: " + p1_times.stream().mapToDouble(a -> a).average().getAsDouble()
                        + ". Avg move time (top 5% longest moves): " + p1_times_top5.stream().mapToDouble(a -> a).average().getAsDouble());
                System.out.println("P2 ["+Board.getInstance().getPlayers().get(1).getAgent().getClass().getSimpleName()+"] wins: " + p2_wins
                        + ". Average move time: " + p2_times.stream().mapToDouble(a -> a).average().getAsDouble()
                        + ". Avg move time (top 5% longest moves): " + p2_times_top5.stream().mapToDouble(a -> a).average().getAsDouble());
                System.exit(0);
            }

            System.out.println("Game simulation finished " + counter + "/" + maxRounds + " ("+ (double) 100*counter/maxRounds + "%) .. Resetting board");

            if (counter%10==0) System.out.println("Progress: simulated " + counter + " out of " + maxRounds + " games.");

            Board.reset();
            this.board = Board.getInstance();
            ensureAI();
        } else {
            long millis = System.currentTimeMillis();
            boolean move = Board.getInstance().getCurrentPlayer().GetAIMove();
            if (Board.getInstance().getCurrentPlayer().getID() == 1) p1_times.add(System.currentTimeMillis()-millis);
            else if (Board.getInstance().getCurrentPlayer().getID() == 2) p2_times.add(System.currentTimeMillis()-millis);

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
