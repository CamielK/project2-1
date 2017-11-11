package Uct;

public class UnifierNode extends Node{
	private Node child;
	private Node[] parents;
	private static int id;
	
	public UnifierNode(){
		id++;
	}
	
	@Override
	public void setParent(Node parent){
		System.out.println("Works");
	}
	
}
