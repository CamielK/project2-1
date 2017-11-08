package TS;

import java.util.ArrayList;

import Library.Board;
import Library.Card;
import Library.Player;

public class TS {

public boolean getMove(int Player) {
	int Pnumber=2;
	int[] values =new int[Pnumber+1];
	Card card = Board.getInstance().getCurrentCard();
	int cardnum =card.getNumber();
	Player P0 = Board.getInstance().getCurrentPlayer();
	int chips= Board.getInstance().getCurrentChips();
	for(int i=0;i<values.length;i++) {// needs to be fixed to find the value for each player, need to make order of players from 0 back to 0
		ArrayList<Card> Pcards = P0.getCards();
		int[] Pcard = new int[Pcards.size()];
		 for(int k=0; k<Pcard.length;k++) {
			 Pcard[k]=Pcards.get(k).getNumber();
		 } // to get the number of the cards that is used to evaluate the player likelihood
		int Pchips=P0.getChips();
		values[i]=valueP(cardnum,chips,Pcard,Pchips);
	}
	
	
}




// These numbers are arbitrary and subject to change when with adjustment. 
public double valueP (int card, int chips, int[] Pcards, int Pchips) {
	int CenterValue = chips/card;
	double OwnedCard=0;
		for (int i=0;i<Pcards.length;i++) {
			if(Pcards[i]==card+1) {
				OwnedCard=OwnedCard +2;//would decrease the overall score of the card by 1 so quite valuable
			}
			if(Pcards[i]==card-1) {
				OwnedCard=OwnedCard +1.5;// less valuable to take a card after
			}
		}
	double OwnedChips = Math.pow(Pchips, -1)+1;// +1 since it is multiplied. so larger X leads to no influence on Value
	double Value =CenterValue*OwnedCard*OwnedChips;
	return Value;		
	}


		
}
