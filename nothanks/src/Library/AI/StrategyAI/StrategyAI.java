package Library.AI.StrategyAI;

import Library.*;
import java.util.ArrayList;

/**
 * Created by sandersalahmadibapost on 30/11/2017.
 */
public class StrategyAI implements Library.AI.AIInterface
{

    private Board board;
    private ArrayList<Card> cards;
    private int takeThreshold = 8;

    //StrategyAI constructor
    public StrategyAI(Board board)
    {
        this.board = board;
        cards = board.getCurrentPlayer().getCards();
    }

    //method required by AI interface, returns true if the AI has determined to take the card and false if it has determined to toss a chip
    public boolean GetMove()
    {
        boolean takeCard = false;

        if(extendsSequence())
        {

           if(playersOutOfChips())
           {
               takeCard = true;
           }

           if(canAlsoExtend())
           {
               takeCard = true;
           }

        }

        if(lowCard())
        {
            takeCard = true;
        }
        return takeCard;
    }

    //method to check whether the current card can extend the sequence in the AI's current cards
    private boolean extendsSequence()
    {
        boolean extend = false;

        for(int i = 0; i < cards.size() ; i++)
        {
            int myCard = cards.get(i).getNumber();
            int deckCard = board.getCurrentCard().getNumber();

            if(myCard + 1 == deckCard || myCard - 1 == deckCard)
            {

                extend = true;
            }

        }

        return extend;
    }

    //method to check whether there is a player that is out of chips
    private boolean playersOutOfChips()
    {
        boolean outOfChips = false;

        int currentPlayerID = board.getCurrentPlayer().getID();

        for(int i = 0; i < board.getPlayers().size() ; i++)
        {
            if(board.getPlayers().get(i).getChips() == 0 && currentPlayerID != board.getPlayers().get(i).getID())
            {
                outOfChips = true;
            }
        }

        return outOfChips;
    }

    //method to check whether the current card is a 'low' one
    private boolean lowCard()
    {
        boolean lowCard = false;

        if(board.getCurrentCard().getNumber() < takeThreshold)
        {
            lowCard = true;
        }

        return lowCard;
    }

    //method to check whether or not anybody else can also extend any of their sequences using the current card
    private boolean canAlsoExtend()
    {
        boolean canExtend = false;

        for(int i = 0; i < board.getPlayers().size(); i++)
        {
            for (int j = 0; j < board.getPlayers().get(i).getCards().size(); j++)
            {
                if (board.getPlayers().get(i).getID() != board.getCurrentPlayer().getID())
                {
                    if (board.getPlayers().get(i).getCards().get(j).getNumber() + 1 == board.getCurrentCard().getNumber() || board.getPlayers().get(i).getCards().get(j).getNumber() - 1 == board.getCurrentCard().getNumber())
                    {
                        canExtend = true;
                    }
                }
            }
        }

        return canExtend;
    }

    public void gameIsFinished(ArrayList<Player> winner)
    {

    }

}
