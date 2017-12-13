package Library.AI.GA;
import Library.AI.AIInterface;
import Library.Player;
import java.util.ArrayList;
//import Library.Board;
//import java.lang.Math;


public class GeneticAI implements AIInterface{

    private static TS ts = new TS();
    static final int defaultGeneLength = 3;
    static final int maxChips = 22;
    private double[] genes = new double[defaultGeneLength];
    static double minValue;         //minimum value of TS.valueP
    static double maxValue;                   //maximum value of TS.valueP
    final static double one = (double) 1;
    private int fitness = 0;


    public GeneticAI(){
        minValue = (one/maxChips + one) * one/(double) 35;
       // maxValue = (double) 2* (double) 2 * (double) 22/(double) 3;
        maxValue = 1.5;    //Chosen 1.5 through the frequency list of values during games
        generate();
    }

    //Generates the gene, position 0 contains the lowerBound for the threshold, position 1 the upperBound, position 2 the middle threshold for value 2
    public void generate() {
        genes[0] = minValue;
        genes[1] = maxValue;
        genes[2] = (minValue + maxValue) /2;
    }

    public double getGene(int index){
        return genes[index];
    }

    public void setGene(int index, double value){
        genes[index] = value;
        fitness = 0;
    }

    public int size() {
        return defaultGeneLength;
    }

    // Returns the fitness of this gene
    public int getFitness() {
          //  fitness = FitnessCalc.getFitness(this);
    //    fitness = 0;
     //   int rankFitness = (Board.getInstance().getPlayers().size() - getRank()) ;
     //   fitness += chipsLeft*5 + rankFitness*50 + getScoreDiff(); //+ scoreDiffDown - scoreDiffUp;
        return fitness;
    }

    public void win(){
        fitness++;
    }

    public boolean GetMove() {
        return ts.GetMove(getGene(0),getGene(1),getGene(2));
    }

    public void gameIsFinished(ArrayList<Player> winner){
     //   if (board.getIsFinished()){
            //stop?
      //  }
    }

  /*

    int rank;
    int score;
    int chipsLeft;

    public void resetFitness(){
        fitness = 0;
    }

    public void setChips(int chips){
        chipsLeft = chips;
    }

    public int getChips(){
        return chipsLeft;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public int getScoreDiff(){
        Player opponent = Board.getInstance().getPlayers().get(0);    //HardCoded : Player 0 is RandomAI and Player 1 is GA
        return Math.abs(score - opponent.getScore());
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }*/


}
