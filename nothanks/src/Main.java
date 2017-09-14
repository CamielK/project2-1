import Library.Deck;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private static Deck deck;
	
    public static void main(String[] args) {
    	deck = new Deck();
    	
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Gui/Interface.fxml"));
        primaryStage.setTitle("No thanks!");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

}
