import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    private int size = 200;
    private int maxNumber = 1;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World");
        PuzzleFrame puzzleFrame = new PuzzleFrame(maxNumber, size, 0);
        ScrollPane root = new ScrollPane();
        root.setContent(puzzleFrame.getFrame());
        primaryStage.setScene(new Scene(root));
//        primaryStage.setFullScreen(true);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
