package Library.AI.GeneticNeuralNetwork;

import java.util.ArrayList;

import Library.Board;
import Library.BoardNeural;
import Library.Card;
import Library.Player;
import Library.AI.AIInterface;

public class NeuralNetwork implements AIInterface{
	
	private NeuronLayer neuronLayers[];
	private int maxNeurons = 64;
	private int maxneuronLayers = 4;
	private int learningCycles = 20;
	private int fitness = 0;
	private BoardNeural boardNeural = null;
	
	private double learningRate = 0.05;
	
	public NeuralNetwork(int inputSize) {		
		neuronLayers = new NeuronLayer[5];
		neuronLayers[0] = new NeuronLayer(4,4);
		neuronLayers[1] = new NeuronLayer(4,20);
		neuronLayers[2] = new NeuronLayer(20,20);
		neuronLayers[3] = new NeuronLayer(20,20);
		neuronLayers[4] = new NeuronLayer(20,1);
	}
	
	public NeuralNetwork(NeuronLayer neuronLayers[]) {
		this.neuronLayers = neuronLayers.clone();
	}
	
	public double getOutputs(double inputs[]) {
		System.out.println(inputs[1] + " Inputs");
		for (int i = 0; i < neuronLayers.length; i++) {
			inputs = neuronLayers[i].outputs(inputs);
		}
		System.out.println(inputs[0] + " Outputs");
		return inputs[0];
	}
	
	public NeuronLayer[] getneuronLayers() {
		return neuronLayers;
	}
	
	public double[] getInputs() {
		if(boardNeural == null) {
			Board board = Board.getInstance();
			
			double inputs[] = {(double)board.getCurrentPlayer().getScore(), (double)board.getCurrentPlayer().getChips(), (double)board.getCurrentChips(), (double)board.getCurrentCard().getNumber()};
			return inputs;
		} else {
			
			double inputs[] = {(double)boardNeural.getCurrentPlayer().getScore(), (double)boardNeural.getCurrentPlayer().getChips(), (double)boardNeural.getCurrentChips(), (double)boardNeural.getCurrentCard().getNumber()};
			return inputs;
		}
	}
	
	public void setBoardNeural(BoardNeural boardNeural) {
		this.boardNeural = boardNeural;
	}
	
	public int getFitness() {
		System.out.println("Fitness " + fitness);
		
		return fitness;
	}

	public boolean GetMove() {
		//System.out.println("NETWORKS TURN");
		double output = getOutputs(getInputs());
		if(output >= 0.5) {
			return true;
		} else {
			return false;
		}
	}

	public void gameIsFinished(ArrayList<Player> winner) {
		if(winner.get(0).getID() == 2) {
			fitness++;
		}
		
		boardNeural = null;
	}
}
