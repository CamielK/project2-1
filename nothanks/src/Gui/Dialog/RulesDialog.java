package Gui.Dialog;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;


//show help dialog with information about prototype app
public class RulesDialog {

    public RulesDialog(Stage parentStage) {
        // Load rules image
        ImageView rules = new ImageView();
        try {
            URL path = getClass().getResource("../Images/rules.jpg");
            Image deckImgSource = SwingFXUtils.toFXImage(ImageIO.read(path), null);
            rules.setImage(deckImgSource);
            rules.setFitWidth(700);
            rules.setPreserveRatio(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Scrollview
        ScrollPane scrollView = new ScrollPane();
        scrollView.setPrefSize(720,500);
        scrollView.setContent(rules);

        // Button to close dialog
        final Button okay = new Button("Okay, got it");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) okay.getScene().getWindow();
                stage.close();
            }
        });


        // Create and show new dialog with error messages
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Game rules");
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(scrollView, okay).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.showAndWait();
    }


}
