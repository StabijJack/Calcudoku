import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;


class PuzzleUserView {
    private final Scene puzzleScene;
    private final ScrollPane puzzleScrollPane = new ScrollPane();
    private PuzzleData puzzleData;
    private PuzzleBlockView[][] puzzleBlockView;
    private int currentRow = 0;
    private int currentColumn = 0;
    private BlockPosition currentBlockPosition = new BlockPosition(currentColumn, currentRow);
    private boolean playMode;
    private final Label communicationLabel = new Label();
    private final Label playModeLabel = new Label();

    PuzzleUserView() {
        buildPuzzle(2,1);
        puzzleScene = new Scene(puzzleScrollPane);
        puzzleScene.addEventFilter(KeyEvent.KEY_RELEASED, e -> manageKeyEvent(e));
    }

    private void buildPuzzle(int maxNumber, int startNumber){
        puzzleData = new PuzzleData(maxNumber, startNumber);
        buildPuzzleView();
    }
    private void buildPuzzleView(){
        currentRow = 0;
        currentColumn = 0;
        puzzleBlockView = new PuzzleBlockView[puzzleData.numberOfBlocks][puzzleData.numberOfBlocks];
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
                puzzleBlockView[column][row] = new PuzzleBlockView();
            }
        }
        puzzleBlockView[0][0].setSelected(true);
        playMode = true;

        puzzleScrollPane.setContent(getFrame());

    }

    Scene getScene(){
        return puzzleScene;
    }
    @NotNull
    private GridPane getFrame() {
        GridPane puzzleFrame = new GridPane();
        //noinspection Convert2MethodRef
        puzzleFrame.setOnMouseClicked(e -> manageMouseEvent(e));
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
                puzzleFrame.add(puzzleBlockView[column][row].getBlock(), column, row);
            }
        }
        GridPane windowsFrame = new GridPane();
        //noinspection Convert2MethodRef
