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
        manageMode(primaryStage);
        scrollPane.setContent(puzzleGridPane.getFrame());
        scrollPane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (switchMode.match(event)) {
                manageMode(primaryStage);
            } else {
                KeyCode keyCode = event.getCode();
                if (keyCode.isNavigationKey()) {
                    puzzleGridPane.manageCursorKeys(keyCode);
                } else {
                    if (puzzleGridPane.isPlayMode()) {
                        if (keyCode.isDigitKey()||keyCode.isLetterKey()) puzzleGridPane.manageInputBlock(keyCode, event.isShiftDown());
                        else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                            puzzleGridPane.manageClearBlock();
                    } else {
                        if (keyCode.isDigitKey()) puzzleGridPane.manageFormulaDigits(keyCode);
                        else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                            puzzleGridPane.manageFormulaClearBlock();
                        else if ((keyCode == KeyCode.ADD) || (keyCode == KeyCode.SUBTRACT) || (keyCode == KeyCode.MULTIPLY) || (keyCode == KeyCode.DIVIDE))
                            puzzleGridPane.manageFormulaLetters(keyCode);
                    }
                }
            }
        });
        scrollPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
                puzzleGridPane.manageMouse(event.getX(), event.getY()));
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void manageMode(@NotNull Stage primaryStage) {
        puzzleGridPane.setPlayMode(!puzzleGridPane.isPlayMode());
        if (puzzleGridPane.isPlayMode()) {
            primaryStage.setTitle("Calcudoku in Play Mode");
        } else {
            primaryStage.setTitle("Calcudoku in Create Mode");
        }
    }
}
