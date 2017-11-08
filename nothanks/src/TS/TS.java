package TS;

public class TS {

public boolean getMove(int Player) {
	
	
}


public double valueP (int card, int chips, int[] Pcards, int Pchips) {
	int CenterValue = chips/card;
	double OwnedCard=0;
		for (int i=0;i<Pcards.length;i++) {
			if(Pcards[i]==card+1) {
				OwnedCard=OwnedCard +2;
			}
			if(Pcards[i]==card-1) {
				OwnedCard=OwnedCard +1.5;
			}
		}
	double OwnedChips = Math.pow(Pchips, -1)+1;
	double Value =CenterValue*OwnedCard*OwnedChips;
	return Value;		
	}


		
}
