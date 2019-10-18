import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;


public class Main extends Application {

    private static final KeyCombination switchMode = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
    private PuzzleFrame puzzleFrame;

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(@NotNull Stage primaryStage) {
        ScrollPane scrollPane = new ScrollPane();
        int size = 60;
        int maxNumber = 7;
        int startNumber = 0;
        puzzleFrame = new PuzzleFrame(maxNumber, size, startNumber);
        manageMode(primaryStage);
        scrollPane.setContent(puzzleFrame.getFrame());
        scrollPane.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (switchMode.match(event)) {
                manageMode(primaryStage);
            } else {
                KeyCode keyCode = event.getCode();
                if (keyCode.isNavigationKey()) {
                    puzzleFrame.manageCursorKeys(keyCode);
                } else {
                    if (puzzleFrame.isPlayMode()) {
                        if (keyCode.isDigitKey()||keyCode.isLetterKey()) puzzleFrame.manageInputBlock(keyCode, event.isShiftDown());
                        else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                            puzzleFrame.manageClearBlock();
                    } else {
                        if (keyCode.isDigitKey()) puzzleFrame.manageFormulaDigits(keyCode);
                        else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                            puzzleFrame.manageFormulaClearBlock();
                        else if ((keyCode == KeyCode.ADD) || (keyCode == KeyCode.SUBTRACT) || (keyCode == KeyCode.MULTIPLY) || (keyCode == KeyCode.DIVIDE))
                            puzzleFrame.manageFormulaLetters(keyCode);
                    }
                }
            }
        });
        scrollPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
                puzzleFrame.manageMouse(event.getX(), event.getY()));
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void manageMode(@NotNull Stage primaryStage) {
        puzzleFrame.setPlayMode(!puzzleFrame.isPlayMode());
        if (puzzleFrame.isPlayMode()) {
            primaryStage.setTitle("Calcudoku in Play Mode");
        } else {
            primaryStage.setTitle("Calcudoku in Create Mode");
        }
    }
}
