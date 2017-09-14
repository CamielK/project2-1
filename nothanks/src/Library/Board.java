package Library;

import java.util.ArrayList;

public class Board {
	
	private static Board board;

    private int currentCard = 0;
    private int currentChips = 0;
    private Deck cardDeck = null;

    private Board() {
        cardDeck = new Deck();
    }
    
    public static Board getInstance() {
		if (board == null) {
			return (board = new Board());
		} else {
			return board;
		}
	}

}
