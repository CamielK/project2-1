package Uct;

import java.util.ArrayList;

import Library.Board;
import Library.AI.AIInterface;

public class UCT_AI implements AIInterface{
	/*
	 *TO DO:
	 *
	 *Merge with game
	 *Link super and subtree maybe s and S
	 *add win rate
	 *Comment Tree and Node Classes
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
	
	private void setCurrentCard(){
		board.getCurrentCard();
	}
	
	public boolean GetMove() {
		return computeMove();
	}
	
	private boolean computeMove(){
		visitedNodes.add(currentNode);
		if(hasChildren()){
			Node highestWinr = highestWinrate();
			double hWinrate = highestWinr.getWinrate();
			if(hWinrate < Math.random() || currentNode.getVisited()){
				int nextNode = (int) ((Math.random()* hWinrate)+1);
				currentNode = currentNode.getChildren().get(nextNode);		
				currentNode.visit();
				return currentNode.takeCard();
			}
			else{
				currentNode = highestWinr;
				currentNode.visit();
				return currentNode.takeCard();
			}
		}
		else{
			addNode();
			currentNode.visit();
			return Math.random() < 0.5;
		}
	}
	
	private void evaluate(){
		String ai = "Player ";
		boolean won = false;
		for(int i = 0; i < board.getPlayers().size(); i++){
			if(board.getPlayers().get(i).getAgent() == this){
				int winner = board.getPlayers().get(i).getID();
				ai += winner;
			}
		}
		if(board.getWinners().contains(ai)) won = true;
		for(int i = 0; i < visitedNodes.size(); i++){
			visitedNodes.get(i).addGame(won);
		}
		visitedNodes = null;
		visitedNodes.add(tree.getRoot());
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

