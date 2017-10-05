package Gui.Dialog;

import Gui.Graphics.CardGfx;
import Library.Board;
import Library.Player;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//show help dialog with information about prototype app
public class WinnerDialog {

    public WinnerDialog(Stage parentStage) {
        // Load rules image
        Text dialogHeader = new Text(Board.getInstance().getWinners());
        dialogHeader.setStyle("-fx-font-size: 24px; -fx-font-family: \"Calibri\"; -fx-font-weight: bold; -fx-fill: #2A5058;");

        VBox playersList = new VBox();
        ArrayList<Player> players = Board.getInstance().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Text label = new Text("Player " + players.get(i).getID() + ":");

            BufferedImage deck = CardGfx.getInstance().renderPlayerDeck(players.get(i).getCards(), players.get(i).getChips());
            Image playerDeck = SwingFXUtils.toFXImage(deck, null);
            ImageView view = new ImageView();
            view.setFitHeight(130);
            view.setPreserveRatio(true);
            view.setImage(playerDeck);
            playersList.getChildren().addAll(label, view);
        }

        // Button to close dialog
        final Button okay = new Button("Play again!");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) okay.getScene().getWindow();
                stage.close();
            }
        });

        // Quit button
        final Button quit = new Button("Quit");
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
                Stage stage = (Stage) okay.getScene().getWindow();
                stage.close();
            }
        });

        // Stack buttons
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);
        hbox.getChildren().addAll(okay, quit);

        // Create and show new dialog with error messages
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Game finished!");
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(dialogHeader, playersList, hbox).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.showAndWait();
    }


}
