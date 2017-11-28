package Library.AI.MLR;

import Library.Board;
import Library.Card;
import Library.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Logger {

    private static String dataPath = null;
    private static Logger logger = null;

    private Logger() {
        dataPath = System.getProperty("user.dir") + "\\Data\\MLR\\logs.txt";
        System.out.println("Logging to file: " + dataPath);
    }

    /**
     * Log MLR training record
     * Format: bPickedCard, CardNumber, NumChipsOnCard, ScoreDiff, bFitsInSeries, bFitsInSeriesAlmost, NumOwnedChips, NumHighCardsLeft, NumMediumCardsLeft, NumLowCardsLeft,
     *
     * @param pickedCard bool
     */
    public static void logMlrGameProgress(boolean pickedCard) {
        if (logger == null) logger = new Logger();
        Board board = Board.getInstance();
        String csvProgress = "";

        // Classifier
        if (pickedCard) csvProgress += "1,";
        else csvProgress += "0,";

        // Current card num
        Card currentCard = board.getCurrentCard();
        csvProgress += currentCard.getNumber() + ",";

        // Current chips on card
        int currentChips = board.getCurrentChips();
        csvProgress += currentChips + ",";

        // Score diff between players?
        // Hardcode other player id..
        int otherPlayerId = 0;
        if (board.getCurrentPlayer().getID() == 1) otherPlayerId = 1;
        int diff = board.getCurrentPlayer().getScore() - board.getPlayers().get(otherPlayerId).getScore();
        csvProgress += diff + ",";

        // Fits in series
        List<Card> owned = board.getCurrentPlayer().getCards();
        boolean fitsInSeries = false;
        for(int i = 1; i < owned.size(); i++) {
            if (currentCard.getNumber() == owned.get(i).getNumber()+1 ||
                    currentCard.getNumber() == owned.get(i).getNumber()-1) {
                fitsInSeries = true;
            }
        }
        if (fitsInSeries) csvProgress += "1,";
        else csvProgress += "0,";

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
        if (fitsInSeriesAlmost) csvProgress += "1,";
        else csvProgress += "0,";

        //TODO: fits in opponents series?
        //TODO: distance to nearest fit?

        // Cards left
        List<Card> cardsLeft  = board.getCurrentDeck();
        int numLow = 0;
        int numMedium = 0;
        int numHigh = 0;
        for(int i = 1; i < cardsLeft.size(); i++) {
            if (cardsLeft.get(i).getNumber() < 15) numLow++;
            else if (cardsLeft.get(i).getNumber() > 25) numHigh++;
            else numMedium++;
        }
        csvProgress += numLow + ",";
        csvProgress += numMedium + ",";
        csvProgress += numHigh + ",";

        // Number of owned chips
        csvProgress += board.getCurrentPlayer().getChips() + ",";

        // TODO: number owned cards cards in range ??

        // Strip last comma
        csvProgress = csvProgress.substring(0, csvProgress.length()-1);
        System.out.println("Logged MLR record: " + csvProgress);

        write(csvProgress);
    }

    private static void write(String message) {
        if (dataPath != null) {
            try {
                FileWriter fw = new FileWriter(dataPath, true);
                BufferedWriter writer = new BufferedWriter(fw);
                writer.write(message);
                writer.newLine();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
