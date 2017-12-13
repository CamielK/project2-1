package Simulate;

import Library.AI.GA.Algorithm;
import Library.AI.GA.GeneticAI;
import Library.AI.GA.Population;
import Library.AI.RandomAI.RandomAI;
import Library.Board;
import Library.Player;
//import Uct.UCT_AI;

import java.util.ArrayList;
import java.util.List;

// Logging Transistent


public class Controller {

    private Board board;
    private int counter = 0;
    private int maxRounds = 0;
    final int id = 1;
    private int index = 0 ;

    Population population;
    Algorithm genAlgo;

    //Used to get the threshold for TS
    private int roundsPerIndiv = 200;
    int popSize = 25;

    //Used to test a Single Gene
    boolean testSingleGene = false;
    private GeneticAI fitIndiv;
    double min = 0.24987012987012985;
    double max = 0.256;
    double mid = 0.34793506493506493;


    /**
     * Start playing games until the max number of rounds is reached
     * @param numRounds max number of rounds
     */
    public void launch(int numRounds)
    {
        this.maxRounds = numRounds;
        this.board = Board.getInstance();

        if (testSingleGene) {
            fitIndiv = new GeneticAI();
            fitIndiv.setGenes(min, max, mid);
            board.setPlayerAsAI(id,fitIndiv);
        } else {
            genAlgo = new Algorithm();
            population = new Population(popSize, true);
            population = genAlgo.evolvePopulation(population, true);   //Need to evolve initial Population (where all genes are still the same)
        }


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

        //Loop through every individual in the population to let it play
        if (testSingleGene) {
            board.setPlayerAsAI(id, fitIndiv);
            System.out.println("Gene: " + fitIndiv.getGene(0) + " / " + fitIndiv.getGene(1) + " / " + fitIndiv.getGene(2));
            System.out.println("Fitness: " + fitIndiv.getFitness());
        } else {
            board.setPlayerAsAI(id,population.getIndividual(index%popSize));
            index++;
        }
        //   System.out.println("Gene: " + population.getIndividual(index%popSize).getGene(0) + " / " + population.getIndividual(index%popSize).getGene(1) + " / " + population.getIndividual(index%popSize).getGene(2));
        //   System.out.println("Fitness: " + population.getIndividual(index%popSize).getFitness());


        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).isAI()) {
                System.err.println("Warning: error while simulating gameplay; player " + i + " is not an AI agent. Setting player " + i + " as RandomAI. Edit the Board constructor to initialize the correct AI agents");
                board.setPlayerAsAI(i, new RandomAI());
            }
        }
    }


    //Updating the GA
    public void updateGeneticAI(){

     //Increase Fitness if ga wins
        ArrayList<Player> winners = board.getWinnerList();
        for (Player winner : winners) {
            if (winner.getAgent() instanceof GeneticAI){
                ((GeneticAI) winner.getAgent()).win();
            }
        }

        if(!testSingleGene) {
            //Get next Generation if every individual has played it's rounds
            if ((counter % (popSize * roundsPerIndiv)) == 0) {
                population = genAlgo.evolvePopulation(population, false);
            }
        }
    }

    public void playRound() {
        if (board.getIsFinished()) {
            counter++;
            if (counter > maxRounds) {
                System.out.println("Finished simulating " + maxRounds + " rounds.");
                System.exit(0);
            }
            System.out.println("Game simulation "+ (counter-1) + " finished.. Resetting board");

            updateGeneticAI();
            Board.reset();
            this.board = Board.getInstance();
            ensureAI();

//          System.out.println("Player 0 is AI : " + board.getPlayers().get(0).isAI());
//          System.out.println("Player 1 is AI : " + board.getPlayers().get(1).isAI());

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