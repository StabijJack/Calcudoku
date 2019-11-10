import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) {

        new Style(40);
        PuzzleUserView puzzleUserView = new PuzzleUserView();
        primaryStage.setScene(puzzleUserView.getScene());
        primaryStage.setTitle("Calcudoku");
        primaryStage.show();
    }
}
