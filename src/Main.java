import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


public class Main extends Application {

    private static final KeyCombination switchMode = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    private PuzzleGridPane puzzleGridPane;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane();
        int size = 60;
        int maxNumber = 7;
        int startNumber = 0;
        puzzleGridPane = new PuzzleGridPane(maxNumber, size, startNumber);
        scrollPane.setContent(puzzleGridPane.getFrame());
        scrollPane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (switchMode.match(event)) {
                puzzleGridPane.togglePlayMode();
                managePlayMode(primaryStage);
            }
            else {
                puzzleGridPane.manageKeyEvent(event);
            }
        });
        scrollPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> puzzleGridPane.manageMouseEvent(event));
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        managePlayMode(primaryStage);
        primaryStage.show();
    }

    private void managePlayMode(@NotNull Stage primaryStage) {
        if (puzzleGridPane.isPlayMode()) {
            primaryStage.setTitle("Calcudoku in Play Mode");
        } else {
            primaryStage.setTitle("Calcudoku in Create Mode");
        }
    }
}
