package Library;

import java.util.ArrayList;

public class Deck extends ArrayList<Card> {
    
    public Deck() {
        for(int i = 3; i <= 35; i++) {
        	this.add(new Card(i));
        }
    }
}
