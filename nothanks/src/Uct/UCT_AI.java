package Uct;

import java.util.ArrayList;

import Library.Board;
import Library.Player;
import Library.AI.AIInterface;

public class UCT_AI implements AIInterface{
	/*
	 *TO DO:
	 *
	 * Node addition
	 *
	 *Comment Tree and Node Classes
	 *Children addidtion doesnt work good
	 */
	
	
	/*
	 * Weakness of UCT
	 * Train against one strategy will make it perform poorly against other strategies 
	 * since the winrate is trimmed against that one
	 */
	private Board board;
	private Tree tree;
	private boolean addedNode;
	private Node currentNode;
	private ArrayList<Node> visitedNodes = null;
	
	public UCT_AI(Board board){
		this.board = board;
		tree = new Tree().load();
		currentNode = tree.getRoot();
		addedNode = false;
		visitedNodes = new ArrayList<Node>();
	}
	
	public boolean GetMove() {
		System.out.println("My Move");
		System.out.println("Compute: " + computeMove());
		return computeMove();
	}
	
	private boolean computeMove(){
		System.out.println(visitedNodes.size());
		if(hasChildren()){
			Node highestWinr = highestWinrate();
			double hWinrate = highestWinr.getWinrate();
			if(hWinrate < Math.random() || currentNode.getVisited()){
				int nextNode = (int) ((Math.random()* hWinrate));
				
				if(!currentNode.getVisited())visitedNodes.add(currentNode);
				currentNode.visit();
				currentNode = currentNode.getChildren().get(nextNode);		
				return currentNode.takeCard();
			}
			else{
				currentNode = highestWinr;
				if(!currentNode.getVisited())visitedNodes.add(currentNode);
				currentNode.visit();
				return currentNode.takeCard();
			}
		}
		else{
			addNode();
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit();
			return Math.random() < 0.5;
		}
	}
	
	@Override
	public void gameIsFinished(ArrayList<Player> winner) {
		evaluate(winner);
	}
	
	private void evaluate(ArrayList<Player> winner){
		boolean won = false;
		for(int i = 0; i < winner.size(); i++){
			if(winner.get(i).getAgent() == this){
				won = true;
			}
		}
		for(int i = 0; i < visitedNodes.size(); i++){
			visitedNodes.get(i).addGame(won);
			System.out.println("V nodes value: " + visitedNodes.get(i).getCardValue());
		}
		visitedNodes = null;
		tree.save();
	}
	
	private Node highestWinrate(){
		Node highest = new Node();
		for(int i = 0; i < currentNode.getChildren().size(); i++){
			if(highest.getWinrate() >= currentNode.getChildren().get(i).getWinrate()
					&& board.getCurrentCard().getNumber() == currentNode.getCardValue()){
				highest = currentNode.getChildren().get(i);
			}
		}
		return highest;
	}
	
	/**
	 * Returns true if the current Node has Children
	 * @return true if the amount of Children is > 0
	 */
	private boolean hasChildren(){
		return !(currentNode.getChildren().isEmpty());
	}
	
	private void addNode(){
		if(addedNode)return;
		else{
			currentNode = tree.addNode(currentNode, board.getCurrentCard().getNumber());
			addedNode = true;
		}
	}

}

