package Gui;

import Gui.Dialog.RulesDialog;
import Helper.CardSpriteReader;
import Library.Board;
import Library.Card;
import Library.Player;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Integer.valueOf;

public class Controller implements Initializable {

    // Main grid
    @FXML private GridPane grid;
    @FXML private VBox currentPlayerBox;

    // Labels
    @FXML private Label currentCardLbl;
    @FXML private Label currentChipsLbl;
    @FXML private Label scores;
    @FXML private Label currentPlayerCards;
    @FXML private Label currentPlayerChips;
    @FXML private Label currentPlayerLbl;
    @FXML private Label cardsLeftLbl;
    @FXML private Label resultsLbl;

    // Inputs
    @FXML private Button takeCardBtn;
    @FXML private Button tossChipBtn;
    @FXML private Button resetBtn;
    @FXML private Button rulesBtn;

    // Graphics
    @FXML private ImageView deckImg;
    @FXML private ImageView activeCardImg;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        // init method is run when fxml is finished loading
        updateInterface();

        // init graphics
        try {
            Image deckImgSource = SwingFXUtils.toFXImage(ImageIO.read(getClass().getResource("Images/deck.png")), null);
            deckImg.setImage(deckImgSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML protected void rules(ActionEvent event) {
        new RulesDialog((Stage) rulesBtn.getScene().getWindow());
    }

    @FXML protected void takeCard(ActionEvent event) {
        Board.getInstance().giveCardChips();
        updateInterface();
    }
    
    @FXML protected void tossChip(ActionEvent event) {
    	Board.getInstance().tossChip();
    	updateInterface();
    }

    @FXML protected void reset(ActionEvent event) {
    	Board.reset();
    	updateInterface();
    }

    private void updateInterface() {
        updateCurrentPlayer();
        updateCurrentCardAndChips();
        updateScoreboard();
    }

    // remove old card to player and flip a new card
    private void flipNewCard(Integer cardNumber) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            try {
                // Load image sources
                Image cardImgSource = SwingFXUtils.toFXImage(ImageIO.read(getClass().getResource("Images/back_flipped.png")), null);
                Image newCardImgSource = SwingFXUtils.toFXImage(CardSpriteReader.getInstance().getFlippedCard(cardNumber), null);
                activeCardImg.setImage(cardImgSource);

                // Rotate back of card
                RotateTransition rotator = new RotateTransition(Duration.millis(800), activeCardImg);
                rotator.setAxis(Rotate.Y_AXIS);
                rotator.setFromAngle(0);
                rotator.setToAngle(90);
                rotator.setInterpolator(Interpolator.LINEAR);
                rotator.setCycleCount(1);
                rotator.play();

                // Switch image source at halfway point
                Thread.sleep(800);
                activeCardImg.setImage(newCardImgSource);

                // Finish rotation
                RotateTransition rotatorFront = new RotateTransition(Duration.millis(800), activeCardImg);
                rotatorFront.setAxis(Rotate.Y_AXIS);
                rotatorFront.setFromAngle(90);
                rotatorFront.setToAngle(0);
                rotatorFront.setInterpolator(Interpolator.LINEAR);
                rotatorFront.setCycleCount(1);
                rotatorFront.play();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
            }
        };
        new Thread(sleeper).start();

    }

    private void updateCurrentCardAndChips() {
        if (Board.getInstance().getIsFinished()) {
            cardsLeftLbl.setText("Game is finished!");
            currentCardLbl.setText("");
            currentChipsLbl.setText("");
            takeCardBtn.setDisable(true);
            tossChipBtn.setDisable(true);
            currentPlayerBox.setVisible(false);
            resetBtn.setVisible(true);
            resultsLbl.setText(Board.getInstance().getWinners());
        } else {
            takeCardBtn.setDisable(false);
            tossChipBtn.setDisable(false);
            currentPlayerBox.setVisible(true);
            resetBtn.setVisible(false);
            resultsLbl.setText("");
            cardsLeftLbl.setText("Cards left: " + String.valueOf(Board.getInstance().getNumCardsLeft()));
            currentCardLbl.setText("Current card: " + String.valueOf(Board.getInstance().getCurrentCard().getNumber()));
            currentChipsLbl.setText("Current chips: " + String.valueOf(Board.getInstance().getCurrentChips()));

            flipNewCard(valueOf(Board.getInstance().getCurrentCard().getNumber()));
        }
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
