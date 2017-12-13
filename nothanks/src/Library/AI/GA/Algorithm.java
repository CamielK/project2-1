package Library.AI.GA;

//import com.sun.javafx.geom.transform.GeneralTransform3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Algorithm {

    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.05;
    private static int tournamentSize;
    private int ind = 0;
    private static Random random = new Random();
    int genSize = GeneticAI.defaultGeneLength;
    int amount = 3;  //FIXME CHANGE AMOUNT DEPENDING ON NUMBER OF ROUNDS AND POPULATION SIZE
    boolean r = false;

    public Population evolvePopulation(Population pop,boolean initialize) {
        Population nextGeneration = new Population();
        Population fittestPop = new Population();

        if (initialize) {
            for (int i = 0; i < amount; i++) {
                fittestPop.addIndividual(pop.getIndividual(i));
            }
        } else {
            addFittest(pop, fittestPop, amount);
        }


        //For each Gene in the fittest population, mutate the gene and add it to the new population until we reach the initial population size
        for (int i = 0; i < fittestPop.size(); i++) {
            for (int j = 0; j < pop.size()/fittestPop.size(); j++) {
                GeneticAI newIndiv = new GeneticAI();
                copyIndiv(fittestPop.getIndividual(i),newIndiv);
                if (r) {
                		int ind;
                		double ran = Math.random();
                		if(ran < 0.33) {
                			ind = 0;
                		} else if(ran < 0.66) {
                			ind = 1;
                		} else
                			ind = 2;
                		mutate(newIndiv,ind);
                } else {
                mutate(newIndiv, ind % genSize);    // Mutate only one chromosome at a time (minValue or maxValue or midValue)
                }                               
                nextGeneration.addIndividual(newIndiv);            //To get a better understanding how it affects each other
            }                                                       //TODO maybe change it to random and see the results?
        }

        if (nextGeneration.size() != pop.size()) {
            if (nextGeneration.size() > pop.size()) {
                Population smallGeneration = new Population();
                for (int i = 0; i < pop.size(); i++) {
                    smallGeneration.addIndividual(nextGeneration.getIndividual(i));
                }
                return smallGeneration;
            }

            while (nextGeneration.size() < pop.size()) {
                nextGeneration.addIndividual(crossover(tournamentSelection(pop), tournamentSelection(pop)));
            }
        }

        ind++;
        return nextGeneration;
    }

    //Helper function to copy an individual
    private void copyIndiv(GeneticAI ga,GeneticAI copy){
        copy.setGene(0,ga.getGene(0));
        copy.setGene(1,ga.getGene(1));
        copy.setGene(2,ga.getGene(2));
    }

    //Add individuals with the best fitness, the second best , ... , the amount best fitness from pop to newPop
    public void addFittest(Population pop, Population newPop, int amount){
        ArrayList<Integer> fitnessArray = new ArrayList<>();

        //Initiate a list of all fitness' of our population
        for (int i = 0; i < pop.size(); i++){
            fitnessArray.add(pop.getIndividual(i).getFitness());
        }

        Collections.sort(fitnessArray);

        //Add individuals with the best fitness, the second best , ... , the amount best fitness
        for (int i = 1 ; i <= amount ; i++){
            for (int j = 0 ; j < pop.size() ; j++){
                if (pop.getIndividual(j).getFitness() == fitnessArray.get(fitnessArray.size()-i)){
                    newPop.addIndividual(pop.getIndividual(j));
                }
                if (newPop.size() == amount){
                    return;
                }
            }
        }
    }

    // Crossover individuals
    private static GeneticAI crossover(GeneticAI indiv1, GeneticAI indiv2) {
        GeneticAI newSol = new GeneticAI();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(GeneticAI indiv,int index) {
        int lowerInt = (int) (indiv.getGene(0)*1000);     //*1000 , to not loose information from double -> int
        int upperInt = (int) (indiv.getGene(1)*1000);

        double value=0;

        //Mutating lower threshold, checks if lower < upper
        if (index == 0){
            int x;
            if (Math.random() > mutationRate){
                x = 1;
            } else {
                x = -1;
            }
            value = indiv.getGene(index) +(double) random.nextInt(Math.abs(upperInt-lowerInt))/1000*x;   // divided by 1000 to negate the *1000 above
            while (value > indiv.getGene(1)){
                value = indiv.getGene(index) +(double) random.nextInt(Math.abs(upperInt-lowerInt))/1000*x;
            }
        }

        //Mutating upper threshold, checks if lower < upper
        if (index == 1){
            int x;
            if (Math.random() > mutationRate){
                x = 1;
            } else {
                x = -1;
            }
            value = indiv.getGene(index) - (double) random.nextInt(upperInt-lowerInt)/1000*x;   // divided by 1000 to negate the *1000 above
            while (value < indiv.getGene(0)){
                value = indiv.getGene(index) - (double) random.nextInt(Math.abs(upperInt-lowerInt))/1000*x;
            }
        }

        //Mutating mid threshold
        if (index == 2){
            int x;
            if (Math.random() < uniformRate){
                x = 1;
            } else {
                x = -1;
            }
            value = indiv.getGene(index) + (double) random.nextInt(Math.abs(upperInt-lowerInt))/1000*x;
        }
        indiv.setGene(index,value);

        if (value == 0){
            System.err.print("Threshold value = 0 , wrong index!");
        }
    }

    // Select individuals for crossover
    private static GeneticAI tournamentSelection(Population pop) {
        tournamentSize = Math.round(pop.size()/3);     //fixme find a better size?
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        GeneticAI fittest = tournament.getFittest();
        return fittest;
    }

}