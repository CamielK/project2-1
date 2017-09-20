package Gui;

import Library.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ResourceBundle;

public class Controller implements Initializable {

    // Main grid
    @FXML private GridPane grid;

    // Labels
    @FXML private Label clickLbl;

    // Inputs
    @FXML private Button takeCardBtn;
    @FXML private Button tossChipBtn;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        // init method is run when fxml is finished loading
    }

    @FXML protected void takeCard(ActionEvent event) {
    	Board.getInstance().giveCard();
    }
    
    @FXML protected void tossChip(ActionEvent event) {
    	Board.getInstance().tossChip();
    }
}
