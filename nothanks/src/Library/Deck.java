package Library;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author 
 *
 * Class containing the Deck
 */
public class Deck extends ArrayList<Card> {

    /**
     * Construct the Deck and initialize the Cards from 3-35
     */
    public Deck() {
        for(int i = 35; i >= 3; i--) {
        
        	this.add(new Card(i));
        
        }
        
        shuffleCards();
        //printCards();
        //sort();
        //System.out.println();
        //printCards();
        removeCards(9);
    }

    /**
     * Shuffles the cards in the deck
     */
    private void shuffleCards(){
        Collections.shuffle(this);  
    }
    
    /**
     * Prints out the cards contained in the deck in ascending index order
     */
    private void printCards(){
        for (int i = 0; i < this.size(); i++) {
            System.out.print(this.get(i).getNumber() + ", ");
        }
        System.out.println("");
    }
    
    /**
     * Mock sort algorithm
     */
    public void sort() {
		int temp;
		for(int i = 1; i < this.size(); i++) {
			for(int j = 0; j < this.size() - i; j++) {
				if(this.get(j).getNumber() > this.get(j + 1).getNumber()) {
					temp=this.get(j).getNumber();
					this.get(j).setNumber(this.get(j + 1).getNumber());
					this.get(j + 1).setNumber(temp);
				}
			}
		}
	}

	public int getNumCards() {
        return this.size();
    }

    public Card removeCards(int number) {
    	if(this.size() == 1) {
    		Card lastCard = this.get(0);
    		this.remove(0);
    		return lastCard;
    	}
    	
    	for(int i = 0; i < number; i++) {
    		this.remove(i);
    	}
    	
    	return this.get(0);
    }
}
