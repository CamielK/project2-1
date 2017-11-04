package Uct;

public class Edge {

	private Vertex parent, child;
	private int move;
	
	/**
	 * Create an egde in the subtree linking the parent and the child node
	 * The Edge contains information about the type of move made i.e. toss chip or take card
	 * 
	 * @param parent parent node of the edge
	 * @param child child node of the edge
	 * @param move type of move (0=take card, 1=toss chip)
	 */
	public Edge(Vertex parent, Vertex child, int move){
		this.parent = parent;
		this.child = child;
	}
	/**
	 * Create a Edge in the Supertree linking the cards
	 * 
	 * @param parent parent node of the edge
	 * @param child child node of the edge
	 */
	public Edge(Vertex parent, Vertex child){
		this.parent = parent;
		this.child = child;
	}
	
	/**
	 * Set the move which is made between the parent and the child node.
	 * 
	 * @param move type of move (0=take card, 1=toss chip)
	 */
	public void setMove(int move){
		if(move == 0 || move == 1) this.move = move;
	}
	
	/**
	 * Returns the move which is made between the parent and the child node.
	 * 
	 * @return int move type of move (0=take card, 1=toss chip)
	 */
	public int getMove(){
		return move;
	}
	
	/**
	 * Set the child vertex 
	 * 
	 * @param child child node
	 */
	public void setChild(Vertex child){
		this.child = child;
	}
	
	/**
	 * Get the child node
	 * 
	 * @return child node of the edge
	 */
	public Vertex getChild(){
		return child;
	}
	
	/**
	 * set the parent node
	 * 
	 * @param parent parent node
	 */
	public void setParent(Vertex parent){
		this.parent = parent;
	}
	
	/**
	 * Returns the parent node
	 * 
	 * @return parent node
	 */
	public Vertex getParent(){
		return parent;
	}
}
