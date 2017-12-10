package Library.AI.MLR;

import Library.AI.AIInterface;
import Library.Board;
import Library.Card;
import Library.Player;

import java.util.ArrayList;
import java.util.List;

public class MultipleLinearRegressionAI implements AIInterface{

    @Override
    public boolean GetMove() {
        Board board = Board.getInstance();

        // Current card num
        Card currentCard = board.getCurrentCard();
        int currentCardNum = currentCard.getNumber();

        // Current chips on card
        int currentChips = board.getCurrentChips();
        int currentChipsNum = currentChips;

        // Fits in series
        List<Card> owned = board.getCurrentPlayer().getCards();
        boolean fitsInSeries = false;
        for(int i = 1; i < owned.size(); i++) {
            if (currentCard.getNumber() == owned.get(i).getNumber()+1 ||
                    currentCard.getNumber() == owned.get(i).getNumber()-1) {
                fitsInSeries = true;
            }
        }

        // Almost fits in series
        //      (card either fits or is a close fit: e.g. another card would connect the current card with the owned series
        //      and is a good fit because a player might expect to complete this series in the future)
        boolean fitsInSeriesAlmost = false;
        for(int i = 1; i < owned.size(); i++) {
            if (currentCard.getNumber() == owned.get(i).getNumber()+1 ||
                    currentCard.getNumber() == owned.get(i).getNumber()-1 ||
                    currentCard.getNumber() == owned.get(i).getNumber()+2 ||
                    currentCard.getNumber() == owned.get(i).getNumber()-2) {
                fitsInSeriesAlmost = true;
            }
        }

        // Cards left
        List<Card> cardsLeft  = board.getCurrentDeck();
        int numLowCardsLeft = 0;
        int numMediumCardsLeft = 0;
        int numHighCardsLeft = 0;
        for(int i = 1; i < cardsLeft.size(); i++) {
            if (cardsLeft.get(i).getNumber() < 15) numLowCardsLeft++;
            else if (cardsLeft.get(i).getNumber() > 25) numHighCardsLeft++;
            else numMediumCardsLeft++;
        }


        // Apply MLR model
        // Note: the following model was created using Weka Linear Regression.
        // The model was build on training data gathered for this purpose. The training data consists of human plays.
//        System.out.println("currentCardNum = " + currentCardNum);
//        System.out.println("currentChipsNum = " + currentChipsNum);
//        System.out.println("fitsInSeries = " + fitsInSeries);
//        System.out.println("fitsInSeriesAlmost = " + fitsInSeriesAlmost);
//        System.out.println("numHighCardsLeft = " + numHighCardsLeft);
//        System.out.println("numMediumCardsLeft = " + numMediumCardsLeft);
        double pickCard =
                (-0.0133    * currentCardNum) +
                (-0.1662    * currentChipsNum) +
                (0.1779     * ((fitsInSeries) ? 1 : 0)) +
                (0.477      * ((fitsInSeriesAlmost) ? 1 : 0)) +
                (0.0262     * numHighCardsLeft) +
                (0.0417     * numMediumCardsLeft) +
                0.4805;

//        System.out.println("pickCard = " + pickCard);
        return pickCard > 0.6;
    }

    @Override
    public void gameIsFinished(ArrayList<Player> winner) {

    }
}
