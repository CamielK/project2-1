package TS;

import Helper.Config;
import Library.AI.AIInterface;
import Library.Board;
import Library.Card;
import Library.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TS implements AIInterface{
    private File f;
    FileWriter fileWriter;
    private double[] valuess=null;
	
    
	private double lower=0.07;
	private double upper=0.09;
	private double Pmid=0.08;

	
@Override
public boolean GetMove() {
	//if(valuess!=null) {
	//OPmoves();}
	ArrayList<Player> Playerlist = Board.getInstance().getPlayers();
	double[] values= new double[Playerlist.size()];
	System.out.println("TS move");
	Card card = Board.getInstance().getCurrentCard();
	int cardnum =card.getNumber();
	Player P0 = Board.getInstance().getCurrentPlayer();
	
	int Pnum= P0.getID();
	if(Pnum!=0) {
	for(int i=0;i<Pnum-1;i++) {
		Player X = Playerlist.remove(i); // removes element at I
		Playerlist.add(X);        // adds element to end of list
	}}
	
	int chips= Board.getInstance().getCurrentChips();
	
	for(int i=0;i<values.length;i++) {// needs to be fixed to find the value for each player, need to make order of players from 0 back to 0
		Player playnum = Playerlist.get(i);
		ArrayList<Card> Pcards = playnum.getCards();
		int[] Pcard = new int[Pcards.size()];
		 for(int k=0; k<Pcard.length;k++) {
			 Pcard[k]=Pcards.get(k).getNumber();
		 } // to get the number of the cards that is used to evaluate the player likelihood
		int Pchips=playnum.getChips();
		values[i]=valueP(cardnum,chips,Pcard,Pchips);
		//System.out.println("cardsnum:"+cardnum+" chips:"+chips+"Pchips"+Pchips);
	}
	this.valuess=values;
	String pvalues=Arrays.toString(values);
	//System.out.println(pvalues);
	try {
		fileWrite(pvalues);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(values[0]<lower) {
		return false;
	}
	else if(values[0]>upper) {
		return true;
	}
	else if(values[0] >= lower && values[0]<=upper) {
		for(int i=1; i<values.length;i++) {
			if(values[i]>Pmid) {
				return true;
			}
		}
		return false;
	}
	throw new RuntimeException("this part of the code should not run :)");
}


// These numbers are arbitrary and subject to change when with adjustment.
public double valueP (int card, int chips, int[] Pcards, int Pchips) {
	double CenterValue=(double)(chips+1)/card;
	//System.out.println("CenterVAlue:" +CenterValue);
	double OwnedCard=1;
		for (int i=0;i<Pcards.length;i++) {
			if(Pcards[i]==card+1) {
				OwnedCard=OwnedCard +1;//would decrease the overall score of the card by 1 so quite valuable
			}
			if(Pcards[i]==card-1) {
				OwnedCard=OwnedCard +0.5;// less valuable to take a card after
			}
		}
		//System.out.println("OwnedCards:" +OwnedCard);
	double OwnedChips;
	if(Pchips==0) {
		OwnedChips=100;}
	else {
		OwnedChips= Math.pow(Pchips, -1)+1;// +1 since it is multiplied. so larger X leads to no influence on Value
	}
	//System.out.println("OwnedChips:" +OwnedChips);
	double Value =CenterValue*OwnedCard*OwnedChips;
	//System.out.println("values"+Value);
	return Value;
	}


private void fileWrite(String info) throws IOException {
  	f= new File(Config.logpath+"/Data/TSlogs.txt");
		f.createNewFile();
		fileWriter = new FileWriter(f,true);
		fileWriter.write(info+"\n");
		fileWriter.close();		
}

@Override
public void gameIsFinished(ArrayList<Player> winner) {
	// TODO Auto-generated method stub
	
}

public void OPmoves() {
	int[] took =Board.getInstance().takes();
	int[] taken = new int[valuess.length];
	int a=0;
	for (int i=(took.length-valuess.length-1);i<took.length;i++) {
		taken[a]=took[i];		
	}
	int wrong=0;
	for(int j =1;j<taken.length;j++) {
		if(valuess[j]>Pmid && taken[j]==0 && valuess[0]<lower) {
			wrong++;
		}
	}
	if(wrong!=0) {
		//change value of Pmid
		this.Pmid=Pmid-0.005;
		System.out.println("Pmid has increased to "+Pmid);
	}
	
	
	
}






		
}
