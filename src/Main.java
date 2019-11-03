import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


public class Main extends Application {

    private static final KeyCombination switchMode = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    private static final KeyCombination savePuzzle = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
    private static final KeyCombination loadPuzzle = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);
    private PuzzleUserView puzzleUserView;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) {

        ScrollPane scrollPane = new ScrollPane();
        new Style(60);
        int maxNumber = 7;
        int startNumber = 0;
        puzzleUserView = new PuzzleUserView(maxNumber, startNumber);
        scrollPane.setContent(puzzleUserView.getFrame());
        scrollPane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (switchMode.match(event)) {
                puzzleUserView.togglePlayMode();
                managePlayMode(primaryStage);
            } else if (savePuzzle.match(event)) {
                    puzzleUserView.manageSavePuzzle();
            } else if (loadPuzzle.match(event)) {
                    puzzleUserView.manageLoadPuzzle();
            } else {
                puzzleUserView.manageKeyEvent(event);
            }
        });
        scrollPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> puzzleUserView.manageMouseEvent(event));
        Scene scene = new Scene(scrollPane);
        primaryStage.setScene(scene);
        managePlayMode(primaryStage);
        primaryStage.show();
    }

    private void managePlayMode(@NotNull Stage primaryStage) {
        if (puzzleUserView.isPlayMode()) {
            primaryStage.setTitle("Calcudoku in Play Mode");
        } else {
            primaryStage.setTitle("Calcudoku in Create Mode");
        }
    }
}
