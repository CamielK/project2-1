package Gui;

import Library.Board;
import Library.Card;
import Library.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Main grid
    @FXML private GridPane grid;

    // Labels
    @FXML private Label currentCardLbl;
    @FXML private Label currentChipsLbl;
    @FXML private Label scores;
    @FXML private Label currentPlayerCards;
    @FXML private Label currentPlayerChips;
    @FXML private Label currentPlayerLbl;
    @FXML private Label cardsLeftLbl;

    // Inputs
    @FXML private Button takeCardBtn;
    @FXML private Button tossChipBtn;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        // init method is run when fxml is finished loading
        updateInterface();
    }

    @FXML protected void takeCard(ActionEvent event) {
        Board.getInstance().giveCardChips();
        updateInterface();
    }
    
    @FXML protected void tossChip(ActionEvent event) {
    	Board.getInstance().tossChip();
    	updateInterface();
    }

    private void updateInterface() {
        updateCurrentCardAndChips();
        updateScoreboard();
        updateCurrentPlayer();
    }

    private void updateCurrentCardAndChips() {
        cardsLeftLbl.setText("Cards left: " + String.valueOf(Board.getInstance().getNumCardsLeft()));
        currentCardLbl.setText("Current card: " + String.valueOf(Board.getInstance().getCurrentCard().getNumber()));
        currentChipsLbl.setText("Current chips: " + String.valueOf(Board.getInstance().getCurrentChips()));
    }

    private void updateCurrentPlayer() {
        currentPlayerLbl.setText("Player " + Board.getInstance().getCurrentPlayer().getID() + "'s turn:");
        currentPlayerCards.setText("Your cards: [" + showCards(Board.getInstance().getCurrentPlayer().getCards()) + "]");

        Integer playerChips = Board.getInstance().getCurrentPlayer().getChips();
        if (playerChips <= 0) {
            tossChipBtn.setDisable(true);
        } else tossChipBtn.setDisable(false);
        currentPlayerChips.setText("Current chips: " + playerChips);
    }

    private void updateScoreboard() {
        ArrayList<Player> players = Board.getInstance().getPlayers();
        StringBuilder scoreboard = new StringBuilder();
        for (int i=0; i < players.size(); i++) {
            scoreboard.append("Player " +  players.get(i).getID() + ". Score: " + players.get(i).getScore());
            scoreboard.append(". Cards: ["+showCards(players.get(i).getCards())+"]");
            scoreboard.append("\n");
        }
        scores.setText(scoreboard.toString());
    }

    private String showCards(ArrayList<Card> cards){
        String cardString = "";
        for (int i = 0; i < cards.size(); i++) {
            cardString += cards.get(i).getNumber();
            if (i<cards.size()-1) {
                cardString += ", ";
            }
        }
        return cardString;
    }
}
