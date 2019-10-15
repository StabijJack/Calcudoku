import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    private PuzzleFrame puzzleFrame;
    private static KeyCombination switchMode = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    @Override
    public void start(Stage primaryStage) {
        ScrollPane root = new ScrollPane();
        int size = 100;
        int maxNumber = 3;
        int startNumber = 1;
        puzzleFrame = new PuzzleFrame(maxNumber, size, startNumber);
        manageMode(primaryStage);
        root.setContent(puzzleFrame.getFrame());
        root.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (switchMode.match(event)) {
                manageMode(primaryStage);
            }
            else {
                KeyCode keyCode = event.getCode();
                if (keyCode.isNavigationKey()) {
                    puzzleFrame.manageCursorKeys(keyCode);
                }
                else {
                    if (puzzleFrame.isPlayMode()) {
                        if (keyCode.isDigitKey()) puzzleFrame.manageDigits(keyCode, event.isAltDown());
                        else if (keyCode.isLetterKey()) puzzleFrame.manageLetters(keyCode, event.isAltDown());
                        else if (keyCode == KeyCode.DELETE || keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.SPACE) puzzleFrame.manageClearBlock(keyCode);
                    }
                    else{
                        if (keyCode.isDigitKey()) puzzleFrame.manageFormulaDigits(keyCode);
                        else if (keyCode == KeyCode.DELETE || keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.SPACE) puzzleFrame.manageFormulaClearBlock(keyCode);
                        else if (keyCode == KeyCode.ADD || keyCode == KeyCode.SUBTRACT || keyCode == KeyCode.MULTIPLY || keyCode == KeyCode.DIVIDE) puzzleFrame.manageFormulaLetters(keyCode);

                    }
                }

            }
        });
        root.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->
            puzzleFrame.manageMouse(event.getX(), event.getY()));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

    private void manageMode(Stage primaryStage) {
        puzzleFrame.setPlayMode(!puzzleFrame.isPlayMode());
        if (puzzleFrame.isPlayMode()) {
            primaryStage.setTitle("Calcudoku in Play Mode");
        } else {
            primaryStage.setTitle("Calcudoku in Create Mode");
        }
    }
}
