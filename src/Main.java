import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    private PuzzleFrame puzzleFrame;
    private static KeyCombination switchMode;
    private static ArrayList<KeyCode> cursorKeys;

    @Override
    public void start(Stage primaryStage) {
        ScrollPane root = new ScrollPane();
        int size = 200;
        int maxNumber = 2;
        int startNumber = 0;
        puzzleFrame = new PuzzleFrame(maxNumber, size, startNumber);
        manageMode(primaryStage);
        root.setContent(puzzleFrame.getFrame());
        root.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (switchMode.match(event)) manageMode(primaryStage);
            KeyCode keyCode = event.getCode();
            if (cursorKeys.contains(keyCode)) puzzleFrame.manageCursorKeys(keyCode);
        });
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    private void manageMode(Stage primaryStage) {
        puzzleFrame.setPlayMode(!puzzleFrame.isPlayMode());
        if (puzzleFrame.isPlayMode()) {
            primaryStage.setTitle("Calcudoku in Play Mode");
        } else {
            primaryStage.setTitle("Calcudoku in Create Mode");
        }
    }
    public static void main(String[] args) {
        switchMode = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        cursorKeys = new ArrayList<>();
        cursorKeys.add(KeyCode.DOWN);
        cursorKeys.add(KeyCode.UP);
        cursorKeys.add(KeyCode.LEFT);
        cursorKeys.add(KeyCode.RIGHT);
        cursorKeys.add(KeyCode.HOME);
        cursorKeys.add(KeyCode.END);
        cursorKeys.add(KeyCode.PAGE_DOWN);
        cursorKeys.add(KeyCode.PAGE_UP);

        launch(args);
    }
}
