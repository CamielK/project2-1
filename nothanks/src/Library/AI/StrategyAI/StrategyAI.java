package Library.AI.StrategyAI;

import Library.*;

import java.util.ArrayList;

/**
 * Created by sandersalahmadibapost on 30/11/2017.
 */
public class StrategyAI implements Library.AI.AIInterface {

    private Board board;
    private Player player;
    private ArrayList<Card> cards;
    private int takeThreshold = 15;
    private int lowChipsThreshold = 10;
    
    /* Rule 1:
     * 
	 * If the current card extends AI's series
	 *	- If someone has no chips => take the card
	 *	- Check if someone's series would also be extended by the card => take the card
	 */
    
    /* Rule 2:
     * 
	 * If the AI's chips are lower or equals than lowChipsThreshold and the score of the card minus chips is lower than 19
	 */
    
    /* Rule 3:
     * 
	 * If the card number is lower or equal to 10 => take card
	 */
    
    //Change the numbers of the rules around to change the order of the rules executed. Set to false to disable rule
    private Object[][] rules = {
    		{"Rule1", true},
    		{"Rule2", true},
    		{"Rule3", true},
    		//{"Rule4", true},
    };

    //StrategyAI constructor
    public StrategyAI(Board board) {
        this.board = board;
    }

    //method required by AI interface, returns true if the AI has determined to take the card and false if it has determined to toss a chip
    public boolean GetMove() {
    	if(player == null) {
    		player = board.getCurrentPlayer();
    		cards = player.getCards();
    	}
    	
    	for(int i = 0; i < rules.length; i++) {
    		
    		if((boolean)rules[i][1] == false) {
    			continue;
    		}
    		
    		try {
				if((boolean)StrategyAI.class.getMethod((String)rules[i][0]).invoke(this)) {
					return true;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
    	}
    	
    	return false;
    }

    //method to check whether the current card is a 'low' one
    private boolean lowCard() {
        boolean lowCard = false;

        if(board.getCurrentCard().getNumber() < takeThreshold) {
            lowCard = true;
        }

        return lowCard;
    }

    public void gameIsFinished(ArrayList<Player> winner) {
    }
    
    //Rule 1
    public boolean Rule1() {
    	if(extendsSequence()) {
    		
    		if(playersOutOfChips()) {
    			return true;
    		}

    		if(canAlsoExtend()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //Rule 2
    public boolean Rule2() {
    	if(lowChipsThreshold < player.getChips()) {
    		return false;
    	}
    		
    	if(board.getCurrentCard().getNumber() - board.getCurrentChips() > 19 || board.getCurrentChips() < 1) {
			return false;
		}
    	
    	return true;
    }
    
    //Rule 3
    public boolean Rule3() {
    	if(board.getCurrentCard().getNumber() > 10) {
    		return false;
    	}
    	
    	return true;
    }
    
    //method to check whether the current card can extend the sequence in the AI's current cards
    private boolean extendsSequence() {
        for(int i = 0; i < cards.size() ; i++) {
            int myCard = cards.get(i).getNumber();
            int deckCard = board.getCurrentCard().getNumber();

            if(myCard + 1 == deckCard || myCard - 1 == deckCard) {
                return true;
            }
        }
        
        return false;
    }

    //method to check whether there is a player that is out of chips
    private boolean playersOutOfChips() {
        for(int i = 0; i < board.getPlayers().size() ; i++) {
        	
        	if(player == board.getPlayers().get(i)) {
        		continue;
        	}
        	
            if(board.getPlayers().get(i).getChips() == 0) {
            	return true;
            }
        }

        return false;
    }
    
    //method to check whether or not anybody else can also extend any of their sequences using the current card
    private boolean canAlsoExtend() {
        for(int i = 0; i < board.getPlayers().size(); i++) {
            for (int j = 0; j < board.getPlayers().get(i).getCards().size(); j++) {
            	
                if (player == board.getCurrentPlayer()) {
                	continue;
                }
                
                if (board.getPlayers().get(i).getCards().get(j).getNumber() + 1 == board.getCurrentCard().getNumber() || board.getPlayers().get(i).getCards().get(j).getNumber() - 1 == board.getCurrentCard().getNumber()) {
                	return true;
                }
            }
        }

        return false;
    }
}
