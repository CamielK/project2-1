package Library;

import java.util.ArrayList;
import java.util.Collections;


public class Deck extends ArrayList<Card> {

    public Deck() {
        for(int i = 3; i <= 35; i++) {
        	this.add(new Card(i));
        }
    }

    private void shuffleCards(){
        Collections.shuffle(this);
    }

    private void printCards(){
        for (int i = 0; i <= 32; i++) {
            System.out.print(this.get(i) + " ");
        }
    }
}
