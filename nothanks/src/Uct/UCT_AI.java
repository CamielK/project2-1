package Uct;

import Helper.Config;
import Library.AI.AIInterface;
import Library.Board;
import Library.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UCT_AI implements AIInterface{
	/*
	 *TO DO:
	 *
<<<<<<< HEAD
	 * Comment Tree and Node Classes
	 * Cluster Nodes (3-8),(9-13),(14-18),(19-23),(24-28)(29-33)
	 * Tree with fixed order:2^16 = 65.536 nodes for every card order and always take "AI"
	 * Maximum depth of the Tree = 23 (for 2 players) (24Cards - every 2nd turn + 11 chips played) --> Might go deeper?
	 * 2^23 = Nodes 8.388.608 times permutations (38567100) (33 over 24) 
	 * 323.524.283.596.800 Total possibilities (323,5 trillion different possibilities) without clustering
	 *  
	 */
	
	
	/*
	 * Weakness of UCT
	 * Train against one strategy will make it perform poorly against other strategies 
	 * since the winrate is trimmed against that one
	 */
	private Board board;
	private Tree tree;
	private boolean addedNode, print = false;
	private Node currentNode;
	private ArrayList<Node> visitedNodes = null;
	private String logpath = Config.logpath + "\\src\\Uct\\Gamelog.txt";
	private File f;
	private long lastTime;
	FileWriter fileWriter;
	
	/**
	 * Construct the AI, using the UCT adaption of MiniMax tree search
	 * @param board the current borad. Needed for Information like playercount and currentCard
	 */
	public UCT_AI(Board board){
		lastTime = System.currentTimeMillis();
		this.board = board;
		logpath = Config.logpath + "\\src\\Uct\\Logs\\Gamelog" + board.getPlayers().size() + ".txt";
		f = new File(logpath);
		tree = new Tree(board.getPlayers().size());
		tree = tree.load();
		currentNode = tree.getRoot();
		addedNode = false;
		visitedNodes = new ArrayList<Node>();
	}
	
	/**
	 * retrieves the move, the AI will make
	 */
	public boolean GetMove() {
		return computeMove();
	}
	
	/**
	 * Computes the move. Based on the value assigned to the nodes. Consider the following cases
	 * Case 1: Both possibilities, taking a card and tossing a chip have been explored. With a certain chance 
	 * 		   (currently 80%) the one with the higher winrate will be chosen.
	 * Case 2: One of the two possibilites has already been explored. With a chance of (currently 80%) the node
	 * 		   which has already been explored will be chosen. Otherwise the unexplored node will be explored.
	 * Case 3: No possibility exisits. A node will be created with a 50% chance of taking the card or declining it.
	 * 
	 * @return returns the move the AI will be made
	 */
	private boolean computeMove(){
		if(!(board.getCurrentPlayer().getChips() > 0)){
			System.out.println("No chips!");
			return true;
		}
		int exists = 0;
		double threshold = Math.random();
		Node[] choices = new Node[2];
		for(int i = 0; i < currentNode.getChildren().size() && exists < 2; i++){
			if(currentNode.getChildren().get(i).getCardValue() == board.getCurrentCard().getNumber()){
				//System.out.println("Existing: " + exists);
				choices[exists] = currentNode.getChildren().get(i);
				exists++;
			}
		}
		/*Both choices (take card and toss chip) already exists. 
		 *Choose the one with the higher winrate and if a random number is smaller than the winrate
		 *else choose the other one 
		 */
		if(exists == 2){
			if(print)System.out.println("Two children");
			if(choices[0].getWinrate() > choices[1].getWinrate()){
				//if(choices[0].getWinrate() < threshold){
				if(0.8 < threshold){
					if(print)System.out.println("Chose good child");
					currentNode = choices[1];
				}
				else {
					if(print)System.out.println("Chose bad child");
					currentNode = choices[0];
				}
			}
			else{
				//if(choices[1].getWinrate() < threshold){
				if(0.8 < threshold){
					if(print)System.out.println("Chose good child");
					currentNode = choices[0];
				}
				else {
					currentNode = choices[1];
					if(print)System.out.println("Chose bad child");
				}
			}
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit(true);
			if(print)System.out.println(currentNode.toString());
			return currentNode.takeCard();
		}
		
		/*One choice already exists.
		 *If win rate is bigger than random number choose this one.
		 *else generate the other choice
		 */
		else if(exists == 1){
			if(print)System.out.println("One Child");
			//if(choices[0].getWinrate() < threshold){
			if(0.8 < threshold){
				if(print)System.out.println("Add Child");
				addNode(choices[0]);
			}
			else{
				if(print)System.out.println("Chose good child");
				currentNode = choices[0];
			}
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit(true);
			if(print)System.out.println(currentNode.toString());
			return currentNode.takeCard();
		}
		else if(exists == 0){
			if(print)System.out.println("No child");
			boolean takeCard;
			takeCard = addNode(null);
			if(!currentNode.getVisited())visitedNodes.add(currentNode);
			currentNode.visit(true);
			if(print)System.out.println(currentNode.toString());
			return takeCard;
		}
		return true;
	}	
		
	/**
	 * When the game is over notify the AI and start the evaluation
	 */
	@Override
	public void gameIsFinished(ArrayList<Player> winner) {
		evaluate(winner);
	}
	private void evaluate(ArrayList<Player> winner){
		System.out.println("Seconds needed: " + (System.currentTimeMillis()-lastTime)/1000);
		lastTime = System.currentTimeMillis();
		
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

		double temp = 0;
		if(visitedNodes!=null) {
			for(int i = 0; i < visitedNodes.size(); i++){
				//System.out.println("Nodes: " + visitedNodes.get(i).getCardValue());
				temp = 1/(visitedNodes.size()-i);
				visitedNodes.get(i).addGame(won, temp);
				visitedNodes.get(i).visit(false);
			}
		}
		visitedNodes = null;
		tree.save();
	}
	
	/**
	 * Logs the game (if its a win or a loose)
	 * @param won win = true, lose = false
	 * @throws IOException
	 */
	private void logGame(boolean won) throws IOException{
		fileWriter = new FileWriter(f,true);
		if(won)fileWriter.write("Won\n");
		else fileWriter.write("Lose\n");
		fileWriter.close();
	}
	
	/**
	 * Add one node per game.
	 * @param existing If a node with that value already exists. Add the node with the opposing move
	 * @return returns the added node
	 */
	private boolean addNode(Node existing){
		if(addedNode) {
			double random = Math.random();
			if(board.getCurrentCard().getNumber() <=13) {
				if(random<0.75) {
					if(print)System.out.println("Take " + board.getCurrentCard().getNumber() );
					return true;
				}
			}
			else if(board.getCurrentCard().getNumber() <=24) {
				if(random<0.5) {
					if(print)System.out.println("Take " + board.getCurrentCard().getNumber() );
					return true;
				}
			}
			else if(board.getCurrentCard().getNumber() <=35) {
				if(random<0.25) {
					if(print)System.out.println("Take " + board.getCurrentCard().getNumber() );
					return true;
				}
			}
			if(print)System.out.println("Decline " + board.getCurrentCard().getNumber() );
			return false;
		}
		else{
			//System.out.println("Add Node");
			if(existing == null){
				boolean take;
				if(Math.random()>currentNode.getCardValue()*(1/36))take = true;
				else take = false;
				currentNode = tree.addNode(currentNode, board.getCurrentCard().getNumber(), take);
			}
			else{
				currentNode = tree.addNode(currentNode, board.getCurrentCard().getNumber(), !existing.takeCard());
			}
			addedNode = true;
			return currentNode.takeCard();
		}
	}

}

