import Gui.BoardGui;
import Library.Board;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Board board;
	
    public static void main(String[] args) {
    	board = Board.getInstance();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        new BoardGui();
    }

}
