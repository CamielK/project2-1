package Uct;

public class TestMain {

	public static void main(String[] args) {
		
		Node tempNode = new Node();
		tempNode.setCardValue(-1);
		Tree temp = new Tree().load();
		//Tree temp = new Tree(tempNode);
		String preOrder = temp.preOrder(temp.getRoot());
		System.out.println("PreOrder: " + preOrder);
		String ai = "Player ";
		int win = 5;
		ai += win;
		System.out.println(ai);
		
		//temp.save();
	}
}
