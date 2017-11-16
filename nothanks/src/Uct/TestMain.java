package Uct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestMain {

	public static void main(String[] args) throws IOException {
		//Excel Formula: =WENN(C4="Won";((B3/100+1)/A4)*100;((B3/100)/A4)*100)
		Node tempNode = new Node();
		tempNode.setCardValue(-1);
		Tree temp = new Tree();
		String preOrder = temp.preOrder(temp.getRoot());
		System.out.println("PreOrder: " + preOrder);
		temp.save();
	}
}
