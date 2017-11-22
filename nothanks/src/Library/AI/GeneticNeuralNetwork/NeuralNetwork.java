package Library.AI.GeneticNeuralNetwork;

public class NeuralNetwork {
	private NeuronLayer neuronLayers[];
	
	public NeuralNetwork() {
		//Create network here
	}
	
	public double[] outputs(double inputs[]) {
		for (int i = 0; i < neuronLayers.length; i++) {
			inputs = neuronLayers[i].outputs(inputs);
		}
		return inputs;
	}
}
