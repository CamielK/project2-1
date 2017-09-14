package Library;

import java.util.ArrayList;
import java.util.Collections;


public class Deck extends ArrayList<Card> {

    /**
     * Construct the Deck and initialise the Cards from 3-35
     */
    public Deck() {
        for(int i = 3; i <= 35; i++) {
        	this.add(new Card(i));
        }
    }

    /**
     * shuffels the cards in the deck
     */
    private void shuffleCards(){
        Collections.shuffle(this);
    }

    /**
     * prints out the cards contained in the deck in ascending index order
     */
    private void printCards(){
        for (int i = 0; i <= 32; i++) {
            System.out.print(this.get(i) + " ");
        }
    }
}
