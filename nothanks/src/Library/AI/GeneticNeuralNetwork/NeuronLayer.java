package Library.AI.GeneticNeuralNetwork;

import java.util.Random;

public class NeuronLayer {
	
	private Neuron neurons[];
	private double minWeight = -1;
	private double maxWeight = 1;
	
	public NeuronLayer(int inputSize, int size) {
		neurons = new Neuron[size];
		for (int i = 0; i < size; i++) {
			double[] weights = new double[inputSize];
			for (int j = 0; j < inputSize; j++) {
				weights[j] = randomWeight();
			}
			neurons[i] = new Neuron(weights);
		}
	}
	
	public double[] outputs(double inputs[]) {
		double[] out = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			out[i] = neurons[i].output(inputs);
		}
		return out;
	}
	
	public int getSize() {
		return this.neurons.length;
	}
	
	public Neuron getNeuron(int i) {
		return neurons[i];
	}
	
	private double randomWeight() {
        Random random = new Random();
        
        return minWeight + (maxWeight - minWeight) * random.nextDouble();
    }
}
