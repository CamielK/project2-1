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
    private int takeThreshold = 10;
    private int lowChipsThreshold = 15;
    private boolean debug = false;
    
    /* Rule 1:
     * 
	 * If the current card extends AI's series
	 *	- If someone has no chips => take the card
	 *	- Check if someone's series would also be extended by the card => take the card
	 */
    
    /* Rule 2:
     * 
	 * If someone can extend their series and after taking the card the AI's score lead is maximally halved => take the card
	 */
    
    /* Rule 3:
     * 
	 * If the card number is lower than takeThreshold => take the card
	 * If AI's chip are lower than lowChipsThreshold => toss chip
	 * If the added score of taking the card is greater than 19 or there are no chips on the card => toss chip
	 * If no rule applies => take card
	 */
    
    //Change the numbers of the rules around to change the order of the rules executed. Set to false to disable rule
    private static Object[][] rules = {
    		{"Rule1", true},
    		{"Rule2", true},
    		{"Rule3", true},
    };
    
    private static int[] rulesSayTake = new int[rules.length + 1];

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
					if(debug) rulesSayTake[i]++;
					return true;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
    	}
    	
    	if(debug) rulesSayTake[rulesSayTake.length - 1]++;
    	return false;
    }

    public void gameIsFinished(ArrayList<Player> winner) {
    	if(!debug) {
    		return;
    	}
    	
    	for(int i = 0; i < rulesSayTake.length; i ++) {
    		if(i == rulesSayTake.length - 1) {
    			System.out.println("We toss " + rulesSayTake[i] + " times");
    		} else {
    			System.out.println("Rule " + (i + 1) + " said take " + rulesSayTake[i] + " times");
    		}
    	}
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
    	if(canAlsoExtend() && pointsInLead() > board.getCurrentCard().getNumber() - board.getCurrentChips() + Math.round(pointsInLead() / 2)) {
    		return true;
    	}
    	
    	return false;
    }
    
    //Rule 3
    public boolean Rule3() {
    	if(board.getCurrentCard().getNumber() <= takeThreshold) {
    		return true;
    	}
    	
    	if(lowChipsThreshold < player.getChips()) {
    		return false;
    	}
    	
    	if(board.getCurrentCard().getNumber() - board.getCurrentChips() > 19 || board.getCurrentChips() < 1) {
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
            	
                if (player == board.getPlayers().get(i)) {
                	continue;
                }
                
                if (board.getPlayers().get(i).getCards().get(j).getNumber() + 1 == board.getCurrentCard().getNumber() || board.getPlayers().get(i).getCards().get(j).getNumber() - 1 == board.getCurrentCard().getNumber()) {
                	return true;
                }
            }
        }

        return false;
    }
    
    private int pointsInLead() {
    	int lowestScore = 0;
    	boolean first = true;
    	for(int i = 0; i < board.getPlayers().size(); i++) {
    		if(player == board.getPlayers().get(i)) {
    			continue;
    		}
    		
    		if(first) {
    			lowestScore = board.getPlayers().get(i).getScore();
    			first = false;
    		} else if(lowestScore > board.getPlayers().get(i).getScore()){
    			lowestScore = board.getPlayers().get(i).getScore();
    		}
    		
    		return lowestScore - player.getScore();
    	}
    	
    	return lowestScore;
    }
    
    public void Sleep() {
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
