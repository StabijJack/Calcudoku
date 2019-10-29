import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;


class PuzzleUserView extends PuzzleData {
    private final PuzzleBlockView[][] puzzleBlockView;
    private int currentRow = 0;
    private int currentColumn = 0;
    private BlockPosition currentBlockPosition = new BlockPosition(currentColumn, currentRow);
    private boolean playMode;


    PuzzleUserView(int maxNumber, int startNumber) {
        super(maxNumber, startNumber);
        puzzleBlockView = new PuzzleBlockView[numberOfBlocks][numberOfBlocks];
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
                puzzleBlockView[column][row] = new PuzzleBlockView();
            }
        }
        puzzleBlockView[currentColumn][currentRow].setSelected(true);
        playMode = true;
    }

    @NotNull GridPane getFrame() {
        GridPane puzzleFrame = new GridPane();
        TextField t = new TextField();
        puzzleFrame.add(t, 0, numberOfBlocks + 1, numberOfBlocks, 1);
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
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
            if (togglePossibility(currentColumn, currentRow, value)) {
                StringBuilder s = new StringBuilder();
                for (int possibility = 0; possibility < numberOfBlocks; possibility++) {
                    if (getPossibility(currentColumn, currentRow, possibility)) s.append(" ").append(possibility);
                }
                puzzleBlockView[currentColumn][currentRow].setPossibilities(s.toString());
            }
        } else {
            if (setSolution(currentColumn, currentRow, value)) {
                puzzleBlockView[currentColumn][currentRow].setSolution(value);
                checkSolutionUniqueOnColumnAndRow();
            }
        }
    }

    private void manageClearBlock() {
        if (getSolution(currentColumn,currentRow) == null) {
            for (int value = 0; value < numberOfBlocks; value++) {
                resetPossibility(currentColumn, currentRow, value);
                puzzleBlockView[currentColumn][currentRow].setPossibilities("");
            }
        } else {
            setSolution(currentColumn,currentRow, null);
            puzzleBlockView[currentColumn][currentRow].setSolution(null);
            checkSolutionUniqueOnColumnAndRow();
        }
    }

    private void manageFormulaDigits(@NotNull KeyCode keyCode) {
        int value = keyCode.getCode();
        if (value > 57) value -= 48;
        value -= 48;
        setFormulaNumber(currentColumn, currentRow, value);
        puzzleBlockView[currentColumn][currentRow].setFormula(getFormulaNumber(currentColumn,currentRow) + getFormulaOperator(currentColumn,currentRow));
        setFormulaBorders();
    }

    private void manageFormulaLetters(@NotNull KeyCode keyCode) {
        switch (keyCode) {
            case ADD:
                setFormulaOperator(currentColumn, currentRow, operators.ADD);
                break;
            case SUBTRACT:
                setFormulaOperator(currentColumn, currentRow, operators.SUBTRACT);
                break;
            case MULTIPLY:
                setFormulaOperator(currentColumn, currentRow, operators.MULTIPLY);
                break;
            case DIVIDE:
                setFormulaOperator(currentColumn, currentRow, operators.DIVIDE);
                break;
        }
        puzzleBlockView[currentColumn][currentRow].setFormula(getFormulaNumber(currentColumn,currentRow) + getFormulaOperator(currentColumn,currentRow));
    }

    private void manageFormulaClearBlock() {
        setFormulaNumber(currentColumn, currentRow, null);
        setFormulaOperator(currentColumn, currentRow, operators.NONE);
        if (getFormulaNumber(currentColumn,currentRow) != null)
            puzzleBlockView[currentColumn][currentRow].setFormula(getFormulaNumber(currentColumn,currentRow) + getFormulaOperator(currentColumn,currentRow));
        else
            puzzleBlockView[currentColumn][currentRow].setFormula("");
    }

    void manageMouseEvent(MouseEvent event) {
        BlockPosition mousePoint = getMousePosition(event);
        if (event.isShiftDown()) manageMouseFormulaSelect(mousePoint);
        else manageMouseMoveSelect(mousePoint);
    }

    private void manageMouseFormulaSelect(@NotNull BlockPosition mousePosition) {
        if (getFormulaNumber(currentColumn,currentRow) != null & getFormulaNumber(mousePosition.getColumn(),mousePosition.getRow()) == null) {
            if (currentBlockPosition.areNeighbors(mousePosition)) {
                setParent(mousePosition.getColumn(),mousePosition.getRow(), currentBlockPosition);
            } else {
                int column = mousePosition.getColumn();
                int row = mousePosition.getRow();
                if (column > 0) {
                    if (getParent(column - 1, row) == currentBlockPosition) setParent(column, row, currentBlockPosition);
                }
                if (column < numberOfBlocks) {
                    if(getParent(column + 1, row) == currentBlockPosition) setParent(column, row, currentBlockPosition);
                }
                if (row > 0){
                    if(getParent(column, row - 1) == currentBlockPosition) setParent(column, row, currentBlockPosition);
                }
                if (row < numberOfBlocks){
                    if(getParent(column, row + 1) == currentBlockPosition) setParent(column, row, currentBlockPosition);
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

    private BlockPosition getMousePosition(MouseEvent m) {
        int column;
        int row;
        for (column = 0; column < numberOfBlocks; column++)
            if (m.getX() < puzzleBlockView[column][0].getBlock().getLayoutX() + puzzleBlockView[column][0].getBlock().getWidth())
                break;
        for (row = 0; row < numberOfBlocks; row++) {
            if (m.getY() < puzzleBlockView[0][row].getBlock().getLayoutY() + puzzleBlockView[0][row].getBlock().getHeight())
                break;
        }
        if(row >= numberOfBlocks) row = numberOfBlocks - 1;
        if(column >= numberOfBlocks) column = numberOfBlocks - 1;
        return new BlockPosition(column, row);

    }

    private void checkSolutionUniqueOnColumnAndRow() {
        for (int column = 0; column < numberOfBlocks; column++) {
            for (int row = 0; row < numberOfBlocks; row++) {
                puzzleBlockView[column][row].SetSolutionError(false);
            }
        }
        for (int targetColumn = 0; targetColumn < numberOfBlocks; targetColumn++) {
            for (int targetRow = 0; targetRow < numberOfBlocks; targetRow++) {
                Integer targetValue = getSolution(targetColumn,targetRow);
                if (targetValue != null) {
                    for (int column = 0; column < targetColumn; column++) {
                        if (getSolution(column,targetRow) != null)
                            if (getSolution(column,targetRow).intValue() == targetValue)
                                puzzleBlockView[column][targetRow].SetSolutionError(true);
                    }
                    for (int column = targetColumn + 1; column < numberOfBlocks; column++) {
                        if (getSolution(column,targetRow) != null)
                            if (getSolution(column,targetRow).intValue() == targetValue)
                                puzzleBlockView[column][targetRow].SetSolutionError(true);
                    }
                    for (int row = 0; row < targetRow; row++) {
                        if (getSolution(targetColumn, row) != null)
                            if (getSolution(targetColumn, row).intValue() == targetValue)
                                puzzleBlockView[targetColumn][row].SetSolutionError(true);
                    }
                    for (int row = targetRow + 1; row < numberOfBlocks; row++) {
                        if (getSolution(targetColumn, row) != null)
                            if (getSolution(targetColumn, row).intValue() == targetValue)
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
        for (int column = 0; column < numberOfBlocks; column++) {
                puzzleBlockView[column][0].setBlockTopBorderColor(r);
            if (getFormulaParent(column,0)!= null){
                puzzleBlockView[column][0].setBlockTopBorderColor(c);
            }
            for (int row = 1; row < numberOfBlocks; row++) {
                puzzleBlockView[column][row].setBlockTopBorderColor(r);
                p1 = getFormulaParent(column, row );
                if (p1 != null){
                    p2 = getFormulaParent(column, row-1);
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
            for (int row = 0; row < numberOfBlocks-1; row++) {
                puzzleBlockView[column][row].setBlockBottomBorderColor(r);
                p1 = getFormulaParent(column, row );
                if (p1 != null){
                    p2 = getFormulaParent(column, row+1);
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
            puzzleBlockView[column][numberOfBlocks-1].setBlockTopBorderColor(r);
            if (getFormulaParent(column,numberOfBlocks-1)!= null){
                puzzleBlockView[column][numberOfBlocks-1].setBlockTopBorderColor(c);
            }
        }
        for (int row = 0; row < numberOfBlocks; row++) {
            puzzleBlockView[0][row].setBlockLeftBorderColor(r);
            if (getFormulaParent(0,row)!= null){
                puzzleBlockView[0][row].setBlockLeftBorderColor(c);
            }
            for (int column = 1; column < numberOfBlocks; column++) {
                puzzleBlockView[column][row].setBlockLeftBorderColor(r);
                p1 = getFormulaParent(column, row );
                if (p1 != null){
                    p2 = getFormulaParent(column-1, row);
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
            for (int column = 0; column < numberOfBlocks-1; column++) {
                puzzleBlockView[column][row].setBlockRightBorderColor(r);
                p1 = getFormulaParent(column, row );
                if (p1 != null){
                    p2 = getFormulaParent(column+1, row);
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
            puzzleBlockView[numberOfBlocks-1][row].setBlockRightBorderColor(r);
            if (getFormulaParent(numberOfBlocks-1,row)!= null){
                puzzleBlockView[numberOfBlocks-1][row].setBlockRightBorderColor(c);
            }
        }
    }
}