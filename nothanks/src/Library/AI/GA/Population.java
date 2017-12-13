package Library.AI.GA;

import java.util.ArrayList;

public class Population {

    ArrayList<GeneticAI> population;

    public Population (){
        population = new ArrayList<>();
    }

    public Population (int populationSize,boolean initialize){

        population = new ArrayList<GeneticAI>(populationSize);

        if (initialize) {
            for (int i=0 ; i < populationSize ; i++){
                GeneticAI newIndividual = new GeneticAI();
                saveIndividual(i, newIndividual);
            }
        }

    }

    public GeneticAI getIndividual(int index) {
        return population.get(index);
    }

    public GeneticAI getFittest() {
        GeneticAI fittest = getIndividual(0);

        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    public ArrayList<GeneticAI> getPopulation() {
        return population;
    }

    public int size() {
        return population.size();
    }

    public void addIndividual(GeneticAI individual){
        population.add(individual);
    }

    public void saveIndividual(int index, GeneticAI individual) {
        population.add(index,individual);
    }

}