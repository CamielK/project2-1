package Uct;

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
	
	public static void main (String[]args){
		SuperTree superT = new SuperTree(new Node(2));
		SubTree subT = new SubTree(new Node(0));
	
		superT = superT.loadSuperTree();
		superT.saveSuperTree();
		
		subT = subT.loadSubTree();
		subT.saveSubTree();
	
	}
	
	
	public boolean GetMove() {
		return false;
	}

}

