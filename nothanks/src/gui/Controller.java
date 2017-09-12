package Gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static int clickCount = 0;

    // Main grid
    @FXML private GridPane grid;

    // Labels
    @FXML private Label clickLbl;

    // Inputs
    @FXML private Button clickBtn;

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        // init method is run when fxml is finished loading
    }

    // Click example
    @FXML protected void clickExample(ActionEvent event) {
        clickCount++;
        clickLbl.setText("Clicked "+clickCount+" times!");
    }
}
