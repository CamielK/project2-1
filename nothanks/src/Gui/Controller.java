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

    private static Image cardBackside;
    private static ArrayList<Task<Void>> activeTasks = new ArrayList<Task<Void>>();

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

            cardBackside = SwingFXUtils.toFXImage(ImageIO.read(getClass().getResource("Images/back_flipped.png")), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        flipNewCard(valueOf(Board.getInstance().getCurrentCard().getNumber()), true);
    }

    @FXML protected void rules(ActionEvent event) {
        new RulesDialog((Stage) rulesBtn.getScene().getWindow());
    }

    @FXML protected void quit(ActionEvent event) {
        Platform.exit();
    }

    @FXML protected void takeCard(ActionEvent event) {
        processTakeCard(true);
    }

    private void processTakeCard(boolean animate) {
        Board.getInstance().giveCardChips();
        updateInterface();
        flipNewCard(valueOf(Board.getInstance().getCurrentCard().getNumber()), animate);
    }
    
    @FXML protected void tossChip(ActionEvent event) {
        processTossChip();
    }

    private void processTossChip() {
        Board.getInstance().tossChip();
        updateInterface();
    }

    private void updateInterface() {
        updateCurrentPlayer();
        updateCurrentCardAndChips();
        updateScoreboard();

        // Get AI move if next player is AI
        if (Board.getInstance().getCurrentPlayer().isAI()) {
            // TODO: get move in a new thread, disable inputs while retrieving AI move
            //System.out.println("Current player is AI agent " + Board.getInstance().getCurrentPlayer().getID() + ". Retrieving AI move...");

            boolean move = Board.getInstance().getCurrentPlayer().GetAIMove();
            //System.out.println("AI move: '" + move + "'.");

            if (move) processTakeCard(false);
            else if (!move) processTossChip();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    // remove old card to player and flip a new card
    private void flipNewCard(Integer cardNumber, boolean animate) {
        for (int i = 0; i < activeTasks.size(); i++) {
            activeTasks.get(i).cancel();
        }

        // Load image sources
        Image newCardImgSource = SwingFXUtils.toFXImage(CardGfx.getInstance().getFlippedCard(cardNumber), null);
        activeCardImg.setImage(cardBackside);

        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            try {

                // Rotate back of card
                RotateTransition rotator = new RotateTransition(Duration.millis(600), activeCardImg);
                rotator.setAxis(Rotate.Y_AXIS);
                rotator.setFromAngle(0);
                rotator.setToAngle(90);
                rotator.setInterpolator(Interpolator.LINEAR);
                rotator.setCycleCount(1);
                rotator.play();

                // Switch image source at halfway point if rotator is still running
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

                // Make sure card image is drawn if rotator is still running
                Thread.sleep(850);
                activeCardImg.setImage(newCardImgSource);

            } catch (InterruptedException e) {
                // Ignore sleep interruptions (threads may be cancelled)
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
            }
        };

        // Start animation thread
        activeTasks.add(sleeper);
        if (activeTasks.size() > 1 && animate) new Thread(sleeper).start();
        else {
            activeCardImg.setImage(newCardImgSource); // Skip animation on first run or if animate is set to false
        }
    }
    int counter = 0;
    private void updateCurrentCardAndChips() {
        if (Board.getInstance().getIsFinished()) {
            takeCardBtn.setDisable(true);
            tossChipBtn.setDisable(true);
            currentPlayerLbl.setVisible(false);
            playerDeckImg.setVisible(false);

            // Call winner dialog
            updateScoreboard();
            new WinnerDialog((Stage) takeCardBtn.getScene().getWindow());

//            if(counter > 5000) new WinnerDialog((Stage) takeCardBtn.getScene().getWindow());
//            counter++;
//            System.out.println(counter);

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
            if (players.get(i).isAI()) scoreboard.append("AI: "+players.get(i).getAgent().getClass().getSimpleName()+" " + players.get(i).getID());
            else scoreboard.append("Player " +  players.get(i).getID());
            scoreboard.append(". Score: " + players.get(i).getScore());
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
