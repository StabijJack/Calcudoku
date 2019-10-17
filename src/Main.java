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
        int size = 45;
        int maxNumber = 19;
        int startNumber = 1;
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
                        if (keyCode.isDigitKey()) puzzleFrame.manageDigits(keyCode, event.isAltDown());
                        else if (keyCode.isLetterKey()) puzzleFrame.manageLetters(keyCode, event.isAltDown());
                        else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                            puzzleFrame.manageClearBlock(keyCode);
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
        Scene scene = new Scene(scrollPane,800,800);
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
