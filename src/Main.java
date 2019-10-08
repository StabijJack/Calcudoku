import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    private int size = 100;
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        BasicBlock puzzleBlock1 = new BasicBlock(size);
        puzzleBlock1.setSolution("19");
        puzzleBlock1.setPossibilities("88");
        puzzleBlock1.setFormula("135X");



        GridPane gridPane = new GridPane();
        gridPane.addRow(0, puzzleBlock1.getBlock());

        primaryStage.setScene(new Scene(gridPane, 400, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
