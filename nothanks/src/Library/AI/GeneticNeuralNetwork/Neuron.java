package Library.AI.GeneticNeuralNetwork;

public class Neuron {
	private double inputWeights[];
	private double lastInputs[];
	private double error = 0;
	private double lastOutput = 0;
	
	public Neuron(double inputWeights[]) {
		this.inputWeights = inputWeights;
	}
	
	public double output(double inputs[]) {
		this.lastInputs = inputs;
		double out = 0;
		for (int i = 0; i < inputWeights.length; i++) {
			out += inputs[i] * inputWeights[i];
		}
		this.lastOutput = sigmoid(out);
		return lastOutput;
	}
	
	private double sigmoid(double input) {
		return ( 1.0 / ( 1.0 + Math.exp(-input)));
	}
	
	public double getError() {
		return error;
	}
	
	public void setError(double error) {
		this.error = error;
	}
	
	public double lastOutput() {
		return lastOutput;
	}
	
	public int getInputSize() {
		return this.inputWeights.length;
	}
	
	public double getWeight(int previousNode) {
		return inputWeights[previousNode];
	}
	
	public void setWeight(int previousNode, double weight) {
		inputWeights[previousNode] = weight;
	}
	
	public double getLastInput(int k) {
		return lastInputs[k];
	}
}