//        windowsFrame.setOnKeyReleased(e -> manageKeyEvent(e)); key management on scene level
        windowsFrame.add(puzzleFrame,0,0,puzzleData.numberOfBlocks,puzzleData.numberOfBlocks);
        windowsFrame.add(communicationLabel, 0, puzzleData.numberOfBlocks + 1, puzzleData.numberOfBlocks,1);

        windowsFrame.add(playModeLabel,puzzleData.numberOfBlocks + 1, 0);
        togglePlayMode();

        Button createModifyPuzzle = new Button("Toggle Play Mode");
        createModifyPuzzle.setOnMouseClicked(e -> togglePlayMode());
        windowsFrame.add(createModifyPuzzle,puzzleData.numberOfBlocks + 2, 0);

        Label startNumberLabel = new Label("StartNumber");
        windowsFrame.add(startNumberLabel,puzzleData.numberOfBlocks + 1,1);
        Spinner<Integer> startNumber = new Spinner<>(0,1,puzzleData.startNumber);
        windowsFrame.add(startNumber,puzzleData.numberOfBlocks + 2,1);

        Label maxNumberLabel = new Label("MaxNumber");
        windowsFrame.add(maxNumberLabel,puzzleData.numberOfBlocks + 1, 2);
        Spinner<Integer> maxNumber = new Spinner<>(2,19,puzzleData.maxNumber);
        windowsFrame.add(maxNumber,puzzleData.numberOfBlocks + 2, 2);

        Button newPuzzle = new Button("New Puzzle");
        windowsFrame.add(newPuzzle,puzzleData.numberOfBlocks + 2, 3);
        newPuzzle.setOnMouseClicked(mouseEvent -> buildPuzzle(maxNumber.getValue(), startNumber.getValue()));

        Button savePuzzle = new Button("Save Puzzle");
        savePuzzle.setOnMouseClicked(mouseEvent -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileChooser loadPuzzleFieChooser = new FileChooser();
            loadPuzzleFieChooser.setInitialDirectory(Paths.get("./src").toFile());
            loadPuzzleFieChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON Files", "*.JSON"));
            File savePuzzleFile = loadPuzzleFieChooser.showOpenDialog(new Stage());

            try (FileWriter writer = new FileWriter(savePuzzleFile)) {
                gson.toJson(puzzleData, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        windowsFrame.add(savePuzzle,puzzleData.numberOfBlocks + 1, 4);

        Button loadPuzzle = new Button("Load Puzzle");
        loadPuzzle.setOnMouseClicked(mouseEvent -> {
            FileChooser loadPuzzleFieChooser = new FileChooser();
            loadPuzzleFieChooser.setInitialDirectory(Paths.get("./src").toFile());
            loadPuzzleFieChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON Files", "*.JSON"));
            File loadPuzzleFile = loadPuzzleFieChooser.showOpenDialog(new Stage());

            Gson gson = new Gson();
            try (Reader reader = new FileReader(loadPuzzleFile)) {
                puzzleData = gson.fromJson(reader, PuzzleData.class);
                buildPuzzleView();
                setPuzzleDataOnView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        windowsFrame.add(loadPuzzle,puzzleData.numberOfBlocks + 2, 4);


        return windowsFrame;
    }

    @Contract(pure = true)
    private boolean isPlayMode() {
        return playMode;
    }

    private void togglePlayMode() {
        playMode = !playMode;
        if(playMode) playModeLabel.setText("Play Mode");
        else playModeLabel.setText("create Modify Mode");
    }

    private void manageKeyEvent(@NotNull KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.isNavigationKey()) {
            manageCursorKeys(keyCode);
        } else {
            if (isPlayMode()) {
                if (keyCode.isDigitKey() || keyCode.isLetterKey()) manageInputBlock(keyCode, event.isShiftDown());
                else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                    manageClearBlock();
            } else {
                if (keyCode.isDigitKey())
                    manageFormulaDigits(keyCode);
                else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                    manageFormulaClearBlock();
                else if ((keyCode == KeyCode.ADD) || (keyCode == KeyCode.SUBTRACT) || (keyCode == KeyCode.MULTIPLY) || (keyCode == KeyCode.DIVIDE))
                    manageFormulaLetters(keyCode);
            }
        }
    }

    private void manageCursorKeys(@NotNull KeyCode keyCode) {
        puzzleBlockView[currentColumn][currentRow].setSelected(false);
        switch (keyCode) {
            case DOWN:
                ++currentRow;
                break;
            case UP:
                --currentRow;
                break;
            case RIGHT:
                ++currentColumn;
                break;
            case LEFT:
                --currentColumn;
                break;
            case HOME:
                currentColumn = 0;
                break;
            case END:
                currentColumn = puzzleData.numberOfBlocks - 1;
                break;
            case PAGE_UP:
                currentRow = 0;
                break;
            case PAGE_DOWN:
                currentRow = puzzleData.numberOfBlocks - 1;
                break;
        }

        currentRow = (currentRow + puzzleData.numberOfBlocks) % puzzleData.numberOfBlocks;
        currentColumn = (currentColumn + puzzleData.numberOfBlocks) % puzzleData.numberOfBlocks;
        currentBlockPosition = new BlockPosition(currentColumn, currentRow);
        puzzleBlockView[currentColumn][currentRow].setSelected(true);

    }

    private void manageInputBlock(@NotNull KeyCode keyCode, boolean shiftDown) {
        int value = keyCode.getCode();
        if (keyCode.isDigitKey()) {
            if (value > 57) value -= 48;
            value -= 48;
        }
        //isLetterKey()
        else {
            value -= 55;
        }
        if (shiftDown) {
            if (puzzleData.togglePossibility(currentColumn, currentRow, value)) {
                setPossibilities(currentColumn,currentRow);
            }
        } else {
            if (puzzleData.setSolution(currentColumn, currentRow, value)) {
                puzzleBlockView[currentColumn][currentRow].setSolution(value);
                checkSolutionUniqueOnColumnAndRow();
            }
        }
    }

    private void manageClearBlock() {
        if (puzzleData.getSolution(currentColumn,currentRow) == null) {
            for (int value = puzzleData.startNumber; value <= puzzleData.maxNumber; value++) {
                puzzleData.resetPossibility(currentColumn, currentRow, value);
                setPossibilities(currentColumn, currentRow);
            }
        } else {
            puzzleData.setSolution(currentColumn,currentRow, null);
            setSolution(currentColumn,currentRow);
            checkSolutionUniqueOnColumnAndRow();
        }
    }

    private void manageFormulaDigits(@NotNull KeyCode keyCode) {
        int value = keyCode.getCode();
        if (value > 57) value -= 48;
        value -= 48;
        puzzleData.setFormulaNumber(currentColumn, currentRow, value);
        setFormula(currentColumn,currentRow);
        setFormulaBorders();
    }

    private void manageFormulaLetters(@NotNull KeyCode keyCode) {
        switch (keyCode) {
            case ADD:
                puzzleData.setFormulaOperator(currentColumn, currentRow, operators.ADD);
                break;
            case SUBTRACT:
                puzzleData.setFormulaOperator(currentColumn, currentRow, operators.SUBTRACT);
                break;
            case MULTIPLY:
                puzzleData.setFormulaOperator(currentColumn, currentRow, operators.MULTIPLY);
                break;
            case DIVIDE:
                puzzleData.setFormulaOperator(currentColumn, currentRow, operators.DIVIDE);
                break;
        }
        setFormula(currentColumn,currentRow);
    }

    private void manageFormulaClearBlock() {
        puzzleData.setFormulaNumber(currentColumn, currentRow, null);
        puzzleData.setFormulaOperator(currentColumn, currentRow, operators.NONE);
        setFormula(currentColumn,currentRow);
    }

    private void manageMouseEvent(MouseEvent mouseEvent) {
        BlockPosition mousePoint = getMousePosition(mouseEvent);
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                communicationLabel.setText(puzzleBlockView[mousePoint.getColumn()][mousePoint.getRow()].getPossibilities());
            }
            else {
                if (mouseEvent.isShiftDown())
                    manageMouseFormulaSelect(mousePoint);
                else manageMouseMoveSelect(mousePoint);
            }
        }
    }

    private void manageMouseFormulaSelect(@NotNull BlockPosition mousePosition) {
        if (puzzleData.getFormulaNumber(currentColumn,currentRow) != null & puzzleData.getFormulaNumber(mousePosition.getColumn(),mousePosition.getRow()) == null) {
            if (currentBlockPosition.areNeighbors(mousePosition)) {
                puzzleData.setParent(mousePosition.getColumn(),mousePosition.getRow(), currentBlockPosition);
            } else {
                int column = mousePosition.getColumn();
                int row = mousePosition.getRow();
                if (column > 0) {
                    if (puzzleData.getParent(column - 1, row) == currentBlockPosition) puzzleData.setParent(column, row, currentBlockPosition);
                }
                if (column < puzzleData.numberOfBlocks) {
                    if(puzzleData.getParent(column + 1, row) == currentBlockPosition) puzzleData.setParent(column, row, currentBlockPosition);
                }
                if (row > 0){
                    if(puzzleData.getParent(column, row - 1) == currentBlockPosition) puzzleData.setParent(column, row, currentBlockPosition);
                }
                if (row < puzzleData.numberOfBlocks){
                    if(puzzleData.getParent(column, row + 1) == currentBlockPosition) puzzleData.setParent(column, row, currentBlockPosition);
                }
            }
            setFormulaBorders();
        }
    }

    private void manageMouseMoveSelect(@NotNull BlockPosition mousePosition) {
        puzzleBlockView[currentColumn][currentRow].setSelected(false);
        currentColumn = mousePosition.getColumn();
        currentRow = mousePosition.getRow();
        currentBlockPosition = new BlockPosition(currentColumn, currentRow);
        puzzleBlockView[currentColumn][currentRow].setSelected(true);
    }

    @NotNull
    @Contract("_ -> new")
    private BlockPosition getMousePosition(MouseEvent m) {
        int column;
        int row;
        for (column = 0; column < puzzleData.numberOfBlocks; column++)
            if (m.getX() < puzzleBlockView[column][0].getBlock().getLayoutX() + puzzleBlockView[column][0].getBlock().getWidth())
                break;
        for (row = 0; row < puzzleData.numberOfBlocks; row++) {
            if (m.getY() < puzzleBlockView[0][row].getBlock().getLayoutY() + puzzleBlockView[0][row].getBlock().getHeight())
                break;
        }
        if(row >= puzzleData.numberOfBlocks) row = puzzleData.numberOfBlocks - 1;
        if(column >= puzzleData.numberOfBlocks) column = puzzleData.numberOfBlocks - 1;
        return new BlockPosition(column, row);

    }

    private void setPuzzleDataOnView() {
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
                setFormula(column,row);
                setPossibilities(column,row);
                setSolution(column,row);
            }
        }
        setFormulaBorders();
        checkSolutionUniqueOnColumnAndRow();
        puzzleBlockView[currentColumn][currentRow].setSelected(false);
        puzzleBlockView[0][0].setSelected(true);
        if (!isPlayMode()){
            togglePlayMode();
        };
    }

    private void setFormula(int column, int row) {
        if (puzzleData.getFormulaNumber(column,row) != null)
            puzzleBlockView[column][row].setFormula(puzzleData.getFormulaNumber(column,row) + puzzleData.getFormulaOperator(column,row));
        else
            puzzleBlockView[column][row].setFormula("");
    }

    private void setPossibilities(int column, int row) {
        StringBuilder s = new StringBuilder();
        for (int possibility = puzzleData.startNumber; possibility <= puzzleData.maxNumber; possibility++) {
            if (puzzleData.getPossibility(column, row, possibility))
                s.append(" ").append(possibility);
        }
        puzzleBlockView[column][row].setPossibilities(s.toString());
    }
    private void setSolution(int column, int row){
        puzzleBlockView[column][row].setSolution(puzzleData.getSolution(column, row));
    }

    private void checkSolutionUniqueOnColumnAndRow() {
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
                puzzleBlockView[column][row].SetSolutionError(false);
            }
        }
        for (int targetColumn = 0; targetColumn < puzzleData.numberOfBlocks; targetColumn++) {
            for (int targetRow = 0; targetRow < puzzleData.numberOfBlocks; targetRow++) {
                Integer targetValue = puzzleData.getSolution(targetColumn,targetRow);
                if (targetValue != null) {
                    for (int column = 0; column < targetColumn; column++) {
                        if (puzzleData.getSolution(column,targetRow) != null)
                            if (puzzleData.getSolution(column,targetRow).intValue() == targetValue)
                                puzzleBlockView[column][targetRow].SetSolutionError(true);
                    }
                    for (int column = targetColumn + 1; column < puzzleData.numberOfBlocks; column++) {
                        if (puzzleData.getSolution(column,targetRow) != null)
                            if (puzzleData.getSolution(column,targetRow).intValue() == targetValue)
                                puzzleBlockView[column][targetRow].SetSolutionError(true);
                    }
                    for (int row = 0; row < targetRow; row++) {
                        if (puzzleData.getSolution(targetColumn, row) != null)
                            if (puzzleData.getSolution(targetColumn, row).intValue() == targetValue)
                                puzzleBlockView[targetColumn][row].SetSolutionError(true);
                    }
                    for (int row = targetRow + 1; row < puzzleData.numberOfBlocks; row++) {
                        if (puzzleData.getSolution(targetColumn, row) != null)
                            if (puzzleData.getSolution(targetColumn, row).intValue() == targetValue)
                                puzzleBlockView[targetColumn][row].SetSolutionError(true);
                    }
                }
            }
        }
    }

    private void setFormulaBorders() {
        Color c = Style.blockFormulaBorderColor;
        Color r = Style.blockBorderColor;
        BlockPosition p1, p2;
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            puzzleBlockView[column][0].setBlockTopBorderColor(r);
            if (puzzleData.getFormulaParent(column,0)!= null){
                puzzleBlockView[column][0].setBlockTopBorderColor(c);
            }
            for (int row = 1; row < puzzleData.numberOfBlocks; row++) {
                puzzleBlockView[column][row].setBlockTopBorderColor(r);
                p1 = puzzleData.getFormulaParent(column, row );
                if (p1 != null){
                    p2 = puzzleData.getFormulaParent(column, row-1);
                    if (p2!=null){
                        if (!p1.isEqual(p2)) {
                            puzzleBlockView[column][row].setBlockTopBorderColor(c);
                        }
                    }
                    else{
                        puzzleBlockView[column][row].setBlockTopBorderColor(c);
                    }
                }
            }
            for (int row = 0; row < puzzleData.numberOfBlocks-1; row++) {
                puzzleBlockView[column][row].setBlockBottomBorderColor(r);
                p1 = puzzleData.getFormulaParent(column, row );
                if (p1 != null){
                    p2 = puzzleData.getFormulaParent(column, row+1);
                    if (p2!=null){
                        if (!p1.isEqual(p2)) {
                            puzzleBlockView[column][row].setBlockBottomBorderColor(c);
                        }
                    }
                    else{
                        puzzleBlockView[column][row].setBlockBottomBorderColor(c);
                    }
                }
            }
            puzzleBlockView[column][puzzleData.numberOfBlocks-1].setBlockTopBorderColor(r);
            if (puzzleData.getFormulaParent(column,puzzleData.numberOfBlocks-1)!= null){
                puzzleBlockView[column][puzzleData.numberOfBlocks-1].setBlockTopBorderColor(c);
            }
        }
        for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
            puzzleBlockView[0][row].setBlockLeftBorderColor(r);
            if (puzzleData.getFormulaParent(0,row)!= null){
                puzzleBlockView[0][row].setBlockLeftBorderColor(c);
            }
            for (int column = 1; column < puzzleData.numberOfBlocks; column++) {
                puzzleBlockView[column][row].setBlockLeftBorderColor(r);
                p1 = puzzleData.getFormulaParent(column, row );
                if (p1 != null){
                    p2 = puzzleData.getFormulaParent(column-1, row);
                    if (p2!=null){
                        if (!p1.isEqual(p2)) {
                            puzzleBlockView[column][row].setBlockLeftBorderColor(c);
                        }
                    }
                    else{
                        puzzleBlockView[column][row].setBlockLeftBorderColor(c);
                    }
                }
            }
            for (int column = 0; column < puzzleData.numberOfBlocks-1; column++) {
                puzzleBlockView[column][row].setBlockRightBorderColor(r);
                p1 = puzzleData.getFormulaParent(column, row );
                if (p1 != null){
                    p2 = puzzleData.getFormulaParent(column+1, row);
                    if (p2!=null){
                        if (!p1.isEqual(p2)) {
                            puzzleBlockView[column][row].setBlockRightBorderColor(c);
                        }
                    }
                    else{
                        puzzleBlockView[column][row].setBlockRightBorderColor(c);
                    }
                }
            }
            puzzleBlockView[puzzleData.numberOfBlocks-1][row].setBlockRightBorderColor(r);
            if (puzzleData.getFormulaParent(puzzleData.numberOfBlocks-1,row)!= null){
                puzzleBlockView[puzzleData.numberOfBlocks-1][row].setBlockRightBorderColor(c);
            }
        }
    }

}