package Library.AI.GeneticNeuralNetwork;

public class NeuronLayer {
	
	private Neuron neurons[];
	
	public NeuronLayer(int inputSize, int size) {
		neurons = new Neuron[size];
		for (int i = 0; i < size; i++) {
			double[] weights = new double[inputSize];
			for (int j = 0; j < inputSize; j++) {
				weights[j] = (Math.random()-0.5)*10;
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
}
