package Uct;

import Helper.Config;

import java.io.IOException;

public class TestMain {

	public static void main(String[] args) throws IOException {

		new Config();
		//Excel Formula: =WENN(C4="Won";((B3/100+1)/A4)*100;((B3/100)/A4)*100)
		Tree temp = new Tree(2).load();
		//temp.resetAllTrees();
		String preOrder = temp.preOrder(temp.getRoot());
		//System.out.println("PreOrder: " + preOrder);
		temp.save();
	}
}
