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

public class TS_OM implements AIInterface{
    private File f;
    FileWriter fileWriter;
    //   private double[] valuess=null;


    private double lower=0.10987012987012985;
    private double upper=0.256;
//    private double lower = 0.12251695;
//    private double upper = 0.4056314;
    private double Pmid=0.34793506493506493;

    double incr = 0.003;   //The incremental value for adapting the threshold , learning rate
    private double surd = 0.05;     // % to make an irrational move
    private boolean valuableCard = false;    // True if the current card is valuable for the opponent
    private Card preCard;  //the card of the last turn

    @Override
    public boolean GetMove() {


        //Initialization of the variables
        ArrayList<Player> Playerlist = new ArrayList<>();
        for (int i = 0; i < Board.getInstance().getPlayers().size(); i++) {
            Playerlist.add(Board.getInstance().getPlayers().get(i));
        }
        double[] values= new double[Playerlist.size()];
        Card card = Board.getInstance().getCurrentCard();
        Player P0 = Board.getInstance().getCurrentPlayer();
        int cardnum =card.getNumber();
        int Pnum= P0.getID();
        int chips= Board.getInstance().getCurrentChips();

        //Rearranges the Player_ID order according to the current player
        if(Pnum!=0) {
            for(int i=0;i<Pnum-1;i++) {
                Player X = Playerlist.remove(0); 	// removes element at I
                Playerlist.add(X);        				// adds element to end of list
            }
        }

        //Calculate the value of the card for each player
        for(int i=0;i<values.length;i++) {                // needs to be fixed to find the value for each player, need to make order of players from 0 back to 0
            Player playnum = Playerlist.get(i);
            ArrayList<Card> Pcards = playnum.getCards();
            int[] Pcard = new int[Pcards.size()];
            for(int k=0; k<Pcard.length;k++) {            // to get the number of the cards that is used to evaluate the player likelihood
                Pcard[k]=Pcards.get(k).getNumber();
            }
            int Pchips=playnum.getChips();
            values[i]=valueP(cardnum,chips,Pcard,Pchips);
            //System.out.println("cardsnum:"+cardnum+" chips:"+chips+"Pchips"+Pchips);
        }
//	this.valuess=values;   //FIXME Necessary?

        ////////////////////////////////////////////////////////////////////////////////////////////
        //Debugg
        String pvalues=Arrays.toString(values);
        //System.out.println(pvalues);
        try {
            fileWrite(pvalues);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

        if (valuableCard && (preCard == card)){   //If prediction is wrong
            Pmid += incr + ((Math.random()/1000));
        }
        if (!valuableCard && (preCard != card)){
            Pmid -= (incr + (Math.random()/1000));
        }

        //Decide to either take the card or toss a chip
        if(values[0]<lower) {
            return false;
        } else
        if(values[0]>upper) {
            return true;
        } else
        if(values[0] >= lower && values[0]<=upper) {    //redundant if statement?

            //Checks if card is valuable for opponents
            for(int i=1; i<values.length;i++) {
                if(values[i]>Pmid) {    //If valuable card
                    //Adapts Pmid, according to the predicted move being true or false

                    valuableCard = true;

                    if (Math.random() > surd) {
                        valuableCard = false;
                        return true;
                    } else {
                        preCard = card;
                        return false;
                    }
                }
            }

            valuableCard = false;
            preCard = card;
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



    public double getPmid(){
        return Pmid;
    }



}