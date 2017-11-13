package Uct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	 *Children addidtion doesnt work
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
	private String logpath = "nothanks/src/Uct/Gamelog.txt";
	private File f = new File(logpath);
	FileWriter fileWriter;
	
	public UCT_AI(Board board){
		this.board = board;
		tree = new Tree().load();
		currentNode = tree.getRoot();
		addedNode = false;
		visitedNodes = new ArrayList<Node>();
	}
	
	public boolean GetMove() {
		return computeMove();
	}
	
	private boolean computeMove(){
		if(!(board.getCurrentPlayer().getChips() > 0)){
			System.out.println("No chips!");
			return true;
		}
		System.out.println("UCT AI`s move:");
		int exists = 0;
		double threshold = Math.random();
		Node[] choices = new Node[2];
		for(int i = 0; i < currentNode.getChildren().size() && exists < 2; i++){
			if(currentNode.getChildren().get(i).getCardValue() == board.getCurrentCard().getNumber()){
				System.out.println("Existing: " + exists);
				choices[exists] = currentNode.getChildren().get(i);
				exists++;
			}
		}
		/*Both choices (take card and toss chip) already exists. 
		 *Choose the one with the higher winrate and if a random number is smaller than the winrate
		 *else choose the other one 
		 */
		if(exists == 2){
			System.out.println("Two children");
			if(choices[0].getWinrate() > choices[1].getWinrate()){
				if(choices[0].getWinrate() < threshold){
					currentNode = choices[1];
				}
				else currentNode = choices[0];
			}
			else{
				if(choices[1].getWinrate() < threshold){
					currentNode = choices[0];
				}
				else currentNode = choices[1];
			}
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit(true);
			return currentNode.takeCard();
		}
		/*One choice already exists.
		 *If win rate is bigger than random number choose this one.
		 *else generate the other choice
		 */
		else if(exists == 1){
			System.out.println("One Child");
			if(choices[0].getWinrate() < threshold){
				addNode(choices[0]);
			}
			else{
				currentNode = choices[0];
			}
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit(true);
			return currentNode.takeCard();
		}
		else if(exists == 0){
			System.out.println("No child");
			boolean takeCard;
			takeCard = addNode(null);
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit(true);
			return takeCard;
		}
		return true;
	}	/*
			if(hWinrate < threshold && !currentNode.getVisited()){
				/*int nextNode = (int) ((Math.random()* currentNode.getChildren().size()));
				if(!currentNode.getVisited())visitedNodes.add(currentNode);
				currentNode.visit(true);
				currentNode = currentNode.getChildren().get(nextNode);		
				return currentNode.takeCard();
			}
			//pick node with highest winrate
			else{
				currentNode = highestWinr;
				if(!currentNode.getVisited())visitedNodes.add(currentNode);
				currentNode.visit(true);
				return currentNode.takeCard();
			}
		}
*/
	
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
		
		try {
			logGame(won);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < visitedNodes.size(); i++){
			visitedNodes.get(i).addGame(won);
			visitedNodes.get(i).visit(false);
			System.out.println("V nodes value: " + visitedNodes.get(i).getCardValue());
		}
		visitedNodes = null;
		System.out.println("PreOrder: " + tree.preOrder(tree.getRoot()));
		System.out.println("Save the Trees!");
		tree.save();
	}
	
	private void logGame(boolean won) throws IOException{
		fileWriter = new FileWriter(f,true);
		if(won)fileWriter.write("Won\n");
		else fileWriter.write("Lose\n");
		fileWriter.close();
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
	
	private boolean addNode(Node existing){
		if(addedNode)return true;
		else{
			System.out.println("Add Node");
			if(existing == null){
				currentNode = tree.addNode(currentNode, board.getCurrentCard().getNumber(), Math.random()<0.5);
			}
			else{
				currentNode = tree.addNode(currentNode, board.getCurrentCard().getNumber(), !existing.takeCard());
			}
			addedNode = true;
			return currentNode.takeCard();
		}
	}

}

