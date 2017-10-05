package Gui;

import Gui.Dialog.RulesDialog;
import Gui.Dialog.WinnerDialog;
import Gui.Graphics.CardGfx;
import Gui.Graphics.ChipGfx;
import Library.Board;
import Library.Card;
import Library.Player;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Integer.valueOf;

public class Controller implements Initializable {

    // Main grid
    @FXML private GridPane grid;

    // Labels
    @FXML private Label scores;
    @FXML private Label currentPlayerLbl;

    // Inputs
    @FXML private Button takeCardBtn;
    @FXML private Button tossChipBtn;
    @FXML private Button rulesBtn;

    // Graphics
    @FXML private ImageView deckImg;
    @FXML private ImageView activeCardImg;
    @FXML private ImageView activeChipImg;
    @FXML private ImageView playerDeckImg;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        // init method is run when fxml is finished loading
        updateInterface();

        // init graphics
        try {
            // todo: move to DeckGfx
            Image deckImgSource = SwingFXUtils.toFXImage(ImageIO.read(getClass().getResource("Images/deck.png")), null);
            deckImg.setImage(deckImgSource);

            Image chipImgSource = SwingFXUtils.toFXImage(ChipGfx.getInstance().getChipGfx(0), null);
            activeChipImg.setImage(chipImgSource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        flipNewCard(valueOf(Board.getInstance().getCurrentCard().getNumber()));
    }

    @FXML protected void rules(ActionEvent event) {
        new RulesDialog((Stage) rulesBtn.getScene().getWindow());
    }

    @FXML protected void quit(ActionEvent event) {
        Platform.exit();
    }

    @FXML protected void takeCard(ActionEvent event) {
        Board.getInstance().giveCardChips();
        updateInterface();
        flipNewCard(valueOf(Board.getInstance().getCurrentCard().getNumber()));
    }
    
    @FXML protected void tossChip(ActionEvent event) {
    	Board.getInstance().tossChip();
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
                Image newCardImgSource = SwingFXUtils.toFXImage(CardGfx.getInstance().getFlippedCard(cardNumber), null);
                activeCardImg.setImage(cardImgSource);

                // Rotate back of card
                RotateTransition rotator = new RotateTransition(Duration.millis(600), activeCardImg);
                rotator.setAxis(Rotate.Y_AXIS);
                rotator.setFromAngle(0);
                rotator.setToAngle(90);
                rotator.setInterpolator(Interpolator.LINEAR);
                rotator.setCycleCount(1);
                rotator.play();

                // Switch image source at halfway point
                Thread.sleep(600);
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
            takeCardBtn.setDisable(true);
            tossChipBtn.setDisable(true);
            currentPlayerLbl.setVisible(false);
            playerDeckImg.setVisible(false);

            // Call winner dialog
            updateScoreboard();
            new WinnerDialog((Stage) takeCardBtn.getScene().getWindow());

            // Reset game when winnerdialog returns
            Board.reset();
            updateInterface();
        } else {
            takeCardBtn.setDisable(false);
            tossChipBtn.setDisable(false);
            currentPlayerLbl.setVisible(true);
            playerDeckImg.setVisible(true);

            Image chipImgSource = SwingFXUtils.toFXImage(ChipGfx.getInstance().getChipGfx(Board.getInstance().getCurrentChips()), null);
            activeChipImg.setImage(chipImgSource);
        }
    }

    private void updateCurrentPlayer() {
        currentPlayerLbl.setText("Player " + Board.getInstance().getCurrentPlayer().getID() + "'s turn:");
        currentPlayerLbl.setStyle("-fx-font-size: 24px; -fx-fill: #3b7b84;");

        Integer playerChips = Board.getInstance().getCurrentPlayer().getChips();
        if (playerChips <= 0) {
            tossChipBtn.setDisable(true);
        } else tossChipBtn.setDisable(false);

        // Load deck graphics
        BufferedImage deck = CardGfx.getInstance().renderPlayerDeck(Board.getInstance().getCurrentPlayer().getCards(), playerChips);
        if (deck != null) {
            Image playerDeck = SwingFXUtils.toFXImage(deck, null);
            playerDeckImg.setImage(playerDeck);
        } else {
            playerDeckImg.setImage(null);
        }
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
        scores.setStyle("-fx-font-size: 20px; -fx-fill: #4ebec6;");
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
