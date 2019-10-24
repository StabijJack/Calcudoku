import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

class PuzzleGridPane extends PuzzleData {
    private final BasicBlock[][] puzzleBlock;
    private int currentRow = 0;
    private int currentColumn = 0;
    private boolean playMode;


    PuzzleGridPane(int maxNumber, int size, int startNumber) {
        super(maxNumber, startNumber);
        puzzleBlock = new BasicBlock[numberOfBlocks][numberOfBlocks];
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
                puzzleBlock[column][row] = new BasicBlock(size);
            }
        }
        puzzleBlock[currentColumn][currentRow].setSelected(true);
        playMode = true;
    }

    @NotNull GridPane getFrame() {
        GridPane puzzleFrame = new GridPane();
//        to get Initial KeyEvent within GridPane
        TextField t = new TextField();
        puzzleFrame.add(t, 0, numberOfBlocks + 1, numberOfBlocks, 1);
//        to get Initial KeyEvent within GridPane
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
                puzzleFrame.add(puzzleBlock[column][row].getBlock(), column, row);
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

    void manageKeyEvent(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode.isNavigationKey()) {
            manageCursorKeys(keyCode);
        } else {
            if (isPlayMode()) {
                if (keyCode.isDigitKey() || keyCode.isLetterKey()) manageInputBlock(keyCode, event.isShiftDown());
                else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                    manageClearBlock();
            } else {
                if (keyCode.isDigitKey()) manageFormulaDigits(keyCode);
                else if ((keyCode == KeyCode.DELETE) || (keyCode == KeyCode.BACK_SPACE) || (keyCode == KeyCode.SPACE))
                    manageFormulaClearBlock();
                else if ((keyCode == KeyCode.ADD) || (keyCode == KeyCode.SUBTRACT) || (keyCode == KeyCode.MULTIPLY) || (keyCode == KeyCode.DIVIDE))
                    manageFormulaLetters(keyCode);
            }
        }
    }

    private void manageCursorKeys(@NotNull KeyCode keyCode) {
        puzzleBlock[currentColumn][currentRow].setSelected(false);
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
                currentColumn = numberOfBlocks - 1;
                break;
            case PAGE_UP:
                currentRow = 0;
                break;
            case PAGE_DOWN:
                currentRow = numberOfBlocks - 1;
                break;
        }

        currentRow = (currentRow + numberOfBlocks) % numberOfBlocks;
        currentColumn = (currentColumn + numberOfBlocks) % numberOfBlocks;
        puzzleBlock[currentColumn][currentRow].setSelected(true);

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
            if (togglePossibility(currentColumn, currentRow, value)) {
                StringBuilder s = new StringBuilder();
                for (int possibility = 0; possibility < numberOfBlocks; possibility++) {
                    if (getPossibility(currentColumn, currentRow, possibility)) s.append(" ").append(possibility);
                }
                puzzleBlock[currentColumn][currentRow].setPossibilities(s.toString());
            }
        } else {
            if (setSolution(currentColumn, currentRow, value)) {
                puzzleBlock[currentColumn][currentRow].setSolution(String.valueOf(solution[currentColumn][currentRow]));
                checkSolutionUniqueOnColumnAndRow();
            }
        }
    }

    private void manageClearBlock() {
        if (solution[currentColumn][currentRow] == null) {
            for (int value = 0; value < numberOfBlocks; value++) {
                resetPossibility(currentColumn, currentRow, value);
            }
            puzzleBlock[currentColumn][currentRow].setPossibilities("");
        } else {
            setSolution(currentColumn, currentRow, null);
            puzzleBlock[currentColumn][currentRow].setSolution("");
        }
    }

    private void manageFormulaDigits(@NotNull KeyCode keyCode) {
        int value = keyCode.getCode();
        if (value > 57) value -= 48;
        value -= 48;
        setNumberFormula(currentColumn, currentRow, value);
        if (getOperatorFormula(currentColumn, currentRow) == null)
            puzzleBlock[currentColumn][currentRow].setFormula(getNumberFormula(currentColumn, currentRow).toString());
        else
            puzzleBlock[currentColumn][currentRow].setFormula(getNumberFormula(currentColumn, currentRow).toString() + getOperatorFormula(currentColumn, currentRow));
    }

    private void manageFormulaLetters(@NotNull KeyCode keyCode) {
        switch (keyCode) {
            case ADD:
                setOperatorFormula(currentColumn, currentRow, operators.ADD);
                break;
            case SUBTRACT:
                setOperatorFormula(currentColumn, currentRow, operators.SUBTRACT);
                break;
            case MULTIPLY:
                setOperatorFormula(currentColumn, currentRow, operators.MULTIPLY);
                break;
            case DIVIDE:
                setOperatorFormula(currentColumn, currentRow, operators.DIVIDE);
                break;
        }
        puzzleBlock[currentColumn][currentRow].setFormula(getNumberFormula(currentColumn, currentRow).toString() + getOperatorFormula(currentColumn, currentRow));
    }

    private void manageFormulaClearBlock() {
        setNumberFormula(currentColumn, currentRow, null);
        setOperatorFormula(currentColumn, currentRow, null);
        puzzleBlock[currentColumn][currentRow].setFormula("");
    }

    void manageMouseEvent(MouseEvent event) {
        BlockPosition mousePoint = getMousePoint(event);
        if (event.isShiftDown()) manageMouseFormulaSelect(mousePoint);
        else manageMouseMoveSelect(mousePoint);
    }

    private void manageMouseFormulaSelect(BlockPosition mousePoint) {
        BlockPosition formulaOwner;
        formulaOwner = getFormulaOwner(new BlockPosition(currentColumn, currentRow));
        if (formulaOwner == null) {
            formulaOwner = getFormulaOwner(mousePoint);
            if (formulaOwner == null) {
                puzzleBlock[currentColumn][currentRow].setSelected(false);
                currentColumn = mousePoint.getColumn();
                currentRow = mousePoint.getRow();
                puzzleBlock[currentColumn][currentRow].setSelected(true);
                return;
            }
        }
        if (addFormulaMember(formulaOwner.getColumn(), formulaOwner.getRow(), mousePoint)) {
            puzzleBlock[mousePoint.getColumn()][mousePoint.getRow()].setFormula("oke");
//                 change screen properties
            buildFormulaBorder(formulaOwner);
        } else {
            puzzleBlock[mousePoint.getColumn()][mousePoint.getRow()].setFormula("not oke");
        }


    }

    private void buildFormulaBorder(BlockPosition formulaOwner) {
        HashSet formulaMembers = getFormulaMembers(formulaOwner);

    }

    private void manageMouseMoveSelect(BlockPosition mousePoint) {
        puzzleBlock[currentColumn][currentRow].setSelected(false);
        currentColumn = mousePoint.getColumn();
        currentRow = mousePoint.getRow();
        puzzleBlock[currentColumn][currentRow].setSelected(true);
    }

    private BlockPosition getMousePoint(MouseEvent m) {
        int column;
        int row;
        for (column = 0; column < numberOfBlocks; column++)
            if (m.getX() < puzzleBlock[column][0].getBlock().getLayoutX() + puzzleBlock[column][0].getBlock().getWidth())
                break;
        for (row = 0; row < numberOfBlocks; row++) {
            if (m.getY() < puzzleBlock[0][row].getBlock().getLayoutY() + puzzleBlock[0][row].getBlock().getHeight())
                break;
        }
        return new BlockPosition(column, row);

    }

    private void checkSolutionUniqueOnColumnAndRow() {
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
                puzzleBlock[column][row].SetSolutionError(false);
            }
        }
        for (int targetColumn = 0; targetColumn < numberOfBlocks; targetColumn++) {
            for (int targetRow = 0; targetRow < numberOfBlocks; targetRow++) {
                Integer targetValue = solution[targetColumn][targetRow];
                if (targetValue != null) {
                    for (int column = 0; column < targetColumn; column++) {
                        if ((solution[column][targetRow]) != null)
                            if (solution[column][targetRow].intValue() == targetValue)
                                puzzleBlock[column][targetRow].SetSolutionError(true);
                    }
                    for (int column = targetColumn + 1; column < numberOfBlocks; column++) {
                        if (solution[column][targetRow] != null)
                            if (solution[column][targetRow].intValue() == targetValue)
                                puzzleBlock[column][targetRow].SetSolutionError(true);
                    }
                    for (int row = 0; row < targetRow; row++) {
                        if (solution[targetColumn][row] != null)
                            if (solution[targetColumn][row].intValue() == targetValue)
                                puzzleBlock[targetColumn][row].SetSolutionError(true);
                    }
                    for (int row = targetRow + 1; row < numberOfBlocks; row++) {
                        if (solution[targetColumn][row] != null)
                            if (solution[targetColumn][row].intValue() == targetValue)
                                puzzleBlock[targetColumn][row].SetSolutionError(true);
                    }
                }
            }
        }
    }

}