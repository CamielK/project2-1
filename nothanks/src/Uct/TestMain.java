package Uct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestMain {

	public static void main(String[] args) throws IOException {
		
		Node tempNode = new Node();
		tempNode.setCardValue(-1);
		Tree temp = new Tree(tempNode);
		//temp = new Tree().load();
		String preOrder = temp.preOrder(temp.getRoot());
		System.out.println("PreOrder: " + preOrder);
		temp.save();
		Node root = temp.getRoot();
		Node c1 = root.getChildren().get(1);
		Node c2 = c1.getChildren().get(0);
		System.out.println();
		System.out.println(c1.getCardValue() + " " + c1.takeCard());
		System.out.println(c2.getCardValue() + " " + c2.takeCard());
	}
}
