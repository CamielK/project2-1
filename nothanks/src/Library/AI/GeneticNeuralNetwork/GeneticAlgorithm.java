package Library.AI.GeneticNeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

import Library.Player;
import Library.AI.AIInterface;
import Simulate.ControllerNeural;

public class GeneticAlgorithm implements AIInterface {
	
	private double mutationRate = 0.05;
	private int tournamentSize = 5;
	private int populationSize = 10;
	private static Population currentPopulation;
	private boolean trained = false;
	private static int loseCount = 0, winCount = 0;
	
	
	public GeneticAlgorithm() {
		currentPopulation = new Population(populationSize);
	}
	
	public Population evolve(Population oldNetworks) {
		Population newNetworks = new Population(oldNetworks.getPopulationSize());
		
		newNetworks.setNetwork(oldNetworks.getFittest(), 0);
		
		for(int i = 1; i < oldNetworks.getPopulationSize(); i++) {
			NeuralNetwork networkMother = tournamentSelection(oldNetworks);
			NeuralNetwork networkFather = tournamentSelection(oldNetworks);
			
			NeuralNetwork networkChild = crossover(networkMother, networkFather);
			
			newNetworks.setNetwork(networkChild, i);
		}
		
		for(int i = 1; i < newNetworks.getPopulationSize(); i++) {
			mutate(newNetworks.getNetwork(i));
		}
		
		return newNetworks;
	}
	
	public NeuralNetwork tournamentSelection(Population networks) {
		Population tournamentNetworks = new Population(tournamentSize);
		
		for(int i = 0; i < tournamentSize; i++) {
			int randomNetwork = (int)(Math.random() * networks.getPopulationSize());
			tournamentNetworks.setNetwork(networks.getNetwork(randomNetwork), i);
		}
		
		return tournamentNetworks.getFittest();
	}
	
	public NeuralNetwork crossover(NeuralNetwork motherNetwork, NeuralNetwork fatherNetwork) {
		Random random = new Random();
		NeuronLayer neuronLayer[] = motherNetwork.getneuronLayers();
		NeuronLayer fatherNeuronLayer[] = fatherNetwork.getneuronLayers();
		for(int i = 2; i < 3; i++) {
			if(random.nextBoolean()) {
				neuronLayer[i] = fatherNeuronLayer[i];
			}
		}
		return new NeuralNetwork(neuronLayer);
	}
	
	public void mutate(NeuralNetwork network) {
		/*if(Math.random() <= mutationRate) {
			
		}*/
	}

	public boolean GetMove() {
		System.out.println("GENETIC TURN");
		if(!trained) {
			System.out.println("TRAINING GENETIC");
			
			
			for(int i = 0; i < currentPopulation.getPopulationSize(); i++) {
				new ControllerNeural().launch(5, currentPopulation.getNetwork(i));
			}
			currentPopulation = evolve(currentPopulation);
			trained = true;
			System.out.println("NEW GENERATION");
		}
		return currentPopulation.getFittest().GetMove();
	}

	public void gameIsFinished(ArrayList<Player> winner) {
		trained = false;
		
		if(winner.get(0).getID() == 2) {
			winCount++;
		} else {
			loseCount++;
		}
		System.out.println("WE WON " + winCount + " TIMES");
		System.out.println("WE LOST " + loseCount + " TIMES");
	}
}
