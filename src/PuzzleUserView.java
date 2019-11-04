import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;


class PuzzleUserView {
    private PuzzleData puzzleData;
    private final PuzzleBlockView[][] puzzleBlockView;
    private int currentRow = 0;
    private int currentColumn = 0;
    private BlockPosition currentBlockPosition = new BlockPosition(currentColumn, currentRow);
    private boolean playMode;
    private final TextField communicationTextField = new TextField();



    PuzzleUserView(int maxNumber, int startNumber) {
        puzzleData = new PuzzleData(maxNumber, startNumber);
        puzzleBlockView = new PuzzleBlockView[puzzleData.numberOfBlocks][puzzleData.numberOfBlocks];
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
                puzzleBlockView[column][row] = new PuzzleBlockView();
            }
        }
        puzzleBlockView[0][0].setSelected(true);
        playMode = true;
    }

    @NotNull GridPane getFrame() {
        GridPane puzzleFrame = new GridPane();
        puzzleFrame.add(communicationTextField, 0, puzzleData.numberOfBlocks + 1, puzzleData.numberOfBlocks, 1);
        for (int column = 0; column < puzzleData.numberOfBlocks; column++) {
            for (int row = 0; row < puzzleData.numberOfBlocks; row++) {
                puzzleFrame.add(puzzleBlockView[column][row].getBlock(), column, row);
            }
        }
        return puzzleFrame;
    }

    boolean isPlayMode() {
        return playMode;
    }

    void togglePlayMode() {
        playMode = !playMode;
    }

    void manageKeyEvent(@NotNull KeyEvent event) {
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

    void manageMouseEvent(MouseEvent mouseEvent) {
        BlockPosition mousePoint = getMousePosition(mouseEvent);
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                communicationTextField.setText(puzzleBlockView[mousePoint.getColumn()][mousePoint.getRow()].getPossibilities());
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

    void manageSavePuzzle(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("src/puzzleData.json")) {
            gson.toJson(puzzleData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void manageLoadPuzzle(){
        Gson gson = new Gson();
        try (Reader reader = new FileReader("src/PuzzleData.json")) {
            puzzleData = gson.fromJson(reader, PuzzleData.class);
            setPuzzleDataOnView();
        } catch (IOException e) {
            e.printStackTrace();
        }
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