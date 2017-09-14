package Library;

/**
 * 
 * @author 
 *
 * A class representing the cards
*/
public class Card {
	
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
}
