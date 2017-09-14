package Library;

import java.util.ArrayList;

public class Deck {

    private static ArrayList<Integer> Cards = new ArrayList<Integer>(32);
    
    Deck() {
        for(int i = 3; i <= 35; i++) {
            Cards.add(i);
        }
    }
}
