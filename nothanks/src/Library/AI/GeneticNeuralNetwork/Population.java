package Library.AI.GeneticNeuralNetwork;

public class Population {
	
	NeuralNetwork networks[];
	
	public Population(int populationSize) {
		networks = new NeuralNetwork[populationSize];
		
		for(int i = 0; i < populationSize; i++) {
			networks[i] = new NeuralNetwork(2);
		}
	}
	
	public NeuralNetwork getFittest() {
		NeuralNetwork fittest = networks[0];
		
		for(int i = 0; i < getPopulationSize(); i++) {
			if(fittest.getFitness() < networks[i].getFitness()) {
				fittest = networks[i];
			}
		}
		
		return fittest;
	}
	
	public int getPopulationSize() {
		return networks.length;
	}
	
	public NeuralNetwork getNetwork(int index) {
		return networks[index];
	}
	
	public void setNetwork(NeuralNetwork network, int index) {
		networks[index] = network;
	}
}
