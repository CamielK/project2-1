package Library;

/**
 * 
 * @author 
 *
 * A class representing the cards
*/
public class Card implements Comparable<Card> {
	
	private int number;
	
	public Card(int number) {
		this.number = number;
	}
	
	/**
	 * returns the number of the card
	 * @return value of the card
	 */
	public int getNumber(){
		return number;
	}
	
	/**
	 * Set
	 * @param value of the card
	 */
	public void setNumber(int number){
		this.number = number;
	}

	public int compareTo(Card cardToCompare)
	{	
		if(this.getNumber() > cardToCompare.getNumber())
			return 1;
		else if(this.getNumber() < cardToCompare.getNumber())
			return -1;
		else
			return 0;
	}
}
