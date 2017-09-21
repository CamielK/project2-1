package Gui;

import Library.Board;
import Library.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Main grid
    @FXML private GridPane grid;

    // Boxes
    @FXML private HBox scoreboardBox;

    // Labels
    @FXML private Label currentCardLbl;
    @FXML private Label currentChipsLbl;
    @FXML private Label scores;

    // Inputs
    @FXML private Button takeCardBtn;
    @FXML private Button tossChipBtn;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        // init method is run when fxml is finished loading
        updateCurrentCardAndChips();
        updateScoreboard();
    }

    @FXML protected void takeCard(ActionEvent event) {
        Board.getInstance().giveCardChips();
        updateCurrentCardAndChips();
        updateScoreboard();
    }
    
    @FXML protected void tossChip(ActionEvent event) {
    	Board.getInstance().tossChip();
    	updateCurrentCardAndChips();
    	updateScoreboard();
    }

    private void updateCurrentCardAndChips() {
        currentCardLbl.setText("Current card: " + String.valueOf(Board.getInstance().getCurrentCard().getNumber()));
        currentChipsLbl.setText("Current chips: " + String.valueOf(Board.getInstance().getCurrentChips()));
    }

    private void updateScoreboard() {
        ArrayList<Player> players = Board.getInstance().getPlayers();
        StringBuilder scoreboard = new StringBuilder();
        for (int i=0; i < players.size(); i++) {
            scoreboard.append("player " + i + ": " + players.get(i).getScore());
            scoreboard.append("\n");
        }
        scores.setText(scoreboard.toString());
    }
}
